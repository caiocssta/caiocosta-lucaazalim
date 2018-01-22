package br.com.wombocraft.lobby.server;

import br.com.wombocraft.lobby.LobbyManager;
import br.com.wombocraft.lobby.LobbyPlugin;

public class ServerManager extends LobbyPlugin {

	private int _totalplayers;
	private int _hgplayers;
	private int _teamhgplayers;
	private int _ultrahgplayers;
	private int _pvpplayers;
	private int _lobbyplayers;

	public ServerManager(LobbyManager lbm) {
		super("Server Manager", lbm);
		this._totalplayers = 0;
		this._hgplayers = 0;
		this._teamhgplayers = 0;
		this._ultrahgplayers = 0;
		this._pvpplayers = 0;
		this._lobbyplayers = 0;
	}

	public int getTotalPlayers() {
		return this._totalplayers;
	}

	public int getHGPlayer() {
		return this._hgplayers;
	}

	public int getTeamHGPlayer() {
		return this._teamhgplayers;
	}

	public int getUltraHGPlayer(){
		return this._ultrahgplayers;
	}
	
	public int getPvPPlayers() {
		return this._pvpplayers;
	}

	public void updateTotalPlayer() {
		this._totalplayers = this._pvpplayers + this._hgplayers + this._teamhgplayers + _lobbyplayers
				+ this._ultrahgplayers;
	}

	public void setHGPlayers(int p) {
		this._hgplayers = p;
	}

	public void setTHGPlayers(int p) {
		this._teamhgplayers = p;
	}
	public void setUltraHGPlayers(int p) {
		this._teamhgplayers = p;
	}

	public void setPvPPlayers(int p) {
		this._pvpplayers = p;
	}

	public void setLobbyPlayers(int p) {
		this._lobbyplayers = p;
	}
	
	
}
