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

package io.github.Leonardo0013YT.UltraSkyWars;

import com.google.gson.Gson;
import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.config.Settings;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.MongoDBDatabase;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.MySQLDatabase;
import io.github.Leonardo0013YT.UltraSkyWars.api.fanciful.FancyMessage;
import io.github.Leonardo0013YT.UltraSkyWars.api.managers.*;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.DependUtils;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.MetricsLite;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Randomizer;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import io.github.Leonardo0013YT.UltraSkyWars.cmds.LeaveCMD;
import io.github.Leonardo0013YT.UltraSkyWars.cmds.SkyWarsCMD;
import io.github.Leonardo0013YT.UltraSkyWars.controllers.VersionController;
import io.github.Leonardo0013YT.UltraSkyWars.controllers.WorldController;
import io.github.Leonardo0013YT.UltraSkyWars.listeners.*;
import io.github.Leonardo0013YT.UltraSkyWars.managers.AddonManager;
import io.github.Leonardo0013YT.UltraSkyWars.menus.GameMenu;
import io.github.Leonardo0013YT.UltraSkyWars.menus.UltraInventoryMenu;
import io.github.Leonardo0013YT.UltraSkyWars.migrators.SkyWarsMigrator;
import io.github.Leonardo0013YT.UltraSkyWars.migrators.SkyWarsXMigrator;
import io.github.Leonardo0013YT.UltraSkyWars.placeholders.Placeholders;
import io.github.Leonardo0013YT.UltraSkyWarsSetup.api.USWSetupAPI;
import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Getter
public class UltraSkyWars extends UltraSkyWarsApi {
    
    public static Gson getGson(){
        return gson;
    }
    
    @Override
    public void onEnable(){
        instance = this;
        if (!getServer().getPluginManager().isPluginEnabled("WorldEdit") || !getServer().getPluginManager().isPluginEnabled("FastAsyncWorldEdit")){
            Bukkit.getConsoleSender().sendMessage("§b§lUltraSkyWars Plugin §cNeed §eWorldEdit §c& §eFastAsyncWorldEdit§c.");
            Bukkit.getScheduler().cancelTasks(this);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        getConfig().options().copyDefaults(true);
        vc = new VersionController(this);
        saveConfig();
        loadMainLobby();
        updateDirectories();
        debugMode = getConfig().getBoolean("debugMode");
        du = new DependUtils(this);
        du.loadDepends();
        if (getConfig().getBoolean("mongodb.enabled")){
            db = new MongoDBDatabase(this);
        } else {
            db = new MySQLDatabase(this);
        }
        balloon = new Settings("cosmetics/balloon", false);
        glass = new Settings("cosmetics/glass", false);
        killeffect = new Settings("cosmetics/killeffect", true);
        killsound = new Settings("cosmetics/killsound", false);
        parting = new Settings("cosmetics/parting", false);
        taunt = new Settings("cosmetics/taunt", false);
        trail = new Settings("cosmetics/trail", false);
        windance = new Settings("cosmetics/windance", true);
        wineffect = new Settings("cosmetics/wineffect", true);
        chestType = new Settings("chestType", false);
        migrators = new Settings("migrators", true);
        join = new Settings("customjoin", false);
        arenas = new Settings("arenas", false);
        lang = new Settings("lang", true, true);
        menus = new Settings("menus", false);
        chests = new Settings("chests", false);
        kits = new Settings("kits", false);
        levels = new Settings("levels", false);
        rewards = new Settings("rewards", false);
        sounds = new Settings("sounds", true);
        cm = new ConfigManager();
        im = new ItemManager();
        adm = new AddonManager();
        randomizer = new Randomizer();
        cos = new CosmeticManager(this);
        cos.reload();
        ctm = new ChestManager();
        uim = new UltraInventoryMenu(this);
        uim.loadMenus();
        ijm = new ModuleManager();
        //ijm.loadWEInjection();
        adm.reload();
        wc = new WorldController();
        gm = new GameManager(this);
        gem = new GameMenu(this);
        km = new KitManager(this);
        km.loadKits();
        sb = new ScoreboardManager();
        tgm = new TaggedManager();
        top = new TopManager();
        mm = new MultiplierManager();
        lvl = new LevelManager();
        shm = new ShopManager();
        gm.reload();
        getCommand("sw").setExecutor(new SkyWarsCMD(this));
        if (getConfig().getBoolean("leaveCMD")){
            getCommand("leave").setExecutor(new LeaveCMD(this));
        }
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new SpecialListener(), this);
        getServer().getPluginManager().registerEvents(new SpectatorListener(this), this);
        getServer().getPluginManager().registerEvents(new WorldListener(), this);
        getServer().getPluginManager().registerEvents(new GeneralListener(), this);
        if (cm.isAutoLapiz()){
            getServer().getPluginManager().registerEvents(new LapisListener(), this);
        }
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")){
            new Placeholders().register();
        }
        /*
        REMOVED DUE TO REPO SERVER DOWN
        if (getServer().getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")){
            new MVdWPlaceholders().register();
        }*/
        tsm = new TaskManager(this);
        saveSchematics();
        new SkyWarsMigrator(this);
        new SkyWarsXMigrator(this);
        new BukkitRunnable() {
            @Override
            public void run(){
                ijm.loadInjections();
            }
        }.runTaskLater(this, 30);
        getDb().loadTopKills();
        getDb().loadTopWins();
        getDb().loadTopDeaths();
        getDb().loadTopCoins();
        getDb().loadTopElo();
        getDb().loadMultipliers(b -> {
        });
        new MetricsLite(this, 9620);
    }
    
    public void loadMainLobby(){
        if (getConfig().getString("mainLobby") != null){
            mainLobby = Utils.getStringLocation(getConfig().getString("mainLobby"));
        }
        if (getConfig().getString("topKills") != null){
            topKills = Utils.getStringLocation(getConfig().getString("topKills"));
        }
        if (getConfig().getString("topWins") != null){
            topWins = Utils.getStringLocation(getConfig().getString("topWins"));
        }
        if (getConfig().getString("topDeaths") != null){
            topDeaths = Utils.getStringLocation(getConfig().getString("topDeaths"));
        }
        if (getConfig().getString("topCoins") != null){
            topCoins = Utils.getStringLocation(getConfig().getString("topCoins"));
        }
        if (getConfig().getString("topElo") != null){
            topElo = Utils.getStringLocation(getConfig().getString("topElo"));
        }
    }
    
    @Override
    public void onDisable(){
        if (disabled) return;
        if (top != null){
            getTop().remove();
        }
        stop = true;
        getServer().getOnlinePlayers().forEach(on -> {
            getCos().remove(on);
            getGm().removePlayerAllGame(on);
        });
        if (!getDb().getPlayers().keySet().isEmpty()){
            Collection<UUID> sws = new ArrayList<>(getDb().getPlayers().keySet());
            for ( UUID sw : sws ){
                getDb().savePlayerSync(sw);
            }
        }
        for ( String s : getGm().getWorlds().keySet() ){
            getWc().deleteWorld(s);
        }
        File f = new File(Bukkit.getWorldContainer() + "/plugins/FastAsyncWorldEdit/clipboard");
        getWc().deleteDirectory(f);
        db.close();
    }
    
    public void broadcastGame(Game game){
        if (!getCm().isBroadcastGame()) return;
        FancyMessage fm = new FancyMessage(getLang().get(null, "fancy.message").replaceAll("<game>", game.getName()))
                .addMsg(getLang().get(null, "fancy.click"))
                .setClick(ClickEvent.Action.RUN_COMMAND, "/sw join " + game.getGameType().toLowerCase() + " " + game.getName())
                .setHover(HoverEvent.Action.SHOW_TEXT, getLang().get(null, "fancy.hover")).build();
        for ( Player on : Bukkit.getOnlinePlayers() ){
            if (getGm().isPlayerInGame(on)){
                continue;
            }
            fm.send(on);
        }
    }
    
    public void saveSchematics(){
        saveResource("clearGlass.schematic", true);
        saveResource("glass.schematic", true);
        File f = new File(Bukkit.getWorldContainer() + "/plugins/WorldEdit/schematics");
        if (!f.exists()){
            f.mkdirs();
        }
        copy(getDataFolder().getAbsolutePath() + "/clearGlass.schematic", Bukkit.getWorldContainer() + "/plugins/WorldEdit/schematics/clearGlass.schematic");
        copy(getDataFolder().getAbsolutePath() + "/glass.schematic", Bukkit.getWorldContainer() + "/plugins/WorldEdit/schematics/glass.schematic");
        try {
            Files.delete(Paths.get(getDataFolder().getAbsolutePath() + "/clearGlass.schematic"));
            Files.delete(Paths.get(getDataFolder().getAbsolutePath() + "/glass.schematic"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void sendDebugMessage(String... s){
        if (debugMode){
            for ( String st : s ){
                Bukkit.getConsoleSender().sendMessage("§b[USW Debug] §e" + st);
            }
        }
    }
    
    public void copy(String origin, String destiny){
        try (
                InputStream in = new BufferedInputStream(new FileInputStream(origin));
                OutputStream out = new BufferedOutputStream(new FileOutputStream(destiny))) {
            byte[] buffer = new byte[1024];
            int lengthRead;
            while ((lengthRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, lengthRead);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean isBungeeMode(){
        return false;
    }
    
    public void reload(){
        reloadConfig();
        arenas.reload();
        lang.reload();
        chests.reload();
        balloon.reload();
        glass.reload();
        rewards.reload();
        killeffect.reload();
        killsound.reload();
        kits.reload();
        menus.reload();
        levels.reload();
        parting.reload();
        taunt.reload();
        trail.reload();
        windance.reload();
        join.reload();
        wineffect.reload();
        sb.reload();
        adm.remove();
        uim.loadMenus();
        ctm.reload();
        im.reload();
        adm.reload();
        top.createTops();
        km.loadKits();
        cm.reload();
        ijm.reload();
    }
    
    public void sendToServer(Player p, String server){
    }
    
    public void reloadLang(){
        lang.reload();
    }
    
    public void sendLogMessage(String msg){
        Bukkit.getConsoleSender().sendMessage("§c§lUltraSkyWars §8| " + msg);
    }
    
    public void sendLogMessage(String... msg){
        for ( String m : msg ){
            Bukkit.getConsoleSender().sendMessage("§c§lUltraSkyWars §8| §e" + m);
        }
    }
    
    public void updateDirectories(){
        try {
            File cosmetics = new File(getDataFolder(), "cosmetics");
            if (!cosmetics.exists()){
                cosmetics.mkdirs();
                File balloons = new File(getDataFolder(), "balloon.yml");
                if (balloons.exists()){
                    File to = new File(cosmetics, "balloon.yml");
                    Files.copy(balloons.toPath(), to.toPath());
                    balloons.delete();
                }
                File glass = new File(getDataFolder(), "glass.yml");
                if (glass.exists()){
                    File to = new File(cosmetics, "glass.yml");
                    Files.copy(glass.toPath(), to.toPath());
                    glass.delete();
                }
                File killeffect = new File(getDataFolder(), "killeffect.yml");
                if (killeffect.exists()){
                    File to = new File(cosmetics, "killeffect.yml");
                    Files.copy(killeffect.toPath(), to.toPath());
                    killeffect.delete();
                }
                File killsound = new File(getDataFolder(), "killsound.yml");
                if (killsound.exists()){
                    File to = new File(cosmetics, "killsound.yml");
                    Files.copy(killsound.toPath(), to.toPath());
                    killsound.delete();
                }
                File parting = new File(getDataFolder(), "parting.yml");
                if (parting.exists()){
                    File to = new File(cosmetics, "parting.yml");
                    Files.copy(parting.toPath(), to.toPath());
                    parting.delete();
                }
                File taunt = new File(getDataFolder(), "taunt.yml");
                if (taunt.exists()){
                    File to = new File(cosmetics, "taunt.yml");
                    Files.copy(taunt.toPath(), to.toPath());
                    taunt.delete();
                }
                File trail = new File(getDataFolder(), "trail.yml");
                if (trail.exists()){
                    File to = new File(cosmetics, "trail.yml");
                    Files.copy(trail.toPath(), to.toPath());
                    trail.delete();
                }
                File windance = new File(getDataFolder(), "windance.yml");
                if (windance.exists()){
                    File to = new File(cosmetics, "windance.yml");
                    Files.copy(windance.toPath(), to.toPath());
                    windance.delete();
                }
                File wineffect = new File(getDataFolder(), "wineffect.yml");
                if (wineffect.exists()){
                    File to = new File(cosmetics, "wineffect.yml");
                    Files.copy(wineffect.toPath(), to.toPath());
                    wineffect.delete();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean isSetupLobby(Player p){
        if (Bukkit.getPluginManager().isPluginEnabled("UltraSkyWars-Setup")){
            return USWSetupAPI.isLobbySetup(p);
        }
        return false;
    }
    
}