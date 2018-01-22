package me.caio.HungerGames.Utils;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

public class Minifeast {

	private boolean showCords;
	private Location loc;

	public Minifeast(boolean showCords) {
		this(showCords, null);
	}

	public Minifeast(boolean showCords, Location loc) {
		this.showCords = showCords;
		if (loc == null) {
			Random r = new Random();
			loc = new Location(Bukkit.getWorlds().get(0), r.nextInt(800) - 400, 1, r.nextInt(800) - 400);
			loc.setY(Bukkit.getWorld("world").getHighestBlockYAt(loc));
		}
		this.loc = loc;
	}

	@SuppressWarnings("deprecation")
	public Minifeast spawnChests() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (showCords) {
				player.sendMessage(
						"§cUm minifeast foi visto entre (" 
								+ replaceNumber(loc.getBlockX() + 50) + ", "
								+ replaceNumber(loc.getBlockX() - 50) + ") e ("

								+ replaceNumber(loc.getBlockZ() + 50) + ", "
								+ replaceNumber(loc.getBlockZ() - 50) + ")");
				player.playSound(player.getLocation(), Sound.NOTE_PLING, 1F, 3F);
			}
		}
		Material[] materiais = new Material[] { Material.CHEST, null, Material.CHEST, null, Material.ENCHANTMENT_TABLE,
				null, Material.CHEST, null, Material.CHEST };

		Location l = loc.clone().add(1, 1, 1);
		for (int i = 0; i < materiais.length; i++) {
			if (materiais[i] != null) {
				l.getBlock().setType(materiais[i]);
				if (l.getBlock().getType().equals(Material.CHEST)) {
					FillChest.miniFeast((Chest) l.getBlock().getState());
				}
			}
			l.add(-1, 0, 0);
			if ((i + 1) % 3 == 0) {
				l.add(3, 0, -1);
			}
		}
		return this;
	}

	public static int replaceNumber(int n) {
		int r = 0;
		if (n > 0) {
			if (n >= 500) {
				r = 600;
			} else if (n >= 400) {
				r = 500;
			} else if (n >= 300) {
				r = 400;
			} else if (n >= 200) {
				r = 300;
			} else if (n >= 100) {
				r = 200;
			} else {
				r = 100;
			}
		} else if (n < 0) {
			n = -n;
			if (n >= 500) {
				r = 600;
			} else if (n >= 400) {
				r = 500;
			} else if (n >= 300) {
				r = 400;
			} else if (n >= 200) {
				r = 300;
			} else if (n >= 100) {
				r = 200;
			} else {
				r = 100;
			}
			r = -r;
		}
		return r;

	}

	public Minifeast spawnFloor() {
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				Location l = loc.clone().add(x, 0, z);
				if(l.distance(loc) <= 3.5) {
					removeAbove(l.getBlock());
					l.getBlock().setType(Material.GLASS);
				}
			}
		}
		return this;
	}

	private Minifeast removeAbove(Block block) {
		Location l = block.getLocation().clone();
		l.setY(l.getY() + 1);
		Block b = l.getBlock();
		while (l.getY() < loc.getY() + 10) {
			b.setType(Material.AIR);
			l.setY(l.getY() + 1);
			b = l.getBlock();
		}
		return this;
	}
}
