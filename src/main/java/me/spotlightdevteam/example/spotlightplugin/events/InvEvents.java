package me.spotlightdevteam.example.spotlightplugin.events;

import me.spotlightdevteam.example.spotlightplugin.SpotlightPlugin;
import me.spotlightdevteam.example.spotlightplugin.utils.removeItems;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public class InvEvents implements Listener {

    @EventHandler
    public void onPickup(EntityPickupItemEvent e){
        Player player = (Player) e.getEntity();
        Item item = e.getItem();
        (new BukkitRunnable() {
            public void run() {
                removeItems.clear(player);
            }
        }).runTaskLater(SpotlightPlugin.getInstance(), 1);
        return;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        (new BukkitRunnable() {
            public void run() {
                removeItems.clear(player);
            }
        }).runTaskLater(SpotlightPlugin.getInstance(), 1);
        return;
    }
}
