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

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.config.Settings;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.RewardType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class SoulWellRewardSetup {
    
    private final ArrayList<String> cmds = new ArrayList<>();
    private String name;
    private ItemStack icon = new ItemStack(Material.ARROW, 1);
    private RewardType type;
    private double chance;
    
    public SoulWellRewardSetup(String name, RewardType type, double chance){
        this.name = name;
        this.type = type;
        this.chance = chance;
    }
    
    public ArrayList<String> getCmds(){
        return cmds;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public ItemStack getIcon(){
        ItemStack icon = this.icon.clone();
        ItemMeta iconM = icon.getItemMeta();
        iconM.setDisplayName(name.replaceAll("&", "§"));
        Settings lang = UltraSkyWarsApi.get().getLang();
        String s = lang.get(null, "menus.soulwellmenu.item.loreItem").replace("<rarity>", lang.get(null, "soulwell.rarity." + type.name().toLowerCase())).replaceAll("<chance>", String.valueOf(chance));
        iconM.setLore(s.isEmpty() ? new ArrayList<>() : Arrays.asList(s.split("\\n")));
        icon.setItemMeta(iconM);
        return icon;
    }
    
    public void setIcon(ItemStack icon){
        this.icon = icon;
    }
    
    public double getChance(){
        return chance;
    }
    
    public void setChance(double chance){
        this.chance = chance;
    }
    
    public RewardType getType(){
        return type;
    }
    
    public void setType(RewardType type){
        this.type = type;
    }
}