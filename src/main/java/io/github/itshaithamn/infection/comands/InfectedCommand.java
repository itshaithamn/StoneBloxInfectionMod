package io.github.itshaithamn.infection.comands;

import io.github.itshaithamn.infection.teammanager.Initializer;
import io.github.itshaithamn.infection.teammanager.TeamManager;
import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class InfectedCommand implements CommandExecutor, ChatRenderer {
    private static TeamManager teamManager = new Initializer();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) sender;

        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("Only players can use this command"));
            return true;
        }

        if (args.length == 0) {
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(Component.text("§c§l[ERROR]§r: Usage -> /infection team <'survivor' or 'infected'> <player> \n-> /infection rmteam <team> <player>"));
        }

        switch (args[0].toLowerCase()) {
            case "rmteam":
                if(player.hasPermission("infection.command.rmteam")) {
                    handleTeamRemovalCommand(player, args);
                }
                player.sendMessage(Component.text("§c§lNo permission to use this command"));
                return true;
            case "team":
                if(player.hasPermission("infection.command.team")) {
                    handleTeamCommand(player, args);
                } else {
                    player.sendMessage(Component.text("§c§lNo permission to use this command"));
                }
                return true;
            case "list":
                if(player.hasPermission("infection.command.list")) {
                    handleScoreboardCommand(player, args);
                } else {
                    player.sendMessage(Component.text("§c§lNo permission to use this command"));
                }
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

    private void handleTeamCommand(Player player, String[] args) {
//        You need team, then you need a player to add to that team
        if (args.length < 3) {
            player.sendMessage(Component.text("Usage: /infected team <team> <player>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            player.sendMessage(Component.text("Player " + args[2] + " is not online."));
            return;
        }

        String teamName = args[1];
        if (!teamName.equalsIgnoreCase("survivor") && !teamName.equalsIgnoreCase("infected")) {
            player.sendMessage(Component.text("Invalid team. Teams are 'infected' or 'survivor'."));
            return;
        }

        teamManager.addPlayertoTeam(target, teamName);
        target.sendMessage(Component.text("You are now part of the " + teamName + " team."));
        player.sendMessage(Component.text("Player " + target.getName() + " has been added to the " + teamName + " team."));
    }

    private void handleScoreboardCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Component.text("Usage: /infected list <team>"));
            return;
        }

        Set<String> List = teamManager.getScoreboardPlayers(args[1]);
        String playerList = List.isEmpty()
                ? "No players in this team."
                : String.join(", ", List);

        player.sendMessage(Component.text("Players: " + playerList));
    }

    //Unneccesary code, this is for removing a player entirely from the game
    private void handleTeamRemovalCommand(Player player, String[] args) {
        if (teamManager.verifyPlayer(args[3]) && args[2].equals("survivor")) {
            Team survivors = teamManager.getSurvivorsTeam();
            survivors.removeEntry(args[3]);
        } else if (teamManager.verifyPlayer(args[3]) && args[2].equals("infected")) {
            Team infected = teamManager.getInfectedTeam();
            infected.removeEntry(args[3]);
        }
    }
}