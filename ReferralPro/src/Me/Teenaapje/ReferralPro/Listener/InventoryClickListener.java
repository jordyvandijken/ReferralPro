package Me.Teenaapje.ReferralPro.Listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import Me.Teenaapje.ReferralPro.ReferralPro;
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
		String title = e.getView().getTitle();


		// The Main UI
		if (title.equals(UIReferral.invName)) {
			e.setCancelled(true);
			if (e.getCurrentItem() == null || e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
				return;
			}
			UIReferral.Clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
		}
		
		// The UI where the referral invites are
		if (title.equals(UIReferInvites.invName)) {
			e.setCancelled(true);
			if (e.getCurrentItem() == null || e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
				return;
			}
			UIReferInvites.Clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
		}
		
		// The UI where the player can accept the referral
		if (title.equals(UIReferralAccept.invName)) {
			e.setCancelled(true);
			if (e.getCurrentItem() == null || e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
				return;
			}
			UIReferralAccept.Clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
		}
		
		// The UI where the player gets response from the anvil
		if (title.equals(UIAnvilResponse.invName)) {
			e.setCancelled(true);
			if (e.getCurrentItem() == null || e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
				return;
			}
			UIAnvilResponse.Clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
		}
		
		// The UI where the player can see its profile
		if (title.equals(UIProfile.invName)) {
			e.setCancelled(true);
			if (e.getCurrentItem() == null || e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
				return;
			}
			UIProfile.Clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
		}
		
		// The UI where the player can see the blocked list
		if (title.equals(UIBlocked.invName)) {
			e.setCancelled(true);
			if (e.getCurrentItem() == null || e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
				return;
			}
			UIBlocked.Clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
		}
		
		// The UI where the player can see the blocked list
		if (title.equals(UIRewards.invName)) {
			e.setCancelled(true);
			if (e.getCurrentItem() == null || e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
				return;
			}
			UIRewards.Clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
		}
		
		// The UI where the player can see the blocked list
		if (title.equals(UICodeConfirm.invName)) {
			e.setCancelled(true);
			if (e.getCurrentItem() == null || e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
				return;
			}
			UICodeConfirm.Clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
		}
    }
}
