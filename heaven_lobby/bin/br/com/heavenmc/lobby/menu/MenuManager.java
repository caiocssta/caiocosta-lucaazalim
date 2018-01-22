package br.com.wombocraft.lobby.menu;

import br.com.wombocraft.lobby.LobbyManager;
import br.com.wombocraft.lobby.LobbyPlugin;

public class MenuManager extends LobbyPlugin {

	public MenuManager(LobbyManager lm) {
		super("Menu Manager", lm);
		RegisterEvents(new MenuListeners());
	}

}
