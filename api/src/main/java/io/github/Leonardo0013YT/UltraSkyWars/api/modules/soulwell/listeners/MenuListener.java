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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.CustomSound;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.InjectionSoulWell;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.soulwell.SoulWell;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.soulwell.SoulWellSession;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.soulwell.SoulWellShop;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.soulwell.upgrades.SoulWellAngelOfDeath;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.soulwell.upgrades.SoulWellUpgrade;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuListener implements Listener {
    
    private final UltraSkyWarsApi plugin;
    private final InjectionSoulWell is;
    
    public MenuListener(UltraSkyWarsApi plugin, InjectionSoulWell is){
        this.plugin = plugin;
        this.is = is;
    }
    
    @EventHandler
    public void onMenu(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.heads.title"))){
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                return;
            }
            String display = e.getCurrentItem().getItemMeta().getDisplayName();
            if (display.equals(plugin.getLang().get(p, "menus.back.nameItem"))){
                is.getWel().createSoulWellMenu(p);
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.angelofdeath.title"))){
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta()){
                return;
            }
            if (!item.getItemMeta().hasDisplayName()){
                return;
            }
            ItemMeta im = item.getItemMeta();
            String display = im.getDisplayName();
            if (display.equals(plugin.getLang().get(p, "menus.cancel.nameItem"))){
                p.closeInventory();
                return;
            }
            String key = NBTEditor.getString(item, "SOULWELL", "ANGELDEATH", "KEY");
            if (key == null) return;
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            SoulWellAngelOfDeath sa = is.getSwm().getAngel().get(key);
            plugin.getAdm().removeCoins(p, sa.getPrice());
            sw.setSoulWellHead(sa.getLevel());
            p.closeInventory();
            p.sendMessage(is.getSoulwell().get("angelBuyed").replaceAll("<name>", sa.getName()));
        }
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.soulwellupgrade.title"))){
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta()){
                return;
            }
            if (!item.getItemMeta().hasDisplayName()){
                return;
            }
            ItemMeta im = item.getItemMeta();
            String display = im.getDisplayName();
            if (display.equals(plugin.getLang().get(p, "menus.back.nameItem"))){
                is.getWel().createSoulWellMenu(p);
                return;
            }
            String key = NBTEditor.getString(item, "SOULWELL", "UPGRADE", "KEY");
            if (key == null) return;
            int level = NBTEditor.getInt(item, "SOULWELL", "UPGRADE", "LEVEL");
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (is.getSwm().getExtra().containsKey(key)){
                SoulWellUpgrade swu = is.getSwm().getExtraByLevel(level);
                if (sw.getSoulWellExtra() == swu.getLevel()){
                    p.sendMessage(is.getSoulwell().get("maxed"));
                    return;
                }
                if (swu.getPrice() > plugin.getAdm().getCoins(p)){
                    p.sendMessage(is.getSoulwell().get("noMoney"));
                    return;
                }
                plugin.getAdm().removeCoins(p, swu.getPrice());
                sw.setSoulWellExtra(swu.getLevel());
                p.sendMessage(is.getSoulwell().get("extraBuyed").replaceAll("<name>", swu.getName()));
                is.getWel().createUpgradeSoulWellMenu(p);
            }
            if (is.getSwm().getMax().containsKey(key)){
                SoulWellUpgrade swu = is.getSwm().getMaxByLevel(level);
                if (sw.getSoulWellMax() == swu.getLevel()){
                    p.sendMessage(is.getSoulwell().get("maxed"));
                    return;
                }
                if (swu.getPrice() > plugin.getAdm().getCoins(p)){
                    p.sendMessage(is.getSoulwell().get("noMoney"));
                    return;
                }
                plugin.getAdm().removeCoins(p, swu.getPrice());
                sw.setSoulWellMax(swu.getLevel());
                p.sendMessage(is.getSoulwell().get("maxBuyed").replaceAll("<name>", swu.getName()));
                is.getWel().createUpgradeSoulWellMenu(p);
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.soulwellshop.title"))){
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta()){
                return;
            }
            if (!item.getItemMeta().hasDisplayName()){
                return;
            }
            ItemMeta im = item.getItemMeta();
            String display = im.getDisplayName();
            if (display.equals(plugin.getLang().get(p, "menus.back.nameItem"))){
                is.getWel().createSoulWellMenu(p);
                return;
            }
            String key = NBTEditor.getString(item, "SOULWELL", "SHOP", "SOULS");
            if (key == null) return;
            SoulWellShop s = is.getSwm().getShops().get(key);
            if (s == null) return;
            if (s.getPrice() > plugin.getAdm().getCoins(p)){
                p.sendMessage(is.getSoulwell().get("noMoney"));
                return;
            }
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw.getSouls() + s.getGive() > is.getSwm().getMaxSouls(sw.getSoulWellMax())){
                p.sendMessage(plugin.getLang().get("messages.maxSouls"));
                return;
            }
            plugin.getAdm().removeCoins(p, s.getPrice());
            sw.addSouls(s.getGive());
            p.sendMessage(is.getSoulwell().get("buyed").replaceAll("<name>", s.getName()).replaceAll("<souls>", "" + s.getGive()));
            is.getWel().createShopSoulWellMenu(p);
        }
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.soulwells.title"))){
            if (plugin.isSetupLobby(p)) return;
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta()){
                return;
            }
            if (!item.getItemMeta().hasDisplayName()){
                return;
            }
            ItemMeta im = item.getItemMeta();
            String display = im.getDisplayName();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (display.equals(plugin.getLang().get(p, "menus.soulwells.soulwell.nameItem"))){
                if (!is.getSwm().getSoulWells().isEmpty()){
                    SoulWell sel = is.getSwm().getSoulWells().values().stream().findAny().orElse(null);
                    if (sel != null){
                        p.teleport(sel.getLoc().clone().add(2, 0, 0));
                    } else {
                        p.sendMessage(plugin.getLang().get("messages.noAvailable"));
                    }
                } else {
                    p.sendMessage(plugin.getLang().get("messages.noAvailable"));
                }
                p.closeInventory();
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.close.nameItem"))){
                p.closeInventory();
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.soulwells.shop.nameItem"))){
                is.getWel().createShopSoulWellMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.soulwells.upgrades.nameItem"))){
                is.getWel().createUpgradeSoulWellMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.soulwells.settings.nameItem"))){
                is.getWel().createSettingsMenu(p);
                return;
            }
            if (display.equals(plugin.getLang().get(p, "menus.soulwells.heads.nameItem"))){
                is.getWel().createHeadsSoulWellMenu(p);
                return;
            }
            String key = NBTEditor.getString(item, "SOULWELL", "ANGELDEATH", "KEY");
            if (key == null) return;
            SoulWellAngelOfDeath sa = is.getSwm().getAngel().get(key);
            if (sa == null) return;
            if (sw.getSoulWellHead() == sa.getLevel()){
                p.sendMessage(is.getSoulwell().get("maxed"));
                return;
            }
            if (sa.getPrice() > plugin.getAdm().getCoins(p)){
                p.sendMessage(is.getSoulwell().get("noMoney"));
                return;
            }
            is.getWel().createSoulWellAngelMenu(p);
        }
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.soulwellsettings.title"))){
            if (plugin.isSetupLobby(p)) return;
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta()){
                return;
            }
            if (!item.getItemMeta().hasDisplayName()){
                return;
            }
            ItemMeta im = item.getItemMeta();
            String display = im.getDisplayName();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (display.equals(plugin.getLang().get(p, "menus.soulwellsettings.normal.nameItem"))){
                sw.setSoulanimation(0);
                is.getWel().createSettingsMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.soulwellsettings.blaze.nameItem"))){
                sw.setSoulanimation(1);
                is.getWel().createSettingsMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.soulwellsettings.3d.nameItem"))){
                sw.setSoulanimation(2);
                is.getWel().createSettingsMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.soulwellsettings.add.nameItem"))){
                if (sw.getRows() == 5){
                    p.sendMessage(plugin.getLang().get(p, "messages.maxRows"));
                    return;
                }
                sw.addRows(1);
                is.getWel().createSettingsMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.soulwellsettings.remove.nameItem"))){
                if (sw.getRows() == 1){
                    p.sendMessage(plugin.getLang().get(p, "messages.minRows"));
                    return;
                }
                sw.removeRows(1);
                is.getWel().createSettingsMenu(p);
            }
        }
        if (e.getView().getTitle().equals(plugin.getLang().get(p, "menus.soulwellmenu.title"))){
            if (plugin.isSetupLobby(p)) return;
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (!item.hasItemMeta()){
                return;
            }
            if (!item.getItemMeta().hasDisplayName()){
                return;
            }
            ItemMeta im = item.getItemMeta();
            String display = im.getDisplayName();
            if (display.equals(plugin.getLang().get(p, "menus.soulwellmenu.confirm.nameItem"))){
                SoulWellSession sw = is.getSwm().getSession(p);
                SWPlayer sws = plugin.getDb().getSWPlayer(p);
                int price5 = plugin.getCm().getSoulWellPrice() * 5;
                int price4 = plugin.getCm().getSoulWellPrice() * 4;
                int price3 = plugin.getCm().getSoulWellPrice() * 3;
                int price2 = plugin.getCm().getSoulWellPrice() * 2;
                int price1 = plugin.getCm().getSoulWellPrice();
                if (sws.getRows() >= 5){
                    if (sws.getSouls() < price5 && sws.getSouls() >= price4){
                        sw.execute(sws, 4);
                        sws.removeSouls(price4);
                        return;
                    } else if (check3Rounds(p, sw, sws, price4, price3, price2, price1)) return;
                    sw.execute(sws, 5);
                    sws.removeSouls(price5);
                } else if (sws.getRows() == 4){
                    if (check3Rounds(p, sw, sws, price4, price3, price2, price1)) return;
                    sw.execute(sws, sws.getRows());
                    sws.removeSouls(price4);
                } else if (sws.getRows() == 3){
                    if (sws.getSouls() < price3 && sws.getSouls() >= price2){
                        sw.execute(sws, 2);
                        sws.removeSouls(price2);
                        return;
                    } else if (check2Rounds(p, sw, sws, price2, price1)) return;
                    sw.execute(sws, sws.getRows());
                    sws.removeSouls(price3);
                } else if (sws.getRows() == 2){
                    if (check2Rounds(p, sw, sws, price2, price1)) return;
                    sw.execute(sws, sws.getRows());
                    sws.removeSouls(price2);
                } else {
                    if (sws.getSouls() < price1){
                        p.sendMessage(plugin.getLang().get(p, "messages.minSouls"));
                        CustomSound.NOSOULS.reproduce(p);
                        return;
                    }
                    sw.execute(sws, sws.getRows());
                    sws.removeSouls(price1);
                }
                Utils.updateSB(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.soulwellmenu.settings.nameItem"))){
                is.getWel().createSettingsMenu(p);
            }
            if (display.equals(plugin.getLang().get(p, "menus.soulwellmenu.deny.nameItem"))){
                p.closeInventory();
            }
        }
    }
    
    private boolean check3Rounds(Player p, SoulWellSession sw, SWPlayer sws, int price4, int price3, int price2, int price1){
        if (sws.getSouls() < price4 && sws.getSouls() >= price3){
            sw.execute(sws, 3);
            sws.removeSouls(price3);
            return true;
        } else if (sws.getSouls() < price3 && sws.getSouls() >= price2){
            sw.execute(sws, 2);
            sws.removeSouls(price2);
            return true;
        } else return check2Rounds(p, sw, sws, price2, price1);
    }
    
    private boolean check2Rounds(Player p, SoulWellSession sw, SWPlayer sws, int price2, int price1){
        if (sws.getSouls() < price2 && sws.getSouls() >= price1){
            sw.execute(sws, 1);
            sws.removeSouls(price1);
            return true;
        } else if (sws.getSouls() < price1){
            p.sendMessage(plugin.getLang().get(p, "messages.minSouls"));
            CustomSound.NOSOULS.reproduce(p);
            return true;
        }
        return false;
    }
    
}