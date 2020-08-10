package Me.Teenaapje.ReferralPro.UI;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import Me.Teenaapje.ReferralPro.ReferralPro;
import Me.Teenaapje.ReferralPro.ConfigManager.ConfigManager;
import Me.Teenaapje.ReferralPro.Listener.Reward;
import Me.Teenaapje.ReferralPro.UIElements.UIElement;
import Me.Teenaapje.ReferralPro.UIElements.UIElementManager;
import Me.Teenaapje.ReferralPro.Utils.Utils;

public class UIReferralAccept {
	public static Inventory inv;
	public static String invName;
	public static int invRows = 3;
	public static int invTotal = invRows * 9;
	public static UIElement element;
	
	public static void Initialize() {
		invName = Utils.FormatString(null, ConfigManager.instance.uIRefAcceptTitle);
		
		element = UIElementManager.instance.GetElement("refaccept");
		
		invRows = element.rows;
		invTotal = invRows * 9;
		
		inv = Bukkit.createInventory(null, invTotal); 

		
		Utils.CreateFillers(inv, element.fillers);
		
		Utils.CreateButton(inv, element.GetButton("back"), Utils.FormatString(null, ConfigManager.instance.uIButtonGoBack));
		Utils.CreateButton(inv, element.GetButton("close"), Utils.FormatString(null, ConfigManager.instance.uIButtonClose));
	}
	
	public static Inventory GUI (Player p, String playerSender) {
		Inventory toReturn = Bukkit.createInventory(null,  invTotal, invName);
		toReturn.setContents(inv.getContents());

		if (ReferralPro.perms.has(p, "ReferralPro.Block")) {
			Utils.CreateButton(toReturn, element.GetButton("block"), Utils.FormatString(null, ConfigManager.instance.uIButtonBlock));
		}
		

		Utils.CreateButton(toReturn, element.GetButton("yes"), Utils.FormatString(null, ConfigManager.instance.uIButtonYes));
		Utils.CreatePlayerHead(toReturn, element.GetButton("playerhead").position, playerSender, playerSender, Utils.FormatString(null, ConfigManager.instance.uIConfirmDidInvite));
		Utils.CreateButton(toReturn, element.GetButton("no"), Utils.FormatString(null, ConfigManager.instance.uIButtonNo));

        
		return toReturn;
	}
	
	@SuppressWarnings("deprecation")
	public static void Clicked(Player p , int slot, ItemStack clicked, Inventory inv) {
		if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.instance.uIButtonGoBack))) {
			p.openInventory(UIReferInvites.GUI(p, 1));
		}
		else if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.instance.uIButtonClose))) {
			p.closeInventory();
		}
		else if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.instance.uIButtonYes))) {
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
			
			// Update Leaderboard
			ReferralPro.Instance.leaderboard.UpdateLeaderboard();
		}
		else if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.instance.uIButtonNo))) {
			// get the player
			OfflinePlayer op = Bukkit.getOfflinePlayer(inv.getItem(13).getItemMeta().getDisplayName());
			// remove request from database
			ReferralPro.Instance.db.RemoveRequest(op.getUniqueId().toString(), p.getUniqueId().toString());
			
			// return
			p.openInventory(UIReferInvites.GUI(p, 1));
		}
		else if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.instance.uIButtonBlock))) {
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
