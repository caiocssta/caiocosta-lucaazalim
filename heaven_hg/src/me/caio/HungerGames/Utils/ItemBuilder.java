package me.caio.HungerGames.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.server.v1_7_R4.NBTTagList;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ItemBuilder {
	private final ItemStack is;

	public ItemBuilder(final Material m) {
		is = CraftItemStack.asCraftCopy(new ItemStack(m));
	}

	public ItemBuilder(final Material m, int amout, int data) {
		is = CraftItemStack.asCraftCopy(new ItemStack(m, amout, (short) data));
	}

	public ItemBuilder(final ItemStack i) {
		this.is = i;
	}

	public ItemBuilder quantidade(final int qtd) {
		is.setAmount(qtd);
		return this;
	}

	public ItemBuilder nome(final String nome) {
		final ItemMeta meta = is.getItemMeta();
		meta.setDisplayName(nome);
		is.setItemMeta(meta);
		return this;
	}

	public ItemBuilder lore(List<String> lore) {
		final ItemMeta meta = is.getItemMeta();
		meta.setLore(lore);
		is.setItemMeta(meta);
		return this;
	}

	public ItemBuilder durabilidade(final int dur) {
		is.setDurability((short) dur);
		return this;
	}

	public ItemBuilder encantamento(final Enchantment enchantment, final int level) {
		is.addUnsafeEnchantment(enchantment, level);
		return this;
	}

	public ItemBuilder encantamento(final Enchantment enchantment) {
		is.addUnsafeEnchantment(enchantment, 1);
		return this;
	}

	public ItemBuilder tipo(final Material m) {
		is.setType(m);
		return this;
	}

	public ItemBuilder zerarLore() {
		final ItemMeta meta = is.getItemMeta();
		meta.setLore(new ArrayList<String>());
		is.setItemMeta(meta);
		return this;
	}

	public ItemBuilder addGlow() {
		net.minecraft.server.v1_7_R4.ItemStack nmsStack = (net.minecraft.server.v1_7_R4.ItemStack) getField(is, "handle");
		NBTTagCompound compound = nmsStack.tag;

		// Initialize the compound if we need to
		if (compound == null) {
			compound = new NBTTagCompound();
			nmsStack.tag = compound;
		}

		// Empty enchanting compound
		compound.set("ench", new NBTTagList());
		return this;
	}

	public ItemBuilder zerarEncantamento() {
		for (final Enchantment e : is.getEnchantments().keySet()) {
			is.removeEnchantment(e);
		}
		return this;
	}

	public ItemBuilder cor(Color c) {
		if (is.getType() == Material.LEATHER_BOOTS || is.getType() == Material.LEATHER_CHESTPLATE || is.getType() == Material.LEATHER_HELMET || is.getType() == Material.LEATHER_LEGGINGS) {
			LeatherArmorMeta meta = (LeatherArmorMeta) is.getItemMeta();
			meta.setColor(c);
			is.setItemMeta(meta);
			return this;
		} else {
			throw new IllegalArgumentException("Apenas armadura de leather pode receber cor!");
		}
	}

	public static org.bukkit.inventory.ItemStack removeAttributes(org.bukkit.inventory.ItemStack i) {
		if (i == null) {
			return i;
		}
		if (i.getType() == Material.BOOK_AND_QUILL) {
			return i;
		}
		org.bukkit.inventory.ItemStack item = i.clone();
		net.minecraft.server.v1_7_R4.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag;
		if (!nmsStack.hasTag()) {
			tag = new NBTTagCompound();
			nmsStack.setTag(tag);
		} else {
			tag = nmsStack.getTag();
		}
		NBTTagList am = new NBTTagList();
		tag.set("AttributeModifiers", am);
		nmsStack.setTag(tag);
		return CraftItemStack.asCraftMirror(nmsStack);
	}

	public ItemStack construir() {
		return is;
	}

	private Object getField(Object obj, String name) {
		try {
			Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);

			return field.get(obj);
		} catch (Exception e) {
			// We don't care
			throw new RuntimeException("Unable to retrieve field content.", e);
		}
	}
}
