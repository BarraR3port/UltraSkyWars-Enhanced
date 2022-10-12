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

package io.github.Leonardo0013YT.UltraSkyWars.api.game;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.chests.ChestItem;
import io.github.Leonardo0013YT.UltraSkyWars.api.chests.ChestType;
import io.github.Leonardo0013YT.UltraSkyWars.api.chests.SWChest;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GameChest {
    
    private final Map<Location, UltraGameChest> invs = new HashMap<>();
    private final boolean center;
    private final List<Location> chests;
    
    public GameChest(boolean center, List<Location> chests){
        this.center = center;
        this.chests = chests;
        for ( Location l : chests ){
            if (l.getBlock().getState() instanceof Chest){
                invs.put(l.getBlock().getLocation(), new UltraGameChest(l));
            }
        }
    }
    
    public List<Location> getChests(){
        return chests;
    }
    
    public Map<Location, UltraGameChest> getInvs(){
        return invs;
    }
    
    public boolean isCenter(){
        return center;
    }
    
    public void fill(Game game, boolean refill){
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        for ( Location l : chests ){
            if (l.getBlock().getState() instanceof Chest){
                invs.put(l, new UltraGameChest(l));
            }
        }
        if (invs.isEmpty()){
            return;
        }
        int[][] secure = new int[invs.size()][(game.getTeamSize() * 6) / invs.size()];
        for ( int i = 0; i < secure.length; i++ ){
            for ( int x = 0; x < secure[i].length; x++ ){
                secure[i][x] = plugin.getRandomizer().getSelectors()[x];
            }
        }
        int i = 0;
        int s = 0;
        ChestType ct = plugin.getCtm().getChests().get(game.getChestType());
        SWChest sw = ct.getChest();
        ArrayList<ItemStack> selected = new ArrayList<>();
        for ( int t = 0; t < game.getTeamSize(); t++ ){
            selected.addAll(Arrays.asList(sw.getRandomHelmet(ct.isRefillChange() && refill, game.getGameType()), sw.getRandomChestplate(ct.isRefillChange() && refill, game.getGameType()), sw.getRandomLeggings(ct.isRefillChange() && refill, game.getGameType()), sw.getRandomBoots(ct.isRefillChange() && refill, game.getGameType()), sw.getRandomSword(ct.isRefillChange() && refill, game.getGameType()), sw.getRandomBowItem(ct.isRefillChange() && refill, game.getGameType())));
            if (!ct.isArmorAllTeams()){
                break;
            }
        }
        Collections.shuffle(selected);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for ( UltraGameChest ugc : invs.values() ){
            Inventory inv = ugc.getInv();
            if (inv == null){
                continue;
            }
            inv.clear();
            Integer[] randomizer = plugin.getRandomizer().getRandomizer();
            int added = 0;
            for ( int r : randomizer ){
                ChestItem ci;
                if (added >= plugin.getCm().getMaxItemsChest()){
                    break;
                }
                if (center){
                    if (ct.isRefillChange()){
                        ci = sw.getCenterItem(refill, game.getGameType());
                    } else {
                        ci = sw.getCenterItem(false, game.getGameType());
                    }
                } else {
                    if (ct.isRefillChange()){
                        ci = sw.getRandomItem(refill, game.getGameType());
                    } else {
                        ci = sw.getRandomItem(false, game.getGameType());
                    }
                }
                if (ci != null && random.nextInt(0, 10000) <= ci.getPercent()){
                    inv.setItem(r, ci.getItem());
                    added++;
                } else {
                    if (ci != null && random.nextInt(0, 10000) <= ci.getPercent()){
                        inv.setItem(r, ci.getItem());
                        added++;
                    } else {
                        if (game.getProjectileType().isAppear() && !center){
                            ChestItem ci2 = sw.getRandomProjectileItem(ct.isRefillChange() && refill, game.getGameType());
                            if (ci2 != null && random.nextInt(0, 10000) <= ci2.getPercent()){
                                inv.setItem(r, ci2.getItem());
                                added++;
                            }
                        }
                    }
                }
            }
            if (!center){
                for ( int x : secure[i] ){
                    if (s >= selected.size()) continue;
                    inv.setItem(x, selected.get(s));
                    s++;
                }
            }
            i++;
        }
    }
    
}