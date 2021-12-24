package one.lunchclub.data

import one.lunchclub.MissingNo
import one.lunchclub.util.stringToInventory
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import java.util.*

class CachedPlayer(val uuid: UUID, val username: String, val lastLogin: Long, val inventory: String) {
    fun convertInventory(sender: InventoryHolder, plugin: MissingNo): Inventory {
        return stringToInventory(sender, inventory, plugin, "$username's Inventory")
    }

    override fun equals(other: Any?): Boolean {
        if (other is UUID) {
            return uuid == other
        }

        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = uuid.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + lastLogin.hashCode()
        result = 31 * result + inventory.hashCode()
        return result
    }
}