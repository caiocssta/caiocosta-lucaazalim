package me.caio.bungeecord.clan;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import me.caio.bungeecord.Main;
import me.caio.bungeecord.clan.MemberType.Member;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ClanManager {

	
	public HashMap<String, Clan> clans;
	public HashMap<UUID, String> pclan;

	public Main m;

	
	public ClanManager(Main main) {
		this.m = main;
		this.clans = new HashMap<>();
		this.pclan = new HashMap<>();
//		 m.getProxy().getScheduler().runAsync(m, new Runnable() {
//		@Override
//		public void run() {
//		try {
//			loadClans();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		}
//		});		 
//	    m.getProxy().getPluginManager().registerCommand(m, new ClanCommand(m));


	}
	
	
	
	
	@SuppressWarnings("deprecation")
	public void createClan(ProxiedPlayer p, String nome, String tag) throws SQLException {
	if(nameExist(nome)) {
	p.sendMessage(ChatColor.RED +"Esse nome ja esta em uso.");
	return;
	}
	if(tagExist(tag)) {
	p.sendMessage(ChatColor.RED +"Essa tag ja esta em uso.");
	return;
	}
	Clan clan = new Clan(this, nome, tag, new ClanStatus(0, 0, 0, 0, 0));
	clan.setOwner(p.getUniqueId());
	clan.save();
	this.pclan.put(p.getUniqueId(), nome);
	p.sendMessage(ChatColor.YELLOW +"Clan criado com sucesso!");
	p.sendMessage(ChatColor.YELLOW +"Use /clan para mais informações.");
	}
	
	public boolean nameExist(final String nome) throws SQLException{
		boolean exist = false;
				PreparedStatement stmt = m.mainConnection.prepareStatement("SELECT name FROM `clans` WHERE `name`='" + nome + "';");
				ResultSet result = stmt.executeQuery();
				if(result.next()) {
				exist = true;
				}		
				result.close();
				stmt.close();
		return exist;
	}
	
	public boolean tagExist(final String tag) throws SQLException{
		boolean exist = false;
				PreparedStatement stmt = m.mainConnection.prepareStatement("SELECT tag FROM `clans` WHERE `tag`='" + tag + "';");
				ResultSet result = stmt.executeQuery();
				if(result.next()) {
				exist = true;
				}
				result.close();
				stmt.close();
		return exist;
	}
	
	
	
	public void loadPlayerClan(ProxiedPlayer p) throws SQLException {
		
		PreparedStatement stmt = m.mainConnection.prepareStatement("SELECT * FROM `clans_players` WHERE `uuid`='" + p.getUniqueId().toString().replace("-", "") + "';");
		ResultSet result = stmt.executeQuery();
		if(result.next()) {
		this.pclan.put(p.getUniqueId(), result.getString("name"));
		}
		result.close();
		stmt.close();
		
	}
	
	
	public void saveClan(Clan clan) throws SQLException {
		this.clans.put(clan.getName(), clan);
		PreparedStatement stmt = m.mainConnection.prepareStatement("SELECT * FROM `clans` WHERE `name`='" + clan.getName() + "';");
		ResultSet result = stmt.executeQuery();
		if(result.next()) {
	    stmt.execute("UPDATE `clans` SET `name`='" + clan.getName() + "', `tag`='" + clan.getTag() + "';");
		} else { 
		stmt.execute("INSERT INTO `clans` (`name`, `tag`) VALUES ('" + clan.getName() + "', '" + clan.getTag() + "')");	
		}
		result.close();
		stmt.close();
		for(UUID uuid : clan.getPlayersUUID()) {
		stmt = m.mainConnection.prepareStatement("SELECT * FROM `clans_players` WHERE `uuid`='" + uuid.toString().replace("-", "") + "';");
		result = stmt.executeQuery();
		if(result.next()) {
	    stmt.execute("UPDATE `clans_players` WHERE `name`='" +clan.getName()+"', `uuid`='" + uuid.toString().replace("-", "") + "' AND rank='" + clan.getPlayerMember(uuid) + "';");
		} else {
		stmt.execute("INSERT INTO `clans_players` (`name`, `uuid`, `rank`) VALUES ('" +clan.getName()+"', '" + uuid.toString().replace("-", "") + "', '" + clan.getPlayerMember(uuid) + "')");
		}
		}
		result.close();
		stmt.close();
		
		stmt = m.mainConnection.prepareStatement("SELECT * FROM `clans_status` WHERE `name`='" + clan.getName() + "';");
		result = stmt.executeQuery();
		if(result.next()) {
		stmt.execute("UPDATE `clans_status` SET `name`='" + clan.getName() + "', `kills`='" + clan.getStatus().getKills() + "', `deaths`='" +clan.getStatus().getDeaths()+ "', `wins`='" +clan.getStatus().getWins()+ "', `coins`='" + clan.getStatus().getMoney() + "', `elo`='" + clan.getStatus().getElo() + "';" );
		} else { 
		stmt.execute("INSERT INTO `clans_status` (`name`, `kills`, `deaths`, `wins`, `coins`, `elo`) VALUES ('" + clan.getName() + "', '" + clan.getStatus().getKills() + "', '" + clan.getStatus().getDeaths() + "', '" + clan.getStatus().getWins() + "', '" + clan.getStatus().getMoney() + "', '" + clan.getStatus().getElo() + "')");
		}
		result.close();
		stmt.close();
		
		
	}
	
	
	public void loadClans() throws SQLException {
		System.out.println("LOADING CLANS");
		HashMap<String, String> nameandtag = new HashMap<>();
        HashMap<UUID, Member> participants = new HashMap<>();
        HashMap<String, ClanStatus> status = new HashMap<>();
		PreparedStatement stmt = m.mainConnection.prepareStatement("SELECT * FROM `clans`;");
		ResultSet result = stmt.executeQuery();
		while (result.next()) {
		nameandtag.put(result.getString("name"), result.getString("tag"));
	    }
		result.close();
		stmt.close();
		
		stmt = m.mainConnection.prepareStatement("SELECT * FROM `clans_players`;");
		result = stmt.executeQuery();
		while(result.next()) {
		participants.put(m.name.makeUUID(result.getString("uuid")), Member.valueOf(result.getString("rank")));
		}
		result.close();
		stmt.close();
		
		stmt = m.mainConnection.prepareStatement("SELECT * FROM `clans_status`;");
		result = stmt.executeQuery();
		while(result.next()) {
		status.put(result.getString("name"), new ClanStatus(result.getInt("kills"), result.getInt("deaths"), result.getInt("wins"), result.getInt("coins"), result.getInt("elo")));
		}
		result.close();
		stmt.close();
		
		for(String nomes : nameandtag.keySet()) {
		String tag = nameandtag.get(nomes);
		Clan clan = new Clan(this, nomes, tag, null);
		for(UUID uuid : participants.keySet()) {
		Member member = participants.get(uuid);
		clan.addPlayer(uuid, member);
		}
		for(ClanStatus s : status.values()) {
		clan.setStatus(s);
		}
		clans.put(clan.getName(), clan);
		}
		System.out.println("Carregados " + clans.size() + " CLANS!");
		nameandtag.clear();
		participants.clear();
		status.clear();
		nameandtag = null;
		participants = null;
		status = null;
	}
	
	
//	while(result.next()) {
//	clan.addPlayer(m.name.makeUUID(result.getString("uuid")), Member.valueOf(result.getString("rank")));
//	}
//	
//	stmt = m.mainConnection.prepareStatement("SELECT * FROM `clans_status`;");
//	result = stmt.executeQuery();
//	while(result.next()) {
//	clan.setKills(result.getInt("kills"));
//	clan.setDeaths(result.getInt("deaths"));
//	clan.setWins(result.getInt("wins"));
//	clan.setMoney(result.getInt("coins"));
//	clan.setElo(result.getInt("elo"));
//	}
//	
//	this.clans.put(clan.getName(), clan);
//	
//	
	
	public String getPlayerClanName(ProxiedPlayer p) {
		if(getPlayerClan(p) != null) {
		return getPlayerClan(p).getName();
		}
		return "Nenhum";
	}
	
	public String getPlayerClanTag(ProxiedPlayer p) {
		if(getPlayerClan(p) != null) {
		return getPlayerClan(p).getTag();
		}
		return "";
	}
	public Clan getPlayerClan(ProxiedPlayer p) {
		if(this.clans.containsKey(pclan.get(p.getUniqueId()))) {
			return this.clans.get(pclan.get(p.getUniqueId()));
		}
		return null;
	}
 
}
