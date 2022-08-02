package me.spotlightdevteam.example.spotlightplugin.events;

import me.spotlightdevteam.example.spotlightplugin.FileManager;
import me.spotlightdevteam.example.spotlightplugin.SpotlightPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.io.Console;
import java.util.List;
import java.util.logging.Logger;


public class DamageEvent implements Listener {
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e){
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            FileManager file = new FileManager(SpotlightPlugin.getInstance(), "serverData.yml");
            Player p = (Player) e.getEntity();
            Player d = (Player) e.getDamager();
            Integer tidp = file.getConfig().getInt("playerData." + p.getUniqueId().toString() + ".team.id");
            Integer tidd = file.getConfig().getInt("playerData." + d.getUniqueId().toString() + ".team.id");
            Boolean betp = file.getConfig().getBoolean("playerData." + p.getUniqueId().toString() + ".team.betray");
            Boolean betd = file.getConfig().getBoolean("playerData." + d.getUniqueId().toString() + ".team.betray");
            if(tidp == tidd && (!betd && !betp) && tidp != 0){
                e.setCancelled(true);
            }

            if(p.isDead()) {
                d.sendMessage("boop");
                String[] pList = file.getConfig().getString("players").split("\\|");

                if (tidp != tidd || (betd || betp)) {

                    if (betd) {
                        String form = file.getConfig().getString("playerData." + d.getUniqueId().toString() + ".team.formatted");
                        List<Integer> bans = file.getConfig().getIntegerList("playerData." + d.getUniqueId().toString() + ".team.bans");
                        bans.add(tidd);
                        file.getConfig().set("playerData." + d.getUniqueId().toString() + ".team.id", null);
                        file.getConfig().set("playerData." + d.getUniqueId().toString() + ".team.name", null);
                        file.getConfig().set("playerData." + d.getUniqueId().toString() + ".team.betray", null);
                        file.getConfig().set("playerData." + d.getUniqueId().toString() + ".team.formatted", null);
                        file.getConfig().set("playerData." + d.getUniqueId().toString() + ".team.bans", bans);
                        file.saveConfig();
                        for (String uuid : pList) {
                            int cid = file.getConfig().getInt("playerData." + uuid + ".team.id");
                            if (cid == tidd) {
                                Bukkit.getPlayer(uuid).sendMessage(ChatColor.GOLD + d.getName() + ChatColor.RED + " has killed a teammate, and been automatically removed!");
                            }

                        }
                    } else {
                        String form = file.getConfig().getString("playerData." + p.getUniqueId().toString() + ".team.formatted");
                        List<Integer> bans = file.getConfig().getIntegerList("playerData." + p.getUniqueId().toString() + ".team.bans");
                        bans.add(tidp);
                        file.getConfig().set("playerData." + p.getUniqueId().toString() + ".team.id", null);
                        file.getConfig().set("playerData." + p.getUniqueId().toString() + ".team.name", null);
                        file.getConfig().set("playerData." + p.getUniqueId().toString() + ".team.betray", null);
                        file.getConfig().set("playerData." + p.getUniqueId().toString() + ".team.formatted", null);
                        file.getConfig().set("playerData." + p.getUniqueId().toString() + ".team.bans", bans);
                        file.saveConfig();
                        for (String uuid : pList) {
                            int cid = file.getConfig().getInt("playerData." + uuid + ".team.id");
                            if (cid == tidd) {
                                Bukkit.getPlayer(uuid).sendMessage(ChatColor.GOLD + d.getName() + ChatColor.RED + " has killed " + ChatColor.GOLD + p.getName() + ChatColor.RED + ", and been automatically removed!");
                            }

                        }
                    }
                }
            }
        }
    }


}
