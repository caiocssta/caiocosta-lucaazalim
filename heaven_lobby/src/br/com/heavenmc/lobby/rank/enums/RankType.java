package br.com.wombocraft.lobby.rank.enums;

import java.util.Arrays;
import java.util.List;

import br.com.wombocraft.lobby.rank.tag.Tag;


public enum RankType {

	NORMAL("Normal", "§7", 1, Arrays.asList(""), Tag.NORMAL, 4, 2, 0, 0, 5), 
	VIP("Vip", "§aLIGHT ", 2, Arrays.asList(""), Tag.VIP, 10, 5, 10, 0.5,20), 
	MVP("Mvp", "§9MVP ", 3, Arrays.asList(""), Tag.MVP, 15, 10, 20, 1.5,30), 
	PRO("Pro", "§6PRO ",4, Arrays.asList(""), Tag.PRO, 20, 15, 30, 1.5,40), 
	BETA("Beta", "§dBETA ", 5, Arrays.asList(""), Tag.BETA, 25, 25, 40, 2.0,50),
	YOUTUBER("Youtuber", "§bYT ", 6, Arrays.asList(""), Tag.YOUTUBER, 25, 25, 40, 2.0,50), 
	TRIAL("Trial", "§5TRIAL ", 7, Arrays.asList(""), Tag.TRIAL, 25, 25, 40, 2.0, 50), 
	MOD("Mod", "§5§oMOD ", 8, Arrays.asList(""), Tag.MOD, 25, 25, 40, 2.0,50), 
	ADMIN("Admin", "§cADMIN§c ", 9, Arrays.asList(""), Tag.ADMIN, 25, 25, 40, 2.0, 50), 
	DONO("Dono", "§4§lDONO§4 ", 10, Arrays.asList("*"), Tag.DONO, 25, 25, 40, 2.0, 50);

	private String name;
	private String color;
	private Integer level;
	private Tag tag;
	private List<String> permissions;
	private int maxhomes;
	private int maxmercado;
	private int desconto;
	private double expBonus;
	private int chance;
	
	private RankType(String name, String colorCode, int level, List<String> permissions, Tag tag, int maxhomes,
			int maxmercados, int desconto, double expBonus, int chance) {
		this.name = name;
		this.color = colorCode;
		this.level = level;
		this.permissions = permissions;
		this.tag = tag;
		this.maxhomes = maxhomes;
		this.maxmercado = maxmercados;
		this.desconto = desconto;
		this.expBonus = expBonus;
		this.chance = chance;
	}
	
	public int getChance(){
		return this.chance;
	}

	public double getExpBonus() {
		return this.expBonus;
	}

	public int getDesconto() {
		return this.desconto;
	}

	public int getMaxMercadoItems() {
		return this.maxmercado;
	}

	public int getMaxHomes() {
		return this.maxhomes;
	}

	public Tag getTag() {
		return this.tag;
	}

	public String getColor() {
		return this.color;
	}

	public List<String> getPermissions() {
		return this.permissions;
	}

	public String getRankName() {
		return this.color + this.name;
	}

	public String getName() {
		return this.name;
	}

	public int getLevel() {
		return this.level;
	}

	public boolean hasRank(RankType rt) {
		if (getLevel() >= rt.getLevel()) {
			return true;
		}
		return false;
	}

}
