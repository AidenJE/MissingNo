package one.lunchclub.manager

import one.lunchclub.MissingNo
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*

class WhitelistManager(plugin: MissingNo) {
    private val url: String = plugin.config.getString("whitelist.url")!!
    private lateinit var connection: Connection

    init {
        plugin.server.scheduler.runTaskAsynchronously(plugin, (Runnable {
            setupPlayerTable()
            setupCodeTable()
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
        if (!connection.isClosed) {
            connection.close()
        }
    }

    private fun setupPlayerTable() {
        executeUpdate("""CREATE TABLE IF NOT EXISTS player(
            |uuid varchar(64) PRIMARY KEY,
            |whitelisted BOOLEAN NOT NULL DEFAULT 0
            |);""".trimMargin())
    }

    private fun setupCodeTable() {
        executeUpdate("""CREATE TABLE IF NOT EXISTS code(
            |code varchar(64) PRIMARY KEY,
            |player_uuid varchar(64) NOT NULL,
            |CONSTRAINT fk_player_uuid FOREIGN KEY(player_uuid) REFERENCES player(uuid)
            |);""".trimMargin())
    }

    fun isPlayerWhitelisted(uuid: UUID): Boolean {
        val data = executeQuery("SELECT whitelisted FROM player WHERE uuid = '$uuid';")
        if (data != null && data.next())
            return data.getBoolean("whitelisted")

        return false
    }

    private fun isPlayerRegistered(uuid: UUID): Boolean {
        val data = executeQuery("SELECT COUNT(1) FROM player WHERE uuid = '$uuid';")
        if (data != null && data.next())
            return data.getBoolean(1)

        return false
    }

    fun registerPlayer(uuid: UUID) {
        if (!isPlayerRegistered(uuid)) {
            executeUpdate("INSERT INTO player (uuid) VALUES ('$uuid');")
        }
    }

    private fun isCodeRegistered(code: String): Boolean {
        val data = executeQuery("SELECT COUNT(1) FROM player WHERE uuid = '$code';")
        if (data != null && data.next())
            return data.getBoolean(1)

        return false
    }

    fun registerCode(code: String, uuid: UUID) {
        if (!isCodeRegistered(code)) {
            executeUpdate("INSERT INTO code (code, player_uuid) VALUES ('$code', '$uuid');")
        }
    }

    fun getPlayerCode(uuid: UUID): String {
        return uuid.toString().reversed().uppercase().substring(0, 12)
    }

    private fun executeUpdate(sql: String) {
        if (!isConnected())
            connect()

        try {
            connection.createStatement().executeUpdate(sql)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    private fun executeQuery(sql: String): ResultSet? {
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