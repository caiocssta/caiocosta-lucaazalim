package me.skater.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemBuilder {

	private ItemStack itemStack;

	public ItemBuilder setMaterial(Material type) {
		itemStack = new ItemStack(type);
		return this;
	}

	public ItemBuilder setFast(Material type, String name, int data) {
		setMaterial(type);
		setName(name);
		setDurability(data);
		return this;
	}

	public ItemBuilder setFast(Material type, String name) {
		setMaterial(type);
		setName(name);
		return this;
	}

	public ItemBuilder setType(Material type) {
		itemStack = new ItemStack(type);
		itemStack.setType(type);
		return this;
	}

	public ItemBuilder setAmount(int amount) {
		itemStack.setAmount(amount);
		return this;
	}

	public ItemBuilder setDurability(int durability) {
		itemStack.setDurability((short) durability);
		return this;
	}

	public ItemBuilder setName(String name) {
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(name);
		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setDescription(String... desc) {
		ItemMeta meta = itemStack.getItemMeta();
		meta.setLore(Arrays.asList(desc));
		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setDescription(String text) {
		List<String> lore = new ArrayList<>();
		String[] split = text.split(" ");
		text = "";

		for (int i = 0; i < split.length; ++i) {
			if (ChatColor.stripColor(text).length() > 25 || ChatColor.stripColor(text).endsWith(".") || ChatColor.stripColor(text).endsWith("!")) {
				lore.add("§7" + text);
				if (text.endsWith(".") || text.endsWith("!")) {
					lore.add("");
				}
				text = "";
			}
			String toAdd = split[i];
			if (toAdd.contains("\n")) {
				toAdd = toAdd.substring(0, toAdd.indexOf("\n"));
				split[i] = split[i].substring(toAdd.length() + 1);
				lore.add("§7" + text + ((text.length() == 0) ? "" : " ") + toAdd);
				text = "";
				--i;
			} else {
				text += ((text.length() == 0) ? "" : " ") + toAdd;
			}
		}
		lore.add("§7" + text);
		setDescription(lore.toArray(new String[] {}));
		return this;
	}

	public ItemBuilder setEnchant(Enchantment[] enchant, int[] level) {
		for (int i = 0; i < enchant.length; ++i) {
			itemStack.addUnsafeEnchantment(enchant[i], level[i]);
		}
		return this;
	}

	public ItemBuilder setEnchant(Enchantment enchant, int level) {
		itemStack.addUnsafeEnchantment(enchant, level);
		return this;
	}

	public ItemBuilder setUnbreakable() {
		ItemMeta meta = itemStack.getItemMeta();
		meta.spigot().setUnbreakable(true);
		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setBreakable() {
		ItemMeta meta = itemStack.getItemMeta();
		meta.spigot().setUnbreakable(false);
		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setBreakable(boolean breakable) {
		ItemMeta meta = itemStack.getItemMeta();
		meta.spigot().setUnbreakable(!breakable);
		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder build(Player player, int... slot) {
		build(player.getInventory(), slot);
		player.updateInventory();
		return this;
	}

	public ItemBuilder build(Player player) {
		player.getInventory().addItem(itemStack);
		player.updateInventory();
		return this;
	}

	public ItemBuilder build(Inventory inventory, int... slot) {

		for (int slots : slot) {
			inventory.setItem(slots, itemStack);
		}

		return this;
	}

	public ItemBuilder build(Inventory inventory) {
		inventory.addItem(itemStack);
		return this;
	}

	public ItemStack getStack() {
		return itemStack;
	}

	public ItemMeta setName(ItemStack stack, String name) {
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name);
		return meta;
	}

	public ItemBuilder setSkull(String owner) {
		SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
		meta.setOwner(owner);
		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemStack setColor(Material material, Color color, String name) {
		ItemStack stack = new ItemStack(material);
		LeatherArmorMeta armorMeta = (LeatherArmorMeta) stack.getItemMeta();
		armorMeta.setColor(color);
		armorMeta.setDisplayName(name);
		stack.setItemMeta(armorMeta);
		return stack;
	}

	public ItemBuilder chanceItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
		return this;
	}

	public boolean checkItem(ItemStack item, String display) {
		return (item != null && item.getType() != Material.AIR && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase(display));
	}

}
