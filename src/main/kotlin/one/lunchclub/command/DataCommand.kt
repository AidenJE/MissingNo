package one.lunchclub.command

import net.kyori.adventure.text.Component
import one.lunchclub.MissingNo
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import java.text.SimpleDateFormat
import java.util.*

class DataCommand(private val plugin: MissingNo) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage(Component.text("${ChatColor.RED}Correct usage: /fbi <identifier>"))
            return false
        }

        val cachedPlayer = plugin.playerManager.getCachedPlayer(args[0])
        if (cachedPlayer != null) {
            val player = plugin.server.getPlayer(cachedPlayer.uuid)
            if (player != null) {
                plugin.playerManager.logPlayerData(player)
            }

            val date = Date(cachedPlayer.lastLogin)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            val message = Component.text()
                .append(Component.text("${ChatColor.BLUE}-=- ${cachedPlayer.username}'s File -=-${ChatColor.RESET}\n"))
                .append(Component.text("${ChatColor.GOLD}UUID: ${ChatColor.RESET}${cachedPlayer.uuid}\n"))
                .append(Component.text("${ChatColor.GOLD}DIMENSION: ${ChatColor.RESET}${cachedPlayer.dimension}\n"))
                .append(Component.text("${ChatColor.GOLD}X: ${ChatColor.RESET}${cachedPlayer.x}\n"))
                .append(Component.text("${ChatColor.GOLD}Y: ${ChatColor.RESET}${cachedPlayer.y}\n"))
                .append(Component.text("${ChatColor.GOLD}Z: ${ChatColor.RESET}${cachedPlayer.z}\n"))
                .append(Component.text("${ChatColor.GOLD}HEALTH: ${ChatColor.RESET}${cachedPlayer.health}\n"))
                .append(Component.text("${ChatColor.GOLD}LAST SEEN: ${ChatColor.RESET}${dateFormat.format(date)}\n"))
                .append(Component.text("${ChatColor.BLUE}-=- End File -=-${ChatColor.RESET}"))

            sender.sendMessage(message)
        } else {
            sender.sendMessage(Component.text("${ChatColor.RED}Player ${args[0]} not found"))
        }

        return true
    }
}