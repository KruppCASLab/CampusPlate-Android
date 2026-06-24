package edu.cwru.caslab.campusplate.security

interface Cryptographer {
    fun encrypt(plaintext: String): String
    fun decrypt(stored: String): String
}