/* REMOVED DUE TO REPO SERVER DOWN
package io.github.Leonardo0013YT.UltraSkyWars.addons.nametags;

import com.nametagedit.plugin.NametagEdit;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.NametagAddon;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class NametagEditAddon implements NametagAddon {
    
    private HashMap<UUID, String> prefix = new HashMap<>();
    private HashMap<UUID, String> suffix = new HashMap<>();
    
    @Override
    public void addPlayerNameTag(Player p){
        prefix.put(p.getUniqueId(), NametagEdit.getApi().getNametag(p).getPrefix());
        suffix.put(p.getUniqueId(), NametagEdit.getApi().getNametag(p).getSuffix());
    }
    
    @Override
    public void removePlayerNameTag(Player p){
        prefix.remove(p.getUniqueId());
        suffix.remove(p.getUniqueId());
    }
    
    @Override
    public void resetPlayerNameTag(Player p){
        NametagEdit.getApi().clearNametag(p);
        NametagEdit.getApi().reloadNametag(p);
        NametagEdit.getApi().setNametag(p, prefix.get(p.getUniqueId()), suffix.get(p.getUniqueId()));
        removePlayerNameTag(p);
    }
    
    @Override
    public String getPrefix(Player p){
        if (!prefix.containsKey(p.getUniqueId())) return "";
        return prefix.get(p.getUniqueId());
    }
    
    @Override
    public String getSuffix(Player p){
        if (!suffix.containsKey(p.getUniqueId())) return "";
        return suffix.get(p.getUniqueId());
    }
    
}*/
