package one.lunchclub.data

import java.util.*

class CachedPlayer(val uuid: UUID, val username: String, val dimension: String, val x: Double, val y: Double, val z: Double, val health: Double, val lastLogin: Long, val inventory: String) {
    override fun equals(other: Any?): Boolean {
        if (other is UUID) {
            return uuid == other
        }

        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = uuid.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + dimension.hashCode()
        result = 31 * result + x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        result = 31 * result + health.hashCode()
        result = 31 * result + lastLogin.hashCode()
        result = 31 * result + inventory.hashCode()
        return result
    }
}