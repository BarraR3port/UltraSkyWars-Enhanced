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
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XSound;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Optional;

public class KillSound extends Cosmetic {
    
    private final Sound sound;
    private final float vol1;
    private final float vol2;
    private final ItemStack icon;
    
    public KillSound(UltraSkyWarsApi plugin, String s){
        super(plugin.getKillsound(), s, "killsound");
        Optional<XSound> xs = XSound.matchXSound(plugin.getKillsound().get(s + ".sound"));
        if (xs.isPresent()){
            this.sound = xs.get().parseSound();
        } else {
            this.sound = XSound.ENTITY_PLAYER_LEVELUP.parseSound();
        }
        this.icon = Utils.getIcon(plugin.getKillsound(), s);
        this.vol1 = (float) plugin.getKillsound().getConfig().getDouble(s + ".vol1");
        this.vol2 = (float) plugin.getKillsound().getConfig().getDouble(s + ".vol1");
        plugin.getCos().setLastPage("KillSound", page);
    }
    
    public Sound getSound(){
        return sound;
    }
    
    public float getVol1(){
        return vol1;
    }
    
    public float getVol2(){
        return vol2;
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
                    if (!sw.getKillsounds().contains(id)){
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
                        if (isBuy && !sw.getKillsounds().contains(id)){
                            lore.set(i, plugin.getLang().get(p, "menus.killsoundsselector.price").replaceAll("<price>", String.valueOf(price)));
                        } else if (!isBuy && !sw.getKillsounds().contains(id)){
                            if (needPermToBuy && p.hasPermission(permission)){
                                lore.set(i, plugin.getLang().get(p, "menus.killsoundsselector.price").replaceAll("<price>", String.valueOf(price)));
                            } else {
                                lore.set(i, plugin.getLang().get(p, "menus.killsoundsselector.noBuyable"));
                            }
                        } else if (sw.getKillsounds().contains(id) || !needPermToBuy){
                            lore.set(i, plugin.getLang().get(p, "menus.killsoundsselector.buyed"));
                        }
                    } else {
                        lore.set(i, plugin.getLang().get(p, "menus.killsoundsselector.buyed"));
                    }
                    break;
                case "<status>":
                    if (!p.hasPermission(autoGivePermission)){
                        if (sw.getKillsounds().contains(id)){
                            lore.set(i, plugin.getLang().get(p, "menus.killsoundsselector.hasBuy"));
                        } else if (isBuy){
                            if (plugin.getAdm().getCoins(p) > price){
                                lore.set(i, plugin.getLang().get(p, "menus.killsoundsselector.buy"));
                            } else {
                                lore.set(i, plugin.getLang().get(p, "menus.killsoundsselector.noMoney"));
                            }
                        } else if (needPermToBuy){
                            if (plugin.getAdm().getCoins(p) > price){
                                lore.set(i, plugin.getLang().get(p, "menus.killsoundsselector.buy"));
                            } else {
                                lore.set(i, plugin.getLang().get(p, "menus.killsoundsselector.noMoney"));
                            }
                        } else {
                            lore.set(i, plugin.getLang().get(p, "menus.killsoundsselector.noPermission"));
                        }
                    } else {
                        lore.set(i, plugin.getLang().get(p, "menus.killsoundsselector.hasBuy"));
                    }
                    break;
            }
        }
        iconM.setLore(lore);
        icon.setItemMeta(iconM);
        return NBTEditor.set(icon, id, "ULTRASKYWARS", "KILLSOUND");
    }
    
    public void execute(Player k, Player d){
        k.playSound(k.getLocation(), sound, getVol1(), getVol2());
        d.playSound(d.getLocation(), sound, getVol1(), getVol2());
    }
    
}