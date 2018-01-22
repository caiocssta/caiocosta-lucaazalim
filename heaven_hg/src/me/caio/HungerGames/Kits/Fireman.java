package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;
import me.caio.HungerGames.Utils.Enum.GameStage;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class Fireman extends KitInterface {
	public Fireman(Main main) {
		super(main);
	}

	@EventHandler
	public void onFireman(EntityDamageEvent event) {
		if (getMain().stage != GameStage.GAMETIME) {
			return;
		}
		Entity entity = event.getEntity();
		if (!(entity instanceof Player)) {
			return;
		}
		Player fireman = (Player) entity;
		if (!hasAbility(fireman)) {
			return;
		}
		EntityDamageEvent.DamageCause fire = event.getCause();
		if ((fire == EntityDamageEvent.DamageCause.FIRE) || (fire == EntityDamageEvent.DamageCause.LAVA) || (fire == EntityDamageEvent.DamageCause.FIRE_TICK) || (fire == EntityDamageEvent.DamageCause.LIGHTNING)) {
			event.setCancelled(true);
		}
	}

	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<ItemStack>();
		kitItems.add(new ItemStack(Material.WATER_BUCKET));
		return new Kit("fireman", Arrays.asList(new String[] { "Imune à dano de fogo e raio,", "começa com um balde de água" }), kitItems, new ItemStack(Material.WATER_BUCKET));
	}
}
