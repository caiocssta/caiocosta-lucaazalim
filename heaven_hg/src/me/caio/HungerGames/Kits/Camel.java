package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;
import me.caio.HungerGames.Utils.Enum.GameStage;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Camel extends KitInterface {
	public Camel(Main main) {
		super(main);
		ItemStack item = new ItemStack(Material.MUSHROOM_SOUP);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Sopa do deserto");
		item.setItemMeta(meta);
		camel = new ShapelessRecipe(item);
		camel.addIngredient(Material.SAND);
		camel.addIngredient(1, Material.CACTUS);
		Bukkit.addRecipe(camel);
	}

	public static ShapelessRecipe camel;

	public static boolean areEqual(Recipe recipe1, Recipe recipe2) {
		if (recipe1 == recipe2) {
			return true;
		}

		if (recipe1 == null || recipe2 == null) {
			return false;
		}

		if (!recipe1.getResult().equals(recipe2.getResult())) {
			return false;
		}

		return match(recipe1, recipe2);
	}

	@SuppressWarnings("deprecation")
	private static boolean match(Recipe recipe1, Recipe recipe2) {
		if (recipe1 instanceof ShapedRecipe) {
			if (recipe2 instanceof ShapedRecipe == false) {
				return false;
			}

			return true;
		} else if (recipe1 instanceof ShapelessRecipe) {
			if (recipe2 instanceof ShapelessRecipe == false) {
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
		} else if (recipe1 instanceof FurnaceRecipe) {
			if (recipe2 instanceof FurnaceRecipe == false) {
				return false;
			}

			FurnaceRecipe r1 = (FurnaceRecipe) recipe1;
			FurnaceRecipe r2 = (FurnaceRecipe) recipe2;

			return r1.getInput().getTypeId() == r2.getInput().getTypeId();
		} else {
			throw new IllegalArgumentException("Unsupported recipe type: '" + recipe1 + "', update this class!");
		}
	}

	@EventHandler
	public void preCraft(PrepareItemCraftEvent event) {
		boolean equal = areEqual(event.getRecipe(), camel);
		if (((event.getView().getPlayer() instanceof Player)) && (equal) && (!hasAbility(((Player) event.getView().getPlayer())))) {
			event.getInventory().setResult(null);
		}
	}

	@EventHandler
	public void Mover(PlayerMoveEvent e) {
		if (Main.getInstance().stage == GameStage.PREGAME) {
			return;
		}
		Player p = e.getPlayer();
		if (hasAbility(p)) {
			Location loc = new Location(p.getWorld(), e.getTo().getX(), e.getTo().getY() - 1.0D, e.getTo().getZ());

			if (loc.getBlock().getType() == Material.SAND) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 0));
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();

		return new Kit("Camel", Arrays.asList(new String[] { "Crafite sopas com areia e cactos", "Ande mais rapido no deserto" }), kititems, new ItemStack(Material.SAND));
	}
}
