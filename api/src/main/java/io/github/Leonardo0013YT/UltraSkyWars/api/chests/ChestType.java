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

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class ChestType {
    
    private final SWChest chest;
    private final String key, edit, vote, editLore, voteLore, editName, voteName;
    private final int setupSlot, voteSlot;
    private final ItemStack voteItem, setupItem;
    private final boolean refillChange, armorAllTeams;
    
    public ChestType(UltraSkyWarsApi plugin, String key){
        this.key = key;
        this.edit = plugin.getChestType().get("types." + key + ".edit");
        this.setupSlot = plugin.getChestType().getInt("types." + key + ".slots.setup");
        this.voteSlot = plugin.getChestType().getInt("types." + key + ".slots.votes");
        this.vote = plugin.getChestType().get("votes.chest." + key);
        this.editName = plugin.getChestType().get("lang.chests." + key + ".nameItem");
        this.editLore = listString(plugin.getChestType().getList("lang.chests." + key + ".loreItem"));
        this.voteName = plugin.getChestType().get("lang.chest." + key + ".nameItem");
        this.voteLore = listString(plugin.getChestType().getList("lang.chest." + key + ".loreItem"));
        this.voteItem = plugin.getChestType().getConfig().getItemStack("menus.chest.items." + voteSlot);
        this.setupItem = plugin.getChestType().getConfig().getItemStack("menus.chests.items." + setupSlot);
        this.refillChange = plugin.getChestType().getBooleanOrDefault("types." + key + ".refillChange", false);
        this.armorAllTeams = plugin.getChestType().getBooleanOrDefault("types." + key + ".armorAllTeams", true);
        this.chest = new SWChest(plugin, "chests." + key);
    }
    
    public String listString(List<String> list){
        return String.join("\\n", list);
    }
    
}