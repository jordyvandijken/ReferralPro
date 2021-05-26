package Me.Teenaapje.ReferralPro.DataBase;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import Me.Teenaapje.ReferralPro.ReferralPro;
import Me.Teenaapje.ReferralPro.LeaderBoard.LeaderboardPlayer;
import Me.Teenaapje.ReferralPro.Listener.Request;
import Me.Teenaapje.ReferralPro.Listener.Reward;
import Me.Teenaapje.ReferralPro.Utils.Utils;

public class DataBase {
	public String host, database, username, password, mainTable, requestTable, blockedTable, rewardsTable, playerCodeTable, parameters, dboption;
	public int port;
	private Connection connection;
	
	ReferralPro referralPro = ReferralPro.Instance;

	public DataBase() {
		host = referralPro.getConfig().getString("host");
		port = referralPro.getConfig().getInt("port");
		database = referralPro.getConfig().getString("database");
		username = referralPro.getConfig().getString("username");
		password = referralPro.getConfig().getString("password");
		mainTable = referralPro.getConfig().getString("mainTable");
		requestTable = referralPro.getConfig().getString("requestTable");
		blockedTable = referralPro.getConfig().getString("blockedTable");
		rewardsTable = referralPro.getConfig().getString("rewardsTable");
		playerCodeTable = referralPro.getConfig().getString("playerCodeTable");
		parameters = referralPro.getConfig().getString("databaseParameters");
		dboption = referralPro.getConfig().getString("db");

		if (dboption.compareTo("local") == 0) {
			sqlLiteSetup();
		} else if (dboption.compareTo("mysql") == 0) {
			mysqlSetup();
		} else {
			referralPro.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + referralPro.getDescription().getName() + " Incorrect selected database!");
			return;
		}
		
		createReferredDB();
		createRequestDB();
		createBlockedDB();
		createPlayerCodeDB();
	}
	
	public void sqlLiteSetup() {
	  	File dir = referralPro.getDataFolder();
		
		// look if directory exists and create
		if (!dir.exists()) {
			if (!dir.mkdir()) {
				System.out.println("Could not create directory for plugin: " + referralPro.getDescription().getName());
			}
		}
		
		// check and create file if it doesn't exist
		File file = new File(dir, database + ".db");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

        try {
            if(connection!=null&&!connection.isClosed()){
                return;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + file);
			
            
            Utils.SendMessage(null, null, ChatColor.GREEN + "Connected to local database");
            
        } catch (SQLException ex) {
            referralPro.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            referralPro.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        
		createRewardsDB("AUTOINCREMENT");
	}
	
	public void mysqlSetup() {
		try {
			
			synchronized (this) {
				if (getConnection() != null && !getConnection().isClosed()) {
					return;
				}

				Class.forName("com.mysql.cj.jdbc.Driver");
				setConnection(DriverManager.getConnection("jdbc:mysql://" + 
															this.host + ":" + 
															this.port + "/" + 
															this.database +
															this.parameters, 
															this.username, 
															this.password));

	            Utils.SendMessage(null, null, ChatColor.GREEN + "Database Connected to mysql");

			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		createRewardsDB("AUTO_INCREMENT");
	}
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	private void createReferredDB() {
		String CreatemainTable = "CREATE TABLE IF NOT EXISTS " + mainTable + "(" + 
				"  `UUID` varchar(40) NOT NULL," +
				"  `NAME` varchar(40) NOT NULL," +
				"  `REFERRED` varchar(40) DEFAULT NULL," +
				"  `LASTREWARD` int(255) NOT NULL DEFAULT 0," +
				"  `USERIP`	varchar(255) DEFAULT NULL)";
	    
	    try {
            Statement s = connection.createStatement();
            s.executeUpdate(CreatemainTable);
            s.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
	    
	}
	
	private void createRequestDB() {
		String createRequestDB = "CREATE TABLE IF NOT EXISTS " + requestTable + "(" +
				" `RequestForUUID`	varchar(40) NOT NULL," +
				" `RequestFromUUID`	varchar(40) NOT NULL)";
	    
	    try {
            Statement s = connection.createStatement();
            s.executeUpdate(createRequestDB);
            s.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	private void createBlockedDB() {
		String createBlockedDB = "CREATE TABLE IF NOT EXISTS " + blockedTable + "(" +
				" `RequestForUUID`	varchar(40) NOT NULL," +
				" `RequestFromUUID`	varchar(40) NOT NULL)";
	    
	    try {
            Statement s = connection.createStatement();
            s.executeUpdate(createBlockedDB);
            s.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	private void createRewardsDB(String typeOfAuto) {
		String createRewardsDB = "CREATE TABLE IF NOT EXISTS " + rewardsTable +
				" (`ID`				INTEGER PRIMARY KEY " + typeOfAuto + "," +
				"  `ForUUID`		varchar(40) NOT NULL," +
				"  `FromUUID`		varchar(40) NOT NULL," +
				"  `DidRefer`		int(5)    NOT NULL)";
	    
	    try {
            Statement s = connection.createStatement();
            s.executeUpdate(createRewardsDB);
            s.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	private void createPlayerCodeDB() {
		String createplayerCodeDB = "CREATE TABLE IF NOT EXISTS " + playerCodeTable + "(" +
				" `UUID`	varchar(40) NOT NULL," +
				" `CODE`	varchar(40) NOT NULL)";
	    
	    try {
            Statement s = connection.createStatement();
            s.executeUpdate(createplayerCodeDB);
            s.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	public int TotalPlayersReferred() {
		try {
			PreparedStatement statement = getConnection().prepareStatement("SELECT count(*) as total from " + mainTable + " WHERE REFERRED IS NOT NULL");
			
			ResultSet result = statement.executeQuery();
						
			if (result.next()) {
				return result.getInt("total");
			}
			
			
			statement.close();
			
		} catch (SQLException e) {
			System.out.print("Error Function PlayerExists");
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public boolean PlayerExists (String uuid) {
		try {
			PreparedStatement statement = getConnection().prepareStatement("select * from " + mainTable + " where UUID=?");
			
			statement.setString(1, uuid);
			
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				return true;
			}
			
			statement.close();
			
		} catch (SQLException e) {
			System.out.print("Error Function PlayerExists");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean PlayerExistsName (String name) {
		try {
			PreparedStatement statement = getConnection().prepareStatement("select * from " + mainTable + " where LOWER(NAME)=?");
			
			statement.setString(1, name.toLowerCase());
			
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				return true;
			}
			
			statement.close();
			
		} catch (SQLException e) {
			System.out.print("Error Function PlayerExists");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public List<LeaderboardPlayer> GetTopPlayers (int min, int max) {
		try {
			List<LeaderboardPlayer> topPlayer = new ArrayList<LeaderboardPlayer>();
			//


			PreparedStatement statement;
			
			if (referralPro.getConfig().getString("db").compareTo("local") == 0) {
				statement = getConnection().prepareStatement("SELECT U.UUID, U.NAME, (SELECT count(*)from [" + mainTable + "] US WHERE US.REFERRED=U.UUID) as REFTOTAL "
						+ " FROM " + mainTable + " U ORDER BY REFTOTAL DESC, NAME ASC LIMIT ?, ?");
			} else {
				statement = getConnection().prepareStatement("SELECT U.UUID, U.NAME, (SELECT count(*)from " + mainTable + " US WHERE US.REFERRED=U.UUID) as REFTOTAL "
						+ " FROM " + mainTable + " U ORDER BY REFTOTAL DESC, NAME ASC LIMIT ?, ?");
			}
				
			statement.setInt(1, min);
			statement.setInt(2, max);
			
			
			ResultSet result = statement.executeQuery();
			
			int position = min + 1;
			while (result.next()) {
				// create and add top player
				topPlayer.add(new LeaderboardPlayer(result.getString("UUID"), result.getString("NAME"), position, result.getInt("REFTOTAL")));
				position++;
			}
			
			statement.close();
			
			return topPlayer;
			
		} catch (SQLException e) {
			System.out.print("Error Function GetReferrals");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void CreatePlayer(String playerUUID, String playerName) {
		try {
			if (PlayerExists(playerUUID)) {
				return;
			}

			PreparedStatement insert = getConnection().prepareStatement("insert into " + mainTable + " (UUID, NAME, REFERRED) values (?,?,?)");

			insert.setString(1, playerUUID);
			insert.setString(2, playerName.toLowerCase());
			insert.setString(3, null);

			insert.executeUpdate();
			
			insert.close();

		} catch (SQLException e) {
			System.out.print("Error Function CreatePlayer");
			e.printStackTrace();
		}
	}

	public void ReferralPlayer(String senderUUID, String receiverUUID) {
		try {
			PreparedStatement update = getConnection()
					.prepareStatement("update " + mainTable + " set REFERRED=? where UUID=?");
			
			update.setString(1, senderUUID);
			update.setString(2, receiverUUID);
			update.executeUpdate();
			
			update.close();

		} catch (SQLException e) {
			System.out.print("Error Function ReferralPlayer");
			e.printStackTrace();
		}
	}
	
	public void UpdatePlayerIP(String UUID, String ip) {
		try {
			PreparedStatement update = getConnection()
					.prepareStatement("update " + mainTable + " set USERIP=? where UUID=?");
			
			update.setString(1, ip);
			update.setString(2, UUID);
			update.executeUpdate();
			
			update.close();

		} catch (SQLException e) {
			System.out.print("Error Function ReferralPlayer");
			e.printStackTrace();
		}
	}
	
	public boolean PlayerReferrald (String playerUUID) {
		try {
			if (!PlayerExists(playerUUID)) {
				return false;
			}	
			
			PreparedStatement statement = getConnection().prepareStatement("select * from " + mainTable + " where UUID=?");
			
			statement.setString(1, playerUUID);
			
			ResultSet result = statement.executeQuery();
						
			if (result.next() && result.getString("REFERRED") == null) {
				return false;
			}
			
			statement.close();
			
		} catch (SQLException e) {
			System.out.print("Error Function playerReferrald");
			e.printStackTrace();
		}
		
		return true;
	}
	
	public String GetPlayersUUID (String playerName) {
		try {
			if (!PlayerExistsName(playerName)) {
				return null;
			}	
			
			PreparedStatement statement = getConnection().prepareStatement("select UUID from " + mainTable + " where LOWER(NAME)=?");
			
			statement.setString(1, playerName.toLowerCase());
			
			ResultSet result = statement.executeQuery();
						
			if (result.next()) {
				return result.getString("UUID");
			}
			
			statement.close();
			
		} catch (SQLException e) {
			System.out.print("Error Function playerReferrald");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String GetPlayersName (String UUID) {
		try {
			if (!PlayerExists(UUID)) {
				return null;
			}	
			
			PreparedStatement statement = getConnection().prepareStatement("select NAME from " + mainTable + " where UUID=?");
			
			statement.setString(1, UUID);
			
			ResultSet result = statement.executeQuery();
						
			if (result.next()) {
				return result.getString("NAME");
			}
			
			statement.close();
			
		} catch (SQLException e) {
			System.out.print("Error Function playerReferrald");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String PlayerReferraldBy (String playerUUID) {
		try {
			if (!PlayerExists(playerUUID)) {
				return null;
			}	
			
			PreparedStatement statement = getConnection().prepareStatement("select REFERRED from " + mainTable + " where UUID=?");
			
			statement.setString(1, playerUUID);
			
			ResultSet result = statement.executeQuery();
						
			if (result.next() && result.getString("REFERRED") != null) {
				return result.getString("REFERRED");
			}
			
			statement.close();
			
		} catch (SQLException e) {
			System.out.print("Error Function playerReferrald");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean PlayerReset(String playerUUID) {
		try {
			if (!PlayerExists(playerUUID)) {
				return true;
			}	
			
			PreparedStatement statement = getConnection().prepareStatement("update " + mainTable + " set REFERRED=null, LASTREWARD=0, USERIP=null where UUID=?");
			
			statement.setString(1, playerUUID);
			
			statement.executeUpdate();
			
			statement.close();
			
			return true;
		} catch (SQLException e) {
			System.out.print("Error Function PlayerReset");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean PlayerResetReferral(String playerUUID) {
		try {
			if (!PlayerExists(playerUUID)) {
				return true;
			}	
			
			PreparedStatement statement = getConnection().prepareStatement("update " + mainTable + " set REFERRED=null, USERIP=null where REFERRED=?");
			
			statement.setString(1, playerUUID);
			
			statement.executeUpdate();
			
			statement.close();
			
			return true;
		} catch (SQLException e) {
			System.out.print("Error Function PlayerResetReferral");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean PlayerRemove(String player) {
		try {
			if (!PlayerExists(player)) {
				return true;
			}	
			
			PreparedStatement statement = getConnection().prepareStatement("delete from " + mainTable + " where UUID=?");
			
			statement.setString(1, player);
			
			statement.executeUpdate();
			
			statement.close();
			
			return true;
		} catch (SQLException e) {
			System.out.print("Error Function PlayerRemove");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public int GetReferrals (String playerUUID) {
		try {
			if (!PlayerExists(playerUUID)) {
				return 0;
			}	
			
			PreparedStatement statement = getConnection().prepareStatement("SELECT count(*) as total from " + mainTable + " WHERE REFERRED=?");
			
			statement.setString(1, playerUUID);
			
			ResultSet result = statement.executeQuery();
						
			if (result.next()) {
				return result.getInt("total");
			}
			
			statement.close();
			
		} catch (SQLException e) {
			System.out.print("Error Function GetReferrals");
			e.printStackTrace();
		}
		
		return 0;
	}

	public int GetLastReward (String playerUUID) {
		try {
			if (!PlayerExists(playerUUID)) {
				return 0;
			}	
			
			PreparedStatement statement = getConnection().prepareStatement("SELECT LASTREWARD as amount from " + mainTable + " WHERE UUID=?");
			
			statement.setString(1, playerUUID);
			
			ResultSet result = statement.executeQuery();
						
			if (result.next()) {
				return result.getInt("amount");
			}
			
			statement.close();
			
		} catch (SQLException e) {
			System.out.print("Error Function GetLastReward");
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int GetUsedRefIP (String playerUUID, String ip) {
		try {
			if (!PlayerExists(playerUUID)) {
				return 0;
			}	
			
			PreparedStatement statement = getConnection().prepareStatement("SELECT count(*) as total from " + mainTable + " WHERE USERIP=? and not UUID=?");
			
			statement.setString(1, ip);
			statement.setString(2, playerUUID);
			
			ResultSet result = statement.executeQuery();
						
			if (result.next()) {
				return result.getInt("total");
			}
			
			statement.close();
			
		} catch (SQLException e) {
			System.out.print("Error Function GetUsedRefIP");
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public String GetPlayerIP (String playerUUID) {
		try {
			if (!PlayerExists(playerUUID)) {
				return null;
			}	
			
			PreparedStatement statement = getConnection().prepareStatement("SELECT USERIP from " + mainTable + " WHERE UUID=?");
			
			statement.setString(1, playerUUID);
			
			ResultSet result = statement.executeQuery();
						
			if (result.next()) {
				return result.getString("USERIP");
			}
			
			statement.close();
			
		} catch (SQLException e) {
			System.out.print("Error Function GetPlayerIP");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean ResetAll() {
		try {
			PreparedStatement statement = getConnection().prepareStatement("update " + mainTable + " set REFERRED=null, LASTREWARD=0, USERIP=null");
						
			statement.executeUpdate();
			
			statement.close();
			
			return true;
		} catch (SQLException e) {
			System.out.print("Error Function ResetAll");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean RemoveAll() {
		try {
			PreparedStatement statement = getConnection().prepareStatement("delete from " + mainTable);
						
			statement.executeUpdate();
			
			statement.close();
			
			return true;
		} catch (SQLException e) {
			System.out.print("Error Function RemoveAll");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public void LastRewardUpdate(Player player, int lastReward) {
		try {
			// check if the both players are added to the database
			CreatePlayer(player.getUniqueId().toString(), player.getName());
			
			PreparedStatement update = getConnection()
					.prepareStatement("update " + mainTable + " set LASTREWARD=? where UUID=?");
			
			update.setInt(1, lastReward);
			update.setString(2, player.getUniqueId().toString());
			update.executeUpdate();
			
			update.close();

		} catch (SQLException e) {
			System.out.print("Error Function ReferralPlayer");
			e.printStackTrace();
		}
	}
	
	/////////////////////////////
	///
	/// Here are Methods for requests
	///
	/////////////////////////////
	
	public boolean RequestExists (String senderUUID, String receiverUUID) {
		try {
			PreparedStatement statement = getConnection().prepareStatement("select * from " + requestTable + " where RequestForUUID=? and RequestFromUUID=?");
			
			statement.setString(1, receiverUUID);
			statement.setString(2, senderUUID);
			
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				return true;
			}
			
			statement.close();
			
		} catch (SQLException e) {
			System.out.print("Error Function PlayerExists");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean PlayerHasRequests (String receiverUUID) {
		try {
			PreparedStatement statement = getConnection().prepareStatement("select * from " + requestTable + " where RequestForUUID=?");
			
			statement.setString(1, receiverUUID);
			
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				return true;
			}
			
			statement.close();
			
		} catch (SQLException e) {
			System.out.print("Error Function PlayerExists");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public ArrayList<Request> GetPlayerRequests (String receiverUUID, int page, int max) {
		try {
			ArrayList<Request> requests = new ArrayList<Request>();
			
			PreparedStatement statement = getConnection().prepareStatement("select * from " + requestTable + " where RequestForUUID=? LIMIT " + ((page-1) * (max - 1)) + " , " + (page * max + 1));
			
			statement.setString(1, receiverUUID);
			
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				Request newReq = new Request(result.getString("RequestFromUUID"), result.getString("RequestForUUID"));
				requests.add(newReq);
			}
			
			statement.close();
			
			return requests;
			
		} catch (SQLException e) {
			System.out.print("Error Function PlayerExists");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void CreateRequest(String senderUUID, String receiverUUID) {
		try {
			PreparedStatement insert = getConnection().prepareStatement("insert into " + requestTable + " (RequestForUUID, RequestFromUUID) values (?,?)");

			insert.setString(1, receiverUUID);
			insert.setString(2, senderUUID);

			insert.executeUpdate();
			
			insert.close();

		} catch (SQLException e) {
			System.out.print("Error Function CreateRequest");
			e.printStackTrace();
		}
	}
	
	public boolean RemoveRequest(String senderUUID, String receiverUUID) {
		try {
			PreparedStatement statement = getConnection().prepareStatement("delete from " + requestTable + " where RequestForUUID=? and RequestFromUUID=?");
					
			statement.setString(1, receiverUUID);
			statement.setString(2, senderUUID);
			
			statement.executeUpdate();
			
			statement.close();
			
			return true;
		} catch (SQLException e) {
			System.out.print("Error Function RemoveRequest");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean RemovePlayerRequests(String receiverUUID) {
		try {
			PreparedStatement statement = getConnection().prepareStatement("delete from " + requestTable + " where RequestForUUID=?");
					
			statement.setString(1, receiverUUID);
			
			statement.executeUpdate();
			
			statement.close();
			
			return true;
		} catch (SQLException e) {
			System.out.print("Error Function RemovePlayerRequests");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean RemoveAllRequests() {
		try {
			PreparedStatement statement = getConnection().prepareStatement("delete from " + requestTable);
					
			statement.executeUpdate();
			
			statement.close();
			
			return true;
		} catch (SQLException e) {
			System.out.print("Error Function RemoveAllRequests");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public int TotalRequests (String fromUUID) {
		try {
			PreparedStatement statement = getConnection().prepareStatement("SELECT count(*) as total from " + requestTable + " WHERE RequestFromUUID=?");
			
			statement.setString(1, fromUUID);
			
			ResultSet result = statement.executeQuery();
						
			if (result.next()) {
				return result.getInt("total");
			}
			
			statement.close();
			
		} catch (SQLException e) {
			System.out.print("Error Function GetReferrals");
			e.printStackTrace();
		}
		
		return 0;
	}
	
	/////////////////////////////
	///
	/// Here are Methods for Blocks
	///
	/////////////////////////////
	
	public boolean BlockExists (String senderUUID, String receiverUUID) {
		try {
			PreparedStatement statement = getConnection().prepareStatement("select * from " + blockedTable + " where RequestForUUID=? and RequestFromUUID=?");
			
			statement.setString(1, receiverUUID);
			statement.setString(2, senderUUID);
			
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				return true;
			}
			
			statement.close();
			
		} catch (SQLException e) {
			System.out.print("Error Function PlayerExists");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public void CreateBlock(String senderUUID, String receiverUUID) {
		try {
			PreparedStatement insert = getConnection().prepareStatement("insert into " + blockedTable + " (RequestForUUID, RequestFromUUID) values (?,?)");

			insert.setString(1, receiverUUID);
			insert.setString(2, senderUUID);

			insert.executeUpdate();
			
			insert.close();

		} catch (SQLException e) {
			System.out.print("Error Function CreateRequest");
			e.printStackTrace();
		}
	}
	
	public boolean RemoveBlock(String senderUUID, String receiverUUID) {
		try {
			PreparedStatement statement = getConnection().prepareStatement("delete from " + blockedTable + " where RequestForUUID=? and RequestFromUUID=?");
					
			statement.setString(1, receiverUUID);
			statement.setString(2, senderUUID);
			
			statement.executeUpdate();
			
			statement.close();
			
			return true;
		} catch (SQLException e) {
			System.out.print("Error Function RemoveRequest");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean RemoveAllBlocks() {
		try {
			PreparedStatement statement = getConnection().prepareStatement("delete from " + blockedTable);
					
			statement.executeUpdate();
			
			statement.close();
			
			return true;
		} catch (SQLException e) {
			System.out.print("Error Function RemoveAllBlocks");
			e.printStackTrace();
		}
		return false;
	}
	
	public ArrayList<Request> GetPlayerBlocks (String receiverUUID, int page, int max) {
		try {
			ArrayList<Request> requests = new ArrayList<Request>();
			
			PreparedStatement statement = getConnection().prepareStatement("select * from " + blockedTable + " where RequestForUUID=? LIMIT " + ((page-1) * (max - 1)) + " , " + (page * max + 1));
			
			statement.setString(1, receiverUUID);
			
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				Request newReq = new Request(result.getString("RequestFromUUID"), result.getString("RequestForUUID"));
				requests.add(newReq);
			}
			
			statement.close();
			
			return requests;
			
		} catch (SQLException e) {
			System.out.print("Error Function GetPlayerBlocks");
			e.printStackTrace();
		}
		
		return null;
	}
	
	/////////////////////////////
	///
	/// Here are Methods for Rewards
	///
	/////////////////////////////
	
	public void CreateReward(Reward reward) {
		try {
			PreparedStatement insert = getConnection().prepareStatement("insert into " + rewardsTable + " (ForUUID, FromUUID, DidRefer) values (?,?,?)");

			insert.setString(1, reward.forUUID);
			insert.setString(2, reward.fromUUID);
			insert.setInt(   3, reward.didRefer);

			insert.executeUpdate();
			
			insert.close();

		} catch (SQLException e) {
			System.out.print("Error Function CreateReward");
			e.printStackTrace();
		}
	}
	
	public boolean RemoveReward(int id) {
		try {
			PreparedStatement statement = getConnection().prepareStatement("delete from " + rewardsTable + " where ID=?");
					
			statement.setInt(1, id);
			
			statement.executeUpdate();
			
			statement.close();
			
			return true;
		} catch (SQLException e) {
			System.out.print("Error Function RemoveReward");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean RemoveRewardUUID(String pUUID) {
		try {
			PreparedStatement statement = getConnection().prepareStatement("delete from " + rewardsTable + " where ForUUID=? or FromUUID=?");
					
			statement.setString(1, pUUID);
			statement.setString(2, pUUID);
			
			statement.executeUpdate();
			
			statement.close();
			
			return true;
		} catch (SQLException e) {
			System.out.print("Error Function RemoveRewardUUID");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean RemoveAllRewards() {
		try {
			PreparedStatement statement = getConnection().prepareStatement("delete from " + rewardsTable);
					
			statement.executeUpdate();
			
			statement.close();
			
			return true;
		} catch (SQLException e) {
			System.out.print("Error Function RemoveAllRewards");
			e.printStackTrace();
		}
		return false;
	}
	
	public ArrayList<Reward> GetPlayerRewards (String receiverUUID, int page, int max) {
		try {
			ArrayList<Reward> requests = new ArrayList<Reward>();
			
			PreparedStatement statement = getConnection().prepareStatement("select * from " + rewardsTable + " where ForUUID=? LIMIT " + ((page-1) * (max - 1)) + " , " + (page * max + 1));
			
			statement.setString(1, receiverUUID);
			
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				Reward newReq = new Reward(result.getInt("ID"), result.getString("ForUUID"), result.getString("FromUUID"), result.getInt("DidRefer"));
				requests.add(newReq);
			}
			
			statement.close();
			
			return requests;
			
		} catch (SQLException e) {
			System.out.print("Error Function GetPlayerRewards");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String GetIDReward (int ID) {
		try {			
			PreparedStatement statement = getConnection().prepareStatement("select ForUUID from " + rewardsTable + " where ID=?");
			
			statement.setInt(1, ID);
			
			ResultSet result = statement.executeQuery();
			
			if (result.next()) {
				return result.getString("ForUUID");
			}

			statement.close();
			
		} catch (SQLException e) {
			System.out.print("Error Function GetIDReward");
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/////////////////////////////
	///
	/// Here are Methods for Player codes
	///
	/////////////////////////////
	
	public boolean CodeExists (String code) {
		try {
			PreparedStatement statement = getConnection().prepareStatement("select * from " + playerCodeTable + " where CODE=?");
			
			statement.setString(1, code);
			
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				return true;
			}
			
			statement.close();
			
		} catch (SQLException e) {
			System.out.print("Error Function CodeExists");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean PlayerHasCode (String pUUID) {
		try {
			PreparedStatement statement = getConnection().prepareStatement("select * from " + playerCodeTable + " where UUID=?");
			
			statement.setString(1, pUUID);
			
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				return true;
			}
			
			statement.close();
			
		} catch (SQLException e) {
			System.out.print("Error Function PlayerHasCode");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public void CreateCode(String UUID, String code) {
		try {
			if (CodeExists(code)) {
				return;
			}
			
			PreparedStatement insert = getConnection().prepareStatement("insert into " + playerCodeTable + " (UUID, CODE) values (?,?)");

			insert.setString(1, UUID);
			insert.setString(2, code);

			insert.executeUpdate();
			
			insert.close();

		} catch (SQLException e) {
			System.out.print("Error Function CreateCode");
			e.printStackTrace();
		}
	}
	
	public void UpdateCode(String pUUID, String code) {
		try {
			if (!PlayerHasCode(pUUID)) {
				CreateCode(pUUID, code);
				return;
			}
			
			PreparedStatement insert = getConnection().prepareStatement("Update " + playerCodeTable + " set CODE=? where UUID=?");

			insert.setString(1, code);
			insert.setString(2, pUUID);

			insert.executeUpdate();
			
			insert.close();

		} catch (SQLException e) {
			System.out.print("Error Function CreateCode");
			e.printStackTrace();
		}
	}
	
	public boolean RemoveCode(String code) {
		try {
			PreparedStatement statement = getConnection().prepareStatement("delete from " + playerCodeTable + " where CODE=?");
					
			statement.setString(1, code);
			
			statement.executeUpdate();
			
			statement.close();
			
			return true;
		} catch (SQLException e) {
			System.out.print("Error Function RemoveCode");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean RemoveCodeUUID(String UUID) {
		try {
			PreparedStatement statement = getConnection().prepareStatement("delete from " + playerCodeTable + " where UUID=?");
					
			statement.setString(1, UUID);
			
			statement.executeUpdate();
			
			statement.close();
			
			return true;
		} catch (SQLException e) {
			System.out.print("Error Function RemoveCodeUUID");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean RemoveAllCodes() {
		try {
			PreparedStatement statement = getConnection().prepareStatement("delete from " + playerCodeTable);
					
			statement.executeUpdate();
			
			statement.close();
			
			return true;
		} catch (SQLException e) {
			System.out.print("Error Function RemoveAllCodes");
			e.printStackTrace();
		}
		return false;
	}
	
	public String GetPlayerCode (String UUID) {
		try {			
			PreparedStatement statement = getConnection().prepareStatement("select CODE from " + playerCodeTable + " where UUID=?");
			
			statement.setString(1, UUID);
			
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				return result.getString("CODE");
			}
			
			statement.close();
			
			return null;
			
		} catch (SQLException e) {
			System.out.print("Error Function GetPlayerCode");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String GetPlayerCodeUUID (String code) {
		try {			
			PreparedStatement statement = getConnection().prepareStatement("select UUID from " + playerCodeTable + " where CODE=?");
			
			statement.setString(1, code);
			
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				return result.getString("UUID");
			}
			
			statement.close();
			
			return null;
			
		} catch (SQLException e) {
			System.out.print("Error Function GetPlayerCodeName");
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/////////////////////////////
	///
	/// The end close
	///
	/////////////////////////////
	
	public void CloseConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
