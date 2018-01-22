package me.caio.bungeecord.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import me.caio.bungeecord.Main;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MotdUtils {

	private HashMap<String, ArrayList<String>> motds;
	private HashMap<Integer, String> motds_ids;
	private HashMap<Integer, String> servers_ids;

	ArrayList<String> servers;

	private Main m;

	public MotdUtils(Main m) {
		this.m = m;
		this.motds = new HashMap<String, ArrayList<String>>();
		this.motds_ids = new HashMap<>();
		this.servers_ids = new HashMap<>();
		this.servers = new ArrayList<String>();
		this.servers.add("heavenmc.com.br");
		this.servers.add("hg.heavenmc.com.br");
		// this.servers.add("teamhg.wombocraft.com.br");
		// this.servers.add("pvp.wombocraft.com.br");
		// this.servers.add("copa.wombocraft.com.br");
		// this.servers.add("minicopa.wombocraft.com.br");
		// this.servers.add("ultrahg.wombocraft.com.br");
		this.loadMotds();
	}

	public void loadMotds() {
		m.getProxy().getScheduler().runAsync(m, new Runnable() {
			@Override
			public void run() {
				motds.clear();
				motds_ids.clear();
				servers_ids.clear();
				try {
					PreparedStatement stmt = null;
					ResultSet result = null;
					for (String server : servers) {
						stmt = m.mainConnection.prepareStatement("SELECT * FROM motd WHERE server='" + server + "';");
						result = stmt.executeQuery();
						ArrayList<String> motd = new ArrayList<String>();
						while (result.next()) {
							motd.add(result.getString("motd"));
							motds_ids.put(result.getInt("ID"), result.getString("motd"));
							servers_ids.put(result.getInt("ID"), result.getString("server"));

						}
						motds.put(server, motd);
					}
					result.close();
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Erro ao carregar motds");
				}
			}
		});
	}

	public void reloadMotds() {
		m.getProxy().getScheduler().runAsync(m, new Runnable() {
			@Override
			public void run() {
				motds.clear();
				motds_ids.clear();
				servers_ids.clear();

				try {
					PreparedStatement stmt = null;
					ResultSet result = null;
					for (String server : servers) {
						stmt = m.mainConnection.prepareStatement("SELECT * FROM motd WHERE server='" + server + "';");
						result = stmt.executeQuery();
						ArrayList<String> motd = new ArrayList<String>();
						while (result.next()) {
							motd.add(result.getString("motd"));
							motds_ids.put(result.getInt("ID"), result.getString("motd"));
							servers_ids.put(result.getInt("ID"), result.getString("server"));

						}
						motds.put(server, motd);
					}
					result.close();
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Erro ao carregar motds");
				}
			}
		});
	}

	public void addMotd(String server, String name) {
		m.connect.SQLQuerySyncNoLock("INSERT INTO `motd`(`server`, `motd`) VALUES ('" + server + "', '" + name + "');");

	}

	public void removeMotd(int id) {
		m.connect.SQLQuerySyncNoLock("DELETE FROM `motd` WHERE `ID`='" + id + "';");
	}

	@SuppressWarnings("deprecation")
	public void getMotds(ProxiedPlayer p) {
		for (Integer i : motds_ids.keySet()) {
			String motd = motds_ids.get(i);
			p.sendMessage("ID: " + i + " " + motd + " §7[§c" + filter(getServerByMotd(i)) + "§7]");
		}
	}

	public String getRandomMotd(String server) {
		ArrayList<String> motds = new ArrayList<>();
		for (String motd : this.motds.get(server)) {
			motds.add(motd);
		}
		return motds.get(new Random().nextInt(motds.size()));
	}

	public String filter(String s) {
		if (s.contains("teamhg")) {
			return "TEAMHG";
		} else
			return s.substring(0, 2).toUpperCase();
	}

	public String getServerByMotd(int id) {
		return this.servers_ids.get(id);
	}

}
