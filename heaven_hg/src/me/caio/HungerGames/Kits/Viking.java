package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Viking extends KitInterface {
	public Viking(Main main) {
		super(main);
	}

	@EventHandler
	public void onDamageViking(EntityDamageByEntityEvent e) {
		if ((e.getDamager() instanceof Player)) {
			Player d = (Player) e.getDamager();
			ItemStack item = d.getItemInHand();
			if (item.getType().name().contains("_AXE")) {
				e.setDamage(e.getDamage() + 1.0D);
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		return new Kit("viking", Arrays.asList(new String[] { "De dano extra com machados" }), kititems, new ItemStack(Material.STONE_AXE));
	}
}
