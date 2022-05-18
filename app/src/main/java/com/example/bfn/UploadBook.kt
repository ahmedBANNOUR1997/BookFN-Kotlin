package com.example.bfn

import android.content.ContentResolver
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.bfn.prefs.PrefsManager
import com.example.bfn.util.ApiClient
import com.example.bfn.util.ApiClient.createPartFromString
import com.example.bfn.util.ApiClient.prepareFilePart
import kotlinx.android.synthetic.main.activity_upload_book.*
import okhttp3.RequestBody
import java.io.File


class UploadBook : AppCompatActivity() {
    private val apiService = ApiClient.apiService
    private var imageUri: Uri? = null
    private var audioUri: Uri? = null
    private var pdfUri: Uri? = null
    private var fileUri: Uri? = null


    private val getFileUri =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->


            if (result != null) {

                val mime = MimeTypeMap.getSingleton()
                val type = mime.getExtensionFromMimeType(this.contentResolver.getType(result))

                when (type) {
                    "jpg" -> {
                        fileUri = result
                        imBook.setImageURI(result)
                        fileUri?.let { fileUri ->
                            btnUpload.isEnabled = true

                        }
                    }
                    "pdf" -> {
                        fileUri = result
                        ic_yes.playAnimation()
                        fileUri?.let { fileUri ->
                            btnUpload.isEnabled = true

                        }
                    }

                    "mp3" -> {
                        fileUri = result
                        ic_yes2.playAnimation()
                        fileUri?.let { fileUri ->
                            btnUpload.isEnabled = true

                        }
                    }

                }


            } else {
                Log.d("CreateAdsFragment", "Uri  is null")
                btnUpload.isEnabled = false

            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_book)
        btnUpload.isEnabled = false
    }

    override fun onStart() {
        super.onStart()

        imBook.setOnClickListener {

            getFileUri.launch("image/*")
        }
        imPdf.setOnClickListener {
            getFileUri.launch("application/pdf")

        }

        imAudio.setOnClickListener {
            getFileUri.launch("audio/*")
        }

    }

    private fun upload() {
        btnUpload.setOnClickListener {
            val token = PrefsManager.geToken(this)
            token?.let {
                val map: HashMap<String, RequestBody> = HashMap()
                map["title"] = createPartFromString(edTitle.text.toString())
                map["author"] = createPartFromString(edDesc.text.toString())
                map["price"] = createPartFromString(edPrice.text.toString())
                map["category"] = createPartFromString(edCategory.text.toString())
                map["nbPages"] = createPartFromString(edNumPages.text.toString())
                map["description"] = createPartFromString(edDesc.text.toString())
                map["userid"] = createPartFromString(it)
                apiService.uploadBook(
                    map,
                    prepareFilePart("coverImage", File(imageUri!!.path!!)),
                    prepareFilePart("filePDF", File(pdfUri!!.path!!)),
                    prepareFilePart("fileAudio", File(audioUri!!.path!!))
                )


            }
        }
    }
}