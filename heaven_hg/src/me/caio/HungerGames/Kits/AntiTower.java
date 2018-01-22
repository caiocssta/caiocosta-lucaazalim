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

public class AntiTower extends KitInterface {

	public AntiTower(Main main) {
		super(main);
	}

	@EventHandler
	public void onStomper(EntityDamageEvent event) {
		if (getMain().stage != GameStage.GAMETIME) {
			return;
		}
		if (event.isCancelled()) {
			return;
		}
		Entity stomper = event.getEntity();
		if (!(stomper instanceof Player)) {
			return;
		}
		Player stomped = (Player) stomper;
		if (!hasAbility(stomped)) {
			return;
		}
		EntityDamageEvent.DamageCause cause = event.getCause();
		if (cause != EntityDamageEvent.DamageCause.FALL) {
			return;
		}
		double dmg = event.getDamage();
		if (dmg > 4.0D) {
			event.setCancelled(true);
			stomped.damage(4.0D);
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		return new Kit("antitower", Arrays.asList(new String[] { "O kit Anti Tower possui a habilidade de não ser puxado por um Endermage e também não ser estompado por um Stomper." }), items,
				new ItemStack(Material.GOLD_BOOTS, 1));
	}
}
