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

package io.github.Leonardo0013YT.UltraSkyWarsSetup.setup;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import io.github.Leonardo0013YT.UltraSkyWarsSetup.UltraSkyWarsSetup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ArenaSetup {
    
    private final String name;
    private final ArrayList<Location> centers = new ArrayList<>();
    private final HashMap<Integer, IslandArenaSetup> islands = new HashMap<>();
    private final ArrayList<EventSetup> events = new ArrayList<>();
    private final UltraSkyWarsApi plugin;
    private ChatColor color = ChatColor.BLACK;
    private IslandArenaSetup actual = null;
    private EventSetup eactual = null;
    private Location lobby = null, spectator = null;
    private int min = 2, teamSize = 1, borderX = 0, borderZ = 0, last = 0;
    private boolean ranked = false, votes = true;
    
    public ArenaSetup(UltraSkyWarsApi plugin, Player p, String name, String clone){
        this.plugin = plugin;
        this.name = name;
        String path = "arenas." + name;
        this.lobby = Utils.getStringLocation(plugin.getArenas().get(path + ".lobby").replace(name, clone));
        this.spectator = Utils.getStringLocation(plugin.getArenas().get(path + ".spectator").replace(name, clone));
        this.votes = plugin.getArenas().getBooleanOrDefault(path + ".votes", true);
        this.color = ChatColor.BLACK;
        this.borderX = plugin.getArenas().getIntOrDefault(path + ".borderX", 0);
        this.borderZ = plugin.getArenas().getIntOrDefault(path + ".borderZ", 0);
        this.min = plugin.getArenas().getInt(path + ".min");
        this.teamSize = Math.max(plugin.getArenas().getInt(path + ".teamSize"), 1);
        this.ranked = plugin.getArenas().getBoolean(path + ".ranked");
        this.votes = plugin.getArenas().getBoolean(path + ".votes");
        List<String> eve = plugin.getArenas().getListOrDefault(path + ".events", new ArrayList<>());
        for ( String e : eve ){
            String[] ev = e.split(":");
            String type = ev[0];
            int s = Integer.parseInt(ev[1]);
            if (type.equals("REFILL")){
                EventSetup rev = new EventSetup("refill");
                rev.setSeconds(s);
                events.add(rev);
            }
            if (type.equals("FINAL")){
                EventSetup rev = new EventSetup("final");
                rev.setSeconds(s);
                events.add(rev);
            }
        }
        ConfigurationSection conf = plugin.getArenas().getConfig().getConfigurationSection(path + ".islands");
        int i = 0;
        for ( String c : conf.getKeys(false) ){
            String teamPath = path + ".islands." + c;
            int id;
            try {
                id = Integer.parseInt(c.replace("island", ""));
            } catch (NumberFormatException e) {
                id = i;
            }
            Location spawn = Utils.getStringLocation(plugin.getArenas().get(teamPath + ".spawn").replace(name, clone));
            Location balloon = Utils.getStringLocation(plugin.getArenas().get(teamPath + ".balloon").replace(name, clone));
            Location fence = Utils.getStringLocation(plugin.getArenas().get(teamPath + ".fence").replace(name, clone));
            IslandArenaSetup ias = new IslandArenaSetup(p, id);
            for ( String ch : plugin.getArenas().getList(teamPath + ".chests") ){
                ias.addChest(Utils.getStringLocation(ch.replace(name, clone)));
            }
            ias.setSpawn(spawn);
            ias.setBalloon(balloon);
            ias.setFence(fence);
            islands.put(id, ias);
            if (getLast() < id){
                setLast(id);
            }
            i++;
        }
        for ( String c : plugin.getArenas().getList(path + ".center") ){
            centers.add(Utils.getStringLocation(c.replace(name, clone)));
        }
    }
    
    public ArenaSetup(UltraSkyWarsApi plugin, String name){
        this.plugin = plugin;
        this.name = name;
    }
    
    public void save(Player p, String clone){
        String path = "arenas." + clone;
        if (lobby == null){
            p.sendMessage(plugin.getLang().get(p, "setup.arena.notSet.lobby"));
            return;
        }
        if (spectator == null){
            p.sendMessage(plugin.getLang().get(p, "setup.arena.notSet.spect"));
            return;
        }
        if (centers.isEmpty()){
            p.sendMessage(plugin.getLang().get(p, "setup.arena.notSet.noCenter"));
            return;
        }
        if (islands.size() < 2){
            p.sendMessage(plugin.getLang().get(p, "setup.arena.notSet.noIslands"));
            return;
        }
        plugin.getArenas().set(path + ".name", clone);
        plugin.getWc().backUpWorld(Bukkit.getWorld(name), clone);
        plugin.getArenas().set(path + ".min", min);
        plugin.getArenas().set(path + ".ranked", ranked);
        plugin.getArenas().set(path + ".teamSize", teamSize);
        plugin.getArenas().set(path + ".lobby", Utils.getLocationString(lobby));
        plugin.getArenas().set(path + ".spectator", Utils.getLocationString(spectator));
        String is = path + ".islands";
        for ( IslandArenaSetup ias : islands.values() ){
            String pat = is + ".island" + ias.getId();
            plugin.getArenas().set(pat + ".spawn", Utils.getLocationString(ias.getSpawn()));
            plugin.getArenas().set(pat + ".balloon", Utils.getLocationString(ias.getBalloon()));
            plugin.getArenas().set(pat + ".fence", Utils.getLocationString(ias.getFence()));
            ArrayList<String> chests = ias.getChests().stream().map(Utils::getLocationString).collect(Collectors.toCollection(ArrayList::new));
            plugin.getArenas().set(pat + ".chests", chests);
        }
        List<String> even = new ArrayList<>();
        for ( EventSetup es : events ){
            if (es == null) continue;
            even.add(es.getType().toUpperCase() + ":" + es.getSeconds());
        }
        ArrayList<String> chests = centers.stream().map(Utils::getLocationString).collect(Collectors.toCollection(ArrayList::new));
        plugin.getArenas().set(path + ".center", chests);
        plugin.getArenas().set(path + ".votes", votes);
        plugin.getArenas().set(path + ".color", "§" + color.getChar());
        plugin.getArenas().set(path + ".borderX", borderX);
        plugin.getArenas().set(path + ".borderZ", borderZ);
        plugin.getArenas().set(path + ".events", even);
        plugin.getArenas().save();
        UltraSkyWarsSetup.get().getSm().remove(p);
        p.getInventory().remove(plugin.getIm().getSetup());
        p.getInventory().remove(plugin.getIm().getCenter());
        p.sendMessage(plugin.getLang().get(p, "setup.arena.save"));
        p.closeInventory();
    }
    
    public HashMap<Integer, IslandArenaSetup> getIslands(){
        return islands;
    }
    
    public int getLast(){
        return last;
    }
    
    public void setLast(int last){
        this.last = last;
    }
    
    public IslandArenaSetup getActual(){
        return actual;
    }
    
    public void setActual(IslandArenaSetup actual){
        this.actual = actual;
    }
    
    public EventSetup getEactual(){
        return eactual;
    }
    
    public void setEactual(EventSetup eactual){
        this.eactual = eactual;
    }
    
    public void saveIsland(Player p){
        if (actual.getSpawn() == null){
            p.sendMessage(plugin.getLang().get(p, "setup.arena.notSet.noSpawn"));
            return;
        }
        if (actual.getBalloon() == null){
            p.sendMessage(plugin.getLang().get(p, "setup.arena.notSet.noBalloon"));
            return;
        }
        if (actual.getFence() == null){
            p.sendMessage(plugin.getLang().get(p, "setup.arena.notSet.noFence"));
            return;
        }
        if (actual.getChests().isEmpty()){
            p.sendMessage(plugin.getLang().get(p, "setup.arena.notSet.noChest"));
            return;
        }
        islands.put(actual.getId(), actual);
        actual = null;
        p.sendMessage(plugin.getLang().get(p, "setup.arena.saveIsland").replaceAll("<amount>", "" + islands.size()));
        p.getInventory().remove(plugin.getIm().getIsland());
    }
    
    public void saveEvent(Player p){
        events.add(eactual);
        eactual = null;
        p.sendMessage(plugin.getLang().get(p, "setup.event.save"));
    }
    
    public ArrayList<EventSetup> getEvents(){
        return events;
    }
    
    public String getName(){
        return name;
    }
    
    public boolean isRanked(){
        return ranked;
    }
    
    public void setRanked(boolean ranked){
        this.ranked = ranked;
    }
    
    public boolean isVotes(){
        return votes;
    }
    
    public void setVotes(boolean votes){
        this.votes = votes;
    }
    
    public ChatColor getColor(){
        return color;
    }
    
    public void setColor(ChatColor color){
        this.color = color;
    }
    
    public int getBorderX(){
        return borderX;
    }
    
    public void setBorderX(int borderX){
        this.borderX = borderX;
    }
    
    public int getBorderZ(){
        return borderZ;
    }
    
    public void setBorderZ(int borderZ){
        this.borderZ = borderZ;
    }
    
    public void removeCenter(Location loc){
        centers.remove(loc);
    }
    
    public void addCenter(Location loc){
        centers.add(loc);
    }
    
    public boolean isCenter(Location loc){
        return centers.contains(loc);
    }
    
    public ArrayList<Location> getCenters(){
        return centers;
    }
    
    public Location getLobby(){
        return lobby;
    }
    
    public void setLobby(Location lobby){
        this.lobby = lobby;
    }
    
    public Location getSpectator(){
        return spectator;
    }
    
    public void setSpectator(Location spectator){
        this.spectator = spectator;
    }
    
    public int getMin(){
        return min;
    }
    
    public void setMin(int min){
        this.min = min;
    }
    
    public int getTeamSize(){
        return teamSize;
    }
    
    public void setTeamSize(int teamSize){
        this.teamSize = teamSize;
    }
    
    public boolean isTeam(){
        return teamSize > 1;
    }
    
    public int getMax(){
        return islands.size() * teamSize;
    }
    
}