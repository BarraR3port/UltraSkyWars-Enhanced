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

package io.github.Leonardo0013YT.UltraSkyWars.api.utils;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.StatType;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tagged {
    
    private final HashMap<Player, Double> damagers = new HashMap<>();
    private final HashMap<Player, Long> timer = new HashMap<>();
    private final Player damaged;
    private final Game game;
    private final DecimalFormat f = new DecimalFormat("##.#");
    private Player last;
    
    public Tagged(Player damaged, Game game){
        this.damaged = damaged;
        this.game = game;
    }
    
    public void addPlayerDamage(Player p, double damage){
        last = p;
        if (!damagers.containsKey(p)){
            damagers.put(p, damage);
            timer.put(p, getTime());
            return;
        }
        double d = damagers.get(p);
        damagers.put(p, d + damage);
        timer.put(p, getTime());
    }
    
    public void removeDamage(double dam){
        List<Player> to = new ArrayList<>();
        for ( Player on : damagers.keySet() ){
            if (timer.get(on) < System.currentTimeMillis()){
                to.add(on);
                continue;
            }
            if (damagers.get(on) - dam < 0){
                to.add(on);
                continue;
            }
            damagers.put(on, damagers.get(on) - dam);
        }
        for ( Player on : to ){
            timer.remove(on);
            damagers.remove(on);
        }
    }
    
    public void executeRewards(double maxHealth){
        List<Player> to = new ArrayList<>();
        for ( Player on : damagers.keySet() ){
            if (on == null || !on.isOnline()) continue;
            if (timer.get(on) < System.currentTimeMillis()){
                to.add(on);
                continue;
            }
            if (on.getName().equals(last.getName())){
                continue;
            }
            double damage = damagers.get(on);
            double percent = (damage * 100) / maxHealth;
            on.sendMessage(UltraSkyWarsApi.get().getLang().get(on, "assists").replaceAll("<percent>", f.format(percent)).replaceAll("<name>", damaged.getName()));
            SWPlayer up = UltraSkyWarsApi.get().getDb().getSWPlayer(on);
            up.addCoins(UltraSkyWarsApi.get().getCm().getCoinsAssists());
            up.addSouls(UltraSkyWarsApi.get().getCm().getSoulsAssists());
            up.addXp(UltraSkyWarsApi.get().getCm().getXpAssists());
            up.addStat(StatType.ASSISTS, game.getGameType(), 1);
        }
        for ( Player on : to ){
            timer.remove(on);
            damagers.remove(on);
        }
        if (last != null){
            if (!timer.containsKey(last) || !damagers.containsKey(last)){
                return;
            }
            if (timer.get(last) < System.currentTimeMillis()){
                timer.remove(last);
                return;
            }
            if (damagers.size() == 1){
                SWPlayer up = UltraSkyWarsApi.get().getDb().getSWPlayer(last);
                if (up == null) return;
                double percent = (last.getHealth() * 100) / last.getMaxHealth();
                if (percent <= 50 && percent > 25){
                    up.addStat(StatType.KILL50, game.getGameType(), 1);
                }
                if (percent <= 25 && percent > 5){
                    up.addStat(StatType.KILL25, game.getGameType(), 1);
                }
                if (percent <= 5 && percent > 1){
                    up.addStat(StatType.KILL5, game.getGameType(), 1);
                }
            }
        }
    }
    
    public Player getLast(){
        return last;
    }
    
    private long getTime(){
        return System.currentTimeMillis() + (10 * 1000);
    }
    
}