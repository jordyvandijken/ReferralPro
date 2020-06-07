package Me.Teenaapje.ReferralPro.UIElements;

import java.util.HashMap;
import java.util.List;

public class UIElement {
	public int rows;
	public HashMap<String, Button> buttons;
	public List<Filler> fillers;
	
	public UIElement(int rows, HashMap<String, Button> buttons, List<Filler> fillers) {
		this.rows = rows;
		this.buttons = buttons;
		this.fillers = fillers;
	}
	
	public Button GetButton(String buttonName) {
		return buttons.get(buttonName);
	}
}
