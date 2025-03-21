package io.github.itshaithamn.infection;

import io.github.itshaithamn.infection.comands.InfectedCommand;
import io.github.itshaithamn.infection.teammanager.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    FileConfiguration config;

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(PlayerData.class);
        saveDefaultConfig();
        this.config = getConfig();
        saveConfig();

        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("infected").setExecutor(new InfectedCommand());
    }

    @Override
    public void onDisable() {
        Bukkit.getPluginManager().disablePlugin(this);
    }

    @EventHandler
    public void onPlayerJoinListener(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = new PlayerData(player.getUniqueId(), "survivors");
        config.set("infectionplayers." + player.getUniqueId(),playerData);
        saveConfig();
    }
}