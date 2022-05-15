package com.example.bfn.models

data class Response(
    val __v: Int,
    val _id: String,
    val author: String,
    val category: String,
    val coverImage: String,
    val createdAt: String,
    val description: String,
    val fileAudio: String,
    val filePDF: String,
    val isPodcast: String,
    val like: List<Any>,
    val nbPages: Int,
    val nbVue: Int,
    val price: Int,
    val title: String,
    val updatedAt: String,
    val userid: String
)