package me.caio.HungerGames.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Feast {

	private boolean showCords;
	
	public boolean isShowCords() {
		return showCords;
	}

	public void setShowCords(boolean showCords) {
		this.showCords = showCords;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public BukkitTask getTask() {
		return task;
	}

	public void setTask(BukkitTask task) {
		this.task = task;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public List<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}

	private int time;
	private BukkitTask task;
	private Location loc;
	private List<Block> blocks;
	Boolean spawned;

	public Feast(int time, boolean showCords) {
		this(time, showCords, null);
		spawned = false;
	}

	public Feast(int time, boolean showCords, Location loc) {
		this.showCords = showCords;
		this.time = time;
		spawned = false;
		if (loc == null) {
			Random r = new Random();
			loc = new Location(Bukkit.getWorlds().get(0), r.nextInt(100) - 50, 1, r.nextInt(100) - 50);
			loc.setY(Bukkit.getWorld("world").getHighestBlockYAt(loc));
		}
		this.loc = loc;
		blocks = new ArrayList<>();

		Bukkit.getServer().getPluginManager().registerEvents(new Listener() {

			@EventHandler
			public void onBuild(BlockPlaceEvent e) {
				if (blocks.contains(e.getBlock())) {
					e.setCancelled(true);
				}
			}

			@EventHandler
			public void onBreak(BlockBreakEvent e) {
				if (blocks.contains(e.getBlock())) {
					e.setCancelled(true);
				}
			}

			/*
			 * @EventHandler public void onExplode(BlockExplodeEvent e) { if
			 * (blocks.contains(e.getBlock())) { e.blockList().clear(); } }
			 */

			@EventHandler
			public void onExplode(EntityExplodeEvent e) {
				Iterator<Block> blockIt = e.blockList().iterator();
				while (blockIt.hasNext()) {
					Block b = blockIt.next();
					if (blocks.contains(b)) {
						e.blockList().remove(b);
						blockIt.remove();
					}
				}
			}

			@EventHandler
			public void onBlock(BlockDamageEvent e) {
				if (blocks.contains(e.getBlock())) {
					e.setCancelled(true);
				}
			}

			@EventHandler
			public void onBlockChange(BlockFormEvent e) {
				if (blocks.contains(e.getBlock())) {
					e.setCancelled(true);
				}
			}

		}, me.caio.HungerGames.Main.getInstance());
	}

	public void startCountdown() {
		spawnFloor();
		task = new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if (time > 0 && (time <= 5 || time == 10 || time % 60 == 0)) {
					if (showCords) {
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.sendMessage(
									"§cO feast irá spawnar nas cordenadas (" + loc.getBlockX() + ", " + loc.getBlockY()
											+ ", " + loc.getBlockZ() + ") em " + formatTime(time) + "!");
							player.playSound(player.getLocation(), Sound.NOTE_PLING, 1F, 3F);
						}
					}
				}
				if (time == 0) {
					spawnChests();
					stopCountdown();
					for (Player player : Bukkit.getOnlinePlayers()) {
						player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1F, 1F);
						if (!showCords) {
					//		player.sendMessage("§cUm Bônus-Feast spawnou em um lugar aleatório do mapa!");
						}
					}
				}
				time--;
			}
		}.runTaskTimer(me.caio.HungerGames.Main.getInstance(), 0, 20);
	}

	public Feast stopCountdown() {
		task.cancel();
		return this;
	}
	
	public boolean isFeastBlock(Block block){
		return blocks.contains(block);
	}

	public Feast spawnChests() {
		stopCountdown();
		blocks.clear();
		Material[] materiais = new Material[] { Material.CHEST, null, Material.CHEST, null, Material.CHEST, null,
				Material.CHEST, null, Material.CHEST, null, Material.CHEST, null, Material.ENCHANTMENT_TABLE, null,
				Material.CHEST, null, Material.CHEST, null, Material.CHEST, null, Material.CHEST, null, Material.CHEST,
				null, Material.CHEST };

		Location l = loc.clone().add(2, 1, 2);
		for (int i = 0; i < materiais.length; i++) {
			if (materiais[i] != null) {
				l.getBlock().setType(materiais[i]);
				if (l.getBlock().getType().equals(Material.CHEST)) {
					FillChest.feast((Chest) l.getBlock().getState());
				}
			}
			l.add(-1, 0, 0);
			if ((i + 1) % 5 == 0) {
				l.add(5, 0, -1);
			}
		}
		return this;
	}

	public Feast spawnFloor() {
		spawned = true;
		for (int x = -20; x <= 20; x++) {
			for (int z = -20; z <= 20; z++) {
				Location l = loc.clone().add(x, 0, z);
				if (l.distance(loc) < 20) {
					blocks.add(l.getBlock());
					removeAbove(l.getBlock());

					l.getBlock().setType(Material.GRASS);
				}
			}
		}
		return this;
	}

	private Feast removeAbove(Block block) {
		Location l = block.getLocation().clone();
		l.setY(l.getY() + 1);
		Block b = l.getBlock();
		while (l.getY() < 100) {
			b.setType(Material.AIR);
			l.setY(l.getY() + 1);
			b = l.getBlock();

			if (l.getY() < loc.getY() + 10) {
				blocks.add(b);
			}
		}
		return this;
	}
	
	public static String formatTime(int i) {
		if (i >= 60) {
			int minutos = (int) i / 60;
			int segundos = i - (minutos * 60);
			if (segundos == 0) {
				return minutos + "m";
			}
			return minutos + "m " + segundos + "s";
		}
		return i + "s";
	}

}