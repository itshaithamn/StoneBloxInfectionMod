package io.github.itshaithamn.infection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Initializer implements TeamManager {
    private Scoreboard scoreboard;
    private Team infected;
    private Team survivor;

    public Initializer() {
        Bukkit.getLogger().info("§aInitializing teams...");
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        loadTeams();
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    private void loadTeams() {
        infected = getOrCreateTeam("infected");
        survivor = getOrCreateTeam("survivors");
    }

    private Team getOrCreateTeam(String teamName) {
        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
        }
        return team;
    }

    public Team getInfectedTeam() {
        return infected;
    }

    public Team getSurvivorsTeam() {
        return survivor;
    }

    public void addPlayertoTeam(String teamName, String player) {
        Team team = scoreboard.getTeam(teamName);
        team.addEntry(player);
    }

    public boolean verifyPlayer(String player) {
        Player target = Bukkit.getPlayer(player);
        return target != null;
    }

    public void removePlayerInfected(Player player) {
        this.infected.removeEntry(player.getName());
    }

    public void removePlayerSurvivors(Player player) {
        this.survivor.removeEntry(player.getName());
    }

    public String getScoreboardPlayers() {return this.scoreboard.getEntries().toString();}
}
