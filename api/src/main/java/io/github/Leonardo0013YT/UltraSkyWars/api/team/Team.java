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

package io.github.Leonardo0013YT.UltraSkyWars.api.team;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.specials.PlayerNametagReceivedEvent;
import io.github.Leonardo0013YT.UltraSkyWars.api.game.GameChest;
import io.github.Leonardo0013YT.UltraSkyWars.api.nametags.Nametags;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
@Setter
public class Team {
    
    private Collection<Player> members = new ArrayList<>();
    private Map<Player, Location> center = new HashMap<>();
    private UltraSkyWarsApi plugin;
    private Game game;
    private int id;
    private int kills;
    private Location spawn, balloon, fence;
    private GameChest chest;
    private Nametags friendly, fire;
    private boolean created = false;
    
    public Team(UltraSkyWarsApi plugin, Game game, String path, int id){
        this.plugin = plugin;
        this.game = game;
        this.id = id;
        this.kills = 0;
        this.spawn = Utils.getStringLocation(plugin.getArenas().get(path + ".spawn"));
        this.balloon = Utils.getStringLocation(plugin.getArenas().get(path + ".balloon"));
        this.fence = Utils.getStringLocation(plugin.getArenas().get(path + ".fence"));
        List<Location> chests = new ArrayList<>();
        for ( String c : plugin.getArenas().getList(path + ".chests") ){
            chests.add(Utils.getStringLocation(c));
        }
        chest = new GameChest(false, chests);
        friendly = plugin.getVc().getNameTag("A" + getName() + "FR", "AFriendly", "§a");
        fire = plugin.getVc().getNameTag("B" + getName() + "FI", "ZFire", "§c");
    }
    
    public void setCenter(Player p, Location l){
        center.put(p, l);
    }
    
    public Location getCenter(Player p){
        return center.get(p);
    }
    
    public void addKill(){
        kills++;
    }
    
    public void updateWorld(World w){
        spawn.setWorld(w);
        balloon.setWorld(w);
        if (fence != null){
            fence.setWorld(w);
        }
        for ( Location l : chest.getChests() ){
            l.setWorld(w);
        }
        for ( Location l : chest.getInvs().keySet() ){
            l.setWorld(w);
        }
    }
    
    public void reset(){
        created = false;
        center.clear();
        members.clear();
        List<Location> chests = new ArrayList<>(chest.getInvs().keySet());
        chest = new GameChest(false, chests);
        friendly.deleteTeam("A" + getName() + "FR");
        fire.deleteTeam("B" + getName() + "FI");
        friendly = plugin.getVc().getNameTag("A" + getName() + "FR", "friendly", "§a");
        fire = plugin.getVc().getNameTag("B" + getName() + "FI", "fire", "§c");
    }
    
    public void execute(){
        for ( Player on : game.getPlayers() ){
            if (members.contains(on)){
                friendly.addPlayer(on);
            } else {
                fire.addPlayer(on);
            }
        }
        for ( Player on : members ){
            PlayerNametagReceivedEvent e = new PlayerNametagReceivedEvent(on, friendly, fire);
            Bukkit.getPluginManager().callEvent(e);
            if (e.isCancelled()) continue;
            friendly.sendToPlayer(on);
            fire.sendToPlayer(on);
        }
    }
    
    public void addMember(Player p){
        if (!members.contains(p)){
            members.add(p);
        }
    }
    
    public void removeMember(Player p){
        members.remove(p);
        center.remove(p);
        friendly.deleteTeam(p, "A" + getName() + "FR");
        fire.deleteTeam(p, "B" + getName() + "FI");
    }
    
    public int getTeamSize(){
        return members.size();
    }
    
    private String getName(){
        String name = game.getName();
        if (name.length() > 12){
            return name.substring(0, 12);
        }
        return name;
    }
    
}