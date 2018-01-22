package me.skater.MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import me.skater.Main;

public class Connect {
	private Main m;
	public static ReentrantLock lock = new ReentrantLock(true);

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
			String ip = "localhost";
			this.m.getLogger().info("Tentando conexão com o mysql em " + ip);
			String conn = "jdbc:mysql://" + ip + ":" + 3306 + "/" + "heaven_utils" + "?autoReconnect=true&failOverReadOnly=false&maxReconnects=2147483647";
			return DriverManager.getConnection(conn, "root", "sv[-hNnnEKxrJ8_^");
		} catch (ClassNotFoundException ex) {
			this.m.getLogger().warning("MySQL Driver nao encontrado!");
			this.m.sql = false;
		} catch (SQLException ex) {
			this.m.getLogger().warning("Erro enquanto tentava conectar ao Mysql!");
			ex.printStackTrace();
			this.m.sql = false;
		} catch (Exception ex) {
			this.m.getLogger().warning("Erro desconhecido enquanto tentava conectar ao MySQL.");
			this.m.sql = false;
		}
		return null;
	}

	public void prepareSQL(Connection con) {
		if (this.m.sql) {
			SQLQuerySync(
					"CREATE TABLE IF NOT EXISTS `playertitles` (`ID` int(10) unsigned NOT NULL AUTO_INCREMENT, `Uuid` varchar(255) NOT NULL, `titles` varchar(255), `activetitle` int(10), PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1 ;");
			SQLQuerySync(
					"CREATE TABLE IF NOT EXISTS `playerranking` (`ID` int(10) unsigned NOT NULL AUTO_INCREMENT, `Uuid` varchar(255) NOT NULL, `coins` int(10), `rewards` varchar(255), `xp` int(10), `xptotal` int(10), `level` int(10), PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1 ;");
			SQLQuerySync(
					"CREATE TABLE IF NOT EXISTS `ranks` (`id` int(10) unsigned NOT NULL AUTO_INCREMENT, `uuid` varchar(255) NOT NULL, `rank` varchar(255) NOT NULL, PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1 ;");
			SQLQuerySync("CREATE TABLE IF NOT EXISTS `multiplier` (`id` int(10) unsigned NOT NULL AUTO_INCREMENT, `multiplier` int(10), PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1 ;");
			SQLQuerySync(
					"CREATE TABLE IF NOT EXISTS `uuidfetcher` (`id` int(10) unsigned NOT NULL AUTO_INCREMENT, `uuid` varchar(255) NOT NULL, `name` varchar(255) NOT NULL, `lastip` varchar(255) NOT NULL, PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1 ;");
			SQLQuerySync("CREATE TABLE IF NOT EXISTS `rewards` (`ID` int(10) unsigned NOT NULL AUTO_INCREMENT, `Uuid` varchar(255) NOT NULL, PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1 ;");
			SQLQuerySync(
					"CREATE TABLE IF NOT EXISTS `copahg` (`id` int(10) unsigned NOT NULL AUTO_INCREMENT, `uuid` varchar(255) NOT NULL, `grupo` varchar(255) NOT NULL, PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1 ;");
			SQLQuerySync(
					"CREATE TABLE IF NOT EXISTS `motd` (`ID` int(10) unsigned NOT NULL AUTO_INCREMENT, `server` varchar(255) NOT NULL, `motd` varchar(255) NOT NULL, PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1 ;");
			SQLQuerySync(
					"CREATE TABLE IF NOT EXISTS `broadcast` (`ID` int(10) unsigned NOT NULL AUTO_INCREMENT, `message` varchar(255) NOT NULL, PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1 ;");
			SQLQuerySync(
					"CREATE TABLE IF NOT EXISTS `changelog` (`ID` int(10) unsigned NOT NULL AUTO_INCREMENT, `changes` varchar(255) NOT NULL, PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1 ;");
			SQLQuerySync(
					"CREATE TABLE IF NOT EXISTS `clans` (`ID` int(10) unsigned NOT NULL AUTO_INCREMENT, `name` varchar(255) NOT NULL, `tag` varchar(255) NOT NULL, PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1 ;");
			SQLQuerySync(
					"CREATE TABLE IF NOT EXISTS `clans_players` (`ID` int(10) unsigned NOT NULL AUTO_INCREMENT, `name` varchar(255) NOT NULL, `uuid` varchar(255) NOT NULL, `rank` varchar(255) NOT NULL, PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1 ;");
			SQLQuerySync(
					"CREATE TABLE IF NOT EXISTS `clans_status` (`ID` int(10) unsigned NOT NULL AUTO_INCREMENT, `name` varchar(255) NOT NULL, `kills` int(10), `deaths` int(10), `wins` int(10), `coins` int(10), `elo` int(10), PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1 ;");
			SQLQuerySync("CREATE TABLE IF NOT EXISTS `fakebanned` (`ID` int(10) unsigned NOT NULL AUTO_INCREMENT, `Uuid` varchar(255) NOT NULL, PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1 ;");
			this.m.getLogger().info("Criando Tabelas no SQL");
		}
	}

	public static void SQLdisconnect(Connection con) {
		try {
			if ((con != null) && (!con.isClosed())) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void SQLdisconnect() {
		try {
			if ((m.mainConnection != null) && (!m.mainConnection.isClosed())) {
				m.mainConnection.close();
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
			@Override
			public void run() {
				Connect.lock.lock();
				try {
					Statement stmt = m.mainConnection.createStatement();
					stmt.executeUpdate(sql);
					stmt.close();
				} catch (SQLException e) {
					Connect.this.m.getLogger().info("Erro ao tentar executar Query");
					Connect.this.m.getLogger().info(sql);
					Connect.this.m.getLogger().info(e.getMessage());
				}
				Connect.lock.unlock();
			}
		}));
		executor.shutdown();
	}

	public void SQLQuerySync(String sql) {
		if (!this.m.sql) {
			return;
		}
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