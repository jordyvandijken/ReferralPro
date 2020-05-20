package Me.Teenaapje.ReferralPro.UI;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import Me.Teenaapje.ReferralPro.ReferralPro;
import Me.Teenaapje.ReferralPro.ConfigManager.ConfigManager;
import Me.Teenaapje.ReferralPro.Listener.Reward;
import Me.Teenaapje.ReferralPro.Utils.HiddenStringUtils;
import Me.Teenaapje.ReferralPro.Utils.Utils;

public class UIRewards {
	public static Inventory inv;
	public static String invName;
	public static int invRows = 3;
	public static int invTotal = invRows * 9;

	
	public static void Initialize() {
		invName = Utils.FormatString(null, ConfigManager.uIRewardsTitle);
		
		inv = Bukkit.createInventory(null, invTotal); 
		Utils.CreateItem(inv, "OAK_DOOR", 1, invTotal - 9, Utils.FormatString(null, ConfigManager.uIButtonGoBack));
		Utils.CreateItem(inv, "IRON_DOOR", 1, invTotal - 1, Utils.FormatString(null, ConfigManager.uIButtonClose));

	}
	
	public static Inventory GUI (Player p, int page) {
		Inventory toReturn = Bukkit.createInventory(null,  invTotal, invName);
		toReturn.setContents(inv.getContents());


		ArrayList<Reward> rewards = ReferralPro.Instance.db.GetPlayerRewards(p.getUniqueId().toString(), page);
        
		if (rewards.size() == 0) {
    		Utils.CreateItem(toReturn, "BARRIER", 1, 13, Utils.FormatString(null, ConfigManager.uIRewardsNon));
		} else {
			// when there are requests
			int index = 0;
			
			// show page number
    		Utils.CreateItem(toReturn, "OAK_SIGN", 1, invTotal - 5, Utils.FormatString(null, ConfigManager.uIButtonPage) + page);
			
    		// make scroll
    		if (rewards.size() == 19) {
	        	Utils.CreatePlayerHead(toReturn, invTotal - 4, "MHF_ArrowRight", Utils.FormatString(null, ConfigManager.uIButtonNextPage));
			}
    		if (page > 1 ) {
	        	Utils.CreatePlayerHead(toReturn, invTotal - 6, "MHF_ArrowLeft", Utils.FormatString(null, ConfigManager.uIButtonReturnPage));
			}
    		
    		//loop trough blocks
	        for (Reward reward : rewards) {
	        	if (index >= (invRows - 1) * 9) {
					break;
				}
	        	
	        	String playerName = Bukkit.getOfflinePlayer(UUID.fromString(reward.fromUUID)).getName();
	        	
	        	String pRefed = reward.didRefer == 1 ? Utils.FormatString(null, ConfigManager.uIRewardsYouRefed) : Utils.FormatString(null, ConfigManager.uIRewardsGotRefed);
	        	
	        	Utils.CreatePlayerHead(toReturn, index, playerName, playerName, pRefed, Utils.FormatString(null, ConfigManager.uIRewardsClick), 
	        																			Utils.FormatString(null, ConfigManager.uIRewardsSpace), 
	        																			HiddenStringUtils.encodeString("{RewardID: " + reward.id + "}"));
	        	
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
				page = Integer.parseInt(inv.getItem(invTotal - 5).getItemMeta().getDisplayName().replace(Utils.RemoveButtonNormal(ConfigManager.uIButtonPage), ""));
			}
			catch (NumberFormatException nfe) {
				System.out.println("NumberFormatException: " + nfe.getMessage());
			}

			p.openInventory(UIRewards.GUI(p, page - 1));
		}
		// Next page
		else if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.uIButtonNextPage))) {
			int page = 1;
			try {
				// the String to int conversion happens here
				page = Integer.parseInt(inv.getItem(invTotal - 5).getItemMeta().getDisplayName().replace(Utils.RemoveButtonNormal(ConfigManager.uIButtonPage), ""));
			}
			catch (NumberFormatException nfe) {
				System.out.println("NumberFormatException: " + nfe.getMessage());
			}

			p.openInventory(UIRewards.GUI(p, page + 1));
		}
		// Click on player head
		else if (clicked.getType() == Material.PLAYER_HEAD) {
			List<String> lore = clicked.getItemMeta().getLore();
			
			String json = HiddenStringUtils.extractHiddenString(lore.get(3));
						
			int id = -999;
			
			try {
				// the String to inter conversion happens here
				id = Integer.parseInt(json.replace("{RewardID: ", "").replace("}", ""));
			}
			catch (NumberFormatException nfe) {
				System.out.println("NumberFormatException: " + nfe.getMessage());
			}
			
			// Just checking its for the right player
			if (!p.getUniqueId().toString().equalsIgnoreCase(ReferralPro.Instance.db.GetIDReward(id))) {
				return;
			}
			
			// remove the Reward
			ReferralPro.Instance.db.RemoveReward(id);
			
			// give player the reward
			//ReferralPro.Instance.rewards.GiveRewards(p, false);
			boolean isSender = lore.get(0).equalsIgnoreCase(Utils.ColorCode(ConfigManager.uIRewardsYouRefed));
			
			if (isSender) {
				ReferralPro.Instance.rewards.GiveSenderReward(p);
			} else {
				ReferralPro.Instance.rewards.GiveReceiverReward(p);
			}
			
			p.openInventory(UIRewards.GUI(p, 1));
		}
	}
}