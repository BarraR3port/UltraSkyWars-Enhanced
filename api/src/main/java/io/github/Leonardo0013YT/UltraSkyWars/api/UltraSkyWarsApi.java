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

package io.github.Leonardo0013YT.UltraSkyWars.api;

import com.google.gson.Gson;
import io.github.Leonardo0013YT.UltraSkyWars.api.config.Settings;
import io.github.Leonardo0013YT.UltraSkyWars.api.game.GameData;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.*;
import io.github.Leonardo0013YT.UltraSkyWars.api.managers.*;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.parties.party.Party;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.DependUtils;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Randomizer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@Setter
@Getter
public abstract class UltraSkyWarsApi extends JavaPlugin {
    protected static final Gson gson = new Gson();
    protected static UltraSkyWarsApi instance;
    protected final ArrayList<Entity> entities = new ArrayList<>();
    protected Settings sounds, chestType, migrators, join, arenas, lang, menus, chests, balloon, glass, killeffect, killsound, kits, parting, taunt, trail, windance, wineffect, levels, rewards;
    protected ItemManager im;
    protected ConfigManager cm;
    protected IUltraInventoryMenu uim;
    protected IWorldController wc;
    protected IVersionController vc;
    protected GameManager gm;
    protected KitManager km;
    protected Location mainLobby, topKills, topWins, topDeaths, topCoins, topElo;
    protected Database db;
    protected TaggedManager tgm;
    protected ScoreboardManager sb;
    protected TopManager top;
    protected MultiplierManager mm;
    protected IAddonManager adm;
    protected ShopManager shm;
    protected LevelManager lvl;
    protected IGameMenu gem;
    protected DependUtils du;
    protected TaskManager tsm;
    protected ModuleManager ijm;
    protected ChestManager ctm;
    protected CosmeticManager cos;
    protected Randomizer randomizer;
    protected boolean debugMode, stop = false;
    @Setter
    protected boolean disabled = false;
    
    public UltraSkyWarsApi(){
        instance = this;
        
    }
    
    public static UltraSkyWarsApi get(){
        return instance;
    }
    
    public static Gson getGson(){
        return gson;
    }
    
    public static void sendRedisMessage(String channel, String msg){
        //instance.getBm().sendMessage(channel, msg);
    }
    
    public static boolean isSpectator(Player p){
        Game game = instance.getGm().getGameByPlayer(p);
        if (game != null){
            return game.getSpectators().contains(p);
        }
        return false;
    }
    
    public static boolean isPlayerGame(Player p){
        Game game = instance.getGm().getGameByPlayer(p);
        return game != null;
    }
    
    public static Map<String, GameData> getGameData(){
        return instance.getGm().getGameData();
    }
    
    public static boolean isInParty(Player p){
        if (!instance.getIjm().isParty()) return false;
        return instance.getIjm().getParty().getPam().isInParty(p);
    }
    
    public static boolean isPartyLeader(Player p){
        if (!instance.getIjm().isParty()) return false;
        return instance.getIjm().getParty().getPam().isLeader(p);
    }
    
    public static Party getPartyByPlayer(Player p){
        return getPartyByPlayer(p.getUniqueId());
    }
    
    public static Party getPartyByPlayer(UUID uuid){
        if (!instance.getIjm().isParty()) return null;
        return instance.getIjm().getParty().getPam().getPartyByPlayer(uuid);
    }
    
    public abstract void sendLogMessage(String msg);
    
    public abstract void sendLogMessage(String... msg);
    
    public abstract void updateDirectories();
    
    public abstract void reloadLang();
    
    public abstract void sendToServer(Player p, String server);
    
    public abstract void reload();
    
    public abstract void copy(String origin, String destiny);
    
    public abstract void sendDebugMessage(String... s);
    
    public abstract void saveSchematics();
    
    public abstract void broadcastGame(Game game);
    
    public abstract void loadMainLobby();
    
    public abstract boolean isSetupLobby(Player p);
    
    public abstract boolean isBungeeMode();
    
}
