package one.lunchclub.listener

import net.kyori.adventure.text.Component
import one.lunchclub.MissingNo
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class DeathListener(private val plugin: MissingNo) : Listener {
    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        var message = event.deathMessage()
        if (message == null) {
            message = Component.text("${event.player.name} died of unknown causes.")
        }

        val newMessage = Component.text("${ChatColor.GOLD}->${ChatColor.RESET} ${ChatColor.GRAY}").append(message)
        event.deathMessage(newMessage)
    }
}