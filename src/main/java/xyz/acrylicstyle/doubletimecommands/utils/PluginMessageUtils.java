package xyz.acrylicstyle.doubletimecommands.utils;

import org.bukkit.entity.Player;
import xyz.acrylicstyle.doubletimecommands.DoubleTimeCommands;
import xyz.acrylicstyle.tomeito_core.utils.Ranks;

class PluginMessageUtils {
    static void getRank(Player player, Callback<Ranks> callback) {
        DoubleTimeCommands.pcl.get(player, "rank", player.getUniqueId().toString(), new Callback<String>() {
            @Override
            public void done(String rank, Throwable e) {
                if (rank == null && e != null) { // it shouldn't happen... if happened, check the proxy log
                    e.printStackTrace();
                    throw new NullPointerException("Couldn't fetch rank for player: " + player.getUniqueId());
                }
                callback.done(Ranks.valueOf(rank), null);
            }
        });
    }
}
