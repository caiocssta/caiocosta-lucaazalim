package br.com.wombocraft.lobby.comando;

import java.util.Collection;

import br.com.wombocraft.lobby.gamer.Gamer;
import br.com.wombocraft.lobby.rank.enums.RankType;

public abstract interface IComando {
	public abstract void SetComandoCentro(ComandoManager comando);

	public abstract void Executar(Gamer gamer, String[] strings);

	public abstract Collection<String> getComandoFormas();

	public abstract void SetComando(String paramString);

	public abstract RankType GetRankRequerido();
}
