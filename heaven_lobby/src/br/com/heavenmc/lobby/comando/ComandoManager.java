package br.com.wombocraft.lobby.comando;

import br.com.wombocraft.lobby.Lobby;
import br.com.wombocraft.lobby.LobbyManager;
import br.com.wombocraft.lobby.comando.eventos.ComandoEventos;
import br.com.wombocraft.lobby.utils.data.NautHashMap;

public class ComandoManager {
	protected NautHashMap<String, IComando> _comandos;
	public static ComandoManager Instance;

	public static ComandoManager inicializar(LobbyManager lbm) {
		if (Instance == null) {
			Instance = new ComandoManager(lbm);
		}
		return Instance;
	}

	private ComandoManager(LobbyManager lbm) {
		this._comandos = new NautHashMap<>();
		Lobby.getLobby().getServer().getPluginManager().registerEvents(new ComandoEventos(),
				Lobby.getLobby());
	}

	public void addComando(IComando comando) {
		for (String c : comando.getComandoFormas()) {
			this._comandos.put(c.toLowerCase(), comando);
			comando.SetComandoCentro(this);
		}
	}

	public NautHashMap<String, IComando> getComandos() {
		return this._comandos;
	}

	public void removerComando(IComando comando) {
		for (String c : comando.getComandoFormas()) {
			this._comandos.remove(c.toLowerCase());
			comando.SetComandoCentro(null);
		}
	}
}
