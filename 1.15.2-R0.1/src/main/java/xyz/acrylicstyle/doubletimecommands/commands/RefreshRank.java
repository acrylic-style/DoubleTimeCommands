package xyz.acrylicstyle.doubletimecommands.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.acrylicstyle.doubletimecommands.DoubleTimeCommands;
import xyz.acrylicstyle.doubletimecommands.utils.PlayerUtils;
import xyz.acrylicstyle.tomeito_api.utils.Callback;
import xyz.acrylicstyle.tomeito_api.utils.Log;
import xyz.acrylicstyle.tomeito_api.utils.Ranks;

import java.util.concurrent.atomic.AtomicReference;

public class RefreshRank implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command must run from in-game.");
            return true;
        }
        final AtomicReference<Player> player = new AtomicReference<>();
        player.set((Player) sender);
        try {
            Ranks before = PlayerUtils.getRank(player.get().getUniqueId());
            PlayerUtils.refreshRank(player.get(), new Callback<Ranks>() {
                @Override
                public void done(Ranks after, Throwable e) {
                    String name = PlayerUtils.getName(player.get(), after);
                    player.get().setDisplayName(name);
                    player.get().setPlayerListName(name);
                    if (DoubleTimeCommands.config.getBoolean("flyable_vip", false) && PlayerUtils.must(Ranks.SAND, player.get().getUniqueId())) {
                        player.get().setAllowFlight(true);
                        player.get().setFlying(true);
                        if (after.ordinal() <= Ranks.MODERATOR.ordinal()) player.get().setOp(true);
                    }
                    if (after.ordinal() > Ranks.MODERATOR.ordinal()) player.get().setOp(false);
                    if (before.equals(after)) {
                        player.get().sendMessage(ChatColor.GREEN + "Refreshed rank, but you're still " + before.name() + " because we couldn't find any changes.");
                        return;
                    }
                    player.get().sendMessage(ChatColor.GREEN + "Refreshed rank, new your rank is " + after.name() + "! Enjoy!");
                }
            });
        } catch (NullPointerException e) {
            Log.error("We got null while refreshing(fetching) rank!");
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + "An error occurred while refreshing rank, sorry!");
        }
        return true;
    }
}
