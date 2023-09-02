package it.craftopoly.co_tickets.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ElementsUtils
{
    public static ItemStack createCustomElement(Material id, short data, int amount, List<String> lore, String display)
    {
        @SuppressWarnings("deprecation")
        ItemStack item = new ItemStack(id, amount, data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(display);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createCustomSkull(String username, List<String> lore, String display)
    {
        @SuppressWarnings("deprecation")
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1, (short) 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(display);
        meta.setLore(lore);
        SkullMeta skullMeta = (SkullMeta) meta;
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(username));
        item.setItemMeta(meta);
        return item;
    }

    public static ArrayList<String> createLore(String... lines)
    {
        ArrayList<String> lore = new ArrayList<>();
        Collections.addAll(lore, lines);
        return lore;
    }
}
