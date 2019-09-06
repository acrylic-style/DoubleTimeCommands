package xyz.acrylicstyle.doubletimecommands.utils;

import java.util.UUID;

import org.bukkit.OfflinePlayer;

import net.md_5.bungee.api.ChatColor;
import xyz.acrylicstyle.tomeito_core.providers.ConfigProvider;
import xyz.acrylicstyle.tomeito_core.utils.Ranks;

public class PlayerUtils {
	/**
	 * @param something UUID or username.
	 * @param uuid Is "something" uuid or not
	 */
	public static Player getBySomething(String something, boolean uuid) {
		return new Player(something, uuid);
	}

	public static Player getByName(String username) {
		return new Player(username);
	}

	public static Player getByUUID(UUID uuid) {
		return new Player(uuid);
	}

	public static String getName(org.bukkit.entity.Player player) {
		try {
			String rankString = ConfigProvider.getString("players." + player.getUniqueId() + ".rank", "DEFAULT", "DoubleTimeCommands");
			Ranks rank = Ranks.valueOf(rankString);
			return rank.getPrefix() + player.getName();
		} catch (Exception e1) {
			e1.printStackTrace();
			return ChatColor.GRAY + player.getName();
		}
	}

	public static String getName(OfflinePlayer player) {
		try {
			String rankString = ConfigProvider.getString("players." + player.getUniqueId() + ".rank", "DEFAULT", "DoubleTimeCommands");
			Ranks rank = Ranks.valueOf(rankString);
			return rank.getPrefix() + player.getName();
		} catch (Exception e1) {
			e1.printStackTrace();
			return ChatColor.GRAY + player.getName();
		}
	}

	public static Ranks getRank(UUID uuid) {
		try {
			return Ranks.valueOf(ConfigProvider.getString("players." + uuid + ".rank", "DEFAULT", "DoubleTimeCommands"));
		} catch (Exception e) { return Ranks.DEFAULT; }
	}
}
