package me.nelmin.spigot.other

import com.comphenix.protocol.wrappers.PlayerInfoData

fun PlayerInfoData.withName(newName: String): PlayerInfoData =
    PlayerInfoData(profile.withName(newName), latency, gameMode, displayName)
