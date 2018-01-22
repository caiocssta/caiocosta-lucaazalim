package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Viper extends KitInterface {
	public Viper(Main main) {
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
		Player viper = (Player) event.getDamager();
		if (!hasAbility(viper)) {
			return;
		}
		Random r = new Random();
		if (((p instanceof Player)) && (r.nextInt(3) == 0)) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 0));
		}
	}

	public Kit getKit() {
		return new Kit("viper", Arrays.asList(new String[] { "Todo hit dado à um jogador", "tem uma chance de envenená-lo." }), new ArrayList<ItemStack>(), new ItemStack(Material.SPIDER_EYE));
	}
}
