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

package io.github.Leonardo0013YT.UltraSkyWars.menus;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.game.GameData;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.IGameMenu;
import io.github.Leonardo0013YT.UltraSkyWars.api.objects.Level;
import io.github.Leonardo0013YT.UltraSkyWars.api.objects.PrestigeIcon;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Perk;
import io.github.Leonardo0013YT.UltraSkyWars.api.team.Team;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import io.github.Leonardo0013YT.UltraSkyWars.inventories.selectors.GameSelectorMenu;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class GameMenu implements IGameMenu {
    
    private final ConcurrentHashMap<UUID, String> views = new ConcurrentHashMap<>();
    private final int[] slotsLevel = {11, 12, 13, 14, 15};
    private final int[] kitsPreview = {4, 5, 6, 7, 8, 36, 37, 38, 39, 40, 41, 42, 43, 44};
    private final UltraSkyWarsApi plugin;
    
    public GameMenu(UltraSkyWarsApi plugin){
        this.plugin = plugin;
    }
    
    public ConcurrentHashMap<UUID, String> getViews(){
        return views;
    }
    
    public void createPrestigeIcons(Player p){
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getLang().get(p, "menus.prestige.title"));
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        for ( PrestigeIcon pi : plugin.getLvl().getPrestige().values() ){
            inv.setItem(pi.getSlot(), pi.getItemStack(p, sw));
        }
        ItemStack back = ItemBuilder.item(plugin.getCm().getBack(), plugin.getLang().get(p, "menus.back.nameItem"), plugin.getLang().get(p, "menus.back.loreItem"));
        ItemStack close = ItemBuilder.item(plugin.getCm().getCloseitem(), plugin.getLang().get(p, "menus.close.nameItem"), plugin.getLang().get(p, "menus.close.loreItem"));
        inv.setItem(48, back);
        inv.setItem(49, close);
        p.openInventory(inv);
    }
    
    public void createKitsMenu(Player p, ItemStack[] contents, ItemStack[] armor){
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getLang().get(p, "menus.preview.title"));
        ItemStack barrier = ItemBuilder.item(XMaterial.BARRIER, 1, "§7", "§7");
        ItemStack white = ItemBuilder.item(XMaterial.WHITE_STAINED_GLASS_PANE, 1, "§7", "§7");
        for ( int i : kitsPreview ){
            inv.setItem(i, white);
        }
        ArrayUtils.reverse(armor);
        for ( int i = 0; i < 4; i++ ){
            if (armor.length <= i) continue;
            ItemStack item = armor[i];
            if (item == null || item.getType().equals(Material.AIR)) inv.setItem(i, barrier);
            else inv.setItem(i, item);
        }
        for ( int i = 0; i < 36; i++ ){
            if (contents.length <= i) continue;
            ItemStack item = contents[i];
            if (i < 9){
                inv.setItem(45 + i, item);
            } else {
                inv.setItem(i, item);
            }
        }
        p.openInventory(inv);
    }
    
    public void createLevelMenu(Player p){
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getLang().get(p, "menus.levels.title"));
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        Level level = plugin.getLvl().getLevel(p);
        Level nLevel = plugin.getLvl().getLevelByLevel(sw.getLevel() + 1);
        if (nLevel == null){
            nLevel = level;
        }
        boolean max = nLevel.equals(level);
        ItemStack nowLevel = ItemBuilder.item(XMaterial.IRON_INGOT, 1, plugin.getLang().get(p, "menus.levels.nowLevel.nameItem").replaceAll("<level>", "" + sw.getLevel()), plugin.getLang().get(p, "menus.levels.nowLevel.loreItem"));
        ItemStack glassYellow = ItemBuilder.item(XMaterial.YELLOW_STAINED_GLASS_PANE, 1, plugin.getLang().get(p, "menus.levels.glass.nameItem").replaceAll("<level>", (max) ? "§cMax" : "" + nLevel.getLevel()), plugin.getLang().get(p, "menus.levels.glass.loreItem"));
        ItemStack glassGreen = ItemBuilder.item(XMaterial.LIME_STAINED_GLASS_PANE, 1, plugin.getLang().get(p, "menus.levels.glass.nameItem").replaceAll("<level>", (max) ? "§cMax" : "" + nLevel.getLevel()), plugin.getLang().get(p, "menus.levels.glass.loreItem"));
        ItemStack nextLevel = ItemBuilder.item(XMaterial.IRON_INGOT, 1, plugin.getLang().get(p, "menus.levels.nextLevel.nameItem").replaceAll("<level>", (max) ? "§cMax" : "" + nLevel.getLevel()), plugin.getLang().get(p, "menus.levels.nextLevel.loreItem"));
        ItemStack prestige = ItemBuilder.item(XMaterial.NETHER_STAR, 1, plugin.getLang().get(p, "menus.levels.prestige.nameItem"), plugin.getLang().get(p, "menus.levels.prestige.loreItem").replaceAll("<player>", p.getName()));
        ItemStack hide = ItemBuilder.item(XMaterial.LIME_DYE, 1, plugin.getLang().get(p, "menus.levels.hide.nameItem"), plugin.getLang().get(p, "menus.levels.hide.loreItem"));
        ItemStack show = ItemBuilder.item(XMaterial.GRAY_DYE, 1, plugin.getLang().get(p, "menus.levels.show.nameItem"), plugin.getLang().get(p, "menus.levels.show.loreItem"));
        ItemStack close = ItemBuilder.item(XMaterial.BARRIER, 1, plugin.getLang().get(p, "menus.close.nameItem"), plugin.getLang().get(p, "menus.close.loreItem"));
        inv.setItem(10, nowLevel);
        double perGlass = (level.getLevelUp() - level.getXp()) * 0.2;
        double counter = 0.0;
        double progress = sw.getXp() - level.getXp();
        int i = 0;
        while (counter < level.getLevelUp()) {
            if (i >= slotsLevel.length) break;
            if (progress > counter){
                inv.setItem(slotsLevel[i], glassGreen);
            } else {
                inv.setItem(slotsLevel[i], glassYellow);
            }
            counter += perGlass;
            i++;
        }
        inv.setItem(16, nextLevel);
        inv.setItem(31, prestige);
        inv.setItem(49, close);
        if (sw.isShowLevel()){
            inv.setItem(50, hide);
        } else {
            inv.setItem(50, show);
        }
        p.openInventory(inv);
    }
    
    public void createSelectorMenu(Player p, String ignore, String type){
        views.put(p.getUniqueId(), type);
        int page = plugin.getUim().getPages().get(p.getUniqueId());
        GameSelectorMenu gss = (GameSelectorMenu) plugin.getUim().getMenus("game" + type + "selector");
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getLang().get(p, "menus.selector.title").replaceAll("<type>", plugin.getLang().get(p, "selector." + type)));
        fillInventory(p, inv, ignore, page, gss, type);
        p.openInventory(inv);
    }
    
    public void updateGameMenu(Player p, Inventory inv, String ignore, String type){
        inv.clear();
        int page = plugin.getUim().getPages().getOrDefault(p.getUniqueId(), 1);
        GameSelectorMenu gss = (GameSelectorMenu) plugin.getUim().getMenus("game" + type + "selector");
        fillInventory(p, inv, ignore, page, gss, type);
    }
    
    private void fillInventory(Player p, Inventory inv, String ignore, int page, GameSelectorMenu gss, String type){
        for ( int s : gss.getExtra() ){
            inv.setItem(s, gss.getContents().get(s));
        }
        ArrayList<Integer> gs = gss.getGameSlots();
        List<GameData> games = (!type.equals("all") ? plugin.getGm().getGameData().values().stream().filter(d -> d.getType().equals(type)).collect(Collectors.toList()) : new ArrayList<>(plugin.getGm().getGameData().values().stream().sorted(Comparator.comparing(GameData::getType)).collect(Collectors.toList())));
        int i = 0, counter = 0;
        for ( GameData game : games ){
            if (!ignore.equals("none") && game.getMap().equals(ignore)){
                continue;
            }
            counter++;
            if (counter <= (page - 1) * Math.max(gs.size(), 1)){
                continue;
            }
            if (gs.size() <= i){
                continue;
            }
            boolean fav = plugin.getDb().getSWPlayer(p).getFavorites().contains(game.getMap());
            ItemStack g = getFirework((fav) ? "FAVORITE" : game.getState(), ((fav) ? "§b" : "§e") + game.getMap(), plugin.getLang().get(p, "menus.selector.loreItem").replaceAll("<state>", plugin.getLang().get(p, "selector." + game.getState().toLowerCase())).replaceAll("<players>", String.valueOf(game.getPlayers())).replaceAll("<max>", String.valueOf(game.getMax())).replaceAll("<mode>", plugin.getLang().get(p, "selector." + game.getType().toLowerCase())));
            inv.setItem(gs.get(i), g);
            i++;
        }
        int slot = gss.getSlot("{RANDOM}");
        int slot2 = gss.getSlot("{FAVORITES}");
        int slot3 = gss.getSlot("{CLOSE}");
        int slot4 = gss.getSlot("{LAST}");
        int slot5 = gss.getSlot("{NEXT}");
        Map<Integer, ItemStack> contents = gss.getContents();
        if (slot > -1 && slot < 54){
            inv.setItem(slot, contents.get(slot));
        }
        if (slot2 > -1 && slot2 < 54){
            inv.setItem(slot2, contents.get(slot2));
        }
        if (slot3 > -1 && slot3 < 54){
            inv.setItem(slot3, contents.get(slot3));
        }
        if (page > 1){
            if (slot4 > -1 && slot4 < 54){
                inv.setItem(slot4, contents.get(slot4));
            }
        }
        if (page < Utils.getMaxPages(games.size(), gs.size())){
            if (slot5 > -1 && slot5 < 54){
                inv.setItem(slot5, contents.get(slot5));
            }
        }
    }
    
    public void createTeamSelector(Player p, Game game){
        views.put(p.getUniqueId(), "teams");
        Inventory inv = Bukkit.createInventory(null, getSize(game.getTeams().size()), plugin.getLang().get(p, "menus.teamselector.title"));
        for ( Team team : game.getTeams().values() ){
            inv.addItem(getTeamItem(team, game.getTeamSize()));
        }
        p.openInventory(inv);
    }
    
    public void updateTeamSelector(Game game, Inventory inv){
        inv.clear();
        for ( Team team : game.getTeams().values() ){
            inv.addItem(getTeamItem(team, game.getTeamSize()));
        }
    }
    
    public void createPerksMenu(Player p, String type){
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getLang().get(p, "menus.perks.title"));
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        for ( Perk perk : plugin.getIjm().getPerks().getPem().getPerks().values() ){
            if (!perk.getGameTypes().contains(type) || perk.isDisabled()) continue;
            inv.setItem(perk.getSlot(), perk.toIcon(p, sw));
        }
        ItemStack close = ItemBuilder.item(plugin.getCm().getCloseitem(), plugin.getLang().get(p, "menus.back.nameItem"), plugin.getLang().get(p, "menus.back.loreItem"));
        inv.setItem(49, close);
        p.openInventory(inv);
    }
    
    public void createCubeletsAnimationMenu(Player p){
        Inventory inv = Bukkit.createInventory(null, 27, plugin.getLang().get(p, "menus.cubelets.title"));
        ItemStack firework = ItemBuilder.item(XMaterial.FIREWORK_ROCKET, 1, plugin.getLang().get(p, "menus.cubelets.fireworks.nameItem"), plugin.getLang().get(p, "menus.cubelets.fireworks.loreItem").replaceAll("<status>", getState(p, plugin.getIjm().getCubelets().getCubelets().getInt("animations.fireworks.id"), plugin.getIjm().getCubelets().getCubelets().get(null, "animations.fireworks.perm"))));
        ItemStack head = ItemBuilder.createSkull(plugin.getLang().get(p, "menus.cubelets.head.nameItem"), plugin.getLang().get(p, "menus.cubelets.head.loreItem").replaceAll("<status>", getState(p, plugin.getIjm().getCubelets().getCubelets().getInt("animations.head.id"), plugin.getIjm().getCubelets().getCubelets().get(null, "animations.head.perm"))), plugin.getCm().getUrl());
        ItemStack circle = ItemBuilder.item(XMaterial.LAVA_BUCKET, plugin.getLang().get(p, "menus.cubelets.flames.nameItem"), plugin.getLang().get(p, "menus.cubelets.flames.loreItem").replaceAll("<status>", getState(p, plugin.getIjm().getCubelets().getCubelets().getInt("animations.flames.id"), plugin.getIjm().getCubelets().getCubelets().get(null, "animations.flames.perm"))));
        inv.setItem(10, firework);
        inv.setItem(11, head);
        inv.setItem(12, circle);
        p.openInventory(inv);
    }
    
    public String getState(Player p, int id, String perm){
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        if (!p.hasPermission(perm)){
            return plugin.getLang().get(p, "menus.cubelets.noPerm");
        }
        if (sw.getCubeAnimation() == id){
            return plugin.getLang().get(p, "menus.cubelets.selected");
        }
        return plugin.getLang().get(p, "menus.cubelets.select");
    }
    
    public int getSize(int games){
        if (games > 45){
            return 54;
        }
        if (games > 36){
            return 45;
        }
        if (games > 27){
            return 36;
        }
        if (games > 18){
            return 27;
        }
        if (games > 9){
            return 18;
        }
        return 9;
    }
    
    public ItemStack getTeamItem(Team team, int teamSize){
        String state = "empty";
        String color = "§a";
        ItemStack item = ItemBuilder.item(XMaterial.LIME_WOOL, 1, "", "");
        if (!team.getMembers().isEmpty()){
            if (team.getTeamSize() >= teamSize){
                item = ItemBuilder.item(XMaterial.RED_WOOL, 1, "", "");
                color = "§4";
                state = "full";
            } else {
                item = ItemBuilder.item(XMaterial.YELLOW_WOOL, 1, "", "");
                color = "§e";
                state = "inFull";
            }
        }
        ItemMeta itemM = item.getItemMeta();
        itemM.setDisplayName(plugin.getLang().get(null, "menus.teamselector.team.nameItem").replaceAll("<max>", String.valueOf(teamSize)).replaceAll("<now>", String.valueOf(team.getTeamSize())).replaceAll("<colorState>", color).replaceAll("<#>", String.valueOf((team.getId() + 1))));
        List<String> lore = new ArrayList<>();
        for ( String l : plugin.getLang().getList("menus.teamselector.team.loreItem") ){
            if (l.equals("<players>")){
                for ( Player on : team.getMembers() ){
                    lore.add(plugin.getLang().get(null, "menus.teamselector.players").replaceAll("<name>", on.getName()));
                }
            } else if (l.equals("<state>")){
                lore.add(plugin.getLang().get(null, "menus.teamselector." + state));
            } else {
                lore.add(l.replaceAll("&", "§"));
            }
        }
        itemM.setLore(lore);
        ItemBuilder.addItemFlags(itemM);
        item.setItemMeta(itemM);
        return NBTEditor.set(item, team.getId(), "TEAM", "ID");
    }
    
    public ItemStack getFirework(String state, String display, String lore){
        ItemStack star = new ItemStack(XMaterial.FIREWORK_STAR.parseMaterial(), 1);
        FireworkEffectMeta metaFw = (FireworkEffectMeta) star.getItemMeta();
        metaFw.setDisplayName(display);
        metaFw.setLore(lore.isEmpty() ? new ArrayList<>() : Arrays.asList(lore.split("\\n")));
        FireworkEffect effect;
        switch(state.toUpperCase()){
            case "WAITING":
                effect = FireworkEffect.builder().withColor(Color.LIME).build();
                break;
            case "STARTING":
                effect = FireworkEffect.builder().withColor(Color.YELLOW).build();
                break;
            case "PREGAME":
                effect = FireworkEffect.builder().withColor(Color.FUCHSIA).build();
                break;
            case "GAME":
                effect = FireworkEffect.builder().withColor(Color.RED).build();
                break;
            case "EMPTY":
                effect = FireworkEffect.builder().withColor(Color.WHITE).build();
                break;
            case "FINISH":
                effect = FireworkEffect.builder().withColor(Color.BLUE).build();
                break;
            case "RESTARTING":
                effect = FireworkEffect.builder().withColor(Color.PURPLE).build();
                break;
            default:
                effect = FireworkEffect.builder().withColor(Color.AQUA).build();
                break;
        }
        metaFw.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
        metaFw.setEffect(effect);
        star.setItemMeta(metaFw);
        return star;
    }
    
    public void updateInventories(String type, String ignore){
        /*switch (type.toUpperCase()) {
            case "ALL": {
                plugin.getGem().getAllView().removeIf(p -> p == null || !p.isOnline() || p.getOpenInventory() == null || p.getOpenInventory().getTopInventory() == null);
                plugin.getGem().getAllView().removeIf(p -> !p.getOpenInventory().getTitle().equals(plugin.getLang().get(p, "menus.selector.title").replaceAll("<type>", plugin.getLang().get(p, "selector.all"))));
                for (Player p : plugin.getGem().getAllView()) {
                    InventoryView inv = p.getOpenInventory();
                    plugin.getGem().updateGameAllMenu(p, inv.getTopInventory(), ignore);
                }
                break;
            }
            case "SOLO": {
                plugin.getGem().getSoloView().removeIf(p -> p == null || !p.isOnline() || p.getOpenInventory() == null || p.getOpenInventory().getTopInventory() == null);
                plugin.getGem().getSoloView().removeIf(p -> !p.getOpenInventory().getTitle().equals(plugin.getLang().get(p, "menus.selector.title").replaceAll("<type>", plugin.getLang().get(p, "selector.normal"))));
                for (Player p : plugin.getGem().getSoloView()) {
                    InventoryView inv = p.getOpenInventory();
                    plugin.getGem().updateGameNormalMenu(p, inv.getTopInventory(), ignore);
                }
                break;
            }
            case "TEAM": {
                plugin.getGem().getTeamView().removeIf(p -> p == null || !p.isOnline() || p.getOpenInventory() == null || p.getOpenInventory().getTopInventory() == null);
                plugin.getGem().getTeamView().removeIf(p -> !p.getOpenInventory().getTitle().equals(plugin.getLang().get(p, "menus.selector.title").replaceAll("<type>", plugin.getLang().get(p, "selector.team"))));
                for (Player p : plugin.getGem().getTeamView()) {
                    InventoryView inv = p.getOpenInventory();
                    plugin.getGem().updateGameTeamMenu(p, inv.getTopInventory(), ignore);
                }
                break;
            }
            case "RANKED":
                plugin.getGem().getRankedView().removeIf(p -> p == null || !p.isOnline() || p.getOpenInventory() == null || p.getOpenInventory().getTopInventory() == null);
                plugin.getGem().getRankedView().removeIf(p -> !p.getOpenInventory().getTitle().equals(plugin.getLang().get(p, "menus.selector.title").replaceAll("<type>", plugin.getLang().get(p, "selector.ranked"))));
                for (Player p : plugin.getGem().getRankedView()) {
                    InventoryView inv = p.getOpenInventory();
                    plugin.getGem().updateGameRankedMenu(p, inv.getTopInventory(), ignore);
                }
                break;
        }*/
    }
    
}