package Me.Teenaapje.ReferralPro;

import java.util.List;
import java.util.UUID;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import Me.Teenaapje.ReferralPro.ConfigManager.ConfigManager;
import Me.Teenaapje.ReferralPro.DataBase.DataBase;
import Me.Teenaapje.ReferralPro.Listener.InventoryClickListener;
import Me.Teenaapje.ReferralPro.Listener.JoinListener;
import Me.Teenaapje.ReferralPro.PlaceHolder.Placeholders;
import Me.Teenaapje.ReferralPro.Rewards.Rewards;
import Me.Teenaapje.ReferralPro.UI.UIAdmin;
import Me.Teenaapje.ReferralPro.UI.UIAnvilResponse;
import Me.Teenaapje.ReferralPro.UI.UIBlocked;
import Me.Teenaapje.ReferralPro.UI.UICodeConfirm;
import Me.Teenaapje.ReferralPro.UI.UIProfile;
import Me.Teenaapje.ReferralPro.UI.UIReferInvites;
import Me.Teenaapje.ReferralPro.UI.UIReferral;
import Me.Teenaapje.ReferralPro.UI.UIReferralAccept;
import Me.Teenaapje.ReferralPro.UI.UIRewards;
import Me.Teenaapje.ReferralPro.Utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.permission.Permission;

public class ReferralPro extends JavaPlugin{
	
	public static ReferralPro Instance;
	
    public static Permission perms = null;
    
    public static boolean papiEnabled;

	public DataBase db;
	public JoinListener joinListener;
	public InventoryClickListener clickListener;
	public Referral referral;
	public Rewards rewards;
	
	
	public void onEnable() {
		Instance = this;
		
		// the placeholderapi check
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			Utils.SendMessage(null, null, "&2PlaceHolderAPI detected and support enabled");
			// make sure the bool gets set to true
            papiEnabled = true;
            // register the plugin
            new Placeholders().register();
        } else {
			Utils.SendMessage(null, null, "&4PlaceHolderAPI not detected!");
			// make sure the bool gets set to false
        	papiEnabled = false;
        }
		
		int pluginId = 7522;
		@SuppressWarnings("unused")
		Metrics metrics = new Metrics(this, pluginId);

		
		
		Initialize();
	}
	
	public int getone() {
		return 1;
	}
	
	// Initialize plugin
	public void Initialize () {
		// Load the config
		//getConfig().options().copyDefaults();
		saveDefaultConfig();
		
		ConfigManager.LoadConfigSettings();
		
		// The Database handling
		if (db != null) {
			db.CloseConnection();
		}
		this.db = new DataBase();
		
		// setup the vault 
		setupPermissions();
		
		
		/// The commands
		this.referral = new Referral();
		this.getCommand("Ref").setExecutor(this.referral);
		this.getCommand("Referral").setExecutor(this.referral);
		
		// The listener
		this.joinListener = new JoinListener();
		this.clickListener = new InventoryClickListener();
		
		// Initialize the UI's
		UIReferral.Initialize();
		UIReferInvites.Initialize();
		UIReferralAccept.Initialize();
		UIAnvilResponse.Initialize();
		UIProfile.Initialize();
		UIBlocked.Initialize();
		UIRewards.Initialize();
		UICodeConfirm.Initialize();
		UIAdmin.Initialize();
		
		this.rewards = new Rewards();	
	}
	
	public void Reload () {
		// reload config
		reloadConfig();
				
		Initialize();
	}
	
	public void ReloadConfig () {
		// reload config
		reloadConfig();

		// Load the config
		//getConfig().options().copyDefaults();
		saveDefaultConfig();
		
		ConfigManager.LoadConfigSettings();
	}
	
	public void onDisable() {
		if (getConfig().getBoolean("clearOnDisable")) {
			db.RemoveAllRequests();
		}
		
		db.CloseConnection();
	}
	
	public void UseCommand(List<?> list, Player player) {
		// Give the player his rewards
		for (int i = 0; i < list.size(); i++) {
			String command = (String) list.get(i);

			getServer().dispatchCommand(getServer().getConsoleSender(), command.toLowerCase().replace("<player>", player.getName()));
		}
	}
	
	private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
	
	// give player a notification
	public void PlayerHasBox(String receiverUUID) {
		// get the players
		Player playerReceiver = this.getServer().getPlayer(UUID.fromString(receiverUUID));
		
		/// create text box
        TextComponent Accept = new TextComponent("Open invites");
        Accept.setColor(ChatColor.GREEN);
        Accept.setBold(true);
        Accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ref invites"));
		
        // send to reveiver
        playerReceiver.sendMessage("You have open referrals");
        playerReceiver.spigot().sendMessage(Accept);    

        
        return;
	}

}
