package xyz.acrylicstyle.doubletimecommands;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.acrylicstyle.doubletimecommands.commands.KickAll;
import xyz.acrylicstyle.doubletimecommands.commands.Maintenance;
import xyz.acrylicstyle.doubletimecommands.commands.ResetNickname;
import xyz.acrylicstyle.doubletimecommands.commands.ResetPrefix;
import xyz.acrylicstyle.doubletimecommands.commands.SetGamemodeOnJoin;
import xyz.acrylicstyle.doubletimecommands.commands.SetNickname;
import xyz.acrylicstyle.doubletimecommands.commands.SetPrefix;
import xyz.acrylicstyle.doubletimecommands.commands.SetSpawnOnJoin;
import xyz.acrylicstyle.doubletimecommands.events.PlayerChat;
import xyz.acrylicstyle.doubletimecommands.utils.PlayerUtils;
import xyz.acrylicstyle.tomeito_core.providers.ConfigProvider;
import xyz.acrylicstyle.tomeito_core.utils.Log;

public class DoubleTimeCommands extends JavaPlugin implements Listener {
	public static ConfigProvider bungee = null;

	public void onEnable() {
		Log.info(" > Registering events");
		Bukkit.getPluginManager().registerEvents(this, this);
		String apcePriority = null;
		try {
			apcePriority = ConfigProvider.getString("priority.AsyncPlayerChatEvent", "HIGHEST", "DoubleTimeCommands");
			String configPath = ConfigProvider.getString("bungeeConfig", null, "DoubleTimeCommands");
			if (configPath != null) {
				File config = new File(configPath);
				if (config.exists()) bungee = new ConfigProvider(config.getAbsolutePath());
			}
		} catch (IOException | InvalidConfigurationException e) {
			Log.error("An error occurred while fetching event priority of AsyncPlayerChatEvent:");
			e.printStackTrace();
			apcePriority = "HIGHEST";
		}
		Bukkit.getPluginManager().registerEvent(AsyncPlayerChatEvent.class, this, EventPriority.valueOf(apcePriority), new PlayerChat(), this);
		Log.info(" > Registering commands");
		Bukkit.getPluginCommand("setspawnonjoin").setExecutor(new SetSpawnOnJoin());
		Bukkit.getPluginCommand("nick").setExecutor(new SetNickname());
		Bukkit.getPluginCommand("prefix").setExecutor(new SetPrefix());
		Bukkit.getPluginCommand("resetnick").setExecutor(new ResetNickname());
		Bukkit.getPluginCommand("resetprefix").setExecutor(new ResetPrefix());
		Bukkit.getPluginCommand("setgamemodeonjoin").setExecutor(new SetGamemodeOnJoin());
		Bukkit.getPluginCommand("maintenance").setExecutor(new Maintenance());
		Bukkit.getPluginCommand("kickall").setExecutor(new KickAll());
		Log.info(" > Enabled DoubleTimeCommands");
	}

	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerJoin(PlayerJoinEvent e) {
		ConfigProvider config = null;
		try {
			config = new ConfigProvider("./plugins/DoubleTimeCommands/config.yml");
		} catch (Exception ex) {
			Log.error("Error while loading configuration:");
			ex.printStackTrace();
			ex.getCause().printStackTrace();
			return;
		}
		if (config.getBoolean("maintenance", false)) {
			e.getPlayer().sendMessage(ChatColor.GOLD + "Server is currently in maintenance mode!");
		}
		e.getPlayer().setDisplayName(PlayerUtils.getName(e.getPlayer()));
		e.getPlayer().setPlayerListName(PlayerUtils.getName(e.getPlayer()));
		String gamemode = config.getString("gamemodeOnJoin");
		if (gamemode != null) {
			e.getPlayer().setGameMode(GameMode.valueOf(gamemode));
		}
		if ((config.getBoolean("spawnOnJoin", false)) &&
			(!e.getPlayer().performCommand("spawn"))) {
			e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
		}
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerLogin(PlayerLoginEvent e) {
		try {
			if (!ConfigProvider.getBoolean("maintenance", false, "DoubleTimeCommands")) { // if NOT in maintenance
				e.allow();
				return;
			}
			// if in maintenance
			if (!Bukkit.getWhitelistedPlayers().contains(Bukkit.getOfflinePlayer(e.getPlayer().getUniqueId()))) {
				e.disallow(Result.KICK_OTHER, ChatColor.RED + "This server is currently in " + ChatColor.GOLD + "maintenance" + ChatColor.RED + " mode!\nPlease try again later.");
				return;
			} else {
				e.allow();
			}
		} catch (IOException | InvalidConfigurationException e1) {
			Log.error("Exception while processing login of " + e.getPlayer().getUniqueId());
			e1.printStackTrace();
		}
	}
}
