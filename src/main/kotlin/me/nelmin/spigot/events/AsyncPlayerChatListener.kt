package me.nelmin.spigot.events

import me.clip.placeholderapi.PlaceholderAPI
import me.nelmin.spigot.nControl
import me.nelmin.spigot.other.TextBuilder
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class AsyncPlayerChatListener(private val plugin: nControl) : Listener {

    @EventHandler
    fun onAsyncChat(event: AsyncPlayerChatEvent) {
        event.isCancelled = true

        val player = event.player
        var message = PlaceholderAPI.setPlaceholders(player, plugin.config.get("format.chat") as String)
            .replace("%nc_chat_message%", event.message)

        if (player.hasPermission("nc.chat.colored")) {
            message = TextBuilder(message).getColorized()
        }

        plugin.server.onlinePlayers.forEach { onlinePlayer ->
            val data = plugin.loadedUserData.find { it.uuid == onlinePlayer.uniqueId }
            val formattedMessage = message.replace(
                onlinePlayer.name,
                TextBuilder(plugin.config.get("format.chat_you") as String).getColorized()
            )
            val finalMessage = PlaceholderAPI.setPlaceholders(onlinePlayer, formattedMessage)

            if (data?.chatDisabled != true) {
                onlinePlayer.sendMessage(finalMessage)
            }
        }
    }
}
