package io.github.itshaithamn.infection.comands;

import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InfectedCommand implements CommandExecutor, ChatRenderer {


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        Player player = (Player) commandSender;

        if (!(commandSender instanceof Player)){
            Component playerName = Component.text(player.getName());
            Component message = Component.text("You must be a player to use this command");
            Audience audience = Audience.audience(player);
            render(player, playerName, message, audience);

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
}
