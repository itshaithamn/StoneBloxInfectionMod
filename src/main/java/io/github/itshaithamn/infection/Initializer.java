package io.github.itshaithamn.infection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Initializer implements TeamManager {
    private Scoreboard scoreboard;
    private Team infected;
    private Team survivors;

    @Override
    public Initializer createTeams() {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.infected = scoreboard.registerNewTeam("infected");
        this.survivors = scoreboard.registerNewTeam("survivors");

        return this;
    }

    public void addPlayerInfected(Player player) {
        infected.addEntry(player.getName());
    }

    public void removePlayerInfected(Player player) {
        infected.removeEntry(player.getName());
    }

    public void addPlayerSurvivors(Player player) {
        survivors.addEntry(player.getName());
    }

    public void removePlayerSurvivors(Player player) {
        survivors.removeEntry(player.getName());
    }

    public void getScoreboardPlayers() {scoreboard.getEntries();}
}
