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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.challenges.menus;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.game.GamePlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.challenges.enums.ChallengeType;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChallengeMenu {
    
    private final UltraSkyWarsApi plugin;
    
    public ChallengeMenu(UltraSkyWarsApi plugin){
        this.plugin = plugin;
    }
    
    public void createChallengeMenu(Player p){
        Game game = plugin.getGm().getGameByPlayer(p);
        GamePlayer gp = game.getGamePlayer().get(p.getUniqueId());
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getLang().get("menus.challenges.title"));
        if (ChallengeType.UHC.isEnabled()){
            ItemStack uhc = ItemBuilder.item(ChallengeType.UHC.getMaterial(), 1, plugin.getLang().get("menus.challenges.uhc.nameItem"), plugin.getLang().get("menus.challenges.uhc.loreItem").replace("<amount>", String.valueOf(sw.getChallengeCompleted("UHC"))).replace("<status>", Utils.parseBoolean(gp.hasChallenge("UHC"))));
            inv.setItem(ChallengeType.UHC.getIconSlot(), uhc);
            inv.setItem(ChallengeType.UHC.getStatusSlot(), getItemState(sw, "uhc", gp.hasChallenge("UHC")));
        }
        if (ChallengeType.NO_CHEST.isEnabled()){
            ItemStack nochest = ItemBuilder.item(ChallengeType.NO_CHEST.getMaterial(), 1, plugin.getLang().get("menus.challenges.nochest.nameItem"), plugin.getLang().get("menus.challenges.nochest.loreItem").replace("<amount>", String.valueOf(sw.getChallengeCompleted("NO_CHEST"))).replace("<status>", Utils.parseBoolean(gp.hasChallenge("NO_CHEST"))));
            inv.setItem(ChallengeType.NO_CHEST.getIconSlot(), nochest);
            inv.setItem(ChallengeType.NO_CHEST.getStatusSlot(), getItemState(sw, "nochest", gp.hasChallenge("NO_CHEST")));
        }
        if (ChallengeType.ARCHERY.isEnabled()){
            ItemStack archery = ItemBuilder.item(ChallengeType.ARCHERY.getMaterial(), 1, plugin.getLang().get("menus.challenges.archery.nameItem"), plugin.getLang().get("menus.challenges.archery.loreItem").replace("<amount>", String.valueOf(sw.getChallengeCompleted("ARCHERY"))).replace("<status>", Utils.parseBoolean(gp.hasChallenge("ARCHERY"))));
            inv.setItem(ChallengeType.ARCHERY.getIconSlot(), archery);
            inv.setItem(ChallengeType.ARCHERY.getStatusSlot(), getItemState(sw, "archery", gp.hasChallenge("ARCHERY")));
        }
        if (ChallengeType.PAPER.isEnabled()){
            ItemStack paper = ItemBuilder.item(ChallengeType.PAPER.getMaterial(), 1, plugin.getLang().get("menus.challenges.paper.nameItem"), plugin.getLang().get("menus.challenges.paper.loreItem").replace("<amount>", String.valueOf(sw.getChallengeCompleted("PAPER"))).replace("<status>", Utils.parseBoolean(gp.hasChallenge("PAPER"))));
            inv.setItem(ChallengeType.PAPER.getIconSlot(), paper);
            inv.setItem(ChallengeType.PAPER.getStatusSlot(), getItemState(sw, "paper", gp.hasChallenge("PAPER")));
        }
        if (ChallengeType.HEARTS.isEnabled()){
            ItemStack hearts = ItemBuilder.item(ChallengeType.HEARTS.getMaterial(), 1, plugin.getLang().get("menus.challenges.hearts.nameItem"), plugin.getLang().get("menus.challenges.hearts.loreItem").replace("<amount>", String.valueOf(sw.getChallengeCompleted("HEARTS"))).replace("<status>", Utils.parseBoolean(gp.hasChallenge("HEARTS"))));
            inv.setItem(ChallengeType.HEARTS.getIconSlot(), hearts);
            inv.setItem(ChallengeType.HEARTS.getStatusSlot(), getItemState(sw, "hearts", gp.hasChallenge("HEARTS")));
        }
        if (ChallengeType.WARRIOR.isEnabled()){
            ItemStack warrior = ItemBuilder.item(ChallengeType.WARRIOR.getMaterial(), 1, plugin.getLang().get("menus.challenges.warrior.nameItem"), plugin.getLang().get("menus.challenges.warrior.loreItem").replace("<amount>", String.valueOf(sw.getChallengeCompleted("WARRIOR"))).replace("<status>", Utils.parseBoolean(gp.hasChallenge("WARRIOR"))));
            inv.setItem(ChallengeType.WARRIOR.getIconSlot(), warrior);
            inv.setItem(ChallengeType.WARRIOR.getStatusSlot(), getItemState(sw, "warrior", gp.hasChallenge("WARRIOR")));
        }
        if (ChallengeType.NO_BLOCKS.isEnabled()){
            ItemStack noblocks = ItemBuilder.item(ChallengeType.NO_BLOCKS.getMaterial(), 1, plugin.getLang().get("menus.challenges.noblocks.nameItem"), plugin.getLang().get("menus.challenges.noblocks.loreItem").replace("<amount>", String.valueOf(sw.getChallengeCompleted("NO_BLOCKS"))).replace("<status>", Utils.parseBoolean(gp.hasChallenge("NO_BLOCKS"))));
            inv.setItem(ChallengeType.NO_BLOCKS.getIconSlot(), noblocks);
            inv.setItem(ChallengeType.NO_BLOCKS.getStatusSlot(), getItemState(sw, "noblocks", gp.hasChallenge("NO_BLOCKS")));
        }
        if (ChallengeType.NOOB.isEnabled()){
            ItemStack noob = ItemBuilder.item(ChallengeType.NOOB.getMaterial(), 1, plugin.getLang().get("menus.challenges.noob.nameItem"), plugin.getLang().get("menus.challenges.noob.loreItem").replace("<amount>", String.valueOf(sw.getChallengeCompleted("NOOB"))).replace("<status>", Utils.parseBoolean(gp.hasChallenge("NOOB"))));
            inv.setItem(ChallengeType.NOOB.getIconSlot(), noob);
            inv.setItem(ChallengeType.NOOB.getStatusSlot(), getItemState(sw, "noob", gp.hasChallenge("NOOB")));
        }
        p.openInventory(inv);
    }
    
    public ItemStack getItemState(SWPlayer sw, String type, boolean value){
        if (value){
            return ItemBuilder.item(XMaterial.LIME_DYE, plugin.getLang().get("menus.challenges." + type + ".nameItem"), plugin.getLang().get("menus.challenges." + type + ".loreItem").replace("<amount>", String.valueOf(sw.getChallengeCompleted(type))).replace("<status>", Utils.parseBoolean(true)));
        }
        return ItemBuilder.item(XMaterial.GRAY_DYE, plugin.getLang().get("menus.challenges." + type + ".nameItem"), plugin.getLang().get("menus.challenges." + type + ".loreItem").replace("<amount>", String.valueOf(sw.getChallengeCompleted(type))).replace("<status>", Utils.parseBoolean(false)));
    }
    
}