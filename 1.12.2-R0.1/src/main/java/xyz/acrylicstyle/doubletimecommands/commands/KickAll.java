package xyz.acrylicstyle.doubletimecommands.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import util.CollectionList;
import util.ICollectionList;

public class KickAll implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CollectionList<String> list = ICollectionList.asList(args);
		list.shift();
		String reason = list.join(" ");
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (sender instanceof Player) if (player.getUniqueId() == ((Player) sender).getUniqueId()) continue;
			player.kickPlayer(ChatColor.RED + "You have kicked from server with reason: " + reason);
		}
		sender.sendMessage(ChatColor.GREEN + "You've kicked all players with reason: " + reason);
		return true;
	}
}
