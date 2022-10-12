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

import io.github.Leonardo0013YT.UltraSkyWars.api.actions.InventoryAction;
import io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.kits.Kit;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.OrderType;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.UltraInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public interface IUltraInventoryMenu {
    void loadMenus();
    
    void removeInventory(Player p);
    
    UltraInventory getMenus(String t);
    
    HashMap<String, UltraInventory> getMenus();
    
    void openInventory(Player p, UltraInventory i);
    
    Inventory openContentInventory(Player p, UltraInventory i);
    
    void openPlayersInventory(Player p, UltraInventory i, Game game, OrderType type, String[]... t);
    
    void openInventory(Player p, UltraInventory i, String[]... t);
    
    void openChestInventory(Player p, UltraInventory i, String[]... t);
    
    void createPartingSelectorMenu(Player p);
    
    void createKitSelectorMenu(Player p, String type, boolean game);
    
    void createTrailsSelectorMenu(Player p);
    
    void createTauntsSelectorMenu(Player p);
    
    void createGlassSelectorMenu(Player p);
    
    void createBalloonSelectorMenu(Player p);
    
    void createKillSoundSelectorMenu(Player p);
    
    void createKillEffectSelectorMenu(Player p);
    
    void createKitLevelSelectorMenu(Player p, Kit k, String type);
    
    void createWinDanceSelectorMenu(Player p);
    
    void createWinEffectSelectorMenu(Player p);
    
    void setInventory(String inv, Inventory close);
    
    HashMap<String, Consumer<InventoryAction>> getActions();
    
    HashMap<UUID, Integer> getPages();
    
    void addPage(Player p);
    
    void removePage(Player p);
    
    
}
