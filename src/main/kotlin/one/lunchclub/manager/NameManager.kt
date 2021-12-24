package one.lunchclub.manager

import one.lunchclub.MissingNo
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*
import kotlin.collections.ArrayList

class NameManager(private val plugin: MissingNo) {
    data class NameData(val uuid: UUID, val name: String?)

    fun logNameData(uuid: UUID, name: String) {
        val isPlayerRegistered = getName(uuid) != null

        plugin.server.scheduler.runTaskAsynchronously(plugin, (Runnable {
            if (isPlayerRegistered) {
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

    fun getName(uuid: UUID): String? {
        val data = plugin.dataManager.executeQuery("SELECT * FROM name WHERE player_uuid = '$uuid';")
        if (data != null && !data.isClosed && data.next())
            return resultSetToNameData(data).name

        return null
    }

    private fun getNames(): Array<NameData> {
        val nameData: ArrayList<NameData> = ArrayList()
        val data = plugin.dataManager.executeQuery("SELECT * FROM name;")

        if (data != null) {
            try {
                while (data.next()) {
                    nameData.add(resultSetToNameData(data))
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }

        return nameData.toTypedArray()
    }

    private fun resultSetToNameData(data: ResultSet): NameData {
        val uuid = UUID.fromString(data.getString("player_uuid"))
        val name = data.getString("player_name")

        return NameData(uuid, name)
    }
}