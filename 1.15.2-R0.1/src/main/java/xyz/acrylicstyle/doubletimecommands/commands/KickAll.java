package xyz.acrylicstyle.doubletimecommands.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class KickAll implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		StringBuilder reason = new StringBuilder();
		if (args.length != 0) {
			for (String arg : args) reason.append(arg).append(" ");
		}
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (sender instanceof Player) if (player.getUniqueId() == ((Player) sender).getUniqueId()) continue;
			player.kickPlayer(ChatColor.RED + "You have kicked from server with reason: " + reason);
		}
		sender.sendMessage(ChatColor.GREEN + "You've kicked all players with reason: " + reason);
		return true;
	}
}
