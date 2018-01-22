package me.skater.clans;

public class ClanStatus {

	private int kills;
	private int deaths;
	private int wins;
	private int coins;
	private int elo;

	public ClanStatus(int kills, int deaths, int wins, int coins, int elo) {
		this.kills = kills;
		this.deaths = deaths;
		this.wins = wins;
		this.coins = coins;
		this.elo = elo;
	}

	public int getMoney() {
		return this.coins;
	}

	public int getElo() {
		return this.elo;
	}

	public int getKills() {
		return this.kills;
	}

	public int getDeaths() {
		return this.deaths;
	}

	public int getWins() {
		return this.wins;
	}

	public void setMoney(int money) {
		this.coins = money;
	}

	public void setElo(int elo) {
		this.elo = elo;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public void setDeaths(int deaths) {
		this.kills = deaths;
	}

	public void setWins(int wins) {
		this.kills = wins;
	}

}
