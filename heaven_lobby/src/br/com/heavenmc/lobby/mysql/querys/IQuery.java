package br.com.wombocraft.lobby.mysql.querys;

import java.sql.Connection;

import br.com.wombocraft.lobby.mysql.MysqlConnection;
import br.com.wombocraft.lobby.mysql.MysqlManager;

public class IQuery {

	private MysqlManager mysqlManager;

	public IQuery(MysqlManager mm) {
		this.mysqlManager = mm;
	}

	public Connection getCon() {
		return this.mysqlManager.getMysqlConnection().getConnection();
	}

	public MysqlConnection getMysqlCon() {
		return this.mysqlManager.getMysqlConnection();
	}

}
