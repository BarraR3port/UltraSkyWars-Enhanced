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

package io.github.Leonardo0013YT.UltraSkyWars.api.utils;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.calls.CallBackAPI;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DependUtils {
    
    private final UltraSkyWarsApi plugin;
    
    public DependUtils(UltraSkyWarsApi plugin){
        this.plugin = plugin;
    }
    
    public void loadDepends(){
        File libs = new File(plugin.getDataFolder(), "libs");
        if (!libs.exists()){
            libs.mkdirs();
        }
        try {
            File hikari = new File(plugin.getDataFolder(), "libs/HikariCP-3.4.5.jar");
            if (!hikari.exists()){
                this.download(new URL("https://repo1.maven.org/maven2/com/zaxxer/HikariCP/3.4.5/HikariCP-3.4.5.jar"), "HikariCP-3.4.5.jar");
            } else {
                loadJarFile(hikari);
            }
            File jedis = new File(plugin.getDataFolder(), "libs/jedis-3.4.0.jar");
            if (!jedis.exists()){
                this.download(new URL("https://repo1.maven.org/maven2/redis/clients/jedis/3.4.0/jedis-3.4.0.jar"), "jedis-3.4.0.jar");
            } else {
                loadJarFile(jedis);
            }
            File commons = new File(plugin.getDataFolder(), "libs/commons-pool2-2.9.0.jar");
            if (!commons.exists()){
                this.download(new URL("https://repo1.maven.org/maven2/org/apache/commons/commons-pool2/2.9.0/commons-pool2-2.9.0.jar"), "commons-pool2-2.9.0.jar");
            } else {
                loadJarFile(commons);
            }
            File mongo = new File(plugin.getDataFolder(), "libs/mongo-java-driver-3.12.10.jar");
            if (!mongo.exists()){
                this.download(new URL("https://repo1.maven.org/maven2/org/mongodb/mongo-java-driver/3.12.10/mongo-java-driver-3.12.10.jar"), "mongo-java-driver-3.12.10.jar");
            } else {
                loadJarFile(mongo);
            }
            if (plugin.getVc().getVersion().equals("v1_15_R1") || plugin.getVc().getVersion().equals("v1_16_R1") || plugin.getVc().getVersion().equals("v1_16_R2")){
                File commonsio = new File(plugin.getDataFolder(), "libs/commons-io-1.3.2.jar");
                if (!commonsio.exists()){
                    this.download(new URL("https://repo1.maven.org/maven2/org/apache/commons/commons-io/1.3.2/commons-io-1.3.2.jar"), "commons-io-1.3.2.jar");
                } else {
                    loadJarFile(commonsio);
                }
            }
            try {
                Class.forName("org/slf4j/LoggerFactory");
            } catch (ClassNotFoundException e) {
                File slf4j = new File(plugin.getDataFolder(), "libs/slf4j-api-1.7.9.jar");
                if (!slf4j.exists()){
                    this.download(new URL("https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.9/slf4j-api-1.7.9.jar"), "slf4j-api-1.7.9.jar");
                } else {
                    loadJarFile(slf4j);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    
    private void downloadDependency(URL url, File fileName, CallBackAPI<Double> callback){
        BufferedInputStream in = null;
        FileOutputStream out = null;
        try {
            URLConnection conn = url.openConnection();
            int size = conn.getContentLength();
            in = new BufferedInputStream(url.openStream());
            out = new FileOutputStream(fileName);
            byte[] data = new byte[1024];
            int count;
            double sumCount = 0.0;
            while ((count = in.read(data, 0, 1024)) != -1) {
                out.write(data, 0, count);
                sumCount += count;
                if (size > 0){
                    double porcentage = (sumCount / size * 100.0);
                    callback.done(porcentage);
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            if (out != null)
                try {
                    out.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
        }
    }
    
    private void download(URL url, String name){
        File libraries = new File(plugin.getDataFolder(), "libs");
        if (!libraries.exists()){
            libraries.mkdir();
        }
        
        File fileName = new File(libraries, name + ".jar");
        if (!fileName.exists()){
            try {
                fileName.createNewFile();
                downloadDependency(url, fileName, value -> {
                    if (value >= 100){
                        plugin.sendLogMessage("Downloading §b" + name + " §e" + String.format("%.1f", value).replaceAll(",", ".") + "%");
                        loadJarFile(fileName);
                        plugin.sendLogMessage("Download of §b" + name + " §ehas been completed.");
                    }
                });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            loadJarFile(fileName);
        }
        
    }
    
    private void loadJarFile(File jar){
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            Class<?> getClass = classLoader.getClass();
            Method method = getClass.getSuperclass().getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(classLoader, jar.toURI().toURL());
        } catch (NoSuchMethodException | MalformedURLException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
}