package one.lunchclub.listener

import one.lunchclub.MissingNo
import one.lunchclub.task.OnePunchTask
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Hanging
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

        if (victim is Hanging) return // Ignore: Item Frames, Paintings, and Leash Hitches

        if (attacker is Player && isCarlos(attacker)) {
            val isPlayerHoldingNothing = attacker.inventory.itemInMainHand.type == Material.AIR
            val isPlayerPunching = event.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK

            if (isPlayerHoldingNothing && isPlayerPunching) {
                event.isCancelled = true

                // Do effects
                attacker.playSound(attacker.location, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f)
                attacker.spawnParticle(Particle.EXPLOSION_HUGE, attacker.location, 3)
                attacker.playSound(attacker.location, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f)

                // Launch player
                val direction = attacker.location.direction.normalize()
                OnePunchTask(plugin, direction, victim).runTaskTimer(plugin, 0L, 5L)
            }
        }
    }
}