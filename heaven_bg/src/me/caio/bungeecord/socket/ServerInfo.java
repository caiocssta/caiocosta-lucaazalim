package me.caio.bungeecord.socket;

import java.net.InetSocketAddress;

public class ServerInfo implements Cloneable {
	private final String name;
	private int playersOnline = 0;
	private int maxPlayers = 0;
	private String motd;
	private boolean online = true;
	private String favicon;
	private final InetSocketAddress address;

	public ServerInfo(String name, InetSocketAddress address) {
		this.name = name;
		this.address = address;
	}

	public ServerInfo clone() {
		try {
			return (ServerInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}

	public int getPlayersOnline() {
		return this.playersOnline;
	}

	public String getName() {
		return this.name;
	}

	public String getFavicon() {
		return this.favicon;
	}

	public int getMaxPlayers() {
		return this.maxPlayers;
	}

	public String getMotd() {
		return this.motd;
	}

	public boolean isOnline() {
		return this.online;
	}

	public InetSocketAddress getAddress() {
		return this.address;
	}

	public void setPlayersOnline(int playersOnline) {
		this.playersOnline = playersOnline;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public void setMotd(String motd) {
		this.motd = motd;
	}

	public void setFavicon(String facivon) {
		this.favicon = facivon;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}
}
