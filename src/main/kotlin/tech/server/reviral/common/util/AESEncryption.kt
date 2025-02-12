package tech.server.reviral.common.util

import java.util.*
import java.util.concurrent.atomic.AtomicLong
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

/**
 *packageName    : tech.server.reviral.common.config.crypt
 * fileName       : AESEncryption
 * author         : joy58
 * date           : 2025-01-21
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-21        joy58       최초 생성
 */
object AESEncryption {

    private const val ALGORITHM = "AES"

    // Generate a secret key
    fun generateKey(): SecretKey {
        val keyGen = KeyGenerator.getInstance(ALGORITHM)
        keyGen.init(256) // AES-256
        return keyGen.generateKey()
    }

    // Encrypt text using a secret key
    fun encrypt(data: String, secretKey: SecretKey): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedBytes = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    // Decrypt text using a secret key
    fun decrypt(encryptedData: String, secretKey: SecretKey): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData))
        return String(decryptedBytes, Charsets.UTF_8)
    }

    // Convert a secret key to a string (for storage or transmission)
    fun keyToString(secretKey: SecretKey): String {
        return Base64.getEncoder().encodeToString(secretKey.encoded)
    }

    // Convert a string back to a secret key
    fun stringToKey(keyString: String): SecretKey {
        val decodedKey = Base64.getDecoder().decode(keyString)
        return SecretKeySpec(decodedKey, 0, decodedKey.size, ALGORITHM)
    }

    private val counter = AtomicLong(System.currentTimeMillis() % 10000)
    // Generated UniqueKey
    fun generatedUniqueKey(): String {
        val timestampPart = System.currentTimeMillis().toString().takeLast(10)
        val counterPart = counter.incrementAndGet() % 10000
        return (timestampPart + counterPart.toString().padStart(4, '0'))
    }
}