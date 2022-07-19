package me.mudkip989.example.spotlightplugin;

import com.sun.jdi.connect.spi.TransportService;
import me.mudkip989.example.spotlightplugin.events.CommandBounty;
import me.mudkip989.example.spotlightplugin.events.DeathEvent;
import me.mudkip989.example.spotlightplugin.events.RightClickListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.time.*;

import static java.time.temporal.ChronoUnit.SECONDS;


public final class SpotlightPlugin extends JavaPlugin {
    private static SpotlightPlugin instance;

    public SpotlightPlugin() {
    }

    public void onEnable() {
        instance = this;
        System.out.println("SpotlightV2.2 had been sucessfully loaded");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                remove_items.clear();
            }
        }, 0L, 20L);
        new BukkitRunnable(){
            @Override
            public void run(){

                FileManager file = new FileManager(getInstance(), "serverData.yml");
                if(!file.getConfig().contains("lastChange")){
                    Instant temp = Instant.now();
                    temp.minus(10000000, SECONDS);
                    file.getConfig().set("lastChange", temp.toString());
                    file.saveConfig();
                }

                int interval = Integer.parseInt(file.getConfig().getString("resetPeriod"));
                Instant finish = Instant.now();
                Instant lastDate = Instant.parse(file.getConfig().getString("lastChange"));
                long Diff = Duration.between(lastDate, finish).toSeconds();

                Random rand = new Random();

                rand.setSeed(System.currentTimeMillis());
                if(Diff > interval){
                    boolean success = setBounties(rand);
                    FileManager filem = new FileManager(getInstance(), "serverData.yml");
                    if (success) {
                        filem.getConfig().set("lastChange", finish.toString());
                        filem.saveConfig();
                    }

                }
            }
        }.runTaskTimerAsynchronously(getInstance(), 0, 20);
        this.getCommand("bounty").setExecutor(new CommandBounty());
        this.registerEvents();
        SpotlightManager.registerSpotlight();
    }

    public boolean setBounties(Random rand){
        FileManager file = new FileManager(SpotlightPlugin.getInstance(), "serverData.yml");
        String[] pList = file.getConfig().getString("players").split("\\|");
        int len = pList.length;
        if(len < 2){
            return false;
        }
        Long timestamp = System.currentTimeMillis();
        for (String uid: pList) {

            List<String> tempList = new LinkedList<>(Arrays.asList(pList));
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
