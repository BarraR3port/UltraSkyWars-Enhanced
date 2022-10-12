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

package io.github.Leonardo0013YT.UltraSkyWars.api.managers;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.CustomSound;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XSound;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class ConfigManager {
    
    private ItemStack head;
    private byte waitingState, startingState, emptyState;
    private byte redPanelData;
    private int kitsSlot, votesSlot, teamSlot, challengesSlot, leaveSlot, enderPearlSeconds, rankedLevels, maxMobFriends, soulWellPrice, maxCollectHeads, timeBorderReduction, minBorderReduction, updatePlayersPlaceholder, maxItemsChest, ticksAni3, executesAni3, ticksAni1, upYAni1, cubeletChance, gamesToRestart, itemLobbySlot, winTime, progressBarAmount, maxMultiplier, starting, pregame, coinsKill, coinsWin, coinsAssists, xpKill, xpWin, xpAssists, soulsKill, soulsWin, soulsAssists;
    private XMaterial next, last, deselect, challenges, back, closeitem, lobby, team, spectate, options, play, leave, kits, setup, center, island, soulwell;
    private Material redPanelMaterial, votes;
    private boolean cosmeticsGlasses, cosmeticsKillEffect, cosmeticsBalloons, cosmeticsKillSounds, cosmeticsPartings, cosmeticsTaunts, cosmeticsTrails, cosmeticsWinDance, cosmeticsWinEffects,
            disableLevels, chestHolograms, broadcastRankLevelUp, broadcastRankLevelDown, disableAllScoreboards, spectatorChatGlobal, showBowDamage, enderPearlsCountdown, rankedJoin, rankedJoinParties, spectatorChat, disableVotesRanked, bungeeModeLobby, broadcastGame, broadcastLevelUp, naturalSpawnEggs, disableKitLevels, savingDataSync, motdStates, autoLapiz, wCMDEnabled, kCMDEnabled, dCMDEnabled, teamOneCage, signsRotation, cubeletsEnabled, mainLobby, preLobbySystem, kitLevelsOrder, itemLobbyEnabled, redPanelInLocked, lobbyScoreboard, soulwellEnabled, chatSystem, signsRight, signsLeft;
    private boolean autoFlyEnabled;
    private String teamModeChar, lobbyWorld, slimeworldmanagerLoader, perm, url, itemLobbyCMD, lobbyServer, gameServer, autoFlyPermission;
    private String gameType;
    private List<String> whitelistedCMD, winCommands, killCommands, deathCommands;
    private Location previewPlayerGlass, previewCosmeticGlass, previewPlayerBalloon, previewCosmeticBalloon;
    
    public ConfigManager(){
        reload();
    }
    
    public void reload(){
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        this.cosmeticsGlasses = plugin.getConfig().getBoolean("cosmetics.glasses");
        this.cosmeticsKillEffect = plugin.getConfig().getBoolean("cosmetics.killEffect");
        this.cosmeticsBalloons = plugin.getConfig().getBoolean("cosmetics.balloons");
        this.cosmeticsKillSounds = plugin.getConfig().getBoolean("cosmetics.killSounds");
        this.cosmeticsPartings = plugin.getConfig().getBoolean("cosmetics.partings");
        this.cosmeticsTaunts = plugin.getConfig().getBoolean("cosmetics.taunts");
        this.cosmeticsTrails = plugin.getConfig().getBoolean("cosmetics.trails");
        this.cosmeticsWinDance = plugin.getConfig().getBoolean("cosmetics.winDance");
        this.cosmeticsWinEffects = plugin.getConfig().getBoolean("cosmetics.winEffects");
        this.kitsSlot = plugin.getConfig().getInt("items.kits.slot");
        this.votesSlot = plugin.getConfig().getInt("items.votes.slot");
        this.teamSlot = plugin.getConfig().getInt("items.team.slot");
        this.challengesSlot = plugin.getConfig().getInt("items.challenges.slot");
        this.leaveSlot = plugin.getConfig().getInt("items.leave.slot");
        this.disableLevels = plugin.getConfig().getBoolean("specials.disableLevels");
        this.chestHolograms = plugin.getConfig().getBoolean("specials.chestHolograms");
        this.showBowDamage = plugin.getConfig().getBoolean("specials.showBowDamage");
        this.disableAllScoreboards = plugin.getConfig().getBoolean("specials.disableAllScoreboards");
        this.enderPearlsCountdown = plugin.getConfig().getBoolean("specials.enderPearlsCountdown.enabled");
        this.enderPearlSeconds = plugin.getConfig().getInt("specials.enderPearlsCountdown.seconds");
        this.rankedJoin = plugin.getConfig().getBoolean("specials.rankedJoin.enabled");
        this.rankedJoinParties = plugin.getConfig().getBoolean("specials.rankedJoin.parties");
        this.autoFlyEnabled = plugin.getConfig().getBoolean("specials.autoFly.enabled");
        this.autoFlyPermission = plugin.getConfig().getString("specials.autoFly.permission");
        this.rankedLevels = plugin.getConfig().getInt("specials.rankedJoin.levels");
        this.disableVotesRanked = plugin.getConfig().getBoolean("disableVotesRanked");
        this.teamModeChar = plugin.getConfig().getString("teamModeChar");
        this.previewPlayerGlass = Utils.getStringLocation(plugin.getConfig().getString("previews.glass.player"));
        this.previewCosmeticGlass = Utils.getStringLocation(plugin.getConfig().getString("previews.glass.cosmetic"));
        this.previewPlayerBalloon = Utils.getStringLocation(plugin.getConfig().getString("previews.balloon.player"));
        this.previewCosmeticBalloon = Utils.getStringLocation(plugin.getConfig().getString("previews.balloon.cosmetic"));
        for ( CustomSound cs : CustomSound.values() ){
            String path = "sounds." + cs.name();
            cs.setSound(XSound.matchXSound(plugin.getSounds().get(path + ".sound")).orElse(XSound.BLOCK_NOTE_BLOCK_HAT).parseSound());
            cs.setVolume((float) plugin.getSounds().getDouble(path + ".volume"));
            cs.setPitch((float) plugin.getSounds().getDouble(path + ".pitch"));
        }
        this.bungeeModeLobby = false;
        this.soulWellPrice = plugin.getConfig().getInt("soulWellPrice");
        this.maxCollectHeads = plugin.getConfig().getInt("maxCollectHeads");
        this.maxMobFriends = plugin.getConfig().getInt("gameDefaults.maxMobFriends");
        this.timeBorderReduction = plugin.getConfig().getInt("gameDefaults.timeBorderReduction");
        this.minBorderReduction = plugin.getConfig().getInt("gameDefaults.minBorderReduction");
        this.disableKitLevels = plugin.getConfig().getBoolean("disableKitLevels");
        this.savingDataSync = plugin.getConfig().getBoolean("savingDataSync");
        this.lobbyWorld = plugin.getConfig().getString("mainLobbyWorld");
        this.motdStates = plugin.getConfig().getBoolean("motdStates");
        this.autoLapiz = plugin.getConfig().getBoolean("autoLapiz");
        this.wCMDEnabled = plugin.getConfig().getBoolean("win-commands.enabled");
        this.kCMDEnabled = plugin.getConfig().getBoolean("kill-commands.enabled");
        this.dCMDEnabled = plugin.getConfig().getBoolean("death-commands.enabled");
        this.winCommands = plugin.getConfig().getStringList("win-commands.cmds");
        this.killCommands = plugin.getConfig().getStringList("kill-commands.cmds");
        this.deathCommands = plugin.getConfig().getStringList("death-commands.cmds");
        this.teamOneCage = plugin.getConfig().getBoolean("teamOneCage");
        this.signsRotation = plugin.getConfig().getBoolean("signsRotation");
        this.preLobbySystem = plugin.getConfig().getBoolean("preLobbySystem");
        this.kitLevelsOrder = plugin.getConfig().getBoolean("kitLevelsOrder");
        this.whitelistedCMD = plugin.getConfig().getStringList("whitelistedCMD");
        this.updatePlayersPlaceholder = plugin.getConfig().getInt("updatePlayersPlaceholder") * 1000;
        this.gamesToRestart = plugin.getConfig().getInt("bungee.gamesToRestart");
        this.gameServer = plugin.getConfig().getString("bungee.gameServer");
        this.lobbyServer = plugin.getConfig().getString("bungee.lobbyServer");
        this.gameType = plugin.getConfig().getString("bungee.gameType").toUpperCase();
        this.itemLobbyEnabled = plugin.getConfig().getBoolean("items.lobby.enabled");
        this.itemLobbySlot = plugin.getConfig().getInt("items.lobby.slot");
        this.itemLobbyCMD = plugin.getConfig().getString("items.lobby.cmd");
        this.winTime = plugin.getConfig().getInt("gameDefaults.winTime");
        this.redPanelData = (byte) plugin.getConfig().getInt("redPanel.data");
        this.redPanelMaterial = Material.valueOf(plugin.getConfig().getString("redPanel.material"));
        this.redPanelInLocked = plugin.getConfig().getBoolean("redPanelInLocked");
        this.progressBarAmount = plugin.getConfig().getInt("progressBarAmount");
        this.soulwellEnabled = plugin.getConfig().getBoolean("rewards.soulwell");
        this.lobbyScoreboard = plugin.getConfig().getBoolean("lobbyScoreboard");
        this.mainLobby = plugin.getConfig().getBoolean("chat.mainLobby");
        this.chatSystem = plugin.getConfig().getBoolean("chat.games");
        this.spectatorChat = plugin.getConfig().getBoolean("chat.spectator");
        this.spectatorChatGlobal = plugin.getConfig().getBoolean("chat.spectatorGlobal");
        this.broadcastRankLevelDown = plugin.getConfig().getBoolean("chat.broadcastRankLevelDown");
        this.broadcastRankLevelUp = plugin.getConfig().getBoolean("chat.broadcastRankLevelUp");
        this.broadcastLevelUp = plugin.getConfig().getBoolean("chat.broadcastLevelUp");
        this.broadcastGame = plugin.getConfig().getBoolean("chat.broadcastGame");
        this.slimeworldmanagerLoader = plugin.getConfig().getString("addons.SlimeWorldManager-Loader");
        this.perm = plugin.getConfig().getString("permCMD");
        this.signsRight = plugin.getConfig().getBoolean("signs.right");
        this.signsLeft = plugin.getConfig().getBoolean("signs.left");
        this.waitingState = (byte) plugin.getConfig().getInt("states.waiting");
        this.startingState = (byte) plugin.getConfig().getInt("states.starting");
        this.emptyState = (byte) plugin.getConfig().getInt("states.empty");
        this.naturalSpawnEggs = plugin.getConfig().getBoolean("gameDefaults.naturalSpawnEggs");
        this.maxItemsChest = plugin.getConfig().getInt("gameDefaults.maxItemsChest");
        this.maxMultiplier = plugin.getConfig().getInt("gameDefaults.maxMultiplier");
        this.starting = plugin.getConfig().getInt("gameDefaults.starting");
        this.pregame = plugin.getConfig().getInt("gameDefaults.pregame");
        this.coinsKill = plugin.getConfig().getInt("gameDefaults.coins.kill");
        this.coinsWin = plugin.getConfig().getInt("gameDefaults.coins.win");
        this.coinsAssists = plugin.getConfig().getInt("gameDefaults.coins.assists");
        this.xpKill = plugin.getConfig().getInt("gameDefaults.xp.kill");
        this.xpWin = plugin.getConfig().getInt("gameDefaults.xp.win");
        this.xpAssists = plugin.getConfig().getInt("gameDefaults.xp.assists");
        this.soulsKill = plugin.getConfig().getInt("gameDefaults.souls.kill");
        this.soulsWin = plugin.getConfig().getInt("gameDefaults.souls.win");
        this.soulsAssists = plugin.getConfig().getInt("gameDefaults.souls.assists");
        this.lobby = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.lobby")).orElse(XMaterial.DIAMOND);
        this.soulwell = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.soulwell")).orElse(XMaterial.WHITE_STAINED_GLASS);
        this.team = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.team")).orElse(XMaterial.NETHER_STAR);
        this.spectate = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.spectate")).orElse(XMaterial.COMPASS);
        this.options = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.options")).orElse(XMaterial.COMPARATOR);
        this.play = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.play")).orElse(XMaterial.PAPER);
        this.leave = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.leave")).orElse(XMaterial.RED_BED);
        this.kits = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.kits")).orElse(XMaterial.BOW);
        this.votes = Material.valueOf(plugin.getConfig().getString("materials.votes"));
        this.setup = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.setup")).orElse(XMaterial.DIAMOND);
        this.center = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.center")).orElse(XMaterial.BLAZE_ROD);
        this.island = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.island")).orElse(XMaterial.BLAZE_POWDER);
        this.closeitem = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.closeitem")).orElse(XMaterial.BOOK);
        this.back = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.back")).orElse(XMaterial.BOOK);
        this.challenges = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.challenges")).orElse(XMaterial.BLAZE_POWDER);
        this.deselect = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.deselect")).orElse(XMaterial.BARRIER);
        this.next = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.next")).orElse(XMaterial.ARROW);
        this.last = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.last")).orElse(XMaterial.ARROW);
    }
    
    public void reloadInjections(){
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        if (plugin.getIjm() == null) return;
        if (plugin.getIjm().isCubeletsInjection()){
            this.ticksAni3 = plugin.getIjm().getCubelets().getCubelets().getInt("animations.flames.ticks");
            this.executesAni3 = plugin.getIjm().getCubelets().getCubelets().getInt("animations.flames.executes");
            this.url = plugin.getIjm().getCubelets().getCubelets().get(null, "animations.head.url");
            this.head = ItemBuilder.createSkull("§7", "§7", plugin.getIjm().getCubelets().getCubelets().get(null, "animations.head.url"));
            this.upYAni1 = plugin.getIjm().getCubelets().getCubelets().getInt("animations.fireworks.upY");
            this.ticksAni1 = plugin.getIjm().getCubelets().getCubelets().getInt("animations.fireworks.ticks");
            this.cubeletChance = plugin.getIjm().getCubelets().getCubelets().getInt("gameDefaults.cubeletsChance");
            this.cubeletsEnabled = plugin.getIjm().getCubelets().getCubelets().getBoolean("cubelets");
        }
    }
    
}