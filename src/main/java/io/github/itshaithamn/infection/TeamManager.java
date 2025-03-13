package io.github.itshaithamn.infection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TeamManager {
    private Team infected;
    private Team survivors;
    private Scoreboard scoreboard;

    public void createTeams() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.infected = scoreboard.registerNewTeam("infected");
        this.survivors = scoreboard.registerNewTeam("survivors");
    }

    public void addPlayer(Player player, Team teamChoice) {
        teamChoice.addEntry(player.getName());
    }

    public void removePlayer(Player player, Team teamChoice) {
        teamChoice.addEntry(player.getName());
    }

}
