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

package io.github.Leonardo0013YT.UltraSkyWars.addons;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.IPartiesAddon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PartiesAddon implements IPartiesAddon {
    
    private final PartiesAPI api;
    
    public PartiesAddon(){
        api = Parties.getApi();
    }
    
    public boolean isInParty(Player p){
        return !api.getPartyPlayer(p.getUniqueId()).getPartyName().isEmpty();
    }
    
    public boolean isPartyLeader(Player p){
        PartyPlayer pp = api.getPartyPlayer(p.getUniqueId());
        if (pp == null){
            return false;
        }
        Party party = api.getParty(pp.getPartyName());
        if (party == null || party.getLeader() == null){
            return false;
        }
        return party.getLeader().equals(p.getUniqueId());
    }
    
    public List<Player> getPlayersParty(Player leader){
        PartyPlayer pp = api.getPartyPlayer(leader.getUniqueId());
        Party party = api.getParty(pp.getPartyName());
        List<Player> online = new ArrayList<>();
        party.getOnlineMembers(true).forEach(o -> online.add(Bukkit.getPlayer(o.getPlayerUUID())));
        return online;
    }
    
}