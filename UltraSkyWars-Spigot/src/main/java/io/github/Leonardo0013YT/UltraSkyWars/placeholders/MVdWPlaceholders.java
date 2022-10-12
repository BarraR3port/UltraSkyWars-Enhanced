/* REMOVED DUE TO REPO SERVER DOWN
package io.github.Leonardo0013YT.UltraSkyWars.placeholders;

import be.maximvdw.placeholderapi.PlaceholderAPI;

import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.StatType;
import io.github.Leonardo0013YT.UltraSkyWars.api.objects.Level;
import io.github.Leonardo0013YT.UltraSkyWars.api.objects.PrestigeIcon;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import org.bukkit.entity.Player;

public class MVdWPlaceholders {
    
    public void register(){
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        PlaceholderAPI.registerPlaceholder(plugin, "usw_players_solo", e -> "" + plugin.getGm().getGameSize("solo"));
        PlaceholderAPI.registerPlaceholder(plugin, "usw_players_team", e -> "" + plugin.getGm().getGameSize("team"));
        PlaceholderAPI.registerPlaceholder(plugin, "usw_players_ranked", e -> "" + plugin.getGm().getGameSize("ranked"));
        PlaceholderAPI.registerPlaceholder(plugin, "usw_total_kits", e -> "" + plugin.getKm().getKitsSize());
        PlaceholderAPI.registerPlaceholder(plugin, "usw_total_balloons", e -> "" + plugin.getCos().getBalloonSize());
        PlaceholderAPI.registerPlaceholder(plugin, "usw_total_partings", e -> "" + plugin.getCos().getPartingsSize());
        PlaceholderAPI.registerPlaceholder(plugin, "usw_total_killsounds", e -> "" + plugin.getCos().getKillSoundsSize());
        PlaceholderAPI.registerPlaceholder(plugin, "usw_total_killeffects", e -> "" + plugin.getCos().getKillEffectSize());
        PlaceholderAPI.registerPlaceholder(plugin, "usw_total_trails", e -> "" + plugin.getCos().getTrailsSize());
        PlaceholderAPI.registerPlaceholder(plugin, "usw_total_wineffects", e -> "" + plugin.getCos().getWinEffectsSize());
        PlaceholderAPI.registerPlaceholder(plugin, "usw_total_windances", e -> "" + plugin.getCos().getWinDancesSize());
        PlaceholderAPI.registerPlaceholder(plugin, "usw_total_glass", e -> "" + plugin.getCos().getGlassesSize());
        PlaceholderAPI.registerPlaceholder(plugin, "usw_total_taunts", e -> "" + plugin.getCos().getTauntsSize());
        PlaceholderAPI.registerPlaceholder(plugin, "usw_ranking", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + sw.getRanking();
            }
            return "";
        });
        for ( StatType st : StatType.values() ){
            for ( String mode : plugin.getGm().getModes() ){
                PlaceholderAPI.registerPlaceholder(plugin, "usw_stat_" + mode + "_" + st.name(), e -> {
                    Player p = e.getPlayer();
                    SWPlayer sw = plugin.getDb().getSWPlayer(p);
                    if (sw != null){
                        return "" + sw.getStat(st, mode);
                    }
                    return "";
                });
            }
            PlaceholderAPI.registerPlaceholder(plugin, "usw_total_stat_" + st.name(), e -> {
                Player p = e.getPlayer();
                SWPlayer sw = plugin.getDb().getSWPlayer(p);
                if (sw != null){
                    return "" + sw.getTotalStat(st);
                }
                return "";
            });
        }
        PlaceholderAPI.registerPlaceholder(plugin, "usw_xp", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + sw.getXp();
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_souls", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + sw.getSouls();
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_max_souls", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (plugin.getIjm().isSoulWellInjection()){
                return "" + plugin.getIjm().getSoulwell().getSwm().getMaxSouls(sw.getSoulWellMax());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_coins", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + plugin.getAdm().getCoins(p);
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_cubelets", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + sw.getCubelets();
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_level", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + sw.getLevel();
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_elo", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + sw.getElo();
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_prestige_icon", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                if (!sw.isShowLevel()){
                    return "";
                }
                PrestigeIcon pi = plugin.getLvl().getPrestige().get(sw.getPrestigeIcon());
                if (pi != null){
                    return pi.getPrefix();
                }
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_level_prefix", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                if (!sw.isShowLevel()){
                    return "";
                }
                return plugin.getLvl().getLevelPrefix(p);
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_level_progress", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + sw.getXp();
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_level_bar", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                Level l = plugin.getLvl().getLevel(p);
                int xp = sw.getXp() - l.getXp();
                int max = l.getLevelUp() - l.getXp();
                return Utils.getProgressBar(xp, max, plugin.getCm().getProgressBarAmount());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_bar_kits", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + Utils.getProgressBar(sw.getKits().size(), plugin.getKm().getKitsSize(), plugin.getCm().getProgressBarAmount());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_percentage_kits", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + Utils.getProgressBar(sw.getKits().size(), plugin.getKm().getKitsSize());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_unlocked_kits", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + sw.getKits().size();
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_bar_balloons", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + Utils.getProgressBar(sw.getBalloons().size(), plugin.getCos().getBalloonSize(), plugin.getCm().getProgressBarAmount());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_percentage_balloons", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + Utils.getProgressBar(sw.getBalloons().size(), plugin.getCos().getBalloonSize());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_unlocked_balloons", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + sw.getBalloons().size();
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_bar_glass", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + Utils.getProgressBar(sw.getGlasses().size(), plugin.getCos().getGlassesSize(), plugin.getCm().getProgressBarAmount());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_percentage_glass", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + Utils.getProgressBar(sw.getGlasses().size(), plugin.getCos().getGlassesSize());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_bar_taunts", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + Utils.getProgressBar(sw.getTaunts().size(), plugin.getCos().getTauntsSize(), plugin.getCm().getProgressBarAmount());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_percentage_taunts", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + Utils.getProgressBar(sw.getTaunts().size(), plugin.getCos().getTauntsSize());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_unlocked_partings", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + sw.getPartings().size();
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_bar_partings", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + Utils.getProgressBar(sw.getPartings().size(), plugin.getCos().getPartingsSize(), plugin.getCm().getProgressBarAmount());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_percentage_partings", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + Utils.getProgressBar(sw.getPartings().size(), plugin.getCos().getPartingsSize());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_unlocked_killsounds", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + sw.getKillsounds().size();
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_bar_killsounds", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + Utils.getProgressBar(sw.getKillsounds().size(), plugin.getCos().getKillSoundsSize(), plugin.getCm().getProgressBarAmount());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_percentage_killsounds", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + Utils.getProgressBar(sw.getKillsounds().size(), plugin.getCos().getKillSoundsSize());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_unlocked_killeffects", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + sw.getKilleffects().size();
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_bar_killeffects", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + Utils.getProgressBar(sw.getKilleffects().size(), plugin.getCos().getKillEffectSize(), plugin.getCm().getProgressBarAmount());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_percentage_killeffects", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + Utils.getProgressBar(sw.getKilleffects().size(), plugin.getCos().getKillEffectSize());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_unlocked_glass", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + sw.getGlasses().size();
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_bar_windances", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + Utils.getProgressBar(sw.getWindances().size(), plugin.getCos().getWinDancesSize(), plugin.getCm().getProgressBarAmount());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_percentage_windances", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + Utils.getProgressBar(sw.getWindances().size(), plugin.getCos().getWinDancesSize());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_unlocked_wineffects", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + sw.getWineffects().size();
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_bar_wineffects", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + Utils.getProgressBar(sw.getWineffects().size(), plugin.getCos().getWinEffectsSize(), plugin.getCm().getProgressBarAmount());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_percentage_wineffects", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + Utils.getProgressBar(sw.getWineffects().size(), plugin.getCos().getWinEffectsSize());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_unlocked_taunts", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + sw.getTaunts().size();
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_selected_trail", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return plugin.getCos().getSelected(sw.getTrail(), plugin.getCos().getTrails());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_selected_windance", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return plugin.getCos().getSelected(sw.getWinDance(), plugin.getCos().getWinDance());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_selected_wineffect", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return plugin.getCos().getSelected(sw.getWinEffect(), plugin.getCos().getWinEffects());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_selected_taunt", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return plugin.getCos().getSelected(sw.getTaunt(), plugin.getCos().getTaunts());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_selected_parting", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return plugin.getCos().getSelected(sw.getParting(), plugin.getCos().getPartings());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_selected_killsound", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return plugin.getCos().getSelected(sw.getKillSound(), plugin.getCos().getKillSounds());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_selected_killeffect", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return plugin.getCos().getSelected(sw.getKillEffect(), plugin.getCos().getKillEffect());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_selected_glass", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return plugin.getCos().getSelected(sw.getGlass(), plugin.getCos().getGlasses());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_selected_balloon", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return plugin.getCos().getSelected(sw.getBalloon(), plugin.getCos().getBalloons());
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_selected_solo_kit", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return plugin.getKm().getSelected(sw, "SOLO");
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_selected_team_kit", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return plugin.getKm().getSelected(sw, "TEAM");
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_selected_ranked_kit", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return plugin.getKm().getSelected(sw, "RANKED");
            }
            return "";
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_unlocked_trails", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw != null){
                return "" + sw.getTrails().size();
            }
            return "";
        });
    }
    
}*/
