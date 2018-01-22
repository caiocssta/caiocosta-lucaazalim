package me.caio.bungeecord.socket;

import java.util.List;

public class StatusResponse {
	private String description;
	private Players players;
	private Version version;
	private String favicon;
	private int time;

	public class Player {
		@SuppressWarnings("unused")
		private String name;
		@SuppressWarnings("unused")
		private String id;

		public Player() {
		}
	}

	public class Players {
		private int max;
		private int online;
		@SuppressWarnings("unused")
		private List<StatusResponse.Player> sample;

		public Players() {
		}

		public int getMax() {
			return this.max;
		}

		public int getOnline() {
			return this.online;
		}
	}

	public String getDescription() {
		return this.description;
	}

	public Players getPlayers() {
		return this.players;
	}

	public Version getVersion() {
		return this.version;
	}

	public String getFavicon() {
		return this.favicon;
	}

	public int getTime() {
		return this.time;
	}

	public class Version {
		@SuppressWarnings("unused")
		private String name;
		@SuppressWarnings("unused")
		private String protocol;

		public Version() {
		}
	}
}
