package one.lunchclub

import one.lunchclub.listener.CarlosListener
import org.bukkit.plugin.java.JavaPlugin

class MissingNo : JavaPlugin() {
    override fun onEnable() {
        saveDefaultConfig()

        server.pluginManager.registerEvents(CarlosListener(this), this)
    }
}