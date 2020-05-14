package Me.Teenaapje.ReferralPro.Rewards;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import Me.Teenaapje.ReferralPro.ReferralPro;

public class Rewards {
	
	ReferralPro referralPro;
	
	List<RankRewards> senderRewards;
	List<RankRewards> receiverRewards;
	
	
	List<MileStoneRewards> mileStoneRewards;
	
	public Rewards() {
		referralPro = ReferralPro.Instance;
		
		senderRewards = new ArrayList<RankRewards>();
		receiverRewards = new ArrayList<RankRewards>();
		mileStoneRewards = new ArrayList<MileStoneRewards>();
		
		LoadRewards();
	}
	
	public void LoadRewards() {
		senderRewards.clear();
		receiverRewards.clear();
		mileStoneRewards.clear();
				
		// Load the sender rewards
		for (String key : referralPro.getConfig().getConfigurationSection("rewardsSender").getKeys(false)) {
			// make new reward
			RankRewards reward = new RankRewards (
					referralPro.getConfig().getString("rewardsSender." + key + ".rank"), 
					referralPro.getConfig().getStringList("rewardsSender." + key + ".commands"));
			
			// add the new reward
			senderRewards.add(reward);
		}//*/
		
		// Load the receiver rewards
		for (String key : referralPro.getConfig().getConfigurationSection("rewardsReceiver").getKeys(false)) {
			// make new reward
			RankRewards reward = new RankRewards (
					referralPro.getConfig().getString("rewardsReceiver." + key + ".rank"), 
					referralPro.getConfig().getStringList("rewardsReceiver." + key + ".commands"));
			
			// add the new reward
			receiverRewards.add(reward);
		}
		
		// Load the Milestone rewards
		for (String key : referralPro.getConfig().getConfigurationSection("rewardsReceiver").getKeys(false)) {
			// make new reward
			MileStoneRewards reward = new MileStoneRewards (
					referralPro.getConfig().getInt("mileStoneRewards." 		  + key + ".total"), 
					referralPro.getConfig().getStringList("mileStoneRewards." + key + ".commands"));
			
			// add the new reward
			mileStoneRewards.add(reward);
		}
	}
	
	public void GiveReceiverReward (Player p) {		
		for (RankRewards rankRewards : receiverRewards) {
			if (ReferralPro.perms.getPrimaryGroup(p).equals(rankRewards.rank)) {
				referralPro.UseCommand(rankRewards.commands, p);
				return;
			}
		}
		
		// if rank not found give basic reward
		referralPro.UseCommand(receiverRewards.get(0).commands, p);
	}
	
	public void GiveSenderReward (Player p) {		
		for (RankRewards rankRewards : senderRewards) {
			if (ReferralPro.perms.getPrimaryGroup(p).equals(rankRewards.rank)) {
				referralPro.UseCommand(rankRewards.commands, p);
				return;
			}
		}	
		
		// if rank not found give basic reward
		referralPro.UseCommand(senderRewards.get(0).commands, p);
	}
	
	public class RankRewards {
		public String rank;
		public List<String> commands;
		 
		public RankRewards (String rank, List<String> commands){
	        this.rank = rank;
	        this.commands = commands;
	    }
	}
	
	
	//////////////////////////////////
	//
	//  MileStone Rewards
	//
	//////////////////////////////////
	
	
	public void GiveMileStoneRewards(Player p) {		
		// the last reward
		int lastReward = ReferralPro.Instance.db.GetLastReward(p.getUniqueId().toString());
		
		for (MileStoneRewards mileRewards : mileStoneRewards) {
			if (lastReward < mileRewards.min) {
				// use commands
				ReferralPro.Instance.UseCommand(mileRewards.commands, p);
				
				referralPro.db.LastRewardUpdate(p, mileRewards.min);
				
				return;
			}
		}
	}
	
	public int NextMileReward(String UUID) {
		int dif = 0;
		int lastReward = ReferralPro.Instance.db.GetLastReward(UUID);
		
		
		for (MileStoneRewards mileRewards : mileStoneRewards) {
			if (lastReward < mileRewards.min) {
				dif = mileRewards.min - ReferralPro.Instance.db.GetReferrals(UUID);
				break;
			}
		}

		// reset if to low
		if (dif < 0) {
			dif = 0;
		}
		
		return dif;
	}
	
	public int NextMileRewardTotal(String UUID) {
		int lastReward = ReferralPro.Instance.db.GetLastReward(UUID);
		
		
		for (MileStoneRewards mileRewards : mileStoneRewards) {
			if (lastReward < mileRewards.min) {
				return mileRewards.min;
			}
		}

		
		return 0;
	}
	
	public class MileStoneRewards {
		public int min;
		public List<String> commands;
		 
		public MileStoneRewards (int min, List<String> commands){
	        this.min = min;
	        this.commands = commands;
	    }
	}
}
