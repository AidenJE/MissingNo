package one.lunchclub.command

import net.kyori.adventure.text.Component
import one.lunchclub.MissingNo
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class NameCommand(private val plugin: MissingNo) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage(Component.text("${plugin.fancyName} ${ChatColor.RED}Correct usage: ${ChatColor.GRAY}/name <name>"))
            return false
        }

        if (sender is Player) {
            val name = args.reduce{prev, next -> "$prev $next"}
            plugin.nameManager.logName(sender.uniqueId, name)
            sender.sendMessage(Component.text("${plugin.fancyName} ${ChatColor.GREEN}Updated your name to: ${ChatColor.WHITE}$name"))

            return true
        }

        return false
    }
}