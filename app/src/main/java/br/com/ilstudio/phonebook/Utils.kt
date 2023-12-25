package br.com.ilstudio.phonebook

import android.content.SharedPreferences
import java.math.BigInteger
import java.security.MessageDigest

class Utils {
    fun generateHashString(input: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val hashBytes = md.digest(input.toByteArray())
        val hashText = BigInteger(1, hashBytes).toString(16)

        return hashText.padStart(32, '0')
    }

    fun isHexHash(input: String): Boolean {
        val hexRegex = Regex("[0-9a-fA-F]{64}")
        return input.matches(hexRegex)
    }

    fun clearData(editor: SharedPreferences.Editor) {
        editor.putString("username", "")
        editor.putString("password", "")
        editor.putInt("userId", 0)
        editor.apply()
    }
}
