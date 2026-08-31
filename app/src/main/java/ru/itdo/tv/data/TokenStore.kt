package ru.itdo.tv.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Хранилище токенов после QR-входа. Простые SharedPreferences —
 * для первой версии этого достаточно (можно перейти на
 * EncryptedSharedPreferences позже, если понадобится).
 */
class TokenStore(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("itdo_tv_auth", Context.MODE_PRIVATE)

    var accessToken: String?
        get() = prefs.getString("access_token", null)
        set(value) = prefs.edit().putString("access_token", value).apply()

    var refreshToken: String?
        get() = prefs.getString("refresh_token", null)
        set(value) = prefs.edit().putString("refresh_token", value).apply()

    var username: String?
        get() = prefs.getString("username", null)
        set(value) = prefs.edit().putString("username", value).apply()

    var displayName: String?
        get() = prefs.getString("display_name", null)
        set(value) = prefs.edit().putString("display_name", value).apply()

    var avatar: String?
        get() = prefs.getString("avatar", null)
        set(value) = prefs.edit().putString("avatar", value).apply()

    val isLoggedIn: Boolean get() = !accessToken.isNullOrBlank()

    fun saveSession(accessToken: String, refreshToken: String, username: String, displayName: String, avatar: String?) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
        this.username = username
        this.displayName = displayName
        this.avatar = avatar
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}
