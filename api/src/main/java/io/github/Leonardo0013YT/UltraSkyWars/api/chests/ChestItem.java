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

package io.github.Leonardo0013YT.UltraSkyWars.api.chests;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class ChestItem {
    
    private final ItemStack item;
    @Getter
    private final boolean center;
    @Getter
    private final boolean refill;
    private final ArrayList<String> modes;
    private final int percent;
    @Getter
    private final int min;
    @Getter
    private final int max;
    
    public ChestItem(ItemStack item, int percent, int min, int max, boolean center, boolean refill, ArrayList<String> modes){
        this.item = item;
        this.min = min;
        this.max = max;
        this.center = center;
        this.percent = percent;
        this.refill = refill;
        this.modes = modes;
    }
    
    public ArrayList<String> getModes(){
        return modes;
    }
    
    public int getPercent(){
        return percent * 100;
    }
    
    public ItemStack getItem(){
        ItemStack item = this.item.clone();
        if (min != max){
            int now = new Random().nextInt(this.max - this.min + 1) + this.min;
            item.setAmount(now);
            return item;
        }
        return item;
    }
    
}