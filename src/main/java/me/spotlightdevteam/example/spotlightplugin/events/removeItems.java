package me.spotlightdevteam.example.spotlightplugin.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class removeItems {


    public static void clear() {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            Player player = p.getPlayer();
            assert player != null;
            Material[] blacklistedItems = new Material[]{
                    Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS,
                    Material.TIPPED_ARROW, Material.END_CRYSTAL, Material.RESPAWN_ANCHOR, Material.NETHERITE_HELMET};
            for (int i = 0; i < 5; i++) {
                if (p.getPlayer().getInventory().getHelmet() != null && p.getPlayer().getInventory().getHelmet().getType() == Material.NETHERITE_HELMET) {
                    p.getInventory().setHelmet(null);
                    p.getPlayer().updateInventory();
                }else if (p.getPlayer().getInventory().getChestplate() != null && p.getPlayer().getInventory().getChestplate().getType() == Material.NETHERITE_CHESTPLATE) {
                    p.getInventory().setChestplate(null);
                    p.getPlayer().updateInventory();
                }else if (p.getPlayer().getInventory().getLeggings() != null && p.getPlayer().getInventory().getLeggings().getType() == Material.NETHERITE_LEGGINGS) {
                    p.getInventory().setLeggings(null);
                    p.getPlayer().updateInventory();
                }else if (p.getPlayer().getInventory().getBoots() != null && p.getPlayer().getInventory().getBoots().getType() == Material.NETHERITE_BOOTS) {
                    p.getInventory().setBoots(null);
                    p.getPlayer().updateInventory();
                }
            }

            for (Material blacklistedItem : blacklistedItems) {

                if (p.getPlayer().getInventory().getItemInOffHand() != null && p.getPlayer().getInventory().getItemInOffHand().getType() == blacklistedItem) {
                    p.getInventory().setItemInOffHand(null);
                    p.getPlayer().updateInventory();
                }

            }


            for (int items = 0; items < blacklistedItems.length; items++) {
                player.getInventory().removeItem(new ItemStack(blacklistedItems[items], 2048));
                player.updateInventory();

            }



            }


        }
    }

/*     public static void clear() {
        int i = 0;
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                Player player = p.getPlayer();
                assert player != null;
                player.getInventory().removeItem(new ItemStack(Material.NETHERITE_SWORD));
                player.getInventory().removeItem(new ItemStack(Material.NETHERITE_AXE));
                player.getInventory().removeItem(new ItemStack(Material.NETHERITE_HELMET));
                player.getInventory().removeItem(new ItemStack(Material.NETHERITE_CHESTPLATE));
                player.getInventory().removeItem(new ItemStack(Material.NETHERITE_LEGGINGS));
                player.getInventory().removeItem(new ItemStack(Material.NETHERITE_BOOTS));
                player.getInventory().removeItem(new ItemStack(Material.TIPPED_ARROW));
                player.getInventory().removeItem(new ItemStack(Material.END_CRYSTAL));
                player.getInventory().removeItem(new ItemStack(Material.RESPAWN_ANCHOR));
                player.updateInventory();

            }
                   for (ItemStack item : p.getPlayer().getInventory().getArmorContents()) {
                    if (item != null) {
                        System.out.println(item + "hmm");
                        for (int i = 0; i < blacklistedItems.length; i++) {


                            System.out.println(blacklistedItems[i] + "hmmmm");

                            if (Objects.equals(item.getType(), blacklistedItems[i])) {

                                p.getPlayer().getInventory().removeItem(item);
                                System.out.println("removed" + item);

                            }
                        }

                        player.updateInventory();

                    }
                }

    }*/


