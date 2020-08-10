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
import Me.Teenaapje.ReferralPro.UIElements.UIElement;
import Me.Teenaapje.ReferralPro.UIElements.UIElementManager;
import Me.Teenaapje.ReferralPro.Utils.Utils;

public class UIReferInvites {
	public static Inventory inv;
	public static String invName;
	public static int invRows;
	public static int invTotal;

	public static UIElement element;
	
	public static void Initialize() {
		invName = Utils.FormatString(null, ConfigManager.instance.uIInvitesTitle);
		
		element = UIElementManager.instance.GetElement("refinvites");
		
		invRows = element.rows;
		invTotal = invRows * 9;
		
		inv = Bukkit.createInventory(null, invTotal); 
		
		
		Utils.CreateFillers(inv, element.fillers);

		Utils.CreateButton(inv, element.GetButton("back"), Utils.FormatString(null, ConfigManager.instance.uIButtonGoBack));
		Utils.CreateButton(inv, element.GetButton("close"), Utils.FormatString(null, ConfigManager.instance.uIButtonClose));

	}
	
	public static Inventory GUI (Player p, int page) {
		Inventory toReturn = Bukkit.createInventory(null,  invTotal, invName);
		toReturn.setContents(inv.getContents());

		ArrayList<Request> requests = ReferralPro.Instance.db.GetPlayerRequests(p.getUniqueId().toString(), page, invTotal - 9);
        
		if (requests.size() == 0) {
    		Utils.CreateButton(toReturn, element.GetButton("none"), Utils.FormatString(null, ConfigManager.instance.uIInvitesNone));
		} else {
			// when there are requests
			int index = 0;
			
			// show page number
    		Utils.CreateButton(toReturn, element.GetButton("page"), Utils.FormatString(null, ConfigManager.instance.uIButtonPage) + page);
			
    		// make scroll
    		if (requests.size() == 19) {
	        	Utils.CreatePlayerHead(toReturn, element.GetButton("next").position, "MHF_ArrowRight", Utils.FormatString(null, ConfigManager.instance.uIButtonNextPage));
			}
    		if (page > 1 ) {
	        	Utils.CreatePlayerHead(toReturn, element.GetButton("return").position, "MHF_ArrowLeft", Utils.FormatString(null, ConfigManager.instance.uIButtonReturnPage));
			}
    		
	        for (Request request : requests) {
	        	if (index >= (invRows - 1) * 9) {
					break;
				}
	        	
	        	String playerName = Bukkit.getOfflinePlayer(UUID.fromString(request.senderUUID)).getName();
	        	
	        	Utils.CreatePlayerHead(toReturn, index, playerName, playerName, Utils.FormatString(null, ConfigManager.instance.uIConfirmDidInvite));
	        	
	        	index++;
			}
		}
        
        
        
		return toReturn;
	}
	
	public static void Clicked(Player p , int slot, ItemStack clicked, Inventory inv) {
		if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.instance.uIButtonGoBack))) {
			p.openInventory(UIReferral.GUI(p));
		}
		
		else if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.instance.uIButtonClose))) {
			p.closeInventory();
		}
		
		else if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.instance.uIButtonBlock))) {
			p.closeInventory();
		}
		
		// Return page
		else if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.instance.uIButtonReturnPage))) {
			int page = 1;
			try {
				// the String to int conversion happens here
				page = Integer.parseInt(inv.getItem(invTotal - 5).getItemMeta().getDisplayName().replace(Utils.FormatString(null, ConfigManager.instance.uIButtonPage), ""));
			}
			catch (NumberFormatException nfe) {
				System.out.println("NumberFormatException: " + nfe.getMessage());
			}

			p.openInventory(UIReferInvites.GUI(p, page - 1));
		}
		
		// Next page
		else if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.instance.uIButtonNextPage))) {
			int page = 1;
			try {
				// the String to int conversion happens here
				page = Integer.parseInt(inv.getItem(invTotal - 5).getItemMeta().getDisplayName().replace(Utils.FormatString(null, ConfigManager.instance.uIButtonPage), ""));
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
			p.openInventory(UIReferralAccept.GUI(p, Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName())));
		}
	}
}
