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
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.FinalType;
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

public class VoteFinalMenu extends UltraInventory {
    
    public VoteFinalMenu(UltraSkyWarsApi plugin, String name){
        super(name);
        this.title = plugin.getLang().get("menus." + name + ".title");
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
            if (display.equals(plugin.getLang().get(p, "menus.final.dragon.nameItem"))){
                if (!p.hasPermission("ultraskywars.votes.final.*") && !p.hasPermission("ultraskywars.votes.final.dragon")){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getFinalType() != null){
                    if (vp.getFinalType().equals(FinalType.DRAGON)){
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.final.dragon")));
                        return;
                    }
                }
                vp.setFinalType(FinalType.DRAGON);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.final.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.final.dragon")).replaceAll("<votes>", String.valueOf(vote.getVotes("DRAGON"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("final"),
                        new String[]{"<dragonfinal>", String.valueOf(vote.getVotes("DRAGON"))},
                        new String[]{"<borderfinal>", String.valueOf(vote.getVotes("BORDER"))},
                        new String[]{"<tntfinal>", String.valueOf(vote.getVotes("TNT"))},
                        new String[]{"<zombiesfinal>", String.valueOf(vote.getVotes("ZOMBIES"))},
                        new String[]{"<witherfinal>", String.valueOf(vote.getVotes("WITHER"))},
                        new String[]{"<nonefinal>", String.valueOf(vote.getVotes("NONE"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.final.tnt.nameItem"))){
                if (!p.hasPermission("ultraskywars.votes.final.*") && !p.hasPermission("ultraskywars.votes.final.tnt")){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getFinalType() != null){
                    if (vp.getFinalType().equals(FinalType.TNT)){
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.final.tnt")));
                        return;
                    }
                }
                vp.setFinalType(FinalType.TNT);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.final.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.final.tnt")).replaceAll("<votes>", String.valueOf(vote.getVotes("TNT"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("final"),
                        new String[]{"<dragonfinal>", String.valueOf(vote.getVotes("DRAGON"))},
                        new String[]{"<borderfinal>", String.valueOf(vote.getVotes("BORDER"))},
                        new String[]{"<tntfinal>", String.valueOf(vote.getVotes("TNT"))},
                        new String[]{"<zombiesfinal>", String.valueOf(vote.getVotes("ZOMBIES"))},
                        new String[]{"<witherfinal>", String.valueOf(vote.getVotes("WITHER"))},
                        new String[]{"<nonefinal>", String.valueOf(vote.getVotes("NONE"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.final.wither.nameItem"))){
                if (!p.hasPermission("ultraskywars.votes.final.*") && !p.hasPermission("ultraskywars.votes.final.wither")){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getFinalType() != null){
                    if (vp.getFinalType().equals(FinalType.WITHER)){
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.final.wither")));
                        return;
                    }
                }
                vp.setFinalType(FinalType.WITHER);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.final.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.final.wither")).replaceAll("<votes>", String.valueOf(vote.getVotes("WITHER"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("final"),
                        new String[]{"<dragonfinal>", String.valueOf(vote.getVotes("DRAGON"))},
                        new String[]{"<borderfinal>", String.valueOf(vote.getVotes("BORDER"))},
                        new String[]{"<tntfinal>", String.valueOf(vote.getVotes("TNT"))},
                        new String[]{"<zombiesfinal>", String.valueOf(vote.getVotes("ZOMBIES"))},
                        new String[]{"<witherfinal>", String.valueOf(vote.getVotes("WITHER"))},
                        new String[]{"<nonefinal>", String.valueOf(vote.getVotes("NONE"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.final.zombie.nameItem"))){
                if (!p.hasPermission("ultraskywars.votes.final.*") && !p.hasPermission("ultraskywars.votes.final.zombie")){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getFinalType() != null){
                    if (vp.getFinalType().equals(FinalType.ZOMBIES)){
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.final.zombies")));
                        return;
                    }
                }
                vp.setFinalType(FinalType.ZOMBIES);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.final.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.final.zombies")).replaceAll("<votes>", String.valueOf(vote.getVotes("ZOMBIES"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("final"),
                        new String[]{"<dragonfinal>", String.valueOf(vote.getVotes("DRAGON"))},
                        new String[]{"<borderfinal>", String.valueOf(vote.getVotes("BORDER"))},
                        new String[]{"<tntfinal>", String.valueOf(vote.getVotes("TNT"))},
                        new String[]{"<zombiesfinal>", String.valueOf(vote.getVotes("ZOMBIES"))},
                        new String[]{"<witherfinal>", String.valueOf(vote.getVotes("WITHER"))},
                        new String[]{"<nonefinal>", String.valueOf(vote.getVotes("NONE"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.final.none.nameItem"))){
                if (!p.hasPermission("ultraskywars.votes.final.*") && !p.hasPermission("ultraskywars.votes.final.none")){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getFinalType() != null){
                    if (vp.getFinalType().equals(FinalType.NONE)){
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.final.none")));
                        return;
                    }
                }
                vp.setFinalType(FinalType.NONE);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.final.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.final.none")).replaceAll("<votes>", String.valueOf(vote.getVotes("NONE"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("final"),
                        new String[]{"<dragonfinal>", String.valueOf(vote.getVotes("DRAGON"))},
                        new String[]{"<borderfinal>", String.valueOf(vote.getVotes("BORDER"))},
                        new String[]{"<tntfinal>", String.valueOf(vote.getVotes("TNT"))},
                        new String[]{"<zombiesfinal>", String.valueOf(vote.getVotes("ZOMBIES"))},
                        new String[]{"<witherfinal>", String.valueOf(vote.getVotes("WITHER"))},
                        new String[]{"<nonefinal>", String.valueOf(vote.getVotes("NONE"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.final.border.nameItem"))){
                if (!p.hasPermission("ultraskywars.votes.final.*") && !p.hasPermission("ultraskywars.votes.final.border")){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getFinalType() != null){
                    if (vp.getFinalType().equals(FinalType.BORDER)){
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.final.border")));
                        return;
                    }
                }
                vp.setFinalType(FinalType.BORDER);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.final.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.final.border")).replaceAll("<votes>", String.valueOf(vote.getVotes("BORDER"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("final"),
                        new String[]{"<dragonfinal>", String.valueOf(vote.getVotes("DRAGON"))},
                        new String[]{"<borderfinal>", String.valueOf(vote.getVotes("BORDER"))},
                        new String[]{"<tntfinal>", String.valueOf(vote.getVotes("TNT"))},
                        new String[]{"<zombiesfinal>", String.valueOf(vote.getVotes("ZOMBIES"))},
                        new String[]{"<witherfinal>", String.valueOf(vote.getVotes("WITHER"))},
                        new String[]{"<nonefinal>", String.valueOf(vote.getVotes("NONE"))});
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
                            new String[]{"{FINALDRAGON}", plugin.getLang().get("menus.final.dragon.nameItem"), plugin.getLang().get("menus.final.dragon.loreItem")},
                            new String[]{"{FINALTNT}", plugin.getLang().get("menus.final.tnt.nameItem"), plugin.getLang().get("menus.final.tnt.loreItem")},
                            new String[]{"{FINALZOMBIE}", plugin.getLang().get("menus.final.zombie.nameItem"), plugin.getLang().get("menus.final.zombie.loreItem")},
                            new String[]{"{FINALWITHER}", plugin.getLang().get("menus.final.wither.nameItem"), plugin.getLang().get("menus.final.wither.loreItem")},
                            new String[]{"{FINALBORDER}", plugin.getLang().get("menus.final.border.nameItem"), plugin.getLang().get("menus.final.border.loreItem")},
                            new String[]{"{FINALNONE}", plugin.getLang().get("menus.final.none.nameItem"), plugin.getLang().get("menus.final.none.loreItem")});
                    contents.put(slot, item);
                    config.put(slot, litem);
                }
                this.contents = contents;
                this.config = config;
            }
        }
    }
    
}