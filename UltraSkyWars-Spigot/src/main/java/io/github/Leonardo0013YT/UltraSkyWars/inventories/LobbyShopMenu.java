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

package io.github.Leonardo0013YT.UltraSkyWars.inventories;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.UltraInventory;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class LobbyShopMenu extends UltraInventory {
    
    public LobbyShopMenu(UltraSkyWarsApi plugin, String name){
        super(name);
        this.title = plugin.getLang().get("menus." + name + ".title");
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
                            new String[]{"{LOBBYSOULWELL}", plugin.getLang().get("menus.lobby.soulwell.nameItem"), plugin.getLang().get("menus.lobby.soulwell.loreItem")},
                            new String[]{"{LOBBYKITS}", plugin.getLang().get("menus.lobby.perksKits.nameItem"), plugin.getLang().get("menus.lobby.perksKits.loreItem")},
                            new String[]{"{LOBBYLEVELS}", plugin.getLang().get("menus.lobby.levels.nameItem"), plugin.getLang().get("menus.lobby.levels.loreItem")},
                            new String[]{"{LOBBYTRAILS}", plugin.getLang().get("menus.lobby.trails.nameItem"), plugin.getLang().get("menus.lobby.trails.loreItem")},
                            new String[]{"{LOBBYTAUNTS}", plugin.getLang().get("menus.lobby.taunts.nameItem"), plugin.getLang().get("menus.lobby.taunts.loreItem")},
                            new String[]{"{LOBBYBALLOONS}", plugin.getLang().get("menus.lobby.balloons.nameItem"), plugin.getLang().get("menus.lobby.balloons.loreItem")},
                            new String[]{"{LOBBYGLASS}", plugin.getLang().get("menus.lobby.glass.nameItem"), plugin.getLang().get("menus.lobby.glass.loreItem")},
                            new String[]{"{LOBBYKILLSOUND}", plugin.getLang().get("menus.lobby.killsound.nameItem"), plugin.getLang().get("menus.lobby.killsound.loreItem")},
                            new String[]{"{LOBBYKILLEFFECTS}", plugin.getLang().get("menus.lobby.killeffects.nameItem"), plugin.getLang().get("menus.lobby.killeffects.loreItem")},
                            new String[]{"{LOBBYWINDANCES}", plugin.getLang().get("menus.lobby.windances.nameItem"), plugin.getLang().get("menus.lobby.windances.loreItem")},
                            new String[]{"{LOBBYPARTING}", plugin.getLang().get("menus.lobby.parting.nameItem"), plugin.getLang().get("menus.lobby.parting.loreItem")},
                            new String[]{"{LOBBYWINEFFECTS}", plugin.getLang().get("menus.lobby.wineffects.nameItem"), plugin.getLang().get("menus.lobby.wineffects.loreItem")});
                    if (selected.get().equals("{LOBBYTRAILS}") && plugin.getCm().isCosmeticsTrails()){
                        contents.put(slot, item);
                        config.put(slot, litem);
                    } else if (selected.get().equals("{LOBBYTAUNTS}") && plugin.getCm().isCosmeticsTaunts()){
                        contents.put(slot, item);
                        config.put(slot, litem);
                    } else if (selected.get().equals("{LOBBYBALLOONS}") && plugin.getCm().isCosmeticsBalloons()){
                        contents.put(slot, item);
                        config.put(slot, litem);
                    } else if (selected.get().equals("{LOBBYGLASS}") && plugin.getCm().isCosmeticsGlasses()){
                        contents.put(slot, item);
                        config.put(slot, litem);
                    } else if (selected.get().equals("{LOBBYKILLSOUND}") && plugin.getCm().isCosmeticsKillSounds()){
                        contents.put(slot, item);
                        config.put(slot, litem);
                    } else if (selected.get().equals("{LOBBYKILLEFFECTS}") && plugin.getCm().isCosmeticsKillEffect()){
                        contents.put(slot, item);
                        config.put(slot, litem);
                    } else if (selected.get().equals("{LOBBYWINDANCES}") && plugin.getCm().isCosmeticsWinDance()){
                        contents.put(slot, item);
                        config.put(slot, litem);
                    } else if (selected.get().equals("{LOBBYPARTING}") && plugin.getCm().isCosmeticsPartings()){
                        contents.put(slot, item);
                        config.put(slot, litem);
                    } else if (selected.get().equals("{LOBBYWINEFFECTS}") && plugin.getCm().isCosmeticsWinEffects()){
                        contents.put(slot, item);
                        config.put(slot, litem);
                    } else {
                        contents.put(slot, item);
                        config.put(slot, litem);
                    }
                }
                this.contents = contents;
                this.config = config;
            }
        }
    }
    
}