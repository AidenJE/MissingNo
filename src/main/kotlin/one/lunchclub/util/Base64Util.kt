package one.lunchclub.util

import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.Exception
import java.util.*

fun anyToBase64(any: Any): String? {
    return try {
        val byteStream = ByteArrayOutputStream()
        val bukkitStream = BukkitObjectOutputStream(byteStream)

        bukkitStream.writeObject(any)
        bukkitStream.close()

        Base64.getEncoder().encodeToString(byteStream.toByteArray())
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

fun base64ToAny(base64: String): Any? {
    return try {
        val byteStream = ByteArrayInputStream(Base64.getDecoder().decode(base64))
        val bukkitStream = BukkitObjectInputStream(byteStream)

        bukkitStream.readObject()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}