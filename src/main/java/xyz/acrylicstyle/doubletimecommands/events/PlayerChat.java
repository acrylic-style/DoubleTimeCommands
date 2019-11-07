package xyz.acrylicstyle.doubletimecommands.events;

import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.EventExecutor;
import xyz.acrylicstyle.doubletimecommands.utils.PlayerUtils;
import xyz.acrylicstyle.tomeito_core.utils.Ranks;

public class PlayerChat implements EventExecutor {
	@Override
	public void execute(Listener listener, Event event) {
		AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) event;
		e.getPlayer().setDisplayName(e.getPlayer().getName());
		e.getPlayer().setPlayerListName(e.getPlayer().getName());
		String message = PlayerUtils.getRank(e.getPlayer().getUniqueId()) == Ranks.DEFAULT ? ChatColor.GRAY + e.getMessage() : ChatColor.WHITE + e.getMessage();
		e.setFormat(ChatColor.translateAlternateColorCodes('&', e.getPlayer().getName() + "&r&f: " + message));
	}
}
