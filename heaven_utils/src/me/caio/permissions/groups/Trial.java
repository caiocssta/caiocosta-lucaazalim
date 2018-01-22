package me.skater.permissions.groups;

import java.util.ArrayList;
import java.util.List;

public class Trial extends MainGroup {
	@Override
	public List<String> getPermissions() {
		List<String> permissions = new ArrayList<String>();
		permissions.add("heavenmc.trial");
		permissions.add("minecraft.command.tp");
		permissions.add("bukkit.command.teleport");
		permissions.add("bukkit.command.kick");
		permissions.add("minecraft.command.kick");
		return permissions;
	}
}
