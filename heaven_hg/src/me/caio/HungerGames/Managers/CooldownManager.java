package me.caio.HungerGames.Managers;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import me.caio.HungerGames.Main;

import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class CooldownManager {
	private Table<UUID, String, Long> cooldowns;
	private Main main;

	public CooldownManager(Main main) {
		this.cooldowns = HashBasedTable.create();
		this.main = main;
	}

	public void checkCooldowns() {
		new BukkitRunnable() {
			@SuppressWarnings("unchecked")
			public void run() {
				Iterator<?> l;
				for (Iterator<?> l2 = CooldownManager.this.cooldowns.columnMap().values().iterator(); l2.hasNext(); l.hasNext()) {
					Map<UUID, Long> map = (Map<UUID, Long>) l2.next();
					l = map.entrySet().iterator();
					continue;
				}
			}
		}.runTaskTimerAsynchronously(this.main, 20L, 20L);
	}

	public void removeAllCooldowns(UUID id) {
		this.cooldowns.row(id).clear();
	}

	public boolean hasCooldown(UUID id, String key) {
		if ((this.cooldowns.containsRow(id)) && (this.cooldowns.row(id).containsKey(key))) {
			return true;
		}
		return false;
	}

	public void removeCooldown(UUID id, String key) {
		if (hasCooldown(id, key)) {
			this.cooldowns.remove(id, key);
		}
	}

	public void setCooldown(UUID id, String key, int seconds) {
		this.cooldowns.put(id, key, Long.valueOf(System.currentTimeMillis() + seconds * 1000));
	}

	public boolean isOnCooldown(UUID id, String key) {
		if (hasCooldown(id, key)) {
			if (System.currentTimeMillis() - getLongTime(id, key).longValue() >= 0L) {
				removeCooldown(id, key);
				return false;
			}
			return true;
		}
		return false;
	}

	public String getCooldownTimeFormated(UUID id, String key) {
		if (isOnCooldown(id, key)) {
			return this.main.getTime(Integer.valueOf((int) ((getLongTime(id, key).longValue() - System.currentTimeMillis()) / 1000L)));
		}
		return "1 segundo";
	}

	private Long getLongTime(UUID id, String key) {
		if (hasCooldown(id, key)) {
			return (Long) this.cooldowns.row(id).get(key);
		}
		return Long.valueOf(System.currentTimeMillis());
	}
}
