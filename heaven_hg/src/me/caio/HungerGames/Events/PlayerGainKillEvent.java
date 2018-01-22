package me.caio.HungerGames.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerGainKillEvent extends Event {
	public static final HandlerList handlers = new HandlerList();
	private Player player;
	private int kills;

	public PlayerGainKillEvent(Player player, int kills) {
		this.player = player;
		this.kills = kills;
	}

	public Player getPlayer() {
		return this.player;
	}

	public int getKills() {
		return this.kills;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
