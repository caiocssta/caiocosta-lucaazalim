package me.caio.bungeecord.permissions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import me.caio.bungeecord.Main;
import me.caio.bungeecord.enums.Group;
import me.caio.bungeecord.mysql.Connect;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Permissions {
	public Main m;

	private HashMap<UUID, Group> permissions;

	public Permissions(Main m) {
		this.m = m;
		this.permissions = new HashMap<>();
	}

	public void removeProxiedPlayerGroup(ProxiedPlayer p) {
		if (this.permissions.containsKey(p.getUniqueId())) {
			this.permissions.remove(p.getUniqueId());
		}
	}

	public boolean isLoaded(ProxiedPlayer p) {
		return this.permissions.containsKey(p.getUniqueId());
	}

	public void unloadPlayer(ProxiedPlayer p) {
		if (isLoaded(p)) {
			permissions.remove(p.getUniqueId());
		}
	}

	public void loadProxiedPlayerGroup(ProxiedPlayer p) throws SQLException {
		System.out.println("Carregando grupo de " + p.getName());
		Connect.lock.lock();
		PreparedStatement stmt = m.mainConnection.prepareStatement(
				"SELECT * FROM `ranks` WHERE `uuid` = '" + p.getUniqueId().toString().replace("-", "") + "';");
		ResultSet result = stmt.executeQuery();
		if (result.next()) {

			Group grupo = Group.valueOf(result.getString("rank").toUpperCase());
			setProxiedPlayerGroup(p.getUniqueId(), grupo);
			System.out.println("Carregado com sucesso grupo de " + p.getUniqueId());

		}
		result.close();
		stmt.close();
		Connect.lock.unlock();
	}

	public void setProxiedPlayerGroup(UUID uuid, Group group) {
		this.permissions.put(uuid, group);

	}

	public Group getProxiedPlayerGroup(ProxiedPlayer p) {
		if (!permissions.containsKey(p.getUniqueId())) {
			return Group.NORMAL;
		}
		return permissions.get(p.getUniqueId());

	}

	public boolean isGroup(ProxiedPlayer p, Group group) {
		if (getProxiedPlayerGroup(p) == group) {
			return true;
		}
		return false;
	}

	public boolean isVip(ProxiedPlayer p) {
		return (isMvp(p)) || (isGroup(p, Group.VIP));
	}

	public boolean isMvp(ProxiedPlayer p) {
		return (isPro(p)) || (isGroup(p, Group.MVP));
	}

	public boolean isPro(ProxiedPlayer p) {
		return (isBeta(p)) || (p.hasPermission("heavenmc.pro")) || (isGroup(p, Group.PRO));
	}

	public boolean isBeta(ProxiedPlayer p) {
		return (isYouTuber(p)) || (p.hasPermission("heavenmc.beta")) || (isGroup(p, Group.BETA));
	}

	public boolean isYouTuber(ProxiedPlayer p) {
		return (isTrial(p)) || (isGroup(p, Group.YOUTUBER));
	}

	public boolean isTrial(ProxiedPlayer p) {
		return (isMod(p)) || (isGroup(p, Group.TRIAL));
	}

	public boolean isMod(ProxiedPlayer p) {
		return (isAdmin(p)) || (isGroup(p, Group.MOD));
	}

	public boolean isAdmin(ProxiedPlayer p) {
		return (isOwner(p)) || (isGroup(p, Group.ADMIN));
	}

	public boolean isOwner(ProxiedPlayer p) {
		return isGroup(p, Group.DONO);
	}

}
