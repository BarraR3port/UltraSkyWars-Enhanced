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
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.UUID;

public class SkyWarsMigrator {
    
    private HikariDataSource hikari;
    private String tableData;
    private boolean enabled;
    private UltraSkyWarsApi plugin;
    private Connection connection;
    
    public SkyWarsMigrator(UltraSkyWarsApi plugin){
        if (!plugin.getMigrators().getBoolean("skywars.enabled")) return;
        tableData = plugin.getMigrators().get(null, "skywars.data");
        enabled = plugin.getMigrators().getBoolean("skywars.mysql.enabled");
        this.plugin = plugin;
        if (enabled){
            hikari = new HikariDataSource();
            hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
            hikari.addDataSourceProperty("serverName", plugin.getMigrators().get(null, "skywars.mysql.server"));
            hikari.addDataSourceProperty("port", plugin.getMigrators().get(null, "skywars.mysql.port"));
            hikari.addDataSourceProperty("databaseName", plugin.getMigrators().get(null, "skywars.mysql.db"));
            hikari.addDataSourceProperty("user", plugin.getMigrators().get(null, "skywars.mysql.user"));
            hikari.addDataSourceProperty("password", plugin.getMigrators().get(null, "skywars.mysql.password"));
            hikari.setMaximumPoolSize(10);
            hikari.setMaxLifetime(Long.MAX_VALUE);
            plugin.sendLogMessage("Migration §bMySQL Cookloco§e connected!");
        } else {
            File DataFile = new File("plugins/SkyWars/Database.db");
            if (!DataFile.exists()){
                try {
                    DataFile.createNewFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Bukkit.getPluginManager().disablePlugin(plugin);
                }
            }
            try {
                Class.forName("org.sqlite.JDBC");
                try {
                    connection = DriverManager.getConnection("jdbc:sqlite:" + DataFile);
                    plugin.sendLogMessage("Migration §bSQLLite Cookloco§e connected!");
                } catch (SQLException ex2) {
                    ex2.printStackTrace();
                    Bukkit.getPluginManager().disablePlugin(plugin);
                }
            } catch (ClassNotFoundException ex3) {
                ex3.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(plugin);
            }
        }
        migrate();
        plugin.getMigrators().set("skywars.enabled", false);
        plugin.getMigrators().save();
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
                        readResult(result);
                    } catch (SQLException ignored) {
                    }
                } else {
                    try {
                        String MULTI = "SELECT * FROM " + tableData + ";";
                        PreparedStatement select = connection.prepareStatement(MULTI);
                        ResultSet result = select.executeQuery();
                        readResult(result);
                    } catch (SQLException ignored) {
                    }
                }
            }
            
            private void readResult(ResultSet result) throws SQLException{
                while (result.next()) {
                    String name = result.getString("username");
                    if (result.getString("uuid") == null){
                        plugin.sendLogMessage("§cError migrating data of §b" + name + "§e! §6SkyWars Cookloco");
                        failed++;
                        continue;
                    }
                    UUID uuid = UUID.fromString(result.getString("uuid"));
                    int wins = result.getInt("wins");
                    int kills = result.getInt("kills");
                    int deaths = result.getInt("deaths");
                    int played = result.getInt("played");
                    int arrow_shot = result.getInt("arrow_shot");
                    int arrow_hit = result.getInt("arrow_hit");
                    int blocks_broken = result.getInt("blocks_broken");
                    int blocks_placed = result.getInt("blocks_placed");
                    int distance_walked = result.getInt("distance_walked");
                    plugin.getDb().createPlayer(uuid, name, getSw(wins, kills, deaths, played, arrow_shot, arrow_hit, blocks_broken, blocks_placed, distance_walked));
                    plugin.sendLogMessage("§eMigrating data of §b" + name + "§e! §dSkyWars Cookloco");
                    parsed++;
                }
                plugin.sendLogMessage("§eMigration completed: §aCorrect -> §b" + parsed + " §cError -> §b" + failed);
            }
        }.runTaskAsynchronously(plugin);
    }
    
    public SWPlayer getSw(int wins, int kills, int deaths, int played, int arrow_shot, int arrow_hit, int blocks_broken, int blocks_placed, int distance_walked){
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
        return pw;
    }
    
}