package xyz.acrylicstyle.doubletimecommands.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.acrylicstyle.tomeito_api.utils.Callback;
import xyz.acrylicstyle.tomeito_api.utils.PluginMessageUtils;

public class Transfer implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command must run from in-game.");
            return true;
        }
        if (args.length <= 0) {
            sender.sendMessage(ChatColor.RED + "This command needs 1 more argument. (game)");
            return true;
        }
        Player player = (Player) sender;
        PluginMessageUtils.get(player, args[0], "commons:transfer", new Callback<String>() {
            @Override
            public void done(String s, Throwable throwable) {
                // do nothing
            }
        });
        return true;
    }
}
