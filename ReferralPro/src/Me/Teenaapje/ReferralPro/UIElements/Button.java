package Me.Teenaapje.ReferralPro.UIElements;

public class Button {
	public int position;
	public String buttonType;
	public String buttonItem;
	public boolean enable;
	
	public Button(int pos, String item, boolean enable) {
		position = pos;
		buttonItem = item;
		this.enable = enable;
	}
}
