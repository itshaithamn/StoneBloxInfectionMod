package io.github.itshaithamn.infection.comands;

import io.github.itshaithamn.infection.Initializer;
import io.github.itshaithamn.infection.TeamManager;
import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

public class InfectedCommand implements CommandExecutor, ChatRenderer {
    private static TeamManager teamManager = new Initializer();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) sender;

        if (!(sender instanceof Player)){
            Component playerName = Component.text(player.getName());
            Component message = Component.text("You must be a player to use this command");
            Audience audience = Audience.audience(player);
            render(player, playerName, message, audience);
            return true;
        }

        if (args.length == 0) {
            handleInfectedCommand(player);
            return true;
        }

        if(args.length < 3) {
            player.sendMessage(Component.text("§c§l[ERROR]§r: Usage -> /infection team <'survivor' or 'infected'> <player>"));
        }

        switch (args[0].toLowerCase()) {
            case "rmteam":
                handleTeamRemovalCommand(player, args);
                return true;
            case "team":
                handleTeamCommand(player, args);
                return true;
            case "list":
                handleScoreboardCommand(player);
                return true;
        }

        return true;
    }

    @Override
    public @NotNull Component render(@NotNull Player source, Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
        return sourceDisplayName
                .append(Component.text(": "))
                .append(message);
    }

    private void handleInfectedCommand(Player player) {
        Bukkit.broadcast(Component.text("§c§lThere are infected among you. . . ."));
    }

    private void handleTeamCommand(Player player, String[] args) {
//        You need team, then you need a player to add to that team
        if(teamManager.verifyPlayer(args[2]) && (args[1].equals("survivor") || args[1].equals("infected"))) {
            teamManager.addPlayertoTeam(args[1], args[2]);
            Player target = Bukkit.getPlayer(args[2]);
            target.sendMessage(Component.text("You have been infected."));
        } else {
            player.sendMessage(Component.text("Player " + args[2] + " does not exist or Team " + args[1] + "does not exist.\nTeams are infected or survivor."));
        }
    }

    private void handleScoreboardCommand(Player player) {
        player.sendMessage(Component.text(teamManager.getScoreboardPlayers()));
    }

    private void handleTeamRemovalCommand(Player player, String[] args) {

    }
}
