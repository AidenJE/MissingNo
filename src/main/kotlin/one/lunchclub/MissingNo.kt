package one.lunchclub

import one.lunchclub.command.MissingNoCommand
import one.lunchclub.listener.CarlosListener
import org.bukkit.plugin.java.JavaPlugin

class MissingNo : JavaPlugin() {
    override fun onEnable() {
        saveDefaultConfig()

        getCommand("missingno")!!.setExecutor(MissingNoCommand(this))

        server.pluginManager.registerEvents(CarlosListener(this), this)
    }
}