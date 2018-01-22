package me.caio.HungerGames.MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.caio.HungerGames.Main;

public class Connect {
	private Main m;

	public Connect(Main m) {
		this.m = m;
	}

	public synchronized Connection trySQLConnection() {
		if (!this.m.sql) {
			this.m.getLogger().info("MySQL Desativado!");
			return null;
		}
		try {
			this.m.getLogger().info("Conectando ao MySQL");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String conn = "jdbc:mysql://" + "localhost" + ":" + 3306 + "/" + "heaven_hungergames" + "?autoReconnect=true&failOverReadOnly=false&maxReconnects=2147483647";
			Main.con = DriverManager.getConnection(conn, "root", "sv[-hNnnEKxrJ8_^");
		} catch (ClassNotFoundException ex) {
			this.m.getLogger().warning("MySQL Driver nao encontrado!");
			this.m.sql = false;
		} catch (SQLException ex) {
			this.m.getLogger().warning("Erro enquanto tentava conectar ao Mysql!");
			this.m.sql = false;
		} catch (Exception ex) {
			this.m.getLogger().warning("Erro desconhecido enquanto tentava conectar ao MySQL.");
			this.m.sql = false;
		}
		return null;
	}

	public void prepareSQL() {
		if (this.m.sql) {
			SQLQuerySync(
					"CREATE TABLE IF NOT EXISTS `kits` (`ID` int(10) unsigned NOT NULL AUTO_INCREMENT, `Player` varchar(255) NOT NULL, `Kits` varchar(255) NOT NULL, PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1 ;");
			SQLQuerySync(
					"CREATE TABLE IF NOT EXISTS `kitrotation` (`ID` int(10) unsigned NOT NULL AUTO_INCREMENT, `Rank` varchar(255) NOT NULL, `Kits` varchar(255) NOT NULL, PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1 ;");
			SQLQuerySync("CREATE TABLE IF NOT EXISTS `kitprices` (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name varchar(32) NOT NULL, price INT(100))");
			SQLQuerySync(
					"CREATE TABLE IF NOT EXISTS `caixadiaria` (`ID` int(10) unsigned NOT NULL AUTO_INCREMENT, `Player` varchar(255) NOT NULL, PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1 ;");

			this.m.getLogger().info("Criando Tabelas no SQL");
		}
	}

	public static void SQLdisconnect() {
		try {
			if ((Main.con != null) && (!Main.con.isClosed())) {
				Main.con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void SQLQuery(final String sql) {
		if (!this.m.sql) {
			return;
		}
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(new Thread(new Runnable() {
			public void run() {
				try {
					Statement stmt = Main.con.createStatement();
					stmt.executeUpdate(sql);
					stmt.close();
				} catch (SQLException e) {
					Connect.this.m.getLogger().info("Erro ao tentar executar Query");
					Connect.this.m.getLogger().info(sql);
					Connect.this.m.getLogger().info(e.getMessage());
				}
			}
		}));
		executor.shutdown();
	}

	public void SQLQuerySync(String sql) {
		if (!this.m.sql) {
			return;
		}
		try {
			Statement stmt = Main.con.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			this.m.getLogger().info("Erro ao tentar executar Query");
			this.m.getLogger().info(sql);
			this.m.getLogger().info(e.getMessage());
		}
	}

	public void SQLQuerySyncNoLock(String sql) {
		if (!this.m.sql) {
			return;
		}
		try {
			Statement stmt = Main.con.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			this.m.getLogger().info("Erro ao tentar executar Query");
			this.m.getLogger().info(sql);
			this.m.getLogger().info(e.getMessage());
		}
	}

	/*
	 * public void loadServer() { if(m.ipAddress.equals("Carregando...")) {
	 * ByteArrayDataOutput out = ByteStreams.newDataOutput();
	 * out.writeUTF("GetServer"); Bukkit.getServer().sendPluginMessage(m,
	 * "BungeeCord", out.toByteArray());
	 * 
	 * }
	 * 
	 * PreparedStatement sql; ResultSet result; try { sql = Main.con.
	 * prepareStatement("SELECT server_id FROM wb_website.hg_servers sv WHERE sv.hostname = "
	 * + getServerName()); result = sql.executeQuery(); if(!result.next()) {
	 * SQLQuery("INSERT INTO wb_website.hg_servers (`name`, `hostname`, `status`) VALUES ('"
	 * + m.filterIp(getServerName()) + "','"+ getServerName()+ "'," +
	 * Status.ONLINE +");"); } } catch (SQLException e) { e.printStackTrace(); }
	 * 
	 * }
	 */
	public enum Status {
		OFFLINE,
		ONLINE,
		PROGRESS,
		PERMISSION,
		MAINTENANCE;

	}

	public String getServerName() {
		if (m.getServer().getPort() == 25565) {
			return "a1.hg.heavenmc.com.br";
		}
		return "Não encontrado";
	}

}
