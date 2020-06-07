package Me.Teenaapje.ReferralPro.UI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import Me.Teenaapje.ReferralPro.ReferralPro;
import Me.Teenaapje.ReferralPro.ConfigManager.ConfigManager;
import Me.Teenaapje.ReferralPro.UIElements.UIElement;
import Me.Teenaapje.ReferralPro.UIElements.UIElementManager;
import Me.Teenaapje.ReferralPro.Utils.Utils;
import net.wesjd.anvilgui.AnvilGUI;

public class UIAdmin {
	public static Inventory inv;
	public static String invName;
	public static int invRows;
	public static int invTotal;
	
	public static UIElement element;
	
	public static void Initialize() {
		invName = Utils.FormatString(null, ConfigManager.uIAdminTitle);
		
		element = UIElementManager.instance.GetElement("admin");
		
		invRows = element.rows;
		invTotal = invRows * 9;
		inv = Bukkit.createInventory(null, invTotal); 
		
		
		Utils.CreateFillers(inv, element.fillers);
		
		// resting/removeing players
		Utils.CreateButton(inv, element.GetButton("resetallplayers"), Utils.FormatString(null, ConfigManager.uIAdminResetAll));
		Utils.CreateButton(inv, element.GetButton("resetallplayercodes"), Utils.FormatString(null, ConfigManager.uIAdminRemoveAllCod));
		Utils.CreateButton(inv, element.GetButton("resetallplayerrequests"), Utils.FormatString(null, ConfigManager.uIAdminRemoveAllReq));
		Utils.CreateButton(inv, element.GetButton("resetallplayerrewards"), Utils.FormatString(null, ConfigManager.uIAdminRemoveAllRew));

		
		// lookUp player
		Utils.CreateButton(inv, element.GetButton("lookupplayer"), Utils.FormatString(null, ConfigManager.uIAdminLookUpPlayer));


		// default buttons
		Utils.CreateButton(inv, element.GetButton("back"), Utils.FormatString(null, ConfigManager.uIButtonGoBack));
		Utils.CreateButton(inv, element.GetButton("close"), Utils.FormatString(null, ConfigManager.uIButtonClose));
		
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
		//Utils.CreateItem(toReturn, "EGG", 1, 16, Utils.FormatString(null, "Enable plugin for non Admins"), Utils.FormatString(null, "This is " + (ReferralPro.Instance.getConfig().getBoolean("enablePlugin") ? "on" : "off")));

		
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
		else if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal(ConfigManager.uIAdminResetAll))) {			
			// Reset player
			ReferralPro.Instance.db.ResetAll();

			// reopen the profiles
			p.openInventory(UIAdmin.GUI(p));
		}
		
		// Clicked on Remove player codes
		else if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal(ConfigManager.uIAdminRemoveAllCod))) {
			// Remove player codes
			ReferralPro.Instance.db.RemoveAllCodes();
			
			// reopen the profiles
			p.openInventory(UIAdmin.GUI(p));
		}
		// Clicked on Remove all requests
		else if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal(ConfigManager.uIAdminRemoveAllReq))) {
			// Remove all requests
			ReferralPro.Instance.db.RemoveAllRequests();
			
			// reopen the profiles
			p.openInventory(UIAdmin.GUI(p));
		}
		// Clicked on Remove all rewards
		else if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal(ConfigManager.uIAdminRemoveAllRew))) {
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
		else if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal(ConfigManager.uIAdminLookUpPlayer))) {
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


