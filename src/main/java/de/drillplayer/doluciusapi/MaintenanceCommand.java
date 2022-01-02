package de.drillplayer.doluciusapi;

import net.milkbowl.vault.permission.Permission;
import org.apache.commons.lang.ObjectUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

public class MaintenanceCommand implements CommandExecutor {

    private static Permission perms = getPermissions();

    public static boolean wartung = false;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        setupPermissions();
        if (sender instanceof ConsoleCommandSender) {
            wartung = !wartung;
            activateMaintenance(wartung, perms, sender);
        } else if (sender instanceof Player) {
            if (perms.playerInGroup(((Player) sender).getPlayer(), "owner")) {
                wartung = !wartung;
                activateMaintenance(wartung, perms, sender);
            } else if (perms.playerInGroup(((Player) sender).getPlayer(), "admin")) {
                wartung = !wartung;
                activateMaintenance(wartung, perms, sender);
            } else if (perms.playerInGroup(((Player) sender).getPlayer(), "dev")) {
                wartung = !wartung;
                activateMaintenance(wartung, perms, sender);
            } else {
                sender.sendMessage("§cDu hast keine Rechte diesen Befehl auszuführen!");
            }
        }
        return true;
    }
    public void activateMaintenance (boolean wartung, Permission perms, CommandSender sender) {

        if (wartung) {
            sender.sendMessage("§aDer Wartungsmodus wurde aktiviert!");
        } else {
            sender.sendMessage("§cDer Wartungsmodus wurde deaktiviert!");
        }
        if (wartung) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (!perms.playerInGroup(all, "owner") || !perms.playerInGroup(all, "admin") || !perms.playerInGroup(all, "dev") || !perms.playerInGroup(all, "mod") || !perms.playerInGroup(all, "architekt") || !perms.playerInGroup(all, "sup") ) {
                    all.kickPlayer(ChatColor.RED + "Der Server befindet sich aktuell im " + ChatColor.DARK_RED + "" + ChatColor.BOLD + "Wartungsmodus!");
                }
                }
            }
        }


    public static Permission getPermissions() {
        return perms;
    }



    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }


}
