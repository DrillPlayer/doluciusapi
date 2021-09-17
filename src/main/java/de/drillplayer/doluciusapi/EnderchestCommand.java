package de.drillplayer.doluciusapi;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class EnderchestCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            System.out.println("Du bist kein Spieler du Frechdachs!");
            return false;
        }

        Player p = (Player) sender;
        if (p.hasPermission("dapi.ec")) {
            Inventory ec = p.getEnderChest();
            p.openInventory(ec);
            return true;
        }
        return false;
        }

    }

