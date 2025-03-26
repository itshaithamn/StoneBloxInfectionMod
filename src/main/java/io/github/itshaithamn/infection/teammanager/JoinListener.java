package io.github.itshaithamn.infection.teammanager;

import io.github.itshaithamn.infection.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    private Main main;

    public JoinListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
//Control Player Join Events here in the future?
    }
}
