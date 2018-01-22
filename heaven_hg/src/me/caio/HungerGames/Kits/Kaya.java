package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

public class Kaya extends KitInterface {
	public Kaya(Main main) {
		super(main);
		ShapelessRecipe recipe = new ShapelessRecipe(new ItemStack(Material.GRASS));
		recipe.addIngredient(Material.DIRT);
		recipe.addIngredient(Material.SEEDS);
		Bukkit.addRecipe(recipe);
	}

	private transient HashMap<Block, Player> Blocos = new HashMap<Block, Player>();

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (!e.isCancelled())
			Blocos.remove(e.getBlock());
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void asdads(PrepareItemCraftEvent event) {
		if (event.getRecipe().getResult() != null && event.getRecipe().getResult().getType() == Material.GRASS) {
			for (HumanEntity entity : event.getViewers())
				if (hasAbility((Player) entity))
					return;
			event.getInventory().setItem(0, new ItemStack(0, 0));
		}
	}

	@EventHandler
	public void onExplode(EntityExplodeEvent e) {
		for (Block b : e.blockList())
			Blocos.remove(b);
	}

	@EventHandler
	public void onPiston(BlockPistonExtendEvent e) {
		for (Block b : e.getBlocks())
			Blocos.remove(b);
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		Block b = e.getBlock();
		if (!e.isCancelled())
			if (e.getBlock().getType() == Material.GRASS && hasAbility(p)) {
				Blocos.put(b, p);
			}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (!Main.getInstance().adm.isSpectating(p)) {
			Location loc = p.getLocation();
			for (int z = -1; z <= 1; z++) {
				for (int x = -1; x <= 1; x++) {
					for (int y = -1; y <= 2; y++) {
						Block b = loc.clone().add(x, y, z).getBlock();
						if (Blocos.containsKey(b) && b.getType() == Material.GRASS) {
							if (Blocos.get(b) != p) {
								b.setType(Material.AIR);
								Blocos.remove(b);
							}
						}
					}
				}
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		kititems.add(createItem(Material.GRASS, 16, "§4Kaya"));

		return new Kit("Kaya", Arrays.asList(new String[] { "Blocos de grama disaparecem", "quando alguém pisa neles" }), kititems, new ItemStack(Material.GRASS));
	}
}
