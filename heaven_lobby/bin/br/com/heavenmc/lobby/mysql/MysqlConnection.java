package br.com.wombocraft.lobby.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.wombocraft.lobby.Lobby;

public class MysqlConnection {
	private Lobby main;
	private Connection _con;

	public MysqlConnection(Lobby hc) {
		this.main = hc;
	}

	public synchronized void trySQLConnection() {
		try {
			Lobby.getLobby().getLogger().info("Conectando ao MySQL");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String conn = "jdbc:mysql://" + "localhost" + ":" + 3306 + "/" + "heaven_utils"
					+ "?autoReconnect=true&failOverReadOnly=false&maxReconnects=2147483647";
			this._con = DriverManager.getConnection(conn, "root", "sv[-hNnnEKxrJ8_^");

		} catch (ClassNotFoundException ex) {
			Lobby.getLobby().getLogger().warning("MySQL Driver nao encontrado!");
		} catch (SQLException ex) {
			Lobby.getLobby().getLogger().warning("Erro enquanto tentava conectar ao Mysql!");
		} catch (Exception ex) {
			Lobby.getLobby().getLogger().warning("Erro desconhecido enquanto tentava conectar ao MySQL.");
		}
	}

	public Connection getConnection() {
		return this._con;
	}

	public void SQLdisconnect() {
		try {
			if ((this._con != null) && (!this._con.isClosed())) {
				this._con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void SQLQuery(final String sql) {
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(new Thread(new Runnable() {
			public void run() {
				try {
					Statement stmt = _con.createStatement();
					stmt.executeUpdate(sql);
					stmt.close();
				} catch (SQLException e) {
					main.getLogger().info("Erro ao tentar executar Query");
					main.getLogger().info(sql);
					main.getLogger().info(e.getMessage());
				}
			}
		}));
		executor.shutdown();
	}

	public void SQLQuerySync(String sql) {
		try {
			Statement stmt = _con.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			Lobby.getLobby().getLogger().info("Erro ao tentar executar Query");
			Lobby.getLobby().getLogger().info(sql);
			Lobby.getLobby().getLogger().info(e.getMessage());
		}
	}

	public void SQLQuerySyncNoLock(String sql) {
		try {
			Statement stmt = _con.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			Lobby.getLobby().getLogger().info("Erro ao tentar executar Query");
			Lobby.getLobby().getLogger().info(sql);
			Lobby.getLobby().getLogger().info(e.getMessage());
		}
	}
}
