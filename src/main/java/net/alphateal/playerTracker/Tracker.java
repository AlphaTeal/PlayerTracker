package net.alphateal.playerTracker;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

public class Tracker implements CommandExecutor {

    private JavaPlugin plugin;

    public Tracker(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Command must be run by a player!"); // Otherwise who gets the compass?
            return false;
        }

        if(args.length < 1) {
            return false;
        }

        if(args[0].equalsIgnoreCase("create")) {
            Player hunter = (Player)sender;
            Player huntee = Bukkit.getPlayer(args[1]);

            // can setCompassTarget and getCompassTarget
            // But I don't think it lets you have more than one

            // Might need to add something to the scheduler too

            // AHA! Lodestones are the key!
            // Basically, each compass spawned will behave like a lodestone
            // compass with the huntee as the lodestone

            if(huntee == null) {
                sender.sendMessage("Failed to find that player!");
                return false;
            }

            Location hunteeLocation = huntee.getLocation();
            hunter.setCompassTarget(hunteeLocation);

            // /tracker create DjBrownie
            // Should spawn a new compass that always points to DjBrownie
            
            ItemStack tracker = new ItemStack(Material.COMPASS);
            tracker.setAmount(1);
            CompassMeta trackerMeta = (CompassMeta)tracker.getItemMeta();
            trackerMeta.setDisplayName(huntee.getDisplayName() + " Tracker");
            trackerMeta.setLodestoneTracked(true);
            trackerMeta.setLodestone(hunteeLocation); //Might need to schedule

            tracker.setItemMeta(trackerMeta);

            hunter.getInventory().addItem(tracker);

            sender.sendMessage("Issuing a tracker for " + huntee.getDisplayName());
            sender.sendMessage("Happy hunting!");

            Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    sender.sendMessage("Refreshing " + huntee.getDisplayName() + " tracker...");
                    Location hunteeLocation = huntee.getLocation();
                    trackerMeta.setLodestoneTracked(true);
                    trackerMeta.setLodestone(hunteeLocation);
                    trackerMeta.setDisplayName("DEBUG");
                    tracker.setItemMeta(trackerMeta);
                }
            }, 0L, 10L);
        }

        return true;
    }
}