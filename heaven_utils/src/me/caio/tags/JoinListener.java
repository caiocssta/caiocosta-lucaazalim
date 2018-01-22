package me.skater.tags;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinListener implements Listener {
	private TagManager manager;

	public JoinListener(final TagManager manager) {
		this.manager = manager;
		new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				for (Player player : JoinListener.this.manager.getServer().getOnlinePlayers()) {
					JoinListener.this.manager.addPlayerTag(player, manager.getDefaultTag(player));
				}
			}
		}.runTaskLater(manager.getPlugin(), 11L);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent event) {
		final Player p = event.getPlayer();
		manager.addPlayerTag(p, manager.getDefaultTag(p));
	}

}
