package me.caio.HungerGames.Utils;

import me.caio.HungerGames.Main;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MinecraftFont;

public class Map extends MapRenderer {
	private Main m;

	public Map(Main m) {
		this.m = m;
	}

	public void render(MapView view, MapCanvas canvas, Player p) {
		if (p.getUniqueId() != this.m.winner) {
			return;
		}
		canvas.drawText(40, 9, MinecraftFont.Font, "Parabéns!");
		canvas.drawText(9, 19, MinecraftFont.Font, "Você venceu o torneio!");
		canvas.drawText(getX(p), 29, MinecraftFont.Font, p.getName());
	}

	private int getX(Player p) {
		int i = 0;
		int name = p.getName().length();
		if (name == 16) {
			i = 22;
		}
		if (name == 15) {
			i = 25;
		}
		if (name == 14) {
			i = 28;
		}
		if (name == 13) {
			i = 30;
		}
		if (name == 12) {
			i = 33;
		}
		if (name == 11) {
			i = 36;
		}
		if (name == 10) {
			i = 38;
		}
		if (name == 9) {
			i = 41;
		}
		if (name == 8) {
			i = 44;
		}
		if (name == 7) {
			i = 46;
		}
		if (name == 6) {
			i = 49;
		}
		if (name == 5) {
			i = 52;
		}
		if (name == 4) {
			i = 54;
		}
		if (name == 3) {
			i = 57;
		}
		if (name == 2) {
			i = 60;
		}
		if (name == 1) {
			i = 62;
		}
		return i;
	}
}
