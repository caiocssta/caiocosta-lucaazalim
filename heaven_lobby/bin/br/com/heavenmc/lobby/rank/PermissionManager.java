package br.com.wombocraft.lobby.rank;

import br.com.wombocraft.lobby.LobbyManager;
import br.com.wombocraft.lobby.LobbyPlugin;
import br.com.wombocraft.lobby.rank.injector.PermissionMatcher;
import br.com.wombocraft.lobby.rank.injector.RegExpMatcher;
import br.com.wombocraft.lobby.rank.injector.listeners.PermissionListeners;
import br.com.wombocraft.lobby.rank.injector.regexperms.RegexPermissions;

public class PermissionManager extends LobbyPlugin {
	private RegexPermissions regexPerms;
	protected PermissionMatcher matcher;
	protected PermissionListeners superms;

	public PermissionManager(LobbyManager lbm) {
		super("Permissão Manager", lbm);
		this.regexPerms = new RegexPermissions(this);
		this.matcher = new RegExpMatcher();
		this.superms = new PermissionListeners(this);
		RegisterEvents(this.superms);
	}

	public RegexPermissions getRegexPerms() {
		return this.regexPerms;
	}

	public PermissionMatcher getPermissionMatcher() {
		return this.matcher;
	}

	public void disable() {
		if (this.regexPerms != null) {
			this.regexPerms.onDisable();
			this.regexPerms = null;
		}
		if (this.superms != null) {
			this.superms.onDisable();
			this.superms = null;
		}
	}
}
