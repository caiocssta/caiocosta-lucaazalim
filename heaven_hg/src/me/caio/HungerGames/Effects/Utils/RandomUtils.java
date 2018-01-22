package me.caio.HungerGames.Effects.Utils;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.util.Vector;

public final class RandomUtils {
	public static final Random random = new Random(System.nanoTime());

	public static Vector getRandomVector() {
		double x = random.nextDouble() * 2.0D - 1.0D;
		double y = random.nextDouble() * 2.0D - 1.0D;
		double z = random.nextDouble() * 2.0D - 1.0D;

		return new Vector(x, y, z).normalize();
	}

	public static Vector getRandomCircleVector() {
		double rnd = random.nextDouble() * 2.0D * 3.141592653589793D;
		double x = Math.cos(rnd);
		double z = Math.sin(rnd);

		return new Vector(x, 0.0D, z);
	}

	public static Material getRandomMaterial(Material[] materials) {
		return materials[random.nextInt(materials.length)];
	}

	public static double getRandomAngle() {
		return random.nextDouble() * 2.0D * 3.141592653589793D;
	}
}
