package com.example.bfn

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.example.bfn.util.ApiClient
import com.example.bfn.util.UserSession
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_profile.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*


class ProfileActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)



        val backBtn = findViewById<ImageButton>(R.id.back_btn)
        backBtn.setOnClickListener {
            onBackPressed()

        }


        val Nom = findViewById<EditText>(R.id.firstName_text_profile)
        val Prenom = findViewById<EditText>(R.id.lastName_text_profile)
        val Email = findViewById<EditText>(R.id.email_text_profile)
        val UpdateProfileBTN = findViewById<Button>(R.id.updateProfile_btn)

        Nom.setText(UserSession.firstName)
        Prenom.setText(UserSession.lastName)
        Email.setText(UserSession.email)

        UpdateProfileBTN.setOnClickListener{
            if(Nom.text.toString() == UserSession.firstName && Prenom.text.toString() == UserSession.lastName && Email.text.toString() == UserSession.email)
            {
                Toast.makeText(this, getString(R.string.aucun_changement), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val params = HashMap<String?, String?>()
            params["id"] = UserSession.id
            params["firstName"] = Nom.text.toString()
            params["lastName"] = Prenom.text.toString()
            params["email"] = Email.text.toString()

            ApiClient.apiService.editProfile(params).enqueue(
                object : Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                        t.printStackTrace()

                    }
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful && response.body() != null) {

                            val content = response.body()

                            if(response.code() == 200)
                            {

                                if(UserSession.email != Email.text.toString())
                                {
                                    val sharedPref = getSharedPreferences(
                                        getString(R.string.user), Context.MODE_PRIVATE)

                                    with (sharedPref.edit()) {
                                        putString(getString(R.string.token), content.get("token").asString)
                                        commit()
                                    }
                                }

                                UserSession.firstName = Nom.text.toString()
                                UserSession.lastName = Prenom.text.toString()
                                UserSession.email = Email.text.toString()

                                finish()

                            }

                            Toast.makeText(this@ProfileActivity,content.get("message").asString,Toast.LENGTH_LONG).show()


                        }
                        else {
                            val content = response.body()
                            println(content)

                        }
                    }
                }
            )

        }



        val delete_account_btn = findViewById<Button>(R.id.delete_account_btn)
        delete_account_btn.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setMessage(getString(R.string.delete_account))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.yes)) { dialog, id ->

                    ApiClient.apiService.deleteAccount(UserSession.id).enqueue(
                        object : Callback<JsonObject> {
                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                                t.printStackTrace()

                            }
                            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                                if (response.isSuccessful && response.body() != null) {

                                    if(response.code() == 200)
                                    {
                                        //delete shared prefs
                                        //reset session
                                        //send to login screen
                                        val sharedPref = getSharedPreferences(
                                            getString(R.string.user), Context.MODE_PRIVATE)

                                        with (sharedPref.edit()) {
                                            clear()
                                            commit()
                                        }

                                        UserSession.reset()

                                        val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent)
                                        ActivityCompat.finishAffinity(this@ProfileActivity)


                                    }

                                }
                                else {
                                    val content = response.body()

                                    println(content)


                                }
                            }
                        }
                    )

                }
                .setPositiveButton(getString(R.string.no)) { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()

            val nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            nbutton.setTextColor(Color.RED)
            val pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            pbutton.setTextColor(Color.BLACK)

        }

    }
}