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

package io.github.Leonardo0013YT.UltraSkyWars.inventories.votes;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.TimeType;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.UltraInventory;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.api.vote.Vote;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class VoteTimeMenu extends UltraInventory {
    
    public VoteTimeMenu(UltraSkyWarsApi plugin, String name){
        super(name);
        this.title = plugin.getLang().get(null, "menus." + name + ".title");
        reload();
        plugin.getUim().getActions().put(title, (b) -> {
            InventoryClickEvent e = b.getInventoryClickEvent();
            Player p = b.getPlayer();
            if (plugin.isSetupLobby(p)) return;
            Game g = plugin.getGm().getGameByPlayer(p);
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
            if (g == null) return;
            Vote vote = g.getVote();
            ItemMeta im = item.getItemMeta();
            String display = im.getDisplayName();
            if (display.equals(plugin.getLang().get(p, "menus.time.dawn.nameItem"))){
                if (!p.hasPermission("ultraskywars.votes.time.*") && !p.hasPermission("ultraskywars.votes.time.dawn")){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getTimeType() != null){
                    if (vp.getTimeType().equals(TimeType.DAWN)){
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.time.dawn")));
                        return;
                    }
                }
                vp.setTimeType(TimeType.DAWN);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.time.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.time.dawn")).replaceAll("<votes>", String.valueOf(vote.getVotes("DAWN"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("time"),
                        new String[]{"<dawntime>", String.valueOf(vote.getVotes("DAWN"))},
                        new String[]{"<daytime>", String.valueOf(vote.getVotes("DAY"))},
                        new String[]{"<afternoontime>", String.valueOf(vote.getVotes("AFTERNOON"))},
                        new String[]{"<nighttime>", String.valueOf(vote.getVotes("NIGHT"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.time.day.nameItem"))){
                if (!p.hasPermission("ultraskywars.votes.time.*") && !p.hasPermission("ultraskywars.votes.time.day")){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getTimeType() != null){
                    if (vp.getTimeType().equals(TimeType.DAY)){
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.time.day")));
                        return;
                    }
                }
                vp.setTimeType(TimeType.DAY);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.time.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.time.day")).replaceAll("<votes>", String.valueOf(vote.getVotes("DAY"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("time"),
                        new String[]{"<dawntime>", String.valueOf(vote.getVotes("DAWN"))},
                        new String[]{"<daytime>", String.valueOf(vote.getVotes("DAY"))},
                        new String[]{"<afternoontime>", String.valueOf(vote.getVotes("AFTERNOON"))},
                        new String[]{"<nighttime>", String.valueOf(vote.getVotes("NIGHT"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.time.afternoon.nameItem"))){
                if (!p.hasPermission("ultraskywars.votes.time.*") && !p.hasPermission("ultraskywars.votes.time.afternoon")){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getTimeType() != null){
                    if (vp.getTimeType().equals(TimeType.AFTERNOON)){
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.time.afternoon")));
                        return;
                    }
                }
                vp.setTimeType(TimeType.AFTERNOON);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.time.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.time.afternoon")).replaceAll("<votes>", String.valueOf(vote.getVotes("AFTERNOON"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("time"),
                        new String[]{"<dawntime>", String.valueOf(vote.getVotes("DAWN"))},
                        new String[]{"<daytime>", String.valueOf(vote.getVotes("DAY"))},
                        new String[]{"<afternoontime>", String.valueOf(vote.getVotes("AFTERNOON"))},
                        new String[]{"<nighttime>", String.valueOf(vote.getVotes("NIGHT"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.time.night.nameItem"))){
                if (!p.hasPermission("ultraskywars.votes.time.*") && !p.hasPermission("ultraskywars.votes.time.night")){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getTimeType() != null){
                    if (vp.getTimeType().equals(TimeType.NIGHT)){
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.time.night")));
                        return;
                    }
                }
                vp.setTimeType(TimeType.NIGHT);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.time.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.time.night")).replaceAll("<votes>", String.valueOf(vote.getVotes("NIGHT"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("time"),
                        new String[]{"<dawntime>", String.valueOf(vote.getVotes("DAWN"))},
                        new String[]{"<daytime>", String.valueOf(vote.getVotes("DAY"))},
                        new String[]{"<afternoontime>", String.valueOf(vote.getVotes("AFTERNOON"))},
                        new String[]{"<nighttime>", String.valueOf(vote.getVotes("NIGHT"))});
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
                            new String[]{"{TIMEDAWN}", plugin.getLang().get(null, "menus.time.dawn.nameItem"), plugin.getLang().get(null, "menus.time.dawn.loreItem")},
                            new String[]{"{TIMEDAY}", plugin.getLang().get(null, "menus.time.day.nameItem"), plugin.getLang().get(null, "menus.time.day.loreItem")},
                            new String[]{"{TIMEAFTERNOON}", plugin.getLang().get(null, "menus.time.afternoon.nameItem"), plugin.getLang().get(null, "menus.time.afternoon.loreItem")},
                            new String[]{"{TIMENIGHT}", plugin.getLang().get(null, "menus.time.night.nameItem"), plugin.getLang().get(null, "menus.time.night.loreItem")});
                    contents.put(slot, item);
                    config.put(slot, litem);
                }
                this.contents = contents;
                this.config = config;
            }
        }
    }
    
}