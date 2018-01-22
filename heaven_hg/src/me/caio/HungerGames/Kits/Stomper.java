package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;

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

public class Stomper extends KitInterface {
	public Stomper(Main main) {
		super(main);
	}

	@EventHandler
	public void Stompar(EntityDamageEvent e) {
		Entity ent = e.getEntity();
		if ((ent instanceof Player)) {
			final Player p = (Player) ent;
			if ((e.getCause() == EntityDamageEvent.DamageCause.FALL) && hasAbility(p)) {

				if (Launcher.nofalldamage.contains(p.getName())) {
					e.setCancelled(true);
					Launcher.nofalldamage.remove(p.getName());
					return;
				}
				if (Main.getInstance().stage != GameStage.GAMETIME) {
					return;
				}
				e.setCancelled(true);
				p.damage(4);
				if (Endermage.invencible.contains(p.getName())) {
					return;
				}
				for (Entity nearby : p.getNearbyEntities(4.0D, 5.0D, 4.0D)) {
					if ((nearby instanceof Player)) {
						Player t = (Player) nearby;
						if (!Main.getInstance().adm.isSpectating(t)) {
							if (hasAbility(t, "antitower")) {
								return;
							}
							if (t.isSneaking()) {
								if (e.getDamage() <= 10.0D) {
									t.damage(e.getDamage() / 2.0D, p);
								} else {
									t.damage(10.0D, p);
								}
							} else {
								t.damage(e.getDamage() + 3, p);

							}
						}
					}
				}
			}
		}
	}

	public int getDanoMore(ItemStack[] contents) {
		int dano = 0;
		if (contents != null) {
			for (ItemStack item : contents) {
				if (item != null) {
					dano = dano + getDanoMaterial(item.getType());
				}
			}
		}
		return dano;
	}

	public int getDanoMaterial(Material m) {
		if (m == Material.DIAMOND_CHESTPLATE) {
			return 18;
		} else if (m == Material.DIAMOND_HELMET) {
			return 12;
		} else if (m == Material.DIAMOND_LEGGINGS) {
			return 15;
		} else if (m == Material.DIAMOND_BOOTS) {
			return 10;
		}
		return 0;
	}

	public Kit getKit() {
		return new Kit("stomper", Arrays.asList(new String[] { "Esmague seus inimigos" }), new ArrayList<ItemStack>(), new ItemStack(Material.IRON_BOOTS));
	}
}
