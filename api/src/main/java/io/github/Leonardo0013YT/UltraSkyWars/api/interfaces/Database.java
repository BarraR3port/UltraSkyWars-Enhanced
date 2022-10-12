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

package io.github.Leonardo0013YT.UltraSkyWars.api.interfaces;

import io.github.Leonardo0013YT.UltraSkyWars.api.calls.CallBackAPI;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.HashMap;
import java.util.UUID;

public interface Database {
    
    int getRanking(UUID uuid);
    
    void loadMultipliers(CallBackAPI<Boolean> request);
    
    void createMultiplier(String type, String name, double amount, long ending, CallBackAPI<Boolean> request);
    
    boolean removeMultiplier(int id);
    
    void loadTopElo();
    
    void loadTopCoins();
    
    void loadTopKills();
    
    void loadTopWins();
    
    void loadTopDeaths();
    
    void loadPlayer(Player p);
    
    void savePlayer(Player p);
    
    void saveAll(CallBackAPI<Boolean> done);
    
    void savePlayerSync(UUID uuid);
    
    HashMap<UUID, SWPlayer> getPlayers();
    
    void close();
    
    void createPlayer(UUID uuid, String name, SWPlayer ps);
    
    Connection getConnection();
    
    void clearStats(Player p);
    
    SWPlayer getSWPlayer(Player p);
}