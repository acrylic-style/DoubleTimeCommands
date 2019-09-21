package xyz.acrylicstyle.doubletimecommands.utils;

import xyz.acrylicstyle.doubletimecommands.DoubleTimeCommands;
import xyz.acrylicstyle.tomeito_core.utils.Ranks;

public class PluginMessageUtils {
    public static Ranks getRank(org.bukkit.entity.Player player) {
        String rank = (String) DoubleTimeCommands.pcl.get(player, "rank", player.getUniqueId().toString());
        if (rank == null) { // it shouldn't happen... if happened, check the proxy log
            throw new NullPointerException("Couldn't fetch rank for player: " + player.getUniqueId());
        }
        return Ranks.valueOf(rank);
    }
}
