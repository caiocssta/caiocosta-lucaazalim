package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Grandpa extends KitInterface {
	public Grandpa(Main main) {
		super(main);
	}

	public static HashMap<String, Integer> Usou = new HashMap<String, Integer>();

	@EventHandler(ignoreCancelled = true)
	public void grandpa(EntityDamageByEntityEvent event) {
		if (((event.getEntity() instanceof LivingEntity)) && ((event.getDamager() instanceof Player))) {
			Player d = (Player) event.getDamager();
			if ((hasAbility(d)) && (d.getInventory().getItemInHand() != null) && (d.getInventory().getItemInHand().getType() == Material.STICK)) {
				Vector vector = ((LivingEntity) event.getEntity()).getLocation().toVector().subtract(d.getLocation().toVector()).normalize();
				double knockBack = 2.0D;
				try {
					// Particulas.WITCH_MAGIC.part(d,
					// ((LivingEntity)event.getEntity()).getLocation(), 0.2F,
					// 0.2F, 0.2F, 1.0F, 30);
				} catch (Exception e) {
					e.printStackTrace();
				}
				((LivingEntity) event.getEntity()).setVelocity(vector.multiply(knockBack));
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		kititems.add(createItem(Material.STICK, ChatColor.GREEN + "Grandpa"));
		return new Kit("Grandpa", Arrays.asList(new String[] { "Começe com um graveto que, ", "pode empurar um jogador para longe" }), kititems, new ItemStack(Material.STICK));
	}
}
