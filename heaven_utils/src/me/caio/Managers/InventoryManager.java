package me.skater.Managers;

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayOutOpenWindow;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryManager {

	private HashMap<UUID, Inventory> playerInventory;
	private HashMap<UUID, String> openInventory;

	public InventoryManager() {
		this.playerInventory = new HashMap<>();
		this.openInventory = new HashMap<>();
	}

	public boolean isOpen(Player p) {
		return this.openInventory.containsKey(p.getUniqueId());
	}

	public String getOpenInventoryTitle(Player p) {
		return this.openInventory.get(p.getUniqueId());
	}

	public void handle(Player p) {
		if (playerInventory.containsKey(p.getUniqueId())) {
			playerInventory.remove(p.getUniqueId());
		}
		if (openInventory.containsKey(p.getUniqueId())) {
			openInventory.remove(p.getUniqueId());
		}
		updateTitle(p, "", 54);
		p.closeInventory();
	}

	public void openInventory(Player p, String title, int size) {
		if (!isOpen(p)) {
			p.openInventory(getPlayerInventory(p));
			openInventory.put(p.getUniqueId(), title);
		}
		if (openInventory.containsKey(p.getUniqueId())) {
			openInventory.put(p.getUniqueId(), title);
		}
		EntityPlayer ep = ((CraftPlayer) p).getHandle();
		PacketPlayOutOpenWindow openWindow = new PacketPlayOutOpenWindow(ep.activeContainer.windowId, 0, title, size, false);
		ep.playerConnection.sendPacket(openWindow);
		ep.updateInventory(ep.activeContainer);
		openWindow = null;
		ep = null;
	}

	public Inventory getPlayerInventory(Player p) {
		if (playerInventory.containsKey(p.getUniqueId())) {
			return playerInventory.get(p.getUniqueId());
		}
		Inventory inv = Bukkit.createInventory(p, 54, "");
		playerInventory.put(p.getUniqueId(), inv);
		return playerInventory.get(p.getUniqueId());
	}

	public static void updateTitle(Player p, String title, int size) {
		EntityPlayer ep = ((CraftPlayer) p).getHandle();

		PacketPlayOutOpenWindow openWindow = new PacketPlayOutOpenWindow(ep.activeContainer.windowId, 0, title, size, false);
		ep.playerConnection.sendPacket(openWindow);
		ep.updateInventory(ep.activeContainer);
		openWindow = null;
		ep = null;
	}

	@SuppressWarnings("deprecation")
	public void clearInventory(Player p) {
		p.getPlayer().getInventory().setArmorContents(new ItemStack[4]);
		p.getPlayer().getInventory().clear();
		p.getPlayer().setItemOnCursor(new ItemStack(0));
	}
}
