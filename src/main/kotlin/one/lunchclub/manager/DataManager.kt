package one.lunchclub.manager

import one.lunchclub.MissingNo
import java.io.File
import java.sql.*

class DataManager(plugin: MissingNo) {
    private val url: String = "jdbc:sqlite:${plugin.dataFolder}${File.separatorChar}data.db"
    private lateinit var connection: Connection

    init {
        plugin.server.scheduler.runTaskAsynchronously(plugin, (Runnable {
            setupNameTable()
        }))
    }

    private fun connect() {
        try {
            connection = DriverManager.getConnection(url)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    private fun isConnected(): Boolean {
        return try {
            this::connection.isInitialized && !connection.isClosed
        } catch (e: SQLException) {
            e.printStackTrace()
            false
        }
    }

    fun closeConnection() {
        if (isConnected())
            connection.close()
    }

    private fun setupNameTable() {
        executeUpdate("""CREATE TABLE IF NOT EXISTS name (
            |player_uuid VARCHAR(255) UNIQUE,
            |player_name VARCHAR(255)
            |);""".trimMargin())
    }

    fun executeUpdate(sql: String) {
        if (!isConnected())
            connect()

        try {
            connection.createStatement().executeUpdate(sql)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun executeQuery(sql: String): ResultSet? {
        if (!isConnected())
            connect()

        return try {
            connection.createStatement().executeQuery(sql)
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }
}