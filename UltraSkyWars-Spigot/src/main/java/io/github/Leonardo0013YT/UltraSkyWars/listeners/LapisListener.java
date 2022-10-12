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

package io.github.Leonardo0013YT.UltraSkyWars.listeners;

import org.bukkit.DyeColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

public class LapisListener implements Listener {
    
    private final ItemStack lapis;
    
    public LapisListener(){
        Dye d = new Dye();
        d.setColor(DyeColor.BLUE);
        this.lapis = d.toItemStack();
        this.lapis.setAmount(3);
    }
    
    @EventHandler
    public void openInventoryEvent(InventoryOpenEvent e){
        if (e.getInventory() instanceof EnchantingInventory){
            e.getInventory().setItem(1, this.lapis);
        }
        
    }
    
    @EventHandler
    public void closeInventoryEvent(InventoryCloseEvent e){
        if (e.getInventory() instanceof EnchantingInventory){
            e.getInventory().setItem(1, null);
        }
    }
    
    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent e){
        if (e.getClickedInventory() instanceof EnchantingInventory && e.getSlot() == 1){
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void enchantItemEvent(EnchantItemEvent e){
        e.getInventory().setItem(1, this.lapis);
    }
    
}