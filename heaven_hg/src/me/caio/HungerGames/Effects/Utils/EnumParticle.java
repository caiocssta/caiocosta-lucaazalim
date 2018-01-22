package me.caio.HungerGames.Effects.Utils;

import java.util.HashMap;
import java.util.Map;

public enum EnumParticle {
	HUGE_EXPLOSION(

			"hugeexplosion"),
	LARGE_EXPLODE(

			"largeexplode"),
	FIREWORKS_SPARK(

			"fireworksSpark"),
	BUBBLE(

			"bubble", true),
	SUSPEND(

			"suspend", true),
	DEPTH_SUSPEND(

			"depthSuspend"),
	TOWN_AURA(

			"townaura"),
	CRIT(

			"crit"),
	MAGIC_CRIT(

			"magicCrit"),
	SMOKE(

			"smoke"),
	SPELL_MOB(

			"mobSpell"),
	SPELL_MOB_AMBIENT(

			"mobSpellAmbient"),
	SPELL(

			"spell"),
	INSTANT_SPELL(

			"instantSpell"),
	WITCH_MAGIC(

			"witchMagic"),
	NOTE(

			"note"),
	PORTAL(

			"portal"),
	ENCHANTMENT_TABLE(

			"enchantmenttable"),
	EXPLODE(

			"explode"),
	FLAME(

			"flame"),
	LAVA(

			"lava"),
	FOOTSTEP(

			"footstep"),
	SPLASH(

			"splash"),
	WAKE(

			"wake"),
	LARGE_SMOKE(

			"largesmoke"),
	CLOUD(

			"cloud"),
	REDSTONE(

			"reddust"),
	SNOWBALL_POOF(

			"snowballpoof"),
	DRIP_WATER(

			"dripWater"),
	DRIP_LAVA(

			"dripLava"),
	SNOW_SHOVEL(

			"snowshovel"),
	SLIME(

			"slime"),
	HEART(

			"heart"),
	ANGRY_VILLAGER(

			"angryVillager"),
	HAPPY_VILLAGER(

			"happyVillager");

	private static final Map<String, EnumParticle> NAME_MAP;
	private final String name;
	private final boolean requiresWater;

	static {
		NAME_MAP = new HashMap<String, EnumParticle>();
		for (EnumParticle effect : values()) {
			NAME_MAP.put(effect.name, effect);
		}
	}

	private EnumParticle(String name, boolean requiresWater) {
		this.name = name;
		this.requiresWater = requiresWater;
	}

	private EnumParticle(String name) {
		this(name, false);
	}

	public String getName() {
		return this.name;
	}

	public boolean getRequiresWater() {
		return this.requiresWater;
	}

	public static EnumParticle fromName(String name) {
		for (Map.Entry<String, EnumParticle> entry : NAME_MAP.entrySet()) {
			if (((String) entry.getKey()).equalsIgnoreCase(name)) {
				return (EnumParticle) entry.getValue();
			}
		}
		return null;
	}
}
