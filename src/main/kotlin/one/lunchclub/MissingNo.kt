package one.lunchclub

import one.lunchclub.command.CacheCommand
import one.lunchclub.command.DataCommand
import one.lunchclub.command.InventoryCommand
import one.lunchclub.command.NameCommand
import one.lunchclub.listener.CarlosListener
import one.lunchclub.listener.ChatListener
import one.lunchclub.listener.PlayerListener
import one.lunchclub.listener.WhitelistListener
import one.lunchclub.manager.NameManager
import one.lunchclub.manager.DataManager
import one.lunchclub.manager.PlayerManager
import one.lunchclub.manager.WhitelistManager
import org.bukkit.plugin.java.JavaPlugin

class MissingNo : JavaPlugin() {
    lateinit var dataManager: DataManager
    lateinit var nameManager: NameManager
    lateinit var whitelistManager: WhitelistManager
    lateinit var playerManager: PlayerManager

    override fun onLoad() {
        saveDefaultConfig()
    }

    override fun onEnable() {
        dataManager = DataManager(this)
        nameManager = NameManager(this)
        whitelistManager = WhitelistManager(this)
        playerManager = PlayerManager(this)

        registerListeners()
        registerCommands()
        registerCouples()
    }

    override fun onDisable() {
        dataManager.closeConnection()
        whitelistManager.closeConnection()
    }

    private fun registerListeners() {
        server.pluginManager.registerEvents(WhitelistListener(this), this)
        server.pluginManager.registerEvents(CarlosListener(this), this)
        server.pluginManager.registerEvents(ChatListener(this), this)
        server.pluginManager.registerEvents(PlayerListener(this), this)
    }

    private fun registerCommands() {
        getCommand("name")?.setExecutor(NameCommand(this))
        getCommand("fbi")?.setExecutor(DataCommand(this))
        getCommand("listfbi")?.setExecutor(CacheCommand(this))
    }

    private fun registerCouples() {
        val inventoryInstance = InventoryCommand(this)
        server.pluginManager.registerEvents(inventoryInstance, this)
        getCommand("inventory")?.setExecutor(inventoryInstance)
    }
}