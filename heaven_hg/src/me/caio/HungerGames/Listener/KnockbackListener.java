package me.caio.HungerGames.Listener;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Utils.Enum.GameStage;

public class KnockbackListener implements Listener {

	HashMap<Player, Player> batalha = new HashMap<>();
	HashMap<Player, Boolean> sprint = new HashMap<>();
	HashMap<Player, Enchantment> enchant = new HashMap<>();

	@EventHandler
	public void dmg(EntityDamageByEntityEvent e) {
		if ((e.getEntity() instanceof Player)) {
			if ((e.getDamager() instanceof Player)) {
				if (Main.getInstance().stage == GameStage.GAMETIME) {
					Player p = (Player) e.getEntity();
					Player b = (Player) e.getDamager();
					batalha.remove(p);
					sprint.remove(b);
					enchant.remove(b);

					batalha.put(p, b);
					if (batalha.get(p).isSprinting()) {
						sprint.put(batalha.get(p), true);
					}
					if (b.getItemInHand().getEnchantments().containsKey(Enchantment.KNOCKBACK)) {
						enchant.put(b, Enchantment.KNOCKBACK);
					}

					if (batalha.get(p) != null) {
						Location loc = batalha.get(p).getLocation();
						Vector vector = loc.getDirection().normalize().multiply(0.65);
						vector.setY(0.3);

						if (sprint.get(batalha.get(p)) != null) {
							if (sprint.get(batalha.get(p))) {
								if (enchant.containsKey(batalha.get(p))) {
									Vector vector1 = loc.getDirection().normalize().multiply(0.90);
									vector1.setY(0.5);
									p.setVelocity(vector1);
									return;
								}
								Vector vector1 = loc.getDirection().normalize().multiply(0.83);
								vector1.setY(0.4);
								p.setVelocity(vector1);
								return;
							}
						}
						if (enchant.containsKey(batalha.get(p))) {
							Vector vector1 = loc.getDirection().normalize().multiply(0.82);
							vector1.setY(0.5);
							p.setVelocity(vector1);
							return;
						}
						p.setVelocity(vector);
					}
				}
			}
		}
	}

	@EventHandler
	public void asdasd(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			sprint.remove(p);
			enchant.remove(p);
			sprint.remove(batalha.get(p));
			enchant.remove(batalha.get(p));
			batalha.remove(p);
		}
	}

	/*
	@EventHandler
	public void kb(PlayerVelocityEvent e) {
		if (!Kangaroo.getCooldown().contains(e.getPlayer()) && !Grappler.getCooldown().contains(e.getPlayer()) && HGMain.getInstance().getKitKanager().hasAbility(e.getPlayer(), "Anchor")) {
			if (batalha.get(e.getPlayer()) != null) {
				Location loc = batalha.get(e.getPlayer()).getLocation();
				Vector vector = loc.getDirection().normalize().multiply(0.65);
				vector.setY(0.3);

				if (sprint.get(batalha.get(e.getPlayer())) != null) {
					if (sprint.get(batalha.get(e.getPlayer()))) {
						if (enchant.containsKey(batalha.get(e.getPlayer()))) {
							Vector vector1 = loc.getDirection().normalize().multiply(0.90);
							vector1.setY(0.5);
							e.getPlayer().setVelocity(vector1);
							return;
						}
						Vector vector1 = loc.getDirection().normalize().multiply(0.83);
						vector1.setY(0.4);
						e.getPlayer().setVelocity(vector1);
						return;
					}
				}
				if (enchant.containsKey(batalha.get(e.getPlayer()))) {
					Vector vector1 = loc.getDirection().normalize().multiply(0.82);
					vector1.setY(0.5);
					e.getPlayer().setVelocity(vector1);
					return;
				}
				e.getPlayer().setVelocity(vector);
			}
		}
	}*/

}

