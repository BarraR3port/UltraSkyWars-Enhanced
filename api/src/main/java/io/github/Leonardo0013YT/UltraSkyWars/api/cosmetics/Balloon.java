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
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Cosmetic;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import org.bukkit.Location;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Balloon extends Cosmetic {
    
    private final ItemStack icon;
    private final List<ItemStack> heads = new ArrayList<>();
    private final HashMap<UUID, Giant> giants = new HashMap<>();
    private String url;
    private int animation = 0;
    
    public Balloon(UltraSkyWarsApi plugin, String path){
        super(plugin.getBalloon(), path, "balloon");
        Object url = plugin.getBalloon().getConfig().get(path + ".url");
        if (url instanceof String){
            heads.add(ItemBuilder.createSkull("", "", url.toString()));
            this.url = url.toString();
        } else {
            for ( String s : plugin.getBalloon().getList(path + ".url") ){
                heads.add(ItemBuilder.createSkull("", "", s));
                this.url = s;
            }
        }
        this.icon = plugin.getBalloon().getConfig().getItemStack(path + ".icon");
        plugin.getCos().setLastPage("Balloon", page);
    }
    
    public List<ItemStack> getHeads(){
        return heads;
    }
    
    public int spawn(Player p, Location balloon, Location fence){
        giants.put(p.getUniqueId(), Utils.spawn(balloon, fence, getActualHead()));
        return id;
    }
    
    public void remove(Player p){
        if (giants.containsKey(p.getUniqueId())){
            Giant g = giants.get(p.getUniqueId());
            if (g != null && !g.isDead()){
                g.remove();
            }
        }
        giants.remove(p.getUniqueId());
    }
    
    public boolean needUpdate(){
        return !giants.isEmpty();
    }
    
    public void update(){
        ItemStack head = getActualHead();
        giants.values().forEach(g -> g.getEquipment().setItemInHand(head));
    }
    
    public ItemStack getIcon(Player p){
        if (!icon.hasItemMeta()){
            return icon;
        }
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        ItemStack icon = this.icon.clone();
        ItemMeta iconM = icon.getItemMeta();
        List<String> lore = icon.getItemMeta().getLore();
        for ( int i = 0; i < lore.size(); i++ ){
            String s = lore.get(i);
            switch(s){
                case "<price>":
                    if (!p.hasPermission(autoGivePermission)){
                        if (isBuy && !sw.getBalloons().contains(id)){
                            lore.set(i, plugin.getLang().get(p, "menus.balloonsselector.price").replaceAll("<price>", String.valueOf(price)));
                        } else if (!isBuy && !sw.getBalloons().contains(id)){
                            if (needPermToBuy && p.hasPermission(permission)){
                                lore.set(i, plugin.getLang().get(p, "menus.balloonsselector.price").replaceAll("<price>", String.valueOf(price)));
                            } else {
                                lore.set(i, plugin.getLang().get(p, "menus.balloonsselector.noBuyable"));
                            }
                        } else if (sw.getBalloons().contains(id) || !needPermToBuy){
                            lore.set(i, plugin.getLang().get(p, "menus.balloonsselector.buyed"));
                        }
                    } else {
                        lore.set(i, plugin.getLang().get(p, "menus.balloonsselector.buyed"));
                    }
                    break;
                case "<status>":
                    if (!p.hasPermission(autoGivePermission)){
                        if (sw.getBalloons().contains(id)){
                            lore.set(i, plugin.getLang().get(p, "menus.balloonsselector.hasBuy"));
                        } else if (isBuy){
                            if (plugin.getAdm().getCoins(p) > price){
                                lore.set(i, plugin.getLang().get(p, "menus.balloonsselector.buy"));
                            } else {
                                lore.set(i, plugin.getLang().get(p, "menus.balloonsselector.noMoney"));
                            }
                        } else if (needPermToBuy){
                            if (plugin.getAdm().getCoins(p) > price){
                                lore.set(i, plugin.getLang().get(p, "menus.balloonsselector.buy"));
                            } else {
                                lore.set(i, plugin.getLang().get(p, "menus.balloonsselector.noMoney"));
                            }
                        } else {
                            lore.set(i, plugin.getLang().get(p, "menus.balloonsselector.noPermission"));
                        }
                    } else {
                        lore.set(i, plugin.getLang().get(p, "menus.balloonsselector.hasBuy"));
                    }
                    break;
            }
        }
        iconM.setLore(lore);
        icon.setItemMeta(iconM);
        if (!p.hasPermission(autoGivePermission)){
            if (price > 0){
                if (plugin.getCm().isRedPanelInLocked() && !sw.getBalloons().contains(id)){
                    icon = NBTEditor.set(ItemBuilder.item(XMaterial.matchDefinedXMaterial(plugin.getCm().getRedPanelMaterial().name(), plugin.getCm().getRedPanelData()).orElse(XMaterial.RED_STAINED_GLASS_PANE), 1, icon.getItemMeta().getDisplayName(), icon.getItemMeta().getLore()), id, "ULTRASKYWARS", "BALLOON");
                    return icon;
                }
            }
        }
        return NBTEditor.set(ItemBuilder.createSkull(icon.getItemMeta().getDisplayName(), lore, url), id, "ULTRASKYWARS", "BALLOON");
    }
    
    public String getUrl(){
        return url;
    }
    
    public boolean isAnimated(){
        return heads.size() > 1;
    }
    
    public ItemStack getActualHead(){
        if (isAnimated()){
            int a = animation;
            animation++;
            if (animation >= heads.size()){
                animation = 0;
            }
            return heads.get(a);
        }
        return heads.get(0);
    }
    
}