package me.mudkip989.example.spotlightplugin.events;

import me.mudkip989.example.spotlightplugin.FileManager;
import me.mudkip989.example.spotlightplugin.SpotlightPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;

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

    }
}