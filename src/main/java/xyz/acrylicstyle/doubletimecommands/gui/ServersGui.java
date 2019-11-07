package xyz.acrylicstyle.doubletimecommands.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitRunnable;
import util.Collection;
import util.CollectionList;
import xyz.acrylicstyle.doubletimecommands.DoubleTimeCommands;
import xyz.acrylicstyle.doubletimecommands.utils.Server;
import xyz.acrylicstyle.tomeito_core.utils.Callback;
import xyz.acrylicstyle.tomeito_core.utils.PluginMessageUtils;

import java.util.Objects;

import static xyz.acrylicstyle.doubletimecommands.DoubleTimeCommands.config;

public class ServersGui implements InventoryHolder, Listener {
    private String ctc1 = "➤ Click to Connect";
    private String ctc2 = "  Click to Connect";
    private String playingText = "%s currently playing!";
    private Inventory inventory;
    private boolean cycle = false;
    private Collection<Integer, Server> servers = new Collection<>();
    private Collection<String, Server> itemNames = new Collection<>();
    private Collection<Integer, String> playing = new Collection<>();

    public ServersGui() {
        this.inventory = Bukkit.createInventory(this, 9*6, "Game Menu");
        initializeItems();
    }

    private void initializeItems() {
        for (int i = 0; i < 9*6; i++) {
            String name;
            try {
                name = config.getString("servers.slot" + i + ".name", null);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            if (name == null) continue;
            String category = config.getString("servers.slot" + i + ".category", "???");
            String description = config.getString("servers.slot" + i + ".description", "");
            String itemName = config.getString("servers.slot" + i + ".item", "barrier");
            Material item = Material.valueOf(Objects.requireNonNull(itemName).toUpperCase());
            String gamePrefix = config.getString("servers.slot" + i + ".gamePrefix", "");
            Server server = new Server(name, category, description, item, Objects.requireNonNull(gamePrefix));
            servers.add(i, server);
            itemNames.add(server.getName(), server);
            this.inventory.setItem(i, server.toItemStack(ctc1, String.format(playingText, "0")));
        }
        new BukkitRunnable() {
            public void run() {
                cycle = !cycle;
                servers.forEach((i, server) -> ServersGui.this.inventory.setItem(i, server.toItemStack(cycle ? ctc1 : ctc2, String.format(playingText, playing.get(i)))));
            }
        }.runTaskTimer(DoubleTimeCommands.getPlugin(DoubleTimeCommands.class), 0, 500);
        new BukkitRunnable() {
            public void run() {
                if (Bukkit.getOnlinePlayers().size() <= 0) return;
                servers.forEach((i, server) -> PluginMessageUtils.get(new CollectionList<Player>(Bukkit.getOnlinePlayers()).first(), server.getGamePrefix(), "dtc:playing", new Callback<String>() {
                    @Override
                    public void done(String s, Throwable throwable) {
                        playing.add(i, s);
                    }
                }));
            }
        }.runTaskTimer(DoubleTimeCommands.getPlugin(DoubleTimeCommands.class), 0, 3000);
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (e.getClickedInventory().getHolder() != this || e.getCurrentItem() == null) return;
        e.setCancelled(true);
        String name = Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName();
        if (!itemNames.containsKey(name)) return;
        PluginMessageUtils.get(Objects.requireNonNull(Bukkit.getPlayer(e.getWhoClicked().getUniqueId())), itemNames.get(name).getGamePrefix(), "commons:transfer2", new Callback<String>() {
            @Override
            public void done(String s, Throwable throwable) {
                // do nothing
            }
        });
    }
}
