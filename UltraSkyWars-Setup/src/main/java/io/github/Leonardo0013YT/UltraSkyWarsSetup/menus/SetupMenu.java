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

package io.github.Leonardo0013YT.UltraSkyWarsSetup.menus;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.chests.ChestType;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.DamageCauses;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import io.github.Leonardo0013YT.UltraSkyWarsSetup.setup.*;
import io.github.Leonardo0013YT.UltraSkyWarsSetup.setup.cosmetics.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SetupMenu {
    
    private final UltraSkyWarsApi plugin;
    
    public SetupMenu(UltraSkyWarsApi plugin){
        this.plugin = plugin;
    }
    
    public void createPreviewMenu(Player p, PreviewSetup sp){
        Inventory inv = Bukkit.createInventory(null, 36, plugin.getLang().get("menus.setupPreview.title"));
        ItemStack player = ItemBuilder.item(XMaterial.PLAYER_HEAD, 1, plugin.getLang().get("menus.setupPreview.player.nameItem"), plugin.getLang().get("menus.setupPreview.player.loreItem").replace("<loc>", Utils.getFormatedLocation(sp.getPlayer())));
        ItemStack cosmetic = ItemBuilder.item(XMaterial.ENDER_PEARL, 1, plugin.getLang().get("menus.setupPreview.cosmetic.nameItem"), plugin.getLang().get("menus.setupPreview.cosmetic.loreItem").replace("<loc>", Utils.getFormatedLocation(sp.getCosmetic())));
        ItemStack save = ItemBuilder.item(XMaterial.NETHER_STAR, 1, plugin.getLang().get("menus.setupPreview.save.nameItem"), plugin.getLang().get("menus.setupPreview.save.loreItem"));
        inv.setItem(11, player);
        inv.setItem(15, cosmetic);
        inv.setItem(31, save);
        p.openInventory(inv);
    }
    
    public void createChestSetupMenu(Player p, ChestTypeSetup cts){
        Inventory inv = Bukkit.createInventory(null, 45, plugin.getLang().get("menus.chestsetup.title"));
        ItemStack name = ItemBuilder.item(XMaterial.PAPER, 1, plugin.getLang().get("menus.chestsetup.name.nameItem"), plugin.getLang().get("menus.chestsetup.name.loreItem").replace("<name>", cts.getName()));
        ItemStack key = ItemBuilder.item(XMaterial.MAP, 1, plugin.getLang().get("menus.chestsetup.key.nameItem"), plugin.getLang().get("menus.chestsetup.key.loreItem").replace("<key>", cts.getKey()));
        ItemStack edit = ItemBuilder.item(XMaterial.ANVIL, 1, plugin.getLang().get("menus.chestsetup.edit.nameItem"), plugin.getLang().get("menus.chestsetup.edit.loreItem").replace("<edit>", cts.getEdit()));
        ItemStack slotSetup = ItemBuilder.item(XMaterial.GOLD_NUGGET, 1, plugin.getLang().get("menus.chestsetup.slotSetup.nameItem"), plugin.getLang().get("menus.chestsetup.slotSetup.loreItem").replace("<slotSetup>", "" + cts.getSlotSetup()));
        ItemStack slotVotes = ItemBuilder.item(XMaterial.GHAST_TEAR, 1, plugin.getLang().get("menus.chestsetup.slotVotes.nameItem"), plugin.getLang().get("menus.chestsetup.slotVotes.loreItem").replace("<slotSetup>", "" + cts.getSlotVotes()));
        ItemStack refillChange = ItemBuilder.item(XMaterial.ENDER_CHEST, 1, plugin.getLang().get("menus.chestsetup.refillChange.nameItem"), plugin.getLang().get("menus.chestsetup.refillChange.loreItem").replace("<refillChange>", Utils.parseBoolean(cts.isRefillChange())));
        ItemStack armorAllTeams = ItemBuilder.item(XMaterial.DIAMOND_CHESTPLATE, 1, plugin.getLang().get("menus.chestsetup.armorAllTeams.nameItem"), plugin.getLang().get("menus.chestsetup.armorAllTeams.loreItem").replace("<armorAllTeams>", Utils.parseBoolean(cts.isArmorAllTeams())));
        ItemStack save = ItemBuilder.item(XMaterial.NETHER_STAR, 1, plugin.getLang().get("menus.chestsetup.save.nameItem"), plugin.getLang().get("menus.chestsetup.save.loreItem"));
        inv.setItem(11, name);
        inv.setItem(12, key);
        inv.setItem(14, refillChange);
        inv.setItem(15, armorAllTeams);
        inv.setItem(20, edit);
        inv.setItem(22, slotSetup);
        inv.setItem(24, slotVotes);
        inv.setItem(40, save);
        p.openInventory(inv);
    }
    
    public void createSetupArenaMenu(Player p, ArenaSetup as){
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getLang().get("menus.setup.title"));
        ItemStack lobby = ItemBuilder.item(XMaterial.COMPASS, 1, plugin.getLang().get("menus.setup.lobby.nameItem"), plugin.getLang().get("menus.setup.lobby.loreItem").replace("<lobby>", Utils.getFormatedLocation(as.getLobby())));
        ItemStack spect = ItemBuilder.item(XMaterial.COMPASS, 1, plugin.getLang().get("menus.setup.spect.nameItem"), plugin.getLang().get("menus.setup.spect.loreItem").replace("<spect>", Utils.getFormatedLocation(as.getSpectator())));
        ItemStack name = ItemBuilder.item(XMaterial.PAPER, 1, plugin.getLang().get("menus.setup.name.nameItem"), plugin.getLang().get("menus.setup.name.loreItem").replace("<name>", as.getName()));
        ItemStack min = ItemBuilder.item(XMaterial.WRITABLE_BOOK, 1, plugin.getLang().get("menus.setup.min.nameItem"), plugin.getLang().get("menus.setup.min.loreItem").replace("<min>", "" + as.getMin()));
        ItemStack ranked = ItemBuilder.item(XMaterial.BLAZE_POWDER, 1, plugin.getLang().get("menus.setup.ranked.nameItem"), plugin.getLang().get("menus.setup.ranked.loreItem").replace("<ranked>", Utils.parseBoolean(as.isRanked())));
        ItemStack islands = ItemBuilder.item(XMaterial.BEACON, 1, plugin.getLang().get("menus.setup.islands.nameItem"), plugin.getLang().get("menus.setup.islands.loreItem"));
        ItemStack teamSize = ItemBuilder.item(XMaterial.BOOK, 1, plugin.getLang().get("menus.setup.teamSize.nameItem"), plugin.getLang().get("menus.setup.teamSize.loreItem").replace("<teamSize>", "" + as.getTeamSize()));
        ItemStack center = ItemBuilder.item(XMaterial.CHEST, 1, plugin.getLang().get("menus.setup.center.nameItem"), plugin.getLang().get("menus.setup.center.loreItem"));
        ItemStack votes = ItemBuilder.item(XMaterial.MAP, 1, plugin.getLang().get("menus.setup.votes.nameItem"), plugin.getLang().get("menus.setup.votes.loreItem").replace("<votes>", Utils.parseBoolean(as.isVotes())));
        ItemStack events = ItemBuilder.item(XMaterial.ANVIL, 1, plugin.getLang().get("menus.setup.events.nameItem"), plugin.getLang().get("menus.setup.events.loreItem"));
        ItemStack color = ItemBuilder.item(XMaterial.RED_DYE, 1, plugin.getLang().get("menus.setup.color.nameItem"), plugin.getLang().get("menus.setup.color.loreItem").replace("<color>", as.getColor().name()));
        ItemStack borderX = ItemBuilder.item(XMaterial.OAK_BUTTON, 1, plugin.getLang().get("menus.setup.borderX.nameItem"), plugin.getLang().get("menus.setup.borderX.loreItem").replace("<borderX>", String.valueOf(as.getBorderX())));
        ItemStack borderZ = ItemBuilder.item(XMaterial.STONE_BUTTON, 1, plugin.getLang().get("menus.setup.borderZ.nameItem"), plugin.getLang().get("menus.setup.borderZ.loreItem").replace("<borderZ>", String.valueOf(as.getBorderZ())));
        ItemStack save = ItemBuilder.item(XMaterial.NETHER_STAR, 1, plugin.getLang().get("menus.setup.save.nameItem"), plugin.getLang().get("menus.setup.save.loreItem"));
        inv.setItem(3, lobby);
        inv.setItem(5, spect);
        inv.setItem(11, name);
        inv.setItem(15, min);
        inv.setItem(19, ranked);
        inv.setItem(21, islands);
        inv.setItem(23, teamSize);
        inv.setItem(25, center);
        inv.setItem(29, votes);
        inv.setItem(31, events);
        inv.setItem(33, color);
        inv.setItem(39, borderX);
        inv.setItem(41, borderZ);
        inv.setItem(49, save);
        p.openInventory(inv);
    }
    
    public void createSetupBalloonsMenu(Player p, BalloonSetup bs){
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getLang().get("menus.balloons.title"));
        ItemStack icon = ItemBuilder.item(XMaterial.PLAYER_HEAD, 1, plugin.getLang().get("menus.balloons.icon.nameItem"), plugin.getLang().get("menus.balloons.icon.loreItem"));
        ItemStack price = ItemBuilder.item(XMaterial.SUNFLOWER, 1, plugin.getLang().get("menus.balloons.price.nameItem"), plugin.getLang().get("menus.balloons.price.loreItem").replaceAll("<price>", String.valueOf(bs.getPrice())));
        ItemStack slot = ItemBuilder.item(XMaterial.BOOK, 1, plugin.getLang().get("menus.balloons.slot.nameItem"), plugin.getLang().get("menus.balloons.slot.loreItem").replaceAll("<slot>", String.valueOf(bs.getSlot())));
        ItemStack page = ItemBuilder.item(XMaterial.PAPER, 1, plugin.getLang().get("menus.balloons.page.nameItem"), plugin.getLang().get("menus.balloons.page.loreItem").replaceAll("<page>", String.valueOf(bs.getPage())));
        ItemStack permission = ItemBuilder.item(XMaterial.WRITABLE_BOOK, 1, plugin.getLang().get("menus.balloons.permission.nameItem"), plugin.getLang().get("menus.balloons.permission.loreItem").replaceAll("<permission>", bs.getPermission()));
        ItemStack name = ItemBuilder.item(XMaterial.MAP, 1, plugin.getLang().get("menus.balloons.name.nameItem"), plugin.getLang().get("menus.balloons.name.loreItem").replaceAll("<name>", bs.getName()));
        ItemStack url = ItemBuilder.item(XMaterial.NAME_TAG, 1, plugin.getLang().get("menus.balloons.url.nameItem"), plugin.getLang().get("menus.balloons.url.loreItem").replaceAll("<uri>", bs.getUrl()));
        ItemStack isBuy = ItemBuilder.item(XMaterial.REDSTONE, 1, plugin.getLang().get("menus.balloons.isBuy.nameItem"), plugin.getLang().get("menus.balloons.isBuy.loreItem").replaceAll("<purchasable>", Utils.parseBoolean(bs.isBuy())));
        ItemStack save = ItemBuilder.item(XMaterial.NETHER_STAR, 1, plugin.getLang().get("menus.balloons.save.nameItem"), plugin.getLang().get("menus.balloons.save.loreItem"));
        inv.setItem(4, icon);
        inv.setItem(12, price);
        inv.setItem(14, slot);
        inv.setItem(20, page);
        inv.setItem(22, permission);
        inv.setItem(24, name);
        inv.setItem(30, url);
        inv.setItem(32, isBuy);
        inv.setItem(49, save);
        p.openInventory(inv);
    }
    
    public void createSetupChestMenu(Player p){
        Inventory inv = Bukkit.createInventory(null, 27, plugin.getChestType().get("lang.chests.title"));
        for ( String key : plugin.getCtm().getChests().keySet() ){
            ChestType ct = plugin.getCtm().getChests().get(key);
            String u = key.toUpperCase();
            String l = key.toLowerCase();
            if (ct != null && ct.getSetupItem() != null){
                inv.setItem(ct.getSetupSlot(), NBTEditor.set(ItemBuilder.item(ct.getSetupItem().getType(), 1, plugin.getChestType().get("lang.chests." + l + ".nameItem"), plugin.getChestType().get("lang.chests." + l + ".loreItem")), u, "SETUPVOTECHEST"));
            }
        }
        p.openInventory(inv);
    }
    
    public void createSetupChestTypeMenu(Player p, ChestSetup gs){
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getChestType().get("lang.chests.title"));
        fillChests(p, gs, inv);
        ItemStack filter = ItemBuilder.item(XMaterial.OAK_SIGN, 1, plugin.getLang().get("menus.cheststype.filter.nameItem"), plugin.getLang().get("menus.cheststype.filter.loreItem").replaceAll("<filter>", gs.getFilter()));
        ItemStack add = ItemBuilder.item(XMaterial.EMERALD, 1, plugin.getLang().get("menus.cheststype.add.nameItem"), plugin.getLang().get("menus.cheststype.add.loreItem"));
        ItemStack save = ItemBuilder.item(XMaterial.NETHER_STAR, 1, plugin.getLang().get("menus.cheststype.save.nameItem"), plugin.getLang().get("menus.cheststype.save.loreItem"));
        ItemStack remove = ItemBuilder.item(XMaterial.REDSTONE, 1, plugin.getLang().get("menus.cheststype.remove.nameItem"), plugin.getLang().get("menus.cheststype.remove.loreItem"));
        inv.setItem(48, add);
        inv.setItem(49, save);
        inv.setItem(50, remove);
        inv.setItem(51, filter);
        p.openInventory(inv);
    }
    
    public void createSetupChestRemoveMenu(Player p, ChestSetup gs){
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getLang().get("menus.chestsremove.title"));
        fillChests(p, gs, inv);
        ItemStack save = ItemBuilder.item(XMaterial.NETHER_STAR, 1, plugin.getLang().get("menus.chestsremove.save.nameItem"), plugin.getLang().get("menus.chestsremove.save.loreItem"));
        inv.setItem(49, save);
        p.openInventory(inv);
    }
    
    private void fillChests(Player p, ChestSetup gs, Inventory inv){
        int page = plugin.getUim().getPages().getOrDefault(p.getUniqueId(), 1);
        int itt = 0, counter = 0;
        for ( ItemSetup l : gs.getItems() ){
            counter++;
            if (counter < (page - 1) * 35){
                continue;
            }
            itt++;
            inv.addItem(l.getDisplay());
            if (itt == 36){
                break;
            }
        }
        ItemStack next = ItemBuilder.item(XMaterial.ARROW, 1, plugin.getLang().get(p, "menus.next.nameItem"), plugin.getLang().get(p, "menus.next.loreItem"));
        ItemStack last = ItemBuilder.item(XMaterial.ARROW, 1, plugin.getLang().get(p, "menus.last.nameItem"), plugin.getLang().get(p, "menus.last.loreItem"));
        if (page > 1){
            inv.setItem(45, last);
        }
        if (page < Utils.getMaxPages(gs.getItems().size(), 35)){
            inv.setItem(53, next);
        }
    }
    
    public void createSetupChestAddMenu(Player p, ChestSetup gs){
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getLang().get("menus.chestsadd.title"));
        if (!gs.getTemporarily().isEmpty()){
            gs.getTemporarily().forEach(inv::addItem);
            gs.setTemporarily(new ArrayList<>());
        }
        int amount = (gs.getActual() == null) ? 25 : gs.getActual().getChance();
        boolean cet = gs.getActual() != null && gs.getActual().isCenter();
        boolean ref = gs.getActual() != null && gs.getActual().isRefill();
        List<String> mods = gs.getActual() == null ? new ArrayList<>() : gs.getActual().getModes();
        ItemStack chance = ItemBuilder.item(XMaterial.ARROW, 1, plugin.getLang().get("menus.chestsadd.chance.nameItem"), plugin.getLang().get("menus.chestsadd.chance.loreItem").replace("<chance>", String.valueOf(amount)));
        ItemStack save = ItemBuilder.item(XMaterial.NETHER_STAR, 1, plugin.getLang().get("menus.chestsadd.save.nameItem"), plugin.getLang().get("menus.chestsadd.save.loreItem"));
        ItemStack center = ItemBuilder.item(XMaterial.CHEST, 1, plugin.getLang().get("menus.chestsadd.center.nameItem"), plugin.getLang().get("menus.chestsadd.center.loreItem").replace("<center>", Utils.parseBoolean(cet)));
        ItemStack modes = ItemBuilder.item(XMaterial.DIAMOND, 1, plugin.getLang().get("menus.chestsadd.modes.nameItem"), plugin.getLang().get("menus.chestsadd.modes.loreItem").replace("<modes>", Utils.formatList(mods)));
        ItemStack refill = ItemBuilder.item(XMaterial.ENDER_CHEST, 1, plugin.getLang().get("menus.chestsadd.refill.nameItem"), plugin.getLang().get("menus.chestsadd.refill.loreItem").replace("<refill>", Utils.parseBoolean(ref)));
        inv.setItem(47, modes);
        inv.setItem(48, chance);
        inv.setItem(49, save);
        inv.setItem(50, center);
        inv.setItem(51, refill);
        p.openInventory(inv);
    }
    
    public void createEventMenu(Player p, EventSetup es){
        Inventory inv = Bukkit.createInventory(null, 36, plugin.getLang().get("menus.event.title"));
        ItemStack info = ItemBuilder.item(XMaterial.PAPER, 1, plugin.getLang().get("menus.event.info.nameItem"), plugin.getLang().get("menus.event.info.loreItem").replace("<second>", String.valueOf(es.getSeconds())));
        ItemStack remove5 = ItemBuilder.item(XMaterial.RED_DYE, 1, plugin.getLang().get("menus.event.remove5.nameItem"), plugin.getLang().get("menus.event.remove5.loreItem").replace("<value-5>", "5"));
        ItemStack remove1 = ItemBuilder.item(XMaterial.PINK_DYE, 1, plugin.getLang().get("menus.event.remove1.nameItem"), plugin.getLang().get("menus.event.remove1.loreItem").replace("<value-1>", "1"));
        ItemStack add1 = ItemBuilder.item(XMaterial.LIME_DYE, 1, plugin.getLang().get("menus.event.add1.nameItem"), plugin.getLang().get("menus.event.add1.loreItem").replace("<value+1>", "1"));
        ItemStack add5 = ItemBuilder.item(XMaterial.GREEN_DYE, 1, plugin.getLang().get("menus.event.add5.nameItem"), plugin.getLang().get("menus.event.add5.loreItem").replace("<value+5>", "5"));
        ItemStack save = ItemBuilder.item(XMaterial.ENDER_CHEST, 1, plugin.getLang().get("menus.event.save.nameItem"), plugin.getLang().get("menus.event.save.loreItem"));
        inv.setItem(11, remove5);
        inv.setItem(12, remove1);
        inv.setItem(13, info);
        inv.setItem(14, add1);
        inv.setItem(15, add5);
        inv.setItem(31, save);
        p.openInventory(inv);
    }
    
    public void createEventsMenu(Player p, ArenaSetup as){
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getLang().get("menus.events.title"));
        int t = 0;
        for ( EventSetup es : as.getEvents() ){
            t += es.getSeconds();
            inv.addItem(ItemBuilder.item(XMaterial.BOOK, plugin.getLang().get(p, "events." + es.getType().toLowerCase()), plugin.getLang().get(p, "menus.events.event.loreItem").replaceAll("<seconds>", String.valueOf(t))));
        }
        ItemStack refill = ItemBuilder.item(XMaterial.CHEST, 1, plugin.getLang().get("menus.events.refill.nameItem"), plugin.getLang().get("menus.events.refill.loreItem"));
        ItemStack remove = ItemBuilder.item(XMaterial.BARRIER, 1, plugin.getLang().get("menus.events.remove.nameItem"), plugin.getLang().get("menus.events.remove.loreItem"));
        ItemStack finish = ItemBuilder.item(XMaterial.DIAMOND_CHESTPLATE, 1, plugin.getLang().get("menus.events.final.nameItem"), plugin.getLang().get("menus.events.final.loreItem"));
        inv.setItem(48, refill);
        inv.setItem(49, remove);
        inv.setItem(50, finish);
        p.openInventory(inv);
    }
    
    public void createGlassMenu(Player p, GlassSetup gs){
        Inventory inv = Bukkit.createInventory(null, 45, plugin.getLang().get("menus.glass.title"));
        ItemStack name = ItemBuilder.item(XMaterial.PAPER, 1, plugin.getLang().get("menus.glass.name.nameItem"), plugin.getLang().get("menus.glass.name.loreItem").replaceAll("<name>", gs.getName()));
        ItemStack schematic = ItemBuilder.item(XMaterial.MAP, 1, plugin.getLang().get("menus.glass.schematic.nameItem"), plugin.getLang().get("menus.glass.schematic.loreItem").replaceAll("<schematic>", gs.getSchematic()));
        ItemStack clear = ItemBuilder.item(XMaterial.REDSTONE, 1, plugin.getLang().get("menus.glass.clear.nameItem"), plugin.getLang().get("menus.glass.clear.loreItem").replaceAll("<clear>", gs.getClear()));
        ItemStack icon = ItemBuilder.item(XMaterial.GLASS, 1, plugin.getLang().get("menus.glass.icon.nameItem"), plugin.getLang().get("menus.glass.icon.loreItem"));
        ItemStack isBuy = ItemBuilder.item(XMaterial.OAK_SIGN, 1, plugin.getLang().get("menus.glass.isBuy.nameItem"), plugin.getLang().get("menus.glass.isBuy.loreItem").replaceAll("<purchasable>", Utils.parseBoolean(gs.isBuy())));
        ItemStack permission = ItemBuilder.item(XMaterial.WRITABLE_BOOK, 1, plugin.getLang().get("menus.glass.permission.nameItem"), plugin.getLang().get("menus.glass.permission.loreItem").replaceAll("<permission>", gs.getPermission()));
        ItemStack price = ItemBuilder.item(XMaterial.SUNFLOWER, 1, plugin.getLang().get("menus.glass.price.nameItem"), plugin.getLang().get("menus.glass.price.loreItem").replaceAll("<price>", String.valueOf(gs.getPrice())));
        ItemStack slot = ItemBuilder.item(XMaterial.ANVIL, 1, plugin.getLang().get("menus.glass.slot.nameItem"), plugin.getLang().get("menus.glass.slot.loreItem").replaceAll("<slot>", String.valueOf(gs.getSlot())));
        ItemStack page = ItemBuilder.item(XMaterial.BOOK, 1, plugin.getLang().get("menus.glass.page.nameItem"), plugin.getLang().get("menus.glass.page.loreItem").replaceAll("<page>", String.valueOf(gs.getPage())));
        ItemStack save = ItemBuilder.item(XMaterial.NETHER_STAR, 1, plugin.getLang().get("menus.glass.save.nameItem"), plugin.getLang().get("menus.glass.save.loreItem"));
        inv.setItem(3, name);
        inv.setItem(4, schematic);
        inv.setItem(5, clear);
        inv.setItem(11, icon);
        inv.setItem(13, isBuy);
        inv.setItem(15, permission);
        inv.setItem(20, price);
        inv.setItem(22, slot);
        inv.setItem(24, page);
        inv.setItem(40, save);
        p.openInventory(inv);
    }
    
    public void createIslandsMenu(Player p, ArenaSetup as){
        Inventory inv = Bukkit.createInventory(null, 45, plugin.getLang().get("menus.islands.title"));
        for ( IslandArenaSetup ias : as.getIslands().values() ){
            inv.addItem(NBTEditor.set(getIslandItem(ias), ias.getId(), "ULTRASKYWARS_ISLAND_ID"));
        }
        ItemStack add = ItemBuilder.item(XMaterial.EMERALD, plugin.getLang().get("menus.islands.addIsland.nameItem"), plugin.getLang().get("menus.islands.addIsland.loreItem"));
        inv.setItem(44, add);
        p.openInventory(inv);
    }
    
    private ItemStack getIslandItem(IslandArenaSetup ias){
        List<String> lore = new ArrayList<>();
        for ( String l : plugin.getLang().get("menus.islands.island.loreItem").split("\\n") ){
            if (l.contains("<chests>")){
                ias.getChests().forEach(o -> lore.add("§b" + Utils.getFormatedLocation(o)));
            } else {
                lore.add(l.replace("<spawn>", Utils.getFormatedLocation(ias.getSpawn())).replace("<balloon>", Utils.getFormatedLocation(ias.getBalloon())).replace("<fence>", Utils.getFormatedLocation(ias.getFence())));
            }
        }
        return ItemBuilder.item(XMaterial.BEACON, 1, plugin.getLang().get("menus.islands.island.nameItem").replace("<#>", String.valueOf(ias.getId())), lore);
    }
    
    public void createIslandMenu(Player p, IslandArenaSetup ias){
        Inventory inv = Bukkit.createInventory(null, 36, plugin.getLang().get("menus.island.title"));
        ItemStack spawn = ItemBuilder.item(XMaterial.BEACON, 1, plugin.getLang().get("menus.island.spawn.nameItem"), plugin.getLang().get("menus.island.spawn.loreItem").replace("<spawn>", Utils.getFormatedLocation(ias.getSpawn())));
        ItemStack balloon = ItemBuilder.item(XMaterial.PLAYER_HEAD, 1, plugin.getLang().get("menus.island.balloon.nameItem"), plugin.getLang().get("menus.island.balloon.loreItem").replace("<balloon>", Utils.getFormatedLocation(ias.getBalloon())));
        ItemStack fence = ItemBuilder.item(XMaterial.OAK_FENCE, 1, plugin.getLang().get("menus.island.fence.nameItem"), plugin.getLang().get("menus.island.fence.loreItem").replace("<fence>", Utils.getFormatedLocation(ias.getFence())));
        ItemStack chests = ItemBuilder.item(XMaterial.CHEST, 1, plugin.getLang().get("menus.island.chests.nameItem"), plugin.getLang().get("menus.island.chests.loreItem"));
        ItemStack save = ItemBuilder.item(XMaterial.NETHER_STAR, 1, plugin.getLang().get("menus.island.save.nameItem"), plugin.getLang().get("menus.island.save.loreItem"));
        inv.setItem(10, spawn);
        inv.setItem(12, balloon);
        inv.setItem(14, chests);
        inv.setItem(16, fence);
        inv.setItem(31, save);
        p.openInventory(inv);
    }
    
    public void createKillSoundMenu(Player p, KillSoundSetup ks){
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getLang().get("menus.killsounds.title"));
        ItemStack icon = ItemBuilder.item(XMaterial.GHAST_TEAR, 1, plugin.getLang().get("menus.killsounds.icon.nameItem"), plugin.getLang().get("menus.killsounds.icon.loreItem"));
        ItemStack price = ItemBuilder.item(XMaterial.SUNFLOWER, 1, plugin.getLang().get("menus.killsounds.price.nameItem"), plugin.getLang().get("menus.killsounds.price.loreItem").replaceAll("<price>", String.valueOf(ks.getPrice())));
        ItemStack slot = ItemBuilder.item(XMaterial.OAK_SIGN, 1, plugin.getLang().get("menus.killsounds.slot.nameItem"), plugin.getLang().get("menus.killsounds.slot.loreItem").replaceAll("<slot>", String.valueOf(ks.getSlot())));
        ItemStack page = ItemBuilder.item(XMaterial.BOOK, 1, plugin.getLang().get("menus.killsounds.page.nameItem"), plugin.getLang().get("menus.killsounds.page.loreItem").replaceAll("<page>", String.valueOf(ks.getPage())));
        ItemStack sound = ItemBuilder.item(XMaterial.NOTE_BLOCK, 1, plugin.getLang().get("menus.killsounds.sound.nameItem"), plugin.getLang().get("menus.killsounds.sound.loreItem").replaceAll("<sound>", ks.getSound().name()));
        ItemStack vol1 = ItemBuilder.item(XMaterial.REPEATER, 1, plugin.getLang().get("menus.killsounds.vol1.nameItem"), plugin.getLang().get("menus.killsounds.vol1.loreItem").replaceAll("<vol1>", String.valueOf(ks.getVol1())));
        ItemStack vol2 = ItemBuilder.item(XMaterial.COMPARATOR, 1, plugin.getLang().get("menus.killsounds.vol2.nameItem"), plugin.getLang().get("menus.killsounds.vol2.loreItem").replaceAll("<vol2>", String.valueOf(ks.getVol2())));
        ItemStack permission = ItemBuilder.item(XMaterial.MAP, 1, plugin.getLang().get("menus.killsounds.permission.nameItem"), plugin.getLang().get("menus.killsounds.permission.loreItem").replaceAll("<permission>", ks.getPermission()));
        ItemStack name = ItemBuilder.item(XMaterial.BEACON, 1, plugin.getLang().get("menus.killsounds.name.nameItem"), plugin.getLang().get("menus.killsounds.name.loreItem").replaceAll("<name>", ks.getName()));
        ItemStack isBuy = ItemBuilder.item(XMaterial.REDSTONE, 1, plugin.getLang().get("menus.killsounds.isBuy.nameItem"), plugin.getLang().get("menus.killsounds.isBuy.loreItem").replaceAll("<purchasable>", Utils.parseBoolean(ks.isBuy())));
        ItemStack save = ItemBuilder.item(XMaterial.NETHER_STAR, 1, plugin.getLang().get("menus.killsounds.save.nameItem"), plugin.getLang().get("menus.killsounds.save.loreItem"));
        inv.setItem(4, icon);
        inv.setItem(12, price);
        inv.setItem(13, slot);
        inv.setItem(14, page);
        inv.setItem(20, sound);
        inv.setItem(22, vol1);
        inv.setItem(24, vol2);
        inv.setItem(30, permission);
        inv.setItem(31, name);
        inv.setItem(32, isBuy);
        inv.setItem(49, save);
        p.openInventory(inv);
    }
    
    public void createKitLevelMenu(Player p, KitLevelSetup kls){
        Inventory inv = Bukkit.createInventory(null, 36, plugin.getLang().get("menus.kitlevel.title"));
        ItemStack slot = ItemBuilder.item(XMaterial.ANVIL, 1, plugin.getLang().get("menus.kitlevel.slot.nameItem"), plugin.getLang().get("menus.kitlevel.slot.loreItem").replaceAll("<slot>", "" + kls.getSlot()));
        ItemStack isBuy = ItemBuilder.item(XMaterial.OAK_SIGN, 1, plugin.getLang().get("menus.kitlevel.isBuy.nameItem"), plugin.getLang().get("menus.kitlevel.isBuy.loreItem").replaceAll("<purchasable>", Utils.parseBoolean(kls.isBuy())));
        ItemStack icon = ItemBuilder.item(XMaterial.DIAMOND_SWORD, 1, plugin.getLang().get("menus.kitlevel.icon.nameItem"), plugin.getLang().get("menus.kitlevel.icon.loreItem"));
        ItemStack price = ItemBuilder.item(XMaterial.SUNFLOWER, 1, plugin.getLang().get("menus.kitlevel.price.nameItem"), plugin.getLang().get("menus.kitlevel.price.loreItem").replaceAll("<price>", "" + kls.getPrice()));
        ItemStack items = ItemBuilder.item(XMaterial.ENDER_CHEST, 1, plugin.getLang().get("menus.kitlevel.items.nameItem"), plugin.getLang().get("menus.kitlevel.items.loreItem"));
        ItemStack save = ItemBuilder.item(XMaterial.NETHER_STAR, 1, plugin.getLang().get("menus.kitlevel.save.nameItem"), plugin.getLang().get("menus.kitlevel.save.loreItem"));
        inv.setItem(3, slot);
        inv.setItem(5, isBuy);
        inv.setItem(11, icon);
        inv.setItem(13, price);
        inv.setItem(15, items);
        inv.setItem(31, save);
        p.openInventory(inv);
    }
    
    public void createKitMenu(Player p, KitSetup ks){
        Inventory inv = Bukkit.createInventory(null, 36, plugin.getLang().get("menus.kits.title"));
        ItemStack name = ItemBuilder.item(XMaterial.PAPER, 1, plugin.getLang().get("menus.kits.name.nameItem"), plugin.getLang().get("menus.kits.name.loreItem").replaceAll("<name>", ks.getName()));
        ItemStack permission = ItemBuilder.item(XMaterial.WRITABLE_BOOK, 1, plugin.getLang().get("menus.kits.permission.nameItem"), plugin.getLang().get("menus.kits.permission.loreItem").replaceAll("<permission>", ks.getPermission()));
        ItemStack slot = ItemBuilder.item(XMaterial.ANVIL, 1, plugin.getLang().get("menus.kits.slot.nameItem"), plugin.getLang().get("menus.kits.slot.loreItem").replaceAll("<slot>", "" + ks.getSlot()));
        ItemStack page = ItemBuilder.item(XMaterial.BOOK, 1, plugin.getLang().get("menus.kits.page.nameItem"), plugin.getLang().get("menus.kits.page.loreItem").replaceAll("<page>", "" + ks.getPage()));
        ItemStack levels = ItemBuilder.item(XMaterial.EMERALD, 1, plugin.getLang().get("menus.kits.levels.nameItem"), plugin.getLang().get("menus.kits.levels.loreItem"));
        ItemStack save = ItemBuilder.item(XMaterial.NETHER_STAR, 1, plugin.getLang().get("menus.kits.save.nameItem"), plugin.getLang().get("menus.kits.save.loreItem"));
        inv.setItem(11, name);
        inv.setItem(12, permission);
        inv.setItem(14, slot);
        inv.setItem(15, page);
        inv.setItem(35, levels);
        inv.setItem(31, save);
        p.openInventory(inv);
    }
    
    public void createPartingMenu(Player p, PartingSetup bs){
        Inventory inv = Bukkit.createInventory(null, 45, plugin.getLang().get("menus.parting.title"));
        ItemStack icon = ItemBuilder.item(XMaterial.ARMOR_STAND, 1, plugin.getLang().get("menus.parting.icon.nameItem"), plugin.getLang().get("menus.parting.icon.loreItem"));
        ItemStack price = ItemBuilder.item(XMaterial.SUNFLOWER, 1, plugin.getLang().get("menus.parting.price.nameItem"), plugin.getLang().get("menus.parting.price.loreItem").replace("<price>", "" + bs.getPrice()));
        ItemStack slot = ItemBuilder.item(XMaterial.ANVIL, 1, plugin.getLang().get("menus.parting.slot.nameItem"), plugin.getLang().get("menus.parting.slot.loreItem").replace("<slot>", "" + bs.getSlot()));
        ItemStack page = ItemBuilder.item(XMaterial.BOOK, 1, plugin.getLang().get("menus.parting.page.nameItem"), plugin.getLang().get("menus.parting.page.loreItem").replace("<page>", "" + bs.getPage()));
        ItemStack message = ItemBuilder.item(XMaterial.WRITABLE_BOOK, 1, plugin.getLang().get("menus.parting.message.nameItem"), plugin.getLang().get("menus.parting.message.loreItem").replace("<messages>", "" + bs.getMessage()));
        ItemStack permission = ItemBuilder.item(XMaterial.MAP, 1, plugin.getLang().get("menus.parting.permission.nameItem"), plugin.getLang().get("menus.parting.permission.loreItem").replace("<permission>", bs.getPermission()));
        ItemStack name = ItemBuilder.item(XMaterial.PAPER, 1, plugin.getLang().get("menus.parting.name.nameItem"), plugin.getLang().get("menus.parting.name.loreItem").replace("<name>", bs.getName()));
        ItemStack isBuy = ItemBuilder.item(XMaterial.BARRIER, 1, plugin.getLang().get("menus.parting.isBuy.nameItem"), plugin.getLang().get("menus.parting.isBuy.loreItem").replace("<purchasable>", Utils.parseBoolean(bs.isBuy())));
        ItemStack save = ItemBuilder.item(XMaterial.NETHER_STAR, 1, plugin.getLang().get("menus.parting.save.nameItem"), plugin.getLang().get("menus.parting.save.loreItem"));
        inv.setItem(4, icon);
        inv.setItem(11, price);
        inv.setItem(13, slot);
        inv.setItem(15, page);
        inv.setItem(21, message);
        inv.setItem(23, permission);
        inv.setItem(19, name);
        inv.setItem(25, isBuy);
        inv.setItem(40, save);
        p.openInventory(inv);
    }
    
    public void createSoulWellMenu(Player p){
        Inventory inv = Bukkit.createInventory(null, 36, plugin.getLang().get("menus.soulwell.title"));
        ItemStack rewards = ItemBuilder.item(XMaterial.CHEST, 1, plugin.getLang().get("menus.soulwell.rewards.nameItem"), plugin.getLang().get("menus.soulwell.rewards.loreItem"));
        ItemStack save = ItemBuilder.item(XMaterial.NETHER_STAR, 1, plugin.getLang().get("menus.soulwell.save.nameItem"), plugin.getLang().get("menus.soulwell.save.loreItem"));
        inv.setItem(13, rewards);
        inv.setItem(31, save);
        p.openInventory(inv);
    }
    
    public void createSoulWellRewardMenu(Player p, SoulWellRewardSetup sr){
        Inventory inv = Bukkit.createInventory(null, 36, plugin.getLang().get("menus.soulwellreward.title"));
        ItemStack name = ItemBuilder.item(XMaterial.PAPER, 1, plugin.getLang().get("menus.soulwellreward.name.nameItem"), plugin.getLang().get("menus.soulwellreward.name.loreItem").replace("<name>", String.valueOf(sr.getName())));
        ItemStack rarity = ItemBuilder.item(XMaterial.DIAMOND, 1, plugin.getLang().get("menus.soulwellreward.rarity.nameItem"), plugin.getLang().get("menus.soulwellreward.rarity.loreItem").replace("<rarity>", plugin.getLang().get(p, "soulwell.rarity." + sr.getType().name().toLowerCase())));
        ItemStack rewards = ItemBuilder.item(XMaterial.BEACON, 1, plugin.getLang().get("menus.soulwellreward.rewards.nameItem"), plugin.getLang().get("menus.soulwellreward.rewards.loreItem"));
        ItemStack icon = ItemBuilder.item(XMaterial.ARROW, 1, plugin.getLang().get("menus.soulwellreward.icon.nameItem"), plugin.getLang().get("menus.soulwellreward.icon.loreItem"));
        ItemStack chance = ItemBuilder.item(XMaterial.ENDER_PEARL, 1, plugin.getLang().get("menus.soulwellreward.chance.nameItem"), plugin.getLang().get("menus.soulwellreward.chance.loreItem").replace("<chance>", String.valueOf(sr.getChance())));
        ItemStack save = ItemBuilder.item(XMaterial.NETHER_STAR, 1, plugin.getLang().get("menus.soulwellreward.save.nameItem"), plugin.getLang().get("menus.soulwellreward.save.loreItem"));
        inv.setItem(10, name);
        inv.setItem(12, rarity);
        inv.setItem(13, rewards);
        inv.setItem(14, icon);
        inv.setItem(16, chance);
        inv.setItem(31, save);
        p.openInventory(inv);
    }
    
    public void createTauntMenu(Player p, TauntSetup ts){
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getLang().get("menus.taunts.title"));
        int s = 0;
        for ( String c : DamageCauses.valueOf(plugin.getVc().getVersion()).getCauses() ){
            ItemStack dam = ItemBuilder.item(XMaterial.PAPER, "§e" + c, plugin.getLang().get(null, "menus.taunts.damage.loreItem"));
            inv.setItem(s, dam);
            s++;
        }
        ItemStack name = ItemBuilder.item(XMaterial.PAPER, 1, plugin.getLang().get("menus.taunts.name.nameItem"), plugin.getLang().get("menus.taunts.name.loreItem").replaceAll("<name>", ts.getName()));
        ItemStack settitle = ItemBuilder.item(XMaterial.SNOWBALL, 1, plugin.getLang().get("menus.taunts.settitle.nameItem"), plugin.getLang().get("menus.taunts.settitle.loreItem").replaceAll("<title>", ts.getTitle()));
        ItemStack setsubtitle = ItemBuilder.item(XMaterial.SLIME_BALL, 1, plugin.getLang().get("menus.taunts.setsubtitle.nameItem"), plugin.getLang().get("menus.taunts.setsubtitle.loreItem").replaceAll("<subtitle>", ts.getSubtitle()));
        ItemStack setplayer = ItemBuilder.item(XMaterial.LIME_DYE, 1, plugin.getLang().get("menus.taunts.setplayer.nameItem"), plugin.getLang().get("menus.taunts.setplayer.loreItem").replaceAll("<player>", ts.getPlayer()));
        ItemStack setnone = ItemBuilder.item(XMaterial.GRAY_DYE, 1, plugin.getLang().get("menus.taunts.setnone.nameItem"), plugin.getLang().get("menus.taunts.setnone.loreItem").replaceAll("<none>", ts.getNone()));
        ItemStack price = ItemBuilder.item(XMaterial.SUNFLOWER, 1, plugin.getLang().get("menus.taunts.price.nameItem"), plugin.getLang().get("menus.taunts.price.loreItem").replaceAll("<price>", String.valueOf(ts.getPrice())));
        ItemStack slot = ItemBuilder.item(XMaterial.ANVIL, 1, plugin.getLang().get("menus.taunts.slot.nameItem"), plugin.getLang().get("menus.taunts.slot.loreItem").replaceAll("<slot>", String.valueOf(ts.getSlot())));
        ItemStack page = ItemBuilder.item(XMaterial.BOOK, 1, plugin.getLang().get("menus.taunts.page.nameItem"), plugin.getLang().get("menus.taunts.page.loreItem").replaceAll("<page>", String.valueOf(ts.getPage())));
        ItemStack icon = ItemBuilder.item(XMaterial.ARROW, 1, plugin.getLang().get("menus.taunts.icon.nameItem"), plugin.getLang().get("menus.taunts.icon.loreItem"));
        ItemStack permission = ItemBuilder.item(XMaterial.MAP, 1, plugin.getLang().get("menus.taunts.permission.nameItem"), plugin.getLang().get("menus.taunts.permission.loreItem").replaceAll("<permission>", ts.getPermission()));
        ItemStack isBuy = ItemBuilder.item(XMaterial.BARRIER, 1, plugin.getLang().get("menus.taunts.isBuy.nameItem"), plugin.getLang().get("menus.taunts.isBuy.loreItem").replaceAll("<purchasable>", Utils.parseBoolean(ts.isBuy())));
        ItemStack save = ItemBuilder.item(XMaterial.NETHER_STAR, 1, plugin.getLang().get("menus.taunts.save.nameItem"), plugin.getLang().get("menus.taunts.save.loreItem"));
        inv.setItem(38, name);
        inv.setItem(39, settitle);
        inv.setItem(40, setsubtitle);
        inv.setItem(41, setplayer);
        inv.setItem(42, setnone);
        inv.setItem(45, price);
        inv.setItem(47, slot);
        inv.setItem(48, page);
        inv.setItem(49, icon);
        inv.setItem(50, permission);
        inv.setItem(51, isBuy);
        inv.setItem(53, save);
        p.openInventory(inv);
    }
    
    public void createTauntTypeMenu(Player p, TauntTypeSetup tts){
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getLang().get("menus.tauntstype.title"));
        for ( String l : tts.getMsg() ){
            ItemStack it = ItemBuilder.item(XMaterial.PAPER, "§eMessage", l);
            inv.addItem(it);
        }
        ItemStack add = ItemBuilder.item(XMaterial.EMERALD, 1, plugin.getLang().get("menus.tauntstype.add.nameItem"), plugin.getLang().get("menus.tauntstype.add.loreItem"));
        ItemStack save = ItemBuilder.item(XMaterial.NETHER_STAR, 1, plugin.getLang().get("menus.tauntstype.save.nameItem"), plugin.getLang().get("menus.tauntstype.save.loreItem"));
        inv.setItem(49, add);
        inv.setItem(53, save);
        p.openInventory(inv);
    }
    
    public void createTrailMenu(Player p, TrailSetup ts){
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getLang().get("menus.trails.title"));
        ItemStack icon = ItemBuilder.item(XMaterial.BOW, 1, plugin.getLang().get("menus.trails.icon.nameItem"), plugin.getLang().get("menus.trails.icon.loreItem"));
        ItemStack speed = ItemBuilder.item(XMaterial.FEATHER, 1, plugin.getLang().get("menus.trails.speed.nameItem"), plugin.getLang().get("menus.trails.speed.loreItem").replaceAll("<speed>", "" + ts.getSpeed()));
        ItemStack offsetX = ItemBuilder.item(XMaterial.GUNPOWDER, 1, plugin.getLang().get("menus.trails.offsetX.nameItem"), plugin.getLang().get("menus.trails.offsetX.loreItem").replaceAll("<offsetX>", "" + ts.getOffsetX()));
        ItemStack offsetY = ItemBuilder.item(XMaterial.GLOWSTONE_DUST, 1, plugin.getLang().get("menus.trails.offsetY.nameItem"), plugin.getLang().get("menus.trails.offsetY.loreItem").replaceAll("<offsetY>", "" + ts.getOffsetY()));
        ItemStack offsetZ = ItemBuilder.item(XMaterial.SUGAR, 1, plugin.getLang().get("menus.trails.offsetZ.nameItem"), plugin.getLang().get("menus.trails.offsetZ.loreItem").replaceAll("<offsetZ>", "" + ts.getOffsetZ()));
        ItemStack amount = ItemBuilder.item(XMaterial.DIAMOND, 1, plugin.getLang().get("menus.trails.amount.nameItem"), plugin.getLang().get("menus.trails.amount.loreItem").replaceAll("<amount>", "" + ts.getAmount()));
        ItemStack price = ItemBuilder.item(XMaterial.SUNFLOWER, 1, plugin.getLang().get("menus.trails.price.nameItem"), plugin.getLang().get("menus.trails.price.loreItem").replaceAll("<price>", "" + ts.getPrice()));
        ItemStack range = ItemBuilder.item(XMaterial.STICK, 1, plugin.getLang().get("menus.trails.range.nameItem"), plugin.getLang().get("menus.trails.range.loreItem").replaceAll("<range>", "" + ts.getRange()));
        ItemStack slot = ItemBuilder.item(XMaterial.ANVIL, 1, plugin.getLang().get("menus.trails.slot.nameItem"), plugin.getLang().get("menus.trails.slot.loreItem").replaceAll("<slot>", "" + ts.getSlot()));
        ItemStack page = ItemBuilder.item(XMaterial.BOOK, 1, plugin.getLang().get("menus.trails.page.nameItem"), plugin.getLang().get("menus.trails.page.loreItem").replaceAll("<page>", "" + ts.getPage()));
        ItemStack isBuy = ItemBuilder.item(XMaterial.REDSTONE, 1, plugin.getLang().get("menus.trails.isBuy.nameItem"), plugin.getLang().get("menus.trails.isBuy.loreItem").replaceAll("<purchasable>", Utils.parseBoolean(ts.isBuy())));
        ItemStack name = ItemBuilder.item(XMaterial.PAPER, 1, plugin.getLang().get("menus.trails.name.nameItem"), plugin.getLang().get("menus.trails.name.loreItem").replaceAll("<name>", ts.getName()));
        ItemStack permission = ItemBuilder.item(XMaterial.MAP, 1, plugin.getLang().get("menus.trails.permission.nameItem"), plugin.getLang().get("menus.trails.permission.loreItem").replaceAll("<permission>", ts.getPermission()));
        ItemStack particle = ItemBuilder.item(XMaterial.BLAZE_POWDER, 1, plugin.getLang().get("menus.trails.particle.nameItem"), plugin.getLang().get("menus.trails.particle.loreItem").replaceAll("<particle>", ts.getParticle()));
        ItemStack save = ItemBuilder.item(XMaterial.NETHER_STAR, 1, plugin.getLang().get("menus.trails.save.nameItem"), plugin.getLang().get("menus.trails.save.loreItem"));
        inv.setItem(4, icon);
        inv.setItem(10, speed);
        inv.setItem(12, offsetX);
        inv.setItem(13, offsetY);
        inv.setItem(14, offsetZ);
        inv.setItem(16, amount);
        inv.setItem(20, price);
        inv.setItem(21, range);
        inv.setItem(22, slot);
        inv.setItem(23, page);
        inv.setItem(24, isBuy);
        inv.setItem(30, name);
        inv.setItem(31, permission);
        inv.setItem(32, particle);
        inv.setItem(49, save);
        p.openInventory(inv);
    }
    
}