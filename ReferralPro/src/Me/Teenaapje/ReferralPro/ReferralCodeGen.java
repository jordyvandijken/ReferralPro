package Me.Teenaapje.ReferralPro;

import java.util.UUID;

import Me.Teenaapje.ReferralPro.ConfigManager.ConfigManager;

public class ReferralCodeGen {

	public static String GetShortID()
	{
		String code = UUID.randomUUID().toString().substring(0, ConfigManager.instance.codeLength);
		
		code = code.replace("-", "");
		
		if (ReferralPro.Instance.db.CodeExists(code)) {
			return GetShortID();
		}
		
		return code;
	}
}
