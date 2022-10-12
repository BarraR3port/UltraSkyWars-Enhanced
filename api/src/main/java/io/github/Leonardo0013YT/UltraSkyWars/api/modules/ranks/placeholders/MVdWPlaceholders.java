/*REMOVED DUE TO REPO SERVER DOWN
package io.github.Leonardo0013YT.UltraSkyWars.api.modules.ranks.placeholders;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.ranks.InjectionEloRank;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.ranks.ranks.EloRank;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import org.bukkit.entity.Player;

public class MVdWPlaceholders {
    
    private UltraSkyWarsApi plugin;
    private InjectionEloRank injectionEloRank;
    
    public MVdWPlaceholders(UltraSkyWarsApi plugin, InjectionEloRank injectionEloRank){
        this.plugin = plugin;
        this.injectionEloRank = injectionEloRank;
    }
    
    public void register(){
        PlaceholderAPI.registerPlaceholder(plugin, "usw_ranked_progress", e -> {
            Player p = e.getPlayer();
            SWPlayer sw = UltraSkyWarsApi.get().getDb().getSWPlayer(p);
            if (sw == null){
                return "";
            }
            EloRank er = injectionEloRank.getErm().getEloRank(p);
            int elo = sw.getElo() - er.getMin();
            int max = er.getMax() - er.getMin();
            return Utils.getProgressBar(elo, max, UltraSkyWarsApi.get().getCm().getProgressBarAmount());
        });
        PlaceholderAPI.registerPlaceholder(plugin, "usw_ranked_rank", e -> {
            Player p = e.getPlayer();
            return injectionEloRank.getErm().getEloRankChat(p);
        });
    }
}*/
