package io.github.itshaithamn.infection.comands;

import io.github.itshaithamn.infection.Initializer;
import io.github.itshaithamn.infection.TeamManager;
import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InfectedCommand implements CommandExecutor, ChatRenderer {

    public TeamManager teamManager;

    public InfectedCommand(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

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

        teamManager = new Initializer();
        teamManager.createTeams();
        teamManager.addPlayerInfected(player);
        player.sendMessage(Component.text("You are now on the infected team."));
        return true;
    }

    @Override
    public @NotNull Component render(@NotNull Player source, Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
        return sourceDisplayName
                .append(Component.text(": "))
                .append(message);
    }
}
