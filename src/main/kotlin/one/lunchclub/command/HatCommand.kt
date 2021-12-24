package one.lunchclub.command

import one.lunchclub.MissingNo
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class HatCommand(private val plugin: MissingNo) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val heldItem = sender.inventory.itemInMainHand

            sender.inventory.setItemInMainHand(sender.inventory.helmet)
            sender.inventory.helmet = heldItem
            return true
        }

        return false
    }
}