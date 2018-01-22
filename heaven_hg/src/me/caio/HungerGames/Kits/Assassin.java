package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Assassin extends KitInterface implements Listener {
	public Assassin(Main main) {
		super(main);
	}

	private HashMap<String, Integer> Dano = new HashMap<String, Integer>();

	@EventHandler
	public void aa(PlayerToggleSneakEvent e) {
		final Player p = e.getPlayer();
		if (hasAbility(p)) {
			new BukkitRunnable() {
				int tempo = 5;

				public void run() {
					this.tempo -= 1;
					if (this.tempo < 6) {
						if (p.isSneaking()) {
							if (!Assassin.this.Dano.containsKey(p.getName())) {
								Assassin.this.Dano.put(p.getName(), Integer.valueOf(1));
							} else if (((Integer) Assassin.this.Dano.get(p.getName())).intValue() <= 10) {
								Assassin.this.Dano.put(p.getName(), Integer.valueOf(((Integer) Assassin.this.Dano.get(p.getName())).intValue() + 1));
							} else {
								cancel();
							}
							if (((Integer) Assassin.this.Dano.get(p.getName())).intValue() == 2) {
								p.sendMessage(ChatColor.GREEN + "20% de carga!");
								Assassin.this.Dano.put(p.getName(), Integer.valueOf(((Integer) Assassin.this.Dano.get(p.getName())).intValue() + 1));
							}
							if (((Integer) Assassin.this.Dano.get(p.getName())).intValue() == 4) {
								p.sendMessage(ChatColor.GREEN + "40% de carga!");
								Assassin.this.Dano.put(p.getName(), Integer.valueOf(((Integer) Assassin.this.Dano.get(p.getName())).intValue() + 1));
							}
							if (((Integer) Assassin.this.Dano.get(p.getName())).intValue() == 6) {
								p.sendMessage(ChatColor.GREEN + "60% de carga!");
								Assassin.this.Dano.put(p.getName(), Integer.valueOf(((Integer) Assassin.this.Dano.get(p.getName())).intValue() + 1));
							}
							if (((Integer) Assassin.this.Dano.get(p.getName())).intValue() == 8) {
								p.sendMessage(ChatColor.GREEN + "80% de carga!");
								Assassin.this.Dano.put(p.getName(), Integer.valueOf(((Integer) Assassin.this.Dano.get(p.getName())).intValue() + 1));
							}
							if (((Integer) Assassin.this.Dano.get(p.getName())).intValue() == 10) {
								p.sendMessage(ChatColor.GREEN + "100% de carga!");
								Assassin.this.Dano.put(p.getName(), Integer.valueOf(((Integer) Assassin.this.Dano.get(p.getName())).intValue() + 1));
							}
						} else {
							Assassin.this.Dano.remove(p.getName());
						}
					}
				}
			}.runTaskTimer(Main.getInstance(), 0L, 20L);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void dano(EntityDamageByEntityEvent e) {
		if ((e.getDamager() instanceof Player)) {
			Player p = (Player) e.getDamager();
			if (this.Dano.containsKey(p.getName())) {
				if (((Integer) this.Dano.get(p.getName())).intValue() <= 2) {
					e.setDamage(e.getDamage() + 1.0D);
					this.Dano.remove(p.getName());
				} else if (((Integer) this.Dano.get(p.getName())).intValue() <= 4) {
					e.setDamage(e.getDamage() + 1.0D);
					this.Dano.remove(p.getName());
				} else if (((Integer) this.Dano.get(p.getName())).intValue() <= 6) {
					e.setDamage(e.getDamage() + 2.0D);
					this.Dano.remove(p.getName());
				} else if (((Integer) this.Dano.get(p.getName())).intValue() <= 8) {
					e.setDamage(e.getDamage() + 2.0D);
					this.Dano.remove(p.getName());
				} else if (((Integer) this.Dano.get(p.getName())).intValue() <= 11) {
					e.setDamage(e.getDamage() + 3.0D);
					this.Dano.remove(p.getName());
				}
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();

		return new Kit("assassin", Arrays.asList(new String[] { "Segure shift para carregar seu dano" }), kititems, new ItemStack(Material.IRON_SWORD));
	}
}
