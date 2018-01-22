package br.com.wombocraft.lobby.admin.comando;

import org.bukkit.entity.Player;

import br.com.wombocraft.lobby.admin.AdminManager;
import br.com.wombocraft.lobby.comando.ComandoBase;
import br.com.wombocraft.lobby.gamer.Gamer;
import br.com.wombocraft.lobby.rank.enums.RankType;

public class AdminComando extends ComandoBase<AdminManager> {

	public AdminComando(AdminManager adm) {
		super(adm, RankType.TRIAL, new String[] { "adm", "admin" });
	}

	@Override
	public void Executar(Gamer gamer, String[] strings) {
		Player p = gamer.getPlayer();
		AdminManager adm = ((AdminManager) this.Plugin);
		if (adm.isAdmin(p)) {
			adm.setPlayer(p);
		} else {
			adm.setAdmin(p);
		}
	}
}