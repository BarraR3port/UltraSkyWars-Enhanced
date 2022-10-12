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

import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public interface IGameMenu {
    ConcurrentHashMap<UUID, String> getViews();
    
    void createPrestigeIcons(Player p);
    
    void createKitsMenu(Player p, ItemStack[] contents, ItemStack[] armor);
    
    void createLevelMenu(Player p);
    
    void createSelectorMenu(Player p, String ignore, String type);
    
    void updateGameMenu(Player p, Inventory inv, String ignore, String type);
    
    void createTeamSelector(Player p, Game game);
    
    void updateTeamSelector(Game game, Inventory inv);
    
    void createPerksMenu(Player p, String type);
    
    void createCubeletsAnimationMenu(Player p);
    
    String getState(Player p, int id, String perm);
    
    int getSize(int games);
    
    void updateInventories(String type, String ignore);
    
}
