package me.caio.HungerGames.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.caio.HungerGames.Main;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class FinalBattle {
	private Main m;

	public FinalBattle(Main m) {
		this.m = m;
		spawnPit();
		teleportPlayers();
		removeEntities();
	}

	private void spawnPit() {
		Location spawn = this.m.getSpawn();
		spawn.setY(6.0D);
		int radius = 31;
		int bed = 40;
		while (spawn.getBlockY() <= spawn.getWorld().getMaxHeight()) {
			for (int x = -radius; x <= radius; x++) {
				for (int z = -radius; z <= radius; z++) {
					Location l = new Location(spawn.getWorld(), x, spawn.getBlockY(), z);
					if (spawn.distance(l) < radius) {
						l.getBlock().setType(Material.AIR);
					}
				}
			}
			for (int x = -bed; x <= bed; x++) {
				for (int z = -bed; z <= bed; z++) {
					if ((x == bed) || (x == -bed) || (z == bed) || (z == -bed)) {
						Location l = new Location(spawn.getWorld(), x, spawn.getBlockY() - 5.0D, z);
						l.getBlock().setType(Material.BEDROCK);
					}
				}
			}
			spawn = spawn.add(0.0D, 1.0D, 0.0D);
		}
	}

	private void teleportPlayers() {
		Random r = new Random();
		int radius = 25;
		List<Location> locations = new ArrayList<Location>();
		Location spawn = this.m.getSpawn();
		spawn.setY(11.0D);
		int z;
		Location l;
		for (int x = -radius; x <= radius; x++) {
			for (z = -radius; z <= radius; z++) {
				l = new Location(spawn.getWorld(), x, 11.0D, z);
				if ((spawn.distance(l) <= radius) && (spawn.distance(l) >= radius - 2)) {
					locations.add(l);
				}
			}
		}
		for (Player p : this.m.getServer().getOnlinePlayers()) {
			Location loc;
			if (locations.size() > 0) {
				int nexR = r.nextInt(locations.size() + 1);
				if (nexR < locations.size()) {
					loc = (Location) locations.get(nexR);
				} else {
					loc = (Location) locations.get(0);
				}
			} else {
				loc = new Location(spawn.getWorld(), 0.0D, 8.0D, 0.0D);
			}
			p.setFallDistance(-5.0F);
			p.teleport(loc);
		}
	}

	private void removeEntities() {
		for (Entity e : this.m.getSpawn().getWorld().getEntities()) {
			if (!(e instanceof Player)) {
				e.remove();
			}
		}
	}
}
