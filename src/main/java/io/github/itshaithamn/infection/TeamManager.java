package io.github.itshaithamn.infection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.naming.InitialContext;

public interface TeamManager {
    Initializer createTeams();
    void addPlayerInfected(Player player);
    void removePlayerInfected(Player player);
    void addPlayerSurvivors(Player player);
    void removePlayerSurvivors(Player player);
}

