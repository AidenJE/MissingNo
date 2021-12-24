package one.lunchclub.command

import net.kyori.adventure.text.Component
import one.lunchclub.MissingNo
import one.lunchclub.util.InventorySingleton
import one.lunchclub.util.getInventory
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.Inventory

class InventoryCommand(private val plugin: MissingNo) : CommandExecutor, Listener {
    private val inventories = InventorySingleton.inventories

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage(Component.text("${ChatColor.RED}Correct usage: /inventory <identifier>"))
            return false
        }

        val target = args[0]
        if (sender is Player) {
            val targetPlayer = plugin.server.getPlayer(target)
            val cachedTargetPlayer = plugin.playerManager.getCachedPlayer(target)

            val inventory: Inventory? = getInventory(targetPlayer, plugin) ?: cachedTargetPlayer?.convertInventory(sender, plugin)
            if (inventory == null) {
                sender.sendMessage("${ChatColor.RED}Player $target not found")
                return true
            }

            inventories.add(inventory)
            sender.openInventory(inventory)
        }

        return true
    }
}