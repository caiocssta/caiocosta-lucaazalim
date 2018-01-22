package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Surprise extends KitInterface {
	public Surprise(Main main) {
		super(main);
	}

	public Kit getKit() {
		return new Kit("surprise", Arrays.asList(new String[] { "Pegue um kit aleatorio" }), new ArrayList<ItemStack>(), new ItemStack(Material.NAME_TAG));
	}
}
