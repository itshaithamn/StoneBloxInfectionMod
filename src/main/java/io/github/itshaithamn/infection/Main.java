package io.github.itshaithamn.infection;

import io.github.itshaithamn.infection.comands.InfectedCommand;
import io.github.itshaithamn.infection.teammanager.ConfigSave;
import io.github.itshaithamn.infection.teammanager.ConfigSaveInterface;
import net.kyori.adventure.text.Component;
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
import java.util.UUID;

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
        String uuid = player.getUniqueId().toString();
//        Maybe add a if player.getString(path) == null then add player to path else send info message

//       if player new then execute new Player send message else do nothing but save config
        if(!playerTeamStorageConfig.contains("players." + player.getUniqueId())) {
            ConfigSave = new ConfigSave(player.getUniqueId(), "survivor");
            ConfigSave.save();
        } else {
            player.sendMessage(Component.text("[INFO]§r §1§lYou are on team: " ));
            String path = "players." + uuid + ".team";
            String team = playerTeamStorageConfig.getString(path);
            switch (team) {
                case "survivor":
                    player.sendMessage(Component.text("§asurvivor"));
                    break;
                case "infected":
                    player.sendMessage(Component.text("§minfected"));
                    break;
                case null:
                    player.sendMessage(Component.text("Weird ass error, you should be in a team...."));
                    break;
            }

        }
//        ConfigSave = new ConfigSave(player.getUniqueId(), "survivor");
//        ConfigSave.save();
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