package xyz.acrylicstyle.doubletimecommands.events;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.EventExecutor;

public class PlayerMove implements EventExecutor {
    @Override
    public void execute(Listener listener, Event event) {
        PlayerMoveEvent e = (PlayerMoveEvent) event;
        if (e.getPlayer().getLocation().getY() < 0) if (!e.getPlayer().performCommand("spawn")) e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
    }
}