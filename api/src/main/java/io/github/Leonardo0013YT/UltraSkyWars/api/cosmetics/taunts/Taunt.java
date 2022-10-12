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

package io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.taunts;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Cosmetic;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.CenterMessage;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;

public class Taunt extends Cosmetic {
    
    private final HashMap<String, TauntType> taunts = new HashMap<>();
    private final String title;
    private final String subtitle;
    private final String player;
    private final String none;
    private final ItemStack icon;
    private final UltraSkyWarsApi plugin;
    
    public Taunt(UltraSkyWarsApi plugin, String s){
        super(plugin.getTaunt(), s, "taunts");
        this.plugin = plugin;
        this.title = plugin.getTaunt().get(s + ".title");
        this.subtitle = plugin.getTaunt().get(s + ".subtitle");
        this.player = plugin.getTaunt().get(s + ".player");
        this.none = plugin.getTaunt().get(s + ".none");
        this.icon = Utils.getIcon(plugin.getTaunt(), s);
        ConfigurationSection conf = plugin.getTaunt().getConfig().getConfigurationSection(s + ".taunts");
        for ( String d : conf.getKeys(false) ){
            taunts.put(d, new TauntType(d, plugin.getTaunt().getList(s + ".taunts." + d + ".msg")));
        }
        plugin.getCos().setLastPage("Taunt", page);
    }
    
    public void execute(Player d, Game game){
        String msg = taunts.get("CONTACT").getRandomMessage();
        String death = d.getName();
        msg = msg.replaceAll("<death>", death);
        for ( Player p : game.getPlayers() ){
            p.sendMessage(msg + none);
        }
        for ( Player p : game.getSpectators() ){
            p.sendMessage(msg + none);
        }
    }
    
    public void executePreview(Player p){
        String m1 = plugin.getAdm().parsePlaceholders(p, taunts.getOrDefault("CONTACT", taunts.get("CONTACT")).getRandomMessage().replaceAll("<death>", p.getName()) + player.replaceAll("<killer>", plugin.getLang().get("messages.tauntPreview.killer")));
        String m2 = plugin.getAdm().parsePlaceholders(p, taunts.getOrDefault("PROJECTILE", taunts.get("CONTACT")).getRandomMessage().replaceAll("<death>", p.getName()) + player.replaceAll("<killer>", plugin.getLang().get("messages.tauntPreview.killer")));
        String m3 = plugin.getAdm().parsePlaceholders(p, taunts.getOrDefault("VOID", taunts.get("CONTACT")).getRandomMessage().replaceAll("<death>", p.getName()) + player.replaceAll("<killer>", plugin.getLang().get("messages.tauntPreview.killer")));
        String m4 = plugin.getAdm().parsePlaceholders(p, taunts.getOrDefault("FIRE", taunts.get("CONTACT")).getRandomMessage().replaceAll("<death>", p.getName()) + player.replaceAll("<killer>", plugin.getLang().get("messages.tauntPreview.killer")));
        for ( String m : plugin.getLang().get("messages.tauntPreview.lines").split("\\n") ){
            if (m.contains("<empty>")){
                p.sendMessage("§7        ");
            } else if (m.contains("<center>")){
                p.sendMessage(CenterMessage.getCenteredMessage(m.replaceAll("<taunt>", name).replaceAll("<center>", "")));
            } else if (m.contains("<lines>")){
                p.sendMessage(m1);
                p.sendMessage(m2);
                p.sendMessage(m3);
                p.sendMessage(m4);
            } else {
                p.sendMessage(m);
            }
        }
    }
    
    public void execute(Player d, EntityDamageEvent.DamageCause cause, Game game){
        Player k = null;
        if (UltraSkyWarsApi.get().getTgm().hasTag(d)){
            k = UltraSkyWarsApi.get().getTgm().getTagged(d).getLast();
        }
        if (!game.getPlayers().contains(k)){
            k = null;
        }
        new BukkitRunnable() {
            @Override
            public void run(){
                if (d != null){
                    plugin.getVc().getNMS().sendTitle(title, subtitle, 0, 60, 0, d);
                }
            }
        }.runTaskLater(UltraSkyWarsApi.get(), 5L);
        if (k == null){
            String msg = taunts.getOrDefault(cause.name(), taunts.get("CONTACT")).getRandomMessage();
            String death = d.getName();
            msg = msg.replaceAll("<death>", death);
            for ( Player p : game.getCached() ){
                p.sendMessage(plugin.getAdm().parsePlaceholders(p, msg + none));
            }
        } else {
            String msg = taunts.getOrDefault(cause.name(), taunts.get("CONTACT")).getRandomMessage();
            String killer = player.replaceAll("<killer>", k.getName());
            String death = d.getName();
            msg = msg.replaceAll("<killer>", killer).replaceAll("<death>", death);
            for ( Player p : game.getCached() ){
                p.sendMessage(plugin.getAdm().parsePlaceholders(p, msg + killer));
            }
        }
    }
    
    public HashMap<String, TauntType> getTypes(){
        return taunts;
    }
    
    public ItemStack getIcon(Player p){
        if (!icon.hasItemMeta()){
            return icon;
        }
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        ItemStack icon = this.icon.clone();
        if (!p.hasPermission(autoGivePermission)){
            if (price > 0){
                if (plugin.getCm().isRedPanelInLocked()){
                    if (!sw.getTaunts().contains(id)){
                        icon = ItemBuilder.item(XMaterial.matchDefinedXMaterial(plugin.getCm().getRedPanelMaterial().name(), plugin.getCm().getRedPanelData()).orElse(XMaterial.RED_STAINED_GLASS_PANE), 1, icon.getItemMeta().getDisplayName(), icon.getItemMeta().getLore());
                    }
                }
            }
        }
        ItemMeta iconM = icon.getItemMeta();
        List<String> lore = icon.getItemMeta().getLore();
        for ( int i = 0; i < lore.size(); i++ ){
            String s = lore.get(i);
            switch(s){
                case "<price>":
                    if (!p.hasPermission(autoGivePermission)){
                        if (isBuy && !sw.getTaunts().contains(id)){
                            lore.set(i, plugin.getLang().get(p, "menus.tauntsselector.price").replaceAll("<price>", String.valueOf(price)));
                        } else if (!isBuy && !sw.getTaunts().contains(id)){
                            if (needPermToBuy && p.hasPermission(permission)){
                                lore.set(i, plugin.getLang().get(p, "menus.tauntsselector.price").replaceAll("<price>", String.valueOf(price)));
                            } else {
                                lore.set(i, plugin.getLang().get(p, "menus.tauntsselector.noBuyable"));
                            }
                        } else if (sw.getTaunts().contains(id) || !needPermToBuy){
                            lore.set(i, plugin.getLang().get(p, "menus.tauntsselector.buyed"));
                        }
                    } else {
                        lore.set(i, plugin.getLang().get(p, "menus.tauntsselector.buyed"));
                    }
                    break;
                case "<status>":
                    if (!p.hasPermission(autoGivePermission)){
                        if (sw.getTaunts().contains(id)){
                            lore.set(i, plugin.getLang().get(p, "menus.tauntsselector.hasBuy"));
                        } else if (isBuy){
                            if (plugin.getAdm().getCoins(p) > price){
                                lore.set(i, plugin.getLang().get(p, "menus.tauntsselector.buy"));
                            } else {
                                lore.set(i, plugin.getLang().get(p, "menus.tauntsselector.noMoney"));
                            }
                        } else if (needPermToBuy){
                            if (plugin.getAdm().getCoins(p) > price){
                                lore.set(i, plugin.getLang().get(p, "menus.tauntsselector.buy"));
                            } else {
                                lore.set(i, plugin.getLang().get(p, "menus.tauntsselector.noMoney"));
                            }
                        } else {
                            lore.set(i, plugin.getLang().get(p, "menus.tauntsselector.noPermission"));
                        }
                    } else {
                        lore.set(i, plugin.getLang().get(p, "menus.tauntsselector.hasBuy"));
                    }
                    break;
            }
        }
        iconM.setLore(lore);
        icon.setItemMeta(iconM);
        return NBTEditor.set(icon, id, "ULTRASKYWARS", "TAUNT");
    }
    
    public String getTitle(){
        return title;
    }
    
    public String getPlayer(){
        return player;
    }
    
    public String getNone(){
        return none;
    }
    
}