package one.lunchclub.listener

import one.lunchclub.MissingNo
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerListener(private val plugin: MissingNo) : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        plugin.playerManager.logPlayerData(event.player)
    }

    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        plugin.playerManager.logPlayerData(event.player)
    }
}