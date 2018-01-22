package me.caio.HungerGames.Listener;

import java.util.HashMap;
import java.util.Map;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Events.TimeSecondEvent;
import me.caio.HungerGames.Utils.Enum.GameStage;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BorderListener implements Listener {
	private Main m;
	public Map<Player, Location> locations = new HashMap<Player, Location>();

	public BorderListener(Main m) {
		this.m = m;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onTime(TimeSecondEvent event) {
		for (Player p : this.m.getServer().getOnlinePlayers()) {
			if ((isOnWarning(p)) && (this.m.stage != GameStage.PREGAME)) {
				p.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Voce esta proximo da Borda!");
			} else {
				if ((isNotInBoard(p)) || (p.getLocation().getY() > 129.0D)) {
					if (this.m.stage == GameStage.PREGAME) {
						if (this.locations.containsKey(p)) {
							p.teleport((Location) this.locations.get(p));
							continue;
						}
					} else {
						p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Voce esta fora da Borda do Mundo!");
						EntityDamageEvent e = new EntityDamageEvent(p, EntityDamageEvent.DamageCause.CUSTOM, 4.0D);
						if (e.isCancelled()) {
							e.setCancelled(false);
						}
						p.setLastDamageCause(e);
						p.damage(4.0D);
					}
				}
				this.locations.put(p, p.getLocation());
			}
		}
	}

	private boolean isNotInBoard(Player p) {
		return (p.getLocation().getBlockX() > 500) || (p.getLocation().getBlockX() < -500) || (p.getLocation().getBlockZ() > 500) || (p.getLocation().getBlockZ() < -500);
	}

	private boolean isOnWarning(Player p) {
		return (!isNotInBoard(p)) && ((p.getLocation().getBlockX() > 480) || (p.getLocation().getBlockX() < -480) || (p.getLocation().getBlockZ() > 480) || (p.getLocation().getBlockZ() < -480));
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (isOnWarning(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (isOnWarning(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (isOnWarning(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
}
