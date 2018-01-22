package me.skater.titles;

import org.bukkit.ChatColor;

public enum Title {
	NENHUM(0, ChatColor.GREEN + "Nenhum"),
	LEVEL(1, ChatColor.GREEN + "Level"),
	RANK(2, ChatColor.GREEN + "Rank"),
	STARTER(3, ChatColor.GREEN + "Starter"),
	SEMIPRO(4, ChatColor.GREEN + "Semi-Pro"),
	PRO(5, ChatColor.GREEN + "Pro"),
	VETERAN(6, ChatColor.GREEN + "Veteran"),
	EXPERT(7, ChatColor.GREEN + "Expert"),
	MASTER(8, ChatColor.GREEN + "Master"),
	LEGEND(9, ChatColor.GREEN + "Legend"),
	COPA(10, ChatColor.GREEN + "Copa"),
	CLAN(11, ChatColor.GREEN + "Clan");

	private int id;
	private String prefix;

	private Title(int id, String prefix) {
		this.id = id;
		this.prefix = prefix;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public int getID() {
		return this.id;
	}

	public static Title getTitleByID(int id) {
		Title ptitle = STARTER;
		for (Title title : values()) {
			if (title.getID() == id) {
				ptitle = title;
			}
		}
		return ptitle;
	}
}
