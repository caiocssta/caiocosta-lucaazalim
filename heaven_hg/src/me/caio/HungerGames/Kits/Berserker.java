package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Berserker extends KitInterface {
	public Berserker(Main main) {
		super(main);
	}

	@EventHandler
	public void onPlayerKillEntity(EntityDeathEvent event) {
		if ((event.getEntity().getKiller() instanceof Player)) {
			Player p = event.getEntity().getKiller();
			if (hasAbility(p)) {
				if ((event.getEntity() instanceof Player)) {
					if (event.getEntity().isDead()) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 1));
						p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 1));
						p.playSound(p.getLocation(), Sound.WITHER_SPAWN, 2.0F, 2.0F);
					}
				} else if (event.getEntity().isDead()) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 1));
					p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 1));
				}
			}
		}
	}

	public Kit getKit() {
		return new Kit("berserker", Arrays.asList(new String[] { "sua fúria libera Força e Confusão" }), new ArrayList<ItemStack>(), new ItemStack(Material.WOOD_SWORD));
	}
}
