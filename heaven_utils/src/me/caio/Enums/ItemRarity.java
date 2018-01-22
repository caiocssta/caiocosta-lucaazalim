package me.skater.Enums;

public enum ItemRarity {
	COMUM("§aComum"),
	RARO("§9Raro"),
	EPICO("§5Epico"),
	LENDARIO("§6Lendário");

	private String s;

	private ItemRarity() {
		this.s = "";
	}

	private ItemRarity(String s) {
		this.s = s;
	}

	public String getName() {
		return this.s;
	}

	public String getColor() {
		return this.s.substring(0, 2);
	}
}
