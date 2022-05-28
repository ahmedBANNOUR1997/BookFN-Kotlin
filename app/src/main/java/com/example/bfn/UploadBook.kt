package com.example.bfn

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.bfn.prefs.PrefsManager
import com.example.bfn.util.ApiClient
import com.example.bfn.util.ApiClient.createPartFromString
import com.example.bfn.util.ApiClient.prepareFilePart
import com.example.bfn.util.RealPathUtil
import kotlinx.android.synthetic.main.activity_upload_book.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class UploadBook : AppCompatActivity() {
    private val apiService = ApiClient.apiService
    private var fileUri: Uri? = null

    private lateinit var coverImage :MultipartBody.Part
    private lateinit var filePDF :MultipartBody.Part


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
                            coverImage= prepareFilePart("coverImage", File(RealPathUtil.getRealPath(this, fileUri)!!))


                        }
                    }
                    "pdf" -> {
                        fileUri = result
                        ic_yes.playAnimation()
                        fileUri?.let { fileUri ->
                            btnUpload.isEnabled = true
                            filePDF  = prepareFilePart("filePDF",File(RealPathUtil.getRealPath(this, fileUri)!!))
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

        imBook.setOnClickListener {

            getFileUri.launch("image/*")
        }
        imPdf.setOnClickListener {
            getFileUri.launch("application/pdf")

        }

        upload()

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




                apiService.uploadBook(map,coverImage,filePDF).enqueue(object:Callback<JSONObject>{
                    override fun onResponse(call: Call<JSONObject>?, response: Response<JSONObject>) {
                        if (response.isSuccessful){
                            Toast.makeText(
                                this@UploadBook,
                                "Book Added Successfully !!",
                                Toast.LENGTH_LONG
                            ).show()
                           onBackPressed()
                        }
                    }

                    override fun onFailure(call: Call<JSONObject>?, t: Throwable?) {
                        Toast.makeText(
                            this@UploadBook,
                            "Error Occurred: ${t?.message.toString()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                })


            }        }

    }
}