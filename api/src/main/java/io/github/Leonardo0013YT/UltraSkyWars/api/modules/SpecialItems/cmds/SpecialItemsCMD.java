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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.SpecialItems.cmds;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.fanciful.FancyMessage;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.SpecialItems.InjectionSpecialItems;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpecialItemsCMD implements CommandExecutor {
    
    private final UltraSkyWarsApi plugin;
    private final InjectionSpecialItems isi;
    
    public SpecialItemsCMD(UltraSkyWarsApi plugin, InjectionSpecialItems isi){
        this.plugin = plugin;
        this.isi = isi;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (sender instanceof Player){
            Player p = (Player) sender;
            if (!p.hasPermission("usw.admin")){
                p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                return true;
            }
            if (args.length < 1){
                sendHelp(sender);
                return true;
            }
            if ("getitem".equalsIgnoreCase(args[0])){
                if (args.length < 2){
                    p.sendMessage("§eAvailable Items:");
                    p.sendMessage("§7 - §aCompass");
                    p.sendMessage("§7 - §aInstantTNT");
                    p.sendMessage("§7 - §aSoup");
                    p.sendMessage("§7 - §aEndBuff");
                    p.sendMessage("§7 - §aTNTLaunch");
                    return true;
                }
                switch(args[1].toLowerCase()){
                    case "compass":
                        p.getInventory().addItem(isi.getIm().getCompass());
                        break;
                    case "instanttnt":
                        p.getInventory().addItem(isi.getIm().getInstantTNT());
                        break;
                    case "soup":
                        p.getInventory().addItem(isi.getIm().getSoup());
                        break;
                    case "endbuff":
                        p.getInventory().addItem(isi.getIm().getEndBuff());
                        break;
                    case "tntlaunch":
                        p.getInventory().addItem(isi.getIm().getTNTLaunch());
                        break;
                }
            }
        }
        return false;
    }
    
    private void sendHelp(CommandSender s){
        s.sendMessage("§7§m---------------§r   §6§lUltraSkyWars §ev" + plugin.getDescription().getVersion() + "§r   §7§m---------------");
        s.sendMessage("§7       §7");
        new FancyMessage("§b/swi getitem <name> §7- §eGet the item.").setHover(HoverEvent.Action.SHOW_TEXT, "§fClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/swi getitem ").build().send(s);
        s.sendMessage("§7§m----------------------------------------------------");
    }
    
}