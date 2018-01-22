package me.skater.Managers;

import me.skater.Main;
import me.skater.permissions.enums.Group;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Permissions {
	public Main m;

	public Permissions(Main m) {
		this.m = m;
	}

	public boolean isCopaPlayer(Player p) {
		return m.getCopaManager().isParticipating(p.getUniqueId());

	}

	public boolean isVip(Player p) {
		return (isMvp(p)) || (p.hasPermission("heavenmc.vip")) || (m.getPermissionManager().isGroup(p, Group.VIP));
	}

	public boolean isMvp(Player p) {
		return (isPro(p)) || (p.hasPermission("heavenmc.mvp")) || (m.getPermissionManager().isGroup(p, Group.MVP));
	}

	public boolean isPro(Player p) {
		return (isBeta(p)) || (p.hasPermission("heavenmc.pro")) || (m.getPermissionManager().isGroup(p, Group.PRO));
	}

	public boolean isBeta(Player p) {
		return (isYouTuber(p)) || (p.hasPermission("heavenmc.beta")) || (m.getPermissionManager().isGroup(p, Group.BETA));
	}

	public boolean isYouTuber(Player p) {
		return (isTrial(p)) || (p.hasPermission("heavenmc.youtuber")) || (m.getPermissionManager().isGroup(p, Group.YOUTUBER));
	}

	public boolean isBuilder(Player p) {
		return (p.hasPermission("heavenmc.builder")) || m.getPermissionManager().isGroup(p, Group.MOD);
	}

	public boolean isTrial(Player p) {
		return (isMod(p)) || (p.hasPermission("heavenmc.trial")) || (m.getPermissionManager().isGroup(p, Group.TRIAL));
	}

	public boolean isMod(Player p) {
		return (isAdmin(p)) || (p.hasPermission("heavenmc.mod")) || (m.getPermissionManager().isGroup(p, Group.MOD));
	}

	public boolean isAdmin(Player p) {
		return (isOwner(p)) || (p.hasPermission("heavenmc.admin")) || (m.getPermissionManager().isGroup(p, Group.ADMIN));
	}

	public boolean isOwner(Player p) {
		return (p.hasPermission("heavenmc.dono")) || (m.getPermissionManager().isGroup(p, Group.DONO)) || (p.isOp());
	}

	public boolean isTrial(CommandSender sender) {
		return (isMod(sender)) || (sender.hasPermission("heavenmc.trial"));
	}

	public boolean isMod(CommandSender sender) {
		return sender.hasPermission("heavenmc.mod");
	}

	public boolean isAdmin(CommandSender sender) {
		return sender.hasPermission("heavenmc.admin");
	}
}
