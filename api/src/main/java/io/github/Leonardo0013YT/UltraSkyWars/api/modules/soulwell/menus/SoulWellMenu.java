/*
 * Copyright (c) 2022.
 *
 *  This program/library is free software: you can redistribute it and/or modify
 * it under the terms of the New BSD License (3-clause license).
 *
 * This program/library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the New BSD License (3-clause license)
 * for more details.
 *
 * You should have received a copy of the New BSD License (3-clause license)
 * along with this program/library; If not, see http://directory.fsf.org/wiki/License:BSD_3Clause/
 * for the New BSD License (3-clause license).
 *
 */

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.menus;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.InjectionSoulWell;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.soulwell.SoulWellShop;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.soulwell.upgrades.SoulWellAngelOfDeath;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.soulwell.upgrades.SoulWellUpgrade;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SoulWellMenu {
    
    private final UltraSkyWarsApi plugin;
    private final InjectionSoulWell is;
    private final int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
    private final DateFormat df;
    
    public SoulWellMenu(UltraSkyWarsApi plugin, InjectionSoulWell is){
        this.plugin = plugin;
        this.is = is;
        this.df = new SimpleDateFormat(is.getSoulwell().get("date"));
    }
    
    public void createHeadsSoulWellMenu(Player p){
        Inventory inv = Bukkit.createInventory(null, 45, plugin.getLang().get(p, "menus.heads.title"));
        ItemStack back = ItemBuilder.item(XMaterial.BARRIER, plugin.getLang().get(p, "menus.back.nameItem"), plugin.getLang().get(p, "menus.back.loreItem"));
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        ItemStack stats = ItemBuilder.item(XMaterial.PLAYER_HEAD, is.getSoulwell().get(p, "heads.stats.nameItem"), is.getSoulwell().get(p, "heads.stats.loreItem").replaceAll("<heads>", "" + sw.getHeadsCollected()).replaceAll("<eww>", String.valueOf(sw.getHeadCount("eww"))).replaceAll("<disgusting>", String.valueOf(sw.getHeadCount("disgusting"))).replaceAll("<bah>", String.valueOf(sw.getHeadCount("bah"))).replaceAll("<decent>", String.valueOf(sw.getHeadCount("decent"))).replaceAll("<salty>", String.valueOf(sw.getHeadCount("salty"))).replaceAll("<tasty>", String.valueOf(sw.getHeadCount("tasty"))).replaceAll("<succulent>", String.valueOf(sw.getHeadCount("succulent"))).replaceAll("<candy>", String.valueOf(sw.getHeadCount("candy"))).replaceAll("<divine>", String.valueOf(sw.getHeadCount("divine"))).replaceAll("<heavenly>", String.valueOf(sw.getHeadCount("heavenly"))));
        int i = 0;
        for ( String rarity : sw.getHeads().keySet() ){
            if (i >= slots.length) break;
            for ( String head : sw.getHeads().get(rarity).keySet() ){
                String date = df.format(new Date(sw.getHeads().get(rarity).get(head)));
                String ra = is.getSoulwell().get("raritys." + rarity);
                inv.setItem(slots[i], getHead(ra, head, date));
                i++;
            }
        }
        inv.setItem(40, back);
        inv.setItem(41, stats);
        p.openInventory(inv);
    }
    
    public ItemStack getHead(String rarity, String player, String date){
        return ItemBuilder.skull(XMaterial.PLAYER_HEAD, 1, is.getSoulwell().get("heads.head.nameItem").replaceAll("<name>", player), is.getSoulwell().get("heads.head.loreItem").replaceAll("<rarity>", rarity).replaceAll("<date>", date), player);
    }
    
    public void createUpgradeSoulWellMenu(Player p){
        Inventory inv = Bukkit.createInventory(null, 36, plugin.getLang().get(p, "menus.soulwellupgrade.title"));
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        ItemStack back = ItemBuilder.item(XMaterial.BARRIER, plugin.getLang().get(p, "menus.back.nameItem"), plugin.getLang().get(p, "menus.back.loreItem"));
        SoulWellUpgrade extra = is.getSwm().getExtraMax(sw.getSoulWellExtra());
        if (extra != null){
            if (extra.getPrice() < plugin.getAdm().getCoins(p)){
                inv.setItem(11, extra.getIcon(is.getSoulwell().get("buy")));
            } else {
                inv.setItem(11, extra.getIcon(is.getSoulwell().get("noMoney")));
            }
        } else {
            SoulWellUpgrade last = is.getSwm().getExtraMax(sw.getSoulWellExtra() - 1);
            inv.setItem(11, last.getIcon(is.getSoulwell().get("unlocked")));
        }
        SoulWellUpgrade max = is.getSwm().getMaxMax(sw.getSoulWellMax());
        if (max != null){
            if (max.getPrice() < plugin.getAdm().getCoins(p)){
                inv.setItem(15, max.getIcon(is.getSoulwell().get("buy")));
            } else {
                inv.setItem(15, max.getIcon(is.getSoulwell().get("noMoney")));
            }
        } else {
            SoulWellUpgrade last = is.getSwm().getMaxMax(sw.getSoulWellMax() - 1);
            inv.setItem(15, last.getIcon(is.getSoulwell().get("unlocked")));
        }
        inv.setItem(31, back);
        p.openInventory(inv);
    }
    
    public void createShopSoulWellMenu(Player p){
        Inventory inv = Bukkit.createInventory(null, 36, plugin.getLang().get(p, "menus.soulwellshop.title"));
        ItemStack back = ItemBuilder.item(XMaterial.BARRIER, plugin.getLang().get(p, "menus.back.nameItem"), plugin.getLang().get(p, "menus.back.loreItem"));
        for ( SoulWellShop s : is.getSwm().getShops().values() ){
            inv.setItem(s.getSlot(), s.getIcon(plugin.getAdm().getCoins(p)));
        }
        inv.setItem(31, back);
        p.openInventory(inv);
    }
    
    public void createSoulWellMenu(Player p){
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getLang().get(p, "menus.soulwells.title"));
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        ItemStack soulwell = ItemBuilder.item(XMaterial.END_PORTAL_FRAME, plugin.getLang().get(p, "menus.soulwells.soulwell.nameItem"), plugin.getLang().get(p, "menus.soulwells.soulwell.loreItem"));
        ItemStack shop = ItemBuilder.item(XMaterial.PAPER, plugin.getLang().get(p, "menus.soulwells.shop.nameItem"), plugin.getLang().get(p, "menus.soulwells.shop.loreItem"));
        ItemStack upgrades = ItemBuilder.item(XMaterial.GHAST_TEAR, plugin.getLang().get(p, "menus.soulwells.upgrades.nameItem"), plugin.getLang().get(p, "menus.soulwells.upgrades.loreItem"));
        ItemStack heads = ItemBuilder.item(XMaterial.PLAYER_HEAD, plugin.getLang().get(p, "menus.soulwells.heads.nameItem"), plugin.getLang().get(p, "menus.soulwells.heads.loreItem").replaceAll("<heads>", String.valueOf(sw.getHeadsCollected())));
        ItemStack close = ItemBuilder.item(XMaterial.BARRIER, plugin.getLang().get(p, "menus.close.nameItem"), plugin.getLang().get(p, "menus.close.loreItem"));
        ItemStack settings = ItemBuilder.item(XMaterial.ENCHANTING_TABLE, plugin.getLang().get(p, "menus.soulwells.settings.nameItem"), plugin.getLang().get(p, "menus.soulwells.settings.loreItem"));
        inv.setItem(13, soulwell);
        inv.setItem(29, shop);
        inv.setItem(30, upgrades);
        inv.setItem(32, getAngelOfDeath(sw.getSoulWellHead()));
        inv.setItem(33, heads);
        inv.setItem(49, close);
        inv.setItem(50, settings);
        p.openInventory(inv);
    }
    
    public void createSoulWellAngelMenu(Player p){
        Inventory inv = Bukkit.createInventory(null, 36, plugin.getLang().get(p, "menus.angelofdeath.title"));
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        ItemStack cancel = ItemBuilder.item(XMaterial.RED_TERRACOTTA, plugin.getLang().get(p, "menus.cancel.nameItem"), plugin.getLang().get(p, "menus.cancel.loreItem"));
        inv.setItem(11, getAngelOfDeath(sw.getSoulWellHead()));
        inv.setItem(15, cancel);
        p.openInventory(inv);
    }
    
    public void createSettingsMenu(Player p){
        Inventory inv = Bukkit.createInventory(null, 45, plugin.getLang().get(p, "menus.soulwellsettings.title"));
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        ItemStack add = ItemBuilder.item(XMaterial.ARROW, plugin.getLang().get(p, "menus.soulwellsettings.add.nameItem"), plugin.getLang().get(p, "menus.soulwellsettings.add.loreItem"));
        ItemStack remove = ItemBuilder.item(XMaterial.ARROW, plugin.getLang().get(p, "menus.soulwellsettings.remove.nameItem"), plugin.getLang().get(p, "menus.soulwellsettings.remove.loreItem"));
        ItemStack s1y = ItemBuilder.item(XMaterial.TERRACOTTA, plugin.getLang().get(p, "menus.soulwellsettings.yesHas.nameItem").replaceAll("<rows>", "1"), plugin.getLang().get(p, "menus.soulwellsettings.yesHas.loreItem").replaceAll("<rows>", "1"));
        ItemStack s2y = ItemBuilder.item(XMaterial.TERRACOTTA, plugin.getLang().get(p, "menus.soulwellsettings.yesHas.nameItem").replaceAll("<rows>", "2"), plugin.getLang().get(p, "menus.soulwellsettings.yesHas.loreItem").replaceAll("<rows>", "2"));
        ItemStack s3y = ItemBuilder.item(XMaterial.TERRACOTTA, plugin.getLang().get(p, "menus.soulwellsettings.yesHas.nameItem").replaceAll("<rows>", "3"), plugin.getLang().get(p, "menus.soulwellsettings.yesHas.loreItem").replaceAll("<rows>", "3"));
        ItemStack s4y = ItemBuilder.item(XMaterial.TERRACOTTA, plugin.getLang().get(p, "menus.soulwellsettings.yesHas.nameItem").replaceAll("<rows>", "4"), plugin.getLang().get(p, "menus.soulwellsettings.yesHas.loreItem").replaceAll("<rows>", "4"));
        ItemStack s5y = ItemBuilder.item(XMaterial.TERRACOTTA, plugin.getLang().get(p, "menus.soulwellsettings.yesHas.nameItem").replaceAll("<rows>", "5"), plugin.getLang().get(p, "menus.soulwellsettings.yesHas.loreItem").replaceAll("<rows>", "5"));
        ItemStack s2n = ItemBuilder.item(XMaterial.GRAY_TERRACOTTA, 1, plugin.getLang().get(p, "menus.soulwellsettings.notHas.nameItem"), plugin.getLang().get(p, "menus.soulwellsettings.notHas.loreItem"));
        ItemStack s3n = ItemBuilder.item(XMaterial.GRAY_TERRACOTTA, 1, plugin.getLang().get(p, "menus.soulwellsettings.notHas.nameItem"), plugin.getLang().get(p, "menus.soulwellsettings.notHas.loreItem"));
        ItemStack s4n = ItemBuilder.item(XMaterial.GRAY_TERRACOTTA, 1, plugin.getLang().get(p, "menus.soulwellsettings.notHas.nameItem"), plugin.getLang().get(p, "menus.soulwellsettings.notHas.loreItem"));
        ItemStack s5n = ItemBuilder.item(XMaterial.GRAY_TERRACOTTA, 1, plugin.getLang().get(p, "menus.soulwellsettings.notHas.nameItem"), plugin.getLang().get(p, "menus.soulwellsettings.notHas.loreItem"));
        ItemStack normal = ItemBuilder.item(XMaterial.GREEN_STAINED_GLASS, plugin.getLang().get(p, "menus.soulwellsettings.normal.nameItem"), plugin.getLang().get(p, "menus.soulwellsettings.normal.loreItem").replaceAll("<status>", (sw.getSoulanimation() == 0) ? plugin.getLang().get(p, "menus.soulwellsettings.status.selected") : plugin.getLang().get(p, "menus.soulwellsettings.status.select")));
        ItemStack blaze = ItemBuilder.item(XMaterial.BLAZE_POWDER, plugin.getLang().get(p, "menus.soulwellsettings.blaze.nameItem"), plugin.getLang().get(p, "menus.soulwellsettings.blaze.loreItem").replaceAll("<status>", (sw.getSoulanimation() == 1) ? plugin.getLang().get(p, "menus.soulwellsettings.status.selected") : plugin.getLang().get(p, "menus.soulwellsettings.status.select")));
        ItemStack d3 = ItemBuilder.item(XMaterial.ARMOR_STAND, plugin.getLang().get(p, "menus.soulwellsettings.3d.nameItem"), plugin.getLang().get(p, "menus.soulwellsettings.3d.loreItem").replaceAll("<status>", (sw.getSoulanimation() == 2) ? plugin.getLang().get(p, "menus.soulwellsettings.status.selected") : plugin.getLang().get(p, "menus.soulwellsettings.status.select")));
        inv.setItem(12, normal);
        inv.setItem(13, blaze);
        inv.setItem(14, d3);
        inv.setItem(28, remove);
        inv.setItem(29, s1y);
        if (sw.getRows() > 1){
            inv.setItem(30, s2y);
        } else {
            inv.setItem(30, s2n);
        }
        if (sw.getRows() > 2){
            inv.setItem(31, s3y);
        } else {
            inv.setItem(31, s3n);
        }
        if (sw.getRows() > 3){
            inv.setItem(32, s4y);
        } else {
            inv.setItem(32, s4n);
        }
        if (sw.getRows() > 4){
            inv.setItem(33, s5y);
        } else {
            inv.setItem(33, s5n);
        }
        inv.setItem(34, add);
        p.openInventory(inv);
    }
    
    public ItemStack getAngelOfDeath(int levelActual){
        SoulWellAngelOfDeath sa = is.getSwm().getAngelByLevel(levelActual);
        SoulWellAngelOfDeath na = is.getSwm().getAngelByLevel(levelActual + 1);
        if (na == null){
            ItemStack angelofdeath = ItemBuilder.item(XMaterial.CHEST, sa.getName(), sa.getLore().replaceAll("<desc>", is.getSoulwell().get("angelLore"))
                    .replaceAll("<now>", "" + sa.getProbability()).replaceAll("<new>", "" + is.getSoulwell().get("unlocked")).replaceAll("<arrow>", "➤"));
            angelofdeath = NBTEditor.set(angelofdeath, sa.getLevel(), "SOULWELL", "ANGELDEATH", "LEVEL");
            return NBTEditor.set(angelofdeath, sa.getKey(), "SOULWELL", "ANGELDEATH", "KEY");
        }
        ItemStack angelofdeath = ItemBuilder.item(XMaterial.CHEST, sa.getName(), sa.getLore().replaceAll("<desc>", is.getSoulwell().get("angelLore"))
                .replaceAll("<now>", "" + sa.getProbability()).replaceAll("<new>", "" + na.getProbability()).replaceAll("<arrow>", "➤"));
        angelofdeath = NBTEditor.set(angelofdeath, na.getLevel(), "SOULWELL", "ANGELDEATH", "LEVEL");
        return NBTEditor.set(angelofdeath, na.getKey(), "SOULWELL", "ANGELDEATH", "KEY");
    }
    
}