package xyz.acrylicstyle.doubletimecommands.utils;

import xyz.acrylicstyle.doubletimecommands.DoubleTimeCommands;
import xyz.acrylicstyle.tomeito_core.utils.Ranks;

class PluginMessageUtils {
    static Ranks getRank(org.bukkit.entity.Player player) {
        String rank = (String) DoubleTimeCommands.pcl.get(player, "rank", player.getUniqueId().toString(), Ranks.DEFAULT.name());
        if (rank == null) { // it shouldn't happen... if happened, check the proxy log
            throw new NullPointerException("Couldn't fetch rank for player: " + player.getUniqueId());
        }
        return Ranks.valueOf(rank);
    }
}
