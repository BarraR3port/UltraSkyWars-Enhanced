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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PartingSetup {
    
    private final Player p;
    private final List<String> lines = new ArrayList<>();
    private ItemStack icon;
    private String name, permission;
    private int slot, page, price;
    private boolean isBuy;
    
    public PartingSetup(Player p, String name){
        this.p = p;
        this.name = name;
        this.permission = "ultraskywars.parting." + name;
        this.icon = new ItemStack(Material.ARMOR_STAND);
        this.slot = 10;
        this.page = 1;
        this.price = 500;
        this.isBuy = true;
    }
    
    public void saveParting(Player p){
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        plugin.getParting().set("partings." + name + ".id", plugin.getCos().getNextPartingId());
        plugin.getParting().set("partings." + name + ".name", name);
        plugin.getParting().set("partings." + name + ".permission", permission);
        ItemStack icon = this.icon.clone();
        ItemMeta im = icon.getItemMeta();
        im.setDisplayName("§a" + name);
        im.setLore(Arrays.asList("§7This is a default lore.", "§7Change me in parting.yml"));
        icon.setItemMeta(im);
        plugin.getParting().set("partings." + name + ".icon", icon);
        plugin.getParting().set("partings." + name + ".slot", slot);
        plugin.getParting().set("partings." + name + ".page", page);
        plugin.getParting().set("partings." + name + ".price", price);
        plugin.getParting().set("partings." + name + ".isBuy", isBuy);
        plugin.getParting().set("partings." + name + ".message", lines);
        plugin.getParting().save();
        p.sendMessage(plugin.getLang().get(p, "setup.parting.save"));
        plugin.getCos().reload();
    }
    
    public List<String> getLines(){
        return lines;
    }
    
    public String getMessage(){
        StringBuilder msg = new StringBuilder();
        for ( String s : lines ){
            msg.append(s).append("\n");
        }
        return msg.toString();
    }
    
    public ItemStack getIcon(){
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