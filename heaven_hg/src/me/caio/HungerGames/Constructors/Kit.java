package me.caio.HungerGames.Constructors;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public class Kit {
	public String name;
	public List<ItemStack> items;
	public List<String> kitInfo;
	public ItemStack icon;

	public Kit(String kitname, List<String> kitInfo, List<ItemStack> kititems, ItemStack icon) {
		this.name = kitname.toLowerCase();
		this.kitInfo = kitInfo;
		this.items = kititems;
		this.icon = icon;
	}

	public String getName() {
		return this.name;
	}

	public List<ItemStack> getItems() {
		return this.items;
	}

	public List<String> getKitInfo() {
		return this.kitInfo;
	}

	public ItemStack getIcon() {
		return this.icon;
	}
}
