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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.SpecialItems.managers;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.SpecialItems.InjectionSpecialItems;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.SpecialItems.items.SpecialItem;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.SpecialItems.items.types.CompassItem;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.SpecialItems.items.types.InstantTNTItem;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.SpecialItems.items.types.SoupItem;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.SpecialItems.items.types.TNTLaunchItem;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public class ItemManager {
    
    private final UltraSkyWarsApi plugin;
    private final InjectionSpecialItems isi;
    private ItemStack compass, instantTNT, soup, endBuff, TNTLaunch;
    private SpecialItem compassItem, instantTNTItem, soupItem, endBuffItem, TNTLaunchItem;
    
    public ItemManager(UltraSkyWarsApi plugin, InjectionSpecialItems isi){
        this.plugin = plugin;
        this.isi = isi;
        reload();
    }
    
    public void reload(){
        this.compass = ItemBuilder.item(XMaterial.COMPASS, plugin.getLang().get("items.compass.nameItem"), plugin.getLang().get("items.compass.loreItem"));
        this.instantTNT = ItemBuilder.item(XMaterial.TNT, plugin.getLang().get("items.instantTNT.nameItem"), plugin.getLang().get("items.instantTNT.loreItem"));
        this.soup = ItemBuilder.item(XMaterial.MUSHROOM_STEW, plugin.getLang().get("items.soup.nameItem"), plugin.getLang().get("items.soup.loreItem"));
        this.endBuff = ItemBuilder.item(XMaterial.ENDER_PEARL, plugin.getLang().get("items.endBuff.nameItem"), plugin.getLang().get("items.endBuff.loreItem"));
        this.TNTLaunch = ItemBuilder.item(XMaterial.TNT, plugin.getLang().get("items.TNTLaunch.nameItem"), plugin.getLang().get("items.TNTLaunch.loreItem"));
        this.compassItem = new CompassItem(isi);
        this.instantTNTItem = new InstantTNTItem(isi);
        this.TNTLaunchItem = new TNTLaunchItem(isi);
        this.soupItem = new SoupItem(isi);
    }
}