package io.github.itshaithamn.infection;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class PluginEventHandler implements Listener {
    Player player;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
        player = playerJoinEvent.getPlayer();
    }

}