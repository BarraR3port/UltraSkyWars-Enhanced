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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.soulwell.upgrades;

import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.InjectionSoulWell;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public class SoulWellAngelOfDeath {
    
    private final int level;
    private final int probability;
    private final double price;
    private final String name;
    private final String lore;
    private final InjectionSoulWell is;
    private final String key;
    
    public SoulWellAngelOfDeath(InjectionSoulWell is, String path, String key){
        this.is = is;
        this.key = key;
        this.level = is.getSoulwell().getInt(path + ".level");
        this.probability = is.getSoulwell().getInt(path + ".probability");
        this.price = is.getSoulwell().getDouble(path + ".price");
        this.name = is.getSoulwell().get(path + ".name");
        this.lore = is.getSoulwell().get(path + ".lore");
    }
    
    public ItemStack getIcon(){
        ItemStack icon = ItemBuilder.item(XMaterial.WITHER_SKELETON_SKULL, name, lore.replaceAll("<desc>", is.getSoulwell().get("angelLore")));
        return icon;
    }
    
}