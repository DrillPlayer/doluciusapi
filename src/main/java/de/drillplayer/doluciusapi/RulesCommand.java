package de.drillplayer.doluciusapi;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class RulesCommand implements CommandExecutor {
    private static HashMap<Player, Boolean> rulesmap = new HashMap<Player, Boolean>();
    private static Set<String> rules = new HashSet<String>();


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            if (!RulesCommand.getRules().contains(sender.getName())) {
                Player player = (Player) sender;
                rules.add(player.getName());
                player.setWalkSpeed(0.2f);
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    player.removePotionEffect(effect.getType());
                }
                player.showPlayer(DoluciusAPIMain.getInstance(), player);
                player.sendMessage(ChatColor.GREEN + "Viel Spaß auf dem Server!");
            } else {
                sender.sendMessage(ChatColor.RED + "Du hast die Regeln bereits akzeptiert!");
            }
        } else {
            sender.sendMessage("Diesen Befehl darf nur ein Spieler ausführen!");
        }
        return true;
    }


    public static Set<String> getRules() {
        return rules;
    }

    }
