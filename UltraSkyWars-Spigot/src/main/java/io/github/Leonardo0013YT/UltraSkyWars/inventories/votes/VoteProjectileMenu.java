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
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.ProjectileType;
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

public class VoteProjectileMenu extends UltraInventory {
    
    public VoteProjectileMenu(UltraSkyWarsApi plugin, String name){
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
            if (display.equals(plugin.getLang().get(p, "menus.projectile.noProj.nameItem"))){
                if (!p.hasPermission("ultraskywars.votes.projectile.*") && !p.hasPermission("ultraskywars.votes.projectile.noproj")){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getProjectileType() != null){
                    if (vp.getProjectileType().equals(ProjectileType.NOPROJ)){
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.projectile.noproj")));
                        return;
                    }
                }
                vp.setProjectileType(ProjectileType.NOPROJ);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.projectile.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.projectile.noproj")).replaceAll("<votes>", String.valueOf(vote.getVotes("NOPROJ"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("projectile"),
                        new String[]{"<noproj>", String.valueOf(vote.getVotes("NOPROJ"))},
                        new String[]{"<yesproj>", String.valueOf(vote.getVotes("YESPROJ"))},
                        new String[]{"<exproj>", String.valueOf(vote.getVotes("EXPROJ"))},
                        new String[]{"<desproj>", String.valueOf(vote.getVotes("DESPROJ"))},
                        new String[]{"<teleproj>", String.valueOf(vote.getVotes("TELEPROJ"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.projectile.siProj.nameItem"))){
                if (!p.hasPermission("ultraskywars.votes.projectile.*") && !p.hasPermission("ultraskywars.votes.projectile.siproj")){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getProjectileType() != null){
                    if (vp.getProjectileType().equals(ProjectileType.YESPROJ)){
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.projectile.yesproj")));
                        return;
                    }
                }
                vp.setProjectileType(ProjectileType.YESPROJ);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.projectile.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.projectile.yesproj")).replaceAll("<votes>", String.valueOf(vote.getVotes("YESPROJ"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("projectile"),
                        new String[]{"<noproj>", String.valueOf(vote.getVotes("NOPROJ"))},
                        new String[]{"<yesproj>", String.valueOf(vote.getVotes("YESPROJ"))},
                        new String[]{"<exproj>", String.valueOf(vote.getVotes("EXPROJ"))},
                        new String[]{"<desproj>", String.valueOf(vote.getVotes("DESPROJ"))},
                        new String[]{"<teleproj>", String.valueOf(vote.getVotes("TELEPROJ"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.projectile.exProj.nameItem"))){
                if (!p.hasPermission("ultraskywars.votes.projectile.*") && !p.hasPermission("ultraskywars.votes.projectile.exproj")){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getProjectileType() != null){
                    if (vp.getProjectileType().equals(ProjectileType.EXPROJ)){
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.projectile.exproj")));
                        return;
                    }
                }
                vp.setProjectileType(ProjectileType.EXPROJ);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.projectile.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.projectile.exproj")).replaceAll("<votes>", String.valueOf(vote.getVotes("EXPROJ"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("projectile"),
                        new String[]{"<noproj>", String.valueOf(vote.getVotes("NOPROJ"))},
                        new String[]{"<yesproj>", String.valueOf(vote.getVotes("YESPROJ"))},
                        new String[]{"<exproj>", String.valueOf(vote.getVotes("EXPROJ"))},
                        new String[]{"<desproj>", String.valueOf(vote.getVotes("DESPROJ"))},
                        new String[]{"<teleproj>", String.valueOf(vote.getVotes("TELEPROJ"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.projectile.desProj.nameItem"))){
                if (!p.hasPermission("ultraskywars.votes.projectile.*") && !p.hasPermission("ultraskywars.votes.projectile.desproj")){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getProjectileType() != null){
                    if (vp.getProjectileType().equals(ProjectileType.DESPROJ)){
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.projectile.desproj")));
                        return;
                    }
                }
                vp.setProjectileType(ProjectileType.DESPROJ);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.projectile.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.projectile.desproj")).replaceAll("<votes>", String.valueOf(vote.getVotes("DESPROJ"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("projectile"),
                        new String[]{"<noproj>", String.valueOf(vote.getVotes("NOPROJ"))},
                        new String[]{"<yesproj>", String.valueOf(vote.getVotes("YESPROJ"))},
                        new String[]{"<exproj>", String.valueOf(vote.getVotes("EXPROJ"))},
                        new String[]{"<desproj>", String.valueOf(vote.getVotes("DESPROJ"))},
                        new String[]{"<teleproj>", String.valueOf(vote.getVotes("TELEPROJ"))});
            }
            if (display.equals(plugin.getLang().get(p, "menus.projectile.teleProj.nameItem"))){
                if (!p.hasPermission("ultraskywars.votes.projectile.*") && !p.hasPermission("ultraskywars.votes.projectile.teleproj")){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                Vote.VotePlayer vp = vote.getVotePlayer(p);
                if (vp.getProjectileType() != null){
                    if (vp.getProjectileType().equals(ProjectileType.TELEPROJ)){
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyVoted").replaceAll("<type>", plugin.getLang().get(p, "votes.projectile.teleproj")));
                        return;
                    }
                }
                vp.setProjectileType(ProjectileType.TELEPROJ);
                g.sendGameMessage(plugin.getLang().get(p, "messages.voted").replaceAll("<player>", p.getName()).replaceAll("<category>", plugin.getLang().get(p, "votes.projectile.name")).replaceAll("<type>", plugin.getLang().get(p, "votes.projectile.teleproj")).replaceAll("<votes>", String.valueOf(vote.getVotes("TELEPROJ"))));
                plugin.getUim().openInventory(p, plugin.getUim().getMenus("projectile"),
                        new String[]{"<noproj>", String.valueOf(vote.getVotes("NOPROJ"))},
                        new String[]{"<yesproj>", String.valueOf(vote.getVotes("YESPROJ"))},
                        new String[]{"<exproj>", String.valueOf(vote.getVotes("EXPROJ"))},
                        new String[]{"<desproj>", String.valueOf(vote.getVotes("DESPROJ"))},
                        new String[]{"<teleproj>", String.valueOf(vote.getVotes("TELEPROJ"))});
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
                            new String[]{"{NOPROJ}", plugin.getLang().get(null, "menus.projectile.noProj.nameItem"), plugin.getLang().get(null, "menus.projectile.noProj.loreItem")},
                            new String[]{"{YESPROJ}", plugin.getLang().get(null, "menus.projectile.siProj.nameItem"), plugin.getLang().get(null, "menus.projectile.siProj.loreItem")},
                            new String[]{"{EXPROJ}", plugin.getLang().get(null, "menus.projectile.exProj.nameItem"), plugin.getLang().get(null, "menus.projectile.exProj.loreItem")},
                            new String[]{"{DESPROJ}", plugin.getLang().get(null, "menus.projectile.desProj.nameItem"), plugin.getLang().get(null, "menus.projectile.desProj.loreItem")},
                            new String[]{"{TELEPROJ}", plugin.getLang().get(null, "menus.projectile.teleProj.nameItem"), plugin.getLang().get(null, "menus.projectile.teleProj.loreItem")});
                    contents.put(slot, item);
                    config.put(slot, litem);
                }
                this.contents = contents;
                this.config = config;
            }
        }
    }
    
}