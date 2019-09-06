package xyz.acrylicstyle.doubletimecommands.events;

import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.EventExecutor;

import xyz.acrylicstyle.doubletimecommands.utils.PlayerUtils;

public class PlayerChat implements EventExecutor {
	@Override
	public void execute(Listener listener, Event event) throws EventException {
		AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) event;
		e.getPlayer().setDisplayName(PlayerUtils.getName(e.getPlayer()));
		e.getPlayer().setPlayerListName(PlayerUtils.getName(e.getPlayer()));
		e.setFormat(ChatColor.translateAlternateColorCodes('&', PlayerUtils.getName(e.getPlayer()) + "&r&f: " + e.getMessage()));
	}
}
