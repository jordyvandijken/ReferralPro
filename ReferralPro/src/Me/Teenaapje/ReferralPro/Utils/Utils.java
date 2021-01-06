package Me.Teenaapje.ReferralPro.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import Me.Teenaapje.ReferralPro.ReferralPro;
import Me.Teenaapje.ReferralPro.ConfigManager.ConfigManager;
import Me.Teenaapje.ReferralPro.UIElements.Button;
import Me.Teenaapje.ReferralPro.UIElements.Filler;
import me.clip.placeholderapi.PlaceholderAPI;

public class Utils {
	
	public static String ColorCode(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	public static void SendMessage(Player player, OfflinePlayer p, String s) {
		// Check if the message is for console
		if (player == null) {
			s = "[" + ReferralPro.Instance.getDescription().getPrefix() + "] " + s;
			ReferralPro.Instance.getServer().getConsoleSender().sendMessage(ColorCode(s));
			return;
		}
		
		// If the p == null set it to the player self
		if (p == null) {
			p = (OfflinePlayer)player;
		}
		
		// Check if enabled
		if (ReferralPro.papiEnabled) {
			s = PlaceholderAPI.setPlaceholders(p, s);
		} else {
			s = ReplacePlaceHolders(p, s);
		}
	
		// ColorCode message
		s = ColorCode(s);
		
		// Send message
		player.sendMessage(s);
	}
	
	public static String FormatString(OfflinePlayer p, String s) {
		if (p == null) {
			return ColorCode(s);
		}
		
		// Check if enabled
		if (ReferralPro.papiEnabled) {
			s = PlaceholderAPI.setPlaceholders(p, s);
		} else {
			s = ReplacePlaceHolders(p, s);
		}
		
		// ColorCode message
		return ColorCode(s);
	}
	
	public static String RemoveButtonNormal(String s) {
		return ChatColor.stripColor(ColorCode(s));
	}
	
	public static String ReplacePlaceHolders(OfflinePlayer p, String string) {
		ReferralPro plugin = ReferralPro.Instance;
		String playerName = p.getName();
		String playerUUID = p.getUniqueId().toString();
		
		String playerReferredBy = plugin.db.GetPlayersName(plugin.db.PlayerReferraldBy(playerUUID));
		if (playerReferredBy == null) {
			playerReferredBy = "This player hasn't been referred";
		}
		
		int pos = ReferralPro.Instance.leaderboard.GetPlayerPosition(playerUUID) ;
		String posString = "";
    	if (pos == 0) {
    		posString = Integer.toString(ReferralPro.Instance.leaderboard.GetLeaderboard().size()) + "+";
		}
    			
    	posString = Integer.toString(pos);
		
		return ChatColor.translateAlternateColorCodes('&', string.replace("%player_name%", playerName)
				 .replace("%referralpro_total%", Integer.toString(plugin.db.GetReferrals(playerUUID)))
				 .replace("%referralpro_referred%", Boolean.toString(plugin.db.PlayerReferrald(playerUUID)))
				 .replace("%referralpro_refedby%", playerReferredBy)
				 .replace("%referralpro_nextmilepoint%", Integer.toString(ReferralPro.Instance.rewards.NextMileRewardTotal(playerUUID)))
				 .replace("%referralpro_nextmileneeded%", Integer.toString(ReferralPro.Instance.rewards.NextMileReward(playerUUID)))
				 .replace("%referralpro_refcode%", ReferralPro.Instance.db.GetPlayerCode(playerUUID))
				 .replace("%referralpro_boardposition%", posString));
	}
	
	public static ItemStack CreateItem(Inventory inv, String materialID, int amount, int invSlot, String displayName, String... loreString) {
		ItemStack item;
		List<String> lore = new ArrayList<String>();
				
		item = new ItemStack(Material.getMaterial(materialID),  amount);
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Utils.ColorCode(displayName));
		for (String s : loreString) {
			lore.add(s);
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		if (inv != null) {
			inv.setItem(invSlot, item);
		}

		return item;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack CreatePlayerHead(Inventory inv, int invSlot, String playerName, String displayName, String... loreString) {
		ItemStack skull;
		List<String> lore = new ArrayList<String>();
				
		//if (ServerVersion.isServerVersionAtLeast(ServerVersion.V1_13)) {
		    //Utils.SendMessage(null, null, ChatColor.GREEN + "Version Above 1.12");
		    skull = new ItemStack(Material.PLAYER_HEAD,  1);

			SkullMeta skullM = (SkullMeta) skull.getItemMeta();
			// the player
			skullM.setDisplayName(Utils.ColorCode(displayName));
			// add lore
			for (String s : loreString) {
				lore.add(s);
			}
			skullM.setLore(lore);
			
			skullM.setOwner(playerName) ;
			
			skull.setItemMeta(skullM);
		//} else {
		    //Utils.SendMessage(null, null, ChatColor.GREEN + "Version lower or eqeul to 1.12");
		    //skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		    //SkullMeta skullM = (SkullMeta) skull.getItemMeta();
		    
		    // the player
		    //skullM.setDisplayName(Utils.ColorCode(displayName));
 			// add lore
 			//for (String s : loreString) {
 			//	lore.add(s);
 			//}
 			//skullM.setLore(lore);
		    
		    //skullM.setOwner(playerName);
		    
		    //skull.setItemMeta(skullM);
		//}
		
		
		if (inv != null) {
			inv.setItem(invSlot, skull);
		}
		return skull;
	}
	
	public static ItemStack CreateButton(Inventory inv, Button button, String displayName, String... loreString) {
		if (!button.enable) {
			return null;
		}
		
		ItemStack item;
		List<String> lore = new ArrayList<String>();
		
		item = new ItemStack(Material.getMaterial(button.buttonItem),  1);
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Utils.ColorCode(displayName));
		for (String s : loreString) {
			lore.add(s);
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		if (inv != null) {
			inv.setItem(button.position, item);
		}

		return item;
	}
	
	public static void CreateFillers(Inventory inv, List<Filler> fillers) {
		for (Filler filler : fillers) {
			ItemStack item;
			
			item = new ItemStack(Material.getMaterial(filler.fillerItem),  1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ConfigManager.instance.fillerName);
			item.setItemMeta(meta);
			
			if (inv != null) {
				inv.setItem(filler.position, item);
			} else {
				return;
			}
		}
	}
}
