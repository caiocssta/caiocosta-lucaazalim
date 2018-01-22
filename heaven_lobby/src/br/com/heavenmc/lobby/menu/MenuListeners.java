package br.com.wombocraft.lobby.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import br.com.wombocraft.lobby.Lobby;
import br.com.wombocraft.lobby.gamer.Gamer;
import br.com.wombocraft.lobby.menu.servers.ServersMenu;

public class MenuListeners implements Listener {

	@EventHandler
	public void inventoryclickevent(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (!event.hasItem()) {
			return;
		}
		if (event.getItem().getType() == Material.COMPASS) {
			Gamer g = Lobby.getLobbyManager().getGamerManager().getGamerByUUID(p.getUniqueId());
			new ServersMenu(g).openInventory();
		}
	}

}
