package me.skater.permissions.listeners;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.skater.Main;
import me.skater.permissions.enums.Group;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.scheduler.BukkitRunnable;

public class LoginListener implements Listener {
	private Main main;
	private final Map<UUID, PermissionAttachment> attachments;

	public LoginListener(Main main) {
		this.attachments = new HashMap<UUID, PermissionAttachment>();
		this.main = main;
		new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				for (Player player : LoginListener.this.main.getServer().getOnlinePlayers()) {
					Group group = LoginListener.this.main.getPermissionManager().getPlayerGroup(player);
					LoginListener.this.updateAttachment(player, group);
				}
			}
		}.runTaskLater(main, 10L);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onAsyng(AsyncPlayerPreLoginEvent event) {
		try {
			this.main.getPermissionManager().loadPlayerGroup(event.getUniqueId());
		} catch (SQLException e) {
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Nao foi possivel carregar suas permissoes.");
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		Group group = this.main.getPermissionManager().getPlayerGroup(player);
		updateAttachment(player, group);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onMonitorLogin(PlayerLoginEvent event) {
		if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
			removeAttachment(event.getPlayer());
			this.main.getPermissionManager().removePlayerGroup(event.getPlayer().getUniqueId());
		}
	}

	protected void updateAttachment(Player player, Group group) {
		PermissionAttachment attach = this.attachments.get(player.getUniqueId());
		Permission playerPerm = getCreateWrapper(player, player.getUniqueId().toString());
		if (attach == null) {
			attach = player.addAttachment(this.main);
			this.attachments.put(player.getUniqueId(), attach);
			attach.setPermission(playerPerm, true);
		}
		playerPerm.getChildren().clear();
		for (String perm : group.getGroup().getPermissions()) {
			if (!playerPerm.getChildren().containsKey(perm)) {
				playerPerm.getChildren().put(perm, Boolean.valueOf(true));
			}
		}
		player.recalculatePermissions();
	}

	private Permission getCreateWrapper(Player player, String name) {
		Permission perm = this.main.getServer().getPluginManager().getPermission(name);
		if (perm == null) {
			perm = new Permission(name, "Permissao interna", PermissionDefault.FALSE);
			this.main.getServer().getPluginManager().addPermission(perm);
		}
		return perm;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent event) {
		removeAttachment(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onKick(PlayerKickEvent event) {
		removeAttachment(event.getPlayer());
	}

	protected void removeAttachment(Player player) {
		PermissionAttachment attach = this.attachments.remove(player.getUniqueId());
		if (attach != null) {
			attach.remove();
		}
		this.main.getServer().getPluginManager().removePermission(player.getUniqueId().toString());
	}

	public void onDisable() {
		for (PermissionAttachment attach : this.attachments.values()) {
			attach.remove();
		}
		this.attachments.clear();
	}
}
