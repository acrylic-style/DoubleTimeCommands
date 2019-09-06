package xyz.acrylicstyle.doubletimecommands.commands;

import java.util.Locale;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import xyz.acrylicstyle.doubletimecommands.providers.ConfigProvider;
import xyz.acrylicstyle.tomeito_core.utils.Log;

public class SetGamemodeOnJoin implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "You need 1 more argument! (any gamemode or null)");
			return true;
		}
		String value = args[0].toUpperCase(Locale.ROOT);
		if (!(value.equalsIgnoreCase("SURVIVAL")
				|| value.equalsIgnoreCase("ADVENTURE")
				|| value.equalsIgnoreCase("CREATIVE")
				|| value.equalsIgnoreCase("SPECTATOR")
				|| value.equalsIgnoreCase("NULL"))) {
			sender.sendMessage(ChatColor.RED + "Gamemode must be survival, adventure, creative, spectator, or null(delete settings). (Given "+ value +")");
			return true;
		}
		try {
			ConfigProvider.setThenSave("gamemodeOnJoin", value.equalsIgnoreCase("NULL") ? null : value, "DoubleTimeCommands");
		} catch (Exception e) {
			Log.error("Couldn't save config:");
			e.printStackTrace();
			sender.sendMessage(ChatColor.RED + "Couldn't save config! Please try again later.");
			return true;
		}
		sender.sendMessage(ChatColor.GREEN + "Config \"gamemodeOnJoin\" has set to: " + value);
		return true;
	}
}
