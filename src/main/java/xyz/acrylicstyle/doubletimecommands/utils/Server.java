package xyz.acrylicstyle.doubletimecommands.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Server {
    private final String name;
    private final String category;
    private final String description;
    private final Material item;
    private final String gamePrefix;

    public Server(String name, String category, String description, Material item, String gamePrefix) {
        this.name = ChatColor.GREEN + ChatColor.translateAlternateColorCodes('&', name);
        this.category = ChatColor.DARK_GRAY + ChatColor.translateAlternateColorCodes('&', category);
        this.description = ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', description);
        this.item = item;
        this.gamePrefix = gamePrefix.toUpperCase();
    }

    public String getName() { return this.name; }
    public String getCategory() { return this.category; }
    public String getDescription() { return this.description; }
    public Material getItem() { return this.item; }
    public String getGamePrefix() { return this.gamePrefix; }
    public ItemStack toItemStack(String s, String playing) {
        ItemStack item = new ItemStack(getItem());
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setUnbreakable(true);
        meta.setDisplayName(getName());
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_GRAY + getCategory());
        lore.add("");
        String[] lines = getDescription().split("\\\\n");
        Arrays.asList(lines).forEach(str -> lore.add(ChatColor.GRAY + str));
        lore.add("");
        lore.add(ChatColor.GREEN + s);
        lore.add(ChatColor.GRAY + playing);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
