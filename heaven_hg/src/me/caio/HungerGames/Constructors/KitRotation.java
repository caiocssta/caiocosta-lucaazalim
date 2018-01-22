package me.caio.HungerGames.Constructors;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.caio.HungerGames.Main;

public class KitRotation {
	private List<String> freeKits;
	private List<String> vipKits;
	private List<String> mvpKits;
	private Main main;

	@SuppressWarnings("unchecked")
	public KitRotation(Main main) {
		this.main = main;
		this.freeKits = new ArrayList<String>();
		this.vipKits = new ArrayList<String>();
		this.mvpKits = new ArrayList<String>();
		freeKits.add("surprise");
		List<String> allKits = (ArrayList<String>) main.kit.kits.clone();
		for (int i = 0; i < 4; i++) {
			String kit = (String) allKits.get(new Random().nextInt(allKits.size()));
			this.freeKits.add(kit);
			allKits.remove(kit);
		}
		for (int i = 0; i < 5; i++) {
			String kit = (String) allKits.get(new Random().nextInt(allKits.size()));
			this.vipKits.add(kit);
			allKits.remove(kit);
		}
		for (int i = 0; i < 5; i++) {
			String kit = (String) allKits.get(new Random().nextInt(allKits.size()));
			this.mvpKits.add(kit);
			allKits.remove(kit);
		}
	}

	public List<String> getFreeKits() {
		return this.freeKits;
	}

	public List<String> getVipKits() {
		return this.vipKits;
	}

	public List<String> getMvpKits() {
		return this.mvpKits;
	}

	public void aplicarRotacao() {
		this.main.connection.SQLQuery("TRUNCATE KitRotation");
		for (String str : this.freeKits) {
			this.main.connection.SQLQuery("INSERT INTO `kitrotation`(`Rank`, `Kits`) VALUES ('normal','" + str + "');");
		}
		for (String str : this.vipKits) {
			this.main.connection.SQLQuery("INSERT INTO `kitrotation`(`Rank`, `Kits`) VALUES ('vip','" + str + "');");
		}
		for (String str : this.mvpKits) {
			this.main.connection.SQLQuery("INSERT INTO `kitrotation`(`Rank`, `Kits`) VALUES ('mvp','" + str + "');");
		}
		this.main.kit.loadFreeKits();
	}

	public String getMessage() {
		StringBuilder builder = new StringBuilder();
		builder.append("§c§m----------------------------");
		builder.append("\n");
		builder.append("§f§l    Rotação de Kits");
		builder.append("\n");
		builder.append(" ");
		builder.append("\n");
		builder.append("§6▪ §7Free§8: ");
		for (String s : this.freeKits) {
			builder.append("§f" + s + "§6,§f ");
		}
		builder.append("\n");
		builder.append("§6▪ §aVip§8: ");
		for (String s : this.vipKits) {
			builder.append("§f" + s + "§6,§f ");
		}
		builder.append("\n");
		builder.append("§6▪ §9Mvp§8: ");
		for (String s : this.mvpKits) {
			builder.append("§f" + s + "§6,§f ");
		}
		builder.append("\n");
		builder.append(" ");
		builder.append("\n");
		builder.append("§6▪ §fUse §6/rotation aceitar §fpara aplicar.");
		builder.append("\n");
		builder.append("§6▪ §fUse §e/rotation gerar §fpara gerar novamente.");
		builder.append("\n");
		builder.append("§c§m----------------------------");
		return builder.toString();
	}
}
