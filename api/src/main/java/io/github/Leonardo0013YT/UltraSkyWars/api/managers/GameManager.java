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

package io.github.Leonardo0013YT.UltraSkyWars.api.managers;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.State;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.*;
import io.github.Leonardo0013YT.UltraSkyWars.api.game.GameData;
import io.github.Leonardo0013YT.UltraSkyWars.api.game.UltraGame;
import io.github.Leonardo0013YT.UltraSkyWars.api.game.UltraRankedGame;
import io.github.Leonardo0013YT.UltraSkyWars.api.game.UltraTeamGame;
import io.github.Leonardo0013YT.UltraSkyWars.api.objects.CustomJoin;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.GameEvent;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameManager {
    
    private final HashSet<String> modes = new HashSet<>();
    private final Map<String, GameData> gameData = new HashMap<>();
    private final Map<Integer, Game> games = new HashMap<>();
    private final Map<String, Integer> gamesID = new HashMap<>();
    private final Map<String, CustomJoin> joins = new HashMap<>();
    private final Map<UUID, Integer> playerGame = new HashMap<>();
    private final Map<String, GameEvent> events = new HashMap<>();
    private final Map<Integer, String> gamesUpdating = new ConcurrentHashMap<>();
    private final Map<String, Integer> worlds = new HashMap<>();
    private final Map<String, Integer> players = new HashMap<>();
    private final Map<Integer, BukkitTask> tasks = new HashMap<>();
    private final UltraSkyWarsApi plugin;
    private long lastUpdatePlayers = 0L;
    private Game bungee;
    
    public GameManager(UltraSkyWarsApi plugin){
        this.plugin = plugin;
    }
    
    public void reload(){
        modes.add("SOLO");
        modes.add("TEAM");
        modes.add("RANKED");
        modes.add("ALL");
        modes.add("TNT_MADNESS");
        modes.add("LUCKY");
        modes.add("RUSH");
        events.put("none", new NoneEvent(plugin, 0));
        events.put("refill", new RefillEvent(plugin, 0));
        events.put("dragon", new DragonEvent(plugin, 0));
        events.put("tnt", new TNTEvent(plugin, 0));
        events.put("wither", new WitherEvent(plugin, 0));
        events.put("zombies", new ZombieEvent(plugin, 0));
        events.put("border", new BorderEvent(plugin, 0));
        if (plugin.getArenas().isSet("arenas")){
            ConfigurationSection conf = plugin.getArenas().getConfig().getConfigurationSection("arenas");
            for ( String c : conf.getKeys(false) ){
                int id = games.size();
                boolean ranked = plugin.getArenas().getBoolean("arenas." + c + ".ranked");
                boolean enabled = plugin.getArenas().getBooleanOrDefault("arenas." + c + ".enabled", true);
                if (!enabled){
                    plugin.sendLogMessage("&cArena &e" + c + " &cis disabled.");
                    continue;
                }
                int teamSize = plugin.getArenas().getInt("arenas." + c + ".teamSize");
                Game game;
                AtomicBoolean load = new AtomicBoolean(false);
                if (ranked){
                    game = new UltraRankedGame(plugin, this, "arenas." + c, c, id, (b) -> setAtomic(b, c, load));
                } else {
                    if (teamSize > 1){
                        game = new UltraTeamGame(plugin, this, "arenas." + c, c, id, (b) -> setAtomic(b, c, load));
                    } else {
                        game = new UltraGame(plugin, this, "arenas." + c, c, id, (b) -> setAtomic(b, c, load));
                    }
                }
                game.setState(State.WAITING);
                if (!load.get()){
                    plugin.sendLogMessage("§cArena " + c + " no loaded correctly!");
                    continue;
                }
                games.put(id, game);
                gamesID.put(game.getName(), id);
                worlds.put(game.getName(), id);
            }
        }
        if (plugin.getJoin().isSet("joins")){
            ConfigurationSection j = plugin.getJoin().getConfig().getConfigurationSection("joins");
            for ( String s : j.getKeys(false) ){
                String name = plugin.getJoin().get(null, "joins." + s + ".name");
                joins.put(name, new CustomJoin(plugin, "joins." + s));
            }
        }
    }
    
    public HashSet<String> getModes(){
        return modes;
    }
    
    public void handleTeleport(Location l, String name, boolean add, double addX, double addY, double addZ, Player... players){
        if (l == null || l.getWorld() == null){
            plugin.sendLogMessage("Location null or world null " + name);
            return;
        }
        if (add){
            l.add(addX, addY, addZ);
            for ( Player p : players ){
                p.teleport(l);
            }
            l.subtract(addX, addY, addZ);
        } else {
            for ( Player p : players ){
                p.teleport(l);
                if (name.equals("MainLobby") && plugin.getCm().isAutoFlyEnabled()){
                    if (p.hasPermission(plugin.getCm().getAutoFlyPermission())){
                        p.setAllowFlight(true);
                        p.setFlying(true);
                    }
                }
            }
        }
    }
    
    public void setAtomic(String b, String c, AtomicBoolean load){
        if (b.equals("NO_ISLANDS")){
            plugin.sendLogMessage("Arena error §c" + c + " §eno islands.");
        }
        if (b.equals("NO_CENTERS")){
            plugin.sendLogMessage("Arena error §c" + c + " §eno center chests.");
        }
        if (b.equals("NO_LOBBY")){
            plugin.sendLogMessage("Arena error §c" + c + " §eno lobby set.");
        }
        if (b.equals("NO_SPECTATOR")){
            plugin.sendLogMessage("Arena error §c" + c + " §eno spectator set.");
        }
        if (b.equals("DONE")){
            load.set(true);
        }
    }
    
    public void updateGame(String server, String map, String color, String state, String type, int players, int max){
        if (!gameData.containsKey(server)){
            gameData.put(server, new GameData(server, map, color, state, type.toLowerCase(), players, max));
        } else {
            GameData gd = gameData.get(server);
            gd.setState(state);
            gd.setPlayers(players);
            gd.setMax(max);
        }
        if (plugin.getIjm().isSignsInjection()){
            plugin.getIjm().getSigns().getSim().update(type.toLowerCase(), server, map, state, players, max);
        }
    }
    
    public void updateGame(String map, String color, String state, String type, int players, int max){
        if (!gameData.containsKey(map)){
            gameData.put(map, new GameData(map, color, state, type.toLowerCase(), players, max));
        } else {
            GameData gd = gameData.get(map);
            gd.setState(state);
            gd.setPlayers(players);
            gd.setMax(max);
        }
        if (plugin.getIjm().isSignsInjection()){
            plugin.getIjm().getSigns().getSim().update(type.toLowerCase(), map, state, players, max);
        }
    }
    
    public void removeGameServer(String server){
        if (gameData.containsKey(server)){
            gameData.get(server).setState("EMPTY");
        }
        if (plugin.getIjm().isSignsInjection()){
            for ( GameData gd : gameData.values() ){
                if (gd == null) continue;
                plugin.getIjm().getSigns().getSim().update(gd.getType().toLowerCase(), gd.getServer(), gd.getMap(), gd.getState(), gd.getPlayers(), gd.getMax());
            }
        }
    }
    
    public void removeGameMap(String map){
        gameData.remove(map);
    }
    
    public Game getBungee(){
        if (bungee == null){
            if (games.size() > 0){
                bungee = this.games.get(0);
            }
        }
        return bungee;
    }
    
    public synchronized void addPlayerGame(Player p, int id){
        addPlayerGame(p, id, false);
    }
    
    public synchronized void addPlayerGame(Player p, int id, boolean ignoreParty){
        Game game = games.get(id);
        if (checkPlayerGame(p, game)) return;
        if (plugin.getAdm().getParties() != null && !ignoreParty){
            if (plugin.getAdm().getParties().isInParty(p) && plugin.getAdm().getParties().isPartyLeader(p)){
                boolean noRanked = game.getGameType().equals("RANKED") && plugin.getCm().isRankedJoin() && !plugin.getCm().isRankedJoinParties();
                if (!noRanked){
                    for ( Player on : plugin.getAdm().getParties().getPlayersParty(p) ){
                        if (on == null || !on.isOnline()) continue;
                        addPartyPlayer(on, p, game);
                    }
                } else {
                    p.sendMessage(plugin.getLang().get("messages.noGameRankedWithParty"));
                }
                return;
            } else {
                if (plugin.getAdm().getParties().isInParty(p)){
                    p.sendMessage(plugin.getLang().get(p, "parties.noJoin"));
                    return;
                }
            }
        }
        if (!ignoreParty && plugin.getIjm().isParty() && plugin.getIjm().getParty().getPam().isLeader(p)){
            boolean noRanked = game.getGameType().equals("RANKED") && plugin.getCm().isRankedJoin() && !plugin.getCm().isRankedJoinParties();
            if (!noRanked){
                for ( UUID uuid : plugin.getIjm().getParty().getPam().getPartyByPlayer(p.getUniqueId()).getMembers().keySet() ){
                    Player on = Bukkit.getPlayer(uuid);
                    if (on == null || !on.isOnline()) continue;
                    addPartyPlayer(on, p, game);
                }
            } else {
                p.sendMessage(plugin.getLang().get("messages.noGameRankedWithParty"));
            }
            return;
        }
        USWGameJoinEvent e = new USWGameJoinEvent(p, game);
        Bukkit.getServer().getPluginManager().callEvent(e);
        if (e.isCancelled()){
            return;
        }
        if (plugin.getCm().isRankedJoin()){
            int l = plugin.getCm().getRankedLevels();
            boolean has = plugin.getLvl().getLevel(p).getLevel() >= l;
            if (game.getGameType().equals("RANKED") && !has){
                p.sendMessage(plugin.getLang().get("messages.noLevelToRanked").replace("<level>", String.valueOf(l)));
                return;
            }
        }
        playerGame.put(p.getUniqueId(), id);
        game.addPlayer(p);
    }
    
    public void addPartyPlayer(Player on, Player leader, Game game){
        if (on.equals(leader)){
            on.sendMessage(plugin.getLang().get(on, "parties.join"));
        } else {
            on.sendMessage(plugin.getLang().get(on, "parties.joinGame"));
        }
        removePlayerAllGame(on);
        addPlayerGame(on, game.getId(), true);
    }
    
    private boolean checkPlayerGame(Player on, Game game){
        if (game.isState(State.FINISH) || game.isState(State.RESTARTING) || game.isState(State.GAME) || game.isState(State.PREGAME) || (game.isState(State.STARTING) && game.getStarting() < 3)){
            on.sendMessage(plugin.getLang().get(on, "messages.alreadyStart"));
            return true;
        }
        if (game.getCached().size() >= game.getMax()){
            on.sendMessage(plugin.getLang().get(on, "messages.fullGame"));
            return true;
        }
        return false;
    }
    
    public void removePlayerGame(Player p, int id){
        Game game = games.get(id);
        USWGameQuitEvent event = new USWGameQuitEvent(p, game);
        Bukkit.getServer().getPluginManager().callEvent(event);
        game.removePlayer(p);
    }
    
    public GameData getGameRandomFavorites(Player p, String type){
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        ArrayList<GameData> games = getGamesByType(type.toLowerCase()).stream().filter(d -> d.getState().equals("WAITING") || d.getState().equals("STARTING")).filter(d -> d.getPlayers() < d.getMax()).filter(game -> sw.getFavorites().contains(game.getMap())).collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(games);
        int alt = 0;
        GameData g = null;
        for ( GameData game : games ){
            if (g == null || alt <= game.getPlayers()){
                g = game;
                alt = game.getPlayers();
            }
        }
        return g;
    }
    
    public boolean addRandomGame(Player p, String type){
        List<GameData> now = new ArrayList<>(gameData.values());
        Collections.shuffle(now);
        int alt = 0;
        GameData g = null;
        Stream<GameData> filter = now.stream().filter(d -> d.getState().equals("WAITING") || d.getState().equals("STARTING")).filter(d -> d.getPlayers() < d.getMax());
        List<GameData> fixed;
        if (type.equals("ALL")){
            fixed = filter.collect(Collectors.toList());
        } else {
            fixed = filter.filter(d -> d.getType().equalsIgnoreCase(type)).collect(Collectors.toList());
        }
        for ( GameData game : fixed ){
            if (g == null || alt < game.getPlayers()){
                g = game;
                alt = game.getPlayers();
            }
        }
        if (g != null){
            addPlayerGame(p, gamesID.get(g.getMap()));
            return true;
        }
        return false;
    }
    
    public synchronized void removePlayerAllGame(Player p){
        if (!playerGame.containsKey(p.getUniqueId())) return;
        int id = playerGame.get(p.getUniqueId());
        removePlayerGame(p, id);
        plugin.getLvl().checkUpgrade(p);
        playerGame.remove(p.getUniqueId());
        Utils.updateSB(p);
    }
    
    public GameData getDataByMap(String map){
        for ( GameData gd : gameData.values() ){
            if (gd.getMap().equals(map)){
                return gd;
            }
        }
        return null;
    }
    
    public Map<String, GameData> getGameData(){
        return gameData;
    }
    
    public Map<String, Integer> getWorlds(){
        return worlds;
    }
    
    public Map<Integer, Game> getGames(){
        return games;
    }
    
    public Game getGameByPlayer(Player p){
        return games.get(playerGame.get(p.getUniqueId()));
    }
    
    public boolean isPlayerInGame(Player p){
        return playerGame.containsKey(p.getUniqueId());
    }
    
    public GameData getGameByPlayers(String type, int amount, List<GameData> ready){
        List<GameData> games = getGamesByType(type);
        for ( GameData game : games ){
            if (game.getPlayers() == amount && !ready.contains(game)){
                return game;
            }
        }
        return null;
    }
    
    public Game getGameByName(String name){
        for ( Game g : games.values() ){
            if (g.getName().equals(name)){
                return g;
            }
        }
        return null;
    }
    
    public int getGameSize(String type){
        if (lastUpdatePlayers + plugin.getCm().getUpdatePlayersPlaceholder() < System.currentTimeMillis()){
            updatePlayersPlaceholder();
        }
        return players.getOrDefault(type, 0);
    }
    
    public void updatePlayersPlaceholder(){
        for ( String t : modes ){
            String type = t.toLowerCase();
            int count = 0;
            for ( GameData g : getGamesByType(type) ){
                if (g == null) continue;
                count += g.getPlayers();
            }
            players.put(type, count);
        }
        for ( String t : joins.keySet() ){
            CustomJoin cj = joins.get(t);
            int count = cj.getGameSize();
            players.put(t, count);
        }
        lastUpdatePlayers = System.currentTimeMillis();
    }
    
    public ArrayList<GameData> getGamesByType(String type){
        ArrayList<GameData> games = new ArrayList<>();
        for ( GameData gd : gameData.values() ){
            if (gd.getType().equals(type.toLowerCase())){
                games.add(gd);
            }
        }
        return games;
    }

    /*public void update() {
        for (int id : gamesUpdating.keySet()) {
            if (!games.containsKey(id)) continue;
            Game game = games.get(id);
            game.update();
        }
    }*/
    
    public void updateFinish(){
        for ( int id : gamesUpdating.keySet() ){
            if (!games.containsKey(id)) continue;
            Game game = games.get(id);
            game.debug();
        }
    }
    
    public void addGameUpdating(int id){
        if (!gamesUpdating.containsKey(id)){
            gamesUpdating.put(id, "");
            if (tasks.containsKey(id)){
                tasks.get(id).cancel();
            }
            tasks.put(id, new BukkitRunnable() {
                @Override
                public void run(){
                    Game game = games.get(id);
                    game.update();
                }
            }.runTaskTimer(plugin, 0, 20));
        }
    }
    
    public void removeGameUpdating(int id){
        if (tasks.containsKey(id)){
            tasks.get(id).cancel();
        }
        tasks.remove(id);
        gamesUpdating.remove(id);
    }
    
    public Map<String, CustomJoin> getJoins(){
        return joins;
    }
    
    public GameEvent getEvent(String type){
        return events.get(type);
    }
    
    public int getGameID(String map){
        return gamesID.get(map);
    }
    
    public Map<UUID, Integer> getPlayerGame(){
        return playerGame;
    }
}