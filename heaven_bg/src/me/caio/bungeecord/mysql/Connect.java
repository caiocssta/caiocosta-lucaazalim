package me.caio.bungeecord.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import me.caio.bungeecord.Main;

public class Connect {
	private Main m;
	public static ReentrantLock lock = new ReentrantLock(true);

	public Connect(Main m) {
		this.m = m;
	}

	public synchronized Connection trySQLConnection() {
		lock.lock();
		try {
			this.m.getLogger().info("Conectando ao MySQL");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			this.m.getLogger().info("Tentando conexão para localhost...");
			String conn = "jdbc:mysql://" + "localhost" + ":" + 3306 + "/" + "heaven_utils"
					+ "?autoReconnect=true&failOverReadOnly=false&maxReconnects=2147483647";
			lock.unlock();
			return DriverManager.getConnection(conn, "root", "sv[-hNnnEKxrJ8_^");
		} catch (ClassNotFoundException ex) {
			this.m.getLogger().warning("MySQL Driver nao encontrado!");
		} catch (SQLException ex) {
			this.m.getLogger().warning("Erro enquanto tentava conectar ao Mysql!");
			ex.printStackTrace();
		} catch (Exception ex) {
			this.m.getLogger().warning("Erro desconhecido enquanto tentava conectar ao MySQL.");
		}
		lock.unlock();
		return null;
	}

	public static void SQLdisconnect(Connection con) {
		lock.lock();
		try {
			if ((con != null) && (!con.isClosed())) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		lock.unlock();
	}

	public void SQLdisconnect() {
		lock.lock();
		try {
			if ((m.mainConnection != null) && (!m.mainConnection.isClosed())) {
				m.mainConnection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		lock.unlock();
	}

	public void SQLQuery(final String sql) {
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(new Thread(new Runnable() {
			public void run() {
				Connect.lock.lock();
				try {
					if (m.mainConnection.isClosed()) {
						trySQLConnection();
					}
					Statement stmt = m.mainConnection.createStatement();
					stmt.executeUpdate(sql);
					stmt.close();
				} catch (SQLException e) {
					m.getLogger().info("Erro ao tentar executar Query");
					m.getLogger().info(e.getMessage());
				}
				Connect.lock.unlock();
			}
		}));
		executor.shutdown();
	}

	public void SQLQuerySync(String sql) {
		lock.lock();
		try {
			Statement stmt = m.mainConnection.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			this.m.getLogger().info("Erro ao tentar executar Query");
			this.m.getLogger().info(sql);
			this.m.getLogger().info(e.getMessage());
		}
		lock.unlock();
	}

	public void SQLQuerySyncNoLock(String sql) {
		try {
			Statement stmt = m.mainConnection.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			this.m.getLogger().info("Erro ao tentar executar Query");
			this.m.getLogger().info(sql);
			this.m.getLogger().info(e.getMessage());
		}
	}

}
