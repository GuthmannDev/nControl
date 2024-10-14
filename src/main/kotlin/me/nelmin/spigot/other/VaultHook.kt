package me.nelmin.spigot.other

import me.nelmin.spigot.nControl
import net.milkbowl.vault.chat.Chat
import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.economy.EconomyResponse
import net.milkbowl.vault.permission.Permission
import org.bukkit.OfflinePlayer

class VaultHook(private val plugin: nControl) {

    private val chat: Chat? by lazy { getService(Chat::class.java) }
    private val economy: Economy? by lazy { getService(Economy::class.java) }
    private val permissions: Permission? by lazy { getService(Permission::class.java) }

    init {
        if (plugin.server.pluginManager.getPlugin("Vault") != null) {
            chat ?: throw IllegalStateException("Chat service not available.")
            economy ?: throw IllegalStateException("Economy service not available.")
            permissions ?: throw IllegalStateException("Permissions service not available.")
        }
    }

    private fun <T> getService(serviceClass: Class<T>): T? {
        return plugin.server.servicesManager.getRegistration(serviceClass)?.provider
    }

    fun getBalance(offlinePlayer: OfflinePlayer): Double {
        return economy?.getBalance(offlinePlayer) ?: 0.0
    }

    fun withdraw(offlinePlayer: OfflinePlayer, amount: Double): EconomyResponse {
        return economy?.withdrawPlayer(offlinePlayer, amount) ?: EconomyResponse(
            0.0,
            0.0,
            EconomyResponse.ResponseType.FAILURE,
            "Economy service not available."
        )
    }

    fun deposit(offlinePlayer: OfflinePlayer, amount: Double): EconomyResponse {
        return economy?.depositPlayer(offlinePlayer, amount) ?: EconomyResponse(
            0.0,
            0.0,
            EconomyResponse.ResponseType.FAILURE,
            "Economy service not available."
        )
    }

    fun formatCurrencySymbol(amount: Double): String {
        return economy?.format(amount) ?: "$amount"
    }
}
