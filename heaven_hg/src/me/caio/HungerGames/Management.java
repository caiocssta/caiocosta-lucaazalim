package me.caio.HungerGames;

import java.util.logging.Logger;

import org.bukkit.Server;

public abstract class Management {
	private Main main;

	public Management(Main main) {
		this.main = main;
	}

	public abstract void onEnable();

	public abstract void onDisable();

	public Server getServer() {
		return this.main.getServer();
	}

	public Main getPlugin() {
		return this.main;
	}

	public Logger getLogger() {
		return this.main.getLogger();
	}

}
