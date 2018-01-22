package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class Endermage extends KitInterface {
	private ArrayList<Block> endermages;
	public static ArrayList<String> invencible;

	public Endermage(Main main) {
		super(main);
		this.endermages = new ArrayList<Block>();
		invencible = new ArrayList<String>();
	}

	HashMap<Player, Integer> pnum = new HashMap<Player, Integer>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		final Player p = event.getPlayer();
		Action a = event.getAction();
		ItemStack item = event.getItem();
		final Block b = event.getClickedBlock();
		if (!a.toString().contains("BLOCK")) {
			return;
		}
		if (item == null) {
			return;
		}
		if (item.getType() != Material.ENDER_PORTAL_FRAME) {
			return;
		}
		if (!hasAbility(p)) {
			return;
		}
		event.setCancelled(true);
		if ((b.getType() == Material.CACTUS) || (b.getType() == Material.TRAP_DOOR) || (b.getType() == Material.WALL_SIGN) || (b.getType() == Material.SIGN) || (b.getType() == Material.SIGN_POST)) {
			p.sendMessage(ChatColor.RED + "Voce nao pode colocar nesse bloco");
			return;
		}
		if (this.endermages.contains(b)) {
			return;
		}
		this.endermages.add(b);
		pnum.put(p, event.getPlayer().getInventory().getHeldItemSlot());
		item.setAmount(0);
		if (item.getAmount() == 0) {
			event.getPlayer().setItemInHand(null);
		}
		final Material material = b.getType();
		final byte dataValue = b.getData();
		final Location portal = b.getLocation().clone().add(0.5D, 0.5D, 0.5D);
		portal.getBlock().setType(Material.ENDER_PORTAL_FRAME);
		for (int i = 0; i <= 5; i++) {
			final int no = i;
			new BukkitRunnable() {
				public void run() {
					if (portal.getBlock().getType() != Material.ENDER_PORTAL_FRAME) {
						return;
					}
					if (no < 5) {
						for (final Player gamer : portal.getWorld().getPlayers()) {
							if (gamer != p) {
								if (Endermage.this.isEnderable(portal, gamer.getLocation())) {
									if ((!Endermage.this.hasAbility(gamer)) && (!Endermage.this.hasAbility(gamer, "antitower") && (!Main.getInstance().adm.isSpectating(gamer)))) {
										if (gamer.getLocation().distance(portal) > 3.0D) {
											gamer.teleport(portal.clone().add(0.0D, 0.5D, 0.0D));
											p.teleport(portal.clone().add(0.0D, 0.5D, 0.0D));
											gamer.sendMessage(ChatColor.LIGHT_PURPLE + "Voce foi puxado por " + p.getName() + "\n" + ChatColor.RED + "Voce esta invencivel por 5 segundos!\nLute ou Corra!");
											p.sendMessage(ChatColor.LIGHT_PURPLE + "Voce puxou alguem!\n" + ChatColor.RED + "Voces estao invencivel por 5 segundos!");
											Endermage.invencible.add(gamer.getName());
											Endermage.invencible.add(p.getName());
											ItemStack portalItem = new ItemStack(Material.ENDER_PORTAL_FRAME);
											ItemMeta meta = portalItem.getItemMeta();
											meta.setDisplayName(ChatColor.RED + "Endermage");
											portalItem.setItemMeta(meta);
											p.getInventory().setItem(pnum.get(p), portalItem);
											Endermage.this.endermages.remove(b);
											portal.getBlock().setTypeIdAndData(material.getId(), dataValue, true);
											Bukkit.getScheduler().scheduleSyncDelayedTask(Endermage.this.getMain(), new Runnable() {
												public void run() {
													Endermage.invencible.remove(gamer.getName());
													Endermage.invencible.remove(p.getName());
												}
											}, 100L);
										}
									}
								}
							}
						}
					} else {
						ItemStack portalItem = new ItemStack(Material.ENDER_PORTAL_FRAME);
						ItemMeta meta = portalItem.getItemMeta();
						meta.setDisplayName(ChatColor.RED + "Endermage");
						portalItem.setItemMeta(meta);
						portal.getBlock().setTypeIdAndData(material.getId(), dataValue, true);
						p.getInventory().setItem(pnum.get(p), portalItem);
						Endermage.this.endermages.remove(b);
					}
				}
			}.runTaskLater(getMain(), i * 20);
		}
	}

	@EventHandler
	public void onHabilidadeDeHitReceberDano(EntityDamageEvent event) {
		Entity vitima = event.getEntity();
		if (vitima.isDead()) {
			return;
		}
		if (!(vitima instanceof Player)) {
			return;
		}
		Player p = (Player) vitima;
		if (invencible.contains(p.getName())) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onHabilidadeDeHitReceberDano(EntityDamageByEntityEvent event) {
		Entity vitima = event.getDamager();
		if (event.getCause() != DamageCause.ENTITY_ATTACK) {
			return;
		}
		if (!(vitima instanceof Player)) {
			return;
		}
		if (!(vitima instanceof Player)) {
			return;
		}
		Player p = (Player) vitima;
		if (invencible.contains(p.getName())) {
			event.setCancelled(true);
			return;
		}
	}

	private boolean isEnderable(Location portal, Location player) {
		return (Math.abs(portal.getX() - player.getX()) < 2.0D) && (Math.abs(portal.getZ() - player.getZ()) < 2.0D);
	}

	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<ItemStack>();
		kitItems.add(createItem(Material.ENDER_PORTAL_FRAME, ""));
		return new Kit("endermage", Arrays.asList(new String[] { "Crie um portal que teleporta", "os players ao seu redor ate voce" }), kitItems, new ItemStack(Material.ENDER_PORTAL_FRAME));

	}
}
