package Me.Teenaapje.ReferralPro.ConfigManager;

import org.bukkit.configuration.file.FileConfiguration;

import Me.Teenaapje.ReferralPro.ReferralPro;

public class ConfigManager {
	public static FileConfiguration config;
	//########################################################
	//#
	//#   General Options
	//#
	//########################################################
	
	public static boolean milestoneEnabled;
	
	public static boolean canReferEachOther;
	public static String tryRefEachOther;

	public static boolean canReferOlderPlayers;
	public static String tryRefOlderPlayer;

	public static int minPlayTime;
	public static String notEnoughPlayTimeCode;
	public static String notEnoughPlayTime;
	
	public static int maxPlayTime;
	public static String tooMuchPlayTimeCode;
	public static String tooMuchPlayTime;
	
	public static int maxPendingRequests;
	public static String maxRequestSend;
	
	public static boolean allowSameIPRefer;
	public static String sameIPRefer;
	
	public static boolean clearOnDisable;
	public static boolean notifyPlayerRequest;
	
	//########################################################
	//#
	//#   UI panels
	//#
	//########################################################
	//##########################
	//#   UI General things
	//##########################
	public static String uIButtonMainMenu, uIButtonGoBack, uIButtonRetry, uIButtonClose,
	uIButtonPage, uIButtonNextPage, uIButtonReturnPage, uIButtonYes, uIButtonNo, 
	uIButtonBlock, uIButtonclickReward, uIButtonEnoughSpace, uIConfirmDidInvite,
	uIProfiles;
	
	
	//##########################
	//#   UI anvil (player name)
	//##########################
	public static String uIAnvilTitle, uIAnvilNeverPlayed, uIAnvilAlreadyRefed, uIAnvilCantRefSelf,
	uIAnvilAlreadyInQ, uIAnvilBlocked, uIAnvilRequestQed, uIAnvilInvited;
	
	//##########################
	//#   UI anvil (player code)
	//##########################
	public static String uIAnvilCodeTitle, uIAnvilCodeShort, uIAnvilCodeLong, uIAnvilCodeNotExist,
	uIAnvilUseOwnCode;
	
	//##########################
	//#   UI anvil response
	//##########################
	public static String uIAnvilResponseTitle;
	
	//##########################
	//#   UI Blocked
	//##########################
	public static String uIBlockedTitle, uIBlockedNone, uIBlockedIsBlocked, uIBlockedUnban;
	
	//##########################
	//#   UI Code Confirm
	//##########################
	public static String uICodeConfirmTitle;
	
	//##########################
	//#   UI Profile
	//##########################
	public static String uIProfileTitle, uIProfileTotal, uIProfileTotalExpl, uIProfileIsRefed,
	uIProfileRefedBy, uIProfileNonExist, uIProfileButtonReward, uIProfileButtonRewardExpl,
	uIProfileButtonBlocked, uIProfileButtonBlockedExpl, uIProfileButtonMilestone,
	uIProfileNextMilestone, uIProfiletooNextMilestone;
	
	//##########################
	//#   UI Invites
	//##########################
	public static String uIInvitesTitle, uIInvitesNone, uIInvitesReferredYou;
	
	//##########################
	//#   UI Main menu
	//##########################
	public static String uIRefTitle, uIRefButtonInvite, uIRefButtonInviteExpl, uIRefUniqCode,
	uIRefCodeExpl, uIRefButtonRef, uIRefButtonRefCode, uIRefButtonAdmin;
	
	//##########################
	//#   UI Referral accept
	//##########################
	public static String uIRefAcceptTitle;
	
	//##########################
	//#   UI Rewards
	//##########################
	public static String uIRewardsTitle, uIRewardsNon, uIRewardsGotRefed, uIRewardsYouRefed,
	uIRewardsClick, uIRewardsSpace;
	
	//##########################
	//#   Placeholder
	//##########################
	public static String placeHolderGotRefed, placeHolderNotRefed;
	
	
	public ConfigManager() {
	}
	
	public static void LoadConfigSettings() {
		config = ReferralPro.Instance.getConfig();

		//########################################################
		//#
		//#   General Options
		//#
		//########################################################
		
		milestoneEnabled = config.getBoolean("enableMilestone");
		
		canReferEachOther = config.getBoolean("canReferEachOther");
		tryRefEachOther = config.getString("tryRefEachOther");

		canReferOlderPlayers = config.getBoolean("canReferOlderPlayer");
		tryRefOlderPlayer = config.getString("tryRefOlderPlayer");

		minPlayTime = config.getInt("minPlayTime");
		notEnoughPlayTimeCode = config.getString("notEnoughPlayTimeCode");
		notEnoughPlayTime = config.getString("notEnoughPlayTime");	
		
		maxPendingRequests = config.getInt("maxPendingRequests");
		maxRequestSend = config.getString("maxRequestSend");
		
		allowSameIPRefer = config.getBoolean("allowSameIPRefer");
		sameIPRefer = config.getString("sameIPRefer");	

		clearOnDisable = config.getBoolean("clearOnDisable");
		notifyPlayerRequest = config.getBoolean("notifyPlayerRequest");
		
		//########################################################
		//#
		//#   UI panels
		//#
		//########################################################
		//##########################
		//#   UI General things
		//##########################
		uIButtonMainMenu = config.getString("uIButtonMainMenu");	
		uIButtonGoBack = config.getString("uIButtonGoBack");	
		uIButtonRetry = config.getString("uIButtonRetry");	
		uIButtonClose = config.getString("uIButtonClose");	
		uIButtonPage = config.getString("uIButtonPage");	
		uIButtonNextPage = config.getString("uIButtonNextPage");	
		uIButtonReturnPage = config.getString("uIButtonReturnPage");	
		uIButtonYes = config.getString("uIButtonYes");	
		uIButtonNo = config.getString("uIButtonNo");	
		uIButtonBlock = config.getString("uIButtonBlock");	
		uIButtonclickReward = config.getString("uIButtonclickReward");	
		uIButtonEnoughSpace = config.getString("uIButtonEnoughSpace");	
		uIConfirmDidInvite = config.getString("uIConfirmDidInvite");	
		uIProfiles = config.getString("uIProfiles");	
		
		//##########################
		//#   UI anvil (player name)
		//##########################
		uIAnvilTitle = config.getString("uIAnvilTitle");	
		uIAnvilNeverPlayed = config.getString("uIAnvilNeverPlayed");	
		uIAnvilAlreadyRefed = config.getString("uIAnvilAlreadyRefed");	
		uIAnvilCantRefSelf = config.getString("uIAnvilCantRefSelf");	
		uIAnvilAlreadyInQ = config.getString("uIAnvilAlreadyInQ");	
		uIAnvilBlocked = config.getString("uIAnvilBlocked");	
		uIAnvilRequestQed = config.getString("uIAnvilRequestQed");	
		uIAnvilInvited = config.getString("uIAnvilInvited");	
		
		//##########################
		//#   UI anvil (player code)
		//##########################
		uIAnvilCodeTitle = config.getString("uIAnvilCodeTitle");	
		uIAnvilCodeShort = config.getString("uIAnvilCodeShort");	
		uIAnvilCodeLong = config.getString("uIAnvilCodeLong");	
		uIAnvilCodeNotExist = config.getString("uIAnvilCodeNotExist");	
		uIAnvilUseOwnCode = config.getString("uIAnvilUseOwnCode");	
		
		//##########################
		//#   UI anvil response
		//##########################
		uIAnvilResponseTitle = config.getString("uIAnvilResponseTitle");	
		
		//##########################
		//#   UI Blocked
		//##########################
		uIBlockedTitle = config.getString("uIBlockedTitle");	
		uIBlockedNone = config.getString("uIBlockedNone");	
		uIBlockedIsBlocked = config.getString("uIBlockedIsBlocked");	
		uIBlockedUnban = config.getString("uIBlockedUnban");	
		
		//##########################
		//#   UI Code Confirm
		//##########################
		uICodeConfirmTitle = config.getString("uICodeConfirmTitle");	
	
		//##########################
		//#   UI Profile
		//##########################
		uIProfileTitle = config.getString("uIProfileTitle");	
		uIProfileTotal = config.getString("uIProfileTotal");	
		uIProfileTotalExpl = config.getString("uIProfileTotalExpl");	
		uIProfileIsRefed = config.getString("uIProfileIsRefed");	
		uIProfileRefedBy = config.getString("uIProfileRefedBy");	
		uIProfileNonExist = config.getString("uIProfileNonExist");	
		uIProfileButtonReward = config.getString("uIProfileButtonReward");	
		uIProfileButtonRewardExpl = config.getString("uIProfileButtonRewardExpl");	
		uIProfileButtonBlocked = config.getString("uIProfileButtonBlocked");	
		uIProfileButtonBlockedExpl = config.getString("uIProfileButtonBlockedExpl");	
		uIProfileButtonMilestone = config.getString("uIProfileButtonMilestone");	
		uIProfileNextMilestone = config.getString("uIProfileNextMilestone");	
		uIProfiletooNextMilestone = config.getString("uIProfiletooNextMilestone");	
		
		//##########################
		//#   UI Invites
		//##########################
		uIInvitesTitle = config.getString("uIInvitesTitle");	
		uIInvitesNone = config.getString("uIInvitesNone");	
		uIInvitesReferredYou = config.getString("uIInvitesReferredYou");	
		
		//##########################
		//#   UI Main menu
		//##########################
		uIRefTitle = config.getString("uIRefTitle");	
		uIRefButtonInvite = config.getString("uIRefButtonInvite");	
		uIRefButtonInviteExpl = config.getString("uIRefButtonInviteExpl");	
		uIRefUniqCode = config.getString("uIRefUniqCode");	
		uIRefCodeExpl = config.getString("uIRefCodeExpl");	
		uIRefButtonRef = config.getString("uIRefButtonRef");	
		uIRefButtonRefCode = config.getString("uIRefButtonRefCode");	
		uIRefButtonAdmin = config.getString("uIRefButtonAdmin");	

		//##########################
		//#   UI Referral accept
		//##########################
		uIRefAcceptTitle = config.getString("uIRefAcceptTitle");	

		//##########################
		//#   UI Rewards
		//##########################
		uIRewardsTitle = config.getString("uIRewardsTitle");	
		uIRewardsNon = config.getString("uIRewardsNon");	
		uIRewardsGotRefed = config.getString("uIRewardsGotRefed");	
		uIRewardsYouRefed = config.getString("uIRewardsYouRefed");	
		uIRewardsClick = config.getString("uIRewardsClick");	
		uIRewardsSpace = config.getString("uIRewardsSpace");
		
		//##########################
		//#   Placeholder
		//##########################
		placeHolderGotRefed = config.getString("placeHolderGotRefed");	
		placeHolderNotRefed = config.getString("placeHolderNotRefed");
	}
}
