package br.com.wombocraft.lobby.gamer;

import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import br.com.wombocraft.lobby.gamer.enums.GamerLoginResultType;

public abstract class GamerLoginResult {

	private Gamer _g;
	private GamerLoginResultType _gamerLoginResult;

	public GamerLoginResult(Gamer g, GamerLoginResultType result) {
		this._g = g;
		this._gamerLoginResult = result;
	}

	public Gamer getGamer() {
		return this._g;
	}

	public GamerLoginResultType getResult() {
		return this._gamerLoginResult;
	}

	public abstract void post(AsyncPlayerPreLoginEvent event);

}
