package me.caio.HungerGames.NMS.barapi;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Location;

public class v1_8Fake extends FakeDragon {
	private Object dragon;
	private int id;

	public v1_8Fake(String name, Location loc) {
		super(name, loc);
	}

	public Object getSpawnPacket() {
		Class<?> Entity = Util.getCraftClass("Entity");
		Class<?> EntityLiving = Util.getCraftClass("EntityLiving");
		Class<?> EntityEnderDragon = Util.getCraftClass("EntityWither");
		Object packet = null;
		try {
			this.dragon = EntityEnderDragon.getConstructor(new Class[] { Util.getCraftClass("World") }).newInstance(new Object[] { getWorld() });

			Method setLocation = Util.getMethod(EntityEnderDragon, "setLocation", new Class[] { Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE });
			setLocation.invoke(this.dragon, new Object[] { Integer.valueOf(getX()), Integer.valueOf(getY()), Integer.valueOf(getZ()), Integer.valueOf(getPitch()), Integer.valueOf(getYaw()) });

			Method setInvisible = Util.getMethod(EntityEnderDragon, "setInvisible", new Class[] { Boolean.TYPE });
			setInvisible.invoke(this.dragon, new Object[] { Boolean.valueOf(true) });

			Method setCustomName = Util.getMethod(EntityEnderDragon, "setCustomName", new Class[] { String.class });
			setCustomName.invoke(this.dragon, new Object[] { this.name });

			Method setHealth = Util.getMethod(EntityEnderDragon, "setHealth", new Class[] { Float.TYPE });
			if (this.health < 151.0F) {
				this.health = 151.0F;
			}
			setHealth.invoke(this.dragon, new Object[] { Float.valueOf(this.health) });

			Field motX = Util.getField(Entity, "motX");
			motX.set(this.dragon, Byte.valueOf(getXvel()));

			Field motY = Util.getField(Entity, "motY");
			motY.set(this.dragon, Byte.valueOf(getYvel()));

			Field motZ = Util.getField(Entity, "motZ");
			motZ.set(this.dragon, Byte.valueOf(getZvel()));

			Method getId = Util.getMethod(EntityEnderDragon, "getId", new Class[0]);
			this.id = ((Integer) getId.invoke(this.dragon, new Object[0])).intValue();

			Class<?> PacketPlayOutSpawnEntityLiving = Util.getCraftClass("PacketPlayOutSpawnEntityLiving");

			packet = PacketPlayOutSpawnEntityLiving.getConstructor(new Class[] { EntityLiving }).newInstance(new Object[] { this.dragon });
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return packet;
	}

	public Object getDestroyPacket() {
		Class<?> PacketPlayOutEntityDestroy = Util.getCraftClass("PacketPlayOutEntityDestroy");
		Object packet = null;
		try {
			packet = PacketPlayOutEntityDestroy.newInstance();
			Field a = PacketPlayOutEntityDestroy.getDeclaredField("a");
			a.setAccessible(true);
			a.set(packet, new int[] { this.id });
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return packet;
	}

	public Object getMetaPacket(Object watcher) {
		Class<?> DataWatcher = Util.getCraftClass("DataWatcher");

		Class<?> PacketPlayOutEntityMetadata = Util.getCraftClass("PacketPlayOutEntityMetadata");
		Object packet = null;
		try {
			packet = PacketPlayOutEntityMetadata.getConstructor(new Class[] { Integer.TYPE, DataWatcher, Boolean.TYPE }).newInstance(new Object[] { Integer.valueOf(this.id), watcher, Boolean.valueOf(true) });
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return packet;
	}

	public Object getTeleportPacket(Location loc) {
		Class<?> PacketPlayOutEntityTeleport = Util.getCraftClass("PacketPlayOutEntityTeleport");
		Object packet = null;
		try {
			packet = PacketPlayOutEntityTeleport.getConstructor(new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Byte.TYPE, Byte.TYPE, Boolean.TYPE, Boolean.TYPE })
					.newInstance(new Object[] { Integer.valueOf(this.id), Integer.valueOf(loc.getBlockX() * 32), Integer.valueOf(loc.getBlockY() * 32), Integer.valueOf(loc.getBlockZ() * 32),
							Byte.valueOf((byte) ((int) loc.getYaw() * 256 / 360)), Byte.valueOf((byte) ((int) loc.getPitch() * 256 / 360)), Boolean.valueOf(false), Boolean.valueOf(true) });
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return packet;
	}

	public Object getWatcher() {
		Class<?> Entity = Util.getCraftClass("Entity");
		Class<?> DataWatcher = Util.getCraftClass("DataWatcher");

		Object watcher = null;
		try {
			watcher = DataWatcher.getConstructor(new Class[] { Entity }).newInstance(new Object[] { this.dragon });
			Method a = Util.getMethod(DataWatcher, "a", new Class[] { Integer.TYPE, Object.class });

			a.invoke(watcher, new Object[] { Integer.valueOf(5), Byte.valueOf((byte) (isVisible() ? 0 : 32)) });
			if (this.health < 151.0F) {
				this.health = 151.0F;
			}
			a.invoke(watcher, new Object[] { Integer.valueOf(6), Float.valueOf(this.health) });
			a.invoke(watcher, new Object[] { Integer.valueOf(7), Integer.valueOf(0) });
			a.invoke(watcher, new Object[] { Integer.valueOf(8), Byte.valueOf((byte) 0) });
			a.invoke(watcher, new Object[] { Integer.valueOf(10), this.name });
			a.invoke(watcher, new Object[] { Integer.valueOf(11), Byte.valueOf((byte) 1) });
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return watcher;
	}
}
