package io.github.itshaithamn.infection;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public interface TeamManager {
    void addPlayertoTeam(String teamName, String player);
    void removePlayerInfected(Player player);
    void removePlayerSurvivors(Player player);
    String getScoreboardPlayers();
    Scoreboard getScoreboard();
    Team getSurvivorsTeam();
    Team getInfectedTeam();
    boolean verifyPlayer(String player);
}

