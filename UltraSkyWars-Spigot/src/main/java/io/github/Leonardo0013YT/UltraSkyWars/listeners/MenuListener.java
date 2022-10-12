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

package io.github.Leonardo0013YT.UltraSkyWars.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.actions.InventoryAction;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.OrderType;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.State;
import io.github.Leonardo0013YT.UltraSkyWars.api.game.GameData;
import io.github.Leonardo0013YT.UltraSkyWars.api.game.GamePlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.game.UltraGameChest;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.GameEvent;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.UltraInventory;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MenuListener implements Listener {
    
    private final HashMap<UUID, Location> chests = new HashMap<>();
    
    @EventHandler
    public void onClose(InventoryCloseEvent e){
        Player p = (Player) e.getPlayer();
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        if (plugin.getCm().isChestHolograms()){
            if (chests.containsKey(p.getUniqueId())){
                if (plugin.getGm().isPlayerInGame(p)){
                    Game game = plugin.getGm().getGameByPlayer(p);
                    if (e.getInventory().getType().equals(InventoryType.CHEST)){
                        UltraGameChest ugc = game.getChestByLocation(chests.get(p.getUniqueId()));
                        if (ugc != null){
                            GameEvent ge = game.getNowEvent();
                            ugc.spawn(Utils.isEmpty(ugc.getInv()));
                            if (ge != null){
                                String text = plugin.getLang().get("timer").replace("<time>", Utils.convertTime(ge.getTime()));
                                ugc.updateTimer(text);
                            }
                        }
                    }
                    chests.remove(p.getUniqueId());
                }
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get("menus.teamselector.title"))){
            plugin.getGem().getViews().remove(p.getUniqueId());
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        if (plugin.getCm().isChestHolograms()){
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getType().name().endsWith("CHEST")){
                if (!plugin.getGm().isPlayerInGame(p)){
                    return;
                }
                Game game = plugin.getGm().getGameByPlayer(p);
                GameEvent ge = game.getNowEvent();
                if (ge != null){
                    if (game.isState(State.GAME) && ge.getName().equals("refill")){
                        chests.put(p.getUniqueId(), e.getClickedBlock().getLocation());
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onMenu(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        Game g = plugin.getGm().getGameByPlayer(p);
        if (!e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)){
            List<ItemStack> items = new ArrayList<>();
            items.add(e.getCurrentItem());
            items.add(e.getCursor());
            items.add((e.getClick() == ClickType.NUMBER_KEY) ? e.getWhoClicked().getInventory().getItem(e.getHotbarButton()) : e.getCurrentItem());
            for ( ItemStack item : items ){
                if (plugin.getIm().getItems().contains(item)){
                    e.setCancelled(true);
                }
            }
        }
        if (plugin.getUim().getActions().containsKey(e.getView().getTitle())){
            plugin.getUim().getActions().get(e.getView().getTitle()).accept(new InventoryAction(e, p));
            return;
        }
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.selector.title").replaceAll("<type>", plugin.getLang().get(p, "selector.tnt_madness"))) || e.getView().getTitle().equals(plugin.getLang().get(p, "menus.selector.title").replaceAll("<type>", plugin.getLang().get(p, "selector.all"))) || e.getView().getTitle().equals(plugin.getLang().get(p, "menus.selector.title").replaceAll("<type>", plugin.getLang().get(p, "selector.normal"))) || e.getView().getTitle().equals(plugin.getLang().get(p, "menus.selector.title").replaceAll("<type>", plugin.getLang().get(p, "selector.team"))) || e.getView().getTitle().equals(plugin.getLang().get(p, "menus.selector.title").replaceAll("<type>", plugin.getLang().get(p, "selector.ranked")))){
            if (plugin.isSetupLobby(p)) return;
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()){
                return;
            }
            ItemMeta im = item.getItemMeta();
            String display = im.getDisplayName();
            if (display.equals(plugin.getLang().get(p, "menus.close.nameItem"))){
                if (e.getClick().equals(ClickType.RIGHT)){
                    p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                    return;
                }
                p.closeInventory();
                return;
            }
            String gameType = "SOLO";
            if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.selector.title").replaceAll("<type>", plugin.getLang().get(p, "selector.team")))){
                gameType = "TEAM";
            } else if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.selector.title").replaceAll("<type>", plugin.getLang().get(p, "selector.ranked")))){
                gameType = "RANKED";
            } else if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.selector.title").replaceAll("<type>", plugin.getLang().get(p, "selector.all")))){
                gameType = "ALL";
            } else if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.selector.title").replaceAll("<type>", plugin.getLang().get(p, "selector.tnt_madness")))){
                gameType = "TNT_MADNESS";
            }
            if (display.equals(plugin.getLang().get(p, "menus.next.nameItem"))){
                plugin.getUim().addPage(p);
                createMenuGames(p, gameType, plugin);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.last.nameItem"))){
                plugin.getUim().removePage(p);
                createMenuGames(p, gameType, plugin);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.selector.random.nameItem"))){
                plugin.getGm().removePlayerAllGame(p);
                boolean added = plugin.getGm().addRandomGame(p, gameType);
                if (!added){
                    p.sendMessage(plugin.getLang().get(p, "messages.noRandom"));
                }
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.selector.favorites.nameItem"))){
                if (!p.hasPermission("ultraskywars.selector.favorites")){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                GameData game = plugin.getGm().getGameRandomFavorites(p, gameType);
                if (game != null){
                    plugin.getGm().removePlayerAllGame(p);
                    plugin.getGm().addPlayerGame(p, plugin.getGm().getGameID(game.getMap()));
                } else {
                    p.sendMessage(plugin.getLang().get(p, "messages.noRandom"));
                }
                return;
            }
            String name = display.replaceFirst("§b", "").replaceFirst("§e", "");
            GameData game = plugin.getGm().getGameData().get(name);
            if (e.getClick().equals(ClickType.RIGHT)){
                if (!p.hasPermission("ultraskywars.selector.favorites")){
                    p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                    return;
                }
                SWPlayer sw = plugin.getDb().getSWPlayer(p);
                if (sw.getFavorites().contains(name)){
                    sw.getFavorites().remove(name);
                    p.sendMessage(plugin.getLang().get(p, "messages.favoriteRemove").replaceAll("<map>", name));
                } else {
                    sw.getFavorites().add(name);
                    p.sendMessage(plugin.getLang().get(p, "messages.favoriteAdd").replaceAll("<map>", name));
                }
                createMenuGames(p, gameType, plugin);
            } else {
                if (game != null){
                    plugin.getGm().removePlayerAllGame(p);
                    plugin.getGm().addPlayerGame(p, plugin.getGm().getGameID(game.getMap()));
                }
                p.closeInventory();
            }
        }
        if (g == null){
            return;
        }
        UltraInventory sp = plugin.getUim().getMenus("players");
        if (e.getView().getTitle().equals(sp.getTitle())){
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                return;
            }
            ItemStack it = e.getCurrentItem();
            if (!it.hasItemMeta()){
                return;
            }
            if (!it.getItemMeta().hasDisplayName()){
                return;
            }
            ItemMeta im = it.getItemMeta();
            String display = im.getDisplayName();
            if (sp.getContents().containsKey(e.getSlot())){
                ItemStack item = sp.getContents().get(e.getSlot());
                if (!item.hasItemMeta()){
                    return;
                }
                if (!item.getItemMeta().hasDisplayName()){
                    return;
                }
                ItemMeta im2 = item.getItemMeta();
                String display2 = im2.getDisplayName();
                if (display2.equals(plugin.getLang().get(p, "menus.players.close.nameItem"))){
                    if (e.getClick().equals(ClickType.RIGHT)){
                        p.sendMessage(plugin.getLang().get(p, "messages.closeWithClick"));
                        return;
                    }
                    p.closeInventory();
                    return;
                }
                if (display2.equals(plugin.getLang().get(p, "menus.players.order.nameItem"))){
                    GamePlayer gp = g.getGamePlayer().get(p.getUniqueId());
                    if (gp.getOrderType().equals(OrderType.NONE) || gp.getOrderType().equals(OrderType.PLAYERS)){
                        gp.setOrderType(OrderType.ALPHABETICALLY);
                    } else if (gp.getOrderType().equals(OrderType.ALPHABETICALLY)){
                        gp.setOrderType(OrderType.KILLS);
                    } else {
                        gp.setOrderType(OrderType.NONE);
                    }
                    plugin.getUim().openPlayersInventory(p, plugin.getUim().getMenus("players"), g, gp.getOrderType(), new String[]{"<order>", plugin.getLang().get(p, "order." + gp.getOrderType().name())});
                    return;
                }
                return;
            }
            String name = display.replaceFirst("§e", "");
            Player on = Bukkit.getPlayer(name);
            if (on == null){
                return;
            }
            p.teleport(on);
            p.closeInventory();
            p.sendMessage(plugin.getLang().get(p, "messages.teleported").replaceAll("<player>", on.getName()));
        }
    }
    
    private void createMenuGames(Player p, String gameType, UltraSkyWarsApi plugin){
        plugin.getGem().createSelectorMenu(p, "none", gameType.toLowerCase());
    }
    
}