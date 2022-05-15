package com.example.bfn.models

data class Message(
    val __v: Int,
    val _id: String,
    val abonne: List<Any>,
    val abonnenement: List<Any>,
    val createdAt: String,
    val email: String,
    val emailCode: String,
    val emailEtat: String,
    val favBook: List<Any>,
    val favPlaylist: List<Any>,
    val firstName: String,
    val img: String,
    val isAdmin: Boolean,
    val lastName: String,
    val pwd: String,
    val tel: String,
    val token: String,
    val updatedAt: String
)