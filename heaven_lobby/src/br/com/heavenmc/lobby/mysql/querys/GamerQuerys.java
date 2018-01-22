package br.com.wombocraft.lobby.mysql.querys;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import br.com.wombocraft.lobby.gamer.Gamer;
import br.com.wombocraft.lobby.gamer.GamerLoginResult;
import br.com.wombocraft.lobby.gamer.enums.GamerLoginResultType;
import br.com.wombocraft.lobby.mysql.MysqlManager;
import br.com.wombocraft.lobby.rank.enums.RankType;

public class GamerQuerys extends IQuery {

	public GamerQuerys(MysqlManager mm) {
		super(mm);
	}

	public GamerLoginResult loadGamer(String name, UUID uuid) throws SQLException {
		UUID pUUID = uuid;
		String tUUID = pUUID.toString().replace("-", "");

		String selectplayerquery = "SELECT heaven_utils.ranks.uuid, heaven_utils.ranks.rank FROM heaven_utils.ranks WHERE heaven_utils.ranks.uuid = '" + tUUID + "';";

		Statement stmt = getCon().createStatement();
		ResultSet resultGamer = stmt.executeQuery(selectplayerquery);
		Gamer g = null;
		if (resultGamer.next()) {
			RankType r = (resultGamer.getString("rank") == null) ? RankType.NORMAL
					: RankType.valueOf(resultGamer.getString("rank").toUpperCase());
			g = new Gamer(name, uuid, r);
			resultGamer.close();
		} else {
			g = new Gamer(name, uuid, RankType.NORMAL);
			resultGamer.close();
		}
		stmt.close();
		return new GamerLoginResult(g, GamerLoginResultType.ALLOW) {
			@Override
			public void post(AsyncPlayerPreLoginEvent event) {
				event.allow();
			}
		};
	}

}
