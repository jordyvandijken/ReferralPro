package Me.Teenaapje.ReferralPro.Listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import Me.Teenaapje.ReferralPro.ReferralPro;
import Me.Teenaapje.ReferralPro.UI.UIAdmin;
import Me.Teenaapje.ReferralPro.UI.UIAnvilResponse;
import Me.Teenaapje.ReferralPro.UI.UIBlocked;
import Me.Teenaapje.ReferralPro.UI.UICodeConfirm;
import Me.Teenaapje.ReferralPro.UI.UIProfile;
import Me.Teenaapje.ReferralPro.UI.UIReferInvites;
import Me.Teenaapje.ReferralPro.UI.UIReferral;
import Me.Teenaapje.ReferralPro.UI.UIReferralAccept;
import Me.Teenaapje.ReferralPro.UI.UIRewards;

public class InventoryClickListener implements Listener {

	ReferralPro referralPro;
	
	public InventoryClickListener () {
		this.referralPro = ReferralPro.Instance;
		Bukkit.getPluginManager().registerEvents(this, referralPro);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		// check if click was on window
		if (e == null) {
			return;
		}
		
		
		
		String title = e.getView().getTitle();

		if (title.equals(UIReferral.invName) 		||
			title.equals(UIReferInvites.invName) 	|| 
			title.equals(UIReferralAccept.invName) 	|| 
			title.equals(UIAnvilResponse.invName) 	||
			title.equals(UIProfile.invName) 		||
			title.equals(UIBlocked.invName)			||
			title.equals(UIRewards.invName) 		||
			title.equals(UICodeConfirm.invName) 	||
			title.equals(UIAdmin.invName)) {
		
			e.setCancelled(true);

			if (e.getCurrentItem() == null || e.getInventory() == null || e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
				return;
			}
			
			
			// The Main UI
			if (title.equals(UIReferral.invName)) {
				UIReferral.Clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
				return;
			}
			
			// The UI where the referral invites are
			if (title.equals(UIReferInvites.invName)) {
				UIReferInvites.Clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
				return;
			}
			
			// The UI where the player can accept the referral
			if (title.equals(UIReferralAccept.invName)) {		
				UIReferralAccept.Clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
				return;
			}
			
			// The UI where the player gets response from the anvil
			if (title.equals(UIAnvilResponse.invName)) {
				UIAnvilResponse.Clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
				return;
			}
			
			// The UI where the player can see its profile
			if (title.equals(UIProfile.invName)) {
				UIProfile.Clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
				return;
			}
			
			// The UI where the player can see the blocked list
			if (title.equals(UIBlocked.invName)) {
				UIBlocked.Clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
				return;
			}
			
			// The UI where the player can see the Rewards
			if (title.equals(UIRewards.invName)) {
				UIRewards.Clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
				return;
			}
			
			// The UI where the player can see the Code confirm
			if (title.equals(UICodeConfirm.invName)) {
				UICodeConfirm.Clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
				return;
			}
			
			// The UI where the player can see the admin panel
			if (title.equals(UIAdmin.invName)) {
				UIAdmin.Clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
				return;
			}
		}
    }
}
