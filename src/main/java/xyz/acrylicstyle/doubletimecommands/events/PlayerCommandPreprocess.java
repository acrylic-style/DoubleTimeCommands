package xyz.acrylicstyle.doubletimecommands.events;

import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.EventExecutor;
import xyz.acrylicstyle.doubletimecommands.DoubleTimeCommands;
import xyz.acrylicstyle.doubletimecommands.utils.PlayerUtils;
import xyz.acrylicstyle.tomeito_core.providers.ConfigProvider;
import xyz.acrylicstyle.tomeito_core.utils.Log;
import xyz.acrylicstyle.tomeito_core.utils.Ranks;

public class PlayerCommandPreprocess implements EventExecutor {
    @Override
    public void execute(Listener listener, Event e) {
        Log.debug("called");
        PlayerCommandPreprocessEvent event = (PlayerCommandPreprocessEvent) e;
        if (DoubleTimeCommands.bungee == null) return;
        String[] args = event.getMessage().toLowerCase().substring(1).split(" ");
        Log.debug(event.getMessage().toLowerCase().substring(1));
        Ranks rank;
        try {
            String rankStr = ConfigProvider.getString("commands." + args[0], "DEFAULT", DoubleTimeCommands.file);
            rank = Ranks.valueOf(rankStr);
        } catch(Exception ex) {
            event.getPlayer().sendMessage(ChatColor.RED + "An error occurred while processing command");
            ex.printStackTrace();
            return;
        }
        if (!PlayerUtils.must(rank, event.getPlayer())) {
            event.setCancelled(true);
            Log.debug("Cancelled");
        }
    }
}
