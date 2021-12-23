package one.lunchclub.listener

import one.lunchclub.MissingNo
import one.lunchclub.manager.WhitelistManager
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.server.PluginDisableEvent
import java.util.*
import kotlin.collections.ArrayList

class WhitelistListener(plugin: MissingNo) : Listener {
    private val crud = WhitelistManager(plugin)
    private val registeredCache: ArrayList<UUID> = ArrayList()

    @EventHandler
    fun onPlayerLogin(event: PlayerLoginEvent) {
        val player = event.player
        val uuid = player.uniqueId

        if (registeredCache.contains(uuid))
            return

        if (!crud.isPlayerWhitelisted(uuid)) {
            crud.registerPlayer(uuid)
            val code = crud.getPlayerCode(uuid)
            crud.registerCode(code, uuid)

            val kickMessage = "You are not whitelisted. Register with the code: $code"
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/kick ${player.name} $kickMessage") // Awful Bedrock hack
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, kickMessage)
        } else {
            registeredCache.add(uuid)
        }
    }

    @EventHandler
    fun onDisable(event: PluginDisableEvent) {
        crud.closeConnection()
    }
}