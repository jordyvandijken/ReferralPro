package Me.Teenaapje.ReferralPro.UI;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import Me.Teenaapje.ReferralPro.ReferralPro;
import Me.Teenaapje.ReferralPro.ConfigManager.ConfigManager;
import Me.Teenaapje.ReferralPro.Utils.Utils;
import net.wesjd.anvilgui.AnvilGUI;

public class UIAnvil {
	@SuppressWarnings("deprecation")
	public static void GUI (Player p, String name) {
		// TODO open anvil UI
		new AnvilGUI.Builder()
	    .onClose(player -> {                      //called when the inventory is closing
//				        player.sendMessage("You closed the inventory.");
//				        return AnvilGUI.Response.close();
//				        player.closeInventory();
	    	// Not able to reopen last inv - is wonky
	    	// TODO need to try to open with a delay
	    })
	    .onClick((slot, stateSnapshot) -> {           //called when the inventory output slot is clicked
	    	if (slot != AnvilGUI.Slot.OUTPUT) {
	            return java.util.Collections.emptyList();
	        } 
	    	
	    	Player player = stateSnapshot.getPlayer();
	    	String text = stateSnapshot.getText();
	    	
	    	
	    	// make uuid fromm string to get op
	    	OfflinePlayer op = Bukkit.getOfflinePlayer(text);
	    	
	    	// check if the player is online
	    	if (op.getPlayer() == null) {
			
		        //* Add option to disalble it
			    if (!op.hasPlayedBefore()) {
			    	player.openInventory(UIAnvilResponse.GUI(player, Utils.FormatString(player, ConfigManager.instance.uIAnvilNeverPlayed), 1));
		            return AnvilGUI.Response.text("");
			    }//*/
				
			}
    	    
	        // turn the UUID to String
			String senderString = player.getUniqueId().toString();
			String receiverString = "";
			//
			if (op.isOnline()) {
				receiverString = op.getUniqueId().toString();
			} else {
				receiverString = ReferralPro.Instance.db.GetPlayersUUID(text);
			}			
			
			// check if the other player is already referred
			if (ReferralPro.Instance.db.PlayerReferrald(receiverString)) {
				player.openInventory(UIAnvilResponse.GUI(player, Utils.FormatString(player, ConfigManager.instance.uIAnvilAlreadyRefed), 1));
	            return AnvilGUI.Response.text("");
			}
			
			
			// check if its not the same player
			if (senderString.compareTo(receiverString) == 0) {
		        player.openInventory(UIAnvilResponse.GUI(player, Utils.FormatString(player, ConfigManager.instance.uIAnvilCantRefSelf), 1));
	            return AnvilGUI.Response.text("");
			}
			
			// Check if the request already exists
			if (ReferralPro.Instance.db.RequestExists(senderString, receiverString)) {			
		        player.openInventory(UIAnvilResponse.GUI(player, Utils.FormatString(player, ConfigManager.instance.uIAnvilAlreadyInQ), 1));
	            return AnvilGUI.Response.text("");
			}

			/////////////////////////////////////////////////////
			//
			//	Config stuf start
			//
			/////////////////////////////////////////////////////
			
			// Check if player is referred
			if (ReferralPro.Instance.db.PlayerReferrald(p.getUniqueId().toString())) {
				
				String refedName = ReferralPro.Instance.db.GetPlayersName(ReferralPro.Instance.db.PlayerReferraldBy(p.getUniqueId().toString()));
						
				// Check if the player try to refer each other
				if (!ReferralPro.Instance.getConfig().getBoolean("canReferEachOther") && refedName.equalsIgnoreCase(text)) {			
			        player.openInventory(UIAnvilResponse.GUI(player, Utils.FormatString(op, ConfigManager.instance.tryRefEachOther), 1));
		            return AnvilGUI.Response.text("");
				}
			}
			
			// Check if the player try to refer an older player
			if (!ReferralPro.Instance.getConfig().getBoolean("canReferOlderPlayer") && p.getFirstPlayed() > op.getFirstPlayed()) {			
		        player.openInventory(UIAnvilResponse.GUI(player, Utils.FormatString(op, ConfigManager.instance.tryRefOlderPlayer), 1));
	            return AnvilGUI.Response.text("");
			}
			
			// Check if server uses time limit if so did the player play enough
			int minTime = ReferralPro.Instance.getConfig().getInt("minPlayTime");
	        if (minTime != -1 && ((op.getLastPlayed() - op.getFirstPlayed()) / 60000) < minTime) {
		        player.openInventory(UIAnvilResponse.GUI(player, Utils.FormatString(op, ConfigManager.instance.notEnoughPlayTime), 1));
	            return AnvilGUI.Response.text("");
			}
	        
	        // Check if server uses time limit if so did the player play enough
 			int maxTime = ReferralPro.Instance.getConfig().getInt("maxPlayTime");
 	        if (maxTime != -1 && ((op.getLastPlayed() - op.getFirstPlayed()) / 60000) > maxTime) {
 		        player.openInventory(UIAnvilResponse.GUI(player, Utils.FormatString(op, ConfigManager.instance.tooMuchPlayTime), 1));
	            return AnvilGUI.Response.text("");
 			}
 	        
 	        
 	        // Check if the server uses a max request
 	        int maxRequests = ReferralPro.Instance.getConfig().getInt("maxPendingRequests");
 	        if (maxRequests != -1 && ReferralPro.Instance.db.TotalRequests(senderString) >= maxRequests) {
		        player.openInventory(UIAnvilResponse.GUI(player, Utils.FormatString(player, ConfigManager.instance.maxRequestSend), 1));
	            return AnvilGUI.Response.text("");
			}
 	        
 	        // Check if the server blocks same ips
 	        if (!ReferralPro.Instance.getConfig().getBoolean("allowSameIPRefer") && p.getAddress().getHostName().equals(ReferralPro.Instance.db.GetPlayerIP(receiverString))) {
		        player.openInventory(UIAnvilResponse.GUI(player, Utils.FormatString(op, ConfigManager.instance.sameIPRefer), 1));
	            return AnvilGUI.Response.text("");
			}
 	        
 	        
			/////////////////////////////////////////////////////
			//
			//	Config stuf end
			//
			/////////////////////////////////////////////////////
			
			// Check if the player blocked him
			if (ReferralPro.Instance.db.BlockExists(senderString, receiverString)) {			
		        player.openInventory(UIAnvilResponse.GUI(player, Utils.FormatString(op, ConfigManager.instance.uIAnvilBlocked), 1));
	            return AnvilGUI.Response.text("");
			}
			
			// Make request
			ReferralPro.Instance.db.CreateRequest(senderString, receiverString);
			
			// check if the player is online
			Player playerReceiver = ReferralPro.Instance.getServer().getPlayer(receiverString);
		    if (playerReceiver == null) {
		        // Not Online
		        player.openInventory(UIAnvilResponse.GUI(player, Utils.FormatString(op, ConfigManager.instance.uIAnvilRequestQed), 0));
	            return AnvilGUI.Response.text("");
		    }
		    		    
		    // If the player is online make a notification
		    //ReferralPro.Instance.commandReferral.CreateResponseBox(senderString, receiverString);

		    player.openInventory(UIAnvilResponse.GUI(player, Utils.FormatString(op, ConfigManager.instance.uIAnvilInvited), 0));
            return AnvilGUI.Response.text("");			
	    })
	    //.preventClose()                           					//prevents the inventory from being closed
	    .text(name)														//sets the text the GUI should start with
	    .itemLeft(Utils.CreateItem(null, "PLAYER_HEAD", 1, 0, " ", " ")) 	//use a custom item for the first slot
	    //.item(new ItemStack(Material.PLAYER_HEAD)) 					//use a custom item for the first slot
	    .title(Utils.FormatString(p, ConfigManager.instance.uIAnvilTitle))   											//set the title of the GUI (only works in 1.14+)
	    .plugin(ReferralPro.Instance)             						//set the plugin instance
	    .open(p);            
		//toReturn.setContents(inv.getContents());
		return;
	}
}