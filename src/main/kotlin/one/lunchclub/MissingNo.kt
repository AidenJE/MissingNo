package one.lunchclub

import one.lunchclub.command.HatCommand
import one.lunchclub.command.InventoryCommand
import one.lunchclub.command.NameCommand
import one.lunchclub.listener.*
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
        server.pluginManager.registerEvents(InventoryListener(this), this)
    }

    private fun registerCommands() {
        getCommand("name")?.setExecutor(NameCommand(this))
        getCommand("inventory")?.setExecutor(InventoryCommand(this))
        getCommand("hat")?.setExecutor(HatCommand(this))
    }
}