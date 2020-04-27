package xyz.acrylicstyle.doubletimecommands.events;

import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;
import xyz.acrylicstyle.doubletimecommands.utils.PlayerUtils;
import xyz.acrylicstyle.tomeito_api.utils.Ranks;

public class PlayerChat implements EventExecutor {
	@Override
	public void execute(@NotNull Listener listener, @NotNull Event event) {
		AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) event;
		e.getPlayer().setDisplayName(PlayerUtils.getName(e.getPlayer()));
		e.getPlayer().setPlayerListName(PlayerUtils.getName(e.getPlayer()));
		String message = PlayerUtils.getRank(e.getPlayer().getUniqueId()) == Ranks.DEFAULT ? ChatColor.GRAY + e.getMessage() : ChatColor.WHITE + e.getMessage();
		e.setFormat(ChatColor.translateAlternateColorCodes('&', PlayerUtils.getName(e.getPlayer()) + "&r&f: " + message));
	}
}
