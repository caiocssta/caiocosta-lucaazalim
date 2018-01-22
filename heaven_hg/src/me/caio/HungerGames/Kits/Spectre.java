package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;
import me.caio.HungerGames.Utils.Cooldown;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Spectre extends KitInterface {
	public Spectre(Main main) {
		super(main);
	}

	public static ItemStack Item = createItem(Material.REDSTONE, ChatColor.RED + "Spectre");
	public static ItemStack Item2 = createItem(Material.SUGAR, ChatColor.AQUA + "Spectre");
	public HashMap<Player, ItemStack[]> armadura = new HashMap<Player, ItemStack[]>();

	@EventHandler
	public void InteractOpen(PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		if (((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK))) {
			if (hasAbility(p)) {
				if ((e.getPlayer().getItemInHand().getType().equals(Material.REDSTONE)) && (p.getItemInHand().hasItemMeta()) && (p.getItemInHand().getItemMeta().hasDisplayName())
						&& (p.getItemInHand().getItemMeta().getDisplayName().equals("§cSpectre"))) {
					e.setCancelled(true);
					p.updateInventory();
					if (Cooldown.isInCooldown(p.getUniqueId(), "Spectre")) {
						int timeleft = Cooldown.getTimeLeft(p.getUniqueId(), "Spectre");
						p.sendMessage("§6Spectre em cooldown de §f" + timeleft + "§6 segundos!");
						return;
					}
					if (!Cooldown.isInCooldown(p.getUniqueId(), "Spectre")) {
						Cooldown c = new Cooldown(p.getUniqueId(), "Spectre", 40);
						c.start();
					}
					for (Entity entity : p.getNearbyEntities(10, 10, 10)) {
						if (entity instanceof Player) {
							Player t = (Player) entity;
							t.playSound(t.getLocation(), Sound.NOTE_PIANO, 5, 1);
						}
					}
					p.setItemInHand(Item2);
					p.updateInventory();
					armadura.put(p, p.getInventory().getArmorContents());
					new BukkitRunnable() {
						int tempo = 20;

						public void run() {
							tempo -= 1;
							if (!p.hasPotionEffect(PotionEffectType.INVISIBILITY))
								p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10 * 20, 1));
							p.getInventory().setBoots(new ItemStack(Material.AIR));
							p.getInventory().setLeggings(new ItemStack(Material.AIR));
							p.getInventory().setChestplate(new ItemStack(Material.AIR));
							p.getInventory().setHelmet(new ItemStack(Material.AIR));
							p.updateInventory();
							if (tempo <= 0) {
								if (p.getInventory().contains(Item2)) {
									p.getInventory().removeItem(Item2);
									p.getInventory().addItem(Item);
									p.updateInventory();
									cancel();
								}
								p.getInventory().setArmorContents((ItemStack[]) armadura.get(p));
								if (p.hasPotionEffect(PotionEffectType.INVISIBILITY))
									p.removePotionEffect(PotionEffectType.INVISIBILITY);
							}
						}
					}.runTaskTimer(Main.getInstance(), 20, 20);

				}
			}
		}
	}

	@EventHandler
	public void Damage(EntityDamageByEntityEvent e) {
		if (((e.getEntity() instanceof Player)) && ((e.getDamager() instanceof Player))) {
			Player d = (Player) e.getDamager();
			if (hasAbility(d)) {
				if (d.hasPotionEffect(PotionEffectType.INVISIBILITY))
					d.removePotionEffect(PotionEffectType.INVISIBILITY);
				d.getInventory().setArmorContents((ItemStack[]) armadura.get(d));
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();

		kititems.add(Item);

		return new Kit("Spectre", Arrays.asList(new String[] { "Fique invisível e surpreenda seus inimigos!" }), kititems, new ItemStack(Material.REDSTONE));
	}
}
