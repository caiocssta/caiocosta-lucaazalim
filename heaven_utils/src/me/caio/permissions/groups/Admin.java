package me.skater.permissions.groups;

import java.util.ArrayList;
import java.util.List;

public class Admin extends MainGroup {
	@Override
	public List<String> getPermissions() {
		List<String> permissions = new ArrayList<String>();
		permissions.add("heavenmc.admin");
		permissions.add("minecraft.command.*");
		permissions.add("bukkit.command.*");
		return permissions;
	}
}
