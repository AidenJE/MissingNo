package one.lunchclub.listener

import one.lunchclub.MissingNo
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

class CarlosListener(private val plugin: MissingNo) : Listener {
    private fun isPlayerCarlos(player: Player): Boolean {
        val carlosUniqueId = "5aac6576-86f7-468a-8f27-2d3c3aac74d7"
        val playerUniqueId = player.uniqueId.toString()

        return playerUniqueId == carlosUniqueId
    }

    @EventHandler
    fun onAttack(event: EntityDamageByEntityEvent) {
        val attacker = event.damager
        val victim = event.entity

        if (attacker is Player) {
            val isPlayerHoldingNothing = attacker.inventory.itemInMainHand.type == Material.AIR
            val isPlayerAttacking = event.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK

            if (isPlayerCarlos(attacker) && isPlayerHoldingNothing && isPlayerAttacking) {
                val knockback = plugin.config.getDouble("carlos.knockback")
                victim.velocity = attacker.location.direction.normalize().multiply(knockback)
            }
        }
    }
}