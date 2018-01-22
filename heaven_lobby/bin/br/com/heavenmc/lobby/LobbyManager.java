package br.com.wombocraft.lobby;

import br.com.wombocraft.lobby.admin.AdminManager;
import br.com.wombocraft.lobby.gamer.GamerManager;
import br.com.wombocraft.lobby.menu.MenuManager;
import br.com.wombocraft.lobby.mysql.MysqlManager;
import br.com.wombocraft.lobby.rank.PermissionManager;
import br.com.wombocraft.lobby.rank.tag.TagManager;
import br.com.wombocraft.lobby.scoreboard.ScoreBoardManager;
import br.com.wombocraft.lobby.server.ServerManager;

public class LobbyManager {

	private Lobby _lobby;

	private MysqlManager _mysqlManager;
	private GamerManager _gamerManager;
	private TagManager _tagManager;
	private AdminManager _adminManager;
	private MenuManager _menumanager;
	private PermissionManager _permissionManager;
	private ScoreBoardManager _scoreboardManager;
	private ServerManager _serverManager;

	public LobbyManager(Lobby lobby) {
		this._lobby = lobby;
		this._mysqlManager = new MysqlManager(this);
		this._gamerManager = new GamerManager(this);
		this._tagManager = new TagManager(this);
		this._menumanager = new MenuManager(this);
		this._adminManager = new AdminManager(this);
		this._scoreboardManager = new ScoreBoardManager(this);
		this._permissionManager = new PermissionManager(this);
		this._serverManager = new ServerManager(this);
	}

	public Lobby getLobby() {
		return this._lobby;
	}

	public MysqlManager getMysqlManager() {
		return this._mysqlManager;
	}

	public GamerManager getGamerManager() {
		return this._gamerManager;
	}

	public TagManager getTagManager() {
		return this._tagManager;
	}

	public MenuManager getMenuManager() {
		return this._menumanager;
	}

	public AdminManager getAdminManager() {
		return this._adminManager;
	}

	public ScoreBoardManager getScoreboardManager() {
		return this._scoreboardManager;
	}

	public PermissionManager getPermissionManager() {
		return this._permissionManager;
	}

	public ServerManager getServerManager() {
		return this._serverManager;
	}

}
