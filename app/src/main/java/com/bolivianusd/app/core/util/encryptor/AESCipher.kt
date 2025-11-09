package com.bolivianusd.app.core.util.encryptor

import com.bolivianusd.app.core.util.emptyString
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

class AESCipher(val keyBase64: String, val ivBase64: String) {

    constructor() : this(emptyString, emptyString)

    companion object {
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val GCM_IV_LENGTH = 12
        private const val GCM_TAG_LENGTH = 128
        private const val AES_KEY_LENGTH = 32

        fun generateKeyBase64(): String {
            val key = ByteArray(AES_KEY_LENGTH).apply {
                SecureRandom().nextBytes(this)
            }
            return Base64.getEncoder().encodeToString(key)
        }

        fun generateIVBase64(): String {
            val iv = ByteArray(GCM_IV_LENGTH).apply {
                SecureRandom().nextBytes(this)
            }
            return Base64.getEncoder().encodeToString(iv)
        }
    }

    @Throws(CryptoException::class)
    fun encrypt(plaintext: String, keyBase64: String, ivBase64: String): String {
        validateInputs(plaintext, keyBase64, ivBase64)

        return try {
            val key = decodeKey(keyBase64)
            val iv = decodeIV(ivBase64)

            val cipher = Cipher.getInstance(TRANSFORMATION)
            val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
            cipher.init(Cipher.ENCRYPT_MODE, key, spec)

            val ciphertext = cipher.doFinal(plaintext.toByteArray(StandardCharsets.UTF_8))
            Base64.getEncoder().encodeToString(ciphertext)
        } catch (ex: Exception) {
            throw CryptoException("Encryption failed", ex)
        }
    }

    @Throws(CryptoException::class)
    fun encrypt(plaintext: String) = encrypt(plaintext, keyBase64, ivBase64)

    @Throws(CryptoException::class)
    fun decrypt(ciphertextBase64: String, keyBase64: String, ivBase64: String): String {
        validateInputs(ciphertextBase64, keyBase64, ivBase64)

        return try {
            val key = decodeKey(keyBase64)
            val iv = decodeIV(ivBase64)
            val ciphertext = Base64.getDecoder().decode(ciphertextBase64)

            val cipher = Cipher.getInstance(TRANSFORMATION)
            val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
            cipher.init(Cipher.DECRYPT_MODE, key, spec)

            val plaintext = cipher.doFinal(ciphertext)
            String(plaintext, StandardCharsets.UTF_8)
        } catch (ex: Exception) {
            throw CryptoException("Decryption failed", ex)
        }
    }

    fun decrypt(ciphertextBase64: String) = decrypt(ciphertextBase64, keyBase64, ivBase64)

    private fun decodeKey(keyBase64: String): SecretKey {
        val keyBytes = Base64.getDecoder().decode(keyBase64)
        if (keyBytes.size != AES_KEY_LENGTH) {
            throw CryptoException("Key must be $AES_KEY_LENGTH bytes long after decoding")
        }
        return SecretKeySpec(keyBytes, "AES")
    }

    private fun decodeIV(ivBase64: String): ByteArray {
        val iv = Base64.getDecoder().decode(ivBase64)
        if (iv.size != GCM_IV_LENGTH) {
            throw CryptoException("IV must be $GCM_IV_LENGTH bytes long after decoding")
        }
        return iv
    }

    private fun validateInputs(text: String, keyBase64: String, ivBase64: String) {
        if (text.isEmpty()) {
            throw CryptoException("Text cannot be empty")
        }
        if (keyBase64.isEmpty()) {
            throw CryptoException("Key cannot be empty")
        }
        if (ivBase64.isEmpty()) {
            throw CryptoException("IV cannot be empty")
        }
    }
}

class CryptoException(message: String, cause: Throwable? = null) : Exception(message, cause)