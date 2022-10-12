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

package io.github.Leonardo0013YT.UltraSkyWars.api.objects;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.Balloon;
import io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.Glass;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PreviewCosmetic {
    
    private final Location playerLocation;
    private final String type;
    private final ArmorStand cosmetic;
    private final Location cosmeticLocation;
    private final HashMap<UUID, PreviewSession> preview = new HashMap<>();
    private ArmorStand player;
    
    public PreviewCosmetic(String type, Location playerLocation, Location cosmeticLocation){
        this.type = type;
        this.playerLocation = playerLocation;
        this.player = Utils.getArmorStand(playerLocation);
        this.cosmetic = Utils.getArmorStand(cosmeticLocation);
        this.cosmeticLocation = cosmeticLocation;
    }
    
    public void addPreview(Player p){
        if (player == null || player.isDead()){
            this.player = Utils.getArmorStand(playerLocation);
        }
        preview.put(p.getUniqueId(), new PreviewSession(0, p));
        p.teleport(player);
        p.closeInventory();
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.setAllowFlight(true);
        p.setFlying(true);
        p.setFlySpeed(0f);
        p.setWalkSpeed(0f);
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 999999));
        new BukkitRunnable() {
            @Override
            public void run(){
                UltraSkyWarsApi.get().getCos().getPlayerPreview().remove(p.getUniqueId());
                removePreview(p);
            }
        }.runTaskLater(UltraSkyWarsApi.get(), 5 * 20);
    }
    
    public void removePreview(Player p){
        PreviewSession ps = preview.get(p.getUniqueId());
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        if (type.equals("balloon")){
            plugin.getVc().getNMS().destroy(p, ps.getEntityId());
        }
        if (type.equals("glass")){
            Glass glass = plugin.getCos().getGlass(ps.getId());
            for ( Map.Entry<Vector, GlassBlock> entry : glass.getPreview().entrySet() ){
                Vector v = entry.getKey();
                p.sendBlockChange(cosmeticLocation.clone().add(v), Material.AIR, (byte) 0);
            }
        }
        ps.reset();
        preview.remove(p.getUniqueId());
    }
    
    public void execute(Player p, int id){
        PreviewSession ps = preview.get(p.getUniqueId());
        ps.setId(id);
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        if (type.equals("glass")){
            Glass glass = plugin.getCos().getGlass(id);
            if (glass.getPreview().isEmpty()){
                glass.setPreview(plugin.getWc().getEdit().getBlocks(glass.getSchematic()));
            }
            for ( Map.Entry<Vector, GlassBlock> entry : glass.getPreview().entrySet() ){
                Vector v = entry.getKey();
                GlassBlock g = entry.getValue();
                p.sendBlockChange(cosmeticLocation.clone().add(v), g.getMaterial(), (byte) g.getData());
            }
        }
        if (type.equals("balloon")){
            Balloon balloon = plugin.getCos().getBalloon(id);
            plugin.getVc().getNMS().spawn(p, cosmeticLocation, balloon.getActualHead()).forEach(ps::addEntityId);
        }
    }
    
}