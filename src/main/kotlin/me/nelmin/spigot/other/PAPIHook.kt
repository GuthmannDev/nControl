package me.nelmin.spigot.other

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.nelmin.spigot.nControl
import org.bukkit.OfflinePlayer

class PAPIExpansion(//
    private val plugin: nControl
) : PlaceholderExpansion() {

    override fun getIdentifier(): String {
        return plugin.description.name + "-Money"
    }

    override fun getAuthor(): String {
        return plugin.description.authors.joinToString(", ")
    }

    override fun getVersion(): String {
        return plugin.description.version
    }

    override fun persist(): Boolean {
        return true
    }

    override fun onRequest(player: OfflinePlayer?, params: String): String? {


        return null;
    }
}