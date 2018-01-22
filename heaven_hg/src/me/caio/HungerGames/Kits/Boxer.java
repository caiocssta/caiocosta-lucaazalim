package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Boxer extends KitInterface {
	public Boxer(Main main) {
		super(main);
	}

	@EventHandler
	public void onHitBoxer(EntityDamageByEntityEvent e) {
		if ((e.getDamager() instanceof Player)) {
			Player p = (Player) e.getDamager();
			if ((hasAbility(p)) && (p.getItemInHand().getType() == Material.AIR)) {
				e.setDamage(e.getDamage() + 2.0D);
			}
		}
	}

	@EventHandler
	public void onDamageBoxer(EntityDamageByEntityEvent e) {
		if ((e.getEntity() instanceof Player)) {
			Player p = (Player) e.getEntity();
			if ((hasAbility(p)) && (e.getDamage() > 1.0D)) {
				e.setDamage(e.getDamage() - 1.0D);
			}
		}
	}

	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<ItemStack>();
		return new Kit("boxer", Arrays.asList(new String[] { "Leve menos dano" }), kitItems, new ItemStack(Material.STONE_SWORD));
	}
}
