package Me.Teenaapje.ReferralPro.UI;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import Me.Teenaapje.ReferralPro.ReferralPro;
import Me.Teenaapje.ReferralPro.ConfigManager.ConfigManager;
import Me.Teenaapje.ReferralPro.Listener.Reward;
import Me.Teenaapje.ReferralPro.Utils.Utils;

public class UIReferralAccept {
	public static Inventory inv;
	public static String invName;
	public static int invRows = 3;
	public static int invTotal = invRows * 9;

	
	public static void Initialize() {
		invName = Utils.FormatString(null, ConfigManager.uIRefAcceptTitle);
		
		inv = Bukkit.createInventory(null, invTotal); 
	}
	
	public static Inventory GUI (Player p, String playerSender) {
		Inventory toReturn = Bukkit.createInventory(null,  invTotal, invName);
		
		Utils.CreateItem(toReturn, "OAK_DOOR", 1, invTotal - 9, Utils.FormatString(null, ConfigManager.uIButtonGoBack));
		Utils.CreateItem(toReturn, "IRON_DOOR", 1, invTotal - 1, Utils.FormatString(null, ConfigManager.uIButtonClose));

		if (ReferralPro.perms.has(p, "ReferralPro.Block")) {
			Utils.CreateItem(toReturn, "BARRIER", 1, invTotal - 5, Utils.FormatString(null, ConfigManager.uIButtonBlock));
		}
		

		Utils.CreateItem(toReturn, "GREEN_STAINED_GLASS", 1, 11, Utils.FormatString(null, ConfigManager.uIButtonYes));
		Utils.CreatePlayerHead(toReturn, 13, playerSender, playerSender, Utils.FormatString(null, ConfigManager.uIConfirmDidInvite));
		Utils.CreateItem(toReturn, "RED_STAINED_GLASS", 1, 15, Utils.FormatString(null, ConfigManager.uIButtonNo));
	
        
		//toReturn.setContents(inv.getContents());
		return toReturn;
	}
	
	@SuppressWarnings("deprecation")
	public static void Clicked(Player p , int slot, ItemStack clicked, Inventory inv) {
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.uIButtonGoBack))) {
			p.openInventory(UIReferInvites.GUI(p, 1));
		}
		else if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.uIButtonClose))) {
			p.closeInventory();
		}
		else if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.uIButtonYes))) {
			// get the player who send the refer

			String refUUID = ReferralPro.Instance.db.GetPlayersUUID(inv.getItem(13).getItemMeta().getDisplayName());

			String playerUUID = p.getUniqueId().toString();
    	    
    	    // remove from the databse
			ReferralPro.Instance.db.RemoveRequest(refUUID, playerUUID);
						
			if (ReferralPro.Instance.db.PlayerReferrald(playerUUID)) {
				// Check if the player try to refer each other
				if (!ReferralPro.Instance.getConfig().getBoolean("CanReferEachOther") &&
					ReferralPro.Instance.db.PlayerReferraldBy(playerUUID).equalsIgnoreCase(refUUID)) {	
			        p.openInventory(UIReferInvites.GUI(p, 1));
		            return;
				}	
			}
			
			
			// Set the Referral
			ReferralPro.Instance.db.ReferralPlayer(refUUID, playerUUID);
			
			
			
			// The player who got referred
			ReferralPro.Instance.db.CreateReward(new Reward(-999, playerUUID, refUUID, 0));
			
			// The player who referred someone
			ReferralPro.Instance.db.CreateReward(new Reward(-999, refUUID, playerUUID, 1));

			// Remove all open requests
			ReferralPro.Instance.db.RemovePlayerRequests(playerUUID);
			
			// return
			p.openInventory(UIReferInvites.GUI(p, 1));
		}
		else if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.uIButtonNo))) {
			// get the player
			OfflinePlayer op = Bukkit.getOfflinePlayer(inv.getItem(13).getItemMeta().getDisplayName());
			// remove request from database
			ReferralPro.Instance.db.RemoveRequest(op.getUniqueId().toString(), p.getUniqueId().toString());
			
			// return
			p.openInventory(UIReferInvites.GUI(p, 1));
		}
		else if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.uIButtonBlock))) {
			// get the player
			OfflinePlayer op = Bukkit.getOfflinePlayer(inv.getItem(13).getItemMeta().getDisplayName());

			// Create block in database
			ReferralPro.Instance.db.CreateBlock(op.getUniqueId().toString(), p.getUniqueId().toString());

			// remove request from database
			ReferralPro.Instance.db.RemoveRequest(op.getUniqueId().toString(), p.getUniqueId().toString());
			
			// return
			p.openInventory(UIReferInvites.GUI(p, 1));
		}
	}
}