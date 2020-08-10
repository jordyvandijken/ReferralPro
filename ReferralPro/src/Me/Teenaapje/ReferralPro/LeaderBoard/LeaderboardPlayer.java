package Me.Teenaapje.ReferralPro.LeaderBoard;

public class LeaderboardPlayer {
	public String playerUUID;
	public String playerName;
	public int playerPos;
	public int totalRefers;
	
	public LeaderboardPlayer(String pUUID, String p, int pos, int t) {
		playerUUID = pUUID;
		playerName = p;
		playerPos = pos;
		totalRefers = t;
	}
}

