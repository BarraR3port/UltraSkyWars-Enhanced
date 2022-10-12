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

package io.github.Leonardo0013YT.UltraSkyWars.api.managers;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ItemManager {
    
    private final ArrayList<ItemStack> items = new ArrayList<>();
    private ItemStack challenges, lobby, team, setup, center, island, votes, kits, leave, spectate, options, play;
    
    public ItemManager(){
        reload();
    }
    
    public void reload(){
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        items.clear();
        if (!plugin.getCm().getChallenges().equals(XMaterial.AIR)){
            this.challenges = ItemBuilder.item(plugin.getCm().getChallenges(), 1, plugin.getLang().get("items.challenges.nameItem"), plugin.getLang().get("items.challenges.loreItem"));
            items.add(challenges);
        }
        if (!plugin.getCm().getLobby().equals(XMaterial.AIR)){
            this.lobby = ItemBuilder.item(plugin.getCm().getLobby(), 1, plugin.getLang().get("items.lobby.nameItem"), plugin.getLang().get("items.lobby.loreItem"));
            items.add(lobby);
        }
        if (!plugin.getCm().getTeam().equals(XMaterial.AIR)){
            this.team = ItemBuilder.item(plugin.getCm().getTeam(), 1, plugin.getLang().get("items.team.nameItem"), plugin.getLang().get("items.team.loreItem"));
            items.add(team);
        }
        if (!plugin.getCm().getSpectate().equals(XMaterial.AIR)){
            this.spectate = ItemBuilder.item(plugin.getCm().getSpectate(), 1, plugin.getLang().get("items.spectate.nameItem"), plugin.getLang().get("items.spectate.loreItem"));
            items.add(spectate);
        }
        if (!plugin.getCm().getOptions().equals(XMaterial.AIR)){
            this.options = ItemBuilder.item(plugin.getCm().getOptions(), 1, plugin.getLang().get("items.options.nameItem"), plugin.getLang().get("items.options.loreItem"));
            items.add(options);
        }
        if (!plugin.getCm().getPlay().equals(XMaterial.AIR)){
            this.play = ItemBuilder.item(plugin.getCm().getPlay(), 1, plugin.getLang().get("items.play.nameItem"), plugin.getLang().get("items.play.loreItem"));
            items.add(play);
        }
        if (!plugin.getCm().getLeave().equals(XMaterial.AIR)){
            this.leave = ItemBuilder.item(plugin.getCm().getLeave(), 1, plugin.getLang().get("items.leave.nameItem"), plugin.getLang().get("items.leave.loreItem"));
            items.add(leave);
        }
        if (!plugin.getCm().getKits().equals(XMaterial.AIR)){
            this.kits = ItemBuilder.item(plugin.getCm().getKits(), 1, plugin.getLang().get("items.kits.nameItem"), plugin.getLang().get("items.kits.loreItem"));
            items.add(kits);
        }
        if (!plugin.getCm().getVotes().equals(Material.AIR)){
            this.votes = ItemBuilder.item(plugin.getCm().getVotes(), 1, plugin.getLang().get("items.votes.nameItem"), plugin.getLang().get("items.votes.loreItem"));
            items.add(votes);
        }
        if (!plugin.getCm().getSetup().equals(XMaterial.AIR)){
            this.setup = ItemBuilder.item(plugin.getCm().getSetup(), 1, plugin.getLang().get("items.setup.nameItem"), plugin.getLang().get("items.setup.loreItem"));
            items.add(setup);
        }
        if (!plugin.getCm().getCenter().equals(XMaterial.AIR)){
            this.center = ItemBuilder.item(plugin.getCm().getCenter(), 1, plugin.getLang().get("items.center.nameItem"), plugin.getLang().get("items.center.loreItem"));
            items.add(center);
        }
        if (!plugin.getCm().getIsland().equals(XMaterial.AIR)){
            this.island = ItemBuilder.item(plugin.getCm().getIsland(), 1, plugin.getLang().get("items.island.nameItem"), plugin.getLang().get("items.island.loreItem"));
            items.add(island);
        }
    }
    
    public ArrayList<ItemStack> getItems(){
        return items;
    }
    
    public ItemStack getChallenges(){
        return challenges;
    }
    
    public ItemStack getLobby(){
        return lobby;
    }
    
    public ItemStack getTeam(){
        return team;
    }
    
    public ItemStack getSpectate(){
        return spectate;
    }
    
    public ItemStack getOptions(){
        return options;
    }
    
    public ItemStack getPlay(){
        return play;
    }
    
    public ItemStack getLeave(){
        return leave;
    }
    
    public ItemStack getKits(){
        return kits;
    }
    
    public ItemStack getVotes(){
        return votes;
    }
    
    public ItemStack getIsland(){
        return island;
    }
    
    public ItemStack getCenter(){
        return center;
    }
    
    public ItemStack getSetup(){
        return setup;
    }
}