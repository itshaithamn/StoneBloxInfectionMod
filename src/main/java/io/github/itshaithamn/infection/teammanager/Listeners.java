package io.github.itshaithamn.infection.teammanager;


import io.github.itshaithamn.infection.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
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
            assert team != null;
            player.sendMessage(Component.text(team));
            TeamCheckListener(player);
        }, 5L);
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        Player reciever;
        Player attacker;
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            reciever = (Player) event.getEntity();
            attacker = (Player) event.getDamager();
        } else {
            return;
        }

        String uuidAttacker = attacker.getUniqueId().toString();
        String pathAttacker = "players." + uuidAttacker + ".team";
        String teamAttacker = playerTeamStorageConfig.getString(pathAttacker);

        double damagebyAttacker = reciever.getLastDamage();

        if (teamAttacker != null && teamAttacker.equals("infected") && damagebyAttacker >= 20) {
            reciever.sendMessage(Component.text("§1§lYou are infected!"));
            playerTeamStorageConfig.set(reciever.getUniqueId().toString(), "infected");
            ConfigSaveInterface ConfigSave = new ConfigSave(reciever.getUniqueId(), "infected");
            ConfigSave.save();
            attacker.sendMessage(Component.text("§1§lYou have infected:§r " + reciever.getName()));
        }
    }

    @EventHandler
    public void onPlayerJoinListener(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
//        Moved ConfigSaveInterface from a global to local variable
//        Maybe add a if player.getString(path) == null then add player to path else send info message

//       if player new then execute new Player send message else do nothing but save config
        if(!playerTeamStorageConfig.contains("players." + player.getUniqueId())) {
            ConfigSaveInterface ConfigSave = new ConfigSave(player.getUniqueId(), "survivor");
            ConfigSave.save();
            return;
        }

        player.sendMessage(Component.text("[INFO]§r §1§lYou are on team: " ));
        String path = "players." + uuid + ".team";
        String team = playerTeamStorageConfig.getString(path);
        player.sendMessage(Component.text(team));

        TeamCheckListener(player);
    }

//    Can probably dumb down all the issues I've been having into this right here creating one listener for everything that just always runs
    public static void TeamCheckListener(Player player){
        String uuid = player.getUniqueId().toString();
//        Moved ConfigSaveInterface from a global to local variable
//        Maybe add a if player.getString(path) == null then add player to path else send info message

//       if player new then execute new Player send message else do nothing but save config
        if(!playerTeamStorageConfig.contains("players." + player.getUniqueId())) {
            ConfigSaveInterface ConfigSave = new ConfigSave(player.getUniqueId(), "survivor");
            ConfigSave.save();
            return;
        }

        String path = "players." + uuid + ".team";
        String team = playerTeamStorageConfig.getString(path);
        switch (team) {
            case "survivor":
//                    Might be better to add a check here instead of doing all this
                player.removePotionEffect(PotionEffectType.SPEED);
                player.removePotionEffect(PotionEffectType.STRENGTH);
                player.setMaxHealth(20);
                break;
            case "infected":
                player.addPotionEffect(PotionEffectType.SPEED.createEffect(999999, 3));
                player.addPotionEffect(PotionEffectType.STRENGTH.createEffect(999999, 2));
                player.setMaxHealth(12);
                break;
            case null:
                player.sendMessage(Component.text("Weird ass error, you should be in a team...."));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + team);
        }
    }
}
