package xyz.acrylicstyle.doubletimecommands.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import util.Collection;
import util.CollectionList;
import xyz.acrylicstyle.doubletimecommands.DoubleTimeCommands;
import xyz.acrylicstyle.tomeito_core.utils.Callback;
import xyz.acrylicstyle.tomeito_core.utils.PluginMessageUtils;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Utils {
    private Utils() {}

    private static Collection<UUID, Collection<Integer, String>> scores = new Collection<>();
    private static Collection<UUID, Objective> objectives = new Collection<>();

    public static void morningCall(final UUID player) {
        Scoreboard board = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard();
        final Objective objective = objectives.getOrDefault(player, board.getObjective("subToLetMeHitIt") == null ? board.registerNewObjective("subToLetMeHitIt",
                "dummy") : board.getObjective("subToLetMeHitIt"));
        objectives.putIfAbsent(player, objective);
        objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(DoubleTimeCommands.config.getString("scoreboard.name", "Sky Wars")).toUpperCase()));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        Objects.requireNonNull(Bukkit.getPlayer(player)).setScoreboard(board);
        List<String> list = DoubleTimeCommands.config.getStringList("scoreboard.list");
        Collections.reverse(list);
        new BukkitRunnable() {
            public void run() {
                if (Bukkit.getPlayer(player) == null) {
                    this.cancel();
                    return;
                }
                new CollectionList<>(list).foreach((s, i) -> {
                    placeHolder(i, s, objective, player);
                });
            }
        }.runTaskTimer(DoubleTimeCommands.getPlugin(DoubleTimeCommands.class), 20, 5*20);
    }

    private static void placeHolder(final int score, final String text, final Objective obj, final UUID player) {
        if (text.contains("%points%")) {
            String text2 = ChatColor.translateAlternateColorCodes('&', text.replaceAll("%points%", "?"));
            setScore(score, text2, obj, player);
            getPoints(Bukkit.getPlayer(player), new Callback<Long>() {
                @Override
                public void done(Long l, Throwable throwable) {
                    String text2 = ChatColor.translateAlternateColorCodes('&', text.replaceAll("%points%", NumberFormat.getInstance().format(l)));
                    setScore(score, text2, obj, player);
                }
            });
        } else if (text.contains("%experience%")) {
            String text2 = ChatColor.translateAlternateColorCodes('&', text.replaceAll("%experience%", "?"));
            setScore(score, text2, obj, player);
            getExperience(Bukkit.getPlayer(player), new Callback<Long>() {
                @Override
                public void done(Long l, Throwable throwable) {
                    String text2 = ChatColor.translateAlternateColorCodes('&', text.replaceAll("%experience%", NumberFormat.getInstance().format(l)));
                    setScore(score, text2, obj, player);
                    Objects.requireNonNull(Bukkit.getPlayer(player)).setExp(l);
                }
            });
        } else if (text.contains("%players%")) {
            String text2 = ChatColor.translateAlternateColorCodes('&', text.replaceAll("%players%", "?"));
            setScore(score, text2, obj, player);
            PluginMessageUtils.get(Bukkit.getPlayer(player), "", player + ",,,,", "dtc:playing", new Callback<String>() {
                @Override
                public void done(String s, Throwable throwable) {
                    String text2 = ChatColor.translateAlternateColorCodes('&', text.replaceAll("%players%", s));
                    setScore(score, text2, obj, player);
                }
            });
        } else if (text.contains("%rank%")) {
            setScore(score, ChatColor.translateAlternateColorCodes('&', text.replaceAll("%rank%", PlayerUtils.getRank(player).defaultColor + PlayerUtils.getRank(player).getName())), obj, player);
        } else setScore(score, ChatColor.translateAlternateColorCodes('&', text), obj, player);
    }

    public static void setScore(Integer score, String text, Objective objective, UUID uuid) {
        if (score == null) {
            Objects.requireNonNull(objective.getScoreboard()).resetScores(text);
            return;
        }
        if (scores.get(uuid) == null) {
            scores.put(uuid, new Collection<>());
            Objects.requireNonNull(objective.getScoreboard()).getEntries().forEach(str -> Objects.requireNonNull(objective.getScoreboard()).resetScores(str));
        }
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
