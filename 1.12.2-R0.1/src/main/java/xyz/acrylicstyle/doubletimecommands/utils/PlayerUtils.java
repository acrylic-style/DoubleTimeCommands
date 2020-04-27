package xyz.acrylicstyle.doubletimecommands.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import util.CollectionStrictSync;
import util.DataSerializer;
import xyz.acrylicstyle.doubletimecommands.DoubleTimeCommands;
import xyz.acrylicstyle.tomeito_api.providers.ConfigProvider;
import xyz.acrylicstyle.tomeito_api.utils.Callback;
import xyz.acrylicstyle.tomeito_api.utils.Log;
import xyz.acrylicstyle.tomeito_api.utils.PluginMessageUtils;
import xyz.acrylicstyle.tomeito_api.utils.Ranks;

import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

public class PlayerUtils {
    private static final CollectionStrictSync<UUID, Ranks> ranks = new CollectionStrictSync<>();

    /**
     * @param something UUID or username.
     * @param uuid Is "something" uuid or not
     */
    public static Player getBySomething(String something, boolean uuid) {
        return new Player(something, uuid);
    }

    public static Player getByName(String username) {
        return new Player(username);
    }

    public static Player getByUUID(UUID uuid) {
        return new Player(uuid);
    }

    public static String getName(org.bukkit.entity.Player player) {
        try {
            //String rankString = ConfigProvider.getString("players." + player.getUniqueId() + ".rank", "DEFAULT", DoubleTimeCommands.file);
            //Ranks rank = Ranks.valueOf(rankString);
            return customPrefixes.getOrDefault(player.getUniqueId(), getRank(player.getUniqueId()).getPrefix()) + player.getCustomName();
        } catch (Exception e1) {
            e1.printStackTrace();
            return ChatColor.GRAY + player.getCustomName();
        }
    }

    public static String getName(org.bukkit.entity.Player player, Ranks rank) {
        return customPrefixes.getOrDefault(player.getUniqueId(), rank.getPrefix()) + player.getCustomName();
    }

    private static final CollectionStrictSync<UUID, String> customPrefixes = new CollectionStrictSync<>();
    public static void getPlayerData(org.bukkit.entity.Player player, Callback<DataSerializer> callback) {
        PluginMessageUtils.get(player, "", "dtc:getplayer", new Callback<String>() {
            @Override
            public void done(String s, Throwable e) {
                try {
                    DataSerializer dataSerializer = DataSerializer.fromString(s);
                    if (dataSerializer.get("customPrefix") != null) customPrefixes.put(player.getUniqueId(), dataSerializer.get("customPrefix") + " ");
                    callback.done(dataSerializer, null);
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    /**
     * Fetches offline player's name.<br>
     * <b>It uses LOCAL config!</b>
     */
    public static String getName(OfflinePlayer player) {
        try {
            String rankString = ConfigProvider.getString("players." + player.getUniqueId() + ".rank", "DEFAULT", DoubleTimeCommands.file);
            Ranks rank = Ranks.valueOf(rankString);
            return rank.getPrefix() + player.getName();
        } catch (Exception e1) {
            e1.printStackTrace();
            return ChatColor.GRAY + player.getName();
        }
    }

    public static Ranks getRank(UUID uuid) {
        return ranks.getOrDefault(uuid, Ranks.DEFAULT);
    }

    public static void refreshRank(org.bukkit.entity.Player player, Callback<Ranks> callback) {
        PluginMessageUtils.getRank(player, new Callback<Ranks>() {
            @Override
            public void done(Ranks rank, Throwable e) {
                ranks.add(player.getUniqueId(), rank);
                callback.done(rank, null);
            }
        });
    }

    /**
     * example: <pre>if (!PlayerUtils.must(Ranks.ADMIN, player)) return; // it sends message automatically, so do only return</pre>
     *
     * @param required Required rank for do something
     * @param sender anything extends CommandSender for check if they have Admin rank
     * @return True if the required rank equals actual rank but console always returns true
     */
    public static boolean must(Ranks required, CommandSender sender) {
        if (!(sender instanceof org.bukkit.entity.Player)) {
            Log.info(sender.getName() + "'s check has been skipped because they're not a Player");
            return true;
        }
        org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
        Ranks actual = PlayerUtils.getRank(player.getUniqueId());
        if (required.ordinal() < actual.ordinal()) {
            player.sendMessage(ChatColor.RED + "You must be " + required.name().toLowerCase(Locale.ROOT) + " or higher to use this command!");
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param required Required rank for do something
     * @return True if the required rank equals actual rank
     * @example if (!PlayerUtils.must(Ranks.ADMIN, uuid)) System.out.println("they dont have permission fuck");
     */
    public static boolean must(Ranks required, UUID uuid) {
        Ranks actual = PlayerUtils.getRank(uuid);
        return required.ordinal() >= actual.ordinal();
    }
}
