package com.example.bfn

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.bfn.util.ApiClient
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class ForgotPwdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pwd)

        val close_btn = findViewById<ImageView>(R.id.close_btn)
        close_btn.setOnClickListener{
            onBackPressed()
        }

        val email_et = findViewById<EditText>(R.id.email_et)
        val cnx = findViewById<Button>(R.id.connexion_btn)
        cnx.setOnClickListener{
            if(email_et.text.isNullOrBlank())
               {
                   email_et.error = getString(R.string.champ_vide)
                return@setOnClickListener
            }

            val codeReinit = (10000..99999).random()
            ApiClient.apiService.resetPass(
               email_et.text.toString(),
                codeReinit.toString()

            ).enqueue(
                object : Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                        t.printStackTrace()

                    }

                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            val content = response.body()

                            if (response.code() == 200) {

                                Toast.makeText(this@ForgotPwdActivity, content.get("message").asString, Toast.LENGTH_LONG).show()
                                val intent = Intent(this@ForgotPwdActivity, EmailSentActivity::class.java)
                                intent.putExtra("email", email_et.text.toString())
                                intent.putExtra("code", codeReinit.toString())
                                startActivity(intent)


                            }else if(response.code() == 404)
                            {
                                Toast.makeText(this@ForgotPwdActivity, content.get("message").asString, Toast.LENGTH_LONG).show()

                            }



                        }
                    }
                }
            )

        }

    }
}