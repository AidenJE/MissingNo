package one.lunchclub

import one.lunchclub.command.*
import one.lunchclub.listener.*
import one.lunchclub.manager.*
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import org.geysermc.floodgate.api.FloodgateApi

class MissingNo : JavaPlugin() {
    val fancyName = "${ChatColor.DARK_GRAY}[${ChatColor.GOLD}MissingNo${ChatColor.DARK_GRAY}]${ChatColor.RESET}"

    lateinit var dataManager: DataManager
    lateinit var nameManager: NameManager
    lateinit var whitelistManager: WhitelistManager

    lateinit var floodgateApi: FloodgateApi

    override fun onLoad() {
        saveDefaultConfig()
    }

    override fun onEnable() {
        registerManagers()
        registerApis()
        registerListeners()
        registerCommands()
    }

    override fun onDisable() {
        dataManager.closeConnection()
        whitelistManager.closeConnection()
    }

    private fun registerManagers() {
        dataManager = DataManager(this)
        nameManager = NameManager(this)
        whitelistManager = WhitelistManager(this)
    }

    private fun registerApis() {
        floodgateApi = FloodgateApi.getInstance()
    }

    private fun registerListeners() {
        server.pluginManager.registerEvents(WhitelistListener(this), this)
        server.pluginManager.registerEvents(ChatListener(this), this)
        server.pluginManager.registerEvents(DeathListener(this), this)
    }

    private fun registerCommands() {
        getCommand("name")?.setExecutor(NameCommand(this))
        getCommand("hat")?.setExecutor(HatCommand(this))
    }
}