package xyz.acrylicstyle.doubletimecommands.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.acrylicstyle.doubletimecommands.DoubleTimeCommands;

public class Servers implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command must run from in-game.");
            return true;
        }
        ((Player) sender).openInventory(DoubleTimeCommands.serversGui.getInventory());
        return true;
    }
}
