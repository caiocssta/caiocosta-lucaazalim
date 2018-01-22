package me.skater.Managers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.libs.joptsimple.OptionSet;
import org.bukkit.entity.Player;

public class ReflectionManager {
	private SimpleCommandMap commandMap;
	private String currentVersion;
	private Object propertyManager;

	public ReflectionManager() {
		try {
			this.commandMap = ((SimpleCommandMap) Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap", new Class[0]).invoke(Bukkit.getServer(), new Object[0]));
			Object obj = Bukkit.getServer().getClass().getDeclaredMethod("getServer", new Class[0]).invoke(Bukkit.getServer(), new Object[0]);
			this.propertyManager = obj.getClass().getDeclaredMethod("getPropertyManager", new Class[0]).invoke(obj, new Object[0]);
			this.currentVersion = this.propertyManager.getClass().getPackage().getName();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public SimpleCommandMap getCommandMap() {
		return this.commandMap;
	}

	public Object getPropertiesConfig(String name, Object obj) {
		try {
			Properties properties = (Properties) this.propertyManager.getClass().getDeclaredField("properties").get(this.propertyManager);
			if (!properties.containsKey(name)) {
				properties.setProperty(name, (String) obj);
				savePropertiesConfig();
			}
			Field opt = this.propertyManager.getClass().getDeclaredField("options");
			opt.setAccessible(true);
			OptionSet options = (OptionSet) opt.get(this.propertyManager);
			if ((options != null) && (options.has(name)) && (!name.equals("online-mode"))) {
				return options.valueOf(name);
			}
			if ((obj instanceof String)) {
				return properties.getProperty(name, (String) obj);
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public void removeArrows(Player player) {
		try {
			Method handle = player.getClass().getMethod("getHandle", new Class[0]);
			String methodName = "abcdefg";
			if (this.currentVersion.contains("v1_6_R2")) {
				methodName = "m";
			}
			handle.invoke(player, new Object[0]).getClass().getMethod(methodName, new Class[] { Integer.TYPE }).invoke(handle.invoke(player, new Object[0]), new Object[] { Integer.valueOf(0) });
		} catch (Exception localException) {
		}
	}

	public void savePropertiesConfig() {
		try {
			this.propertyManager.getClass().getMethod("savePropertiesFile", new Class[0]).invoke(this.propertyManager, new Object[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void sendChunk(Player p, int x, int z) {
		try {
			Object obj = p.getClass().getDeclaredMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
			List list = (List) obj.getClass().getField("chunkCoordIntPairQueue").get(obj);
			Constructor con = Class.forName(this.currentVersion + ".ChunkCoordIntPair").getConstructor(new Class[] { Integer.TYPE, Integer.TYPE });
			list.add(con.newInstance(new Object[] { Integer.valueOf(x), Integer.valueOf(z) }));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Object setPropertiesConfig(String name, Object obj) {
		try {
			return this.propertyManager.getClass().getMethod("a", new Class[] { String.class, Object.class }).invoke(this.propertyManager, new Object[] { name, obj });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	@SuppressWarnings("rawtypes")
	public void setWidthHeight(Player p, float height, float width, float length) {
		try {
			Method handle = p.getClass().getMethod("getHandle", new Class[0]);
			Class c = Class.forName(this.currentVersion + ".Entity");
			Field field1 = c.getDeclaredField("height");
			Field field2 = c.getDeclaredField("width");
			Field field3 = c.getDeclaredField("length");
			field1.setFloat(handle.invoke(p, new Object[0]), height);
			field2.setFloat(handle.invoke(p, new Object[0]), width);
			field3.setFloat(handle.invoke(p, new Object[0]), length);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
