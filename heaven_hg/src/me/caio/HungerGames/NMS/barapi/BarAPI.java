package me.caio.HungerGames.NMS.barapi;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import me.caio.HungerGames.Main;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BarAPI implements Listener {
	public static HashMap<UUID, FakeDragon> players = new HashMap<UUID, FakeDragon>();
	private static HashMap<UUID, Integer> timers = new HashMap<UUID, Integer>();
	private static Main m;

	public BarAPI(Main main) {
		m = main;
		new BukkitRunnable() {
			public void run() {
				for (UUID uuid : BarAPI.players.keySet()) {
					Player p = Bukkit.getPlayer(uuid);
					if (p != null) {
						Util.sendPacket(p, ((FakeDragon) BarAPI.players.get(uuid)).getTeleportPacket(BarAPI.getDragonLocation(p)));
					}
				}
			}
		}.runTaskTimer(m, 100L, 4L);
	}

	public static void clear() {
		for (Player player : m.getServer().getOnlinePlayers()) {
			quit(player);
		}
		players.clear();
		for (Iterator<Integer> localIterator = timers.values().iterator(); localIterator.hasNext();) {
			int timerID = ((Integer) localIterator.next()).intValue();
			Bukkit.getScheduler().cancelTask(timerID);
		}
		timers.clear();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void PlayerLoggout(PlayerQuitEvent event) {
		quit(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerKick(PlayerKickEvent event) {
		quit(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		handleTeleport(event.getPlayer(), event.getTo().clone());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerTeleport(PlayerRespawnEvent event) {
		handleTeleport(event.getPlayer(), event.getRespawnLocation().clone());
	}

	private void handleTeleport(final Player player, final Location loc) {
		if (!hasBar(player)) {
			return;
		}
		Bukkit.getScheduler().runTaskLater(m, new Runnable() {
			public void run() {
				if (!BarAPI.hasBar(player)) {
					return;
				}
				FakeDragon oldDragon = BarAPI.getDragon(player, "");

				float health = oldDragon.health;
				String message = oldDragon.name;

				Util.sendPacket(player, BarAPI.getDragon(player, "").getDestroyPacket());

				BarAPI.players.remove(player.getUniqueId());

				FakeDragon dragon = BarAPI.addDragon(player, loc, message);
				dragon.health = health;

				BarAPI.sendDragon(dragon, player);
			}
		}, 2L);
	}

	public static void quit(Player player) {
		removeBar(player);
	}

	public static void setMessage(String message) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			setMessage(player, message);
		}
	}

	public static void setMessage(Player player, String message) {
		FakeDragon dragon = getDragon(player, message);

		dragon.name = cleanMessage(message);
		dragon.health = dragon.getMaxHealth();

		cancelTimer(player);
		sendDragon(dragon, player);
	}

	public static void setMessage(String message, int seconds) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			setMessage(player, message, seconds);
		}
	}

	public static void setMessage(final Player player, String message, int seconds) {
		Validate.isTrue(seconds > 0, "Seconds must be above 1 but was: ", seconds);

		FakeDragon dragon = getDragon(player, message);

		dragon.name = cleanMessage(message);
		dragon.health = dragon.getMaxHealth();

		cancelTimer(player);

		timers.put(player.getUniqueId(),

				Integer.valueOf(Bukkit.getScheduler().runTaskLater(m, new Runnable() {
					public void run() {
						BarAPI.removeBar(player);
						BarAPI.cancelTimer(player);
					}
				}, 20L * seconds).getTaskId()));
		sendDragon(dragon, player);
	}

	public static boolean hasBar(Player player) {
		return players.get(player.getUniqueId()) != null;
	}

	public static void removeBar(Player player) {
		if (!hasBar(player)) {
			return;
		}
		Util.sendPacket(player, getDragon(player, "").getDestroyPacket());

		players.remove(player.getUniqueId());

		cancelTimer(player);
	}

	public static String getMessage(Player player) {
		if (!hasBar(player)) {
			return "";
		}
		return getDragon(player, "").name;
	}

	private static String cleanMessage(String message) {
		if (message.length() > 64) {
			message = message.substring(0, 63);
		}
		return message;
	}

	private static void cancelTimer(Player player) {
		Integer timerID = (Integer) timers.remove(player.getUniqueId());
		if (timerID != null) {
			Bukkit.getScheduler().cancelTask(timerID.intValue());
		}
	}

	private static void sendDragon(FakeDragon dragon, Player player) {
		Util.sendPacket(player, dragon.getMetaPacket(dragon.getWatcher()));
		Util.sendPacket(player, dragon.getTeleportPacket(getDragonLocation(player)));
	}

	private static FakeDragon getDragon(Player player, String message) {
		if (hasBar(player)) {
			return (FakeDragon) players.get(player.getUniqueId());
		}
		return addDragon(player, cleanMessage(message));
	}

	private static FakeDragon addDragon(Player player, String message) {
		FakeDragon dragon = Util.newDragon(message, getDragonLocation(player));

		Util.sendPacket(player, dragon.getSpawnPacket());

		players.put(player.getUniqueId(), dragon);

		return dragon;
	}

	private static FakeDragon addDragon(Player player, Location loc, String message) {
		FakeDragon dragon = Util.newDragon(message, getDragonLocation(player));

		Util.sendPacket(player, dragon.getSpawnPacket());

		players.put(player.getUniqueId(), dragon);

		return dragon;
	}

	public static Location getDragonLocation(Player p) {
		Location loc = p.getLocation();

		float pitch = loc.getPitch();
		if (pitch >= 55.0F) {
			loc.add(0.0D, -32.0D, 0.0D);
		} else if (pitch <= -55.0F) {
			loc.add(0.0D, 32.0D, 0.0D);
		} else {
			loc = loc.getBlock().getRelative(getDirection(loc), 32).getLocation();
		}
		return loc;
	}

	private static BlockFace getDirection(Location loc) {
		float dir = Math.round(loc.getYaw() / 90.0F);
		if ((dir == -4.0F) || (dir == 0.0F) || (dir == 4.0F)) {
			return BlockFace.SOUTH;
		}
		if ((dir == -1.0F) || (dir == 3.0F)) {
			return BlockFace.EAST;
		}
		if ((dir == -2.0F) || (dir == 2.0F)) {
			return BlockFace.NORTH;
		}
		if ((dir == -3.0F) || (dir == 1.0F)) {
			return BlockFace.WEST;
		}
		return null;
	}
}
