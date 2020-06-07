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
		invName = Utils.FormatString(null, ConfigManager.uIProfileTitle);
		
		element = UIElementManager.instance.GetElement("profile");
		
		invRows = element.rows;
		invTotal = invRows * 9;
		
		inv = Bukkit.createInventory(null, invTotal); 

		
		Utils.CreateFillers(inv, element.fillers);

		Utils.CreateButton(inv, element.GetButton("back"), Utils.FormatString(null, ConfigManager.uIButtonGoBack));
		Utils.CreateButton(inv, element.GetButton("close"), Utils.FormatString(null, ConfigManager.uIButtonClose));
		
	}
	
	@SuppressWarnings("deprecation")
	public static Inventory GUI (Player p, String pProfile) {
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
		Utils.CreateButton(toReturn, element.GetButton("profiletotal"), Utils.FormatString(op, ConfigManager.uIProfileTotal), Utils.FormatString(null, ConfigManager.uIProfileTotalExpl));

		
		boolean playerExists = ReferralPro.Instance.db.PlayerExistsName(pProfile);
		
		// check if the player has joined the server once
		if (playerExists) {
			// the player
			Utils.CreatePlayerHead(toReturn, element.GetButton("playerhead").position, pProfile, pProfile + Utils.FormatString(op, ConfigManager.uIProfiles),
																 											Utils.FormatString(op, ConfigManager.uIProfileIsRefed), 
																 											Utils.FormatString(op, ConfigManager.uIProfileRefedBy));
		} else {
			// the player
			Utils.CreatePlayerHead(toReturn, element.GetButton("playerhead").position, pProfile, pProfile + Utils.FormatString(op, ConfigManager.uIProfiles), Utils.FormatString(null, ConfigManager.uIProfileNonExist));

		}

		
		
		if (p.getName().equalsIgnoreCase(pProfile)) {
			// the list of rewards
			Utils.CreateButton(toReturn, element.GetButton("rewards"), Utils.FormatString(null, ConfigManager.uIProfileButtonReward), 
													   Utils.FormatString(null, ConfigManager.uIProfileButtonRewardExpl));
			
			// the list of blocked 
			Utils.CreateButton(toReturn, element.GetButton("blocked"), Utils.FormatString(null, ConfigManager.uIProfileButtonBlocked),
														 Utils.FormatString(null, ConfigManager.uIProfileButtonBlockedExpl));
			
			
			
			// make the text change if player can receive reward
			if (ReferralPro.Instance.rewards.NextMileReward(p.getUniqueId().toString()) == 0 && ReferralPro.Instance.getConfig().getBoolean("enableMilestone")) {
				// the list of mileStone Rewards
				Utils.CreateButton(toReturn, element.GetButton("milerewards"), Utils.FormatString(null, ConfigManager.uIProfileButtonMilestone),
								Utils.FormatString(op, ConfigManager.uIProfileNextMilestone),
								Utils.FormatString(op, ConfigManager.uIProfiletooNextMilestone),
								Utils.FormatString(op, ConfigManager.uIButtonclickReward),
								Utils.FormatString(op, ConfigManager.uIButtonEnoughSpace));
			} else if (ReferralPro.Instance.getConfig().getBoolean("enableMilestone")){
				// the list of mileStone Rewards
				Utils.CreateButton(toReturn, element.GetButton("milerewards"), Utils.FormatString(null, ConfigManager.uIProfileButtonMilestone),
								Utils.FormatString(op, ConfigManager.uIProfileNextMilestone),
								Utils.FormatString(op, ConfigManager.uIProfiletooNextMilestone));
			}
		}	

		
		if (ReferralPro.perms.has(p, "ReferralPro.Admin") && playerExists) {
			// Reset Player
			Utils.CreateButton(toReturn, element.GetButton("resetallplayers"), Utils.FormatString(null, ConfigManager.uIAdminPReset), 
																		 Utils.FormatString(null, "&cClicking on this WILL reset the player in the database"));
			// Remove Player Referrals 
			Utils.CreateButton(toReturn, element.GetButton("resetallplayercodes"), Utils.FormatString(null, ConfigManager.uIAdminpRemoveRew), 
																		 Utils.FormatString(null, "&cClicking on this WILL remove the player referrals in the database"), 
																		 Utils.FormatString(null, "&cThis will not reset the milestone rewards"));
			// Reset player Code
			Utils.CreateButton(toReturn, element.GetButton("resetallplayerrequests"), Utils.FormatString(null, ConfigManager.uIAdminpResetCode), 
													 					 Utils.FormatString(null, "&cClicking on this WILL reset the player his code"));
			// Remove rewards
			Utils.CreateButton(toReturn, element.GetButton("resetallplayerrewards"), Utils.FormatString(null, ConfigManager.uIAdminpRemoveRew), 
													 					 Utils.FormatString(null, "&cClicking on this WILL remove every reward that's connected to this player"));
			
			
			// the list of blocked 
			Utils.CreateButton(toReturn, element.GetButton("adminpanel"), Utils.FormatString(null, ConfigManager.uIRefButtonAdmin));
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
		if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal(ConfigManager.uIButtonGoBack))) {
			p.openInventory(UIReferral.GUI(p));
		} 
		
		// Clicked close
		else if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal(ConfigManager.uIButtonClose))) {
			//p.openInventory(UIReferralPlayer.GUI(p));
			p.closeInventory();
		}
		
		// Clicked on blocked list
		else if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal(ConfigManager.uIProfileButtonBlocked))) {
			p.openInventory(UIBlocked.GUI(p, 1));
		}
		
		// Clicked on Reward list
		else if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal(ConfigManager.uIProfileButtonReward))) {
			p.openInventory(UIRewards.GUI(p, 1));
		}
		
		// Clicked on milestone Reward
		else if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal(ConfigManager.uIProfileButtonMilestone))) {
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
		else if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal(ConfigManager.uIAdminPReset))) {
			String pName = inv.getItem(13).getItemMeta().getDisplayName().replace(Utils.ColorCode(ConfigManager.uIProfiles), "");
			
			// Reset player
			ReferralPro.Instance.db.PlayerReset(ReferralPro.Instance.db.GetPlayersUUID(pName));

			// reopen the profiles
			p.openInventory(UIProfile.GUI(p, pName));
		}
		//////////////////////////////////
		// Clicked on remove refers
		//////////////////////////////////
		else if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal(ConfigManager.uIAdminpRemoveRew))) {
			String pName = inv.getItem(13).getItemMeta().getDisplayName().replace(Utils.ColorCode(ConfigManager.uIProfiles), "");
			
			// Remove player
			ReferralPro.Instance.db.PlayerResetReferral(ReferralPro.Instance.db.GetPlayersUUID(pName));
			
			// reopen the profiles
			p.openInventory(UIProfile.GUI(p, pName));
		}
		//////////////////////////////////
		// Clicked on Reset Player Code
		//////////////////////////////////
		else if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal(ConfigManager.uIAdminpResetCode))) {
			String pName = inv.getItem(13).getItemMeta().getDisplayName().replace(Utils.ColorCode(ConfigManager.uIProfiles), "");
			
			String pUUID = ReferralPro.Instance.db.GetPlayersUUID(pName);
			
    		ReferralPro.Instance.db.UpdateCode(pUUID, ReferralCodeGen.GetShortID());

			// reopen the profiles
			p.openInventory(UIProfile.GUI(p, pName));
		}
		//////////////////////////////////
		// Clicked on Remove Player Rewards
		//////////////////////////////////
		else if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal(ConfigManager.uIAdminpRemoveRew))) {
			String pName = inv.getItem(13).getItemMeta().getDisplayName().replace(Utils.ColorCode(ConfigManager.uIProfiles), "");
			
			String pUUID = ReferralPro.Instance.db.GetPlayersUUID(pName);
			
			// Remove player rewards
			ReferralPro.Instance.db.RemoveRewardUUID(pUUID);
			
			// reopen the profiles
			p.openInventory(UIProfile.GUI(p, pName));
		}
		//////////////////////////////////
		// Clicked on Admin panel
		//////////////////////////////////
		else if (clicked.getItemMeta().getDisplayName().equals(Utils.RemoveButtonNormal(ConfigManager.uIRefButtonAdmin))) {
			p.openInventory(UIAdmin.GUI(p));
		}
		
	}
}


