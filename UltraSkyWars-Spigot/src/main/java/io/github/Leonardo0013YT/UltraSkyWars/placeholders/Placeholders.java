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

package io.github.Leonardo0013YT.UltraSkyWars.placeholders;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.StatType;
import io.github.Leonardo0013YT.UltraSkyWars.api.objects.Level;
import io.github.Leonardo0013YT.UltraSkyWars.api.objects.PrestigeIcon;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Placeholders extends PlaceholderExpansion {
    
    private final List<String> modes;
    
    public Placeholders(){
        this.modes = Arrays.stream(StatType.values()).map(Enum::name).collect(Collectors.toList());
    }
    
    @Nonnull
    public String getIdentifier(){
        return "usw";
    }
    
    @Nonnull
    public String getAuthor(){
        return "Leonardo0013YT";
    }
    
    @Nonnull
    public String getVersion(){
        return "7.7.0";
    }
    
    @Override
    public boolean persist(){
        return true;
    }
    
    @Override
    public String onPlaceholderRequest(Player p, @Nonnull String id){
        if (p == null || !p.isOnline()) return null;
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        if (id.startsWith("players_")){
            String type = id.replaceFirst("players_", "").replaceAll("%", "");
            return "" + plugin.getGm().getGameSize(type);
        }
        if (id.equals("total_balloons")){
            return "" + plugin.getCos().getBalloonSize();
        }
        if (id.equals("total_kits")){
            return "" + plugin.getKm().getKitsSize();
        }
        if (id.equals("total_killeffects")){
            return "" + plugin.getCos().getKillEffectSize();
        }
        if (id.equals("total_glass")){
            return "" + plugin.getCos().getGlassesSize();
        }
        if (id.equals("total_killsounds")){
            return "" + plugin.getCos().getKillSoundsSize();
        }
        if (id.equals("total_partings")){
            return "" + plugin.getCos().getPartingsSize();
        }
        if (id.equals("total_taunts")){
            return "" + plugin.getCos().getTauntsSize();
        }
        if (id.equals("total_wineffects")){
            return "" + plugin.getCos().getWinEffectsSize();
        }
        if (id.equals("total_windances")){
            return "" + plugin.getCos().getWinDancesSize();
        }
        if (id.equals("total_trails")){
            return "" + plugin.getCos().getTrailsSize();
        }
        if (sw == null){
            return "";
        }
        if (id.equals("prestige_icon")){
            if (!sw.isShowLevel()){
                return "";
            }
            PrestigeIcon pi = plugin.getLvl().getPrestige().get(sw.getPrestigeIcon());
            if (pi != null){
                return pi.getPrefix();
            }
            return "";
        }
        if (id.equals("ranking")){
            return "" + sw.getRanking();
        }
        if (id.equals("selected_trail")){
            return plugin.getCos().getSelected(sw.getTrail(), plugin.getCos().getTrails());
        }
        if (id.equals("selected_windance")){
            return plugin.getCos().getSelected(sw.getWinDance(), plugin.getCos().getWinDance());
        }
        if (id.equals("selected_wineffect")){
            return plugin.getCos().getSelected(sw.getWinEffect(), plugin.getCos().getWinEffects());
        }
        if (id.equals("selected_taunt")){
            return plugin.getCos().getSelected(sw.getTaunt(), plugin.getCos().getTaunts());
        }
        if (id.equals("selected_parting")){
            return plugin.getCos().getSelected(sw.getParting(), plugin.getCos().getPartings());
        }
        if (id.equals("selected_killsound")){
            return plugin.getCos().getSelected(sw.getKillSound(), plugin.getCos().getKillSounds());
        }
        if (id.equals("selected_killeffect")){
            return plugin.getCos().getSelected(sw.getKillEffect(), plugin.getCos().getKillEffect());
        }
        if (id.equals("selected_glass")){
            return plugin.getCos().getSelected(sw.getGlass(), plugin.getCos().getGlasses());
        }
        if (id.equals("selected_balloon")){
            return plugin.getCos().getSelected(sw.getBalloon(), plugin.getCos().getBalloons());
        }
        if (id.equals("selected_solo_kit")){
            return plugin.getKm().getSelected(sw, "SOLO");
        }
        if (id.equals("selected_team_kit")){
            return plugin.getKm().getSelected(sw, "TEAM");
        }
        if (id.equals("selected_ranked_kit")){
            return plugin.getKm().getSelected(sw, "RANKED");
        }
        if (id.equals("unlocked_trails")){
            return "" + sw.getTrails().size();
        }
        if (id.equals("bar_trails")){
            return "" + Utils.getProgressBar(sw.getTrails().size(), plugin.getCos().getTrailsSize(), plugin.getCm().getProgressBarAmount());
        }
        if (id.equals("percentage_trails")){
            return "" + Utils.getProgressBar(sw.getTrails().size(), plugin.getCos().getTrailsSize());
        }
        if (id.equals("unlocked_windances")){
            return "" + sw.getWindances().size();
        }
        if (id.equals("bar_windances")){
            return "" + Utils.getProgressBar(sw.getWindances().size(), plugin.getCos().getWinDancesSize(), plugin.getCm().getProgressBarAmount());
        }
        if (id.equals("percentage_windances")){
            return "" + Utils.getProgressBar(sw.getWindances().size(), plugin.getCos().getWinDancesSize());
        }
        if (id.equals("unlocked_wineffects")){
            return "" + sw.getWineffects().size();
        }
        if (id.equals("bar_wineffects")){
            return "" + Utils.getProgressBar(sw.getWineffects().size(), plugin.getCos().getWinEffectsSize(), plugin.getCm().getProgressBarAmount());
        }
        if (id.equals("percentage_wineffects")){
            return "" + Utils.getProgressBar(sw.getWineffects().size(), plugin.getCos().getWinEffectsSize());
        }
        if (id.equals("unlocked_taunts")){
            return "" + sw.getTaunts().size();
        }
        if (id.equals("bar_taunts")){
            return "" + Utils.getProgressBar(sw.getTaunts().size(), plugin.getCos().getTauntsSize(), plugin.getCm().getProgressBarAmount());
        }
        if (id.equals("percentage_taunts")){
            return "" + Utils.getProgressBar(sw.getTaunts().size(), plugin.getCos().getTauntsSize());
        }
        if (id.equals("unlocked_partings")){
            return "" + sw.getPartings().size();
        }
        if (id.equals("bar_partings")){
            return "" + Utils.getProgressBar(sw.getPartings().size(), plugin.getCos().getPartingsSize(), plugin.getCm().getProgressBarAmount());
        }
        if (id.equals("percentage_partings")){
            return "" + Utils.getProgressBar(sw.getPartings().size(), plugin.getCos().getPartingsSize());
        }
        if (id.equals("unlocked_killsounds")){
            return "" + sw.getKillsounds().size();
        }
        if (id.equals("bar_killsounds")){
            return "" + Utils.getProgressBar(sw.getKillsounds().size(), plugin.getCos().getKillSoundsSize(), plugin.getCm().getProgressBarAmount());
        }
        if (id.equals("percentage_killsounds")){
            return "" + Utils.getProgressBar(sw.getKillsounds().size(), plugin.getCos().getKillSoundsSize());
        }
        if (id.equals("unlocked_killeffects")){
            return "" + sw.getKilleffects().size();
        }
        if (id.equals("bar_killeffects")){
            return "" + Utils.getProgressBar(sw.getKilleffects().size(), plugin.getCos().getKillEffectSize(), plugin.getCm().getProgressBarAmount());
        }
        if (id.equals("percentage_killeffects")){
            return "" + Utils.getProgressBar(sw.getKilleffects().size(), plugin.getCos().getKillEffectSize());
        }
        if (id.equals("unlocked_glass")){
            return "" + sw.getGlasses().size();
        }
        if (id.equals("bar_glass")){
            return "" + Utils.getProgressBar(sw.getGlasses().size(), plugin.getCos().getGlassesSize(), plugin.getCm().getProgressBarAmount());
        }
        if (id.equals("percentage_glass")){
            return "" + Utils.getProgressBar(sw.getGlasses().size(), plugin.getCos().getGlassesSize());
        }
        if (id.equals("unlocked_balloons")){
            return "" + sw.getBalloons().size();
        }
        if (id.equals("bar_balloons")){
            return "" + Utils.getProgressBar(sw.getBalloons().size(), plugin.getCos().getBalloonSize(), plugin.getCm().getProgressBarAmount());
        }
        if (id.equals("percentage_balloons")){
            return "" + Utils.getProgressBar(sw.getBalloons().size(), plugin.getCos().getBalloonSize());
        }
        if (id.equals("unlocked_kits")){
            return "" + sw.getKits().size();
        }
        if (id.equals("bar_kits")){
            return "" + Utils.getProgressBar(sw.getKits().size(), plugin.getKm().getKitsSize(), plugin.getCm().getProgressBarAmount());
        }
        if (id.equals("percentage_kits")){
            return "" + Utils.getProgressBar(sw.getKits().size(), plugin.getKm().getKitsSize());
        }
        if (id.equals("level_progress")){
            return "" + sw.getXp();
        }
        if (id.equals("level_max")){
            Level l = plugin.getLvl().getLevel(p);
            return "" + l.getLevelUp();
        }
        if (id.equals("level_bar")){
            Level l = plugin.getLvl().getLevel(p);
            int xp = sw.getXp() - l.getXp();
            int max = l.getLevelUp() - l.getXp();
            return Utils.getProgressBar(xp, max, plugin.getCm().getProgressBarAmount());
        }
        if (id.equals("level_prefix")){
            if (!sw.isShowLevel()){
                return "";
            }
            return plugin.getLvl().getLevelPrefix(p);
        }
        if (id.equals("level_nextprefix")){
            Level l = plugin.getLvl().getLevelByLevel(sw.getLevel() + 1);
            if (l == null) return "" + plugin.getLvl().getLevel(p).getPrefix();
            return l.getPrefix();
        }
        if (id.equals("elo")){
            return "" + sw.getElo();
        }
        if (id.equals("level")){
            return "" + sw.getLevel();
        }
        if (id.equals("cubelets")){
            return "" + sw.getCubelets();
        }
        if (id.equals("coins")){
            return "" + plugin.getAdm().getCoins(p);
        }
        if (id.equals("souls")){
            return "" + sw.getSouls();
        }
        if (id.equals("max_souls")){
            if (plugin.getIjm().isSoulWellInjection()){
                return "" + plugin.getIjm().getSoulwell().getSwm().getMaxSouls(sw.getSoulWellMax());
            }
            return "";
        }
        if (id.equals("xp")){
            return "" + sw.getXp();
        }
        if (id.startsWith("stat_")){
            // Format %usw_stat_<mode>_<type>%
            String[] s = id.split("_", 3);
            if (s.length < 3){
                return "§cError";
            }
            String mode = s[1].toUpperCase();
            String type = s[2].toUpperCase();
            if (!modes.contains(type)){
                return "§cError";
            }
            StatType stat = StatType.valueOf(type);
            return String.valueOf(sw.getStat(stat, mode));
        }
        if (id.startsWith("total_stat")){
            // Format %usw_total_stat_<type>%
            String[] s = id.split("_", 3);
            String type = s[2].toUpperCase();
            if (!modes.contains(type)){
                return "§cError";
            }
            StatType stat = StatType.valueOf(type);
            return String.valueOf(sw.getTotalStat(stat));
        }
        return null;
    }
    
}