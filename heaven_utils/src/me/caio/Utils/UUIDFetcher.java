package me.skater.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.skater.Main;

public abstract class UUIDFetcher {
	private ArrayList<String> servers;
	private HashMap<String, Integer> fails;
	private int current;
	private Pattern namePattern;

	public UUIDFetcher() {
		this.servers = new ArrayList<String>();
		this.servers.add("https://api.mojang.com/users/profiles/minecraft/%player-name%#id#name");
		this.servers.add("https://craftapi.com/api/user/uuid/%player-name%#uuid#username");
		this.servers.add("https://us.mc-api.net/v3/uuid/%player-name%#full_uuid#name");

		this.fails = new HashMap<String, Integer>();
		this.current = 0;
		this.namePattern = Pattern.compile("[a-zA-Z0-9_]{1,16}");
	}

	private String getNextServer() {
		if (this.current == this.servers.size() - 1) {
			this.current = 0;
		} else {
			this.current += 1;
		}
		return this.servers.get(this.current);
	}

	public UUID loadFromServers(String name) {
		UUID id = null;
		String server1 = getNextServer();
		id = load(name, server1);
		if (id == null) {
			id = load(name, getNextServer());
			if (id != null) {
				if (this.fails.containsKey(server1)) {
					this.fails.put(server1, Integer.valueOf(this.fails.get(server1).intValue() + 1));
				} else {
					this.fails.put(server1, Integer.valueOf(1));
				}
				Main.getInstance().getLogger().warning(server1 + " falhou em tentar obter a UUID do jogador " + name);
			}
		}
		server1 = null;
		return id;
	}

	public boolean isValidName(String username) {
		if (username.length() < 3) {
			return false;
		}
		if (username.length() > 16) {
			return false;
		}
		Matcher matcher = this.namePattern.matcher(username);
		return matcher.matches();
	}

	public UUID getUUIDFromString(String id) {
		if (id.length() == 36) {
			return UUID.fromString(id);
		}
		if (id.length() == 32) {
			return UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id.substring(20, 32));
		}
		return null;
	}

	public abstract UUID load(String paramString1, String paramString2);

	public abstract UUID getUUID(String paramString);
}
