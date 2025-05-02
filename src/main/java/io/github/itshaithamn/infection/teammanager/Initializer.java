package io.github.itshaithamn.infection.teammanager;

import io.github.itshaithamn.infection.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Set;

public class Initializer implements TeamManager {
    private Scoreboard scoreboard;
    private Team infected;
    private Team survivor;

    static Main main = Main.getInstance();
    private static FileConfiguration config = main.getPlayerTeamStorageConfig();

    public Initializer() {
        this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        this.infected  = getOrCreateTeam("infected");
        this.survivor  = getOrCreateTeam("survivor");
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

    @Override
    public void addPlayertoTeam(Player player, String teamName) {
        Team t = scoreboard.getTeam(teamName);
        if (t == null) {
            Bukkit.getLogger().warning("No such team: "+teamName);
            return;
        }
        // assign the player to the scoreboard
        // only add if they’re not already in it
        if (!t.hasEntry(player.getName())) {
            t.addEntry(player.getName());
        }
        // persist to config
        FileConfiguration cfg = Main.getPlayerTeamStorageConfig();
        cfg.set("players."+player.getUniqueId()+".team", teamName);
        Main.savePlayerTeamStorageConfig(cfg);
        Bukkit.getLogger().info("Added " + player.getName() + " to " + teamName);
    }

    public boolean verifyPlayer(String player) {
        Player target = Bukkit.getPlayer(player);
        return target != null;
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

    public void save(String playerId, String teamName) {
        config.set("players." + playerId + ".team", teamName);
        Main.savePlayerTeamStorageConfig(config);
    }
}