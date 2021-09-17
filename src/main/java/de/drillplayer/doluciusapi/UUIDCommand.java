package de.drillplayer.doluciusapi;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class UUIDCommand implements CommandExecutor {

    private static Permission perms = getPermissions();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        setupPermissions();
        if (args.length == 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (perms.playerInGroup(player, "owner")) {
                    getUUID(player, args);
                } else if (perms.playerInGroup(player, "admin")) {
                    getUUID(player, args);
                } else if (perms.playerInGroup(player, "dev")) {
                    getUUID(player, args);
                } else if (perms.playerInGroup(player, "mod")) {
                    getUUID(player, args);
                } else if (perms.playerInGroup(player, "sup")) {
                    getUUID(player, args);
                } else {
                    sender.sendMessage(ChatColor.RED + "Du hast keine Rechte diesen Befehl auszuführen!");
                }
            } else {
                getUUID(sender, args);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Falscher Syntax! Bitte nutze /uuid <Spieler>");
        }
        return true;
    }


    public static Permission getPermissions() {
        return perms;
    }




    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    public void getUUID(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (DoluciusAPIMain.getIP().getString("ips.players." + args[0] + ".uuid") != null) {
                sender.sendMessage(ChatColor.GREEN + "Die UUID des Spielers " + ChatColor.YELLOW + args[0] + ChatColor.GREEN + " ist " + ChatColor.YELLOW + DoluciusAPIMain.getIP().getString("ips.players." + args[0] + ".uuid"));
            } else {
                sender.sendMessage(ChatColor.RED + "Der Spieler " + ChatColor.YELLOW + args[0] + ChatColor.RED + " war noch nie online!");
            }
        }
    }

}
