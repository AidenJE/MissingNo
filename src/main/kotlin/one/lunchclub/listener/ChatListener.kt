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

        // If no custom name is set default to the player's username
        var name = plugin.nameManager.getName(player.uniqueId)
        if (name == null) {
            name = player.name
        }

        // Set Bedrock player names to blue and Java player names to red
        val isBedrockPlayer = plugin.floodgateApi.isFloodgatePlayer(player.uniqueId)
        val highlightColor = if (isBedrockPlayer) {
            ChatColor.GREEN
        } else {
            ChatColor.RED
        }

        // Create modified chat message
        val component = Component.text()
            .append(Component.text("${highlightColor}${player.name}")
                .hoverEvent(HoverEvent.showText(Component.text("${ChatColor.YELLOW}${name}"))))
            .append(Component.text("${ChatColor.RESET}: "))
            .append(message)
            .build()

        // Send modified chat message
        for (viewer in event.viewers())
            viewer.sendMessage(component)
    }
}