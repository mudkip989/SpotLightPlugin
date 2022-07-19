package me.spotlightdevteam.example.spotlightplugin.events;

import me.spotlightdevteam.example.spotlightplugin.FileManager;
import me.spotlightdevteam.example.spotlightplugin.SpotlightPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
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
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (Objects.equals(player.getUniqueId().toString(), "f4ecfa65-a67d-4bc1-a0e3-3c76c56dae8a")){
            player.sendMessage("A̶f̸r̵a̷i̷d̷ ̶t̶o̸ ̸w̸a̶i̶l̵,̸ ̷a̶f̶r̷a̷i̶d̵ ̸o̷f̷ ̶h̶i̵s̵ ̵t̶a̵l̴e̷");
        }
        if (Objects.equals(player.getUniqueId().toString(), "79cbd6b6-eac9-4201-9ff5-c029075fcace")){
            player.sendMessage("omg clownpierce joined -Ricegang 2022");

        }
        if (Objects.equals(player.getUniqueId().toString(), "a64cd226-5a85-4655-a696-788a9fcb2f5c")){
            player.sendMessage("L Bozo");
        }
        
        FileManager file = new FileManager(SpotlightPlugin.getInstance(), "serverData.yml");
        if (!file.getConfig().getString("players").contains(player.getUniqueId().toString())) {
            player.sendMessage("§6Welcome to Spotlight SMP!");
            String curValue = file.getConfig().getString("players");
            file.getConfig().set("players", curValue + "|" + player.getUniqueId());
            file.saveConfig();
        }

    }
}
