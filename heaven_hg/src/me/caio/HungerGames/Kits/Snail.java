package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Snail extends KitInterface {
	public Snail(Main main) {
		super(main);
	}

	@EventHandler
	public void onSnail(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		if (!(event.getDamager() instanceof Player)) {
			return;
		}
		Player p = (Player) event.getEntity();
		Player snail = (Player) event.getDamager();
		Location loc = p.getLocation();
		if (!(snail instanceof Player)) {
			return;
		}
		if (!hasAbility(snail)) {
			return;
		}
		Random r = new Random();
		if (((p instanceof Player)) && (r.nextInt(3) == 0)) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0));
			p.getLocation().getWorld().playEffect(loc.add(0.0D, 0.4D, 0.0D), Effect.STEP_SOUND, 159, 13);
		}
	}

	public Kit getKit() {
		return new Kit("snail", Arrays.asList(new String[] { "Dê hits em outros jogadores enquanto", "você tem uma chance em 3 de deixá-lo lento" }), new ArrayList<ItemStack>(), new ItemStack(Material.SOUL_SAND));
	}
}
