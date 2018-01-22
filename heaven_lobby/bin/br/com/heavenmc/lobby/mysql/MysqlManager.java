package br.com.wombocraft.lobby.mysql;

import br.com.wombocraft.lobby.LobbyManager;
import br.com.wombocraft.lobby.LobbyPlugin;
import br.com.wombocraft.lobby.mysql.querys.GamerQuerys;

public class MysqlManager extends LobbyPlugin {

	private MysqlConnection _mysqlConnection;

	private GamerQuerys _gamerquerys;

	public MysqlManager(LobbyManager lbm) {
		super("Mysql Manager", lbm);
		this._mysqlConnection = new MysqlConnection(lbm.getLobby());
		_mysqlConnection.trySQLConnection();
		loadQuerys();
	}

	public void loadQuerys() {
		this._gamerquerys = new GamerQuerys(this);
	}

	public GamerQuerys getGamerQuerys() {
		return this._gamerquerys;
	}

	public MysqlConnection getMysqlConnection() {
		return this._mysqlConnection;
	}

}
