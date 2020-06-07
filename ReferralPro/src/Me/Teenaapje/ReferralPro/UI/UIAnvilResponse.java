package Me.Teenaapje.ReferralPro.UI;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import Me.Teenaapje.ReferralPro.ConfigManager.ConfigManager;
import Me.Teenaapje.ReferralPro.UIElements.UIElement;
import Me.Teenaapje.ReferralPro.UIElements.UIElementManager;
import Me.Teenaapje.ReferralPro.Utils.Utils;

public class UIAnvilResponse {
	public static Inventory inv;
	public static String invName;
	public static int invRows;
	public static int invTotal;

	public static UIElement element;
	
	public static void Initialize() {
		invName = Utils.FormatString(null, ConfigManager.uIAnvilResponseTitle);
		
		inv = Bukkit.createInventory(null, invTotal); 
		
		element = UIElementManager.instance.GetElement("anvilresponse");
		
		invRows = element.rows;
		invTotal = invRows * 9;
		
		Utils.CreateFillers(inv, element.fillers);
		
		Utils.CreateButton(inv, element.GetButton("back"), Utils.FormatString(null, ConfigManager.uIButtonMainMenu));
		Utils.CreateButton(inv, element.GetButton("retry"), Utils.FormatString(null, ConfigManager.uIButtonRetry));
		Utils.CreateButton(inv, element.GetButton("close"), Utils.FormatString(null, ConfigManager.uIButtonClose));
	}
	
	public static Inventory GUI (String anvilMessage, int anvilCase) {
		Inventory toReturn = Bukkit.createInventory(null,  invTotal, invName);
		toReturn.setContents(inv.getContents());


		if (anvilCase == 0) {
			Utils.CreateItem(toReturn, "GREEN_STAINED_GLASS", 1, element.GetButton("anvilsucces").position, anvilMessage);
		} else {
			Utils.CreateItem(toReturn, "RED_STAINED_GLASS", 1, element.GetButton("anvilsucces").position, anvilMessage);
		}

        
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