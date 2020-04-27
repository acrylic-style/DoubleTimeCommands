package xyz.acrylicstyle.doubletimecommands.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.acrylicstyle.tomeito_api.providers.ConfigProvider;
import xyz.acrylicstyle.tomeito_api.utils.Log;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SetSpawnOnJoin implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		ConfigProvider config = null;
		try {
			config = new ConfigProvider("./plugins/DoubleTimeCommands/config.yml");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			Log.error("Error while loading configuration:");
			e.printStackTrace();
			e.getCause().printStackTrace();
			sender.sendMessage(ChatColor.RED + "Couldn't read config! Please try again later.");
			return true;
		}
		if (config == null) {
			sender.sendMessage(ChatColor.RED + "Couldn't read config! Please try again later.");
			return true;
		}
		Boolean value = args.length == 0 ? !config.getBoolean("spawnOnJoin", false) : Boolean.parseBoolean(args[0]);
		try {
			config.setThenSave("spawnOnJoin", value);
		} catch (IOException e) {
			Log.error("Couldn't save config:");
			e.printStackTrace();
			sender.sendMessage(ChatColor.RED + "Couldn't save config! Please try again later.");
			return true;
		}
		sender.sendMessage(ChatColor.GREEN + "Config \"spawnOnJoin\" has set to: " + value);
		return true;
	}
}
