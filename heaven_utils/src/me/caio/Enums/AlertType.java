package me.skater.Enums;

public enum AlertType {
	AutoSoup("§c");

	String color;

	private AlertType(String cor) {
		this.color = cor;
	}

	public String getColor() {
		return this.color;
	}
}
