package one.lunchclub

import one.lunchclub.command.HatCommand
import one.lunchclub.command.NameCommand
import one.lunchclub.listener.*
import one.lunchclub.manager.NameManager
import one.lunchclub.manager.DataManager
import one.lunchclub.manager.WhitelistManager
import org.bukkit.plugin.java.JavaPlugin

class MissingNo : JavaPlugin() {
    lateinit var dataManager: DataManager
    lateinit var nameManager: NameManager
    lateinit var whitelistManager: WhitelistManager

    override fun onLoad() {
        saveDefaultConfig()
    }

    override fun onEnable() {
        dataManager = DataManager(this)
        nameManager = NameManager(this)
        whitelistManager = WhitelistManager(this)

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
    }

    private fun registerCommands() {
        getCommand("name")?.setExecutor(NameCommand(this))
        getCommand("hat")?.setExecutor(HatCommand(this))
    }
}