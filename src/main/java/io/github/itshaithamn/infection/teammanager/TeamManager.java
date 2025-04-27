package io.github.itshaithamn.infection.teammanager;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.Set;

public interface TeamManager {
    void addPlayertoTeam(Player player, String teamName);
    Set<String> getScoreboardPlayers(String teamName);
    Team getSurvivorsTeam();
    Team getInfectedTeam();
    boolean verifyPlayer(String player);
}

