package xyz.acrylicstyle.doubletimecommands.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import util.Collection;
import util.CollectionList;
import xyz.acrylicstyle.doubletimecommands.DoubleTimeCommands;
import xyz.acrylicstyle.tomeito_core.utils.Callback;
import xyz.acrylicstyle.tomeito_core.utils.PluginMessageUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Utils {
    private Utils() {}

    private static ScoreboardManager manager;

    static {
        manager = Bukkit.getScoreboardManager();
    }

    private static Collection<UUID, Scoreboard> scoreboards = new Collection<>();
    private static Collection<UUID, Collection<Integer, String>> scores = new Collection<>();

    public static void morningCall(final UUID player) {
        Scoreboard board = manager.getNewScoreboard();
        final Objective objective = board.registerNewObjective("scoreboard",
                "dummy",
                ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(DoubleTimeCommands.config.getString("scoreboard.name", "Sky Wars")).toUpperCase()),
                RenderType.INTEGER);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        scoreboards.put(player, board);
        Objects.requireNonNull(Bukkit.getPlayer(player)).setScoreboard(board);
        List<String> list = DoubleTimeCommands.config.getStringList("scoreboard.list");
        Collections.reverse(list);
        new CollectionList<>(list).foreach((s, i) -> {
            placeHolder(i, s, objective, player);
        });
    }

    private static void placeHolder(final int score, final String text, final Objective obj, final UUID player) {
        if (text.contains("%points%")) {
            getPoints(Bukkit.getPlayer(player), new Callback<Long>() {
                @Override
                public void done(Long l, Throwable throwable) {
                    String text2 = ChatColor.translateAlternateColorCodes('&', text.replaceAll("%points%", Long.toString(l)));
                    setScore(score, text2, obj, player);
                }
            });
        } else if (text.contains("%experience%")) {
            getPoints(Bukkit.getPlayer(player), new Callback<Long>() {
                @Override
                public void done(Long l, Throwable throwable) {
                    String text2 = ChatColor.translateAlternateColorCodes('&', text.replaceAll("%experience%", Long.toString(l)));
                    setScore(score, text2, obj, player);
                }
            });
        } else if (text.contains("%players%")) {
            PluginMessageUtils.get(Bukkit.getPlayer(player), "", player + ",,,,", "dtc:playing", new Callback<String>() {
                @Override
                public void done(String s, Throwable throwable) {
                    String text2 = ChatColor.translateAlternateColorCodes('&', text.replaceAll("%players%", s));
                    setScore(score, text2, obj, player);
                }
            });
        } else setScore(score, ChatColor.translateAlternateColorCodes('&', text), obj, player);
    }

    public static void setScore(Integer score, String s, Objective objective, UUID uuid) {
        final String text = s + "        ";
        if (score == null) {
            Objects.requireNonNull(objective.getScoreboard()).resetScores(text);
            return;
        }
        if (scores.get(uuid) == null) scores.put(uuid, new Collection<>());
        if (scores.get(uuid).get(score) != null) {
            if (scores.get(uuid).get(score).equalsIgnoreCase(text)) return; // return if name is same as last score entry
            Objects.requireNonNull(objective.getScoreboard()).resetScores(scores.get(uuid).get(score));
        }
        Score scoreObj = objective.getScore(text);
        scoreObj.setScore(score);
        Collection<Integer, String> collection = scores.get(uuid);
        collection.put(score, text);
        scores.put(uuid, collection);
        if (Bukkit.getPlayer(uuid) != null) Bukkit.getPlayer(uuid).setScoreboard(Objects.requireNonNull(objective.getScoreboard())); // no it wont produce npe
    }

    public static void getPoints(Player player, Callback<Long> callback) {
        PluginMessageUtils.get(player, "", "dtc:points", new Callback<String>() {
            @Override
            public void done(String s, Throwable throwable) {
                callback.done(Long.parseLong(s), null);
            }
        });
    }

    public static void getExperience(Player player, Callback<Long> callback) {
        PluginMessageUtils.get(player, "", "dtc:experience", new Callback<String>() {
            @Override
            public void done(String s, Throwable throwable) {
                callback.done(Long.parseLong(s), null);
            }
        });
    }

    public static void addPoints(Player player, long points) {
        PluginMessageUtils.get(player, Long.toString(points), "dtc:addpoints", new Callback<String>() {
            @Override
            public void done(String s, Throwable throwable) {}
        });
    }

    public static void addExperience(Player player, long experience) {
        PluginMessageUtils.get(player, Long.toString(experience), "dtc:addexperience", new Callback<String>() {
            @Override
            public void done(String s, Throwable throwable) {}
        });
    }

    public static void removeScores(UUID uuid) {
        scores.remove(uuid);
    }
}
