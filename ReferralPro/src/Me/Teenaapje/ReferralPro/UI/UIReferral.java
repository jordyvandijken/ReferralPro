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

public class UIReferral {
	public static Inventory inv;
	public static String invName;
	public static int invRows;
	public static int invTotal;
	
	public static UIElement element;

	
	public static void Initialize() {
		invName = Utils.FormatString(null, ConfigManager.instance.uIRefTitle);
		
		element = UIElementManager.instance.GetElement("referral");
		
		invRows = element.rows;
		invTotal = invRows * 9;
		
		inv = Bukkit.createInventory(null, invTotal); 

		Utils.CreateFillers(inv, element.fillers);
		
		Utils.CreateButton(inv, element.GetButton("invites"), Utils.FormatString(null, ConfigManager.instance.uIRefButtonInvite),
				  											  Utils.FormatString(null , ConfigManager.instance.uIRefButtonInviteExpl));


		Utils.CreateButton(inv, element.GetButton("refer"), Utils.FormatString(null, ConfigManager.instance.uIRefButtonRef));

		Utils.CreateButton(inv, element.GetButton("close"), Utils.FormatString(null, ConfigManager.instance.uIButtonClose));
	}
	
	public static Inventory GUI (Player p) {
		Inventory toReturn = Bukkit.createInventory(null,  invTotal, invName);
		toReturn.setContents(inv.getContents());

		Utils.CreatePlayerHead(toReturn, element.GetButton("playerhead").position, p.getName(), p.getName() + Utils.FormatString(p, ConfigManager.instance.uIProfiles), 

				   Utils.FormatString(p, ConfigManager.instance.uIRefUniqCode),
				   Utils.FormatString(null, ConfigManager.instance.uIRefCodeExpl));
		
		// the referral code
		if (!ReferralPro.Instance.db.PlayerReferrald(p.getUniqueId().toString())) {
			Utils.CreateButton(toReturn, element.GetButton("refercode"), Utils.FormatString(p, ConfigManager.instance.uIRefButtonRefCode));
		}
		
		
		if (ReferralPro.perms.has(p, "ReferralPro.Admin")) {
			Utils.CreateButton(toReturn, element.GetButton("adminpanel"), Utils.FormatString(p, ConfigManager.instance.uIRefButtonAdmin));
		}
		
		return toReturn;
	}
	
	public static void Clicked(Player p , int slot, ItemStack clicked, Inventory inv) {
		// Clicked on player referrals
		if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equals(Utils.RemoveButtonNormal(ConfigManager.instance.uIRefButtonInvite))) {
			p.openInventory(UIReferInvites.GUI(p, 1));
		} 
		
		// Clicked on refer a player
		else if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equals(Utils.RemoveButtonNormal(ConfigManager.instance.uIRefButtonRef))) {
			// Check for perms
			if (!(ReferralPro.perms.has(p, "ReferralPro.Invite") || ReferralPro.perms.has(p, "ReferralPro.Admin"))) {
				p.sendMessage("No permision");
				return;
			}
		
			UIAnvil.GUI(p, ConfigManager.instance.uIAnvilNameDefault);
		}
		
		// Use referral code
		else if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equals(Utils.RemoveButtonNormal(ConfigManager.instance.uIRefButtonRefCode))) {
			UIAnvilCode.GUI(p, ConfigManager.instance.uIAnvilCodeDefault);
		}
		
		// Use referral code
		else if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equals(Utils.RemoveButtonNormal(ConfigManager.instance.uIRefButtonAdmin))) {
			p.openInventory(UIAdmin.GUI(p));
		}
				
		// Go to profile
		else if (clicked.getType().toString().equalsIgnoreCase("PLAYER_HEAD")) {
			//p.openInventory(UIReferralPlayer.GUI(p));
			p.openInventory(UIProfile.GUI(p, p.getName()));
		}
				
		// Clicked on close
		else if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equals(Utils.RemoveButtonNormal(ConfigManager.instance.uIButtonClose))) {
			p.closeInventory();
		}
	}
}
