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
import io.github.Leonardo0013YT.UltraSkyWars.api.actions.InventoryAction;
import io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.Balloon;
import io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.Glass;
import io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.KillSound;
import io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.Parting;
import io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.killeffects.UltraKillEffect;
import io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.kits.Kit;
import io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.kits.KitLevel;
import io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.taunts.Taunt;
import io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.trails.Trail;
import io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.windances.UltraWinDance;
import io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.wineffects.UltraWinEffect;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.CustomSound;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.OrderType;
import io.github.Leonardo0013YT.UltraSkyWars.api.game.GamePlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.game.UltraTeamGame;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.IUltraInventoryMenu;
import io.github.Leonardo0013YT.UltraSkyWars.api.objects.PerkLevel;
import io.github.Leonardo0013YT.UltraSkyWars.api.objects.PrestigeIcon;
import io.github.Leonardo0013YT.UltraSkyWars.api.objects.PreviewCosmetic;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Perk;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.UltraInventory;
import io.github.Leonardo0013YT.UltraSkyWars.api.team.Team;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import io.github.Leonardo0013YT.UltraSkyWars.inventories.*;
import io.github.Leonardo0013YT.UltraSkyWars.inventories.selectors.*;
import io.github.Leonardo0013YT.UltraSkyWars.inventories.votes.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class UltraInventoryMenu implements IUltraInventoryMenu {
    
    private final HashMap<String, UltraInventory> menus = new HashMap<>();
    private final HashMap<UUID, Integer> pages = new HashMap<>();
    private final HashMap<String, Consumer<InventoryAction>> actions = new HashMap<>();
    private final UltraSkyWarsApi plugin;
    
    public UltraInventoryMenu(UltraSkyWarsApi plugin){
        this.plugin = plugin;
    }
    
    public void removeInventory(Player p){
        pages.remove(p.getUniqueId());
        plugin.getGem().getViews().remove(p.getUniqueId());
    }
    
    public void loadMenus(){
        menus.clear();
        menus.put("kitsperks", new KitsPerksMenu(plugin, "kitsperks"));
        menus.put("lobby", new LobbyShopMenu(plugin, "lobby"));
        menus.put("votes", new VoteMainMenu(plugin, "votes"));
        menus.put("chest", new VoteChestMenu(plugin, "chest"));
        menus.put("final", new VoteFinalMenu(plugin, "final"));
        menus.put("health", new VoteHealthMenu(plugin, "health"));
        menus.put("projectile", new VoteProjectileMenu(plugin, "projectile"));
        menus.put("time", new VoteTimeMenu(plugin, "time"));
        menus.put("options", new SpectateOptionsMenu(plugin, "options"));
        menus.put("players", new SpectatePlayersMenu(plugin, "players"));
        menus.put("soulwellmenu", new SoulWellMenuRolling(plugin, "soulwellmenu"));
        menus.put("balloonsselector", new BalloonSelectorMenu(plugin, "balloonsselector"));
        menus.put("glassselector", new GlassSelectorMenu(plugin, "glassselector"));
        menus.put("tauntsselector", new TauntsSelectorMenu(plugin, "tauntsselector"));
        menus.put("trailsselector", new TrailsSelectorMenu(plugin, "trailsselector"));
        menus.put("partingselector", new PartingSelectorMenu(plugin, "partingselector"));
        menus.put("killsoundsselector", new KillSoundsSelectorMenu(plugin, "killsoundsselector"));
        menus.put("killeffectsselector", new KillEffectsSelectorMenu(plugin, "killeffectsselector"));
        menus.put("wineffectsselector", new WinEffectsSelectorMenu(plugin, "wineffectsselector"));
        menus.put("windancesselector", new WinDancesSelectorMenu(plugin, "windancesselector"));
        menus.put("gamesoloselector", new GameSelectorMenu(plugin, "gamesoloselector", "solo"));
        menus.put("gameteamselector", new GameSelectorMenu(plugin, "gameteamselector", "team"));
        menus.put("gametnt_madnessselector", new GameSelectorMenu(plugin, "gametnt_madnessselector", "tnt_madness"));
        menus.put("gamerankedselector", new GameSelectorMenu(plugin, "gamerankedselector", "ranked"));
        menus.put("gameallselector", new GameSelectorMenu(plugin, "gameallselector", "all"));
        menus.put("kitselector", new KitSelectorMenu(plugin, "kitselector"));
        loadMenusActions();
    }
    
    public UltraInventory getMenus(String t){
        return menus.get(t);
    }
    
    public void openInventory(Player p, UltraInventory i){
        Inventory inv = Bukkit.createInventory(null, i.getRows() * 9, i.getTitle());
        for ( Map.Entry<Integer, ItemStack> entry : i.getConfig().entrySet() ){
            Integer s = entry.getKey();
            ItemStack it = entry.getValue();
            inv.setItem(s, it);
        }
        p.openInventory(inv);
    }
    
    public Inventory openContentInventory(Player p, UltraInventory i){
        Inventory inv = Bukkit.createInventory(null, i.getRows() * 9, i.getTitle());
        for ( Map.Entry<Integer, ItemStack> entry : i.getContents().entrySet() ){
            Integer s = entry.getKey();
            ItemStack it = entry.getValue();
            inv.setItem(s, ItemBuilder.parseVariables(p, it));
        }
        p.openInventory(inv);
        return inv;
    }
    
    public void openPlayersInventory(Player p, UltraInventory i, Game game, OrderType type, String[]... t){
        Inventory inv = Bukkit.createInventory(null, i.getRows() * 9, i.getTitle());
        if (type.equals(OrderType.ALPHABETICALLY)){
            List<String> pls = new ArrayList<>();
            game.getPlayers().forEach(player -> pls.add(player.getName()));
            for ( String l : Utils.orderABC(pls) ){
                GamePlayer gp = game.getGamePlayerByName(l);
                if (gp.getP().equals(p) || gp.isDead()) continue;
                ItemStack skull = ItemBuilder.skull(XMaterial.PLAYER_HEAD, 1, "§e" + l, plugin.getLang().get(p, "menus.players.players.loreItem").replaceAll("<health>", Utils.formatDouble(gp.getP().getHealth())).replaceAll("<food>", Utils.formatDouble(gp.getP().getFoodLevel())).replaceAll("<kills>", String.valueOf(gp.getKills())), l);
                inv.addItem(skull);
            }
        } else if (type.equals(OrderType.KILLS)){
            List<Integer> pls = new ArrayList<>();
            for ( GamePlayer gp : game.getGamePlayer().values() ){
                if (gp.isDead()) continue;
                pls.add(gp.getKills());
            }
            List<GamePlayer> ready = new ArrayList<>();
            for ( int l : Utils.orderDESC(pls) ){
                GamePlayer gp = game.getGamePlayerByKills(l, ready);
                ready.add(gp);
                checkEquals(p, inv, gp);
            }
        } else {
            for ( GamePlayer gp : game.getGamePlayer().values() ){
                if (gp.isDead()) continue;
                checkEquals(p, inv, gp);
            }
        }
        for ( Map.Entry<Integer, ItemStack> entry : i.getContents().entrySet() ){
            Integer s = entry.getKey();
            ItemStack it = entry.getValue();
            inv.setItem(s, ItemBuilder.parseVariables(p, it, plugin, t));
        }
        p.openInventory(inv);
    }
    
    private void checkEquals(Player p, Inventory inv, GamePlayer gp){
        if (gp == null || gp.getP() == null || !gp.getP().isOnline()) return;
        if (gp.getP().getUniqueId().equals(p.getUniqueId())) return;
        ItemStack skull = ItemBuilder.skull(XMaterial.PLAYER_HEAD, 1, "§e" + gp.getP().getName(), plugin.getLang().get(p, "menus.players.players.loreItem").replaceAll("<health>", Utils.formatDouble(gp.getP().getHealth())).replaceAll("<food>", Utils.formatDouble(gp.getP().getFoodLevel())).replaceAll("<kills>", String.valueOf(gp.getKills())), gp.getP().getName());
        inv.addItem(skull);
    }
    
    public void openInventory(Player p, UltraInventory i, String[]... t){
        Inventory inv = Bukkit.createInventory(null, i.getRows() * 9, i.getTitle());
        for ( Map.Entry<Integer, ItemStack> entry : i.getContents().entrySet() ){
            Integer s = entry.getKey();
            ItemStack it = entry.getValue().clone();
            inv.setItem(s, ItemBuilder.parseVariables(p, it, plugin, t));
        }
        p.openInventory(inv);
    }
    
    public void openChestInventory(Player p, UltraInventory i, String[]... t){
        Inventory inv = Bukkit.createInventory(null, i.getRows() * 9, i.getTitle());
        for ( Map.Entry<Integer, ItemStack> entry : i.getContents().entrySet() ){
            Integer s = entry.getKey();
            ItemStack it = entry.getValue().clone();
            AtomicReference<String> selected = new AtomicReference<>("");
            ItemStack now = ItemBuilder.parseChestVariables(p, it, plugin, selected::set, t);
            inv.setItem(s, NBTEditor.set(now, selected.get(), "CHEST", "VOTE", "TYPE"));
        }
        p.openInventory(inv);
    }
    
    public void setInventory(String inv, Inventory close){
        if (menus.containsKey(inv)){
            Map<Integer, ItemStack> items = new HashMap<>();
            for ( int i = 0; i < close.getSize(); i++ ){
                ItemStack it = close.getItem(i);
                if (it == null || it.getType().equals(Material.AIR)){
                    continue;
                }
                items.put(i, it);
            }
            menus.get(inv).setConfig(items);
            menus.get(inv).save();
        }
    }
    
    public void createPartingSelectorMenu(Player p){
        int page = pages.get(p.getUniqueId());
        PartingSelectorMenu selector = (PartingSelectorMenu) getMenus("partingselector");
        Inventory inv = Bukkit.createInventory(null, selector.getRows() * 9, plugin.getLang().get(p, "menus.partingselector.title"));
        for ( int s : selector.getExtra() ){
            inv.setItem(s, selector.getContents().get(s));
        }
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        for ( Parting k : plugin.getCos().getPartings().values() ){
            if (k.getId() == sw.getParting()){
                ItemStack i = k.getIcon(p);
                ItemStack kit = ItemBuilder.nameLore(i.clone(), plugin.getLang().get(p, "menus.partingselector.parting.nameItem"), plugin.getLang().get(p, "menus.partingselector.parting.loreItem"));
                int s = selector.getSlot("{SELECTED}");
                if (s > -1 && s < 54){
                    inv.setItem(s, kit);
                }
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), i);
            } else {
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), k.getIcon(p));
            }
        }
        if (sw.getParting() != 999999){
            int s = selector.getSlot("{DESELECT}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page > 1){
            int s = selector.getSlot("{LAST}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page < plugin.getCos().getLastPage("Parting")){
            int s = selector.getSlot("{NEXT}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        int s = selector.getSlot("{CLOSE}");
        if (s > -1 && s < 54){
            inv.setItem(s, selector.getContents().get(s));
        }
        p.openInventory(inv);
    }
    
    public void createKitSelectorMenu(Player p, String type, boolean game){
        int page = pages.get(p.getUniqueId());
        KitSelectorMenu selector = (KitSelectorMenu) getMenus("kitselector");
        Inventory inv = Bukkit.createInventory(null, selector.getRows() * 9, plugin.getLang().get(p, "menus.kitselector.title"));
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        int kitSelected = sw.getSoloKit();
        int kitSelectedLevel = sw.getSoloKitLevel();
        if (type.equals("TEAM")){
            kitSelected = sw.getTeamKit();
            kitSelectedLevel = sw.getTeamKitLevel();
        } else if (type.equals("RANKED")){
            kitSelected = sw.getRankedKit();
            kitSelectedLevel = sw.getRankedKitLevel();
        }
        for ( Kit k : plugin.getKm().getKits().values() ){
            if (!k.getModes().contains(type)){
                continue;
            }
            if (k.getId() == kitSelected){
                KitLevel kl = k.getLevels().get(kitSelectedLevel);
                if (kl == null){
                    inv.setItem(k.getSlot(), NBTEditor.set(k.getLevels().get(1).getIcon(p), type, "KIT", "SELECTOR", "TYPE"));
                    continue;
                }
                ItemStack i = kl.getIcon(p);
                ItemStack kit = ItemBuilder.nameLore(i.clone(), plugin.getLang().get(p, "menus.kitselector.kit.nameItem"), plugin.getLang().get(p, "menus.kitselector.kit.loreItem"));
                int s = selector.getSlot("{SELECTED}");
                if (s > -1 && s < 54){
                    inv.setItem(s, NBTEditor.set(kit, type, "KIT", "SELECTOR", "TYPE"));
                }
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), NBTEditor.set(i, type, "KIT", "SELECTOR", "TYPE"));
            } else {
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), NBTEditor.set(k.getLevels().get(1).getIcon(p), type, "KIT", "SELECTOR", "TYPE"));
            }
        }
        if (kitSelected != 999999){
            int s = selector.getSlot("{DESELECT}");
            if (s > -1 && s < 54){
                inv.setItem(s, NBTEditor.set(selector.getContents().get(s), type, "KIT", "SELECTOR", "TYPE"));
            }
        }
        if (page > 1){
            int s = selector.getSlot("{LAST}");
            if (s > -1 && s < 54){
                inv.setItem(s, NBTEditor.set(selector.getContents().get(s), type, "KIT", "SELECTOR", "TYPE"));
            }
        }
        if (page < plugin.getKm().getLastPage()){
            int s = selector.getSlot("{NEXT}");
            if (s > -1 && s < 54){
                inv.setItem(s, NBTEditor.set(selector.getContents().get(s), type, "KIT", "SELECTOR", "TYPE"));
            }
        }
        if (game){
            int s = selector.getSlot("{CLOSE}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        } else {
            int s = selector.getSlot("{BACK}");
            if (s > -1 && s < 54){
                inv.setItem(s, NBTEditor.set(selector.getContents().get(s), type, "KIT", "SELECTOR", "TYPE"));
            }
        }
        p.openInventory(inv);
    }
    
    public void createTrailsSelectorMenu(Player p){
        int page = pages.get(p.getUniqueId());
        TrailsSelectorMenu selector = (TrailsSelectorMenu) getMenus("trailsselector");
        Inventory inv = Bukkit.createInventory(null, selector.getRows() * 9, plugin.getLang().get(p, "menus.trailsselector.title"));
        for ( int s : selector.getExtra() ){
            inv.setItem(s, selector.getContents().get(s));
        }
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        for ( Trail k : plugin.getCos().getTrails().values() ){
            if (k.getId() == sw.getTrail()){
                ItemStack i = k.getIcon(p);
                ItemStack kit = ItemBuilder.nameLore(i.clone(), plugin.getLang().get(p, "menus.trailsselector.trail.nameItem"), plugin.getLang().get(p, "menus.trailsselector.trail.loreItem"));
                int s = selector.getSlot("{SELECTED}");
                if (s > -1 && s < 54){
                    inv.setItem(s, kit);
                }
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), i);
            } else {
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), k.getIcon(p));
            }
        }
        if (sw.getTrail() != 999999){
            int s = selector.getSlot("{DESELECT}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page > 1){
            int s = selector.getSlot("{LAST}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page < plugin.getCos().getLastPage("Trail")){
            int s = selector.getSlot("{NEXT}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        int s = selector.getSlot("{CLOSE}");
        if (s > -1 && s < 54){
            inv.setItem(s, selector.getContents().get(s));
        }
        p.openInventory(inv);
    }
    
    public void createTauntsSelectorMenu(Player p){
        int page = pages.get(p.getUniqueId());
        TauntsSelectorMenu selector = (TauntsSelectorMenu) getMenus("tauntsselector");
        Inventory inv = Bukkit.createInventory(null, selector.getRows() * 9, plugin.getLang().get(p, "menus.tauntsselector.title"));
        for ( int s : selector.getExtra() ){
            inv.setItem(s, selector.getContents().get(s));
        }
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        for ( Taunt k : plugin.getCos().getTaunts().values() ){
            if (k.getId() == sw.getTaunt()){
                ItemStack i = k.getIcon(p);
                ItemStack kit = ItemBuilder.nameLore(i.clone(), plugin.getLang().get(p, "menus.tauntsselector.taunt.nameItem"), plugin.getLang().get(p, "menus.tauntsselector.taunt.loreItem"));
                int s = selector.getSlot("{SELECTED}");
                if (s > -1 && s < 54){
                    inv.setItem(s, kit);
                }
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), i);
            } else {
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), k.getIcon(p));
            }
        }
        if (sw.getTaunt() != 999999){
            int s = selector.getSlot("{DESELECT}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page > 1){
            int s = selector.getSlot("{LAST}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page < plugin.getCos().getLastPage("Taunt")){
            int s = selector.getSlot("{NEXT}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        int s = selector.getSlot("{CLOSE}");
        if (s > -1 && s < 54){
            inv.setItem(s, selector.getContents().get(s));
        }
        p.openInventory(inv);
    }
    
    public void createGlassSelectorMenu(Player p){
        int page = pages.get(p.getUniqueId());
        GlassSelectorMenu selector = (GlassSelectorMenu) getMenus("glassselector");
        Inventory inv = Bukkit.createInventory(null, selector.getRows() * 9, plugin.getLang().get(p, "menus.glassselector.title"));
        for ( int s : selector.getExtra() ){
            inv.setItem(s, selector.getContents().get(s));
        }
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        for ( Glass k : plugin.getCos().getGlasses().values() ){
            if (k.getId() == sw.getGlass()){
                ItemStack i = k.getIcon(p);
                ItemStack kit = ItemBuilder.nameLore(i.clone(), plugin.getLang().get(p, "menus.glassselector.glass.nameItem"), plugin.getLang().get(p, "menus.glassselector.glass.loreItem"));
                int s = selector.getSlot("{SELECTED}");
                if (s > -1 && s < 54){
                    inv.setItem(s, kit);
                }
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), i);
            } else {
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), k.getIcon(p));
            }
        }
        if (sw.getGlass() != 999999){
            int s = selector.getSlot("{DESELECT}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page > 1){
            int s = selector.getSlot("{LAST}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page < plugin.getCos().getLastPage("Glass")){
            int s = selector.getSlot("{NEXT}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        int s = selector.getSlot("{CLOSE}");
        if (s > -1 && s < 54){
            inv.setItem(s, selector.getContents().get(s));
        }
        p.openInventory(inv);
    }
    
    public void createBalloonSelectorMenu(Player p){
        int page = pages.get(p.getUniqueId());
        BalloonSelectorMenu selector = (BalloonSelectorMenu) getMenus("balloonsselector");
        Inventory inv = Bukkit.createInventory(null, selector.getRows() * 9, plugin.getLang().get(p, "menus.balloonsselector.title"));
        for ( int s : selector.getExtra() ){
            inv.setItem(s, selector.getContents().get(s));
        }
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        for ( Balloon k : plugin.getCos().getBalloons().values() ){
            if (k.getId() == sw.getBalloon()){
                ItemStack i = k.getIcon(p);
                ItemStack kit = ItemBuilder.createSkull(plugin.getLang().get(p, "menus.balloonsselector.balloon.nameItem"), plugin.getLang().get(p, "menus.balloonsselector.balloon.loreItem"), k.getUrl());
                int s = selector.getSlot("{SELECTED}");
                if (s > -1 && s < 54){
                    inv.setItem(s, kit);
                }
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), i);
            } else {
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), k.getIcon(p));
            }
        }
        if (sw.getBalloon() != 999999){
            int s = selector.getSlot("{DESELECT}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page > 1){
            int s = selector.getSlot("{LAST}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page < plugin.getCos().getLastPage("Balloon")){
            int s = selector.getSlot("{NEXT}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        int s = selector.getSlot("{CLOSE}");
        if (s > -1 && s < 54){
            inv.setItem(s, selector.getContents().get(s));
        }
        p.openInventory(inv);
    }
    
    public void createKillSoundSelectorMenu(Player p){
        int page = pages.get(p.getUniqueId());
        KillSoundsSelectorMenu selector = (KillSoundsSelectorMenu) getMenus("killsoundsselector");
        Inventory inv = Bukkit.createInventory(null, selector.getRows() * 9, plugin.getLang().get(p, "menus.killsoundsselector.title"));
        for ( int s : selector.getExtra() ){
            inv.setItem(s, selector.getContents().get(s));
        }
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        for ( KillSound k : plugin.getCos().getKillSounds().values() ){
            if (k.getId() == sw.getKillSound()){
                ItemStack i = k.getIcon(p);
                ItemStack kit = ItemBuilder.nameLore(i.clone(), plugin.getLang().get(p, "menus.killsoundsselector.killsound.nameItem"), plugin.getLang().get(p, "menus.killsoundsselector.killsound.loreItem"));
                int s = selector.getSlot("{SELECTED}");
                if (s > -1 && s < 54){
                    inv.setItem(s, kit);
                }
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), i);
            } else {
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), k.getIcon(p));
            }
        }
        if (sw.getKillSound() != 999999){
            int s = selector.getSlot("{DESELECT}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page > 1){
            int s = selector.getSlot("{LAST}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page < plugin.getCos().getLastPage("KillSound")){
            int s = selector.getSlot("{NEXT}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        int s = selector.getSlot("{CLOSE}");
        if (s > -1 && s < 54){
            inv.setItem(s, selector.getContents().get(s));
        }
        p.openInventory(inv);
    }
    
    public void createKillEffectSelectorMenu(Player p){
        int page = pages.get(p.getUniqueId());
        KillEffectsSelectorMenu selector = (KillEffectsSelectorMenu) getMenus("killeffectsselector");
        Inventory inv = Bukkit.createInventory(null, selector.getRows() * 9, plugin.getLang().get(p, "menus.killeffectsselector.title"));
        for ( int s : selector.getExtra() ){
            inv.setItem(s, selector.getContents().get(s));
        }
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        for ( UltraKillEffect k : plugin.getCos().getKillEffect().values() ){
            if (k.getId() == sw.getKillEffect()){
                ItemStack i = k.getIcon(p);
                ItemStack kit = ItemBuilder.nameLore(i.clone(), plugin.getLang().get(p, "menus.killeffectsselector.killeffect.nameItem"), plugin.getLang().get(p, "menus.killeffectsselector.killeffect.loreItem"));
                int s = selector.getSlot("{SELECTED}");
                if (s > -1 && s < 54){
                    inv.setItem(s, kit);
                }
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), i);
            } else {
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), k.getIcon(p));
            }
        }
        if (sw.getKillEffect() != 999999){
            int s = selector.getSlot("{DESELECT}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page > 1){
            int s = selector.getSlot("{LAST}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page < plugin.getCos().getLastPage("KillEffect")){
            int s = selector.getSlot("{NEXT}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        int s = selector.getSlot("{CLOSE}");
        if (s > -1 && s < 54){
            inv.setItem(s, selector.getContents().get(s));
        }
        p.openInventory(inv);
    }
    
    public void createWinEffectSelectorMenu(Player p){
        int page = pages.get(p.getUniqueId());
        WinEffectsSelectorMenu selector = (WinEffectsSelectorMenu) getMenus("wineffectsselector");
        Inventory inv = Bukkit.createInventory(null, selector.getRows() * 9, plugin.getLang().get(p, "menus.wineffectsselector.title"));
        for ( int s : selector.getExtra() ){
            inv.setItem(s, selector.getContents().get(s));
        }
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        for ( UltraWinEffect k : plugin.getCos().getWinEffects().values() ){
            if (k.getId() == sw.getWinEffect()){
                ItemStack i = k.getIcon(p);
                ItemStack kit = ItemBuilder.nameLore(i.clone(), plugin.getLang().get(p, "menus.wineffectsselector.wineffect.nameItem"), plugin.getLang().get(p, "menus.wineffectsselector.wineffect.loreItem"));
                int s = selector.getSlot("{SELECTED}");
                if (s > -1 && s < 54){
                    inv.setItem(s, kit);
                }
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), i);
            } else {
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), k.getIcon(p));
            }
        }
        if (sw.getWinEffect() != 999999){
            int s = selector.getSlot("{DESELECT}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page > 1){
            int s = selector.getSlot("{LAST}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page < plugin.getCos().getLastPage("WinEffect")){
            int s = selector.getSlot("{NEXT}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        int s = selector.getSlot("{CLOSE}");
        if (s > -1 && s < 54){
            inv.setItem(s, selector.getContents().get(s));
        }
        p.openInventory(inv);
    }
    
    public void createWinDanceSelectorMenu(Player p){
        int page = pages.get(p.getUniqueId());
        WinDancesSelectorMenu selector = (WinDancesSelectorMenu) getMenus("windancesselector");
        Inventory inv = Bukkit.createInventory(null, selector.getRows() * 9, plugin.getLang().get(p, "menus.windancesselector.title"));
        for ( int s : selector.getExtra() ){
            inv.setItem(s, selector.getContents().get(s));
        }
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        for ( UltraWinDance k : plugin.getCos().getWinDance().values() ){
            if (k.getId() == sw.getWinDance()){
                ItemStack i = k.getIcon(p);
                ItemStack kit = ItemBuilder.nameLore(i.clone(), plugin.getLang().get(p, "menus.windancesselector.windance.nameItem"), plugin.getLang().get(p, "menus.windancesselector.windance.loreItem"));
                int s = selector.getSlot("{SELECTED}");
                if (s > -1 && s < 54){
                    inv.setItem(s, kit);
                }
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), i);
            } else {
                if (k.getPage() != page) continue;
                inv.setItem(k.getSlot(), k.getIcon(p));
            }
        }
        if (sw.getWinDance() != 999999){
            int s = selector.getSlot("{DESELECT}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page > 1){
            int s = selector.getSlot("{LAST}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        if (page < plugin.getCos().getLastPage("WinDance")){
            int s = selector.getSlot("{NEXT}");
            if (s > -1 && s < 54){
                inv.setItem(s, selector.getContents().get(s));
            }
        }
        int s = selector.getSlot("{CLOSE}");
        if (s > -1 && s < 54){
            inv.setItem(s, selector.getContents().get(s));
        }
        p.openInventory(inv);
    }
    
    public void createKitLevelSelectorMenu(Player p, Kit k, String type){
        Inventory inv = Bukkit.createInventory(null, 54, plugin.getLang().get(p, "menus.kitlevels.title"));
        ItemStack close = ItemBuilder.item(plugin.getCm().getCloseitem(), plugin.getLang().get(p, "menus.back.nameItem"), plugin.getLang().get(p, "menus.back.loreItem"));
        for ( KitLevel kl : k.getLevels().values() ){
            ItemStack i = kl.getIcon(p);
            inv.setItem(kl.getSlot(), NBTEditor.set(i, type, "KIT", "SELECTOR", "TYPE"));
        }
        inv.setItem(49, NBTEditor.set(close, type, "KIT", "SELECTOR", "TYPE"));
        p.openInventory(inv);
    }
    
    public HashMap<UUID, Integer> getPages(){
        return pages;
    }
    
    public void addPage(Player p){
        pages.putIfAbsent(p.getUniqueId(), 1);
        pages.put(p.getUniqueId(), pages.get(p.getUniqueId()) + 1);
    }
    
    public void removePage(Player p){
        pages.put(p.getUniqueId(), pages.get(p.getUniqueId()) - 1);
    }
    
    public void loadMenusActions(){
        /*actions.put(plugin.getLang().get("menus.balloonsselector.title"), (b) -> {
            InventoryClickEvent e = b.getInventoryClickEvent();
            Player p = b.getPlayer();
        });
        actions.put(plugin.getLang().get("menus.balloonsselector.title"), (b) -> {
            InventoryClickEvent e = b.getInventoryClickEvent();
            Player p = b.getPlayer();
        });
        actions.put(plugin.getLang().get("menus.balloonsselector.title"), (b) -> {
            InventoryClickEvent e = b.getInventoryClickEvent();
            Player p = b.getPlayer();
        });*/
        actions.put(plugin.getLang().get("menus.kitselector.title"), (b) -> {
            InventoryClickEvent e = b.getInventoryClickEvent();
            Player p = b.getPlayer();
            if (plugin.isSetupLobby(p)) return;
            String display = checkDisplayName(b);
            if (display.equals("none")) return;
            if (display.equals(plugin.getLang().get(p, "menus.close.nameItem"))){
                if (e.getClick().equals(ClickType.RIGHT)){
                    p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                    return;
                }
                p.closeInventory();
                return;
            }
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            boolean isGame = plugin.getGm().isPlayerInGame(p);
            String type = NBTEditor.getString(e.getCurrentItem(), "KIT", "SELECTOR", "TYPE");
            if (type == null) return;
            if (display.equals(plugin.getLang().get(p, "menus.back.nameItem"))){
                plugin.getUim().openContentInventory(p, plugin.getUim().getMenus("kitsperks"));
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.next.nameItem"))){
                plugin.getUim().addPage(p);
                plugin.getUim().createKitSelectorMenu(p, type, isGame);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.last.nameItem"))){
                plugin.getUim().removePage(p);
                plugin.getUim().createKitSelectorMenu(p, type, isGame);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.kitselector.kit.nameItem"))){
                int kitSelected = sw.getSoloKit();
                int kitSelectedLevel = sw.getSoloKitLevel();
                if (type.equals("TEAM")){
                    kitSelected = sw.getTeamKit();
                    kitSelectedLevel = sw.getTeamKitLevel();
                } else if (type.equals("RANKED")){
                    kitSelected = sw.getRankedKit();
                    kitSelectedLevel = sw.getRankedKitLevel();
                }
                Kit kit = plugin.getKm().getKits().get(kitSelected);
                KitLevel kl = kit.getLevels().get(kitSelectedLevel);
                plugin.getGem().createKitsMenu(p, kl.getInv(), kl.getArmors());
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.kitselector.deselect.nameItem"))){
                if (type.equals("SOLO")){
                    if (sw.getSoloKit() == 999999){
                        p.sendMessage(plugin.getLang().get(p, "messages.noSelect"));
                        return;
                    }
                    sw.setSoloKit(999999);
                    sw.setSoloKitLevel(1);
                } else if (type.equals("TEAM")){
                    if (sw.getTeamKit() == 999999){
                        p.sendMessage(plugin.getLang().get(p, "messages.noSelect"));
                        return;
                    }
                    sw.setTeamKit(999999);
                    sw.setTeamKitLevel(1);
                } else {
                    if (sw.getRankedKit() == 999999){
                        p.sendMessage(plugin.getLang().get(p, "messages.noSelect"));
                        return;
                    }
                    sw.setRankedKit(999999);
                    sw.setRankedKitLevel(1);
                }
                p.sendMessage(plugin.getLang().get(p, "messages.deselect"));
                plugin.getUim().createKitSelectorMenu(p, type, isGame);
                return;
            }
            Kit k = plugin.getKm().getKitByItem(p, e.getCurrentItem());
            if (k == null){
                return;
            }
            if (plugin.getCm().isDisableKitLevels()){
                KitLevel kl = k.getFirstLevel().getValue();
                disableKit(p, sw, isGame, type, k, kl);
            } else {
                plugin.getUim().createKitLevelSelectorMenu(p, k, type);
            }
        });
        actions.put(plugin.getLang().get("menus.preview.title"), (b) -> {
            InventoryClickEvent e = b.getInventoryClickEvent();
            e.setCancelled(true);
        });
        actions.put(plugin.getLang().get("menus.kitlevels.title"), (b) -> {
            InventoryClickEvent e = b.getInventoryClickEvent();
            String display = checkDisplayName(b);
            if (display.equals("none")) return;
            Player p = b.getPlayer();
            boolean isGame = plugin.getGm().isPlayerInGame(p);
            ItemStack item = e.getCurrentItem();
            String type = NBTEditor.getString(item, "KIT", "SELECTOR", "TYPE");
            if (type == null) return;
            if (display.equals(plugin.getLang().get(p, "menus.back.nameItem"))){
                plugin.getUim().createKitSelectorMenu(p, type, isGame);
                return;
            }
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            Kit k = plugin.getKm().getKitByItem(p, item);
            if (k == null){
                return;
            }
            KitLevel kl = plugin.getKm().getKitLevelByItem(k, p, item);
            disableKit(p, sw, isGame, type, k, kl);
        });
        actions.put(plugin.getLang().get("menus.kitsperks.title"), (b) -> {
            InventoryClickEvent e = b.getInventoryClickEvent();
            Player p = b.getPlayer();
            if (plugin.isSetupLobby(p)) return;
            String display = checkDisplayName(b);
            if (display.equals("none")) return;
            if (display.equals(plugin.getLang().get(p, "menus.kitsperks.kitsolo.nameItem"))){
                plugin.getUim().getPages().put(p.getUniqueId(), 1);
                plugin.getUim().createKitSelectorMenu(p, "SOLO", false);
            }
            if (display.equals(plugin.getLang().get(p, "menus.kitsperks.kitteam.nameItem"))){
                plugin.getUim().getPages().put(p.getUniqueId(), 1);
                plugin.getUim().createKitSelectorMenu(p, "TEAM", false);
            }
            if (display.equals(plugin.getLang().get(p, "menus.kitsperks.kitranked.nameItem"))){
                plugin.getUim().getPages().put(p.getUniqueId(), 1);
                plugin.getUim().createKitSelectorMenu(p, "RANKED", false);
            }
            if (display.equals(plugin.getLang().get(p, "menus.kitsperks.perksolo.nameItem"))){
                if (!plugin.getIjm().isPerksInjection()){
                    p.sendMessage(plugin.getLang().get(p, "injections.perks"));
                    return;
                }
                plugin.getGem().createPerksMenu(p, "SOLO");
            }
            if (display.equals(plugin.getLang().get(p, "menus.kitsperks.perkteam.nameItem"))){
                if (!plugin.getIjm().isPerksInjection()){
                    p.sendMessage(plugin.getLang().get(p, "injections.perks"));
                    return;
                }
                plugin.getGem().createPerksMenu(p, "TEAM");
            }
            if (display.equals(plugin.getLang().get(p, "menus.kitsperks.perkranked.nameItem"))){
                if (!plugin.getIjm().isPerksInjection()){
                    p.sendMessage(plugin.getLang().get(p, "injections.perks"));
                    return;
                }
                plugin.getGem().createPerksMenu(p, "RANKED");
            }
            if (display.equals(plugin.getLang().get("menus.kitsperks.close.nameItem"))){
                if (e.getClick().equals(ClickType.RIGHT)){
                    p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                    return;
                }
                p.closeInventory();
            }
        });
        actions.put(plugin.getLang().get("menus.windancesselector.title"), (b) -> {
            InventoryClickEvent e = b.getInventoryClickEvent();
            Player p = b.getPlayer();
            if (plugin.isSetupLobby(p)) return;
            String display = checkDisplayName(b);
            if (display.equals("none")) return;
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (display.equals(plugin.getLang().get(p, "menus.next.nameItem"))){
                plugin.getUim().addPage(p);
                plugin.getUim().createWinDanceSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.last.nameItem"))){
                plugin.getUim().removePage(p);
                plugin.getUim().createWinDanceSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.windancesselector.kit.nameItem"))){
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.windancesselector.deselect.nameItem"))){
                if (sw.getWinDance() == 999999){
                    p.sendMessage(plugin.getLang().get(p, "messages.noSelect"));
                    return;
                }
                sw.setWinDance(999999);
                p.sendMessage(plugin.getLang().get(p, "messages.deselectWinDance"));
                plugin.getUim().createWinDanceSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.windancesselector.close.nameItem"))){
                if (e.getClick().equals(ClickType.RIGHT)){
                    p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                    return;
                }
                p.closeInventory();
                return;
            }
            UltraWinDance k = plugin.getCos().getWinDanceByItem(e.getCurrentItem());
            if (k == null){
                return;
            }
            if (p.hasPermission(k.getAutoGivePermission())){
                sw.setWinDance(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectWinDance").replaceAll("<windance>", k.getName()));
                plugin.getUim().createWinDanceSelectorMenu(p);
                return;
            }
            if (!sw.getWindances().contains(k.getId())){
                if (k.needPermToBuy() && !p.hasPermission(k.getPermission())){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermit"));
                } else {
                    plugin.getShm().buy(p, k, k.getName());
                }
            } else {
                sw.setWinDance(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectWinDance").replaceAll("<windance>", k.getName()));
            }
            plugin.getUim().createWinDanceSelectorMenu(p);
        });
        actions.put(plugin.getLang().get("menus.killeffectsselector.title"), (b) -> {
            InventoryClickEvent e = b.getInventoryClickEvent();
            Player p = b.getPlayer();
            if (plugin.isSetupLobby(p)) return;
            String display = checkDisplayName(b);
            if (display.equals("none")) return;
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (display.equals(plugin.getLang().get(p, "menus.next.nameItem"))){
                plugin.getUim().addPage(p);
                plugin.getUim().createKillEffectSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.last.nameItem"))){
                plugin.getUim().removePage(p);
                plugin.getUim().createKillEffectSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.killeffectsselector.kit.nameItem"))){
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.killeffectsselector.deselect.nameItem"))){
                if (sw.getKillEffect() == 999999){
                    p.sendMessage(plugin.getLang().get(p, "messages.noSelect"));
                    return;
                }
                sw.setKillEffect(999999);
                p.sendMessage(plugin.getLang().get(p, "messages.deselectKillEffect"));
                plugin.getUim().createKillEffectSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.killeffectsselector.close.nameItem"))){
                if (e.getClick().equals(ClickType.RIGHT)){
                    p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                    return;
                }
                p.closeInventory();
                return;
            }
            UltraKillEffect k = plugin.getCos().getKillEffectByItem(e.getCurrentItem());
            if (k == null){
                return;
            }
            if (p.hasPermission(k.getAutoGivePermission())){
                sw.setKillEffect(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectKillEffect").replaceAll("<killeffect>", k.getName()));
                plugin.getUim().createKillEffectSelectorMenu(p);
                return;
            }
            if (!sw.getKilleffects().contains(k.getId())){
                if (k.needPermToBuy() && !p.hasPermission(k.getPermission())){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermit"));
                } else {
                    plugin.getShm().buy(p, k, k.getName());
                }
            } else {
                sw.setKillEffect(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectKillEffect").replaceAll("<killeffect>", k.getName()));
            }
            plugin.getUim().createKillEffectSelectorMenu(p);
        });
        actions.put(plugin.getLang().get("menus.wineffectsselector.title"), (b) -> {
            InventoryClickEvent e = b.getInventoryClickEvent();
            Player p = b.getPlayer();
            if (plugin.isSetupLobby(p)) return;
            String display = checkDisplayName(b);
            if (display.equals("none")) return;
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (display.equals(plugin.getLang().get(p, "menus.next.nameItem"))){
                plugin.getUim().addPage(p);
                plugin.getUim().createWinEffectSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.last.nameItem"))){
                plugin.getUim().removePage(p);
                plugin.getUim().createWinEffectSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.wineffectsselector.kit.nameItem"))){
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.wineffectsselector.deselect.nameItem"))){
                if (sw.getWinEffect() == 999999){
                    p.sendMessage(plugin.getLang().get(p, "messages.noSelect"));
                    return;
                }
                sw.setWinEffect(999999);
                p.sendMessage(plugin.getLang().get(p, "messages.deselectWinEffect"));
                plugin.getUim().createWinEffectSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.wineffectsselector.close.nameItem"))){
                if (e.getClick().equals(ClickType.RIGHT)){
                    p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                    return;
                }
                p.closeInventory();
                return;
            }
            UltraWinEffect k = plugin.getCos().getWinEffectByItem(e.getCurrentItem());
            if (k == null){
                return;
            }
            if (p.hasPermission(k.getAutoGivePermission())){
                sw.setWinEffect(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectWinEffect").replaceAll("<wineffect>", k.getName()));
                plugin.getUim().createWinEffectSelectorMenu(p);
                return;
            }
            if (!sw.getWineffects().contains(k.getId())){
                if (k.needPermToBuy() && !p.hasPermission(k.getPermission())){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermit"));
                } else {
                    plugin.getShm().buy(p, k, k.getName());
                }
            } else {
                sw.setWinEffect(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectWinEffect").replaceAll("<wineffect>", k.getName()));
            }
            plugin.getUim().createWinEffectSelectorMenu(p);
        });
        actions.put(plugin.getLang().get("menus.killsoundsselector.title"), (b) -> {
            InventoryClickEvent e = b.getInventoryClickEvent();
            Player p = b.getPlayer();
            if (plugin.isSetupLobby(p)) return;
            String display = checkDisplayName(b);
            if (display.equals("none")) return;
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (display.equals(plugin.getLang().get(p, "menus.next.nameItem"))){
                plugin.getUim().addPage(p);
                plugin.getUim().createKillSoundSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.last.nameItem"))){
                plugin.getUim().removePage(p);
                plugin.getUim().createKillSoundSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.killsoundsselector.kit.nameItem"))){
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.killsoundsselector.deselect.nameItem"))){
                if (sw.getKillSound() == 999999){
                    p.sendMessage(plugin.getLang().get(p, "messages.noSelect"));
                    return;
                }
                sw.setKillSound(999999);
                p.sendMessage(plugin.getLang().get(p, "messages.deselect"));
                plugin.getUim().createKillSoundSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.killsoundsselector.close.nameItem"))){
                if (e.getClick().equals(ClickType.RIGHT)){
                    p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                    return;
                }
                p.closeInventory();
                return;
            }
            KillSound k = plugin.getCos().getKillSoundByItem(e.getCurrentItem());
            if (k == null){
                return;
            }
            if (e.getClick().equals(ClickType.RIGHT)){
                p.playSound(p.getLocation(), k.getSound(), k.getVol1(), k.getVol2());
                return;
            }
            if (p.hasPermission(k.getAutoGivePermission())){
                sw.setKillSound(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectKillSound").replaceAll("<killsound>", k.getName()));
                plugin.getUim().createKillSoundSelectorMenu(p);
                return;
            }
            if (!sw.getKillsounds().contains(k.getId())){
                if (k.needPermToBuy() && !p.hasPermission(k.getPermission())){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermit"));
                } else {
                    plugin.getShm().buy(p, k, k.getName());
                }
            } else {
                sw.setKillSound(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectKillSound").replaceAll("<killsound>", k.getName()));
            }
            plugin.getUim().createKillSoundSelectorMenu(p);
        });
        actions.put(plugin.getLang().get("menus.balloonsselector.title"), (b) -> {
            InventoryClickEvent e = b.getInventoryClickEvent();
            Player p = b.getPlayer();
            if (plugin.isSetupLobby(p)) return;
            String display = checkDisplayName(b);
            if (display.equals("none")) return;
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (display.equals(plugin.getLang().get(p, "menus.next.nameItem"))){
                plugin.getUim().addPage(p);
                plugin.getUim().createBalloonSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.last.nameItem"))){
                plugin.getUim().removePage(p);
                plugin.getUim().createBalloonSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.balloonsselector.kit.nameItem"))){
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.balloonsselector.deselect.nameItem"))){
                if (sw.getBalloon() == 999999){
                    p.sendMessage(plugin.getLang().get(p, "messages.noSelect"));
                    return;
                }
                sw.setBalloon(999999);
                p.sendMessage(plugin.getLang().get(p, "messages.deselectBalloon"));
                plugin.getUim().createBalloonSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.balloonsselector.close.nameItem"))){
                if (e.getClick().equals(ClickType.RIGHT)){
                    p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                    return;
                }
                p.closeInventory();
                return;
            }
            Balloon k = plugin.getCos().getBalloonByItem(e.getCurrentItem());
            if (k == null){
                return;
            }
            if (e.getClick().equals(ClickType.RIGHT)){
                if (plugin.getCos().getPreviewCosmetic("balloon") == null){
                    p.sendMessage(plugin.getLang().get("setup.noBalloonPreview"));
                    return;
                }
                if (plugin.getCos().getPlayerPreview().containsKey(p.getUniqueId())){
                    p.sendMessage(plugin.getLang().get("setup.alreadyPreview"));
                    return;
                }
                p.closeInventory();
                PreviewCosmetic pc = plugin.getCos().getPreviewCosmetic("balloon");
                plugin.getCos().getPlayerPreview().put(p.getUniqueId(), "balloon");
                pc.addPreview(p);
                pc.execute(p, k.getId());
                return;
            }
            if (p.hasPermission(k.getAutoGivePermission())){
                sw.setBalloon(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectBalloon").replaceAll("<balloon>", k.getName()));
                plugin.getUim().createBalloonSelectorMenu(p);
                return;
            }
            if (!sw.getBalloons().contains(k.getId())){
                if (k.needPermToBuy() && !p.hasPermission(k.getPermission())){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermit"));
                } else {
                    plugin.getShm().buy(p, k, k.getName());
                }
            } else {
                sw.setBalloon(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectBalloon").replaceAll("<balloon>", k.getName()));
            }
            plugin.getUim().createBalloonSelectorMenu(p);
        });
        actions.put(plugin.getLang().get("menus.glassselector.title"), (b) -> {
            InventoryClickEvent e = b.getInventoryClickEvent();
            Player p = b.getPlayer();
            if (plugin.isSetupLobby(p)) return;
            String display = checkDisplayName(b);
            if (display.equals("none")) return;
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (display.equals(plugin.getLang().get(p, "menus.next.nameItem"))){
                plugin.getUim().addPage(p);
                plugin.getUim().createGlassSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.last.nameItem"))){
                plugin.getUim().removePage(p);
                plugin.getUim().createGlassSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.glassselector.kit.nameItem"))){
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.glassselector.deselect.nameItem"))){
                if (sw.getGlass() == 999999){
                    p.sendMessage(plugin.getLang().get(p, "messages.noSelect"));
                    return;
                }
                sw.setGlass(999999);
                p.sendMessage(plugin.getLang().get(p, "messages.deselectGlass"));
                plugin.getUim().createGlassSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.glassselector.close.nameItem"))){
                if (e.getClick().equals(ClickType.RIGHT)){
                    p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                    return;
                }
                p.closeInventory();
                return;
            }
            Glass k = plugin.getCos().getGlassByItem(e.getCurrentItem());
            if (k == null){
                return;
            }
            if (e.getClick().equals(ClickType.RIGHT)){
                if (plugin.getCos().getPreviewCosmetic("glass") == null){
                    p.sendMessage(plugin.getLang().get("setup.noGlassPreview"));
                    return;
                }
                if (plugin.getCos().getPlayerPreview().containsKey(p.getUniqueId())){
                    p.sendMessage(plugin.getLang().get("setup.alreadyPreview"));
                    return;
                }
                p.closeInventory();
                PreviewCosmetic pc = plugin.getCos().getPreviewCosmetic("glass");
                plugin.getCos().getPlayerPreview().put(p.getUniqueId(), "glass");
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    pc.addPreview(p);
                    pc.execute(p, k.getId());
                }, 1L);
                return;
            }
            if (p.hasPermission(k.getAutoGivePermission())){
                sw.setGlass(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectGlass").replaceAll("<glass>", k.getName()));
                plugin.getUim().createGlassSelectorMenu(p);
                return;
            }
            if (!sw.getGlasses().contains(k.getId())){
                if (k.needPermToBuy() && !p.hasPermission(k.getPermission())){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermit"));
                } else {
                    plugin.getShm().buy(p, k, k.getName());
                }
            } else {
                sw.setGlass(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectGlass").replaceAll("<glass>", k.getName()));
            }
            plugin.getUim().createGlassSelectorMenu(p);
        });
        actions.put(plugin.getLang().get("menus.partingselector.title"), (b) -> {
            InventoryClickEvent e = b.getInventoryClickEvent();
            Player p = b.getPlayer();
            if (plugin.isSetupLobby(p)) return;
            String display = checkDisplayName(b);
            if (display.equals("none")) return;
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (display.equals(plugin.getLang().get(p, "menus.next.nameItem"))){
                plugin.getUim().addPage(p);
                plugin.getUim().createTauntsSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.last.nameItem"))){
                plugin.getUim().removePage(p);
                plugin.getUim().createTauntsSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.partingselector.kit.nameItem"))){
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.partingselector.deselect.nameItem"))){
                if (sw.getParting() == 999999){
                    p.sendMessage(plugin.getLang().get(p, "messages.noSelect"));
                    return;
                }
                sw.setParting(999999);
                p.sendMessage(plugin.getLang().get(p, "messages.deselectParting"));
                plugin.getUim().createPartingSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.partingselector.close.nameItem"))){
                if (e.getClick().equals(ClickType.RIGHT)){
                    p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                    return;
                }
                p.closeInventory();
                return;
            }
            Parting k = plugin.getCos().getPartingByItem(e.getCurrentItem());
            if (k == null){
                return;
            }
            if (p.hasPermission(k.getAutoGivePermission())){
                sw.setParting(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectParting").replaceAll("<parting>", k.getName()));
                plugin.getUim().createPartingSelectorMenu(p);
                return;
            }
            if (!sw.getPartings().contains(k.getId())){
                if (k.needPermToBuy() && !p.hasPermission(k.getPermission())){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermit"));
                } else {
                    plugin.getShm().buy(p, k, k.getName());
                }
            } else {
                sw.setParting(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectParting").replaceAll("<parting>", k.getName()));
            }
            plugin.getUim().createPartingSelectorMenu(p);
        });
        actions.put(plugin.getLang().get("menus.tauntsselector.title"), (b) -> {
            InventoryClickEvent e = b.getInventoryClickEvent();
            Player p = b.getPlayer();
            if (plugin.isSetupLobby(p)) return;
            String display = checkDisplayName(b);
            if (display.equals("none")) return;
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (display.equals(plugin.getLang().get(p, "menus.next.nameItem"))){
                plugin.getUim().addPage(p);
                plugin.getUim().createTauntsSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.last.nameItem"))){
                plugin.getUim().removePage(p);
                plugin.getUim().createTauntsSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.tauntsselector.kit.nameItem"))){
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.tauntsselector.deselect.nameItem"))){
                if (sw.getTaunt() == 999999){
                    p.sendMessage(plugin.getLang().get(p, "messages.noSelect"));
                    return;
                }
                sw.setTaunt(999999);
                p.sendMessage(plugin.getLang().get(p, "messages.deselectTaunt"));
                plugin.getUim().createTauntsSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.tauntsselector.close.nameItem"))){
                if (e.getClick().equals(ClickType.RIGHT)){
                    p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                    return;
                }
                p.closeInventory();
                return;
            }
            Taunt k = plugin.getCos().getTauntByItem(e.getCurrentItem());
            if (k == null){
                return;
            }
            if (e.getClick().equals(ClickType.RIGHT)){
                k.executePreview(p);
                return;
            }
            if (p.hasPermission(k.getAutoGivePermission())){
                sw.setTaunt(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectTaunt").replaceAll("<taunt>", k.getName()));
                plugin.getUim().createTauntsSelectorMenu(p);
                return;
            }
            if (!sw.getTaunts().contains(k.getId())){
                if (k.needPermToBuy() && !p.hasPermission(k.getPermission())){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermit"));
                } else {
                    plugin.getShm().buy(p, k, k.getName());
                }
            } else {
                sw.setTaunt(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectTaunt").replaceAll("<taunt>", k.getName()));
            }
            plugin.getUim().createTauntsSelectorMenu(p);
        });
        actions.put(plugin.getLang().get("menus.trailsselector.title"), (b) -> {
            InventoryClickEvent e = b.getInventoryClickEvent();
            Player p = b.getPlayer();
            if (plugin.isSetupLobby(p)) return;
            String display = checkDisplayName(b);
            if (display.equals("none")) return;
            if (display.equals(plugin.getLang().get(p, "menus.next.nameItem"))){
                plugin.getUim().addPage(p);
                plugin.getUim().createTrailsSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.last.nameItem"))){
                plugin.getUim().removePage(p);
                plugin.getUim().createTrailsSelectorMenu(p);
                return;
            }
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (display.equals(plugin.getLang().get(p, "menus.trailsselector.kit.nameItem"))){
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.trailsselector.deselect.nameItem"))){
                if (sw.getTrail() == 999999){
                    p.sendMessage(plugin.getLang().get(p, "messages.noSelect"));
                    return;
                }
                sw.setTrail(999999);
                p.sendMessage(plugin.getLang().get(p, "messages.deselectTrail"));
                plugin.getUim().createTrailsSelectorMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.trailsselector.close.nameItem"))){
                if (e.getClick().equals(ClickType.RIGHT)){
                    p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                    return;
                }
                p.closeInventory();
                return;
            }
            Trail k = plugin.getCos().getTrailByItem(e.getCurrentItem());
            if (k == null){
                return;
            }
            if (p.hasPermission(k.getAutoGivePermission())){
                sw.setTrail(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectTrail").replaceAll("<trail>", k.getName()));
                plugin.getUim().createTrailsSelectorMenu(p);
                return;
            }
            if (!sw.getTrails().contains(k.getId())){
                if (k.needPermToBuy() && !p.hasPermission(k.getPermission())){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermit"));
                } else {
                    plugin.getShm().buy(p, k, k.getName());
                }
            } else {
                sw.setTrail(k.getId());
                p.sendMessage(plugin.getLang().get(p, "messages.selectTrail").replaceAll("<trail>", k.getName()));
            }
            plugin.getUim().createTrailsSelectorMenu(p);
        });
        actions.put(plugin.getLang().get("menus.lobby.title"), (b) -> {
            Player p = b.getPlayer();
            if (plugin.isSetupLobby(p)) return;
            String display = checkDisplayName(b);
            if (display.equals("none")) return;
            if (display.equals(plugin.getLang().get(p, "menus.lobby.soulwell.nameItem"))){
                if (!plugin.getIjm().isSoulWellInjection()){
                    p.sendMessage(plugin.getLang().get(p, "injections.soulwell"));
                    return;
                }
                plugin.getIjm().getSoulwell().getWel().createSoulWellMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.lobby.levels.nameItem"))){
                plugin.getGem().createLevelMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.lobby.perksKits.nameItem"))){
                plugin.getUim().openContentInventory(p, plugin.getUim().getMenus("kitsperks"));
            }
            if (display.equals(plugin.getLang().get(p, "menus.lobby.trails.nameItem"))){
                plugin.getUim().getPages().put(p.getUniqueId(), 1);
                plugin.getUim().createTrailsSelectorMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.lobby.parting.nameItem"))){
                plugin.getUim().getPages().put(p.getUniqueId(), 1);
                plugin.getUim().createPartingSelectorMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.lobby.taunts.nameItem"))){
                plugin.getUim().getPages().put(p.getUniqueId(), 1);
                plugin.getUim().createTauntsSelectorMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.lobby.balloons.nameItem"))){
                plugin.getUim().getPages().put(p.getUniqueId(), 1);
                plugin.getUim().createBalloonSelectorMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.lobby.glass.nameItem"))){
                plugin.getUim().getPages().put(p.getUniqueId(), 1);
                plugin.getUim().createGlassSelectorMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.lobby.wineffects.nameItem"))){
                plugin.getUim().getPages().put(p.getUniqueId(), 1);
                plugin.getUim().createWinEffectSelectorMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.lobby.killeffects.nameItem"))){
                plugin.getUim().getPages().put(p.getUniqueId(), 1);
                plugin.getUim().createKillEffectSelectorMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.lobby.windances.nameItem"))){
                plugin.getUim().getPages().put(p.getUniqueId(), 1);
                plugin.getUim().createWinDanceSelectorMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.lobby.killsound.nameItem"))){
                plugin.getUim().getPages().put(p.getUniqueId(), 1);
                plugin.getUim().createKillSoundSelectorMenu(p);
            }
        });
        actions.put(plugin.getLang().get("menus.teamselector.title"), (b) -> {
            InventoryClickEvent e = b.getInventoryClickEvent();
            String display = checkDisplayName(b);
            if (display.equals("none")) return;
            Player p = b.getPlayer();
            Game game = plugin.getGm().getGameByPlayer(p);
            if (game == null){
                return;
            }
            UltraTeamGame utg = (UltraTeamGame) game;
            int id = NBTEditor.getInt(e.getCurrentItem(), "TEAM", "ID");
            Team team = utg.getTeamByID(id);
            if (team == null){
                return;
            }
            if (team.getTeamSize() >= game.getTeamSize()){
                p.sendMessage(plugin.getLang().get(p, "messages.teamFull"));
                return;
            }
            p.closeInventory();
            game.removePlayerAllTeam(p);
            game.addPlayerTeam(p, team);
            plugin.getGem().getViews().entrySet().stream().filter(c -> c.getValue().equals("teams")).filter(c -> game.getPlayers().contains(p)).forEach(u -> {
                Player v = Bukkit.getPlayer(u.getKey());
                plugin.getGem().updateTeamSelector(game, v.getOpenInventory().getTopInventory());
            });
            p.sendMessage(plugin.getLang().get(p, "messages.joinTeam").replaceAll("<id>", String.valueOf((id + 1))));
        });
        actions.put(plugin.getLang().get("menus.perks.title"), (b) -> {
            InventoryClickEvent e = b.getInventoryClickEvent();
            String display = checkDisplayName(b);
            if (display.equals("none")) return;
            Player p = b.getPlayer();
            if (display.equals(plugin.getLang().get(p, "menus.back.nameItem"))){
                plugin.getUim().openContentInventory(p, plugin.getUim().getMenus("kitsperks"));
                return;
            }
            String[] data = NBTEditor.getString(e.getCurrentItem(), "PERK_DATA").split(":");
            int id = Integer.parseInt(data[0]);
            boolean locked = Boolean.parseBoolean(data[1]);
            int level = Integer.parseInt(data[2]);
            boolean maxed = Boolean.parseBoolean(data[3]);
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (e.isRightClick()){
                if (locked){
                    p.sendMessage(plugin.getLang().get("messages.perks.perkLocked"));
                    CustomSound.PERK_LOCKED.reproduce(p);
                    return;
                }
                if (sw.getPerksEnabled().contains(id)){
                    sw.getPerksEnabled().remove(id);
                    CustomSound.PERK_OFF.reproduce(p);
                } else {
                    sw.getPerksEnabled().add(id);
                    CustomSound.PERK_ON.reproduce(p);
                }
                p.sendMessage(plugin.getLang().get("messages.perks.toggle").replace("<state>", (!sw.getPerksEnabled().contains(id) ? plugin.getLang().get("messages.perks.onState") : plugin.getLang().get("messages.perks.offState"))));
            } else {
                if (maxed){
                    p.sendMessage(plugin.getLang().get("messages.perks.maxedPerk"));
                    CustomSound.PERK_MAXED.reproduce(p);
                    return;
                }
                Perk perk = plugin.getIjm().getPerks().getPem().getPerkByID(id);
                PerkLevel next = perk.getLevels().get(level + 1);
                if (plugin.getAdm().getCoins(p) < next.getPrice()){
                    p.sendMessage(plugin.getLang().get("messages.perks.noCoins"));
                    CustomSound.PERK_NO_COINS.reproduce(p);
                    return;
                }
                sw.getPerksData().put(id, level + 1);
                plugin.getAdm().removeCoins(p, next.getPrice());
                p.sendMessage(plugin.getLang().get("messages.perks.buyed").replace("<level>", String.valueOf(level + 1)));
                CustomSound.PERK_BUY.reproduce(p);
            }
            p.closeInventory();
        });
        actions.put(plugin.getLang().get("menus.levels.title"), (b) -> {
            InventoryClickEvent e = b.getInventoryClickEvent();
            String display = checkDisplayName(b);
            if (display.equals("none")) return;
            Player p = b.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (display.equals(plugin.getLang().get(p, "menus.levels.prestige.nameItem"))){
                plugin.getGem().createPrestigeIcons(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.levels.hide.nameItem"))){
                sw.setShowLevel(false);
                p.sendMessage(plugin.getLang().get("messages.showPrefix").replaceAll("<status>", Utils.parseBoolean(sw.isShowLevel())));
                plugin.getGem().createLevelMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.levels.show.nameItem"))){
                sw.setShowLevel(true);
                p.sendMessage(plugin.getLang().get("messages.showPrefix").replaceAll("<status>", Utils.parseBoolean(sw.isShowLevel())));
                plugin.getGem().createLevelMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.close.nameItem"))){
                if (e.getClick().equals(ClickType.RIGHT)){
                    p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                    return;
                }
                p.closeInventory();
            }
        });
        actions.put(plugin.getLang().get("menus.prestige.title"), (b) -> {
            InventoryClickEvent e = b.getInventoryClickEvent();
            String display = checkDisplayName(b);
            if (display.equals("none")) return;
            Player p = b.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            ItemStack item = e.getCurrentItem();
            if (item.getItemMeta().getDisplayName().equals(plugin.getLang().get(p, "menus.back.nameItem"))){
                plugin.getGem().createLevelMenu(p);
                return;
            }
            if (item.getItemMeta().getDisplayName().equals(plugin.getLang().get(p, "menus.close.nameItem"))){
                p.closeInventory();
                return;
            }
            String id = NBTEditor.getString(item, "PRESTIGE_ICON_ID");
            boolean has = NBTEditor.getBoolean(item, "PRESTIGE_ICON_HAS");
            if (!has){
                p.sendMessage(plugin.getLang().get("messages.levels.prestigeNoHas"));
                CustomSound.PRESTIGE_NO_HAS.reproduce(p);
                return;
            }
            if (sw.getPrestigeIcon().equals(id)){
                p.sendMessage(plugin.getLang().get("messages.levels.alreadySelected"));
                CustomSound.PRESTIGE_ALREADY_SELECTED.reproduce(p);
            } else {
                PrestigeIcon pi = plugin.getLvl().getPrestige().get(id);
                p.sendMessage(plugin.getLang().get("messages.levels.selectPrestige").replace("<name>", pi.getName()));
                sw.setPrestigeIcon(id);
                plugin.getGem().createPrestigeIcons(p);
                CustomSound.PRESTIGE_SELECT.reproduce(p);
            }
        });
    }
    
    private void disableKit(Player p, SWPlayer sw, boolean isGame, String type, Kit k, KitLevel kl){
        if (kl == null){
            return;
        }
        if (p.hasPermission(kl.getAutoGivePermission())){
            selectKit(p, sw, type, k, kl);
            plugin.getUim().createKitSelectorMenu(p, type, isGame);
            return;
        }
        if (!sw.hasKitLevel(k.getId(), kl.getLevel())){
            if (kl.needPermToBuy() && !p.hasPermission(k.getPermission())){
                p.sendMessage(plugin.getLang().get(p, "messages.noPermit"));
            } else {
                plugin.getShm().buy(p, kl, k.getName());
            }
        } else {
            selectKit(p, sw, type, k, kl);
        }
        plugin.getUim().createKitSelectorMenu(p, type, isGame);
    }
    
    private void selectKit(Player p, SWPlayer sw, String type, Kit k, KitLevel kl){
        if (type.equals("SOLO")){
            sw.setSoloKit(k.getId());
            sw.setSoloKitLevel(kl.getLevel());
        } else if (type.equals("TEAM")){
            sw.setTeamKit(k.getId());
            sw.setTeamKitLevel(kl.getLevel());
        } else {
            sw.setRankedKit(k.getId());
            sw.setRankedKitLevel(kl.getLevel());
        }
        p.sendMessage(plugin.getLang().get(p, "messages.select").replaceAll("<kit>", k.getName()));
    }
    
    public String checkDisplayName(InventoryAction b){
        InventoryClickEvent e = b.getInventoryClickEvent();
        e.setCancelled(true);
        if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
            return "none";
        }
        ItemStack item = e.getCurrentItem();
        if (!item.hasItemMeta()){
            return "none";
        }
        if (!item.getItemMeta().hasDisplayName()){
            return "none";
        }
        ItemMeta im = item.getItemMeta();
        return im.getDisplayName();
    }
    
    public HashMap<String, UltraInventory> getMenus(){
        return menus;
    }
    
    public HashMap<String, Consumer<InventoryAction>> getActions(){
        return actions;
    }
}