package me.spotlightdevteam.example.spotlightplugin.events;

import java.util.Iterator;
import java.util.Random;

import me.spotlightdevteam.example.spotlightplugin.SpotlightManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RightClickListener implements Listener {
    private static final Random random = new Random();

    public RightClickListener() {
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = e.getItem();
            if (SpotlightManager.isSpotlight(item)) {
                Player p = e.getPlayer();
                item.setAmount(item.getAmount() - 1);

                PotionEffectType effect;
                int amplifier;
                for(Iterator<PotionEffectType> var4 = SpotlightManager.getRandomEffects(2).iterator(); var4.hasNext(); p.addPotionEffect(new PotionEffect(effect, Integer.MAX_VALUE, amplifier))) {
                    effect = var4.next();
                    amplifier = 0;
                    if (effect != PotionEffectType.FIRE_RESISTANCE) {
                        amplifier = random.nextInt(2);
                    }
                }
            } else if (item != null && item.getType() == Material.LIGHT) {
                e.setCancelled(true);
            }

        }
    }
}
