package br.com.wombocraft.lobby.admin.comando;

import org.bukkit.entity.Player;

import br.com.wombocraft.lobby.admin.AdminManager;
import br.com.wombocraft.lobby.comando.ComandoBase;
import br.com.wombocraft.lobby.gamer.Gamer;
import br.com.wombocraft.lobby.rank.enums.RankType;

public class StaffComando extends ComandoBase<AdminManager> {

	public StaffComando(AdminManager adm) {
		super(adm, RankType.TRIAL, new String[] { "staff" });
	}

	@Override
	public void Executar(Gamer gamer, String[] strings) {
		Player p = gamer.getPlayer();
		AdminManager adm = ((AdminManager) this.Plugin);
		if (!adm._playerschat.contains(p.getUniqueId())) {
			adm._playerschat.add(p.getUniqueId());
			if (adm._playerschat.contains(p.getUniqueId()))
				p.sendMessage("§aVoce entrou no chat da staff");
		} else {
			if (adm._playerschat.contains(p.getUniqueId()))
				p.sendMessage("§cVoce saiu do chat da staff");
			adm._playerschat.remove(p.getUniqueId());
		}
	}
}