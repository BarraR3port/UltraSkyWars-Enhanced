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

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.StatType;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class SWPlayer {
    
    private ArrayList<String> favorites = new ArrayList<>();
    private HashMap<Integer, List<Integer>> kits = new HashMap<>();
    private List<Integer> balloons = new ArrayList<>(), glasses = new ArrayList<>(), killeffects = new ArrayList<>(), killsounds = new ArrayList<>(), partings = new ArrayList<>(), taunts = new ArrayList<>(), trails = new ArrayList<>(), windances = new ArrayList<>(), wineffects = new ArrayList<>();
    private int ranking = 999999, headsCollected = 0, soulWellHead = 1, soulWellExtra = 0, soulWellMax = 0, soulanimation = 0, cubeanimation = 0, cubelets = 0, rows = 1, armorDance = 999999, balloon = 999999,
            glass = 0, killEffect = 999999, killSound = 999999, parting = 999999, taunt = 0, trail = 999999, winDance = 999999, winEffect = 0, elo = 0, souls = 0, xp = 0, coins = 0, level = 1;
    private String name = "", eloRank = "classify", prestigeIcon = "icon1";
    private float speed = 0.2F;
    private long lastChange = 0L;
    private boolean nightVision = true, spectatorsView = false, firstPerson = false, showLevel = true;
    private int soloKit = 999999, soloKitLevel = 1, teamKit = 999999, teamKitLevel = 1, rankedKit = 999999, rankedKitLevel = 1;
    // Stat Type -> Mode -> Amount
    private HashSet<Integer> perksEnabled = new HashSet<>();
    private HashMap<Integer, Integer> perksData = new HashMap<>();
    private HashMap<String, Integer> challengesCompleted = new HashMap<>();
    private HashMap<String, HashMap<String, Long>> heads = new HashMap<>();
    private HashMap<String, HashMap<String, Integer>> stats = new HashMap<>();
    private HashMap<String, Integer> totalStats = new HashMap<>();
    
    public HashSet<Integer> getPerksEnabled(){
        return perksEnabled;
    }
    
    public String getPrestigeIcon(){
        return prestigeIcon;
    }
    
    public void setPrestigeIcon(String prestigeIcon){
        this.prestigeIcon = prestigeIcon;
    }
    
    public int getChallengeCompleted(String challenge){
        return challengesCompleted.getOrDefault(challenge, 0);
    }
    
    public void addChallengeCompleted(String challenge){
        challengesCompleted.put(challenge, challengesCompleted.getOrDefault(challenge, 0) + 1);
    }
    
    public void addStat(StatType stat, String mode, int amount){
        stats.putIfAbsent(stat.name(), new HashMap<>());
        stats.get(stat.name()).put(mode, stats.get(stat.name()).getOrDefault(mode, 0) + amount);
        totalStats.put(stat.name(), totalStats.getOrDefault(stat.name(), 0) + amount);
    }
    
    public int getStat(StatType stat, String mode){
        stats.putIfAbsent(stat.name(), new HashMap<>());
        return stats.get(stat.name()).getOrDefault(mode, 0);
    }
    
    public int getTotalStat(StatType stat){
        return totalStats.getOrDefault(stat.name(), 0);
    }
    
    public boolean isShowLevel(){
        return showLevel;
    }
    
    public void setShowLevel(boolean showLevel){
        this.showLevel = showLevel;
    }
    
    public int getRankedKit(){
        return rankedKit;
    }
    
    public void setRankedKit(int rankedKit){
        this.rankedKit = rankedKit;
    }
    
    public int getRankedKitLevel(){
        return rankedKitLevel;
    }
    
    public void setRankedKitLevel(int rankedKitLevel){
        this.rankedKitLevel = rankedKitLevel;
    }
    
    public int getSoloKit(){
        return soloKit;
    }
    
    public void setSoloKit(int soloKit){
        this.soloKit = soloKit;
    }
    
    public int getSoloKitLevel(){
        return soloKitLevel;
    }
    
    public void setSoloKitLevel(int soloKitLevel){
        this.soloKitLevel = soloKitLevel;
    }
    
    public int getTeamKit(){
        return teamKit;
    }
    
    public void setTeamKit(int teamKit){
        this.teamKit = teamKit;
    }
    
    public int getTeamKitLevel(){
        return teamKitLevel;
    }
    
    public void setTeamKitLevel(int teamKitLevel){
        this.teamKitLevel = teamKitLevel;
    }
    
    public int getRanking(){
        return ranking;
    }
    
    public void setRanking(int ranking){
        this.ranking = ranking;
    }
    
    public long getLastChange(){
        return lastChange;
    }
    
    public void setLastChange(long lastChange){
        this.lastChange = lastChange;
    }
    
    public HashMap<String, HashMap<String, Long>> getHeads(){
        return heads;
    }
    
    public void setHeads(HashMap<String, HashMap<String, Long>> heads){
        this.heads = heads;
    }
    
    public int getHeadsCollected(){
        return headsCollected;
    }
    
    public void setHeadsCollected(int headsCollected){
        this.headsCollected = headsCollected;
    }
    
    public boolean hasHead(String rarity, String name){
        heads.putIfAbsent(rarity, new HashMap<>());
        return heads.get(rarity).containsKey(name);
    }
    
    public void addHead(String rarity, String name, long date){
        heads.putIfAbsent(rarity, new HashMap<>());
        if (headsCollected > UltraSkyWarsApi.get().getCm().getMaxCollectHeads()){
            NavigableMap<String, Long> nav = new TreeMap<>(heads.get(rarity));
            nav.remove(nav.firstKey());
            headsCollected--;
        }
        heads.get(rarity).putIfAbsent(name, date);
        headsCollected++;
    }
    
    public int getHeadCount(String rarity){
        if (heads.containsKey(rarity)){
            return heads.get(rarity).size();
        }
        return 0;
    }
    
    public int getSoulWellHead(){
        return soulWellHead;
    }
    
    public void setSoulWellHead(int soulWellHead){
        this.soulWellHead = soulWellHead;
    }
    
    public int getSoulWellExtra(){
        return soulWellExtra;
    }
    
    public void setSoulWellExtra(int soulWellExtra){
        this.soulWellExtra = soulWellExtra;
    }
    
    public int getSoulWellMax(){
        return soulWellMax;
    }
    
    public void setSoulWellMax(int soulWellMax){
        this.soulWellMax = soulWellMax;
    }
    
    public int getSoulanimation(){
        return soulanimation;
    }
    
    public void setSoulanimation(int soulanimation){
        this.soulanimation = soulanimation;
    }
    
    public float getSpeed(){
        return speed;
    }
    
    public void setSpeed(float speed){
        this.speed = speed;
    }
    
    public boolean isNightVision(){
        return nightVision;
    }
    
    public void setNightVision(boolean nightVision){
        this.nightVision = nightVision;
    }
    
    public boolean isSpectatorsView(){
        return spectatorsView;
    }
    
    public void setSpectatorsView(boolean spectatorsView){
        this.spectatorsView = spectatorsView;
    }
    
    public boolean isFirstPerson(){
        return firstPerson;
    }
    
    public void setFirstPerson(boolean firstPerson){
        this.firstPerson = firstPerson;
    }
    
    public ArrayList<String> getFavorites(){
        return favorites;
    }
    
    public void setFavorites(ArrayList<String> favorites){
        this.favorites = favorites;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public HashMap<Integer, Integer> getPerksData(){
        return perksData;
    }
    
    public List<Integer> getBalloons(){
        return balloons;
    }
    
    public void setBalloons(List<Integer> balloons){
        this.balloons = balloons;
    }
    
    public void addBalloons(int id){
        balloons.add(id);
    }
    
    public void removeBalloons(int id){
        balloons.remove(id);
    }
    
    public List<Integer> getKilleffects(){
        return killeffects;
    }
    
    public void setKilleffects(List<Integer> killeffects){
        this.killeffects = killeffects;
    }
    
    public void addKillEffects(int id){
        killeffects.add(id);
    }
    
    public void removeKillEffects(int id){
        killeffects.remove(id);
    }
    
    public List<Integer> getKillsounds(){
        return killsounds;
    }
    
    public void setKillsounds(List<Integer> killsounds){
        this.killsounds = killsounds;
    }
    
    public void addKillSounds(int id){
        killsounds.add(id);
    }
    
    public void removeKillSounds(int id){
        killsounds.remove(id);
    }
    
    public HashMap<Integer, List<Integer>> getKits(){
        return kits;
    }
    
    public void setKits(HashMap<Integer, List<Integer>> kits){
        this.kits = kits;
    }
    
    public void addKitLevel(int kitID, int level){
        if (!kits.containsKey(kitID)){
            kits.put(kitID, new ArrayList<>());
        }
        kits.get(kitID).add(level);
    }
    
    public void removeKitLevel(int kitID, int level){
        if (kits.containsKey(kitID)){
            kits.get(kitID).remove(level);
        }
    }
    
    public boolean hasKitLevel(int kitID, int level){
        if (kits.containsKey(kitID)){
            return kits.get(kitID).contains(level);
        }
        return false;
    }
    
    public List<Integer> getPartings(){
        return partings;
    }
    
    public void setPartings(List<Integer> partings){
        this.partings = partings;
    }
    
    public void addPartings(int id){
        partings.add(id);
    }
    
    public void removePartings(int id){
        partings.remove(id);
    }
    
    public List<Integer> getTaunts(){
        return taunts;
    }
    
    public void setTaunts(List<Integer> taunts){
        this.taunts = taunts;
    }
    
    public void addTaunts(int id){
        taunts.add(id);
    }
    
    public void removeTaunts(int id){
        taunts.remove(id);
    }
    
    public List<Integer> getTrails(){
        return trails;
    }
    
    public void setTrails(List<Integer> trails){
        this.trails = trails;
    }
    
    public void addTrails(int id){
        trails.add(id);
    }
    
    public void removeTrails(int id){
        trails.remove(id);
    }
    
    public List<Integer> getWindances(){
        return windances;
    }
    
    public void setWindances(List<Integer> windances){
        this.windances = windances;
    }
    
    public void addWinDances(int id){
        windances.add(id);
    }
    
    public void removeWinDances(int id){
        windances.remove(id);
    }
    
    public List<Integer> getWineffects(){
        return wineffects;
    }
    
    public void setWineffects(List<Integer> wineffects){
        this.wineffects = wineffects;
    }
    
    public void addWinEffects(int id){
        wineffects.add(id);
    }
    
    public void removeWinEffects(int id){
        wineffects.remove(id);
    }
    
    public List<Integer> getGlasses(){
        return glasses;
    }
    
    public void setGlasses(List<Integer> glasses){
        this.glasses = glasses;
    }
    
    public void addGlasses(int id){
        glasses.add(id);
    }
    
    public void removeGlasses(int id){
        glasses.remove(id);
    }
    
    public int getCubeAnimation(){
        return cubeanimation;
    }
    
    public void setCubeAnimation(int cubeanimation){
        this.cubeanimation = cubeanimation;
    }
    
    public void addCubeAnimation(int cubeanimation){
        this.cubeanimation += cubeanimation;
    }
    
    public void removeCubeAnimation(int cubeanimation){
        this.cubeanimation -= cubeanimation;
    }
    
    public int getCubelets(){
        return cubelets;
    }
    
    public void setCubelets(int cubelets){
        this.cubelets = cubelets;
    }
    
    public void addCubelets(int cubelets){
        this.cubelets += cubelets;
    }
    
    public void removeCubelets(int cubelets){
        this.cubelets -= cubelets;
    }
    
    public int getRows(){
        return rows;
    }
    
    public void setRows(int rows){
        this.rows = rows;
    }
    
    public void addRows(int rows){
        this.rows += rows;
    }
    
    public void removeRows(int rows){
        this.rows -= rows;
    }
    
    public int getLevel(){
        return level;
    }
    
    public void setLevel(int level){
        this.level = level;
    }
    
    public String getEloRank(){
        return eloRank;
    }
    
    public void setEloRank(String eloRank){
        this.eloRank = eloRank;
    }
    
    public void removeElo(int elo){
        this.elo -= elo;
    }
    
    public void addElo(int elo){
        this.elo += elo;
    }
    
    public int getArmorDance(){
        return armorDance;
    }
    
    public void setArmorDance(int armorDance){
        this.armorDance = armorDance;
    }
    
    public int getBalloon(){
        return balloon;
    }
    
    public void setBalloon(int balloon){
        this.balloon = balloon;
    }
    
    public int getGlass(){
        return glass;
    }
    
    public void setGlass(int glass){
        this.glass = glass;
    }
    
    public int getKillEffect(){
        return killEffect;
    }
    
    public void setKillEffect(int killEffect){
        this.killEffect = killEffect;
    }
    
    public int getKillSound(){
        return killSound;
    }
    
    public void setKillSound(int killSound){
        this.killSound = killSound;
    }
    
    public int getParting(){
        return parting;
    }
    
    public void setParting(int parting){
        this.parting = parting;
    }
    
    public int getTaunt(){
        return taunt;
    }
    
    public void setTaunt(int taunt){
        this.taunt = taunt;
    }
    
    public int getTrail(){
        return trail;
    }
    
    public void setTrail(int trail){
        this.trail = trail;
    }
    
    public int getWinDance(){
        return winDance;
    }
    
    public void setWinDance(int winDance){
        this.winDance = winDance;
    }
    
    public int getWinEffect(){
        return winEffect;
    }
    
    public void setWinEffect(int winEffect){
        this.winEffect = winEffect;
    }
    
    public int getElo(){
        return elo;
    }
    
    public void setElo(int elo){
        this.elo = elo;
    }
    
    public void addSouls(int souls){
        if (UltraSkyWarsApi.get().getIjm().isSoulWellInjection()){
            this.souls = Math.min(this.souls += souls, UltraSkyWarsApi.get().getIjm().getSoulwell().getSwm().getMaxSouls(soulWellMax));
        } else {
            this.souls += souls;
        }
    }
    
    public void addXp(int xp){
        this.xp += xp;
    }
    
    public void addCoins(int coins){
        this.coins += coins;
    }
    
    public void removeCoins(int coins){
        this.coins -= coins;
    }
    
    public void removeSouls(int souls){
        this.souls -= souls;
    }
    
    public void removeXp(int xp){
        this.xp -= xp;
    }
    
    public int getSouls(){
        return souls;
    }
    
    public void setSouls(int souls){
        if (UltraSkyWarsApi.get().getIjm().isSoulWellInjection()){
            this.souls = Math.min(souls, UltraSkyWarsApi.get().getIjm().getSoulwell().getSwm().getMaxSouls(soulWellMax));
        } else {
            this.souls = souls;
        }
    }
    
    public int getXp(){
        return xp;
    }
    
    public void setXp(int xp){
        this.xp = xp;
    }
    
    public int getCoins(){
        return coins;
    }
    
    public void setCoins(int coins){
        this.coins = coins;
    }
    
}