package de.drillplayer.doluciusapi;

import de.drillplayer.mysql.MySQL;
import de.drillplayer.mysql.SQLGetter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinsCommand implements CommandExecutor {

    public MySQL SQL;
    public SQLGetter data;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        this.data = new SQLGetter(DoluciusAPIMain.getInstance());
        if (sender instanceof Player) {
            Player player = (Player) sender;
            sender.sendMessage(ChatColor.GREEN + "Coins: " + data.getCoins(player.getUniqueId()));
        }

        return true;
    }
}