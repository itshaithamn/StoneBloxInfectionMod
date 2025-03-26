package io.github.itshaithamn.infection.teammanager;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Set;

public interface TeamManager {
    void addPlayertoTeam(String teamName, String player);
    void removePlayerInfected(Player player);
    void removePlayerSurvivors(Player player);
    Set<String> getScoreboardPlayers(String teamName);
    Scoreboard getScoreboard();
    Team getSurvivorsTeam();
    Team getInfectedTeam();
    boolean verifyPlayer(String player);
}

