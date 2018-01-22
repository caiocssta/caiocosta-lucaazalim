package me.skater.ranking;

import java.sql.SQLException;
import java.util.UUID;

import me.skater.Enums.ServerType;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class RankingListener implements Listener {
	private RankingManager manager;

	public RankingListener(RankingManager manager) {
		this.manager = manager;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getPlayer().getItemInHand().equals(manager.gui)) {
			if (e.getAction() != Action.PHYSICAL) {
				manager.openRankSelector(e.getPlayer());
			}
		}
	}

	@EventHandler
	public void onFirstJoin(final PlayerLoginEvent e) {
		new BukkitRunnable() {

			@Override
			public void run() {
				try {
					manager.refreshPlayer(e.getPlayer());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}.runTaskAsynchronously(manager.getPlugin());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(final PlayerLoginEvent event) {
		new BukkitRunnable() {

			@Override
			public void run() {
				try {
					manager.loadPlayerRank(event.getPlayer().getUniqueId());
				//	if (manager.getPlugin().type == ServerType.HG) {
				//		me.caio.HungerGames.Main.getInstance().getScoreboardManager().updatePlayerRank(event.getPlayer());
				//	}
					if (manager.getPlugin().type == ServerType.LOBBY) {
						// me.caio.Lobby.instance.getScoreboard().updatePlayerRank(event.getPlayer());
					}
				} catch (Exception e) {
					event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Nao foi possível carregar seu rank!");
				}
			}
		}.runTaskAsynchronously(manager.getPlugin());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();
		this.manager.removePlayerRank(uuid);
	}

}
