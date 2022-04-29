package one.lunchclub.command

import net.luckperms.api.node.Node
import one.lunchclub.MissingNo
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ChairCommand(private val plugin: MissingNo) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            plugin.luckpermsApi.userManager.modifyUser(sender.uniqueId) { user ->
                val hasSitPermission = user.cachedData.permissionData.checkPermission("chairs.use").asBoolean()

                if (hasSitPermission) {
                    user.data().remove(Node.builder("chairs.use").build())
                } else {
                    user.data().add(Node.builder("chairs.use").build())
                }
            }
            return true
        }

        return false
    }
}