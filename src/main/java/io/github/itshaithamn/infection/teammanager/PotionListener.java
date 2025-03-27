package io.github.itshaithamn.infection.teammanager;


import io.github.itshaithamn.infection.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.plugin.java.JavaPlugin;

public class PotionListener implements Listener {
    private static FileConfiguration playerTeamStorageConfig = Main.getPlayerTeamStorageConfig();

//    @EventHandler
//    public void onPlayerDeath(PlayerDeathEvent event) {
//        Player player = event.getPlayer();
//        player.sendMessage(Component.text("[INFO]§r §1§lYou are on team: " ));
//        String uuid = player.getUniqueId().toString();
//        String path = "players." + uuid + ".team";
//        String team = this.playerTeamStorageConfig.getString(path);
//    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), task -> {
            Player player = event.getPlayer();
            player.sendMessage(Component.text("[INFO]§r §1§lYou are on team: " ));
            String uuid = player.getUniqueId().toString();
            String path = "players." + uuid + ".team";
            String team = playerTeamStorageConfig.getString(path);
            switch (team) {
                case "survivor":
                    player.sendMessage(Component.text("§2 survivor"));
                    player.addPotionEffect(PotionEffectType.SPEED.createEffect(999999999, 10));
                    player.removePotionEffect(PotionEffectType.HUNGER);
                    break;
                case "infected":
                    player.sendMessage(Component.text("§4 infected"));
                    player.addPotionEffect(PotionEffectType.HUNGER.createEffect(999999999, 10));
                    player.removePotionEffect(PotionEffectType.SPEED);
                    break;
                case null:
                    player.sendMessage(Component.text("Weird ass error, you should be in a team...."));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + team);
            }
        }, 5L);

    }
}
