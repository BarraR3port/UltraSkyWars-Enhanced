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
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.UltraInventory;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class SpectateOptionsMenu extends UltraInventory {
    
    public SpectateOptionsMenu(UltraSkyWarsApi plugin, String name){
        super(name);
        this.title = plugin.getLang().get(null, "menus." + name + ".title");
        reload();
        plugin.getUim().getActions().put(title, (b) -> {
            InventoryClickEvent e = b.getInventoryClickEvent();
            Player p = b.getPlayer();
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta()){
                return;
            }
            if (!item.getItemMeta().hasDisplayName()){
                return;
            }
            Game game = plugin.getGm().getGameByPlayer(p);
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            ItemMeta im = item.getItemMeta();
            String display = im.getDisplayName();
            if (display.equals(plugin.getLang().get(p, "menus.options.speedI.nameItem"))){
                sw.setSpeed(0.2F);
                game.updateSpectatorOptions(p);
                p.sendMessage(plugin.getLang().get(p, "messages.setSpeed").replaceAll("<speed>", "I"));
                p.closeInventory();
            }
            if (display.equals(plugin.getLang().get(p, "menus.options.speedII.nameItem"))){
                sw.setSpeed(0.4F);
                game.updateSpectatorOptions(p);
                p.sendMessage(plugin.getLang().get(p, "messages.setSpeed").replaceAll("<speed>", "II"));
                p.closeInventory();
            }
            if (display.equals(plugin.getLang().get(p, "menus.options.speedIII.nameItem"))){
                sw.setSpeed(0.6F);
                game.updateSpectatorOptions(p);
                p.sendMessage(plugin.getLang().get(p, "messages.setSpeed").replaceAll("<speed>", "III"));
                p.closeInventory();
            }
            if (display.equals(plugin.getLang().get(p, "menus.options.speedIV.nameItem"))){
                sw.setSpeed(0.8F);
                game.updateSpectatorOptions(p);
                p.sendMessage(plugin.getLang().get(p, "messages.setSpeed").replaceAll("<speed>", "IV"));
                p.closeInventory();
            }
            if (display.equals(plugin.getLang().get(p, "menus.options.speedV.nameItem"))){
                sw.setSpeed(1.0F);
                game.updateSpectatorOptions(p);
                p.sendMessage(plugin.getLang().get(p, "messages.setSpeed").replaceAll("<speed>", "V"));
                p.closeInventory();
            }
            if (display.equals(plugin.getLang().get(p, "menus.options.nightvision.nameItem"))){
                if (!sw.isNightVision()){
                    sw.setNightVision(true);
                    p.sendMessage(plugin.getLang().get(p, "messages.setNightVision").replaceAll("<state>", plugin.getLang().get(p, "activated")));
                } else {
                    sw.setNightVision(false);
                    p.sendMessage(plugin.getLang().get(p, "messages.setNightVision").replaceAll("<state>", plugin.getLang().get(p, "deactivated")));
                }
                game.updateSpectatorOptions(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.options.spects.nameItem"))){
                if (!sw.isSpectatorsView()){
                    sw.setSpectatorsView(true);
                    p.sendMessage(plugin.getLang().get(p, "messages.setSpectator").replaceAll("<state>", plugin.getLang().get(p, "activated")));
                } else {
                    sw.setSpectatorsView(false);
                    p.sendMessage(plugin.getLang().get(p, "messages.setSpectator").replaceAll("<state>", plugin.getLang().get(p, "deactivated")));
                }
                game.updateSpectatorOptions(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.options.first.nameItem"))){
                if (!sw.isFirstPerson()){
                    sw.setFirstPerson(true);
                    p.sendMessage(plugin.getLang().get(p, "messages.setFirstPerson").replaceAll("<state>", plugin.getLang().get(p, "activated")));
                } else {
                    sw.setFirstPerson(false);
                    p.sendMessage(plugin.getLang().get(p, "messages.setFirstPerson").replaceAll("<state>", plugin.getLang().get(p, "deactivated")));
                }
                game.updateSpectatorOptions(p);
            }
        });
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
                    ItemStack item = ItemBuilder.parse(plugin.getMenus().getConfig().getItemStack("menus." + name + ".items." + c).clone(),
                            new String[]{"{OPTIONSSPEEDI}", plugin.getLang().get(null, "menus.options.speedI.nameItem"), plugin.getLang().get(null, "menus.options.speedI.loreItem")},
                            new String[]{"{OPTIONSSPEEDII}", plugin.getLang().get(null, "menus.options.speedII.nameItem"), plugin.getLang().get(null, "menus.options.speedII.loreItem")},
                            new String[]{"{OPTIONSSPEEDIII}", plugin.getLang().get(null, "menus.options.speedIII.nameItem"), plugin.getLang().get(null, "menus.options.speedIII.loreItem")},
                            new String[]{"{OPTIONSSPEEDIV}", plugin.getLang().get(null, "menus.options.speedIV.nameItem"), plugin.getLang().get(null, "menus.options.speedIV.loreItem")},
                            new String[]{"{OPTIONSSPEEDV}", plugin.getLang().get(null, "menus.options.speedV.nameItem"), plugin.getLang().get(null, "menus.options.speedV.loreItem")},
                            new String[]{"{OPTIONSNIGHTVISION}", plugin.getLang().get(null, "menus.options.nightvision.nameItem"), plugin.getLang().get(null, "menus.options.nightvision.loreItem")},
                            new String[]{"{OPTIONSSPECTS}", plugin.getLang().get(null, "menus.options.spects.nameItem"), plugin.getLang().get(null, "menus.options.spects.loreItem")},
                            new String[]{"{OPTIONSFIRST}", plugin.getLang().get(null, "menus.options.first.nameItem"), plugin.getLang().get(null, "menus.options.first.loreItem")});
                    contents.put(slot, item);
                    config.put(slot, litem);
                }
                this.contents = contents;
                this.config = config;
            }
        }
    }
    
}