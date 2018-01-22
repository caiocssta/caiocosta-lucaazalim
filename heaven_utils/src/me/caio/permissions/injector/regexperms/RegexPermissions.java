package me.skater.permissions.injector.regexperms;

import java.util.logging.Level;

import me.skater.permissions.PermissionManager;
import me.skater.permissions.injector.CraftBukkitInterface;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.Permissible;

public class RegexPermissions {
	private final PermissionManager plugin;
	private PermissionList permsList;
	private PEXPermissionSubscriptionMap subscriptionHandler;

	public RegexPermissions(PermissionManager plugin) {
		this.plugin = plugin;
		this.subscriptionHandler = PEXPermissionSubscriptionMap.inject(plugin.getPlugin(), plugin.getServer().getPluginManager());
		this.permsList = PermissionList.inject(plugin.getServer().getPluginManager());
		plugin.getServer().getPluginManager().registerEvents(new EventListener(), plugin.getPlugin());
		injectAllPermissibles();
	}

	public void onDisable() {
		this.subscriptionHandler.uninject();
		uninjectAllPermissibles();
	}

	public PermissionList getPermissionList() {
		return this.permsList;
	}

	public void injectPermissible(Player player) {
		try {
			PermissiblePEX permissible = new PermissiblePEX(player, this.plugin);
			PermissibleInjector injector = new PermissibleInjector.ClassPresencePermissibleInjector(CraftBukkitInterface.getCBClassName("entity.CraftHumanEntity"), "perm", true);
			boolean success = false;
			if (injector.isApplicable(player)) {
				Permissible oldPerm = injector.inject(player, permissible);
				if (oldPerm != null) {
					permissible.setPreviousPermissible(oldPerm);
					success = true;
				}
			}
			if (!success) {
				this.plugin.getPlugin().getLogger().warning("Unable to inject PEX's permissible for " + player.getName());
			}
			permissible.recalculatePermissions();
		} catch (Throwable e) {
			this.plugin.getPlugin().getLogger().log(Level.SEVERE, "Unable to inject permissible for " + player.getName(), e);
		}
	}

	private void injectAllPermissibles() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			injectPermissible(player);
		}
	}

	private void uninjectPermissible(Player player) {
		try {
			boolean success = false;
			PermissibleInjector injector = new PermissibleInjector.ClassPresencePermissibleInjector(CraftBukkitInterface.getCBClassName("entity.CraftHumanEntity"), "perm", true);
			if (injector.isApplicable(player)) {
				Permissible pexPerm = injector.getPermissible(player);
				if ((pexPerm instanceof PermissiblePEX)) {
					if (injector.inject(player, ((PermissiblePEX) pexPerm).getPreviousPermissible()) != null) {
						success = true;
					}
				} else {
					success = true;
				}
			}
			if (!success) {
				this.plugin.getPlugin().getLogger().warning("No Permissible injector found for your server implementation (while uninjecting for " + player.getName() + "!");
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void uninjectAllPermissibles() {
		for (Player player : this.plugin.getServer().getOnlinePlayers()) {
			uninjectPermissible(player);
		}
	}

	private class EventListener implements Listener {
		private EventListener() {
		}

		@EventHandler(priority = EventPriority.LOWEST)
		public void onPlayerLogin(PlayerLoginEvent event) {
			injectPermissible(event.getPlayer());
		}

		@EventHandler(priority = EventPriority.MONITOR)
		public void onPlayerQuit(PlayerQuitEvent event) {
			uninjectPermissible(event.getPlayer());
		}

		@EventHandler(priority = EventPriority.MONITOR)
		public void onPlayerKick(PlayerKickEvent event) {
			uninjectPermissible(event.getPlayer());
		}
	}
}
