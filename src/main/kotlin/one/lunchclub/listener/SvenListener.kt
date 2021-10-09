package one.lunchclub.listener

import io.papermc.paper.event.player.PlayerNameEntityEvent
import one.lunchclub.MissingNo
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.Damageable
import org.bukkit.entity.Player
import org.bukkit.entity.Wolf
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

class SvenListener(private val plugin: MissingNo) : Listener {
    // Kill anything that attacks Sven
    @EventHandler
    fun onEntityAttack(event: EntityDamageByEntityEvent) {
        val attacker = event.damager
        val victim = event.entity

        if (victim is Wolf && victim.name == "Sven" && !victim.isAngry) {
            if (attacker is Player)
                attacker.playSound(attacker.location, Sound.ENTITY_ENDER_DRAGON_GROWL, 50.0f, 50.0f)
            if (attacker is Damageable)
                attacker.health = 0.0
        }
    }

    // Make Sven impervious to everything except for the void
    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        val entity = event.entity

        if (entity is Wolf && entity.name == "Sven") {
            if (event.cause != EntityDamageEvent.DamageCause.VOID && !entity.isAngry)
                event.isCancelled = true
        }
    }

    // Stop attempts at renaming wolves named "Sven"
    @EventHandler
    fun onEntityRename(event: PlayerNameEntityEvent) {
        val player = event.player
        val entity = event.entity

        if (entity is Wolf) {
            val wolfName = entity.name
            val nameTag = event.name.toString()

            if (wolfName == "Sven" && nameTag != "Sven") {
                player.playSound(player.location, Sound.BLOCK_GLASS_BREAK, 50.0f, 50.0f)
                player.velocity = player.location.direction.normalize().multiply(-2)
                event.isCancelled = true
            }
        }
    }
}