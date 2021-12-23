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
        if (sender is Player) {
            if (args.isEmpty()) {
                sender.sendMessage(Component.text("${ChatColor.RED}Correct usage: /name <name>"))
                return false
            }

            val name = args.reduce{prev, next -> "$prev $next"}
            plugin.nameManager.addNameData(sender.uniqueId, name)
            sender.sendMessage(Component.text("${ChatColor.GREEN}Updated your name to: $name"))

            return true
        }

        return false
    }
}