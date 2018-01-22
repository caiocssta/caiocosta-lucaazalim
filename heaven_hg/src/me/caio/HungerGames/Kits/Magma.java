package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Events.TimeSecondEvent;
import me.caio.HungerGames.Managers.KitInterface;
import me.caio.HungerGames.Utils.Enum.GameStage;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class Magma extends KitInterface {
	public Magma(Main main) {
		super(main);
	}

	@EventHandler
	public void onMagma(EntityDamageEvent event) {
		if (getMain().stage != GameStage.GAMETIME) {
			return;
		}
		Entity entity = event.getEntity();
		if (!(entity instanceof Player)) {
			return;
		}
		Player p = (Player) entity;
		if (!hasAbility(p)) {
			return;
		}
		EntityDamageEvent.DamageCause fire = event.getCause();
		if ((fire == EntityDamageEvent.DamageCause.FIRE) || (fire == EntityDamageEvent.DamageCause.LAVA) || (fire == EntityDamageEvent.DamageCause.FIRE_TICK) || (fire == EntityDamageEvent.DamageCause.LIGHTNING)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDAmager(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		if (!(e.getDamager() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getEntity();
		Player d = (Player) e.getDamager();
		if (!hasAbility(p)) {
			return;
		}
		Random r = new Random();
		if (((d instanceof Player)) && (r.nextInt(5) == 0)) {
			d.setFireTicks(100);
		}
	}

	@EventHandler
	public void onPoseidon(TimeSecondEvent e) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (hasAbility(p)) {
				if (getMain().stage != GameStage.GAMETIME) {
					return;
				}
				Block b = p.getLocation().getBlock();
				if (b.getType() == Material.WATER || b.getType() == Material.STATIONARY_WATER) {
					p.damage(2.0);
				}
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		return new Kit("magma", Arrays.asList(new String[] { "Chances de por fogo em seu inimigo", "e liberar fogo por volta" }), items, new ItemStack(Material.FIRE));
	}
}
