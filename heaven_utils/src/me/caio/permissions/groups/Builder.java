package me.skater.permissions.groups;

import java.util.ArrayList;
import java.util.List;

public class Builder extends MainGroup {
	@Override
	public List<String> getPermissions() {
		List<String> permissions = new ArrayList<String>();
		permissions.add("heavenmc.builder");
		permissions.add("minecraft.command.tp");
		permissions.add("bukkit.command.teleport");
		permissions.add("bukkit.command.gamemode");
		return permissions;
	}
}
