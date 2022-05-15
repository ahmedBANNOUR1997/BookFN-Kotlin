package com.example.bfn.util.UploadImages

data class UploadResponse(
    val error: Boolean,
    val message: String,
    val image: String
)