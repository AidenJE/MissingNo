package one.lunchclub.util

import org.bukkit.entity.Entity
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

class CurveEntityTask(private val entity: Entity, private var counter: Int) : BukkitRunnable() {
    override fun run() {
        if (counter == 0) cancel()

        entity.velocity = entity.velocity.add(Vector(0.0, 1.0, 0.0).normalize())
        entity.velocity = entity.velocity.add(Vector(0.0, entity.velocity.y * 2, 0.0).normalize())
        counter--
    }
}