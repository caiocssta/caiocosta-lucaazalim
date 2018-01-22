package me.caio.HungerGames.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class RecipeUtil {
	public static final short DATA_WILDCARD = 32767;

	public static boolean areEqual(Recipe recipe1, Recipe recipe2) {
		if (recipe1 == recipe2) {
			return true;
		}
		if ((recipe1 == null) || (recipe2 == null)) {
			return false;
		}
		if (!recipe1.getResult().equals(recipe2.getResult())) {
			return false;
		}
		return match(recipe1, recipe2);
	}

	public static boolean areSimilar(Recipe recipe1, Recipe recipe2) {
		if (recipe1 == recipe2) {
			return true;
		}
		if ((recipe1 == null) || (recipe2 == null)) {
			return false;
		}
		return match(recipe1, recipe2);
	}

	private static boolean match(Recipe recipe1, Recipe recipe2) {
		if ((recipe1 instanceof ShapedRecipe)) {
			if (!(recipe2 instanceof ShapedRecipe)) {
				return false;
			}
			ShapedRecipe r1 = (ShapedRecipe) recipe1;
			ShapedRecipe r2 = (ShapedRecipe) recipe2;

			ItemStack[] matrix1 = shapeToMatrix(r1.getShape(), r1.getIngredientMap());
			ItemStack[] matrix2 = shapeToMatrix(r2.getShape(), r2.getIngredientMap());
			if (!Arrays.equals(matrix1, matrix2)) {
				mirrorMatrix(matrix1);

				return Arrays.equals(matrix1, matrix2);
			}
			return true;
		}
		if ((recipe1 instanceof ShapelessRecipe)) {
			if (!(recipe2 instanceof ShapelessRecipe)) {
				return false;
			}
			ShapelessRecipe r1 = (ShapelessRecipe) recipe1;
			ShapelessRecipe r2 = (ShapelessRecipe) recipe2;

			List<ItemStack> find = r1.getIngredientList();
			List<ItemStack> compare = r2.getIngredientList();
			if (find.size() != compare.size()) {
				return false;
			}
			for (ItemStack item : compare) {
				if (!find.remove(item)) {
					return false;
				}
			}
			return find.isEmpty();
		}
		if ((recipe1 instanceof FurnaceRecipe)) {
			if (!(recipe2 instanceof FurnaceRecipe)) {
				return false;
			}
			FurnaceRecipe r1 = (FurnaceRecipe) recipe1;
			FurnaceRecipe r2 = (FurnaceRecipe) recipe2;

			return r1.getInput().getType() == r2.getInput().getType();
		}
		throw new IllegalArgumentException("Unsupported recipe type: '" + recipe1 + "', update this class!");
	}

	private static ItemStack[] shapeToMatrix(String[] shape, Map<Character, ItemStack> map) {
		ItemStack[] matrix = new ItemStack[9];
		int slot = 0;
		for (int r = 0; r < shape.length; r++) {
			for (char col : shape[r].toCharArray()) {
				matrix[slot] = ((ItemStack) map.get(Character.valueOf(col)));
				slot++;
			}
			slot = (r + 1) * 3;
		}
		return matrix;
	}

	private static void mirrorMatrix(ItemStack[] matrix) {
		for (int r = 0; r < 3; r++) {
			ItemStack tmp = matrix[(r * 3)];
			matrix[(r * 3)] = matrix[(r * 3 + 2)];
			matrix[(r * 3 + 2)] = tmp;
		}
	}
}
