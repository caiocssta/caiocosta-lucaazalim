package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;
import me.caio.HungerGames.Utils.Cooldown;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Phantom extends KitInterface {
	public Phantom(Main main) {
		super(main);
	}

	public static HashMap<String, ItemStack[]> armadura = new HashMap<String, ItemStack[]>();

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (armadura.containsKey(e.getWhoClicked().getName())) {
			if (!(e.getWhoClicked() instanceof Player)) {
				return;
			}
			if (hasAbility((Player) e.getWhoClicked())) {

			}
			if (e.getCurrentItem().getType().name().contains("LEATHER_") && ((LeatherArmorMeta) e.getCurrentItem().getItemMeta()).getColor().equals(Color.fromRGB(40, 240, 33)))
				e.setCancelled(true);
		}
	}

	public void Voar(final Player p) {
		new BukkitRunnable() {
			int tempo = 6;

			public void run() {
				tempo--;
				if (tempo == 5) {
					armadura.put(p.getName(), p.getInventory().getArmorContents());
					ItemStack lhelmet = new ItemStack(Material.LEATHER_HELMET);
					ItemStack lchestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
					ItemStack lleggings = new ItemStack(Material.LEATHER_LEGGINGS);
					ItemStack lboots = new ItemStack(Material.LEATHER_BOOTS);
					for (int i = 0; i <= 50; i++)
						p.getLocation().getWorld().playEffect(p.getLocation(), Effect.SMOKE, i);
					LeatherArmorMeta lamh = (LeatherArmorMeta) lhelmet.getItemMeta();
					lamh.setColor(Color.fromRGB(40, 240, 33));
					lhelmet.setItemMeta(lamh);
					p.getInventory().setHelmet(lhelmet);

					LeatherArmorMeta lamc = (LeatherArmorMeta) lchestplate.getItemMeta();
					lamc.setColor(Color.fromRGB(40, 240, 33));
					lchestplate.setItemMeta(lamc);
					p.getInventory().setChestplate(lchestplate);

					LeatherArmorMeta laml = (LeatherArmorMeta) lleggings.getItemMeta();
					laml.setColor(Color.fromRGB(40, 240, 33));
					lleggings.setItemMeta(laml);
					p.getInventory().setLeggings(lleggings);

					LeatherArmorMeta lamb = (LeatherArmorMeta) lboots.getItemMeta();
					lamb.setColor(Color.fromRGB(40, 240, 33));
					lboots.setItemMeta(lamb);
					p.getInventory().setBoots(lboots);

					p.setAllowFlight(true);
					p.playSound(p.getLocation(), Sound.WITHER_DEATH, 10.0F, 1.0F);
				}
				if (tempo == 3) {
					p.sendMessage(ChatColor.RED + "3...");
					p.playSound(p.getLocation(), Sound.NOTE_PIANO, 10.0F, 1.0F);
				}
				if (tempo == 2) {
					p.sendMessage(ChatColor.RED + "2...");
					p.playSound(p.getLocation(), Sound.NOTE_PIANO, 10.0F, 1.0F);
				}
				if (tempo == 1) {
					p.sendMessage(ChatColor.RED + "1...");
					p.playSound(p.getLocation(), Sound.NOTE_PIANO, 10.0F, 1.0F);
				}
				if (tempo <= 0) {
					p.playSound(p.getLocation(), Sound.WITHER_SPAWN, 10.0F, 1.0F);
					p.setAllowFlight(false);
					p.setFlying(false);
					p.getInventory().setArmorContents((ItemStack[]) armadura.get(p.getName()));
					cancel();
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (p.getItemInHand().getType() == Material.FEATHER && hasAbility(p)) {
			if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				if (Cooldown.isInCooldown(p.getUniqueId(), "Phantom")) {
					int timeleft = Cooldown.getTimeLeft(p.getUniqueId(), "Phantom");
					p.sendMessage("§6Phantom em cooldown de §f" + timeleft + "§6 segundos!");
					return;
				}
				if (Cooldown.isInCooldown(p.getUniqueId(), "phantompvp")) {
					int timeleft = Cooldown.getTimeLeft(p.getUniqueId(), "phantompvp");
					p.sendMessage("§6Você esta em pvp, aguarde §f" + timeleft + "§6 segundos!");
					p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2 * 20, 5));
					return;
				}
				if (!Cooldown.isInCooldown(p.getUniqueId(), "Phantom")) {
					Cooldown c = new Cooldown(p.getUniqueId(), "Phantom", 90);
					c.start();
				}
				for (Entity entity : p.getNearbyEntities(10, 10, 10)) {
					if (entity instanceof Player) {
						Player tar = (Player) entity;
						tar.sendMessage(ChatColor.BOLD + "Um Phantom por perto...");
						tar.sendMessage(ChatColor.BOLD + "OBS: Isso nao é um hacker e sim um kit.");
					}
				}
				Voar(p);
			}
		}
	}

	@EventHandler
	public void death(ItemSpawnEvent e) {
		if (e.getEntity().getItemStack().getType().name().contains("LEATHER_") && ((LeatherArmorMeta) e.getEntity().getItemStack().getItemMeta()).getColor().equals(Color.fromRGB(40, 240, 33))) {
			e.setCancelled(true);
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		kititems.add(createItem(Material.FEATHER, "Phantom"));
		return new Kit("Phantom", Arrays.asList(new String[] { "Use sua pena para ganhar", "uma habilidade de vôo temporário." }), kititems, new ItemStack(Material.FEATHER));
	}

	@EventHandler
	public void damage(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		if (hasAbility((Player) e.getEntity())) {
			if (e.getCause() == DamageCause.ENTITY_ATTACK) {
				if (this.getMain().adm.isSpectating((Player) e.getDamager())) {
					return;
				}
				if (!Cooldown.isInCooldown(e.getEntity().getUniqueId(), "phantompvp")) {
					Cooldown c = new Cooldown(e.getEntity().getUniqueId(), "phantompvp", 10);
					c.start();
				}
			}
		}
	}
}
