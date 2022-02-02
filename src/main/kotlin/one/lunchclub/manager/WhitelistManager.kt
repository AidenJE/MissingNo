package one.lunchclub.manager

import one.lunchclub.MissingNo
import java.util.*

class WhitelistManager(plugin: MissingNo) : SQLManager(plugin) {
    override val url: String = plugin.config.getString("whitelist.url")!!
    override fun setupTables() {
        setupPlayerTable()
        setupCodeTable()
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
        val stmt = prepareStatement("SELECT whitelisted FROM player WHERE uuid = ?")
        stmt.setString(1, uuid.toString())

        val data = executeQuery(stmt)
        if (data != null && data.next())
            return data.getBoolean("whitelisted")

        return false
    }

    private fun isPlayerRegistered(uuid: UUID): Boolean {
        val stmt = prepareStatement("SELECT COUNT(1) FROM player WHERE uuid = ?")
        stmt.setString(1, uuid.toString())

        val data = executeQuery(stmt)
        if (data != null && data.next())
            return data.getBoolean(1)

        return false
    }

    fun registerPlayer(uuid: UUID) {
        val stmt = prepareStatement("INSERT INTO player (uuid) VALUES (?)")
        stmt.setString(1, uuid.toString())

        if (!isPlayerRegistered(uuid)) {
            executeUpdate(stmt)
        }
    }

    private fun isCodeRegistered(code: String): Boolean {
        val stmt = prepareStatement("SELECT COUNT(1) FROM code WHERE code = ?")
        stmt.setString(1, code)

        val data = executeQuery(stmt)
        if (data != null && data.next())
            return data.getBoolean(1)

        return false
    }

    fun registerCode(code: String, uuid: UUID) {
        val stmt = prepareStatement("INSERT INTO code (code, player_uuid) VALUES (?, ?)")
        stmt.setString(1, code)
        stmt.setString(2, uuid.toString())

        if (!isCodeRegistered(code)) {
            executeUpdate(stmt)
        }
    }

    fun getPlayerCode(uuid: UUID): String {
        return uuid.toString().reversed().uppercase().substring(0, 12)
    }
}