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

package io.github.Leonardo0013YT.UltraSkyWars.support.version.v1_18_R2;

import com.mojang.datafixers.util.Pair;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.DamageCauses;
import io.github.Leonardo0013YT.UltraSkyWars.api.nms.NewNMS;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutAttachEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectList;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.ambient.EntityBat;
import net.minecraft.world.entity.boss.enderdragon.EntityEnderDragon;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.entity.monster.EntityGiantZombie;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public class v1_18_R2 extends NewNMS {
    
    @Override
    public Vehicle spawnHorse(Location loc, Player p){
        SkeletonHorse horse = loc.getWorld().spawn(loc, SkeletonHorse.class);
        AbstractHorseInventory inv = horse.getInventory();
        inv.setSaddle(new ItemStack(Material.SADDLE));
        horse.setOwner(p);
        horse.setDomestication(horse.getMaxDomestication());
        horse.setAdult();
        return horse;
    }
    
    @Override
    public Collection<Integer> spawn(Player p, Location loc, ItemStack head){
        EntityGiantZombie ev = new EntityGiantZombie(EntityTypes.G, (World) loc.getWorld());
        EntityArmorStand ar = new EntityArmorStand(EntityTypes.c, (World) loc.getWorld());
        EntityBat bat = new EntityBat(EntityTypes.f, (World) loc.getWorld());
        Location l = loc.clone().add(-2, 9, 3.5);
        ar.a(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        ev.a(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        bat.a(l.getX(), l.getY(), l.getZ(), 0, 0);
        ar.j(true);
        ev.j(true);
        ev.addEffect(new MobEffect(MobEffectList.a(14), 1000, 1000), EntityPotionEffectEvent.Cause.PLUGIN);
        bat.j(true);
        List<Pair<EnumItemSlot, net.minecraft.world.item.ItemStack>> equipmentList = new ArrayList<>();
        PacketPlayOutEntityEquipment equipment = new PacketPlayOutEntityEquipment(ev.ae(), equipmentList);
        PacketPlayOutSpawnEntityLiving ent = new PacketPlayOutSpawnEntityLiving(ev);
        PacketPlayOutSpawnEntityLiving ent2 = new PacketPlayOutSpawnEntityLiving(ar);
        PacketPlayOutSpawnEntityLiving ent3 = new PacketPlayOutSpawnEntityLiving(bat);
        PacketPlayOutAttachEntity attach = new PacketPlayOutAttachEntity(bat, ar);
        ServerPlayerConnection pc = ((CraftPlayer) p).getHandle().b;
        pc.a(ent);
        pc.a(ent2);
        pc.a(ent3);
        pc.a(attach);
        pc.a(equipment);
        return Arrays.asList(ev.ae(), ar.ae(), bat.ae());
    }
    
    @Override
    public void destroy(Player p, Collection<Integer> id){
        for ( int i : id ){
            PacketPlayOutEntityDestroy ent = new PacketPlayOutEntityDestroy(i);
            ((CraftPlayer) p).getHandle().b.a(ent);
        }
    }
    
    @Override
    public void followPlayer(Player player, LivingEntity entity, double d){
        float f = (float) d;
        ((EntityInsentient) ((CraftEntity) entity).getHandle()).D().a(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), f);
    }
    
    @Override
    public void displayParticle(Player p, Location location, float offsetX, float offsetY, float offsetZ, int speed, String enumParticle, int amount){
        if (location.getWorld() == null) return;
        location.getWorld().spawnParticle(Particle.valueOf(enumParticle), location, amount, offsetX, offsetY, offsetZ, speed);
    }
    
    @Override
    public void broadcastParticle(Location location, float offsetX, float offsetY, float offsetZ, int speed, String enumParticle, int amount, double range){
        if (location.getWorld() == null) return;
        location.getWorld().spawnParticle(Particle.valueOf(enumParticle), location, amount, offsetX, offsetY, offsetZ, speed);
    }
    
    @Override
    public boolean isParticle(String particle){
        try {
            Particle.valueOf(particle);
        } catch (EnumConstantNotPresentException | IllegalArgumentException e) {
            return false;
        }
        return true;
    }
    
    public void sendPacket(Player player, Object packet){
        try {
            EntityPlayer cp = (EntityPlayer) getHandle.invoke(player);
            cp.b.a((Packet<?>) packet);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    
    public void setCollidesWithEntities(Player p, boolean bol){
        p.setCollidable(bol);
    }
    
    public DamageCauses getCauses(){
        return causes;
    }
    
    public void freezeMob(LivingEntity mob){
        if (!version.equals("v1_8_R3")){
            mob.setAI(false);
        }
    }
    
    public void sendActionBar(String msg, Player... players){
        sendActionBar(msg, Arrays.asList(players));
    }
    
    public void sendActionBar(String msg, Collection<Player> players){
        BaseComponent[] text = new ComponentBuilder(msg).create();
        for ( Player p : players ){
            if (p == null || !p.isOnline()) continue;
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, text);
        }
    }
    
    public void moveDragon(Entity ent, double x, double y, double z, float yaw, float pitch){
        if (ent == null) return;
        try {
            ((EntityEnderDragon) getHandleEntity.invoke(ent)).b(x, y, z, yaw, pitch);
        } catch (Exception ignored) {
        }
    }
    
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut, Player... players){
        sendTitle(title, subtitle, fadeIn, stay, fadeOut, Arrays.asList(players));
    }
    
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut, Collection<Player> players){
        for ( Player p : players ){
            if (p == null || !p.isOnline()) continue;
            try {
                sendTitle.invoke(p, title, subtitle, fadeIn, stay, fadeOut);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}