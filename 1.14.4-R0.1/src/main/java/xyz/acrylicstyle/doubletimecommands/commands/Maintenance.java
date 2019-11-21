package xyz.acrylicstyle.doubletimecommands.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import xyz.acrylicstyle.tomeito_core.providers.ConfigProvider;
import xyz.acrylicstyle.tomeito_core.utils.Log;

import java.io.IOException;

public class Maintenance implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		try {
			ConfigProvider.setThenSave("maintenance", !ConfigProvider.getBoolean("maintenance", false, "DoubleTimeCommands"), "DoubleTimeCommands");
			sender.sendMessage(ChatColor.GREEN + "Turned " + (!ConfigProvider.getBoolean("maintenance", false, "DoubleTimeCommands") ? "off" : "on") + " maintenance mode.");
		} catch (IOException | InvalidConfigurationException e) {
			Log.error("Unknown error while setting maintenance mode:");
			e.printStackTrace();
			sender.sendMessage(ChatColor.RED + "An error occurred while setting maintenance mode");
		}
		return true;
	}
}
