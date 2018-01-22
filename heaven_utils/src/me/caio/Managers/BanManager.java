package me.skater.Managers;

import java.util.UUID;

import me.skater.Main;

import org.bukkit.ChatColor;

public class BanManager {

	public Main m;

	public BanManager(Main main) {
		this.m = main;
	}

	public String getBanMessage(UUID uuid, String by, String reason) {
		return ChatColor.RED + "Sua conta foi suspensa permanentemente por " + by + "\n§eMotivo: " + reason + "\n§6Compre seu unban em: §floja.heavenmc.com.br";
	}

	public String getTempBanMessage(UUID uuid, String reason, String time, String by) {
		return ChatColor.RED + "Sua conta foi suspensa temporariamente por " + by + "\n§eMotivo: " + reason + "\n§aTempo restante:§e " + time + "\n§6Compre seu unban em: §floja.heavenmc.com.br";
	}

	public String getStatsTime(Integer input) {
		int month = (int) Math.floor(input / 2592000);
		int days = (int) Math.floor((input % 2592000) / 86400);
		int hours = (int) Math.floor(((input % 2592000) % 3600) / 3600);
		int minutes = (int) Math.floor(((input % 2592000) % 3600) / 60);
		int seconds = (int) Math.floor(((input % 2592000) % 3600) % 60);

		String sec = seconds + "s" + (seconds > 1 ? "" : "");
		String min = minutes + "m" + (minutes > 1 ? "" : "");
		String hora = hours + "h" + (hours > 1 ? "" : "");
		String dia = days + "d" + (days > 1 ? "" : "");
		String mes = month + "m" + (month > 1 ? "" : "");

		if (month > 0) {
			return mes + (days > 0 ? " " + dia : "") + (hours > 0 ? " " + hora : "") + (minutes > 0 ? " " + min : "") + (seconds > 0 ? " e " + sec : "");
		} else if (days > 0) {
			return dia + (hours > 0 ? " " + hora : "") + (minutes > 0 ? " " + min : "") + (seconds > 0 ? " e " + sec : "");
		} else if (hours > 0) {
			return hora + (minutes > 0 ? " " + min : "") + (seconds > 0 ? " e " + sec : "");
		} else if (minutes > 0) {
			return min + (seconds > 0 ? " e " + sec : "");
		} else {
			return sec;
		}
	}
}
