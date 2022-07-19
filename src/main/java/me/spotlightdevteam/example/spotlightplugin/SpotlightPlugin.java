package me.spotlightdevteam.example.spotlightplugin;

import me.spotlightdevteam.example.spotlightplugin.commands.CommandBounty;
import me.spotlightdevteam.example.spotlightplugin.commands.Version;
import me.spotlightdevteam.example.spotlightplugin.events.DeathEvent;
import me.spotlightdevteam.example.spotlightplugin.events.RightClickListener;
import me.spotlightdevteam.example.spotlightplugin.utils.removeItems;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.time.*;


public final class SpotlightPlugin extends JavaPlugin {
    private static SpotlightPlugin instance;

    public SpotlightPlugin() {
    }

    public void onEnable() {
        instance = this;
        System.out.println("SpotlightV2.0 had been successfully loaded");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                removeItems.clear();
            }
        }, 0L, 20L);

        (new BukkitRunnable() {
            public void run() {
                FileManager file = new FileManager(SpotlightPlugin.getInstance(), "serverData.yml");
                if (!file.getConfig().contains("lastChange")) {
                    Instant temp = Instant.now();
                    temp.minus(10000000L, ChronoUnit.SECONDS);
                    file.getConfig().set("lastChange", temp.toString());
                    file.saveConfig();
                }

                int interval = Integer.parseInt(file.getConfig().getString("resetPeriod"));
                Instant finish = Instant.now();
                Instant lastDate = Instant.parse(file.getConfig().getString("lastChange"));
                long Diff = Duration.between(lastDate, finish).toSeconds();
                Random rand = new Random();
                rand.setSeed(System.currentTimeMillis());
                if (Diff > (long)interval) {
                    boolean success = SpotlightPlugin.this.setBounties(rand);
                    FileManager filem = new FileManager(SpotlightPlugin.getInstance(), "serverData.yml");
                    if (success) {
                        filem.getConfig().set("lastChange", finish.toString());
                        filem.saveConfig();
                    }
                }

            }
        }).runTaskTimerAsynchronously(getInstance(), 0L, 20L);
        this.getCommand("bounty").setExecutor(new CommandBounty());
        this.getCommand("version").setExecutor(new Version());
        this.registerEvents();
        SpotlightManager.registerSpotlight();
    }

    public boolean setBounties(Random rand) {
        FileManager file = new FileManager(getInstance(), "serverData.yml");
        String[] pList = file.getConfig().getString("players").split("\\|");
        int len = pList.length;
        if (len < 2) {
            return false;
        }
        //The else statement here was unneeded as the if statement with the "return false;" takes us out of here if there is not enough players
        Long timestamp = System.currentTimeMillis();
        //String[] var6 = pList;
        //int var7 = pList.length;

        //The for loops here seem different, but really the shorter one is a faster and more efficient one than the longer one.
        //We can use this to minimize number of variables in the future and cause less confusion
        //for(int var8 = 0; var8 < var7; ++var8) {
        for(String uid: pList){
            List<String> tempList = new LinkedList(Arrays.asList(pList));
            tempList.remove(uid);
            tempList.remove(0);
            String target = tempList.get(rand.nextInt(0, tempList.size() - 1));
            file.getConfig().set("playerData." + uid + ".bounty", target);
            file.saveConfig();
        }

        return true;
    }

    void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new DeathEvent(), this);
        pm.registerEvents(new RightClickListener(), this);
    }

    public static SpotlightPlugin getInstance() {
        return instance;
    }

}
