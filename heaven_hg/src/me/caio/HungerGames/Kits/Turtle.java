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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

public class Turtle extends KitInterface {
	public Turtle(Main main) {
		super(main);
	}

	public static ArrayList<String> Dano = new ArrayList<String>();

	@EventHandler
	public void viper(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Player d = (Player) e.getDamager();
			if (hasAbility(d)) {
				if (Dano.contains(d.getName())) {
					e.setCancelled(true);
					return;
				}
			}
		}
	}

	@EventHandler
	public void De4(PlayerToggleSneakEvent event) {
		Player p = event.getPlayer();
		if (hasAbility(p)) {
			if ((!p.isSneaking())) {
				if (!Dano.contains(p.getName())) {
					Dano.add(p.getName());
				}
			} else {
				if (Dano.contains(p.getName())) {
					Dano.remove(p.getName());
				}
			}
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getEntity();
		if (hasAbility(p) && (p.isSneaking())
				&& ((e.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || (e.getCause() == EntityDamageEvent.DamageCause.CONTACT) || (e.getCause() == EntityDamageEvent.DamageCause.CUSTOM)
						|| (e.getCause() == EntityDamageEvent.DamageCause.DROWNING) || (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) || (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)
						|| (e.getCause() == EntityDamageEvent.DamageCause.FALL) || (e.getCause() == EntityDamageEvent.DamageCause.FALLING_BLOCK) || (e.getCause() == EntityDamageEvent.DamageCause.FIRE)
						|| (e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) || (e.getCause() == EntityDamageEvent.DamageCause.LAVA) || (e.getCause() == EntityDamageEvent.DamageCause.LIGHTNING)
						|| (e.getCause() == EntityDamageEvent.DamageCause.MAGIC) || (e.getCause() == EntityDamageEvent.DamageCause.MELTING) || (e.getCause() == EntityDamageEvent.DamageCause.POISON)
						|| (e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) || (e.getCause() == EntityDamageEvent.DamageCause.STARVATION) || (e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION)
						|| (e.getCause() == EntityDamageEvent.DamageCause.THORNS) || (e.getCause() == EntityDamageEvent.DamageCause.VOID) || (e.getCause() == EntityDamageEvent.DamageCause.WITHER))) {
			e.setDamage(1);
			return;
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();

		return new Kit("Turtle", Arrays.asList(new String[] { "Agache para receber dano mínimo", "mas não poderá atacar enquanto agachado!" }), kititems, new ItemStack(Material.DIAMOND_CHESTPLATE));
	}
}
