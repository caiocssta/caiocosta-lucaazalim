package me.caio.HungerGames.Listener;

import me.caio.HungerGames.Constructors.CombatLog;
import me.caio.HungerGames.Managers.CombatLogManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CombatLogListener implements Listener {
	private CombatLogManager manager;

	public CombatLogListener(CombatLogManager manager) {
		this.manager = manager;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDamage(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		if (!(event.getDamager() instanceof Player)) {
			return;
		}
		Player damager = (Player) event.getDamager();
		Player damaged = (Player) event.getEntity();
		this.manager.newCombatLog(damaged.getUniqueId(), damager.getUniqueId());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onDeath(PlayerDeathEvent event) {
		Player p = event.getEntity();
		this.manager.removeCombatLog(p.getUniqueId());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		CombatLog log = this.manager.getCombatLog(p.getUniqueId());
		if (log == null) {
			return;
		}
		if (System.currentTimeMillis() < log.getTime()) {
			Player combatLogger = Bukkit.getPlayer(log.getCombatLogged());
			if ((combatLogger != null) && (combatLogger.isOnline())) {
				p.damage(10000.0D, combatLogger);
			}
		}
		this.manager.removeCombatLog(p.getUniqueId());
	}

	@EventHandler
	public void onVoidDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		if (event.getCause() != EntityDamageEvent.DamageCause.VOID) {
			return;
		}
		Player p = (Player) event.getEntity();
		CombatLog log = this.manager.getCombatLog(p.getUniqueId());
		if (log == null) {
			return;
		}
		if (System.currentTimeMillis() < log.getTime()) {
			Player combatLogger = Bukkit.getPlayer(log.getCombatLogged());
			if ((combatLogger != null) && (combatLogger.isOnline())) {
				p.damage(10000.0D, combatLogger);
			}
		}
		this.manager.removeCombatLog(p.getUniqueId());
	}
}
