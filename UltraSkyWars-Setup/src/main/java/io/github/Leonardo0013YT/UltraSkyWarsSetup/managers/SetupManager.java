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

package io.github.Leonardo0013YT.UltraSkyWarsSetup.managers;

import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.UltraInventory;
import io.github.Leonardo0013YT.UltraSkyWarsSetup.setup.*;
import io.github.Leonardo0013YT.UltraSkyWarsSetup.setup.cosmetics.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class SetupManager {
    
    private final ArrayList<UUID> setupCubeBlock = new ArrayList<>();
    private final ArrayList<UUID> setupSoulBlock = new ArrayList<>();
    private final HashMap<UUID, PreviewSetup> setupPreview = new HashMap<>();
    private final HashMap<UUID, UltraInventory> setupInventory = new HashMap<>();
    private final HashMap<UUID, GlassSetup> setupGlass = new HashMap<>();
    private final HashMap<UUID, EventSetup> setupEvent = new HashMap<>();
    private final HashMap<UUID, ArenaSetup> setup = new HashMap<>();
    private final HashMap<UUID, KillSoundSetup> setupKillSound = new HashMap<>();
    private final HashMap<UUID, PartingSetup> setupParting = new HashMap<>();
    private final HashMap<UUID, TrailSetup> setupTrail = new HashMap<>();
    private final HashMap<UUID, BalloonSetup> setupBalloon = new HashMap<>();
    private final HashMap<UUID, TauntSetup> setupTaunt = new HashMap<>();
    private final HashMap<UUID, KitSetup> setupKit = new HashMap<>();
    private final HashMap<UUID, String> editName = new HashMap<>();
    private final HashMap<UUID, ChestSetup> setupChest = new HashMap<>();
    private final HashMap<UUID, SoulWellSetup> setupSoulWell = new HashMap<>();
    private final HashMap<UUID, String> delete = new HashMap<>();
    private final HashMap<UUID, ChestTypeSetup> setupChestType = new HashMap<>();
    
    public void setSetupPreview(Player p, PreviewSetup name){
        setupPreview.put(p.getUniqueId(), name);
    }
    
    public boolean isSetupPreview(Player p){
        return setupPreview.containsKey(p.getUniqueId());
    }
    
    public void removeSetupPreview(Player p){
        setupPreview.remove(p.getUniqueId());
    }
    
    public PreviewSetup getSetupPreview(Player p){
        return setupPreview.get(p.getUniqueId());
    }
    
    public void setChestType(Player p, ChestTypeSetup name){
        setupChestType.put(p.getUniqueId(), name);
    }
    
    public boolean isChestType(Player p){
        return setupChestType.containsKey(p.getUniqueId());
    }
    
    public void removeChestType(Player p){
        setupChestType.remove(p.getUniqueId());
    }
    
    public ChestTypeSetup getChestType(Player p){
        return setupChestType.get(p.getUniqueId());
    }
    
    public void setDelete(Player p, String name){
        delete.put(p.getUniqueId(), name);
    }
    
    public boolean isDelete(Player p){
        return delete.containsKey(p.getUniqueId());
    }
    
    public void removeDelete(Player p){
        delete.remove(p.getUniqueId());
    }
    
    public String getDelete(Player p){
        return delete.get(p.getUniqueId());
    }
    
    public void setSetupCubeBlock(Player p){
        setupCubeBlock.add(p.getUniqueId());
    }
    
    public boolean isSetupCubeBlock(Player p){
        return setupCubeBlock.contains(p.getUniqueId());
    }
    
    public void removeCubeBlock(Player p){
        setupCubeBlock.remove(p.getUniqueId());
    }
    
    public void setSetupSoulBlock(Player p){
        setupSoulBlock.add(p.getUniqueId());
    }
    
    public boolean isSetupSoulBlock(Player p){
        return setupSoulBlock.contains(p.getUniqueId());
    }
    
    public void removeSoulBlock(Player p){
        setupSoulBlock.remove(p.getUniqueId());
    }
    
    public void setSetupSoulWell(Player p, SoulWellSetup a){
        setupSoulWell.put(p.getUniqueId(), a);
    }
    
    public SoulWellSetup getSetupSoulWell(Player p){
        return setupSoulWell.get(p.getUniqueId());
    }
    
    public boolean isSetupSoulWell(Player p){
        return setupSoulWell.containsKey(p.getUniqueId());
    }
    
    public void removeSoulWell(Player p){
        setupSoulWell.remove(p.getUniqueId());
    }
    
    public void setSetupParting(Player p, PartingSetup a){
        setupParting.put(p.getUniqueId(), a);
    }
    
    public PartingSetup getSetupParting(Player p){
        return setupParting.get(p.getUniqueId());
    }
    
    public boolean isSetupParting(Player p){
        return setupParting.containsKey(p.getUniqueId());
    }
    
    public void removeParting(Player p){
        setupParting.remove(p.getUniqueId());
    }
    
    public void setSetupKillSound(Player p, KillSoundSetup a){
        setupKillSound.put(p.getUniqueId(), a);
    }
    
    public KillSoundSetup getSetupKillSound(Player p){
        return setupKillSound.get(p.getUniqueId());
    }
    
    public boolean isSetupKillSound(Player p){
        return setupKillSound.containsKey(p.getUniqueId());
    }
    
    public void removeKillSound(Player p){
        setupKillSound.remove(p.getUniqueId());
    }
    
    public void setSetupBalloon(Player p, BalloonSetup a){
        setupBalloon.put(p.getUniqueId(), a);
    }
    
    public BalloonSetup getSetupBalloon(Player p){
        return setupBalloon.get(p.getUniqueId());
    }
    
    public boolean isSetupBalloon(Player p){
        return setupBalloon.containsKey(p.getUniqueId());
    }
    
    public void removeBalloon(Player p){
        setupBalloon.remove(p.getUniqueId());
    }
    
    public void setSetupTrail(Player p, TrailSetup a){
        setupTrail.put(p.getUniqueId(), a);
    }
    
    public TrailSetup getSetupTrail(Player p){
        return setupTrail.get(p.getUniqueId());
    }
    
    public boolean isSetupTrail(Player p){
        return setupTrail.containsKey(p.getUniqueId());
    }
    
    public void removeTrail(Player p){
        setupTrail.remove(p.getUniqueId());
    }
    
    public void setSetupChest(Player p, ChestSetup a){
        setupChest.put(p.getUniqueId(), a);
    }
    
    public ChestSetup getSetupChest(Player p){
        return setupChest.get(p.getUniqueId());
    }
    
    public boolean isSetupChest(Player p){
        return setupChest.containsKey(p.getUniqueId());
    }
    
    public void removeChest(Player p){
        setupChest.remove(p.getUniqueId());
    }
    
    public void setSetupTaunt(Player p, TauntSetup a){
        setupTaunt.put(p.getUniqueId(), a);
    }
    
    public TauntSetup getSetupTaunt(Player p){
        return setupTaunt.get(p.getUniqueId());
    }
    
    public boolean isSetupTaunt(Player p){
        return setupTaunt.containsKey(p.getUniqueId());
    }
    
    public void removeTaunt(Player p){
        setupTaunt.remove(p.getUniqueId());
    }
    
    public void setSetupEvent(Player p, EventSetup a){
        setupEvent.put(p.getUniqueId(), a);
    }
    
    public boolean isSetupEvent(Player p){
        return setupEvent.containsKey(p.getUniqueId());
    }
    
    public void removeEvent(Player p){
        setupEvent.remove(p.getUniqueId());
    }
    
    public void setSetupName(Player p, String a){
        editName.put(p.getUniqueId(), a);
    }
    
    public String getSetupName(Player p){
        return editName.get(p.getUniqueId());
    }
    
    public boolean isSetupName(Player p){
        return editName.containsKey(p.getUniqueId());
    }
    
    public void removeName(Player p){
        editName.remove(p.getUniqueId());
    }
    
    public void setSetupInventory(Player p, UltraInventory a){
        setupInventory.put(p.getUniqueId(), a);
    }
    
    public UltraInventory getSetupInventory(Player p){
        return setupInventory.get(p.getUniqueId());
    }
    
    public boolean isSetupInventory(Player p){
        return setupInventory.containsKey(p.getUniqueId());
    }
    
    public void removeInventory(Player p){
        setupInventory.remove(p.getUniqueId());
    }
    
    public void setSetup(Player p, ArenaSetup a){
        setup.put(p.getUniqueId(), a);
    }
    
    public ArenaSetup getSetup(Player p){
        return setup.get(p.getUniqueId());
    }
    
    public boolean isSetup(Player p){
        return setup.containsKey(p.getUniqueId());
    }
    
    public void remove(Player p){
        setup.remove(p.getUniqueId());
    }
    
    public void setSetupGlass(Player p, GlassSetup a){
        setupGlass.put(p.getUniqueId(), a);
    }
    
    public GlassSetup getSetupGlass(Player p){
        return setupGlass.get(p.getUniqueId());
    }
    
    public boolean isSetupGlass(Player p){
        return setupGlass.containsKey(p.getUniqueId());
    }
    
    public void removeGlass(Player p){
        setupGlass.remove(p.getUniqueId());
    }
    
    public void setSetupKit(Player p, KitSetup a){
        setupKit.put(p.getUniqueId(), a);
    }
    
    public KitSetup getSetupKit(Player p){
        return setupKit.get(p.getUniqueId());
    }
    
    public boolean isSetupKit(Player p){
        return setupKit.containsKey(p.getUniqueId());
    }
    
    public void removeKit(Player p){
        setupKit.remove(p.getUniqueId());
    }
    
}