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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class InfectedCommand implements CommandExecutor, ChatRenderer {

    public TeamManager teamManager;

    public InfectedCommand() {
        this.teamManager = teamManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        int infectedCommandCount = 0;
//      The infectedCommandCount ensures that the /infected command is not run 500 million times unless it has arguements
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

        if (args.length == 0) {
            Bukkit.broadcast(Component.text("§c§lYou are among infected. . . ."));
        }

        teamManager.addPlayerInfected(player);
        player.sendMessage(Component.text("You are now on the infected team."));
        player.addPotionEffect((new PotionEffect(PotionEffectType.ABSORPTION, 200, 10)));
        return true;
    }

    @Override
    public @NotNull Component render(@NotNull Player source, Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
        return sourceDisplayName
                .append(Component.text(": "))
                .append(message);
    }
}
