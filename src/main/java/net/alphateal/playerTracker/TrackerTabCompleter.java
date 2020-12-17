package net.alphateal.playerTracker;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TrackerTabCompleter implements TabCompleter {
    @Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            list.add("create");
            return list;
        } else {
            return null;
        }
	}
}
