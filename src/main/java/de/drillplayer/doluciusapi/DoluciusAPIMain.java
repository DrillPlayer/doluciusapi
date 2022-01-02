package de.drillplayer.doluciusapi;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import de.drillplayer.doluciusapi.mysql.MySQL;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.ArrayUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

public class DoluciusAPIMain extends JavaPlugin implements Listener {
    private static Permission perms = getPermissions();
    private ProtocolManager protocolManager;
    private static DoluciusAPIMain instance;
    private File customConfigFile;
    private FileConfiguration customConfig;
    public static File customReportFile;
    private static FileConfiguration customReport;
    public static File customIPFile;
    private static FileConfiguration customIP;

    public MySQL SQL;

    @Override
    public void onEnable() {
        instance = this;
        createReport();
        createCustomConfig();
        createIP();
        World world = Bukkit.getWorld("world");
        Difficulty peaceful = Difficulty.PEACEFUL;
        world.setDifficulty(peaceful);
        getLogger().info("onEnable is called");
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("ec").setExecutor(new EnderchestCommand());
        this.getCommand("tempban").setExecutor(new BanCommand());
        this.getCommand("wartung").setExecutor(new MaintenanceCommand());
        this.getCommand("vanish").setExecutor(new VanishCommand());
        this.getCommand("rulesaccept").setExecutor(new RulesCommand());
        this.getCommand("ip").setExecutor(new IPCommand());
        this.getCommand("uuid").setExecutor(new UUIDCommand());
        setupPermissions();
        this.SQL = new MySQL();

        try {
            SQL.connect();
        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().info("Datenbank ist nicht connected");
        } catch (SQLException throwables) {
            Bukkit.getLogger().info("Datenbank ist nicht connected");
        }

        if (SQL.isConnected()) {
            Bukkit.getLogger().info("Datenbank ist connected");
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
    public void onRespawn(PlayerRespawnEvent event) {
        World world = Bukkit.getWorld("world");
        Location respawn = new Location(world,-312 ,95 ,-7);
        event.setRespawnLocation(respawn);
    }


    @EventHandler
    public void preLogin (AsyncPlayerPreLoginEvent event) throws IOException {
        getIP().set("ips.players." + event.getName() + ".uuid", event.getUniqueId().toString());
        getIP().set("ips.players." + event.getName() + ".ip", event.getAddress());
        getIP().save(customIPFile);
    }



    @EventHandler

    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage("");
        Player player = event.getPlayer();
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        loadTablist(player, board);
        //loadScoreboard(player, board);
        Bukkit.getLogger().info("Wartung: " + String.valueOf(MaintenanceCommand.wartung));
        PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS,Integer.MAX_VALUE,1);
        PotionEffect nojump = new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 250);
        if (!RulesCommand.getRules().contains(player.getName())) {
            player.hidePlayer(this, player);
            TextComponent text = new TextComponent(ChatColor.GREEN + "[AKZEPTIEREN]");
            player.sendMessage(ChatColor.GRAY + "1. Verhaltensregeln \n" +
                    "\n" +
                    "1.1 Anweisungen vom Serverteam sind folge zu leisten! \n" +
                    "\n" +
                    "1.2 Pornographische, rechtsradikale, rassistische Skins und Namen werden hier nicht geduldet. \n" +
                    "\n" +
                    "1.3 Rassistische, rechtsradikale sowie pornographische Aussagen sind strengstens zu unterlassen! \n" +
                    "\n" +
                    "1.4 Werbung in jeglicher Art ist untersagt. \n" +
                    "\n" +
                    "1.5 Jeglicher Echtgeld-Handel ist untersagt. \n" +
                    "\n" +
                    "1.6 Beleidigungen, in jeglicher Sprache, so wie Spam als auch Provokation ist strengstens untersagt. \n" +
                    "\n" +
                    "1.7 Drohungen in jeglicher Art und Weise sind strengstens untersagt. \n" +
                    "\n" +
                    "1.8 Das senden von Links ist zu unterlassen. \n" +
                    "\n" +
                    "1.9 Respektiere die anderen Mitspieler. \n" +
                    "\n" +
                    "1.10 Das Nutzen von Zweitaccounts um unfaire Spielvorteile zu bekommen ist untersagt. \n" +
                    "\n" +
                    "1.11 Dauerhaftes schreiben im Capslock ist verboten. \n" +
                    "\n" +
                    "1.12 Betteln ist untersagt. \n" +
                    "\n" +
                    "1.13 Das benutzen einer VPN ist zu unterlassen. \n" +
                    "\n" +
                    " \n" +
                    "\n" +
                    "2. Regeln rund um Redstone und Farmen \n" +
                    "\n" +
                    "2.1 Das bauen von jeglichen Arten von Clocks ist strengstens untersagt! \n" +
                    "\n" +
                    "2.2 Vollautomatische Farmen sind zu unterlassen, halbautomatische sind akzeptabel. \n" +
                    "\n" +
                    "2.3 Jegliche Art von Lag-Maschinen sind strengstens untersagt! \n" +
                    "\n" +
                    "2.4 AFK-farmen sind untersagt. \n" +
                    "\n" +
                    "2.5 Sehr große Redstone Schaltungen, welche Server lastig sein können sind untersagt. \n" +
                    "\n" +
                    "2.6 Automatische Einlagerungssysteme sind untersagt. \n" +
                    "\n" +
                    " \n" +
                    "\n" +
                    "3. Modifikationen und Bots \n" +
                    "\n" +
                    "3.1 Das Nutzen von Hack-Clients oder allgemein Hacks (X-Ray, Fly Hacks, Autoclicker, Killaura usw.) ist strengstens untersagt. \n" +
                    "\n" +
                    "3.2 Erlaubt sind Modifikationen wie LabyMod, OptiFine, Forge, Fabric, Makromods, Texture-Packs (OHNE Blockfilter) oder Toggle-able Sneak. \n" +
                    "\n" +
                    " \n" +
                    "\n" +
                    "4. Spielfehler \n" +
                    "\n" +
                    "4.1 Das duplizieren von Items ist strengstens untersagt! \n" +
                    "\n" +
                    "4.2 Allgemeines ausnutzen von Spielbugs ist zu unterlassen. \n" +
                    "\n" +
                    " \n" +
                    "\n" +
                    "5. Landschafts-/Bauregeln \n" +
                    "\n" +
                    "5.1 Rechtsradikale, rassistische als auch pornographische bauten sind strengstens zu unterlassen. \n" +
                    "\n" +
                    "5.2 Griefing ist verboten. \n" +
                    "\n" +
                    "5.3 Userfallen sind strengstens untersagt. \n" +
                    "\n" +
                    " \n" +
                    "\n" +
                    "6. Rückerstattung von Items \n" +
                    "\n" +
                    "6.1 Falls ihr Items durch Bugs, Lags oder sonstiges verlieren solltet, ist eine Rückerstattung nicht garantiert. \n" +
                    "\n" +
                    " \n" +
                    "\n" +
                    "7. Abschließende Bestimmungen \n" +
                    "\n" +
                    "7.1 Teammitglieder behalten sich vor, das Verhalten von Spielern auch dann zu bestrafen, wenn es hier nicht als Fehlverhalten aufgelistet ist. \n" +
                    "\n" +
                    "7.2 Die Strafen können je nach Ermessen der Admins und dem Gewicht des „Vergehens“ angepasst werden. \n" +
                    "\n" +
                    "7.3 Die Entscheidung eines Teammitglids, unabhängig von seiner Position, ist absolut und kann nur von der jeweiligen Teamleitung oder einem Mitglied der Administration ungültig gemacht werden." +
                    "\n" +
                    "7.4 Beschwerden über Teammitglieder sollten per Discord über ein Ticket erfolgen. Andere beschwerden über Teammitglieder werden nicht bearbeitet." +
                    "\n" +
                    "7.5 Alle IP Adressen der Spieler werden aus rechtlichen Gründen gespeichert. Diese können ausschließlich von der Administration in Notfällen aufgerufen werden." +
                    "\n" +
                    "7.6 Die Administration behält sich das Recht vor, die Regeln jederzeit zu ändern"+
                    "\n" +
                    " \n" +
                    "\n");
            player.spigot().sendMessage(text);
            text.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/rulesaccept") );
            player.setWalkSpeed(0);
            player.addPotionEffect(blindness);
            player.addPotionEffect(nojump);

        } else {
            player.showPlayer(this, player);
            player.setWalkSpeed(0.2f);
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }
        }
        if (MaintenanceCommand.wartung) {
                if (!perms.playerInGroup(player, "owner") || !perms.playerInGroup(player, "admin") || !perms.playerInGroup(player, "dev") || !perms.playerInGroup(player, "mod") || !perms.playerInGroup(player, "architekt") || !perms.playerInGroup(player, "sup") ) {
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

        if (!RulesCommand.getRules().contains(player.getName())) {
            event.setCancelled(true);
            player.hidePlayer(this, player);
            Set recipients = event.getRecipients();
            for (String p : RulesCommand.getRules()) {
                event.getRecipients().remove(Bukkit.getPlayer(p));
            }
            player.sendMessage(ChatColor.RED + "Du musst zuerst die Regeln akzeptieren!");
        } else {

            Bukkit.getLogger().info(event.getRecipients().toString());

            if (event.getPlayer().getName().equals("Syphica")) {
                event.setFormat("§7[Spieler]§r %1$s >> Ich bin ein spast und marc, nini und nick sind ehrenmänner!");
            } else if (ArrayUtils.contains(perms.getPlayerGroups(player), "owner")) {
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

    private static void loadScoreboard (Player player, Scoreboard board) {
        String rank = "a";

        for (Player all : Bukkit.getOnlinePlayers()) {
            if (perms.playerInGroup(all, "owner")) {
                rank = ChatColor.RED + "[Owner]";
            } else if (perms.playerInGroup(all, "admin")) {
                rank = ChatColor.RED + "[Admin]";
            } else if (perms.playerInGroup(all, "dev")) {
                rank = ChatColor.RED + "[Developer]";
            } else if (perms.playerInGroup(all, "mod")) {
                rank = ChatColor.RED + "[Moderator]";
            } else if (perms.playerInGroup(all, "architekt")) {
                rank = ChatColor.RED + "[Architekt]";
            } else if (perms.playerInGroup(all, "sup")) {
                rank = ChatColor.RED + "[Supporter]";
            } else if (perms.playerInGroup(all, "creator")) {
                rank = ChatColor.RED + "[Creator]";
            } else if (perms.playerInGroup(all, "vip")) {
                rank = ChatColor.RED + "[VIP]";
            } else if (perms.playerInGroup(all, "default")) {
                rank = ChatColor.RED + "[Spieler]";
            }
            Objective objective = board.registerNewObjective("rank", "dummy", "Rank");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName(player.getName());
            Score rankscore = objective.getScore("Dein Rang:");
            rankscore.setScore(5);

            Score rankscore1 = objective.getScore(rank);
            rankscore1.setScore(4);

            Score spacer2 = objective.getScore("§8 ");
            spacer2.setScore(3);

            Score playersonline = objective.getScore("Online Spieler:");
            playersonline.setScore(2);

            Score playersonline1 = objective.getScore(String.valueOf(Bukkit.getOnlinePlayers().size()));
            playersonline1.setScore(1);

            Score spacer3 = objective.getScore("§8 ");
            spacer3.setScore(0);



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

    public FileConfiguration getCustomConfig() {
        return this.customConfig;
    }

    private void createCustomConfig() {
        customConfigFile = new File(getDataFolder(), "custom.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("custom.yml", false);
        }

        customConfig= new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static FileConfiguration getReport() {
        return instance.customReport;
    }

    private void createReport() {
        customReportFile = new File(getDataFolder(), "reports.yml");
        if (!customReportFile.exists()) {
            customReportFile.getParentFile().mkdirs();
            saveResource("reports.yml", false);
        }

        customReport= new YamlConfiguration();
        try {
            customReport.load(customReportFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }


    public static FileConfiguration getIP() {
        return instance.customIP;
    }

    private void createIP() {
        customIPFile = new File(getDataFolder(), "ips.yml");
        if (!customIPFile.exists()) {
            customIPFile.getParentFile().mkdirs();
            saveResource("ips.yml", false);
        }

        customIP= new YamlConfiguration();
        try {
            customIP.load(customIPFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }


}