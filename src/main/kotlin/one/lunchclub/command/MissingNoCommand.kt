package one.lunchclub.command

import one.lunchclub.MissingNo
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class MissingNoCommand(private val plugin: MissingNo) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val messageHelper = MessageHelper(sender)
        val subcommand = args[0]

        if (args.isEmpty()) {
            messageHelper.sendInvalidArgumentMessage()
        } else if (subcommand == "get") {
            if (args.size == 2) {
                val key = args[1]

                if (sender.hasPermission("missingno.get")) {
                    if (plugin.config.contains(key)) {
                        val value = plugin.config.getString(key)
                        sender.sendMessage("${ChatColor.DARK_AQUA}${key}${ChatColor.GRAY} is set to ${ChatColor.DARK_AQUA}${value}")
                    } else {
                        messageHelper.sendMissingKeyMessage(key)
                    }
                } else {
                    messageHelper.sendMissingPermissionMessage()
                }
            } else {
                messageHelper.sendInvalidArgumentMessage()
            }
        } else if (subcommand == "set") {
            if (args.size == 3) {
                val key = args[1]
                var value: Any = args[2]

                if (sender.hasPermission("missingno.set")) {
                    if (plugin.config.contains(key)) {
                        value = inferTypeFromString(value.toString())
                        plugin.config.set(key, value)
                        plugin.saveConfig()
                        sender.sendMessage("${ChatColor.DARK_AQUA}${key}${ChatColor.GRAY} has been updated to ${ChatColor.DARK_AQUA}${value}")
                    } else {
                        messageHelper.sendMissingKeyMessage(key)
                    }
                } else {
                    messageHelper.sendMissingPermissionMessage()
                }
            } else {
                messageHelper.sendInvalidArgumentMessage()
            }
        }

        return true
    }

    private fun inferTypeFromString(value: String): Any {
        val doubleInference = value.toDoubleOrNull()
        if (doubleInference != null) {
            return doubleInference
        }

        val booleanInference = value.toBooleanStrictOrNull()
        if (booleanInference != null) {
            return booleanInference
        }

        return value
    }
}

class MessageHelper(private val sender: CommandSender) {
    fun sendInvalidArgumentMessage() {
        sender.sendMessage("${ChatColor.RED}Please specify a command argument.")
    }

    fun sendMissingPermissionMessage() {
        sender.sendMessage("${ChatColor.RED}You do not have permission to run this command.")
    }

    fun sendMissingKeyMessage(key: String) {
        sender.sendMessage("${ChatColor.RED}${key} does not exist.")
    }
}