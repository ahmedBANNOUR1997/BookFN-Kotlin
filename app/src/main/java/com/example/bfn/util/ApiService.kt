package com.example.bfn.util

import com.example.bfn.models.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import java.util.*

interface ApiService {

    @POST("login")
    fun login(@Body post: User): Call<LoginResponse>

    @POST("users")
    fun register(@Body post: User): Call<JsonObject>

    @GET("allBooks")
    fun getBooksHome(): Call<JsonObject>

    @FormUrlEncoded
    @PATCH("updateuser")
    fun editProfile(@FieldMap params: HashMap<String?, String?>): Call<JsonObject>

    @Multipart
    @PUT("edit-profile-picture")
    fun editProfilePicture(
        @Part img: MultipartBody.Part,
        @Part("email") email: String
    ): Call<JsonObject>

    @POST("getuserbyid")
    fun getUserById(@Body user_id: UserX): Call<GetUserResponse>

    @DELETE("one/{user_id}")
    fun deleteAccount(@Path("user_id") user_id: String): Call<JsonObject>

    @GET("allBooks")
    fun getAllBooks(): Call<BooksResponse>

    @POST("showbook")
    fun showBook(@Body bookid: BookId): Call<BookResponse>


    @GET
    fun getPdf(@Url pdfName: String): Call<ResponseBody>


}