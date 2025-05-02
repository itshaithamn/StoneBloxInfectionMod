package io.github.itshaithamn.infection.teammanager;


import io.github.itshaithamn.infection.Main;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.nametag.NameTagManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
        if(event.getEntity().getKiller() != null) {
            killed = event.getEntity();
            killer = event.getEntity().getKiller();
        } else {
            return;
        }

        String uuidKiller = killer.getUniqueId().toString();
        UUID uuidKillerObj = killer.getUniqueId();
        String pathKiller = "players." + uuidKiller + ".team";
        String teamKiller = config.getString(pathKiller);

        String uuidKilled = killed.getUniqueId().toString();
        UUID uuidKilledObj = killed.getUniqueId();
        String pathKilled = "players." + uuidKilled + ".team";
        String teamKilled = config.getString(pathKilled);

        if (teamKiller != null && teamKiller.equals("infected")) {
            try {
                saveKillInfected(uuidKiller, uuidKilledObj);
                teamManager.addPlayertoTeam(Bukkit.getPlayer(uuidKilledObj), "infected");
            } catch (Exception e) {
                Bukkit.broadcast(Component.text("Debug, killed player == null, Null pointer exception."));
            }
            try {
                TeamCheckListener(killed);
            } catch (Exception e) {
                Bukkit.broadcast(Component.text("Debug, killed player == null, Null pointer exception."));
            }
            killer.sendMessage(Component.text("§1§lYou have infected:§r&c " + killed.getName()));
        }

        if(teamKiller != null && teamKiller.equals("survivor") && teamKilled.equals("infected")) {
            try {
                saveKillSurvivor(uuidKiller, uuidKilledObj);
            } catch (Exception e) {
                Bukkit.broadcast(Component.text("Debug, Killer not Survivor: " + teamKiller + " or Killed not Infected " + teamKilled));
            }
//               teamManager.addPlayertoTeam(Bukkit.getPlayer(uuidKillerObj), teamKilled);
            try {
                TeamCheckListener(killed);
            } catch (NullPointerException e) {
                Bukkit.broadcast(Component.text("Debug, killed player == null. Null pointer exception."));
            }
            killer.sendMessage(Component.text("§aYou have killed the infected: §c" + killed.getName()));
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
            teamManager.addPlayertoTeam(player, "survivor");
            return;
        }

        player.sendMessage(Component.text("[INFO]§r §1§lYou are on team: " ));
        String path = "players." + uuid + ".team";
        String team = config.getString(path);
        assert team != null;
        player.sendMessage(Component.text(team));
        teamManager.addPlayertoTeam(player, team);
    }

    public static void TeamCheckListener(Player player){
        String uuid = player.getUniqueId().toString();
        String playerName = player.getName();
        String path = "players." + uuid + ".team";
        String team = config.getString(path);

        TabAPI tabApi = TabAPI.getInstance();
        TabPlayer tabPlayer = null;
        try {
            tabPlayer = tabApi.getPlayer(player.getUniqueId());
        } catch (NullPointerException e) {
            Bukkit.getLogger().info("TabPlayer not found, player UUID does not exist?" + player.getUniqueId() + "with username " + playerName);
        }
        NameTagManager ntm = tabApi.getNameTagManager();

        Team infected = teamManager.getInfectedTeam();
        Team survivor = teamManager.getSurvivorsTeam();
        switch (team) {
            case "survivor":
                if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                    PotionEffect e = player.getPotionEffect(PotionEffectType.SPEED);
                    if (e.getAmplifier() == 2 && e.getDuration() > 900000) {
                        player.removePotionEffect(PotionEffectType.SPEED);
                    }
                }
                if (player.hasPotionEffect(PotionEffectType.STRENGTH)) {
                    PotionEffect e = player.getPotionEffect(PotionEffectType.STRENGTH);
                    if (e.getAmplifier() == 1 && e.getDuration() > 900000) {
                        player.removePotionEffect(PotionEffectType.STRENGTH);
                    }
                }
                infected.removeEntry(player.getName());
                survivor.addEntry(player.getName());
                ntm.setPrefix(tabPlayer, "§l[§r§9Survivor§r§l]§r ");
                player.setMaxHealth(20);
                break;
            case "infected":
                player.addPotionEffect(PotionEffectType.SPEED.createEffect(999999, 2));
                player.addPotionEffect(PotionEffectType.STRENGTH.createEffect(999999, 1));
                survivor.removeEntry(player.getName());
                infected.addEntry(player.getName());
                ntm.setPrefix(tabPlayer, "§l[§r§aInfected§r§l]§r ");
                player.setMaxHealth(12);
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

    public void saveKillInfected(String playerKiller, UUID playerKilled) {
        String path = "players." + playerKiller + ".kills";
        List<String> kills = config.getStringList(path);

        if (!kills.contains(playerKilled.toString())) {
            kills.add(playerKilled.toString());
            config.set("players." + playerKiller + ".kills", kills);
            config.set("players." + playerKilled + ".team", "infected");
            Main.savePlayerTeamStorageConfig(config);
        }
    }

    public void saveKillSurvivor(String playerKiller, UUID playerKilledObj) {
        String path = "players." + playerKiller + ".kills";
        List<String> kills = config.getStringList(path);

        if (!kills.contains(playerKilledObj.toString())) {
            kills.add(playerKilledObj.toString());
            config.set("players." + playerKiller + ".kills", kills);
            Main.savePlayerTeamStorageConfig(config);
        }
    }

    public static void getKills(Player player) {
        UUID uuid = player.getUniqueId();
        String path = "players." + uuid + ".kills";
        List<String> kills = config.getStringList(path);
        int killCount = kills.size();
        player.sendMessage(Component.text("§c§l[Infection Event]§r§r §f[§r§cPlayer Kills§r§f]§r" + "§f§l" + player.getName() + "§r has §c§l" + killCount + " kills." ));
    };

    public static void getTop(Player player) {
        if (config == null || !config.isConfigurationSection("players")) {
            player.sendMessage("§c§l[Infection Event] §r§cNo player data found.");
            return;
        }

        ConfigurationSection playersSection = config.getConfigurationSection("players");

        List<Map.Entry<UUID, Integer>> topList = playersSection.getKeys(false).stream()
                .filter(key -> {
                    try {
                        UUID.fromString(key);
                        return true;
                    } catch (IllegalArgumentException ex) {
                        return false;
                    }
                })
                .map(UUID::fromString)
                .collect(Collectors.toMap(
                        uuid -> uuid,
                        uuid -> config.getStringList("players." + uuid + ".kills").size()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<UUID, Integer>comparingByValue().reversed())
                .limit(10)
                .toList();

        player.sendMessage("§c§l[Infection Event] §r§fTop 10 Killers:");
        for (int i = 0; i < topList.size(); i++) {
            UUID uuid = topList.get(i).getKey();
            int count = topList.get(i).getValue();
            OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
            String name = op.getName() != null ? op.getName() : uuid.toString();
            player.sendMessage(String.format(" §7• §b#%d §f%s §7– §c%d §fkills", i + 1, name, count));
        }
    }
}
