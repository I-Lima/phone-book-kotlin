package br.com.ilstudio.phonebook

import java.math.BigInteger
import java.security.MessageDigest

class Utils {
    fun generateHashString(input: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val hashBytes = md.digest(input.toByteArray())
        val hashText = BigInteger(1, hashBytes).toString(16)

        return hashText.padStart(32, '0')
    }
}
