package me.skater.ranking;

import org.bukkit.ChatColor;

public enum Level {
	UNRANKED(0, 0, ChatColor.WHITE + "-"),
	PRIMARY(1, 5000, ChatColor.GREEN + "="),
	ADVANCED(2, 10000, ChatColor.YELLOW + "☰"),
	EXPERT(3, 15000, ChatColor.DARK_BLUE + "☷"),
	SILVER(4, 20000, ChatColor.GRAY + "✶"),
	GOLD(5, 25000, ChatColor.GOLD + "✷"),
	DIAMOND(6, 30000, ChatColor.AQUA + "✸"),
	SAPHIRE(7, 35000, ChatColor.DARK_AQUA + "✠"),
	EMERALD(8, 40000, ChatColor.DARK_GREEN + "✡"),
	CRYSTAL(9, 45000, ChatColor.BLUE + "✺"),
	ELITE(10, 50000, ChatColor.DARK_PURPLE + "✹"),
	MASTER(11, 55000, ChatColor.RED + "✫"),
	LEGENDARY(12, 60000, ChatColor.DARK_RED + "✪"),
	LEVEL100(100, 9999999, ChatColor.BLACK + "-");

	private int level;
	private int max;
	private String symbol;
	
	private Level(int level, int max, String symbol) {
		this.level = level;
		this.max = max;
		this.symbol = symbol;
	}

	public int getMax() {
		return this.max;
	}

	public int getLevel() {
		return this.level;
	}
	
	public String getSymbol(){
		return this.symbol;
	}

	public static Level getLevel(int level) {
		Level prank = UNRANKED;
		for (Level rank : values()) {
			if (rank.getLevel() == level) {
				prank = rank;
			}
		}
		return prank;
	}
}
