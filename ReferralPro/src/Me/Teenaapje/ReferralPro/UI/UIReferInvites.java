package Me.Teenaapje.ReferralPro.UI;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import Me.Teenaapje.ReferralPro.ReferralPro;
import Me.Teenaapje.ReferralPro.ConfigManager.ConfigManager;
import Me.Teenaapje.ReferralPro.Listener.Request;
import Me.Teenaapje.ReferralPro.Utils.Utils;

public class UIReferInvites {
	public static Inventory inv;
	public static String invName;
	public static int invRows = 3;
	public static int invTotal = invRows * 9;

	
	public static void Initialize() {
		invName = Utils.FormatString(null, ConfigManager.uIInvitesTitle);
		
		inv = Bukkit.createInventory(null, invTotal); 
	}
	
	public static Inventory GUI (Player p, int page) {
		Inventory toReturn = Bukkit.createInventory(null,  invTotal, invName);
		
		Utils.CreateItem(toReturn, "OAK_DOOR", 1, invTotal - 9, Utils.FormatString(null, ConfigManager.uIButtonGoBack));
		Utils.CreateItem(toReturn, "IRON_DOOR", 1, invTotal - 1, Utils.FormatString(null, ConfigManager.uIButtonClose));

		ArrayList<Request> requests = ReferralPro.Instance.db.GetPlayerRequests(p.getUniqueId().toString(), page);
        
		if (requests.size() == 0) {
    		Utils.CreateItem(toReturn, "BARRIER", 1, 13, Utils.FormatString(null, ConfigManager.uIInvitesNone));
		} else {
			// when there are requests
			int index = 0;
			
			// show page number
    		Utils.CreateItem(toReturn, "OAK_SIGN", 1, invTotal - 5, Utils.FormatString(null, ConfigManager.uIButtonPage) + page);
			
    		// make scroll
    		if (requests.size() == 19) {
	        	Utils.CreatePlayerHead(toReturn, invTotal - 4, "MHF_ArrowRight", Utils.FormatString(null, ConfigManager.uIButtonNextPage));
			}
    		if (page > 1 ) {
	        	Utils.CreatePlayerHead(toReturn, invTotal - 6, "MHF_ArrowLeft", Utils.FormatString(null, ConfigManager.uIButtonReturnPage));
			}
    		
	        for (Request request : requests) {
	        	if (index >= (invRows - 1) * 9) {
					break;
				}
	        	
	        	String playerName = Bukkit.getOfflinePlayer(UUID.fromString(request.senderUUID)).getName();
	        	
	        	Utils.CreatePlayerHead(toReturn, index, playerName, playerName, Utils.FormatString(null, ConfigManager.uIConfirmDidInvite));
	        	
	        	index++;
			}
		}
        
        
        
		//toReturn.setContents(inv.getContents());
		return toReturn;
	}
	
	public static void Clicked(Player p , int slot, ItemStack clicked, Inventory inv) {
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.uIButtonGoBack))) {
			p.openInventory(UIReferral.GUI(p));
		}
		
		else if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.uIButtonClose))) {
			p.closeInventory();
		}
		
		else if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.uIButtonBlock))) {
			p.closeInventory();
		}
		
		// Return page
		else if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.uIButtonReturnPage))) {
			int page = 1;
			try {
				// the String to int conversion happens here
				page = Integer.parseInt(inv.getItem(invTotal - 5).getItemMeta().getDisplayName().replace(Utils.FormatString(null, ConfigManager.uIButtonPage), ""));
			}
			catch (NumberFormatException nfe) {
				System.out.println("NumberFormatException: " + nfe.getMessage());
			}

			p.openInventory(UIReferInvites.GUI(p, page - 1));
		}
		
		// Next page
		else if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.uIButtonNextPage))) {
			int page = 1;
			try {
				// the String to int conversion happens here
				page = Integer.parseInt(inv.getItem(invTotal - 5).getItemMeta().getDisplayName().replace(Utils.FormatString(null, ConfigManager.uIButtonPage), ""));
			}
			catch (NumberFormatException nfe) {
				System.out.println("NumberFormatException: " + nfe.getMessage());
			}

			p.openInventory(UIReferInvites.GUI(p, page + 1));
		}
		
		else if (clicked.getType() == Material.PLAYER_HEAD) {
			// Check for perms
			if (!(ReferralPro.perms.has(p, "ReferralPro.Accept") || ReferralPro.perms.has(p, "ReferralPro.Admin"))) {
				p.sendMessage("No permision");
				return;
			}
			p.openInventory(UIReferralAccept.GUI(p, clicked.getItemMeta().getDisplayName()));
		}
	}
}
