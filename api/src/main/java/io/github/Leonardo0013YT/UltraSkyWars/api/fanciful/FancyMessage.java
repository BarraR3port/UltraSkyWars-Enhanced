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

package io.github.Leonardo0013YT.UltraSkyWars.api.fanciful;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class FancyMessage {
    
    private final Collection<TextComponent> text;
    private TextComponent now;
    
    public FancyMessage(String msg){
        this.text = new ArrayList<>();
        this.now = new TextComponent(msg);
    }
    
    public FancyMessage addMsg(String msg){
        text.add(now);
        this.now = new TextComponent(msg);
        return this;
    }
    
    public FancyMessage bold(boolean b){
        now.setBold(b);
        return this;
    }
    
    public FancyMessage color(ChatColor color){
        now.setColor(color);
        return this;
    }
    
    public FancyMessage setClick(ClickEvent.Action ca, String cmd){
        now.setClickEvent(new ClickEvent(ca, cmd));
        return this;
    }
    
    public FancyMessage setHover(HoverEvent.Action ha, String msg){
        now.setHoverEvent(new HoverEvent(ha, new ComponentBuilder(msg).create()));
        return this;
    }
    
    public FancyMessage build(){
        text.add(now);
        return this;
    }
    
    public void send(Player p){
        TextComponent[] tc = new TextComponent[text.size()];
        int i = 0;
        for ( TextComponent s : text ){
            tc[i] = s;
            i++;
        }
        p.spigot().sendMessage(tc);
    }
    
    public void send(CommandSender p){
        if (p instanceof Player){
            Player s = (Player) p;
            send(s);
            return;
        }
        TextComponent[] tc = text.toArray(new TextComponent[0]);
        p.sendMessage(new TextComponent(tc).toString());
    }
    
}