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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.mobfriends.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.USWGameQuitEvent;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.mobfriends.friends.FriendSession;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.api.team.Team;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MobFriendsListener implements Listener {
    
    private final HashMap<Integer, HashMap<Integer, FriendSession>> friends = new HashMap<>();
    private final UltraSkyWarsApi plugin;
    
    public MobFriendsListener(UltraSkyWarsApi plugin){
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerQuit(USWGameQuitEvent e){
        Player p = e.getPlayer();
        Game game = e.getGame();
        Team t = game.getTeamPlayer(p);
        if (t != null && t.getTeamSize() <= 1 && friends.containsKey(game.getId()) && friends.get(game.getId()).containsKey(t.getId())){
            friends.get(t.getGame().getId()).remove(t.getId()).clear();
        }
    }
    
    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR))
            return;
        if (UltraSkyWarsApi.isPlayerGame(p)){
            ItemStack item = p.getItemInHand();
            if (item.getType().name().contains("POTION")){
                return;
            }
            Team t = checkTeam(p);
            if (t == null) return;
            XMaterial m = XMaterial.matchXMaterial(item);
            if (m.name().endsWith("SPAWN_EGG")){
                friends.putIfAbsent(t.getGame().getId(), new HashMap<>());
                friends.get(t.getGame().getId()).putIfAbsent(t.getId(), new FriendSession());
                int amount = 0;
                for ( Entity ent : new ArrayList<>(friends.get(t.getGame().getId()).get(t.getId()).getEntities()) ){
                    if (ent == null || ent.isDead()) continue;
                    if (ent.getLocation().getY() <= 0){
                        friends.get(t.getGame().getId()).get(t.getId()).getEntities().remove(ent);
                        ent.remove();
                        continue;
                    }
                    amount++;
                }
                if (amount >= plugin.getCm().getMaxMobFriends()){
                    p.sendMessage(plugin.getLang().get("messages.maxMobFriends").replaceAll("<max>", String.valueOf(plugin.getCm().getMaxMobFriends())));
                    return;
                }
                String entName = item.getType().name().replaceFirst("_SPAWN_EGG", "");
                if (entName.equals("ZOMBIE_PIGMAN")){
                    entName = "PIG_ZOMBIE";
                }
                Location loc = e.getClickedBlock().getRelative(e.getBlockFace()).getLocation().add(0.5, 0, 0.5);
                int dt = item.getData().getData();
                Entity entity = (plugin.getVc().is1_13to17() ? p.getWorld().spawnEntity(loc, EntityType.valueOf(entName)) : spawnEntity(loc, dt));
                entity.setCustomName(plugin.getLang().get(p, "mobTitle").replaceAll("<name>", p.getName()));
                entity.setCustomNameVisible(true);
                entity.setMetadata("OWNER", new FixedMetadataValue(plugin, p.getUniqueId().toString()));
                friends.get(t.getGame().getId()).get(t.getId()).getEntities().add(entity);
                removeItemInHand(p);
            }
        }
    }
    
    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Location to = e.getTo();
        Location from = e.getFrom();
        if (to.getBlockX() != from.getBlockX() || to.getBlockY() != from.getBlockY() || to.getBlockZ() != from.getBlockZ()){
            Player p = e.getPlayer();
            Team t = checkTeam(p);
            if (t == null) return;
            if (!friends.containsKey(t.getGame().getId())){
                return;
            }
            if (!friends.get(t.getGame().getId()).containsKey(t.getId())){
                return;
            }
            friends.get(t.getGame().getId()).get(t.getId()).getEntities().forEach(ent -> {
                LivingEntity entity = (LivingEntity) ent;
                if (!entity.getWorld().equals(p.getWorld())) return;
                if (entity.getLocation().distance(p.getLocation()) < 10){
                    plugin.getVc().getNMS().followPlayer(p, entity, 1.5);
                } else {
                    entity.setNoDamageTicks(20);
                    entity.teleport(p);
                }
            });
        }
    }
    
    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        remove(e.getEntity());
    }
    
    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        remove(e.getPlayer());
    }
    
    @EventHandler
    public void onKick(PlayerKickEvent e){
        remove(e.getPlayer());
    }
    
    private void remove(Player p){
        Team t = checkTeam(p);
        if (t == null) return;
        if (friends.containsKey(t.getGame().getId()) && friends.get(t.getGame().getId()).containsKey(t.getId())){
            if (t.getTeamSize() <= 0){
                friends.get(t.getGame().getId()).remove(t.getId()).clear();
            }
        }
    }
    
    @EventHandler
    public void onCombust(EntityCombustEvent e){
        if (e.getEntity().hasMetadata("OWNER")){
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        if (e.getEntity().hasMetadata("OWNER")){
            UUID uuid = UUID.fromString(e.getEntity().getMetadata("OWNER").get(0).asString());
            Player p = Bukkit.getPlayer(uuid);
            Team t = checkTeam(p);
            if (t == null){
                return;
            }
            if (friends.containsKey(t.getGame().getId()) && friends.get(t.getGame().getId()).containsKey(t.getId())){
                friends.get(t.getGame().getId()).get(t.getId()).getEntities().remove(e.getEntity());
            }
        }
    }
    
    @EventHandler
    public void onTarget(EntityTargetEvent e){
        if (e.getTarget() instanceof Player && e.getEntity().hasMetadata("OWNER")){
            Player p = (Player) e.getTarget();
            UUID uuid = UUID.fromString(e.getEntity().getMetadata("OWNER").get(0).asString());
            if (p.getUniqueId().equals(uuid)){
                e.setCancelled(true);
                return;
            }
            Team t = checkTeam(p);
            if (t == null) return;
            if (t.getMembers().contains(p) || UltraSkyWarsApi.isSpectator(p)){
                e.setCancelled(true);
            }
        }
    }
    
    public Team checkTeam(Player p){
        if (p == null) return null;
        Game game = plugin.getGm().getGameByPlayer(p);
        if (game == null) return null;
        return game.getTeamPlayer(p);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent e){
        if (e.isCancelled()){
            return;
        }
        if (e.getDamager() instanceof Player){
            Player d = (Player) e.getDamager();
            Team td = checkTeam(d);
            if (td == null) return;
            if (e.getEntity() instanceof Player){
                Player p = (Player) e.getEntity();
                Team tp = checkTeam(p);
                if (tp == null) return;
                target(p, td);
                target(d, tp);
            } else {
                friends.putIfAbsent(td.getGame().getId(), new HashMap<>());
                FriendSession fd = friends.get(td.getGame().getId()).get(td.getId());
                if (fd != null){
                    if (fd.getEntities().contains(e.getEntity())){
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
    
    private void target(Player d, Team tp){
        friends.putIfAbsent(tp.getGame().getId(), new HashMap<>());
        FriendSession fp = friends.get(tp.getGame().getId()).get(tp.getId());
        if (fp != null){
            fp.getEntities().forEach(end -> {
                if (end instanceof Monster){
                    Monster m = (Monster) end;
                    m.setTarget(d);
                }
            });
        }
    }
    
    private void removeItemInHand(Player p){
        if (p.getItemInHand() != null){
            ItemStack item = p.getItemInHand();
            if (item.getAmount() > 1){
                item.setAmount(item.getAmount() - 1);
            } else {
                p.setItemInHand(null);
            }
        }
    }
    
    public Entity spawnEntity(Location spawn, int data){
        if (data == 68 || data == 4){
            Guardian guardian = spawn.getWorld().spawn(spawn, Guardian.class);
            if (data == 4){
                guardian.setElder(true);
            }
            return guardian;
        } else if (data == 5 || data == 51){
            Skeleton guardian = spawn.getWorld().spawn(spawn, Skeleton.class);
            if (data == 5){
                guardian.setSkeletonType(Skeleton.SkeletonType.WITHER);
            }
            return guardian;
        } else if (data == 27){
            Zombie zombie = spawn.getWorld().spawn(spawn, Zombie.class);
            zombie.setVillager(true);
            return zombie;
        } else if (data == 28 || data == 29 || data == 32){
            Horse zombie = spawn.getWorld().spawn(spawn, Horse.class);
            if (data == 28){
                zombie.setVariant(Horse.Variant.SKELETON_HORSE);
            } else if (data == 29){
                zombie.setVariant(Horse.Variant.UNDEAD_HORSE);
            } else {
                zombie.setVariant(Horse.Variant.MULE);
            }
            return zombie;
        } else if (data == 50){
            return spawn.getWorld().spawn(spawn, Creeper.class);
        } else if (data == 52){
            return spawn.getWorld().spawn(spawn, Spider.class);
        } else if (data == 54){
            return spawn.getWorld().spawn(spawn, Zombie.class);
        } else if (data == 55){
            return spawn.getWorld().spawn(spawn, Slime.class);
        } else if (data == 56){
            return spawn.getWorld().spawn(spawn, Ghast.class);
        } else if (data == 57){
            return spawn.getWorld().spawn(spawn, PigZombie.class);
        } else if (data == 58){
            return spawn.getWorld().spawn(spawn, Enderman.class);
        } else if (data == 59){
            return spawn.getWorld().spawn(spawn, CaveSpider.class);
        } else if (data == 60){
            return spawn.getWorld().spawn(spawn, Silverfish.class);
        } else if (data == 61){
            return spawn.getWorld().spawn(spawn, Blaze.class);
        } else if (data == 62){
            return spawn.getWorld().spawn(spawn, MagmaCube.class);
        } else if (data == 65){
            return spawn.getWorld().spawn(spawn, Bat.class);
        } else if (data == 66){
            return spawn.getWorld().spawn(spawn, Witch.class);
        } else if (data == 95){
            return spawn.getWorld().spawn(spawn, Wolf.class);
        }
        return spawn.getWorld().spawn(spawn, Zombie.class);
    }
    
}
