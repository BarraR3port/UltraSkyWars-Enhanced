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

package io.github.Leonardo0013YT.UltraSkyWarsSetup.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.DamageCauses;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.RewardType;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.UltraInventory;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import io.github.Leonardo0013YT.UltraSkyWarsSetup.UltraSkyWarsSetup;
import io.github.Leonardo0013YT.UltraSkyWarsSetup.setup.*;
import io.github.Leonardo0013YT.UltraSkyWarsSetup.setup.cosmetics.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SetupListener implements Listener {
    
    private final UltraSkyWarsApi plugin;
    private final UltraSkyWarsSetup setup;
    
    public SetupListener(UltraSkyWarsApi plugin, UltraSkyWarsSetup setup){
        this.plugin = plugin;
        this.setup = setup;
    }
    
    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player p = e.getEntity();
        if (setup.getSm().isSetup(p)){
            e.getDrops().clear();
        }
    }
    
    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        Player p = e.getPlayer();
        if (setup.getSm().isSetup(p)){
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Player){
            Player p = (Player) e.getDamager();
            Entity b = e.getEntity();
            if (plugin.getConfig().isSet("cubelets")){
                ConfigurationSection conf = plugin.getConfig().getConfigurationSection("cubelets");
                String now = "";
                for ( String id : conf.getKeys(false) ){
                    String loc = plugin.getConfig().getString("cubelets." + id + ".loc");
                    if (loc.equals(Utils.getLocationString(b.getLocation()))){
                        now = id;
                    }
                }
                if (!now.equals("")){
                    e.setCancelled(true);
                    plugin.getConfig().set("cubelets." + now, null);
                    plugin.saveConfig();
                    p.sendMessage(plugin.getLang().get(p, "messages.cubeletsRemove"));
                    setup.getSm().removeCubeBlock(p);
                }
            }
        }
    }
    
    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e){
        Player p = e.getPlayer();
        if (setup.getSm().isSetupCubeBlock(p)){
            Entity b = e.getRightClicked();
            int size;
            if (plugin.getConfig().isSet("cubelets")){
                ConfigurationSection conf = plugin.getConfig().getConfigurationSection("cubelets");
                size = conf.getKeys(false).size();
            } else {
                size = 0;
            }
            size = size + ThreadLocalRandom.current().nextInt(0, 20);
            plugin.getConfig().set("cubelets." + size + ".loc", Utils.getLocationString(b.getLocation()));
            plugin.saveConfig();
            p.sendMessage(plugin.getLang().get(p, "messages.cubelets"));
            setup.getSm().removeCubeBlock(p);
        }
    }
    
    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if (setup.getSm().isSetupSoulBlock(p)){
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                Block b = e.getClickedBlock();
                int size;
                if (plugin.getConfig().isSet("soulwells")){
                    ConfigurationSection conf = plugin.getConfig().getConfigurationSection("soulwells");
                    size = conf.getKeys(false).size();
                } else {
                    size = 0;
                }
                size = size + ThreadLocalRandom.current().nextInt(0, 20);
                plugin.getConfig().set("soulwells." + size + ".loc", Utils.getLocationString(b.getLocation()));
                plugin.saveConfig();
                p.sendMessage(plugin.getLang().get(p, "messages.soulWell"));
                setup.getSm().removeSoulBlock(p);
                if (plugin.getIjm().isSoulWellInjection()){
                    plugin.getIjm().getSoulwell().getSwm().loadSoulWells();
                }
            } else if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
                Block b = e.getClickedBlock();
                if (plugin.getConfig().isSet("soulwells")){
                    ConfigurationSection conf = plugin.getConfig().getConfigurationSection("soulwells");
                    String now = "";
                    for ( String id : conf.getKeys(false) ){
                        String loc = plugin.getConfig().getString("soulwells." + id + ".loc");
                        if (loc.equals(Utils.getLocationString(b.getLocation()))){
                            now = id;
                        }
                    }
                    if (!now.equals("")){
                        e.setCancelled(true);
                        plugin.getConfig().set("soulwells." + now, null);
                        plugin.saveConfig();
                        p.sendMessage(plugin.getLang().get(p, "messages.soulWellRemove"));
                        setup.getSm().removeSoulBlock(p);
                        if (plugin.getIjm().isSoulWellInjection()){
                            plugin.getIjm().getSoulwell().getSwm().loadSoulWells();
                        }
                    }
                }
            }
        }
        if (setup.getSm().isSetupCubeBlock(p)){
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                Block b = e.getClickedBlock();
                int size;
                if (plugin.getConfig().isSet("cubelets")){
                    ConfigurationSection conf = plugin.getConfig().getConfigurationSection("cubelets");
                    size = conf.getKeys(false).size();
                } else {
                    size = 0;
                }
                size = size + ThreadLocalRandom.current().nextInt(0, 20);
                plugin.getConfig().set("cubelets." + size + ".loc", Utils.getLocationString(b.getLocation()));
                plugin.saveConfig();
                p.sendMessage(plugin.getLang().get(p, "messages.cubelets"));
                setup.getSm().removeCubeBlock(p);
            } else if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
                Block b = e.getClickedBlock();
                if (plugin.getConfig().isSet("cubelets")){
                    ConfigurationSection conf = plugin.getConfig().getConfigurationSection("cubelets");
                    String now = "";
                    for ( String id : conf.getKeys(false) ){
                        String loc = plugin.getConfig().getString("cubelets." + id + ".loc");
                        if (loc.equals(Utils.getLocationString(b.getLocation()))){
                            now = id;
                        }
                    }
                    if (!now.equals("")){
                        e.setCancelled(true);
                        plugin.getConfig().set("cubelets." + now, null);
                        plugin.saveConfig();
                        p.sendMessage(plugin.getLang().get(p, "messages.cubeletsRemove"));
                        setup.getSm().removeCubeBlock(p);
                    }
                }
            }
        }
        if (setup.getSm().isSetup(p)){
            if (p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR)){
                return;
            }
            ItemStack item = p.getItemInHand();
            if (item.equals(plugin.getIm().getSetup())){
                e.setCancelled(true);
                ArenaSetup as = setup.getSm().getSetup(p);
                setup.getSem().createSetupArenaMenu(p, as);
                changeSlot(p);
            }
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                ArenaSetup as = setup.getSm().getSetup(p);
                Block b = e.getClickedBlock();
                if (item.equals(plugin.getIm().getIsland())){
                    if (as.getActual() == null){
                        return;
                    }
                    IslandArenaSetup ias = as.getActual();
                    if (b.getType().equals(Material.CHEST) || b.getType().equals(Material.TRAPPED_CHEST) || b.getType().equals(Material.ENDER_CHEST)){
                        e.setCancelled(true);
                        if (ias.isChest(b.getLocation())){
                            p.sendMessage(plugin.getLang().get(p, "setup.arena.alreadyAdded"));
                        } else {
                            ias.addChest(b.getLocation());
                            p.sendMessage(plugin.getLang().get(p, "setup.arena.addChest"));
                        }
                    } else {
                        e.setCancelled(true);
                        p.sendMessage(plugin.getLang().get(p, "setup.arena.noChest"));
                    }
                }
                if (item.equals(plugin.getIm().getCenter())){
                    if (b.getType().equals(Material.CHEST) || b.getType().equals(Material.TRAPPED_CHEST) || b.getType().equals(Material.ENDER_CHEST)){
                        e.setCancelled(true);
                        if (as.isCenter(b.getLocation())){
                            p.sendMessage(plugin.getLang().get(p, "setup.arena.alreadyAdded"));
                        } else {
                            as.addCenter(b.getLocation());
                            p.sendMessage(plugin.getLang().get(p, "setup.arena.addChest"));
                        }
                    } else {
                        e.setCancelled(true);
                        p.sendMessage(plugin.getLang().get(p, "setup.arena.noChest"));
                    }
                }
            }
            if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
                ArenaSetup as = setup.getSm().getSetup(p);
                Block b = e.getClickedBlock();
                if (item.equals(plugin.getIm().getIsland())){
                    if (as.getActual() == null){
                        return;
                    }
                    IslandArenaSetup ias = as.getActual();
                    if (b.getType().equals(Material.CHEST) || b.getType().equals(Material.TRAPPED_CHEST) || b.getType().equals(Material.ENDER_CHEST)){
                        e.setCancelled(true);
                        if (!ias.isChest(b.getLocation())){
                            p.sendMessage(plugin.getLang().get(p, "setup.arena.noSetChest"));
                        } else {
                            ias.removeChest(b.getLocation());
                            p.sendMessage(plugin.getLang().get(p, "setup.arena.removeChest"));
                        }
                    } else {
                        e.setCancelled(true);
                        p.sendMessage(plugin.getLang().get(p, "setup.arena.noChest"));
                    }
                }
                if (item.equals(plugin.getIm().getCenter())){
                    if (b.getType().equals(Material.CHEST) || b.getType().equals(Material.TRAPPED_CHEST) || b.getType().equals(Material.ENDER_CHEST)){
                        e.setCancelled(true);
                        if (!as.isCenter(b.getLocation())){
                            p.sendMessage(plugin.getLang().get(p, "setup.arena.noSetChest"));
                        } else {
                            as.removeCenter(b.getLocation());
                            p.sendMessage(plugin.getLang().get(p, "setup.arena.removeChest"));
                        }
                    } else {
                        e.setCancelled(true);
                        p.sendMessage(plugin.getLang().get(p, "setup.arena.noChest"));
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        if (setup.getSm().isSetupGlass(p)){
            if (setup.getSm().isSetupName(p)){
                e.setCancelled(true);
                GlassSetup gs = setup.getSm().getSetupGlass(p);
                String type = setup.getSm().getSetupName(p);
                if (type.equals("permission")){
                    gs.setPermission(e.getMessage());
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createGlassMenu(p, gs));
                }
                if (type.equals("price")){
                    int price;
                    try {
                        price = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (price < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.glass.minPrice"));
                        return;
                    }
                    gs.setPrice(price);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createGlassMenu(p, gs));
                }
                if (type.equals("slot")){
                    int slot;
                    try {
                        slot = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (slot < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.glass.minSlot"));
                        return;
                    }
                    if (slot > 53){
                        p.sendMessage(plugin.getLang().get(p, "setup.glass.maxSlot"));
                        return;
                    }
                    gs.setSlot(slot);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createGlassMenu(p, gs));
                }
                if (type.equals("page")){
                    int page;
                    try {
                        page = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (page < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.glass.minPage"));
                        return;
                    }
                    gs.setPage(page);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createGlassMenu(p, gs));
                }
            }
        }
        if (setup.getSm().isSetup(p)){
            if (setup.getSm().isSetupName(p)){
                e.setCancelled(true);
                ArenaSetup as = setup.getSm().getSetup(p);
                String type = setup.getSm().getSetupName(p);
                if (type.equals("min")){
                    int min;
                    try {
                        min = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (min < 2){
                        p.sendMessage(plugin.getLang().get(p, "setup.arena.minMin"));
                        return;
                    }
                    as.setMin(min);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createSetupArenaMenu(p, as));
                }
                if (type.equals("teamsize")){
                    int teamsize;
                    try {
                        teamsize = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (teamsize < 1){
                        p.sendMessage(plugin.getLang().get(p, "setup.arena.minTeamSize"));
                        return;
                    }
                    as.setTeamSize(teamsize);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createSetupArenaMenu(p, as));
                }
                if (type.equals("borderX")){
                    int borderX;
                    try {
                        borderX = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    as.setBorderX(borderX);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createSetupArenaMenu(p, as));
                }
                if (type.equals("borderZ")){
                    int borderZ;
                    try {
                        borderZ = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    as.setBorderZ(borderZ);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createSetupArenaMenu(p, as));
                }
                if (type.equals("color")){
                    ArrayList<String> colors = new ArrayList<>();
                    for ( ChatColor cc : ChatColor.values() ){
                        colors.add(cc.name());
                    }
                    if (!colors.contains(e.getMessage().toUpperCase())){
                        p.sendMessage(plugin.getLang().get("setup.arena.writeColor"));
                        return;
                    }
                    as.setColor(ChatColor.valueOf(e.getMessage().toUpperCase()));
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createSetupArenaMenu(p, as));
                }
            }
        }
        if (setup.getSm().isSetupKit(p)){
            if (setup.getSm().isSetupName(p)){
                e.setCancelled(true);
                KitSetup ks = setup.getSm().getSetupKit(p);
                String type = setup.getSm().getSetupName(p);
                if (ks.getKls() != null){
                    if (type.equals("kitlevelslot")){
                        int slot;
                        try {
                            slot = Integer.parseInt(e.getMessage());
                        } catch (NumberFormatException ex) {
                            p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                            return;
                        }
                        if (slot < 0){
                            p.sendMessage(plugin.getLang().get(p, "setup.kitlevel.minSlot"));
                            return;
                        }
                        if (slot > 53){
                            p.sendMessage(plugin.getLang().get(p, "setup.kitlevel.maxSlot"));
                            return;
                        }
                        KitLevelSetup kls = ks.getKls();
                        kls.setSlot(slot);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createKitLevelMenu(p, kls));
                    }
                    if (type.equals("kitlevelprice")){
                        int price;
                        try {
                            price = Integer.parseInt(e.getMessage());
                        } catch (NumberFormatException ex) {
                            p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                            return;
                        }
                        if (price < 0){
                            p.sendMessage(plugin.getLang().get(p, "setup.kitlevel.minPrice"));
                            return;
                        }
                        KitLevelSetup kls = ks.getKls();
                        kls.setPrice(price);
                        setup.getSm().removeName(p);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createKitLevelMenu(p, kls));
                    }
                }
                if (type.equals("kitslot")){
                    int slot;
                    try {
                        slot = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (slot < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.kits.minSlot"));
                        return;
                    }
                    if (slot > 53){
                        p.sendMessage(plugin.getLang().get(p, "setup.kits.maxSlot"));
                        return;
                    }
                    ks.setSlot(slot);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createKitMenu(p, ks));
                }
                if (type.equals("kitpage")){
                    int page;
                    try {
                        page = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (page < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.kits.minPage"));
                        return;
                    }
                    ks.setPage(page);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createKitMenu(p, ks));
                }
                if (type.equals("kitpermission")){
                    ks.setPermission(e.getMessage());
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createKitMenu(p, ks));
                }
            }
        }
        if (setup.getSm().isSetupTaunt(p)){
            if (setup.getSm().isSetupName(p)){
                e.setCancelled(true);
                TauntSetup ts = setup.getSm().getSetupTaunt(p);
                String type = setup.getSm().getSetupName(p);
                if (ts.getActual() != null){
                    TauntTypeSetup tts = ts.getActual();
                    if (type.equals("tauntstypeadd")){
                        tts.getMsg().add(e.getMessage());
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createTauntTypeMenu(p, tts));
                    }
                    return;
                }
                if (type.equals("tauntslot")){
                    int slot;
                    try {
                        slot = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (slot < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.taunts.minSlot"));
                        return;
                    }
                    if (slot > 53){
                        p.sendMessage(plugin.getLang().get(p, "setup.taunts.maxSlot"));
                        return;
                    }
                    ts.setSlot(slot);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createTauntMenu(p, ts));
                }
                if (type.equals("tauntpage")){
                    int page;
                    try {
                        page = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (page < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.taunts.minPage"));
                        return;
                    }
                    ts.setPage(page);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createTauntMenu(p, ts));
                }
                if (type.equals("tauntprice")){
                    int price;
                    try {
                        price = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (price < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.taunts.minPrice"));
                        return;
                    }
                    ts.setPrice(price);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createTauntMenu(p, ts));
                }
                if (type.equals("tauntpermission")){
                    ts.setPermission(e.getMessage());
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createTauntMenu(p, ts));
                }
                if (type.equals("taunttitle")){
                    ts.setTitle(e.getMessage());
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createTauntMenu(p, ts));
                }
                if (type.equals("tauntsubtitle")){
                    ts.setSubtitle(e.getMessage());
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createTauntMenu(p, ts));
                }
                if (type.equals("tauntplayer")){
                    ts.setPlayer(e.getMessage());
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createTauntMenu(p, ts));
                }
                if (type.equals("tauntnone")){
                    ts.setNone(e.getMessage());
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createTauntMenu(p, ts));
                }
            }
        }
        if (setup.getSm().isSetupChest(p)){
            if (setup.getSm().isSetupName(p)){
                e.setCancelled(true);
                ChestSetup gs = setup.getSm().getSetupChest(p);
                String type = setup.getSm().getSetupName(p);
                if (gs.getActual() != null){
                    ChestLoteSetup cls = gs.getActual();
                    if (type.equals("chestMode")){
                        if (!plugin.getGm().getModes().contains(e.getMessage())){
                            p.sendMessage(plugin.getLang().get("setup.chests.noModeChest"));
                            return;
                        }
                        if (cls.getModes().contains(e.getMessage())){
                            p.sendMessage(plugin.getLang().get("setup.chests.alreadyMode"));
                            return;
                        }
                        cls.getModes().add(e.getMessage());
                        p.sendMessage(plugin.getLang().get("setup.chests.addedChestMode").replace("<mode>", e.getMessage()));
                        setup.getSm().removeName(p);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createSetupChestAddMenu(p, gs));
                    }
                    if (type.equals("chestchance")){
                        int chance;
                        try {
                            chance = Integer.parseInt(e.getMessage());
                        } catch (NumberFormatException ex) {
                            p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                            return;
                        }
                        if (chance < 0){
                            p.sendMessage(plugin.getLang().get(p, "setup.chests.minChance"));
                            return;
                        }
                        cls.setChance(chance);
                        setup.getSm().removeName(p);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createSetupChestAddMenu(p, gs));
                    }
                }
            }
        }
        if (setup.getSm().isSetupTrail(p)){
            if (setup.getSm().isSetupName(p)){
                e.setCancelled(true);
                TrailSetup ts = setup.getSm().getSetupTrail(p);
                String type = setup.getSm().getSetupName(p);
                if (type.equals("trailspeed")){
                    int speed;
                    try {
                        speed = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (speed < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.trails.minSpeed"));
                        return;
                    }
                    ts.setSpeed(speed);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createTrailMenu(p, ts));
                }
                if (type.equals("trailoffsetx")){
                    float offsetX;
                    try {
                        offsetX = Float.parseFloat(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (offsetX < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.trails.minoffsetX"));
                        return;
                    }
                    ts.setOffsetX(offsetX);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createTrailMenu(p, ts));
                }
                if (type.equals("trailoffsety")){
                    float offsetY;
                    try {
                        offsetY = Float.parseFloat(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (offsetY < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.trails.minoffsetY"));
                        return;
                    }
                    ts.setOffsetY(offsetY);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createTrailMenu(p, ts));
                }
                if (type.equals("trailoffsetz")){
                    float offsetZ;
                    try {
                        offsetZ = Float.parseFloat(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (offsetZ < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.trails.minoffsetZ"));
                        return;
                    }
                    ts.setOffsetZ(offsetZ);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createTrailMenu(p, ts));
                }
                if (type.equals("trailpermission")){
                    ts.setPermission(e.getMessage());
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createTrailMenu(p, ts));
                }
                if (type.equals("trailamount")){
                    int amount;
                    try {
                        amount = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (amount < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.trails.minAmount"));
                        return;
                    }
                    ts.setAmount(amount);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createTrailMenu(p, ts));
                }
                if (type.equals("trailrange")){
                    double range;
                    try {
                        range = Double.parseDouble(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (range < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.trails.minRange"));
                        return;
                    }
                    ts.setRange(range);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createTrailMenu(p, ts));
                }
                if (type.equals("trailprice")){
                    int price;
                    try {
                        price = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (price < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.trails.minPrice"));
                        return;
                    }
                    ts.setPrice(price);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createTrailMenu(p, ts));
                }
                if (type.equals("trailslot")){
                    int slot;
                    try {
                        slot = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (slot < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.trails.minSlot"));
                        return;
                    }
                    if (slot > 53){
                        p.sendMessage(plugin.getLang().get(p, "setup.trails.maxSlot"));
                        return;
                    }
                    ts.setSlot(slot);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createTrailMenu(p, ts));
                }
                if (type.equals("trailpage")){
                    int page;
                    try {
                        page = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (page < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.trails.minPage"));
                        return;
                    }
                    ts.setPage(page);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createTrailMenu(p, ts));
                }
                if (type.equals("trailparticle")){
                    if (!plugin.getVc().getNMS().isParticle(e.getMessage().toUpperCase())){
                        p.sendMessage(plugin.getLang().get(p, "setup.trails.noParticle"));
                        return;
                    }
                    ts.setParticle(e.getMessage().toUpperCase());
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createTrailMenu(p, ts));
                }
                if (type.equals("trailpermission")){
                    ts.setPermission(e.getMessage());
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createTrailMenu(p, ts));
                }
            }
        }
        if (setup.getSm().isSetupBalloon(p)){
            if (setup.getSm().isSetupName(p)){
                e.setCancelled(true);
                BalloonSetup bs = setup.getSm().getSetupBalloon(p);
                String type = setup.getSm().getSetupName(p);
                if (type.equals("balloonsprice")){
                    int price;
                    try {
                        price = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (price < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.balloons.minPrice"));
                        return;
                    }
                    bs.setPrice(price);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createSetupBalloonsMenu(p, bs));
                }
                if (type.equals("balloonsslot")){
                    int slot;
                    try {
                        slot = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (slot < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.balloons.minSlot"));
                        return;
                    }
                    if (slot > 53){
                        p.sendMessage(plugin.getLang().get(p, "setup.balloons.maxSlot"));
                        return;
                    }
                    bs.setSlot(slot);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createSetupBalloonsMenu(p, bs));
                }
                if (type.equals("balloonspage")){
                    int page;
                    try {
                        page = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (page < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.balloons.minPage"));
                        return;
                    }
                    bs.setPage(page);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createSetupBalloonsMenu(p, bs));
                }
                if (type.equals("balloonspermission")){
                    bs.setPermission(e.getMessage());
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createSetupBalloonsMenu(p, bs));
                }
            }
        }
        if (setup.getSm().isSetupKillSound(p)){
            if (setup.getSm().isSetupName(p)){
                e.setCancelled(true);
                KillSoundSetup bs = setup.getSm().getSetupKillSound(p);
                String type = setup.getSm().getSetupName(p);
                if (type.equals("killsoundsprice")){
                    int price;
                    try {
                        price = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (price < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.killsounds.minPrice"));
                        return;
                    }
                    bs.setPrice(price);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createKillSoundMenu(p, bs));
                }
                if (type.equals("killsoundsslot")){
                    int slot;
                    try {
                        slot = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (slot < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.killsounds.minSlot"));
                        return;
                    }
                    if (slot > 53){
                        p.sendMessage(plugin.getLang().get(p, "setup.killsounds.maxSlot"));
                        return;
                    }
                    bs.setSlot(slot);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createKillSoundMenu(p, bs));
                }
                if (type.equals("killsoundspage")){
                    int page;
                    try {
                        page = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (page < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.balloons.minPage"));
                        return;
                    }
                    bs.setPage(page);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createKillSoundMenu(p, bs));
                }
                if (type.equals("killsoundspermission")){
                    bs.setPermission(e.getMessage());
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createKillSoundMenu(p, bs));
                }
                if (type.equals("killsoundssound")){
                    List<String> sounds = new ArrayList<>();
                    for ( Sound v : Sound.values() ){
                        sounds.add(v.name());
                    }
                    if (!sounds.contains(e.getMessage().toUpperCase())){
                        p.sendMessage(plugin.getLang().get(p, "setup.killsounds.noSound"));
                        return;
                    }
                    bs.setSound(Sound.valueOf(e.getMessage().toUpperCase()));
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createKillSoundMenu(p, bs));
                }
                if (type.equals("killsoundsvol1")){
                    float vol1;
                    try {
                        vol1 = Float.parseFloat(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (vol1 < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.killsounds.minVol1"));
                        return;
                    }
                    bs.setVol1(vol1);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createKillSoundMenu(p, bs));
                }
                if (type.equals("killsoundsvol2")){
                    float vol2;
                    try {
                        vol2 = Float.parseFloat(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (vol2 < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.killsounds.minVol2"));
                        return;
                    }
                    bs.setVol2(vol2);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createKillSoundMenu(p, bs));
                }
            }
        }
        if (setup.getSm().isSetupParting(p)){
            if (setup.getSm().isSetupName(p)){
                e.setCancelled(true);
                PartingSetup bs = setup.getSm().getSetupParting(p);
                String type = setup.getSm().getSetupName(p);
                if (type.equals("partingprice")){
                    int price;
                    try {
                        price = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (price < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.parting.minPrice"));
                        return;
                    }
                    bs.setPrice(price);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createPartingMenu(p, bs));
                }
                if (type.equals("partingslot")){
                    int slot;
                    try {
                        slot = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (slot < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.parting.minSlot"));
                        return;
                    }
                    if (slot > 53){
                        p.sendMessage(plugin.getLang().get(p, "setup.parting.maxSlot"));
                        return;
                    }
                    bs.setSlot(slot);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createPartingMenu(p, bs));
                }
                if (type.equals("partingpage")){
                    int page;
                    try {
                        page = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (page < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.balloons.minPage"));
                        return;
                    }
                    bs.setPage(page);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createPartingMenu(p, bs));
                }
                if (type.equals("partingpermission")){
                    bs.setPermission(e.getMessage());
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createPartingMenu(p, bs));
                }
                if (type.equals("partingmessage")){
                    bs.getLines().add(e.getMessage());
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createPartingMenu(p, bs));
                }
            }
        }
        if (setup.getSm().isSetupSoulWell(p)){
            if (setup.getSm().isSetupName(p)){
                e.setCancelled(true);
                SoulWellSetup bs = setup.getSm().getSetupSoulWell(p);
                SoulWellRewardSetup br = bs.getActual();
                String type = setup.getSm().getSetupName(p);
                if (type.equals("soulwellrewardrarity")){
                    List<String> rewards = new ArrayList<>();
                    for ( RewardType v : RewardType.values() ){
                        rewards.add(v.name());
                    }
                    if (!rewards.contains(e.getMessage().toUpperCase())){
                        p.sendMessage(plugin.getLang().get(p, "setup.soulwellreward.noReward"));
                        return;
                    }
                    br.setType(RewardType.valueOf(e.getMessage().toUpperCase()));
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createSoulWellRewardMenu(p, br));
                }
                if (type.equals("soulwellrewardrewards")){
                    br.getCmds().add(e.getMessage());
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createSoulWellRewardMenu(p, br));
                }
                if (type.equals("soulwellrewardname")){
                    br.setName(e.getMessage());
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createSoulWellRewardMenu(p, br));
                }
                if (type.equals("soulwellrewardchance")){
                    double chance;
                    try {
                        chance = Double.parseDouble(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (chance < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.soulwellreward.minChance"));
                        return;
                    }
                    br.setChance(chance);
                    setup.getSm().removeName(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createSoulWellRewardMenu(p, br));
                }
            }
        }
        if (setup.getSm().isChestType(p)){
            if (setup.getSm().isSetupName(p)){
                e.setCancelled(true);
                ChestTypeSetup cts = setup.getSm().getChestType(p);
                String type = setup.getSm().getSetupName(p);
                if (type.equals("chestsetupname")){
                    cts.setName(e.getMessage());
                    p.sendMessage(plugin.getLang().get(p, "setup.chesttype.type").replaceAll("<name>", e.getMessage()));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createChestSetupMenu(p, cts));
                }
                if (type.equals("chestsetupkey")){
                    cts.setKey(e.getMessage());
                    p.sendMessage(plugin.getLang().get(p, "setup.chesttype.type").replaceAll("<name>", e.getMessage()));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createChestSetupMenu(p, cts));
                }
                if (type.equals("chestsetupedit")){
                    cts.setEdit(e.getMessage());
                    p.sendMessage(plugin.getLang().get(p, "setup.chesttype.type").replaceAll("<name>", e.getMessage()));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createChestSetupMenu(p, cts));
                }
                if (type.equals("chestsetupslotsetup")){
                    int slot;
                    try {
                        slot = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (slot < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.chesttype.minSlot"));
                        return;
                    }
                    if (slot > 53){
                        p.sendMessage(plugin.getLang().get(p, "setup.chesttype.maxSlot"));
                        return;
                    }
                    cts.setSlotSetup(slot);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createChestSetupMenu(p, cts));
                }
                if (type.equals("chestsetupslotvotes")){
                    int slot;
                    try {
                        slot = Integer.parseInt(e.getMessage());
                    } catch (NumberFormatException ex) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return;
                    }
                    if (slot < 0){
                        p.sendMessage(plugin.getLang().get(p, "setup.chesttype.minSlot"));
                        return;
                    }
                    if (slot > 53){
                        p.sendMessage(plugin.getLang().get(p, "setup.chesttype.maxSlot"));
                        return;
                    }
                    cts.setSlotSetup(slot);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> setup.getSem().createChestSetupMenu(p, cts));
                }
            }
        }
    }
    
    @EventHandler
    public void onMenu(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        if (setup.getSm().isSetupInventory(p)){
            return;
        }
        if (e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)){
            return;
        }
        if (e.getView().getTitle().equals(plugin.getLang().get("menus.setupPreview.title"))){
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta()){
                return;
            }
            if (!item.getItemMeta().hasDisplayName()){
                return;
            }
            ItemMeta im = item.getItemMeta();
            String display = im.getDisplayName();
            PreviewSetup ps = setup.getSm().getSetupPreview(p);
            if (display.equals(plugin.getLang().get("menus.setupPreview.player.nameItem"))){
                ps.setPlayer(p.getLocation());
                p.sendMessage(plugin.getLang().get("setup.setPlayer"));
                setup.getSem().createPreviewMenu(p, ps);
            }
            if (display.equals(plugin.getLang().get("menus.setupPreview.cosmetic.nameItem"))){
                ps.setCosmetic(p.getLocation());
                p.sendMessage(plugin.getLang().get("setup.setCosmetic"));
                setup.getSem().createPreviewMenu(p, ps);
            }
            if (display.equals(plugin.getLang().get("menus.setupPreview.save.nameItem"))){
                if (ps.getPlayer() == null){
                    p.sendMessage(plugin.getLang().get("setup.noPlayerLocation"));
                    return;
                }
                if (ps.getCosmetic() == null){
                    p.sendMessage(plugin.getLang().get("setup.noCosmeticLocation"));
                    return;
                }
                ps.save();
                p.closeInventory();
                setup.getSm().removeSetupPreview(p);
                p.sendMessage(plugin.getLang().get("setup.savePreview"));
            }
        }
        if (e.getView().getTitle().equals(plugin.getChestType().get("lang.chests.title"))){
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta()){
                return;
            }
            if (!item.getItemMeta().hasDisplayName()){
                return;
            }
            ItemMeta im = item.getItemMeta();
            String type = NBTEditor.getString(item, "SETUPVOTECHEST");
            if (type != null){
                ChestSetup gs = new ChestSetup(plugin, type);
                if (plugin.getCtm().getChests().get(type).getChest() != null){
                    plugin.getCtm().getChests().get(type).getChest().getItems().forEach(t -> gs.addItem(new ItemSetup(t.getItem(), t.isCenter(), t.isRefill(), t.getPercent() / 100, t.getModes())));
                }
                setup.getSm().setSetupChest(p, gs);
                setup.getSem().createSetupChestTypeMenu(p, gs);
            }
        }
        if (setup.getSm().isChestType(p)){
            if (e.getView().getTitle().equals(plugin.getLang().get("menus.chestsetup.title"))){
                if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                    return;
                }
                e.setCancelled(true);
                ItemStack item = e.getCurrentItem();
                if (!item.hasItemMeta()){
                    return;
                }
                if (!item.getItemMeta().hasDisplayName()){
                    return;
                }
                ChestTypeSetup cts = setup.getSm().getChestType(p);
                ItemMeta im = item.getItemMeta();
                String display = im.getDisplayName();
                if (display.equals(plugin.getLang().get("menus.chestsetup.armorAllTeams.nameItem"))){
                    cts.setArmorAllTeams(!cts.isArmorAllTeams());
                    p.sendMessage(plugin.getLang().get(p, "setup.chesttype.setArmorAllTeam").replace("<state>", Utils.parseBoolean(cts.isArmorAllTeams())));
                    setup.getSem().createChestSetupMenu(p, cts);
                }
                if (display.equals(plugin.getLang().get("menus.chestsetup.refillChange.nameItem"))){
                    cts.setRefillChange(!cts.isRefillChange());
                    p.sendMessage(plugin.getLang().get(p, "setup.chesttype.setRefillChange").replace("<state>", Utils.parseBoolean(cts.isRefillChange())));
                    setup.getSem().createChestSetupMenu(p, cts);
                }
                if (display.equals(plugin.getLang().get("menus.chestsetup.name.nameItem"))){
                    setup.getSm().setSetupName(p, "chestsetupname");
                    p.sendMessage(plugin.getLang().get(p, "setup.chesttype.setType"));
                }
                if (display.equals(plugin.getLang().get("menus.chestsetup.key.nameItem"))){
                    setup.getSm().setSetupName(p, "chestsetupkey");
                    p.sendMessage(plugin.getLang().get(p, "setup.chesttype.setType"));
                }
                if (display.equals(plugin.getLang().get("menus.chestsetup.edit.nameItem"))){
                    setup.getSm().setSetupName(p, "chestsetupedit");
                    p.sendMessage(plugin.getLang().get(p, "setup.chesttype.setType"));
                }
                if (display.equals(plugin.getLang().get("menus.chestsetup.slotSetup.nameItem"))){
                    setup.getSm().setSetupName(p, "chestsetupslotsetup");
                    p.sendMessage(plugin.getLang().get(p, "setup.chesttype.setSlot"));
                }
                if (display.equals(plugin.getLang().get("menus.chestsetup.slotVotes.nameItem"))){
                    setup.getSm().setSetupName(p, "chestsetupslotvotes");
                    p.sendMessage(plugin.getLang().get(p, "setup.chesttype.setSlot"));
                }
                if (display.equals(plugin.getLang().get("menus.chestsetup.save.nameItem"))){
                    p.closeInventory();
                    cts.save();
                    p.sendMessage(plugin.getLang().get(p, "setup.chesttype.saved"));
                }
            }
        }
        if (setup.getSm().isSetupSoulWell(p)){
            if (e.getView().getTitle().equals(plugin.getLang().get("menus.soulwell.title"))){
                if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                    return;
                }
                e.setCancelled(true);
                ItemStack item = e.getCurrentItem();
                if (!item.hasItemMeta()){
                    return;
                }
                if (!item.getItemMeta().hasDisplayName()){
                    return;
                }
                SoulWellSetup bs = setup.getSm().getSetupSoulWell(p);
                ItemMeta im = item.getItemMeta();
                String display = im.getDisplayName();
                if (display.equals(plugin.getLang().get(p, "menus.soulwell.rewards.nameItem"))){
                    if (e.getClick().equals(ClickType.RIGHT)){
                        if (bs.getRewards().size() == 0){
                            p.sendMessage(plugin.getLang().get(p, "setup.soulwellreward.noLast"));
                            return;
                        }
                        bs.getRewards().remove(bs.getRewards().size() - 1);
                        p.sendMessage(plugin.getLang().get(p, "setup.soulwellreward.removeReward"));
                    } else {
                        if (bs.getActual() == null){
                            bs.setActual(new SoulWellRewardSetup("Default", RewardType.COMMON, 25));
                        }
                        SoulWellRewardSetup swrs = bs.getActual();
                        setup.getSem().createSoulWellRewardMenu(p, swrs);
                    }
                }
                if (display.equals(plugin.getLang().get(p, "menus.soulwell.save.nameItem"))){
                    bs.saveSoulWell(p);
                    p.closeInventory();
                    setup.getSm().removeSoulWell(p);
                }
            }
            if (e.getView().getTitle().equals(plugin.getLang().get("menus.soulwellreward.title"))){
                if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                    return;
                }
                e.setCancelled(true);
                ItemStack item = e.getCurrentItem();
                if (!item.hasItemMeta()){
                    return;
                }
                if (!item.getItemMeta().hasDisplayName()){
                    return;
                }
                SoulWellSetup bs = setup.getSm().getSetupSoulWell(p);
                SoulWellRewardSetup brs = bs.getActual();
                ItemMeta im = item.getItemMeta();
                String display = im.getDisplayName();
                if (display.equals(plugin.getLang().get(p, "menus.soulwellreward.icon.nameItem"))){
                    if (p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR)){
                        p.sendMessage(plugin.getLang().get(p, "setup.soulwellreward.noHand"));
                        return;
                    }
                    ItemStack it = p.getItemInHand();
                    if (it.hasItemMeta()){
                        ItemMeta imt = it.getItemMeta();
                        imt.setDisplayName(null);
                        imt.setLore(null);
                        it.setItemMeta(imt);
                    }
                    brs.setIcon(it);
                    p.sendMessage(plugin.getLang().get(p, "setup.soulwellreward.setIcon"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.soulwellreward.rarity.nameItem"))){
                    setup.getSm().setSetupName(p, "soulwellrewardrarity");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.soulwellreward.setRarity"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.soulwellreward.rewards.nameItem"))){
                    if (e.getClick().equals(ClickType.RIGHT)){
                        if (brs.getCmds().size() == 0){
                            p.sendMessage(plugin.getLang().get(p, "setup.soulwellreward.noLast"));
                            return;
                        }
                        brs.getCmds().remove(brs.getCmds().size() - 1);
                        p.sendMessage(plugin.getLang().get(p, "setup.soulwellreward.removeCMD"));
                    } else {
                        setup.getSm().setSetupName(p, "soulwellrewardrewards");
                        p.closeInventory();
                        p.sendMessage(plugin.getLang().get(p, "setup.soulwellreward.setRewards"));
                    }
                }
                if (display.equals(plugin.getLang().get(p, "menus.soulwellreward.chance.nameItem"))){
                    setup.getSm().setSetupName(p, "soulwellrewardchance");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.soulwellreward.setChance"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.soulwellreward.name.nameItem"))){
                    setup.getSm().setSetupName(p, "soulwellrewardname");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.soulwellreward.setName"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.soulwellreward.save.nameItem"))){
                    bs.saveSoulWellReward(p);
                    setup.getSem().createSoulWellMenu(p);
                }
            }
        }
        if (setup.getSm().isSetupParting(p)){
            if (e.getView().getTitle().equals(plugin.getLang().get("menus.parting.title"))){
                if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                    return;
                }
                e.setCancelled(true);
                ItemStack item = e.getCurrentItem();
                if (!item.hasItemMeta()){
                    return;
                }
                if (!item.getItemMeta().hasDisplayName()){
                    return;
                }
                PartingSetup bs = setup.getSm().getSetupParting(p);
                ItemMeta im = item.getItemMeta();
                String display = im.getDisplayName();
                if (display.equals(plugin.getLang().get(p, "menus.parting.icon.nameItem"))){
                    if (p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR)){
                        p.sendMessage(plugin.getLang().get(p, "setup.parting.noHand"));
                        return;
                    }
                    ItemStack it = p.getItemInHand();
                    if (it.hasItemMeta()){
                        ItemMeta imt = it.getItemMeta();
                        imt.setDisplayName(null);
                        imt.setLore(null);
                        it.setItemMeta(imt);
                    }
                    bs.setIcon(it);
                    p.sendMessage(plugin.getLang().get(p, "setup.parting.setIcon"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.parting.price.nameItem"))){
                    setup.getSm().setSetupName(p, "partingprice");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.parting.setPrice"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.parting.slot.nameItem"))){
                    setup.getSm().setSetupName(p, "partingslot");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.parting.setSlot"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.parting.page.nameItem"))){
                    setup.getSm().setSetupName(p, "partingpage");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.parting.setPage"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.parting.permission.nameItem"))){
                    setup.getSm().setSetupName(p, "partingpermission");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.parting.setPermission"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.parting.message.nameItem"))){
                    if (e.getClick().equals(ClickType.LEFT)){
                        setup.getSm().setSetupName(p, "partingmessage");
                        p.closeInventory();
                        p.sendMessage(plugin.getLang().get(p, "setup.parting.addMessage"));
                    } else {
                        bs.getLines().remove(bs.getLines().size() - 1);
                        setup.getSem().createPartingMenu(p, bs);
                    }
                }
                if (display.equals(plugin.getLang().get(p, "menus.parting.isBuy.nameItem"))){
                    bs.setBuy(!bs.isBuy());
                    p.sendMessage(plugin.getLang().get(p, "setup.parting.setBuy").replace("<state>", Utils.parseBoolean(bs.isBuy())));
                    setup.getSem().createPartingMenu(p, bs);
                }
                if (display.equals(plugin.getLang().get(p, "menus.parting.save.nameItem"))){
                    bs.saveParting(p);
                    p.closeInventory();
                    setup.getSm().removeParting(p);
                }
            }
        }
        if (setup.getSm().isSetupKillSound(p)){
            if (e.getView().getTitle().equals(plugin.getLang().get("menus.killsounds.title"))){
                if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                    return;
                }
                e.setCancelled(true);
                ItemStack item = e.getCurrentItem();
                if (!item.hasItemMeta()){
                    return;
                }
                if (!item.getItemMeta().hasDisplayName()){
                    return;
                }
                KillSoundSetup bs = setup.getSm().getSetupKillSound(p);
                ItemMeta im = item.getItemMeta();
                String display = im.getDisplayName();
                if (display.equals(plugin.getLang().get(p, "menus.killsounds.icon.nameItem"))){
                    if (p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR)){
                        p.sendMessage(plugin.getLang().get(p, "setup.killsounds.noHand"));
                        return;
                    }
                    ItemStack it = p.getItemInHand();
                    if (it.hasItemMeta()){
                        ItemMeta imt = it.getItemMeta();
                        imt.setDisplayName(null);
                        imt.setLore(null);
                        it.setItemMeta(imt);
                    }
                    bs.setIcon(it);
                    p.sendMessage(plugin.getLang().get(p, "setup.killsounds.setIcon"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.killsounds.price.nameItem"))){
                    setup.getSm().setSetupName(p, "killsoundsprice");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.killsounds.setPrice"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.killsounds.slot.nameItem"))){
                    setup.getSm().setSetupName(p, "killsoundsslot");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.killsounds.setSlot"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.killsounds.page.nameItem"))){
                    setup.getSm().setSetupName(p, "killsoundspage");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.killsounds.setPage"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.killsounds.permission.nameItem"))){
                    setup.getSm().setSetupName(p, "killsoundspermission");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.killsounds.setPermission"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.killsounds.sound.nameItem"))){
                    setup.getSm().setSetupName(p, "killsoundssound");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.killsounds.setSound"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.killsounds.vol1.nameItem"))){
                    setup.getSm().setSetupName(p, "killsoundsvol1");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.killsounds.setVol1"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.killsounds.vol2.nameItem"))){
                    setup.getSm().setSetupName(p, "killsoundsvol2");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.killsounds.setVol2"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.killsounds.isBuy.nameItem"))){
                    bs.setBuy(!bs.isBuy());
                    p.sendMessage(plugin.getLang().get(p, "setup.killsounds.setBuy").replace("<state>", Utils.parseBoolean(bs.isBuy())));
                    setup.getSem().createKillSoundMenu(p, bs);
                }
                if (display.equals(plugin.getLang().get(p, "menus.killsounds.save.nameItem"))){
                    bs.saveKillSound(p);
                    p.closeInventory();
                    setup.getSm().removeKillSound(p);
                }
            }
        }
        if (setup.getSm().isSetupBalloon(p)){
            if (e.getView().getTitle().equals(plugin.getLang().get("menus.balloons.title"))){
                if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                    return;
                }
                e.setCancelled(true);
                ItemStack item = e.getCurrentItem();
                if (!item.hasItemMeta()){
                    return;
                }
                if (!item.getItemMeta().hasDisplayName()){
                    return;
                }
                BalloonSetup bs = setup.getSm().getSetupBalloon(p);
                ItemMeta im = item.getItemMeta();
                String display = im.getDisplayName();
                if (display.equals(plugin.getLang().get(p, "menus.balloons.icon.nameItem"))){
                    if (p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR)){
                        p.sendMessage(plugin.getLang().get(p, "setup.balloons.noHand"));
                        return;
                    }
                    ItemStack it = p.getItemInHand();
                    if (it.hasItemMeta()){
                        ItemMeta imt = it.getItemMeta();
                        imt.setDisplayName(null);
                        imt.setLore(null);
                        it.setItemMeta(imt);
                    }
                    bs.setIcon(it);
                    p.sendMessage(plugin.getLang().get(p, "setup.balloons.setIcon"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.balloons.price.nameItem"))){
                    setup.getSm().setSetupName(p, "balloonsprice");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.balloons.setPrice"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.balloons.slot.nameItem"))){
                    setup.getSm().setSetupName(p, "balloonsslot");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.balloons.setSlot"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.balloons.page.nameItem"))){
                    setup.getSm().setSetupName(p, "balloonspage");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.balloons.setPage"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.balloons.permission.nameItem"))){
                    setup.getSm().setSetupName(p, "balloonspermission");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.balloons.setPermission"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.balloons.isBuy.nameItem"))){
                    bs.setBuy(!bs.isBuy());
                    p.sendMessage(plugin.getLang().get(p, "setup.balloons.setBuy").replace("<state>", Utils.parseBoolean(bs.isBuy())));
                    setup.getSem().createSetupBalloonsMenu(p, bs);
                }
                if (display.equals(plugin.getLang().get(p, "menus.balloons.save.nameItem"))){
                    bs.saveBalloon(p);
                    p.closeInventory();
                    setup.getSm().removeBalloon(p);
                }
            }
        }
        if (setup.getSm().isSetupTrail(p)){
            if (e.getView().getTitle().equals(plugin.getLang().get("menus.trails.title"))){
                if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                    return;
                }
                e.setCancelled(true);
                ItemStack item = e.getCurrentItem();
                if (!item.hasItemMeta()){
                    return;
                }
                if (!item.getItemMeta().hasDisplayName()){
                    return;
                }
                TrailSetup ts = setup.getSm().getSetupTrail(p);
                ItemMeta im = item.getItemMeta();
                String display = im.getDisplayName();
                if (display.equals(plugin.getLang().get(p, "menus.trails.icon.nameItem"))){
                    if (p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR)){
                        p.sendMessage(plugin.getLang().get(p, "setup.trails.noHand"));
                        return;
                    }
                    ItemStack it = p.getItemInHand();
                    if (it.hasItemMeta()){
                        ItemMeta imt = it.getItemMeta();
                        imt.setDisplayName(null);
                        imt.setLore(null);
                        it.setItemMeta(imt);
                    }
                    ts.setIcon(it);
                    p.sendMessage(plugin.getLang().get(p, "setup.trails.setIcon"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.trails.speed.nameItem"))){
                    setup.getSm().setSetupName(p, "trailspeed");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.trails.setSpeed"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.trails.offsetX.nameItem"))){
                    setup.getSm().setSetupName(p, "trailoffsetx");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.trails.setoffsetX"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.trails.offsetY.nameItem"))){
                    setup.getSm().setSetupName(p, "trailoffsety");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.trails.setoffsetY"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.trails.offsetZ.nameItem"))){
                    setup.getSm().setSetupName(p, "trailoffsetz");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.trails.setoffsetZ"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.trails.amount.nameItem"))){
                    setup.getSm().setSetupName(p, "trailamount");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.trails.setAmount"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.trails.range.nameItem"))){
                    setup.getSm().setSetupName(p, "trailrange");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.trails.setRange"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.trails.price.nameItem"))){
                    setup.getSm().setSetupName(p, "trailprice");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.trails.setPrice"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.trails.slot.nameItem"))){
                    setup.getSm().setSetupName(p, "trailslot");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.trails.setSlot"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.trails.page.nameItem"))){
                    setup.getSm().setSetupName(p, "trailpage");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.trails.setPage"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.trails.particle.nameItem"))){
                    setup.getSm().setSetupName(p, "trailparticle");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.trails.setParticle"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.trails.permission.nameItem"))){
                    setup.getSm().setSetupName(p, "trailpermission");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.trails.setPermission"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.trails.isBuy.nameItem"))){
                    ts.setBuy(!ts.isBuy());
                    p.sendMessage(plugin.getLang().get(p, "setup.trails.setBuy").replace("<state>", Utils.parseBoolean(ts.isBuy())));
                    setup.getSem().createTrailMenu(p, ts);
                }
                if (display.equals(plugin.getLang().get(p, "menus.trails.save.nameItem"))){
                    ts.saveTrail(p);
                    p.closeInventory();
                    setup.getSm().removeTrail(p);
                }
            }
        }
        if (setup.getSm().isSetupChest(p)){
            if (e.getClickedInventory().getType().equals(InventoryType.PLAYER)){
                return;
            }
            if (e.getView().getTitle().equals(plugin.getLang().get("menus.chestsremove.title"))){
                e.setCancelled(true);
                if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                    return;
                }
                ChestSetup gs = setup.getSm().getSetupChest(p);
                ItemStack item = e.getCurrentItem();
                if (item.hasItemMeta()){
                    if (item.getItemMeta().hasDisplayName()){
                        ItemMeta im = item.getItemMeta();
                        String display = im.getDisplayName();
                        if (display.equals(plugin.getLang().get(p, "menus.next.nameItem"))){
                            plugin.getUim().addPage(p);
                            setup.getSem().createSetupChestRemoveMenu(p, gs);
                            return;
                        }
                        if (display.equals(plugin.getLang().get(p, "menus.last.nameItem"))){
                            plugin.getUim().removePage(p);
                            setup.getSem().createSetupChestRemoveMenu(p, gs);
                            return;
                        }
                        if (display.equals(plugin.getLang().get(p, "menus.chestsremove.save.nameItem"))){
                            setup.getSem().createSetupChestTypeMenu(p, gs);
                            return;
                        }
                    }
                }
                gs.removeItem(item);
                setup.getSem().createSetupChestRemoveMenu(p, gs);
            }
            if (e.getView().getTitle().equals(plugin.getLang().get("menus.chestsadd.title"))){
                if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                    return;
                }
                ChestSetup gs = setup.getSm().getSetupChest(p);
                ItemStack item = e.getCurrentItem();
                if (!item.hasItemMeta()){
                    return;
                }
                if (!item.getItemMeta().hasDisplayName()){
                    return;
                }
                String display = item.getItemMeta().getDisplayName();
                if (display.equals(plugin.getLang().get(p, "menus.next.nameItem"))){
                    plugin.getUim().addPage(p);
                    setup.getSem().createSetupChestAddMenu(p, gs);
                    return;
                }
                if (display.equals(plugin.getLang().get(p, "menus.last.nameItem"))){
                    plugin.getUim().removePage(p);
                    setup.getSem().createSetupChestAddMenu(p, gs);
                    return;
                }
                e.setCancelled(true);
                ChestLoteSetup cls = gs.getActual();
                if (display.equals(plugin.getLang().get(p, "menus.chestsadd.chance.nameItem"))){
                    ArrayList<ItemStack> items = new ArrayList<>();
                    Inventory inv = e.getInventory();
                    for ( int i = 0; i < 45; i++ ){
                        if (inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)){
                            continue;
                        }
                        items.add(inv.getItem(i));
                    }
                    gs.setTemporarily(items);
                    setup.getSm().setSetupName(p, "chestchance");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.chests.setChance"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.chestsadd.center.nameItem"))){
                    ArrayList<ItemStack> items = new ArrayList<>();
                    Inventory inv = e.getInventory();
                    for ( int i = 0; i < 45; i++ ){
                        if (inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)){
                            continue;
                        }
                        items.add(inv.getItem(i));
                    }
                    gs.setTemporarily(items);
                    cls.setCenter(!cls.isCenter());
                    p.sendMessage(plugin.getLang().get(p, "setup.chests.setCenter").replace("<state>", Utils.parseBoolean(cls.isCenter())));
                    setup.getSem().createSetupChestAddMenu(p, gs);
                }
                if (display.equals(plugin.getLang().get(p, "menus.chestsadd.refill.nameItem"))){
                    ArrayList<ItemStack> items = new ArrayList<>();
                    Inventory inv = e.getInventory();
                    for ( int i = 0; i < 45; i++ ){
                        if (inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)){
                            continue;
                        }
                        items.add(inv.getItem(i));
                    }
                    gs.setTemporarily(items);
                    cls.setRefill(!cls.isRefill());
                    p.sendMessage(plugin.getLang().get(p, "setup.chests.setRefill").replace("<state>", Utils.parseBoolean(cls.isRefill())));
                    setup.getSem().createSetupChestAddMenu(p, gs);
                }
                if (display.equals(plugin.getLang().get(p, "menus.chestsadd.modes.nameItem"))){
                    if (e.getClick().isRightClick()){
                        if (cls.getModes().isEmpty()){
                            p.sendMessage(plugin.getLang().get("setup.chests.noLastChest"));
                            return;
                        }
                        cls.getModes().remove(cls.getModes().size() - 1);
                        p.sendMessage(plugin.getLang().get("setup.chests.removedChestMode"));
                        setup.getSem().createSetupChestAddMenu(p, gs);
                    } else {
                        ArrayList<ItemStack> items = new ArrayList<>();
                        Inventory inv = e.getInventory();
                        for ( int i = 0; i < 45; i++ ){
                            if (inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)){
                                continue;
                            }
                            items.add(inv.getItem(i));
                        }
                        gs.setTemporarily(items);
                        setup.getSm().setSetupName(p, "chestMode");
                        p.closeInventory();
                        p.sendMessage(plugin.getLang().get("setup.chests.writeChestMode"));
                    }
                }
                if (display.equals(plugin.getLang().get(p, "menus.chestsadd.save.nameItem"))){
                    gs.saveChestLote(e.getInventory());
                    setup.getSem().createSetupChestTypeMenu(p, gs);
                }
            }
            if (e.getView().getTitle().equals(plugin.getChestType().get("lang.chests.title"))){
                e.setCancelled(true);
                if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                    return;
                }
                ChestSetup gs = setup.getSm().getSetupChest(p);
                ItemStack item = e.getCurrentItem();
                if (!item.hasItemMeta()){
                    return;
                }
                if (!item.getItemMeta().hasDisplayName()){
                    return;
                }
                String display = item.getItemMeta().getDisplayName();
                if (display.equals(plugin.getLang().get(p, "menus.next.nameItem"))){
                    plugin.getUim().addPage(p);
                    setup.getSem().createSetupChestTypeMenu(p, gs);
                    return;
                }
                if (display.equals(plugin.getLang().get(p, "menus.last.nameItem"))){
                    plugin.getUim().removePage(p);
                    setup.getSem().createSetupChestTypeMenu(p, gs);
                    return;
                }
                if (display.equals(plugin.getLang().get(p, "menus.cheststype.filter.nameItem"))){
                    switch(gs.getFilter()){
                        case "NONE":
                            gs.setFilter("REFILL");
                            break;
                        case "REFILL":
                            gs.setFilter("CENTER");
                            break;
                        case "CENTER":
                            gs.setFilter("CHESTPLATE");
                            break;
                        case "CHESTPLATE":
                            gs.setFilter("LEGGINGS");
                            break;
                        case "LEGGINGS":
                            gs.setFilter("BOOTS");
                            break;
                        case "BOOTS":
                            gs.setFilter("HELMET");
                            break;
                        case "HELMET":
                            gs.setFilter("SWORD");
                            break;
                        case "SWORD":
                            gs.setFilter("NONE");
                            break;
                    }
                    setup.getSem().createSetupChestTypeMenu(p, gs);
                }
                if (display.equals(plugin.getLang().get(p, "menus.cheststype.add.nameItem"))){
                    if (gs.getActual() == null){
                        gs.setActual(new ChestLoteSetup(25, false, false, new ArrayList<>(Collections.singletonList("ALL"))));
                    }
                    setup.getSem().createSetupChestAddMenu(p, gs);
                }
                if (display.equals(plugin.getLang().get(p, "menus.cheststype.remove.nameItem"))){
                    setup.getSem().createSetupChestRemoveMenu(p, gs);
                }
                if (display.equals(plugin.getLang().get(p, "menus.cheststype.save.nameItem"))){
                    gs.saveChest(p);
                    p.closeInventory();
                    setup.getSm().removeChest(p);
                }
            }
        }
        if (setup.getSm().isSetupTaunt(p)){
            if (e.getClickedInventory().getType().equals(InventoryType.PLAYER)){
                return;
            }
            if (e.getView().getTitle().equals(plugin.getLang().get("menus.tauntstype.title"))){
                e.setCancelled(true);
                if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                    return;
                }
                ItemStack item = e.getCurrentItem();
                if (!item.hasItemMeta()){
                    return;
                }
                if (!item.getItemMeta().hasDisplayName()){
                    return;
                }
                TauntSetup ts = setup.getSm().getSetupTaunt(p);
                ItemMeta im = item.getItemMeta();
                String display = im.getDisplayName();
                if (display.equals(plugin.getLang().get(p, "menus.tauntstype.add.nameItem"))){
                    setup.getSm().setSetupName(p, "tauntstypeadd");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.tauntstype.setMessage"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.tauntstype.save.nameItem"))){
                    ts.saveTauntType(p);
                    setup.getSem().createTauntMenu(p, ts);
                }
            }
            if (e.getView().getTitle().equals(plugin.getLang().get("menus.taunts.title"))){
                e.setCancelled(true);
                if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                    return;
                }
                ItemStack item = e.getCurrentItem();
                if (!item.hasItemMeta()){
                    return;
                }
                if (!item.getItemMeta().hasDisplayName()){
                    return;
                }
                TauntSetup ts = setup.getSm().getSetupTaunt(p);
                ItemMeta im = item.getItemMeta();
                String display = im.getDisplayName();
                if (display.equals(plugin.getLang().get(p, "menus.taunts.slot.nameItem"))){
                    if (p.getItemOnCursor() == null){
                        p.sendMessage(plugin.getLang().get(p, "setup.taunts.noHand"));
                        return;
                    }
                    ItemStack it = p.getItemInHand();
                    if (it.hasItemMeta()){
                        ItemMeta imt = it.getItemMeta();
                        imt.setDisplayName(null);
                        imt.setLore(null);
                        it.setItemMeta(imt);
                    }
                    ts.setIcon(it);
                    p.sendMessage(plugin.getLang().get(p, "setup.taunts.setIcon"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.taunts.setplayer.nameItem"))){
                    setup.getSm().setSetupName(p, "tauntplayer");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.taunts.setPlayer"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.taunts.setnone.nameItem"))){
                    setup.getSm().setSetupName(p, "tauntnone");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.taunts.setNone"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.taunts.settitle.nameItem"))){
                    setup.getSm().setSetupName(p, "taunttitle");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.taunts.setTitle"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.taunts.setsubtitle.nameItem"))){
                    setup.getSm().setSetupName(p, "tauntsubtitle");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.taunts.setSubTitle"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.taunts.slot.nameItem"))){
                    setup.getSm().setSetupName(p, "tauntslot");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.taunts.setSlot"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.taunts.price.nameItem"))){
                    setup.getSm().setSetupName(p, "tauntprice");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.taunts.setPrice"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.taunts.page.nameItem"))){
                    setup.getSm().setSetupName(p, "tauntpage");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.taunts.setPage"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.taunts.permission.nameItem"))){
                    setup.getSm().setSetupName(p, "tauntpermission");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.taunts.setPermission"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.taunts.isBuy.nameItem"))){
                    ts.setBuy(!ts.isBuy());
                    p.sendMessage(plugin.getLang().get(p, "setup.taunts.setBuy").replace("<state>", Utils.parseBoolean(ts.isBuy())));
                    setup.getSem().createTauntMenu(p, ts);
                }
                List<String> s = Arrays.asList(DamageCauses.valueOf(plugin.getVc().getVersion()).getCauses());
                String d = display.replace("§e", "");
                if (s.contains(d)){
                    if (ts.getActual() != null){
                        setup.getSem().createTauntTypeMenu(p, ts.getActual());
                    } else {
                        TauntTypeSetup tts = ts.getTaunts().get(d);
                        setup.getSem().createTauntTypeMenu(p, tts);
                        ts.setActual(tts);
                    }
                }
                if (display.equals(plugin.getLang().get(p, "menus.taunts.save.nameItem"))){
                    ts.saveTaunt(p);
                    setup.getSm().removeTaunt(p);
                    p.closeInventory();
                }
            }
        }
        if (setup.getSm().isSetupKit(p)){
            if (e.getClickedInventory().getType().equals(InventoryType.PLAYER)){
                return;
            }
            if (e.getView().getTitle().equals(plugin.getLang().get("menus.kitlevel.title"))){
                e.setCancelled(true);
                if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                    return;
                }
                ItemStack item = e.getCurrentItem();
                if (!item.hasItemMeta()){
                    return;
                }
                if (!item.getItemMeta().hasDisplayName()){
                    return;
                }
                KitSetup ks = setup.getSm().getSetupKit(p);
                KitLevelSetup kls = ks.getKls();
                ItemMeta im = item.getItemMeta();
                String display = im.getDisplayName();
                if (display.equals(plugin.getLang().get(p, "menus.kitlevel.icon.nameItem"))){
                    if (p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR)){
                        p.sendMessage(plugin.getLang().get(p, "setup.kitlevel.noHand"));
                        return;
                    }
                    ItemStack it = p.getItemInHand();
                    if (it.hasItemMeta()){
                        ItemMeta imt = it.getItemMeta();
                        imt.setDisplayName(null);
                        imt.setLore(null);
                        it.setItemMeta(imt);
                    }
                    kls.setIcon(it);
                    p.sendMessage(plugin.getLang().get(p, "setup.kitlevel.setIcon"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.kitlevel.slot.nameItem"))){
                    setup.getSm().setSetupName(p, "kitlevelslot");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.kitlevel.setSlot"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.kitlevel.price.nameItem"))){
                    setup.getSm().setSetupName(p, "kitlevelprice");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.kitlevel.setPrice"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.kitlevel.items.nameItem"))){
                    if (e.getClick().equals(ClickType.RIGHT)){
                        plugin.getGem().createKitsMenu(p, kls.getInv(), kls.getArmors());
                    } else {
                        kls.setArmors(p.getInventory().getArmorContents());
                        kls.setInv(p.getInventory().getContents());
                        p.sendMessage(plugin.getLang().get(p, "setup.kitlevel.setItems"));
                    }
                }
                if (display.equals(plugin.getLang().get(p, "menus.kitlevel.isBuy.nameItem"))){
                    kls.setBuy(!kls.isBuy());
                    p.sendMessage(plugin.getLang().get(p, "setup.kits.setBuy").replace("<state>", Utils.parseBoolean(kls.isBuy())));
                    setup.getSem().createKitLevelMenu(p, kls);
                }
                if (display.equals(plugin.getLang().get(p, "menus.kitlevel.save.nameItem"))){
                    ks.saveKitLevel(p);
                    setup.getSem().createKitMenu(p, ks);
                }
            }
            if (e.getView().getTitle().equals(plugin.getLang().get("menus.kits.title"))){
                e.setCancelled(true);
                if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                    return;
                }
                ItemStack item = e.getCurrentItem();
                if (!item.hasItemMeta()){
                    return;
                }
                if (!item.getItemMeta().hasDisplayName()){
                    return;
                }
                KitSetup ks = setup.getSm().getSetupKit(p);
                ItemMeta im = item.getItemMeta();
                String display = im.getDisplayName();
                if (display.equals(plugin.getLang().get(p, "menus.kits.slot.nameItem"))){
                    setup.getSm().setSetupName(p, "kitslot");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.kits.setSlot"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.kits.page.nameItem"))){
                    setup.getSm().setSetupName(p, "kitpage");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.kits.setPage"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.kits.permission.nameItem"))){
                    setup.getSm().setSetupName(p, "kitpermission");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.kits.setPermission"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.kits.levels.nameItem"))){
                    KitLevelSetup kls;
                    if (ks.getKls() != null){
                        kls = ks.getKls();
                    } else {
                        kls = new KitLevelSetup(p);
                        ks.setKls(kls);
                    }
                    setup.getSem().createKitLevelMenu(p, kls);
                }
                if (display.equals(plugin.getLang().get(p, "menus.kits.save.nameItem"))){
                    if (ks.getLevels().size() < 1){
                        p.sendMessage(plugin.getLang().get(p, "setup.kits.notSet.noLevels"));
                        return;
                    }
                    ks.saveKit(p);
                    setup.getSm().removeKit(p);
                    p.closeInventory();
                }
            }
        }
        if (setup.getSm().isSetupEvent(p)){
            if (e.getView().getTitle().equals(plugin.getLang().get("menus.event.title"))){
                e.setCancelled(true);
                if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                    return;
                }
                ItemStack item = e.getCurrentItem();
                if (!item.hasItemMeta()){
                    return;
                }
                if (!item.getItemMeta().hasDisplayName()){
                    return;
                }
                ArenaSetup as = setup.getSm().getSetup(p);
                EventSetup es = as.getEactual();
                ItemMeta im = item.getItemMeta();
                String display = im.getDisplayName();
                if (display.equals(plugin.getLang().get(p, "menus.event.add1.nameItem"))){
                    es.addSeconds(1);
                    p.sendMessage(plugin.getLang().get(p, "setup.event.addSeconds").replaceAll("<value>", "1"));
                    setup.getSem().createEventMenu(p, es);
                }
                if (display.equals(plugin.getLang().get(p, "menus.event.add5.nameItem"))){
                    es.addSeconds(5);
                    p.sendMessage(plugin.getLang().get(p, "setup.event.addSeconds").replaceAll("<value>", "5"));
                    setup.getSem().createEventMenu(p, es);
                }
                if (display.equals(plugin.getLang().get(p, "menus.event.remove5.nameItem"))){
                    es.removeSeconds(5);
                    p.sendMessage(plugin.getLang().get(p, "setup.event.removeSeconds").replaceAll("<value>", "5"));
                    setup.getSem().createEventMenu(p, es);
                }
                if (display.equals(plugin.getLang().get(p, "menus.event.remove1.nameItem"))){
                    es.removeSeconds(1);
                    p.sendMessage(plugin.getLang().get(p, "setup.event.removeSeconds").replaceAll("<value>", "1"));
                    setup.getSem().createEventMenu(p, es);
                }
                if (display.equals(plugin.getLang().get(p, "menus.event.save.nameItem"))){
                    as.saveEvent(p);
                    setup.getSem().createEventsMenu(p, as);
                    setup.getSm().removeEvent(p);
                }
            }
        }
        if (setup.getSm().isSetupGlass(p)){
            if (e.getClickedInventory().getType().equals(InventoryType.PLAYER)){
                return;
            }
            if (e.getView().getTitle().equals(plugin.getLang().get("menus.glass.title"))){
                e.setCancelled(true);
                if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                    return;
                }
                ItemStack item = e.getCurrentItem();
                if (!item.hasItemMeta()){
                    return;
                }
                if (!item.getItemMeta().hasDisplayName()){
                    return;
                }
                GlassSetup gs = setup.getSm().getSetupGlass(p);
                ItemMeta im = item.getItemMeta();
                String display = im.getDisplayName();
                if (display.equals(plugin.getLang().get(p, "menus.glass.permission.nameItem"))){
                    setup.getSm().setSetupName(p, "permission");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.glass.setPermission"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.glass.price.nameItem"))){
                    setup.getSm().setSetupName(p, "price");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.glass.setPrice"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.glass.slot.nameItem"))){
                    setup.getSm().setSetupName(p, "slot");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.glass.setSlot"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.glass.page.nameItem"))){
                    setup.getSm().setSetupName(p, "page");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.glass.setPage"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.glass.isBuy.nameItem"))){
                    gs.setBuy(!gs.isBuy());
                    p.sendMessage(plugin.getLang().get(p, "setup.glass.setBuy").replace("<state>", Utils.parseBoolean(gs.isBuy())));
                    setup.getSem().createGlassMenu(p, gs);
                }
                if (display.equals(plugin.getLang().get(p, "menus.glass.icon.nameItem"))){
                    if (p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR)){
                        p.sendMessage(plugin.getLang().get(p, "setup.glass.noHand"));
                        return;
                    }
                    ItemStack it = p.getItemInHand();
                    if (it.hasItemMeta()){
                        ItemMeta imt = it.getItemMeta();
                        imt.setDisplayName(null);
                        imt.setLore(null);
                        it.setItemMeta(imt);
                    }
                    gs.setItem(it);
                    p.sendMessage(plugin.getLang().get(p, "setup.glass.setIcon"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.glass.save.nameItem"))){
                    gs.save(plugin, p);
                    setup.getSm().removeGlass(p);
                    p.closeInventory();
                }
            }
        }
        if (setup.getSm().isSetup(p)){
            if (e.getView().getTitle().equals(plugin.getLang().get("menus.events.title"))){
                e.setCancelled(true);
                if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                    return;
                }
                ItemStack item = e.getCurrentItem();
                if (!item.hasItemMeta()){
                    return;
                }
                if (!item.getItemMeta().hasDisplayName()){
                    return;
                }
                ArenaSetup as = setup.getSm().getSetup(p);
                ItemMeta im = item.getItemMeta();
                String display = im.getDisplayName();
                if (display.equals(plugin.getLang().get(p, "menus.events.refill.nameItem"))){
                    as.setEactual(new EventSetup("refill"));
                    setup.getSm().setSetupEvent(p, as.getEactual());
                    EventSetup es = as.getEactual();
                    setup.getSem().createEventMenu(p, es);
                    p.sendMessage(plugin.getLang().get(p, "setup.event.addRefill"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.events.final.nameItem"))){
                    as.setEactual(new EventSetup("final"));
                    EventSetup es = as.getEactual();
                    setup.getSm().setSetupEvent(p, as.getEactual());
                    setup.getSem().createEventMenu(p, es);
                    p.sendMessage(plugin.getLang().get(p, "setup.event.addFinal"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.events.remove.nameItem"))){
                    if (as.getEvents().isEmpty()){
                        p.sendMessage(plugin.getLang().get(p, "setup.event.noEvents"));
                        return;
                    }
                    as.getEvents().remove(as.getEvents().size() - 1);
                    p.sendMessage(plugin.getLang().get(p, "setup.event.removeLast"));
                    setup.getSem().createEventsMenu(p, as);
                }
            }
            if (e.getView().getTitle().equals(plugin.getLang().get("menus.island.title"))){
                e.setCancelled(true);
                if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                    return;
                }
                ItemStack item = e.getCurrentItem();
                if (!item.hasItemMeta()){
                    return;
                }
                if (!item.getItemMeta().hasDisplayName()){
                    return;
                }
                ArenaSetup as = setup.getSm().getSetup(p);
                IslandArenaSetup ias = as.getActual();
                ItemMeta im = item.getItemMeta();
                String display = im.getDisplayName();
                if (display.equals(plugin.getLang().get(p, "menus.island.spawn.nameItem"))){
                    if (e.isRightClick()){
                        if (ias.getSpawn() != null){
                            p.teleport(ias.getSpawn());
                            p.sendMessage(plugin.getLang().get(p, "setup.arena.teleportedTo").replace("<to>", "Spawn"));
                        } else {
                            p.sendMessage(plugin.getLang().get(p, "setup.arena.noSetLocation"));
                        }
                    } else {
                        ias.setSpawn(p.getLocation());
                        setup.getSem().createIslandMenu(p, ias);
                        p.sendMessage(plugin.getLang().get(p, "setup.arena.setIslandSpawn"));
                    }
                }
                if (display.equals(plugin.getLang().get(p, "menus.island.balloon.nameItem"))){
                    if (e.isRightClick()){
                        if (ias.getBalloon() != null){
                            p.teleport(ias.getBalloon());
                            p.sendMessage(plugin.getLang().get(p, "setup.arena.teleportedTo").replace("<to>", "Balloon"));
                        } else {
                            p.sendMessage(plugin.getLang().get(p, "setup.arena.noSetLocation"));
                        }
                    } else {
                        ias.setBalloon(p.getLocation());
                        setup.getSem().createIslandMenu(p, ias);
                        p.sendMessage(plugin.getLang().get(p, "setup.arena.setIslandBalloon"));
                    }
                }
                if (display.equals(plugin.getLang().get(p, "menus.island.fence.nameItem"))){
                    if (e.isRightClick()){
                        if (ias.getFence() != null){
                            p.teleport(ias.getFence());
                            p.sendMessage(plugin.getLang().get(p, "setup.arena.teleportedTo").replace("<to>", "Fence"));
                        } else {
                            p.sendMessage(plugin.getLang().get(p, "setup.arena.noSetLocation"));
                        }
                    } else {
                        ias.setFence(p.getLocation());
                        setup.getSem().createIslandMenu(p, ias);
                        p.sendMessage(plugin.getLang().get(p, "setup.arena.setIslandFence"));
                    }
                }
                if (display.equals(plugin.getLang().get(p, "menus.island.chests.nameItem"))){
                    p.closeInventory();
                    if (e.isRightClick()){
                        List<Block> chests = getNearbyBlocks(p.getLocation(), 3);
                        int i = 0;
                        for ( Block c : chests ){
                            if (!ias.isChest(c.getLocation())){
                                ias.addChest(c.getLocation());
                                i++;
                            }
                        }
                        p.sendMessage(plugin.getLang().get(p, "setup.arena.detectedChest").replace("<amount>", String.valueOf(i)));
                    } else {
                        p.getInventory().remove(plugin.getIm().getIsland());
                        p.getInventory().addItem(plugin.getIm().getIsland());
                        p.sendMessage(plugin.getLang().get(p, "setup.arena.setChest"));
                    }
                }
                if (display.equals(plugin.getLang().get(p, "menus.island.save.nameItem"))){
                    as.saveIsland(p);
                    p.closeInventory();
                }
            }
            if (e.getView().getTitle().equals(plugin.getLang().get("menus.islands.title"))){
                e.setCancelled(true);
                if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                    return;
                }
                ItemStack item = e.getCurrentItem();
                if (!item.hasItemMeta()){
                    return;
                }
                if (!item.getItemMeta().hasDisplayName()){
                    return;
                }
                ArenaSetup as = setup.getSm().getSetup(p);
                ItemMeta im = item.getItemMeta();
                String display = im.getDisplayName();
                if (NBTEditor.contains(item, "ULTRASKYWARS_ISLAND_ID")){
                    int id = NBTEditor.getInt(item, "ULTRASKYWARS_ISLAND_ID");
                    if (e.isRightClick()){
                        as.getIslands().remove(id);
                        p.sendMessage(plugin.getLang().get("setup.arena.removeIsland").replace("<#>", String.valueOf(id)));
                        setup.getSem().createIslandsMenu(p, as);
                    } else {
                        IslandArenaSetup ias = as.getIslands().get(id);
                        as.setActual(ias);
                        setup.getSem().createIslandMenu(p, ias);
                        p.sendMessage(plugin.getLang().get("setup.arena.editIsland").replace("<#>", String.valueOf(id)));
                    }
                    return;
                }
                if (display.equals(plugin.getLang().get("menus.islands.addIsland.nameItem"))){
                    as.setActual(new IslandArenaSetup(p, as.getLast()));
                    as.setLast(as.getLast() + 1);
                    IslandArenaSetup nias = as.getActual();
                    setup.getSem().createIslandMenu(p, nias);
                    p.sendMessage(plugin.getLang().get(p, "setup.arena.createdIsland"));
                }
            }
            if (e.getView().getTitle().equals(plugin.getLang().get("menus.setup.title"))){
                e.setCancelled(true);
                if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                    return;
                }
                ItemStack item = e.getCurrentItem();
                if (!item.hasItemMeta()){
                    return;
                }
                if (!item.getItemMeta().hasDisplayName()){
                    return;
                }
                ArenaSetup as = setup.getSm().getSetup(p);
                ItemMeta im = item.getItemMeta();
                String display = im.getDisplayName();
                if (display.equals(plugin.getLang().get(p, "menus.setup.min.nameItem"))){
                    setup.getSm().setSetupName(p, "min");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.arena.setMin"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.setup.ranked.nameItem"))){
                    as.setRanked(!as.isRanked());
                    p.sendMessage(plugin.getLang().get(p, "setup.arena.setRanked").replace("<state>", Utils.parseBoolean(as.isRanked())));
                    setup.getSem().createSetupArenaMenu(p, as);
                }
                if (display.equals(plugin.getLang().get(p, "menus.setup.votes.nameItem"))){
                    as.setVotes(!as.isVotes());
                    p.sendMessage(plugin.getLang().get(p, "setup.arena.setVotes").replace("<state>", Utils.parseBoolean(as.isVotes())));
                    setup.getSem().createSetupArenaMenu(p, as);
                }
                if (display.equals(plugin.getLang().get(p, "menus.setup.teamSize.nameItem"))){
                    setup.getSm().setSetupName(p, "teamsize");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.arena.setTeamSize"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.setup.borderX.nameItem"))){
                    setup.getSm().setSetupName(p, "borderX");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.arena.setBorderX"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.setup.borderZ.nameItem"))){
                    setup.getSm().setSetupName(p, "borderZ");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.arena.setBorderZ"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.setup.color.nameItem"))){
                    setup.getSm().setSetupName(p, "color");
                    p.closeInventory();
                    p.sendMessage(plugin.getLang().get(p, "setup.arena.setColor"));
                }
                if (display.equals(plugin.getLang().get(p, "menus.setup.center.nameItem"))){
                    p.closeInventory();
                    if (e.isRightClick()){
                        List<Block> chests = getNearbyBlocks(p.getLocation(), 3);
                        int i = 0;
                        for ( Block c : chests ){
                            if (!as.isCenter(c.getLocation())){
                                as.addCenter(c.getLocation());
                                i++;
                            }
                        }
                        p.sendMessage(plugin.getLang().get(p, "setup.arena.detectedChest").replace("<amount>", String.valueOf(i)));
                    } else {
                        p.getInventory().remove(plugin.getIm().getCenter());
                        p.getInventory().addItem(plugin.getIm().getCenter());
                        p.sendMessage(plugin.getLang().get(p, "setup.arena.giveWand"));
                    }
                }
                if (display.equals(plugin.getLang().get(p, "menus.setup.islands.nameItem"))){
                    IslandArenaSetup ias = as.getActual();
                    if (ias == null){
                        setup.getSem().createIslandsMenu(p, as);
                    } else {
                        setup.getSem().createIslandMenu(p, ias);
                    }
                }
                if (display.equals(plugin.getLang().get(p, "menus.setup.save.nameItem"))){
                    as.save(p, as.getName());
                }
                if (display.equals(plugin.getLang().get(p, "menus.setup.lobby.nameItem"))){
                    if (e.isRightClick()){
                        if (as.getLobby() != null){
                            p.teleport(as.getLobby());
                            p.sendMessage(plugin.getLang().get(p, "setup.arena.teleportedTo").replace("<to>", "Lobby"));
                        } else {
                            p.sendMessage(plugin.getLang().get(p, "setup.arena.noSetLocation"));
                        }
                    } else {
                        as.setLobby(p.getLocation());
                        setup.getSem().createSetupArenaMenu(p, as);
                        p.sendMessage(plugin.getLang().get(p, "setup.arena.setLobby"));
                    }
                }
                if (display.equals(plugin.getLang().get(p, "menus.setup.spect.nameItem"))){
                    if (e.isRightClick()){
                        if (as.getSpectator() != null){
                            p.teleport(as.getSpectator());
                            p.sendMessage(plugin.getLang().get(p, "setup.arena.teleportedTo").replace("<to>", "Lobby"));
                        } else {
                            p.sendMessage(plugin.getLang().get(p, "setup.arena.noSetLocation"));
                        }
                    } else {
                        as.setSpectator(p.getLocation());
                        setup.getSem().createSetupArenaMenu(p, as);
                        p.sendMessage(plugin.getLang().get(p, "setup.arena.setSpect"));
                    }
                }
                if (display.equals(plugin.getLang().get(p, "menus.setup.events.nameItem"))){
                    if (as.getEactual() != null){
                        EventSetup es = as.getEactual();
                        setup.getSem().createEventMenu(p, es);
                    } else {
                        setup.getSem().createEventsMenu(p, as);
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onClose(InventoryCloseEvent e){
        Player p = (Player) e.getPlayer();
        if (setup.getSm().isSetupInventory(p)){
            UltraInventory i = setup.getSm().getSetupInventory(p);
            plugin.getUim().setInventory(i.getName(), e.getInventory());
            setup.getSm().removeInventory(p);
            p.sendMessage(plugin.getLang().get(p, "setup.menus.finishEdit"));
        }
    }
    
    private void changeSlot(Player p){
        if (!plugin.getVc().is1_13to17()) return;
        int slot = p.getInventory().getHeldItemSlot();
        if (slot == 0){
            p.getInventory().setHeldItemSlot(slot + 1);
        }
        if (slot > 0){
            p.getInventory().setHeldItemSlot(slot - 1);
        }
    }
    
    private List<Block> getNearbyBlocks(Location location, int radius){
        List<Block> blocks = new ArrayList<>();
        for ( int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++ ){
            for ( int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++ ){
                for ( int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++ ){
                    Block block = location.getWorld().getBlockAt(x, y, z);
                    if (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST || block.getType() == Material.ENDER_CHEST){
                        blocks.add(block);
                    }
                }
            }
        }
        return blocks;
    }
    
    
}