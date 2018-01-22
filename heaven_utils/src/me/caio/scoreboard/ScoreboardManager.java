package me.skater.scoreboard;

import java.util.HashMap;
import java.util.UUID;

import me.skater.Main;
import me.skater.Management;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardManager extends Management {
	private HashMap<UUID, Scoreboard> playerBoards;

	public ScoreboardManager(Main main) {
		super(main);
	}

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new QuitListener(this), getPlugin());
		this.playerBoards = new HashMap<UUID, Scoreboard>();
	}

	public Scoreboard getPlayerScoreboard(Player player) {
		Scoreboard board = this.playerBoards.get(player.getUniqueId());
		if (board == null) {
			board = getServer().getScoreboardManager().getNewScoreboard();
			this.playerBoards.put(player.getUniqueId(), board);
		}
		player.setScoreboard(board);
		return board;
	}

	public void removeScoreboard(Player player) {
		this.playerBoards.remove(player.getUniqueId());
	}

	@Override
	public void onDisable() {
		for (Player player : getServer().getOnlinePlayers()) {
			removeScoreboard(player);
		}
		this.playerBoards.clear();
	}
}
