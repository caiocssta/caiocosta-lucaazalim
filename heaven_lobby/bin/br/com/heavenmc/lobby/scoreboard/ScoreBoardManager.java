package br.com.wombocraft.lobby.scoreboard;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import br.com.wombocraft.lobby.Lobby;
import br.com.wombocraft.lobby.LobbyManager;
import br.com.wombocraft.lobby.LobbyPlugin;
import br.com.wombocraft.lobby.gamer.Gamer;
import br.com.wombocraft.lobby.rank.enums.RankType;

public class ScoreBoardManager extends LobbyPlugin {

	private int _animation;
	private String _basetext;
	public HashMap<UUID, String> _teamplayers;

	public ScoreBoardManager(LobbyManager hcm) {
		super("Scoreboard Manager", hcm);
		this._basetext = "§e§lHEAVENMC";
		this._teamplayers = new HashMap<>();
		// this.scroller = new StringScroller("WOMBOCRAFT HARDCORE GAMES", 20,
		// 20);
		new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			public void run() {
				// String n = ScoreboardManager.this.scroller.next();
				String n = null;
				n = nextAnimation();

				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.getScoreboard().getObjective("main") != null) {
						p.getScoreboard().getObjective("main").setDisplayName(n);
					}

					if (p.getScoreboard().getObjective("second") != null) {
						p.getScoreboard().getObjective("second").setDisplayName(n);
					}

				}
			}
		}.runTaskTimerAsynchronously(Lobby.getLobby(), 2L, 2L);
	}

	public String nextAnimation() {
		_animation++;
		if (_animation == 110) {
			_basetext = "§e§lHEAVENMC";
			_animation = 10;
		}
		if (_animation == 11) {
			_basetext = "§f§lH§e§lEAVENMC";
		}
		if (_animation == 12) {
			_basetext = "§e§lH§f§lE§e§lAVENMC";
		}
		if (_animation == 13) {
			_basetext = "§e§lHE§f§lA§e§lVENMC";
		}
		if (_animation == 14) {
			_basetext = "§e§lHEA§f§lV§e§lENMC";
		}
		if (_animation == 15) {
			_basetext = "§e§lHEAV§f§lE§e§lNMC";
		}
		if (_animation == 16) {
			_basetext = "§e§lHEAVEN§f§lM§e§lC";
		}
		if (_animation == 17) {
			_basetext = "§e§lHEAVENM§f§lC";
		}
		if (_animation == 18) {
			_basetext = "§e§lHEAVENMC";
		}
		return _basetext;
	}

	public void createScoreboardBase(Gamer g) {
		Player p = g.getPlayer();
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		/*
		 * for (Objective obs : board.getObjectives()) { obs.unregister(); } for
		 * (Team t : board.getTeams()) { t.unregister(); }
		 */
		board.registerNewObjective("clear", "dummy");
		Objective o = board.registerNewObjective("main", "dummy");
		o.setDisplayName("§e§lHEAVENMC");
		int i = 16;
		// ESPACO \\
		o.getScore("§0").setScore(i);
		i--;
		// RANK \\
		o.getScore("§1").setScore(i);
		i--;
		Team rank = board.registerNewTeam("rank");
		rank.setPrefix("Rank§7: ");
		
		if (g.getRank().getColor().equalsIgnoreCase("§7"))
			rank.setSuffix(g.getRank().getColor() + "MEMBRO");
		else
			rank.setSuffix(g.getRank().getColor());
		
		rank.addEntry("§1");
		// CASH \\
		o.getScore("§e").setScore(i);
		i--;
		Team level = board.registerNewTeam("online");
		level.setPrefix("Online§7: ");
		level.setSuffix("§a0");
		level.addEntry("§e");
		// ESPACO \\
		o.getScore("§3").setScore(i);
		i--;
		// SITE \\
		o.getScore("§c").setScore(i);
		i--;
		Team site = board.registerNewTeam("site");
		site.setPrefix("   §eloja.heaven");
		site.setSuffix("§emc.com.br");
		site.addEntry("§c");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		p.setScoreboard(board);
	}

	public void updateRank(Gamer g) {
		Player p = g.getPlayer();
		if (p.getScoreboard().getTeam("rank") != null) {
			Team rank = p.getScoreboard().getTeam("rank");
			RankType rt = g.getRank();
			rank.setSuffix("§7" + rt.getRankName());
		}
	}

	public void updateOnlinePlayer() {
		for (Gamer g : getLobbyManager().getGamerManager().getGamers())
			if (g.getPlayer() != null) {
				Player p = g.getPlayer();
				if (p.getScoreboard().getTeam("online") != null) {
					Team rank = p.getScoreboard().getTeam("online");
					rank.setSuffix("§a" + getLobbyManager().getServerManager().getTotalPlayers());
				}
			}
	}

	public String replaceMoney(Integer coins) {
		DecimalFormat df = new DecimalFormat("###,###.##");
		return df.format(coins);
	}

}
