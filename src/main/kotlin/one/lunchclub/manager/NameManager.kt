package one.lunchclub.manager

import one.lunchclub.MissingNo
import one.lunchclub.data.NameData
import java.sql.SQLException
import java.util.*
import kotlin.collections.ArrayList

class NameManager(private val plugin: MissingNo) {
    fun addNameData(uuid: UUID, name: String) {
        val data = getNameData()

        var isPlayerRegistered = false
        for (player in data) {
            if (uuid == player.uuid)
                isPlayerRegistered = true
        }

        plugin.server.scheduler.runTaskAsynchronously(plugin, (Runnable {
            if (isPlayerRegistered) {
                plugin.dataManager.executeUpdate("UPDATE name SET player_name = '$name' WHERE player_uuid = '$uuid';")
            } else {
                plugin.dataManager.executeUpdate("INSERT INTO name (player_uuid, player_name) VALUES ('$uuid', '$name');")
            }
        }))
    }

    fun getChatName(uuid: UUID): String? {
        val data = getNameData()

        for (player in data) {
            if (uuid == player.uuid)
                return player.name
        }

        return null
    }

    private fun getNameData(): ArrayList<NameData> {
        val nameData: ArrayList<NameData> = ArrayList()
        val data = plugin.dataManager.executeQuery("SELECT player_uuid, player_name FROM name;")

        if (data != null) {
            try {
                while (data.next()) {
                    val uuid = UUID.fromString(data.getString("player_uuid"))
                    val name = data.getString("player_name")
                    nameData.add(NameData(uuid, name))
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }

        return nameData
    }
}