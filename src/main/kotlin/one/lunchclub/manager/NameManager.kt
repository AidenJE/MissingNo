package one.lunchclub.manager

import one.lunchclub.MissingNo
import java.util.*

class NameManager(private val plugin: MissingNo) {
    fun logName(uuid: UUID, name: String) {
        val isRegistered = isPlayerRegistered(uuid)

        plugin.server.scheduler.runTaskAsynchronously(plugin, (Runnable {
            if (isRegistered) {
                val stmt = plugin.dataManager.prepareStatement("UPDATE name SET player_name = ? WHERE player_uuid = ?")
                stmt.setString(1, name)
                stmt.setString(2, uuid.toString())

                plugin.dataManager.executeUpdate(stmt)
            } else {
                val stmt = plugin.dataManager.prepareStatement("INSERT INTO name (player_uuid, player_name) VALUES (?, ?)")
                stmt.setString(1, uuid.toString())
                stmt.setString(2, name)

                plugin.dataManager.executeUpdate(stmt)
            }
        }))
    }

    private fun isPlayerRegistered(uuid: UUID): Boolean {
        val stmt = plugin.dataManager.prepareStatement("SELECT COUNT(1) FROM Name WHERE player_uuid = ?")
        stmt.setString(1, uuid.toString())

        val data = plugin.dataManager.executeQuery(stmt)
        if (data != null && data.next())
            return data.getBoolean(1)

        return false
    }

    fun getName(uuid: UUID): String? {
        val stmt = plugin.dataManager.prepareStatement("SELECT player_name FROM Name WHERE player_uuid = ?")
        stmt.setString(1, uuid.toString())

        val data = plugin.dataManager.executeQuery(stmt)
        if (data != null && !data.isClosed && data.next())
            return data.getString("player_name")

        return null
    }
}