package Me.Teenaapje.ReferralPro;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import Me.Teenaapje.ReferralPro.ConfigManager.ConfigManager;
import Me.Teenaapje.ReferralPro.LeaderBoard.LeaderboardPlayer;
import Me.Teenaapje.ReferralPro.Listener.Reward;
import Me.Teenaapje.ReferralPro.UI.UIAdmin;
import Me.Teenaapje.ReferralPro.UI.UIBlocked;
import Me.Teenaapje.ReferralPro.UI.UICodeConfirm;
import Me.Teenaapje.ReferralPro.UI.UIProfile;
import Me.Teenaapje.ReferralPro.UI.UIReferInvites;
import Me.Teenaapje.ReferralPro.UI.UIReferral;
import Me.Teenaapje.ReferralPro.UI.UIRewards;
import Me.Teenaapje.ReferralPro.Utils.Utils;

public class Referral implements CommandExecutor, TabExecutor {

	@SuppressWarnings("unused")
	private ReferralPro referralPro;

	public Referral () {
		// The main class
		referralPro = ReferralPro.Instance;
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// check for the command name
        if (!cmd.getName().equalsIgnoreCase("Ref") && !cmd.getName().equalsIgnoreCase("Referral")) {
        	return false;
        }
        
    	Commands chosenCommand = null;
		
		if (args.length > 0) {
			for (Commands  command : Commands.values()) { 
				if (args[0].compareToIgnoreCase(command.toString()) == 0) {
					chosenCommand = command;
				}
		    } 
		}
		
		if (chosenCommand == Commands.Trigger) {
			TriggerCommands(sender, args);
			return false;
		}
        
		Player p = (Player)sender;
        
    	// check if is player
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can use this command.");
			return false;
		}
	
		

		
		if (chosenCommand != null) {
			switch (chosenCommand) {
			case Admin:
				if ((sender instanceof Player) && !ReferralPro.perms.has(sender, "Referral.Admin")) {
					break;
				}
				p.openInventory(UIAdmin.GUI(p));
				
				break;
			case Rewards:
				// Open the rewards UI
				p.openInventory(UIRewards.GUI(p, 1));
				break;
				
			case Blocked:
				// Open the Blocked UI
				p.openInventory(UIBlocked.GUI(p, 1));
				break;
				
			case Invites:
				// Check for perms
				if (!(ReferralPro.perms.has(p, "ReferralPro.Invite") || ReferralPro.perms.has(p, "ReferralPro.Admin"))) {
					p.sendMessage("No permision");
					return false;
				}
				// Show the invites
				p.openInventory(UIReferInvites.GUI(p, 1));
				break;
				
			case Profile:
				// check arguments
				if (args.length == 1) {
					
					p.openInventory(UIProfile.GUI(p, p.getName()));
					// To much info
			        return false;
			    } else if (args.length > 1) {
			    	if (!p.getName().equalsIgnoreCase(args[1])) {
			    		// Check for perms
						if (!(ReferralPro.perms.has(p, "ReferralPro.OtherProfile") || ReferralPro.perms.has(p, "ReferralPro.Admin"))) {
							p.sendMessage("No permision");
							return false;
						}
					}
			    	// Missing info
			    	p.openInventory(UIProfile.GUI(p, args[1]));
			        return false;
			    } 
				
				break;
				
			case Reload:
				if (!ReferralPro.perms.has(p, "ReferralPro.Admin")) {
					p.sendMessage("No permision");
					return false;
				}
				
				ReferralPro.Instance.Reload();
				
				Utils.SendMessage(p, p, "[Referral Pro] &2Plugin Reloaded");

				break;
		
			case Code:
				// get or use code
				if (args.length == 1) {
					// ReferralPro.GetRefCode
					if (!ReferralPro.perms.has(p, "ReferralPro.Admin") && !ReferralPro.perms.has(p, "ReferralPro.GetRefCode")) {
						p.sendMessage("No permision");
						return false;
					}
					p.sendMessage(Utils.FormatString(p, ConfigManager.instance.showCode));
				} else {
					// ReferralPro.UseRefCode
					if (!ReferralPro.perms.has(p, "ReferralPro.Admin") && !ReferralPro.perms.has(p, "ReferralPro.UseRefCode")) {
						p.sendMessage("No permision");
						return false;
					}
					//UIAnvilCode.GUI(p, args[1]);
					ReferPlayerCode(p, args[1]);
				}

				
				break;
				
			case Top:
				// set the title
				p.sendMessage(Utils.FormatString(p, ConfigManager.instance.leaderboardTitle));

				// loop trough board
				for (LeaderboardPlayer topPlayer : ReferralPro.Instance.leaderboard.GetLeaderboard()) {
					OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(topPlayer.playerUUID));
					p.sendMessage(Utils.FormatString(op, ConfigManager.instance.leaderboardPlace));
					//essentials_nickname
				}				
				break;
			case GiveReward:
				if (!ReferralPro.perms.has(p, "ReferralPro.Admin") && !ReferralPro.perms.has(p, "ReferralPro.GiveReward")) {
					p.sendMessage("No permision");
					return false;
				}
				
				if (args.length >= 2) {
					// check which trigger
					if (args[1].equalsIgnoreCase(GiveRewardCommands.Reward.toString())) {
						if (args.length == 4) {
							if (args[2].equalsIgnoreCase("refer")) {
								String uUID = ReferralPro.Instance.db.GetPlayersUUID(args[3]);
								if (uUID != null) {
									ReferralPro.Instance.db.CreateReward(new Reward(-999, uUID, p.getUniqueId().toString(), 1));
									p.sendMessage(Utils.FormatString(p, "Reward given"));
								} else {
									p.sendMessage(Utils.FormatString(p, "Player not found"));
								}
								
							// The referred
							} else if (args[2].equalsIgnoreCase("referred")) {
								String uUID = ReferralPro.Instance.db.GetPlayersUUID(args[3]);
								if (uUID != null) {
									ReferralPro.Instance.db.CreateReward(new Reward(-999, uUID, p.getUniqueId().toString(), 0));
									p.sendMessage(Utils.FormatString(p, "Reward given"));
								} else {
									p.sendMessage(Utils.FormatString(p, "Player not found"));
								}
							} else {
								p.sendMessage(Utils.FormatString(p, "Incorrect use: /Ref Trigger Reward Refer/Referred <player>"));
							}
						} else {
							p.sendMessage(Utils.FormatString(p, "Incorrect use: /Ref Trigger Reward Refer/Referred <player>"));
						}
					} else if (args[1].equalsIgnoreCase(GiveRewardCommands.MilestoneReward.toString())) {
						if (args.length == 4) {
							Player playerReward = Bukkit.getPlayer(args[2]);
							if (playerReward != null) {
								try {
									int rank = Integer.parseInt(args[3]);
									ReferralPro.Instance.rewards.GiveMileStoneRewards(p, rank);
									p.sendMessage(Utils.FormatString(p, "Milestone reward given"));
								} catch (Exception e) {
									// TODO: handle exception
									// or dont
									p.sendMessage(Utils.FormatString(p, "Failt to cast: " + args[3]));
								}
							} else {
								p.sendMessage(Utils.FormatString(p, "This player needs to be online in order to give the reward"));
							}
						}
						else {
							p.sendMessage(Utils.FormatString(p, "Incorrect use: /Ref Trigger MilestoneReward <player> <Milestone - min>"));
						}
					} else if (args[1].equalsIgnoreCase(GiveRewardCommands.RandomReward.toString())) {
						if (args.length == 3) {
							Player playerReward = Bukkit.getPlayer(args[2]);
							if (playerReward != null) {
								ReferralPro.Instance.rewards.GiveRandomRewards(p);
								p.sendMessage(Utils.FormatString(p, "Random reward given"));
							} else {
								p.sendMessage(Utils.FormatString(p, "This player needs to be online in order to give the reward"));
							}
						} else {
							p.sendMessage(Utils.FormatString(p, "Incorrect use: /Ref Trigger RandomReward <player>"));
						}
					}
				}
				break;
			case Trigger:
				
			
				break;
			default:
				
				break;
			}
		} else {
			// check arguments
			if (args.length > 1) {
				
				// To much info
		        return false;
		    } else if (args.length < 1) {
		    	// open menu
		    	p.openInventory(UIReferral.GUI(p));
		        return false;
		    } 
			
			ReferPlayer(p, args[0]);
		}
        
		return false;
	}
	
	private void ReferPlayerCode(Player player, String code) {
		// check length
    	if (code.length() < ConfigManager.instance.codeLength) {
    		player.sendMessage(Utils.FormatString(player, ConfigManager.instance.uIAnvilCodeShort));
            return;
		} // to long
    	else if (code.length() > ConfigManager.instance.codeLength) {
    		player.sendMessage(Utils.FormatString(player, ConfigManager.instance.uIAnvilCodeLong));
            return;
		}
    	
    	// Check if the code exists
    	if (!ReferralPro.Instance.db.CodeExists(code)) {
    		player.sendMessage(Utils.FormatString(player, ConfigManager.instance.uIAnvilCodeNotExist));
            return;
		}
    	
    	// Check if is own code
    	if (ReferralPro.Instance.db.GetPlayerCode(player.getUniqueId().toString()).equalsIgnoreCase(code)) {
    		player.sendMessage(Utils.FormatString(player, ConfigManager.instance.uIAnvilUseOwnCode));
            return;
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
		if (ReferralPro.Instance.db.PlayerReferrald(player.getUniqueId().toString())) {		
			
			
			// Check if the player try to refer each other
			if (!ReferralPro.Instance.getConfig().getBoolean("canReferEachOther") 
			  && ReferralPro.Instance.db.PlayerReferraldBy(player.getUniqueId().toString()).equalsIgnoreCase(UUIDs)) {	
				
				
		        //player.openInventory(UIAnvilResponse.GUI("You can't refer each other.", 1));
				player.sendMessage(Utils.FormatString(player, ConfigManager.instance.tryRefEachOther));
	            return;
			}
		}

		
		// Check if the player try to refer an older player
		if (!ReferralPro.Instance.getConfig().getBoolean("canReferOlderPlayer") && player.getFirstPlayed() > op.getFirstPlayed()) {			
	        //player.openInventory(UIAnvilResponse.GUI("You can't refer an older player!", 1));
			player.sendMessage(Utils.FormatString(player, ConfigManager.instance.tryRefOlderPlayer));
            return;
		}
					
		// Check if server uses time limit if so did the player play enough
		int pTime = ReferralPro.Instance.getConfig().getInt("minPlayTime");
        if (pTime != -1 && ((player.getLastPlayed() - player.getFirstPlayed()) / 60000) < pTime) {
	        //player.openInventory(UIAnvilResponse.GUI("You haven't played enough to use a refer", 1));
        	player.sendMessage(Utils.FormatString(player, ConfigManager.instance.notEnoughPlayTimeCode));
            return;
		}
        
        // Check if server uses time limit if so did the player play enough
		int maxTime = ReferralPro.Instance.getConfig().getInt("maxPlayTime");
        if (maxTime != -1 && ((player.getLastPlayed() - player.getFirstPlayed()) / 60000) > maxTime) {
	        //player.openInventory(UIAnvilResponse.GUI("You have played too much to use a refer", 1));
        	player.sendMessage(Utils.FormatString(player, ConfigManager.instance.tooMuchPlayTimeCode));
            return;
		}
    
        // Check if the server blocks same ips
        if (!ReferralPro.Instance.getConfig().getBoolean("allowSameIPRefer") && player.getAddress().getHostName().equals(ReferralPro.Instance.db.GetPlayerIP(UUIDs))) {
	        //player.openInventory(UIAnvilResponse.GUI("You can't refer on the same ip", 1));
        	player.sendMessage(Utils.FormatString(player, ConfigManager.instance.sameIPRefer));
            return;
		}
        
        // check if the other player is already referred
    	if (ReferralPro.Instance.db.PlayerReferrald(player.getUniqueId().toString())) {
			//player.openInventory(UIAnvilResponse.GUI("This Player already got referd.", 1));
    		player.sendMessage(Utils.FormatString(player, ConfigManager.instance.uIAnvilAlreadyRefed));
            return;
		}

    
		/////////////////////////////////////////////////////
		//
		//	Check for confirm
		//
		/////////////////////////////////////////////////////
    
    	if (ConfigManager.instance.codeConfirmation) {
    		// Open confirmation pannel
    	    player.openInventory(UICodeConfirm.GUI(ReferralPro.Instance.db.GetPlayersName(UUIDs)));
		} else {
			// get the players UUID
			String playerUUID = player.getUniqueId().toString();
			
			// Set the Referral
			ReferralPro.Instance.db.ReferralPlayer(UUIDs, playerUUID);

			// The player who got referred
			ReferralPro.Instance.db.CreateReward(new Reward(-999, playerUUID, UUIDs, 0));
			
			// The player who referred someone
			ReferralPro.Instance.db.CreateReward(new Reward(-999, UUIDs, playerUUID, 1));

			// Remove all open requests
			ReferralPro.Instance.db.RemovePlayerRequests(playerUUID);
			
			player.sendMessage(Utils.FormatString(player, ConfigManager.instance.uICodeRefed));
			
			ReferralPro.Instance.leaderboard.UpdateLeaderboard();
		}
    	
	}
	
	@SuppressWarnings("deprecation")
	private void ReferPlayer (Player player, String refP) {
		// get the player from the profile
		String profileUUID = ReferralPro.Instance.db.GetPlayersUUID(refP);

		OfflinePlayer op = null;
		
		if (profileUUID != null) {
			//convert
			UUID pUUID = UUID.fromString(profileUUID);
			// get offline p
			op = Bukkit.getOfflinePlayer(pUUID);
		} else {
			op = Bukkit.getOfflinePlayer(refP);
		}

    	// check if the player is online
    	if (op.getPlayer() == null) {
		
	        //* Add option to disalble it
		    if (!op.hasPlayedBefore()) {
		    	player.sendMessage(Utils.FormatString(player, ConfigManager.instance.uIAnvilNeverPlayed));
	            return;
		    }//*/
			
		}

        // turn the UUID to String
		String senderString = player.getUniqueId().toString();
		String receiverString = "";
		//
		if (op.isOnline()) {
			receiverString = op.getUniqueId().toString();
		} else {
			receiverString = ReferralPro.Instance.db.GetPlayersUUID(refP);
		}			
		
		// check if the other player is already referred
		if (ReferralPro.Instance.db.PlayerReferrald(receiverString)) {
			player.sendMessage(Utils.FormatString(player, ConfigManager.instance.uIAnvilAlreadyRefed));
            return;
		}
		
		
		// check if its not the same player
		if (senderString.compareTo(receiverString) == 0) {
	        player.sendMessage(Utils.FormatString(player, ConfigManager.instance.uIAnvilCantRefSelf));
            return;
		}
		
		// Check if the request already exists
		if (ReferralPro.Instance.db.RequestExists(senderString, receiverString)) {			
	        player.sendMessage(Utils.FormatString(player, ConfigManager.instance.uIAnvilAlreadyInQ));
            return;
		}

		/////////////////////////////////////////////////////
		//
		//	Config stuf start
		//
		/////////////////////////////////////////////////////
		
		// Check if player is referred
		if (ReferralPro.Instance.db.PlayerReferrald(player.getUniqueId().toString())) {
			
			String refedName = ReferralPro.Instance.db.GetPlayersName(ReferralPro.Instance.db.PlayerReferraldBy(player.getUniqueId().toString()));
					
			// Check if the player try to refer each other
			if (!ReferralPro.Instance.getConfig().getBoolean("canReferEachOther") && refedName.equalsIgnoreCase(refP)) {			
		        player.sendMessage(Utils.FormatString(op, ConfigManager.instance.tryRefEachOther));
	            return;
			}
		}
		
		// Check if the player try to refer an older player
		if (!ReferralPro.Instance.getConfig().getBoolean("canReferOlderPlayer") && player.getFirstPlayed() > op.getFirstPlayed()) {			
	        player.sendMessage(Utils.FormatString(op, ConfigManager.instance.tryRefOlderPlayer));
            return;
		}
		
		// Check if server uses time limit if so did the player play enough
		int minTime = ReferralPro.Instance.getConfig().getInt("minPlayTime");
        if (minTime != -1 && ((op.getLastPlayed() - op.getFirstPlayed()) / 60000) < minTime) {
	        player.sendMessage(Utils.FormatString(op, ConfigManager.instance.notEnoughPlayTime));
            return;
		}
        
        // Check if server uses time limit if so did the player play enough
			int maxTime = ReferralPro.Instance.getConfig().getInt("maxPlayTime");
	        if (maxTime != -1 && ((op.getLastPlayed() - op.getFirstPlayed()) / 60000) > maxTime) {
		        player.sendMessage(Utils.FormatString(op, ConfigManager.instance.tooMuchPlayTime));
            return;
			}
	        
	        
	        // Check if the server uses a max request
	        int maxRequests = ReferralPro.Instance.getConfig().getInt("maxPendingRequests");
	        if (maxRequests != -1 && ReferralPro.Instance.db.TotalRequests(senderString) >= maxRequests) {
	        player.sendMessage(Utils.FormatString(player, ConfigManager.instance.maxRequestSend));
            return;
		}
	        
	        // Check if the server blocks same ips
	        if (!ReferralPro.Instance.getConfig().getBoolean("allowSameIPRefer") && player.getAddress().getHostName().equals(ReferralPro.Instance.db.GetPlayerIP(receiverString))) {
	        player.sendMessage(Utils.FormatString(op, ConfigManager.instance.sameIPRefer));
            return;
		}
	        
	        
		/////////////////////////////////////////////////////
		//
		//	Config stuf end
		//
		/////////////////////////////////////////////////////
		
		// Check if the player blocked him
		if (ReferralPro.Instance.db.BlockExists(senderString, receiverString)) {			
	        player.sendMessage(Utils.FormatString(op, ConfigManager.instance.uIAnvilBlocked));
            return;
		}
		
		// Make request
		ReferralPro.Instance.db.CreateRequest(senderString, receiverString);
		
		// check if the player is online
		Player playerReceiver = ReferralPro.Instance.getServer().getPlayer(receiverString);
	    if (playerReceiver == null) {
	        // Not Online
	        player.sendMessage(Utils.FormatString(op, ConfigManager.instance.uIAnvilRequestQed));
            return;
	    }
	    		    
	    // If the player is online make a notification
	    //ReferralPro.Instance.commandReferral.CreateResponseBox(senderString, receiverString);

	    player.sendMessage(Utils.FormatString(op, ConfigManager.instance.uIAnvilInvited));
        return;		
	}
	
	private void TriggerCommands (CommandSender sender, String[] args) {  
		// /ref Trigger <Player> <Commands> <Commands> <Commands> <Commands>
		if (args.length < 3) {
			sender.sendMessage("incorrect use /ref Trigger <player> <Command>");
			return;
		}
		
		String playerUUIDLookup = ReferralPro.Instance.db.GetPlayersUUID(args[1]);
		if (playerUUIDLookup == null) {
			return;
		}
		
		String playerUUID = ReferralPro.Instance.db.PlayerReferraldBy(playerUUIDLookup); 
		if (playerUUID == null) {
			return;
		}
		
		String playerName = ReferralPro.Instance.db.GetPlayersName(playerUUID); 
		if (playerName == null) {
			return;
		}
		
		String strCommand = "";
		
		for (int i = 2; i < args.length; i++) {
			if (i >= 3) {
				strCommand += " ";	
			}
			strCommand += args[i];
		}
		
		sender.sendMessage("player: " + playerName);
		
		strCommand = strCommand.replace("%player%", playerName);
		
		sender.sendMessage("Command: " + strCommand);
		
		ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
		Bukkit.dispatchCommand(console, strCommand);
	}
	
	public enum Commands {
		Invites,
		Rewards,
		Blocked,
		Admin,
		Profile,
		Reload,
		Code,
		Top,
		Trigger,
		GiveReward
	}
	
	public enum GiveRewardCommands {
		Reward,
		MilestoneReward,
		RandomReward
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// Show the commands first 
		if (!cmd.getName().equalsIgnoreCase("Ref") && !cmd.getName().equalsIgnoreCase("Referral")) {
			return null;
		}
		
    	// make a list
    	ArrayList<String> availableCommands = new ArrayList<String>();
    	
    	if (args.length <= 1) {
    		for (Commands  command : Commands.values()) { 
        		if (command.toString().toLowerCase().contains(args[0].toLowerCase()) || args.length == 0) {
        			if ((command == Commands.Admin || command == Commands.Reload || command == Commands.Trigger || command == Commands.GiveReward) && !ReferralPro.perms.has(sender, "ReferralPro.Admin")) {
						continue;
					}
        			
                	availableCommands.add(command.toString());
    			}	
            }
		} else {
	    	// Command profile
	    	if (args[0].equalsIgnoreCase(Commands.Profile.toString()) && args.length == 2) {
	    		for (Player p : ReferralPro.Instance.getServer().getOnlinePlayers()) {
	        		if (p.getName().toString().toLowerCase().contains(args[1].toLowerCase())) {
	        			availableCommands.add(p.getName());
	        		}
				}
			}
	    	
	    	// Command GiveRewardCommands
	    	if (args[0].equalsIgnoreCase(Commands.GiveReward.toString()) && args.length >= 2 && ReferralPro.perms.has(sender, "ReferralPro.Admin")) {
	    		// show triggers
	    		if (args.length == 2) {
					for (GiveRewardCommands trigger : GiveRewardCommands.values()) { 
		        		if (trigger.toString().toLowerCase().contains(args[1].toLowerCase())) {
		                	availableCommands.add(trigger.toString());
		    			}	
		            }
				} 
	    		// default reward
	    		else if(args[1].equalsIgnoreCase(GiveRewardCommands.Reward.toString()) && (args.length >= 3 && args.length <= 4)) {
	    			if (args.length == 3) {
	    				availableCommands.add("Refer");
		    			availableCommands.add("Referred");
		    		} else if (args.length == 4){
			    		availableCommands = AddOnlinePlayers(availableCommands);
		    		} else{
						//availableCommands = ReferralPro.Instance.rewards.getAvailableRewards();
					}
	    		} 
				// 
				else if(args[1].equalsIgnoreCase(GiveRewardCommands.MilestoneReward.toString()) && (args.length == 3 || args.length == 4)) {
					if (args.length == 3) {
			    		availableCommands = AddOnlinePlayers(availableCommands, args[2]);
					} else {
						availableCommands = ReferralPro.Instance.rewards.getAvailableMilestoneRewards();
					}
	    		} else {
		    		availableCommands = AddOnlinePlayers(availableCommands);
	    		}
			}
	    	
		}
    	
    	
    	if (availableCommands.isEmpty()) {
    		availableCommands = AddOnlinePlayers(availableCommands, args[0]);
		}
    	    	
    	return availableCommands;
    }
	
	public ArrayList<String> AddOnlinePlayers(ArrayList<String> currentlist, String arg) {
		for (Player p : ReferralPro.Instance.getServer().getOnlinePlayers()) {
    		if (p.getName().toString().toLowerCase().contains(arg.toLowerCase())) {
        		currentlist.add(p.getName());
    		}
		}
		return currentlist;
	}
	
	public ArrayList<String> AddOnlinePlayers(ArrayList<String> currentlist) {
		for (Player p : ReferralPro.Instance.getServer().getOnlinePlayers()) {
        		currentlist.add(p.getName());
		}
		return currentlist;
	}
}
