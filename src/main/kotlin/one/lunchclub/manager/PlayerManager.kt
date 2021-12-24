package one.lunchclub.manager

import one.lunchclub.MissingNo
import one.lunchclub.data.CachedPlayer
import one.lunchclub.util.inventoryToString
import org.bukkit.entity.Player
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*
import kotlin.collections.ArrayList

class PlayerManager(private val plugin: MissingNo) {
    fun logPlayerData(player: Player) {
        var isPlayerRegistered = false
        if (getCachedPlayer(player.uniqueId) != null)
            isPlayerRegistered = true

        plugin.server.scheduler.runTaskAsynchronously(plugin, (Runnable {
            if (isPlayerRegistered) {
                val stmt = plugin.dataManager.prepareStatement("UPDATE player SET username = ?, dimension = ?, x = ?, y = ?, z = ?, health = ?, last_login = ?, inventory = ? WHERE uuid = ?")
                stmt.setString(1, player.name)
                stmt.setString(2, player.world.environment.toString())
                stmt.setDouble(3, player.location.x)
                stmt.setDouble(4, player.location.y)
                stmt.setDouble(5, player.location.z)
                stmt.setDouble(6, player.health)
                stmt.setLong(7, player.lastLogin)
                stmt.setString(8, inventoryToString(player.inventory))
                stmt.setString(9, player.uniqueId.toString())

                plugin.dataManager.executeUpdate(stmt)
            } else {
                val stmt = plugin.dataManager.prepareStatement("INSERT INTO player (uuid, username, dimension, x, y, z, health, last_login, inventory) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")
                stmt.setString(1, player.uniqueId.toString())
                stmt.setString(2, player.name)
                stmt.setString(3, player.world.environment.toString())
                stmt.setDouble(4, player.location.x)
                stmt.setDouble(5, player.location.y)
                stmt.setDouble(6, player.location.z)
                stmt.setDouble(7, player.health)
                stmt.setLong(8, player.lastLogin)
                stmt.setString(9, inventoryToString(player.inventory))

                plugin.dataManager.executeUpdate(stmt)
            }
        }))
    }

    fun getCachedPlayer(uuid: UUID): CachedPlayer? {
        val data = plugin.dataManager.executeQuery("SELECT * FROM player WHERE uuid = '$uuid';")
        if (data != null)
            return resultSetToCachedPlayer(data)

        return null
    }

    fun getCachedPlayer(username: String): CachedPlayer? {
        val data = plugin.dataManager.executeQuery("SELECT * FROM player WHERE username = '$username';")
        if (data != null)
            return resultSetToCachedPlayer(data)

        return null
    }

    fun getCachedPlayers(): Array<CachedPlayer> {
        val playerData: ArrayList<CachedPlayer> = ArrayList()
        val data = plugin.dataManager.executeQuery("SELECT * FROM player;")

        if (data != null) {
            try {
                while (data.next()) {
                    playerData.add(resultSetToCachedPlayer(data))
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }

        return playerData.toTypedArray()
    }

    private fun resultSetToCachedPlayer(data: ResultSet): CachedPlayer {
        val uuid = UUID.fromString(data.getString("uuid"))
        val username = data.getString("username")
        val dimension = data.getString("dimension")
        val x = data.getDouble("x")
        val y = data.getDouble("y")
        val z = data.getDouble("z")
        val health = data.getDouble("health")
        val lastLogin = data.getLong("last_login")
        val inventory = data.getString("inventory")

        return CachedPlayer(uuid, username, dimension, x, y, z, health, lastLogin, inventory)
    }
}