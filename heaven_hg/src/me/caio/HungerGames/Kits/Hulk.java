package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;
import me.caio.HungerGames.Utils.Cooldown;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Hulk extends KitInterface {
	public Hulk(Main main) {
		super(main);
	}

	public static HashMap<String, Integer> Usou = new HashMap<String, Integer>();

	@EventHandler
	public void hulk(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		if ((e.getRightClicked() instanceof Player)) {
			Player clicked = (Player) e.getRightClicked();
			if ((!p.isInsideVehicle()) && (!clicked.isInsideVehicle()) && (p.getItemInHand().getType() == Material.AIR) && (hasAbility(p))) {
				if (Cooldown.isInCooldown(p.getUniqueId(), "Hulk")) {
					int timeleft = Cooldown.getTimeLeft(p.getUniqueId(), "Hulk");
					p.sendMessage("§6Hulk em cooldown de §f" + timeleft + "§6 segundos!");
					return;
				}
				if (!Cooldown.isInCooldown(p.getUniqueId(), "Hulk")) {
					Cooldown c = new Cooldown(p.getUniqueId(), "Hulk", 15);
					c.start();
				}
				p.setPassenger(clicked);
			}
		}
	}

	@EventHandler
	public void noHulkMor(EntityDamageByEntityEvent e) {
		if (((e.getEntity() instanceof Player)) && ((e.getDamager() instanceof Player))) {
			final Player p = (Player) e.getEntity();
			Player hulk = (Player) e.getDamager();
			if ((hulk.getPassenger() != null) && (hulk.getPassenger() == p) && (hasAbility(hulk)) && (hulk.getPassenger() == p)) { // &&
				/* (hulk.getItemInHand().getType() == Material.AIR)) { */
				e.setCancelled(true);
				p.setSneaking(true);
				Vector v = p.getEyeLocation().getDirection().multiply(1.5F);
				v.setY(0.6D);
				p.setVelocity(v);
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
					public void run() {
						p.setSneaking(false);
					}
				}, 10L);
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();

		return new Kit("Hulk", Arrays.asList(new String[] { "Agarre os jogadores", "e lance eles para longe" }), kititems, new ItemStack(Material.DISPENSER));
	}
}
