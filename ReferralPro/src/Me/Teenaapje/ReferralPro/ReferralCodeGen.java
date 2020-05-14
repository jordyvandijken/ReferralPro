package Me.Teenaapje.ReferralPro;

import java.util.UUID;

public class ReferralCodeGen {

	public static String GetShortID()
	{
		String code = UUID.randomUUID().toString().substring(0, 8);
		
		if (ReferralPro.Instance.db.CodeExists(code)) {
			return GetShortID();
		}
		
		return code;
	}
}
