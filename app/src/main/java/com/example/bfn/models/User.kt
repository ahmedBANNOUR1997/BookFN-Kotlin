package com.example.bfn.models

import com.google.gson.annotations.SerializedName

data class User (



    @SerializedName("id") val id: String?="",
    @SerializedName("firstName") val firstName: String?="",
    @SerializedName("lastName") val lastName: String?="",
    @SerializedName("email") val email: String?="",
    @SerializedName("pwd") val pwd: String?="",
    @SerializedName("emailCode") val emailCode: String?="",
    @SerializedName("emailEtat") val emailEtat: String?="",
    @SerializedName("token") val token: String?="",
    @SerializedName("img") val img: String?="",
    @SerializedName("tel") val tel: String?=""



) {
    override fun toString(): String {
        return "User(id=$id, firstName=$firstName, lastName=$lastName, email=$email, pwd=$pwd, emailCode=$emailCode, emailEtat=$emailEtat, token=$token, img=$img, tel=$tel)"
    }
}