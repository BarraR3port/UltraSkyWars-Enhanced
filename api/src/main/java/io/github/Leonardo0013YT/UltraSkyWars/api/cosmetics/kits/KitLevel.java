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

package io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.kits;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.Purchasable;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class KitLevel implements Purchasable {
    
    private final ItemStack[] inv, armors;
    private final ItemStack icon;
    private final String permission, autoGivePermission;
    private final int price, slot, level;
    private final boolean isBuy, needPermToBuy;
    private final Kit kit;
    private ItemStack helmet = null, chestplate = null, leggings = null, boots = null;
    
    public KitLevel(UltraSkyWarsApi plugin, String path, Kit kit){
        this.kit = kit;
        this.level = plugin.getKits().getInt(path + ".level");
        this.permission = plugin.getKits().get(null, path + ".permission");
        this.autoGivePermission = plugin.getKits().getOrDefault(path + ".autoGivePermission", "ultraskywars.kits.autogive." + kit.getName() + "." + level);
        this.isBuy = plugin.getKits().getBoolean(path + ".isBuy");
        this.icon = Utils.getIcon(plugin.getKits(), path);
        this.price = plugin.getKits().getInt(path + ".price");
        this.slot = plugin.getKits().getInt(path + ".slot");
        this.armors = ((List<String>) plugin.getKits().getConfig().get(path + ".armor")).toArray(new ItemStack[0]);
        this.inv = ((List<String>) plugin.getKits().getConfig().get(path + ".inv")).toArray(new ItemStack[0]);
        this.needPermToBuy = plugin.getKits().getBooleanOrDefault(path + ".needPermToBuy", false);
        for ( ItemStack i : armors ){
            if (i == null || i.getType().equals(Material.AIR)) continue;
            if (i.getType().name().endsWith("HELMET")){
                helmet = i;
            }
            if (i.getType().name().endsWith("CHESTPLATE")){
                chestplate = i;
            }
            if (i.getType().name().endsWith("LEGGINGS")){
                leggings = i;
            }
            if (i.getType().name().endsWith("BOOTS")){
                boots = i;
            }
        }
    }
    
    public void giveKitLevel(Player p){
        p.getInventory().setHelmet(helmet);
        p.getInventory().setChestplate(chestplate);
        p.getInventory().setLeggings(leggings);
        p.getInventory().setBoots(boots);
        p.getInventory().setContents(inv);
    }
    
    @Override
    public String getAutoGivePermission(){
        return autoGivePermission;
    }
    
    public ItemStack getIcon(Player p){
        if (!icon.hasItemMeta()){
            return icon;
        }
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        ItemStack icon = this.icon.clone();
        if (!p.hasPermission(autoGivePermission)){
            if (price > 0){
                if (plugin.getCm().isRedPanelInLocked()){
                    if (!sw.hasKitLevel(kit.getId(), level)){
                        icon = ItemBuilder.item(XMaterial.matchDefinedXMaterial(plugin.getCm().getRedPanelMaterial().name(), plugin.getCm().getRedPanelData()).orElse(XMaterial.RED_STAINED_GLASS_PANE), 1, icon.getItemMeta().getDisplayName(), icon.getItemMeta().getLore());
                    }
                }
            }
        }
        ItemMeta iconM = icon.getItemMeta();
        List<String> lore = icon.getItemMeta().getLore();
        for ( int i = 0; i < lore.size(); i++ ){
            String s = lore.get(i);
            switch(s){
                case "<level>":
                    lore.set(i, plugin.getLang().get(p, "menus.kitselector.level").replaceAll("<level>", String.valueOf(level)));
                    break;
                case "<price>":
                    if (!p.hasPermission(autoGivePermission)){
                        if (isBuy && !sw.hasKitLevel(kit.getId(), level)){
                            lore.set(i, plugin.getLang().get(p, "menus.kitselector.price").replaceAll("<price>", String.valueOf(price)));
                        } else if (!isBuy && !sw.hasKitLevel(kit.getId(), level)){
                            if (needPermToBuy && p.hasPermission(permission)){
                                lore.set(i, plugin.getLang().get(p, "menus.kitselector.price").replaceAll("<price>", String.valueOf(price)));
                            } else {
                                lore.set(i, plugin.getLang().get(p, "menus.kitselector.noBuyable"));
                            }
                        } else if (sw.hasKitLevel(kit.getId(), level) || !needPermToBuy){
                            lore.set(i, plugin.getLang().get(p, "menus.kitselector.buyed"));
                        }
                    } else {
                        lore.set(i, plugin.getLang().get(p, "menus.kitselector.buyed"));
                    }
                    break;
                case "<status>":
                    if (!p.hasPermission(autoGivePermission)){
                        if (sw.hasKitLevel(kit.getId(), level)){
                            lore.set(i, plugin.getLang().get(p, "menus.kitselector.hasBuy"));
                        } else if (isBuy){
                            if (plugin.getAdm().getCoins(p) > price){
                                lore.set(i, plugin.getLang().get(p, "menus.kitselector.buy"));
                            } else {
                                lore.set(i, plugin.getLang().get(p, "menus.kitselector.noMoney"));
                            }
                        } else if (needPermToBuy){
                            if (plugin.getAdm().getCoins(p) > price){
                                lore.set(i, plugin.getLang().get(p, "menus.kitselector.buy"));
                            } else {
                                lore.set(i, plugin.getLang().get(p, "menus.kitselector.noMoney"));
                            }
                        } else {
                            lore.set(i, plugin.getLang().get(p, "menus.kitselector.noPermission"));
                        }
                    } else {
                        lore.set(i, plugin.getLang().get(p, "menus.kitselector.hasBuy"));
                    }
                    break;
            }
        }
        iconM.setLore(lore);
        icon.setItemMeta(iconM);
        return icon;
    }
    
    public Kit getKit(){
        return kit;
    }
    
    public ItemStack[] getInv(){
        return inv;
    }
    
    public ItemStack[] getArmors(){
        return armors;
    }
    
    @Override
    public String getPermission(){
        return permission;
    }
    
    @Override
    public int getPrice(){
        return price;
    }
    
    public int getSlot(){
        return slot;
    }
    
    public int getLevel(){
        return level;
    }
    
    @Override
    public boolean isBuy(){
        return isBuy;
    }
    
    @Override
    public boolean needPermToBuy(){
        return needPermToBuy;
    }
    
}