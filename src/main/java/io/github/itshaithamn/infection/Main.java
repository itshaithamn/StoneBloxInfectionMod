package io.github.itshaithamn.infection;

import io.github.itshaithamn.infection.comands.InfectedCommand;
import io.github.itshaithamn.infection.teammanager.ConfigSave;
import io.github.itshaithamn.infection.teammanager.ConfigSaveInterface;
import io.github.itshaithamn.infection.teammanager.PotionListener;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
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

        Objects.requireNonNull(getCommand("infected")).setExecutor(new InfectedCommand());
        getServer().getPluginManager().registerEvents(new PotionListener(), this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        Bukkit.getPluginManager().disablePlugin(this);
    }


    //This method will eventually need to be severly altered to prevent players from rejoining to gain speed buff, bug
    // Actually, players will still spawn with their respective teams, however, this buff will stil initialze everytime
//    They join making it a unecessary resource, however, it might be okay in terms of server pop, unimportant, fixable bug
    @EventHandler
    public void onPlayerJoinListener(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
//        Moved ConfigSaveInterface from a global to local variable
//        Maybe add a if player.getString(path) == null then add player to path else send info message

//       if player new then execute new Player send message else do nothing but save config
        if(!playerTeamStorageConfig.contains("players." + player.getUniqueId())) {
            ConfigSaveInterface ConfigSave = new ConfigSave(player.getUniqueId(), "survivor");
            ConfigSave.save();
            player.addPotionEffect(PotionEffectType.SPEED.createEffect(999999999, 10));

            return;
        }

        player.sendMessage(Component.text("[INFO]§r §1§lYou are on team: " ));
        String path = "players." + uuid + ".team";
        String team = playerTeamStorageConfig.getString(path);
        switch (team) {
            case "survivor":
                player.sendMessage(Component.text("§2 survivor"));
                player.addPotionEffect(PotionEffectType.SPEED.createEffect(999999999, 10));
                break;
            case "infected":
                player.sendMessage(Component.text("§4 infected"));
                player.addPotionEffect(PotionEffectType.HUNGER.createEffect(999999999, 10));
                break;
            case null:
                player.sendMessage(Component.text("Weird ass error, you should be in a team...."));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + team);
        }
//        ConfigSave = new ConfigSave(player.getUniqueId(), "survivor");
//        ConfigSave.save();
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