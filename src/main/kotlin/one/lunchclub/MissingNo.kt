package one.lunchclub

import one.lunchclub.command.NameCommand
import one.lunchclub.listener.CarlosListener
import one.lunchclub.listener.ChatListener
import one.lunchclub.listener.WhitelistListener
import one.lunchclub.manager.NameManager
import one.lunchclub.manager.DataManager
import org.bukkit.plugin.java.JavaPlugin

class MissingNo : JavaPlugin() {
    lateinit var dataManager: DataManager
    lateinit var nameManager: NameManager

    override fun onLoad() {
        saveDefaultConfig()
    }

    override fun onEnable() {
        dataManager = DataManager(this)
        nameManager = NameManager(this)

        registerListeners()
        registerCommands()
    }

    override fun onDisable() {
        dataManager.closeConnection()
    }

    private fun registerListeners() {
        server.pluginManager.registerEvents(WhitelistListener(this), this)
        server.pluginManager.registerEvents(CarlosListener(this), this)
        server.pluginManager.registerEvents(ChatListener(this), this)
    }

    private fun registerCommands() {
        getCommand("name")?.setExecutor(NameCommand(this))
    }
}