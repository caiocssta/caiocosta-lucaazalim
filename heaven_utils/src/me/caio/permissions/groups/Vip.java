package me.skater.permissions.groups;

import java.util.ArrayList;
import java.util.List;

public class Vip extends MainGroup {
	@Override
	public List<String> getPermissions() {
		List<String> permissions = new ArrayList<String>();
		permissions.add("heavenmc.vip");
		return permissions;
	}
}
