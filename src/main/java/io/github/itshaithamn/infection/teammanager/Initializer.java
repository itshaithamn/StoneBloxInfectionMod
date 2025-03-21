package io.github.itshaithamn.infection.teammanager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Set;

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
        this.infected = getOrCreateTeam("infected");
        this.survivor = getOrCreateTeam("survivor");
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

    public void addPlayertoTeam(String teamName, String playerName) {
        Team team = this.scoreboard.getTeam(teamName);
        if (team == null) {
            Bukkit.getLogger().warning("Team '" + teamName + "' does not exist.");
            return;
        }
        if (team.hasEntry(playerName)) {
            Bukkit.getLogger().info("Player '" + playerName + "' is already on team '" + teamName + "'.");
            return;
        }
        team.addEntry(playerName);
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            Bukkit.getLogger().info("Player '" + playerName + " was not found");
            return;
        }
        Bukkit.getLogger().info("Added player '" + playerName + "' to team '" + teamName + "'.");
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

    public Set<String> getScoreboardPlayers(String teamName) {
        Team team = null;
        if (teamName.equals("infected")) {
            team = this.infected;
        } else if (teamName.equals("survivor")) {
            team = this.survivor;
        }

//        Must check if getEntries is not null by checking the team
        return team.getEntries();
    }
}