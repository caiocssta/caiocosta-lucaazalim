package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Kangaroo extends KitInterface {
	public Kangaroo(Main main) {
		super(main);
	}

	HashMap<Player, Integer> pcount = new HashMap<Player, Integer>();

	HashMap<Player, Integer> Pulo = new HashMap<Player, Integer>();

	@EventHandler
	public void interact(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (hasAbility(p)) {
			if (p.getItemInHand().getType() == Material.FIREWORK) {
				event.setCancelled(true);

				if (!pcount.containsKey(p)) {
					if (!(Pulo.containsKey(p))) {
						if (!(p.isSneaking())) {
							if (!((CraftPlayer) p).getHandle().onGround) {
								Pulo.put(p, 1);
								p.setVelocity(new Vector(p.getVelocity().getX(), 1, p.getVelocity().getZ()));
							} else {
								p.setVelocity(new Vector(p.getVelocity().getX(), 1, p.getVelocity().getZ()));
							}
						} else {
							if (!((CraftPlayer) p).getHandle().onGround) {
								p.setVelocity(p.getLocation().getDirection().multiply(1.2));
								p.setVelocity(new Vector(p.getVelocity().getX(), 0.5, p.getVelocity().getZ()));
								Pulo.put(p, 1);
							} else {
								p.setVelocity(p.getLocation().getDirection().multiply(1.2));
								p.setVelocity(new Vector(p.getVelocity().getX(), 0.5, p.getVelocity().getZ()));
							}
						}
					}
				} else {
					if (!(Pulo.containsKey(p))) {
						if (!((CraftPlayer) p).getHandle().onGround) {
							p.setVelocity(new Vector(p.getVelocity().getX(), 0.5, p.getVelocity().getZ()));
							Pulo.put(p, 1);
						} else {
							p.setVelocity(new Vector(p.getVelocity().getX(), 0.5, p.getVelocity().getZ()));
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void landed(PlayerMoveEvent e) {
		if (!(e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR)) {
			if (Pulo.containsKey(e.getPlayer())) {
				Pulo.remove(e.getPlayer());
			}
		}
	}

	@EventHandler
	public void gotdamage(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			final Player p = (Player) e.getEntity();
			if (hasAbility(p)) {
				if (e.getCause() == DamageCause.ENTITY_ATTACK) {
					if (!(e.getDamager() instanceof Player)) {
						return;
					}
					if (this.getMain().adm.isSpectating((Player) e.getDamager())) {
						return;
					}
					if (pcount.containsKey(p)) {
						Bukkit.getScheduler().cancelTask(pcount.get(p));
					}
					pcount.put(p, Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
						public void run() {
							pcount.remove(p);
						}
					}, 10 * 20));
					return;
				}
			}
		}
	}

	@EventHandler
	public void entitydamage(EntityDamageEvent e) {
		if (e.getCause() == DamageCause.FALL) {
			if (e.getEntity() instanceof Player) {
				if (hasAbility((Player) e.getEntity())) {
					if (e.getDamage() > 7) {
						e.setDamage(7.0);
					} else {
						e.setDamage(e.getDamage());
					}
				}
			}
		}
	}

	// public ArrayList<String> Combate = new ArrayList<String>();

	@SuppressWarnings("deprecation")
	public boolean isOnGround(Player p) {
		Location l = p.getLocation();
		l = l.add(0, -1, 0);
		return l.getBlock().getState().getTypeId() != 0;
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();

		kititems.add(createItem(Material.FIREWORK, ChatColor.GREEN + "Kangaroo"));

		return new Kit("Kangaroo", Arrays.asList(new String[] { "Começe com um item que", "te permite dar um pulo duplo!" }), kititems, new ItemStack(Material.FIREWORK));
	}
}
