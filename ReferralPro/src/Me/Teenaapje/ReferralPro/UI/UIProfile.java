package Me.Teenaapje.ReferralPro.UI;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import Me.Teenaapje.ReferralPro.ReferralPro;
import Me.Teenaapje.ReferralPro.ConfigManager.ConfigManager;
import Me.Teenaapje.ReferralPro.Utils.Utils;

public class UIProfile {
	public static Inventory inv;
	public static String invName;
	public static int invRows = 3;
	public static int invTotal = invRows * 9;
	
	public static void Initialize() {
		invName = Utils.FormatString(null, ConfigManager.uIProfileTitle);
		
		inv = Bukkit.createInventory(null, invTotal); 
		
		
		Utils.CreateItem(inv, "OAK_DOOR", 1, invTotal - 9, Utils.FormatString(null, ConfigManager.uIButtonGoBack));
		Utils.CreateItem(inv, "IRON_DOOR", 1, invTotal - 1, Utils.FormatString(null, ConfigManager.uIButtonClose));
		
	}
	
	public static Inventory GUI (Player p, String pProfile) {
		//long startTime = System.nanoTime();
		
		Inventory toReturn = Bukkit.createInventory(null,  invTotal, invName);
		
		String profileUUID = ReferralPro.Instance.db.GetPlayersUUID(pProfile);
		
		@SuppressWarnings("deprecation")
		OfflinePlayer op = Bukkit.getOfflinePlayer(profileUUID);
		
		// the total of player referrals
		Utils.CreateItem(inv, "NAME_TAG", 1, 4, Utils.FormatString(op, ConfigManager.uIProfileTotal), Utils.FormatString(null, ConfigManager.uIProfileTotalExpl));
		
		boolean playerExists = ReferralPro.Instance.db.PlayerExistsName(pProfile);
		
		// check if the player has joined the server once
		if (playerExists) {
			// the player
			Utils.CreatePlayerHead(inv, 13, pProfile, pProfile + Utils.FormatString(op, ConfigManager.uIProfiles),
																 Utils.FormatString(op, ConfigManager.uIProfileIsRefed), 
																 Utils.FormatString(op, ConfigManager.uIProfileRefedBy));
		} else {
			// the player
			Utils.CreatePlayerHead(inv, 13, pProfile, pProfile + Utils.FormatString(op, ConfigManager.uIProfiles), Utils.FormatString(null, ConfigManager.uIProfileNonExist));
		}

		toReturn.setContents(inv.getContents());
		
		if (p.getName().equalsIgnoreCase(pProfile)) {
			// the list of rewards
			Utils.CreateItem(toReturn, "CHEST", 1, 12, Utils.FormatString(null, ConfigManager.uIProfileButtonReward), 
													   Utils.FormatString(null, ConfigManager.uIProfileButtonRewardExpl));
			
			// the list of blocked 
			Utils.CreateItem(toReturn, "BARRIER", 1, 22, Utils.FormatString(null, ConfigManager.uIProfileButtonBlocked),
														 Utils.FormatString(null, ConfigManager.uIProfileButtonBlockedExpl));
			
			
			
			// make the text change if player can receive reward
			if (ReferralPro.Instance.rewards.NextMileReward(p.getUniqueId().toString()) == 0 && ReferralPro.Instance.getConfig().getBoolean("enableMilestone")) {
				// the list of mileStone Rewards
				Utils.CreateItem(toReturn, "ENDER_CHEST", 1, 14, Utils.FormatString(null, ConfigManager.uIProfileButtonMilestone),
								Utils.FormatString(op, ConfigManager.uIProfileNextMilestone),
								Utils.FormatString(op, ConfigManager.uIProfiletooNextMilestone),
								Utils.FormatString(op, ConfigManager.uIButtonclickReward),
								Utils.FormatString(op, ConfigManager.uIButtonEnoughSpace));
			} else if (ReferralPro.Instance.getConfig().getBoolean("enableMilestone")){
				// the list of mileStone Rewards
				Utils.CreateItem(toReturn, "ENDER_CHEST", 1, 14, Utils.FormatString(null, ConfigManager.uIProfileButtonMilestone),
								Utils.FormatString(op, ConfigManager.uIProfileNextMilestone),
								Utils.FormatString(op, ConfigManager.uIProfiletooNextMilestone));
			}
		}	

		
		if (ReferralPro.perms.has(p, "ReferralPro.Admin") && playerExists) {
			// the list of blocked 
			Utils.CreateItem(toReturn, "TNT", 1, 2, "Reset Player", "Clicking on this WILL reset the player in the database");
			// the list of blocked 
			Utils.CreateItem(toReturn, "TNT", 1, 6, "Remove Player Referrals", "Clicking on this WILL remove the player referrals in the database", "This will not reset the milestone rewards");
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
				
		// Clicked on reset player
		else if (clicked.getItemMeta().getDisplayName().equals("Reset Player")) {
			String pName = inv.getItem(13).getItemMeta().getDisplayName().replace(Utils.RemoveButtonNormal(ConfigManager.uIProfiles), "");
			
			// Reset player
			ReferralPro.Instance.db.PlayerReset(ReferralPro.Instance.db.GetPlayersUUID(pName));

			// reopen the profiles
			p.openInventory(UIProfile.GUI(p, pName));
		}
		
		// Clicked on remove refers
		else if (clicked.getItemMeta().getDisplayName().equals("Remove Player Referrals")) {
			String pName = inv.getItem(13).getItemMeta().getDisplayName().replace(Utils.RemoveButtonNormal(ConfigManager.uIProfiles), "");
			
			// Remove player
			ReferralPro.Instance.db.PlayerResetReferral(ReferralPro.Instance.db.GetPlayersUUID(pName));
			
			// reopen the profiles
			p.openInventory(UIProfile.GUI(p, pName));
		}
		
	}
}