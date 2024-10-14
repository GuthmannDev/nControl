package me.nelmin.spigot

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import me.clip.placeholderapi.PlaceholderAPI
import me.nelmin.spigot.configs.FeaturesConfig
import me.nelmin.spigot.configs.MainConfig
import me.nelmin.spigot.configs.MessageConfig
import me.nelmin.spigot.configs.UserData
import me.nelmin.spigot.events.AsyncPlayerChatListener
import me.nelmin.spigot.events.PlayerJoinEvent
import me.nelmin.spigot.events.PlayerQuitEvent
import me.nelmin.spigot.features.economy.Economy
import me.nelmin.spigot.other.VaultHook
import me.nelmin.spigot.other.withName
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * What is nControl?
 * nControl is going to be a Server System like EssentialsX, ServerSystem (RealEntity303)
 *
 * @author NelminDev
 * @since 1.0.0
 */
@Suppress("ClassName")
class nControl : JavaPlugin() {

    companion object {
        lateinit var instance: nControl
    }

    lateinit var vault: VaultHook
    lateinit var config: MainConfig
    lateinit var features: FeaturesConfig
    lateinit var messages: MessageConfig

    val loadedUserData: MutableSet<UserData> = ConcurrentHashMap.newKeySet()

    override fun onEnable() {
        instance = this
        vault = VaultHook(this)

        config = MainConfig(this).apply { load() }
        features = FeaturesConfig(this).apply { load() }
        messages = MessageConfig(this).apply { load() }

        UserData.loadAll()

        if (features.get("economy") as Boolean) Economy.register()
        if (features.get("chat") as Boolean) server.pluginManager.registerEvents(AsyncPlayerChatListener(this), this)

        server.pluginManager.apply {
            registerEvents(PlayerJoinEvent(this@nControl), this@nControl)
            registerEvents(PlayerQuitEvent(this@nControl), this@nControl)
        }

        ProtocolLibrary.getProtocolManager()
            .addPacketListener(object : PacketAdapter(this, PacketType.Play.Server.PLAYER_INFO) {
                override fun onPacketSending(event: PacketEvent) {
                    val format = config.get("format.overHead") as String
                    val updatedDataList = event.packet.playerInfoDataLists.read(0).map { data ->
                        val player = Bukkit.getPlayer(data.profile.uuid)
                        data.withName(player?.let { PlaceholderAPI.setPlaceholders(it, format) } ?: data.profile.name)
                    }
                    event.packet.playerInfoDataLists.write(0, updatedDataList)
                }
            })

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, ::saveUserData, 0L, 5 * 60 * 20L)
    }

    override fun onDisable() {
        config.save()
        features.save()
        messages.save()
        saveUserData()
    }

    private fun saveUserData() {
        loadedUserData.forEach(UserData::save)
    }

    fun saveUserData(uuid: UUID) {
        loadedUserData.find { it.uuid == uuid }?.save()
    }
}
