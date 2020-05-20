package Me.Teenaapje.ReferralPro.UI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import Me.Teenaapje.ReferralPro.ReferralPro;
import Me.Teenaapje.ReferralPro.ConfigManager.ConfigManager;
import Me.Teenaapje.ReferralPro.Utils.Utils;

public class UIReferral {
	public static Inventory inv;
	public static String invName;
	public static int invRows = 3;
	public static int invTotal = invRows * 9;
	
	public static void Initialize() {
		invName = Utils.FormatString(null, ConfigManager.uIRefTitle);
		
		inv = Bukkit.createInventory(null, invTotal); 
		
		// close
		Utils.CreateItem(inv, "IRON_DOOR", 1, invTotal - 1, Utils.FormatString(null, ConfigManager.uIButtonClose));
		
		// Ref invite
		Utils.CreateItem(inv, "SHULKER_SHELL", 1, 11, Utils.FormatString(null, ConfigManager.uIRefButtonInvite),
				  									  Utils.FormatString(null, ConfigManager.uIRefButtonInviteExpl));
		// Ref Ref
		Utils.CreateItem(inv, "NAME_TAG", 1, 15, Utils.FormatString(null, ConfigManager.uIRefButtonRef));

	}
	
	public static Inventory GUI (Player p) {
		Inventory toReturn = Bukkit.createInventory(null,  invTotal, invName);
		toReturn.setContents(inv.getContents());
		
		
		Utils.CreatePlayerHead(toReturn, 13, p.getName(), p.getName() + Utils.FormatString(p, ConfigManager.uIProfiles), 
				   Utils.FormatString(p, ConfigManager.uIRefUniqCode),
				   Utils.FormatString(null, ConfigManager.uIRefCodeExpl));
		
		// the referral code
		if (!ReferralPro.Instance.db.PlayerReferrald(p.getUniqueId().toString())) {
			Utils.CreateItem(toReturn, "NAME_TAG", 1, 4, Utils.FormatString(p, ConfigManager.uIRefButtonRefCode));
		}
		
		
		if (ReferralPro.perms.has(p, "ReferralPro.Admin")) {
			Utils.CreateItem(toReturn, "COMMAND_BLOCK", 1, invTotal - 9, Utils.FormatString(p, ConfigManager.uIRefButtonAdmin));
		}
		
		return toReturn;
	}
	
	public static void Clicked(Player p , int slot, ItemStack clicked, Inventory inv) {
		// Clicked on player referrals
		if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal(ConfigManager.uIRefButtonInvite))) {
			p.openInventory(UIReferInvites.GUI(p, 1));
		} 
		
		// Clicked on refer a player
		else if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal(ConfigManager.uIRefButtonRef))) {
			// Check for perms
			if (!(ReferralPro.perms.has(p, "ReferralPro.Invite") || ReferralPro.perms.has(p, "ReferralPro.Admin"))) {
				p.sendMessage("No permision");
				return;
			}
		
			UIAnvil.GUI(p, "Player");
		}
		
		// Use referral code
		else if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal(ConfigManager.uIRefButtonRefCode))) {
			UIAnvilCode.GUI(p, "Code");
		}
		
		// Use referral code
		else if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal(ConfigManager.uIRefButtonAdmin))) {
			p.openInventory(UIAdmin.GUI(p));
		}
				
		// Go to profile
		else if (clicked.getType().toString().equalsIgnoreCase("PLAYER_HEAD")) {
			//p.openInventory(UIReferralPlayer.GUI(p));
			p.openInventory(UIProfile.GUI(p, p.getName()));
		}
				
		// Clicked on close
		else if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal(ConfigManager.uIButtonClose))) {
			p.closeInventory();
		}
	}
}
