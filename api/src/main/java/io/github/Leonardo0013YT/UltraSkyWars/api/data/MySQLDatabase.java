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

package io.github.Leonardo0013YT.UltraSkyWars.api.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.calls.CallBackAPI;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.StatType;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.TopType;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.data.USWPlayerLoadEvent;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.Database;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MySQLDatabase implements Database {
    
    private final static String SAVE = "UPDATE UltraSkyWars SET Data=?, Kills=?, Wins=?, Deaths=?, Coins=?, Elo=? WHERE UUID=?";
    private final static HashMap<UUID, SWPlayer> players = new HashMap<>();
    private static boolean enabled;
    private final UltraSkyWarsApi plugin;
    private HikariDataSource hikari;
    private Connection connection;
    
    public MySQLDatabase(UltraSkyWarsApi plugin){
        this.plugin = plugin;
        enabled = plugin.getConfig().getBoolean("mysql.enabled");
        connect();
    }
    
    public void connect(){
        if (enabled){
            int port = plugin.getConfig().getInt("mysql.port");
            String ip = plugin.getConfig().getString("mysql.host");
            String database = plugin.getConfig().getString("mysql.database");
            String username = plugin.getConfig().getString("mysql.username");
            String password = plugin.getConfig().getString("mysql.password");
            HikariConfig config = new HikariConfig();
            String connectionString = "jdbc:mysql://" + ip + ":" + port + "/" + database + "?autoReconnect=true";
            config.setJdbcUrl(connectionString);
            config.setUsername(username);
            config.setPassword(password);
            config.setDriverClassName("com.mysql.jdbc.Driver");
            config.addDataSourceProperty("cachePrepStmts", true);
            config.addDataSourceProperty("prepStmtCacheSize", 250);
            config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
            config.addDataSourceProperty("useServerPrepStmts", true);
            config.addDataSourceProperty("useLocalSessionState", true);
            config.addDataSourceProperty("rewriteBatchedStatements", true);
            config.addDataSourceProperty("cacheResultSetMetadata", true);
            config.addDataSourceProperty("cacheServerConfiguration", true);
            config.addDataSourceProperty("elideSetAutoCommits", true);
            config.addDataSourceProperty("maintainTimeStats", false);
            config.addDataSourceProperty("characterEncoding", "utf8");
            config.addDataSourceProperty("encoding", "UTF-8");
            config.addDataSourceProperty("useUnicode", "true");
            config.addDataSourceProperty("useSSL", false);
            config.addDataSourceProperty("tcpKeepAlive", true);
            config.setPoolName("UltraSkyWars " + UUID.randomUUID());
            config.setMaxLifetime(Long.MAX_VALUE);
            config.setMinimumIdle(0);
            config.setIdleTimeout(30000L);
            config.setConnectionTimeout(10000L);
            config.setMaximumPoolSize(10);
            hikari = new HikariDataSource(config);
            createTable();
            plugin.sendLogMessage("§eMySQL connected correctly.");
        } else {
            File DataFile = new File(plugin.getDataFolder(), "/UltraSkyWars.db");
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
                    plugin.sendLogMessage("§eSQLLite connected correctly.");
                    createTable();
                } catch (SQLException ex2) {
                    ex2.printStackTrace();
                    Bukkit.getPluginManager().disablePlugin(plugin);
                }
            } catch (ClassNotFoundException ex3) {
                ex3.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(plugin);
            }
        }
    }
    
    @Override
    public HashMap<UUID, SWPlayer> getPlayers(){
        return players;
    }
    
    public void close(){
        if (enabled){
            if (hikari != null){
                hikari.close();
            }
        } else {
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void createTable(){
        if (enabled){
            try {
                Connection connection = hikari.getConnection();
                Statement statement = connection.createStatement();
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS UltraSkyWars(UUID varchar(36) primary key, Name varchar(20), Data LONGTEXT, Kills INT, Wins INT, Deaths INT, Coins INT, Elo INT, UNIQUE(UUID));");
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS Multipliers(ID INT AUTO_INCREMENT, Type varchar(10), Name varchar(20), Amount DOUBLE, Ending DATETIME, PRIMARY KEY(ID));");
                close(connection, statement, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS UltraSkyWars(UUID varchar(36) primary key, Name varchar(20), Data LONGTEXT, Kills INT, Wins INT, Deaths INT, Coins INT, Elo INT, UNIQUE(UUID));"
                        + "CREATE TABLE IF NOT EXISTS Multipliers(ID INT AUTO_INCREMENT, Type varchar(10), Name varchar(20), Amount DOUBLE, Ending DATETIME, PRIMARY KEY(ID));");
                close(connection, statement, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public int getRanking(UUID uuid){
        // SELECT UUID, Elo, (SELECT COUNT(*)+1 FROM UltraSkyWars WHERE Elo>x.Elo) AS Ranking FROM UltraSkyWars x WHERE x.UUID = '3f1f95d8-fd39-11ea-ba2b-d05099d6ad05';
        // SET @rank=0;
        // SELECT UUID, @rank:=@rank+1 AS Rank, Elo FROM UltraSkyWars ORDER BY Elo DESC;
        try {
            Connection con = getConnection();
            PreparedStatement select = con.prepareStatement("SELECT UUID, Elo, (SELECT COUNT(*)+1 FROM UltraSkyWars WHERE Elo>x.Elo) AS Ranking FROM UltraSkyWars x WHERE x.UUID = '" + uuid.toString() + "';");
            ResultSet result = select.executeQuery();
            if (result.next()){
                return result.getInt("Ranking");
            }
            close(con, select, result);
        } catch (SQLException ignored) {
        }
        return 0;
    }
    
    @Override
    public void loadMultipliers(CallBackAPI<Boolean> request){
        plugin.getMm().clear();
        new BukkitRunnable() {
            @Override
            public void run(){
                try {
                    Connection connection = getConnection();
                    String MULTI = "SELECT * FROM Multipliers;";
                    PreparedStatement select = connection.prepareStatement(MULTI);
                    ResultSet result = select.executeQuery();
                    while (result.next()) {
                        String type = result.getString("Type");
                        String name = result.getString("Name");
                        double amount = result.getDouble("Amount");
                        Date date = result.getDate("Ending");
                        plugin.getMm().addMultiplier(result.getInt("ID"), type, name, amount, date.getTime());
                    }
                    close(connection, select, result);
                    request.done(true);
                } catch (SQLException e) {
                    request.done(false);
                }
            }
        }.runTaskAsynchronously(plugin);
    }
    
    @Override
    public void createMultiplier(String type, String name, double amount, long ending, CallBackAPI<Boolean> request){
        Date date = new Date(ending);
        new BukkitRunnable() {
            @Override
            public void run(){
                if (enabled){
                    try {
                        Connection connection = hikari.getConnection();
                        String MULTI = "INSERT INTO Multipliers VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE Name=?;";
                        PreparedStatement insert = connection.prepareStatement(MULTI);
                        insert.setString(1, type);
                        insert.setString(2, name);
                        insert.setDouble(3, amount);
                        insert.setDate(4, date);
                        insert.setString(5, name);
                        insert.execute();
                        close(connection, insert, null);
                        plugin.sendDebugMessage("Se ha creado un Multiplicador:", "§aCantidad: §b" + amount, "§aNombre: §b" + name, "§aFin: §b" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date));
                        request.done(true);
                    } catch (SQLException e) {
                        request.done(false);
                    }
                } else {
                    try {
                        Connection connection = getConnection();
                        String MULTI = "INSERT INTO `Multipliers` (`Type`, `Name`, `Amount`, `Ending`) VALUES (?, ?, ?, ?);";
                        PreparedStatement insert = connection.prepareStatement(MULTI);
                        insert.setString(1, type);
                        insert.setString(2, name);
                        insert.setDouble(3, amount);
                        insert.setDate(4, date);
                        insert.execute();
                        close(connection, insert, null);
                        plugin.sendDebugMessage("Se ha creado un Multiplicador:", "§aCantidad: §b" + amount, "§aNombre: §b" + name, "§aFin: §b" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date));
                        request.done(true);
                    } catch (SQLException e) {
                        request.done(false);
                    }
                }
            }
        }.runTaskAsynchronously(plugin);
    }
    
    @Override
    public boolean removeMultiplier(int id){
        try {
            Connection connection = getConnection();
            String MULTI = "DELETE FROM Multipliers WHERE ID=?;";
            PreparedStatement delete = connection.prepareStatement(MULTI);
            delete.setInt(1, id);
            boolean b = delete.execute();
            close(connection, delete, null);
            return b;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public void loadTopElo(){
        Connection connection;
        PreparedStatement select;
        ResultSet result;
        try {
            connection = getConnection();
            String TOP = "SELECT UUID, Name, Elo FROM UltraSkyWars ORDER BY Elo DESC LIMIT 10;";
            select = connection.prepareStatement(TOP);
            result = select.executeQuery();
            int pos = 1;
            List<String> tops = new ArrayList<>();
            while (result.next()) {
                tops.add(result.getString("UUID") + ":" + result.getString("Name") + ":" + pos + ":" + result.getInt("Elo"));
                pos++;
            }
            plugin.getTop().addTop(TopType.ELO, tops);
            close(connection, select, result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void loadTopCoins(){
        Connection connection;
        PreparedStatement select;
        ResultSet result;
        try {
            connection = getConnection();
            String TOP = "SELECT UUID, Name, Coins FROM UltraSkyWars ORDER BY Coins DESC LIMIT 10;";
            select = connection.prepareStatement(TOP);
            result = select.executeQuery();
            int pos = 1;
            List<String> tops = new ArrayList<>();
            while (result.next()) {
                tops.add(result.getString("UUID") + ":" + result.getString("Name") + ":" + pos + ":" + result.getInt("Coins"));
                pos++;
            }
            plugin.getTop().addTop(TopType.COINS, tops);
            close(connection, select, result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void loadTopKills(){
        Connection connection;
        PreparedStatement select;
        ResultSet result;
        try {
            connection = getConnection();
            String TOP = "SELECT UUID, Name, Kills FROM UltraSkyWars ORDER BY Kills DESC LIMIT 10;";
            select = connection.prepareStatement(TOP);
            result = select.executeQuery();
            int pos = 1;
            List<String> tops = new ArrayList<>();
            while (result.next()) {
                tops.add(result.getString("UUID") + ":" + result.getString("Name") + ":" + pos + ":" + result.getInt("Kills"));
                pos++;
            }
            plugin.getTop().addTop(TopType.KILLS, tops);
            close(connection, select, result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void loadTopWins(){
        Connection connection;
        PreparedStatement select;
        ResultSet result;
        try {
            connection = getConnection();
            String TOP = "SELECT UUID, Name, Wins FROM UltraSkyWars ORDER BY Wins DESC LIMIT 10;";
            select = connection.prepareStatement(TOP);
            result = select.executeQuery();
            int pos = 1;
            List<String> tops = new ArrayList<>();
            while (result.next()) {
                tops.add(result.getString("UUID") + ":" + result.getString("Name") + ":" + pos + ":" + result.getInt("Wins"));
                pos++;
            }
            plugin.getTop().addTop(TopType.WINS, tops);
            close(connection, select, result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void loadTopDeaths(){
        Connection connection;
        PreparedStatement select;
        ResultSet result;
        try {
            connection = getConnection();
            String TOP = "SELECT UUID, Name, Deaths FROM UltraSkyWars ORDER BY Deaths DESC LIMIT 10;";
            select = connection.prepareStatement(TOP);
            result = select.executeQuery();
            int pos = 1;
            List<String> tops = new ArrayList<>();
            while (result.next()) {
                tops.add(result.getString("UUID") + ":" + result.getString("Name") + ":" + pos + ":" + result.getInt("Deaths"));
                pos++;
            }
            plugin.getTop().addTop(TopType.DEATHS, tops);
            close(connection, select, result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void loadPlayer(final Player p){
        new BukkitRunnable() {
            @Override
            public void run(){
                String SELECT = "SELECT * FROM UltraSkyWars WHERE UUID=?";
                if (enabled){
                    Connection connection = null;
                    PreparedStatement insert = null;
                    PreparedStatement select = null;
                    ResultSet result = null;
                    try {
                        connection = getConnection();
                        String INSERT = "INSERT INTO UltraSkyWars VALUES(?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE Name=?";
                        insert = connection.prepareStatement(INSERT);
                        select = connection.prepareStatement(SELECT);
                        setValues(insert, p);
                        insert.setString(9, p.getName());
                        insert.execute();
                        select.setString(1, p.getUniqueId().toString());
                        result = select.executeQuery();
                        if (result.next()){
                            String data = result.getString("Data");
                            if (data != null){
                                addPlayer(p, Utils.fromGson(data));
                            } else {
                                addPlayer(p, new SWPlayer());
                            }
                        }
                    } catch (SQLException ignored) {
                    } finally {
                        close(connection, insert, result);
                        close(null, select, null);
                    }
                } else {
                    try {
                        String INSERT2 = "INSERT INTO `UltraSkyWars` (`UUID`, `Name`, `Data`, `Kills`, `Wins`, `Deaths`, `Coins`, `Elo`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
                        PreparedStatement insert = connection.prepareStatement(INSERT2);
                        PreparedStatement select = connection.prepareStatement(SELECT);
                        select.setString(1, p.getUniqueId().toString());
                        ResultSet result = select.executeQuery();
                        if (result.next()){
                            addPlayer(p, Utils.fromGson(result.getString("Data")));
                        } else {
                            setValues(insert, p);
                            insert.executeUpdate();
                            PreparedStatement select2 = connection.prepareStatement(SELECT);
                            select2.setString(1, p.getUniqueId().toString());
                            ResultSet result2 = select2.executeQuery();
                            if (result2.next())
                                addPlayer(p, Utils.fromGson(result2.getString("Data")));
                            close(connection, select2, result2);
                        }
                        close(connection, insert, result);
                        close(connection, select, null);
                    } catch (SQLException ignored) {
                    }
                }
            }
        }.runTaskLaterAsynchronously(plugin, 10);
    }
    
    private void setValues(PreparedStatement insert, Player p) throws SQLException{
        insert.setString(1, p.getUniqueId().toString());
        insert.setString(2, p.getName());
        insert.setString(3, Utils.toGson(new SWPlayer()));
        insert.setInt(4, 0);
        insert.setInt(5, 0);
        insert.setInt(6, 0);
        insert.setInt(7, 0);
        insert.setInt(8, 0);
    }
    
    @Override
    public void savePlayer(final Player p){
        SWPlayer ps = players.get(p.getUniqueId());
        if (ps == null) return;
        new BukkitRunnable() {
            @Override
            public void run(){
                try {
                    saveValues(ps, p.getUniqueId());
                    removePlayer(p);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }
    
    private void saveValues(SWPlayer ps, UUID p) throws SQLException{
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(SAVE);
        statement.setString(1, Utils.toGson(ps));
        statement.setInt(2, ps.getTotalStat(StatType.KILLS));
        statement.setInt(3, ps.getTotalStat(StatType.WINS));
        statement.setInt(4, ps.getTotalStat(StatType.DEATHS));
        statement.setInt(5, ps.getCoins());
        statement.setInt(6, ps.getElo());
        statement.setString(7, p.toString());
        statement.execute();
        close(connection, statement, null);
    }
    
    @Override
    public void saveAll(CallBackAPI<Boolean> done){
        new BukkitRunnable() {
            @Override
            public void run(){
                Bukkit.getOnlinePlayers().forEach(p -> {
                    SWPlayer ps = players.get(p.getUniqueId());
                    if (ps == null) return;
                    try {
                        saveValues(ps, p.getUniqueId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                done.done(true);
            }
        }.runTaskAsynchronously(plugin);
    }
    
    @Override
    public void savePlayerSync(UUID p){
        SWPlayer ps = players.get(p);
        if (ps == null) return;
        try {
            saveValues(ps, p);
            removePlayer(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void createPlayer(UUID uuid, String name, SWPlayer ps){
        if (enabled){
            try {
                Connection connection = hikari.getConnection();
                String INSERT = "INSERT INTO UltraSkyWars VALUES(?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE Name=?";
                PreparedStatement insert = connection.prepareStatement(INSERT);
                insert.setString(1, uuid.toString());
                insert.setString(2, name);
                insert.setString(3, Utils.toGson(ps));
                insert.setInt(4, ps.getTotalStat(StatType.KILLS));
                insert.setInt(5, ps.getTotalStat(StatType.WINS));
                insert.setInt(6, ps.getTotalStat(StatType.DEATHS));
                insert.setInt(7, ps.getCoins());
                insert.setInt(8, ps.getElo());
                insert.setString(9, name);
                insert.execute();
                close(connection, insert, null);
                plugin.sendLogMessage("§aCompleted migration of §b" + name + "§a! §dSkyWars Cookloco §7to §6UltraSkyWars");
            } catch (SQLException ignored) {
            }
        } else {
            try {
                String INSERT2 = "INSERT INTO `UltraSkyWars` (`UUID`, `Name`, `Data`, `Kills`, `Wins`, `Deaths`, `Coins`, `Elo`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
                PreparedStatement insert = connection.prepareStatement(INSERT2);
                insert.setString(1, uuid.toString());
                insert.setString(2, name);
                insert.setString(3, Utils.toGson(ps));
                insert.setInt(4, ps.getTotalStat(StatType.KILLS));
                insert.setInt(5, ps.getTotalStat(StatType.WINS));
                insert.setInt(6, ps.getTotalStat(StatType.DEATHS));
                insert.setInt(7, ps.getCoins());
                insert.setInt(8, ps.getElo());
                insert.executeUpdate();
                close(connection, insert, null);
                plugin.sendLogMessage("§aCompleted migration of §b" + name + "§a! §dSkyWars Cookloco §7to §6UltraSkyWars");
            } catch (SQLException ignored) {
            }
        }
    }
    
    @Override
    public Connection getConnection(){
        if (enabled){
            try {
                return connection = hikari.getConnection();
            } catch (SQLException ignored) {
            }
        }
        return connection;
    }
    
    private void close(Connection connection, Statement statement, ResultSet result){
        if (enabled && connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (result != null){
            try {
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void clearStats(Player p){
        new BukkitRunnable() {
            int correct = 0, failed = 0;
            
            @Override
            public void run(){
                long start = System.currentTimeMillis();
                try {
                    Connection connection = plugin.getDb().getConnection();
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM UltraSkyWars;");
                    ResultSet result = statement.executeQuery();
                    while (result.next()) {
                        //UUID, Name, Data, Kills, Wins, Deaths, Coins, Elo
                        UUID uuid = UUID.fromString(result.getString("UUID"));
                        SWPlayer sw = UltraSkyWarsApi.getGson().fromJson(result.getString("Data"), SWPlayer.class);
                        sw.getStats().clear();
                        String SAVE = "UPDATE UltraSkyWars SET Data=? WHERE UUID=?;";
                        PreparedStatement saveStatement = connection.prepareStatement(SAVE);
                        saveStatement.setString(1, UltraSkyWarsApi.getGson().toJson(sw, SWPlayer.class));
                        saveStatement.setString(2, uuid.toString());
                        saveStatement.execute();
                        correct++;
                    }
                } catch (Exception e) {
                    failed++;
                }
                long end = System.currentTimeMillis();
                p.sendMessage(plugin.getLang().get("setup.clearedStats").replace("<ms>", String.valueOf(end - start)));
            }
        }.runTaskAsynchronously(plugin);
    }
    
    private void addPlayer(Player p, SWPlayer sw){
        if (p == null || !p.isOnline()) return;
        players.put(p.getUniqueId(), sw);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Bukkit.getPluginManager().callEvent(new USWPlayerLoadEvent(p)));
    }
    
    private void removePlayer(UUID p){
        players.remove(p);
    }
    
    private void removePlayer(Player p){
        players.remove(p.getUniqueId());
    }
    
    @Override
    public SWPlayer getSWPlayer(Player p){
        if (p == null || !p.isOnline() || !players.containsKey(p.getUniqueId())){
            return new SWPlayer();
        }
        return players.get(p.getUniqueId());
    }
}