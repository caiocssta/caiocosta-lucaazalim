package me.skater.clans;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import me.skater.Main;
import me.skater.Management;
import me.skater.Commands.Staff;
import me.skater.Enums.ServerType;
import me.skater.HologramAPI.HologramV2;
import me.skater.MySQL.Connect;
import me.skater.clans.MemberType.Member;
import me.skater.ranking.PlayerRank;
import me.skater.tags.Tag;
import me.skater.titles.PlayerTitle;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ClanManager extends Management {

	public HashMap<UUID, Clan> pclan;
	public HashMap<UUID, String> invites;
	public ArrayList<UUID> chat;

	public ClanManager(Main main) {
		super(main);

	}

	@Override
	public void onEnable() {
		this.pclan = new HashMap<>();
		this.invites = new HashMap<>();
		this.chat = new ArrayList<>();
		getPlugin().getCommand("clan").setExecutor(new ClanCommand(this));
		loadTop10();

	}

	@Override
	public void onDisable() {

	}

	public void setClanChat(Player p) {
		if (getPlayerClan(p) == null) {
			p.sendMessage(ChatColor.RED + "Você não esta em nenhum clan.");
			return;
		}
		if (Staff.chatStaff.contains(p.getName())) {
			p.sendMessage(ChatColor.RED + "Você não pode usar o chat do clan enquanto estiver no chat da staff.");
			return;
		}
		p.sendMessage(ChatColor.YELLOW + "Você entrou no chat do seu clan.");
		this.chat.add(p.getUniqueId());
	}

	public void removeFromClanChat(Player p) {
		if (isOnClanChat(p)) {
			chat.remove(p.getUniqueId());
			p.sendMessage(ChatColor.RED + "Você saiu do chat do seu clan.");
		}
	}

	public boolean isOnClanChat(Player p) {
		return this.chat.contains(p.getUniqueId());
	}

	public Clan getPlayerClan(Player p) {
		Clan clan = null;
		// Connect.lock.lock();
		try {
			clan = loadPlayerClan(p);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Connect.lock.unlock();
		}

		return clan;
	}

	public Clan getClan(String name) {
		Clan clan = null;
		Connect.lock.lock();
		try {
			clan = loadClanByName(name);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Connect.lock.unlock();
		}
		return clan;
	}

	public void ClanStatus(Player p) {
		Clan clan = getPlayerClan(p);
		if (clan == null) {
			p.sendMessage(ChatColor.RED + "Você não esta em nenhum clan.");
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
			return;
		}
		ClanStatus status = clan.getStatus();
		p.sendMessage("§f§m---------------------");
		p.sendMessage("§6• §e" + clan.getName() + " §7[§c" + clan.getTag() + "§7]");
		p.sendMessage("§6• §fMembros§8: §e" + clan.getTotalMembers());
		p.sendMessage("§6• §fAdmins§8: §e" + clan.getAdmins());
		p.sendMessage("§6• §fDono§8: §e" + getPlugin().name.getName(clan.getOwner().toString()));
		p.sendMessage("");
		p.sendMessage("§6• §6§lEstatisticas:");
		p.sendMessage("§6• §fKills§8: §a" + status.getKills() + " §7- §fDeaths§8: §a" + status.getDeaths() + " §7- §fWins§8: §a" + status.getWins());
		p.sendMessage("§6• §fElo§8: §a" + status.getElo() + " §7- §fCoins§8: §a" + status.getMoney());
		p.sendMessage("§6• §fRank§8: " + getClanRankByElo(status.getElo()));
		p.sendMessage("§f§m---------------------");
	}

	public void ClanStatus(Player p, String nome) {
		Clan clan = getClan(nome);
		if (clan == null) {
			p.sendMessage(ChatColor.RED + "Esse clan não existe.");
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
			return;
		}
		ClanStatus status = clan.getStatus();
		p.sendMessage("§f§m---------------------");
		p.sendMessage("§6• §e" + clan.getName() + " §7[§c" + clan.getTag() + "§7]");
		p.sendMessage("§6• §fMembros§8: §e" + clan.getTotalMembers());
		p.sendMessage("§6• §fAdmins§8: §e" + clan.getAdmins());
		p.sendMessage("§6• §fDono§8: §e" + getPlugin().name.getName(clan.getOwner().toString()));
		p.sendMessage("");
		p.sendMessage("§6• §6§lEstatisticas:");
		p.sendMessage("§6• §fKills§8: §a" + status.getKills() + " §7- §fDeaths§8: §a" + status.getDeaths() + " §7- §fWins§8: §a" + status.getWins());
		p.sendMessage("§6• §fElo§8: §a" + status.getElo() + " §7- §fCoins§8: §a" + status.getMoney());
		p.sendMessage("§6• §fRank§8: " + getClanRankByElo(status.getElo()));
		p.sendMessage("§f§m---------------------");
	}

	public String getClanColorByElo(int elo) {
		if (elo >= 17000) {
			return "§4";
		}
		if (elo >= 8000) {
			return "§b";
		}
		if (elo >= 3000) {
			return "§f";
		}
		if (elo >= 1000) {
			return "§e";
		}
		if (elo >= 500) {
			return "§7";
		}

		return "§8";

	}

	public String getClanRankByElo(int elo) {
		String rank = "§8✸ §8§lBronze I";
		if (elo >= 100) {
			rank = "§8✸ §8§lBronze II";
		}
		if (elo >= 200) {
			rank = "§8✸ §8§lBronze III";
		}
		if (elo >= 300) {
			rank = "§8✸ §8§lBronze IV";
		}
		if (elo >= 400) {
			rank = "§8✸ §8§lBronze V";
		}
		if (elo >= 500) {
			rank = "§7✸ §7§lPrata I";
		}
		if (elo >= 600) {
			rank = "§7✸ §7§lPrata II";
		}
		if (elo >= 700) {
			rank = "§7✸ §7§lPrata III";
		}
		if (elo >= 800) {
			rank = "§7✸ §7§lPrata IV";
		}
		if (elo >= 900) {
			rank = "§7✸ §7§lPrata V";
		}
		if (elo >= 1000) {
			rank = "§e✸ §e§lOuro I";
		}
		if (elo >= 1200) {
			rank = "§e✸ §e§lOuro II";
		}
		if (elo >= 1400) {
			rank = "§e✸ §e§lOuro III";
		}
		if (elo >= 1600) {
			rank = "§e✸ §e§lOuro IV";
		}
		if (elo >= 1800) {
			rank = "§e✸ §e§lOuro V";
		}
		if (elo >= 3000) {
			rank = "§f✸ §f§lPlatina I";
		}
		if (elo >= 4000) {
			rank = "§f✸ §f§lPlatina II";
		}
		if (elo >= 5000) {
			rank = "§f✸ §f§lPlatina III";
		}
		if (elo >= 6000) {
			rank = "§f✸ §f§lPlatina IV";
		}
		if (elo >= 7000) {
			rank = "§f✸ §f§lPlatina V";
		}
		if (elo >= 8000) {
			rank = "§b✸ §b§lDiamante I";
		}
		if (elo >= 10000) {
			rank = "§b✸ §b§lDiamante II";
		}
		if (elo >= 12000) {
			rank = "§b✸ §b§lDiamante III";
		}
		if (elo >= 13000) {
			rank = "§b✸ §b§lDiamante IV";
		}
		if (elo >= 15000) {
			rank = "§b✸ §b§lDiamante V";
		}
		if (elo >= 17000) {
			rank = "§c✪ §4§lHeaven§c ✪";
		}

		return rank;
	}

	public Rank getClanRankEnum(Clan clan) {
		ClanStatus status = clan.getStatus();
		if (status.getElo() >= 17000) {
			return Rank.WOMBO;
		}
		if (status.getElo() >= 8000) {
			return Rank.DIAMOND;
		}
		if (status.getElo() >= 3000) {
			return Rank.PLATINUM;
		}
		if (status.getElo() >= 1000) {
			return Rank.GOLD;
		}
		if (status.getElo() >= 500) {
			return Rank.SILVER;
		}
		return Rank.BRONZE;
	}

	public boolean isSameClan(Player p, Player p2) {
		Clan c1 = getPlayerClan(p);
		Clan c2 = getPlayerClan(p2);
		if (c1 != null && c2 != null) {
			if (c1.getName().equalsIgnoreCase(c2.getName())) {
				return true;
			}
		}
		return false;
	}

	public void clanKillerReward(Player p) {
		Clan clan = getPlayerClan(p);
		if (clan == null) {
			return;
		}
		int gain = getRandom(1, 8);
		int i = new Random().nextInt(20);
		if (i == 14) {
			gain = gain * 2;
		}

		int money = getRandom(10, 100);

		if (getClanRankEnum(clan) == Rank.SILVER) {
			gain--;
		}
		if (getClanRankEnum(clan) == Rank.GOLD) {
			gain--;
			gain--;
		}

		if (getClanRankEnum(clan) == Rank.DIAMOND) {
			gain--;
			gain--;
			gain--;
		}

		if (getClanRankEnum(clan) == Rank.PLATINUM) {
			gain--;
			gain--;
			gain--;
			gain--;
		}

		if (getClanRankEnum(clan) == Rank.WOMBO) {
			gain--;
			gain--;
			gain--;
			gain--;
			gain--;
		}

		if (gain <= 0) {
			gain = 1;
		}
		addKill(clan);
		addMoney(clan, money);
		addElo(clan, gain);
		p.sendMessage("§f§m----------------");
		p.sendMessage("§6§lClan Status:");
		p.sendMessage("§a+1 §fKills");
		p.sendMessage("§a+" + money + " §fCoins");
		p.sendMessage("§a+" + gain + " §fElo");
		p.sendMessage("§f§m----------------");

	}

	public void clanWinnerReward(Player p) {
		Clan clan = getPlayerClan(p);
		if (clan == null) {
			return;
		}
		int gain = getRandom(5, 20);
		int i = new Random().nextInt(20);
		if (i == 5) {
			gain = gain * 2;
		}

		int money = getRandom(100, 200);

		if (getClanRankEnum(clan) == Rank.SILVER) {
			gain--;
		}
		if (getClanRankEnum(clan) == Rank.GOLD) {
			gain--;
			gain--;
		}

		if (getClanRankEnum(clan) == Rank.DIAMOND) {
			gain--;
			gain--;
			gain--;
		}

		if (getClanRankEnum(clan) == Rank.PLATINUM) {
			gain--;
			gain--;
			gain--;
			gain--;
		}

		if (getClanRankEnum(clan) == Rank.WOMBO) {
			for (int l = 0; l < 5; l++) {
				gain--;
			}
		}

		if (gain <= 0) {
			gain = 1;
		}

		addMoney(clan, money);
		addElo(clan, gain);
		addWins(clan);
		p.sendMessage("§f§m----------------");
		p.sendMessage("§6§lClan Status:");
		p.sendMessage("§a+1 §fWin");
		p.sendMessage("§a+" + money + " §fCoins");
		p.sendMessage("§a+" + gain + " §fElo");
		p.sendMessage("§f§m----------------");

	}

	public void clanKilledReward(Player p) {
		Clan clan = getPlayerClan(p);
		if (clan == null) {
			return;
		}
		int loss = getRandom(1, 5);
		int i = new Random().nextInt(20);
		if (i == 6) {
			loss = loss * 2;
		}

		if (getClanRankEnum(clan) == Rank.SILVER) {
			loss++;
		}
		if (getClanRankEnum(clan) == Rank.GOLD) {
			loss++;
			loss++;
		}
		if (getClanRankEnum(clan) == Rank.DIAMOND) {
			loss++;
			loss++;
			loss++;
		}
		if (getClanRankEnum(clan) == Rank.PLATINUM) {
			loss++;
			loss++;
			loss++;
			loss++;
		}
		if (getClanRankEnum(clan) == Rank.WOMBO) {
			for (int l = 0; l < 10; l++) {
				loss++;
			}
		}
		addDeath(clan);
		removeElo(clan, loss);
		p.sendMessage("§f§m----------------");
		p.sendMessage("§c§lClan Status:");
		p.sendMessage("§c+1 §fDeath");
		p.sendMessage("§c-" + loss + " §fElo");
		p.sendMessage("§f§m----------------");

	}

	// public void updateClanStatus(Player p, Player killer, boolean Win, int
	// kills) throws SQLException {
	// if(Win) {
	// Clan c = getPlayerClan(p);
	// int money = getRandom(100, 200);
	// int elo = getRandom(10, 30);
	// addWins(c);
	// addMoney(c, money);
	// addElo(c, elo + kills);
	// p.sendMessage("§f§m-----------");
	// p.sendMessage("§6§lClan Status:");
	// p.sendMessage("§a+1 §fWin");
	// p.sendMessage("§a+" + money + " Coins");
	// p.sendMessage("§a+" + elo + kills + " §fElo");
	// p.sendMessage("§f§m-----------");
	// return;
	// }
	// int gain = 5;
	// int loss = 3;
	// int rankdifference = 0;
	// Clan pclan = getPlayerClan(p);
	// Clan kclan = getPlayerClan(killer);
	//
	// if(pclan != null && kclan != null) {
	// rankdifference = getClanRankEnum(kclan).ordinal() -
	// getClanRankEnum(pclan).ordinal();
	// }
	//
	// gain += 7 * rankdifference;
	// loss += 2 * rankdifference;
	//
	// if(gain >= 16) {
	// gain = 16;
	// }
	// if(loss >= 16) {
	// loss = 16;
	// }
	//
	//
	// if(kclan != null) {
	// int money = getRandom(10, 100);
	// addKill(pclan);
	// addMoney(pclan, money);
	// addElo(pclan, gain);
	// killer.sendMessage("§f§m-----------");
	// killer.sendMessage("§6§lClan Status:");
	// killer.sendMessage("§a+1 §fKills");
	// killer.sendMessage("§a+" + money + " §fCoins");
	// killer.sendMessage("§a+" + gain + " §fElo");
	// killer.sendMessage("§f§m-----------");
	// }
	//
	// if(pclan != null) {
	// addDeath(pclan);
	// removeElo(pclan, loss);
	// p.sendMessage("§f§m-----------");
	// p.sendMessage("§c§lClan Status:");
	// p.sendMessage("§c+1 §fDeath");
	// p.sendMessage("§c-" + loss + " §fElo");
	// p.sendMessage("§f§m-----------");
	// }
	//
	//
	// }
	//

	public void addKill(Clan clan) {
		if (clan == null) {
			return;
		}
		int kills = clan.getStatus().getKills() + 1;
		getPlugin().connect.SQLQuerySync("UPDATE `clans_status` SET `kills`=" + kills + " WHERE `name`='" + clan.getName() + "';");

	}

	public void addDeath(Clan clan) {
		if (clan == null) {
			return;
		}
		int deaths = clan.getStatus().getDeaths() + 1;
		getPlugin().connect.SQLQuerySync("UPDATE `clans_status` SET `deaths`=" + deaths + " WHERE `name`='" + clan.getName() + "';");
	}

	public void addWins(Clan clan) {
		if (clan == null) {
			return;
		}
		int wins = clan.getStatus().getWins() + 1;
		getPlugin().connect.SQLQuerySync("UPDATE `clans_status` SET `wins`=" + wins + " WHERE `name`='" + clan.getName() + "';");

	}

	public void addElo(Clan clan, int amount) {
		if (clan == null) {
			return;
		}
		int elo = clan.getStatus().getElo() + amount;
		getPlugin().connect.SQLQuerySync("UPDATE `clans_status` SET `elo`=" + elo + " WHERE `name`='" + clan.getName() + "';");
	}

	public void removeElo(Clan clan, int amount) {
		if (clan == null) {
			return;
		}
		int elo = clan.getStatus().getElo() - amount;
		if (elo < 0) {
			elo = 0;
		}
		getPlugin().connect.SQLQuerySync("UPDATE `clans_status` SET `elo`=" + elo + " WHERE `name`='" + clan.getName() + "';");

	}

	public void addMoney(Clan clan, int amount) {
		if (clan == null) {
			return;
		}
		int multiplier = getPlugin().getMultiplierManager().getActiveMultiplier();
		int money = clan.getStatus().getMoney() + amount * multiplier;
		getPlugin().connect.SQLQuerySync("UPDATE `clans_status` SET `coins`=" + money + " WHERE `name`='" + clan.getName() + "';");

	}

	public enum Rank {
		BRONZE,
		SILVER,
		GOLD,
		DIAMOND,
		PLATINUM,
		WOMBO;
	}

	public void joinClan(final Player p, final String nome) {
		Clan clan = getPlayerClan(p);
		if (clan != null) {
			p.sendMessage(ChatColor.RED + "Você já esta em um clan.");
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
			return;
		}
		if (!invites.containsKey(p.getUniqueId())) {
			p.sendMessage(ChatColor.RED + "Você não possui nenhum convite para clans.");
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
			return;
		}
		if (!invites.get(p.getUniqueId()).equals(nome)) {
			p.sendMessage(ChatColor.RED + "Você não foi convidado para entrar nesse clan.");
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
			return;
		}
		invites.remove(p.getUniqueId());
		getPlugin().connect.SQLQuerySync("INSERT INTO `clans_players` (`name`, `uuid`, `rank`) VALUES ('" + nome + "','" + p.getUniqueId().toString().replace("-", "") + "','" + Member.NORMAL.toString() + "')");
		p.sendMessage(ChatColor.YELLOW + "Você entrou para o clan " + ChatColor.GREEN + nome + ChatColor.YELLOW + " com sucesso!");
		clanMessage(p, ChatColor.RED + p.getName() + " entrou para o clan!");
		p.playSound(p.getLocation(), Sound.LEVEL_UP, 10.0F, 10.0F);
		if (getPlugin().type == ServerType.HG) {
			me.caio.HungerGames.Main.getInstance().getScoreboardManager().updateClan(p);
		}

	}

	public void disbandClan(Player p) {
		Clan clan = getPlayerClan(p);
		if (clan == null) {
			p.sendMessage(ChatColor.RED + "Você não esta em nenhum clan.");
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
			return;
		}
		if (!clan.isOwner(p.getUniqueId())) {
			p.sendMessage(ChatColor.RED + "Apenas o dono do clan pode desfazer.");
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
			return;
		}
		clanMessage(p, "§c" + p.getName() + " desfez o clan!");
		// updateClanScoreboard(p);
		getPlugin().connect.SQLQuerySync("DELETE FROM `clans` WHERE `name`='" + clan.getName() + "';");
		getPlugin().connect.SQLQuerySync("DELETE FROM `clans_players` WHERE `name`='" + clan.getName() + "';");
		getPlugin().connect.SQLQuerySync("DELETE FROM `clans_status` WHERE `name`='" + clan.getName() + "';");
		p.sendMessage(ChatColor.YELLOW + "Você desfez o clan com sucesso.");
	}

	public void updateClanScoreboard(Player p) {
		Clan c = getPlayerClan(p);
		for (Player online : Bukkit.getOnlinePlayers()) {
			if (c.getPlayersUUID().contains(online.getUniqueId())) {
				if (getPlugin().type == ServerType.HG) {
					me.caio.HungerGames.Main.getInstance().getScoreboardManager().updateClan(online);
				}
			}
		}
	}

	public void clanMessage(Player p, String message) {
		Clan c = getPlayerClan(p);
		for (Player online : Bukkit.getOnlinePlayers()) {
			if (c.getPlayersUUID().contains(online.getUniqueId())) {
				online.sendMessage("§7[§cCLAN§7] " + message);
			}
		}
	}

	public void clanPlayerMessage(Player p, String message) {
		Clan c = getPlayerClan(p);
		for (Player online : Bukkit.getOnlinePlayers()) {
			if (c.getPlayersUUID().contains(online.getUniqueId())) {
				online.sendMessage("§7- §6[Clan] " + p.getDisplayName() + "§8: §f" + message);
			}
		}
	}

	public void leaveClan(Player p) {
		Clan clan = getPlayerClan(p);
		if (clan == null) {
			p.sendMessage(ChatColor.RED + "Você não esta em nenhum clan.");
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
			return;
		}
		if (clan.isOwner(p.getUniqueId())) {
			p.sendMessage(ChatColor.YELLOW + "Você não pode abandonar o clan pois você é o dono.");
			p.sendMessage(ChatColor.YELLOW + "Para excluir o clan use /clan excluir");
			return;
		}
		clanMessage(p, ChatColor.YELLOW + p.getName() + " saiu do clan!");
		p.sendMessage(ChatColor.YELLOW + "Você saiu do clan " + ChatColor.GREEN + clan.getName() + ChatColor.YELLOW + "!");
		clan.removePlayer(p.getUniqueId());
		getPlugin().connect.SQLQuerySync("DELETE FROM `clans_players` WHERE `uuid`='" + p.getUniqueId().toString().replace("-", "") + "';");
		if (getPlugin().type == ServerType.HG) {
			me.caio.HungerGames.Main.getInstance().getScoreboardManager().updateClan(p);
		}

	}

	public void promotePlayer(Player p, Player promoted) {
		Clan c1 = getPlayerClan(p);
		if (c1 == null) {
			p.sendMessage(ChatColor.RED + "Você não esta em nenhum clan.");
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
			return;
		}
		Clan c2 = getPlayerClan(promoted);
		if (c2 == null) {
			p.sendMessage(ChatColor.RED + promoted.getName() + " não esta em nenhum clan.");
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
			return;
		}
		if (!c2.getName().equalsIgnoreCase(c1.getName())) {
			p.sendMessage(ChatColor.RED + promoted.getName() + " não é do seu clan.");
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
			return;
		}
		if ((!c1.isOwner(p.getUniqueId())) && (!c1.isAdmin(p.getUniqueId()))) {
			p.sendMessage(ChatColor.RED + "Apenas admins e o dono do clan podem promover players.");
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
			return;
		}
		if (c1.isAdmin(promoted.getUniqueId())) {
			p.sendMessage(ChatColor.RED + promoted.getName() + " já é admin.");
			return;
		}
		if (c1.isOwner(promoted.getUniqueId())) {
			p.sendMessage(ChatColor.RED + promoted.getName() + " é dono e não pode ser promovido.");
			return;
		}
		getPlugin().connect.SQLQuery("UPDATE `clans_players` set `rank`='" + Member.ADMIN.toString() + "' WHERE `uuid`='" + promoted.getUniqueId().toString().replace("-", "") + "';");
		clanMessage(p, ChatColor.YELLOW + p.getName() + " promoveu " + promoted.getName() + " para admin!");

	}

	public void kickPlayer(Player p, Player kicked) {
		Clan c1 = getPlayerClan(p);
		if (c1 == null) {
			p.sendMessage(ChatColor.RED + "Você não esta em nenhum clan.");
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
			return;
		}
		Clan c2 = getPlayerClan(kicked);
		if (c2 == null) {
			p.sendMessage(ChatColor.RED + kicked.getName() + " não esta em nenhum clan.");
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
			return;
		}
		if (!c2.getName().equalsIgnoreCase(c1.getName())) {
			p.sendMessage(ChatColor.RED + kicked.getName() + " não é do seu clan.");
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
			return;
		}
		if ((!c1.isOwner(p.getUniqueId())) && (!c1.isAdmin(p.getUniqueId()))) {
			p.sendMessage(ChatColor.RED + "Apenas admins e o dono do clan podem expulsar players.");
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
			return;
		}
		if (c1.isOwner(kicked.getUniqueId())) {
			p.sendMessage(ChatColor.RED + kicked.getName() + " é dono e não pode ser expulso.");
			return;
		}
		if (isOnClanChat(kicked)) {
			removeFromClanChat(kicked);
		}
		getPlugin().connect.SQLQuery("DELETE FROM `clans_players` WHERE `uuid`='" + kicked.getUniqueId().toString().replace("-", "") + "';");
		clanMessage(p, ChatColor.YELLOW + p.getName() + " expulsou " + kicked.getName() + " do clan!");
		kicked.sendMessage(ChatColor.RED + "Você foi expulso do clan por " + p.getName());
		if (getPlugin().type == ServerType.HG) {
			me.caio.HungerGames.Main.getInstance().getScoreboardManager().updateClan(kicked);
		}

	}

	public void demotePlayer(Player p, Player demoted) {
		Clan c1 = getPlayerClan(p);
		if (c1 == null) {
			p.sendMessage(ChatColor.RED + "Você não esta em nenhum clan.");
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
			return;
		}
		Clan c2 = getPlayerClan(demoted);
		if (c2 == null) {
			p.sendMessage(ChatColor.RED + demoted.getName() + " não esta em nenhum clan.");
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
			return;
		}
		if (!c2.getName().equalsIgnoreCase(c1.getName())) {
			p.sendMessage(ChatColor.RED + demoted.getName() + " não é do seu clan.");
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
			return;
		}
		if ((!c1.isOwner(p.getUniqueId())) && (!c1.isAdmin(p.getUniqueId()))) {
			p.sendMessage(ChatColor.RED + "Apenas admins e o dono do clan podem promover/rebaixar players.");
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
			return;
		}
		if (c1.isOwner(demoted.getUniqueId())) {
			p.sendMessage(ChatColor.RED + demoted.getName() + " é dono e não pode ser rebaixado.");
			return;
		}
		if (!c1.isAdmin(demoted.getUniqueId())) {
			p.sendMessage(ChatColor.RED + demoted.getName() + " não é admin.");
			return;
		}
		getPlugin().connect.SQLQuery("UPDATE `clans_players` set `rank`='" + Member.NORMAL.toString() + "' WHERE `uuid`='" + demoted.getUniqueId().toString().replace("-", "") + "';");
		clanMessage(p, ChatColor.YELLOW + p.getName() + " rebaixou " + demoted.getName() + " para default!");

	}

	public void sendInvite(Player p, Player invited) {
		Clan c1 = getPlayerClan(p);
		Clan c2 = getPlayerClan(invited);

		if (c2 != null) {
			p.sendMessage(ChatColor.RED + invited.getName() + " já esta em um clan.");
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
			return;
		}
		if (c1 == null) {
			p.sendMessage(ChatColor.RED + "Você não esta em nenhum clan.");
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
			return;
		}
		if ((!c1.isOwner(p.getUniqueId())) && (!c1.isAdmin(p.getUniqueId()))) {
			p.sendMessage(ChatColor.RED + "Apenas admins e o dono do clan podem convidar players.");
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
			return;
		}
		if (invites.containsKey(invited.getUniqueId())) {
			if (invites.get(invited.getUniqueId()).equals(c1.getName())) {
				p.sendMessage(ChatColor.RED + invited.getName() + " ja foi invitado para esse clan.");
				p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
				return;
			}
		}
		invites.put(invited.getUniqueId(), c1.getName());
		clanMessage(p, ChatColor.YELLOW + p.getName() + " invitou " + invited.getName() + " para o clan!");
		p.sendMessage(ChatColor.YELLOW + "Você invitou " + invited.getName() + " para o clan " + ChatColor.GREEN + c1.getName() + ChatColor.YELLOW + " com sucesso!");
		invited.sendMessage(ChatColor.YELLOW + "Você foi chamado para o clan " + ChatColor.GREEN + c1.getName() + ChatColor.YELLOW + " por " + p.getName() + "!");
		invited.sendMessage(ChatColor.YELLOW + "Use /clan entrar " + ChatColor.GREEN + c1.getName() + ChatColor.YELLOW + " para entrar.");
		invited.playSound(invited.getLocation(), Sound.NOTE_PLING, 10.0F, 10.0F);
		p.playSound(p.getLocation(), Sound.LEVEL_UP, 10.0F, 10.0F);
	}

	public void createClan(Player p, String nome, String tag) throws SQLException {
		Clan c = getPlayerClan(p);
		if (c != null) {
			p.sendMessage(ChatColor.RED + "Você ja esta em um clan.");
			p.sendMessage(ChatColor.RED + "Para mais informações use: /clan");
			return;
		}
	/*	PlayerRank rank = getPlugin().getRankManager().getPlayerRank(p);
		if (rank.getCoins() < 25000) {
			p.sendMessage(ChatColor.YELLOW + "O preço para criar um clan é de 25,000 WC.");
			return;
		}
*/		if (nameExist(nome)) {
			p.sendMessage(ChatColor.RED + "Esse nome ja esta em uso.");
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
			return;
		}

		if (validateTag(tag)) {
			p.sendMessage(ChatColor.RED + "Você não pode escolher essa tag.");
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
			return;
		}

		if (tagExist(tag)) {
			p.sendMessage(ChatColor.RED + "Essa tag ja esta em uso.");
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
			return;
		}

		Clan clan = new Clan(this, nome, tag, new ClanStatus(0, 0, 0, 0, 0));
		clan.setOwner(p.getUniqueId());
		saveClan(clan);
		if (getPlugin().type == ServerType.HG) {
			me.caio.HungerGames.Main.getInstance().getScoreboardManager().updateClan(p);
		}
		p.sendMessage(ChatColor.YELLOW + "Clan criado com sucesso!");
		p.sendMessage(ChatColor.YELLOW + "Use /clan para mais informações.");
	//	rank.removeCoins(25000);
		PlayerTitle title = getPlugin().getTitleManager().getPlayerTitle(p);
		title.addTitle(11);
		p.sendMessage(ChatColor.YELLOW + "Você recebeu o titulo:" + ChatColor.GREEN + " Clan");
		p.sendMessage(ChatColor.YELLOW + "Escolha ele pelo menu para destacar a tag do seu clan.");
		p.playSound(p.getLocation(), Sound.LEVEL_UP, 10.0F, 10.0F);

	}

	public boolean validateTag(String s) {
		for (Tag t : Tag.values()) {
			if (s.equalsIgnoreCase(t.name())) {
				return true;
			}
		}
		return false;
	}

	public boolean nameExistJoin(final String nome) throws SQLException {
		boolean exist = false;
		PreparedStatement stmt = getPlugin().mainConnection.prepareStatement("SELECT name FROM `clans` WHERE `name`='" + nome + "';");
		ResultSet result = stmt.executeQuery();
		if (result.next()) {
			if (result.equals(nome)) {
				exist = true;
			}
		}
		result.close();
		stmt.close();
		return exist;
	}

	public boolean nameExist(final String nome) throws SQLException {
		boolean exist = false;
		PreparedStatement stmt = getPlugin().mainConnection.prepareStatement("SELECT name FROM `clans` WHERE `name`='" + nome + "';");
		ResultSet result = stmt.executeQuery();
		if (result.next()) {
			exist = true;
		}
		result.close();
		stmt.close();
		return exist;
	}

	public boolean tagExist(final String tag) throws SQLException {
		boolean exist = false;
		PreparedStatement stmt = getPlugin().mainConnection.prepareStatement("SELECT tag FROM `clans` WHERE `tag`='" + tag + "';");
		ResultSet result = stmt.executeQuery();
		if (result.next()) {
			exist = true;
		}
		result.close();
		stmt.close();
		return exist;
	}

	public Clan loadPlayerClan(Player p) throws SQLException {
		Clan clan = new Clan(this);
		boolean exists = true;
		PreparedStatement stmt = getPlugin().mainConnection.prepareStatement("SELECT * FROM `clans_players` WHERE `uuid`='" + p.getUniqueId().toString().replace("-", "") + "';");
		ResultSet result = stmt.executeQuery();
		if (result.next()) {
			clan.setName(result.getString("name"));
			clan.addPlayer(getPlugin().name.makeUUID(result.getString("uuid")), Member.valueOf(result.getString("rank")));
		} else {
			exists = false;
		}
		result.close();
		stmt.close();
		if (!exists) {
			System.out.println("CLANS > NAO CARREGOU PQ N EXISTE.");
			return null;
		}

		stmt = getPlugin().mainConnection.prepareStatement("SELECT * FROM `clans_players` WHERE `name`='" + clan.getName() + "';");
		result = stmt.executeQuery();
		while (result.next()) {
			clan.addPlayer(getPlugin().name.makeUUID(result.getString("uuid")), Member.valueOf(result.getString("rank")));
		}
		result.close();
		stmt.close();

		stmt = getPlugin().mainConnection.prepareStatement("SELECT * FROM `clans` WHERE `name`='" + clan.getName() + "';");
		result = stmt.executeQuery();
		if (result.next()) {
			clan.setTag(result.getString("tag"));
		}
		result.close();
		stmt.close();

		stmt = getPlugin().mainConnection.prepareStatement("SELECT * FROM `clans_status` WHERE `name`='" + clan.getName() + "';");
		result = stmt.executeQuery();
		if (result.next()) {
			ClanStatus status = new ClanStatus(result.getInt("kills"), result.getInt("deaths"), result.getInt("wins"), result.getInt("coins"), result.getInt("elo"));
			clan.setStatus(status);
		}

		result.close();
		stmt.close();
		// if(getPlugin().type == ServerType.HG) {
		// me.caio.HungerGames.Main.getInstance().getScoreboardManager().updateClan(p);
		// }
		// System.out.println("============================");
		// System.out.println("CLAN " + clan.getName() + " CARREGADO COM
		// SUCESSO!");
		// System.out.println("NOME: " + clan.getName());
		// System.out.println("TAG: " + clan.getTag());
		// System.out.println("DONO: " + clan.getOwner());
		// System.out.println("STATUS: " + clan.getStatus().getKills() + " - " +
		// clan.getStatus().getDeaths() + " - " + clan.getStatus().getWins() + "
		// - " + clan.getStatus().getMoney() + " - " +
		// clan.getStatus().getElo());
		// System.out.println("============================");

		return clan;

	}

	public Clan loadClanByName(String nome) throws SQLException {
		Clan clan = new Clan(this);
		boolean exists = true;
		PreparedStatement stmt = getPlugin().mainConnection.prepareStatement("SELECT * FROM `clans` WHERE `tag`='" + nome + "';");
		ResultSet result = stmt.executeQuery();
		if (result.next()) {
			clan.setName(result.getString("name"));
			clan.setTag(result.getString("tag"));
		} else {
			exists = false;
		}
		result.close();
		stmt.close();
		if (!exists) {
			return null;
		}
		stmt = getPlugin().mainConnection.prepareStatement("SELECT * FROM `clans_players` WHERE `name`='" + clan.getName() + "';");
		result = stmt.executeQuery();
		while (result.next()) {
			clan.addPlayer(getPlugin().name.makeUUID(result.getString("uuid")), Member.valueOf(result.getString("rank")));
		}
		result.close();
		stmt.close();

		stmt = getPlugin().mainConnection.prepareStatement("SELECT * FROM `clans_status` WHERE `name`='" + clan.getName() + "';");
		result = stmt.executeQuery();
		if (result.next()) {
			ClanStatus status = new ClanStatus(result.getInt("kills"), result.getInt("deaths"), result.getInt("wins"), result.getInt("coins"), result.getInt("elo"));
			clan.setStatus(status);
		}

		result.close();
		stmt.close();
		// if(getPlugin().type == ServerType.HG) {
		// me.caio.HungerGames.Main.getInstance().getScoreboardManager().updateClan(p);
		// }
		// System.out.println("============================");
		// System.out.println("CLAN " + clan.getName() + " CARREGADO COM
		// SUCESSO!");
		// System.out.println("NOME: " + clan.getName());
		// System.out.println("TAG: " + clan.getTag());
		// System.out.println("DONO: " + clan.getOwner());
		// System.out.println("STATUS: " + clan.getStatus().getKills() + " - " +
		// clan.getStatus().getDeaths() + " - " + clan.getStatus().getWins() + "
		// - " + clan.getStatus().getMoney() + " - " +
		// clan.getStatus().getElo());
		// System.out.println("============================");

		return clan;

	}

	public void saveClan(Clan clan) throws SQLException {
		// Connect.lock.lock();
		PreparedStatement stmt = getPlugin().mainConnection.prepareStatement("SELECT * FROM `clans` WHERE `name`='" + clan.getName() + "';");
		ResultSet result = stmt.executeQuery();
		if (result.next()) {
			stmt.execute("UPDATE `clans` SET `name`='" + clan.getName() + "', `tag`='" + clan.getTag() + "';");
		} else {
			stmt.execute("INSERT INTO `clans` (`name`, `tag`) VALUES ('" + clan.getName() + "', '" + clan.getTag() + "')");
		}
		result.close();
		stmt.close();

		for (UUID uuid : clan.getPlayersUUID()) {
			stmt = getPlugin().mainConnection.prepareStatement("SELECT * FROM `clans_players` WHERE `uuid`='" + uuid.toString().replace("-", "") + "';");
			result = stmt.executeQuery();
			if (result.next()) {
				stmt.execute("UPDATE `clans_players` WHERE `name`='" + clan.getName() + "', `uuid`='" + uuid.toString().replace("-", "") + "' AND rank='" + clan.getPlayerMember(uuid) + "';");
			} else {
				stmt.execute("INSERT INTO `clans_players` (`name`, `uuid`, `rank`) VALUES ('" + clan.getName() + "', '" + uuid.toString().replace("-", "") + "', '" + clan.getPlayerMember(uuid) + "')");
			}
		}
		result.close();
		stmt.close();

		stmt = getPlugin().mainConnection.prepareStatement("SELECT * FROM `clans_status` WHERE `name`='" + clan.getName() + "';");
		result = stmt.executeQuery();
		if (result.next()) {
			stmt.execute("UPDATE `clans_status` SET `name`='" + clan.getName() + "', `kills`='" + clan.getStatus().getKills() + "', `deaths`='" + clan.getStatus().getDeaths() + "', `wins`='" + clan.getStatus().getWins()
					+ "', `coins`='" + clan.getStatus().getMoney() + "', `elo`='" + clan.getStatus().getElo() + "';");
		} else {
			stmt.execute("INSERT INTO `clans_status` (`name`, `kills`, `deaths`, `wins`, `coins`, `elo`) VALUES ('" + clan.getName() + "', '" + clan.getStatus().getKills() + "', '" + clan.getStatus().getDeaths() + "', '"
					+ clan.getStatus().getWins() + "', '" + clan.getStatus().getMoney() + "', '" + clan.getStatus().getElo() + "')");
		}
		result.close();
		stmt.close();
		// Connect.lock.unlock();

	}

	public void saveStatus(Clan clan) throws SQLException {
		// Connect.lock.lock();
		PreparedStatement stmt = getPlugin().mainConnection.prepareStatement("SELECT * FROM `clans_status` WHERE `name`='" + clan.getName() + "';");
		ResultSet result = stmt.executeQuery();
		if (result.next()) {
			stmt.execute("UPDATE `clans_status` SET `name`='" + clan.getName() + "', `kills`='" + clan.getStatus().getKills() + "', `deaths`='" + clan.getStatus().getDeaths() + "', `wins`='" + clan.getStatus().getWins()
					+ "', `coins`='" + clan.getStatus().getMoney() + "', `elo`='" + clan.getStatus().getElo() + "';");
		} else {
			stmt.execute("INSERT INTO `clans_status` (`name`, `kills`, `deaths`, `wins`, `coins`, `elo`) VALUES ('" + clan.getName() + "', '" + clan.getStatus().getKills() + "', '" + clan.getStatus().getDeaths() + "', '"
					+ clan.getStatus().getWins() + "', '" + clan.getStatus().getMoney() + "', '" + clan.getStatus().getElo() + "')");
		}
		result.close();
		stmt.close();
		// Connect.lock.unlock();

	}

/*	public void spawnTop10Hologram(Player p, Location loc) throws SQLException {
		if(!names.isEmpty()) {
		HologramV2 holo = new HologramV2("§f§m-----------------------------", "§6§lTop 10 Clans: §e§lRanking",
				"§a1. §a" + names.get(0) + " §f[" + getClanColorByElo(elos.get(0)) + getTagByName(names.get(0)) + "§f]" + " §fElo§8: §c" + elos.get(0) + " " + getClanRankByElo(elos.get(0)),
				"§a2. §a" + names.get(1) + " §f[" + getClanColorByElo(elos.get(1)) + getTagByName(names.get(1)) + "§f]" + " §fElo§8: §c" + elos.get(1) + " " + getClanRankByElo(elos.get(1)),
				"§a3. §a" + names.get(2) + " §f[" + getClanColorByElo(elos.get(2)) + getTagByName(names.get(2)) + "§f]" + " §fElo§8: §c" + elos.get(2) + " " + getClanRankByElo(elos.get(2)),
				"§a4. §a" + names.get(3) + " §f[" + getClanColorByElo(elos.get(3)) + getTagByName(names.get(3)) + "§f]" + " §fElo§8: §c" + elos.get(3) + " " + getClanRankByElo(elos.get(3)),
				"§a5. §a" + names.get(4) + " §f[" + getClanColorByElo(elos.get(4)) + getTagByName(names.get(4)) + "§f]" + " §fElo§8: §c" + elos.get(4) + " " + getClanRankByElo(elos.get(4)),
				"§a6. §a" + names.get(5) + " §f[" + getClanColorByElo(elos.get(5)) + getTagByName(names.get(5)) + "§f]" + " §fElo§8: §c" + elos.get(5) + " " + getClanRankByElo(elos.get(5)),
				"§a7. §a" + names.get(6) + " §f[" + getClanColorByElo(elos.get(6)) + getTagByName(names.get(6)) + "§f]" + " §fElo§8: §c" + elos.get(6) + " " + getClanRankByElo(elos.get(6)),
				"§a8. §a" + names.get(7) + " §f[" + getClanColorByElo(elos.get(7)) + getTagByName(names.get(7)) + "§f]" + " §fElo§8: §c" + elos.get(7) + " " + getClanRankByElo(elos.get(7)),
				"§a9. §a" + names.get(8) + " §f[" + getClanColorByElo(elos.get(8)) + getTagByName(names.get(8)) + "§f]" + " §fElo§8: §c" + elos.get(8) + " " + getClanRankByElo(elos.get(8)),
				"§a10. §a" + names.get(9) + " §f[" + getClanColorByElo(elos.get(9)) + getTagByName(names.get(9)) + "§f]" + " §fElo§8: §c" + elos.get(9) + " " + getClanRankByElo(elos.get(9)),
				"§f§m-----------------------------");
		holo.show(p, loc);
		}
	}
*/
	
	public boolean isOnTop10(String name) {
		if (names.contains(name)) {
			return true;
		}
		return false;
	}

	public String getTagByName(String name) {
		if (tags.containsKey(name)) {
			return tags.get(name);
		}
		return "";
	}

	// public String getTagByName(String name) throws SQLException {
	// Connect.lock.lock();
	// PreparedStatement stmt =
	// getPlugin().mainConnection.prepareStatement("SELECT * FROM clans WHERE
	// `name`='" + name + "';");
	// ResultSet result = stmt.executeQuery();
	// if(result.next()) {
	// return result.getString("tag");
	// }
	// result.close();
	// stmt.close();
	// Connect.lock.unlock();
	// return null;
	// }

	public ArrayList<String> names;
	public ArrayList<Integer> elos;
	public HashMap<String, String> tags;

	public void loadTop10() {
		names = new ArrayList<>();
		elos = new ArrayList<>();
		tags = new HashMap<>();
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					PreparedStatement stmt = getPlugin().mainConnection.prepareStatement("SELECT elo, name FROM clans_status ORDER BY elo DESC LIMIT 10");
					ResultSet result = stmt.executeQuery();
					while (result.next()) {
						names.add(result.getString("name"));
						elos.add(result.getInt("elo"));
					}
					result.close();
					stmt.close();

					for (String name : names) {
						stmt = getPlugin().mainConnection.prepareStatement("SELECT * FROM clans WHERE `name`='" + name + "';");
						result = stmt.executeQuery();
						while (result.next()) {
							tags.put(result.getString("name"), result.getString("tag"));
						}
					}
					stmt.close();
					result.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.runTaskTimerAsynchronously(getPlugin(), 0L, 300 * 20);
	}

	private final Random rand = new Random();

	private int getRandom(int min, int max) {
		return rand.nextInt(max - min) + min;
	}

}
