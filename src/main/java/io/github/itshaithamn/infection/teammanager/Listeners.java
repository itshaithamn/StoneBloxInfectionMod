package io.github.itshaithamn.infection.teammanager;


import io.github.itshaithamn.infection.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.List;


public class Listeners implements Listener {
    private static FileConfiguration config = Main.getPlayerTeamStorageConfig();
    private static TeamManager teamManager = new Initializer();
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), task -> {
            Player player = event.getPlayer();
            player.sendMessage(Component.text("[INFO]§r §1§lYou are on team: " ));
            String uuid = player.getUniqueId().toString();
            String path = "players." + uuid + ".team";
            String team = config.getString(path);
            assert team != null;
            player.sendMessage(Component.text(team));
            TeamCheckListener(player);
        }, 5L);
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        Player killed;
        Player killer;
        if(event.getEntity().getKiller() instanceof Player && event.getEntity() instanceof Player) {
            killed = event.getEntity();
            killer = event.getEntity().getKiller();
        } else {
            return;
        }

        String uuidKiller = killer.getUniqueId().toString();
        String pathKiller = "players." + uuidKiller + ".team";
        String teamKiller = config.getString(pathKiller);

        String uuidKilled = killed.getUniqueId().toString();
        String pathKilled = "players." + uuidKilled + ".team";
        String teamKilled = config.getString(pathKiller);


        if (teamKiller != null && teamKiller.equals("infected")) {
            saveKill(uuidKiller, uuidKilled);
            teamManager.addPlayertoTeam(killed, teamKilled);
            killer.sendMessage(Component.text("§1§lYou have infected:§r " + killer.getName()));
        }
    }

    @EventHandler
    public void onPlayerJoinListener(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
//        Moved ConfigSaveInterface from a global to local variable
//        Maybe add a if player.getString(path) == null then add player to path else send info message

//       if player new then execute new Player send message else do nothing but save config
        if(!config.contains("players." + player.getUniqueId())) {
            save(uuid, "survivor");
            return;
        }

        player.sendMessage(Component.text("[INFO]§r §1§lYou are on team: " ));
        String path = "players." + uuid + ".team";
        String team = config.getString(path);
        assert team != null;
        player.sendMessage(Component.text(team));
        teamManager.addPlayertoTeam(player, team);
        TeamCheckListener(player);
    }

    public static void TeamCheckListener(Player player){
        String uuid = player.getUniqueId().toString();

        String path = "players." + uuid + ".team";
        String team = config.getString(path);
        switch (team) {
            case "survivor":
//                    Might be better to add a check here instead of doing all this
                player.removePotionEffect(PotionEffectType.SPEED);
                player.removePotionEffect(PotionEffectType.STRENGTH);
                player.setMaxHealth(20);
                break;
            case "infected":
                player.addPotionEffect(PotionEffectType.SPEED.createEffect(999999, 2));
                player.addPotionEffect(PotionEffectType.STRENGTH.createEffect(999999, 1));
                player.setMaxHealth(8);
                break;
            case null:
                player.sendMessage(Component.text("Weird ass error, you should be in a team...."));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + team);
        }
    }

    public void save(String playerId, String teamName) {
        config.set("players." + playerId + ".team", teamName);
        Main.savePlayerTeamStorageConfig(config);
    }

    public void saveKill(String playerKiller, String playerKilled) {
        String path = "players." + playerKiller + ".kills";
        List<String> kills = config.getStringList(path);

        if (!kills.contains(playerKilled)) {
            kills.add(playerKilled);
            config.set("players." + playerKiller + ".kills", kills);
            config.set("players." + playerKilled + ".team", "infected");
            Main.savePlayerTeamStorageConfig(config);
        }
    }
}
