package me.caio.bungeecord.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import me.caio.bungeecord.Main;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class NameFetcher {
	Main m;
	private String nome;
	private String uuid;

	public NameFetcher(Main main) {
		this.m = main;
	}

	public String getName(final String uuid) {
		nome = null;

		try {
			PreparedStatement stmt = m.mainConnection.prepareStatement(
					"SELECT * FROM uuidfetcher WHERE `uuid`='" + uuid.toString().replace("-", "") + "';");
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				nome = result.getString("name");
			}
			result.close();
		} catch (SQLException e) {
			System.out.println("Falha ao buscar nick de " + uuid);
		}
		return nome;
	}

	public UUID makeUUID(String id) {
		return UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-"
				+ id.substring(16, 20) + "-" + id.substring(20, 32));
	}

	public UUID getUUID(final String nick) {
		uuid = null;

		try {
			PreparedStatement stmt = m.mainConnection
					.prepareStatement("SELECT * FROM uuidfetcher WHERE `name`='" + nick + "';");
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				uuid = result.getString("uuid");
			}
			result.close();
		} catch (SQLException e) {
			System.out.println("Falha ao buscar uuid de " + nick);
		}
		return makeUUID(uuid);
	}
	
	public void updateAPI(ProxiedPlayer p) throws SQLException {
		PreparedStatement stmt = m.mainConnection.prepareStatement("SELECT * FROM `uuidfetcher` WHERE `uuid`='" + p.getUniqueId().toString().replace("-", "") + "';");
		ResultSet result = stmt.executeQuery();
		if (result.next()) {
			stmt.execute("UPDATE `uuidfetcher` SET name='" + p.getName() + "', lastip='" + p.getAddress().toString().substring(1).split(":")[0] + "' WHERE uuid='" + p.getUniqueId().toString().replace("-", "") + "';");
		} else {
			stmt.execute("INSERT INTO `uuidfetcher`(`uuid`, `name`, `lastip`) VALUES ('" + p.getUniqueId().toString().replace("-", "") + "', '" + p.getName() + "', '"
					+ p.getAddress().toString().substring(1).split(":")[0] + "');");
		}
		result.close();
		stmt.close();
	}

}
