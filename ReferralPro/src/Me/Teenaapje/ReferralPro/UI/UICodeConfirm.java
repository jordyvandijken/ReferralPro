package Me.Teenaapje.ReferralPro.UI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import Me.Teenaapje.ReferralPro.ReferralPro;
import Me.Teenaapje.ReferralPro.ConfigManager.ConfigManager;
import Me.Teenaapje.ReferralPro.Listener.Reward;
import Me.Teenaapje.ReferralPro.Utils.Utils;

public class UICodeConfirm {
	public static Inventory inv;
	public static String invName;
	public static int invRows = 3;
	public static int invTotal = invRows * 9;

	
	public static void Initialize() {
		invName = Utils.FormatString(null, ConfigManager.uICodeConfirmTitle);
		
		inv = Bukkit.createInventory(null, invTotal); 
	}
	
	public static Inventory GUI (String playerSender) {
		Inventory toReturn = Bukkit.createInventory(null,  invTotal, invName);
		
		
		Utils.CreateItem(toReturn, "OAK_DOOR", 1, invTotal - 9, Utils.FormatString(null, ConfigManager.uIButtonMainMenu));
		Utils.CreateItem(toReturn, "EXPERIENCE_BOTTLE", 1, invTotal - 5, Utils.FormatString(null, ConfigManager.uIButtonRetry));
		Utils.CreateItem(toReturn, "IRON_DOOR", 1, invTotal - 1, Utils.FormatString(null, ConfigManager.uIButtonClose));

		Utils.CreateItem(toReturn, "GREEN_STAINED_GLASS", 1, 4, Utils.FormatString(null, ConfigManager.uIButtonYes));
		Utils.CreatePlayerHead(toReturn, 13, playerSender, playerSender, Utils.FormatString(null, ConfigManager.uIConfirmDidInvite));
	
        
		//toReturn.setContents(inv.getContents());
		return toReturn;
	}
	
	public static void Clicked(Player p , int slot, ItemStack clicked, Inventory inv) {
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.uIButtonMainMenu))) {
			p.openInventory(UIReferral.GUI(p));
		}
		else if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.uIButtonClose))) {
			p.closeInventory();
		}
		else if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.uIButtonYes))) {
			// get the player who send the refer
    	    //OfflinePlayer op = Bukkit.getOfflinePlayer(inv.getItem(13).getItemMeta().getDisplayName());
    	    
			
			String playerUUID = p.getUniqueId().toString();
			// the the other UUID
			String refUUID = ReferralPro.Instance.db.GetPlayersUUID(inv.getItem(13).getItemMeta().getDisplayName());
						
			
			if (ReferralPro.Instance.db.PlayerReferrald(refUUID)) {
				// Check if the player try to refer each other
				if (!ReferralPro.Instance.getConfig().getBoolean("CanReferEachOther") 
				  && ReferralPro.Instance.db.PlayerReferraldBy(playerUUID).equalsIgnoreCase(refUUID)) {	
					
					
			        p.openInventory(UIReferral.GUI(p));
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
			
			// return to profile
			p.openInventory(UIProfile.GUI(p, p.getName()));
		}
		else if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.uIButtonRetry))) {
			UIAnvilCode.GUI(p, "Code");
		}

	}
}