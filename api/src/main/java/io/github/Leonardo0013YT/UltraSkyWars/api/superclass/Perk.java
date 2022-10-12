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

package io.github.Leonardo0013YT.UltraSkyWars.api.superclass;

import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.perks.InjectionPerks;
import io.github.Leonardo0013YT.UltraSkyWars.api.objects.PerkLevel;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Perk {
    
    private final Material material;
    private final String name, lore;
    private final int id, amount, slot;
    private final boolean disabled;
    private final InjectionPerks plugin;
    private final List<String> gameTypes;
    private final HashMap<Integer, PerkLevel> levels = new HashMap<>();
    
    public Perk(InjectionPerks plugin, String path){
        this.plugin = plugin;
        this.disabled = plugin.getPerks().getBooleanOrDefault(path + ".disabled", false);
        this.id = plugin.getPerks().getInt(path + ".id");
        this.material = XMaterial.matchXMaterial(plugin.getPerks().get(path + ".icon.material")).orElse(XMaterial.SILVERFISH_SPAWN_EGG).parseMaterial();
        this.name = plugin.getPerks().get(path + ".name");
        this.lore = plugin.getPerks().get(path + ".lore");
        this.slot = plugin.getPerks().getInt(path + ".slot");
        this.amount = plugin.getPerks().getInt(path + ".icon.amount");
        this.gameTypes = plugin.getPerks().getListOrDefault(path + ".gameTypes", Arrays.asList("SOLO", "TEAM", "RANKED"));
        if (plugin.getPerks().isSet(path + ".levels")){
            for ( String s : plugin.getPerks().getConfig().getConfigurationSection(path + ".levels").getKeys(false) ){
                PerkLevel pl = new PerkLevel(plugin, path + ".levels." + s);
                levels.put(pl.getLevel(), pl);
            }
        }
    }
    
    public boolean isDisabled(){
        return disabled;
    }
    
    public List<String> getGameTypes(){
        return gameTypes;
    }
    
    public ItemStack toIcon(Player p, SWPlayer sw){
        PerkLevel pl = null;
        boolean locked = true;
        if (sw.getPerksData().containsKey(id)){
            pl = levels.get(sw.getPerksData().get(id));
            locked = false;
        }
        if (pl == null){
            pl = levels.get(1);
            if (pl != null && !locked){
                sw.getPerksData().put(id, pl.getLevel());
            }
        }
        int percent = (pl == null) ? 0 : pl.getPercent();
        int probability = (pl == null) ? 0 : pl.getProbability();
        PerkLevel next = levels.get((pl == null) ? 1 : pl.getLevel() + 1);
        ItemStack item = ItemBuilder.item(XMaterial.matchXMaterial(material.name()).orElse(XMaterial.SILVERFISH_SPAWN_EGG), amount, name, lore
                .replace("<amount>", String.valueOf(percent)).replace("<percent>", String.valueOf(percent))
                .replace("<probability>", String.valueOf(probability))
                .replace("<status>", (!sw.getPerksEnabled().contains(id)) ? plugin.getPerks().get(p, "status.offState") : plugin.getPerks().get(p, "status.onState"))
                .replace("<price>", /*(locked ) ? plugin.getPerks().get(p, "price.locked") : */(next == null) ? plugin.getPerks().get(p, "price.unlocked") : plugin.getPerks().get(p, "price.nextPrice").replace("<price>", String.valueOf(next.getPrice())))
                .replace("<state>", (locked ? plugin.getPerks().get(p, "states.noHas") : (next == null) ? plugin.getPerks().get(p, "states.maxed") : plugin.getPerks().get(p, "states.upgrade"))));
        // PERK_DATA -> PerkID, Locked, Level.
        return NBTEditor.set(item, id + ":" + locked + ":" + ((pl == null) ? 1 : pl.getLevel()) + ":" + (next == null), "PERK_DATA");
    }
    
    public HashMap<Integer, PerkLevel> getLevels(){
        return levels;
    }
    
    public String getName(){
        return name;
    }
    
    public int getSlot(){
        return slot;
    }
    
    public int getId(){
        return id;
    }
    
    public boolean isReduced(SWPlayer sw){
        PerkLevel pl = levels.get(sw.getPerksData().get(id));
        int probability = (pl == null) ? 0 : pl.getProbability();
        return ThreadLocalRandom.current().nextInt(0, 100) <= probability;
    }
    
    public double getNewAmount(double damage, SWPlayer sw){
        PerkLevel pl = levels.get(sw.getPerksData().get(id));
        int percent = (pl == null) ? 0 : pl.getPercent();
        double now = damage * (percent * 0.01);
        return damage - now;
    }
}