package de.drillplayer.doluciusapi;

import de.drillplayer.mysql.MySQL;
import de.drillplayer.mysql.SQLGetter;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Date;

public class BanCommand implements CommandExecutor {

    private static Permission perms = getPermissions();
    public MySQL SQL;
    public SQLGetter data;



    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        setupPermissions();
        String target = args[0];
        Player targetplayer = Bukkit.getPlayerExact(target);
        this.data = new SQLGetter(DoluciusAPIMain.getInstance());
        data.createBanPlayer(targetplayer);
        if (args.length != 3) {
            sender.sendMessage("§cFalsche Syntax! Bitte nutze: /tempban <Spieler> <Zeit> <Grund>");
        } else if (args.length == 3) {
            if (Bukkit.getOnlinePlayers().contains(targetplayer)) {
                if (sender instanceof ConsoleCommandSender || sender instanceof CommandBlock) {
                    tempbanPlayer(args, target, targetplayer, sender);
                } else {
                    Player player = (Player) sender;
                    if (perms.playerInGroup(player, "owner")) {
                        tempbanPlayer(args, target, targetplayer, sender);
                    } else if (perms.playerInGroup(player, "admin")) {
                        if (!perms.playerInGroup(targetplayer, "owner") || !perms.playerInGroup(targetplayer, "admin")) {
                            tempbanPlayer(args, target, targetplayer, sender);
                            } else {
                                player.sendMessage("§cDu hast keine Rechte diesen Befehl auszuführen!");
                            }
                    } else if (perms.playerInGroup(player, "dev")) {
                        if (!perms.playerInGroup(targetplayer, "owner") || !perms.playerInGroup(targetplayer, "admin")) {
                                tempbanPlayer(args, target, targetplayer, sender);
                        } else {
                            player.sendMessage("§cDu hast keine Rechte diesen Befehl auszuführen!");
                        }
                    } else if (perms.playerInGroup(player, "mod") || perms.playerInGroup(player, "architekt") || perms.playerInGroup(player, "sup") ) {
                        tempbanPlayer(args, target, targetplayer, sender);
                    } else {
                        player.sendMessage("§cDu hast keine Rechte diesen Befehl auszuführen!");
                    }
                }
            } else {
                sender.sendMessage("§cDer angegebene Spieler ist nicht online!");
            }
        }
        return true;
    }


    public void tempbanPlayer (String[] args, String target, Player targetplayer, CommandSender sender) {
        if (args.length > 0) {
                if (targetplayer != null) {
                    Bukkit.getLogger().info("Target: " + target);
                    int[] bantimeint = new int[1];
                    bantimeint[0] = Integer.parseInt(args[1]);
                    Date date = new Date(System.currentTimeMillis() + 60 * 60 * bantimeint[0] * 1000);
                    String reason = args[2];
                    BanList banlist = Bukkit.getServer().getBanList(BanList.Type.NAME);
                    String bumper = org.apache.commons.lang.StringUtils.repeat("\n", 35);
                    banlist.addBan(target, bumper + "Du wurdest vom Server gebannt! \n Grund: " + reason + " \nAblauf des Banns:\n" + ChatColor.RED + date.toString() + bumper, date, null);
                    targetplayer.kickPlayer("Du wurdest vom Server gebannt! \n Grund: " + reason + " \nAblauf des Banns:\n" + ChatColor.RED + date.toString());
                    sender.sendMessage("§aDer Spieler " + target + " wurde erfolgreich gebannt. Ablaufdatum: " + ChatColor.RED + date.toString());
                    data.addBan(targetplayer.getUniqueId(), reason, bantimeint[0]);
                } else {
                    sender.sendMessage("Der Spieler '" + target + "' ist nicht online");
                }
        } else {
            sender.sendMessage("§cFalsche Syntax! Bitte nutze: /tempban <Spieler> <Zeit> <Grund>");
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
