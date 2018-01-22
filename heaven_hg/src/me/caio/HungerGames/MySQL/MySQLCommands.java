package me.caio.HungerGames.MySQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import me.caio.HungerGames.Main;

public class MySQLCommands {
	private Main m;

	public MySQLCommands(Main m) {
		this.m = m;
	}

	public void giveKit(UUID uuid, String kit) {
		this.m.connection.SQLQuery("INSERT INTO `kits`(`Player`, `Kits`) VALUES ('" + uuid.toString().replace("-", "") + "','" + kit + "');");
	}

	public void setPlayerRankAndKits(UUID uuid) throws SQLException {
		if (this.m.sql) {
			if (this.m.kit.playerKit.containsKey(uuid)) {
				return;
			}
			if (Main.con.isClosed()) {
				this.m.connection.trySQLConnection();
			}
			PreparedStatement sql = Main.con.prepareStatement("SELECT * FROM `kits` WHERE (`Player`='" + uuid.toString().replace("-", "") + "');");
			ResultSet result = sql.executeQuery();
			while (result.next()) {
				String kit = result.getString("Kits");
				this.m.addPlayerKit(uuid, kit);
			}
			result.close();
			sql.close();
		}
	}
}
