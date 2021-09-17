package de.drillplayer.doluciusapi;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class IPCommand implements CommandExecutor {

    private static Permission perms = getPermissions();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        setupPermissions();
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (perms.playerInGroup(player, "owner")) {
                    getIP(player, args);
                } else if (perms.playerInGroup(player, "admin")) {
                    getIP(player, args);
                } else if (perms.playerInGroup(player, "dev")) {
                    getIP(player, args);
                } else {
                    sender.sendMessage(ChatColor.RED + "Du hast keine Rechte diesen Befehl auszuführen!");
                }
            } else {
                getIP(sender, args);
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

    public void getIP(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (DoluciusAPIMain.getIP().getString("ips.players." + args[0] + ".ip") != null) {
                sender.sendMessage(ChatColor.GREEN + "Die IP des Spielers " + ChatColor.YELLOW + args[0] + ChatColor.GREEN + " ist " + ChatColor.YELLOW + DoluciusAPIMain.getIP().getString("ips.players." + args[0] + ".ip"));
            } else {
                sender.sendMessage(ChatColor.RED + "Der Spieler " + ChatColor.YELLOW + args[0] + ChatColor.RED + " war noch nie online!");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Falscher Syntax! Bitte nutze /ip <Spieler>!");
        }
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
