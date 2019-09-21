package xyz.acrylicstyle.doubletimecommands.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import util.CollectionSync;
import xyz.acrylicstyle.doubletimecommands.DoubleTimeCommands;
import xyz.acrylicstyle.tomeito_core.providers.ConfigProvider;
import xyz.acrylicstyle.tomeito_core.utils.Log;
import xyz.acrylicstyle.tomeito_core.utils.Ranks;

import java.util.Locale;
import java.util.UUID;

public class PlayerUtils {
	public static CollectionSync<UUID, Ranks> ranks = new CollectionSync<>();

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
			//String rankString = ConfigProvider.getString("players." + player.getUniqueId() + ".rank", "DEFAULT", DoubleTimeCommands.file);
			//Ranks rank = Ranks.valueOf(rankString);
			return getRank(player.getUniqueId()).getPrefix() + player.getName();
		} catch (Exception e1) {
			e1.printStackTrace();
			return ChatColor.GRAY + player.getName();
		}
	}

	/**
	 * Fetches offline player's name.<br>
	 * <b>It uses LOCAL config!</b>
	 */
	public static String getName(OfflinePlayer player) {
		try {
			String rankString = ConfigProvider.getString("players." + player.getUniqueId() + ".rank", "DEFAULT", DoubleTimeCommands.file);
			Ranks rank = Ranks.valueOf(rankString);
			return rank.getPrefix() + player.getName();
		} catch (Exception e1) {
			e1.printStackTrace();
			return ChatColor.GRAY + player.getName();
		}
	}

	public static Ranks getRank(UUID uuid) {
		return ranks.getOrDefault(uuid, Ranks.DEFAULT);
	}

	public static Ranks refreshRank(org.bukkit.entity.Player player) {
		Ranks rank = PluginMessageUtils.getRank(player);
		ranks.add(player.getUniqueId(), rank);
		return rank;
	}

	/**
	 * @param required Required rank for do something
	 * @param sender anything extends CommandSender for check if they have Admin rank
	 * @return True if the required rank equals actual rank but console always returns true
	 * @example if (!PlayerUtils.must(Ranks.ADMIN, player)) return; // it sends message automatically, so do only return
	 */
	public static boolean must(Ranks required, CommandSender sender) {
		if (!(sender instanceof org.bukkit.entity.Player)) {
			Log.info(sender.getName() + "'s check has been skipped because they're not a Player");
			return true;
		}
		org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
		Ranks actual = PlayerUtils.getRank(player.getUniqueId());
		if (required.ordinal() < actual.ordinal()) {
			player.sendMessage(ChatColor.RED + "You must be " + required.name().toLowerCase(Locale.ROOT) + " or higher to use this command!");
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @param required Required rank for do something
	 * @return True if the required rank equals actual rank
	 * @example if (!PlayerUtils.must(Ranks.ADMIN, uuid)) System.out.println("they dont have permission fuck");
	 */
	public static boolean must(Ranks required, UUID uuid) {
		Ranks actual = PlayerUtils.getRank(uuid);
		return required.ordinal() >= actual.ordinal();
	}
}
