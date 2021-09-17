package de.drillplayer.doluciusapi;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.ArrayUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class VanishCommand implements CommandExecutor {

    private static Permission perms = getPermissions();


    public static boolean vanish = false;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        setupPermissions();
        Plugin plugin = DoluciusAPIMain.getInstance();
        if (sender instanceof Player) {

            Player player = (Player) sender;
                if (perms.playerInGroup(player, "owner")) {
                    vanish = !vanish;
                    if (vanish) {
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            if (perms.playerInGroup(all, "default")) {
                                all.hidePlayer(plugin, player);
                                Scoreboard sb1 = Bukkit.getScoreboardManager().getNewScoreboard();
                                loadTablist(player, sb1);
                            } else if (perms.playerInGroup(all, "vip")) {
                                all.hidePlayer(plugin, player);
                                Scoreboard sb1 = Bukkit.getScoreboardManager().getNewScoreboard();
                                loadTablist(player, sb1);
                            } else if (perms.playerInGroup(all, "creator")) {
                                all.hidePlayer(plugin, player);
                                Scoreboard sb1 = Bukkit.getScoreboardManager().getNewScoreboard();
                                loadTablist(player, sb1);
                            }
                        }
                        player.sendMessage( ChatColor.GREEN + "Vanish wurde aktiviert!");
                    } else {
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            all.showPlayer(plugin, player);
                            Scoreboard sb1 = Bukkit.getScoreboardManager().getNewScoreboard();
                            loadTablist(player, sb1);
                        }
                        player.sendMessage(ChatColor.RED + "Vanish wurde deaktiviert!");
                    }
                } else if (perms.playerInGroup(player, "admin")) {
                        vanish = !vanish;
                        if (vanish) {
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                if (perms.playerInGroup(all, "default")) {
                                    all.hidePlayer(plugin, player);
                                    Scoreboard sb1 = Bukkit.getScoreboardManager().getNewScoreboard();
                                    loadTablist(player, sb1);
                                } else if (perms.playerInGroup(all, "vip")) {
                                    all.hidePlayer(plugin, player);
                                    Scoreboard sb1 = Bukkit.getScoreboardManager().getNewScoreboard();
                                    loadTablist(player, sb1);
                                } else if (perms.playerInGroup(all, "creator")) {
                                    all.hidePlayer(plugin, player);
                                    Scoreboard sb1 = Bukkit.getScoreboardManager().getNewScoreboard();
                                    loadTablist(player, sb1);
                                }
                            }
                            player.sendMessage( ChatColor.GREEN + "Vanish wurde aktiviert!");
                        } else {
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                all.showPlayer(plugin, player);
                                Scoreboard sb1 = Bukkit.getScoreboardManager().getNewScoreboard();
                                loadTablist(player, sb1);
                            }
                            player.sendMessage(ChatColor.RED + "Vanish wurde deaktiviert!");
                        }
                    } else if (perms.playerInGroup(player, "dev")) {
                            vanish = !vanish;
                            if (vanish) {
                                for (Player all : Bukkit.getOnlinePlayers()) {
                                    if (perms.playerInGroup(all, "default")) {
                                        all.hidePlayer(plugin, player);
                                        Scoreboard sb1 = Bukkit.getScoreboardManager().getNewScoreboard();
                                        loadTablist(player, sb1);
                                    } else if (perms.playerInGroup(all, "vip")) {
                                        all.hidePlayer(plugin, player);
                                        Scoreboard sb1 = Bukkit.getScoreboardManager().getNewScoreboard();
                                        loadTablist(player, sb1);
                                    } else if (perms.playerInGroup(all, "creator")) {
                                        all.hidePlayer(plugin, player);
                                        Scoreboard sb1 = Bukkit.getScoreboardManager().getNewScoreboard();
                                        loadTablist(player, sb1);
                                    }
                                }
                                player.sendMessage( ChatColor.GREEN + "Vanish wurde aktiviert!");
                            } else {
                                for (Player all : Bukkit.getOnlinePlayers()) {
                                    all.showPlayer(plugin, player);
                                    Scoreboard sb1 = Bukkit.getScoreboardManager().getNewScoreboard();
                                    loadTablist(player, sb1);
                                }
                                player.sendMessage(ChatColor.RED + "Vanish wurde deaktiviert!");
                            }
                        } else if (perms.playerInGroup(player, "mod")) {
                                vanish = !vanish;
                                if (vanish) {
                                    for (Player all : Bukkit.getOnlinePlayers()) {
                                        if (perms.playerInGroup(all, "default")) {
                                            all.hidePlayer(plugin, player);
                                            Scoreboard sb1 = Bukkit.getScoreboardManager().getNewScoreboard();
                                            loadTablist(player, sb1);
                                        } else if (perms.playerInGroup(all, "vip")) {
                                            all.hidePlayer(plugin, player);
                                            Scoreboard sb1 = Bukkit.getScoreboardManager().getNewScoreboard();
                                            loadTablist(player, sb1);
                                        } else if (perms.playerInGroup(all, "creator")) {
                                            all.hidePlayer(plugin, player);
                                            Scoreboard sb1 = Bukkit.getScoreboardManager().getNewScoreboard();
                                            loadTablist(player, sb1);
                                        }
                                    }
                                    player.sendMessage( ChatColor.GREEN + "Vanish wurde aktiviert!");
                                } else {
                                    for (Player all : Bukkit.getOnlinePlayers()) {
                                        all.showPlayer(plugin, player);
                                        Scoreboard sb1 = Bukkit.getScoreboardManager().getNewScoreboard();
                                        loadTablist(player, sb1);
                                    }
                                    player.sendMessage(ChatColor.RED + "Vanish wurde deaktiviert!");
                                }
                            } else if (perms.playerInGroup(player, "sup")) {
                                    vanish = !vanish;
                                        if (vanish) {
                                            for (Player all : Bukkit.getOnlinePlayers()) {
                                                if (perms.playerInGroup(all, "default")) {
                                                    all.hidePlayer(plugin, player);
                                                    Scoreboard sb1 = Bukkit.getScoreboardManager().getNewScoreboard();
                                                    loadTablist(player, sb1);
                                                } else if (perms.playerInGroup(all, "vip")) {
                                                        all.hidePlayer(plugin, player);
                                                        Scoreboard sb1 = Bukkit.getScoreboardManager().getNewScoreboard();
                                                        loadTablist(player, sb1);
                                                } else if (perms.playerInGroup(all, "creator")) {
                                                            all.hidePlayer(plugin, player);
                                                            Scoreboard sb1 = Bukkit.getScoreboardManager().getNewScoreboard();
                                                            loadTablist(player, sb1);
                                                }
                                            }
                                            player.sendMessage( ChatColor.GREEN + "Vanish wurde aktiviert!");
                                        } else {
                                            for (Player all : Bukkit.getOnlinePlayers()) {
                                            all.showPlayer(plugin, player);
                                            Scoreboard sb1 = Bukkit.getScoreboardManager().getNewScoreboard();
                                            loadTablist(player, sb1);
                                        }
                                            player.sendMessage(ChatColor.RED + "Vanish wurde deaktiviert!");
                                    }
                                } else {
                                    player.sendMessage(ChatColor.RED + "Du hast keine Rechte um diesen Befehl auszuführen!");
                                }
        } else {
            assert sender != null;
            sender.sendMessage(ChatColor.RED + "Diesen Befehl darf nur ein Spieler ausführen!");
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

    private static void loadTablist(Player player, Scoreboard board) {

        Team owner = board.registerNewTeam("a");
        Team admin = board.registerNewTeam("b");
        Team dev = board.registerNewTeam("c");
        Team mod = board.registerNewTeam("d");
        Team architekt = board.registerNewTeam("e");
        Team sup = board.registerNewTeam("f");
        Team creator = board.registerNewTeam("g");
        Team vip = board.registerNewTeam("h");
        Team spieler = board.registerNewTeam("i");


        admin.setPrefix("§c[Admin] ");
        owner.setPrefix("§4[Owner] ");
        dev.setPrefix("§b[Developer] ");
        mod.setPrefix("§2[Moderator] ");
        architekt.setPrefix("§e[Architekt] ");
        sup.setPrefix("§a[Supporter] ");
        creator.setPrefix("§5[Creator] ");
        vip.setPrefix("§d[VIP] ");
        spieler.setPrefix("§7[Spieler] ");

        for (Player all : Bukkit.getOnlinePlayers()) {
            if (ArrayUtils.contains(perms.getPlayerGroups(all), "owner")) {
                owner.addEntry(all.getName());
            } else if (ArrayUtils.contains(perms.getPlayerGroups(all), "admin")) {
                admin.addEntry(all.getName());
            } else if (ArrayUtils.contains(perms.getPlayerGroups(all), "dev")) {
                dev.addEntry(all.getName());
            } else if (ArrayUtils.contains(perms.getPlayerGroups(all), "mod")) {
                mod.addEntry(all.getName());
            } else if (ArrayUtils.contains(perms.getPlayerGroups(all), "architekt")) {
                architekt.addEntry(all.getName());
            } else if (ArrayUtils.contains(perms.getPlayerGroups(all), "sup")) {
                sup.addEntry(all.getName());
            } else if (ArrayUtils.contains(perms.getPlayerGroups(all), "creator")) {
                creator.addEntry(all.getName());
            } else if (ArrayUtils.contains(perms.getPlayerGroups(all), "vip")) {
                vip.addEntry(all.getName());
            } else if (ArrayUtils.contains(perms.getPlayerGroups(all), "default")) {
                spieler.addEntry(all.getName());
            }

            all.setScoreboard(board);
            player.setScoreboard(board);
        }

    }

    public void setVanish(Plugin plugin, Player player) {
        for (Player all : Bukkit.getOnlinePlayers()) {
            if (vanish) {
                if (perms.playerInGroup(all, "default")) {
                    all.hidePlayer(plugin, player);
                    Scoreboard sb1 = Bukkit.getScoreboardManager().getNewScoreboard();
                    loadTablist(player, sb1);
                } else if (perms.playerInGroup(all, "vip")) {
                    all.hidePlayer(plugin, player);
                    Scoreboard sb1 = Bukkit.getScoreboardManager().getNewScoreboard();
                    loadTablist(player, sb1);
                } else if (perms.playerInGroup(all, "creator")) {
                    all.hidePlayer(plugin, player);
                    Scoreboard sb1 = Bukkit.getScoreboardManager().getNewScoreboard();
                    loadTablist(player, sb1);

                }
            } else {
                all.showPlayer(plugin, player);
                Scoreboard sb1 = Bukkit.getScoreboardManager().getNewScoreboard();
                loadTablist(player, sb1);
            }
        }

    }
}
