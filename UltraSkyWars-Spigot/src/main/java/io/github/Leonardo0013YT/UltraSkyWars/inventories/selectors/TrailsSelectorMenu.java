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

package io.github.Leonardo0013YT.UltraSkyWars.inventories.selectors;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.UltraInventory;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class TrailsSelectorMenu extends UltraInventory {
    
    private final HashMap<String, Integer> slots = new HashMap<>();
    private final ArrayList<Integer> extra = new ArrayList<>();
    
    public TrailsSelectorMenu(UltraSkyWarsApi plugin, String name){
        super(name);
        this.title = plugin.getLang().get("menus.trailsselector.title");
        reload();
    }
    
    public ArrayList<Integer> getExtra(){
        return extra;
    }
    
    public HashMap<String, Integer> getSlots(){
        return slots;
    }
    
    @Override
    public void reload(){
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        if (plugin.getMenus().isSet("menus." + name)){
            this.rows = plugin.getMenus().getInt("menus." + name + ".rows");
            Map<Integer, ItemStack> config = new HashMap<>();
            Map<Integer, ItemStack> contents = new HashMap<>();
            if (plugin.getMenus().getConfig().isSet("menus." + name + ".items")){
                ConfigurationSection conf = plugin.getMenus().getConfig().getConfigurationSection("menus." + name + ".items");
                for ( String c : conf.getKeys(false) ){
                    int slot = Integer.parseInt(c);
                    ItemStack litem = plugin.getMenus().getConfig().getItemStack("menus." + name + ".items." + c);
                    AtomicReference<String> selected = new AtomicReference<>("NONE");
                    ItemStack item = ItemBuilder.parse(plugin.getMenus().getConfig().getItemStack("menus." + name + ".items." + c).clone(), selected::set,
                            new String[]{"{SELECTED}", plugin.getLang().get("menus.trailsselector.trail.nameItem"), plugin.getLang().get("menus.trailsselector.trail.loreItem")},
                            new String[]{"{LAST}", plugin.getLang().get("menus.last.nameItem"), plugin.getLang().get("menus.last.loreItem")},
                            new String[]{"{NEXT}", plugin.getLang().get("menus.next.nameItem"), plugin.getLang().get("menus.next.loreItem")},
                            new String[]{"{CLOSE}", plugin.getLang().get("menus.trailsselector.close.nameItem"), plugin.getLang().get("menus.trailsselector.close.loreItem")},
                            new String[]{"{DESELECT}", plugin.getLang().get("menus.trailsselector.deselect.nameItem"), plugin.getLang().get("menus.trailsselector.deselect.loreItem")});
                    contents.put(slot, item);
                    if (selected.get().equals("NONE")){
                        extra.add(slot);
                    } else {
                        slots.put(selected.get(), slot);
                    }
                    config.put(slot, litem);
                }
                this.contents = contents;
                this.config = config;
            }
        }
    }
    
    public int getSlot(String name){
        return slots.getOrDefault(name, -1);
    }
    
}