package Me.Teenaapje.ReferralPro.UIElements;

import java.util.HashMap;
import java.util.List;

public class UIElement {
	public int rows;
	public HashMap<String, Button> buttons;
	public List<Filler> fillers;
	public boolean enable;
	
	public UIElement(int rows, HashMap<String, Button> buttons, List<Filler> fillers, boolean enable) {
		this.rows = rows;
		this.buttons = buttons;
		this.fillers = fillers;
		this.enable  = enable;
	}
	
	public Button GetButton(String buttonName) {
		return buttons.get(buttonName);
	}
}
