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

package io.github.Leonardo0013YT.UltraSkyWars.api.objects;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.RewardType;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.NBTEditor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

@Getter
public class Reward {
    
    private final String name;
    private final RewardType rarity;
    private final List<String> rewards;
    private final List<String> alreadyCommands;
    private final double chance;
    private final boolean useAlreadyCMD;
    private final UltraSkyWarsApi plugin;
    private final int id;
    private ItemStack icon;
    
    public Reward(UltraSkyWarsApi plugin, String path, int id){
        this.plugin = plugin;
        this.id = id;
        this.rarity = RewardType.valueOf(plugin.getRewards().get(path + ".type"));
        this.rewards = plugin.getRewards().getList(path + ".cmds");
        this.useAlreadyCMD = plugin.getRewards().getBooleanOrDefault(path + ".useAlreadyCMD", false);
        this.alreadyCommands = plugin.getRewards().getListOrDefault(path + ".alreadyCommands", Collections.singletonList("eco give <player> 100"));
        this.name = plugin.getRewards().get(path + ".name");
        this.chance = plugin.getRewards().getConfig().getDouble(path + ".chance");
        if (plugin.getRewards().getConfig().get(path + ".icon") instanceof ItemStack){
            icon = plugin.getRewards().getConfig().getItemStack(path + ".icon");
        } else {
            ItemStack head = NBTEditor.getHead(plugin.getRewards().getConfig().getString(path + ".icon"));
            ItemMeta headM = head.getItemMeta();
            headM.setDisplayName(plugin.getRewards().get(path + ".iconName"));
            headM.setLore(plugin.getRewards().getList(path + ".iconLore"));
            head.setItemMeta(headM);
            icon = head;
        }
        if (icon.getAmount() < 1){
            icon.setAmount(1);
        }
        this.icon = NBTEditor.set(icon, id, "REWARD", "ID");
    }
    
    public void execute(Player p){
        for ( String cmd : rewards ){
            if (useAlreadyCMD && cmd.startsWith("/sw ")){
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getAdm().parsePlaceholders(p, cmd.replaceAll("<player>", p.getName())) + " " + id);
            } else {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getAdm().parsePlaceholders(p, cmd.replaceAll("<player>", p.getName())));
            }
        }
    }
    
    public void executeSecond(Player p){
        p.sendMessage(plugin.getLang().get("messages.alreadyReward"));
        for ( String cmd : alreadyCommands ){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getAdm().parsePlaceholders(p, cmd.replaceAll("<player>", p.getName())));
        }
    }
    
}