package net.alphateal.playerTracker;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

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

            // AHA! Lodestones are the key!
            // Basically, each compass spawned will behave like a lodestone
            // compass with the huntee as the lodestone

            if(huntee == null) {
                sender.sendMessage("Failed to find that player!");
                return false;
            }

            ItemStack tracker = new ItemStack(Material.COMPASS, 1);
            CompassMeta trackerMeta = (CompassMeta)tracker.getItemMeta();
            trackerMeta.setDisplayName(huntee.getDisplayName() + " Tracker");
            trackerMeta.setLodestoneTracked(false);
            trackerMeta.setLodestone(huntee.getLocation());
            tracker.setItemMeta(trackerMeta);

            hunter.getInventory().addItem(tracker);

            sender.sendMessage("Issuing a tracker for " + huntee.getDisplayName());
            sender.sendMessage("Happy hunting!");

            Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    PlayerInventory hunterInv = hunter.getInventory();
                    HashMap<Integer, ? extends ItemStack> allCompii = hunterInv.all(Material.COMPASS);

                    for (int i = 0; i < allCompii.size(); i++) {
                        ItemStack compass = allCompii.get(i);
                        CompassMeta newMeta = (CompassMeta)compass.getItemMeta();
                        String itemName = newMeta.getDisplayName();
                        if (itemName.contains("Tracker")) {
                            String hunteeName = itemName.split(" ")[0];
                            Player huntee = Bukkit.getPlayer(hunteeName);
                            newMeta.setLodestoneTracked(false);
                            newMeta.setLodestone(huntee.getLocation());
                            compass.setItemMeta(newMeta);
                        }
                    }
                }
            }, 0L, 20L);
        }

        return true;
    }
}