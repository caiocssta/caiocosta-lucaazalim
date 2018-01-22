package br.com.wombocraft.lobby.rank.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import br.com.wombocraft.lobby.LobbyManager;
import br.com.wombocraft.lobby.LobbyPlugin;
import br.com.wombocraft.lobby.gamer.Gamer;

public class TagManager extends LobbyPlugin {
	private HashMap<Tag, List<Player>> tags;

	public TagManager(final LobbyManager hcm) {
		super("Tag Manager", hcm);
		this.tags = new HashMap<>();
		for (Tag tag : Tag.values()) {
			this.tags.put(tag, new ArrayList<Player>());
		}
		new BukkitRunnable() {
			public void run() {
				for (Gamer g : hcm.getGamerManager().getGamers()) {
					addPlayerTag(g.getPlayer(), g.getRank().getTag());
				}
			}
		}.runTaskLater(hcm.getLobby(), 11L);
	}

	public String getName(String kit) {
		char[] stringArray = kit.toLowerCase().toCharArray();
		stringArray[0] = Character.toUpperCase(stringArray[0]);
		return new String(stringArray);
	}

	public Scoreboard getPlayerScoreboard(Player player) {
		Scoreboard board = player.getScoreboard();
		if (board == null) {
			board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
			player.setScoreboard(board);
		}
		return board;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addPlayerTag(Player player, Tag tag) {
		removePlayerTag(player);
		player.setDisplayName(tag.getPrefix() + player.getName() + ChatColor.RESET);
		Scoreboard board = getPlayerScoreboard(player);
		for (Tag teamTag : Tag.values()) {
			Team team = board.getTeam(teamTag.getTeamName());
			if (team == null) {
				team = board.registerNewTeam(teamTag.getTeamName());
				team.setPrefix(teamTag.getPrefix());
				team.setSuffix(ChatColor.RESET + "");
				team.setCanSeeFriendlyInvisibles(false);
				team.setDisplayName(getName(teamTag.getTeamName().substring(1, team.getName().length())));
			}
		}
		List<Player> playerList = (List) this.tags.get(tag);
		playerList.add(player);
		this.tags.put(tag, playerList);
		for (Iterator localIterator1 = this.tags.entrySet().iterator(); localIterator1.hasNext();) {
			Object entry = (Map.Entry) localIterator1.next();
			Tag tagteam = (Tag) ((Map.Entry) entry).getKey();
			List<Player> players = (List) ((Map.Entry) entry).getValue();
			Team team = board.getTeam(tagteam.getTeamName());
			for (Player participante : players) {
				if (team != null) {
					team.addPlayer(participante);
				}
				Scoreboard playerBoard = getPlayerScoreboard(participante);
				Team playerTeam = playerBoard.getTeam(tag.getTeamName());
				if (playerTeam != null) {
					playerTeam.addPlayer(player);
				}
			}
		}
	}

	public Tag getPlayerActiveTag(Player p) {
		Scoreboard playerBoard = getPlayerScoreboard(p);
		Team playerTeam = playerBoard.getPlayerTeam(p);
		if (playerTeam != null) {
			return Tag.valueOf(playerTeam.getName().toUpperCase().substring(1, playerTeam.getName().length()));
		}
		return Tag.NORMAL;
	}

	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
	public void removePlayerTag(Player player) {
		for (Player participante : Bukkit.getServer().getOnlinePlayers()) {
			Scoreboard playerBoard = getPlayerScoreboard(participante);
			Team playerTeam = playerBoard.getPlayerTeam(player);
			if (playerTeam != null) {
				playerTeam.removePlayer(player);
			}
			participante.setScoreboard(playerBoard);
		}
		for (Map.Entry<Tag, List<Player>> entry : this.tags.entrySet()) {
			Object players = (List) entry.getValue();
			((List) players).remove(player);
			this.tags.put((Tag) entry.getKey(), (List<Player>) players);
		}
	}

	public String getTagName(Player p) {

		return getPlayerActiveTag(p).getColor() + getName(
				getPlayerActiveTag(p).getTeamName().substring(1, getPlayerActiveTag(p).getTeamName().length()));
	}
}
