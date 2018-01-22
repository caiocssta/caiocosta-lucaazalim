package br.com.wombocraft.lobby.gamer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.wombocraft.lobby.LobbyManager;
import br.com.wombocraft.lobby.LobbyPlugin;

public class GamerManager extends LobbyPlugin {
	private List<Gamer> _gamers;

	public GamerManager(LobbyManager hcm) {
		super("Gamer Manager", hcm);
		this._gamers = new ArrayList<>();

		RegisterEvents(new GamerListener(this));
	}

	public List<Gamer> getGamers() {
		List<Gamer> gamers = new ArrayList<>();
		for (Gamer g : this._gamers) {
			if (g.getPlayer() != null) {
				if (g.getPlayer().isOnline()) {
					gamers.add(g);
				}
			}
		}
		return gamers;
	}

	public void addGamer(Gamer g) {
		if (!this._gamers.contains(g)) {
			this._gamers.add(g);
		}
	}

	public Gamer getGamerByUUID(UUID uuid) {
		for (Gamer g : this._gamers) {
			if (uuid == g.getUUID()) {
				return g;
			}
		}
		return null;
	}

	public Gamer getGamerByName(String name) {
		for (Gamer g : this._gamers) {
			if (name.equals(g.getName())) {
				return g;
			}
		}
		return null;
	}

	public void removeGamer(Gamer g) {
		this._gamers.remove(g);
	}

}
