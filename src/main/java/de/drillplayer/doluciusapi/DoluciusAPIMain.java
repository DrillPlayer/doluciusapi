package de.drillplayer.doluciusapi;

import de.drillplayer.mysql.MySQL;
import de.drillplayer.mysql.SQLGetter;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.*;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.ArrayUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import java.sql.SQLException;
import java.util.Set;

public class DoluciusAPIMain extends JavaPlugin implements Listener {
    private static Permission perms = getPermissions();
    private static DoluciusAPIMain instance;

    public MySQL SQL;
    public SQLGetter data;

    @Override
    public void onEnable() {
        instance = this;
        World world = Bukkit.getWorld("world");
        Difficulty peaceful = Difficulty.PEACEFUL;
        world.setDifficulty(peaceful);
        getLogger().info("onEnable is called");
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("ec").setExecutor(new EnderchestCommand());
        this.getCommand("tempban").setExecutor(new BanCommand());
        this.getCommand("wartung").setExecutor(new MaintenanceCommand());
        this.getCommand("vanish").setExecutor(new VanishCommand());
        this.getCommand("enchant").setExecutor(new EnchantCommand());
        this.getCommand("coins").setExecutor(new CoinsCommand());
        setupPermissions();
        this.SQL = new MySQL();
        this.data = new SQLGetter(this);

        try {
            SQL.connect();
        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().info("Datenbank ist nicht connected");
        } catch (SQLException throwables) {
            Bukkit.getLogger().info("Datenbank ist nicht connected");
        }

        if (SQL.isConnected()) {
            Bukkit.getLogger().info("Datenbank ist connected");
            data.createCoinTable();
            data.createBanTable();
        }

    }


    @Override
    public void onDisable() {
        getLogger().info("onDisable is called");
        SQL.disconnect();
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    @EventHandler
    public void onReload(ServerLoadEvent event) {
        World world = Bukkit.getWorld("world");
        Difficulty peaceful = Difficulty.PEACEFUL;
        world.setDifficulty(peaceful);
    }

    @EventHandler
    public void motd(ServerListPingEvent event) {
        event.setMotd(ChatColor.GREEN + "Wilkommen auf Dolucius' Projekt-Server!" + ChatColor.RESET + "" + ChatColor.DARK_GRAY +  " ⇉ 1.16.4\n" + ChatColor.GOLD + "" + ChatColor.BOLD + "Viel Spaß!");
        event.setMaxPlayers(Bukkit.getOnlinePlayers().size() + 1);
        if (MaintenanceCommand.wartung) {
            event.setMotd(ChatColor.RED + "§cDer Server befindet sich aktuell im " + ChatColor.DARK_RED + "" + ChatColor.BOLD + "Wartungsmodus!");
            event.setMaxPlayers(-1);
        }
    }


    @EventHandler
    public void onWorldChange(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        loadTablist(player, board);
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        data.createPlayer(event.getPlayer());
        event.setJoinMessage("");
        Player player = event.getPlayer();
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        loadTablist(player, board);

        if (MaintenanceCommand.wartung) {
                if (!perms.playerInGroup(player, "owner") && !perms.playerInGroup(player, "admin") && !perms.playerInGroup(player, "dev") && !perms.playerInGroup(player, "mod") && !perms.playerInGroup(player, "architekt") && !perms.playerInGroup(player, "sup") ) {
                    player.kickPlayer(ChatColor.RED + "Der Server befindet sich aktuell im " + ChatColor.DARK_RED + "" + ChatColor.BOLD + "Wartungsmodus!");
                }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage("");
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        event.setLeaveMessage("");
    }


    private String ownerFormat = "§4[Owner]§r %1$s >> %2$s";
    private String adminFormat = "§c[Admin]§r %1$s >> %2$s";
    private String devFormat = "§b[Developer]§r %1$s >> %2$s";
    private String modFormat = "§2[Mod]§r %1$s >> %2$s";
    private String architektFormat = "§e[Architekt]§r %1$s >> %2$s";
    private String supFormat = "§a[Supporter]§r %1$s >> %2$s";
    private String creatorFormat = "§5[Creator]§r %1$s >> %2$s";
    private String vipFormat = "§d[VIP]§r %1$s >> %2$s";
    private String spielerFormat = "§7[Spieler]§r %1$s >> %2$s";




    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();


            Bukkit.getLogger().info(event.getRecipients().toString());


            if (ArrayUtils.contains(perms.getPlayerGroups(player), "owner")) {
                event.setFormat(ownerFormat);
            } else if (ArrayUtils.contains(perms.getPlayerGroups(player), "admin")) {
                event.setFormat(adminFormat);
            } else if (ArrayUtils.contains(perms.getPlayerGroups(player), "dev")) {
                event.setFormat(devFormat);
            } else if (ArrayUtils.contains(perms.getPlayerGroups(player), "mod")) {
                event.setFormat(modFormat);
            } else if (ArrayUtils.contains(perms.getPlayerGroups(player), "architekt")) {
                event.setFormat(architektFormat);
            } else if (ArrayUtils.contains(perms.getPlayerGroups(player), "sup")) {
                event.setFormat(supFormat);
            } else if (ArrayUtils.contains(perms.getPlayerGroups(player), "creator")) {
                event.setFormat(creatorFormat);
            } else if (ArrayUtils.contains(perms.getPlayerGroups(player), "vip")) {
                event.setFormat(vipFormat);
            } else if (ArrayUtils.contains(perms.getPlayerGroups(player), "default")) {
                event.setFormat(spielerFormat);
            }


    }

    @EventHandler
    public void onMobKill(EntityDeathEvent event) {
        if (event.getEntity().getKiller() instanceof Player) {
            Player player = event.getEntity().getKiller();
            data.addCoins(player.getUniqueId(), 100);
            player.sendMessage(ChatColor.GREEN + "+100 Coins");

        }
    }

    private static void loadTablist (Player player, Scoreboard board) {
        Objective objective = board.registerNewObjective("tab", "dummy", "Tab");
        objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
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

    public static DoluciusAPIMain getInstance() {
        return instance;
    }

    public static Permission getPermissions() {
        return perms;
    }

}