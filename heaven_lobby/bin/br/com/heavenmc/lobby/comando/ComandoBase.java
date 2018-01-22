package br.com.wombocraft.lobby.comando;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import br.com.wombocraft.lobby.Lobby;
import br.com.wombocraft.lobby.LobbyPlugin;
import br.com.wombocraft.lobby.rank.enums.RankType;

public abstract class ComandoBase<PluginType extends LobbyPlugin> implements IComando, TabCompleter {

	public Lobby m;
	private RankType _rankRequerido;
	private List<String> _comandosformas;
	protected String _comandousado;
	protected ComandoManager _comando;
	protected PluginType Plugin;

	public ComandoBase(PluginType pt, RankType rank, String... args) {
		this._rankRequerido = rank;
		this._comandosformas = Arrays.asList(args);
		this.m = Lobby.getLobby();
		this.Plugin = pt;
	}

	public Lobby getLobby() {
		return this.m;
	}

	public Collection<String> getComandoFormas() {
		return this._comandosformas;
	}

	public void SetComando(String commando) {
		this._comandousado = commando;
	}

	public String getComandoUsado() {
		return this._comandousado;
	}

	public RankType GetRankRequerido() {
		return this._rankRequerido;
	}

	public void SetComandoCentro(ComandoManager comando) {
		this._comando = comando;
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		return null;
	}

}
