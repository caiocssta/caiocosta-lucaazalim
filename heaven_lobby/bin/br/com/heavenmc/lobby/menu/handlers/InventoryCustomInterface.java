package br.com.wombocraft.lobby.menu.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface InventoryCustomInterface {

	public void inventory();

	@EventHandler
	public void inventoryClickEvent(InventoryClickEvent event);

}
