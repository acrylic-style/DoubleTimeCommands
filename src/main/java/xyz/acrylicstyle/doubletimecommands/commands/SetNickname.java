package xyz.acrylicstyle.doubletimecommands.commands;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

import xyz.acrylicstyle.doubletimecommands.providers.ConfigProvider;
import xyz.acrylicstyle.doubletimecommands.utils.PlayerUtils;

public class SetNickname implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "This command must run from in-game.");
			return true;
		}
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "You need 1 more argument! (new nick)");
			return true;
		}
		String uuid = null;
		String name = null;
		Player target = null;
		try {
			name = args.length == 2 ? PlayerUtils.getByName(args[1]).toUsername() : ((Player) sender).getName();
			uuid = args.length == 2 ? PlayerUtils.getByName(args[1]).toStringUUID() : ((Player) sender).getUniqueId().toString();
			target = args.length == 2 ? PlayerUtils.getByName(args[1]).getPlayer() : (Player) sender;
			if (name == null || uuid == null) throw new NullPointerException("name or uuid is null!");
		} catch (Exception e1) {
			sender.sendMessage(ChatColor.RED + "Couldn't find player: " + args[1]);
			return true;
		}
		try {
			ConfigProvider.setThenSave("players." + uuid + ".nick", args[0], "DoubleTimeCommands");
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
			e.getCause().printStackTrace();
			sender.sendMessage(ChatColor.RED + "Error while saving nickname! Please try again later.");
			return true;
		}
		sender.sendMessage(ChatColor.GREEN + "Your nickname has been set to: \"" + ChatColor.translateAlternateColorCodes('&', args[0]) + ChatColor.RESET + ChatColor.GREEN + "\"");
		sender.sendMessage(ChatColor.GRAY + "To reset your nickname, type /unnick [player].");
		if (target == null) return true;
		try {
			final String prefix = ConfigProvider.getString("players." + uuid + ".prefix", "", "DoubleTimeCommands");
			final String nick = ConfigProvider.getString("players." + uuid + ".nick", name, "DoubleTimeCommands");
			target.setDisplayName(ChatColor.translateAlternateColorCodes('&', nick));
			target.setPlayerListName(ChatColor.translateAlternateColorCodes('&', (prefix == "" ? "" : prefix + " ") + nick));
		} catch(Exception e) {
			e.printStackTrace();
			e.getCause().printStackTrace();
			sender.sendMessage(ChatColor.RED + "Error while setting your name! Please try again later.");
		}
		return true;
	}
}
