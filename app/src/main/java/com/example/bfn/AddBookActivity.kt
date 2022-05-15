package com.example.bfn//package com.example.bfn
//
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.graphics.BitmapFactory
//import android.graphics.Color
//import android.net.Uri
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.View
//import android.widget.*
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.core.net.toUri
//import com.google.android.material.textfield.TextInputEditText
//import com.google.gson.JsonObject
//import com.rhdev.mixhub.Model.Track
//import com.rhdev.mixhub.Util.ApiClient
//import com.rhdev.mixhub.Util.UploadImages.UploadRequestBodyTrack
//import com.rhdev.mixhub.Util.UploadImages.getFileName
//import okhttp3.MediaType
//import okhttp3.MultipartBody
//import okhttp3.RequestBody
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.io.*
//
//
//class AddBookActivity : AppCompatActivity() , AdapterView.OnItemSelectedListener{
//    private var mImageView: ImageView? = null
//    private var selectedImageUri: Uri? = null
//    private var selectedTrackUri: Uri? = null
//    var isPicked = false
//    var genre = ""
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_track)
//        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
//        window.statusBarColor = Color.TRANSPARENT
//        supportActionBar?.hide()
//        val pref = applicationContext.getSharedPreferences("UserPref", AppCompatActivity.MODE_PRIVATE)
//        val id = pref?.getString("id", null).toString()
//        val extras = intent.extras
//        if (extras != null) {
//            val value = extras.getString("uri_music")
//            if (value != null) {
//                selectedTrackUri = value.toUri()
//
//            }
//        }
//
//        val exitBtn = findViewById<ImageButton>(R.id.returnfrag)
//        val addImg = findViewById<ImageView>(R.id.imageAdd)
//        val validBtn = findViewById<ImageButton>(R.id.vald)
//        val titleText = findViewById<TextInputEditText>(R.id.title)
//        val descText = findViewById<TextInputEditText>(R.id.desc)
//        mImageView = findViewById(R.id.imageView)
//
//        val spinner: Spinner = findViewById(R.id.planets_spinner)
//// Create an ArrayAdapter using the string array and a default spinner layout
//
//        ArrayAdapter.createFromResource(
//            this,
//            R.array.planets_array,
//            android.R.layout.simple_spinner_item
//        ).also{ adapter->
//            // Specify the layout to use when the list of choices appears
//
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            // Apply the adapter to the spinner
//            spinner.adapter = adapter
//        }
//
//        spinner.onItemSelectedListener = this
//
//
//        addImg.setOnClickListener {
//
//            val checkSelfPermission = ContextCompat.checkSelfPermission(this,
//                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED){
//                //Requests permissions to be granted to this application at runtime
//                ActivityCompat.requestPermissions(this,
//                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
//            }
//            else{
//                isPicked = true
//                openGallery()
//            }
//
//        }
//        exitBtn.setOnClickListener {
//            finish()
//        }
//        validBtn.setOnClickListener {
//            if(!isPicked || titleText.text.isNullOrEmpty() || descText.text.isNullOrEmpty() || genre.isNullOrEmpty() )
//            {
//                Toast.makeText(this, "Veuillez remplir Tout les champs et choisissez une photo !", Toast.LENGTH_LONG).show()
//            }
//            else
//            {
//                uploadTrack(titleText.text.toString(),id,descText.text.toString(),genre)
//            }
//
//        }
//
//    }
//
//    private fun openGallery(){
//        val intent = Intent("android.intent.action.GET_CONTENT")
//        intent.type = "image/*"
//        startActivityForResult(intent, 1)
//    }
//    override fun onActivityResult(reqCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(reqCode, resultCode, data)
//        if (resultCode == RESULT_OK) {
//            try {
//                selectedImageUri = data?.data
//
//                val imageStream: InputStream? = selectedImageUri?.let { contentResolver.openInputStream(it) }
//                val selectedImage = BitmapFactory.decodeStream(imageStream)
//
//                mImageView?.setImageBitmap(selectedImage)
//            } catch (e: FileNotFoundException) {
//                e.printStackTrace()
//                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
//            }
//        } else {
//            isPicked = false
//        }
//    }
//
//    private fun uploadTrack(title:String , userid:String,desc:String,genre:String) {
//
//
//        val parcelFileDescriptor =
//            contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return
//
//        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
//        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
//        val outputStream = FileOutputStream(file)
//        inputStream.copyTo(outputStream)
//
//        val body = UploadRequestBodyTrack(file, "image", this)
/////////////////////////////////////////
//        val parcelFileDescriptorfile=
//            contentResolver.openFileDescriptor(selectedTrackUri!!, "r", null) ?: return
//
//        val inputStreamfile = FileInputStream(parcelFileDescriptorfile.fileDescriptor)
//        val filemusic = File(cacheDir, contentResolver.getFileName(selectedTrackUri!!))
//        val outputStreamfile = FileOutputStream(filemusic)
//        inputStreamfile.copyTo(outputStreamfile)
//
//        val bodymusic = UploadRequestBodyTrack(filemusic, "file", this)
//
//
//        ApiClient.apiService.uploadImageTrack(MultipartBody.Part.createFormData("image", file.name, body),
//            RequestBody.create(MediaType.parse("multipart/form-data"), "json")
//        ).enqueue(object : Callback<JsonObject> {
//            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
//
//            }
//
//            override fun onResponse(
//                call: Call<JsonObject>,
//                response: Response<JsonObject>
//            ) {
//                if (response.isSuccessful && response.body() != null) {
//                    val content = response.body()
//                    val imgname = content.get("message")
//
//
//                    ApiClient.apiService.uploadFileTrack(MultipartBody.Part.createFormData("file", filemusic.name, bodymusic),
//                        RequestBody.create(MediaType.parse("multipart/form-data"), "json")
//                    ).enqueue(object : Callback<JsonObject> {
//                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
//
//                        }
//
//                        override fun onResponse(
//                            call: Call<JsonObject>,
//                            response: Response<JsonObject>
//                        ) {
//                            if (response.isSuccessful && response.body() != null) {
//                                val contentfile = response.body()
//                                val ispodcast = contentfile.get("isPodcast")
//                                val namefile = contentfile.get("message")
//
//                                ApiClient.apiService.createTrack(Track(title = title, userid = userid, image = imgname.asString
//                                , description = desc, category = genre , file = namefile.asString , isPodcast = ispodcast.asString
//
//                                )).enqueue(object : Callback<JsonObject> {
//                                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
//
//                                    }
//
//                                    override fun onResponse(
//                                        call: Call<JsonObject>,
//                                        response: Response<JsonObject>
//                                    ) {
//                                        if (response.isSuccessful && response.body() != null) {
//                                            Toast.makeText(
//                                                this@AddBookActivity,
//                                                "Track Ajouté",
//                                                Toast.LENGTH_LONG
//                                            ).show()
//                                            finish()
//                                        }
//                                        else
//                                        {
//                                            Toast.makeText(
//                                                this@AddBookActivity,
//                                                "Erreur de serveur veuillez réessayer plus tard",
//                                                Toast.LENGTH_LONG
//                                            ).show()
//                                        }
//                                    }
//                                })
//
//
//
//                            }else
//                            {
//                                Toast.makeText(
//                                    this@AddBookActivity,
//                                    "Erreur de serveur veuillez réessayer plus tard",
//                                    Toast.LENGTH_LONG
//                                ).show()
//                            }
//
//
//                        }
//                    })
//                }else
//                {
//                    Toast.makeText(
//                        this@AddBookActivity,
//                        "Erreur de serveur veuillez réessayer plus tard",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//
//
//            }
//        })
//
//    }
//
//    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
//        // An item was selected. You can retrieve the selected item using
//        val selectedTex :TextView = parent.getChildAt(0) as TextView
//        selectedTex.setTextColor(Color.WHITE)
//         if(parent.getItemAtPosition(pos).toString() != "Choisir un genre...")
//         {
//             genre = parent.getItemAtPosition(pos).toString()
//         }
//    }
//
//    override fun onNothingSelected(parent: AdapterView<*>) {
//        // Another interface callback
//    }
//}