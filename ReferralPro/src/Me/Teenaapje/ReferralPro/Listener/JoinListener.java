package Me.Teenaapje.ReferralPro.Listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import Me.Teenaapje.ReferralPro.ReferralCodeGen;
import Me.Teenaapje.ReferralPro.ReferralPro;

public class JoinListener implements Listener {
	
	public JoinListener () {
		Bukkit.getPluginManager().registerEvents(this, ReferralPro.Instance);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        
        String pUUID = p.getUniqueId().toString();
        
        if (!ReferralPro.Instance.db.PlayerExists(pUUID)) {
        	ReferralPro.Instance.db.CreatePlayer(pUUID, p.getName());        
        } else {
            // Tell the player that he/she has an request
        	if (ReferralPro.Instance.getConfig().getBoolean("notifyPlayerRequest") && ReferralPro.Instance.db.PlayerHasRequests(pUUID)) {
            	ReferralPro.Instance.PlayerHasBox(pUUID);
			}
        }
        
        if (ReferralPro.Instance.db.GetPlayerCode(pUUID) == null) {
    		ReferralPro.Instance.db.CreateCode(pUUID, ReferralCodeGen.GetShortID());
		}
        
        ReferralPro.Instance.db.UpdatePlayerIP(pUUID, p.getAddress().getHostName());
    }
}
