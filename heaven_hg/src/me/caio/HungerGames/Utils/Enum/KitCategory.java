package me.caio.HungerGames.Utils.Enum;

public enum KitCategory {
	FREE("Kits da Semana"),
	STORE("Loja de Kits"),
	BETA("Kits em Beta"),
	TEAM_ALL("Escolha o primeiro kit"),
	TEAM_OWNED("Escolha o segundo kit"),
	CONFIRM("Comprando");

	String s;

	private KitCategory(String s) {
		this.s = s;
	}

	public String getName() {
		return this.s;
	}

}
