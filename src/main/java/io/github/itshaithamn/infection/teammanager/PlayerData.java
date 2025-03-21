package io.github.itshaithamn.infection.teammanager;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData implements ConfigurationSerializable{

    private UUID playerId;
    private String teamName;

    public PlayerData(UUID playerId, String teamName) {
        this.playerId = playerId;
        this.teamName = teamName;
    }

    public PlayerData(Map<String, Object> data) {
        this.playerId = (UUID) data.get("playerId");
        this.teamName = (String) data.get("teamName");
    }


    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("playerId", playerId.toString());
        map.put("teamName", teamName);
        return map;
    }

    public static PlayerData deserialize(Map<String, Object> data) {
        return new PlayerData(data);
    }

    public @NotNull String getPlayerId() {
        return playerId.toString();
    }

    public String getTeamName() {
        return teamName;
    }

}
