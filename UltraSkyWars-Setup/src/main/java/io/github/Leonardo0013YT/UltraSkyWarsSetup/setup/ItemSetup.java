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

package io.github.Leonardo0013YT.UltraSkyWarsSetup.setup;

import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class ItemSetup {
    
    private final ItemStack item;
    private final ItemStack display;
    private final boolean center;
    private final boolean refill;
    private final int chance;
    private final ArrayList<String> modes;
    
    public ItemSetup(ItemStack item, boolean center, boolean refill, int chance, ArrayList<String> modes){
        this.item = item;
        this.display = item.clone();
        this.center = center;
        this.refill = refill;
        this.chance = chance;
        this.modes = modes;
        ItemMeta im = display.getItemMeta();
        im.setDisplayName("§a" + this);
        im.setLore(Arrays.asList("§7", "§eCenter: §b" + center, "§eRefill: §b" + refill, "§eChance: §b" + chance, "§eModes: §b" + Utils.formatList(modes), "§7"));
        display.setItemMeta(im);
    }
    
    public ItemStack getItem(){
        return item;
    }
    
    public boolean isRefill(){
        return refill;
    }
    
    public ArrayList<String> getModes(){
        return modes;
    }
    
    public boolean isCenter(){
        return center;
    }
    
    public int getChance(){
        return chance;
    }
    
    public ItemStack getDisplay(){
        return display;
    }
    
    @Override
    public String toString(){
        return item.getType().name().toLowerCase() + "_" + item.getAmount() + "_" + item.getDurability() + "_" + item.getData().getData() + "_" + ThreadLocalRandom.current().nextInt(0, 999);
    }
    
}