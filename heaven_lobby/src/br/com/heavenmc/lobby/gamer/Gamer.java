package br.com.wombocraft.lobby.gamer;

import java.util.UUID;

import org.bukkit.entity.Player;

import br.com.wombocraft.lobby.menu.handlers.InventoryCustom;
import br.com.wombocraft.lobby.rank.enums.RankType;

public class Gamer {

	private Player _p;
	private String _name;
	private UUID _uuid;
	private RankType _group;
	private InventoryCustom _inventory;

	public Gamer(String name, UUID uuid, RankType rank) {
		this._name = name;
		this._uuid = uuid;
		this._group = rank;
		this._inventory = null;
	}

	public Gamer setInventory(InventoryCustom inv) {
		this._inventory = inv;
		return this;
	}

	public Gamer removeInventory() {
		this._inventory = null;
		return this;
	}

	public InventoryCustom getInventory() {
		return this._inventory;
	}

	public boolean hasOnInventory() {
		return this._inventory != null;
	}

	public void setPlayer(Player p) {
		this._p = p;
	}

	public Gamer setName(String name) {
		this._name = name;
		return this;
	}

	public String getName() {
		return this._name;
	}

	public UUID getUUID() {
		return this._uuid;
	}

	public Player getPlayer() {
		return this._p;
	}

	public RankType getRank() {
		return this._group;
	}
}
