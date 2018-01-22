package br.com.wombocraft.lobby.rank.injector.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionDefault;

import br.com.wombocraft.lobby.Lobby;
import br.com.wombocraft.lobby.gamer.Gamer;
import br.com.wombocraft.lobby.rank.PermissionManager;
import br.com.wombocraft.lobby.rank.enums.RankType;

public class PermissionListeners implements Listener {
	private final Map<UUID, PermissionAttachment> attachments;
	private PermissionManager pm;

	public PermissionListeners(PermissionManager pm) {
		this.attachments = new HashMap<UUID, PermissionAttachment>();
		this.pm = pm;
	}

	public PermissionManager getPermissionManager() {
		return this.pm;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onLogin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Gamer g = Lobby.getLobbyManager().getGamerManager().getGamerByUUID(player.getUniqueId());
		updateAttachment(player, g.getRank());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onMonitorLogin(PlayerLoginEvent event) {
		if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
			removeAttachment(event.getPlayer());
		}
	}

	protected void updateAttachment(Player player, RankType rank) {
		PermissionAttachment attach = (PermissionAttachment) this.attachments.get(player.getUniqueId());
		Permission playerPerm = getCreateWrapper(player, player.getUniqueId().toString());
		if (attach == null) {
			attach = player.addAttachment(Lobby.getLobby());
			this.attachments.put(player.getUniqueId(), attach);
			attach.setPermission(playerPerm, true);
		}
		playerPerm.getChildren().clear();
		for (String perm : rank.getPermissions()) {
			if (!playerPerm.getChildren().containsKey(perm)) {
				playerPerm.getChildren().put(perm, Boolean.valueOf(true));
			}
		}
		player.recalculatePermissions();
	}

	private Permission getCreateWrapper(Player player, String name) {
		Permission perm = Lobby.getLobby().getServer().getPluginManager().getPermission(name);
		if (perm == null) {
			perm = new Permission(name, "Permissao interna", PermissionDefault.FALSE);
			Lobby.getLobby().getServer().getPluginManager().addPermission(perm);
		}
		return perm;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent event) {
		removeAttachment(event.getPlayer());
		Lobby.getLobbyManager().getTagManager().removePlayerTag(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onKick(PlayerKickEvent event) {
		removeAttachment(event.getPlayer());
	}

	protected void removeAttachment(Player player) {
		PermissionAttachment attach = (PermissionAttachment) this.attachments.remove(player.getUniqueId());
		if (attach != null) {
			attach.remove();
		}
		Lobby.getLobby().getServer().getPluginManager().removePermission(player.getUniqueId().toString());
	}

	public void onDisable() {
		for (PermissionAttachment attach : this.attachments.values()) {
			attach.remove();
		}
		this.attachments.clear();
	}
}
