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

package io.github.Leonardo0013YT.UltraSkyWarsSetup.cmds;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.fanciful.FancyMessage;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.UltraInventory;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import io.github.Leonardo0013YT.UltraSkyWarsSetup.UltraSkyWarsSetup;
import io.github.Leonardo0013YT.UltraSkyWarsSetup.setup.*;
import io.github.Leonardo0013YT.UltraSkyWarsSetup.setup.cosmetics.*;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetupCMD implements CommandExecutor {
    
    private final UltraSkyWarsApi plugin;
    private final UltraSkyWarsSetup setup;
    
    public SetupCMD(UltraSkyWarsApi plugin, UltraSkyWarsSetup setup){
        this.plugin = plugin;
        this.setup = setup;
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
                sendHelp(p);
                return true;
            }
            switch(args[0].toLowerCase()){
                case "setpreview":
                    if (args.length < 2){
                        sendHelp(p);
                        return true;
                    }
                    String type = args[1].toUpperCase();
                    if (!type.equals("BALLOON") && !type.equals("GLASS")){
                        p.sendMessage(plugin.getLang().get("setup.noTypePreview"));
                        return true;
                    }
                    if (!setup.getSm().isSetupPreview(p)){
                        setup.getSm().setSetupPreview(p, new PreviewSetup(type));
                    }
                    PreviewSetup sp = setup.getSm().getSetupPreview(p);
                    setup.getSem().createPreviewMenu(p, sp);
                    break;
                case "createchest":
                    if (args.length < 2){
                        sendHelp(p);
                        return true;
                    }
                    String cName = args[1];
                    if (!setup.getSm().isChestType(p)){
                        setup.getSm().setChestType(p, new ChestTypeSetup(cName));
                    }
                    setup.getSem().createChestSetupMenu(p, setup.getSm().getChestType(p));
                    break;
                case "reload":
                    if (args.length == 1){
                        plugin.reload();
                        p.sendMessage(plugin.getLang().get(p, "setup.reload"));
                        return true;
                    }
                    switch(args[1].toLowerCase()){
                        case "lang":
                            plugin.reloadLang();
                            p.sendMessage(plugin.getLang().get(p, "setup.reloadLang"));
                            break;
                        case "config":
                            plugin.reload();
                            p.sendMessage(plugin.getLang().get(p, "setup.reload"));
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "setupblock":
                    if (args.length < 2){
                        sendHelp(p);
                        return true;
                    }
                    switch(args[1].toLowerCase()){
                        case "cubelets":
                            if (!plugin.getIjm().isCubeletsInjection()){
                                p.sendMessage(plugin.getLang().get(p, "injections.cubelets"));
                                return true;
                            }
                            if (!setup.getSm().isSetupCubeBlock(p)){
                                setup.getSm().setSetupCubeBlock(p);
                            }
                            p.sendMessage(plugin.getLang().get(p, "messages.addRemoveBlock"));
                            break;
                        case "soulwell":
                            if (!setup.getSm().isSetupSoulBlock(p)){
                                setup.getSm().setSetupSoulBlock(p);
                            }
                            p.sendMessage(plugin.getLang().get(p, "messages.addRemoveBlock"));
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                case "rewards":
                    if (!setup.getSm().isSetupSoulWell(p)){
                        setup.getSm().setSetupSoulWell(p, new SoulWellSetup(plugin));
                    }
                    setup.getSem().createSoulWellMenu(p);
                    break;
                case "create":
                    if (args.length < 2){
                        sendHelp(p);
                        return true;
                    }
                    if (setup.getSm().isSetup(p)){
                        p.sendMessage(plugin.getLang().get(p, "setup.alreadyCreating"));
                        return true;
                    }
                    String name = args[1];
                    if (plugin.getGm().getGameByName(name) != null){
                        p.sendMessage(plugin.getLang().get("setup.arena.alreadyMapEdit"));
                        return true;
                    }
                    setup.getSm().setSetup(p, new ArenaSetup(plugin, name));
                    /*if (plugin.getCm().isSlimeworldmanager()) {
                        plugin.getAdm().getSlime().createWorld(name);
                    }*/
                    World w = plugin.getWc().createEmptyWorld(name);
                    w.getBlockAt(0, 75, 0).setType(Material.STONE);
                    p.getInventory().clear();
                    p.teleport(w.getSpawnLocation());
                    p.getInventory().remove(plugin.getIm().getSetup());
                    p.getInventory().remove(plugin.getIm().getCenter());
                    p.getInventory().addItem(plugin.getIm().getSetup());
                    p.sendMessage(plugin.getLang().get("setup.arena.createMap").replace("<name>", name));
                    break;
                case "edit":
                    if (args.length < 2){
                        sendHelp(p);
                        return true;
                    }
                    if (setup.getSm().isSetup(p)){
                        p.sendMessage(plugin.getLang().get(p, "setup.alreadyCreating"));
                        return true;
                    }
                    String name2 = args[1];
                    if (plugin.getGm().getGameByName(name2) == null){
                        return true;
                    }
                    setup.getSm().setSetup(p, new ArenaSetup(plugin, p, name2, name2));
                    World w2 = Bukkit.getWorld(name2);
                    p.getInventory().clear();
                    p.teleport(w2.getSpawnLocation());
                    p.getInventory().remove(plugin.getIm().getSetup());
                    p.getInventory().remove(plugin.getIm().getCenter());
                    p.getInventory().addItem(plugin.getIm().getSetup());
                    p.sendMessage("§aNow you're editing the map §e" + name2 + "§a.");
                    break;
                case "delete":{
                    if (args.length < 2){
                        sendHelp(p);
                        return true;
                    }
                    String delete = args[1];
                    if (!plugin.getGm().getWorlds().containsKey(delete)){
                        p.sendMessage("§cThis game not exists.");
                        return true;
                    }
                    if (setup.getSm().isDelete(p)){
                        plugin.getArenas().set("arenas." + delete, null);
                        plugin.getArenas().save();
                        setup.getSm().removeDelete(p);
                        p.sendMessage("§aYou've removed the map §e" + delete + "§a.");
                        return true;
                    }
                    setup.getSm().setDelete(p, delete);
                    p.sendMessage("§cPlease config your delete executing §e/sws delete " + delete + "§c.");
                    break;
                }
                case "clone":{
                    if (args.length < 3){
                        sendHelp(p);
                        return true;
                    }
                    String arena1 = args[1];
                    if (!plugin.getGm().getWorlds().containsKey(arena1)){
                        p.sendMessage("§cThis game not exists.");
                        return true;
                    }
                    String arena2 = args[2];
                    if (plugin.getGm().getWorlds().containsKey(arena2)){
                        p.sendMessage(plugin.getLang().get("setup.arena.alreadyMapEdit"));
                        return true;
                    }
                    plugin.getWc().createEmptyWorld(arena2);
                    ArenaSetup as = new ArenaSetup(plugin, p, arena1, arena2);
                    as.save(p, arena2);
                    p.sendMessage(plugin.getLang().get("setup.arena.cloneMap").replace("<map1>", arena1).replace("<map2>", arena2));
                    break;
                }
                case "killsounds":
                    if (setup.getSm().isSetupKillSound(p)){
                        KillSoundSetup kss = setup.getSm().getSetupKillSound(p);
                        setup.getSem().createKillSoundMenu(p, kss);
                        return true;
                    }
                    if (args.length < 2){
                        sendHelp(p);
                        return true;
                    }
                    String nameks = args[1];
                    KillSoundSetup kss = new KillSoundSetup(p, nameks);
                    setup.getSm().setSetupKillSound(p, kss);
                    setup.getSem().createKillSoundMenu(p, kss);
                    p.sendMessage(plugin.getLang().get(p, "setup.killsounds.created").replaceAll("<name>", kss.getName()));
                    break;
                case "parting":
                    if (setup.getSm().isSetupParting(p)){
                        PartingSetup ps = setup.getSm().getSetupParting(p);
                        setup.getSem().createPartingMenu(p, ps);
                        return true;
                    }
                    if (args.length < 2){
                        sendHelp(p);
                        return true;
                    }
                    String namebp = args[1];
                    PartingSetup ps = new PartingSetup(p, namebp);
                    setup.getSm().setSetupParting(p, ps);
                    setup.getSem().createPartingMenu(p, ps);
                    p.sendMessage(plugin.getLang().get(p, "setup.parting.created").replaceAll("<name>", ps.getName()));
                    break;
                case "balloons":
                    if (setup.getSm().isSetupBalloon(p)){
                        BalloonSetup bs = setup.getSm().getSetupBalloon(p);
                        setup.getSem().createSetupBalloonsMenu(p, bs);
                        return true;
                    }
                    if (args.length < 2){
                        sendHelp(p);
                        return true;
                    }
                    String nameb = args[1];
                    BalloonSetup bs = new BalloonSetup(p, nameb);
                    setup.getSm().setSetupBalloon(p, bs);
                    setup.getSem().createSetupBalloonsMenu(p, bs);
                    p.sendMessage(plugin.getLang().get(p, "setup.balloons.created").replaceAll("<name>", bs.getName()));
                    break;
                case "trails":
                    if (setup.getSm().isSetupTrail(p)){
                        TrailSetup ts = setup.getSm().getSetupTrail(p);
                        setup.getSem().createTrailMenu(p, ts);
                        return true;
                    }
                    if (args.length < 2){
                        sendHelp(p);
                        return true;
                    }
                    String namett = args[1];
                    TrailSetup tts = new TrailSetup(p, namett);
                    setup.getSm().setSetupTrail(p, tts);
                    setup.getSem().createTrailMenu(p, tts);
                    p.sendMessage(plugin.getLang().get(p, "setup.trails.created").replaceAll("<name>", tts.getName()));
                    break;
                case "taunts":
                    if (setup.getSm().isSetupTaunt(p)){
                        TauntSetup ts = setup.getSm().getSetupTaunt(p);
                        setup.getSem().createTauntMenu(p, ts);
                        return true;
                    }
                    if (args.length < 2){
                        sendHelp(p);
                        return true;
                    }
                    String namet = args[1];
                    TauntSetup ts = new TauntSetup(plugin, p, namet);
                    setup.getSm().setSetupTaunt(p, ts);
                    setup.getSem().createTauntMenu(p, ts);
                    p.sendMessage(plugin.getLang().get(p, "setup.taunts.created").replaceAll("<name>", ts.getName()));
                    break;
                case "kits":
                    if (setup.getSm().isSetupKit(p)){
                        KitSetup ks = setup.getSm().getSetupKit(p);
                        setup.getSem().createKitMenu(p, ks);
                        return true;
                    }
                    if (args.length < 2){
                        sendHelp(p);
                        return true;
                    }
                    String namek = args[1];
                    KitSetup ks = new KitSetup(plugin, p, namek);
                    setup.getSm().setSetupKit(p, ks);
                    setup.getSem().createKitMenu(p, ks);
                    p.sendMessage(plugin.getLang().get(p, "setup.kits.created").replaceAll("<name>", ks.getName()));
                    break;
                case "chests":
                    if (setup.getSm().isSetupChest(p)){
                        ChestSetup gs = setup.getSm().getSetupChest(p);
                        setup.getSem().createSetupChestTypeMenu(p, gs);
                        return true;
                    }
                    setup.getSem().createSetupChestMenu(p);
                    break;
                case "glass":
                    if (setup.getSm().isSetupGlass(p)){
                        GlassSetup gs = setup.getSm().getSetupGlass(p);
                        setup.getSem().createGlassMenu(p, gs);
                        return true;
                    }
                    if (args.length < 4){
                        sendHelp(p);
                        return true;
                    }
                    String nameg = args[1];
                    String schematicg = args[2].replaceAll(".schematic", "").replaceAll(".schem", "");
                    String cleag = args[3].replaceAll(".schematic", "").replaceAll(".schem", "");
                    if (!Utils.existsFile(schematicg)){
                        p.sendMessage(plugin.getLang().get(p, "setup.noSchema"));
                        return true;
                    }
                    GlassSetup gs = new GlassSetup(p, nameg, schematicg, cleag);
                    setup.getSm().setSetupGlass(p, gs);
                    setup.getSem().createGlassMenu(p, gs);
                    p.sendMessage(plugin.getLang().get(p, "setup.glass.created").replaceAll("<name>", gs.getName()).replaceAll("<schematic>", schematicg).replaceAll("<clear>", cleag));
                    break;
                case "setmainlobby":
                    plugin.getConfig().set("mainLobby", Utils.getLocationString(p.getLocation()));
                    plugin.saveConfig();
                    plugin.reloadConfig();
                    plugin.loadMainLobby();
                    p.sendMessage(plugin.getLang().get(p, "setup.setMainLobby"));
                    break;
                case "inventory":
                    if (args.length < 2){
                        sendInventories(p);
                        return true;
                    }
                    switch(args[1].toLowerCase()){
                        case "lobby":
                        case "chest":
                        case "health":
                        case "projectile":
                        case "time":
                        case "final":
                        case "votes":
                        case "playagain":
                        case "options":
                        case "players":
                        case "kitsperks":
                        case "balloonsselector":
                        case "glassselector":
                        case "tauntsselector":
                        case "trailsselector":
                        case "partingselector":
                        case "killsoundsselector":
                        case "killeffectsselector":
                        case "wineffectsselector":
                        case "windancesselector":
                        case "gamesoloselector":
                        case "gameteamselector":
                        case "gamerankedselector":
                        case "gameallselector":
                        case "kitselector":
                        case "soulwellmenu":{
                            UltraInventory inventory = plugin.getUim().getMenus(args[1].toLowerCase());
                            plugin.getUim().openInventory(p, inventory);
                            setup.getSm().setSetupInventory(p, inventory);
                            break;
                        }
                        case "mainparty":
                        case "partymember":
                        case "partylist":{
                            if (!plugin.getIjm().isParty()){
                                p.sendMessage(plugin.getLang().get("injections.party"));
                                return true;
                            }
                            UltraInventory inventory = plugin.getUim().getMenus(args[1].toLowerCase());
                            plugin.getUim().openInventory(p, inventory);
                            setup.getSm().setSetupInventory(p, inventory);
                            break;
                        }
                        default:
                            sendInventories(p);
                            break;
                    }
                    break;
                case "settop":
                    if (args.length < 2){
                        sendHelp(sender);
                        return true;
                    }
                    switch(args[1].toLowerCase()){
                        case "elo":
                        case "kills":
                        case "wins":
                        case "deaths":
                        case "coins":
                            String n = args[1].toLowerCase();
                            String generic = n.substring(0, 1).toUpperCase() + n.substring(1).toLowerCase();
                            plugin.getConfig().set("top" + generic, Utils.getLocationString(p.getLocation()));
                            plugin.saveConfig();
                            plugin.reloadConfig();
                            plugin.loadMainLobby();
                            plugin.getTop().createTops();
                            p.sendMessage(plugin.getLang().get(p, "setup.setTop" + generic));
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
            if ("reload".equalsIgnoreCase(args[0])){
                if (args.length == 1){
                    plugin.reload();
                    sender.sendMessage(plugin.getLang().get("setup.reload"));
                    return true;
                }
                switch(args[1].toLowerCase()){
                    case "lang":
                        plugin.reloadLang();
                        sender.sendMessage(plugin.getLang().get("setup.reloadLang"));
                        break;
                    case "config":
                        plugin.reload();
                        sender.sendMessage(plugin.getLang().get("setup.reload"));
                        break;
                    default:
                        sendHelp(sender);
                        break;
                }
            }
        }
        return false;
    }
    
    private void sendInventories(Player p){
        p.sendMessage("§cThe available menus are:");
        p.sendMessage("§7 - §eLobby");
        p.sendMessage("§7 - §eChest");
        p.sendMessage("§7 - §eHealth");
        p.sendMessage("§7 - §eProjectile");
        p.sendMessage("§7 - §eTime");
        p.sendMessage("§7 - §eFinal");
        p.sendMessage("§7 - §eVotes");
        p.sendMessage("§7 - §ePlayAgain");
        p.sendMessage("§7 - §eOptions");
        p.sendMessage("§7 - §ePlayers");
        p.sendMessage("§7 - §eSoulWellMenu");
        p.sendMessage("§7 - §eKitsPerks");
        p.sendMessage("§7 - §eBalloonsSelector");
        p.sendMessage("§7 - §eGlassSelector");
        p.sendMessage("§7 - §eTauntsSelector");
        p.sendMessage("§7 - §eTrailsSelector");
        p.sendMessage("§7 - §ePartingSelector");
        p.sendMessage("§7 - §eKillSoundsSelector");
        p.sendMessage("§7 - §eKillEffectsSelector");
        p.sendMessage("§7 - §eWinEffectsSelector");
        p.sendMessage("§7 - §eWinDancesSelector");
        p.sendMessage("§7 - §eKitSelector");
        p.sendMessage("§7 - §eGameSoloSelector");
    }
    
    private void sendHelp(CommandSender s){
        s.sendMessage("§7§m---------------§r   §6§lUltraSkyWars §ev" + plugin.getDescription().getVersion() + "§r   §7§m---------------");
        s.sendMessage("§7       §7");
        s.sendMessage("§6§lUltraSkyWars §7- §a§lArena Commands");
        new FancyMessage("§b/sws create <name> §7- §eCreating a new arena.").setHover(HoverEvent.Action.SHOW_TEXT, "§fClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/sws create ").build().send(s);
        new FancyMessage("§b/sws edit <name> §7- §eEdit arena.").setHover(HoverEvent.Action.SHOW_TEXT, "§fClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/sws edit ").build().send(s);
        new FancyMessage("§b/sws delete <name> §7- §eDelete this arena.").setHover(HoverEvent.Action.SHOW_TEXT, "§fClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/sws delete ").build().send(s);
        new FancyMessage("§b/sws clone <name> <newName> §7- §eClone this arena.").setHover(HoverEvent.Action.SHOW_TEXT, "§fClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/sws clone ").build().send(s);
        s.sendMessage("§7       §7");
        s.sendMessage("§6§lUltraSkyWars §7- §a§lCosmetics Commands");
        new FancyMessage("§b/sws glass <name> <schematic> <clear> §7- §eCreating a new glass.").setHover(HoverEvent.Action.SHOW_TEXT, "§fClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/sws glass ").build().send(s);
        new FancyMessage("§b/sws taunts <name> §7- §eCreating a new taunt.").setHover(HoverEvent.Action.SHOW_TEXT, "§fClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/sws taunts ").build().send(s);
        new FancyMessage("§b/sws kits <name> §7- §eCreating a new kit.").setHover(HoverEvent.Action.SHOW_TEXT, "§fClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/sws kits ").build().send(s);
        new FancyMessage("§b/sws balloons <name> §7- §eCreating a new balloon.").setHover(HoverEvent.Action.SHOW_TEXT, "§fClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/sws balloons ").build().send(s);
        new FancyMessage("§b/sws trails <name> §7- §eCreating a new trail.").setHover(HoverEvent.Action.SHOW_TEXT, "§fClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/sws trails ").build().send(s);
        new FancyMessage("§b/sws killsounds <name> §7- §eCreating a new killsound.").setHover(HoverEvent.Action.SHOW_TEXT, "§fClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/sws killsounds ").build().send(s);
        new FancyMessage("§b/sws parting <name> §7- §eCreating a new parting.").setHover(HoverEvent.Action.SHOW_TEXT, "§fClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/sws parting ").build().send(s);
        s.sendMessage("§7       §7");
        s.sendMessage("§6§lUltraSkyWars §7- §a§lGeneral Commands");
        new FancyMessage("§b/sws createchest <name> §7- §eCreate chest type.").setHover(HoverEvent.Action.SHOW_TEXT, "§fClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/sws createchest  ").build().send(s);
        new FancyMessage("§b/sws chests §7- §eSetup chests.").setHover(HoverEvent.Action.SHOW_TEXT, "§fClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/sws chests").build().send(s);
        new FancyMessage("§b/sws rewards §7- §eJoin on setup of soulwell/cubelets rewards.").setHover(HoverEvent.Action.SHOW_TEXT, "§fClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/sws rewards").build().send(s);
        new FancyMessage("§b/sws setupblock cubelets/soulwell  §7- §eJoin on setup of cubelet/soulwell block.").setHover(HoverEvent.Action.SHOW_TEXT, "§fClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/sws setupblock ").build().send(s);
        new FancyMessage("§b/sws settop kills/wins/deaths/coins/elo §7- §eSetup tops locations.").setHover(HoverEvent.Action.SHOW_TEXT, "§fClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/sws settop ").build().send(s);
        new FancyMessage("§b/sws setpreview <type> §7- §eCreate new location preview.").setHover(HoverEvent.Action.SHOW_TEXT, "§fClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/sws setpreview ").build().send(s);
        new FancyMessage("§b/sws setmainlobby §7- §eSets the main lobby.").setHover(HoverEvent.Action.SHOW_TEXT, "§fClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/sws setmainlobby").build().send(s);
        new FancyMessage("§b/sws reload [lang/config] §7- §eReloads the plugin.").setHover(HoverEvent.Action.SHOW_TEXT, "§fClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/sws reload ").build().send(s);
        new FancyMessage("§b/sws inventory <type> §7- §eEdit a inventory.").setHover(HoverEvent.Action.SHOW_TEXT, "§fClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/sws inventory ").build().send(s);
        s.sendMessage("§7§m----------------------------------------------------");
    }
    
}