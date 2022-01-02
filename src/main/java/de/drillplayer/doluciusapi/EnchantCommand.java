package de.drillplayer.doluciusapi;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EnchantCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[]args){
        if (sender instanceof Player) {
            if (args.length == 2) {
                Player player = (Player) sender;
                String enchantment = args[0];
                int value = Integer.parseInt(args[1]);
                ItemStack item = player.getInventory().getItemInMainHand();
                Enchantment name = EnchantmentWrapper.getByKey(NamespacedKey.minecraft(enchantment));
                if (name != null) {
                    ItemMeta meta = item.getItemMeta();
                    meta.addEnchant(name, value, true);
                    item.setItemMeta(meta);
                    sender.sendMessage(ChatColor.GREEN + "Das Item wurde erfolgreich Enchanted");
                } else {
                    sender.sendMessage(ChatColor.RED + "Ungültiges Enchantment!");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Falscher Syntax! Bitte nutze /enchant <Enchantment> <Wert>");
            }
        } else {
            sender.sendMessage("Diesen Befehl darf nur ein Spieler benutzen");
        }
        return true;
    }
}