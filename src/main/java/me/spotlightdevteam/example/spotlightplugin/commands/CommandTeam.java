package me.spotlightdevteam.example.spotlightplugin.commands;

import me.spotlightdevteam.example.spotlightplugin.FileManager;
import me.spotlightdevteam.example.spotlightplugin.SpotlightPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTeam implements CommandExecutor {
    /*
    playerData:
        uuid:
            team:
                id: int
                name: String
                formatted: String
                betrayed: bool
                perms: int
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            FileManager file = new FileManager(SpotlightPlugin.getInstance(), "serverData.yml");
            if(args.length == 0){
                String output = file.getConfig().getString("playerData." + player.getUniqueId().toString() + ".team.id");
                String outputName = file.getConfig().getString("playerData." + player.getUniqueId().toString() + ".team.name");

                if(output == null){
                    player.sendMessage(ChatColor.DARK_PURPLE + "You are not currently in a faction! ");
                }else {
                    player.sendMessage(ChatColor.DARK_PURPLE + "You are currently in a faction! " + ChatColor.GOLD + "Team: " + ChatColor.DARK_BLUE + outputName + ChatColor.GREEN + "#" + output);
                }
                return true;
            }else {
                switch (args[0]) {
                    case "create":
                        createTeam(player, args, file);
                        break;
                    default:
                        return false;
                }
                return true;
            }

        }
        return false;
    }



    private boolean createTeam(Player player, String[] args, FileManager file) {
        String output = file.getConfig().getString("playerData." + player.getUniqueId().toString() + ".team.id");


        if(output != null){
            player.sendMessage(ChatColor.RED + "You are already in" + file.getConfig().getString("playerData." + player.getUniqueId().toString() + ".team.id"));
            /*
            file.getConfig().set("playerData." + player.getUniqueId().toString() + ".team.id", null);
            file.getConfig().set("playerData." + player.getUniqueId().toString() + ".team.name", null);
            file.getConfig().set("playerData." + player.getUniqueId().toString() + ".team.betray", null);
            file.getConfig().set("playerData." + player.getUniqueId().toString() + ".team.formatted", null);
            file.saveConfig();
            */
        }else{
            String name = "";
            int i = 0;
            for (String arg: args) {
                if(i!=0) {
                    name += arg;
                    if(i<args.length-1){
                        name += " ";
                    }
                }
                i++;

            }
            String num = file.getConfig().getString("teamIdCounter");
            if(num == null){
                num = "0";
            }
            int id = 1 + Integer.parseInt(num);
            String form = ChatColor.DARK_BLUE + name + ChatColor.GREEN + "#" + id;
            file.getConfig().set("playerData." + player.getUniqueId().toString() + ".team.id", id);
            file.getConfig().set("playerData." + player.getUniqueId().toString() + ".team.name", name);
            file.getConfig().set("playerData." + player.getUniqueId().toString() + ".team.formatted", form);
            file.getConfig().set("playerData." + player.getUniqueId().toString() + ".team.betray", false);
            player.sendMessage(form + ChatColor.GOLD + " was created.");
            file.saveConfig();





        }
        player.sendMessage("non-existent command...yet.");
        return false;
    }
}
