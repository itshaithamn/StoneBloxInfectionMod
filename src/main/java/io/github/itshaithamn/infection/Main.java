package io.github.itshaithamn.infection;

import io.github.itshaithamn.infection.comands.InfectedCommand;
import io.github.itshaithamn.infection.teammanager.ConfigSave;
import io.github.itshaithamn.infection.teammanager.ConfigSaveInterface;
import io.github.itshaithamn.infection.teammanager.Listeners;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Main extends JavaPlugin implements Listener {
    private static File playerTeamStorageFile = new File("./plugins/InfectionModConfigFiles/playerInfectionDataFile.yml");
    private static FileConfiguration playerTeamStorageConfig = YamlConfiguration.loadConfiguration(playerTeamStorageFile);
    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    public Main(){
        instance = this;
    }

    @Override
    public void onEnable() {
        if(!playerTeamStorageFile.exists()) {
            saveResource("playerInfectionDataFile.yml", false);
        }

//        ConfigurationSerialization.registerClass(PlayerData.class);
//        saveDefaultConfig();
        playerTeamStorageConfig = getConfig();
        saveConfig();

        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {

            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()){
                    Listeners.TeamCheckListener(player);
                }
            }
        }, 0, 1L);

        Objects.requireNonNull(getCommand("infected")).setExecutor(new InfectedCommand());
        getServer().getPluginManager().registerEvents(new Listeners(), this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        Bukkit.getPluginManager().disablePlugin(this);
    }

    //Getters and Setters for playerTeamStorageConfig
    public static FileConfiguration getPlayerTeamStorageConfig () {
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