package xyz.acrylicstyle.doubletimecommands.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.acrylicstyle.doubletimecommands.utils.Callback;
import xyz.acrylicstyle.doubletimecommands.utils.PlayerUtils;
import xyz.acrylicstyle.tomeito_core.utils.Log;
import xyz.acrylicstyle.tomeito_core.utils.Ranks;

public class RefreshRank implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command should send from in-game.");
            return true;
        }
        final Player[] player = {(Player) sender};
        Log.debug("UUID: " + player[0].getUniqueId());
        Log.debug("Player name: " + player[0].getName() + ", Sender name: " + sender.getName());
        try {
            Ranks before = PlayerUtils.getRank(player[0].getUniqueId());
            PlayerUtils.refreshRank(player[0], new Callback<Ranks>() {
                @Override
                public void done(Ranks after, Throwable e) {
                    Log.debug("Result for " + player[0].getUniqueId() + ": " + after.name());
                    String name = PlayerUtils.getName(player[0], after);
                    player[0].setDisplayName(name);
                    player[0].setPlayerListName(name);
                    if (before.equals(after)) {
                        Log.debug("No updates.");
                        player[0].sendMessage(ChatColor.GREEN + "Refreshed rank, but you're still " + before.name() + " because we couldn't find any changes.");
                        return;
                    }
                    Log.debug("New rank: " + after.name() + " from " + before.name());
                    player[0].sendMessage(ChatColor.GREEN + "Refreshed rank, new your rank is " + after.name() + "! Enjoy!");
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
