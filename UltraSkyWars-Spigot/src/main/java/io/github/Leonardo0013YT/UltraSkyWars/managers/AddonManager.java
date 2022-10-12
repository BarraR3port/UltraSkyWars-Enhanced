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

package io.github.Leonardo0013YT.UltraSkyWars.managers;

import io.github.Leonardo0013YT.UltraSkyWars.addons.PartiesAddon;
import io.github.Leonardo0013YT.UltraSkyWars.addons.SlimeWorldManagerAddon;
import io.github.Leonardo0013YT.UltraSkyWars.addons.economy.PlayerPointsAddon;
import io.github.Leonardo0013YT.UltraSkyWars.addons.economy.VaultAddon;
import io.github.Leonardo0013YT.UltraSkyWars.addons.holograms.HolographicDisplaysAddon;
import io.github.Leonardo0013YT.UltraSkyWars.addons.leaderheads.*;
import io.github.Leonardo0013YT.UltraSkyWars.addons.nametags.TabAPIAddon;
import io.github.Leonardo0013YT.UltraSkyWars.addons.placeholders.PlaceholderAPIAddon;
import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.EconomyAddon;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.HologramAddon;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.NametagAddon;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.PlaceholderAddon;
import io.github.Leonardo0013YT.UltraSkyWars.api.managers.IAddonManager;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class AddonManager implements IAddonManager {
    
    private EconomyAddon economy;
    private HologramAddon ha;
    private NametagAddon tag;
    private PartiesAddon parties;
    private PlaceholderAddon placeholder;
    private SlimeWorldManagerAddon slime;
    
    public boolean check(String pluginName){
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        if (plugin.getConfig().isSet("addons." + pluginName) && plugin.getConfig().getBoolean("addons." + pluginName)){
            if (Bukkit.getPluginManager().isPluginEnabled(pluginName)){
                plugin.sendLogMessage("Hooked into §a" + pluginName + "§e!");
                return true;
            } else {
                plugin.getConfig().set("addons." + pluginName, false);
                plugin.saveConfig();
                return false;
            }
        }
        return false;
    }
    
    public void reload(){
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        if (check("LeaderHeads")){
            new SkyWarsElo(plugin);
            new SkyWarsSoloKills(plugin);
            new SkyWarsTeamKills(plugin);
            new SkyWarsRankedKills(plugin);
            new SkyWarsSoloWins(plugin);
            new SkyWarsTeamWins(plugin);
            new SkyWarsRankedWins(plugin);
            new SkyWarsSoloDeaths(plugin);
            new SkyWarsTeamDeaths(plugin);
            new SkyWarsRankedDeaths(plugin);
            new SkyWarsCoins(plugin);
            new SkyWarsKills(plugin);
            new SkyWarsWins(plugin);
            new SkyWarsDeaths(plugin);
        }
        if (check("SlimeWorldManager")){
            slime = new SlimeWorldManagerAddon(plugin);
        }
        /*
        REMOVED DUE TO REPO SERVER DOWN
        if (check("MVdWPlaceholderAPI")){
            placeholder = new MVdWPlaceholderAPIAddon();
        }*/
        if (check("PlaceholderAPI")){
            placeholder = new PlaceholderAPIAddon();
        }
        if (check("TAB")){
            tag = new TabAPIAddon();
        }
        /*
        REMOVED DUE TO REPO SERVER DOWN
        if (check("NametagEdit")){
            tag = new NametagEditAddon();
        }*/
        if (check("Parties")){
            parties = new PartiesAddon();
        }
        if (check("Vault")){
            economy = new VaultAddon();
        }
        if (check("PlayerPoints")){
            economy = new PlayerPointsAddon();
        }
        /*
        REMOVED DUE TO REPO SERVER DOWN
        if (check("Coins")){
            economy = new CoinsAddon();
        }*/
        if (!plugin.isDisabled()){
            new BukkitRunnable() {
                @Override
                public void run(){
                    /*
                    REMOVED DUE TO REPO SERVER DOWN
                    if (check("TrHologram")){
                        remove();
                        ha = new TrHologramAddon();
                        reloadHologram();
                    }*/
                    /*
                    REMOVED DUE TO REPO SERVER DOWN
                    if (check("Holograms")){
                        remove();
                        ha = new HologramsAddon();
                        reloadHologram();
                    }*/
                    if (check("HolographicDisplays")){
                        remove();
                        ha = new HolographicDisplaysAddon();
                        reloadHologram();
                    }
                }
            }.runTaskLater(plugin, 80);
        }
    }
    
    public void reloadHologram(){
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        if (plugin.getIjm().isSoulWellInjection()){
            plugin.getIjm().getSoulwell().getSwm().reload();
        }
        if (plugin.getIjm().isCubeletsInjection()){
            plugin.getIjm().getCubelets().getCbm().reload();
        }
    }
    
    public PartiesAddon getParties(){
        return parties;
    }
    
    public void addCoins(Player p, double amount){
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        if (economy != null){
            economy.addCoins(p, amount);
        } else {
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw == null) return;
            sw.addCoins((int) amount);
        }
        Utils.updateSB(p);
    }
    
    public void setCoins(Player p, double amount){
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        if (economy != null){
            economy.setCoins(p, amount);
        } else {
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw == null) return;
            sw.setCoins((int) amount);
        }
        Utils.updateSB(p);
    }
    
    public void removeCoins(Player p, double amount){
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        if (economy != null){
            economy.removeCoins(p, amount);
        } else {
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw == null) return;
            sw.removeCoins((int) amount);
        }
        Utils.updateSB(p);
    }
    
    public double getCoins(Player p){
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        if (economy != null){
            return economy.getCoins(p);
        } else {
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw == null){
                return 0;
            }
            return sw.getCoins();
        }
    }
    
    public void createHologram(Location spawn, List<String> lines){
        if (ha != null){
            ha.createHologram(spawn, lines);
        }
    }
    
    public void createHologram(Location spawn, List<String> lines, ItemStack item){
        if (ha != null){
            ha.createHologram(spawn, lines, item);
        }
    }
    
    public void remove(){
        if (ha != null){
            ha.remove();
        }
    }
    
    public void deleteHologram(Location spawn){
        if (ha != null){
            ha.deleteHologram(spawn);
        }
    }
    
    public boolean hasHologram(Location spawn){
        if (ha != null){
            return ha.hasHologram(spawn);
        }
        return false;
    }
    
    public void addPlayerNameTag(Player p){
        if (tag != null){
            tag.addPlayerNameTag(p);
        }
    }
    
    public void resetPlayerNameTag(Player p){
        if (tag != null){
            tag.resetPlayerNameTag(p);
        }
    }
    
    public String getPlayerPrefix(Player p){
        if (tag != null){
            return tag.getPrefix(p);
        }
        return "";
    }
    
    public String getPlayerSuffix(Player p){
        if (tag != null){
            return tag.getSuffix(p);
        }
        return "";
    }
    
    public String parsePlaceholders(Player p, String value){
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        if (placeholder != null){
            return placeholder.parsePlaceholders(p, value);
        }
        return value;
    }
    
    public SlimeWorldManagerAddon getSlime(){
        return slime;
    }
    
    public boolean hasHologramPlugin(){
        return !(ha == null);
    }
    
}