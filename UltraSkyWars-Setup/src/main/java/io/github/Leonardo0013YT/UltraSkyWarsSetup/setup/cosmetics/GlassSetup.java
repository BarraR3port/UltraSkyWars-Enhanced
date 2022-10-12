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
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GlassSetup {
    
    private final Player p;
    private String schematic;
    private String clear;
    private String name;
    private String permission;
    private ItemStack item;
    private boolean isBuy;
    private int price, slot, page;
    
    public GlassSetup(Player p, String name, String schematic, String clear){
        this.p = p;
        this.name = name;
        this.schematic = schematic;
        this.clear = clear;
        this.item = new ItemStack(Material.GLASS, 1);
        this.isBuy = true;
        this.price = 500;
        this.slot = 10;
        this.page = 1;
        this.permission = "ultraskywars.glass." + name;
    }
    
    public void save(UltraSkyWarsApi plugin, Player p){
        String path = "glasses." + name;
        plugin.getGlass().set(path + ".id", plugin.getCos().getNextGlassId());
        plugin.getGlass().set(path + ".name", name);
        plugin.getGlass().set(path + ".schematic", schematic);
        plugin.getGlass().set(path + ".clear", clear);
        ItemStack item = getItem().clone();
        ItemMeta im = item.getItemMeta();
        im.setDisplayName("§a" + name);
        im.setLore(Arrays.asList("§7This is a default lore.", "§7Change me in glass.yml"));
        item.setItemMeta(im);
        plugin.getGlass().set(path + ".item", item);
        plugin.getGlass().set(path + ".isBuy", isBuy);
        plugin.getGlass().set(path + ".price", price);
        plugin.getGlass().set(path + ".slot", slot);
        plugin.getGlass().set(path + ".page", page);
        plugin.getGlass().set(path + ".permission", permission);
        plugin.getGlass().save();
        p.closeInventory();
        p.sendMessage(plugin.getLang().get(p, "setup.glass.save"));
        plugin.getCos().reload();
    }
    
    public Player getP(){
        return p;
    }
    
    public String getPermission(){
        return permission;
    }
    
    public void setPermission(String permission){
        this.permission = permission;
    }
    
    public ItemStack getItem(){
        if (item == null || item.getType().equals(Material.AIR)){
            return new ItemStack(Material.GLASS);
        }
        return item;
    }
    
    public void setItem(ItemStack item){
        this.item = item;
    }
    
    public boolean isBuy(){
        return isBuy;
    }
    
    public void setBuy(boolean buy){
        isBuy = buy;
    }
    
    public String getSchematic(){
        return schematic;
    }
    
    public void setSchematic(String schematic){
        this.schematic = schematic;
    }
    
    public String getClear(){
        return clear;
    }
    
    public void setClear(String clear){
        this.clear = clear;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public int getPrice(){
        return price;
    }
    
    public void setPrice(int price){
        this.price = price;
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
}