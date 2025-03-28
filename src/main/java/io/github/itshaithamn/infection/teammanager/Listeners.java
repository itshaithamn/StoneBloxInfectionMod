package io.github.itshaithamn.infection.teammanager;


import io.github.itshaithamn.infection.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffectType;

public class Listeners implements Listener {
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
                    player.sendMessage(Component.text("§2 >>survivor"));
                    player.removePotionEffect(PotionEffectType.SPEED);
                    break;
                case "infected":
                    player.sendMessage(Component.text("§4 infected"));
                    player.addPotionEffect(PotionEffectType.SPEED.createEffect(999999, 3));
                    player.addPotionEffect(PotionEffectType.WEAKNESS.createEffect(999999, 1));
                    break;
                case null:
                    player.sendMessage(Component.text("Weird ass error, you should be in a team...."));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + team);
            }
        }, 5L);
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        Player reciever;
        Player attacker;
        Entity affected = event.getEntity();
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            reciever = (Player) event.getDamager();
            attacker = (Player) event.getEntity();
        } else {
            attacker = (Player) event.getDamager();
            attacker.sendMessage(Component.text("[INFO] Working Debugged, no Bugs, I'm a shitty programmer" + affected.getName()));
            return;
        }

        String uuidAttacker = attacker.getUniqueId().toString();
        String pathAttacker = "players." + uuidAttacker + ".team";
        String teamAttacker = playerTeamStorageConfig.getString(pathAttacker);

        double damagebyAttacker = reciever.getLastDamage();

        if (teamAttacker != null && teamAttacker.equals("infected") && damagebyAttacker >= 1) {
            reciever.sendMessage(Component.text("You are infected!"));
            playerTeamStorageConfig.set(reciever.getUniqueId().toString(), "infected");
            ConfigSaveInterface ConfigSave = new ConfigSave(reciever.getUniqueId(), "infected");
            ConfigSave.save();
            attacker.sendMessage(Component.text("You have infected:" + reciever.getName()));
        }

    }
}
