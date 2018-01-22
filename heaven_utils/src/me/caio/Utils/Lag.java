package me.skater.Utils;

import me.skater.Main;
import net.minecraft.server.v1_7_R4.EntityPlayer;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Lag implements Runnable {
	public static int TickCount;
	public long[] Ticks;
	public long LastTick;
	public Main m;

	public Lag(Main main) {
		this.m = main;
		TickCount = 0;
		Ticks = new long[600];
		LastTick = 0L;
	}

	public double getTPS() {
		return getTPS(100);
	}

	public double getTPS(int ticks) {
		if (TickCount < ticks) {
			return 20.0D;
		}
		int target = (TickCount - 1 - ticks) % Ticks.length;
		long elapsed = System.currentTimeMillis() - Ticks[target];
		if (ticks / (elapsed / 1000.0D) > 20.0D) {
			return 20.0D;
		}
		return ticks / (elapsed / 1000.0D);
	}

	public double getElapsed(int tickID) {
		if (TickCount - tickID >= Ticks.length) {
			return (TickCount - tickID) * getTPS();
		}
		long time = Ticks[(tickID % Ticks.length)];
		return System.currentTimeMillis() - time;
	}

	public int getPing(Player p) {
		CraftPlayer cp = (CraftPlayer) p;
		EntityPlayer ep = cp.getHandle();
		return ep.ping;
	}

	@Override
	public void run() {
		Ticks[(TickCount % Ticks.length)] = System.currentTimeMillis();

		TickCount += 1;
	}
}
