package com.example.bfn.models

import com.google.gson.annotations.SerializedName

data class Book (
    @SerializedName("_id") val id: String?="",
    @SerializedName("title") val title: String?="",
    @SerializedName("author") val author: String?="",
    @SerializedName("price") val price: String?="",
    @SerializedName("description") val description: String?="",
    @SerializedName("coverImage") val coverImage: String?="",
    @SerializedName("category") val category: String?="",
    @SerializedName("nbPages") val nbPages: String?="",
    @SerializedName("filePDF") val filePDF: String?="",
    @SerializedName("fileAudio") val fileAudio: String?="",
    @SerializedName("like") val like: Array<String?> = emptyArray(),
    @SerializedName("nbVue") val nbVue: String?="",
    @SerializedName("userid") val userid: String?=""
 ) {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Book

        if (id != other.id) return false
        if (title != other.title) return false
        if (author != other.author) return false
        if (price != other.price) return false
        if (description != other.description) return false
        if (coverImage != other.coverImage) return false
        if (category != other.category) return false
        if (nbPages != other.nbPages) return false
        if (filePDF != other.filePDF) return false
        if (fileAudio != other.fileAudio) return false
        if (!like.contentEquals(other.like)) return false
        if (nbVue != other.nbVue) return false
        if (userid != other.userid) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (author?.hashCode() ?: 0)
        result = 31 * result + (price?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (coverImage?.hashCode() ?: 0)
        result = 31 * result + (category?.hashCode() ?: 0)
        result = 31 * result + (nbPages?.hashCode() ?: 0)
        result = 31 * result + (filePDF?.hashCode() ?: 0)
        result = 31 * result + (fileAudio?.hashCode() ?: 0)
        result = 31 * result + like.contentHashCode()
        result = 31 * result + (nbVue?.hashCode() ?: 0)
        result = 31 * result + (userid?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Books(id=$id, title=$title, author=$author, price=$price, description=$description, coverImage=$coverImage, category=$category, nbPages=$nbPages, filePDF=$filePDF, fileAudio=$fileAudio, like=${like.contentToString()}, nbVue=$nbVue, userid=$userid)"
    }


}
