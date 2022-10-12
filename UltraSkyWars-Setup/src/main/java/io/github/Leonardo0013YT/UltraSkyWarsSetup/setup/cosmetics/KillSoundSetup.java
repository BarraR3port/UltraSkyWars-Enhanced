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

package io.github.Leonardo0013YT.UltraSkyWarsSetup.setup.cosmetics;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class KillSoundSetup {
    
    private final Player p;
    private ItemStack icon;
    private String name, permission;
    private int slot, page, price;
    private boolean isBuy;
    private float vol1, vol2;
    private Sound sound;
    
    public KillSoundSetup(Player p, String name){
        this.p = p;
        this.name = name;
        this.permission = "ultraskywars.killsound." + name;
        this.icon = new ItemStack(Material.GHAST_TEAR);
        this.slot = 10;
        this.page = 1;
        this.price = 500;
        this.vol1 = 1.0f;
        this.vol2 = 1.0f;
        this.sound = (UltraSkyWarsApi.get().getVc().is1_9to17()) ? Sound.valueOf("ENTITY_PLAYER_LEVELUP") : Sound.valueOf("LEVEL_UP");
        this.isBuy = true;
    }
    
    public void saveKillSound(Player p){
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        plugin.getKillsound().set("killsounds." + name + ".id", plugin.getCos().getNextKillSoundId());
        plugin.getKillsound().set("killsounds." + name + ".name", name);
        plugin.getKillsound().set("killsounds." + name + ".permission", permission);
        ItemStack icon = getIcon().clone();
        ItemMeta im = icon.getItemMeta();
        im.setDisplayName("§a" + name);
        im.setLore(Arrays.asList("§7This is a default lore.", "§7Change me in killsounds.yml"));
        icon.setItemMeta(im);
        plugin.getKillsound().set("killsounds." + name + ".icon", icon);
        plugin.getKillsound().set("killsounds." + name + ".slot", slot);
        plugin.getKillsound().set("killsounds." + name + ".sound", sound.name());
        plugin.getKillsound().set("killsounds." + name + ".vol1", vol1);
        plugin.getKillsound().set("killsounds." + name + ".vol2", vol2);
        plugin.getKillsound().set("killsounds." + name + ".page", page);
        plugin.getKillsound().set("killsounds." + name + ".price", price);
        plugin.getKillsound().set("killsounds." + name + ".isBuy", isBuy);
        plugin.getKillsound().set("killsounds." + name + ".message", slot);
        plugin.getKillsound().save();
        p.sendMessage(plugin.getLang().get(p, "setup.killsounds.save"));
        plugin.getCos().reload();
    }
    
    public Sound getSound(){
        return sound;
    }
    
    public void setSound(Sound sound){
        this.sound = sound;
    }
    
    public float getVol2(){
        return vol2;
    }
    
    public void setVol2(float vol2){
        this.vol2 = vol2;
    }
    
    public float getVol1(){
        return vol1;
    }
    
    public void setVol1(float vol1){
        this.vol1 = vol1;
    }
    
    public ItemStack getIcon(){
        if (icon == null || icon.getType().equals(Material.AIR)){
            return new ItemStack(XMaterial.GHAST_TEAR.parseMaterial());
        }
        return icon;
    }
    
    public void setIcon(ItemStack icon){
        this.icon = icon;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getPermission(){
        return permission;
    }
    
    public void setPermission(String permission){
        this.permission = permission;
    }
    
    public int getSlot(){
        return slot;
    }
    
    public void setSlot(int slot){
        this.slot = slot;
    }
    
    public int getPage(){
        return page;
    }
    
    public void setPage(int page){
        this.page = page;
    }
    
    public int getPrice(){
        return price;
    }
    
    public void setPrice(int price){
        this.price = price;
    }
    
    public boolean isBuy(){
        return isBuy;
    }
    
    public void setBuy(boolean buy){
        isBuy = buy;
    }
    
}