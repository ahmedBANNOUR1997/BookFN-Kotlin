package com.example.bfn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.bfn.util.ApiClient
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        val backBtn = findViewById<ImageButton>(R.id.back_btn)
        backBtn.setOnClickListener {
            onBackPressed()

        }
        val email = intent.getStringExtra("email")

        val new_mdp_et = findViewById<EditText>(R.id.new_mdp_et)
        val confirm_mdp_et = findViewById<EditText>(R.id.confirm_mdp_et)

        val change_pass_btn = findViewById<Button>(R.id.change_pass_btn)

        change_pass_btn.setOnClickListener{

            if(new_mdp_et.text.isNullOrBlank())
            {
                new_mdp_et.error = getString(R.string.champ_vide)
                return@setOnClickListener

            }
            if(confirm_mdp_et.text.isNullOrBlank())
            {
                confirm_mdp_et.error = getString(R.string.champ_vide)
                return@setOnClickListener

            }

            if(new_mdp_et.text.toString() != confirm_mdp_et.text.toString() )
            {
                Toast.makeText(this,getString(R.string.confirm_mdp_incorrect), Toast.LENGTH_LONG).show()
                return@setOnClickListener

            }

            ApiClient.apiService.doResetPass(email!!, new_mdp_et.text.toString()).enqueue(
                object : Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                        t.printStackTrace()

                    }
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful && response.body() != null) {
                            val content = response.body()

                            if(response.code() == 201)
                            {

                                val intent = Intent(this@ResetPasswordActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()

                            }

                            Toast.makeText(this@ResetPasswordActivity,content.get("message").asString, Toast.LENGTH_LONG).show()

                        }
                        else {
                            val content = response.body()

                            println(content)


                        }




                    }
                }
            )

        }

    }
}