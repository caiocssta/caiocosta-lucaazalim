package me.skater.Utils;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import me.skater.Main;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_7_R4.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import net.minecraft.util.com.mojang.authlib.GameProfile;

public class FakePlayerUtils {

	public static Map<UUID, String> fakenames = new HashMap<>();
	public static Map<UUID, String> originalnames = new HashMap<>();

	public static boolean isBanned(UUID uuid) throws SQLException {
		PreparedStatement stmt = Main.getInstance().mainConnection.prepareStatement("SELECT * FROM fakebanned WHERE `Uuid`='" + uuid.toString().replace("-", "") + "';");
		ResultSet result = stmt.executeQuery();
		if (result.next()) {
			return true;
		}
		stmt.close();
		result.close();
		return false;

	}

	public static void banPlayer(Player p, UUID uuid) throws SQLException {
		if (!Main.getInstance().perm.isMod(p)) {
			p.sendMessage(ChatColor.RED + "Você não possui permissão.");
			return;
		}
		if (isBanned(uuid)) {
			p.sendMessage(ChatColor.RED + "O player ja esta banido dessa função.");
			return;
		}

		Main.getInstance().connect.SQLQuerySync("INSERT INTO fakebanned (`Uuid`) VALUES ('" + uuid.toString().replace("-", "") + "')");
		p.sendMessage(ChatColor.GREEN + "Player punido com sucesso.");
	}

	public static void changePlayerName(Player player, String name) {
		changePlayerName(player, name, true);
	}

	public static void changePlayerName(Player player, String name, boolean respawn) {
		if (!originalnames.containsKey(player.getUniqueId())) {
			originalnames.put(player.getUniqueId(), player.getName());
		}
		fakenames.put(player.getUniqueId(), name);
		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
		GameProfile playerProfile = entityPlayer.getProfile();
		if (respawn) {
			removeFromTab(player, Bukkit.getOnlinePlayers());
		}
		try {
			Field field = playerProfile.getClass().getDeclaredField("name");
			field.setAccessible(true);
			field.set(playerProfile, name);
			field.setAccessible(false);
			entityPlayer.getClass().getDeclaredField("displayName").set(entityPlayer, name);
			entityPlayer.getClass().getDeclaredField("listName").set(entityPlayer, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (respawn) {
			respawnPlayer(player, Bukkit.getOnlinePlayers());
		}
	}

	public static void removePlayerSkin(Player player) {
		removePlayerSkin(player, true);
	}

	public static void removePlayerSkin(Player player, boolean respawn) {
		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
		GameProfile playerProfile = entityPlayer.getProfile();
		playerProfile.getProperties().clear();
		if (respawn) {
			respawnPlayer(player, Bukkit.getOnlinePlayers());
		}
	}

	public static void changePlayerSkin(Player player, String name, UUID uuid) {
		changePlayerSkin(player, name, uuid, true);
	}

	public static void changePlayerSkin(Player player, String name, UUID uuid, boolean respawn) {
		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
		GameProfile playerProfile = entityPlayer.getProfile();
		playerProfile.getProperties().clear();
		try {
			playerProfile.getProperties().put("textures", CustomPlayerAPI.Textures.get(new GameProfile(uuid, name)));
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		if (respawn) {
			respawnPlayer(player, Bukkit.getOnlinePlayers());
		}
	}

	public void addToTab(Player player, Collection<? extends Player> players) {
		PacketPlayOutPlayerInfo addPlayerInfo = PacketPlayOutPlayerInfo.addPlayer(((CraftPlayer) player).getHandle());
		PacketPlayOutPlayerInfo updatePlayerInfo = PacketPlayOutPlayerInfo.updateDisplayName(((CraftPlayer) player).getHandle());
		for (Player online : players) {
			if (online.canSee(player)) {
				((CraftPlayer) online).getHandle().playerConnection.sendPacket(addPlayerInfo);
				((CraftPlayer) online).getHandle().playerConnection.sendPacket(updatePlayerInfo);
			}
		}
	}

	public static void removeFromTab(Player player, Player[] players) {
		PacketPlayOutPlayerInfo removePlayerInfo = PacketPlayOutPlayerInfo.removePlayer(((CraftPlayer) player).getHandle());
		for (Player online : players) {
			if (online.canSee(player)) {
				((CraftPlayer) online).getHandle().playerConnection.sendPacket(removePlayerInfo);
			}
		}
	}

	public static void respawnPlayer(Player player, Player[] players) {
		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
		PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(new int[] { entityPlayer.getId() });
		PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(entityPlayer);
		PacketPlayOutPlayerInfo addPlayerInfo = PacketPlayOutPlayerInfo.addPlayer(((CraftPlayer) player).getHandle());
		PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(entityPlayer.getId(), entityPlayer.getDataWatcher(), true);
		PacketPlayOutEntityHeadRotation headRotation = new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) MathHelper.d(entityPlayer.getHeadRotation() * 256.0F / 360.0F));
		PacketPlayOutPlayerInfo removePlayerInfo = PacketPlayOutPlayerInfo.removePlayer(((CraftPlayer) player).getHandle());
		for (Player online : players) {
			if (online.canSee(player)) {
				((CraftPlayer) online).getHandle().playerConnection.sendPacket(removePlayerInfo);
				((CraftPlayer) online).getHandle().playerConnection.sendPacket(addPlayerInfo);
				if (online.getUniqueId() != player.getUniqueId()) {
					((CraftPlayer) online).getHandle().playerConnection.sendPacket(destroy);
					((CraftPlayer) online).getHandle().playerConnection.sendPacket(spawn);
					((CraftPlayer) online).getHandle().playerConnection.sendPacket(metadata);
					((CraftPlayer) online).getHandle().playerConnection.sendPacket(headRotation);
				}
			}
		}
	}

	public static Boolean isFake(Player player) {
		return fakenames.containsKey(player.getUniqueId());
	}

	public static String getName(Player player) {
		if (fakenames.containsKey(player.getUniqueId())) {
			return fakenames.get(player.getUniqueId());
		}
		return player.getName();
	}

	public static boolean isFake(String name) {
		boolean valid = true;
		for (String fake : fakenames.values())
			if (fake.equalsIgnoreCase(name))
				valid = false;
		return valid;
	}

	public static void removePlayer(Player player) {
		fakenames.remove(player.getUniqueId());
	}

	public static String getFakeName(Player player) {
		return fakenames.get(player.getUniqueId());
	}

	public static String getOriginalName(Player player) {
		return originalnames.get(player.getUniqueId());
	}

	public static boolean validateName(String username) {
		Pattern pattern = Pattern.compile("[a-zA-Z0-9_]{1,16}");
		Matcher matcher = pattern.matcher(username);
		return matcher.matches();
	}
}
