package me.skater.tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.skater.Main;
import me.skater.Management;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TagManager extends Management {
	private HashMap<Tag, List<Player>> tags;
	private Main m;

	public TagManager(Main main) {
		super(main);
		this.m = main;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onEnable() {
		this.tags = new HashMap();
		for (Tag tag : Tag.values()) {
			this.tags.put(tag, new ArrayList());
		}
		getServer().getPluginManager().registerEvents(new JoinListener(this), getPlugin());
		getServer().getPluginManager().registerEvents(new QuitListener(this), getPlugin());
		getPlugin().getCommand("tag").setExecutor(new TagCommand(this));

	}

	public String getName(String kit) {
		char[] stringArray = kit.toLowerCase().toCharArray();
		stringArray[0] = Character.toUpperCase(stringArray[0]);
		return new String(stringArray);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addPlayerTag(Player player, Tag tag) {
		removePlayerTag(player);
		player.setDisplayName(tag.getPrefix() + player.getName() + ChatColor.RESET  + "§7[" + m.getRankManager().getPlayerRank(player).getRank().getSymbol() + "§r§7]");
		Scoreboard board = getPlugin().getScoreboardManager().getPlayerScoreboard(player);
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
		List<Player> playerList = this.tags.get(tag);
		playerList.add(player);
		this.tags.put(tag, playerList);
		for (Iterator localIterator1 = this.tags.entrySet().iterator(); localIterator1.hasNext();) {
			Object entry = localIterator1.next();
			Tag tagteam = (Tag) ((Map.Entry) entry).getKey();
			List<Player> players = (List) ((Map.Entry) entry).getValue();
			Team team = board.getTeam(tagteam.getTeamName());
			for (Player participante : players) {
				if (team != null) {
					team.addPlayer(participante);
				}
				Scoreboard playerBoard = getPlugin().getScoreboardManager().getPlayerScoreboard(participante);
				Team playerTeam = playerBoard.getTeam(tag.getTeamName());
				if (playerTeam != null) {
					playerTeam.addPlayer(player);
				}
			}
		}
	}

	public Tag getDefaultTag(Player p) {
		if (getPlugin().getPermissions().isOwner(p)) {
			return Tag.DONO;
		}
		if (getPlugin().getPermissions().isAdmin(p)) {
			return Tag.ADMIN;
		}
		if (getPlugin().getPermissions().isMod(p)) {
			return Tag.MOD;
		}
		if (getPlugin().getPermissions().isTrial(p)) {
			return Tag.TRIAL;
		}
		if (getPlugin().getPermissions().isYouTuber(p)) {
			return Tag.YOUTUBER;
		}
		if (getPlugin().getPermissions().isBeta(p)) {
			return Tag.BETA;
		}
		if (getPlugin().getPermissions().isPro(p)) {
			return Tag.PRO;
		}
		if (getPlugin().getPermissions().isMvp(p)) {
			return Tag.MVP;
		}
		if (getPlugin().getPermissions().isVip(p)) {
			return Tag.VIP;
		}
		return Tag.NORMAL;
	}

	public Tag getPlayerActiveTag(Player p) {
		Scoreboard playerBoard = getPlugin().getScoreboardManager().getPlayerScoreboard(p);
		Team playerTeam = playerBoard.getPlayerTeam(p);
		if (playerTeam != null) {
			return Tag.valueOf(playerTeam.getName().toUpperCase().substring(1, playerTeam.getName().length()));
		}
		return Tag.NORMAL;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	public void removePlayerTag(Player player) {
		for (Player participante : getServer().getOnlinePlayers()) {
			Scoreboard playerBoard = getPlugin().getScoreboardManager().getPlayerScoreboard(participante);
			Team playerTeam = playerBoard.getPlayerTeam(player);
			if (playerTeam != null) {
				playerTeam.removePlayer(player);
			}
			participante.setScoreboard(playerBoard);
		}
		for (Map.Entry<Tag, List<Player>> entry : this.tags.entrySet()) {
			Object players = entry.getValue();
			((List) players).remove(player);
			this.tags.put(entry.getKey(), (List<Player>) players);
		}
	}

	public String getTagPrefix(Player p) {
		String grupo = "§7";
		if (getPlugin().getPermissions().isVip(p)) {
			grupo = "§a";
		}
		if (getPlugin().getPermissions().isMvp(p)) {
			grupo = "§9";
		}
		if (getPlugin().getPermissions().isPro(p)) {
			grupo = "§6";
		}
		if (getPlugin().getPermissions().isBeta(p)) {
			grupo = "§d";
		}
		if (getPlugin().getPermissions().isYouTuber(p)) {
			grupo = "§b";
		}
		if (getPlugin().getPermissions().isBuilder(p)) {
			grupo = "§3";
		}
		if (getPlugin().getPermissions().isTrial(p)) {
			grupo = "§5";
		}
		if (getPlugin().getPermissions().isMod(p)) {
			grupo = "§5§o";
		}
		if (getPlugin().getPermissions().isAdmin(p)) {
			grupo = "§c";
		}
		if (getPlugin().getPermissions().isOwner(p)) {
			grupo = "§4";
		}
		return grupo;
	}

	public String getTagName(String color) {
		String grupo = "§7Normal";
		if (color == "§a") {
			grupo = "§aLIGHT ";
		}
		if (color == "§9") {
			grupo = "§9MVP ";
		}
		if (color == "§6") {
			grupo = "§6PRO ";
		}
		if (color == "§d") {
			grupo = "§dBETA ";
		}
		if (color == "§b") {
			grupo = "§bYT ";
		}
		if (color == "§5") {
			grupo = "§5TRIAL ";
		}
		if (color == "§5§o") {
			grupo = "§5§oMOD ";
		}
		if (color == "§c") {
			grupo = "§cADMIN ";
		}
		if (color == "§4") {
			grupo = "§4DONO ";
		}
		return grupo;
	}

	public String getTagName(Player p) {

		return getPlayerActiveTag(p).getColor() + getName(getPlayerActiveTag(p).getTeamName().substring(1, getPlayerActiveTag(p).getTeamName().length()));
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onDisable() {
		for (Player player : getServer().getOnlinePlayers()) {
			removePlayerTag(player);
		}
	}
}
