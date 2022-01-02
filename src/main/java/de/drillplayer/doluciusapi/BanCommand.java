package de.drillplayer.doluciusapi;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class BanCommand implements CommandExecutor {

    private static Permission perms = getPermissions();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        setupPermissions();
        String currdir = System.getProperty("user.dir") + "\\tempban\\";
        Bukkit.getLogger().info("Printing to... " + currdir);
        if (args.length != 3) {
            sender.sendMessage("§cFalsche Syntax! Bitte nutze: /tempban <Spieler> <Zeit> <Grund>");
        } else if (args.length == 3) {
            String target = args[0];
            Player targetplayer = Bukkit.getPlayerExact(target);
            if (Bukkit.getOnlinePlayers().contains(targetplayer)) {
                if (sender instanceof ConsoleCommandSender) {
                    tempbanPlayer(args, target, targetplayer, sender);
                } else if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (perms.playerInGroup(player, "owner")) {
                        tempbanPlayer(args, target, targetplayer, sender);
                    } else if (perms.playerInGroup(player, "admin")) {
                        if (!perms.playerInGroup(targetplayer, "owner")) {
                            if (!perms.playerInGroup(targetplayer, "admin")) {
                                tempbanPlayer(args, target, targetplayer, sender);
                            } else {
                                player.sendMessage("§cDu hast keine Rechte diesen Befehl auszuführen!");
                            }
                        } else {
                            player.sendMessage("§cDu hast keine Rechte diesen Befehl auszuführen!");
                        }
                    } else if (perms.playerInGroup(player, "dev")) {
                        if (!perms.playerInGroup(targetplayer, "owner")) {
                            if (!perms.playerInGroup(targetplayer, "admin")) {
                                tempbanPlayer(args, target, targetplayer, sender);
                            }
                        }
                    } else if (perms.playerInGroup(player, "mod")) {
                        if (!perms.playerInGroup(targetplayer, "owner")) {
                            if (!perms.playerInGroup(targetplayer, "admin")) {
                                if (!perms.playerInGroup(targetplayer, "dev")) {
                                    if (!perms.playerInGroup(targetplayer, "mod")) {
                                        if (!perms.playerInGroup(targetplayer, "architekt")) {
                                            if (!perms.playerInGroup(targetplayer, "sup")) {
                                                tempbanPlayer(args, target, targetplayer, sender);
                                            } else {
                                                player.sendMessage("§cDu hast keine Rechte diesen Befehl auszuführen!");
                                            }
                                        } else {
                                            player.sendMessage("§cDu hast keine Rechte diesen Befehl auszuführen!");
                                        }
                                    } else {
                                        player.sendMessage("§cDu hast keine Rechte diesen Befehl auszuführen!");
                                    }
                                } else {
                                    player.sendMessage("§cDu hast keine Rechte diesen Befehl auszuführen!");
                                }
                            } else {
                                player.sendMessage("§cDu hast keine Rechte diesen Befehl auszuführen!");
                            }
                        } else {
                            player.sendMessage("§cDu hast keine Rechte diesen Befehl auszuführen!");
                        }
                    } else {
                        player.sendMessage("§cDu hast keine Rechte diesen Befehl auszuführen!");
                    }
                    //a
                } else {
                    assert false;
                    sender.sendMessage("§cDu hast keine Rechte diesen Befehl auszuführen!");
                }
            } else {
                sender.sendMessage("§cDer angegebene Spieler ist nicht online!");
            }
        }
        return true;
            }
    
    public void createFile(String filename){
        try {
            File myObj = new File(filename);
            if (myObj.createNewFile()) {
                Bukkit.getLogger().info("File created: " + myObj.getName());
            } else {
                Bukkit.getLogger().info("File already exists.");
            }
        } catch (IOException e) {
            Bukkit.getLogger().info("An error occurred.");
            e.printStackTrace();
        }
    }

    public void writeToFile(String filename, String text){
        try {
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write(text);
            myWriter.close();
            Bukkit.getLogger().info("Successfully wrote to the file.");
        } catch (IOException e) {
            Bukkit.getLogger().info("An error occurred.");
            e.printStackTrace();
        }
    }

    public void tempbanPlayer (String[] args, String target, Player targetplayer, CommandSender sender) {
        new File(System.getProperty("user.dir") + "\\tempban").mkdirs();
        String currdir = System.getProperty("user.dir") + "\\tempban\\";
        Bukkit.getLogger().info("Printing to... " + currdir);
        if (args.length > 0) {
                if (targetplayer != null) {
                    Bukkit.getLogger().info("Target: " + target);
                    String bantime = args[1];
                    int[] bantimeint = new int[1];
                    bantimeint[0] = Integer.parseInt(args[1]);
                    Date date = new Date(System.currentTimeMillis() + 60 * 60 * bantimeint[0] * 1000);
                    String reason = args[2];
                    new File(currdir + "\\" + target).mkdirs();
                    createFile(currdir + "\\" + target + "\\bandata.txt");
                    writeToFile(currdir + target + "\\bandata.txt", date + "\n" + reason);
                    Bukkit.getLogger().info(bantime);
                    BanList banlist = Bukkit.getServer().getBanList(BanList.Type.NAME);
                    String bumper = org.apache.commons.lang.StringUtils.repeat("\n", 35);
                    banlist.addBan(target, bumper + "Du wurdest vom Server gebannt! \n Grund: " + reason + " \nAblauf des Banns:\n" + date + bumper, date, null);
                    targetplayer.kickPlayer("Du wurdest vom Server gebannt! \n Grund: " + reason + " \nAblauf des Banns:\n" + date);
                    sender.sendMessage("§aDer Spieler " + target + " wurde erfolgreich gebannt. Ablaufdatum: " + date);


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
