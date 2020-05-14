package Me.Teenaapje.ReferralPro.UI;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import Me.Teenaapje.ReferralPro.ConfigManager.ConfigManager;
import Me.Teenaapje.ReferralPro.Utils.Utils;

public class UIAnvilResponse {
	public static Inventory inv;
	public static String invName;
	public static int invRows = 3;
	public static int invTotal = invRows * 9;

	
	public static void Initialize() {
		invName = Utils.FormatString(null, ConfigManager.uIAnvilResponseTitle);
		
		inv = Bukkit.createInventory(null, invTotal); 
	}
	
	public static Inventory GUI (String anvilMessage, int anvilCase) {
		//Inventory toReturn = Bukkit.createInventory(null,  invTotal, anvilMessage);
		Inventory toReturn = Bukkit.createInventory(null,  invTotal, invName);
		
		Utils.CreateItem(toReturn, "OAK_DOOR", 1, invTotal - 9, Utils.FormatString(null, ConfigManager.uIButtonMainMenu));
		Utils.CreateItem(toReturn, "EXPERIENCE_BOTTLE", 1, invTotal - 5, Utils.FormatString(null, ConfigManager.uIButtonRetry));
		Utils.CreateItem(toReturn, "IRON_DOOR", 1, invTotal - 1, Utils.FormatString(null, ConfigManager.uIButtonClose));

		if (anvilCase == 0) {
			Utils.CreateItem(toReturn, "GREEN_STAINED_GLASS", 1, 13, anvilMessage);
		} else {
			Utils.CreateItem(toReturn, "RED_STAINED_GLASS", 1, 13, anvilMessage);
		}

        
		//toReturn.setContents(inv.getContents());
		return toReturn;
	}
	
	public static void Clicked(Player p , int slot, ItemStack clicked, Inventory inv) {
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.uIButtonRetry))) {
			UIAnvil.GUI(p, "Player");
		}
		else if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.uIButtonMainMenu))) {
			p.openInventory(UIReferral.GUI(p));
		}
		else if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.uIButtonClose))) {
			p.closeInventory();
		}
	}
}