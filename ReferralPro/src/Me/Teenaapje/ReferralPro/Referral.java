package Me.Teenaapje.ReferralPro;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import Me.Teenaapje.ReferralPro.UI.UIAdmin;
import Me.Teenaapje.ReferralPro.UI.UIAnvil;
import Me.Teenaapje.ReferralPro.UI.UIAnvilCode;
import Me.Teenaapje.ReferralPro.UI.UIBlocked;
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
        
       
        Player p = (Player)sender;
        
    	// check if is player
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can use this command.");
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

		
		if (chosenCommand != null) {
			switch (chosenCommand) {
			case Admin:
				if ((sender instanceof Player) && !ReferralPro.perms.has(sender, "Referral.Admin")) {
					break;
				}
				p.openInventory(UIAdmin.GUI(p));
				
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
				if (args.length == 1) {
					p.sendMessage("Your code is: " + ReferralPro.Instance.db.GetPlayerCode(p.getUniqueId().toString()));
				} else {
					UIAnvilCode.GUI(p, args[1]);
				}

				
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
			
			UIAnvil.GUI(p, args[0].substring(0, args[0].length() - 1));
		}
        
		return false;
	}
	
	
	public enum Commands {
		Invites,
		Rewards,
		Blocked,
		Admin,
		Profile,
		Reload,
		Code
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
        			if ((command == Commands.Admin || command == Commands.Reload) && !ReferralPro.perms.has(sender, "ReferralPro.Admin")) {
						continue;
					}
        			
                	availableCommands.add(command.toString());
    			}	
            }
		}
    	
    	if (args[0].equalsIgnoreCase(Commands.Profile.toString()) && args.length == 2) {
    		for (Player p : ReferralPro.Instance.getServer().getOnlinePlayers()) {
        		if (p.getName().toString().toLowerCase().contains(args[1].toLowerCase())) {
        			availableCommands.add(p.getName());
        		}
			}
		}
    	
    	if (availableCommands.isEmpty()) {
    		for (Player p : ReferralPro.Instance.getServer().getOnlinePlayers()) {
        		if (p.getName().toString().toLowerCase().contains(args[0].toLowerCase())) {
        			availableCommands.add(p.getName());
        		}
			}
		}
    	
    	
    	return availableCommands;


        //return null;
    }
}
