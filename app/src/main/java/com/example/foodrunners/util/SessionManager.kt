package com.example.foodrunners.util
import android.content.Context
import com.example.foodrunners.R

class SessionManager(context: Context) {

    var PRIVATE_MODE = 0
    val PREF_NAME = "pref_file_name"

    val KEY_IS_LOGGEDIN = "isLoggedIn"
    var pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    var editor = pref.edit()

    fun setLogin(isLoggedIn: Boolean){
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn)
        editor.apply()
    }


    fun isLoggedIn(): Boolean {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false)
    }

}