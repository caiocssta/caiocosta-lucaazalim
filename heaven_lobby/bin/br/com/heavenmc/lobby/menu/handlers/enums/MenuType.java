package br.com.wombocraft.lobby.menu.handlers.enums;

import br.com.wombocraft.lobby.menu.handlers.InventoryCustom;
import br.com.wombocraft.lobby.menu.servers.ServersMenu;

public enum MenuType {
	SERVER(ServersMenu.class);

	Class<? extends InventoryCustom> classe;

	private MenuType(Class<? extends InventoryCustom> classe) {
		this.classe = classe;
	}

	public Class<? extends InventoryCustom> getClasse() {
		return this.classe;
	}
}
