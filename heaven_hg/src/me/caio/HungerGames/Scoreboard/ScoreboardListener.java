package me.caio.HungerGames.Scoreboard;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Events.PlayerSelectKitEvent;
import me.caio.HungerGames.Events.TimeSecondEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ScoreboardListener implements Listener {
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoinListener(PlayerJoinEvent e) {
		// if(Main.getInstance().type == Type.TEAM) {
		// if(Main.getInstance().stage != GameStage.PREGAME) {
		// Main.getInstance().getScoreboardManager().createSecondScoreboardBase(e.getPlayer());
		// return;
		// }
		// }
		Main.getInstance().getScoreboardManager().createScoreboardBase(e.getPlayer());
		Main.getInstance().getScoreboardManager().updatePlayers();
	}

	@EventHandler
	public void onPlayerQuitListener(PlayerQuitEvent e) {
		Main.getInstance().getScoreboardManager().updatePlayers();
	}

	@EventHandler
	public void onPlayerDeathListener(PlayerDeathEvent e) {
		Main.getInstance().getScoreboardManager().updatePlayers();
		if ((e.getEntity().getKiller() != null) && ((e.getEntity().getKiller() instanceof Player))) {
			Main.getInstance().getScoreboardManager().updateKills(e.getEntity().getKiller());
		}
	}

	@EventHandler
	public void onTimeSecondListener(TimeSecondEvent e) {
		Main.getInstance().getScoreboardManager().updateTime();
	}

	@EventHandler
	public void onPlayerSelectKitListener(PlayerSelectKitEvent e) {
		Main.getInstance().getScoreboardManager().updateKit(e.getPlayer());
	}
}
