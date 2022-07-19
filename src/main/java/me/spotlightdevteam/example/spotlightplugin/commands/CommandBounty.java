package me.spotlightdevteam.example.spotlightplugin.commands;

import me.spotlightdevteam.example.spotlightplugin.FileManager;
import me.spotlightdevteam.example.spotlightplugin.SpotlightPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandBounty implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            FileManager file = new FileManager(SpotlightPlugin.getInstance(), "serverData.yml");
            String uid = player.getUniqueId().toString();
            if(file.getConfig().contains("playerData." + uid + ".bounty")) {
                String tuid = file.getConfig().getString("playerData." + uid + ".bounty");
                if(tuid.equals("empty")){
                    player.sendMessage("§4§lYou already killed your target, come back §5tommorrow.");
                    player.playSound(player.getLocation(), "minecraft:item.shield.block", 1, 1);
                    return true;
                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(UUID.fromString(tuid));
                player.sendMessage("§6§lYour Target is: §5" + target.getName());
                player.playSound(player.getLocation(), "minecraft:entity.player.levelup", 1, 1);

            }else{
                player.sendMessage("§4§lYou joined too recently, come back §5tommorrow.");
                player.playSound(player.getLocation(), "minecraft:item.shield.block", 1, 1);
            }
        return true;
        }
        return false;
    }
}
