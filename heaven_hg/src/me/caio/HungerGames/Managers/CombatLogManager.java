package me.caio.HungerGames.Managers;

import java.util.HashMap;
import java.util.UUID;

import me.caio.HungerGames.Constructors.CombatLog;

public class CombatLogManager {
	private HashMap<UUID, CombatLog> combats;

	public CombatLogManager() {
		this.combats = new HashMap<UUID, CombatLog>();
	}

	public CombatLog getCombatLog(UUID uuid) {
		return this.combats.containsKey(uuid) ? (CombatLog) this.combats.get(uuid) : null;
	}

	public void newCombatLog(UUID damaged, UUID damager) {
		CombatLog damagedCombatLog = (CombatLog) this.combats.get(damaged);
		if (damagedCombatLog == null) {
			damagedCombatLog = (CombatLog) this.combats.put(damaged, new CombatLog(damager));
		} else {
			damagedCombatLog.hitted(damager);
		}
		CombatLog damagerCombatLog = (CombatLog) this.combats.get(damager);
		if (damagerCombatLog == null) {
			damagerCombatLog = (CombatLog) this.combats.put(damager, new CombatLog(damaged));
		} else {
			damagerCombatLog.hitted(damaged);
		}
	}

	public void removeCombatLog(UUID uuid) {
		CombatLog combatLog = (CombatLog) this.combats.get(uuid);
		if (combatLog == null) {
			return;
		}
		UUID otherPlayer = combatLog.getCombatLogged();
		CombatLog otherPlayerCombatLog = (CombatLog) this.combats.get(otherPlayer);
		if ((otherPlayerCombatLog != null) && (otherPlayerCombatLog.getCombatLogged() == uuid)) {
			this.combats.remove(otherPlayer);
		}
		this.combats.remove(uuid);
	}
}
