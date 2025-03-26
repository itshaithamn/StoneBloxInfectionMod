package io.github.itshaithamn.infection.teammanager;

import io.github.itshaithamn.infection.Main;
import org.bukkit.configuration.file.FileConfiguration;


import java.util.UUID;

public class ConfigSave implements ConfigSaveInterface{
    private FileConfiguration config;

    private final UUID playerId;
    private String teamName;

    public ConfigSave(UUID playerId, String teamName) {
        this.playerId = playerId;
        this.teamName = teamName;
        this.config = Main.getPlayerTeamStorageConfig();
    }

    @Override
    public void save() {
        config.set("players." + this.playerId + ".team", this.teamName);
        Main.savePlayerTeamStorageConfig(config);
    }

    public FileConfiguration getConfig() {
        return this.config;
    }
}
