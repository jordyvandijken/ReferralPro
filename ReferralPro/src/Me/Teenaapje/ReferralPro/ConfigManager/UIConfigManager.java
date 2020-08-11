package Me.Teenaapje.ReferralPro.ConfigManager;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import Me.Teenaapje.ReferralPro.ReferralPro;

public class UIConfigManager {
	public static UIConfigManager instance;
	
    private static File file;
    private static FileConfiguration customFile;
 
    public UIConfigManager () {
    	instance = this;
    }
    
    //Finds or generates the custom config file
    public void Initialize(){
        file = new File(ReferralPro.Instance.getDataFolder(), "customui.yml");
 
        if (!file.exists()){
            try{
                file.createNewFile();
            }catch (IOException e){
                //owww
            }
        }
        customFile = YamlConfiguration.loadConfiguration(file);
        
        SetDefaults();
    }
    
    public void SetDefaults() {
    	// enable plugin
    	//customFile.addDefault("enableplugin", true);
    	
    	if (!customFile.contains("ui.admin")) {
        	UIAdmin();
		}
    	
    	if (!customFile.contains("ui.anvilresponse")) {
    		UIAnvilResponse();
    	}
    	
    	if (!customFile.contains("ui.blocked")) {
    		UIBlocked();
    	}
    	
    	if (!customFile.contains("ui.anvilcodecon")) {
    		UICodeConfirm();
    	}
    	
    	if (!customFile.contains("ui.profile")) {
    		UIProfile();
    	}
    	
    	if (!customFile.contains("ui.refinvites")) {
    		UIRefInvites();
    	}
    	
    	if (!customFile.contains("ui.referral")) {
    		UIRefer();
    	}
    	
    	if (!customFile.contains("ui.refaccept")) {
    		UIReferAccept();
    	}
    	
    	if (!customFile.contains("ui.refrewards")) {
    		UIRewards();
    	}
    	
    	customFile.options().copyDefaults(true);
    	save();
    }
    
    public void UIAdmin() {
    	// rows
    	customFile.addDefault("ui.admin.rows", 3);
    	customFile.addDefault("ui.admin.enable", true);
    	
    	
    	// resetall players
    	customFile.addDefault("ui.admin.buttons.4.buttontype", "resetallplayers");
    	customFile.addDefault("ui.admin.buttons.4.buttonitem", "TNT");
    	customFile.addDefault("ui.admin.buttons.4.enable", true);
    	
    	// resetall players code
    	customFile.addDefault("ui.admin.buttons.12.buttontype", "resetallplayercodes");
    	customFile.addDefault("ui.admin.buttons.12.buttonitem", "TNT");
    	customFile.addDefault("ui.admin.buttons.12.enable", true);

    	// resetall players request
    	customFile.addDefault("ui.admin.buttons.14.buttontype", "resetallplayerrequests");
    	customFile.addDefault("ui.admin.buttons.14.buttonitem", "TNT");
    	customFile.addDefault("ui.admin.buttons.14.enable", true);

    	// resetall players reward
    	customFile.addDefault("ui.admin.buttons.22.buttontype", "resetallplayerrewards");
    	customFile.addDefault("ui.admin.buttons.22.buttonitem", "TNT");
    	customFile.addDefault("ui.admin.buttons.22.enable", true);

    	// look up player
    	customFile.addDefault("ui.admin.buttons.13.buttontype", "lookupplayer");
    	customFile.addDefault("ui.admin.buttons.13.buttonitem", "NAME_TAG");
    	customFile.addDefault("ui.admin.buttons.13.enable", true);

    	
    	// back
    	customFile.addDefault("ui.admin.buttons.18.buttontype", "back");
    	customFile.addDefault("ui.admin.buttons.18.buttonitem", "OAK_DOOR");
    	customFile.addDefault("ui.admin.buttons.18.enable", true);

    	// close
    	customFile.addDefault("ui.admin.buttons.26.buttontype", "close");
    	customFile.addDefault("ui.admin.buttons.26.buttonitem", "IRON_DOOR");
    	customFile.addDefault("ui.admin.buttons.26.enable", true);

    	
    	// add at least one filler example
    	customFile.addDefault("ui.admin.fillers.0.filleritem", "none");
    }
 
    public void UIAnvilResponse() {
    	// rows
    	customFile.addDefault("ui.anvilresponse.rows", 3);
    	customFile.addDefault("ui.anvilresponse.enable", true);

    	
    	// resetall players reward
    	customFile.addDefault("ui.anvilresponse.buttons.13.buttontype", "anvilsucces");
    	customFile.addDefault("ui.anvilresponse.buttons.13.enable", true);

    	// look up player
    	customFile.addDefault("ui.anvilresponse.buttons.22.buttontype", "retry");
    	customFile.addDefault("ui.anvilresponse.buttons.22.buttonitem", "EXPERIENCE_BOTTLE");
    	customFile.addDefault("ui.anvilresponse.buttons.22.enable", true);
    	
    	// back
    	customFile.addDefault("ui.anvilresponse.buttons.18.buttontype", "back");
    	customFile.addDefault("ui.anvilresponse.buttons.18.buttonitem", "OAK_DOOR");
    	customFile.addDefault("ui.anvilresponse.buttons.18.enable", true);

    	// close
    	customFile.addDefault("ui.anvilresponse.buttons.26.buttontype", "close");
    	customFile.addDefault("ui.anvilresponse.buttons.26.buttonitem", "IRON_DOOR");
    	customFile.addDefault("ui.anvilresponse.buttons.26.enable", true);
    	
    	
    	customFile.addDefault("ui.anvilresponse.fillers.0.filleritem", "none");

    }
    
    public void UIBlocked() {
    	// rows
    	customFile.addDefault("ui.blocked.rows", 3);
    	customFile.addDefault("ui.blocked.enable", true);


    	// back
    	customFile.addDefault("ui.blocked.buttons.18.buttontype", "back");
    	customFile.addDefault("ui.blocked.buttons.18.buttonitem", "OAK_DOOR");
    	customFile.addDefault("ui.blocked.buttons.18.enable", true);

    	// close
    	customFile.addDefault("ui.blocked.buttons.26.buttontype", "close");
    	customFile.addDefault("ui.blocked.buttons.26.buttonitem", "IRON_DOOR");
    	customFile.addDefault("ui.blocked.buttons.26.enable", true);
    	
    	// none
    	customFile.addDefault("ui.blocked.buttons.13.buttontype", "none");
    	customFile.addDefault("ui.blocked.buttons.13.buttonitem", "BARRIER");
    	customFile.addDefault("ui.blocked.buttons.13.enable", true);

    	// return
    	customFile.addDefault("ui.blocked.buttons.21.buttontype", "return");
    	customFile.addDefault("ui.blocked.buttons.21.enable", true);

    	// page
    	customFile.addDefault("ui.blocked.buttons.22.buttontype", "page");
    	customFile.addDefault("ui.blocked.buttons.22.buttonitem", "OAK_SIGN");
    	customFile.addDefault("ui.blocked.buttons.22.enable", true);

    	// next
    	customFile.addDefault("ui.blocked.buttons.23.buttontype", "next");
    	customFile.addDefault("ui.blocked.buttons.23.enable", true);

    	
    	customFile.addDefault("ui.blocked.fillers.0.filleritem", "none");

    }
    
    public void UICodeConfirm() {
    	// rows
    	customFile.addDefault("ui.anvilcodecon.rows", 3);
    	customFile.addDefault("ui.anvilcodecon.enable", true);

    	
    	// resetall players reward
    	customFile.addDefault("ui.anvilcodecon.buttons.13.buttontype", "playerhead");
    	customFile.addDefault("ui.anvilcodecon.buttons.13.enable", true);

    	// yes
    	customFile.addDefault("ui.anvilcodecon.buttons.4.buttontype", "yes");
    	customFile.addDefault("ui.anvilcodecon.buttons.4.buttonitem", "GREEN_STAINED_GLASS");
    	customFile.addDefault("ui.anvilcodecon.buttons.4.enable", true);

    	// retry
    	customFile.addDefault("ui.anvilcodecon.buttons.22.buttontype", "retry");
    	customFile.addDefault("ui.anvilcodecon.buttons.22.buttonitem", "EXPERIENCE_BOTTLE");
    	customFile.addDefault("ui.anvilcodecon.buttons.22.enable", true);

    	// back
    	customFile.addDefault("ui.anvilcodecon.buttons.18.buttontype", "back");
    	customFile.addDefault("ui.anvilcodecon.buttons.18.buttonitem", "OAK_DOOR");
    	customFile.addDefault("ui.anvilcodecon.buttons.18.enable", true);

    	// close
    	customFile.addDefault("ui.anvilcodecon.buttons.26.buttontype", "close");
    	customFile.addDefault("ui.anvilcodecon.buttons.26.buttonitem", "IRON_DOOR");
    	customFile.addDefault("ui.anvilcodecon.buttons.26.enable", true);

    	
    	customFile.addDefault("ui.anvilcodecon.fillers.0.filleritem", "none");

    }
    
    public void UIProfile() {
    	// rows
    	customFile.addDefault("ui.profile.rows", 3);
    	customFile.addDefault("ui.profile.enable", true);

    	
    	// resetall players reward
    	customFile.addDefault("ui.profile.buttons.13.buttontype", "playerhead");
    	customFile.addDefault("ui.profile.buttons.13.enable", true);

    	// total
    	customFile.addDefault("ui.profile.buttons.4.buttontype", "profiletotal");
    	customFile.addDefault("ui.profile.buttons.4.buttonitem", "NAME_TAG");
    	customFile.addDefault("ui.profile.buttons.4.enable", true);

    	// back
    	customFile.addDefault("ui.profile.buttons.18.buttontype", "back");
    	customFile.addDefault("ui.profile.buttons.18.buttonitem", "OAK_DOOR");
    	customFile.addDefault("ui.profile.buttons.18.enable", true);

    	// close
    	customFile.addDefault("ui.profile.buttons.26.buttontype", "close");
    	customFile.addDefault("ui.profile.buttons.26.buttonitem", "IRON_DOOR");
    	customFile.addDefault("ui.profile.buttons.26.enable", true);

    	// rewards
    	customFile.addDefault("ui.profile.buttons.12.buttontype", "rewards");
    	customFile.addDefault("ui.profile.buttons.12.buttonitem", "CHEST");
    	customFile.addDefault("ui.profile.buttons.12.enable", true);

    	// milerewards
    	customFile.addDefault("ui.profile.buttons.14.buttontype", "milerewards");
    	customFile.addDefault("ui.profile.buttons.14.buttonitem", "ENDER_CHEST");
    	customFile.addDefault("ui.profile.buttons.14.enable", true);

    	// blocked
    	customFile.addDefault("ui.profile.buttons.22.buttontype", "blocked");
    	customFile.addDefault("ui.profile.buttons.22.buttonitem", "BARRIER");
    	customFile.addDefault("ui.profile.buttons.22.enable", true);

    	
    	
    	// resetall players
    	customFile.addDefault("ui.profile.buttons.2.buttontype", "resetallplayers");
    	customFile.addDefault("ui.profile.buttons.2.buttonitem", "TNT");
    	customFile.addDefault("ui.profile.buttons.2.enable", true);

    	// resetall players code
    	customFile.addDefault("ui.profile.buttons.6.buttontype", "resetallplayercodes");
    	customFile.addDefault("ui.profile.buttons.6.buttonitem", "TNT");
    	customFile.addDefault("ui.profile.buttons.6.enable", true);

    	// resetall players request
    	customFile.addDefault("ui.profile.buttons.11.buttontype", "resetallplayerrequests");
    	customFile.addDefault("ui.profile.buttons.11.buttonitem", "TNT");
    	customFile.addDefault("ui.profile.buttons.11.enable", true);

    	// resetall players reward
    	customFile.addDefault("ui.profile.buttons.15.buttontype", "resetallplayerrewards");
    	customFile.addDefault("ui.profile.buttons.15.buttonitem", "TNT");
    	customFile.addDefault("ui.profile.buttons.15.enable", true);

    	// Back to admin panel
    	customFile.addDefault("ui.profile.buttons.0.buttontype", "adminpanel");
    	customFile.addDefault("ui.profile.buttons.0.buttonitem", "COMMAND_BLOCK");
    	customFile.addDefault("ui.profile.buttons.0.enable", true);

    	
    	customFile.addDefault("ui.profile.fillers.0.filleritem", "none");

    }
    
    public void UIRefInvites() {
    	// rows
    	customFile.addDefault("ui.refinvites.rows", 3);
    	customFile.addDefault("ui.refinvites.enable", true);


    	// back
    	customFile.addDefault("ui.refinvites.buttons.18.buttontype", "back");
    	customFile.addDefault("ui.refinvites.buttons.18.buttonitem", "OAK_DOOR");
    	customFile.addDefault("ui.refinvites.buttons.18.enable", true);

    	// close
    	customFile.addDefault("ui.refinvites.buttons.26.buttontype", "close");
    	customFile.addDefault("ui.refinvites.buttons.26.buttonitem", "IRON_DOOR");
    	customFile.addDefault("ui.refinvites.buttons.26.enable", true);
    	
    	// none
    	customFile.addDefault("ui.refinvites.buttons.13.buttontype", "none");
    	customFile.addDefault("ui.refinvites.buttons.13.buttonitem", "BARRIER");
    	customFile.addDefault("ui.refinvites.buttons.13.enable", true);
    	
    	// return
    	customFile.addDefault("ui.refinvites.buttons.21.buttontype", "return");
    	customFile.addDefault("ui.refinvites.buttons.21.enable", true);

    	// page
    	customFile.addDefault("ui.refinvites.buttons.22.buttontype", "page");
    	customFile.addDefault("ui.refinvites.buttons.22.buttonitem", "OAK_SIGN");
    	customFile.addDefault("ui.refinvites.buttons.22.enable", true);

    	// next
    	customFile.addDefault("ui.refinvites.buttons.23.buttontype", "next");
    	customFile.addDefault("ui.refinvites.buttons.23.enable", true);

    	
    	customFile.addDefault("ui.refinvites.fillers.0.filleritem", "none");

    }
    
    public void UIRefer() {
    	// rows
    	customFile.addDefault("ui.referral.rows", 3);
    	customFile.addDefault("ui.referral.enable", true);



    	// close
    	customFile.addDefault("ui.referral.buttons.26.buttontype", "close");
    	customFile.addDefault("ui.referral.buttons.26.buttonitem", "IRON_DOOR");
    	customFile.addDefault("ui.referral.buttons.26.enable", true);

    	// invites
    	customFile.addDefault("ui.referral.buttons.11.buttontype", "invites");
    	customFile.addDefault("ui.referral.buttons.11.buttonitem", "SHULKER_SHELL");
    	customFile.addDefault("ui.referral.buttons.11.enable", true);

    	// refer
    	customFile.addDefault("ui.referral.buttons.15.buttontype", "refer");
    	customFile.addDefault("ui.referral.buttons.15.buttonitem", "NAME_TAG");
    	customFile.addDefault("ui.referral.buttons.15.enable", true);

    	// refer code
    	customFile.addDefault("ui.referral.buttons.4.buttontype", "refercode");
    	customFile.addDefault("ui.referral.buttons.4.buttonitem", "NAME_TAG");
    	customFile.addDefault("ui.referral.buttons.4.enable", true);

    	// admin pannel
    	customFile.addDefault("ui.referral.buttons.18.buttontype", "adminpanel");
    	customFile.addDefault("ui.referral.buttons.18.buttonitem", "COMMAND_BLOCK");
    	customFile.addDefault("ui.referral.buttons.18.enable", true);

    	// player head
    	customFile.addDefault("ui.referral.buttons.13.buttontype", "playerhead");
    	customFile.addDefault("ui.referral.buttons.13.enable", true);

    	customFile.addDefault("ui.referral.fillers.0.filleritem", "none");

    }
    
    public void UIReferAccept() {
    	// rows
    	customFile.addDefault("ui.refaccept.rows", 3);
    	customFile.addDefault("ui.refaccept.enable", true);


    	// back
    	customFile.addDefault("ui.refaccept.buttons.18.buttontype", "back");
    	customFile.addDefault("ui.refaccept.buttons.18.buttonitem", "OAK_DOOR");
    	customFile.addDefault("ui.refaccept.buttons.18.enable", true);

    	// close
    	customFile.addDefault("ui.refaccept.buttons.26.buttontype", "close");
    	customFile.addDefault("ui.refaccept.buttons.26.buttonitem", "IRON_DOOR");
    	customFile.addDefault("ui.refaccept.buttons.26.enable", true);

    	// block
    	customFile.addDefault("ui.refaccept.buttons.22.buttontype", "block");
    	customFile.addDefault("ui.refaccept.buttons.22.buttonitem", "BARRIER");
    	customFile.addDefault("ui.refaccept.buttons.22.enable", true);

    	// yes
    	customFile.addDefault("ui.refaccept.buttons.11.buttontype", "yes");
    	customFile.addDefault("ui.refaccept.buttons.11.buttonitem", "GREEN_STAINED_GLASS");
    	customFile.addDefault("ui.refaccept.buttons.11.enable", true);

    	// no
    	customFile.addDefault("ui.refaccept.buttons.15.buttontype", "no");
    	customFile.addDefault("ui.refaccept.buttons.15.buttonitem", "RED_STAINED_GLASS");
    	customFile.addDefault("ui.refaccept.buttons.15.enable", true);

    	// player head
    	customFile.addDefault("ui.refaccept.buttons.13.buttontype", "playerhead");
    	customFile.addDefault("ui.refaccept.buttons.13.enable", true);

    	customFile.addDefault("ui.refaccept.fillers.0.filleritem", "none");

    }
    
    public void UIRewards() {
    	// rows
    	customFile.addDefault("ui.refrewards.rows", 3);
    	customFile.addDefault("ui.refrewards.enable", true);


    	// back
    	customFile.addDefault("ui.refrewards.buttons.18.buttontype", "back");
    	customFile.addDefault("ui.refrewards.buttons.18.buttonitem", "OAK_DOOR");
    	customFile.addDefault("ui.refrewards.buttons.18.enable", true);

    	// close
    	customFile.addDefault("ui.refrewards.buttons.26.buttontype", "close");
    	customFile.addDefault("ui.refrewards.buttons.26.buttonitem", "IRON_DOOR");
    	customFile.addDefault("ui.refrewards.buttons.26.enable", true);

    	// none
    	customFile.addDefault("ui.refrewards.buttons.13.buttontype", "none");
    	customFile.addDefault("ui.refrewards.buttons.13.buttonitem", "BARRIER");
    	customFile.addDefault("ui.refrewards.buttons.13.enable", true);

    	// return
    	customFile.addDefault("ui.refrewards.buttons.21.buttontype", "return");
    	customFile.addDefault("ui.refrewards.buttons.21.enable", true);

    	// page
    	customFile.addDefault("ui.refrewards.buttons.22.buttontype", "page");
    	customFile.addDefault("ui.refrewards.buttons.22.buttonitem", "OAK_SIGN");
    	customFile.addDefault("ui.refrewards.buttons.22.enable", true);

    	// next
    	customFile.addDefault("ui.refrewards.buttons.23.buttontype", "next");
    	customFile.addDefault("ui.refrewards.buttons.23.enable", true);

    	
    	customFile.addDefault("ui.refrewards.fillers.0.filleritem", "none");

    }
    
    public FileConfiguration get(){
        return customFile;
    }
 
    public void save(){
        try{
            customFile.save(file);
        }catch (IOException e){
            System.out.println("Couldn't save file");
        }
    }
 
    public void reload(){
        customFile = YamlConfiguration.loadConfiguration(file);
    }
}