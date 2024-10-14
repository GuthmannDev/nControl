package me.nelmin.spigot.features.economy

import me.nelmin.spigot.nControl
import me.nelmin.spigot.other.TextBuilder
import net.milkbowl.vault.economy.AbstractEconomy
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.Bukkit
import org.bukkit.plugin.ServicePriority
import kotlin.math.abs

class Economy : AbstractEconomy() {

    private val plugin = nControl.instance

    companion object {
        fun register() {
            Bukkit.getServicesManager().register(
                Economy::class.java, Economy(), nControl.instance, ServicePriority.Normal
            )
        }
    }

    override fun bankBalance(p0: String?) = notImplementedResponse()

    override fun bankDeposit(p0: String?, p1: Double) = notImplementedResponse()

    override fun bankHas(p0: String?, p1: Double) = notImplementedResponse()

    override fun bankWithdraw(p0: String?, p1: Double) = notImplementedResponse()

    @Deprecated("Deprecated in Java")
    override fun createBank(p0: String?, p1: String?) = notImplementedResponse()

    @Deprecated("Deprecated in Java", ReplaceWith("false"))
    override fun createPlayerAccount(p0: String?) = false

    @Deprecated("Deprecated in Java", ReplaceWith("false"))
    override fun createPlayerAccount(p0: String?, p1: String?) = false

    override fun currencyNamePlural() = plugin.config.get("economy.currency.plural") as String

    override fun currencyNameSingular() = plugin.config.get("economy.currency.singular") as String

    @Deprecated("Deprecated in Java")
    override fun depositPlayer(p0: String?, p1: Double) = depositByName(p0, p1)

    @Deprecated("Deprecated in Java", ReplaceWith("this.depositByName(p0, p2)"))
    override fun depositPlayer(p0: String?, p1: String?, p2: Double) = depositByName(p0, p2)

    override fun deleteBank(p0: String?) = notImplementedResponse()

    override fun fractionalDigits() = 2

    override fun format(amount: Double) = String.format("%.2f", amount)

    override fun getBanks(): MutableList<String> = mutableListOf()

    override fun getName() = "nControl Economy"

    @Deprecated("Deprecated in Java", ReplaceWith("this.getBalanceByName(p0)"))
    override fun getBalance(p0: String?) = getBalanceByName(p0)

    @Deprecated("Deprecated in Java", ReplaceWith("this.getBalanceByName(p0)"))
    override fun getBalance(p0: String?, p1: String?) = getBalanceByName(p0)

    override fun hasBankSupport() = false

    @Deprecated("Deprecated in Java", ReplaceWith("this.hasAccountByName(p0)"))
    override fun hasAccount(p0: String) = hasAccountByName(p0)

    @Deprecated("Deprecated in Java", ReplaceWith("this.hasAccountByName(p0)"))
    override fun hasAccount(p0: String, p1: String?) = hasAccountByName(p0)

    @Deprecated("Deprecated in Java", ReplaceWith("this.hasByName(p0, p1)"))
    override fun has(p0: String?, p1: Double) = hasByName(p0, p1)

    @Deprecated("Deprecated in Java", ReplaceWith("this.hasByName(p0, p2)"))
    override fun has(p0: String?, p1: String?, p2: Double) = hasByName(p0, p2)

    private fun createFailureResponse(playerName: String?, messageKey: String): EconomyResponse {
        return EconomyResponse(
            0.0,
            getBalanceByName(playerName),
            EconomyResponse.ResponseType.FAILURE,
            TextBuilder(messageKey).getColorized()
        )
    }

    private fun createSuccessResponse(playerName: String?, amount: Double): EconomyResponse {
        return EconomyResponse(
            amount,
            getBalanceByName(playerName),
            EconomyResponse.ResponseType.SUCCESS,
            ""
        )
    }

    private fun depositByName(playerName: String?, amount: Double): EconomyResponse {
        if (amount < 0) return withdrawByName(playerName, abs(amount))

        val user = plugin.loadedUserData.find { it.userName == playerName }
            ?: return createFailureResponse(null, "error.no.player_found")

        if (user.balance + amount >= Double.MAX_VALUE) {
            user.balance = Double.MAX_VALUE
            return EconomyResponse(
                amount,
                getBalanceByName(playerName),
                EconomyResponse.ResponseType.SUCCESS,
                TextBuilder("error.economy.deposit.too_much").getColorized()
            )
        }

        user.balance += amount
        return createSuccessResponse(playerName, amount)
    }

    fun getBalanceByName(playerName: String?): Double {
        return plugin.loadedUserData.find { it.userName == playerName }?.balance ?: 0.0
    }

    private fun hasAccountByName(playerName: String?) = plugin.loadedUserData.any { it.userName == playerName }

    private fun hasByName(playerName: String?, amount: Double): Boolean {
        return (plugin.loadedUserData.find { it.userName == playerName }?.balance ?: 0.0) >= amount
    }

    private fun notImplementedResponse(): EconomyResponse {
        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented!")
    }

    @Deprecated("Deprecated in Java", ReplaceWith("this.withdrawByName(p0, p1)"))
    override fun withdrawPlayer(p0: String?, p1: Double) = withdrawByName(p0, p1)

    @Deprecated("Deprecated in Java", ReplaceWith("this.withdrawPlayer(p0, p2)"))
    override fun withdrawPlayer(p0: String?, p1: String?, p2: Double) = withdrawByName(p0, p2)

    private fun withdrawByName(playerName: String?, amount: Double): EconomyResponse {
        if (amount < 0) {
            return createFailureResponse(playerName, "error.economy.withdraw.negative_funds")
        }

        if (!hasByName(playerName, amount)) {
            return createFailureResponse(playerName, "error.economy.withdraw.insufficient_funds")
        }

        val user = plugin.loadedUserData.find { it.userName == playerName }
            ?: return createFailureResponse(null, "error.no.player_found")

        user.balance -= amount
        return createSuccessResponse(playerName, amount)
    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("EconomyResponse(0.0, this.getBalance(p0), EconomyResponse.ResponseType.NOT_IMPLEMENTED, \"Not implemented!\")")
    )
    override fun isBankMember(p0: String?, p1: String?) = notImplementedResponse()

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("EconomyResponse(0.0, this.getBalance(p0), EconomyResponse.ResponseType.NOT_IMPLEMENTED, \"Not implemented!\")")
    )
    override fun isBankOwner(p0: String?, p1: String?) = notImplementedResponse()

    override fun isEnabled() = true
}
