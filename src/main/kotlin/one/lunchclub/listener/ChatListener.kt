package one.lunchclub.listener

import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import one.lunchclub.MissingNo
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ChatListener(private val plugin: MissingNo) : Listener {
    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        val player = event.player
        val message = event.originalMessage()
        event.isCancelled = true

        var name = plugin.nameManager.getName(player.uniqueId)
        if (name == null) {
            name = player.name // If no custom name is set default to the player's username
        }

        val component = Component.text()
            .append(Component.text("${ChatColor.AQUA}${player.name}")
                .hoverEvent(HoverEvent.showText(Component.text("${ChatColor.YELLOW}${name}"))))
            .append(Component.text(": "))
            .append(message)
            .build()

        for (viewer in event.viewers())
            viewer.sendMessage(component)
    }
}