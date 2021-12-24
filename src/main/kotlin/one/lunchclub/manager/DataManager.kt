package one.lunchclub.manager

import one.lunchclub.MissingNo
import java.io.File
import java.sql.*

class DataManager(plugin: MissingNo) {
    private val url: String = "jdbc:sqlite:${plugin.dataFolder}${File.separatorChar}data.db"
    private lateinit var connection: Connection

    init {
        plugin.server.scheduler.runTaskAsynchronously(plugin, (Runnable {
            setupPlayerTable()
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

    private fun setupPlayerTable() {
        executeUpdate("""CREATE TABLE IF NOT EXISTS player (
            |uuid VARCHAR(255) UNIQUE,
            |username VARCHAR(255),
            |dimension VARCHAR(255),
            |x REAL,
            |y REAL,
            |z REAL,
            |health REAL,
            |last_login INTEGER,
            |inventory TEXT
            |);""".trimMargin())
    }

    private fun setupNameTable() {
        executeUpdate("""CREATE TABLE IF NOT EXISTS name (
            |player_uuid VARCHAR(255) UNIQUE,
            |player_name VARCHAR(255)
            |);""".trimMargin())
    }

    fun prepareStatement(sql: String): PreparedStatement {
        return connection.prepareStatement(sql)
    }

    fun executeUpdate(sql: PreparedStatement) {
        if (!isConnected())
            connect()

        try {
            sql.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
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