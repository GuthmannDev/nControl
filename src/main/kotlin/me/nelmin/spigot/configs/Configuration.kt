package me.nelmin.spigot.configs

interface Configuration {

    fun get(path: String): Any?
    fun load()
    fun save()
    fun set(path: String, value: Any?, override: Boolean)
}
