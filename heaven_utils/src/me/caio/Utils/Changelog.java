package me.skater.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import me.skater.Main;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class Changelog {

	private Main m;
	private ItemStack paper;
	private ArrayList<String> lore;
	private HashMap<Integer, String> messages;

	public Changelog(Main m) {
		this.m = m;
		loadChangelog();
	}

	public void loadChangelog() {
		this.messages = new HashMap<>();
		this.paper = new ItemStack(Material.PAPER);
		final ItemMeta meta = paper.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Atualizações");
		lore = new ArrayList<>();
		lore.add(" ");

		new BukkitRunnable() {

			@Override
			public void run() {
				try {
					PreparedStatement stmt = m.mainConnection.prepareStatement("SELECT * FROM changelog;");
					ResultSet result = stmt.executeQuery();
					while (result.next()) {
						lore.add("§6• §r" + result.getString("changes"));
						messages.put(result.getInt("ID"), result.getString("changes"));
					}
					lore.add("");
					meta.setLore(lore);
					paper.setItemMeta(meta);
				} catch (SQLException e) {
				}

			}
		}.runTaskAsynchronously(m);
	}

	public void getActiveMessages(Player p) {
		for (Integer i : messages.keySet()) {
			String msg = messages.get(i);
			p.sendMessage("ID: " + i + " " + msg);
		}
	}

	public void addMessage(String name) {
		m.connect.SQLQuerySync("INSERT INTO `changelog`(`changes`) VALUES ('" + name + "');");
	}

	public void removeMessage(int id) {
		m.connect.SQLQuerySync("DELETE FROM `changelog` WHERE ID='" + id + "';");

	}

	public void resetChangelog() {
		m.connect.SQLQuerySync("TRUNCATE changelog;");
	}

	public void sendChangelogMessage(Player p) {
		for (String s : lore) {
			p.sendMessage(s);
		}
	}

	public ItemStack getChangelogStack() {
		return this.paper;
	}

}
