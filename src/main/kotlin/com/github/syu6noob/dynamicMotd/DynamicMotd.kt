package com.github.syu6noob.dynamicMotd

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.ServerListPingEvent
import org.bukkit.plugin.java.JavaPlugin

class DynamicMotd : JavaPlugin(), Listener {

    private var startTime: Long = 0

    override fun onEnable() {
        saveDefaultConfig()
        server.pluginManager.registerEvents(this, this)
        startTime = System.currentTimeMillis() // 起動時刻を記録
    }

    @EventHandler
    fun onServerListPing(event: ServerListPingEvent) {
        val world = server.worlds[0]
        val ticks = world.time
        val hour = ((ticks / 1000 + 6) % 24).toInt()
        val minute = ((ticks % 1000) * 60 / 1000).toInt()

        val players = server.onlinePlayers.size
        val maxPlayers = server.maxPlayers

        // 稼働時間を日・時・分で計算
        val uptimeMillis = System.currentTimeMillis() - startTime
        val uptimeSeconds = uptimeMillis / 1000

        val uptimeDays = uptimeSeconds / 86400
        val uptimeHours = (uptimeSeconds % 86400) / 3600
        val uptimeMinutes = (uptimeSeconds % 3600) / 60
        val uptimeSecs = uptimeSeconds % 60

        val template = config.getString("motd-template")
            ?: "A Minecraft Server"

        // プレースホルダを置換
        val motd = template
            .replace("%hour%", String.format("%02d", hour))
            .replace("%minute%", String.format("%02d", minute))
            .replace("%players%", players.toString())
            .replace("%maxPlayers%", maxPlayers.toString())
            .replace("%uptimeDays%", String.format("%02d", uptimeDays))
            .replace("%uptimeHours%", String.format("%02d", uptimeHours))
            .replace("%uptimeMinutes%", String.format("%02d", uptimeMinutes))
            .replace("%uptimeSeconds%", String.format("%02d", uptimeSecs))

        event.motd = motd
    }
}