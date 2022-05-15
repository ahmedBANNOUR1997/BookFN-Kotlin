package com.example.bfn.models

import com.google.gson.annotations.SerializedName

data class LastBookX(
    val __v: Int,
    val _id: String,
    val author: String? = null,
    val category: String,
    val coverImage: String? = null,
    val createdAt: String,
    val description: String,
    val fileAudio: String,
    val filePDF: String,
    val isPodcast: String,
    val like: List<Any>,
    val nbPages: Int,
    val nbVue: Int,
    val price: Int,
    val title: String? = null,
    val updatedAt: String,
    val userid: String
)