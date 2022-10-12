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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.perks.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.PerkType;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.State;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.USWGameKillEvent;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.USWGameStartEvent;
import io.github.Leonardo0013YT.UltraSkyWars.api.game.GamePlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.perks.InjectionPerks;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.perks.perks.PotionPerk;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Perk;
import io.github.Leonardo0013YT.UltraSkyWars.api.team.Team;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PerksListener implements Listener {
    
    private final UltraSkyWarsApi plugin;
    private final InjectionPerks injectionPerks;
    
    public PerksListener(UltraSkyWarsApi plugin, InjectionPerks injectionPerks){
        this.plugin = plugin;
        this.injectionPerks = injectionPerks;
    }
    
    @EventHandler
    public void onBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        Game g = plugin.getGm().getGameByPlayer(p);
        if (g == null){
            return;
        }
        if (g.isState(State.GAME)){
            GamePlayer gp = g.getGamePlayer().get(p.getUniqueId());
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw == null || gp.hasChallenge("NOOB")) return;
            int miningexpertise = injectionPerks.getPerks().getInt("perks.miningexpertise.id");
            if (sw.getPerksEnabled().contains(miningexpertise)){
                Perk perk = injectionPerks.getPem().getPerk(PerkType.MINING_EXPERTISE);
                if (perk.isDisabled()) return;
                if (!perk.getGameTypes().contains(g.getGameType())){
                    return;
                }
                if (perk.isReduced(sw)){
                    Block b = e.getBlock();
                    if (b.getType().name().endsWith("_ORE")){
                        b.getDrops().forEach(i -> b.getWorld().dropItemNaturally(b.getLocation(), i));
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onStart(USWGameStartEvent e){
        Game game = e.getGame();
        for ( Player on : game.getPlayers() ){
            GamePlayer gp = game.getGamePlayer().get(on.getUniqueId());
            SWPlayer sw = plugin.getDb().getSWPlayer(on);
            if (sw == null || gp.hasChallenge("NOOB")) return;
            int resistencestart = injectionPerks.getPerks().getInt("perks.resistencestart.id");
            if (sw.getPerksEnabled().contains(resistencestart)){
                PotionPerk perk = (PotionPerk) injectionPerks.getPem().getPerk(PerkType.RESISTENCE_START);
                if (perk.isDisabled()) return;
                if (!perk.getGameTypes().contains(game.getGameType())){
                    return;
                }
                on.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, perk.getDuration(), perk.getAmplifier()));
            }
            int speedstart = injectionPerks.getPerks().getInt("perks.speedstart.id");
            if (sw.getPerksEnabled().contains(speedstart)){
                PotionPerk perk = (PotionPerk) injectionPerks.getPem().getPerk(PerkType.SPEED_START);
                if (perk.isDisabled()) return;
                if (!perk.getGameTypes().contains(game.getGameType())){
                    return;
                }
                on.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, perk.getDuration(), perk.getAmplifier()));
            }
        }
    }
    
    @EventHandler
    public void onKill(USWGameKillEvent e){
        if (plugin.getCm().isBungeeModeLobby()) return;
        Player p = e.getPlayer();
        Game g = plugin.getGm().getGameByPlayer(p);
        if (g == null){
            return;
        }
        if (g.isState(State.GAME)){
            GamePlayer gp = g.getGamePlayer().get(p.getUniqueId());
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw == null || gp.hasChallenge("NOOB")) return;
            int knowledge = injectionPerks.getPerks().getInt("perks.knowledge.id");
            if (sw.getPerksEnabled().contains(knowledge)){
                Perk perk = injectionPerks.getPem().getPerk(PerkType.KNOWLEDGE);
                if (perk.isDisabled()) return;
                if (!perk.getGameTypes().contains(g.getGameType())){
                    return;
                }
                p.giveExp(2);
            }
            int juggernaut = injectionPerks.getPerks().getInt("perks.juggernaut.id");
            if (sw.getPerksEnabled().contains(juggernaut)){
                PotionPerk perk = (PotionPerk) injectionPerks.getPem().getPerk(PerkType.JUGGERNAUT);
                if (perk.isDisabled()) return;
                if (!perk.getGameTypes().contains(g.getGameType())){
                    return;
                }
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, perk.getDuration(), perk.getAmplifier()));
            }
            int savior = injectionPerks.getPerks().getInt("perks.savior.id");
            if (sw.getPerksEnabled().contains(savior)){
                PotionPerk perk = (PotionPerk) injectionPerks.getPem().getPerk(PerkType.SAVIOR);
                if (perk.isDisabled()) return;
                if (!perk.getGameTypes().contains(g.getGameType())){
                    return;
                }
                p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, perk.getDuration(), perk.getAmplifier()));
            }
            int bulldozer = injectionPerks.getPerks().getInt("perks.bulldozer.id");
            if (sw.getPerksEnabled().contains(bulldozer)){
                PotionPerk perk = (PotionPerk) injectionPerks.getPem().getPerk(PerkType.BULLDOZER);
                if (perk.isDisabled()) return;
                if (!perk.getGameTypes().contains(g.getGameType())){
                    return;
                }
                p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, perk.getDuration(), perk.getAmplifier()));
            }
            int nourishment = injectionPerks.getPerks().getInt("perks.nourishment.id");
            if (sw.getPerksEnabled().contains(nourishment)){
                Perk perk = injectionPerks.getPem().getPerk(PerkType.NOURISHMENT);
                if (perk.isDisabled()) return;
                if (!perk.getGameTypes().contains(g.getGameType())){
                    return;
                }
                p.setFoodLevel(20);
                p.setSaturation(10f);
            }
        }
    }
    
    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if (plugin.getCm().isBungeeModeLobby()) return;
        if (e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            Game g = plugin.getGm().getGameByPlayer(p);
            if (g == null){
                return;
            }
            if (g.isState(State.GAME)){
                GamePlayer gp = g.getGamePlayer().get(p.getUniqueId());
                EntityDamageEvent.DamageCause cause = e.getCause();
                if (cause.equals(EntityDamageEvent.DamageCause.SUFFOCATION)){
                    SWPlayer sw = plugin.getDb().getSWPlayer(p);
                    if (sw == null || gp.hasChallenge("NOOB")) return;
                    int fall = injectionPerks.getPerks().getInt("perks.endermastery.id");
                    if (sw.getPerksEnabled().contains(fall)){
                        Perk perk = injectionPerks.getPem().getPerk(PerkType.ENDER_MASTERY);
                        if (perk.isDisabled()) return;
                        if (!perk.getGameTypes().contains(g.getGameType())){
                            return;
                        }
                        if (perk.isReduced(sw)){
                            e.setDamage(perk.getNewAmount(e.getDamage(), sw));
                        }
                    }
                }
                if (cause.equals(EntityDamageEvent.DamageCause.FALL)){
                    SWPlayer sw = plugin.getDb().getSWPlayer(p);
                    if (sw == null || gp.hasChallenge("NOOB")) return;
                    int fall = injectionPerks.getPerks().getInt("perks.fallreduction.id");
                    if (sw.getPerksEnabled().contains(fall)){
                        Perk perk = injectionPerks.getPem().getPerk(PerkType.FALL_REDUCTION);
                        if (perk.isDisabled()) return;
                        if (!perk.getGameTypes().contains(g.getGameType())){
                            return;
                        }
                        if (perk.isReduced(sw)){
                            e.setDamage(perk.getNewAmount(e.getDamage(), sw));
                        }
                    }
                }
                if (cause.equals(EntityDamageEvent.DamageCause.FIRE) || cause.equals(EntityDamageEvent.DamageCause.FIRE_TICK)){
                    SWPlayer sw = plugin.getDb().getSWPlayer(p);
                    if (sw == null || gp.hasChallenge("NOOB")) return;
                    int fall = injectionPerks.getPerks().getInt("perks.firedamagereduction.id");
                    if (sw.getPerksEnabled().contains(fall)){
                        Perk perk = injectionPerks.getPem().getPerk(PerkType.FIRE_DAMAGE_REDUCTION);
                        if (perk.isDisabled()) return;
                        if (!perk.getGameTypes().contains(g.getGameType())){
                            return;
                        }
                        if (perk.isReduced(sw)){
                            e.setDamage(perk.getNewAmount(e.getDamage(), sw));
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent e){
        if (plugin.getCm().isBungeeModeLobby()) return;
        if (e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if (e.getDamager() instanceof Projectile){
                Projectile proj = (Projectile) e.getDamager();
                if (proj.getShooter() instanceof Player){
                    Player d = (Player) proj.getShooter();
                    Game g = plugin.getGm().getGameByPlayer(p);
                    if (g == null){
                        return;
                    }
                    if (g.getSpectators().contains(p)){
                        e.setCancelled(true);
                        return;
                    }
                    Team tp = g.getTeamPlayer(p);
                    Team td = g.getTeamPlayer(d);
                    if (tp == null || td == null){
                        return;
                    }
                    if (tp.getMembers().contains(d) || td.getMembers().contains(p)){
                        e.setCancelled(true);
                        return;
                    }
                    SWPlayer sp = plugin.getDb().getSWPlayer(p);
                    SWPlayer sw = plugin.getDb().getSWPlayer(d);
                    if (sp == null || sw == null) return;
                    GamePlayer gwp = g.getGamePlayer().get(d.getUniqueId());
                    GamePlayer gdp = g.getGamePlayer().get(p.getUniqueId());
                    if (proj instanceof Arrow){
                        int anno = injectionPerks.getPerks().getInt("perks.anno_o_mite.id");
                        if (sw.getPerksEnabled().contains(anno) && !gwp.hasChallenge("NOOB")){
                            Perk perk = injectionPerks.getPem().getPerk(PerkType.ANNO_O_MITE);
                            if (perk.isDisabled()) return;
                            if (!perk.getGameTypes().contains(g.getGameType())){
                                return;
                            }
                            if (perk.isReduced(sw)){
                                d.getWorld().spawn(p.getLocation(), Silverfish.class);
                            }
                        }
                        int recovery = injectionPerks.getPerks().getInt("perks.arrowrecovery.id");
                        if (sw.getPerksEnabled().contains(recovery) && !gwp.hasChallenge("NOOB")){
                            Perk perk = injectionPerks.getPem().getPerk(PerkType.ARROW_RECOVERY);
                            if (perk.isDisabled()) return;
                            if (!perk.getGameTypes().contains(g.getGameType())){
                                return;
                            }
                            if (perk.isReduced(sw)){
                                d.getInventory().addItem(new ItemStack(Material.ARROW));
                            }
                        }
                        int blazing = injectionPerks.getPerks().getInt("perks.blazingarrows.id");
                        if (sw.getPerksEnabled().contains(blazing) && !gwp.hasChallenge("NOOB")){
                            Perk perk = injectionPerks.getPem().getPerk(PerkType.BLAZING_ARROWS);
                            if (perk.isDisabled()) return;
                            if (!perk.getGameTypes().contains(g.getGameType())){
                                return;
                            }
                            if (perk.isReduced(sw)){
                                p.setFireTicks(60);
                            }
                        }
                        int fall = injectionPerks.getPerks().getInt("perks.shotdamagereduction.id");
                        if (sp.getPerksEnabled().contains(fall) && !gdp.hasChallenge("NOOB")){
                            Perk perk = injectionPerks.getPem().getPerk(PerkType.SHOT_DAMAGE_REDUCTION);
                            if (perk.isDisabled()) return;
                            if (!perk.getGameTypes().contains(g.getGameType())){
                                return;
                            }
                            if (perk.isReduced(sp)){
                                e.setDamage(perk.getNewAmount(e.getDamage(), sp));
                            }
                        }
                    }
                }
            }
        }
    }
    
}