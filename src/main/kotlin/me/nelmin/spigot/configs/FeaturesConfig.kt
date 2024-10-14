package me.nelmin.spigot.configs

import me.nelmin.spigot.nControl
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class FeaturesConfig(private val plugin: nControl) : Configuration {

    private lateinit var file: File
    private lateinit var config: YamlConfiguration

    override fun get(path: String): Any? {
        return config.get(path)
    }

    override fun load() {
        file = File(plugin.dataFolder, "features.yml")

        if (!file.exists()) {
            plugin.saveResource("features.yml", false)
        }

        config = YamlConfiguration().apply {
            options().parseComments(true)
        }

        try {
            config.load(file)
        } catch (e: Exception) {
            plugin.logger.severe(e.localizedMessage)
            e.printStackTrace()
        }
    }

    override fun save() {
        try {
            config.save(file)
        } catch (e: Exception) {
            plugin.logger.severe(e.localizedMessage)
            e.printStackTrace()
        }
    }

    override fun set(path: String, value: Any?, override: Boolean) {
        if (config.get(path) != null && override) {
            config.set(path, value)
        }

        if (config.get(path) == null) {
            config.set(path, value)
        }

        save()
    }
}
