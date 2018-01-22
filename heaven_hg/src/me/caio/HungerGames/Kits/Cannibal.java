package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;
import me.caio.HungerGames.Utils.Enum.GameStage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Cannibal extends KitInterface {
	public Cannibal(Main main) {
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
		Player d = (Player) event.getDamager();
		if (!(d instanceof Player)) {
			return;
		}
		if (!hasAbility(d)) {
			return;
		}
		if (Main.getInstance().stage != GameStage.GAMETIME) {
			return;
		}
		Random r = new Random();
		if (((p instanceof Player)) && (r.nextInt(3) == 0)) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 100, 0));
			d.setFoodLevel(d.getFoodLevel() + 1);
		}
	}

	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<ItemStack>();
		return new Kit("Cannibal", Arrays.asList(new String[] { "Recupera sua fome tirando dos oponentes" }), kitItems, new ItemStack(Material.RAW_FISH));
	}
}
