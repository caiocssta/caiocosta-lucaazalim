package me.caio.HungerGames.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Management;
import me.caio.HungerGames.Team.Enums.TeamColors;
import me.caio.HungerGames.Team.Listeners.TeamInventory;
import me.caio.HungerGames.Utils.ItemBuilder;
import me.caio.HungerGames.Utils.Enum.GameStage;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeamManager extends Management {

	public HashMap<TeamColors, ArrayList<UUID>> teamPlayers;
	public HashMap<TeamColors, String> teamNomes;
	private TeamInventory inventory;
	private ItemStack teammenu;

	public TeamManager(Main main) {
		super(main);
	}

	public void onEnable() {
		this.teamPlayers = new HashMap<>();
		this.teamNomes = new HashMap<>();
		int i = 1;
		for (TeamColors t : TeamColors.values()) {
			ArrayList<UUID> playerlist = new ArrayList<UUID>();
			teamPlayers.put(t, playerlist);
			teamNomes.put(t, "" + i);
			i++;
		}
		inventory = new TeamInventory(getPlugin());
		inventory.setupInventory();
		getPlugin().getServer().getPluginManager().registerEvents(inventory, getPlugin());
		this.teammenu = new ItemStack(Material.LEATHER_CHESTPLATE);
		ItemMeta im3 = this.teammenu.getItemMeta();
		im3.setDisplayName("§eEscolha o seu Time§7 (Clique)");
		this.teammenu.setItemMeta(im3);
	}

	public ItemStack getTeamStack() {
		return this.teammenu;
	}

	public void onDisable() {

	}

	public void addPlayerToTeam(UUID uuid, TeamColors team) {
		removerPlayerTeam(uuid);
		teamPlayers.get(team).add(uuid);
		if (Main.getInstance().stage == GameStage.PREGAME) {
			Player p = Bukkit.getPlayer(uuid);
			setPlayerArmadura(p, team);
		}
	}

	public void setPlayerArmadura(Player p, TeamColors team) {
		if (p != null) {
			p.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS).cor(team.getColor()).construir());
			p.getInventory().setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).cor(team.getColor()).construir());
			p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).cor(team.getColor()).construir());
			p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).cor(team.getColor()).construir());
		}
	}

	public void removerPlayerTeam(UUID uuid) {
		for (TeamColors t : teamPlayers.keySet()) {
			if (teamPlayers.get(t).contains(uuid)) {
				if (Main.getInstance().stage == GameStage.PREGAME) {
					teamPlayers.get(t).remove(uuid);
				} else if (Main.getInstance().stage == GameStage.GAMETIME) {
					// ScoreBManager.updateTeam(t);
				}
			}
		}
	}

	public boolean hasTeam(Player p1, Player p2) {
		for (TeamColors t : teamPlayers.keySet()) {
			if (teamPlayers.get(t).contains(p1.getUniqueId())) {
				if (teamPlayers.get(t).contains(p2.getUniqueId())) {
					return true;
				}
			}
		}
		return false;
	}

	public int getKillsTeam(TeamColors tc) {
		int k = 0;
		for (UUID uuid : teamPlayers.get(tc)) {
			if (Bukkit.getPlayer(uuid) != null) {
				k = k + Main.getInstance().getKills(Bukkit.getPlayer(uuid));
			}
		}
		return k;
	}

	public boolean hasTeam(Player p) {
		for (TeamColors t : teamPlayers.keySet()) {
			if (teamPlayers.get(t).contains(p.getUniqueId())) {
				return true;
			}
		}
		return false;
	}

	public TeamColors getTeam(Player p) {
		for (TeamColors t : teamPlayers.keySet()) {
			if (teamPlayers.get(t).contains(p.getUniqueId())) {
				return t;
			}
		}
		return null;
	}

	public String getTeamName(Player p) {
		for (TeamColors t : teamPlayers.keySet()) {
			if (teamPlayers.get(t).contains(p.getUniqueId())) {
				return teamNomes.get(t);
			}
		}
		return "Nenhum";
	}

	public List<Player> getPlayersTeamOnline(TeamColors tc) {
		List<Player> players = new ArrayList<Player>();
		for (UUID uuid : teamPlayers.get(tc)) {
			Player p = Bukkit.getPlayer(uuid);
			if (p != null) {
				if (Main.getInstance().stage != GameStage.PREGAME) {
					if (!Main.getInstance().isNotPlaying(p)) {
						players.add(p);
					}
				} else {
					players.add(p);
				}
			}
		}
		return players;
	}

	public boolean teamIsAlive(TeamColors tc) {
		boolean alive = false;
		for (UUID uuid : teamPlayers.get(tc)) {
			if (Main.getInstance().gamers.contains(uuid)) {
				alive = true;
				break;
			}
		}
		return alive;
	}

	public List<TeamColors> timesRestantes() {
		List<TeamColors> times = new ArrayList<TeamColors>();
		for (TeamColors tc : teamPlayers.keySet()) {
			if (!getPlayersTeamOnline(tc).isEmpty()) {
				times.add(tc);
			}
		}
		return times;
	}

	// public void setupTeamTags(TeamColors tc) {
	// List<Player> players = getPlayersTeamOnline(tc);
	// for (Player player : players) {
	// Scoreboard board2 =
	// me.skater.Main.getInstance().getScoreboardManager().getPlayerScoreboard(player);
	// for (Player otherplayer : Bukkit.getOnlinePlayers()) {
	// if (players.contains(otherplayer)) {
	// Scoreboard board = otherplayer.getScoreboard();
	// if (board != null) {
	// String nome = player.getName();
	// if (board.getTeam(nome) == null) {
	// board.registerNewTeam(nome).addPlayer(player);
	// }
	// board.getTeam(nome).setPrefix("§a");
	// board.getTeam(nome).setSuffix("§a");
	// }
	// if (player != otherplayer) {
	// if (board2 != null) {
	// String nome = otherplayer.getName();
	// if (board2.getTeam(nome) == null) {
	// board2.registerNewTeam(nome).addPlayer(
	// otherplayer);
	// }
	// board2.getTeam(nome).setPrefix("§a");
	// board2.getTeam(nome).setSuffix("§a");
	// }
	// }
	//
	// } else {
	// Scoreboard board =
	// me.skater.Main.getInstance().getScoreboardManager().getPlayerScoreboard(otherplayer);
	// if (board != null) {
	// String nome = player.getName();
	// if (board.getTeam(nome) == null) {
	// board.registerNewTeam(nome).addPlayer(player);
	// }
	// board.getTeam(nome).setPrefix("§c");
	// board.getTeam(nome).setSuffix("§c");
	// }
	// if (player != otherplayer) {
	// if (board2 != null) {
	// String nome = otherplayer.getName();
	// if (board2.getTeam(nome) == null) {
	// board2.registerNewTeam(nome).addPlayer(
	// otherplayer);
	// }
	// board2.getTeam(nome).setPrefix("§c");
	// board2.getTeam(nome).setSuffix("§c");
	// }
	// }
	//
	// }
	// }
	// }
	// }

	public TeamInventory getTeamInventory() {
		return this.inventory;
	}

}
