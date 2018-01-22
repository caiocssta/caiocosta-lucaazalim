package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Specialist extends KitInterface {
	public Specialist(Main main) {
		super(main);
	}

	@EventHandler
	private void onPlayerEdnaldo(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		Action a = event.getAction();
		ItemStack item = event.getItem();
		if (((a == Action.RIGHT_CLICK_BLOCK) || (a == Action.RIGHT_CLICK_AIR)) && (hasAbility(p)) && (item != null) && (item.getType() == Material.BOOK)) {
			event.setCancelled(true);
			p.openEnchanting(null, true);
		}
	}

	@EventHandler
	public void matar(PlayerDeathEvent event) {
		Player p = event.getEntity().getKiller();
		if (p == null) {
			return;
		}
		if (hasAbility(p)) {
			if (p.getInventory().firstEmpty() != -1) {
				p.getInventory().addItem(new ItemStack[] { new ItemStack(Material.EXP_BOTTLE) });
			} else {
				p.getLocation().getWorld().dropItem(p.getLocation(), new ItemStack(Material.EXP_BOTTLE));
			}
		}
	}

	@EventHandler
	public void expbotle(ExpBottleEvent event) {
		event.setExperience(event.getExperience() * 2 + 5);
	}

	public Kit getKit() {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		items.add(createItem(Material.BOOK, "Mesa portatil"));
		return new Kit("specialist", Arrays.asList(new String[] { "Ganhe experiencia toda vez que matar um jogador" }), items, new ItemStack(Material.BOOK));
	}
}
