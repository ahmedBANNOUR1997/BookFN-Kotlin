package com.example.bfn

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.bfn.util.ApiClient
import com.example.bfn.util.UserSession
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        //Biding BTN et TXT
        val main_login_btn = findViewById<Button>(R.id.login_btn)
        //Redirections
        main_login_btn.setOnClickListener{

            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }


        var sharedPref = getSharedPreferences(
            getString(R.string.user), Context.MODE_PRIVATE)

        val token = sharedPref.getString(getString(R.string.token),null)

        println(token)

        if(token != null)
        {
            val params = HashMap<String?, String?>()
            params["token"] = token

            /*ApiClient.apiService.getUserByToken(params).enqueue(
                object : Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                        t.printStackTrace()

                    }
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful && response.body() != null) {
                            val content = response.body()

                            if(response.code() == 200)
                            {

                                val user = content.getAsJsonObject("dataid")

                                println(user)
                                UserSession.id =
                                    user.get("_id").asString

                                UserSession.firstName =
                                    user.get("firstName").asString

                                UserSession.lastName =
                                    user.get("lastName").asString

                                UserSession.email =
                                    user.get("email").asString

                                UserSession.token =
                                    user.get("token").asString

                                val profilePicture = user.get("img").asString

                                if(!profilePicture.isEmpty())
                                    UserSession.img = profilePicture

                                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                                startActivity(intent)
                                finish()



                            }else{
                                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()

                            }


                        }
                        else {
                            val content = response.body()

                            println(content)

                            val intent = Intent(this@MainActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()


                        }




                    }
                }
            )*/
        }
        else{
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }




    }
}