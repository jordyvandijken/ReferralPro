package Me.Teenaapje.ReferralPro.ConfigManager;

import org.bukkit.configuration.file.FileConfiguration;

import Me.Teenaapje.ReferralPro.ReferralPro;

public class ConfigManager {
	public static ConfigManager instance;
	
	public FileConfiguration config;
	//########################################################
	//#
	//#   General Options
	//#
	//########################################################
	
	public boolean milestoneEnabled;
	
	public boolean canReferEachOther;
	public String tryRefEachOther;

	public boolean canReferOlderPlayers;
	public String tryRefOlderPlayer;

	public int minPlayTime;
	public String notEnoughPlayTimeCode;
	public String notEnoughPlayTime;
	
	public int maxPlayTime;
	public String tooMuchPlayTimeCode;
	public String tooMuchPlayTime;
	
	public int maxPendingRequests;
	public String maxRequestSend;
	
	public boolean allowSameIPRefer;
	public String sameIPRefer;
	
	public boolean clearOnDisable;
	public boolean notifyPlayerRequest;
	
	public int codeLength;
	public boolean codeConfirmation;
	public String showCode;
	
	public String leaderboardTitle, leaderboardPlace;
	public int leaderboardShowTotal;
	
	public String fillerName;
	public boolean giveRewardInstant;

	
	//########################################################
	//#
	//#   UI panels
	//#
	//########################################################
	//##########################
	//#   UI Admin
	//##########################
	public String uIAdminTitle, uIAdminResetAll, uIAdminRemoveAllCod, uIAdminRemoveAllReq,
	uIAdminRemoveAllRew, uIAdminLookUpPlayer,
	uIAdminPReset, uIAdminPRemoveRef, uIAdminpResetCode, uIAdminpRemoveRew;
	
	//##########################
	//#   UI General things
	//##########################
	public String uIButtonMainMenu, uIButtonGoBack, uIButtonRetry, uIButtonClose,
	uIButtonPage, uIButtonNextPage, uIButtonReturnPage, uIButtonYes, uIButtonNo, 
	uIButtonBlock, uIButtonclickReward, uIButtonEnoughSpace, uIConfirmDidInvite,
	uIProfiles;
	
	
	//##########################
	//#   UI anvil (player name)
	//##########################
	public String uIAnvilTitle, uIAnvilNeverPlayed, uIAnvilAlreadyRefed, uIAnvilCantRefSelf,
	uIAnvilAlreadyInQ, uIAnvilBlocked, uIAnvilRequestQed, uIAnvilInvited, uIAnvilNameDefault;
	
	//##########################
	//#   UI anvil (player code)
	//##########################
	public String uIAnvilCodeTitle, uIAnvilCodeShort, uIAnvilCodeLong, uIAnvilCodeNotExist,
	uIAnvilUseOwnCode, uIAnvilCodeDefault;
	
	//##########################
	//#   UI anvil response
	//##########################
	public String uIAnvilResponseTitle;
	
	//##########################
	//#   UI Blocked
	//##########################
	public String uIBlockedTitle, uIBlockedNone, uIBlockedIsBlocked, uIBlockedUnban;
	
	//##########################
	//#   UI Code Confirm
	//##########################
	public String uICodeConfirmTitle, uICodeRefed;
	
	//##########################
	//#   UI Profile
	//##########################
	public String uIProfileTitle, uIProfileTotal, uIProfileTotalExpl, uIProfileIsRefed,
	uIProfileRefedBy, uIProfileNonExist, uIProfileButtonReward, uIProfileButtonRewardExpl,
	uIProfileButtonBlocked, uIProfileButtonBlockedExpl, uIProfileButtonMilestone,
	uIProfileNextMilestone, uIProfiletooNextMilestone;
	
	//##########################
	//#   UI Invites
	//##########################
	public String uIInvitesTitle, uIInvitesNone, uIInvitesReferredYou;
	
	//##########################
	//#   UI Main menu
	//##########################
	public String uIRefTitle, uIRefButtonInvite, uIRefButtonInviteExpl, uIRefUniqCode,
	uIRefCodeExpl, uIRefButtonRef, uIRefButtonRefCode, uIRefButtonAdmin;
	
	//##########################
	//#   UI Referral accept
	//##########################
	public String uIRefAcceptTitle;
	
	//##########################
	//#   UI Rewards
	//##########################
	public String uIRewardsTitle, uIRewardsNon, uIRewardsGotRefed, uIRewardsYouRefed,
	uIRewardsClick, uIRewardsSpace;
	
	//##########################
	//#   Placeholder
	//##########################
	public String placeHolderGotRefed, placeHolderNotRefed;
	
	
	public ConfigManager() {
		instance = this;
	}
	
	public void LoadConfigSettings() {
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
		
		maxPlayTime = config.getInt("maxPlayTime");
		tooMuchPlayTimeCode = config.getString("tooMuchPlayTimeCode");
		tooMuchPlayTime = config.getString("tooMuchPlayTime");	
		
		maxPendingRequests = config.getInt("maxPendingRequests");
		maxRequestSend = config.getString("maxRequestSend");
		
		allowSameIPRefer = config.getBoolean("allowSameIPRefer");
		sameIPRefer = config.getString("sameIPRefer");	

		clearOnDisable = config.getBoolean("clearOnDisable");
		notifyPlayerRequest = config.getBoolean("notifyPlayerRequest");
		
		codeLength = config.getInt("codeLength");
		codeConfirmation = config.getBoolean("codeConfirmation");
		showCode = config.getString("codeShow");
		
		leaderboardTitle = config.getString("leaderboardTitle");
		leaderboardPlace = config.getString("leaderboardPlace");
		leaderboardShowTotal = config.getInt("leaderboardShowTotal");
		
		fillerName = config.getString("fillerName");
		giveRewardInstant = config.getBoolean("giveRewardInstant");
		
		//########################################################
		//#
		//#   UI panels
		//#
		//########################################################
		//##########################
		//#   UI Admin
		//##########################
		uIAdminTitle = config.getString("uIAdminTitle");	
		uIAdminResetAll = config.getString("uIAdminResetAll");	
		uIAdminRemoveAllCod = config.getString("uIAdminRemoveAllCod");	
		uIAdminRemoveAllReq = config.getString("uIAdminRemoveAllReq");	
		uIAdminRemoveAllRew = config.getString("uIAdminRemoveAllRew");	
		uIAdminLookUpPlayer = config.getString("uIAdminLookUpPlayer");	
		uIAdminPReset = config.getString("uIAdminPReset");	
		uIAdminPRemoveRef = config.getString("uIAdminPRemoveRef");	
		uIAdminpResetCode = config.getString("uIAdminpResetCode");	
		uIAdminpRemoveRew = config.getString("uIAdminpRemoveRew");	
	
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
		uIAnvilNameDefault = config.getString("uIAnvilNameDefault");
		
		//##########################
		//#   UI anvil (player code)
		//##########################
		uIAnvilCodeTitle = config.getString("uIAnvilCodeTitle");	
		uIAnvilCodeShort = config.getString("uIAnvilCodeShort");	
		uIAnvilCodeLong = config.getString("uIAnvilCodeLong");	
		uIAnvilCodeNotExist = config.getString("uIAnvilCodeNotExist");	
		uIAnvilUseOwnCode = config.getString("uIAnvilUseOwnCode");	
		uIAnvilCodeDefault = config.getString("uIAnvilCodeDefault");	
		
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
		uICodeRefed = config.getString("uICodeRefed");	
	
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
