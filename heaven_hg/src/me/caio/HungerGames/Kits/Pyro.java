package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;

import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Pyro extends KitInterface {
	public Pyro(Main main) {
		super(main);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		ItemStack item = e.getItem();
		Player p = e.getPlayer();
		if (e.getAction() == Action.RIGHT_CLICK_AIR && item != null && item.getType() == Material.FIREBALL && hasAbility(p)) {
			item.setAmount(item.getAmount() - 1);
			if (item.getAmount() == 0)
				e.getPlayer().setItemInHand(new ItemStack(0));
			Fireball ball = e.getPlayer().launchProjectile(Fireball.class);
			ball.setIsIncendiary(true);
			ball.setYield(ball.getYield() * 1.5F);
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		kititems.add(createItem(Material.FIREBALL, 5, "§4Pyro", ""));
		kititems.add(new ItemStack(Material.FLINT_AND_STEEL));

		return new Kit("Pyro", Arrays.asList(new String[] { "Lance bolas de fogo contra um inimigo!", }), kititems, new ItemStack(Material.FIREBALL));
	}
}
