package com.example.bfn

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.bfn.databinding.ActivityBookPdfBinding
import com.example.bfn.util.ApiClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*

class BookPdfActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookPdfBinding
    private var fileUri: Uri? = null
    private var pdfUrl: String? = null
    private val apiService = ApiClient.apiService
    private lateinit var pdfFileName: File
    private lateinit var dirPath: String
    private lateinit var fileName: String

    private val geturiFromFile =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == Activity.RESULT_OK) {
                fileUri = result.data?.data
                binding.pdfView.fromUri(fileUri)
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                    .password(null)
                    .scrollHandle(null)
                    .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                    // spacing between pages in dp. To define spacing color, set view background
                    .spacing(0)
                    .load()

            } else Log.d("kou", "Uri from file is null")
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookPdfBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupUi()
    }


    private fun setupUi() {


        pdfUrl = intent.getStringExtra(BOOK_PDF) ?: return finish()
        initFile()
        binding.imBack.setOnClickListener {
            finish()
        }
        pdfUrl?.let {
            apiService.getPdf(it).enqueue(object : Callback<ResponseBody>{
                override fun onResponse(
                    call: Call<ResponseBody>?,
                    response: Response<ResponseBody>
                ) {
                    if(response.isSuccessful) {
                        val result = response.body()?.byteStream()
                        result?.let {
                            writeToFile(it)
                            /* val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                 addCategory(Intent.CATEGORY_OPENABLE)
                                 type = "application/pdf"
                             }
                             geturiFromFile.launch(intent)*/


                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                }

            })
        }





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
            Log.e("====", "====writeToFile : " )
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

            binding.pdfView.fromUri(pdfFileName.toUri())
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                .load()
        }catch ( e: IOException) {
            Log.e("====", "====IOException : "+e )
        }
    }
    companion object {
        private const val BOOK_PDF = "BOOK_PDF"


        fun start(context: Context, pdf_url: String) {
            val intent = Intent(context, BookPdfActivity::class.java)
            intent.putExtra(BOOK_PDF, pdf_url)

            context.startActivity(intent)
        }
    }
}