package Me.Teenaapje.ReferralPro.Listener;

public class Reward {
	public int id;
	public String forUUID;
	public String fromUUID;
	public int didRefer;		

	public Reward (int id, String forUUID, String fromUUID, int didRefer) {
		this.id = id;
		this.forUUID = forUUID;
		this.fromUUID = fromUUID;
		this.didRefer = didRefer;
	}
}
