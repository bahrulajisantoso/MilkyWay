package com.capstone.milkyway

import android.content.Context

class UserPreference(context: Context) {

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(userId: String, idToken: String, email: String) {
        val editor = preferences.edit()
        editor.putString(USER_ID, userId)
        editor.putString(ID_TOKEN, idToken)
        editor.putString(EMAIL, email)
        editor.apply()
    }

    fun getUserId(): String {
        return preferences.getString(USER_ID, "") ?: ""
    }

    fun getIdToken(): String {
        return preferences.getString(ID_TOKEN, "") ?: ""
    }

    fun getEmail(): String {
        return preferences.getString(EMAIL, "") ?: ""
    }

    fun logOut() {
        val editor = preferences.edit()
        editor.clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val USER_ID = "user_id"
        private const val ID_TOKEN = "token"
        private const val EMAIL = "email"
    }
}