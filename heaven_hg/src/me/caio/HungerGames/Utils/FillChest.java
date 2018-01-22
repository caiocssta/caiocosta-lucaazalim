package me.caio.HungerGames.Utils;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class FillChest {

	public static void feast(Chest chest) {
		Inventory i = chest.getInventory();

		if (c(40)) {
			i.setItem(a(26), new ItemStack(Material.DIAMOND_SWORD, 1));
		}
		if (c(40)) {
			i.setItem(a(26), new ItemStack(Material.DIAMOND_HELMET, 1));
		}
		if (c(40)) {
			i.setItem(a(26), new ItemStack(Material.DIAMOND_CHESTPLATE, 1));
		}
		if (c(40)) {
			i.setItem(a(26), new ItemStack(Material.DIAMOND_LEGGINGS, 1));
		}
		if (c(40)) {
			i.setItem(a(26), new ItemStack(Material.DIAMOND_BOOTS, 1));
		}
		if (c(25)) {
			i.setItem(a(26), new ItemStack(Material.IRON_HELMET, 1));
		}
		if (c(25)) {
			i.setItem(a(26), new ItemStack(Material.IRON_CHESTPLATE, 1));
		}
		if (c(25)) {
			i.setItem(a(26), new ItemStack(Material.IRON_LEGGINGS, 1));
		}
		if (c(25)) {
			i.setItem(a(26), new ItemStack(Material.IRON_BOOTS, 1));
		}
		if (c(25)) {
			i.setItem(a(26), new ItemStack(Material.ENDER_PEARL, a(16)));
		}
		if (c(25)) {
			i.setItem(a(26), new ItemStack(Material.EXP_BOTTLE, a(20)));
		}
		if (c(50)) {
			i.setItem(a(26), new ItemStack(Material.MUSHROOM_SOUP, a(14)));
		}
		if (c(35)) {
			i.setItem(a(26), new ItemStack(Material.FLINT_AND_STEEL));
		}
		if (c(25)) {
			i.setItem(a(26), new ItemStack(Material.TNT, a(25)));
		}
		if (c(25)) {
			i.setItem(a(26), new ItemStack(Material.GOLDEN_APPLE, a(10)));
		}
		if (c(30)) {
			i.setItem(a(26), new ItemStack(Material.INK_SACK, a(20), (short) 3));
		}
		if (c(30)) {
			i.setItem(a(26), new ItemStack(Material.CACTUS, a(20), (short) 3));
		}
		if (c(20)) {
			i.setItem(a(26), new ItemStack(Material.LAVA_BUCKET, 1));
		}
		if (c(20)) {
			i.setItem(a(26), new ItemStack(Material.WATER_BUCKET, 1));
		}
		if (c(20)) {
			i.setItem(a(26), new ItemStack(Material.WATER, 1));
		}
		if (c(30)) {
			i.setItem(a(26), new ItemStack(Material.POTION, 1, (short) 16420));
		}
		if (c(30)) {
			i.setItem(a(26), new ItemStack(Material.POTION, 1, (short) 8259));
		}
		if (c(30)) {
			i.setItem(a(26), new ItemStack(Material.POTION, 1, (short) 16393));
		}
		if (c(30)) {
			i.setItem(a(26), new ItemStack(Material.POTION, 1, (short) 16428));
		}
		if (c(30)) {
			i.setItem(a(26), new ItemStack(Material.POTION, 1, (short) 16418));
		}
		if (c(30)) {
			i.setItem(a(26), new ItemStack(Material.POTION, 1, (short) 16424));
		}
		if (c(30)) {
			i.setItem(a(26), new ItemStack(Material.POTION, 1, (short) 16426));
		}
		if (c(50)) {
			i.setItem(a(26), new ItemStack(Material.COOKED_BEEF, a(8)));
		}
	}
	
	public static void miniFeast(Chest chest) {
		Inventory i = chest.getInventory();

		if (c(36)) {
			i.setItem(a(26), new ItemStack(Material.DIAMOND, a(2)));
		}
		if (c(40)) {
			i.setItem(a(26), new ItemStack(Material.GOLD_INGOT, a(15)));
		}
		if (c(40)) {
			i.setItem(a(26), new ItemStack(Material.TNT, a(5)));
		}
		if (c(30)) {
			i.setItem(a(26), new ItemStack(Material.FLINT_AND_STEEL, 1));
		}
		if (c(40)) {
			i.setItem(a(26), new ItemStack(Material.IRON_INGOT, a(7)));
		}
		if (c(35)) {
			i.setItem(a(26), new ItemStack(Material.IRON_SWORD, 1));
		}
		if (c(35)) {
			i.setItem(a(26), new ItemStack(Material.POTION, 1, (short) 16424));
		}
		if (c(35)) {
			i.setItem(a(26), new ItemStack(Material.POTION, 1, (short) 16426));
		}
		if (c(35)) {
			i.setItem(a(26), new ItemStack(Material.POTION, 1, (short) 16388));
		}
		if (c(35)) {
			i.setItem(a(26), new ItemStack(Material.POTION, 1, (short) 16419));
		}
		if (c(35)) {
			i.setItem(a(26), new ItemStack(Material.EXP_BOTTLE, a(12)));
		}
	}

	private static int a(int max) {
		int random = new Random().nextInt(max);
		if (random == 0) {
			random = 1;
		}
		return random;
	}

	private static boolean c(int chance) {
		if (new Random().nextInt(100) <= chance) {
			return true;
		}
		return false;
	}
}
