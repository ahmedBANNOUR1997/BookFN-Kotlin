package com.example.bfn.util

import com.example.bfn.models.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
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


    @FormUrlEncoded
    @POST("user/get-by-token")
    fun getUserByToken(@FieldMap params: HashMap<String?, String?>): Call<JsonObject>


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

    @POST("lastRecentlyRead")
    fun showLastRecentlyReadBook(@Body user_id: UserY): Call<lastBook?>

    @POST("listRecentlyRead")
    fun showAllRecentlyReadBooks(@Body user_id: UserY): Call<RecentlyReadBooks?>

    @POST("addLikesBook")
    fun addBookToRecentlyRead(@Body request: AddBookRecentlyRead): Call<RecentlyBookAdded>


    @GET
    fun getPdf(@Url pdfName: String): Call<ResponseBody>

    @Multipart
    @JvmSuppressWildcards
    @POST("addbook")
    fun uploadBook(
        @PartMap partMap: Map<String, RequestBody>,
        @Part coverImage: MultipartBody.Part,
        @Part filePDF: MultipartBody.Part
    ):Call<JSONObject>

    @FormUrlEncoded
    @POST("change-password")
    fun changerMdp(@FieldMap params: HashMap<String?, String?>): Call<JsonObject>

    @FormUrlEncoded
    @POST("forgot-password")
    fun resetPass(@Field("email") email : String, @Field("codeDeReinit") codeDeReinit : String): Call<JsonObject>

    @FormUrlEncoded
    @POST("reset-password")
    fun doResetPass(@Field("email") email : String, @Field("newPassword") newPassword : String): Call<JsonObject>
}