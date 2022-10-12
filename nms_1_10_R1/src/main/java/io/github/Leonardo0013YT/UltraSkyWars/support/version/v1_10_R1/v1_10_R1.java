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

package io.github.Leonardo0013YT.UltraSkyWars.support.version.v1_10_R1;

import io.github.Leonardo0013YT.UltraSkyWars.api.enums.DamageCauses;
import io.github.Leonardo0013YT.UltraSkyWars.api.nms.OldNMS;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.minecraft.server.v1_10_R1.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;

@SuppressWarnings("unused")
public class v1_10_R1 extends OldNMS {
    
    @Override
    public Horse spawnHorse(Location loc, Player p){
        Horse horse = loc.getWorld().spawn(loc, Horse.class);
        horse.setVariant(Horse.Variant.SKELETON_HORSE);
        horse.setOwner(p);
        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        horse.setDomestication(horse.getMaxDomestication());
        horse.setAdult();
        return horse;
    }
    
    @Override
    public Collection<Integer> spawn(Player p, Location loc, ItemStack head){
        EntityGiantZombie ev = new EntityGiantZombie(((CraftWorld) loc.getWorld()).getHandle());
        EntityArmorStand ar = new EntityArmorStand(((CraftWorld) loc.getWorld()).getHandle());
        EntityBat bat = new EntityBat(((CraftWorld) loc.getWorld()).getHandle());
        Location l = loc.clone().add(-2, 9, 3.5);
        ar.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        ev.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        bat.setLocation(l.getX(), l.getY(), l.getZ(), 0, 0);
        ar.setInvisible(true);
        ev.setInvisible(true);
        bat.setInvisible(true);
        PacketPlayOutEntityEquipment equipment = new PacketPlayOutEntityEquipment(ev.getId(), EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(head));
        PacketPlayOutSpawnEntityLiving ent = new PacketPlayOutSpawnEntityLiving(ev);
        PacketPlayOutSpawnEntityLiving ent2 = new PacketPlayOutSpawnEntityLiving(ar);
        PacketPlayOutSpawnEntityLiving ent3 = new PacketPlayOutSpawnEntityLiving(bat);
        PacketPlayOutAttachEntity attach = new PacketPlayOutAttachEntity(bat, ar);
        PlayerConnection pc = ((CraftPlayer) p).getHandle().playerConnection;
        pc.sendPacket(ent);
        pc.sendPacket(ent2);
        pc.sendPacket(ent3);
        pc.sendPacket(attach);
        pc.sendPacket(equipment);
        return Arrays.asList(ev.getId(), ar.getId(), bat.getId());
    }
    
    @Override
    public void destroy(Player p, Collection<Integer> id){
        for ( int i : id ){
            PacketPlayOutEntityDestroy ent = new PacketPlayOutEntityDestroy(i);
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(ent);
        }
    }
    
    @Override
    public void followPlayer(Player player, LivingEntity entity, double d){
        float f = (float) d;
        ((EntityInsentient) ((CraftEntity) entity).getHandle()).getNavigation().a(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), f);
    }
    
    @Override
    public void displayParticle(Player player, Location location, float offsetX, float offsetY, float offsetZ, int speed, String enumParticle, int amount){
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.valueOf(enumParticle), false, (float) location.getX(), (float) location.getY(), (float) location.getZ(), offsetX, offsetY, offsetZ, speed, amount);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
    
    @Override
    public void broadcastParticle(Location location, float offsetX, float offsetY, float offsetZ, int speed, String enumParticle, int amount, double range){
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.valueOf(enumParticle), false, (float) location.getX(), (float) location.getY(), (float) location.getZ(), offsetX, offsetY, offsetZ, speed, amount);
        if (location.getWorld() == null) return;
        Collection<Entity> ents;
        try {
            ents = location.getWorld().getNearbyEntities(location, range, range, range);
        } catch (Exception ignored) {
            return;
        }
        ents.removeIf(e -> !e.getType().equals(EntityType.PLAYER));
        for ( Entity p : ents ){
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
    }
    
    @Override
    public boolean isParticle(String particle){
        try {
            EnumParticle.valueOf(particle);
        } catch (EnumConstantNotPresentException | IllegalArgumentException e) {
            return false;
        }
        return true;
    }
    
    public void sendPacket(Player player, Object object){
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object connection = handle.getClass().getField("playerConnection").get(handle);
            connection.getClass().getMethod("sendPacket", packet).invoke(connection, object);
        } catch (Exception e) {
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
        mob.setAI(false);
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
            Object handle = ent.getClass().getMethod("getHandle").invoke(ent);
            position.invoke(handle, x, y, z, yaw, pitch);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut, Player... players){
        sendTitle(title, subtitle, fadeIn, stay, fadeOut, Arrays.asList(players));
    }
    
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut, Collection<Player> players){
        try {
            Object titleC = a.invoke(null, "{\"text\": \"" + title + "\"}");
            Object subtitleC = a.invoke(null, "{\"text\": \"" + subtitle + "\"}");
            Object timesPacket = packetPlayOutTimes.newInstance(enumTimes, null, fadeIn, stay, fadeOut);
            Object titlePacket = packetPlayOutTitle.newInstance(enumTitle, titleC);
            Object subtitlePacket = packetPlayOutTitle.newInstance(enumSubtitle, subtitleC);
            for ( Player p : players ){
                if (p == null || !p.isOnline()) continue;
                sendPacket(p, timesPacket);
                sendPacket(p, titlePacket);
                sendPacket(p, subtitlePacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}