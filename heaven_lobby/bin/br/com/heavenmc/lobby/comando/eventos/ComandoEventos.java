package br.com.wombocraft.lobby.comando.eventos;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import br.com.wombocraft.lobby.Lobby;
import br.com.wombocraft.lobby.comando.ComandoManager;
import br.com.wombocraft.lobby.comando.IComando;
import br.com.wombocraft.lobby.gamer.Gamer;
import br.com.wombocraft.lobby.utils.cd.Cooldown;

public class ComandoEventos implements Listener {

	@EventHandler
	public void OnPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		String comandoNome = event.getMessage().substring(1);
		String[] args = null;
		if (comandoNome.contains(" ")) {
			comandoNome = comandoNome.split(" ")[0];
			args = event.getMessage().substring(event.getMessage().indexOf(' ') + 1).split(" ");
		}
		Gamer g = Lobby.getLobbyManager().getGamerManager().getGamerByUUID(event.getPlayer().getUniqueId());
		ComandoManager cm = ComandoManager.inicializar(Lobby.getLobbyManager());

		IComando command = (IComando) cm.getComandos().get(comandoNome.toLowerCase());

		if (command != null) {

			if (Cooldown.isInCooldown(event.getPlayer().getUniqueId(), "Command")) {
				event.getPlayer().sendMessage("§cAguarde para usar um novo comando");
				event.setCancelled(true);
				return;
			}
			new Cooldown(event.getPlayer().getUniqueId(), "Command", 1).start();
			if (!g.getRank().hasRank(command.GetRankRequerido())) {
				event.getPlayer()
						.sendMessage("§cVocê não possui permissão. Para saber mais acesse: §aloja.heavenmc.com.br");
				event.setCancelled(true);
				return;
			}
			command.SetComando(comandoNome.toLowerCase());
			command.Executar(g, args);

			event.setCancelled(true);
		}
	}

}
