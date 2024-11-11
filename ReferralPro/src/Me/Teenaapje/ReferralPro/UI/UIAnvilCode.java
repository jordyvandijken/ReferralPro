package Me.Teenaapje.ReferralPro.UI;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import Me.Teenaapje.ReferralPro.ReferralPro;
import Me.Teenaapje.ReferralPro.ConfigManager.ConfigManager;
import Me.Teenaapje.ReferralPro.Listener.Reward;
import Me.Teenaapje.ReferralPro.Utils.Utils;
import net.wesjd.anvilgui.AnvilGUI;

public class UIAnvilCode {
	public static void GUI (Player p, String pCode) {
		// TODO open anvil UI
		new AnvilGUI.Builder()
	    .onClose(player -> {                      
	    	//called when the inventory is closing
	    	// Not able to reopen last inv - is wonky
	    	// TODO need to try to open with a delay
	    })
	    //called when the inventory output slot is clicked
	    .onClick((slot, stateSnapshot) -> { 
	    	if (slot != AnvilGUI.Slot.OUTPUT) {
	            return java.util.Collections.emptyList();
	        } 
	    	
	    	Player player = stateSnapshot.getPlayer();
	    	String code = stateSnapshot.getText();
	    	
	    	
	    	// check length
	    	if (code.length() < ConfigManager.instance.codeLength) {
	            return AnvilGUI.Response.text(Utils.FormatString(p, ConfigManager.instance.uIAnvilCodeShort));
			} // to long
	    	else if (code.length() > ConfigManager.instance.codeLength) {
	            return AnvilGUI.Response.text(Utils.FormatString(p, ConfigManager.instance.uIAnvilCodeLong));
			}
	    	
	    	// Check if the code exists
	    	if (!ReferralPro.Instance.db.CodeExists(code)) {
	            return AnvilGUI.Response.text(Utils.FormatString(p, ConfigManager.instance.uIAnvilCodeNotExist));
			}
	    	
	    	// Check if is own code
	    	if (ReferralPro.Instance.db.GetPlayerCode(player.getUniqueId().toString()).equalsIgnoreCase(code)) {
	            return AnvilGUI.Response.text(Utils.FormatString(p, ConfigManager.instance.uIAnvilUseOwnCode));
			}
	    	
	    	// the other players UUID
			String UUIDs = ReferralPro.Instance.db.GetPlayerCodeUUID(code);
			UUID pUUID = UUID.fromString(UUIDs);

			OfflinePlayer op = Bukkit.getOfflinePlayer(pUUID);
	    	
			/////////////////////////////////////////////////////
			//
			//	Config stuf start
			//
			/////////////////////////////////////////////////////
	    	
	    	// Check if this player is referred
			if (ReferralPro.Instance.db.PlayerReferrald(p.getUniqueId().toString())) {		
				
				
				// Check if the player try to refer each other
				if (!ReferralPro.Instance.getConfig().getBoolean("canReferEachOther") 
				  && ReferralPro.Instance.db.PlayerReferraldBy(p.getUniqueId().toString()).equalsIgnoreCase(UUIDs)) {	
					
					
			        //player.openInventory(UIAnvilResponse.GUI("You can't refer each other.", 1));
		            return AnvilGUI.Response.text(Utils.FormatString(p, ConfigManager.instance.tryRefEachOther));
				}
			}

			// Check if the player try to refer an older player
			if (!ReferralPro.Instance.getConfig().getBoolean("canReferOlderPlayer") && op.getFirstPlayed() > p.getFirstPlayed()) {			
		        //player.openInventory(UIAnvilResponse.GUI("You can't refer an older player!", 1));
	            return AnvilGUI.Response.text(Utils.FormatString(p, ConfigManager.instance.tryRefOlderPlayer));
			}
						
			// Check if server uses time limit if so did the player play enough
			int pTime = ReferralPro.Instance.getConfig().getInt("minPlayTime");
	        if (pTime != -1 && ((p.getLastPlayed() - p.getFirstPlayed()) / 60000) < pTime) {
	        	Utils.SendMessage(null, null, Long. toString(p.getLastPlayed()));
		        //player.openInventory(UIAnvilResponse.GUI("You haven't played enough to use a refer", 1));
	            return AnvilGUI.Response.text(Utils.FormatString(p, ConfigManager.instance.notEnoughPlayTimeCode));
			}
	        
	        // Check if server uses time limit if so did the player play enough
 			int maxTime = ReferralPro.Instance.getConfig().getInt("maxPlayTime");
 	        if (maxTime != -1 && ((p.getLastPlayed() - p.getFirstPlayed()) / 60000) > maxTime) {
 		        //player.openInventory(UIAnvilResponse.GUI("You have played too much to use a refer", 1));
 	            return AnvilGUI.Response.text(Utils.FormatString(p, ConfigManager.instance.tooMuchPlayTimeCode));
 			}
 	        
 	        // Check if the server blocks same ips
 	        if (!ReferralPro.Instance.getConfig().getBoolean("allowSameIPRefer") && p.getAddress().getHostName().equals(ReferralPro.Instance.db.GetPlayerIP(UUIDs))) {
		        //player.openInventory(UIAnvilResponse.GUI("You can't refer on the same ip", 1));
	            return AnvilGUI.Response.text(Utils.FormatString(p, ConfigManager.instance.sameIPRefer));
			}
			
			/////////////////////////////////////////////////////
			//
			//	Config stuf end
			//
			/////////////////////////////////////////////////////
	    					    	
	    	// check if the other player is already referred
	    	if (ReferralPro.Instance.db.PlayerReferrald(player.getUniqueId().toString())) {
				//player.openInventory(UIAnvilResponse.GUI("This Player already got referd.", 1));
	            return AnvilGUI.Response.text(Utils.FormatString(p, ConfigManager.instance.uIAnvilAlreadyRefed));
			}
	    	
	    
	    	
			/////////////////////////////////////////////////////
			//
			//	Check for confirm
			//
			/////////////////////////////////////////////////////
	    
	    	if (ConfigManager.instance.codeConfirmation) {
	    		// Open confirmation pannel
	    	    player.openInventory(UICodeConfirm.GUI(player, ReferralPro.Instance.db.GetPlayersName(UUIDs)));
			} else {
				// get the players UUID
				String playerUUID = player.getUniqueId().toString();
				
				// Set the Referral
				ReferralPro.Instance.db.ReferralPlayer(UUIDs, playerUUID);

				OfflinePlayer otherp = Bukkit.getOfflinePlayer(UUID.fromString(UUIDs));

				// Ch
				if (ConfigManager.instance.giveRewardInstant && otherp.isOnline()) {
					ReferralPro.Instance.rewards.GiveSenderReward(Bukkit.getPlayer(UUID.fromString(UUIDs)));
				} else {
					// The player who got referred
					ReferralPro.Instance.db.CreateReward(new Reward(-999, playerUUID, UUIDs, 0));
				}
				
				if (ConfigManager.instance.giveRewardInstant && p.isOnGround()) {
					ReferralPro.Instance.rewards.GiveReceiverReward(p);
				} else {
					// The player who referred someone
					ReferralPro.Instance.db.CreateReward(new Reward(-999, UUIDs, playerUUID, 1));
				}

				// Remove all open requests
				ReferralPro.Instance.db.RemovePlayerRequests(playerUUID);
				p.openInventory(UIProfile.GUI(p, p.getName()));

				// Update Leaderboard
				ReferralPro.Instance.leaderboard.UpdateLeaderboard();
			}
	    	
			return AnvilGUI.Response.text("");
			
	    })
	    //.preventClose()                           					//prevents the inventory from being closed
	    .text(pCode)														//sets the text the GUI should start with
	    .itemLeft(Utils.CreateItem(null, "PLAYER_HEAD", 1, 0, " ", " ")) 	//use a custom item for the first slot
	    //.item(new ItemStack(Material.PLAYER_HEAD)) 					//use a custom item for the first slot
	    .title(Utils.FormatString(p, ConfigManager.instance.uIAnvilCodeTitle))   											//set the title of the GUI (only works in 1.14+)
	    .plugin(ReferralPro.Instance)             						//set the plugin instance
	    .open(p);            
		//toReturn.setContents(inv.getContents());
		return;
	}
}