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

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.calls.CallBackAPI;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

public class ItemBuilder {
    
    public static ItemStack parse(ItemStack item, CallBackAPI<String> done, String[]... t){
        ItemStack i = item.clone();
        String display = (i.hasItemMeta() && i.getItemMeta().hasDisplayName()) ? i.getItemMeta().getDisplayName() : "";
        ItemMeta im = i.getItemMeta();
        for ( String[] s : t ){
            String s1 = s[0];
            String s2 = s[1];
            String s3 = s[2];
            if (display.equals(s1)){
                done.done(s1);
                im.setDisplayName(display.replace(s1, s2));
                im.setLore(s3.isEmpty() ? new ArrayList<>() : Arrays.asList(s3.split("\\n")));
                break;
            }
        }
        if (im != null){
            addItemFlags(im);
        }
        i.setItemMeta(im);
        return i;
    }
    
    public static ItemStack parse(ItemStack item, String[]... t){
        ItemStack i = item.clone();
        String display = (i.hasItemMeta() && i.getItemMeta().hasDisplayName()) ? i.getItemMeta().getDisplayName() : "";
        ItemMeta im = i.getItemMeta();
        for ( String[] s : t ){
            String s1 = s[0];
            String s2 = s[1];
            String s3 = s[2];
            if (display.equals(s1)){
                im.setDisplayName(display.replace(s1, s2));
                im.setLore(s3.isEmpty() ? new ArrayList<>() : Arrays.asList(s3.split("\\n")));
                break;
            }
        }
        if (im != null){
            addItemFlags(im);
        }
        i.setItemMeta(im);
        return i;
    }
    
    public static ItemStack parseVariables(Player p, ItemStack item){
        ItemStack i = item.clone();
        if (!i.hasItemMeta() || !i.getItemMeta().hasDisplayName()){
            return i;
        }
        ItemMeta im = i.getItemMeta();
        List<String> lore = (i.hasItemMeta() && i.getItemMeta().hasLore()) ? i.getItemMeta().getLore() : Collections.emptyList();
        for ( int k = 0; k < lore.size(); k++ ){
            String value = lore.get(k);
            lore.set(k, UltraSkyWarsApi.get().getAdm().parsePlaceholders(p, value));
        }
        im.setLore(lore);
        i.setItemMeta(im);
        return i;
    }
    
    public static ItemStack parseVariables(Player p, ItemStack item, UltraSkyWarsApi plugin, String[]... t){
        ItemStack i = item.clone();
        if (!i.hasItemMeta() || !i.getItemMeta().hasDisplayName()){
            return i;
        }
        String d = i.getItemMeta().getDisplayName();
        List<String> lore = (i.hasItemMeta() && i.getItemMeta().hasLore()) ? i.getItemMeta().getLore() : Collections.emptyList();
        if (!lore.isEmpty()){
            for ( String[] s : t ){
                String s1 = s[0];
                String s2 = s[1];
                for ( int k = 0; k < lore.size(); k++ ){
                    String value = lore.get(k);
                    if (value.contains(s1)){
                        String newValue = value.replace(s1, s2);
                        lore.set(k, plugin.getAdm().parsePlaceholders(p, newValue));
                    } else {
                        lore.set(k, plugin.getAdm().parsePlaceholders(p, value));
                    }
                }
                d = d.replaceAll(s1, s2);
            }
        }
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(d);
        im.setLore(lore);
        i.setItemMeta(im);
        return i;
    }
    
    public static ItemStack parseChestVariables(Player p, ItemStack item, UltraSkyWarsApi plugin, CallBackAPI<String> selected, String[]... t){
        ItemStack i = item.clone();
        if (!i.hasItemMeta() || !i.getItemMeta().hasDisplayName()){
            return i;
        }
        String d = i.getItemMeta().getDisplayName();
        List<String> lore = (i.hasItemMeta() && i.getItemMeta().hasLore()) ? i.getItemMeta().getLore() : Collections.emptyList();
        if (!lore.isEmpty()){
            for ( String[] s : t ){
                String s1 = s[0];
                String s2 = s[1];
                String s3 = s[2];
                for ( int k = 0; k < lore.size(); k++ ){
                    String value = lore.get(k);
                    if (value.contains(s1)){
                        String newValue = value.replace(s1, s2);
                        lore.set(k, plugin.getAdm().parsePlaceholders(p, newValue));
                        selected.done(s3);
                    } else {
                        lore.set(k, plugin.getAdm().parsePlaceholders(p, value));
                    }
                }
                d = d.replaceAll(s1, s2);
            }
        }
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(d);
        im.setLore(lore);
        i.setItemMeta(im);
        return i;
    }
    
    public static ItemStack item(XMaterial material, String displayName, String s){
        ItemStack itemStack = new ItemStack(material.parseMaterial(), 1, material.getData());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(s.isEmpty() ? new ArrayList<>() : Arrays.asList(s.split("\\n")));
        addItemFlags(itemMeta);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    public static ItemStack item(ItemStack item, String displayName, List<String> s){
        ItemStack itemStack = item.clone();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.hasLore()){
            itemMeta.getLore().clear();
        }
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(s);
        addItemFlags(itemMeta);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    public static ItemStack item(Material material, int n, String displayName, String s){
        ItemStack itemStack = new ItemStack(material, n, (short) 0);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(s.isEmpty() ? new ArrayList<>() : Arrays.asList(s.split("\\n")));
        addItemFlags(itemMeta);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    public static ItemStack item(Material material, int n, int data, String displayName, String s){
        ItemStack itemStack = new ItemStack(material, n, (short) data);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(s.isEmpty() ? new ArrayList<>() : Arrays.asList(s.split("\\n")));
        addItemFlags(itemMeta);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    public static ItemStack item(XMaterial material, int n, String displayName, String s){
        ItemStack itemStack = new ItemStack(material.parseMaterial(), n, material.getData());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(s.isEmpty() ? new ArrayList<>() : Arrays.asList(s.split("\\n")));
        addItemFlags(itemMeta);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    public static ItemStack item(XMaterial material, int n, String displayName, List<String> s){
        ItemStack itemStack = new ItemStack(material.parseMaterial(), n, material.getData());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(s);
        addItemFlags(itemMeta);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    public static ItemStack skull(XMaterial material, int n, String displayName, String s, String owner){
        return skull(material, n, displayName, s.isEmpty() ? new ArrayList<>() : Arrays.asList(s.split("\\n")), owner);
    }
    
    public static ItemStack skull(XMaterial material, int n, String displayName, List<String> s, String owner){
        ItemStack itemStack = new ItemStack(material.parseMaterial(), n, material.getData());
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setOwner(owner);
        skullMeta.setDisplayName(displayName);
        skullMeta.setLore(s);
        addItemFlags(skullMeta);
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }
    
    public static ItemStack createSkull(String displayName, String lore, String url){
        ItemStack head = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1, XMaterial.PLAYER_HEAD.getData());
        if (url.isEmpty()) return head;
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        headMeta.setDisplayName(displayName);
        headMeta.setLore(lore.isEmpty() ? new ArrayList<>() : Arrays.asList(lore.split("\\n")));
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", url));
        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }
    
    public static ItemStack createSkull(String displayName, List<String> lore, String url){
        ItemStack head = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1, XMaterial.PLAYER_HEAD.getData());
        if (url.isEmpty()) return head;
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        headMeta.setDisplayName(displayName);
        headMeta.setLore(lore);
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", url));
        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }
    
    public static ItemStack nameLore(ItemStack itemStack, String displayName, String s){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(null);
        itemMeta.setLore(s.isEmpty() ? new ArrayList<>() : Arrays.asList(s.split("\\n")));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    public static void addItemFlags(ItemMeta itemMeta){
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
    }
    
}
