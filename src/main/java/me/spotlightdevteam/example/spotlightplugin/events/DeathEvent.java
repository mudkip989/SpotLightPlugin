package me.spotlightdevteam.example.spotlightplugin.events;

import me.spotlightdevteam.example.spotlightplugin.FileManager;
import me.spotlightdevteam.example.spotlightplugin.SpotlightPlugin;
import me.spotlightdevteam.example.spotlightplugin.commands.CommandTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeathEvent implements Listener {
    public DeathEvent() {
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player victim = e.getEntity();
        Player killer = victim.getKiller();
        if (killer != null && killer != victim) {
            System.out.println("So True........");
            FileManager file = new FileManager(SpotlightPlugin.getInstance(), "serverData.yml");
            String Hunter = killer.getUniqueId().toString();
            String Hider = victim.getUniqueId().toString();
            String target = file.getConfig().getString("playerData." + Hunter + ".bounty");
            if(target.equals(Hider)) {
                e.getDrops().add(new ItemStack(Material.LIGHT));
                file.getConfig().set("playerData." + Hunter + ".bounty", "empty");
                file.saveConfig();
            }
            Integer tidp = file.getConfig().getInt("playerData." + victim.getUniqueId().toString() + ".team.id");
            Integer tidd = file.getConfig().getInt("playerData." + killer.getUniqueId().toString() + ".team.id");
            Boolean betp = file.getConfig().getBoolean("playerData." + victim.getUniqueId().toString() + ".team.betray");
            Boolean betd = file.getConfig().getBoolean("playerData." + killer.getUniqueId().toString() + ".team.betray");
            Player p = victim;
            Player d = killer;

            String[] pList = file.getConfig().getString("players").split("\\|");

            if (tidp == tidd && (betd || betp)) {

                if (betd) {
                    String form = file.getConfig().getString("playerData." + d.getUniqueId().toString() + ".team.formatted");
                    file.getConfig().set("playerData." + d.getUniqueId().toString() + ".team.id", null);
                    file.getConfig().set("playerData." + d.getUniqueId().toString() + ".team.name", null);
                    file.getConfig().set("playerData." + d.getUniqueId().toString() + ".team.betray", null);
                    file.getConfig().set("playerData." + d.getUniqueId().toString() + ".team.formatted", null);
                    file.getConfig().set("playerData." + d.getUniqueId().toString() + ".team.bans", tidd);
                    file.saveConfig();
                    for (String uuid : pList) {
                        int cid = file.getConfig().getInt("playerData." + uuid + ".team.id");
                        if (cid == tidd) {

                            Bukkit.getPlayer(uuid).sendMessage(ChatColor.GOLD + d.getName() + ChatColor.RED + " has killed a teammate, and been automatically removed!");
                        }

                    }
                } else {

                    String form = file.getConfig().getString("playerData." + p.getUniqueId().toString() + ".team.formatted");

                    for (String uuid : pList) {
                        int cid = file.getConfig().getInt("playerData." + uuid + ".team.id");
                        if (cid == tidd) {
                            d.sendMessage(Bukkit.getPlayer(uuid).getName());
                            Bukkit.getPlayer(uuid).sendMessage(ChatColor.GOLD + d.getName() + ChatColor.RED + " has killed " + ChatColor.GOLD + p.getName() + ChatColor.RED + ", and been automatically removed!");
                        }

                    }
                    file.getConfig().set("playerData." + p.getUniqueId().toString() + ".team.id", null);
                    file.getConfig().set("playerData." + p.getUniqueId().toString() + ".team.name", null);
                    file.getConfig().set("playerData." + p.getUniqueId().toString() + ".team.betray", null);
                    file.getConfig().set("playerData." + p.getUniqueId().toString() + ".team.formatted", null);
                    file.getConfig().set("playerData." + p.getUniqueId().toString() + ".team.bans", tidp);
                    file.saveConfig();
                }
            }



        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        
        FileManager file = new FileManager(SpotlightPlugin.getInstance(), "serverData.yml");
        if (!file.getConfig().getString("players").contains(player.getUniqueId().toString())) {
            player.sendMessage("§6Welcome to Spotlight SMP!");
            String curValue = file.getConfig().getString("players");
            file.getConfig().set("players", curValue + "|" + player.getUniqueId());
            file.saveConfig();
        }
        if (!file.getConfig().getString("recents").contains(player.getUniqueId().toString())) {
            player.sendMessage("§6A new Day has begun! Check /bounty!");
            String curValue = file.getConfig().getString("recents");
            file.getConfig().set("recents", curValue + "|" + player.getUniqueId());
            file.saveConfig();
        }

    }
}
