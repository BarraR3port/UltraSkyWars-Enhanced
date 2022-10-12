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

package io.github.Leonardo0013YT.UltraSkyWars.api.objects;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.NBTEditor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class PrestigeIcon {
    
    private final Material material;
    private final String id, prefix, name, permRequired, lore;
    private final int levelRequired, angelDeathRequired, slot, page;
    private final UltraSkyWarsApi plugin;
    
    public PrestigeIcon(UltraSkyWarsApi plugin, String path){
        this.plugin = plugin;
        this.id = plugin.getLevels().get(path + ".id");
        this.prefix = plugin.getLevels().get(path + ".prefix");
        this.name = plugin.getLevels().get(path + ".name");
        this.lore = plugin.getLevels().get(path + ".lore");
        this.material = Material.valueOf(plugin.getLevels().get(path + ".material"));
        this.slot = plugin.getLevels().getInt(path + ".slot");
        this.page = plugin.getLevels().getInt(path + ".page");
        this.levelRequired = plugin.getLevels().getInt(path + ".requirements.level");
        this.angelDeathRequired = plugin.getLevels().getInt(path + ".requirements.angelDeath");
        this.permRequired = plugin.getLevels().get(path + ".requirements.perm");
    }
    
    public ItemStack getItemStack(Player p, SWPlayer sw){
        boolean required = check(p, sw);
        ItemStack i = ItemBuilder.item(material, 1, ((required) ? "§a" : "§c") + name, lore
                .replace("<status>", (required) ? (sw.getPrestigeIcon().equals(id) ? plugin.getLang().get("menus.prestige.status.selected") : plugin.getLang().get("menus.prestige.status.select")) : plugin.getLang().get("menus.prestige.status.noHas"))
                .replace("<name>", p.getName()).replace("<level>", String.valueOf(sw.getLevel())).replace("<icon>", prefix));
        return NBTEditor.set(NBTEditor.set(i, id, "PRESTIGE_ICON_ID"), required, "PRESTIGE_ICON_HAS");
    }
    
    public boolean check(Player p, SWPlayer sw){
        return sw.getLevel() >= levelRequired && sw.getSoulWellHead() >= angelDeathRequired && p.hasPermission(permRequired);
    }
    
}