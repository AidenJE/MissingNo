package one.lunchclub.command

import net.kyori.adventure.text.Component
import net.luckperms.api.node.Node
import one.lunchclub.MissingNo
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ChairCommand(private val plugin: MissingNo) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            plugin.luckpermsApi.userManager.modifyUser(sender.uniqueId) { user ->
                val chairUseNode = Node.builder("chairs.use").build()
                val hasSitPermission = user.cachedData.permissionData.checkPermission("chairs.use").asBoolean()

                if (hasSitPermission) {
                    sender.sendMessage(Component.text("${plugin.fancyName} ${ChatColor.RED}You can no longer right click to sit in chairs."))
                    user.data().remove(chairUseNode)
                } else {
                    sender.sendMessage(Component.text("${plugin.fancyName} ${ChatColor.GREEN}You can now right click to sit in chairs."))
                    user.data().add(chairUseNode)
                }
            }

            return true
        }

        return false
    }
}