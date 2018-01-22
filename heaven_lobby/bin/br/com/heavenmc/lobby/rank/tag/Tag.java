package br.com.wombocraft.lobby.rank.tag;

import org.bukkit.ChatColor;

public enum Tag {
	NORMAL("lnormal", ChatColor.GRAY.toString(), "§7"),
	SPEC("kspec", ChatColor.DARK_GRAY.toString() + "§l§lSPEC§8 " + ChatColor.DARK_GRAY, "§8"),
	COPA("jcopa", ChatColor.YELLOW.toString() + "§lCOPA§e " + ChatColor.YELLOW, "§e"),
	VIP("ivip", ChatColor.GREEN.toString() + "§lLIGHT§a " + ChatColor.GREEN, "§a"),
	MVP("hmvp", ChatColor.BLUE.toString() + "§lMVP§9 " + ChatColor.BLUE, "§9"),
	PRO("gpro", ChatColor.GOLD.toString() + "§lPRO§6 " + ChatColor.GOLD, "§6"),
	BETA("felite", ChatColor.LIGHT_PURPLE.toString() + "§lBETA§d " + ChatColor.LIGHT_PURPLE, "§d"),
	YOUTUBER("eyoutuber", ChatColor.AQUA.toString() + "§lYT§b " + ChatColor.AQUA, "§b"),
	TRIAL("dtrial", ChatColor.DARK_PURPLE.toString() + "§lTRIAL§5 " + ChatColor.DARK_PURPLE, "§5"),
	MOD("cmod", ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "§lMOD§5§o " + ChatColor.DARK_PURPLE, "§5§o"),
	ADMIN("badmin", ChatColor.RED.toString() + "§lADMIN§c " + ChatColor.RED, "§c"),
	DONO("adono", ChatColor.DARK_RED.toString() + "§lDONO§4 " + ChatColor.DARK_RED, "§4");

	private String teamName;
	private String prefix;
	private String color;

	private Tag(String teamName, String prefix, String color) {
		this.teamName = teamName;
		this.prefix = prefix;
		this.color = color;
	}

	public String getColor() {
		return this.color;
	}

	public String getTeamName() {
		return this.teamName;
	}

	public String getPrefix() {
		return this.prefix;
	}
}
