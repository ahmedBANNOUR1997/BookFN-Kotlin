package com.example.bfn.util

object UserSession {

    init {
        println("UserSession invoked.")
    }
    var id = ""
    var firstName = ""
    var lastName =""
    var email =""
    var token = ""
    var img = "default.png"

    fun fullName ():String{
        return "$firstName $lastName"
    }

    fun reset()
    {
        var id = ""
        var firstName = ""
        var lastName =""
        var email =""
        var token = ""
        var img = "default.png"

    }
}