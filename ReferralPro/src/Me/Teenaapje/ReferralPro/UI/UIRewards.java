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
import Me.Teenaapje.ReferralPro.UIElements.UIElement;
import Me.Teenaapje.ReferralPro.UIElements.UIElementManager;
import Me.Teenaapje.ReferralPro.Utils.Utils;

public class UIRewards {
	public static Inventory inv;
	public static String invName;
	public static int invRows;
	public static int invTotal;

	public static UIElement element;
	
	public static void Initialize() {
		invName = Utils.FormatString(null, ConfigManager.instance.uIRewardsTitle);
		

		element = UIElementManager.instance.GetElement("refrewards");
		
		invRows = element.rows;
		invTotal = invRows * 9;
		
		inv = Bukkit.createInventory(null, invTotal);

		
		Utils.CreateFillers(inv, element.fillers);

		Utils.CreateButton(inv, element.GetButton("back"), Utils.FormatString(null, ConfigManager.instance.uIButtonGoBack));
		Utils.CreateButton(inv, element.GetButton("close"), Utils.FormatString(null, ConfigManager.instance.uIButtonClose));
	}
	
	public static Inventory GUI (Player p, int page) {
		if (!element.enable) {
			return UIReferral.GUI(p); 
		}
		
		Inventory toReturn = Bukkit.createInventory(null,  invTotal, invName);
		toReturn.setContents(inv.getContents());


		ArrayList<Reward> rewards = ReferralPro.Instance.db.GetPlayerRewards(p.getUniqueId().toString(), page, invTotal - 9);
        
		if (rewards.size() == 0) {
			Utils.CreateButton(toReturn, element.GetButton("none"), Utils.FormatString(null, ConfigManager.instance.uIRewardsNon));
		} else {
			// when there are requests
			int index = 0;
			
			// show page number
			Utils.CreateButton(toReturn, element.GetButton("page"), Utils.FormatString(null, ConfigManager.instance.uIButtonPage) + page);
			
    		// make scroll
    		if (rewards.size() == 19) {
	        	Utils.CreatePlayerHead(toReturn, element.GetButton("next").position, "MHF_ArrowRight", Utils.FormatString(null, ConfigManager.instance.uIButtonNextPage));
			}
    		if (page > 1 ) {
	        	Utils.CreatePlayerHead(toReturn, element.GetButton("return").position, "MHF_ArrowLeft", Utils.FormatString(null, ConfigManager.instance.uIButtonReturnPage));
			}
    		
    		//loop trough blocks
	        for (Reward reward : rewards) {
	        	if (index >= (invRows - 1) * 9) {
					break;
				}
	        	
	        	String playerName = Bukkit.getOfflinePlayer(UUID.fromString(reward.fromUUID)).getName();
	        	
	        	String pRefed = reward.didRefer == 1 ? Utils.FormatString(null, ConfigManager.instance.uIRewardsYouRefed) : Utils.FormatString(null, ConfigManager.instance.uIRewardsGotRefed);
	        	
	        	Utils.CreatePlayerHead(toReturn, index, playerName, playerName, pRefed, Utils.FormatString(null, ConfigManager.instance.uIRewardsClick), 
	        																			Utils.FormatString(null, ConfigManager.instance.uIRewardsSpace), 
	        																			Integer.toString(reward.id));
	        	
	        	index++;
			}
		}
        
        
		return toReturn;
	}
	
	public static void Clicked(Player p , int slot, ItemStack clicked, Inventory inv) {
		// Return to main page
		if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.instance.uIButtonGoBack))) {
			p.openInventory(UIProfile.GUI(p, p.getName()));
		}
		// Close inv
		else if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.instance.uIButtonClose))) {
			p.closeInventory();
		}
		// Return page
		else if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.instance.uIButtonReturnPage))) {
			int page = 1;
			try {
				// the String to int conversion happens here
				page = Integer.parseInt(inv.getItem(invTotal - 5).getItemMeta().getDisplayName().replace(Utils.RemoveButtonNormal(ConfigManager.instance.uIButtonPage), ""));
			}
			catch (NumberFormatException nfe) {
				System.out.println("NumberFormatException: " + nfe.getMessage());
			}

			p.openInventory(UIRewards.GUI(p, page - 1));
		}
		// Next page
		else if (Utils.RemoveButtonNormal(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(Utils.RemoveButtonNormal(ConfigManager.instance.uIButtonNextPage))) {
			int page = 1;
			try {
				// the String to int conversion happens here
				page = Integer.parseInt(inv.getItem(invTotal - 5).getItemMeta().getDisplayName().replace(Utils.RemoveButtonNormal(ConfigManager.instance.uIButtonPage), ""));
			}
			catch (NumberFormatException nfe) {
				System.out.println("NumberFormatException: " + nfe.getMessage());
			}

			p.openInventory(UIRewards.GUI(p, page + 1));
		}
		// Click on player head
		else if (clicked.getType() == Material.PLAYER_HEAD) {
			List<String> lore = clicked.getItemMeta().getLore();
			
			String hiddenID = lore.get(3);
			
			int id = -999;
			
			try {
				// the String to inter conversion happens here
				id = Integer.parseInt(hiddenID);
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
			boolean isSender = lore.get(0).equalsIgnoreCase(Utils.ColorCode(ConfigManager.instance.uIRewardsYouRefed));
			
			if (isSender) {
				ReferralPro.Instance.rewards.GiveSenderReward(p);
			} else {
				ReferralPro.Instance.rewards.GiveReceiverReward(p);
			}
			
			p.openInventory(UIRewards.GUI(p, 1));
		}
	}
}