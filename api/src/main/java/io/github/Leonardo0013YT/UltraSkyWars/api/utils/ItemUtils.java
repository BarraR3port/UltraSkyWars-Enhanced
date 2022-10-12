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
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ItemUtils {
    
    private final ItemStack item;
    private final ItemMeta im;
    
    public ItemUtils(XMaterial material){
        this.item = new ItemStack(material.parseMaterial(), 1, material.getData());
        this.im = item.getItemMeta();
    }
    
    public ItemUtils(XMaterial material, int amount){
        this.item = new ItemStack(material.parseMaterial(), amount, material.getData());
        this.im = item.getItemMeta();
    }
    
    public ItemUtils(Material material, int amount){
        this.item = new ItemStack(material, amount, (short) 0);
        this.im = item.getItemMeta();
    }
    
    public ItemUtils(Material material, short data){
        this.item = new ItemStack(material, 1, data);
        this.im = item.getItemMeta();
    }
    
    public ItemUtils(Material material, byte amount, short data){
        this.item = new ItemStack(material, amount, data);
        this.im = item.getItemMeta();
    }
    
    public ItemUtils(DyeColor color){
        Material banner = Material.getMaterial("BANNER");
        if (banner == null){
            banner = Material.getMaterial("WHITE_BANNER");
        }
        this.item = new ItemStack(banner);
        this.im = item.getItemMeta();
        BannerMeta bm = (BannerMeta) im;
        bm.setBaseColor(color);
    }
    
    public ItemUtils(ItemStack item, int amount){
        this.item = new ItemStack(item);
        this.item.setAmount(amount);
        this.im = item.getItemMeta();
    }
    
    public ItemUtils(ItemStack item){
        this.item = new ItemStack(item);
        this.im = item.getItemMeta();
    }
    
    public ItemUtils setOwner(String owner){
        if (!item.getType().name().contains("SKULL_ITEM") && !item.getType().name().contains("PLAYER_HEAD"))
            return this;
        if (owner.isEmpty()) return this;
        SkullMeta headMeta = (SkullMeta) im;
        headMeta.setOwner(owner);
        return this;
    }
    
    public ItemUtils setUrl(String url){
        if (!item.getType().name().contains("SKULL_ITEM") && !item.getType().name().contains("PLAYER_HEAD"))
            return this;
        if (url.isEmpty()) return this;
        SkullMeta headMeta = (SkullMeta) im;
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", url));
        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
        }
        return this;
    }
    
    public ItemUtils setTexture(String texture){
        if (!item.getType().name().contains("SKULL_ITEM") && !item.getType().name().contains("PLAYER_HEAD"))
            return this;
        if (texture.isEmpty()) return this;
        SkullMeta headMeta = (SkullMeta) im;
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", texture).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException var8) {
            var8.printStackTrace();
        }
        return this;
    }
    
    public ItemUtils setDisplayName(String displayName){
        this.im.setDisplayName(displayName);
        return this;
    }
    
    public ItemUtils setLore(String lore){
        this.im.setLore(lore.isEmpty() ? new ArrayList<>() : Arrays.asList(lore.split("\\n")));
        return this;
    }
    
    public ItemUtils setLore(List<String> lore){
        this.im.setLore(lore);
        return this;
    }
    
    public ItemUtils setUnbreakable(boolean unbreakable){
        this.im.spigot().setUnbreakable(unbreakable);
        return this;
    }
    
    public ItemUtils addEnchant(Enchantment enchantment, int level){
        this.im.addEnchant(enchantment, level, true);
        return this;
    }
    
    public ItemUtils applyAttributes(){
        this.im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
        return this;
    }
    
    public ItemStack build(){
        item.setItemMeta(im);
        return item;
    }
    
}