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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.soulwell;

import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.InjectionSoulWell;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.NBTEditor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public class SoulWellShop {
    
    private final Material material;
    private final short data;
    private final int amount;
    private final int slot;
    private final int give;
    private final double price;
    private final String name;
    private final String lore;
    private final InjectionSoulWell is;
    private final String key;
    
    public SoulWellShop(InjectionSoulWell is, String path, String key){
        this.is = is;
        this.key = key;
        this.material = Material.valueOf(is.getSoulwell().get(path + ".material"));
        this.data = (short) is.getSoulwell().getInt(path + ".data");
        this.amount = is.getSoulwell().getInt(path + ".amount");
        this.slot = is.getSoulwell().getInt(path + ".slot");
        this.give = is.getSoulwell().getInt(path + ".give");
        this.price = is.getSoulwell().getDouble(path + ".price");
        this.name = is.getSoulwell().get(path + ".name");
        this.lore = is.getSoulwell().get(path + ".lore");
    }
    
    public ItemStack getIcon(double price){
        ItemStack icon = ItemBuilder.item(material, amount, data, name, lore.replaceAll("<status>", (price >= this.price) ? is.getSoulwell().get("buy") : is.getSoulwell().get("noMoney")).replaceAll("<price>", String.valueOf(this.price)).replaceAll("<souls>", String.valueOf(give)));
        return NBTEditor.set(icon, key, "SOULWELL", "SHOP", "SOULS");
    }
    
}