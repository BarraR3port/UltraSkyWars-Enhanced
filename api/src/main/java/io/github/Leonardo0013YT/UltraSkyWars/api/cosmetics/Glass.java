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

package io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.calls.CallBackAPI;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.Purchasable;
import io.github.Leonardo0013YT.UltraSkyWars.api.objects.GlassBlock;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Cosmetic;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;

public class Glass extends Cosmetic implements Purchasable {
    
    private final String schematic;
    private final String clear;
    private final String schematicTeam;
    private final String clearTeam;
    private final ItemStack item;
    private final boolean isBuy;
    private final boolean needPermToBuy;
    private HashMap<Vector, GlassBlock> preview = new HashMap<>();
    
    public Glass(UltraSkyWarsApi plugin, String path){
        super(plugin.getGlass(), path, "glass");
        this.item = Utils.getIcon(plugin.getGlass(), path);
        this.schematic = plugin.getGlass().get(path + ".schematic");
        this.clear = plugin.getGlass().get(path + ".clear");
        this.schematicTeam = plugin.getGlass().getOrDefault(path + ".schematicTeam", schematic);
        this.clearTeam = plugin.getGlass().getOrDefault(path + ".clearTeam", clear);
        this.permission = plugin.getGlass().get(path + ".permission");
        this.isBuy = plugin.getGlass().getBoolean(path + ".isBuy");
        this.autoGivePermission = plugin.getGlass().getOrDefault(path + ".autoGivePermission", "ultraskywars.glass.autogive." + name);
        this.needPermToBuy = plugin.getGlass().getBooleanOrDefault(path + ".needPermToBuy", false);
        plugin.getCos().setLastPage("Glass", page);
    }
    
    public HashMap<Vector, GlassBlock> getPreview(){
        return preview;
    }
    
    public void setPreview(HashMap<Vector, GlassBlock> preview){
        this.preview = preview;
    }
    
    public void createCage(Location loc, boolean team, CallBackAPI<Boolean> done){
        if (team){
            UltraSkyWarsApi.get().getWc().getEdit().paste(loc, schematicTeam, false, done);
        } else {
            UltraSkyWarsApi.get().getWc().getEdit().paste(loc, schematic, false, done);
        }
    }
    
    public void deleteCage(Location loc, boolean team, CallBackAPI<Boolean> done){
        if (loc == null){
            return;
        }
        if (team){
            UltraSkyWarsApi.get().getWc().getEdit().paste(loc, clearTeam, true, done);
        } else {
            UltraSkyWarsApi.get().getWc().getEdit().paste(loc, clear, true, done);
        }
    }
    
    public String getSchematic(){
        return schematic;
    }
    
    @Override
    public String getPermission(){
        return permission;
    }
    
    public ItemStack getIcon(Player p){
        if (!item.hasItemMeta()){
            return item;
        }
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        ItemStack icon = this.item.clone();
        if (!p.hasPermission(autoGivePermission)){
            if (price > 0){
                if (plugin.getCm().isRedPanelInLocked()){
                    if (!sw.getGlasses().contains(id)){
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
                case "<price>":
                    if (!p.hasPermission(autoGivePermission)){
                        if (isBuy && !sw.getGlasses().contains(id)){
                            lore.set(i, plugin.getLang().get(p, "menus.glassselector.price").replaceAll("<price>", String.valueOf(price)));
                        } else if (!isBuy && !sw.getGlasses().contains(id)){
                            if (needPermToBuy && p.hasPermission(permission)){
                                lore.set(i, plugin.getLang().get(p, "menus.glassselector.price").replaceAll("<price>", String.valueOf(price)));
                            } else {
                                lore.set(i, plugin.getLang().get(p, "menus.glassselector.noBuyable"));
                            }
                        } else if (sw.getGlasses().contains(id) || !needPermToBuy){
                            lore.set(i, plugin.getLang().get(p, "menus.glassselector.buyed"));
                        }
                    } else {
                        lore.set(i, plugin.getLang().get(p, "menus.glassselector.buyed"));
                    }
                    break;
                case "<status>":
                    if (!p.hasPermission(autoGivePermission)){
                        if (sw.getGlasses().contains(id)){
                            lore.set(i, plugin.getLang().get(p, "menus.glassselector.hasBuy"));
                        } else if (isBuy){
                            if (plugin.getAdm().getCoins(p) > price){
                                lore.set(i, plugin.getLang().get(p, "menus.glassselector.buy"));
                            } else {
                                lore.set(i, plugin.getLang().get(p, "menus.glassselector.noMoney"));
                            }
                        } else if (needPermToBuy){
                            if (plugin.getAdm().getCoins(p) > price){
                                lore.set(i, plugin.getLang().get(p, "menus.glassselector.buy"));
                            } else {
                                lore.set(i, plugin.getLang().get(p, "menus.glassselector.noMoney"));
                            }
                        } else {
                            lore.set(i, plugin.getLang().get(p, "menus.glassselector.noPermission"));
                        }
                    } else {
                        lore.set(i, plugin.getLang().get(p, "menus.glassselector.hasBuy"));
                    }
                    break;
            }
        }
        iconM.setLore(lore);
        icon.setItemMeta(iconM);
        return NBTEditor.set(icon, id, "ULTRASKYWARS", "GLASS");
    }
    
    @Override
    public String getAutoGivePermission(){
        return autoGivePermission;
    }
    
    public String getName(){
        return name;
    }
    
    @Override
    public int getPrice(){
        return price;
    }
    
    public int getSlot(){
        return slot;
    }
    
    public int getPage(){
        return page;
    }
    
    public int getId(){
        return id;
    }
    
    public boolean isBuy(){
        return isBuy;
    }
    
    public boolean needPermToBuy(){
        return needPermToBuy;
    }
    
}