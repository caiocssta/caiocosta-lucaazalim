package me.skater.permissions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import me.skater.Main;
import me.skater.Management;
import me.skater.permissions.commands.GroupSet;
import me.skater.permissions.enums.Group;
import me.skater.permissions.enums.ServerType;
import me.skater.permissions.injector.PermissionMatcher;
import me.skater.permissions.injector.RegExpMatcher;
import me.skater.permissions.injector.regexperms.RegexPermissions;
import me.skater.permissions.listeners.LoginListener;

import org.bukkit.entity.Player;

public class PermissionManager extends Management {
	private HashMap<UUID, Group> playerGroups;
	private static ServerType type = ServerType.KITPVP;
	private RegexPermissions regexPerms;
	protected PermissionMatcher matcher = new RegExpMatcher();
	protected LoginListener superms;
	private Main m;

	public PermissionManager(Main main) {
		super(main);
		this.m = main;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		this.superms = new LoginListener(getPlugin());
		getServer().getPluginManager().registerEvents(this.superms, getPlugin());
		getPlugin().getCommand("group").setExecutor(new GroupSet(this, m));
		this.regexPerms = new RegexPermissions(this);
		this.playerGroups = new HashMap<UUID, Group>();
		try {
			PreparedStatement stmt = null;
			ResultSet result = null;
			for (Player p : getServer().getOnlinePlayers()) {
				UUID uuid = p.getUniqueId();
				stmt = getMySQL().prepareStatement(
						"SELECT * FROM `ranks` WHERE `uuid` = '" + uuid.toString().replace("-", "") + "';");
				result = stmt.executeQuery();
				if (result.next()) {
					Group grupo = Group.valueOf(result.getString("rank").toUpperCase());
					setPlayerGroup(uuid, grupo);
				}
			}
			if (result != null) {
				result.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isGroup(Player player, Group group) {
		if (!this.playerGroups.containsKey(player.getUniqueId())) {
			return false;
		}
		return this.playerGroups.get(player.getUniqueId()) == group;
	}

	public boolean hasGroupPermission(Player player, Group group) {
		return hasGroupPermission(player.getUniqueId(), group);
	}

	public boolean hasGroupPermission(UUID uuid, Group group) {
		if (!this.playerGroups.containsKey(uuid)) {
			return false;
		}
		Group playerGroup = this.playerGroups.get(uuid);
		return playerGroup.ordinal() >= group.ordinal();
	}

	public static ServerType getServerType() {
		return type;
	}

	public RegexPermissions getRegexPerms() {
		return this.regexPerms;
	}

	public PermissionMatcher getPermissionMatcher() {
		return this.matcher;
	}

	public void setPlayerGroup(Player player, Group group) {
		UUID uuid = player.getUniqueId();
		this.playerGroups.put(uuid, group);
	}
	


	public void savePlayerGroup(String uuid, Group group, Integer time) throws SQLException {
		if (uuid == null) {
			throw new SQLException("UUID nulo");
		}
		PreparedStatement stmt = getMySQL()
				.prepareStatement("SELECT * FROM `ranks` WHERE `uuid` = '" + uuid.toString().replace("-", "") + "';");
		ResultSet result = stmt.executeQuery();
		if (result.next()) {
			stmt.execute("UPDATE `ranks` SET `rank`='" + group.toString().toLowerCase() + "' WHERE uuid='"
					+ uuid.toString().replace("-", "") + "';");
		} else {
			stmt.execute("INSERT INTO `ranks`(`uuid`, `rank`, `rank_time`, `rank_left`, `temporary`) VALUES ('"
					+ uuid.toString().replace("-", "") + "', '" + group.toString().toLowerCase() + "', '"
					+ System.currentTimeMillis() + "', '" + time + "', 'true');");
		}
		result.close();
		stmt.close();
	}

	public void savePlayerGroup(String uuid, Group group) throws SQLException {
		if (uuid == null) {
			throw new SQLException("UUID nulo");
		}
		PreparedStatement stmt = getMySQL()
				.prepareStatement("SELECT * FROM `ranks` WHERE `uuid` = '" + uuid.toString().replace("-", "") + "';");
		ResultSet result = stmt.executeQuery();
		if (result.next()) {
			stmt.execute("UPDATE `ranks` SET `rank`='" + group.toString().toLowerCase() + "' WHERE uuid='"
					+ uuid.toString().replace("-", "") + "';");
		} else {
			stmt.execute("INSERT INTO `ranks`(`uuid`, `rank`, `rank_time`, `rank_left`, `temporary`) VALUES ('"
					+ uuid.toString().replace("-", "") + "', '" + group.toString().toLowerCase() + "', '"
					+ System.currentTimeMillis() + "', '0', 'false');");
		}
		result.close();
		stmt.close();
	}
	public void removePlayer(UUID uuid) throws SQLException {
		PreparedStatement stmt = getMySQL()
				.prepareStatement("SELECT * FROM `ranks` WHERE `uuid` = '" + uuid.toString().replace("-", "") + "';");
		ResultSet result = stmt.executeQuery();
		if (result.next()) {
			stmt.execute("DELETE FROM `ranks` WHERE `uuid`='" + uuid.toString().replace("-", "") + "';");
		}
		result.close();
		stmt.close();
	}

	public void removeRank(UUID uuid) throws SQLException {
		PreparedStatement stmt = getMySQL()
				.prepareStatement("SELECT * FROM `ranks` WHERE `uuid` = '" + uuid.toString().replace("-", "") + "';");
		ResultSet result = stmt.executeQuery();
		if (result.next()) {
			stmt.execute("DELETE FROM `ranks` WHERE `uuid`='" + uuid.toString().replace("-", "") + "';");
		}
		result.close();
		stmt.close();
	}

	public void setPlayerGroup(UUID uuid, Group group) {
		this.playerGroups.put(uuid, group);

	}

	public void removePlayerGroup(UUID uuid) {
		this.playerGroups.remove(uuid);
	}

	public Group getPlayerGroup(Player player) {
		if (!this.playerGroups.containsKey(player.getUniqueId())) {
			return Group.NORMAL;
		}
		return this.playerGroups.get(player.getUniqueId());
	}

	public Group getPlayerGroup(UUID uuid) {
		if (!this.playerGroups.containsKey(uuid)) {
			return Group.NORMAL;
		}
		return this.playerGroups.get(uuid);
	}

	public void loadPlayerGroup(UUID uuid) throws SQLException {
		PreparedStatement stmt = getMySQL()
				.prepareStatement("SELECT * FROM `ranks` WHERE `uuid` = '" + uuid.toString().replace("-", "") + "';");
		ResultSet result = stmt.executeQuery();
		if (result.next()) {

			Group grupo = Group.valueOf(result.getString("rank").toUpperCase());
			setPlayerGroup(uuid, grupo);

		}

		result.close();
		stmt.close();

	}

	@Override
	public void onDisable() {
		if (this.regexPerms != null) {
			this.regexPerms.onDisable();
			this.regexPerms = null;
		}
		if (this.superms != null) {
			this.superms.onDisable();
			this.superms = null;
		}
		if (this.playerGroups != null) {
			this.playerGroups.clear();
		}
	}
}
