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

package io.github.Leonardo0013YT.UltraSkyWars.migrators;

import com.zaxxer.hikari.HikariDataSource;
import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.StatType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SkyWarsXMigrator {
    
    private HikariDataSource hikari;
    private String tableData;
    private boolean enabled, useUUID;
    private File connection;
    private UltraSkyWarsApi plugin;
    
    public SkyWarsXMigrator(UltraSkyWarsApi plugin){
        if (!plugin.getMigrators().getBoolean("skywarsX.enabled")) return;
        tableData = plugin.getMigrators().get(null, "skywarsX.table");
        enabled = plugin.getMigrators().getBoolean("skywarsX.mysql.enabled");
        useUUID = plugin.getMigrators().getBoolean("skywarsX.useUUID");
        if (!useUUID){
            plugin.sendLogMessage("§cThe migration by name is only for MySQL! §bSkyWars X");
        }
        this.plugin = plugin;
        if (enabled){
            hikari = new HikariDataSource();
            hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
            hikari.addDataSourceProperty("serverName", plugin.getMigrators().get(null, "skywarsX.mysql.server"));
            hikari.addDataSourceProperty("port", plugin.getMigrators().get(null, "skywarsX.mysql.port"));
            hikari.addDataSourceProperty("databaseName", plugin.getMigrators().get(null, "skywarsX.mysql.db"));
            hikari.addDataSourceProperty("user", plugin.getMigrators().get(null, "skywarsX.mysql.user"));
            hikari.addDataSourceProperty("password", plugin.getMigrators().get(null, "skywarsX.mysql.password"));
            hikari.setMaximumPoolSize(10);
            hikari.setMaxLifetime(Long.MAX_VALUE);
            plugin.sendLogMessage("§aMigration §bMySQL SkyWars X§e connected!");
        } else {
            connection = new File("plugins/Skywars/players");
            if (!connection.exists()){
                connection.mkdirs();
            }
            plugin.sendLogMessage("§aMigration §bYML Data SkyWars X§e connected!");
        }
        migrate();
        plugin.getMigrators().set("skywarsX.enabled", false);
        plugin.getMigrators().save();
    }
    
    public void loadPlayer(Player p){
        File file = new File(plugin.getDataFolder(), "players");
        if (!file.exists()){
            file.mkdirs();
        }
        File player = new File(file, p.getUniqueId().toString());
        if (player.exists()){
            // CODIGO DE CARGA
            YamlConfiguration cargado = YamlConfiguration.loadConfiguration(player);
            
        } else {
            YamlConfiguration cargado = YamlConfiguration.loadConfiguration(player);
            try {
                cargado.save(player);
            } catch (IOException ignored) {
            }
        }
    }
    
    public void migrate(){
        new BukkitRunnable() {
            int parsed = 0, failed = 0;
            
            @Override
            public void run(){
                if (enabled){
                    try {
                        Connection connection = hikari.getConnection();
                        String MULTI = "SELECT * FROM " + tableData + ";";
                        PreparedStatement select = connection.prepareStatement(MULTI);
                        ResultSet result = select.executeQuery();
                        while (result.next()) {
                            String name = result.getString("player_name");
                            if (result.getString("player_uuid") == null){
                                plugin.sendLogMessage("§cError migrating data of §b" + name + "§e! §6SkyWars X");
                                failed++;
                                continue;
                            }
                            UUID uuid = UUID.fromString(result.getString("player_uuid"));
                            String stats = result.getString("stats");
                            String[] s = stats.split(":");
                            int kills = Integer.parseInt(s[0]);
                            int coins = Integer.parseInt(s[1]);
                            int deaths = Integer.parseInt(s[2]);
                            int wins = Integer.parseInt(s[3]);
                            int arrow_shot = Integer.parseInt(s[5]);
                            int arrow_hit = Integer.parseInt(s[6]);
                            int xp = Integer.parseInt(s[7]);
                            int blocks_placed = Integer.parseInt(s[8]);
                            int blocks_broken = Integer.parseInt(s[9]);
                            int played = 0;
                            int distance_walked = 0;
                            plugin.getDb().createPlayer(uuid, name, getSw(coins, xp, wins, kills, deaths, played, arrow_shot, arrow_hit, blocks_broken, blocks_placed, distance_walked));
                            plugin.sendLogMessage("§eMigrating data of §b" + name + "§e! §dSkyWars X");
                            parsed++;
                        }
                        plugin.sendLogMessage("§eMigration completed: §aCorrect -> §b" + parsed + " §cError -> §b" + failed);
                    } catch (SQLException ignored) {
                    }
                } else {
                    if (!useUUID){
                        plugin.sendLogMessage("§cThe migration by name is only for MySQL! §bSkyWars X");
                        return;
                    }
                    File[] files = connection.listFiles();
                    for ( File f : files ){
                        YamlConfiguration result = YamlConfiguration.loadConfiguration(f);
                        UUID uuid = UUID.fromString(f.getName());
                        String name = result.getString("player_name");
                        String stats = result.getString("stats");
                        String[] s = stats.split(":");
                        int kills = Integer.parseInt(s[0]);
                        int coins = Integer.parseInt(s[1]);
                        int deaths = Integer.parseInt(s[2]);
                        int wins = Integer.parseInt(s[3]);
                        int arrow_shot = Integer.parseInt(s[5]);
                        int arrow_hit = Integer.parseInt(s[6]);
                        int xp = Integer.parseInt(s[7]);
                        int blocks_placed = Integer.parseInt(s[8]);
                        int blocks_broken = Integer.parseInt(s[9]);
                        int played = 0;
                        int distance_walked = 0;
                        plugin.getDb().createPlayer(uuid, name, getSw(coins, xp, wins, kills, deaths, played, arrow_shot, arrow_hit, blocks_broken, blocks_placed, distance_walked));
                        plugin.sendLogMessage("§eMigrating data of §b" + name + "§e! §dSkyWars X");
                        parsed++;
                    }
                    plugin.sendLogMessage("§eMigration completed: §aCorrect -> §b" + parsed + " §cError -> §b" + failed);
                }
            }
        }.runTaskAsynchronously(plugin);
    }
    
    public SWPlayer getSw(int coins, int xp, int wins, int kills, int deaths, int played, int arrow_shot, int arrow_hit, int blocks_broken, int blocks_placed, int distance_walked){
        SWPlayer pw = new SWPlayer();
        pw.addStat(StatType.BREAK, "SOLO", blocks_broken);
        pw.addStat(StatType.DEATHS, "SOLO", deaths);
        pw.addStat(StatType.KILLS, "SOLO", kills);
        pw.addStat(StatType.PLACED, "SOLO", blocks_placed);
        pw.addStat(StatType.PLAYED, "SOLO", played);
        pw.addStat(StatType.SUCCESS_SHOTS, "SOLO", arrow_hit);
        pw.addStat(StatType.SHOTS, "SOLO", arrow_shot);
        pw.addStat(StatType.WALKED, "SOLO", distance_walked);
        pw.addStat(StatType.WINS, "SOLO", wins);
        pw.setCoins(coins);
        pw.setXp(xp);
        return pw;
    }
    
}