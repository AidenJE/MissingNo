package one.lunchclub.listener

import one.lunchclub.MissingNo
import one.lunchclub.util.CurveEntityTask
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

class CarlosListener(private val plugin: MissingNo) : Listener {
    private fun isCarlos(player: Player): Boolean {
        val carlosUniqueId = "5aac6576-86f7-468a-8f27-2d3c3aac74d7"
        val playerUniqueId = player.uniqueId.toString()

        return playerUniqueId == carlosUniqueId
    }

    @EventHandler
    fun onAttack(event: EntityDamageByEntityEvent) {
        val attacker = event.damager
        val victim = event.entity

        if (attacker is Player && isCarlos(attacker)) {
            val isPlayerHoldingNothing = attacker.inventory.itemInMainHand.type == Material.AIR
            val isPlayerPunching = event.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK

            if (isPlayerHoldingNothing && isPlayerPunching) {
                event.isCancelled = true

                // Do effects
                attacker.world.playSound(attacker.location, Sound.ENTITY_GENERIC_EXPLODE, 50.0f, 1.0f)
                attacker.world.spawnParticle(Particle.EXPLOSION_HUGE, attacker.location, 3)
                attacker.world.playSound(attacker.location, Sound.ENTITY_GENERIC_EXPLODE, 50.0f, 1.0f)

                // Launch player
                val knockback = plugin.config.getDouble("carlos.knockback")
                victim.velocity = attacker.location.direction.normalize().multiply(knockback)

                // Curve player
                CurveEntityTask(victim, 5).runTaskTimer(plugin, 10L, 10L)
            }
        }
    }
}