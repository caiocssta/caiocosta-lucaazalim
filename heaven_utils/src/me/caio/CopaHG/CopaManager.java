package me.skater.CopaHG;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.caio.HungerGames.Main;
import me.skater.Management;
import me.skater.Commands.Copa;
import me.skater.CopaHG.listeners.LoginListener;

public class CopaManager extends Management {

	public CopaManager(me.skater.Main main) {
		super(main);
	}

	private List<UUID> copaPlayer;

	@Override
	public void onEnable() {
		this.copaPlayer = new ArrayList<>();
		getServer().getPluginManager().registerEvents(new LoginListener(getPlugin()), getPlugin());
		getPlugin().getCommand("copa").setExecutor(new Copa(Main.getInstance()));
	}

	@Override
	public void onDisable() {
		if (this.copaPlayer != null) {
			this.copaPlayer.clear();
		}
	}

	public boolean isParticipating(UUID uuid) {
		return this.copaPlayer.contains(uuid);
	}

	public void loadCopaPlayer(UUID uuid) throws SQLException {
		PreparedStatement stmt = getMySQL().prepareStatement("SELECT player_id from heaven_hungergames.copa_players WHERE player_uuid='" + uuid.toString().replaceAll("-", "") + "'");
		ResultSet result = stmt.executeQuery();
		if (result.next()) {
			this.copaPlayer.add(uuid);
		}
		result.close();
		stmt.close();

	}

}
