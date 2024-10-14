package me.nelmin.spigot.events

import me.nelmin.spigot.configs.UserData
import me.nelmin.spigot.nControl
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinEvent(private val plugin: nControl) : Listener {

    @EventHandler
    fun onPlayerFirstJoin(event: PlayerJoinEvent) {
        if (event.player.hasPlayedBefore()) return

        val userData = UserData(event.player.name, event.player.uniqueId)
        plugin.loadedUserData.add(userData)
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (plugin.features.get("economy") as Boolean)
            UserData(event.player.name, event.player.uniqueId)
    }
}