package br.com.wombocraft.lobby.menu.handlers;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import br.com.wombocraft.lobby.Lobby;
import br.com.wombocraft.lobby.gamer.Gamer;
import br.com.wombocraft.lobby.menu.handlers.enums.MenuType;
import br.com.wombocraft.lobby.utils.cd.CooldownInventory;
import net.minecraft.server.v1_7_R4.Container;
import net.minecraft.server.v1_7_R4.ContainerChest;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayOutOpenWindow;

public abstract class InventoryCustom implements InventoryCustomInterface {

	private String _invName;
	private int _invSize;
	private Inventory _inv;
	private MenuType _invType;
	private Gamer _g;
	public Listener _listener;

	public InventoryCustom(Gamer g, MenuType mtype, String invname, int invsize, Inventory inv) {
		this._invName = invname;
		this._invSize = invsize;
		this._invType = mtype;
		this._g = g;
		if (g.hasOnInventory()) {
			this._inv = g.getPlayer().getOpenInventory().getTopInventory();
		} else {
			this._inv = Bukkit.createInventory(null, this._invSize, this._invName);
		}
	}

	public Inventory getInventory() {
		return this._inv;
	}

	public String getInventoryName() {
		return this._invName;
	}

	public int getSize() {
		return this._invSize;
	}

	public MenuType getInventoryType() {
		return this._invType;
	}

	public Gamer getGamer() {
		return this._g;
	}

	public Listener getListener() {
		return this._listener;
	}

	public void setSize(int size) {
		this._invSize = size;
	}

	public void setName(String name) {
		this._invName = name;
	}

	public InventoryCustom openInventory() {
		Player p = _g.getPlayer();
		if (!this._g.hasOnInventory()) {
			p.openInventory(this._inv);
		} else {
			this.getGamer().getInventory().unregister();
		}
		this._g.setInventory(this);
		this._inv = this.renameInventory(this._inv, this._g, this._invName, this._invSize);
		this._inv.clear();
		inventory();
		p.updateInventory();
		getNewListener();
		return this;
	}

	public Listener getNewListener() {
		this._listener = new Listener() {

			@EventHandler
			public void listenerclick(InventoryClickEvent event) {
				if (!(event.getWhoClicked() instanceof Player)) {
					return;
				}
				Player p = (Player) event.getWhoClicked();
				if (p != getGamer().getPlayer()) {
					return;
				}
				if (getGamer().getInventory().getInventoryType() != getInventoryType()) {
					return;
				}
				if (CooldownInventory.isInCooldown(p.getUniqueId())) {
					event.setCancelled(true);
					return;
				}
				new CooldownInventory(p.getUniqueId()).start();
				inventoryClickEvent(event);
			}

			@EventHandler
			public void close(InventoryCloseEvent event) {
				Player p = (Player) event.getPlayer();
				if (p != getGamer().getPlayer()) {
					return;
				}
				if (getGamer().getInventory().getInventoryType() != getInventoryType()) {
					return;
				}
				getGamer().removeInventory();
				unregister();
			}

		};
		Lobby.getLobby().getServer().getPluginManager().registerEvents(getListener(), Lobby.getLobby());
		return this._listener;
	}

	public void unregister() {
		HandlerList.unregisterAll(this.getListener());
	}

	private Inventory renameInventory(Inventory inv, Gamer g, String name, int size) {
		Player p = g.getPlayer();
		EntityPlayer entityPlayer = ((CraftPlayer) p).getHandle();
		int containerCounter = 0;
		if (!g.hasOnInventory()) {
			containerCounter = entityPlayer.nextContainerCounter();
			entityPlayer.activeContainer.windowId = containerCounter;
			entityPlayer.playerConnection
					.sendPacket(new PacketPlayOutOpenWindow(containerCounter, 0, name, size, true));
			return inv;
		} else {
			if (size != inv.getSize()) {
				entityPlayer.activeContainer.b(entityPlayer);
				Inventory inventory = Bukkit.createInventory(null, size, name);
				Container countainer = new ContainerChest(entityPlayer.inventory,
						((CraftInventory) inventory).getInventory());
				entityPlayer.activeContainer.transferTo(countainer, entityPlayer.getBukkitEntity());

				int c = entityPlayer.nextContainerCounter();
				entityPlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(c, 0, name, size, true));
				entityPlayer.activeContainer = countainer;
				entityPlayer.activeContainer.windowId = c;
				entityPlayer.activeContainer.addSlotListener(entityPlayer);
				return inventory;
			} else {
				containerCounter = entityPlayer.activeContainer.windowId;
				entityPlayer.activeContainer.windowId = containerCounter;
				entityPlayer.playerConnection
						.sendPacket(new PacketPlayOutOpenWindow(containerCounter, 0, name, size, true));
				return inv;
			}
		}

	}

	@Override
	public void inventory() {
	}

}
