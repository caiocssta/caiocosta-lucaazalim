package me.caio.HungerGames.Coliseu;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Type;
import me.caio.HungerGames.Constructors.BO3Blocks;

public class ColiseuManager {
	private List<BlockState> resetBlocks;
	private List<Block> coliseumBlocks;
	private List<BO3Blocks> coliseum;
	private List<Block> doors;
	private Location spawn;
	private int radius;
	private boolean doorsOpen;
	private boolean constructed;
	private Main main;

	public ColiseuManager(Main main) {
		this.main = main;
		this.doorsOpen = true;
		this.constructed = false;
		this.resetBlocks = new ArrayList<BlockState>();
		this.coliseumBlocks = new ArrayList<Block>();
		if (main.type == Type.TEAM) {
			this.coliseum = main.loadBO3("coliseum");
		} else {
			this.coliseum = main.loadBO3("coliseum");
		}
		this.doors = new ArrayList<Block>();
		this.radius = 38;
		this.spawn = new Location(main.getSpawn().getWorld(), 0.0D, main.getSpawn().getWorld().getHighestBlockYAt(0, 0), 0.0D);
	}
/*
	@SuppressWarnings("deprecation")
	public void spawnColiseum() {
		int z;
		for (int x = -40; x <= 40; x++) {
			for (z = -40; z <= 40; z++) {
				for (int y = -5; y <= 150; y++) {
					Block b = new Location(this.spawn.getWorld(), x, this.spawn.getY() + y, z).getBlock();
					this.resetBlocks.add(b.getState());
				}
			}
		}
		int y = 0;

		for (BO3Blocks bo3 : this.coliseum) {
			int hy = this.spawn.getWorld().getHighestBlockYAt(bo3.getX(), bo3.getZ());
			if (hy > y) {
				y = hy + 1;
			}
		}
		for (BO3Blocks bo3 : this.coliseum) {
			Block b = new Location(this.spawn.getWorld(), bo3.getX(), y + bo3.getY(), bo3.getZ()).getBlock();
			this.coliseumBlocks.add(b);
			if (bo3.getType() == Material.getMaterial(33)) {
				doors.add(b);
			}
			Block block = b;
			int i = 45;
			do {
				if (block.getType() != Material.AIR) {
					block.setType(Material.AIR);
				}
				block = block.getRelative(BlockFace.UP);
				i--;
			} while (i >= 0);
			b.setType(bo3.getType());
			b.setData(bo3.getData());
		}
		this.constructed = true;
		this.spawn = new Location(main.getSpawn().getWorld(), 0.0D, main.getSpawn().getWorld().getHighestBlockYAt(0, 0), 0.0D);

	}
*/
	@SuppressWarnings("deprecation")
	public void teleportRecursive(int time) {
		List<Player> players = new ArrayList<Player>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!isInsideColiseum(p)) {
				players.add(p);
			}
		}
		if (players.size() <= 0) {
			return;
		}
		Random r = new Random();
		int pla = players.size() / time + 1;
		for (int i = 0; i < pla; i++) {
			if (players.size() > 0) {
				Player p = (Player) players.get(r.nextInt(players.size()));
				Main.getInstance().sendToSpawn(p);
				if (!this.constructed) {
				} else {
					p.sendMessage(ChatColor.RED + "Você não pode passar daqui!");
				}
				players.remove(p);
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void teleportOutsidePlayers() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!isInsideColiseum(p)) {
				Main.getInstance().sendToSpawn(p);
				p.sendMessage(ChatColor.RED + "Você não pode passar daqui!");
			}
		}
	}

	public boolean isDoorsOpen() {
		return this.doorsOpen;
	}

	public boolean isConstructed() {
		return this.constructed;
	}

/*	public void destroyColiseum() {
		for (BlockState state : this.resetBlocks) {
			state.update(true);
		}
		this.resetBlocks.clear();
		this.constructed = false;
	}

	@SuppressWarnings("deprecation")
	public void closeDoors() {
		if (!this.constructed) {
			return;
		}
		this.doorsOpen = false;
		for (Block bo3 : this.doors) {
			Block b = new Location(this.spawn.getWorld(), bo3.getX(), this.spawn.getY() + bo3.getY(), bo3.getZ()).getBlock();
			this.coliseumBlocks.add(b);
			b.setType(bo3.getType());
			b.setData(bo3.getData());
		}
	}

	public void openDoors() {
		if (!this.doorsOpen) {
			return;
		}
		this.doorsOpen = true;
		for (Block bo3 : this.doors) {
			bo3.setType(Material.AIR);
		}
	}
*/
	public boolean isColiseumBlock(Block b) {
		return this.coliseumBlocks.contains(b);
	}

	public boolean isInsideColiseum(Player p) {
		Location central = new Location(p.getWorld(), 0.0D, p.getLocation().getY(), 0.0D);
		if (central.distance(p.getLocation()) < this.radius) {
			return true;
		}
		return (p.getLocation().getBlockX() <= this.radius) && (p.getLocation().getBlockX() >= -this.radius) && (p.getLocation().getBlockZ() <= this.radius) && (p.getLocation().getBlockZ() >= -this.radius)
				&& (central.distance(p.getLocation()) <= 41.0D);
	}
}
