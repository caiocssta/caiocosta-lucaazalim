package me.caio.HungerGames.Scoreboard;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import me.caio.HungerGames.Type;
import me.caio.HungerGames.Utils.Enum.GameStage;
import me.skater.clans.Clan;

public class ScoreboardManager {
	private me.caio.HungerGames.Main main;
	// private StringScroller scroller;
	private int animation;
	private String basetext;
	public HashMap<UUID, String> teamplayers;

	public ScoreboardManager() {
		this.main = me.caio.HungerGames.Main.getInstance();
		this.basetext = "§e§lHeavenMC";
		this.teamplayers = new HashMap<>();
		// this.scroller = new StringScroller("WOMBOCRAFT HARDCORE GAMES", 20,
		// 20);
		new BukkitRunnable() {
			public void run() {
				// String n = ScoreboardManager.this.scroller.next();
				String n = null;
				if (main.type == Type.TEAM) {
					n = nextTeamAnimation();
				}
				if (main.type == Type.CLAN) {
					n = nextClanAnimation();
				}
				if (main.type == Type.NORMAL) {
					n = nextAnimation();
				}
				if (main.type == Type.ULTRA) {
					n = nextUltraAnimation();
				}
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.getScoreboard().getObjective("main") != null) {
						p.getScoreboard().getObjective("main").setDisplayName(n);
					}

					if (p.getScoreboard().getObjective("second") != null) {
						p.getScoreboard().getObjective("second").setDisplayName(n);
					}

				}
			}
		}.runTaskTimerAsynchronously(this.main, 2L, 2L);
	}

	public String nextAnimation() {
		animation++;
		if (animation == 110) {
			basetext = "§e§lHEAVENHG";
			animation = 10;
		}
		if (animation == 11) {
			basetext = "§f§lH§e§lEAVENHG";
		}
		if (animation == 12) {
			basetext = "§e§lH§f§lE§e§lAVENHG";
		}
		if (animation == 13) {
			basetext = "§e§lHE§f§lA§e§lVENHG";
		}
		if (animation == 14) {
			basetext = "§e§lHEA§f§lV§e§lENHG";
		}
		if (animation == 15) {
			basetext = "§e§lHEAV§f§lE§e§lNHG";
		}
		if (animation == 17) {
			basetext = "§e§lHEAVE§f§lH§e§lHG";
		}
		if (animation == 16) {
			basetext = "§e§lHEAVEN§f§lH§e§lG";
		}
		if (animation == 17) {
			basetext = "§e§lHEAVENH§f§lG";
		}
		if (animation == 18) {
			basetext = "§e§lHEAVENHG";
		}
		return basetext;
	}

	public String onlineTeamPlayer(UUID uuid) {
		if (Bukkit.getPlayer(uuid) != null) {
			return "§7" + Bukkit.getPlayer(uuid).getName();
		}
		return "§c" + teamplayers.get(uuid);
	}

	public void createSecondScoreboardBasea(Player p) {
		Scoreboard board = p.getScoreboard();
		Objective o = board.registerNewObjective("second", "dummy");
		o.setDisplayName("§e§lHEAVENHG");
		int i = 16;
		o.getScore("§0").setScore(i);
		i--;

		o.getScore("§q").setScore(i);
		i--;
		Team time = p.getScoreboard().registerNewTeam("time");
		time.setPrefix("Seu Time§7: ");
		time.setSuffix("§e" + main.team.getTeamName(p));
		time.addEntry("§q");

		ArrayList<UUID> players = new ArrayList<>();
		if (main.team.teamPlayers.containsValue(p.getUniqueId())) {
			for (UUID uuid : main.team.teamPlayers.get(main.team.getTeam(p))) {
				players.add(uuid);
			}
		}
		o.getScore("§w").setScore(i);
		i--;
		Team p1 = p.getScoreboard().registerNewTeam("p1");
		if (players.size() >= 1) {
			p1.setPrefix("§e» ");
			p1.setSuffix(onlineTeamPlayer(players.get(0)));
		}
		p1.addEntry("§w");

		o.getScore("§y").setScore(i);
		i--;
		Team p2 = p.getScoreboard().registerNewTeam("p2");
		if (players.size() >= 2) {
			p2.setPrefix("§e» ");
			p2.setSuffix(onlineTeamPlayer(players.get(1)));
		}
		p2.addEntry("§y");

		o.getScore("§r").setScore(i);
		i--;
		Team p3 = p.getScoreboard().registerNewTeam("p3");
		if (players.size() >= 3) {
			p3.setPrefix("§e» ");
			p3.setSuffix(onlineTeamPlayer(players.get(2)));
		}
		p3.addEntry("§r");

		o.getScore("§t").setScore(i);
		i--;
		Team p4 = p.getScoreboard().registerNewTeam("p4");
		if (players.size() >= 4) {
			p4.setPrefix("§e» ");
			p4.setSuffix(onlineTeamPlayer(players.get(3)));
		}
		p4.addEntry("§t");

		o.setDisplaySlot(DisplaySlot.SIDEBAR);
	}

	public void updateSecondScoreboard() {
		new BukkitRunnable() {
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					ArrayList<UUID> players = new ArrayList<>();
					for (UUID uuid : main.team.teamPlayers.get(main.team.getTeam(p))) {
						players.add(uuid);
					}
					if (p.getScoreboard().getTeam("p1") != null) {
						Team p1 = p.getScoreboard().getTeam("p1");
						if (players.size() >= 1) {
							p1.setPrefix("§e» ");
							p1.setSuffix(onlineTeamPlayer(players.get(0)));
						}
					}
					if (p.getScoreboard().getTeam("p2") != null) {
						Team p1 = p.getScoreboard().getTeam("p2");
						if (players.size() >= 2) {
							p1.setPrefix("§e» ");
							p1.setSuffix(onlineTeamPlayer(players.get(1)));

						}
					}
					if (p.getScoreboard().getTeam("p3") != null) {
						Team p1 = p.getScoreboard().getTeam("p3");
						if (players.size() >= 3) {
							p1.setPrefix("§e» ");
							p1.setSuffix(onlineTeamPlayer(players.get(2)));
						}
					}
					if (p.getScoreboard().getTeam("p4") != null) {
						Team p1 = p.getScoreboard().getTeam("p4");
						if (players.size() >= 4) {
							p1.setPrefix("§e» ");
							p1.setSuffix(onlineTeamPlayer(players.get(3)));
						}
					}
				}
			}
		}.runTaskTimerAsynchronously(main, 30L, 20L);

	}

	@SuppressWarnings("incomplete-switch")
	public void createScoreboardBase(Player p) {
		Scoreboard board = p.getScoreboard();
		board.registerNewObjective("clear", "dummy");
		Objective o = board.registerNewObjective("main", "dummy");
		o.setDisplayName("§e§lHEAVENHG");
		int i = 16;
		// ESPACO \\
		o.getScore("§0").setScore(i);
		i--;

		o.getScore("§1").setScore(i);
		i--;

		Team c = p.getScoreboard().registerNewTeam("clan");
		c.setPrefix("Clan§7: ");
		Clan clan = me.skater.Main.getInstance().getClanManager().getPlayerClan(p);
		if (clan == null) {
			c.setSuffix("§aNenhum");
		} else {
			if (clan.getName().length() > 10) {
				c.setSuffix("§a" + clan.getName().substring(0, 10) + "...");
			} else {
				c.setSuffix("§a" + clan.getName());
			}
		}
		c.addEntry("§1");
	
		// TEMPO \\

		o.getScore("§2").setScore(i);
		i--;
		
		o.getScore("§3").setScore(i);
		i--;
		Team tempo = p.getScoreboard().registerNewTeam("tempo");
		switch (this.main.stage) {
		case PREGAME:
			tempo.setPrefix("Inicia em");
			tempo.setSuffix("§7: §a" + this.main.getHourTime(Integer.valueOf(this.main.PreGameTimer)));
			break;
		case INVENCIBILITY:
			tempo.setPrefix("Tempo");
			tempo.setSuffix("§7: §a" + this.main.getHourTime(Integer.valueOf(this.main.Invenci)));
			break;
		case GAMETIME:
			tempo.setPrefix("Tempo");
			tempo.setSuffix("§7: §a" + this.main.getHourTime(Integer.valueOf(this.main.GameTimer)));
			break;
		}
		tempo.addEntry("§3");


		// ONLINE \\
		o.getScore("§4").setScore(i);
		i--;
		Team online = p.getScoreboard().registerNewTeam("online");
		online.setPrefix("Players§7: ");
		online.setSuffix("§a" + (Bukkit.getOnlinePlayers().length - this.main.adm.admin.size() - this.main.adm.youtuber.size()) + "/" + Bukkit.getMaxPlayers());
		online.addEntry("§4");
		//
		
		o.getScore("§a").setScore(i);
		i--;

		// KILLS / KIT \\

		o.getScore("§d").setScore(i);
		i--;
		o.getScore("§h").setScore(i);
		i--;
		Team kit = p.getScoreboard().registerNewTeam("kit");
		Team kit2 = p.getScoreboard().registerNewTeam("kit2");
		switch (this.main.stage) {
		case PREGAME:
			if (main.type != Type.ULTRA) {
				kit.setPrefix("Kit 1§7: ");
				kit.setSuffix("§a" + this.main.kit.getKitName(main.kit.getFirstKit(p)));
				kit2.setPrefix("Kit 2§7: ");
				kit2.setSuffix("§a" + this.main.kit.getKitName(main.kit.getSecondKit(p)));
			} else {
				kit.setPrefix("Kit 1§7: ");
				kit.setSuffix("§a" + this.main.kit.getKitName(main.kit.getFirstKit(p)));
			}
			break;
		case INVENCIBILITY:
			if (main.type != Type.ULTRA) {
				kit.setPrefix("Kit 1§7: ");
				kit.setSuffix("§a" + this.main.kit.getKitName(main.kit.getFirstKit(p)));
				kit2.setPrefix("Kit 2§7: ");
				kit2.setSuffix("§a" + this.main.kit.getKitName(main.kit.getSecondKit(p)));
			} else {
				kit.setPrefix("Kit 1§7: ");
				kit.setSuffix("§a" + this.main.kit.getKitName(main.kit.getFirstKit(p)));
			}
			break;
		case GAMETIME:
			if (main.type == Type.TEAM) {
				kit2.setPrefix("Time§7: ");
				kit2.setSuffix("§e" + main.team.getTeamName(p));
			}
			kit.setPrefix("Kills§7: ");
			kit.setSuffix("§a" + this.main.getKills(p));
			break;
		}
		kit.addEntry("§d");
		kit2.addEntry("§h");
		/*
		 * o.getScore("§a").setScore(i); i--; Team ip =
		 * p.getScoreboard().registerNewTeam("ip"); ip.setPrefix("Servidor§7: ");
		 * ip.setSuffix("§7" + main.filterIp(main.ipAddress));
		 * ip.addEntry("§a");
		 */
		// /SCORE \\
		
		// SITE \\
		o.getScore("§i").setScore(i);
		i--;
		o.getScore("§j").setScore(i);
		i--;
		Team site = p.getScoreboard().registerNewTeam("site");
		site.setPrefix("    §eheavenmc");
		site.setSuffix("§e.com.br");
		site.addEntry("§j");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
	}

	public void updateClan(final Player p) {
		if (p.getScoreboard().getTeam("clan") != null) {
			Team c = p.getScoreboard().getTeam("clan");
			Clan clan = me.skater.Main.getInstance().getClanManager().getPlayerClan(p);
			if (clan == null) {
				c.setSuffix("§cNenhum");
			} else {
				if (clan.getName().length() > 10) {
					c.setSuffix("§a" + clan.getName().substring(0, 10) + "...");
				} else {
					c.setSuffix("§a" + clan.getName());
				}
			}
		}
	}
/*
	public void updatePlayerRank(final Player p) {
		new BukkitRunnable() {
			public void run() {
				if (p.getScoreboard().getTeam("levelup") != null) {
					Team up = p.getScoreboard().getTeam("levelup");
					up.setSuffix("§7" + me.skater.Main.getInstance().getRankManager().getLevelPorcentageDouble(p) + "%");
				}
				if (p.getScoreboard().getTeam("level") != null) {
					Team level = p.getScoreboard().getTeam("level");
					level.setSuffix("§a" + me.skater.Main.getInstance().getRankManager().getPlayerRank(p).getLevel());
				}
				
			}
		}.runTaskAsynchronously(this.main);
	}
*/
	public void updatePlayers() {
		final int players;
		if (main.stage == GameStage.PREGAME) {
			players = Bukkit.getOnlinePlayers().length - this.main.adm.admin.size() - this.main.adm.youtuber.size();
		} else {
			players = main.gamers.size();
		}
		new BukkitRunnable() {
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.getScoreboard().getTeam("online") != null) {
						p.getScoreboard().getTeam("online").setSuffix("§a" + players + "/" + Bukkit.getMaxPlayers());
					}
				}
			}
		}.runTaskAsynchronously(this.main);
	}

	public void updateIp() {
		new BukkitRunnable() {
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.getScoreboard().getTeam("ip") != null) {
						p.getScoreboard().getTeam("ip").setSuffix("§a" + main.filterIp(main.ipAddress));

					}
				}
			}
		}.runTaskAsynchronously(this.main);
	}

	public void updateTime() {
		new BukkitRunnable() {
			@SuppressWarnings("incomplete-switch")
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.getScoreboard().getTeam("tempo") != null) {
						Team t = p.getScoreboard().getTeam("tempo");
						switch (ScoreboardManager.this.main.stage) {
						case GAMETIME:
							t.setSuffix("§7: §a" + ScoreboardManager.this.main.getHourTime(Integer.valueOf(ScoreboardManager.this.main.GameTimer)));
							break;
						case INVENCIBILITY:
							t.setSuffix("§7: §a" + ScoreboardManager.this.main.getHourTime(ScoreboardManager.this.main.Invenci));
							break;
						case PREGAME:
							t.setSuffix("§7: §a" + ScoreboardManager.this.main.getHourTime(Integer.valueOf(ScoreboardManager.this.main.PreGameTimer)));
						}
					}
				}
			}
		}.runTaskAsynchronously(this.main);
	}

	public void updateState() {
		new BukkitRunnable() {
			@SuppressWarnings({ "incomplete-switch" })
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.getScoreboard().getTeam("tempo") != null) {
						Team t = p.getScoreboard().getTeam("tempo");
						switch (ScoreboardManager.this.main.stage) {
						case PREGAME:
							t.setPrefix("Inicia em§7: ");
							t.setSuffix("§a" + ScoreboardManager.this.main.getHourTime(Integer.valueOf(ScoreboardManager.this.main.PreGameTimer)));
							break;
						case INVENCIBILITY:
							t.setPrefix("Tempo");
							t.setSuffix("§7: §a" + ScoreboardManager.this.main.getHourTime(ScoreboardManager.this.main.Invenci));
							break;
						case GAMETIME:
							t.setPrefix("Tempo§7: ");
							t.setSuffix("§a" + ScoreboardManager.this.main.getHourTime(Integer.valueOf(ScoreboardManager.this.main.GameTimer)));
						}
					}
				}
			}
		}.runTaskAsynchronously(this.main);
	}

	public void updateKills(final Player p) {
		new BukkitRunnable() {
			public void run() {
				if (p.getScoreboard().getTeam("kit") != null) {
					Team kills = p.getScoreboard().getTeam("kit");
					if ((!ScoreboardManager.this.main.adm.isAdmin(p)) || (!ScoreboardManager.this.main.adm.isSpectating(p)) || (!ScoreboardManager.this.main.adm.isYTPRO(p))) {
						kills.setPrefix("Kills§7: ");
						kills.setSuffix("§a" + ScoreboardManager.this.main.getKills(p));
					}
				}

				if (p.getScoreboard().getTeam("kit2") != null) {
					Team kills = p.getScoreboard().getTeam("kit2");
					if ((!ScoreboardManager.this.main.adm.isAdmin(p)) || (!ScoreboardManager.this.main.adm.isSpectating(p)) || (!ScoreboardManager.this.main.adm.isYTPRO(p))) {
						if (main.type == Type.TEAM) {
							kills.setPrefix("Time§7: ");
							kills.setSuffix("§e" + main.team.getTeamName(p));
						}
					} else {
						kills.unregister();
					}
				}
			}
		}.runTaskAsynchronously(this.main);
	}

	public void updateKit(final Player p) {
		new BukkitRunnable() {
			public void run() {
				if (main.type != Type.ULTRA) {
					if (p.getScoreboard().getTeam("kit2") != null) {
						Team kit = p.getScoreboard().getTeam("kit2");
						if ((!ScoreboardManager.this.main.adm.isAdmin(p)) || (!ScoreboardManager.this.main.adm.isSpectating(p)) || (!ScoreboardManager.this.main.adm.isYTPRO(p))) {
							kit.setPrefix("Kit 2§7: ");
							kit.setSuffix("§a" + main.kit.getKitName(main.kit.getSecondKit(p)));
						}
					}
				}
				if (p.getScoreboard().getTeam("kit") != null) {
					Team kit = p.getScoreboard().getTeam("kit");
					if ((!ScoreboardManager.this.main.adm.isAdmin(p)) || (!ScoreboardManager.this.main.adm.isSpectating(p)) || (!ScoreboardManager.this.main.adm.isYTPRO(p))) {
						if (main.type != Type.ULTRA) {
							kit.setPrefix("Kit 1§7: ");
							kit.setSuffix("§a" + main.kit.getKitName(main.kit.getFirstKit(p)));
						} else {
							kit.setPrefix("Kit§7: ");
							kit.setSuffix("§a" + main.kit.getKitName(main.kit.getPlayerKit(p)));
						}
					}
				}
			}
		}.runTaskAsynchronously(this.main);
	}

	public String replaceCoins(Integer coins) {
		DecimalFormat df = new DecimalFormat("###,###.##");
		/*
		 * int size = coinsString.length(); if(size > 3){ String part1; part1 =
		 * coinsString.substring(0 , 3); //1 //1.1 String sf =
		 * coinsString.replace(part1, "") + "." + part1; return sf; }
		 */
		return df.format(coins);
	}

	public String nextTeamAnimation() {
		animation++;
		if (animation == 110) {
			basetext = "§e§lHARDCORE GAMES";
			animation = 10;
		}
		if (animation == 11) {
			basetext = "§f§lH§e§lARDCORE GAMES";
		}
		if (animation == 12) {
			basetext = "§e§lH§f§lA§e§lRDCORE GAMES";
		}
		if (animation == 13) {
			basetext = "§e§lHA§f§lR§e§lDCORE GAMES";
		}
		if (animation == 14) {
			basetext = "§e§lHAR§f§lD§e§lCORE GAMES";
		}
		if (animation == 15) {
			basetext = "§e§lHARD§f§lC§e§lORE GAMES";
		}
		if (animation == 16) {
			basetext = "§e§lHARDC§f§lO§e§lRE GAMES";
		}
		if (animation == 17) {
			basetext = "§e§lHARDCO§f§lR§e§lE GAMES";
		}
		if (animation == 18) {
			basetext = "§e§lHARDCOR§f§lE§e§l GAMES";
		}
		if (animation == 19) {
			basetext = "§e§lHARDCORE §f§lG§e§lAMES";
		}
		if (animation == 20) {
			basetext = "§e§lHARDCORE §e§lG§f§lA§e§lMES";
		}
		if (animation == 21) {
			basetext = "§e§lHARDCORE §e§lGA§f§lM§e§lES";
		}
		if (animation == 22) {
			basetext = "§e§lHARDCORE §e§lGAM§f§lE§e§lS";
		}
		if (animation == 23) {
			basetext = "§e§lHARDCORE §e§lGAME§f§lS";
		}
		if (animation == 25) {
			basetext = "§f§lHARDCORE GAMES";
		}
		if (animation == 27) {
			basetext = "§e§lHARDCORE GAMES";
		}
		if (animation == 29) {
			basetext = "§f§lHARDCORE GAMES";
		}
		if (animation == 31) {
			basetext = "§e§lHARDCORE GAMES";
		}
		if (animation == 60) {
			basetext = "§f§lT§e§lEAMHG";
		}
		if (animation == 61) {
			basetext = "§e§lT§f§lE§e§lMHG";
		}
		if (animation == 62) {
			basetext = "§e§lTE§f§lA§e§lMHG";
		}
		if (animation == 63) {
			basetext = "§e§lTEA§f§lM§e§lHG";
		}
		if (animation == 64) {
			basetext = "§e§lTEAM§f§lH§e§lG";
		}
		if (animation == 65) {
			basetext = "§e§lTEAMH§f§lG";
		}
		if (animation == 67) {
			basetext = "§f§lTEAMHG";
		}
		if (animation == 69) {
			basetext = "§e§lTEAMHG";
		}
		if (animation == 71) {
			basetext = "§f§lTEAMHG";
		}
		if (animation == 73) {
			basetext = "§e§lTEAMHG";
		}
		return basetext;
	}

	public String nextUltraAnimation() {
		animation++;
		if (animation == 110) {
			basetext = "§e§lHARDCORE GAMES";
			animation = 10;
		}
		if (animation == 11) {
			basetext = "§f§lH§e§lARDCORE GAMES";
		}
		if (animation == 12) {
			basetext = "§e§lH§f§lA§e§lRDCORE GAMES";
		}
		if (animation == 13) {
			basetext = "§e§lHA§f§lR§e§lDCORE GAMES";
		}
		if (animation == 14) {
			basetext = "§e§lHAR§f§lD§e§lCORE GAMES";
		}
		if (animation == 15) {
			basetext = "§e§lHARD§f§lC§e§lORE GAMES";
		}
		if (animation == 16) {
			basetext = "§e§lHARDC§f§lO§e§lRE GAMES";
		}
		if (animation == 17) {
			basetext = "§e§lHARDCO§f§lR§e§lE GAMES";
		}
		if (animation == 18) {
			basetext = "§e§lHARDCOR§f§lE§e§l GAMES";
		}
		if (animation == 19) {
			basetext = "§e§lHARDCORE §f§lG§e§lAMES";
		}
		if (animation == 20) {
			basetext = "§e§lHARDCORE §e§lG§f§lA§e§lMES";
		}
		if (animation == 21) {
			basetext = "§e§lHARDCORE §e§lGA§f§lM§e§lES";
		}
		if (animation == 22) {
			basetext = "§e§lHARDCORE §e§lGAM§f§lE§e§lS";
		}
		if (animation == 23) {
			basetext = "§e§lHARDCORE §e§lGAME§f§lS";
		}
		if (animation == 25) {
			basetext = "§f§lHARDCORE GAMES";
		}
		if (animation == 27) {
			basetext = "§e§lHARDCORE GAMES";
		}
		if (animation == 29) {
			basetext = "§f§lHARDCORE GAMES";
		}
		if (animation == 31) {
			basetext = "§e§lHARDCORE GAMES";
		}
		if (animation == 60) {
			basetext = "§f§lU§e§lLTRAHG";
		}
		if (animation == 61) {
			basetext = "§e§lU§f§lL§e§TRAHG";
		}
		if (animation == 62) {
			basetext = "§e§lUL§f§lT§e§lRAHG";
		}
		if (animation == 63) {
			basetext = "§e§lULT§f§lR§e§lAHG";
		}
		if (animation == 64) {
			basetext = "§e§lULTR§f§lA§e§lHG";
		}
		if (animation == 65) {
			basetext = "§e§lULTRA§f§lH§e§lG";
		}
		if (animation == 67) {
			basetext = "§e§lULTRAH§f§lG";
		}
		if (animation == 69) {
			basetext = "§e§lULTRAHG";
		}
		if (animation == 71) {
			basetext = "§f§lULTRAHG";
		}
		if (animation == 73) {
			basetext = "§e§lULTRAHG";
		}
		return basetext;
	}

	public String nextClanAnimation() {
		animation++;
		if (animation == 110) {
			basetext = "§c§lHARDCORE GAMES";
			animation = 10;
		}
		if (animation == 11) {
			basetext = "§f§lH§c§lARDCORE GAMES";
		}
		if (animation == 12) {
			basetext = "§c§lH§f§lA§c§lRDCORE GAMES";
		}
		if (animation == 13) {
			basetext = "§c§lHA§f§lR§c§lDCORE GAMES";
		}
		if (animation == 14) {
			basetext = "§c§lHAR§f§lD§c§lCORE GAMES";
		}
		if (animation == 15) {
			basetext = "§c§lHARD§f§lC§c§lORE GAMES";
		}
		if (animation == 16) {
			basetext = "§c§lHARDC§f§lO§c§lRE GAMES";
		}
		if (animation == 17) {
			basetext = "§c§lHARDCO§f§lR§c§lE GAMES";
		}
		if (animation == 18) {
			basetext = "§c§lHARDCOR§f§lE§c§l GAMES";
		}
		if (animation == 19) {
			basetext = "§c§lHARDCORE §f§lG§c§lAMES";
		}
		if (animation == 20) {
			basetext = "§c§lHARDCORE §c§lG§f§lA§c§lMES";
		}
		if (animation == 21) {
			basetext = "§c§lHARDCORE §c§lGA§f§lM§c§lES";
		}
		if (animation == 22) {
			basetext = "§c§lHARDCORE §c§lGAM§f§lE§c§lS";
		}
		if (animation == 23) {
			basetext = "§c§lHARDCORE §c§lGAME§f§lS";
		}
		if (animation == 25) {
			basetext = "§f§lHARDCORE GAMES";
		}
		if (animation == 27) {
			basetext = "§c§lHARDCORE GAMES";
		}
		if (animation == 29) {
			basetext = "§f§lHARDCORE GAMES";
		}
		if (animation == 31) {
			basetext = "§c§lHARDCORE GAMES";
		}
		if (animation == 60) {
			basetext = "§f§lC§c§lLANHG";
		}
		if (animation == 61) {
			basetext = "§c§lC§f§lL§c§lANHG";
		}
		if (animation == 62) {
			basetext = "§c§lCL§f§lA§c§lNHG";
		}
		if (animation == 63) {
			basetext = "§c§lCLA§f§lN§c§lHG";
		}
		if (animation == 64) {
			basetext = "§c§lCLAN§f§lH§c§lG";
		}
		if (animation == 65) {
			basetext = "§c§lCLANH§f§lG";
		}
		if (animation == 67) {
			basetext = "§f§lCLANHG";
		}
		if (animation == 69) {
			basetext = "§c§lCLANHG";
		}
		if (animation == 71) {
			basetext = "§f§lCLANHG";
		}
		if (animation == 73) {
			basetext = "§c§lCLANHG";
		}
		return basetext;
	}
}