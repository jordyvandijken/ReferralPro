package Me.Teenaapje.ReferralPro.UI;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import Me.Teenaapje.ReferralPro.ReferralCodeGen;
import Me.Teenaapje.ReferralPro.ReferralPro;
import Me.Teenaapje.ReferralPro.ConfigManager.ConfigManager;
import Me.Teenaapje.ReferralPro.UIElements.UIElement;
import Me.Teenaapje.ReferralPro.UIElements.UIElementManager;
import Me.Teenaapje.ReferralPro.Utils.Utils;

public class UIProfile {
	public static Inventory inv;
	public static String invName;
	public static int invRows;
	public static int invTotal;
	
	public static UIElement element;

	public static void Initialize() {
		invName = Utils.FormatString(null, ConfigManager.instance.uIProfileTitle);
		
		element = UIElementManager.instance.GetElement("profile");
		
		invRows = element.rows;
		invTotal = invRows * 9;
		
		inv = Bukkit.createInventory(null, invTotal); 

		Utils.CreateFillers(inv, element.fillers);

		Utils.CreateButton(inv, element.GetButton("back"), Utils.FormatString(null, ConfigManager.instance.uIButtonGoBack));
		Utils.CreateButton(inv, element.GetButton("close"), Utils.FormatString(null, ConfigManager.instance.uIButtonClose));
	}
	
	@SuppressWarnings("deprecation")
	public static Inventory GUI (Player p, String pProfile) {
		if (!element.enable) {
			return UIReferral.GUI(p); 
		}
		//long startTime = System.nanoTime();
		
		Inventory toReturn = Bukkit.createInventory(null,  invTotal, invName);
		toReturn.setContents(inv.getContents());
		
		
		// get the player from the profile
		String profileUUID = ReferralPro.Instance.db.GetPlayersUUID(pProfile);

		OfflinePlayer op = null;
		
		if (profileUUID != null) {
			//convert
			UUID pUUID = UUID.fromString(profileUUID);
			// get offline p
			op = Bukkit.getOfflinePlayer(pUUID);
		} else {
			op = Bukkit.getOfflinePlayer(pProfile);
		}
		
		// the total of player referrals
		Utils.CreateButton(toReturn, element.GetButton("profiletotal"), Utils.FormatString(op, ConfigManager.instance.uIProfileTotal), Utils.FormatString(null, ConfigManager.instance.uIProfileTotalExpl));

		
		boolean playerExists = ReferralPro.Instance.db.PlayerExistsName(pProfile);
		
		// check if the player has joined the server once
		if (playerExists) {
			// the player
			Utils.CreatePlayerHead(toReturn, element.GetButton("playerhead").position, pProfile, pProfile + Utils.FormatString(op, ConfigManager.instance.uIProfiles),
																 											//Utils.FormatString(op, ConfigManager.instance.uIProfileIsRefed), 
																 											Utils.FormatString(op, ConfigManager.instance.uIProfileRefedBy));
		} else {
			// the player
			Utils.CreatePlayerHead(toReturn, element.GetButton("playerhead").position, pProfile, pProfile + Utils.FormatString(op, ConfigManager.instance.uIProfiles), 
																											Utils.FormatString(null, ConfigManager.instance.uIProfileNonExist));
		}

		
		
		if (p.getName().equalsIgnoreCase(pProfile)) {
			// the list of rewards
			Utils.CreateButton(toReturn, element.GetButton("rewards"), Utils.FormatString(null, ConfigManager.instance.uIProfileButtonReward), 
													   Utils.FormatString(null, ConfigManager.instance.uIProfileButtonRewardExpl));
			
			// the list of blocked 
			Utils.CreateButton(toReturn, element.GetButton("blocked"), Utils.FormatString(null, ConfigManager.instance.uIProfileButtonBlocked),
														 Utils.FormatString(null, ConfigManager.instance.uIProfileButtonBlockedExpl));
			
			
			
			// make the text change if player can receive reward
			if (ReferralPro.Instance.rewards.NextMileReward(p.getUniqueId().toString()) == 0 && ReferralPro.Instance.getConfig().getBoolean("enableMilestone")) {
				// the list of mileStone Rewards
				Utils.CreateButton(toReturn, element.GetButton("milerewards"), Utils.FormatString(null, ConfigManager.instance.uIProfileButtonMilestone),
								Utils.FormatString(op, ConfigManager.instance.uIProfileNextMilestone),
								Utils.FormatString(op, ConfigManager.instance.uIProfiletooNextMilestone),
								Utils.FormatString(op, ConfigManager.instance.uIButtonclickReward),
								Utils.FormatString(op, ConfigManager.instance.uIButtonEnoughSpace));
			} else if (ReferralPro.Instance.getConfig().getBoolean("enableMilestone")){
				// the list of mileStone Rewards
				Utils.CreateButton(toReturn, element.GetButton("milerewards"), Utils.FormatString(null, ConfigManager.instance.uIProfileButtonMilestone),
								Utils.FormatString(op, ConfigManager.instance.uIProfileNextMilestone),
								Utils.FormatString(op, ConfigManager.instance.uIProfiletooNextMilestone));
			}
		}	

		
		if (ReferralPro.perms.has(p, "ReferralPro.Admin") && playerExists) {
			// Reset Player
			Utils.CreateButton(toReturn, element.GetButton("resetallplayers"), Utils.FormatString(null, ConfigManager.instance.uIAdminPReset), 
																		 Utils.FormatString(null, "&cClicking on this WILL reset the player in the database"));
			// Remove Player Referrals 
			Utils.CreateButton(toReturn, element.GetButton("resetallplayercodes"), Utils.FormatString(null, ConfigManager.instance.uIAdminpRemoveRew), 
																		 Utils.FormatString(null, "&cClicking on this WILL remove the player referrals in the database"), 
																		 Utils.FormatString(null, "&cThis will not reset the milestone rewards"));
			// Reset player Code
			Utils.CreateButton(toReturn, element.GetButton("resetallplayerrequests"), Utils.FormatString(null, ConfigManager.instance.uIAdminpResetCode), 
													 					 Utils.FormatString(null, "&cClicking on this WILL reset the player his code"));
			// Remove rewards
			Utils.CreateButton(toReturn, element.GetButton("resetallplayerrewards"), Utils.FormatString(null, ConfigManager.instance.uIAdminpRemoveRew), 
													 					 Utils.FormatString(null, "&cClicking on this WILL remove every reward that's connected to this player"));
			
			
			// the list of blocked 
			Utils.CreateButton(toReturn, element.GetButton("adminpanel"), Utils.FormatString(null, ConfigManager.instance.uIRefButtonAdmin));
		}
		
		// timing 
		//long endTime = System.nanoTime();

		// get difference of two nanoTime values
		//long timeElapsed = endTime - startTime;

		//System.out.println("Execution time in nanoseconds  : " + timeElapsed);

		//System.out.println("Execution time in milliseconds : " + timeElapsed / 1000000);
		return toReturn;
	}
	
	public static void Clicked(Player p , int slot, ItemStack clicked, Inventory inv) {
		// Clicked back		
		if (Utils.RemoveButtonNormal(Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName())).equals(Utils.RemoveButtonNormal(ConfigManager.instance.uIButtonGoBack))) {
			p.openInventory(UIReferral.GUI(p));
		} 
		
		// Clicked close
		else if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equals(Utils.RemoveButtonNormal(ConfigManager.instance.uIButtonClose))) {
			//p.openInventory(UIReferralPlayer.GUI(p));
			p.closeInventory();
		}
		
		// Clicked on blocked list
		else if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equals(Utils.RemoveButtonNormal(ConfigManager.instance.uIProfileButtonBlocked))) {
			p.openInventory(UIBlocked.GUI(p, 1));
		}
		
		// Clicked on Reward list
		else if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equals(Utils.RemoveButtonNormal(ConfigManager.instance.uIProfileButtonReward))) {
			p.openInventory(UIRewards.GUI(p, 1));
		}
		
		// Clicked on milestone Reward
		else if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equals(Utils.RemoveButtonNormal(ConfigManager.instance.uIProfileButtonMilestone))) {
			// check if true
			if (ReferralPro.Instance.rewards.NextMileReward(p.getUniqueId().toString()) != 0) {
				return;
			}
			// give rewards if new reward left
			ReferralPro.Instance.rewards.GiveMileStoneRewards(p);
			
			// reopen the profiles
			p.openInventory(UIProfile.GUI(p, p.getName()));
		}
		
		//////////////////////////////////
		//
		//	Admin stuff
		//
		//////////////////////////////////
				
		// Clicked on reset player
		else if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equals(Utils.RemoveButtonNormal(ConfigManager.instance.uIAdminPReset))) {
			String pName = inv.getItem(13).getItemMeta().getDisplayName().replace(Utils.ColorCode(ConfigManager.instance.uIProfiles), "");
			
			// Reset player
			ReferralPro.Instance.db.PlayerReset(ReferralPro.Instance.db.GetPlayersUUID(pName));

			// reopen the profiles
			p.openInventory(UIProfile.GUI(p, pName));
		}
		//////////////////////////////////
		// Clicked on remove refers
		//////////////////////////////////
		else if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equals(Utils.RemoveButtonNormal(ConfigManager.instance.uIAdminpRemoveRew))) {
			String pName = inv.getItem(13).getItemMeta().getDisplayName().replace(Utils.ColorCode(ConfigManager.instance.uIProfiles), "");
			
			// Remove player
			ReferralPro.Instance.db.PlayerResetReferral(ReferralPro.Instance.db.GetPlayersUUID(pName));
			
			// reopen the profiles
			p.openInventory(UIProfile.GUI(p, pName));
		}
		//////////////////////////////////
		// Clicked on Reset Player Code
		//////////////////////////////////
		else if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equals(Utils.RemoveButtonNormal(ConfigManager.instance.uIAdminpResetCode))) {
			String pName = inv.getItem(13).getItemMeta().getDisplayName().replace(Utils.ColorCode(ConfigManager.instance.uIProfiles), "");
			
			String pUUID = ReferralPro.Instance.db.GetPlayersUUID(pName);
			
    		ReferralPro.Instance.db.UpdateCode(pUUID, ReferralCodeGen.GetShortID());

			// reopen the profiles
			p.openInventory(UIProfile.GUI(p, pName));
		}
		//////////////////////////////////
		// Clicked on Remove Player Rewards
		//////////////////////////////////
		else if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equals(Utils.RemoveButtonNormal(ConfigManager.instance.uIAdminpRemoveRew))) {
			String pName = inv.getItem(13).getItemMeta().getDisplayName().replace(Utils.ColorCode(ConfigManager.instance.uIProfiles), "");
			
			String pUUID = ReferralPro.Instance.db.GetPlayersUUID(pName);
			
			// Remove player rewards
			ReferralPro.Instance.db.RemoveRewardUUID(pUUID);
			
			// reopen the profiles
			p.openInventory(UIProfile.GUI(p, pName));
		}
		//////////////////////////////////
		// Clicked on Admin panel
		//////////////////////////////////
		else if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equals(Utils.RemoveButtonNormal(ConfigManager.instance.uIRefButtonAdmin))) {
			p.openInventory(UIAdmin.GUI(p));
		}
		
	}
}


