package me.skater.HologramAPI;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ReflectionV2 {

	public static String getVersion() {
		String name = Bukkit.getServer().getClass().getPackage().getName();
		String version = name.substring(name.lastIndexOf('.') + 1);
		return version;
	}

	public static Class<?> getProtocolClass(String className) {
		String fullName = "org.spigotmc.ProtocolInjector$" + className;
		Class<?> clazz = null;
		try {
			clazz = Class.forName(fullName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clazz;
	}

	public static Boolean hasPlayerClient1_8(Player player) {
		if (ReflectionV2.isSpigotProtocol().booleanValue()) {
			try {
				Object getHandle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
				Object playerConnection = getHandle.getClass().getField("playerConnection").get(getHandle);
				Object networkManager = playerConnection.getClass().getField("networkManager").get(playerConnection);
				Object getVersion = networkManager.getClass().getMethod("getVersion", new Class[0]).invoke(networkManager, new Object[0]);
				if (Integer.parseInt(getVersion.toString()) > 5) {
					return Boolean.valueOf(true);
				}
				return Boolean.valueOf(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return Boolean.valueOf(false);
	}

	public static Class<?> getProtocolClass(String className, String subClassName) {
		return getProtocolClass(className + "$" + subClassName);
	}

	public static Class<?> getNMSClass(String className) {
		String fullName = "net.minecraft.server." + getVersion() + "." + className;
		Class<?> clazz = null;
		try {
			clazz = Class.forName(fullName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clazz;
	}

	public static Class<?> getOBCClass(String className) {
		String fullName = "org.bukkit.craftbukkit." + getVersion() + "." + className;
		Class<?> clazz = null;
		try {
			clazz = Class.forName(fullName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clazz;
	}

	public static Object getHandle(Object obj) {
		try {
			return getMethod(obj.getClass(), "getHandle", new Class[0]).invoke(obj, new Object[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Field getField(Class<?> clazz, String name) {
		try {
			Field field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			return field;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Method getMethod(Class<?> clazz, String name, Class<?>... args) {
		for (Method m : clazz.getMethods()) {
			if ((m.getName().equals(name)) && ((args.length == 0) || (ClassListEqual(args, m.getParameterTypes())))) {
				m.setAccessible(true);
				return m;
			}
		}
		return null;
	}

	public static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
		boolean equal = true;
		if (l1.length != l2.length) {
			return false;
		}
		for (int i = 0; i < l1.length; i++) {
			if (l1[i] != l2[i]) {
				equal = false;
				break;
			}
		}
		return equal;
	}

	public static void sendPacket(Player player, Object packet) {
		try {
			Object getHandle = player.getClass().getMethod("getHandle").invoke(player);
			Object playerConnection = getHandle.getClass().getField("playerConnection").get(getHandle);
			Method sendPacket = playerConnection.getClass().getMethod("sendPacket", ReflectionV2.getNMSClass("Packet"));
			sendPacket.invoke(playerConnection, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Boolean isSpigotProtocol() {
		try {
			Class.forName("org.spigotmc.ProtocolInjector");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	public static Boolean is1_8OrHigher() {
		return Integer.parseInt(getVersion().split("_")[1]) >= 8;
	}

	public static Boolean is1_6OrPrevious() {
		return Integer.parseInt(getVersion().split("_")[1]) <= 6;
	}
}