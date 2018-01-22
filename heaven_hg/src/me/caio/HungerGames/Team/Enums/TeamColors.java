package me.caio.HungerGames.Team.Enums;

import org.bukkit.Color;

public enum TeamColors {
	AQUA(Color.fromRGB(255, 117, 177), "Aqua"),
	PRETO(Color.fromRGB(255, 133, 133), "Preto"),
	BLUE(Color.fromRGB(255, 66, 66), "Azul"),
	FUCHSIA(Color.fromRGB(255, 74, 240), "1"),
	GRAY(Color.fromRGB(186, 112, 255), "Cinza"),
	GREEN(Color.fromRGB(134, 112, 225), "Verde"),
	LIMA(Color.fromRGB(112, 162, 255), "Lima"),
	MAROON(Color.fromRGB(112, 229, 255), "Marron"),
	NAVY(Color.fromRGB(112, 255, 212), "2"),
	OLIVE(Color.fromRGB(112, 255, 124), "3"),
	ORANGE(Color.fromRGB(191, 255, 112), "Laranja"),
	PURPLE(Color.fromRGB(255, 236, 112), "Roxo"),
	RED(Color.fromRGB(255, 160, 112), "Vermelho"),
	SILVER(Color.fromRGB(255, 112, 112), "Prata"),
	TEAL(Color.fromRGB(255, 0, 0), "3"),
	WHITE(Color.fromRGB(255, 0, 115), "Branco"),
	YELLOW(Color.fromRGB(255, 0, 208), "Amarelo"),
	A(Color.fromRGB(200, 0, 255), "Amarelo"),
	B(Color.fromRGB(98, 0, 255), "Amarelo"),
	C(Color.fromRGB(13, 0, 225), "Amarelo");

	Color c;
	String name;

	TeamColors(Color color, String name) {
		this.c = color;
		this.name = name;
	}

	public Color getColor() {
		return this.c;
	}

	public String getNome() {
		return this.name;
	}

}
