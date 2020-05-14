package Me.Teenaapje.ReferralPro.Listener;

public class Request {
	public String senderUUID;
	public String receiverUUID;
	
	public Request(String sender, String receiver) {
		this.senderUUID = sender;
		this.receiverUUID = receiver;
	}
}
