package Me.Teenaapje.ReferralPro.LeaderBoard;

import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import Me.Teenaapje.ReferralPro.ReferralPro;
import Me.Teenaapje.ReferralPro.ConfigManager.ConfigManager;


public class Leaderboard {

	ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);	
	List<LeaderboardPlayer> board;
	
	public Leaderboard() {
		board = new ArrayList<LeaderboardPlayer>();
		
		//StartUpdating();
		UpdateLeaderboard();
	}
	
	public List<LeaderboardPlayer> GetLeaderboard() {
		if (ConfigManager.instance.leaderboardShowTotal > board.size()) {
			return board.subList(0, board.size());
		} else {
			return board.subList(0, ConfigManager.instance.leaderboardShowTotal);
		}
	}

	public int GetPlayerPosition(String UUID) {
		for (LeaderboardPlayer leaderboardPlayer : board) {
			if (leaderboardPlayer.playerUUID.equalsIgnoreCase(UUID)) {
				return leaderboardPlayer.playerPos;
			}
		}
		return 0;
	}
	
	@SuppressWarnings({ "unused" })
	private void StartUpdating() {
		ScheduledFuture<?> scheduledFuture =
		    scheduledExecutorService.schedule(new Callable<Object>() {
		        public Object call() throws Exception {
		        	UpdateLeaderboard();
					StartUpdating();		            
					
					return "Called!";
		        }
		    }, 30, TimeUnit.SECONDS);
		
		// start the callable
		try {
			scheduledFuture.get();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void StopLeaderboard() {
		scheduledExecutorService.shutdown();
	}
	
	public void UpdateLeaderboard() {
		new Thread(new Runnable() { 
            public void run() 
            { 
            	try {
            		// update board
                	board = ReferralPro.Instance.db.GetTopPlayers(0, 100);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        }).start(); 
	}
}
