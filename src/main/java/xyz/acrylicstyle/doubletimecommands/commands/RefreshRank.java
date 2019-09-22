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
        try {
            Ranks before = PlayerUtils.getRank(((Player) sender).getUniqueId());
            PlayerUtils.refreshRank((Player) sender, new Callback<Ranks>() {
                @Override
                public void done(Ranks after) {
                    Log.debug("Result for " + ((Player) sender).getUniqueId() + ": " + after.name());
                    String name = PlayerUtils.getName((Player) sender, after);
                    ((Player) sender).setDisplayName(name);
                    ((Player) sender).setPlayerListName(name);
                    if (before.equals(after)) {
                        Log.debug("No updates.");
                        sender.sendMessage(ChatColor.GREEN + "Refreshed rank, but you're still " + before.name() + " because we couldn't find any changes.");
                        return;
                    }
                    Log.debug("New rank: " + after.name() + " from " + before.name());
                    sender.sendMessage(ChatColor.GREEN + "Refreshed rank, new your rank is " + after.name() + "! Enjoy!");
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
