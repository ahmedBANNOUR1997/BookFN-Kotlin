package com.example.bfn.util

import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

object ApiClient {

    private const val BASE_URL: String = "http://10.0.2.2:3000/api/"

    private val gson =  GsonBuilder().setLenient().create()


    private val httpClient = OkHttpClient.Builder().build()

    private val retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()


    val apiService = retrofit.create(ApiService::class.java)

    fun createPartFromString(descriptionString: String): RequestBody {
        return RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString)
    }

    fun prepareFilePart(partName: String, file: File): MultipartBody.Part {

        // create RequestBody instance from file
        val requestFile: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }
}