package me.caio.HungerGames.Utils;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class Name {
	private HashMap<String, String> NAMES = new HashMap<String, String>();

	public Name() {
		this.NAMES.put("AIR", "Punhos");
		this.NAMES.put("BOWL", "Pote");
		this.NAMES.put("MUSHROOM_SOUP", "Sopa");
		this.NAMES.put("ARROW", "Flecha");
		this.NAMES.put("SEED", "Semente");
		this.NAMES.put("DIRT", "Terra");
		this.NAMES.put("GRASS", "Grama");
		this.NAMES.put("MUSHROOM_SOUP", "Sopa");
		this.NAMES.put("COMPASS", "Bussola");
	}

	public String getEnchantName(Enchantment enchant) {
		return getName(enchant.getName());
	}

	@SuppressWarnings("deprecation")
	public String getItemName(ItemStack item) {
		if (item == null) {
			item = new ItemStack(0);
		}
		if (this.NAMES.containsKey(item.getType().name())) {
			return (String) this.NAMES.get(item.getType().name());
		}
		return getName(item.getType().name());
	}

	public String getName(String string) {
		if (this.NAMES.containsValue(string)) {
			return (String) this.NAMES.get(string);
		}
		return toReadable(string);
	}

	public String toReadable(String string) {
		String[] names = string.split("_");
		for (int i = 0; i < names.length; i++) {
			names[i] = (names[i].substring(0, 1) + names[i].substring(1).toLowerCase());
		}
		return StringUtils.join(names, " ");
	}
}
