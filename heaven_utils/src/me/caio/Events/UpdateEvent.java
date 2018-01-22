package me.skater.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UpdateEvent extends Event {
	public static final HandlerList handlers = new HandlerList();
	private UpdateType type;

	public UpdateEvent(UpdateType type) {
		this.type = type;
	}

	public UpdateType getType() {
		return this.type;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public static enum UpdateType {
		TICK,
		SECOND,
		MINUTE;
	}
}
