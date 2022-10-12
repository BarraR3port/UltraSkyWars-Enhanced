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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.cubelets.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.cubelets.InjectionCubelets;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.cubelets.cubelets.Cubelets;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CubeletsListener implements Listener {
    
    private final UltraSkyWarsApi plugin;
    private final InjectionCubelets injectionCubelets;
    
    public CubeletsListener(UltraSkyWarsApi plugin, InjectionCubelets injectionCubelets){
        this.plugin = plugin;
        this.injectionCubelets = injectionCubelets;
    }
    
    @EventHandler
    public void onInteractAtEntity(PlayerInteractAtEntityEvent e){
        Player p = e.getPlayer();
        if (e.getRightClicked() instanceof ArmorStand){
            if (injectionCubelets.getEntities().contains(e.getRightClicked())){
                e.setCancelled(true);
            }
            return;
        }
        Entity b = e.getRightClicked();
        if (plugin.getCm().isCubeletsEnabled()){
            if (injectionCubelets.getCbm().getCubelets().containsKey(b.getLocation())){
                if (!plugin.getGm().isPlayerInGame(p)){
                    if (plugin.getLvl().isEmpty()){
                        p.sendMessage(plugin.getLang().get(p, "messages.noRewards"));
                        e.setCancelled(true);
                        return;
                    }
                    SWPlayer pl = plugin.getDb().getSWPlayer(p);
                    if (pl.getCubelets() <= 0){
                        e.setCancelled(true);
                        p.sendMessage(plugin.getLang().get(p, "messages.noCubelets"));
                        return;
                    }
                    Cubelets sw = injectionCubelets.getCbm().getCubelets().get(b.getLocation());
                    if (sw.isInUse() || (sw.getNow() != null && !sw.getNow().equals(p))){
                        e.setCancelled(true);
                        p.sendMessage(plugin.getLang().get(p, "messages.cubeletsUse"));
                        return;
                    }
                    e.setCancelled(true);
                    pl.removeCubelets(1);
                    Utils.updateSB(p);
                    sw.setNow(p);
                    sw.setInUse(true);
                    sw.execute();
                }
            }
        }
    }
    
    @EventHandler
    public void onMenu(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.cubelets.title"))){
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)){
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
            if (display.equals(plugin.getLang().get(p, "menus.cubelets.fireworks.nameItem"))){
                if (!p.hasPermission(injectionCubelets.getCubelets().get(null, "animations.fireworks.perm"))){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                int id = injectionCubelets.getCubelets().getInt("animations.fireworks.id");
                SWPlayer sw = plugin.getDb().getSWPlayer(p);
                if (sw.getCubeAnimation() == id){
                    p.sendMessage(plugin.getLang().get(p, "messages.alreadySelect"));
                } else {
                    sw.setCubeAnimation(id);
                    p.sendMessage(plugin.getLang().get(p, "messages.selected"));
                    plugin.getGem().createCubeletsAnimationMenu(p);
                }
            }
            if (display.equals(plugin.getLang().get(p, "menus.cubelets.head.nameItem"))){
                if (!p.hasPermission(injectionCubelets.getCubelets().get(null, "animations.head.perm"))){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                int id = injectionCubelets.getCubelets().getInt("animations.head.id");
                SWPlayer sw = plugin.getDb().getSWPlayer(p);
                if (sw.getCubeAnimation() == id){
                    p.sendMessage(plugin.getLang().get(p, "messages.alreadySelect"));
                } else {
                    sw.setCubeAnimation(id);
                    p.sendMessage(plugin.getLang().get(p, "messages.selected"));
                    plugin.getGem().createCubeletsAnimationMenu(p);
                }
            }
            if (display.equals(plugin.getLang().get(p, "menus.cubelets.flames.nameItem"))){
                if (!p.hasPermission(injectionCubelets.getCubelets().get(null, "animations.flames.perm"))){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                int id = injectionCubelets.getCubelets().getInt("animations.flames.id");
                SWPlayer sw = plugin.getDb().getSWPlayer(p);
                if (sw.getCubeAnimation() == id){
                    p.sendMessage(plugin.getLang().get(p, "messages.alreadySelect"));
                } else {
                    sw.setCubeAnimation(id);
                    p.sendMessage(plugin.getLang().get(p, "messages.selected"));
                    plugin.getGem().createCubeletsAnimationMenu(p);
                }
            }
        }
    }
    
    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.PHYSICAL)){
            return;
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
            Block b = e.getClickedBlock();
            if (plugin.getCm().isCubeletsEnabled()){
                if (injectionCubelets.getCbm().getCubelets().containsKey(b.getLocation())){
                    if (!plugin.getGm().isPlayerInGame(p)){
                        if (plugin.getLvl().isEmpty()){
                            p.sendMessage(plugin.getLang().get(p, "messages.noRewards"));
                            e.setCancelled(true);
                            return;
                        }
                        SWPlayer pl = plugin.getDb().getSWPlayer(p);
                        if (pl.getCubelets() <= 0){
                            e.setCancelled(true);
                            p.sendMessage(plugin.getLang().get(p, "messages.noCubelets"));
                            return;
                        }
                        Cubelets sw = injectionCubelets.getCbm().getCubelets().get(b.getLocation());
                        if (sw.isInUse() || (sw.getNow() != null && !sw.getNow().equals(p))){
                            e.setCancelled(true);
                            p.sendMessage(plugin.getLang().get(p, "messages.cubeletsUse"));
                            return;
                        }
                        e.setCancelled(true);
                        pl.removeCubelets(1);
                        Utils.updateSB(p);
                        sw.setNow(p);
                        sw.setInUse(true);
                        sw.execute();
                    }
                }
            }
        }
    }
    
}