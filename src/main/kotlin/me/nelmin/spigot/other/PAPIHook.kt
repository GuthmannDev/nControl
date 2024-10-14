package me.nelmin.spigot.other

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.nelmin.spigot.nControl
import org.bukkit.OfflinePlayer

class PAPIHook(//
    private val plugin: nControl
) : PlaceholderExpansion() {

    override fun getIdentifier(): String {
        return "nc"
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

    override fun onRequest(offlinePlayer: OfflinePlayer?, params: String): String? {
        if (offlinePlayer == null)

            if (params.equals("", true))
                return "wuff"

        return null
    }
}