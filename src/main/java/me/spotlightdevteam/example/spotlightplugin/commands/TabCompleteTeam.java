package me.spotlightdevteam.example.spotlightplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabCompleteTeam implements TabCompleter {
    @Override
    public List<String> onTabComplete (CommandSender sender, Command cmd, String label, String[] args){


        if(args.length >= 1){
            List<String> completions = new ArrayList<>();
            completions.add("create");
            completions.add("leave");
            completions.add("invite");
            completions.add("betray");
            completions.add("members");
            if(args.length > 1) {
                completions = new ArrayList<>();
                switch (args[0]) {
                    case "create":
                        completions.add("<faction name>");
                        return completions;
                    case "invite":

                        completions.add("list");
                        completions.add("clear");
                        completions.add("accept");
                        for (Player p: Bukkit.getOnlinePlayers()) {

                            completions.add(p.getName());

                        }

                        if(args.length >= 3){
                            switch(args[1]) {
                                case "accept":
                                    completions = new ArrayList<>();
                                    completions.add("<faction id>");
                                    return completions;
                                default:
                                    break;
                            }
                        }
                        return completions;
                    default:
                        break;

                }
            }
            return completions;
        }


        return Collections.emptyList();
    }

}
