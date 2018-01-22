package me.skater.titles;

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

public class TitleListener implements Listener {
	private TitleManager manager;

	public TitleListener(TitleManager manager) {
		this.manager = manager;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getPlayer().getItemInHand().equals(manager.gui)) {
			if (e.getAction() != Action.PHYSICAL) {
				manager.openTitleSelector(e.getPlayer());
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(final PlayerLoginEvent event) {
		new BukkitRunnable() {

			@Override
			public void run() {
				try {
					manager.loadPlayerTitles(event.getPlayer().getUniqueId());
				//	if (manager.getPlugin().type == ServerType.HG) {
					//	me.caio.HungerGames.Main.getInstance().getScoreboardManager().updatePlayerRank(event.getPlayer());
					//}
				} catch (Exception e) {
					event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Nao foi possivel carregar seus titulos!");
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
