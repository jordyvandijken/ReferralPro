package Me.Teenaapje.ReferralPro.UIElements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import Me.Teenaapje.ReferralPro.ConfigManager.UIConfigManager;

public class UIElementManager {
	public static UIElementManager instance;
	
	private HashMap<String, UIElement> ui;
	
	public UIElementManager() {
		instance = this;
		
		ui = new HashMap<String, UIElement>();
	}
	
	public void LoadUIElements() {
		ui.clear();
		
		FileConfiguration file = UIConfigManager.instance.get();
		
		// loop ui's
		for (String ui : file.getConfigurationSection("ui").getKeys(false)) {
			int rows = file.getInt("ui." + ui + ".rows");
			boolean enableUI = file.getBoolean("ui." + ui + ".enable");

			// 
			HashMap<String, Button> buttons = new HashMap<String, Button>();
			
			//* get every button
			for (String buttonP : UIConfigManager.instance.get().getConfigurationSection("ui." + ui + ".buttons").getKeys(false)) {
				// get button info
				int buttonPos = Integer.parseInt(buttonP);
				String buttonType = file.getString("ui." + ui + ".buttons." + buttonP + ".buttontype");
				String buttonItem = file.getString("ui." + ui + ".buttons." + buttonP + ".buttonitem");
				boolean enable    = file.getBoolean("ui." + ui + ".buttons." + buttonP + ".enable");
				
				// create button
				Button button = new Button(buttonPos, buttonItem, enable);
				
				// add button
				buttons.put(buttonType, button);
			}//*/
			
			
			List<Filler> fillers = new ArrayList<Filler>();
			
			//* get every filler
			for (String fillerP : UIConfigManager.instance.get().getConfigurationSection("ui." + ui + ".fillers").getKeys(false)) {
				// get the filler info
				int fillerPos = Integer.parseInt(fillerP);
				String fillerItem = file.getString("ui." + ui + ".fillers." + fillerP + ".filleritem");
				
				if (!fillerItem.equalsIgnoreCase("none")) {
					// create filler
					Filler filler = new Filler(fillerPos, fillerItem);
					
					// add filler
					fillers.add(filler);
				}	
			}//*/
			
			UIElement element = new UIElement(rows, buttons, fillers, enableUI);
			
			this.ui.put(ui, element);
		}//*/
	}
	
	public UIElement GetElement(String uiTitle) {
		return ui.get(uiTitle);
	}
}
