package br.com.wombocraft.lobby.utils.cd;

import java.util.HashMap;
import java.util.UUID;

public class CooldownInventory {
	private static HashMap<String, CooldownInventory> cooldowns = new HashMap<String, CooldownInventory>();
	private long start;
	private final UUID id;

	public CooldownInventory(UUID id) {
		this.id = id;
	}

	public static boolean isInCooldown(UUID id) {
		if (getTimeLeft(id) >= 1) {
			return true;
		}
		stop(id);
		return false;
	}

	private static void stop(UUID id) {
		cooldowns.remove(id);
	}

	private static CooldownInventory getCooldown(UUID id) {
		return (CooldownInventory) cooldowns.get(id.toString());
	}

	public static int getTimeLeft(UUID id) {
		CooldownInventory cooldown = getCooldown(id);
		int f = -1;
		if (cooldown != null) {
			long now = System.currentTimeMillis();
			long cooldownTime = cooldown.start;
			int totalTime = 1;
			int r = (int) (now - cooldownTime) / 500;
			f = (r - totalTime) * -1;
		}
		return f;
	}

	public void start() {
		this.start = System.currentTimeMillis();
		cooldowns.put(this.id.toString(), this);
	}
}
