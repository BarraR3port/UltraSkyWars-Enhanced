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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.parties.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.specials.RedisPartyMessageEvent;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.parties.InjectionParty;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.parties.party.Party;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.NBTEditor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PartyListener implements Listener {
    
    private final InjectionParty ip;
    
    public PartyListener(InjectionParty ip){
        this.ip = ip;
    }
    
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        if (ip.getPam().isInParty(p)){
            Party party = ip.getPam().getPartyByPlayer(p);
            if (party.getChatMembers().contains(p.getUniqueId())){
                e.setCancelled(true);
                ip.getPam().sendPartyChatMessage(party.getLeader(), p.getUniqueId(), e.getMessage());
            }
        }
    }
    
    @EventHandler
    public void onMenu(InventoryClickEvent e){
        if (e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)){
            return;
        }
        if (e.getView().getTitle().equals(ip.getParties().get("menus.main.title"))){
            Player p = (Player) e.getWhoClicked();
            if (UltraSkyWarsApi.get().isSetupLobby(p)){
                return;
            }
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                return;
            }
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem();
            String d = item.getItemMeta().getDisplayName();
            if (d.equals(ip.getParties().get("menus.main.close.nameItem"))){
                p.closeInventory();
                return;
            }
            if (d.equals(ip.getParties().get("menus.main.create.nameItem"))){
                Party party = ip.getPam().createPartyByCommand(p.getUniqueId(), true, ip.getPam().getMaxParty(p), p.getName());
                ip.getPem().createMembersMenu(p, party);
                return;
            }
            if (d.equals(ip.getParties().get("menus.main.join.nameItem"))){
                ip.getPem().createPartiesMenu(p);
            }
        }
        if (e.getView().getTitle().equals(ip.getParties().get("menus.members.title"))){
            Player p = (Player) e.getWhoClicked();
            if (UltraSkyWarsApi.get().isSetupLobby(p)){
                return;
            }
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                return;
            }
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem();
            String d = item.getItemMeta().getDisplayName();
            if (d.equals(ip.getParties().get("menus.members.close.nameItem"))){
                p.closeInventory();
                return;
            }
            if (d.equals(ip.getParties().get("menus.members.delete.nameItem"))){
                if (!ip.getPam().isLeader(p)){
                    p.sendMessage(ip.getParties().get(p, "noIsLeader"));
                    return;
                }
                ip.getPam().deleteParty(p.getUniqueId());
                p.closeInventory();
                return;
            }
            if (NBTEditor.contains(item, "PARTY_MEMBER")){
                if (!ip.getPam().isLeader(p)){
                    p.sendMessage(ip.getParties().get(p, "noIsLeader"));
                    return;
                }
                Party party = ip.getPam().getPartyByPlayer(p);
                String[] data = NBTEditor.getString(item, "PARTY_MEMBER").split(":");
                UUID memberUUID = UUID.fromString(data[0]);
                if (party.getLeader().equals(memberUUID)){
                    p.sendMessage(ip.getParties().get("noKickLeader"));
                    return;
                }
                ip.getPam().kickPayerParty(p.getUniqueId(), memberUUID);
            }
        }
        if (e.getView().getTitle().equals(ip.getParties().get("menus.parties.title"))){
            Player p = (Player) e.getWhoClicked();
            if (UltraSkyWarsApi.get().isSetupLobby(p)){
                return;
            }
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                return;
            }
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem();
            String d = item.getItemMeta().getDisplayName();
            if (d.equals(ip.getParties().get("menus.parties.close.nameItem"))){
                p.closeInventory();
                return;
            }
            if (NBTEditor.contains(item, "PARTY_LEADER")){
                String[] data = NBTEditor.getString(item, "PARTY_LEADER").split(":");
                boolean privacy = Boolean.parseBoolean(data[1]);
                if (privacy){
                    p.sendMessage(ip.getParties().get("partyPrivate"));
                    return;
                }
                UUID partyUUID = UUID.fromString(data[0]);
                ip.getPam().joinParty(p, ip.getPam().getParties().get(partyUUID));
                p.closeInventory();
            }
        }
    }
    
    @EventHandler
    public void onMessage(RedisPartyMessageEvent e){
        String[] m = e.getMessage().split(";;;");
        String action = m[0];
        if (action.equals("SETPRIVACY")){
            UUID leader = UUID.fromString(m[1]);
            boolean privacy = Boolean.parseBoolean(m[2]);
            ip.getPam().setPrivacy(leader, privacy);
        }
        if (action.equals("NEWLEADER")){
            UUID leader = UUID.fromString(m[1]);
            UUID newLeader = UUID.fromString(m[2]);
            String newName = m[3];
            ip.getPam().setNewLeader(leader, newLeader, newName);
        }
        if (action.equals("DATA")){
            Party party = ip.getGson().fromJson(m[1], Party.class);
            ip.getPam().createPartyByRedis(party.getLeader(), party);
        }
        if (action.equals("KICKED")){
            UUID leader = UUID.fromString(m[1]);
            UUID kicked = UUID.fromString(m[2]);
            ip.getPam().kickPayerParty(leader, kicked);
        }
        if (action.equals("CHAT")){
            UUID leader = UUID.fromString(m[1]);
            UUID kicked = UUID.fromString(m[2]);
            ip.getPam().addPartyChat(leader, kicked);
        }
        if (action.equals("LEAVE")){
            UUID leader = UUID.fromString(m[1]);
            UUID kicked = UUID.fromString(m[2]);
            ip.getPam().leaveParty(leader, kicked);
        }
        if (action.equals("DELETE")){
            UUID leader = UUID.fromString(m[1]);
            ip.getPam().deleteParty(leader);
        }
        if (action.equals("SEND")){
            UUID leader = UUID.fromString(m[1]);
            String server = m[2];
            ip.getPam().sendPartyToServer(leader, server);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if (ip.getPam().isLeader(p)){
            ip.getPam().sendPartyData(ip.getPam().getPartyByPlayer(p));
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onKick(PlayerKickEvent e){
        Player p = e.getPlayer();
        if (ip.getPam().isLeader(p)){
            ip.getPam().sendPartyData(ip.getPam().getPartyByPlayer(p));
        }
    }
    
    
}