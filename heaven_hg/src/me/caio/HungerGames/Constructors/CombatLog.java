package me.caio.HungerGames.Constructors;

import java.util.UUID;

public class CombatLog {
	private UUID combatLogged;
	private long time;

	public CombatLog(UUID combatLogged) {
		this.combatLogged = combatLogged;
		this.time = (System.currentTimeMillis() + 10000L);
	}

	public UUID getCombatLogged() {
		return this.combatLogged;
	}

	public long getTime() {
		return this.time;
	}

	public void hitted(UUID uuid) {
		this.combatLogged = uuid;
		this.time = (System.currentTimeMillis() + 10000L);
	}
}
