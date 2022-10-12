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
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SoulWellSetup {
    
    private final UltraSkyWarsApi plugin;
    private final ArrayList<SoulWellRewardSetup> rewards = new ArrayList<>();
    private SoulWellRewardSetup actual;
    
    public SoulWellSetup(UltraSkyWarsApi plugin){
        this.plugin = plugin;
    }
    
    public SoulWellRewardSetup getActual(){
        return actual;
    }
    
    public void setActual(SoulWellRewardSetup actual){
        this.actual = actual;
    }
    
    public ArrayList<SoulWellRewardSetup> getRewards(){
        return rewards;
    }
    
    public void saveSoulWellReward(Player p){
        SoulWellRewardSetup tts = actual;
        rewards.add(tts);
        actual = null;
        p.sendMessage(plugin.getLang().get(p, "setup.soulwellreward.save"));
    }
    
    public void saveSoulWell(Player p){
        int id = plugin.getLvl().getNextID();
        for ( SoulWellRewardSetup swrs : rewards ){
            plugin.getRewards().set("rewards." + id + ".name", swrs.getName());
            plugin.getRewards().set("rewards." + id + ".type", swrs.getType().name());
            plugin.getRewards().set("rewards." + id + ".chance", swrs.getChance());
            Material m = swrs.getIcon().getType();
            if (m.equals(XMaterial.PLAYER_HEAD.parseMaterial())){
                plugin.getRewards().set("rewards." + id + ".icon", NBTEditor.getTexture(swrs.getIcon()));
                plugin.getRewards().set("rewards." + id + ".iconName", (swrs.getIcon().getItemMeta().hasDisplayName()) ? swrs.getIcon().getItemMeta().getDisplayName() : "§aDefault Name");
                plugin.getRewards().set("rewards." + id + ".iconLore", (swrs.getIcon().getItemMeta().hasLore()) ? swrs.getIcon().getItemMeta().getLore() : new ArrayList<>());
            } else {
                plugin.getRewards().set("rewards." + id + ".icon", swrs.getIcon());
            }
            plugin.getRewards().set("rewards." + id + ".cmds", swrs.getCmds());
            id++;
        }
        plugin.getRewards().save();
        plugin.getLvl().reload();
        p.sendMessage(plugin.getLang().get(p, "setup.soulwellreward.saveC"));
    }
    
}