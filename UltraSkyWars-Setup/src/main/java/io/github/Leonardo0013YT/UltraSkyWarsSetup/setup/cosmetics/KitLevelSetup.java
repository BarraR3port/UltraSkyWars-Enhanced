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


import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitLevelSetup {
    
    private final Player p;
    private ItemStack[] inv = new ItemStack[]{}, armors = new ItemStack[]{};
    private ItemStack icon = new ItemStack(Material.DIAMOND_SWORD, 1);
    private int price = 100, slot = 10;
    private boolean buy = true;
    
    public KitLevelSetup(Player p){
        this.p = p;
    }
    
    public Player getP(){
        return p;
    }
    
    public boolean isBuy(){
        return buy;
    }
    
    public void setBuy(boolean buy){
        this.buy = buy;
    }
    
    public ItemStack[] getInv(){
        return inv;
    }
    
    public void setInv(ItemStack[] inv){
        this.inv = inv;
    }
    
    public ItemStack[] getArmors(){
        return armors;
    }
    
    public void setArmors(ItemStack[] armors){
        this.armors = armors;
    }
    
    public ItemStack getIcon(){
        if (icon == null || icon.getType().equals(Material.AIR)){
            return new ItemStack(XMaterial.DIAMOND_SWORD.parseMaterial());
        }
        return icon;
    }
    
    public void setIcon(ItemStack icon){
        this.icon = icon;
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
    
}