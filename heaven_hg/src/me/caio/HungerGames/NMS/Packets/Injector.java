package me.caio.HungerGames.NMS.Packets;

import java.lang.reflect.Field;
import java.util.Map;

import net.minecraft.server.v1_7_R4.EnumProtocol;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.util.com.google.common.collect.BiMap;

public class Injector {
	public static void inject() {
		try {
			addPacket(EnumProtocol.PLAY, true, 69, PacketPlayOutTitle.class);
			addPacket(EnumProtocol.PLAY, true, 71, PacketPlayOutPlayerListHeaderFooter.class);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void addPacket(EnumProtocol protocol, boolean clientbound, int id, Class<? extends Packet> packet) throws NoSuchFieldException, IllegalAccessException {
		Field packets;
		if (!clientbound) {
			packets = EnumProtocol.class.getDeclaredField("h");
		} else {
			packets = EnumProtocol.class.getDeclaredField("i");
		}
		packets.setAccessible(true);
		BiMap<Integer, Class<? extends Packet>> pMap = (BiMap) packets.get(protocol);
		pMap.put(Integer.valueOf(id), packet);
		Field map = EnumProtocol.class.getDeclaredField("f");
		map.setAccessible(true);
		Map<Class<? extends Packet>, EnumProtocol> protocolMap = (Map) map.get(null);
		protocolMap.put(packet, protocol);
	}

}
