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
    }
    
    public void SetDefaults() {
    	// enable plugin
    	customFile.addDefault("ui.enableplugin", true);
    	
    	UIAdmin();
    	
    	
    }
    
    public void UIAdmin() {
    	// rows
    	customFile.addDefault("ui.admin.rows", 3);
    	// resetall players
    	customFile.addDefault("ui.admin.buttons.4.buttontype", "resetallplayers");
    	customFile.addDefault("ui.admin.buttons.4.buttonitem", "TNT");
    	// resetall players code
    	customFile.addDefault("ui.admin.buttons.12.buttontype", "resetallplayercodes");
    	customFile.addDefault("ui.admin.buttons.12.buttonitem", "TNT");
    	// resetall players request
    	customFile.addDefault("ui.admin.buttons.14.buttontype", "resetallplayerrequests");
    	customFile.addDefault("ui.admin.buttons.14.buttonitem", "TNT");
    	// resetall players reward
    	customFile.addDefault("ui.admin.buttons.22.buttontype", "resetallplayerrewards");
    	customFile.addDefault("ui.admin.buttons.22.buttonitem", "TNT");
    	// look up player
    	customFile.addDefault("ui.admin.buttons.13.buttontype", "lookupplayer");
    	customFile.addDefault("ui.admin.buttons.13.buttonitem", "NAME_TAG");
    	
    	// back
    	customFile.addDefault("ui.admin.buttons.18.buttontype", "back");
    	customFile.addDefault("ui.admin.buttons.18.buttonitem", "OAK_DOOR");
    	// close
    	customFile.addDefault("ui.admin.buttons.26.buttontype", "close");
    	customFile.addDefault("ui.admin.buttons.26.buttonitem", "IRON_DOOR");
    	
    	// add at least one filler example
    	customFile.addDefault("ui.admin.fillers.13.filleritem", "WHITE_WOOL");
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