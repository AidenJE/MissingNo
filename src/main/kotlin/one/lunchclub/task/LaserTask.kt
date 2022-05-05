package one.lunchclub.task

import one.lunchclub.MissingNo
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Damageable
import org.bukkit.scheduler.BukkitRunnable

class LaserTask(plugin: MissingNo, private val victim: Damageable) : BukkitRunnable() {
    private val laserLength = 200.0
    private val particleStep = 1.0
    private val particleOptions = Particle.DustOptions(Color.fromRGB(128, 0, 128), 1.0f)

    override fun run() {
        if (victim.isDead) return

        // Laser effect
        var currentParticleStep = laserLength
        while (currentParticleStep != 0.0) {
            val particleLocation = victim.location.add(0.0, currentParticleStep, 0.0)
            Bukkit.getLogger().info("Spawned particle at: $particleLocation")
            victim.world.spawnParticle(Particle.REDSTONE, particleLocation, 10, particleOptions)

            currentParticleStep -= particleStep
        }

        // Explosion
        victim.world.spawnParticle(Particle.EXPLOSION_HUGE, victim.location, 3)
        victim.world.playSound(victim.location, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f)

        // Kill entity
        victim.health = 0.0
    }
}