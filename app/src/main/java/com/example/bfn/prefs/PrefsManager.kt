package com.example.bfn.prefs

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class PrefsManager {

    companion object {
        const val TOKEN = "token"
        const val PDF = "pdf_url"
        const val BOOKID = "bookid"

        fun getPreferences(context: Context): SharedPreferences {
            return PreferenceManager.getDefaultSharedPreferences(context)
        }

        fun seToken(context: Context, token: String?) {
            val editor = getPreferences(context).edit()
            editor.putString(TOKEN, token)
            editor.apply()
        }

        fun geToken(context: Context): String? {
            return getPreferences(context).getString(TOKEN, null)
        }

        fun seBookid(context: Context, bookid: String?) {
            val editor = getPreferences(context).edit()
            editor.putString(BOOKID, bookid)
            editor.apply()
        }

        fun geBookid(context: Context): String? {
            return getPreferences(context).getString(BOOKID, null)
        }


        fun sePdf(context: Context, token: String?) {
            val editor = getPreferences(context).edit()
            editor.putString(PDF, token)
            editor.apply()
        }

        fun gePdf(context: Context): String? {
            return getPreferences(context).getString(PDF, null)

        }
    }
}