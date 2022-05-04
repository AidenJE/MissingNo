package one.lunchclub.listener

import net.kyori.adventure.text.Component
import one.lunchclub.MissingNo
import one.lunchclub.task.LaserTask
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Damageable
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.inventory.ItemStack

class HunterListener(private val plugin: MissingNo) : Listener {
    private fun isHunter(player: Player): Boolean {
        val hunterUniqueId = "0dbf3846-efe8-4834-ad26-6c4ac7a97029"
        val playerUniqueId = player.uniqueId.toString()

        return playerUniqueId == hunterUniqueId
    }

    @EventHandler
    fun onProjectileHit(event: ProjectileHitEvent) {
        val projectile = event.entity
        val thrower = projectile.shooter
        val victim = event.hitEntity

        if (projectile is Snowball && thrower is Player && isHunter(thrower)) {
            // Send laser
            if (victim is Damageable) {
                thrower.sendMessage(Component.text("${plugin.fancyName}${ChatColor.RED} Space lasers online... Target will be eliminated in 5 seconds."))
                LaserTask(plugin, victim).runTaskLater(plugin, 20L * 5L)
            }

            // Replenish Snowball
            val snowball = ItemStack(Material.SNOWBALL)
            thrower.inventory.addItem(snowball)
        }
    }
}