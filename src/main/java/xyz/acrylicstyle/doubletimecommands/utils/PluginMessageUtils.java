package xyz.acrylicstyle.doubletimecommands.utils;

import xyz.acrylicstyle.doubletimecommands.DoubleTimeCommands;
import xyz.acrylicstyle.tomeito_core.utils.Ranks;

class PluginMessageUtils {
    static void getRank(org.bukkit.entity.Player player, Callback<Ranks> callback) {
        DoubleTimeCommands.pcl.get(player, "rank", player.getUniqueId().toString(), new Callback<String>() {
            @Override
            public void done(String rank) {
                if (rank == null) { // it shouldn't happen... if happened, check the proxy log
                    throw new NullPointerException("Couldn't fetch rank for player: " + player.getUniqueId());
                }
                callback.done(Ranks.valueOf(rank));
            }
        });
    }
}
