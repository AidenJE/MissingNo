package one.lunchclub.command

import net.kyori.adventure.text.Component
import one.lunchclub.MissingNo
import one.lunchclub.util.stringToInventory
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory

class InventoryCommand(private val plugin: MissingNo) : CommandExecutor, Listener {
    private val inventories: ArrayList<Inventory> = ArrayList()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage(Component.text("${ChatColor.RED}Correct usage: /inventory <identifier>"))
            return false
        }

        if (sender is Player) {
            val cachedPlayer = plugin.playerManager.getCachedPlayer(args[0])
            if (cachedPlayer != null) {
                val player = plugin.server.getPlayer(cachedPlayer.uuid)
                if (player != null) {
                    plugin.playerManager.logPlayerData(player)
                }

                val inventory = stringToInventory(sender, cachedPlayer.inventory, plugin, "${cachedPlayer.username}'s Inventory")
                inventories.add(inventory)
                sender.openInventory(inventory)
            } else {
                sender.sendMessage(Component.text("${ChatColor.RED}Player ${args[0]} not found"))
            }

            return true
        }

        return false
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (!inventories.contains(event.inventory)) return
        event.isCancelled = true
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        if (inventories.contains(event.inventory)) {
            inventories.remove(event.inventory)
        }
    }
}