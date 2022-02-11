package one.lunchclub.listener

import net.kyori.adventure.text.Component
import one.lunchclub.MissingNo
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent

class WhitelistListener(private val plugin: MissingNo) : Listener {
    private val whitelistCache = plugin.whitelistManager.getWhitelistedPlayers().toMutableSet()

    @EventHandler
    fun onPlayerLogin(event: AsyncPlayerPreLoginEvent) {
        val uuid = event.uniqueId

        if (whitelistCache.contains(uuid))
            return

        if (!plugin.whitelistManager.isPlayerWhitelisted(uuid)) {
            plugin.whitelistManager.registerPlayer(uuid)
            val code = plugin.whitelistManager.getPlayerCode(uuid)
            plugin.whitelistManager.registerCode(code, uuid)

            val kickMessage = "${ChatColor.RED}You are not whitelisted. Register with the code:${ChatColor.RESET} $code"
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, Component.text(kickMessage))
        } else {
            whitelistCache.add(uuid)
        }
    }
}