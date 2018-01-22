package me.skater.permissions.enums;

import me.skater.permissions.groups.Admin;
import me.skater.permissions.groups.Dono;
import me.skater.permissions.groups.MainGroup;
import me.skater.permissions.groups.Beta;
import me.skater.permissions.groups.Mod;
import me.skater.permissions.groups.Mvp;
import me.skater.permissions.groups.Normal;
import me.skater.permissions.groups.Pro;
import me.skater.permissions.groups.Trial;
import me.skater.permissions.groups.Vip;
import me.skater.permissions.groups.Youtuber;

public enum Group {
	NORMAL(new Normal()),
	VIP(new Vip()),
	MVP(new Mvp()),
	PRO(new Pro()),
	BETA(new Beta()),
	YOUTUBER(new Youtuber()),
	TRIAL(new Trial()),
	MOD(new Mod()),
	ADMIN(new Admin()),
	DONO(new Dono());

	private MainGroup group;

	private Group(MainGroup group) {
		this.group = group;
	}

	public MainGroup getGroup() {
		return this.group;
	}
}
