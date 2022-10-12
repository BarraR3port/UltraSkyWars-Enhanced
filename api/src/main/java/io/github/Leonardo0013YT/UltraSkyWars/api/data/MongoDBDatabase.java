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

package io.github.Leonardo0013YT.UltraSkyWars.api.data;

import com.mongodb.*;
import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.calls.CallBackAPI;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.StatType;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.TopType;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.data.USWPlayerLoadEvent;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.Database;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.Date;
import java.util.*;

public class MongoDBDatabase implements Database {
    
    private static final HashMap<UUID, SWPlayer> SWPlayers = new HashMap<>();
    private final MongoClient mc;
    private final DB admin;
    private final DBCollection players;
    private final DBCollection multipliers;
    private final UltraSkyWarsApi plugin;
    
    public MongoDBDatabase(UltraSkyWarsApi plugin){
        this.plugin = plugin;
        MongoCredential c = MongoCredential.createCredential(plugin.getConfig().getString("mongodb.username"), plugin.getConfig().getString("mongodb.database"), plugin.getConfig().getString("mongodb.password").toCharArray());
        mc = new MongoClient(new ServerAddress(plugin.getConfig().getString("mongodb.host"), plugin.getConfig().getInt("mongodb.port")), Collections.singletonList(c));
        admin = mc.getDB(plugin.getConfig().getString("mongodb.database"));
        plugin.sendLogMessage("§aBase de datos MongoDB iniciada correctamente.");
        if (!admin.collectionExists("usw_players")){
            admin.createCollection("usw_players", new BasicDBObject("capped", false));
            plugin.sendLogMessage("§aLa coleccion de datos §eJugadores §aa sido creada.");
        }
        players = admin.getCollection("players");
        if (!admin.collectionExists("usw_multipliers")){
            admin.createCollection("usw_multipliers", new BasicDBObject("capped", false));
            plugin.sendLogMessage("§aLa coleccion de datos §eMultipliers §aa sido creada.");
        }
        multipliers = admin.getCollection("usw_multipliers");
        plugin.sendLogMessage("§aColeccion de §eGlobal §acargada correctamente.");
    }
    
    @Override
    public HashMap<UUID, SWPlayer> getPlayers(){
        return SWPlayers;
    }
    
    private boolean hasPlayer(UUID p){
        DBObject dbo = new BasicDBObject("uuid", p.toString());
        DBObject r = players.findOne(dbo);
        return r != null;
    }
    
    @Override
    public int getRanking(UUID uuid){
        return 0;
    }
    
    @Override
    public void loadMultipliers(CallBackAPI<Boolean> request){
        plugin.getMm().clear();
        for ( DBObject m : multipliers.find() ){
            String type = (String) m.get("Type");
            String name = (String) m.get("Name");
            double amount = (double) m.get("Amount");
            Date date = new Date((long) m.get("Ending"));
            plugin.getMm().addMultiplier((int) m.get("ID"), type, name, amount, date.getTime());
        }
        request.done(true);
    }
    
    @Override
    public void createMultiplier(String type, String name, double amount, long ending, CallBackAPI<Boolean> request){
        DBObject now = new BasicDBObject("ID", multipliers.find().size());
        now.put("Type", type);
        now.put("Name", name);
        now.put("Amount", amount);
        now.put("Ending", ending);
        multipliers.insert(now);
        request.done(true);
    }
    
    @Override
    public boolean removeMultiplier(int id){
        DBObject find = new BasicDBObject("ID", id);
        DBObject now = multipliers.findOne(find);
        if (now != null){
            DBObject save = new BasicDBObject("ID", id);
            save.put("Type", now.get("Type"));
            save.put("Name", now.get("Name"));
            save.put("Amount", now.get("Amount"));
            save.put("Ending", (long) 0);
            multipliers.update(now, save);
        }
        return false;
    }
    
    @Override
    public void loadTopElo(){
        int pos = 1;
        List<String> tops = new ArrayList<>();
        for ( DBObject m : players.find().sort(new BasicDBObject("elo", -1)).limit(10) ){
            tops.add(UUID.fromString((String) m.get("uuid")) + ":" + m.get("name") + ":" + pos + ":" + m.get("elo"));
            pos++;
        }
        plugin.getTop().addTop(TopType.ELO, tops);
    }
    
    @Override
    public void loadTopCoins(){
        int pos = 1;
        List<String> tops = new ArrayList<>();
        for ( DBObject m : players.find().sort(new BasicDBObject("coins", -1)).limit(10) ){
            tops.add(UUID.fromString((String) m.get("uuid")) + ":" + m.get("name") + ":" + pos + ":" + m.get("coins"));
            pos++;
        }
        plugin.getTop().addTop(TopType.COINS, tops);
    }
    
    @Override
    public void loadTopKills(){
        int pos = 1;
        List<String> tops = new ArrayList<>();
        for ( DBObject m : players.find().sort(new BasicDBObject("kills", -1)).limit(10) ){
            tops.add(UUID.fromString((String) m.get("uuid")) + ":" + m.get("name") + ":" + pos + ":" + m.get("kills"));
            pos++;
        }
        plugin.getTop().addTop(TopType.KILLS, tops);
    }
    
    @Override
    public void loadTopWins(){
        int pos = 1;
        List<String> tops = new ArrayList<>();
        for ( DBObject m : players.find().sort(new BasicDBObject("wins", -1)).limit(10) ){
            tops.add(UUID.fromString((String) m.get("uuid")) + ":" + m.get("name") + ":" + pos + ":" + m.get("wins"));
            pos++;
        }
        plugin.getTop().addTop(TopType.WINS, tops);
    }
    
    @Override
    public void loadTopDeaths(){
        int pos = 1;
        List<String> tops = new ArrayList<>();
        for ( DBObject m : players.find().sort(new BasicDBObject("deaths", -1)).limit(10) ){
            tops.add(UUID.fromString((String) m.get("uuid")) + ":" + m.get("name") + ":" + pos + ":" + m.get("deaths"));
            pos++;
        }
        plugin.getTop().addTop(TopType.DEATHS, tops);
    }
    
    @Override
    public void loadPlayer(Player p){
        if (!hasPlayer(p.getUniqueId())){
            SWPlayer sw = new SWPlayer();
            DBObject now = new BasicDBObject("uuid", p.getUniqueId().toString());
            now.put("name", p.getName());
            a(sw, now);
            addPlayer(p.getUniqueId(), sw);
            players.insert(now);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> Bukkit.getServer().getPluginManager().callEvent(new USWPlayerLoadEvent(p)));
            return;
        }
        DBObject now = new BasicDBObject("uuid", p.getUniqueId().toString());
        DBObject data = players.findOne(now);
        if (data != null){
            if (data.containsKey("skywars")){
                SWPlayer sw = Utils.fromGson(data.get("skywars").toString());
                if (sw != null){
                    sw.setName(p.getName());
                    addPlayer(p.getUniqueId(), sw);
                } else {
                    addPlayer(p.getUniqueId(), new SWPlayer());
                }
            } else {
                addPlayer(p.getUniqueId(), new SWPlayer());
            }
        } else {
            SWPlayer sw = new SWPlayer();
            now.put("name", p.getName());
            a(sw, now);
            addPlayer(p.getUniqueId(), sw);
            players.insert(now);
        }
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> Bukkit.getServer().getPluginManager().callEvent(new USWPlayerLoadEvent(p)));
    }
    
    @Override
    public void savePlayer(Player p){
        b(p);
    }
    
    private void b(Player p){
        if (p == null){
            return;
        }
        DBObject now = new BasicDBObject("uuid", p.getUniqueId().toString());
        DBObject data = players.findOne(now);
        if (data != null){
            DBObject save = new BasicDBObject("uuid", p.getUniqueId().toString());
            SWPlayer sw = SWPlayers.get(p.getUniqueId());
            save.put("name", sw.getName());
            a(sw, save);
            players.update(data, save);
        }
    }
    
    @Override
    public void saveAll(CallBackAPI<Boolean> done){
        new BukkitRunnable() {
            @Override
            public void run(){
                Bukkit.getOnlinePlayers().forEach(p -> {
                    b(p);
                });
                done.done(true);
            }
        }.runTaskAsynchronously(plugin);
    }
    
    @Override
    public void savePlayerSync(UUID uuid){
        DBObject now = new BasicDBObject("uuid", uuid.toString());
        DBObject data = players.findOne(now);
        if (data != null){
            DBObject save = new BasicDBObject("uuid", uuid.toString());
            SWPlayer sw = SWPlayers.get(uuid);
            save.put("name", sw.getName());
            a(sw, save);
            players.update(data, save);
        }
    }
    
    @Override
    public void close(){
        mc.close();
    }
    
    @Override
    public void createPlayer(UUID uuid, String name, SWPlayer ps){
        DBObject now = new BasicDBObject("uuid", uuid.toString());
        now.put("name", name);
        a(ps, now);
        players.insert(now);
    }
    
    private void a(SWPlayer ps, DBObject now){
        now.put("skywars", Utils.toGson(ps));
        now.put("kills", ps.getTotalStat(StatType.KILLS));
        now.put("wins", ps.getTotalStat(StatType.WINS));
        now.put("deaths", ps.getTotalStat(StatType.DEATHS));
        now.put("coins", ps.getCoins());
        now.put("elo", ps.getElo());
    }
    
    private void addPlayer(UUID uuid, SWPlayer sw){
        SWPlayers.put(uuid, sw);
    }
    
    @Override
    public Connection getConnection(){
        return null;
    }
    
    @Override
    public void clearStats(Player p){
    
    }
    
    @Override
    public SWPlayer getSWPlayer(Player p){
        return SWPlayers.get(p.getUniqueId());
    }
    
}