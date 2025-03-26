package io.github.itshaithamn.infection;

import io.github.itshaithamn.infection.comands.InfectedCommand;
import io.github.itshaithamn.infection.teammanager.ConfigSave;
import io.github.itshaithamn.infection.teammanager.ConfigSaveInterface;
import io.github.itshaithamn.infection.teammanager.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin implements Listener {
    private static File playerTeamStorageFile = new File("./plugins/InfectionModConfigFiles/playerInfectionDataFile.yml");
    private static FileConfiguration playerTeamStorageConfig = YamlConfiguration.loadConfiguration(playerTeamStorageFile);
    private static ConfigSaveInterface ConfigSave;

    @Override
    public void onEnable() {
        if(!playerTeamStorageFile.exists()) {
            saveResource("playerInfectionDataFile.yml", false);
        }

//        ConfigurationSerialization.registerClass(PlayerData.class);
//        saveDefaultConfig();
        playerTeamStorageConfig = getConfig();
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

//       if player dne then execute new Player send message else do nothing but save config
//        PlayerData playerData = new PlayerData(player.getUniqueId(), "survivor");
//        playerTeamStorageConfig.set("infectionplayers." + player.getUniqueId(),playerData);
//        player.sendMessage(Component.text("You are on the " + playerData + "Team!"));
//        saveConfig();
        ConfigSave = new ConfigSave(player.getUniqueId(), "survivor");
        ConfigSave.save();
    }

    //Getters and Setters for playerTeamStorageConfig
    public static FileConfiguration getPlayerTeamStorageConfig() {
        return playerTeamStorageConfig;
    }

    public static File getPlayerTeamStorageFile() {
        return playerTeamStorageFile;
    }

    public static void savePlayerTeamStorageConfig(FileConfiguration playerTeamStorageConfig) {
        try {
            playerTeamStorageConfig.save(playerTeamStorageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}