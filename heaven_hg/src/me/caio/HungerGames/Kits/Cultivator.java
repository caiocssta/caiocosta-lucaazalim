package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;

import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class Cultivator extends KitInterface {
	public Cultivator(Main main) {
		super(main);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onCultiavator(BlockPlaceEvent event) {
		Player p = event.getPlayer();
		if (!hasAbility(p)) {
			return;
		}
		if (event.getBlock().getType() == Material.SAPLING) {
			event.getBlock().setType(Material.AIR);

			boolean arvore = event.getBlock().getWorld().generateTree(event.getBlock().getLocation(), TreeType.TREE);
			if (!arvore) {
				event.getBlock().setTypeIdAndData(Material.SAPLING.getId(), event.getBlock().getData(), false);
			}
		} else if (event.getBlock().getType() == Material.CROPS) {
			event.getBlock().setData((byte) 7);
		}
	}

	public Kit getKit() {
		return new Kit("Cultivator", Arrays.asList(new String[] { "Trigo e àrvores crescem instantâneamente", "Suas plantacoes crescem instantaneamente" }), new ArrayList<ItemStack>(), new ItemStack(Material.SAPLING));
	}
}
