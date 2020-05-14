package Me.Teenaapje.ReferralPro.PlaceHolder;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import Me.Teenaapje.ReferralPro.ReferralPro;
import Me.Teenaapje.ReferralPro.ConfigManager.ConfigManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;


public class Placeholders extends PlaceholderExpansion {

    private ReferralPro plugin;

    /**
     * Since we register the expansion inside our own plugin, we
     * can simply use this method here to get an instance of our
     * plugin.
     *
     * @param plugin
     *        The instance of our plugin.
     */
    public Placeholders(){
        this.plugin = ReferralPro.Instance;
    }

    /**
     * Because this is an internal class,
     * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
     * PlaceholderAPI is reloaded
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist(){
        return true;
    }

    /**
     * Because this is a internal class, this check is not needed
     * and we can simply return {@code true}
     *
     * @return Always true since it's an internal class.
     */
    @Override
    public boolean canRegister(){
        return true;
    }

    /**
     * The name of the person who created this expansion should go here.
     * <br>For convienience do we return the author from the plugin.yml
     * 
     * @return The name of the author as a String.
     */
    @Override
    public String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest 
     * method to obtain a value if a placeholder starts with our 
     * identifier.
     * <br>This must be unique and can not contain % or _
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public String getIdentifier(){
        return "referralpro";
    }

    /**
     * This is the version of the expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * For convienience do we return the version from the plugin.yml
     *
     * @return The version as a String.
     */
    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }

    /**
     * This is the method called when a placeholder with our identifier 
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param  player
     *         A {@link org.bukkit.Player Player}.
     * @param  identifier
     *         A String containing the identifier/value.
     *
     * @return possibly-null String of the requested identifier.
     */
    @Override
    public String onPlaceholderRequest(Player player, String identifier){
        if(player == null){
            return "";
        }
        
        String UUIDs = player.getUniqueId().toString();

        // %referralpro_total%
        if(identifier.equals("total")){
            return Integer.toString(plugin.db.GetReferrals(UUIDs));
        }

        // %referralpro_referred%
        if(identifier.equals("referred")){
        	if (plugin.db.PlayerReferrald(UUIDs)) {
				return ConfigManager.placeHolderGotRefed;
			}
            return ConfigManager.placeHolderNotRefed;
        }
        
        // %referralpro_refedby%
        if(identifier.equals("refedby")){
        	if (!plugin.db.PlayerReferrald(UUIDs)) {
				return "not referred";
			}
        	
            return plugin.db.GetPlayersName(plugin.db.PlayerReferraldBy(UUIDs));
        }
        
	    // %referralpro_nextmilepoint%
        if(identifier.equals("nextmilepoint")){
            return Integer.toString(ReferralPro.Instance.rewards.NextMileRewardTotal(UUIDs));
        }
        
        // %referralpro_nextmileneeded%
        if(identifier.equals("nextmileneeded")){
            return Integer.toString(ReferralPro.Instance.rewards.NextMileReward(UUIDs));
        }
        
        // %referralpro_refcode%
        if(identifier.equals("refcode")){
            return ReferralPro.Instance.db.GetPlayerCode(UUIDs);
        }
 
        // We return null if an invalid placeholder (f.e. %someplugin_placeholder3%) 
        // was provided
        return null;
    }
    
    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
    	if(player == null) {
            return "Non";
        }

    	String UUIDs = player.getUniqueId().toString();

        // %referralpro_total%
        if(identifier.equals("total")){
            return Integer.toString(plugin.db.GetReferrals(UUIDs));
        }

        // %referralpro_referred%
        if(identifier.equals("referred")){
        	if (plugin.db.PlayerReferrald(UUIDs)) {
				return ConfigManager.placeHolderGotRefed;
			}
            return ConfigManager.placeHolderNotRefed;
        }
        
        // %referralpro_refedby%
        if(identifier.equals("refedby")){
        	if (!plugin.db.PlayerReferrald(UUIDs)) {
				return "Non";
			}
        	
            return plugin.db.GetPlayersName(plugin.db.PlayerReferraldBy(UUIDs));
        }
        
        // %referralpro_nextmilepoint%
        if(identifier.equals("nextmilepoint")){
            return Integer.toString(ReferralPro.Instance.rewards.NextMileRewardTotal(UUIDs));
        }
        
        // %referralpro_nextmileneeded%
        if(identifier.equals("nextmileneeded")){
            return Integer.toString(ReferralPro.Instance.rewards.NextMileReward(UUIDs));
        }
        
        // %referralpro_refcode%
        if(identifier.equals("refcode")){
            return ReferralPro.Instance.db.GetPlayerCode(UUIDs);
        }

        return null;
    }
}