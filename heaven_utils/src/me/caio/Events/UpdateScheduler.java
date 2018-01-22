package me.skater.Events;

import org.bukkit.Bukkit;

public class UpdateScheduler implements Runnable {
	private long lastSecond = -9223372036854775808L;
	private long lastMinute = -9223372036854775808L;

	@Override
	public void run() {
		Bukkit.getPluginManager().callEvent(new UpdateEvent(UpdateEvent.UpdateType.TICK));
		if (this.lastSecond + 1000L <= System.currentTimeMillis()) {
			Bukkit.getPluginManager().callEvent(new UpdateEvent(UpdateEvent.UpdateType.SECOND));
			this.lastSecond = System.currentTimeMillis();
		}
		if (this.lastMinute + 60000L <= System.currentTimeMillis()) {
			Bukkit.getPluginManager().callEvent(new UpdateEvent(UpdateEvent.UpdateType.MINUTE));
			this.lastMinute = System.currentTimeMillis();
		}
	}
}
