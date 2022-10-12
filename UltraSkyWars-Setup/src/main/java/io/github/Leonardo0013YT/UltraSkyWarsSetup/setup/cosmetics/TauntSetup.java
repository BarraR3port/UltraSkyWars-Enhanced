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

package io.github.Leonardo0013YT.UltraSkyWarsSetup.setup.cosmetics;


import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.DamageCauses;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class TauntSetup {
    
    private final UltraSkyWarsApi plugin;
    private final HashMap<String, TauntTypeSetup> taunts = new HashMap<>();
    private TauntTypeSetup actual;
    private String name, permission, player = "§e by §7<killer>§e.", none = "§e.", title = "§c§lYOU ARE DEATH", subtitle = "§7Now you spectate game!";
    private boolean isBuy = true;
    private int slot = 10, page = 1, price = 500;
    private ItemStack icon = new ItemStack(Material.ARROW);
    
    public TauntSetup(UltraSkyWarsApi plugin, Player p, String name){
        this.plugin = plugin;
        this.name = name;
        this.permission = "ultraskywars.taunt." + name;
        for ( String c : DamageCauses.valueOf(plugin.getVc().getVersion()).getCauses() ){
            taunts.put(c, new TauntTypeSetup(p, c, Collections.singletonList(plugin.getLang().get(p, "taunts." + c))));
        }
    }
    
    public String getPlayer(){
        return player;
    }
    
    public void setPlayer(String player){
        this.player = player;
    }
    
    public String getNone(){
        return none;
    }
    
    public void setNone(String none){
        this.none = none;
    }
    
    public String getTitle(){
        return title;
    }
    
    public void setTitle(String title){
        this.title = title;
    }
    
    public String getSubtitle(){
        return subtitle;
    }
    
    public void setSubtitle(String subtitle){
        this.subtitle = subtitle;
    }
    
    public TauntTypeSetup getActual(){
        return actual;
    }
    
    public void setActual(TauntTypeSetup actual){
        this.actual = actual;
    }
    
    public void saveTauntType(Player p){
        TauntTypeSetup tts = actual;
        taunts.put(tts.getDamage(), tts);
        actual = null;
        p.sendMessage(plugin.getLang().get(p, "setup.tauntstype.save"));
    }
    
    public void saveTaunt(Player p){
        plugin.getTaunt().set("taunts." + name + ".id", plugin.getCos().getNextTauntsId());
        plugin.getTaunt().set("taunts." + name + ".name", name);
        ItemStack icon = getIcon().clone();
        ItemMeta im = icon.getItemMeta();
        im.setDisplayName("§a" + name);
        im.setLore(Arrays.asList("§7This is a default lore.", "§7Change me in taunts.yml"));
        icon.setItemMeta(im);
        plugin.getTaunt().set("taunts." + name + ".icon", icon);
        plugin.getTaunt().set("taunts." + name + ".title", title);
        plugin.getTaunt().set("taunts." + name + ".subtitle", subtitle);
        plugin.getTaunt().set("taunts." + name + ".player", player);
        plugin.getTaunt().set("taunts." + name + ".none", none);
        plugin.getTaunt().set("taunts." + name + ".slot", slot);
        plugin.getTaunt().set("taunts." + name + ".page", page);
        plugin.getTaunt().set("taunts." + name + ".price", price);
        plugin.getTaunt().set("taunts." + name + ".permission", permission);
        plugin.getTaunt().set("taunts." + name + ".isBuy", isBuy);
        for ( String c : taunts.keySet() ){
            TauntTypeSetup tts = taunts.get(c);
            plugin.getTaunt().set("taunts." + name + ".taunts." + c + ".cause", tts.getDamage());
            plugin.getTaunt().set("taunts." + name + ".taunts." + c + ".msg", tts.getMsg());
        }
        plugin.getTaunt().save();
        plugin.getCos().reload();
        p.sendMessage(plugin.getLang().get(p, "setup.taunts.save"));
    }
    
    public ItemStack getIcon(){
        if (icon == null || icon.getType().equals(Material.AIR)){
            return new ItemStack(XMaterial.PAPER.parseMaterial());
        }
        return icon;
    }
    
    public void setIcon(ItemStack icon){
        this.icon = icon;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getPermission(){
        return permission;
    }
    
    public void setPermission(String permission){
        this.permission = permission;
    }
    
    public boolean isBuy(){
        return isBuy;
    }
    
    public void setBuy(boolean buy){
        isBuy = buy;
    }
    
    public int getSlot(){
        return slot;
    }
    
    public void setSlot(int slot){
        this.slot = slot;
    }
    
    public int getPage(){
        return page;
    }
    
    public void setPage(int page){
        this.page = page;
    }
    
    public int getPrice(){
        return price;
    }
    
    public void setPrice(int price){
        this.price = price;
    }
    
    public HashMap<String, TauntTypeSetup> getTaunts(){
        return taunts;
    }
}