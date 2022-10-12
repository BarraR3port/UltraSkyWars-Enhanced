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

package io.github.Leonardo0013YT.UltraSkyWars.cmds;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.State;
import io.github.Leonardo0013YT.UltraSkyWars.api.game.GameData;
import io.github.Leonardo0013YT.UltraSkyWars.api.game.UltraGame;
import io.github.Leonardo0013YT.UltraSkyWars.api.game.UltraRankedGame;
import io.github.Leonardo0013YT.UltraSkyWars.api.game.UltraTeamGame;
import io.github.Leonardo0013YT.UltraSkyWars.api.objects.CustomJoin;
import io.github.Leonardo0013YT.UltraSkyWars.api.objects.Reward;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class SkyWarsCMD implements CommandExecutor {
    
    private final UltraSkyWarsApi plugin;
    
    public SkyWarsCMD(UltraSkyWarsApi plugin){
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (sender instanceof Player){
            Player p = (Player) sender;
            if (args.length < 1){
                sendHelp(sender);
                return true;
            }
            switch(args[0].toLowerCase()){
                case "clearstats":
                    if (!p.hasPermission("usw.admin")){
                        p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                        return true;
                    }
                    if (Bukkit.getOnlinePlayers().size() > 1){
                        p.sendMessage(plugin.getLang().get("setup.noClearStatPlayers").replace("<online>", String.valueOf(Bukkit.getOnlinePlayers().size())));
                        return true;
                    }
                    plugin.getDb().clearStats(p);
                    break;
                case "rankedmenu":
                    if (!plugin.getIjm().isEloRankInjection()){
                        p.sendMessage(plugin.getLang().get(p, "injections.eloRank"));
                        return true;
                    }
                    plugin.getIjm().getEloRank().getRem().createRankedMenu(p);
                    break;
                case "soulwellmenu":
                    if (!plugin.getIjm().isSoulWellInjection()){
                        p.sendMessage(plugin.getLang().get(p, "injections.soulwell"));
                        return true;
                    }
                    plugin.getIjm().getSoulwell().getWel().createSoulWellMenu(p);
                    break;
                case "kitsperks":
                    plugin.getUim().openContentInventory(p, plugin.getUim().getMenus("kitsperks"));
                    break;
                case "kitsmenu":
                    if (args.length < 2){
                        sendHelp(sender);
                        return true;
                    }
                    String kitType = args[1].toUpperCase();
                    if (kitType.equals("SOLO") || kitType.equals("TEAM") || kitType.equals("RANKED")){
                        plugin.getUim().getPages().put(p.getUniqueId(), 1);
                        boolean isGame = plugin.getGm().isPlayerInGame(p);
                        plugin.getUim().createKitSelectorMenu(p, kitType, isGame);
                    } else {
                        p.sendMessage(plugin.getLang().get("messages.kitTypes"));
                    }
                    break;
                case "levelsmenu":
                    plugin.getGem().createLevelMenu(p);
                    break;
                case "prestigemenu":
                    plugin.getGem().createPrestigeIcons(p);
                    break;
                case "glassmenu":
                    plugin.getUim().getPages().put(p.getUniqueId(), 1);
                    plugin.getUim().createGlassSelectorMenu(p);
                    break;
                case "balloonsmenu":
                    plugin.getUim().getPages().put(p.getUniqueId(), 1);
                    plugin.getUim().createBalloonSelectorMenu(p);
                    break;
                case "killsoundsmenu":
                    plugin.getUim().getPages().put(p.getUniqueId(), 1);
                    plugin.getUim().createKillSoundSelectorMenu(p);
                    break;
                case "killeffectsmenu":
                    plugin.getUim().getPages().put(p.getUniqueId(), 1);
                    plugin.getUim().createKillEffectSelectorMenu(p);
                    break;
                case "windancesmenu":
                    plugin.getUim().getPages().put(p.getUniqueId(), 1);
                    plugin.getUim().createWinDanceSelectorMenu(p);
                    break;
                case "wineffectsmenu":
                    plugin.getUim().getPages().put(p.getUniqueId(), 1);
                    plugin.getUim().createWinEffectSelectorMenu(p);
                    break;
                case "trailsmenu":
                    plugin.getUim().getPages().put(p.getUniqueId(), 1);
                    plugin.getUim().createTrailsSelectorMenu(p);
                    break;
                case "tauntsmenu":
                    plugin.getUim().getPages().put(p.getUniqueId(), 1);
                    plugin.getUim().createTauntsSelectorMenu(p);
                    break;
                case "partingmenu":
                    plugin.getUim().getPages().put(p.getUniqueId(), 1);
                    plugin.getUim().createPartingSelectorMenu(p);
                    break;
                case "perksmenu":
                    if (args.length < 2){
                        sendHelp(sender);
                        return true;
                    }
                    if (plugin.getIjm().isPerksInjection()){
                        String perkType = args[1].toUpperCase();
                        if (perkType.equals("SOLO") || perkType.equals("TEAM") || perkType.equals("RANKED")){
                            plugin.getGem().createPerksMenu(p, perkType);
                        } else {
                            p.sendMessage(plugin.getLang().get("messages.perkTypes"));
                        }
                    } else {
                        p.sendMessage(plugin.getLang().get(p, "injections.perks"));
                    }
                    break;
                case "forcestart":
                    if (!p.hasPermission("usw.forcestart")){
                        p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                        return true;
                    }
                    if (!plugin.getGm().isPlayerInGame(p)){
                        p.sendMessage(plugin.getLang().get(p, "messages.noInGame"));
                        return true;
                    }
                    Game gamestart = plugin.getGm().getGameByPlayer(p);
                    int min = gamestart.getTeamSize() * 2;
                    if (gamestart.isState(State.WAITING) || gamestart.isState(State.STARTING)){
                        if (gamestart.getPlayers().size() < min){
                            p.sendMessage(plugin.getLang().get(p, "messages.noMinPlayers"));
                            return true;
                        }
                        if (gamestart.getStarting() < 5){
                            p.sendMessage(plugin.getLang().get(p, "messages.alreadyStart"));
                        } else {
                            gamestart.forceStart(p);
                        }
                    } else {
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyStart"));
                    }
                    break;
                case "perks":
                    if (args.length < 5){
                        sendHelp(sender);
                        return true;
                    }
                    if (!p.hasPermission("usw.admin")){
                        p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                        return true;
                    }
                    if (!plugin.getIjm().isPerksInjection()){
                        p.sendMessage(plugin.getLang().get(p, "injections.perks"));
                        return true;
                    }
                    Player cp = Bukkit.getPlayer(args[2]);
                    if (cp == null){
                        p.sendMessage(plugin.getLang().get(p, "setup.noOnline"));
                        return true;
                    }
                    int idp;
                    try {
                        idp = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return true;
                    }
                    int perkLevel;
                    try {
                        perkLevel = Integer.parseInt(args[4]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return true;
                    }
                    if (args.length > 6){
                        try {
                            int rID = Integer.parseInt(args[5]);
                            Reward r = plugin.getLvl().getRewardByID(rID);
                            r.executeSecond(cp);
                            return true;
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    SWPlayer cswp = plugin.getDb().getSWPlayer(cp);
                    switch(args[1].toLowerCase()){
                        case "add":
                            if (cswp.getPerksData().containsKey(idp)){
                                p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.alreadyHas"));
                                return true;
                            }
                            cswp.getPerksData().put(idp, perkLevel);
                            p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.add").replaceAll("<type>", "Perk").replaceAll("<id>", String.valueOf(idp)).replaceAll("<player>", cp.getName()));
                            break;
                        case "remove":
                            if (!cswp.getPerksData().containsKey(idp)){
                                p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.noHas"));
                                return true;
                            }
                            cswp.getPerksData().remove(idp);
                            p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.remove").replaceAll("<type>", "Perk").replaceAll("<id>", String.valueOf(idp)).replaceAll("<player>", cp.getName()));
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "balloon":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    if (!p.hasPermission("usw.admin")){
                        p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                        return true;
                    }
                    Player c1 = Bukkit.getPlayer(args[2]);
                    if (c1 == null){
                        p.sendMessage(plugin.getLang().get(p, "setup.noOnline"));
                        return true;
                    }
                    int id1;
                    try {
                        id1 = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return true;
                    }
                    if (args.length > 4){
                        try {
                            int rID = Integer.parseInt(args[4]);
                            Reward r = plugin.getLvl().getRewardByID(rID);
                            r.executeSecond(c1);
                            return true;
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    SWPlayer csw1 = plugin.getDb().getSWPlayer(c1);
                    switch(args[1].toLowerCase()){
                        case "add":
                            if (csw1.getBalloons().contains(id1)){
                                p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.alreadyHas"));
                                return true;
                            }
                            csw1.getBalloons().add(id1);
                            p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.add").replaceAll("<type>", "Balloon").replaceAll("<id>", String.valueOf(id1)).replaceAll("<player>", c1.getName()));
                            break;
                        case "remove":
                            if (!csw1.getBalloons().contains(id1)){
                                p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.noHas"));
                                return true;
                            }
                            csw1.getBalloons().removeAll(Collections.singletonList(id1));
                            p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.remove").replaceAll("<type>", "Balloon").replaceAll("<id>", String.valueOf(id1)).replaceAll("<player>", c1.getName()));
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "glass":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    if (!p.hasPermission("usw.admin")){
                        p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                        return true;
                    }
                    Player c2 = Bukkit.getPlayer(args[2]);
                    if (c2 == null){
                        p.sendMessage(plugin.getLang().get(p, "setup.noOnline"));
                        return true;
                    }
                    int id2;
                    try {
                        id2 = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return true;
                    }
                    if (args.length > 4){
                        try {
                            int rID = Integer.parseInt(args[4]);
                            Reward r = plugin.getLvl().getRewardByID(rID);
                            r.executeSecond(c2);
                            return true;
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    SWPlayer csw2 = plugin.getDb().getSWPlayer(c2);
                    switch(args[1].toLowerCase()){
                        case "add":
                            if (csw2.getGlasses().contains(id2)){
                                p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.alreadyHas"));
                                return true;
                            }
                            csw2.getGlasses().add(id2);
                            p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.add").replaceAll("<type>", "Glass").replaceAll("<id>", String.valueOf(id2)).replaceAll("<player>", c2.getName()));
                            break;
                        case "remove":
                            if (!csw2.getGlasses().contains(id2)){
                                p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.noHas"));
                                return true;
                            }
                            csw2.getGlasses().removeAll(Collections.singletonList(id2));
                            p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.remove").replaceAll("<type>", "Glass").replaceAll("<id>", String.valueOf(id2)).replaceAll("<player>", c2.getName()));
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "killeffect":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    if (!p.hasPermission("usw.admin")){
                        p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                        return true;
                    }
                    Player c3 = Bukkit.getPlayer(args[2]);
                    if (c3 == null){
                        p.sendMessage(plugin.getLang().get(p, "setup.noOnline"));
                        return true;
                    }
                    int id3;
                    try {
                        id3 = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return true;
                    }
                    if (args.length > 4){
                        try {
                            int rID = Integer.parseInt(args[4]);
                            Reward r = plugin.getLvl().getRewardByID(rID);
                            r.executeSecond(c3);
                            return true;
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    SWPlayer csw3 = plugin.getDb().getSWPlayer(c3);
                    switch(args[1].toLowerCase()){
                        case "add":
                            if (csw3.getKilleffects().contains(id3)){
                                p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.alreadyHas"));
                                return true;
                            }
                            csw3.getKilleffects().add(id3);
                            p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.add").replaceAll("<type>", "KillEffect").replaceAll("<id>", String.valueOf(id3)).replaceAll("<player>", c3.getName()));
                            break;
                        case "remove":
                            if (!csw3.getKilleffects().contains(id3)){
                                p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.noHas"));
                                return true;
                            }
                            csw3.getKilleffects().removeAll(Collections.singletonList(id3));
                            p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.remove").replaceAll("<type>", "KillEffect").replaceAll("<id>", String.valueOf(id3)).replaceAll("<player>", c3.getName()));
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "killsound":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    if (!p.hasPermission("usw.admin")){
                        p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                        return true;
                    }
                    Player c4 = Bukkit.getPlayer(args[2]);
                    if (c4 == null){
                        p.sendMessage(plugin.getLang().get(p, "setup.noOnline"));
                        return true;
                    }
                    int id4;
                    try {
                        id4 = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return true;
                    }
                    if (args.length > 4){
                        try {
                            int rID = Integer.parseInt(args[4]);
                            Reward r = plugin.getLvl().getRewardByID(rID);
                            r.executeSecond(c4);
                            return true;
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    SWPlayer csw4 = plugin.getDb().getSWPlayer(c4);
                    switch(args[1].toLowerCase()){
                        case "add":
                            if (csw4.getKillsounds().contains(id4)){
                                p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.alreadyHas"));
                                return true;
                            }
                            csw4.getKillsounds().add(id4);
                            p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.add").replaceAll("<type>", "KillSound").replaceAll("<id>", String.valueOf(id4)).replaceAll("<player>", c4.getName()));
                            break;
                        case "remove":
                            if (!csw4.getKillsounds().contains(id4)){
                                p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.noHas"));
                                return true;
                            }
                            csw4.getKillsounds().removeAll(Collections.singletonList(id4));
                            p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.remove").replaceAll("<type>", "KillSound").replaceAll("<id>", String.valueOf(id4)).replaceAll("<player>", c4.getName()));
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "kit":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    if (!p.hasPermission("usw.admin")){
                        p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                        return true;
                    }
                    Player c5 = Bukkit.getPlayer(args[2]);
                    if (c5 == null){
                        p.sendMessage(plugin.getLang().get(p, "setup.noOnline"));
                        return true;
                    }
                    int id5;
                    try {
                        id5 = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return true;
                    }
                    if (args.length > 4){
                        try {
                            int rID = Integer.parseInt(args[4]);
                            Reward r = plugin.getLvl().getRewardByID(rID);
                            r.executeSecond(c5);
                            return true;
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    SWPlayer csw5 = plugin.getDb().getSWPlayer(c5);
                    switch(args[1].toLowerCase()){
                        case "add":
                            if (csw5.getKits().containsKey(id5)){
                                p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.alreadyHas"));
                                return true;
                            }
                            csw5.addKitLevel(id5, 1);
                            p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.add").replaceAll("<type>", "Kit").replaceAll("<id>", String.valueOf(id5)).replaceAll("<player>", c5.getName()));
                            break;
                        case "remove":
                            if (!csw5.getKits().containsKey(id5)){
                                p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.noHas"));
                                return true;
                            }
                            csw5.removeKitLevel(id5, 1);
                            p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.remove").replaceAll("<type>", "Kit").replaceAll("<id>", String.valueOf(id5)).replaceAll("<player>", c5.getName()));
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "parting":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    if (!p.hasPermission("usw.admin")){
                        p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                        return true;
                    }
                    Player c6 = Bukkit.getPlayer(args[2]);
                    if (c6 == null){
                        p.sendMessage(plugin.getLang().get(p, "setup.noOnline"));
                        return true;
                    }
                    int id6;
                    try {
                        id6 = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return true;
                    }
                    if (args.length > 4){
                        try {
                            int rID = Integer.parseInt(args[4]);
                            Reward r = plugin.getLvl().getRewardByID(rID);
                            r.executeSecond(c6);
                            return true;
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    SWPlayer csw6 = plugin.getDb().getSWPlayer(c6);
                    switch(args[1].toLowerCase()){
                        case "add":
                            if (csw6.getPartings().contains(id6)){
                                p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.alreadyHas"));
                                return true;
                            }
                            csw6.getPartings().add(id6);
                            p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.add").replaceAll("<type>", "Parting").replaceAll("<id>", String.valueOf(id6)).replaceAll("<player>", c6.getName()));
                            break;
                        case "remove":
                            if (!csw6.getPartings().contains(id6)){
                                p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.noHas"));
                                return true;
                            }
                            csw6.getPartings().removeAll(Collections.singletonList(id6));
                            p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.remove").replaceAll("<type>", "Parting").replaceAll("<id>", String.valueOf(id6)).replaceAll("<player>", c6.getName()));
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "taunt":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    if (!p.hasPermission("usw.admin")){
                        p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                        return true;
                    }
                    Player c7 = Bukkit.getPlayer(args[2]);
                    if (c7 == null){
                        p.sendMessage(plugin.getLang().get(p, "setup.noOnline"));
                        return true;
                    }
                    int id7;
                    try {
                        id7 = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return true;
                    }
                    if (args.length > 4){
                        try {
                            int rID = Integer.parseInt(args[4]);
                            Reward r = plugin.getLvl().getRewardByID(rID);
                            r.executeSecond(c7);
                            return true;
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    SWPlayer csw7 = plugin.getDb().getSWPlayer(c7);
                    switch(args[1].toLowerCase()){
                        case "add":
                            if (csw7.getTaunts().contains(id7)){
                                p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.alreadyHas"));
                                return true;
                            }
                            csw7.getTaunts().add(id7);
                            p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.add").replaceAll("<type>", "Taunt").replaceAll("<id>", String.valueOf(id7)).replaceAll("<player>", c7.getName()));
                            break;
                        case "remove":
                            if (!csw7.getTaunts().contains(id7)){
                                p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.noHas"));
                                return true;
                            }
                            csw7.getTaunts().removeAll(Collections.singletonList(id7));
                            p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.remove").replaceAll("<type>", "Taunt").replaceAll("<id>", String.valueOf(id7)).replaceAll("<player>", c7.getName()));
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "trail":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    if (!p.hasPermission("usw.admin")){
                        p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                        return true;
                    }
                    Player c8 = Bukkit.getPlayer(args[2]);
                    if (c8 == null){
                        p.sendMessage(plugin.getLang().get(p, "setup.noOnline"));
                        return true;
                    }
                    int id8;
                    try {
                        id8 = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return true;
                    }
                    if (args.length > 4){
                        try {
                            int rID = Integer.parseInt(args[4]);
                            Reward r = plugin.getLvl().getRewardByID(rID);
                            r.executeSecond(c8);
                            return true;
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    SWPlayer csw8 = plugin.getDb().getSWPlayer(c8);
                    switch(args[1].toLowerCase()){
                        case "add":
                            if (csw8.getTrails().contains(id8)){
                                p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.alreadyHas"));
                                return true;
                            }
                            csw8.getTrails().add(id8);
                            p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.add").replaceAll("<type>", "Trail").replaceAll("<id>", String.valueOf(id8)).replaceAll("<player>", c8.getName()));
                            break;
                        case "remove":
                            if (!csw8.getTrails().contains(id8)){
                                p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.noHas"));
                                return true;
                            }
                            csw8.getTrails().removeAll(Collections.singletonList(id8));
                            p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.remove").replaceAll("<type>", "Trail").replaceAll("<id>", String.valueOf(id8)).replaceAll("<player>", c8.getName()));
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "windance":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    if (!p.hasPermission("usw.admin")){
                        p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                        return true;
                    }
                    Player c9 = Bukkit.getPlayer(args[2]);
                    if (c9 == null){
                        p.sendMessage(plugin.getLang().get(p, "setup.noOnline"));
                        return true;
                    }
                    int id9;
                    try {
                        id9 = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return true;
                    }
                    if (args.length > 4){
                        try {
                            int rID = Integer.parseInt(args[4]);
                            Reward r = plugin.getLvl().getRewardByID(rID);
                            r.executeSecond(c9);
                            return true;
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    SWPlayer csw9 = plugin.getDb().getSWPlayer(c9);
                    switch(args[1].toLowerCase()){
                        case "add":
                            if (csw9.getWindances().contains(id9)){
                                p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.alreadyHas"));
                                return true;
                            }
                            csw9.getWindances().add(id9);
                            p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.add").replaceAll("<type>", "WinDance").replaceAll("<id>", String.valueOf(id9)).replaceAll("<player>", c9.getName()));
                            break;
                        case "remove":
                            if (!csw9.getWindances().contains(id9)){
                                p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.noHas"));
                                return true;
                            }
                            csw9.getWindances().removeAll(Collections.singletonList(id9));
                            p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.remove").replaceAll("<type>", "WinDance").replaceAll("<id>", String.valueOf(id9)).replaceAll("<player>", c9.getName()));
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "wineffect":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    if (!p.hasPermission("usw.admin")){
                        p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                        return true;
                    }
                    Player c10 = Bukkit.getPlayer(args[2]);
                    if (c10 == null){
                        p.sendMessage(plugin.getLang().get(p, "setup.noOnline"));
                        return true;
                    }
                    int id10;
                    try {
                        id10 = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                        return true;
                    }
                    if (args.length > 4){
                        try {
                            int rID = Integer.parseInt(args[4]);
                            Reward r = plugin.getLvl().getRewardByID(rID);
                            r.executeSecond(c10);
                            return true;
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    SWPlayer csw10 = plugin.getDb().getSWPlayer(c10);
                    switch(args[1].toLowerCase()){
                        case "add":
                            if (csw10.getWineffects().contains(id10)){
                                p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.alreadyHas"));
                                return true;
                            }
                            csw10.getWineffects().add(id10);
                            p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.add").replaceAll("<type>", "WinEffect").replaceAll("<id>", String.valueOf(id10)).replaceAll("<player>", c10.getName()));
                            break;
                        case "remove":
                            if (!csw10.getWineffects().contains(id10)){
                                p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.noHas"));
                                return true;
                            }
                            csw10.getWineffects().removeAll(Collections.singletonList(id10));
                            p.sendMessage(plugin.getLang().get(p, "messages.cosmetics.remove").replaceAll("<type>", "WinEffect").replaceAll("<id>", String.valueOf(id10)).replaceAll("<player>", c10.getName()));
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "spect":{
                    if (!p.hasPermission("usw.spectate")){
                        p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                        return true;
                    }
                    if (args.length < 2){
                        sendHelp(sender);
                        return true;
                    }
                    String arena = args[1];
                    Game gamea = plugin.getGm().getGameByName(arena);
                    if (gamea == null){
                        p.sendMessage(plugin.getLang().get(p, "messages.noGameExists"));
                        return true;
                    }
                    if (gamea.isState(State.WAITING) || gamea.isState(State.STARTING) || gamea.isState(State.PREGAME)){
                        p.sendMessage(plugin.getLang().get(p, "messages.theGameNotStart"));
                        return true;
                    }
                    plugin.getGm().getPlayerGame().put(p.getUniqueId(), gamea.getId());
                    gamea.getCached().add(p);
                    gamea.remove(p);
                    gamea.setSpect(p);
                    break;
                }
                case "spectplayer":{
                    if (!p.hasPermission("usw.spectate")){
                        p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                        return true;
                    }
                    if (args.length < 2){
                        sendHelp(sender);
                        return true;
                    }
                    String player = args[1];
                    Player on = Bukkit.getPlayer(player);
                    if (on == null){
                        p.sendMessage(plugin.getLang().get(p, "setup.noOnline"));
                        return true;
                    }
                    Game gamea = plugin.getGm().getGameByPlayer(on);
                    if (gamea == null){
                        p.sendMessage(plugin.getLang().get(p, "messages.noGameExists"));
                        return true;
                    }
                    if (gamea.isState(State.WAITING) || gamea.isState(State.STARTING) || gamea.isState(State.PREGAME)){
                        p.sendMessage(plugin.getLang().get(p, "messages.theGameNotStart"));
                        return true;
                    }
                    plugin.getGm().getPlayerGame().put(p.getUniqueId(), gamea.getId());
                    gamea.getCached().add(p);
                    gamea.remove(p);
                    gamea.setSpect(p);
                    break;
                }
                case "menu":
                    if (args.length < 2){
                        sendHelp(sender);
                        return true;
                    }
                    switch(args[1].toLowerCase()){
                        case "all":
                        case "solo":
                        case "team":
                        case "tnt_madness":
                        case "ranked":
                            plugin.getUim().getPages().put(p.getUniqueId(), 1);
                            plugin.getGem().createSelectorMenu(p, "none", args[1].toLowerCase());
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "customjoin":
                    if (args.length < 2){
                        sendHelp(sender);
                        return true;
                    }
                    if (plugin.getGm().isPlayerInGame(p)){
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyGame"));
                        return true;
                    }
                    String cjoin = args[1];
                    if (!plugin.getGm().getJoins().containsKey(cjoin)){
                        p.sendMessage(plugin.getLang().get(p, "messages.noGroup"));
                        return true;
                    }
                    CustomJoin cj = plugin.getGm().getJoins().get(cjoin);
                    GameData g = cj.getRandomGame();
                    if (g == null){
                        p.sendMessage(plugin.getLang().get(p, "messages.noArenaGroup"));
                        return true;
                    }
                    plugin.getGm().addPlayerGame(p, plugin.getGm().getGameID(g.getMap()));
                    break;
                case "randomjoin":
                    if (args.length < 2){
                        sendHelp(sender);
                        return true;
                    }
                    if (plugin.getGm().isPlayerInGame(p)){
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyGame"));
                        return true;
                    }
                    String type3 = args[1].toUpperCase();
                    if (plugin.getGm().getModes().contains(type3)){
                        boolean added = plugin.getGm().addRandomGame(p, type3);
                        if (!added){
                            p.sendMessage(plugin.getLang().get(p, "messages.noRandom"));
                        }
                    } else {
                        sendHelp(sender);
                    }
                    break;
                case "join":
                    if (args.length < 3){
                        sendHelp(sender);
                        return true;
                    }
                    String type = args[1];
                    String name = args[2];
                    if (plugin.getGm().isPlayerInGame(p)){
                        p.sendMessage(plugin.getLang().get(p, "messages.alreadyGame"));
                        return true;
                    }
                    GameData game = plugin.getGm().getGameData().get(name);
                    if (game == null){
                        p.sendMessage(plugin.getLang().get(p, "messages.noGameExists"));
                        return true;
                    }
                    switch(type.toLowerCase()){
                        case "solo":
                            if (game.getType().equals("solo")){
                                plugin.getGm().addPlayerGame(p, plugin.getGm().getGameID(game.getMap()));
                            } else {
                                p.sendMessage(plugin.getLang().get(p, "messages.noGameType"));
                            }
                            break;
                        case "team":
                            if (game.getType().equals("team")){
                                plugin.getGm().addPlayerGame(p, plugin.getGm().getGameID(game.getMap()));
                            } else {
                                p.sendMessage(plugin.getLang().get(p, "messages.noGameType"));
                            }
                            break;
                        case "ranked":
                            if (game.getType().equals("ranked")){
                                plugin.getGm().addPlayerGame(p, plugin.getGm().getGameID(game.getMap()));
                            } else {
                                p.sendMessage(plugin.getLang().get(p, "messages.noGameType"));
                            }
                            break;
                        case "tnt_madness":
                            if (game.getType().equals("tnt_madness")){
                                plugin.getGm().addPlayerGame(p, plugin.getGm().getGameID(game.getMap()));
                            } else {
                                p.sendMessage(plugin.getLang().get(p, "messages.noGameType"));
                            }
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "leave":
                    if (!plugin.getGm().isPlayerInGame(p)){
                        p.sendMessage(plugin.getLang().get(p, "messages.noInGame"));
                        return true;
                    }
                    plugin.getGm().removePlayerAllGame(p);
                    p.sendMessage(plugin.getLang().get(p, "messages.leaveGame"));
                    break;
                case "lobby":
                    plugin.getUim().openContentInventory(p, plugin.getUim().getMenus("lobby"));
                    break;
                case "animations":
                    if (!plugin.getIjm().isCubeletsInjection()){
                        p.sendMessage(plugin.getLang().get(p, "injections.cubelets"));
                        return true;
                    }
                    if (!p.hasPermission("ultraskywars.cubelets.animations")){
                        p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                        return true;
                    }
                    plugin.getGem().createCubeletsAnimationMenu(p);
                    break;
                case "coins":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    if (!p.hasPermission("usw.admin")){
                        p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                        return true;
                    }
                    switch(args[1].toLowerCase()){
                        case "add":
                            Player on = Bukkit.getPlayer(args[2]);
                            if (on == null){
                                p.sendMessage(plugin.getLang().get(p, "setup.noOnline"));
                                return true;
                            }
                            int amount;
                            try {
                                amount = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e) {
                                p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                                return true;
                            }
                            plugin.getAdm().addCoins(on, amount);
                            p.sendMessage(plugin.getLang().get(p, "coins.add.you").replaceAll("<coins>", String.valueOf(amount)).replaceAll("<player>", on.getName()));
                            on.sendMessage(plugin.getLang().get(p, "coins.add.receiver").replaceAll("<coins>", String.valueOf(amount)).replaceAll("<sender>", p.getName()));
                            Utils.updateSB(on);
                            break;
                        case "remove":
                            Player on1 = Bukkit.getPlayer(args[2]);
                            if (on1 == null){
                                p.sendMessage(plugin.getLang().get(p, "setup.noOnline"));
                                return true;
                            }
                            int amount1;
                            try {
                                amount1 = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e) {
                                p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                                return true;
                            }
                            plugin.getAdm().removeCoins(on1, amount1);
                            p.sendMessage(plugin.getLang().get(p, "coins.remove.you").replaceAll("<coins>", String.valueOf(amount1)).replaceAll("<player>", on1.getName()));
                            on1.sendMessage(plugin.getLang().get(p, "coins.remove.receiver").replaceAll("<coins>", String.valueOf(amount1)).replaceAll("<sender>", p.getName()));
                            Utils.updateSB(on1);
                            break;
                        case "set":
                            Player on2 = Bukkit.getPlayer(args[2]);
                            if (on2 == null){
                                p.sendMessage(plugin.getLang().get(p, "setup.noOnline"));
                                return true;
                            }
                            int amount2;
                            try {
                                amount2 = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e) {
                                p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                                return true;
                            }
                            plugin.getAdm().setCoins(on2, amount2);
                            p.sendMessage(plugin.getLang().get(p, "coins.set.you").replaceAll("<coins>", String.valueOf(amount2)).replaceAll("<player>", on2.getName()));
                            on2.sendMessage(plugin.getLang().get(p, "coins.set.receiver").replaceAll("<coins>", String.valueOf(amount2)).replaceAll("<sender>", p.getName()));
                            Utils.updateSB(on2);
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "souls":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    if (!p.hasPermission("usw.admin")){
                        p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                        return true;
                    }
                    switch(args[1].toLowerCase()){
                        case "add":
                            Player on = Bukkit.getPlayer(args[2]);
                            if (on == null){
                                p.sendMessage(plugin.getLang().get(p, "setup.noOnline"));
                                return true;
                            }
                            int amount;
                            try {
                                amount = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e) {
                                p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                                return true;
                            }
                            SWPlayer sw = plugin.getDb().getSWPlayer(on);
                            sw.addSouls(amount);
                            p.sendMessage(plugin.getLang().get(p, "souls.add.you").replaceAll("<souls>", String.valueOf(amount)).replaceAll("<player>", on.getName()));
                            on.sendMessage(plugin.getLang().get(p, "souls.add.receiver").replaceAll("<souls>", String.valueOf(amount)).replaceAll("<sender>", p.getName()));
                            Utils.updateSB(on);
                            break;
                        case "remove":
                            Player on1 = Bukkit.getPlayer(args[2]);
                            if (on1 == null){
                                p.sendMessage(plugin.getLang().get(p, "setup.noOnline"));
                                return true;
                            }
                            int amount1;
                            try {
                                amount1 = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e) {
                                p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                                return true;
                            }
                            SWPlayer sw1 = plugin.getDb().getSWPlayer(on1);
                            sw1.removeSouls(amount1);
                            p.sendMessage(plugin.getLang().get(p, "souls.remove.you").replaceAll("<souls>", String.valueOf(amount1)).replaceAll("<player>", on1.getName()));
                            on1.sendMessage(plugin.getLang().get(p, "souls.remove.receiver").replaceAll("<souls>", String.valueOf(amount1)).replaceAll("<sender>", p.getName()));
                            Utils.updateSB(on1);
                            break;
                        case "set":
                            Player on2 = Bukkit.getPlayer(args[2]);
                            if (on2 == null){
                                p.sendMessage(plugin.getLang().get(p, "setup.noOnline"));
                                return true;
                            }
                            int amount2;
                            try {
                                amount2 = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e) {
                                p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                                return true;
                            }
                            SWPlayer sw2 = plugin.getDb().getSWPlayer(on2);
                            sw2.setSouls(amount2);
                            p.sendMessage(plugin.getLang().get(p, "souls.set.you").replaceAll("<souls>", String.valueOf(amount2)).replaceAll("<player>", on2.getName()));
                            on2.sendMessage(plugin.getLang().get(p, "souls.set.receiver").replaceAll("<souls>", String.valueOf(amount2)).replaceAll("<sender>", p.getName()));
                            Utils.updateSB(on2);
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "cubelets":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    if (!p.hasPermission("usw.admin")){
                        p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                        return true;
                    }
                    if (!plugin.getIjm().isCubeletsInjection()){
                        p.sendMessage(plugin.getLang().get(p, "injections.cubelets"));
                        return true;
                    }
                    switch(args[1].toLowerCase()){
                        case "add":
                            Player on = Bukkit.getPlayer(args[2]);
                            if (on == null){
                                p.sendMessage(plugin.getLang().get(p, "setup.noOnline"));
                                return true;
                            }
                            int amount;
                            try {
                                amount = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e) {
                                p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                                return true;
                            }
                            SWPlayer sw = plugin.getDb().getSWPlayer(on);
                            sw.addCubelets(amount);
                            p.sendMessage(plugin.getLang().get(p, "cubelets.add.you").replaceAll("<cubelets>", String.valueOf(amount)).replaceAll("<player>", on.getName()));
                            on.sendMessage(plugin.getLang().get(p, "cubelets.add.receiver").replaceAll("<cubelets>", String.valueOf(amount)).replaceAll("<sender>", p.getName()));
                            Utils.updateSB(on);
                            break;
                        case "remove":
                            Player on1 = Bukkit.getPlayer(args[2]);
                            if (on1 == null){
                                p.sendMessage(plugin.getLang().get(p, "setup.noOnline"));
                                return true;
                            }
                            int amount1;
                            try {
                                amount1 = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e) {
                                p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                                return true;
                            }
                            SWPlayer sw1 = plugin.getDb().getSWPlayer(on1);
                            sw1.removeCubelets(amount1);
                            p.sendMessage(plugin.getLang().get(p, "cubelets.remove.you").replaceAll("<cubelets>", String.valueOf(amount1)).replaceAll("<player>", on1.getName()));
                            on1.sendMessage(plugin.getLang().get(p, "cubelets.remove.receiver").replaceAll("<cubelets>", String.valueOf(amount1)).replaceAll("<sender>", p.getName()));
                            Utils.updateSB(on1);
                            break;
                        case "set":
                            Player on2 = Bukkit.getPlayer(args[2]);
                            if (on2 == null){
                                p.sendMessage(plugin.getLang().get(p, "setup.noOnline"));
                                return true;
                            }
                            int amount2;
                            try {
                                amount2 = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e) {
                                p.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                                return true;
                            }
                            SWPlayer sw2 = plugin.getDb().getSWPlayer(on2);
                            sw2.setCubelets(amount2);
                            p.sendMessage(plugin.getLang().get(p, "cubelets.set.you").replaceAll("<cubelets>", String.valueOf(amount2)).replaceAll("<player>", on2.getName()));
                            on2.sendMessage(plugin.getLang().get(p, "cubelets.set.receiver").replaceAll("<cubelets>", String.valueOf(amount2)).replaceAll("<sender>", p.getName()));
                            Utils.updateSB(on2);
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "xp":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    if (!p.hasPermission("usw.admin")){
                        p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                        return true;
                    }
                    switch(args[1].toLowerCase()){
                        case "add":
                            Player on = Bukkit.getPlayer(args[2]);
                            if (on == null){
                                sender.sendMessage(plugin.getLang().get(p, "setup.noOnline"));
                                return true;
                            }
                            int amount;
                            try {
                                amount = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                                return true;
                            }
                            SWPlayer sw = plugin.getDb().getSWPlayer(on);
                            sw.addXp(amount);
                            sender.sendMessage(plugin.getLang().get(p, "xp.add.you").replaceAll("<xp>", String.valueOf(amount)).replaceAll("<player>", on.getName()));
                            on.sendMessage(plugin.getLang().get(p, "xp.add.receiver").replaceAll("<xp>", String.valueOf(amount)).replaceAll("<sender>", sender.getName()));
                            plugin.getLvl().checkUpgrade(on);
                            Utils.updateSB(on);
                            break;
                        case "remove":
                            Player on1 = Bukkit.getPlayer(args[2]);
                            if (on1 == null){
                                sender.sendMessage(plugin.getLang().get(p, "setup.noOnline"));
                                return true;
                            }
                            int amount1;
                            try {
                                amount1 = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                                return true;
                            }
                            SWPlayer sw1 = plugin.getDb().getSWPlayer(on1);
                            sw1.removeXp(amount1);
                            sender.sendMessage(plugin.getLang().get(p, "xp.remove.you").replaceAll("<xp>", String.valueOf(amount1)).replaceAll("<player>", on1.getName()));
                            on1.sendMessage(plugin.getLang().get(p, "xp.remove.receiver").replaceAll("<xp>", String.valueOf(amount1)).replaceAll("<sender>", sender.getName()));
                            plugin.getLvl().checkUpgrade(on1);
                            Utils.updateSB(on1);
                            break;
                        case "set":
                            Player on2 = Bukkit.getPlayer(args[2]);
                            if (on2 == null){
                                sender.sendMessage(plugin.getLang().get(p, "setup.noOnline"));
                                return true;
                            }
                            int amount2;
                            try {
                                amount2 = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get(p, "setup.noNumber"));
                                return true;
                            }
                            SWPlayer sw2 = plugin.getDb().getSWPlayer(on2);
                            sw2.setXp(amount2);
                            sender.sendMessage(plugin.getLang().get(p, "xp.set.you").replaceAll("<xp>", String.valueOf(amount2)).replaceAll("<player>", on2.getName()));
                            on2.sendMessage(plugin.getLang().get(p, "xp.set.receiver").replaceAll("<xp>", String.valueOf(amount2)).replaceAll("<sender>", sender.getName()));
                            plugin.getLvl().checkUpgrade(on2);
                            Utils.updateSB(on2);
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "multiplier":
                    if (!p.hasPermission("usw.admin")){
                        p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                        return true;
                    }
                    if (args.length < 5){
                        sendHelp(sender);
                        return true;
                    }
                    switch(args[1].toLowerCase()){
                        case "coins":
                            Player on = Bukkit.getPlayer(args[2]);
                            if (on == null){
                                sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                                return true;
                            }
                            double amount;
                            try {
                                amount = Double.parseDouble(args[3]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                                return true;
                            }
                            int seconds;
                            try {
                                seconds = Integer.parseInt(args[4]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                                return true;
                            }
                            plugin.getDb().createMultiplier("COINS", on.getName(), amount, System.currentTimeMillis() + (seconds * 1000L), b -> {
                                if (b){
                                    plugin.getDb().loadMultipliers(b1 -> {
                                        if (b1){
                                            sender.sendMessage(plugin.getLang().get("messages.multiplier").replaceAll("<type>", "Coins").replace("<name>", on.getName()).replace("<amount>", String.valueOf(amount)).replace("<time>", Utils.convertTime(seconds)));
                                            on.sendMessage(plugin.getLang().get("messages.multiplierReceived").replaceAll("<type>", "Coins").replace("<amount>", String.valueOf(amount)).replace("<time>", Utils.convertTime(seconds)));
                                        }
                                    });
                                }
                            });
                            break;
                        case "souls":
                            Player on2 = Bukkit.getPlayer(args[2]);
                            if (on2 == null){
                                sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                                return true;
                            }
                            double amount2;
                            try {
                                amount2 = Double.parseDouble(args[3]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                                return true;
                            }
                            int seconds2;
                            try {
                                seconds2 = Integer.parseInt(args[4]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                                return true;
                            }
                            plugin.getDb().createMultiplier("SOULS", on2.getName(), amount2, System.currentTimeMillis() + (seconds2 * 1000L), b -> {
                                if (b){
                                    plugin.getDb().loadMultipliers(b1 -> {
                                        if (b1){
                                            sender.sendMessage(plugin.getLang().get("messages.multiplier").replaceAll("<type>", "Souls").replace("<name>", on2.getName()).replace("<amount>", String.valueOf(amount2)).replace("<time>", Utils.convertTime(seconds2)));
                                            on2.sendMessage(plugin.getLang().get("messages.multiplierReceived").replaceAll("<type>", "Souls").replace("<amount>", String.valueOf(amount2)).replace("<time>", Utils.convertTime(seconds2)));
                                        }
                                    });
                                }
                            });
                            break;
                        case "xp":
                            Player on3 = Bukkit.getPlayer(args[2]);
                            if (on3 == null){
                                sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                                return true;
                            }
                            double amount3;
                            try {
                                amount3 = Double.parseDouble(args[3]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                                return true;
                            }
                            int seconds3;
                            try {
                                seconds3 = Integer.parseInt(args[4]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                                return true;
                            }
                            plugin.getDb().createMultiplier("XP", on3.getName(), amount3, System.currentTimeMillis() + (seconds3 * 1000L), b -> {
                                if (b){
                                    plugin.getDb().loadMultipliers(b1 -> {
                                        if (b1){
                                            sender.sendMessage(plugin.getLang().get("messages.multiplier").replaceAll("<type>", "XP").replace("<name>", on3.getName()).replace("<amount>", String.valueOf(amount3)).replace("<time>", Utils.convertTime(seconds3)));
                                            on3.sendMessage(plugin.getLang().get("messages.multiplierReceived").replaceAll("<type>", "XP").replace("<amount>", String.valueOf(amount3)).replace("<time>", Utils.convertTime(seconds3)));
                                        }
                                    });
                                }
                            });
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                default:
                    sendHelp(sender);
                    break;
            }
        } else {
            if (args.length < 1){
                sendHelp(sender);
                return true;
            }
            switch(args[0].toLowerCase()){
                case "perks":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    Player cp = Bukkit.getPlayer(args[2]);
                    if (cp == null){
                        sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                        return true;
                    }
                    int idp;
                    try {
                        idp = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                        return true;
                    }
                    int perkLevel;
                    if (args.length > 4){
                        try {
                            perkLevel = Integer.parseInt(args[4]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                            return true;
                        }
                    } else {
                        perkLevel = 1;
                    }
                    if (args.length > 6){
                        try {
                            int rID = Integer.parseInt(args[5]);
                            Reward r = plugin.getLvl().getRewardByID(rID);
                            r.executeSecond(cp);
                            return true;
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    SWPlayer cswp = plugin.getDb().getSWPlayer(cp);
                    switch(args[1].toLowerCase()){
                        case "add":
                            if (cswp.getPerksData().containsKey(idp)){
                                sender.sendMessage(plugin.getLang().get("messages.cosmetics.alreadyHas"));
                                return true;
                            }
                            cswp.getPerksData().put(idp, perkLevel);
                            sender.sendMessage(plugin.getLang().get("messages.cosmetics.add").replaceAll("<type>", "Perk").replaceAll("<id>", String.valueOf(idp)).replaceAll("<player>", cp.getName()));
                            break;
                        case "remove":
                            if (!cswp.getPerksData().containsKey(idp)){
                                sender.sendMessage(plugin.getLang().get("messages.cosmetics.noHas"));
                                return true;
                            }
                            cswp.getPerksData().remove(idp);
                            sender.sendMessage(plugin.getLang().get("messages.cosmetics.remove").replaceAll("<type>", "Perk").replaceAll("<id>", String.valueOf(idp)).replaceAll("<player>", cp.getName()));
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "balloon":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    Player c1 = Bukkit.getPlayer(args[2]);
                    if (c1 == null){
                        sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                        return true;
                    }
                    int id1;
                    try {
                        id1 = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                        return true;
                    }
                    if (args.length > 4){
                        try {
                            int rID = Integer.parseInt(args[4]);
                            Reward r = plugin.getLvl().getRewardByID(rID);
                            r.executeSecond(c1);
                            return true;
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    SWPlayer csw1 = plugin.getDb().getSWPlayer(c1);
                    switch(args[1].toLowerCase()){
                        case "add":
                            if (csw1.getBalloons().contains(id1)){
                                sender.sendMessage(plugin.getLang().get("messages.cosmetics.alreadyHas"));
                                return true;
                            }
                            csw1.getBalloons().add(id1);
                            sender.sendMessage(plugin.getLang().get("messages.cosmetics.add").replaceAll("<type>", "Balloon").replaceAll("<id>", String.valueOf(id1)).replaceAll("<player>", c1.getName()));
                            break;
                        case "remove":
                            if (!csw1.getBalloons().contains(id1)){
                                sender.sendMessage(plugin.getLang().get("messages.cosmetics.noHas"));
                                return true;
                            }
                            csw1.getBalloons().removeAll(Collections.singletonList(id1));
                            sender.sendMessage(plugin.getLang().get("messages.cosmetics.remove").replaceAll("<type>", "Balloon").replaceAll("<id>", String.valueOf(id1)).replaceAll("<player>", c1.getName()));
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "glass":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    Player c2 = Bukkit.getPlayer(args[2]);
                    if (c2 == null){
                        sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                        return true;
                    }
                    int id2;
                    try {
                        id2 = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                        return true;
                    }
                    if (args.length > 4){
                        try {
                            int rID = Integer.parseInt(args[4]);
                            Reward r = plugin.getLvl().getRewardByID(rID);
                            r.executeSecond(c2);
                            return true;
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    SWPlayer csw2 = plugin.getDb().getSWPlayer(c2);
                    switch(args[1].toLowerCase()){
                        case "add":
                            if (csw2.getGlasses().contains(id2)){
                                sender.sendMessage(plugin.getLang().get("messages.cosmetics.alreadyHas"));
                                return true;
                            }
                            csw2.getGlasses().add(id2);
                            sender.sendMessage(plugin.getLang().get("messages.cosmetics.add").replaceAll("<type>", "Glass").replaceAll("<id>", String.valueOf(id2)).replaceAll("<player>", c2.getName()));
                            break;
                        case "remove":
                            if (!csw2.getGlasses().contains(id2)){
                                sender.sendMessage(plugin.getLang().get("messages.cosmetics.noHas"));
                                return true;
                            }
                            csw2.getGlasses().removeAll(Collections.singletonList(id2));
                            sender.sendMessage(plugin.getLang().get("messages.cosmetics.remove").replaceAll("<type>", "Glass").replaceAll("<id>", String.valueOf(id2)).replaceAll("<player>", c2.getName()));
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "killeffect":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    Player c3 = Bukkit.getPlayer(args[2]);
                    if (c3 == null){
                        sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                        return true;
                    }
                    int id3;
                    try {
                        id3 = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                        return true;
                    }
                    if (args.length > 4){
                        try {
                            int rID = Integer.parseInt(args[4]);
                            Reward r = plugin.getLvl().getRewardByID(rID);
                            r.executeSecond(c3);
                            return true;
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    SWPlayer csw3 = plugin.getDb().getSWPlayer(c3);
                    switch(args[1].toLowerCase()){
                        case "add":
                            if (csw3.getKilleffects().contains(id3)){
                                sender.sendMessage(plugin.getLang().get("messages.cosmetics.alreadyHas"));
                                return true;
                            }
                            csw3.getKilleffects().add(id3);
                            sender.sendMessage(plugin.getLang().get("messages.cosmetics.add").replaceAll("<type>", "KillEffect").replaceAll("<id>", String.valueOf(id3)).replaceAll("<player>", c3.getName()));
                            break;
                        case "remove":
                            if (!csw3.getKilleffects().contains(id3)){
                                sender.sendMessage(plugin.getLang().get("messages.cosmetics.noHas"));
                                return true;
                            }
                            csw3.getKilleffects().removeAll(Collections.singletonList(id3));
                            sender.sendMessage(plugin.getLang().get("messages.cosmetics.remove").replaceAll("<type>", "KillEffect").replaceAll("<id>", String.valueOf(id3)).replaceAll("<player>", c3.getName()));
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "killsound":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    Player c4 = Bukkit.getPlayer(args[2]);
                    if (c4 == null){
                        sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                        return true;
                    }
                    int id4;
                    try {
                        id4 = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                        return true;
                    }
                    if (args.length > 4){
                        try {
                            int rID = Integer.parseInt(args[4]);
                            Reward r = plugin.getLvl().getRewardByID(rID);
                            r.executeSecond(c4);
                            return true;
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    SWPlayer csw4 = plugin.getDb().getSWPlayer(c4);
                    switch(args[1].toLowerCase()){
                        case "add":
                            if (csw4.getKillsounds().contains(id4)){
                                sender.sendMessage(plugin.getLang().get("messages.cosmetics.alreadyHas"));
                                return true;
                            }
                            csw4.getKillsounds().add(id4);
                            sender.sendMessage(plugin.getLang().get("messages.cosmetics.add").replaceAll("<type>", "KillSound").replaceAll("<id>", String.valueOf(id4)).replaceAll("<player>", c4.getName()));
                            break;
                        case "remove":
                            if (!csw4.getKillsounds().contains(id4)){
                                sender.sendMessage(plugin.getLang().get("messages.cosmetics.noHas"));
                                return true;
                            }
                            csw4.getKillsounds().removeAll(Collections.singletonList(id4));
                            sender.sendMessage(plugin.getLang().get("messages.cosmetics.remove").replaceAll("<type>", "KillSound").replaceAll("<id>", String.valueOf(id4)).replaceAll("<player>", c4.getName()));
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "kit":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    Player c5 = Bukkit.getPlayer(args[2]);
                    if (c5 == null){
                        sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                        return true;
                    }
                    int id5;
                    try {
                        id5 = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                        return true;
                    }
                    if (args.length > 4){
                        try {
                            int rID = Integer.parseInt(args[4]);
                            Reward r = plugin.getLvl().getRewardByID(rID);
                            r.executeSecond(c5);
                            return true;
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    SWPlayer csw5 = plugin.getDb().getSWPlayer(c5);
                    switch(args[1].toLowerCase()){
                        case "add":
                            if (csw5.getKits().containsKey(id5)){
                                sender.sendMessage(plugin.getLang().get("messages.cosmetics.alreadyHas"));
                                return true;
                            }
                            csw5.addKitLevel(id5, 1);
                            sender.sendMessage(plugin.getLang().get("messages.cosmetics.add").replaceAll("<type>", "Kit").replaceAll("<id>", String.valueOf(id5)).replaceAll("<player>", c5.getName()));
                            break;
                        case "remove":
                            if (!csw5.getKits().containsKey(id5)){
                                sender.sendMessage(plugin.getLang().get("messages.cosmetics.noHas"));
                                return true;
                            }
                            csw5.removeKitLevel(id5, 1);
                            sender.sendMessage(plugin.getLang().get("messages.cosmetics.remove").replaceAll("<type>", "Kit").replaceAll("<id>", String.valueOf(id5)).replaceAll("<player>", c5.getName()));
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "parting":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    Player c6 = Bukkit.getPlayer(args[2]);
                    if (c6 == null){
                        sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                        return true;
                    }
                    int id6;
                    try {
                        id6 = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                        return true;
                    }
                    if (args.length > 4){
                        try {
                            int rID = Integer.parseInt(args[4]);
                            Reward r = plugin.getLvl().getRewardByID(rID);
                            r.executeSecond(c6);
                            return true;
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    SWPlayer csw6 = plugin.getDb().getSWPlayer(c6);
                    switch(args[1].toLowerCase()){
                        case "add":
                            if (csw6.getPartings().contains(id6)){
                                sender.sendMessage(plugin.getLang().get("messages.cosmetics.alreadyHas"));
                                return true;
                            }
                            csw6.getPartings().add(id6);
                            sender.sendMessage(plugin.getLang().get("messages.cosmetics.add").replaceAll("<type>", "Parting").replaceAll("<id>", String.valueOf(id6)).replaceAll("<player>", c6.getName()));
                            break;
                        case "remove":
                            if (!csw6.getPartings().contains(id6)){
                                sender.sendMessage(plugin.getLang().get("messages.cosmetics.noHas"));
                                return true;
                            }
                            csw6.getPartings().removeAll(Collections.singletonList(id6));
                            sender.sendMessage(plugin.getLang().get("messages.cosmetics.remove").replaceAll("<type>", "Parting").replaceAll("<id>", String.valueOf(id6)).replaceAll("<player>", c6.getName()));
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "taunt":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    Player c7 = Bukkit.getPlayer(args[2]);
                    if (c7 == null){
                        sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                        return true;
                    }
                    int id7;
                    try {
                        id7 = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                        return true;
                    }
                    if (args.length > 4){
                        try {
                            int rID = Integer.parseInt(args[4]);
                            Reward r = plugin.getLvl().getRewardByID(rID);
                            r.executeSecond(c7);
                            return true;
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    SWPlayer csw7 = plugin.getDb().getSWPlayer(c7);
                    switch(args[1].toLowerCase()){
                        case "add":
                            if (csw7.getTaunts().contains(id7)){
                                sender.sendMessage(plugin.getLang().get("messages.cosmetics.alreadyHas"));
                                return true;
                            }
                            csw7.getTaunts().add(id7);
                            sender.sendMessage(plugin.getLang().get("messages.cosmetics.add").replaceAll("<type>", "Taunt").replaceAll("<id>", String.valueOf(id7)).replaceAll("<player>", c7.getName()));
                            break;
                        case "remove":
                            if (!csw7.getTaunts().contains(id7)){
                                sender.sendMessage(plugin.getLang().get("messages.cosmetics.noHas"));
                                return true;
                            }
                            csw7.getTaunts().removeAll(Collections.singletonList(id7));
                            sender.sendMessage(plugin.getLang().get("messages.cosmetics.remove").replaceAll("<type>", "Taunt").replaceAll("<id>", String.valueOf(id7)).replaceAll("<player>", c7.getName()));
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "trail":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    Player c8 = Bukkit.getPlayer(args[2]);
                    if (c8 == null){
                        sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                        return true;
                    }
                    int id8;
                    try {
                        id8 = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                        return true;
                    }
                    if (args.length > 4){
                        try {
                            int rID = Integer.parseInt(args[4]);
                            Reward r = plugin.getLvl().getRewardByID(rID);
                            r.executeSecond(c8);
                            return true;
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    SWPlayer csw8 = plugin.getDb().getSWPlayer(c8);
                    switch(args[1].toLowerCase()){
                        case "add":
                            if (csw8.getTrails().contains(id8)){
                                sender.sendMessage(plugin.getLang().get("messages.cosmetics.alreadyHas"));
                                return true;
                            }
                            csw8.getTrails().add(id8);
                            sender.sendMessage(plugin.getLang().get("messages.cosmetics.add").replaceAll("<type>", "Trail").replaceAll("<id>", String.valueOf(id8)).replaceAll("<player>", c8.getName()));
                            break;
                        case "remove":
                            if (!csw8.getTrails().contains(id8)){
                                sender.sendMessage(plugin.getLang().get("messages.cosmetics.noHas"));
                                return true;
                            }
                            csw8.getTrails().removeAll(Collections.singletonList(id8));
                            sender.sendMessage(plugin.getLang().get("messages.cosmetics.remove").replaceAll("<type>", "Trail").replaceAll("<id>", String.valueOf(id8)).replaceAll("<player>", c8.getName()));
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "windance":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    Player c9 = Bukkit.getPlayer(args[2]);
                    if (c9 == null){
                        sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                        return true;
                    }
                    int id9;
                    try {
                        id9 = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                        return true;
                    }
                    if (args.length > 4){
                        try {
                            int rID = Integer.parseInt(args[4]);
                            Reward r = plugin.getLvl().getRewardByID(rID);
                            r.executeSecond(c9);
                            return true;
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    SWPlayer csw9 = plugin.getDb().getSWPlayer(c9);
                    switch(args[1].toLowerCase()){
                        case "add":
                            if (csw9.getWindances().contains(id9)){
                                sender.sendMessage(plugin.getLang().get("messages.cosmetics.alreadyHas"));
                                return true;
                            }
                            csw9.getWindances().add(id9);
                            sender.sendMessage(plugin.getLang().get("messages.cosmetics.add").replaceAll("<type>", "WinDance").replaceAll("<id>", String.valueOf(id9)).replaceAll("<player>", c9.getName()));
                            break;
                        case "remove":
                            if (!csw9.getWindances().contains(id9)){
                                sender.sendMessage(plugin.getLang().get("messages.cosmetics.noHas"));
                                return true;
                            }
                            csw9.getWindances().removeAll(Collections.singletonList(id9));
                            sender.sendMessage(plugin.getLang().get("messages.cosmetics.remove").replaceAll("<type>", "WinDance").replaceAll("<id>", String.valueOf(id9)).replaceAll("<player>", c9.getName()));
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "wineffect":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    Player c10 = Bukkit.getPlayer(args[2]);
                    if (c10 == null){
                        sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                        return true;
                    }
                    int id10;
                    try {
                        id10 = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                        return true;
                    }
                    if (args.length > 4){
                        try {
                            int rID = Integer.parseInt(args[4]);
                            Reward r = plugin.getLvl().getRewardByID(rID);
                            r.executeSecond(c10);
                            return true;
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    SWPlayer csw10 = plugin.getDb().getSWPlayer(c10);
                    switch(args[1].toLowerCase()){
                        case "add":
                            if (csw10.getWineffects().contains(id10)){
                                sender.sendMessage(plugin.getLang().get("messages.cosmetics.alreadyHas"));
                                return true;
                            }
                            csw10.getWineffects().add(id10);
                            sender.sendMessage(plugin.getLang().get("messages.cosmetics.add").replaceAll("<type>", "WinEffect").replaceAll("<id>", String.valueOf(id10)).replaceAll("<player>", c10.getName()));
                            break;
                        case "remove":
                            if (!csw10.getWineffects().contains(id10)){
                                sender.sendMessage(plugin.getLang().get("messages.cosmetics.noHas"));
                                return true;
                            }
                            csw10.getWineffects().removeAll(Collections.singletonList(id10));
                            sender.sendMessage(plugin.getLang().get("messages.cosmetics.remove").replaceAll("<type>", "WinEffect").replaceAll("<id>", String.valueOf(id10)).replaceAll("<player>", c10.getName()));
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "forcejoin":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    String type2 = args[1];
                    String name2 = args[2];
                    Player on67 = Bukkit.getPlayer(args[3]);
                    if (on67 == null){
                        return true;
                    }
                    if (plugin.getGm().isPlayerInGame(on67)){
                        sender.sendMessage(plugin.getLang().get("messages.alreadyGame"));
                        return true;
                    }
                    Game game2 = plugin.getGm().getGameByName(name2);
                    if (game2 == null){
                        sender.sendMessage(plugin.getLang().get("messages.noGameExists"));
                        return true;
                    }
                    switch(type2.toLowerCase()){
                        case "solo":
                            if (game2 instanceof UltraGame){
                                plugin.getGm().addPlayerGame(on67, game2.getId());
                            } else {
                                sender.sendMessage(plugin.getLang().get("messages.noGameType"));
                            }
                            break;
                        case "team":
                            if (game2 instanceof UltraTeamGame){
                                plugin.getGm().addPlayerGame(on67, game2.getId());
                            } else {
                                sender.sendMessage(plugin.getLang().get("messages.noGameType"));
                            }
                            break;
                        case "ranked":
                            if (game2 instanceof UltraRankedGame){
                                plugin.getGm().addPlayerGame(on67, game2.getId());
                            } else {
                                sender.sendMessage(plugin.getLang().get("messages.noGameType"));
                            }
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "coins":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    switch(args[1].toLowerCase()){
                        case "add":
                            Player on = Bukkit.getPlayer(args[2]);
                            if (on == null){
                                sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                                return true;
                            }
                            int amount;
                            try {
                                amount = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                                return true;
                            }
                            plugin.getAdm().addCoins(on, amount);
                            sender.sendMessage(plugin.getLang().get("coins.add.you").replaceAll("<coins>", String.valueOf(amount)).replaceAll("<player>", on.getName()));
                            on.sendMessage(plugin.getLang().get("coins.add.receiver").replaceAll("<coins>", String.valueOf(amount)).replaceAll("<sender>", sender.getName()));
                            Utils.updateSB(on);
                            break;
                        case "remove":
                            Player on1 = Bukkit.getPlayer(args[2]);
                            if (on1 == null){
                                sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                                return true;
                            }
                            int amount1;
                            try {
                                amount1 = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                                return true;
                            }
                            SWPlayer sw1 = plugin.getDb().getSWPlayer(on1);
                            plugin.getAdm().removeCoins(on1, amount1);
                            sender.sendMessage(plugin.getLang().get("coins.remove.you").replaceAll("<coins>", String.valueOf(amount1)).replaceAll("<player>", on1.getName()));
                            on1.sendMessage(plugin.getLang().get("coins.remove.receiver").replaceAll("<coins>", String.valueOf(amount1)).replaceAll("<sender>", sender.getName()));
                            Utils.updateSB(on1);
                            break;
                        case "set":
                            Player on2 = Bukkit.getPlayer(args[2]);
                            if (on2 == null){
                                sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                                return true;
                            }
                            int amount2;
                            try {
                                amount2 = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                                return true;
                            }
                            SWPlayer sw2 = plugin.getDb().getSWPlayer(on2);
                            plugin.getAdm().setCoins(on2, amount2);
                            sender.sendMessage(plugin.getLang().get("coins.set.you").replaceAll("<coins>", String.valueOf(amount2)).replaceAll("<player>", on2.getName()));
                            on2.sendMessage(plugin.getLang().get("coins.set.receiver").replaceAll("<coins>", String.valueOf(amount2)).replaceAll("<sender>", sender.getName()));
                            Utils.updateSB(on2);
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "souls":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    switch(args[1].toLowerCase()){
                        case "add":
                            Player on = Bukkit.getPlayer(args[2]);
                            if (on == null){
                                sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                                return true;
                            }
                            int amount;
                            try {
                                amount = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                                return true;
                            }
                            SWPlayer sw = plugin.getDb().getSWPlayer(on);
                            sw.addSouls(amount);
                            sender.sendMessage(plugin.getLang().get("souls.add.you").replaceAll("<souls>", String.valueOf(amount)).replaceAll("<player>", on.getName()));
                            on.sendMessage(plugin.getLang().get("souls.add.receiver").replaceAll("<souls>", String.valueOf(amount)).replaceAll("<sender>", sender.getName()));
                            Utils.updateSB(on);
                            break;
                        case "remove":
                            Player on1 = Bukkit.getPlayer(args[2]);
                            if (on1 == null){
                                sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                                return true;
                            }
                            int amount1;
                            try {
                                amount1 = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                                return true;
                            }
                            SWPlayer sw1 = plugin.getDb().getSWPlayer(on1);
                            sw1.removeSouls(amount1);
                            sender.sendMessage(plugin.getLang().get("souls.remove.you").replaceAll("<souls>", String.valueOf(amount1)).replaceAll("<player>", on1.getName()));
                            on1.sendMessage(plugin.getLang().get("souls.remove.receiver").replaceAll("<souls>", String.valueOf(amount1)).replaceAll("<sender>", sender.getName()));
                            Utils.updateSB(on1);
                            break;
                        case "set":
                            Player on2 = Bukkit.getPlayer(args[2]);
                            if (on2 == null){
                                sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                                return true;
                            }
                            int amount2;
                            try {
                                amount2 = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                                return true;
                            }
                            SWPlayer sw2 = plugin.getDb().getSWPlayer(on2);
                            sw2.setSouls(amount2);
                            sender.sendMessage(plugin.getLang().get("souls.set.you").replaceAll("<souls>", String.valueOf(amount2)).replaceAll("<player>", on2.getName()));
                            on2.sendMessage(plugin.getLang().get("souls.set.receiver").replaceAll("<souls>", String.valueOf(amount2)).replaceAll("<sender>", sender.getName()));
                            Utils.updateSB(on2);
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "cubelets":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    if (!plugin.getIjm().isCubeletsInjection()){
                        sender.sendMessage(plugin.getLang().get("injections.cubelets"));
                        return true;
                    }
                    switch(args[1].toLowerCase()){
                        case "add":
                            Player on = Bukkit.getPlayer(args[2]);
                            if (on == null){
                                sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                                return true;
                            }
                            int amount;
                            try {
                                amount = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                                return true;
                            }
                            SWPlayer sw = plugin.getDb().getSWPlayer(on);
                            sw.addCubelets(amount);
                            sender.sendMessage(plugin.getLang().get("cubelets.add.you").replaceAll("<cubelets>", String.valueOf(amount)).replaceAll("<player>", on.getName()));
                            on.sendMessage(plugin.getLang().get("cubelets.add.receiver").replaceAll("<cubelets>", String.valueOf(amount)).replaceAll("<sender>", sender.getName()));
                            Utils.updateSB(on);
                            break;
                        case "remove":
                            Player on1 = Bukkit.getPlayer(args[2]);
                            if (on1 == null){
                                sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                                return true;
                            }
                            int amount1;
                            try {
                                amount1 = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                                return true;
                            }
                            SWPlayer sw1 = plugin.getDb().getSWPlayer(on1);
                            sw1.removeCubelets(amount1);
                            sender.sendMessage(plugin.getLang().get("cubelets.remove.you").replaceAll("<cubelets>", String.valueOf(amount1)).replaceAll("<player>", on1.getName()));
                            on1.sendMessage(plugin.getLang().get("cubelets.remove.receiver").replaceAll("<cubelets>", String.valueOf(amount1)).replaceAll("<sender>", sender.getName()));
                            Utils.updateSB(on1);
                            break;
                        case "set":
                            Player on2 = Bukkit.getPlayer(args[2]);
                            if (on2 == null){
                                sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                                return true;
                            }
                            int amount2;
                            try {
                                amount2 = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                                return true;
                            }
                            SWPlayer sw2 = plugin.getDb().getSWPlayer(on2);
                            sw2.setCubelets(amount2);
                            sender.sendMessage(plugin.getLang().get("cubelets.set.you").replaceAll("<cubelets>", String.valueOf(amount2)).replaceAll("<player>", on2.getName()));
                            on2.sendMessage(plugin.getLang().get("cubelets.set.receiver").replaceAll("<cubelets>", String.valueOf(amount2)).replaceAll("<sender>", sender.getName()));
                            Utils.updateSB(on2);
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "xp":
                    if (args.length < 4){
                        sendHelp(sender);
                        return true;
                    }
                    switch(args[1].toLowerCase()){
                        case "add":
                            Player on = Bukkit.getPlayer(args[2]);
                            if (on == null){
                                sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                                return true;
                            }
                            int amount;
                            try {
                                amount = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                                return true;
                            }
                            SWPlayer sw = plugin.getDb().getSWPlayer(on);
                            sw.addXp(amount);
                            sender.sendMessage(plugin.getLang().get("xp.add.you").replaceAll("<xp>", String.valueOf(amount)).replaceAll("<player>", on.getName()));
                            on.sendMessage(plugin.getLang().get("xp.add.receiver").replaceAll("<xp>", String.valueOf(amount)).replaceAll("<sender>", sender.getName()));
                            plugin.getLvl().checkUpgrade(on);
                            Utils.updateSB(on);
                            break;
                        case "remove":
                            Player on1 = Bukkit.getPlayer(args[2]);
                            if (on1 == null){
                                sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                                return true;
                            }
                            int amount1;
                            try {
                                amount1 = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                                return true;
                            }
                            SWPlayer sw1 = plugin.getDb().getSWPlayer(on1);
                            sw1.removeXp(amount1);
                            sender.sendMessage(plugin.getLang().get("xp.remove.you").replaceAll("<xp>", String.valueOf(amount1)).replaceAll("<player>", on1.getName()));
                            on1.sendMessage(plugin.getLang().get("xp.remove.receiver").replaceAll("<xp>", String.valueOf(amount1)).replaceAll("<sender>", sender.getName()));
                            plugin.getLvl().checkUpgrade(on1);
                            Utils.updateSB(on1);
                            break;
                        case "set":
                            Player on2 = Bukkit.getPlayer(args[2]);
                            if (on2 == null){
                                sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                                return true;
                            }
                            int amount2;
                            try {
                                amount2 = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                                return true;
                            }
                            SWPlayer sw2 = plugin.getDb().getSWPlayer(on2);
                            sw2.setXp(amount2);
                            sender.sendMessage(plugin.getLang().get("xp.set.you").replaceAll("<xp>", String.valueOf(amount2)).replaceAll("<player>", on2.getName()));
                            on2.sendMessage(plugin.getLang().get("xp.set.receiver").replaceAll("<xp>", String.valueOf(amount2)).replaceAll("<sender>", sender.getName()));
                            plugin.getLvl().checkUpgrade(on2);
                            Utils.updateSB(on2);
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "multiplier":
                    if (args.length < 5){
                        sendHelp(sender);
                        return true;
                    }
                    switch(args[1].toLowerCase()){
                        case "coins":
                            Player on = Bukkit.getPlayer(args[2]);
                            if (on == null){
                                sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                                return true;
                            }
                            double amount;
                            try {
                                amount = Double.parseDouble(args[3]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                                return true;
                            }
                            int seconds;
                            try {
                                seconds = Integer.parseInt(args[4]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                                return true;
                            }
                            plugin.getDb().createMultiplier("COINS", on.getName(), amount, System.currentTimeMillis() + (seconds * 1000L), b -> {
                                if (b){
                                    plugin.getDb().loadMultipliers(b1 -> {
                                        if (b1){
                                            sender.sendMessage(plugin.getLang().get("messages.multiplier").replaceAll("<type>", "Coins").replace("<name>", on.getName()).replace("<amount>", String.valueOf(amount)).replace("<time>", Utils.convertTime(seconds)));
                                            on.sendMessage(plugin.getLang().get("messages.multiplierReceived").replaceAll("<type>", "Coins").replace("<amount>", String.valueOf(amount)).replace("<time>", Utils.convertTime(seconds)));
                                        }
                                    });
                                }
                            });
                            break;
                        case "souls":
                            Player on2 = Bukkit.getPlayer(args[2]);
                            if (on2 == null){
                                sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                                return true;
                            }
                            double amount2;
                            try {
                                amount2 = Double.parseDouble(args[3]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                                return true;
                            }
                            int seconds2;
                            try {
                                seconds2 = Integer.parseInt(args[4]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                                return true;
                            }
                            plugin.getDb().createMultiplier("SOULS", on2.getName(), amount2, System.currentTimeMillis() + (seconds2 * 1000L), b -> {
                                if (b){
                                    plugin.getDb().loadMultipliers(b1 -> {
                                        if (b1){
                                            sender.sendMessage(plugin.getLang().get("messages.multiplier").replaceAll("<type>", "Souls").replace("<name>", on2.getName()).replace("<amount>", String.valueOf(amount2)).replace("<time>", Utils.convertTime(seconds2)));
                                            on2.sendMessage(plugin.getLang().get("messages.multiplierReceived").replaceAll("<type>", "Souls").replace("<amount>", String.valueOf(amount2)).replace("<time>", Utils.convertTime(seconds2)));
                                        }
                                    });
                                }
                            });
                            break;
                        case "xp":
                            Player on3 = Bukkit.getPlayer(args[2]);
                            if (on3 == null){
                                sender.sendMessage(plugin.getLang().get("setup.noOnline"));
                                return true;
                            }
                            double amount3;
                            try {
                                amount3 = Double.parseDouble(args[3]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                                return true;
                            }
                            int seconds3;
                            try {
                                seconds3 = Integer.parseInt(args[4]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(plugin.getLang().get("setup.noNumber"));
                                return true;
                            }
                            plugin.getDb().createMultiplier("XP", on3.getName(), amount3, System.currentTimeMillis() + (seconds3 * 1000L), b -> {
                                if (b){
                                    plugin.getDb().loadMultipliers(b1 -> {
                                        if (b1){
                                            sender.sendMessage(plugin.getLang().get("messages.multiplier").replaceAll("<type>", "XP").replace("<name>", on3.getName()).replace("<amount>", String.valueOf(amount3)).replace("<time>", Utils.convertTime(seconds3)));
                                            on3.sendMessage(plugin.getLang().get("messages.multiplierReceived").replaceAll("<type>", "XP").replace("<amount>", String.valueOf(amount3)).replace("<time>", Utils.convertTime(seconds3)));
                                        }
                                    });
                                }
                            });
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                default:
                    sendHelp(sender);
                    break;
            }
        }
        return false;
    }
    
    private void sendHelp(CommandSender s){
        s.sendMessage(plugin.getLang().get("setup.help.separator"));
        plugin.getLang().getList("setup.help.player").forEach(m -> s.sendMessage(m.replaceAll("&", "§")));
        if (s.hasPermission("usw.admin")){
            plugin.getLang().getList("setup.help.admin").forEach(m -> s.sendMessage(m.replaceAll("&", "§")));
        }
        s.sendMessage(plugin.getLang().get("setup.help.separator"));
    }
    
}