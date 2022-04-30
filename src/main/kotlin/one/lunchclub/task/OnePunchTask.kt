package one.lunchclub.task

import one.lunchclub.MissingNo
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

class OnePunchTask(plugin: MissingNo, direction: Vector, private val victim: Entity) : BukkitRunnable() {
    private val knockback = plugin.config.getDouble("carlos.knockback")
    private val velocity = direction.multiply(knockback).setY(direction.y + 0.3).multiply(knockback)
    private var counter = 3

    override fun run() {
        if (counter == 0) {
            cancel()
            return
        }

        victim.velocity = velocity
        counter--
    }
}