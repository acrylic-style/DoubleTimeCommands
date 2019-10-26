package xyz.acrylicstyle.doubletimecommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.acrylicstyle.doubletimecommands.commands.*;
import xyz.acrylicstyle.doubletimecommands.events.PlayerChat;
import xyz.acrylicstyle.doubletimecommands.events.PlayerCommandPreprocess;
import xyz.acrylicstyle.doubletimecommands.events.PlayerMove;
import xyz.acrylicstyle.doubletimecommands.utils.PlayerUtils;
import xyz.acrylicstyle.tomeito_core.providers.ConfigProvider;
import xyz.acrylicstyle.tomeito_core.utils.Callback;
import xyz.acrylicstyle.tomeito_core.utils.Log;
import xyz.acrylicstyle.tomeito_core.utils.Ranks;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class DoubleTimeCommands extends JavaPlugin implements Listener {
    public static ConfigProvider bungee = null;
    public static File file = null;
    private static ConfigProvider config = null;

    public void onEnable() {
        Log.info(" > Loading config");
        try {
            config = new ConfigProvider("./plugins/DoubleTimeCommands/config.yml");
        } catch (Exception ex) {
            Log.error("Error while loading configuration:");
            ex.printStackTrace();
            ex.getCause().printStackTrace();
            return;
        }
        Log.info(" > Registering events");
        Bukkit.getPluginManager().registerEvents(this, this);
        String apcePriority; // apce = AsyncPlayerChatEvent
        String pcppPriority = null; // pcpp = PlayerCommandPreProcessEvent
        try {
            apcePriority = ConfigProvider.getString("priority.AsyncPlayerChatEvent", "HIGHEST", "DoubleTimeCommands");
        } catch (IOException | InvalidConfigurationException e) {
            Log.error("An error occurred while fetching event priority of AsyncPlayerChatEvent:");
            e.printStackTrace();
            apcePriority = "HIGHEST";
        }
        try {
            pcppPriority = ConfigProvider.getString("priority.PlayerCommandPreprocessEvent", "HIGHEST", "DoubleTimeCommands");
        } catch (IOException | InvalidConfigurationException e) {
            Log.error("An error occurred while fetching event priority of PlayerCommandPreprocess:");
            e.printStackTrace();
            apcePriority = "HIGHEST";
        }
        try {
            String configPath = ConfigProvider.getString("bungeeConfig", null, "DoubleTimeCommands");
            if (configPath != null) {
                File config = new File(configPath);
                file = config;
                if (config.exists()) bungee = new ConfigProvider(config.getAbsolutePath());
            }
        } catch (IOException | InvalidConfigurationException e) {
            Log.error("An error occurred while fetching BungeeCord config from config.yml:");
            e.printStackTrace();
        }
        Bukkit.getPluginManager().registerEvent(AsyncPlayerChatEvent.class, this, EventPriority.valueOf(apcePriority), new PlayerChat(), this);
        Bukkit.getPluginManager().registerEvent(PlayerCommandPreprocessEvent.class, this, EventPriority.valueOf(pcppPriority), new PlayerCommandPreprocess(), this);
        if (config.getBoolean("voidless", false)) Bukkit.getPluginManager().registerEvent(PlayerMoveEvent.class, this, EventPriority.NORMAL, new PlayerMove(), this);
        Log.info(" > Registering commands");
        Objects.requireNonNull(Bukkit.getPluginCommand("setspawnonjoin")).setExecutor(new SetSpawnOnJoin());
        Objects.requireNonNull(Bukkit.getPluginCommand("nick")).setExecutor(new SetNickname());
        Objects.requireNonNull(Bukkit.getPluginCommand("prefix")).setExecutor(new SetPrefix());
        Objects.requireNonNull(Bukkit.getPluginCommand("resetnick")).setExecutor(new ResetNickname());
        Objects.requireNonNull(Bukkit.getPluginCommand("resetprefix")).setExecutor(new ResetPrefix());
        Objects.requireNonNull(Bukkit.getPluginCommand("setgamemodeonjoin")).setExecutor(new SetGamemodeOnJoin());
        Objects.requireNonNull(Bukkit.getPluginCommand("maintenance")).setExecutor(new Maintenance());
        Objects.requireNonNull(Bukkit.getPluginCommand("kickall")).setExecutor(new KickAll());
        Objects.requireNonNull(Bukkit.getPluginCommand("refreshrank")).setExecutor(new RefreshRank());
        Objects.requireNonNull(Bukkit.getPluginCommand("transfer")).setExecutor(new Transfer());
        Log.info(" > Enabled DoubleTimeCommands");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (config.getBoolean("maintenance", false)) {
            e.getPlayer().sendMessage(ChatColor.GOLD + "Server is currently in maintenance mode!");
        }
        new BukkitRunnable() {
            public void run() {
                PlayerUtils.refreshRank(e.getPlayer(), new Callback<Ranks>() {
                    @Override
                    public void done(Ranks rank, Throwable ex) {
                        String name = PlayerUtils.getName(e.getPlayer(), rank);
                        e.getPlayer().setDisplayName(name);
                        e.getPlayer().setPlayerListName(name);
                        if (config.getBoolean("flyable_vip", false) && PlayerUtils.must(Ranks.SAND, e.getPlayer().getUniqueId())) {
                            e.getPlayer().setAllowFlight(true);
                            e.getPlayer().setFlying(true);
                        }
                    }
                });
            }
        }.runTask(this);
        String gamemode = config.getString("gamemodeOnJoin");
        if (gamemode != null) {
            e.getPlayer().setGameMode(GameMode.valueOf(gamemode));
        }
        if ((config.getBoolean("spawnOnJoin", false)) &&
                (!e.getPlayer().performCommand("spawn"))) {
            e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent e) {
        try {
            if (!ConfigProvider.getBoolean("maintenance", false, "DoubleTimeCommands")) { // if NOT in maintenance
                e.allow();
                return;
            }
            // if in maintenance
            if (!Bukkit.getWhitelistedPlayers().contains(Bukkit.getOfflinePlayer(e.getPlayer().getUniqueId()))) {
                e.disallow(Result.KICK_OTHER, ChatColor.RED + "This server is currently in " + ChatColor.GOLD + "maintenance" + ChatColor.RED + " mode!\nPlease try again later.");
            } else {
                e.allow();
            }
        } catch (IOException | InvalidConfigurationException e1) {
            Log.error("Exception while processing login of " + e.getPlayer().getUniqueId());
            e1.printStackTrace();
        }
    }
}
