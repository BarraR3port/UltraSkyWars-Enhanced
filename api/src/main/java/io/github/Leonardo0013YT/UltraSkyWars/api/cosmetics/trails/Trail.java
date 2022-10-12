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

package io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.trails;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.TrailType;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Cosmetic;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Trail extends Cosmetic {
    
    private final TrailType type;
    private final ItemStack icon;
    private final String particle;
    private final float offsetX;
    private final float offsetY;
    private final float offsetZ;
    private final int amount;
    private final double range;
    private final double speed;
    
    public Trail(UltraSkyWarsApi plugin, String path){
        super(plugin.getTrail(), path, "trails");
        this.type = TrailType.valueOf(plugin.getTrail().getOrDefault(path + ".type", "NORMAL"));
        this.particle = plugin.getTrail().get(path + ".particle");
        this.amount = plugin.getTrail().getInt(path + ".amount");
        this.icon = Utils.getIcon(plugin.getTrail(), path);
        this.range = plugin.getTrail().getConfig().getDouble(path + ".range");
        this.speed = plugin.getTrail().getConfig().getDouble(path + ".speed");
        this.offsetX = (float) plugin.getTrail().getConfig().getDouble(path + ".offsetX");
        this.offsetY = (float) plugin.getTrail().getConfig().getDouble(path + ".offsetY");
        this.offsetZ = (float) plugin.getTrail().getConfig().getDouble(path + ".offsetZ");
        plugin.getCos().setLastPage("Trail", page);
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
                    if (!sw.getTrails().contains(id)){
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
                        if (isBuy && !sw.getTrails().contains(id)){
                            lore.set(i, plugin.getLang().get(p, "menus.trailsselector.price").replaceAll("<price>", String.valueOf(price)));
                        } else if (!isBuy && !sw.getTrails().contains(id)){
                            if (needPermToBuy && p.hasPermission(permission)){
                                lore.set(i, plugin.getLang().get(p, "menus.trailsselector.price").replaceAll("<price>", String.valueOf(price)));
                            } else {
                                lore.set(i, plugin.getLang().get(p, "menus.trailsselector.noBuyable"));
                            }
                        } else if (sw.getTrails().contains(id) || !needPermToBuy){
                            lore.set(i, plugin.getLang().get(p, "menus.trailsselector.buyed"));
                        }
                    } else {
                        lore.set(i, plugin.getLang().get(p, "menus.trailsselector.buyed"));
                    }
                    break;
                case "<status>":
                    if (!p.hasPermission(autoGivePermission)){
                        if (sw.getTrails().contains(id)){
                            lore.set(i, plugin.getLang().get(p, "menus.trailsselector.hasBuy"));
                        } else if (isBuy){
                            if (plugin.getAdm().getCoins(p) > price){
                                lore.set(i, plugin.getLang().get(p, "menus.trailsselector.buy"));
                            } else {
                                lore.set(i, plugin.getLang().get(p, "menus.trailsselector.noMoney"));
                            }
                        } else if (needPermToBuy){
                            if (plugin.getAdm().getCoins(p) > price){
                                lore.set(i, plugin.getLang().get(p, "menus.trailsselector.buy"));
                            } else {
                                lore.set(i, plugin.getLang().get(p, "menus.trailsselector.noMoney"));
                            }
                        } else {
                            lore.set(i, plugin.getLang().get(p, "menus.trailsselector.noPermission"));
                        }
                    } else {
                        lore.set(i, plugin.getLang().get(p, "menus.trailsselector.hasBuy"));
                    }
                    break;
            }
        }
        iconM.setLore(lore);
        icon.setItemMeta(iconM);
        return NBTEditor.set(icon, id, "ULTRASKYWARS", "TRAIL");
    }
    
    public TrailType getType(){
        return type;
    }
    
    public String getParticle(){
        return particle;
    }
    
    public float getOffsetX(){
        return offsetX;
    }
    
    public float getOffsetY(){
        return offsetY;
    }
    
    public float getOffsetZ(){
        return offsetZ;
    }
    
    public int getAmount(){
        return amount;
    }
    
    public double getSpeed(){
        return speed;
    }
    
    public double getRange(){
        return range;
    }
    
}