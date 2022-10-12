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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.ranks.ranks;

import io.github.Leonardo0013YT.UltraSkyWars.api.modules.ranks.InjectionEloRank;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SeasonDivision {
    
    private final ItemStack icon;
    private final int topMin;
    private final int topMax;
    private final String name;
    private final List<String> lore;
    private final List<String> ranks;
    private final List<String> rewards;
    
    public SeasonDivision(InjectionEloRank ier, String path, int season, boolean passed){
        this.topMin = ier.getRankeds().getInt(path + ".top.min");
        this.topMax = ier.getRankeds().getInt(path + ".top.max");
        this.name = ier.getRankeds().get(path + ".name");
        this.ranks = ier.getRankeds().getList(path + ".ranks");
        this.rewards = ier.getRankeds().getList(path + ".rewards.commands");
        List<String> lore = new ArrayList<>();
        for ( String l : ier.getRankeds().getList(path + ".lore") ){
            lore.add(l.replaceAll("&", "§").replaceAll("<season>", String.valueOf(season)).replaceAll("<status>", passed ? ier.getRankeds().get("passed") : ier.getRankeds().get("now")));
        }
        this.lore = lore;
        this.icon = ItemBuilder.item(new ItemStack(XMaterial.matchDefinedXMaterial(ier.getRankeds().get(path + ".icon.material"), (byte) ier.getRankeds().getInt(path + ".icon.data")).orElse(XMaterial.OAK_WOOD).parseMaterial(), ier.getRankeds().getInt(path + ".icon.amount"), (short) ier.getRankeds().getInt(path + ".icon.data")), name + ier.getRankeds().get("format").replaceAll("<max>", String.valueOf(topMax)).replaceAll("<min>", String.valueOf(topMin)), lore);
    }
    
    public String getName(){
        return name;
    }
    
    public ItemStack getIcon(){
        return icon;
    }
}