package me.caio.HungerGames.Managers;

import me.caio.HungerGames.Main;
import me.skater.permissions.enums.Group;

import org.bukkit.entity.Player;

public class Permissions {
	public Main m2;

	public Permissions(Main m2) {
		this.m2 = m2;

	}

	public boolean isCopaPlayer(Player p) {
		return me.skater.Main.getInstance().getCopaManager().isParticipating(p.getUniqueId());
	}

	public boolean isVip(Player p) {
		return (isMvp(p)) || (p.hasPermission("heavenmc.vip")) || (me.skater.Main.getInstance().getPermissionManager().isGroup(p, Group.VIP));
	}

	public boolean isMvp(Player p) {
		return (isPro(p)) || (p.hasPermission("heavenmc.mvp")) || (me.skater.Main.getInstance().getPermissionManager().isGroup(p, Group.MVP));
	}

	public boolean isPro(Player p) {
		return (isBeta(p) || p.hasPermission("heavenmc.pro")) || (me.skater.Main.getInstance().getPermissionManager().isGroup(p, Group.PRO));
	}

	public boolean isBeta(Player p) {
		return (isYouTuber(p)) || (p.hasPermission("heavenmc.beta")) || (me.skater.Main.getInstance().getPermissionManager().isGroup(p, Group.BETA));
	}

	public boolean isYouTuber(Player p) {
		return (isTrial(p)) || (p.hasPermission("heavenmc.youtuber")) || (me.skater.Main.getInstance().getPermissionManager().isGroup(p, Group.YOUTUBER));
	}

	public boolean isBuilder(Player p) {
		return (p.hasPermission("heavenmc.builder")) || (me.skater.Main.getInstance().getPermissionManager().isGroup(p, Group.MOD));
	}

	public boolean isTrial(Player p) {
		return (isMod(p)) || (p.hasPermission("heavenmc.trial")) || (me.skater.Main.getInstance().getPermissionManager().isGroup(p, Group.TRIAL));
	}

	public boolean isMod(Player p) {
		return (isAdmin(p)) || (p.hasPermission("heavenmc.mod")) || (me.skater.Main.getInstance().getPermissionManager().isGroup(p, Group.MOD));
	}

	public boolean isAdmin(Player p) {
		return (isOwner(p)) || (p.hasPermission("heavenmc.admin")) || (me.skater.Main.getInstance().getPermissionManager().isGroup(p, Group.ADMIN));
	}

	public boolean isOwner(Player p) {
		return (p.hasPermission("heavenmc.dono")) || (me.skater.Main.getInstance().getPermissionManager().isGroup(p, Group.DONO)) || (p.isOp());
	}

}
