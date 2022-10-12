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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.parties.managers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.parties.InjectionParty;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.parties.party.Party;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.parties.party.PartyInvite;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PartyManager {
    
    private final Map<UUID, Party> parties = new HashMap<>();
    private final Map<UUID, UUID> partyPlayer = new HashMap<>();
    private final Cache<UUID, PartyInvite> invites = CacheBuilder.newBuilder().expireAfterWrite(60L, TimeUnit.SECONDS).build();
    private final UltraSkyWarsApi plugin;
    private final InjectionParty ip;
    private int defaultMax;
    
    public PartyManager(UltraSkyWarsApi plugin, InjectionParty ip){
        this.plugin = plugin;
        this.ip = ip;
        reload();
    }
    
    public void reload(){
        this.defaultMax = ip.getParties().getIntOrDefault("config.defaultMax", 3);
    }
    
    public int getMaxParty(Player p){
        for ( PermissionAttachmentInfo attachmentInfo : p.getEffectivePermissions() ){
            String perm = attachmentInfo.getPermission();
            if (perm.startsWith("ultraskywars.party.max.")){
                try {
                    return Integer.parseInt(perm.replaceFirst("ultraskywars.party.max.", ""));
                } catch (NumberFormatException e) {
                    return defaultMax;
                }
            }
        }
        return defaultMax;
    }
    
    
    public void createPartyByRedis(UUID leader, Party party){
        parties.put(leader, party);
        partyPlayer.put(leader, leader);
        for ( UUID member : party.getMembers().keySet() ){
            partyPlayer.put(member, leader);
        }
    }
    
    public Party createPartyByCommand(UUID leader, boolean privacy, int maxCapacity, String leaderName){
        Party party = new Party(leader, leaderName, privacy, maxCapacity, new HashMap<>());
        parties.put(leader, party);
        partyPlayer.put(leader, leader);
        return party;
    }
    
    public void sendPartyChatMessage(Party party, UUID uuid, String msg){
        UltraSkyWarsApi.sendRedisMessage("usw:parties", "CHAT;;;" + party.getLeader().toString() + ";;;" + uuid.toString() + ";;;" + msg);
        if (!UltraSkyWarsApi.get().isBungeeMode()){
            sendPartyChatMessage(party.getLeader(), uuid, msg);
        }
    }
    
    public void sendPartyChat(Party party, UUID added){
        UltraSkyWarsApi.sendRedisMessage("usw:parties", "CHAT;;;" + party.getLeader().toString() + ";;;" + added.toString());
        if (!UltraSkyWarsApi.get().isBungeeMode()){
            addPartyChat(party.getLeader(), added);
        }
    }
    
    public void sendPartyData(Party party){
        UltraSkyWarsApi.sendRedisMessage("usw:parties", "DATA;;;" + ip.getGson().toJson(party, Party.class));
    }
    
    public void sendPartyKick(UUID leader, UUID kicked){
        UltraSkyWarsApi.sendRedisMessage("usw:parties", "KICKED;;;" + leader.toString() + ";;;" + kicked.toString());
        if (!UltraSkyWarsApi.get().isBungeeMode()){
            kickPayerParty(leader, kicked);
        }
    }
    
    public void sendPartyLeave(UUID leader, UUID kicked){
        UltraSkyWarsApi.sendRedisMessage("usw:parties", "LEAVE;;;" + leader.toString() + ";;;" + kicked.toString());
        if (!UltraSkyWarsApi.get().isBungeeMode()){
            leaveParty(leader, kicked);
        }
    }
    
    public void sendDelete(UUID leader){
        UltraSkyWarsApi.sendRedisMessage("usw:parties", "DELETE;;;" + leader.toString());
        if (!UltraSkyWarsApi.get().isBungeeMode()){
            deleteParty(leader);
        }
    }
    
    public void sendPartyServer(UUID leader, String server){
        UltraSkyWarsApi.sendRedisMessage("usw:parties", "SEND;;;" + leader.toString() + ";;;" + server);
    }
    
    public void sendNewLeader(Party party, UUID newLeader, String newName){
        UltraSkyWarsApi.sendRedisMessage("usw:parties", "NEWLEADER;;;" + party.getLeader().toString() + ";;;" + newLeader.toString() + ";;;" + newName);
        if (!UltraSkyWarsApi.get().isBungeeMode()){
            setNewLeader(party.getLeader(), newLeader, newName);
        }
    }
    
    public void sendPrivacy(UUID leader, boolean privacy){
        UltraSkyWarsApi.sendRedisMessage("usw:parties", "SETPRIVACY;;;" + leader.toString() + ";;;" + privacy);
        if (!UltraSkyWarsApi.get().isBungeeMode()){
            setPrivacy(leader, privacy);
        }
    }
    
    public void sendPartyChatMessage(UUID leader, UUID uuid, String message){
        Party party = parties.get(leader);
        if (party == null) return;
        String name = party.getMembers().get(uuid);
        if (name == null) return;
        for ( UUID u : party.getChatMembers() ){
            Player on = Bukkit.getPlayer(u);
            if (on == null) continue;
            on.sendMessage(ip.getParties().get("partyChat").replace("<player>", name).replace("<msg>", message));
        }
    }
    
    public void addPartyChat(UUID leader, UUID joiner){
        Party party = parties.get(leader);
        if (party == null) return;
        if (party.getChatMembers().contains(joiner)){
            party.getChatMembers().remove(joiner);
        } else {
            party.getChatMembers().add(joiner);
        }
    }
    
    public void setPrivacy(UUID leader, boolean privacy){
        Party party = parties.get(leader);
        party.setPrivacy(privacy);
        for ( UUID uuid : party.getMembers().keySet() ){
            Player m = Bukkit.getPlayer(uuid);
            if (m != null){
                m.sendMessage(ip.getParties().get(m, "privacy").replace("<privacy>", (party.isPrivacy()) ? ip.getParties().get("privateParty") : ip.getParties().get("publicParty")));
            }
        }
    }
    
    public void setNewLeader(UUID leader, UUID newLeader, String newName){
        Party party = parties.get(leader);
        UUID old = party.getLeader();
        partyPlayer.entrySet().stream().filter(e -> e.getValue().equals(old)).forEach(e -> e.setValue(newLeader));
        parties.put(newLeader, party);
        parties.remove(old);
        party.setLeader(newLeader);
        party.setLeaderName(newName);
        for ( UUID uuid : party.getMembers().keySet() ){
            Player m = Bukkit.getPlayer(uuid);
            if (m != null){
                m.sendMessage(ip.getParties().get(m, "newLeader").replace("<player>", newName));
            }
        }
    }
    
    public void leaveParty(UUID leader, UUID leaver){
        Party party = getPartyByPlayer(leader);
        if (party == null) return;
        if (party.getMembers().containsKey(leaver)){
            for ( UUID uuid : party.getMembers().keySet() ){
                Player m = Bukkit.getPlayer(uuid);
                if (m != null){
                    m.sendMessage(ip.getParties().get(m, "leave").replace("<player>", party.getMembers().get(leaver)));
                }
            }
            party.getMembers().remove(leaver);
            partyPlayer.remove(leaver);
        }
    }
    
    public void kickPayerParty(UUID leader, UUID kicked){
        Party party = getPartyByPlayer(leader);
        if (party == null) return;
        if (party.getMembers().containsKey(kicked)){
            for ( UUID uuid : party.getMembers().keySet() ){
                Player m = Bukkit.getPlayer(uuid);
                if (m != null){
                    m.sendMessage(ip.getParties().get(m, "kick").replace("<player>", party.getMembers().get(kicked)));
                }
            }
            party.getMembers().remove(kicked);
            partyPlayer.remove(kicked);
        }
    }
    
    public void deleteParty(UUID leader){
        Party party = getPartyByPlayer(leader);
        if (party == null) return;
        for ( UUID j : party.getMembers().keySet() ){
            Player m = Bukkit.getPlayer(j);
            if (m == null) continue;
            m.sendMessage(ip.getParties().get("disbaned"));
        }
        parties.remove(leader);
        partyPlayer.remove(leader);
        partyPlayer.values().removeIf(u -> u.equals(leader));
    }
    
    public void sendPartyToServer(UUID leader, String server){
        Party party = getPartyByPlayer(leader);
        if (party == null) return;
        for ( UUID j : party.getMembers().keySet() ){
            Player m = Bukkit.getPlayer(j);
            if (m == null) continue;
            plugin.sendToServer(m, server);
        }
    }
    
    public void joinParty(Player p, Party party){
        party.getMembers().put(p.getUniqueId(), p.getName());
        partyPlayer.put(p.getUniqueId(), party.getLeader());
        for ( UUID j : party.getMembers().keySet() ){
            Player m = Bukkit.getPlayer(j);
            if (m == null) continue;
            m.sendMessage(ip.getParties().get(m, "joined").replace("<player>", p.getName()));
        }
    }
    
    public void denyParty(Player p, Party party){
        for ( UUID j : party.getMembers().keySet() ){
            Player m = Bukkit.getPlayer(j);
            if (m == null) continue;
            m.sendMessage(ip.getParties().get(m, "denied").replace("<player>", p.getName()));
        }
        invites.invalidate(p.getUniqueId());
        p.sendMessage(ip.getParties().get(p, "denyParty"));
    }
    
    public void createPartyInvite(UUID inviter, UUID invited){
        PartyInvite pi = new PartyInvite(inviter, invited);
        invites.put(invited, pi);
    }
    
    public boolean isInvited(Player p){
        return invites.asMap().containsKey(p.getUniqueId());
    }
    
    public boolean isLeader(Player p){
        return parties.containsKey(p.getUniqueId());
    }
    
    public boolean isInParty(Player p){
        return isInParty(p.getUniqueId());
    }
    
    public boolean isInParty(UUID p){
        return partyPlayer.containsKey(p);
    }
    
    public PartyInvite getPartyInvite(UUID invited){
        return invites.getIfPresent(invited);
    }
    
    public Party getPartyByPlayer(Player p){
        return getPartyByPlayer(p.getUniqueId());
    }
    
    public Party getPartyByPlayer(UUID uuid){
        return parties.get(partyPlayer.get(uuid));
    }
    
    public Map<UUID, Party> getParties(){
        return parties;
    }
}