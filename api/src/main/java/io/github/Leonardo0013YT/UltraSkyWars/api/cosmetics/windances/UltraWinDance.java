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

package io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.windances;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.Purchasable;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Cosmetic;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class UltraWinDance extends Cosmetic implements Purchasable {
    
    private final String type;
    private final ItemStack icon;
    
    public UltraWinDance(UltraSkyWarsApi plugin, String s){
        super(plugin.getWindance(), s, "windances");
        this.type = plugin.getWindance().get(s + ".type");
        this.icon = Utils.getIcon(plugin.getWindance(), s);
        plugin.getCos().setLastPage("WinDance", page);
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
                    if (!sw.getWindances().contains(id)){
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
                        if (isBuy && !sw.getWindances().contains(id)){
                            lore.set(i, plugin.getLang().get(p, "menus.windancesselector.price").replaceAll("<price>", String.valueOf(price)));
                        } else if (!isBuy && !sw.getWindances().contains(id)){
                            if (needPermToBuy && p.hasPermission(permission)){
                                lore.set(i, plugin.getLang().get(p, "menus.windancesselector.price").replaceAll("<price>", String.valueOf(price)));
                            } else {
                                lore.set(i, plugin.getLang().get(p, "menus.windancesselector.noBuyable"));
                            }
                        } else if (sw.getWindances().contains(id) || !needPermToBuy){
                            lore.set(i, plugin.getLang().get(p, "menus.windancesselector.buyed"));
                        }
                    } else {
                        lore.set(i, plugin.getLang().get(p, "menus.windancesselector.buyed"));
                    }
                    break;
                case "<status>":
                    if (!p.hasPermission(autoGivePermission)){
                        if (sw.getWindances().contains(id)){
                            lore.set(i, plugin.getLang().get(p, "menus.windancesselector.hasBuy"));
                        } else if (isBuy){
                            if (plugin.getAdm().getCoins(p) > price){
                                lore.set(i, plugin.getLang().get(p, "menus.windancesselector.buy"));
                            } else {
                                lore.set(i, plugin.getLang().get(p, "menus.windancesselector.noMoney"));
                            }
                        } else if (needPermToBuy){
                            if (plugin.getAdm().getCoins(p) > price){
                                lore.set(i, plugin.getLang().get(p, "menus.windancesselector.buy"));
                            } else {
                                lore.set(i, plugin.getLang().get(p, "menus.windancesselector.noMoney"));
                            }
                        } else {
                            lore.set(i, plugin.getLang().get(p, "menus.windancesselector.noPermission"));
                        }
                    } else {
                        lore.set(i, plugin.getLang().get(p, "menus.windancesselector.hasBuy"));
                    }
                    break;
            }
        }
        iconM.setLore(lore);
        icon.setItemMeta(iconM);
        return NBTEditor.set(icon, id, "ULTRASKYWARS", "WINDANCE");
    }
    
    public String getType(){
        return type;
    }
    
}