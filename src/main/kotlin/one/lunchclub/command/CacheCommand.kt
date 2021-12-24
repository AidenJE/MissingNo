package one.lunchclub.command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import one.lunchclub.MissingNo
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class CacheCommand(private val plugin: MissingNo) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val players = plugin.playerManager.getCachedPlayers()
        val message = Component.text()

        for (player in players) {
            message.append(Component.text("${ChatColor.BLUE}${ChatColor.UNDERLINE}${player.username}${ChatColor.RESET}, "))
                .hoverEvent(HoverEvent.showText(Component.text("${ChatColor.YELLOW}Click to copy FBI file command${ChatColor.RESET}")))
                .clickEvent(ClickEvent.copyToClipboard("/fbi ${player.username}"))
        }

        sender.sendMessage(message)
        return true
    }
}