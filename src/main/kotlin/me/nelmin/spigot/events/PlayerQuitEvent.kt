package me.nelmin.spigot.events

import me.nelmin.spigot.nControl
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuitEvent(private val plugin: nControl) : Listener {

    @EventHandler
    fun onPlayerDisconnect(event: PlayerQuitEvent) {
        if (plugin.features.get("economy") as Boolean) plugin.saveUserData(event.player.uniqueId)
    }
}