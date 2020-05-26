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

public class UIBlocked {
	public static Inventory inv;
	public static String invName;
	public static int invRows = 3;
	public static int invTotal = invRows * 9;

	
	public static void Initialize() {
		invName = Utils.FormatString(null, ConfigManager.uIBlockedTitle);
		
		inv = Bukkit.createInventory(null, invTotal); 
		
		Utils.CreateItem(inv, "OAK_DOOR", 1, invTotal - 9, Utils.FormatString(null, ConfigManager.uIButtonGoBack));
		Utils.CreateItem(inv, "IRON_DOOR", 1, invTotal - 1, Utils.FormatString(null, ConfigManager.uIButtonClose));
	}
	
	public static Inventory GUI (Player p, int page) {
		Inventory toReturn = Bukkit.createInventory(null,  invTotal, invName);
		toReturn.setContents(inv.getContents());



		ArrayList<Request> requests = ReferralPro.Instance.db.GetPlayerBlocks(p.getUniqueId().toString(), page);
        
		if (requests.size() == 0) {
    		Utils.CreateItem(toReturn, "BARRIER", 1, 13, Utils.FormatString(p, ConfigManager.uIBlockedNone));
		} else {
			// when there are requests
			int index = 0;
			
			// show page number
    		Utils.CreateItem(toReturn, "OAK_SIGN", 1, invTotal - 5, Utils.FormatString(null, ConfigManager.uIProfiles) + page);
			
    		// make scroll
    		if (requests.size() == 19) {
	        	Utils.CreatePlayerHead(toReturn, invTotal - 4, "MHF_ArrowRight", Utils.FormatString(null, ConfigManager.uIButtonNextPage));
			}
    		if (page > 1 ) {
	        	Utils.CreatePlayerHead(toReturn, invTotal - 6, "MHF_ArrowLeft", Utils.FormatString(null, ConfigManager.uIButtonReturnPage));
			}
    		
    		//loop trough blocks
	        for (Request request : requests) {
	        	if (index >= (invRows - 1) * 9) {
					break;
				}
	        	
	        	String playerName = Bukkit.getPlayer(UUID.fromString(request.senderUUID)).getName();
	        	
	        	Utils.CreatePlayerHead(toReturn, index, playerName, playerName, Utils.FormatString(null, ConfigManager.uIBlockedIsBlocked), Utils.FormatString(null, ConfigManager.uIBlockedUnban));
	        	
	        	index++;
			}
		}
        
        
		return toReturn;
	}
	
	public static void Clicked(Player p , int slot, ItemStack clicked, Inventory inv) {
		// Return to main page
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.uIButtonGoBack))) {
			p.openInventory(UIProfile.GUI(p, p.getName()));
		}
		// Close inv
		else if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.uIButtonClose))) {
			p.closeInventory();
		}
		// Return page
		else if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.uIButtonReturnPage))) {
			int page = 1;
			try {
				// the String to int conversion happens here
				page = Integer.parseInt(inv.getItem(invTotal - 5).getItemMeta().getDisplayName().replace(Utils.RemoveButtonNormal(ConfigManager.uIProfiles), ""));
			}
			catch (NumberFormatException nfe) {
				System.out.println("NumberFormatException: " + nfe.getMessage());
			}

			p.openInventory(UIBlocked.GUI(p, page - 1));
		}
		// Next page
		else if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.uIButtonNextPage))) {
			int page = 1;
			try {
				// the String to int conversion happens here
				page = Integer.parseInt(inv.getItem(invTotal - 5).getItemMeta().getDisplayName().replace("Page ", ""));
			}
			catch (NumberFormatException nfe) {
				System.out.println("NumberFormatException: " + nfe.getMessage());
			}

			p.openInventory(UIBlocked.GUI(p, page + 1));
		}
		// Click on player head
		else if (clicked.getType() == Material.PLAYER_HEAD) {
			// remove the block
			ReferralPro.Instance.db.RemoveBlock(ReferralPro.Instance.db.GetPlayersUUID(clicked.getItemMeta().getDisplayName()), p.getUniqueId().toString());
			//ReferralPro.Instance.db.RemoveBlock(clicked.getItemMeta().getDisplayName(), ReferralPro.Instance.db.GetPlayersUUID(p.getUniqueId().toString()));
			
			p.openInventory(UIBlocked.GUI(p, 1));
		}
	}
}