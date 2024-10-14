package me.nelmin.spigot.other

import me.nelmin.spigot.nControl
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class TextBuilder(private val text: String, private val isPath: Boolean = false) {
    private val plugin = nControl.instance
    private var updatedText = if (isPath) plugin.messages.get(text) as String? ?: "" else text

    fun append(string: String, before: Boolean): TextBuilder {
        if (!isPath) {
            updatedText = if (before) string + updatedText else updatedText + string
        }
        return this
    }

    fun prefix(prefixName: String): TextBuilder {
        val prefix = plugin.config.get("prefix.$prefixName") as String? ?: return this
        updatedText = "$prefix $updatedText"
        return this
    }

    fun replace(oldValue: Any, newValue: Any): TextBuilder {
        if (!isPath) updatedText = updatedText.replace(oldValue.toString(), newValue.toString())
        return this
    }

    fun replaceAll(map: Map<Any, Any>): TextBuilder {
        if (!isPath) {
            map.forEach { (oldValue, newValue) ->
                updatedText = updatedText.replace(oldValue.toString(), newValue.toString())
            }
        }
        return this
    }

    fun sendToConsole(): TextBuilder {
        plugin.server.consoleSender.sendMessage(getColorized())
        return this
    }

    fun sendTo(player: Player): TextBuilder {
        player.sendMessage(getColorized())
        return this
    }

    fun broadcast(): TextBuilder {
        plugin.server.broadcastMessage(getColorized())
        return this
    }

    fun getColorized(): String {
        return ChatColor.translateAlternateColorCodes('&', updatedText)
    }

    fun get(): String {
        return updatedText
    }
}
