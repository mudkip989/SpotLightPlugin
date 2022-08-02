package me.spotlightdevteam.example.spotlightplugin.commands;

import me.spotlightdevteam.example.spotlightplugin.FileManager;
import me.spotlightdevteam.example.spotlightplugin.SpotlightPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.Console;
import java.io.File;
import java.util.*;

public class CommandTeam implements CommandExecutor {
    /*
    playerData:
        uuid:
            team:
                id: int
                name: String
                formatted: String
                betrayed: bool
                invites: int[]
     */







    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            FileManager file = new FileManager(SpotlightPlugin.getInstance(), "serverData.yml");
            String output = file.getConfig().getString("playerData." + player.getUniqueId().toString() + ".team.id");
            String outputName = file.getConfig().getString("playerData." + player.getUniqueId().toString() + ".team.name");
            String formedName = file.getConfig().getString("playerData." + player.getUniqueId().toString() + ".team.formatted");
            if(args.length == 0){


                if(output == null){
                    player.sendMessage(ChatColor.DARK_PURPLE + "You are not currently in a faction! ");
                }else {
                    player.sendMessage(ChatColor.DARK_PURPLE + "You are currently in a faction! " + ChatColor.GOLD + "Team: " + formedName);
                }
                return true;
            }else {
                switch (args[0]) {
                    case "create":
                        createTeam(player, args, file);
                        break;
                    case "leave":
                        leaveTeam(player, args, file);
                        break;
                    case "invite":
                        invite(player, args, file);
                        break;
                    case "betray":
                        betray(player, args, file);
                        break;
                    case "members":
                        List <String> thing = getTeamNames(file.getConfig().getInt("playerData." + player.getUniqueId().toString() + ".team.id"), file);
                        player.sendMessage(ChatColor.GOLD + "Members of " + formedName + ChatColor.GOLD + ":");
                        for (String name: thing) {
                            player.sendMessage(ChatColor.GOLD + "  -" + ChatColor.DARK_PURPLE + name);
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            }

        }
        return false;
    }

    private List<String> getTeamNames(int id, FileManager file){
        List<String> tems = new ArrayList();

        String[] pList = file.getConfig().getString("players").split("\\|");
        for (String uuid: pList) {
            int cid = file.getConfig().getInt("playerData." + uuid + ".team.id");
            if(cid == id){
                tems.add(Bukkit.getPlayer(UUID.fromString(uuid)).getName());
            }

        }
        return tems;
    }
    private List<String> getTeamUuids(int id, FileManager file){
        List<String> tems = new ArrayList();

        String[] pList = file.getConfig().getString("players").split("\\|");
        for (String uuid: pList) {
            int cid = file.getConfig().getInt("playerData." + uuid + ".team.id");
            if(cid == id){
                tems.add(uuid);
            }

        }
        return tems;
    }
    private String findForm(int id, FileManager file){

        String[] pList = file.getConfig().getString("players").split("\\|");
        for (String uuid: pList) {
            int cid = file.getConfig().getInt("playerData." + uuid + ".team.id");
            if(cid == id){
                return file.getConfig().getString("playerData." + uuid + ".team.formatted");
            }

        }

        return null;
    }
    private String findName(int id, FileManager file){

        String[] pList = file.getConfig().getString("players").split("\\|");
        for (String uuid: pList) {
            int cid = file.getConfig().getInt("playerData." + uuid + ".team.id");
            if(cid == id){
                return file.getConfig().getString("playerData." + uuid + ".team.name");
            }

        }

        return null;
    }

    private void invite(Player player, String[] args, FileManager file){
        String output = file.getConfig().getString("playerData." + player.getUniqueId().toString() + ".team.id");
        String outputName = file.getConfig().getString("playerData." + player.getUniqueId().toString() + ".team.name");
        String formedName = file.getConfig().getString("playerData." + player.getUniqueId().toString() + ".team.formatted");
        switch(args[1]){
            case "list":
                List<Integer> ids = file.getConfig().getIntegerList("playerData." + player.getUniqueId().toString() + ".team.invites");
                for (int id: ids){
                    String tex = findForm(id, file);
                    player.sendMessage(tex);
                }
                player.sendMessage(ChatColor.GOLD + "Above are ids of all invites. Use /faction invite accept <id#>");
                break;
            case "clear":
                file.getConfig().set("playerData." + player.getUniqueId().toString() + ".team.invites", null);
                player.sendMessage(ChatColor.GOLD + "Cleared Invites!");
                file.saveConfig();
                break;
            case "accept":
                List<Integer> ides = file.getConfig().getIntegerList("playerData." + player.getUniqueId().toString() + ".team.invites");

                if(ides.contains(Integer.parseInt(args[2]))){
                    String name = findName(Integer.parseInt(args[2]), file);
                    String form = ChatColor.DARK_AQUA + name + ChatColor.GREEN + "#" + Integer.parseInt(args[2]);
                    file.getConfig().set("playerData." + player.getUniqueId().toString() + ".team.id", Integer.parseInt(args[2]));
                    file.getConfig().set("playerData." + player.getUniqueId().toString() + ".team.name", name);
                    file.getConfig().set("playerData." + player.getUniqueId().toString() + ".team.betray", false);
                    file.getConfig().set("playerData." + player.getUniqueId().toString() + ".team.formatted", form);
                    file.getConfig().set("playerData." + player.getUniqueId().toString() + ".team.invites", null);
                    file.saveConfig();
                    player.sendMessage(ChatColor.GREEN + "You Successfully Joined " + form);
                }else{
                    player.sendMessage(ChatColor.RED + "That faction has not invited you, or doesnt exist.");

                }

                break;
            default:
                String target = args[1];
                Player targetPlayer = Bukkit.getPlayer(target);
                String uid = targetPlayer.getUniqueId().toString();
                if(uid == null){
                    player.sendMessage(ChatColor.RED + "That player is not online right now!");
                    return;
                }
                List <Integer> idees = file.getConfig().getIntegerList("playerData." + uid + ".team.invites");
                List <Integer> bans = file.getConfig().getIntegerList("playerData." + uid + ".team.bans");
                if(bans == null){
                    bans = Collections.emptyList();
                    bans.add(0);
                }
                String teamid = file.getConfig().getString("playerData." + uid + ".team.id");
                if(teamid == output){
                    player.sendMessage(ChatColor.RED + "Player could not be invited. " + ChatColor.GOLD + targetPlayer.getName() + ChatColor.RED + " is in your faction!");


                    break;

                }
                if(bans.contains(Integer.parseInt(output))){
                    player.sendMessage(ChatColor.RED + "Player could not be invited. " + ChatColor.GOLD + targetPlayer.getName() + ChatColor.RED + " is banned from your faction due to betrayal!");
                    break;
                }
                if(!idees.contains(Integer.parseInt(output))){
                    idees.add(Integer.parseInt(output));
                    file.getConfig().set("playerData." + uid + ".team.invites", idees);
                    player.sendMessage(ChatColor.GREEN + "Successfully invited " + ChatColor.GOLD + targetPlayer.getName() + ChatColor.GREEN + " to your faction!");
                    targetPlayer.sendMessage(ChatColor.GREEN + "You have been invited to" + formedName);
                    file.saveConfig();
                    break;
                }
                player.sendMessage(ChatColor.RED + "You already invited " + ChatColor.GOLD + targetPlayer.getName());
                break;
        }
    }


    private void betray(Player player, String[] args, FileManager file){
        String output = file.getConfig().getString("playerData." + player.getUniqueId().toString() + ".team.id");
        if(output != null){
            file.getConfig().set("playerData." + player.getUniqueId().toString() + ".team.betray", true);
            file.saveConfig();
            player.sendMessage(ChatColor.DARK_PURPLE + "You have now betrayed your faction. To undo this, you must leave and rejoin the faction.");

        }
        player.sendMessage(ChatColor.RED + "You are not in a faction! Please join a faction first.");
    }

    private void leaveTeam(Player player, String[] args, FileManager file){
        String output = file.getConfig().getString("playerData." + player.getUniqueId().toString() + ".team.id");

        if(output == null){
            player.sendMessage(ChatColor.RED + "You are currently not in a faction.");
        }else {
            String form = file.getConfig().getString("playerData." + player.getUniqueId().toString() + ".team.formatted");
            file.getConfig().set("playerData." + player.getUniqueId().toString() + ".team.id", null);
            file.getConfig().set("playerData." + player.getUniqueId().toString() + ".team.name", null);
            file.getConfig().set("playerData." + player.getUniqueId().toString() + ".team.betray", null);
            file.getConfig().set("playerData." + player.getUniqueId().toString() + ".team.formatted", null);
            file.saveConfig();
            player.sendMessage(ChatColor.GOLD + "Successfully left " + form);

        }

    }
    private boolean createTeam(Player player, String[] args, FileManager file) {
        String output = file.getConfig().getString("playerData." + player.getUniqueId().toString() + ".team.id");


        if(output != null){
            player.sendMessage(ChatColor.RED + "You are already in " + file.getConfig().getString("playerData." + player.getUniqueId().toString() + ".team.formatted"));
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

            int id = 1 + file.getConfig().getInt("teamIdCounter");
            String form = ChatColor.DARK_AQUA + name + ChatColor.GREEN + "#" + id;
            file.getConfig().set("playerData." + player.getUniqueId().toString() + ".team.id", id);
            file.getConfig().set("playerData." + player.getUniqueId().toString() + ".team.name", name);
            file.getConfig().set("playerData." + player.getUniqueId().toString() + ".team.formatted", form);
            file.getConfig().set("playerData." + player.getUniqueId().toString() + ".team.betray", false);
            file.getConfig().set("teamIdCounter", id);
            file.saveConfig();
            player.sendMessage(form + ChatColor.GOLD + " was created.");





        }
        return false;
    }
}
