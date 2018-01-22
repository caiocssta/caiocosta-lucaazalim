package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Events.TimeSecondEvent;
import me.caio.HungerGames.Managers.KitInterface;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Poseidon extends KitInterface {
	public Poseidon(Main main) {
		super(main);
	}

	@EventHandler
	public void onPoseidon(TimeSecondEvent e) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (hasAbility(p)) {
				Block b = p.getLocation().getBlock();
				if (b.getType() == Material.WATER) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 80, 0));
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 1));
					p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 80, 0));
				} else if (b.getType() == Material.STATIONARY_WATER) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 80, 0));
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 1));
					p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 80, 0));
				}
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		return new Kit("poseidon", Arrays.asList(new String[] { "Tenha Força I e Velocidade II enquanto na água,", "não pode se afogar enquanto estiver se movendo" }), kititems, new ItemStack(Material.WATER));
	}
}
