package me.mudkip989.example.spotlightplugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

public final class SpotlightManager {
    private static final List<PotionEffectType> possibleEffects;
    private static final Random random;
    private static final int spotlightModelData = 10;

    public SpotlightManager() {
    }

    public static List<PotionEffectType> getRandomEffects(int amount) {
        List<PotionEffectType> toReturn = new ArrayList();
        List<PotionEffectType> choices = new ArrayList(possibleEffects);

        for (int x = 0; x < amount && !choices.isEmpty(); ++x) {
            PotionEffectType effect = (PotionEffectType) choices.get(random.nextInt(choices.size()));
            choices.remove(effect);
            toReturn.add(effect);
        }

        return toReturn;
    }

    public static void registerSpotlight() {
        ItemStack spotlight = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = spotlight.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.YELLOW + "Spotlight");
        meta.setCustomModelData(10);
        spotlight.setItemMeta(meta);
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(SpotlightPlugin.getInstance(), "spotlight-recipe"), spotlight);
        recipe.shape(new String[]{"LTL", "DLD", "LGL"});
        recipe.setIngredient('L', Material.LIGHT);
        recipe.setIngredient('T', Material.TOTEM_OF_UNDYING);
        recipe.setIngredient('D', Material.DIAMOND_BLOCK);
        recipe.setIngredient('G', Material.GLASS_BOTTLE);
        Bukkit.addRecipe(recipe);
    }

    public static boolean isSpotlight(ItemStack item) {
        return item != null && item.getType() == Material.NETHER_STAR && item.getItemMeta() != null && item.getItemMeta().getCustomModelData() == 10;
    }

    static {
        possibleEffects = Arrays.asList(PotionEffectType.DAMAGE_RESISTANCE, PotionEffectType.INCREASE_DAMAGE, PotionEffectType.SPEED, PotionEffectType.FIRE_RESISTANCE, PotionEffectType.FAST_DIGGING, PotionEffectType.REGENERATION);
        random = new Random();
    }
}