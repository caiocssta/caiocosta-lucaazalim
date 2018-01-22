package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;
import me.caio.HungerGames.Utils.Cooldown;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Timelord extends KitInterface {
	public Timelord(Main main) {
		super(main);
	}

	private static List<String> travar = new ArrayList<String>();

	public static void freeze(Player p) {
		if (!travar.contains(p.getName())) {
			travar.add(p.getName());
			p.getWorld().playSound(p.getLocation(), Sound.ENDERDRAGON_WINGS, 10.0F, 10.0F);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000, 4));
			p.sendMessage("§cVoce foi congelado por um §bTimelord");
			p.sendMessage("§cEspere 10 segundos");
		}
	}

	public static void unfreeze(Player p) {
		if (travar.contains(p.getName())) {
			travar.remove(p.getName());
			p.removePotionEffect(PotionEffectType.SPEED);
		}
	}

	public static void Efeito(Player p, Location loc, boolean b) {
		for (int i = 0; i <= 50; i += ((!b && (i == 50)) ? 2 : 1))
			loc.getWorld().playEffect(loc, Effect.SMOKE, i);
	}

	public static void Efeito1(final Player p, final Location loc, boolean b) {
		int i = 0;
		for (i = 0; i <= 50; i += ((!b && (i == 50)) ? 2 : 1)) {
			try {
				// Particulas.ANGRY_VILLAGER.part(p, loc, i, i, i, 0, 10);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		if (travar.contains(p.getName())) {
			if (((event.getTo().getX() != event.getFrom().getX()) || (event.getTo().getZ() != event.getFrom().getZ()))) {
				event.setTo(event.getFrom());
				return;
			}
		}
	}

	@EventHandler
	public void InteractOpen(PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		if (((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK))) {
			if (hasAbility(p)) {
				if ((e.getPlayer().getItemInHand().getType().equals(Material.WATCH)) && (p.getItemInHand().hasItemMeta()) && (p.getItemInHand().getItemMeta().hasDisplayName())
						&& (p.getItemInHand().getItemMeta().getDisplayName().equals("§6Timelord"))) {
					if (isInvencibility()) {
						p.sendMessage(ChatColor.RED + "Você não pode usar na invencibilidade.");
						return;
					}
					if (Cooldown.isInCooldown(p.getUniqueId(), "Timelord")) {
						int timeleft = Cooldown.getTimeLeft(p.getUniqueId(), "Timelord");
						p.sendMessage("§6Timelord em cooldown de §f" + timeleft + "§6 segundos!");
						return;
					}
					if (!Cooldown.isInCooldown(p.getUniqueId(), "Timelord")) {
						Cooldown c = new Cooldown(p.getUniqueId(), "Timelord", 30);
						c.start();
					}
					p.getWorld().playSound(p.getLocation(), Sound.AMBIENCE_CAVE, 1.0F, 1.0F);
					Efeito1(p, p.getLocation(), false);
					for (Entity ent : p.getNearbyEntities(6.0D, 6.0D, 6.0D))
						if ((ent instanceof Player)) {
							final Player t = (Player) ent;
							if (!Main.getInstance().adm.isSpectating(t))
								freeze(t);
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
								public void run() {
									unfreeze(t);
								}
							}, 20 * 10);
						}
				}
			}
		}
	}

	@EventHandler
	public void CombateHit(EntityDamageByEntityEvent e) {
		if ((e.getDamager() instanceof Player) && ((e.getEntity() instanceof Player))) {
			final Player p = (Player) e.getEntity();
			// final Player d = (Player) e.getDamager();
			unfreeze(p);
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();

		kititems.add(createItem(Material.WATCH, "§6Timelord"));

		return new Kit("Timelord", Arrays.asList(new String[] { "Clique com o lado direito em", "seu relógio e observe o tempo congelar ao seu redor" }), kititems, new ItemStack(Material.WATCH));
	}
}
