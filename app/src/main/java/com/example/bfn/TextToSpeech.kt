package com.example.bfn

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.example.bfn.databinding.ActivityTexttospeechBinding
import com.example.bfn.util.ApiClient

import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*

const val TAG = "TextToSpeechActivity"

class TextToSpeech : AppCompatActivity(), TextToSpeech.OnInitListener {

    lateinit var binding: ActivityTexttospeechBinding
    lateinit var tts: TextToSpeech
    lateinit var reader: PdfReader

    private var fileUri: Uri? = null
    private var pdfUrl: String? = null
    private val apiService = ApiClient.apiService
    private lateinit var pdfFileName: File
    private lateinit var dirPath: String
    private lateinit var fileName: String


    private var pdfSPEECHURL: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTexttospeechBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pdfSPEECHURL = intent.getStringExtra(SPEECH_PDF) ?: return finish()
        initFile()
        downloadPdf()
        binding.fabPlayTts.isEnabled = false
        hideControls()

        tts = TextToSpeech(this, this)

        tts.setOnUtteranceProgressListener(
            object : UtteranceProgressListener() {
                override fun onStart(p0: String?) {
                    binding.fabPlayTts.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this@TextToSpeech,
                            R.drawable.ic_baseline_stop_24
                        )
                    )
                }

                override fun onStop(utteranceId: String?, interrupted: Boolean) {
                    super.onStop(utteranceId, interrupted)
                    binding.fabPlayTts.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this@TextToSpeech,
                            R.drawable.ic_baseline_play_arrow_24
                        )
                    )
                }

                override fun onDone(p0: String?) {
                    binding.fabPlayTts.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this@TextToSpeech,
                            R.drawable.ic_baseline_play_arrow_24
                        )
                    )

                    nextPageSpeak()
                    //speak(binding.tvPageContent.text.toString())
                }

                override fun onError(p0: String?) {
                    Toast.makeText(
                        this@TextToSpeech, "Error : $p0", Toast.LENGTH_LONG
                    ).show()
                }
            }
        )

        val selectPdfResult = registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (loadPdf(it)) {
                setPageContent(1)
            }
        }

        /*binding.fabSelectFile.setOnClickListener {
            // pickFile(selectPdfResult)
        }*/

        binding.fabNextPage.setOnClickListener {
            nextPage()
        }

        binding.fabPreviousPage.setOnClickListener {
            if (::reader.isInitialized) {
                var currentPgNo = Integer.parseInt(binding.tvCurrentPgNo.text.toString())
                if (1 < currentPgNo) {
                    currentPgNo--
                    setPageContent(currentPgNo)
                    binding.tvCurrentPgNo.text = currentPgNo.toString()

                    if (tts.isSpeaking) {
                        tts.stop()
                        speak(binding.tvPageContent.text.toString())
                    }
                }
            }
        }

        binding.fabPlayTts.setOnClickListener {

            if (::reader.isInitialized) {
                if (tts.isSpeaking) {
                    tts.stop()
                    binding.fabPlayTts.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this@TextToSpeech,
                            R.drawable.ic_baseline_play_arrow_24
                        )
                    )

                } else {
                    speak(binding.tvPageContent.text.toString())

                }
            }
        }

      /*  binding.fabAddBig.setOnClickListener {
            pickFile(selectPdfResult)
        }
        */
    }

    //Changes to Next Page
    private fun nextPageSpeak() {
        if (::reader.isInitialized) {
            var currentPgNo = Integer.parseInt(binding.tvCurrentPgNo.text.toString())
            if (currentPgNo < reader.numberOfPages) {
                currentPgNo++
                setPageContent(currentPgNo)
                binding.tvCurrentPgNo.text = currentPgNo.toString()

                if (tts.isSpeaking) {
                    tts.stop()
                }
                speak(binding.tvPageContent.text.toString())
            }
        }
    }

    //Changes to Next Page
    private fun nextPage() {
        if (::reader.isInitialized) {
            var currentPgNo = Integer.parseInt(binding.tvCurrentPgNo.text.toString())
            if (currentPgNo < reader.numberOfPages) {
                currentPgNo++
                setPageContent(currentPgNo)
                binding.tvCurrentPgNo.text = currentPgNo.toString()

                if (tts.isSpeaking) {
                    tts.stop()
                    speak(binding.tvPageContent.text.toString())
                }
            }
        }
    }

    //Opens File Picker
    private fun pickFile(selectPdfResult: ActivityResultLauncher<String>) {
        if (tts.isSpeaking) {
            tts.stop()
        }

        try {
            selectPdfResult.launch("application/pdf")
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No File Picker Found", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "pickFile: ${e.message}")
        }
    }

    private fun downloadPdf() {
        pdfSPEECHURL?.let {
            apiService.getPdf(it).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>?,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()?.byteStream()
                        result?.let {
                            writeToFile(it)


                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                }

            })
        }
    }

    //Sets the reader
    private fun loadPdf(uri: Uri): Boolean {

        try {
            reader = PdfReader(contentResolver.openInputStream(uri))

            if (reader.numberOfPages == 0) {
                Toast.makeText(this, "Empty Pdf", Toast.LENGTH_SHORT).show()
                return false
            }

            showControls()

            binding.tvTotalPages.text = reader.numberOfPages.toString()
            binding.tvPgNoSeperator.text = "/"
            binding.tvCurrentPgNo.text = "1"
            return true

        } catch (e: Exception) {
            Toast.makeText(this, "Error While Reading PDF", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "loadPdf: ${e.message}")
            return false
        }

    }

    //Sets Text of Page to the TextView
    private fun setPageContent(pageNo: Int) {

        if (pageNo <= reader.numberOfPages) {
            binding.tvPageContent.text = "Page $pageNo \n\n" + PdfTextExtractor.getTextFromPage(
                reader,
                pageNo
            ).trim()
        }
    }

    //For TextToSpeech Init
    override fun onInit(p0: Int) {
        binding.fabPlayTts.isEnabled = true
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.shutdown()
    }

    //Speaks the text with TextToSpeech
    private fun speak(text: String) =
        tts.speak(text.trim(), TextToSpeech.QUEUE_FLUSH, null, "PdfReader")

    private fun hideControls() {
        binding.fabPlayTts.isVisible = false
        binding.fabPreviousPage.isVisible = false
        binding.fabNextPage.isVisible = false
      //  binding.fabSelectFile.isVisible = false
        binding.tvCurrentPgNo.isVisible = false
        binding.tvPgNoSeperator.isVisible = false
        binding.tvTotalPages.isVisible = false

        binding.tvHeadTop.isVisible = false
        binding.ivHeadTop.isVisible = false
        binding.lineHeadTop.isVisible = false
    }

    private fun showControls() {

       // binding.fabAddBig.isVisible = false
      //  binding.tvHead1.isVisible = false
       // binding.tvHead2.isVisible = false
     //   binding.tvHead3.isVisible = false

        binding.fabPlayTts.isVisible = true
        binding.fabPreviousPage.isVisible = true
        binding.fabNextPage.isVisible = true
       // binding.fabSelectFile.isVisible = true
        binding.tvCurrentPgNo.isVisible = true
        binding.tvPgNoSeperator.isVisible = true
        binding.tvTotalPages.isVisible = true

        binding.tvHeadTop.isVisible = true
        binding.ivHeadTop.isVisible = true
        binding.lineHeadTop.isVisible = true
    }


    private fun initFile(){
        dirPath = "${filesDir}/bfn/pdffiles"
        val dirFile = File(dirPath)
        if(!dirFile.exists()) {
            dirFile.mkdirs()
        }
        fileName = "DemoGraphs.pdf"
        val file = "${dirPath}/${fileName}"
        pdfFileName = File(file)
        if(pdfFileName.exists()) {
            pdfFileName.delete()
        }
    }

    private fun writeToFile(inputStream: InputStream) {
        try {
            Log.e("====", "====writeToFile : ")
            val fileReader = ByteArray(4096)
            var fileSizeDownloaded = 0
            val fos: OutputStream = FileOutputStream(pdfFileName)
            do {
                val read = inputStream.read(fileReader)
                if (read != -1) {
                    fos.write(fileReader, 0, read)
                    fileSizeDownloaded += read
                }
            } while (read != -1)
            fos.flush()
            fos.close()
            loadPdf(pdfFileName.toUri())

        } catch (e: IOException) {
            Log.e("====", "====IOException : " + e)
        }
    }

    companion object {
        private const val SPEECH_PDF = "SPEECH_PDF"


        fun start(context: Context, pdf_url: String) {
            val intent = Intent(context, com.example.bfn.TextToSpeech::class.java)
            intent.putExtra(SPEECH_PDF, pdf_url)

            context.startActivity(intent)
        }
    }
}

