package me.skater.HologramAPI;

/*   2:    */
/*   5:    */ import java.lang.reflect.Constructor;
/*   6:    */ import java.lang.reflect.Field;
/*   7:    */ import java.lang.reflect.Method;
/*   8:    */ import java.util.ArrayList;
/*   9:    */ import java.util.Arrays;
/*  10:    */ import java.util.List;
/*  11:    */ import java.util.Map;

/*  12:    */ import org.bukkit.Location;
/*  13:    */ import org.bukkit.entity.Player;

/*  14:    */
/*  15:    */ public class HologramV2
/* 16: */ {
	/* 17: 19 */ public Double distance = Double.valueOf(0.30D);
	/* 18: 20 */ private boolean showing = false;
	/* 19: 22 */ public List<String> lines = new ArrayList<String>();
	/* 20: 23 */ public List<Integer> ids = new ArrayList<Integer>();

	/* 21: */
	/* 22: */ public HologramV2(String... lines)
	/* 23: */ {
		/* 24: 26 */ this.distance = Double.valueOf(0.30D);
		/* 25: 27 */ this.lines = Arrays.asList(lines);
		/* 26: */ }
	/* 27: */

	public void setLines(String... lines) {
		this.lines = Arrays.asList(lines);
	}

	/* 28: */ public void show(Player player, Location loc)
	/* 29: */ {
		/* 30: 31 */ if (this.showing) {
			/* 31: */ try
			/* 32: */ {
				/* 33: 33 */ throw new Exception("Is already showing!");
				/* 34: */ }
			/* 35: */ catch (Exception e)
			/* 36: */ {
				/* 38: */ }
			/* 39: */ }
		/* 40: 38 */ Location first = loc.clone().add(0.0D, this.lines.size() / 2 * this.distance.doubleValue(), 0.0D);
		/* 41: 39 */ for (int i = 0; i < this.lines.size(); i++)
		/* 42: */ {
			/* 43: 40 */ this.ids.addAll(showLine(player, first.clone(), this.lines.get(i)));
			/* 44: 41 */ first.subtract(0.0D, this.distance.doubleValue(), 0.0D);
			/* 45: */ }
		/* 46: 43 */ this.showing = true;
		/* 47: */ }

	/* 48: */
	/* 49: */ public void destroy(Player player)
	/* 50: */ {
		/* 51: 47 */ if (!this.showing) {
			/* 52: */ try
			/* 53: */ {
				/* 54: 49 */
				/* 55: */ }
			/* 56: */ catch (Exception e)
			/* 57: */ {
				/* 59: */ }
			/* 60: */ }
		/* 61: 54 */ int[] ints = new int[this.ids.size()];
		/* 62: 55 */ for (int j = 0; j < ints.length; j++) {
			/* 63: 56 */ ints[j] = this.ids.get(j).intValue();
			/* 64: */ }
		/* 65: */ try
		/* 66: */ {
			/* 67: 59 */ Constructor<?> packetPlayOutEntityDestroyConstr = Class.forName("net.minecraft.server." + ReflectionV2.getVersion() + ".PacketPlayOutEntityDestroy").getConstructor(new Class[] { int[].class });
			/* 68: 60 */ Object destroyPacket = packetPlayOutEntityDestroyConstr.newInstance(new Object[] { ints });
			/* 69: 61 */ Object getHandle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
			/* 70: 62 */ Object playerConnection = getHandle.getClass().getField("playerConnection").get(getHandle);
			/* 71: 63 */ Method sendPacket = playerConnection.getClass().getMethod("sendPacket", new Class[] { Class.forName("net.minecraft.server." + ReflectionV2.getVersion() + ".Packet") });
			/* 72: 64 */ sendPacket.invoke(playerConnection, new Object[] { destroyPacket });
			/* 73: */ }
		/* 74: */ catch (Exception e)
		/* 75: */ {
			/* 76: 66 */ e.printStackTrace();
			/* 77: */ }
		/* 78: 68 */ this.showing = false;
		/* 79: */ }

	/* 80: */
	/* 81: */ public List<Integer> showLine(Player player, Location loc, String text)
	/* 82: */ {
		/* 83: 72 */ if (ReflectionV2.getVersion().contains("1_8")) {
			/* 84: */ try
			/* 85: */ {
				/* 86: 74 */ Method getPlayerHandle = Class.forName("org.bukkit.craftbukkit." + ReflectionV2.getVersion() + ".entity.CraftPlayer").getMethod("getHandle", new Class[0]);
				/* 87: 75 */ Field playerConnection = Class.forName("net.minecraft.server." + ReflectionV2.getVersion() + ".EntityPlayer").getField("playerConnection");
				/* 88: 76 */ playerConnection.setAccessible(true);
				/* 89: 77 */ Method sendPacket = playerConnection.getType().getMethod("sendPacket", new Class[] { Class.forName("net.minecraft.server." + ReflectionV2.getVersion() + ".Packet") });
				/* 90: */
				/* 91: 79 */ Class<?> craftw = Class.forName("org.bukkit.craftbukkit." + ReflectionV2.getVersion() + ".CraftWorld");
				/* 92: 80 */ Class<?> w = Class.forName("net.minecraft.server." + ReflectionV2.getVersion() + ".World");
				/* 93: */
				/* 94: 82 */ Method getWorldHandle = craftw.getDeclaredMethod("getHandle", new Class[0]);
				/* 95: 83 */ Object worldServer = getWorldHandle.invoke(craftw.cast(loc.getWorld()), new Object[0]);
				/* 96: */
				/* 97: 85 */ Constructor<?> packetPlayOutSpawnEntityLivingConstr = Class.forName("net.minecraft.server." + ReflectionV2.getVersion() + ".PacketPlayOutSpawnEntityLiving")
						.getConstructor(new Class[] { Class.forName("net.minecraft.server." + ReflectionV2.getVersion() + ".EntityLiving") });
				/* 98: */
				/* 99: */
				/* 100: */
				/* 101: */
				/* 102: 90 */ Constructor<?> entityArmorStandConstr = Class.forName("net.minecraft.server." + ReflectionV2.getVersion() + ".EntityArmorStand").getConstructor(new Class[] { w });
				/* 103: 91 */ Object entityArmorStand = entityArmorStandConstr.newInstance(new Object[] { worldServer });
				/* 104: 92 */ Method setLoc2 = entityArmorStand.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setLocation", new Class[] { Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE });
				/* 105: 93 */ setLoc2.invoke(entityArmorStand, new Object[] { Double.valueOf(loc.getX()), Double.valueOf(loc.getY() - 1.0D), Double.valueOf(loc.getZ()), Float.valueOf(0.0F), Float.valueOf(0.0F) });
				/* 106: 94 */ Method setCustomName = entityArmorStand.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setCustomName", new Class[] { String.class });
				/* 107: 95 */ setCustomName.invoke(entityArmorStand, new Object[] { text });
				/* 108: 96 */ Method setCustomNameVisible = entityArmorStand.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setCustomNameVisible", new Class[] { Boolean.TYPE });
				/* 109: 97 */ setCustomNameVisible.invoke(entityArmorStand, new Object[] { Boolean.valueOf(true) });
				/* 110: 98 */ Method getArmorStandId = entityArmorStand.getClass().getSuperclass().getSuperclass().getDeclaredMethod("getId", new Class[0]);
				/* 111: 99 */ int armorstandId = ((Integer) getArmorStandId.invoke(entityArmorStand, new Object[0])).intValue();
				/* 112:100 */ Method setInvisble = entityArmorStand.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setInvisible", new Class[] { Boolean.TYPE });
				/* 113:101 */ setInvisble.invoke(entityArmorStand, new Object[] { Boolean.valueOf(true) });
				/* 114: */
				/* 115: */
				/* 116: */
				/* 117: */
				/* 118:106 */ Object horsePacket = packetPlayOutSpawnEntityLivingConstr.newInstance(new Object[] { entityArmorStand });
				/* 119:107 */ sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(player, new Object[0])), new Object[] { horsePacket });
				/* 120: */
				/* 121: */
				/* 122: */
				/* 123:111 */ return Arrays.asList(new Integer[] { Integer.valueOf(armorstandId) });
				/* 124: */ }
			/* 125: */ catch (Exception e)
			/* 126: */ {
				/* 127:113 */ e.printStackTrace();
				/* 128: */ }
			/* 129: */ }
		/* 130: */ try
		/* 131: */ {
			/* 132:118 */ boolean playerIs1_8 = ReflectionV2.hasPlayerClient1_8(player).booleanValue();
			/* 133: */
			/* 134:120 */ Method getPlayerHandle = Class.forName("org.bukkit.craftbukkit." + ReflectionV2.getVersion() + ".entity.CraftPlayer").getMethod("getHandle", new Class[0]);
			/* 135:121 */ Field playerConnection = Class.forName("net.minecraft.server." + ReflectionV2.getVersion() + ".EntityPlayer").getField("playerConnection");
			/* 136:122 */ playerConnection.setAccessible(true);
			/* 137:123 */ Method sendPacket = playerConnection.getType().getMethod("sendPacket", new Class[] { Class.forName("net.minecraft.server." + ReflectionV2.getVersion() + ".Packet") });
			/* 138: */
			/* 139:125 */ Class<?> craftw = Class.forName("org.bukkit.craftbukkit." + ReflectionV2.getVersion() + ".CraftWorld");
			/* 140:126 */ Class<?> w = Class.forName("net.minecraft.server." + ReflectionV2.getVersion() + ".World");
			/* 141:127 */ Class<?> entity = Class.forName("net.minecraft.server." + ReflectionV2.getVersion() + ".Entity");
			/* 142:128 */ Method getWorldHandle = craftw.getDeclaredMethod("getHandle", new Class[0]);
			/* 143:129 */ Object worldServer = getWorldHandle.invoke(craftw.cast(loc.getWorld()), new Object[0]);
			/* 144:130 */ Constructor<?> packetPlayOutSpawnEntityConstr = Class.forName("net.minecraft.server." + ReflectionV2.getVersion() + ".PacketPlayOutSpawnEntity")
					.getConstructor(new Class[] { entity, Integer.TYPE });
			/* 145:131 */ Constructor<?> packetPlayOutSpawnEntityLivingConstr = Class.forName("net.minecraft.server." + ReflectionV2.getVersion() + ".PacketPlayOutSpawnEntityLiving")
					.getConstructor(new Class[] { Class.forName("net.minecraft.server." + ReflectionV2.getVersion() + ".EntityLiving") });
			/* 146:132 */ Constructor<?> packetPlayOutAttachEntityConstr = Class.forName("net.minecraft.server." + ReflectionV2.getVersion() + ".PacketPlayOutAttachEntity")
					.getConstructor(new Class[] { Integer.TYPE, entity, entity });
			/* 147: */
			/* 148: */
			/* 149:135 */ Constructor<?> witherSkullConstr = Class.forName("net.minecraft.server." + ReflectionV2.getVersion() + ".EntityWitherSkull").getConstructor(new Class[] { w });
			/* 150:136 */ Object witherSkull = witherSkullConstr.newInstance(new Object[] { worldServer });
			/* 151:137 */ Method setLoc = witherSkull.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setLocation", new Class[] { Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE });
			/* 152:138 */ setLoc.invoke(witherSkull,
					new Object[] { Double.valueOf(loc.getX()), Double.valueOf(loc.getY() + (playerIs1_8 ? -1.0D : 55.0D)), Double.valueOf(loc.getZ()), Float.valueOf(0.0F), Float.valueOf(0.0F) });
			/* 153:139 */ Method getWitherSkullId = witherSkull.getClass().getSuperclass().getSuperclass().getDeclaredMethod("getId", new Class[0]);
			/* 154:140 */ int witherSkullId = ((Integer) getWitherSkullId.invoke(witherSkull, new Object[0])).intValue();
			/* 155: */
			/* 156: */
			/* 157:143 */ Constructor<?> entityHorseConstr = Class.forName("net.minecraft.server." + ReflectionV2.getVersion() + ".EntityHorse").getConstructor(new Class[] { w });
			/* 158:144 */ Object entityHorse = entityHorseConstr.newInstance(new Object[] { worldServer });
			/* 159: */
			/* 160:146 */ Method setLoc2 = entityHorse.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("setLocation",
					new Class[] { Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE });
			/* 161:147 */ setLoc2.invoke(entityHorse,
					new Object[] { Double.valueOf(loc.getX()), Double.valueOf(loc.getY() + (playerIs1_8 ? -1.0D : 55.0D)), Double.valueOf(loc.getZ()), Float.valueOf(0.0F), Float.valueOf(0.0F) });
			/* 162: */
			/* 163:149 */ Method setAge = entityHorse.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setAge", new Class[] { Integer.TYPE });
			/* 164:150 */ setAge.invoke(entityHorse, new Object[] { Integer.valueOf(-1700000) });
			/* 165: */
			/* 166:152 */ Method setCustomName = entityHorse.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("setCustomName", new Class[] { String.class });
			/* 167:153 */ setCustomName.invoke(entityHorse, new Object[] { text });
			/* 168: */
			/* 169:155 */ Method setCustomNameVisible = entityHorse.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("setCustomNameVisible", new Class[] { Boolean.TYPE });
			/* 170:156 */ setCustomNameVisible.invoke(entityHorse, new Object[] { Boolean.valueOf(true) });
			/* 171: */
			/* 172:158 */ Method getHorseId = entityHorse.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("getId", new Class[0]);
			/* 173:159 */ int horseId = ((Integer) getHorseId.invoke(entityHorse, new Object[0])).intValue();
			/* 174:161 */ if (playerIs1_8)
			/* 175: */ {
				/* 176:163 */ Method setInvisble = entityHorse.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("setInvisible",
						new Class[] { Boolean.TYPE });
				/* 177:164 */ setInvisble.invoke(entityHorse, new Object[] { Boolean.valueOf(true) });
				/* 178: */ }
			/* 179:168 */ Object horsePacket = packetPlayOutSpawnEntityLivingConstr.newInstance(new Object[] { entityHorse });
			/* 180:169 */ if (playerIs1_8)
			/* 181: */ {
				/* 182:171 */ Field b = horsePacket.getClass().getDeclaredField("b");
				/* 183:172 */ b.setAccessible(true);
				/* 184:173 */ b.set(horsePacket, Integer.valueOf(30));
				/* 185: */
				/* 186:175 */ Field datawatcher = entityHorse.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredField("datawatcher");
				/* 187:176 */ datawatcher.setAccessible(true);
				/* 188:177 */ Object datawatcherInstance = datawatcher.get(entityHorse);
				/* 189:178 */ Field d = datawatcherInstance.getClass().getDeclaredField("d");
				/* 190:179 */ d.setAccessible(true);
				/* 191:180 */ Map<?, ?> dmap = (Map<?, ?>) d.get(datawatcherInstance);
				/* 192:181 */ dmap.remove(Integer.valueOf(10));
				/* 193:182 */ dmap.remove(Integer.valueOf(11));
				/* 194:183 */ dmap.remove(Integer.valueOf(12));
				/* 195:184 */ dmap.remove(Integer.valueOf(13));
				/* 196:185 */ dmap.remove(Integer.valueOf(14));
				/* 197:186 */ dmap.remove(Integer.valueOf(15));
				/* 198:187 */ dmap.remove(Integer.valueOf(16));
				/* 199:188 */ Method a = datawatcherInstance.getClass().getDeclaredMethod("a", new Class[] { Integer.TYPE, Object.class });
				/* 200:189 */ a.invoke(datawatcherInstance, new Object[] { Integer.valueOf(10), Byte.valueOf((byte) 0) });
				/* 201: */ }
			/* 202:191 */ sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(player, new Object[0])), new Object[] { horsePacket });
			/* 203:192 */ if (!playerIs1_8)
			/* 204: */ {
				/* 205:193 */ Object witherPacket = packetPlayOutSpawnEntityConstr.newInstance(new Object[] { witherSkull, Integer.valueOf(64) });
				/* 206:194 */ sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(player, new Object[0])), new Object[] { witherPacket });
				/* 207: */
				/* 208:196 */ Object attachPacket = packetPlayOutAttachEntityConstr.newInstance(new Object[] { Integer.valueOf(0), entityHorse, witherSkull });
				/* 209:197 */ sendPacket.invoke(playerConnection.get(getPlayerHandle.invoke(player, new Object[0])), new Object[] { attachPacket });
				/* 210: */ }
			return Arrays.asList(new Integer[] { Integer.valueOf(witherSkullId), Integer.valueOf(horseId) });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
