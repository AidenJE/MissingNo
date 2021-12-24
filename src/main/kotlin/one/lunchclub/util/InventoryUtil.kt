package one.lunchclub.util

import net.kyori.adventure.text.Component
import one.lunchclub.MissingNo
import org.apache.commons.lang.StringUtils
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

object InventorySingleton {
    val inventories: ArrayList<Inventory> = ArrayList()
}

fun getInventorySize(size: Int): Int {
    return when {
        size <= 9 -> {
            9
        }
        size <= 18 -> {
            18
        }
        size <= 27 -> {
            27
        }
        size <= 36 -> {
            36
        }
        size <= 45 -> {
            45
        }
        else -> {
            54
        }
    }
}

fun getInventory(player: Player?, plugin: MissingNo): Inventory? {
    if (player == null)
        return null

    val contents = player.inventory.contents?.map{itemStack -> itemStack ?: ItemStack(Material.AIR)}
    if (contents != null) {
        val inventory = plugin.server.createInventory(player, getInventorySize(contents.size))
        contents.forEachIndexed{i, itemStack -> run {
            inventory.setItem(i, itemStack)
        }}

        return inventory
    }

    return null
}

fun inventoryToString(inventory: Inventory): String {
    val stringList: ArrayList<String?> = ArrayList()
    val airItem = ItemStack(Material.AIR)

    for (item in inventory.contents!!) {
        stringList.add(anyToBase64(item ?: airItem))
    }

    return StringUtils.join(stringList, '|')
}

fun stringToInventory(inventoryHolder: InventoryHolder, string: String, plugin: MissingNo, title: String="Inventory"): Inventory {
    val itemStrings = string.split("|")

    if (itemStrings.isNotEmpty() && itemStrings[0] != "") {
        val inventory = plugin.server.createInventory(inventoryHolder, getInventorySize(itemStrings.size), Component.text(title))

        itemStrings.forEachIndexed{i, itemString -> run {
                val item = base64ToAny(itemString)

                if (item is ItemStack)
                    inventory.setItem(i, item)
        }}

        return inventory
    }

    return plugin.server.createInventory(inventoryHolder, 9)
}