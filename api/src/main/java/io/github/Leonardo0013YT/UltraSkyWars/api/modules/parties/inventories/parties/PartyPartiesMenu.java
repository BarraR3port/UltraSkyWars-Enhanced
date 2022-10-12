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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.parties.inventories.parties;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.parties.InjectionParty;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.UltraInventory;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class PartyPartiesMenu extends UltraInventory {
    
    private final InjectionParty ip;
    private final HashMap<String, Integer> slots = new HashMap<>();
    private final ArrayList<Integer> extra = new ArrayList<>();
    private final ArrayList<Integer> gameSlots = new ArrayList<>();
    
    public PartyPartiesMenu(InjectionParty ip, String name){
        super(name);
        this.ip = ip;
        this.title = ip.getParties().get("menus.parties.title");
        reload();
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
                            new String[]{"{PARTIESCLOSE}", ip.getParties().get("menus.parties.close.nameItem"), ip.getParties().get("menus.parties.close.loreItem")},
                            new String[]{"{PARTYSLOT}", "", ""});
                    if (selected.get().equals("NONE")){
                        extra.add(slot);
                    } else if (selected.get().equals("{PARTYSLOT}")){
                        gameSlots.add(slot);
                    } else {
                        slots.put(selected.get(), slot);
                    }
                    contents.put(slot, item);
                    config.put(slot, litem);
                }
                this.contents = contents;
                this.config = config;
            }
        }
    }
    
    public ArrayList<Integer> getGameSlots(){
        return gameSlots;
    }
    
    public ArrayList<Integer> getExtra(){
        return extra;
    }
    
    public int getSlot(String name){
        return slots.getOrDefault(name, -1);
    }
    
}