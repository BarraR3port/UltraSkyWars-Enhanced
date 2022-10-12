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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.challenges.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.State;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.USWGameWinEvent;
import io.github.Leonardo0013YT.UltraSkyWars.api.game.GamePlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.challenges.InjectionChallenges;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.challenges.enums.ChallengeType;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.challenges.events.ArmorEquipEvent;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.api.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ChallengeListener implements Listener {
    
    private final UltraSkyWarsApi plugin;
    private final InjectionChallenges ic;
    
    public ChallengeListener(UltraSkyWarsApi plugin, InjectionChallenges ic){
        this.plugin = plugin;
        this.ic = ic;
    }
    
    @EventHandler
    public void onFinish(USWGameWinEvent e){
        Game game = e.getGame();
        Team team = e.getWinner();
        if (team != null){
            for ( Player p : team.getMembers() ){
                if (p == null || !p.isOnline()) continue;
                GamePlayer gp = game.getGamePlayer().get(p.getUniqueId());
                if (gp == null) continue;
                SWPlayer sw = plugin.getDb().getSWPlayer(p);
                if (sw == null) continue;
                if (!gp.getChallenges().isEmpty()){
                    p.sendMessage(plugin.getLang().get("messages.challenges.completedChallenge").replace("<player>", p.getName()).replace("<challenges>", getChallenges(gp)));
                }
                gp.getChallenges().forEach(c -> {
                    sw.addChallengeCompleted(c);
                    ChallengeType type = ChallengeType.valueOf(c);
                    if (type.isOnWinEnabled()){
                        type.getOnWinCommands().forEach(cmd -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("<player>", p.getName())));
                    }
                });
            }
        }
    }
    
    @EventHandler
    public void onMenu(InventoryClickEvent e){
        if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getSlotType().equals(InventoryType.SlotType.OUTSIDE))
            return;
        if (e.getView().getTitle().equals(plugin.getLang().get("menus.challenges.title"))){
            Player p = (Player) e.getWhoClicked();
            ItemStack item = e.getCurrentItem();
            if (!plugin.getGm().isPlayerInGame(p)) return;
            Game game = plugin.getGm().getGameByPlayer(p);
            GamePlayer gp = game.getGamePlayer().get(p.getUniqueId());
            String d = item.getItemMeta().getDisplayName();
            e.setCancelled(true);
            if (d.equals(plugin.getLang().get("menus.challenges.uhc.nameItem"))){
                if (gp.hasChallenge("UHC")){
                    gp.removeChallenge("UHC");
                    p.sendMessage(plugin.getLang().get("messages.challenges.disabled").replace("<challenge>", d));
                } else {
                    gp.addChallenge("UHC");
                    p.sendMessage(plugin.getLang().get("messages.challenges.enabled").replace("<challenge>", d));
                }
                ic.getChm().createChallengeMenu(p);
            }
            if (d.equals(plugin.getLang().get("menus.challenges.nochest.nameItem"))){
                if (gp.hasChallenge("NO_CHEST")){
                    gp.removeChallenge("NO_CHEST");
                    p.sendMessage(plugin.getLang().get("messages.challenges.disabled").replace("<challenge>", d));
                } else {
                    gp.addChallenge("NO_CHEST");
                    p.sendMessage(plugin.getLang().get("messages.challenges.enabled").replace("<challenge>", d));
                }
                ic.getChm().createChallengeMenu(p);
            }
            if (d.equals(plugin.getLang().get("menus.challenges.archery.nameItem"))){
                if (gp.hasChallenge("ARCHERY")){
                    gp.removeChallenge("ARCHERY");
                    p.sendMessage(plugin.getLang().get("messages.challenges.disabled").replace("<challenge>", d));
                } else {
                    gp.addChallenge("ARCHERY");
                    p.sendMessage(plugin.getLang().get("messages.challenges.enabled").replace("<challenge>", d));
                }
                ic.getChm().createChallengeMenu(p);
            }
            if (d.equals(plugin.getLang().get("menus.challenges.paper.nameItem"))){
                if (gp.hasChallenge("PAPER")){
                    gp.removeChallenge("PAPER");
                    p.sendMessage(plugin.getLang().get("messages.challenges.disabled").replace("<challenge>", d));
                } else {
                    gp.addChallenge("PAPER");
                    p.sendMessage(plugin.getLang().get("messages.challenges.enabled").replace("<challenge>", d));
                }
                ic.getChm().createChallengeMenu(p);
            }
            if (d.equals(plugin.getLang().get("menus.challenges.hearts.nameItem"))){
                if (gp.hasChallenge("HEARTS")){
                    gp.removeChallenge("HEARTS");
                    p.sendMessage(plugin.getLang().get("messages.challenges.disabled").replace("<challenge>", d));
                } else {
                    gp.addChallenge("HEARTS");
                    p.sendMessage(plugin.getLang().get("messages.challenges.enabled").replace("<challenge>", d));
                }
                ic.getChm().createChallengeMenu(p);
            }
            if (d.equals(plugin.getLang().get("menus.challenges.warrior.nameItem"))){
                if (gp.hasChallenge("WARRIOR")){
                    gp.removeChallenge("WARRIOR");
                    p.sendMessage(plugin.getLang().get("messages.challenges.disabled").replace("<challenge>", d));
                } else {
                    gp.addChallenge("WARRIOR");
                    p.sendMessage(plugin.getLang().get("messages.challenges.enabled").replace("<challenge>", d));
                }
                ic.getChm().createChallengeMenu(p);
            }
            if (d.equals(plugin.getLang().get("menus.challenges.noblocks.nameItem"))){
                if (gp.hasChallenge("NO_BLOCKS")){
                    gp.removeChallenge("NO_BLOCKS");
                    p.sendMessage(plugin.getLang().get("messages.challenges.disabled").replace("<challenge>", d));
                } else {
                    gp.addChallenge("NO_BLOCKS");
                    p.sendMessage(plugin.getLang().get("messages.challenges.enabled").replace("<challenge>", d));
                }
                ic.getChm().createChallengeMenu(p);
            }
            if (d.equals(plugin.getLang().get("menus.challenges.noob.nameItem"))){
                if (gp.hasChallenge("NOOB")){
                    gp.removeChallenge("NOOB");
                    p.sendMessage(plugin.getLang().get("messages.challenges.disabled").replace("<challenge>", d));
                } else {
                    gp.addChallenge("NOOB");
                    p.sendMessage(plugin.getLang().get("messages.challenges.enabled").replace("<challenge>", d));
                }
                ic.getChm().createChallengeMenu(p);
            }
        }
    }
    
    @EventHandler
    public void onRegen(EntityRegainHealthEvent e){
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if (!plugin.getGm().isPlayerInGame(p)) return;
        GamePlayer gp = plugin.getGm().getGameByPlayer(p).getGamePlayer().get(p.getUniqueId());
        if (gp == null || gp.isDead() || !gp.hasChallenge("UHC")) return;
        if (e.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.EATING) || e.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.SATIATED)){
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if (!plugin.getGm().isPlayerInGame(p)) return;
        GamePlayer gp = plugin.getGm().getGameByPlayer(p).getGamePlayer().get(p.getUniqueId());
        if (gp == null || gp.isDead()) return;
        if (gp.hasChallenge("WARRIOR")){
            if (p.getItemInHand() != null){
                ItemStack item = p.getItemInHand();
                if (item.getType().equals(Material.BOW)){
                    e.setCancelled(true);
                    p.sendMessage(plugin.getLang().get("messages.challenges.warriorNoBow"));
                }
            }
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && gp.hasChallenge("NO_CHEST")){
            Block b = e.getClickedBlock();
            if (b.getType().name().endsWith("CHEST")){
                e.setCancelled(true);
                p.sendMessage(plugin.getLang().get("messages.challenges.noChest"));
            }
        }
    }
    
    public String getChallenges(GamePlayer gp){
        StringBuilder sb = new StringBuilder();
        for ( String s : gp.getChallenges() ){
            sb.append(", ").append(plugin.getLang().get("messages.challenges." + s));
        }
        return sb.toString().replaceFirst(", ", "");
    }
    
    @EventHandler
    public void onArmor(ArmorEquipEvent e){
        Player p = e.getPlayer();
        if (!plugin.getGm().isPlayerInGame(p)) return;
        GamePlayer gpp = plugin.getGm().getGameByPlayer(p).getGamePlayer().get(p.getUniqueId());
        if (gpp != null && !gpp.isDead()){
            if (gpp.hasChallenge("WARRIOR")){
                e.setCancelled(true);
                p.sendMessage(plugin.getLang().get("messages.challenges.warriorNoArmor"));
            }
        }
    }
    
    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent e){
        if (e.getEntity() instanceof Player){
            if (e.getDamager() instanceof Player){
                Player p = (Player) e.getEntity();
                Player d = (Player) e.getDamager();
                if (!plugin.getGm().isPlayerInGame(d) || !plugin.getGm().isPlayerInGame(p)) return;
                GamePlayer gpd = plugin.getGm().getGameByPlayer(d).getGamePlayer().get(d.getUniqueId());
                GamePlayer gpp = plugin.getGm().getGameByPlayer(p).getGamePlayer().get(p.getUniqueId());
                if (gpd != null && !gpd.isDead()){
                    if (gpd.hasChallenge("WARRIOR")){
                        if (d.getItemInHand() != null){
                            ItemStack item = d.getItemInHand();
                            if (!item.getType().equals(Material.STONE_SWORD)){
                                e.setCancelled(true);
                                p.sendMessage(plugin.getLang().get("messages.challenges.warriorSword"));
                            }
                        }
                    }
                    if (gpd.hasChallenge("ARCHERY")){
                        e.setCancelled(true);
                        d.sendMessage(plugin.getLang().get("messages.challenges.noBow"));
                    }
                }
                if (gpp != null && !gpp.isDead()){
                    if (gpp.hasChallenge("PAPER")){
                        Vector v = p.getEyeLocation().getDirection();
                        p.setVelocity(v.multiply((v.getY() > 0 ? 1.25 : -1.25)));
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();
        if (!plugin.getGm().isPlayerInGame(p)) return;
        GamePlayer gp = plugin.getGm().getGameByPlayer(p).getGamePlayer().get(p.getUniqueId());
        if (gp == null || gp.isDead() || !gp.hasChallenge("NO_BLOCKS")) return;
        e.setCancelled(true);
    }
    
    @EventHandler
    public void onBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        if (!plugin.getGm().isPlayerInGame(p)) return;
        Game g = plugin.getGm().getGameByPlayer(p);
        if (g == null || !g.isState(State.GAME)) return;
        GamePlayer gp = g.getGamePlayer().get(p.getUniqueId());
        if (gp == null || gp.isDead()) return;
        if (gp.hasChallenge("NO_CHEST")){
            e.setCancelled(true);
            p.sendMessage(plugin.getLang().get("messages.challenges.noChest"));
        }
        if (gp.hasChallenge("NO_BLOCKS")){
            e.getBlock().setType(Material.AIR);
        }
    }
    
}