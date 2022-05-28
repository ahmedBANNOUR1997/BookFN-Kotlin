package com.example.bfn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.bfn.util.ApiClient
import com.example.bfn.util.ApiService
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmailSentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_sent)
        
        val close_btn = findViewById<ImageView>(R.id.close_btn)
        close_btn.setOnClickListener{
            onBackPressed()
        }

        val email = intent.getStringExtra("email")
        var code = intent.getStringExtra("code")

        println(code)


        val desc = "We have sent an email to $email for verification. Please verify your email to obtain your code."

        val emailDesc = findViewById<TextView>(R.id.email_envoye_desc)
        emailDesc.text = desc

        val code_et = findViewById<EditText>(R.id.code_et)
        val confirm_btn = findViewById<Button>(R.id.confirm_btn)
        confirm_btn.setOnClickListener{
            if(code_et.text.isNullOrBlank())
            {
                code_et.error = getString(R.string.champ_vide)
                return@setOnClickListener
            }
            if(code_et.text.length<5)
            {
                code_et.error = getString(R.string.longeur_code)
                return@setOnClickListener

            }

            if (code_et.text.toString() != code)
            {
                code_et.error = getString(R.string.code_incorrect)
                return@setOnClickListener

            }

            val intent = Intent(this@EmailSentActivity, ResetPasswordActivity::class.java)
            intent.putExtra("email", email)
            startActivity(intent)

        }

        val renvoyer_tv = findViewById<TextView>(R.id.renvoyer_tv)
        renvoyer_tv.setOnClickListener{
            code = (10000..99999).random().toString()
            ApiClient.apiService.resetPass(
                email!!,
                code!!

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


                                Toast.makeText(this@EmailSentActivity, content.get("message").asString, Toast.LENGTH_LONG).show()

                            }



                        }
                    }
                }
            )

        }
    }
}