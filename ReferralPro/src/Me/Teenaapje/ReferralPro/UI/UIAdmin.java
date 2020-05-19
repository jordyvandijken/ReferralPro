package Me.Teenaapje.ReferralPro.UI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import Me.Teenaapje.ReferralPro.ReferralPro;
import Me.Teenaapje.ReferralPro.ConfigManager.ConfigManager;
import Me.Teenaapje.ReferralPro.Utils.Utils;
import net.wesjd.anvilgui.AnvilGUI;

public class UIAdmin {
	public static Inventory inv;
	public static String invName;
	public static int invRows = 3;
	public static int invTotal = invRows * 9;
	
	public static void Initialize() {
		invName = Utils.FormatString(null, "Admin Panel");
		
		inv = Bukkit.createInventory(null, invTotal); 
		
		// resting/removeing players
		Utils.CreateItem(inv, "TNT", 1, 4, Utils.FormatString(null, "Reset all players"));
		Utils.CreateItem(inv, "TNT", 1, 12, Utils.FormatString(null, "Remove all player Codes"));
		Utils.CreateItem(inv, "TNT", 1, 14, Utils.FormatString(null, "Remove all requests"));
		Utils.CreateItem(inv, "TNT", 1, 22, Utils.FormatString(null, "Remove all player rewards"));

		
		// lookUp player
		Utils.CreateItem(inv, "NAME_TAG", 1, 13, Utils.FormatString(null, "Look up player"));

		
		
		// default buttons
		Utils.CreateItem(inv, "OAK_DOOR", 1, invTotal - 9, Utils.FormatString(null, ConfigManager.uIButtonGoBack));
		Utils.CreateItem(inv, "IRON_DOOR", 1, invTotal - 1, Utils.FormatString(null, ConfigManager.uIButtonClose));
		
	}
	
	public static Inventory GUI (Player p) {
		// just check if has perm
		if (!ReferralPro.perms.has(p, "ReferralPro.Admin")) {
			// Return to menu
			return UIReferral.GUI(p);
		}

		Inventory toReturn = Bukkit.createInventory(null,  invTotal, invName);
		toReturn.setContents(inv.getContents());

		//Utils.CreateItem(inv, "EGG", 1, 10, Utils.FormatString(null, ""));
		Utils.CreateItem(toReturn, "EGG", 1, 16, Utils.FormatString(null, "Enable plugin for non Admins"),
												 Utils.FormatString(null, "This is " + (ReferralPro.Instance.getConfig().getBoolean("enablePlugin") ? "on" : "off")));

		
		return toReturn;
	}
	
	public static void Clicked(Player p , int slot, ItemStack clicked, Inventory inv) {
		// Clicked back
		if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal(ConfigManager.uIButtonGoBack))) {
			p.openInventory(UIReferral.GUI(p));
		} 
		// Clicked close
		else if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal(ConfigManager.uIButtonClose))) {
			//p.openInventory(UIReferralPlayer.GUI(p));
			p.closeInventory();
		}		

		////////////////////////
		//
		//	Panel stuf
		//
		////////////////////////
		
		// Clicked on reset all player
		else if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal("Reset all players"))) {			
			// Reset player
			ReferralPro.Instance.db.ResetAll();

			// reopen the profiles
			p.openInventory(UIAdmin.GUI(p));
		}
		
		// Clicked on Remove player codes
		else if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal("Remove all player Codes"))) {
			// Remove player codes
			ReferralPro.Instance.db.RemoveAllCodes();
			
			// reopen the profiles
			p.openInventory(UIAdmin.GUI(p));
		}
		// Clicked on Remove all requests
		else if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal("Remove all requests"))) {
			// Remove all requests
			ReferralPro.Instance.db.RemoveAllRequests();
			
			// reopen the profiles
			p.openInventory(UIAdmin.GUI(p));
		}
		// Clicked on Remove all rewards
		else if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal("Remove all player rewards"))) {
			// Remove all requests
			ReferralPro.Instance.db.RemoveAllRewards();
			
			// reopen the profiles
			p.openInventory(UIAdmin.GUI(p));
		}
		// Clicked on Remove all rewards
		else if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal("Enable plugin for non Admins"))) {
			boolean option = ReferralPro.Instance.getConfig().getBoolean("enablePlugin");

			ReferralPro.Instance.getConfig().set("enablePlugin", !option);
			//ReferralPro.Instance.getConfig().options().copyDefaults(true);
			ReferralPro.Instance.saveConfig();
			
			ReferralPro.Instance.ReloadConfig();

			// reopen the profiles
			p.openInventory(UIAdmin.GUI(p));
		}
		
		// Clicked on Look up player
		else if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal("Look up player"))) {
			// Look up player
			LookUpPlayer(p);

		}
	}
	
	private static void LookUpPlayer(Player p) {
		new AnvilGUI.Builder()
	    .onClose(player -> {                      //called when the inventory is closing
//				        player.sendMessage("You closed the inventory.");
//				        return AnvilGUI.Response.close();
//				        player.closeInventory();
	    	// Not able to reopen last inv - is wonky
	    	// TODO need to try to open with a delay
	    })
	    .onComplete((player, text) -> {           //called when the inventory output slot is clicked
	    	player.openInventory(UIProfile.GUI(player, text));

	    	
	    	return AnvilGUI.Response.text("");			
	    })
	    //.preventClose()                           					//prevents the inventory from being closed
	    .text("player")														//sets the text the GUI should start with
	    .item(Utils.CreateItem(null, "PLAYER_HEAD", 1, 0, " ", " ")) 	//use a custom item for the first slot
	    //.item(new ItemStack(Material.PLAYER_HEAD)) 					//use a custom item for the first slot
	    .title(Utils.FormatString(null, "Look up who?"))   											//set the title of the GUI (only works in 1.14+)
	    .plugin(ReferralPro.Instance)             						//set the plugin instance
	    .open(p);            
		//toReturn.setContents(inv.getContents());
		return;
	}
}


