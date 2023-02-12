package com.imdvlpr.chatapp.Shared.Extension

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {

    private var sharedPreference: SharedPreferences = context.getSharedPreferences(Constants.KEY_USERS.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE)

    @SuppressLint("CommitPrefEdits")
    fun putBoolean(key: String, value: Boolean) {
        val editor: SharedPreferences.Editor = sharedPreference.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String): Boolean {
        return sharedPreference.getBoolean(key, false)
    }

    @SuppressLint("CommitPrefEdits")
    fun putString(key: String, value: String) {
        val editor: SharedPreferences.Editor = sharedPreference.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String): String? {
        return sharedPreference.getString(key, null)
    }

    @SuppressLint("CommitPrefEdits")
    fun clear() {
        val editor: SharedPreferences.Editor = sharedPreference.edit()
        editor.clear()
        editor.apply()
    }
}