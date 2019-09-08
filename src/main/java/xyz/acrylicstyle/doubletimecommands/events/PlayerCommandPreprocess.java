package xyz.acrylicstyle.doubletimecommands.events;

import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.EventExecutor;
import xyz.acrylicstyle.doubletimecommands.DoubleTimeCommands;
import xyz.acrylicstyle.doubletimecommands.utils.PlayerUtils;
import xyz.acrylicstyle.tomeito_core.utils.Ranks;

import java.util.regex.Pattern;

public class PlayerCommandPreprocess implements EventExecutor {
    @Override
    public void execute(Listener listener, Event e) throws EventException {
        PlayerCommandPreprocessEvent event = (PlayerCommandPreprocessEvent) e;
        if (DoubleTimeCommands.bungee == null) return;
        String[] args = event.getMessage().replaceAll(Pattern.quote("/"), "").split(" ");
        String rankStr = DoubleTimeCommands.bungee.getString("commands." + args[0], "DEFAULT");
        Ranks rank;
        try {
            rank = Ranks.valueOf(rankStr);
        } catch(Exception ex) {
            event.getPlayer().sendMessage(ChatColor.RED + "An error occurred while processing command");
            ex.printStackTrace();
            return;
        }
        if (!PlayerUtils.must(rank, event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
