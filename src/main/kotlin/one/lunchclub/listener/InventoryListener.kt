package one.lunchclub.listener

import one.lunchclub.MissingNo
import one.lunchclub.util.InventorySingleton
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class InventoryListener(private val plugin: MissingNo) : Listener {
    private val inventories = InventorySingleton.inventories

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