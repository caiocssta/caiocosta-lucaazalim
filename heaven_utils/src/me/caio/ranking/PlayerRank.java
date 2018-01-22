package me.skater.ranking;

import java.util.ArrayList;
import java.util.UUID;

import me.skater.Enums.ServerType;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerRank {
	private UUID uuid;
	private int xp;
	private int xptotal;
	private int level;
	private int coins;
	private RankingManager manager;
	private ArrayList<Integer> rewards;

	public PlayerRank(RankingManager manager, UUID uuid) {
		this(manager, uuid, 0, 0, 0, 0);
	}

	public PlayerRank(RankingManager manager, UUID uuid, int coins, int xp, int xptotal, int level) {
		this.manager = manager;
		this.uuid = uuid;
		this.xp = xp;
		this.xptotal = xptotal;
		this.level = level;
		this.coins = coins;
		this.rewards = new ArrayList<>();

	}

	public ArrayList<Integer> getRewards() {
		return this.rewards;
	}

	public int claimReward(int id) {
		if (!rewards.contains(id)) {
			this.rewards.add(id);
			save();
		}
		return id;
	}

	public int claimRewardNoSave(int id) {
		if (!rewards.contains(id)) {
			this.rewards.add(id);
		}
		return id;
	}

	public int getLevel() {
		return this.level;
	}

	public String getLevelName(Integer level) {
		String retorno = "";
		switch (level) {
		case 0:
			retorno = "Unranked";
		case 1:
			retorno = "Primary";
		case 2:
			retorno = "Advanced";
		case 3:
			retorno = "Expert";
		case 4:
			retorno = "Silver";
		case 5:
			retorno = "Gold";
		case 6:
			retorno = "Diamond";
		case 7:
			retorno = "Saphire";
		case 8:
			retorno = "Emerald";
		case 9:
			retorno = "Crystal";
		case 10:
			retorno = "Elite";
		case 11:
			retorno = "Master";
		case 12:
			retorno = "Legendary";
		default:
			retorno = "Unranked";
			break;
		}

		return retorno;
	}

	public int getCoins() {
		return this.coins;
	}

	public int getTotalXP() {
		return this.xptotal;
	}

	public UUID getUuid() {
		return this.uuid;
	}

	public int getXp() {
		return this.xp;
	}

	private void save() {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					PlayerRank.this.manager.savePlayerRank(PlayerRank.this);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(manager.getPlugin());
	}

	public Level getRank() {
		return Level.getLevel(level);
	}

	public void levelUp() {
		level++;
		resetXp();
	}

	public int addXp(int xp) {
		int multiplier = manager.getPlugin().getMultiplierManager().getActiveMultiplier();
		int plus = xp * multiplier;
		this.xp += plus;
		this.xptotal += plus;
		if (Bukkit.getPlayer(uuid) != null) {
		//	Bukkit.getPlayer(uuid).sendMessage(("§a+" + plus + " §7XP (§a▲" + getXp() + "§7)"));
		//	if (manager.getPlugin().type == ServerType.HG) {
		//		me.caio.HungerGames.Main.getInstance().getScoreboardManager().updatePlayerRank(Bukkit.getPlayer(uuid));
		//	}
			manager.checkLevelUp(Bukkit.getPlayer(uuid));
		}
		save();
		return plus;
	}

	public int addXpNoMultiplier(int xp) {
		int multiplier = 1;
		int plus = xp * multiplier;
		this.xp += plus;
		this.xptotal += plus;
		if (Bukkit.getPlayer(uuid) != null) {
		//	Bukkit.getPlayer(uuid).sendMessage(("§a+" + plus + " §7XP (§a▲" + getXp() + "§7)"));
		//	if (manager.getPlugin().type == ServerType.HG) {
		//		me.caio.HungerGames.Main.getInstance().getScoreboardManager().updatePlayerRank(Bukkit.getPlayer(uuid));
		//	}
			manager.checkLevelUp(Bukkit.getPlayer(uuid));
		}
		save();
		return plus;
	}

	/*
	 * public int removeCoins(int coins) { this.coins -= coins; if (this.coins <
	 * 0) { this.coins = 0; } if (Bukkit.getPlayer(uuid) != null) {
	 * Bukkit.getPlayer(uuid).sendMessage(("§c-" + coins + " §7ⓌCoins (§c▼" +
	 * getCoins() + "§7)")); if (manager.getPlugin().type == ServerType.HG) {
	 * me.caio.HungerGames.Main.getInstance().getScoreboardManager().
	 * updatePlayerRank(Bukkit.getPlayer(uuid)); } } save(); return this.coins;
	 * }
	 * 
	 * public int addCoins(int coins) { int multiplier =
	 * manager.getPlugin().getMultiplierManager().getActiveMultiplier(); int
	 * plus = coins * multiplier; this.coins += plus; if (Bukkit.getPlayer(uuid)
	 * != null) { Bukkit.getPlayer(uuid).sendMessage(("§a+" + plus +
	 * " §7ⓌCoins (§a▲" + getCoins() + "§7)")); if (manager.getPlugin().type ==
	 * ServerType.HG) {
	 * me.caio.HungerGames.Main.getInstance().getScoreboardManager().
	 * updatePlayerRank(Bukkit.getPlayer(uuid)); } } save(); return plus; }
	 * 
	 * public int addCoinsNoMultiplier(int coins) { int multiplier = 1; int plus
	 * = coins * multiplier; this.coins += plus; if (Bukkit.getPlayer(uuid) !=
	 * null) { Bukkit.getPlayer(uuid).sendMessage(("§a+" + plus +
	 * " §7ⓌCoins (§a▲" + getCoins() + "§7)")); if (manager.getPlugin().type ==
	 * ServerType.HG) {
	 * me.caio.HungerGames.Main.getInstance().getScoreboardManager().
	 * updatePlayerRank(Bukkit.getPlayer(uuid)); } } save(); return plus; }
	 */
	public int resetXp() {
		this.xp = 0;
		save();
		if (manager.getPlugin().type == ServerType.HG) {
		//	if (Bukkit.getPlayer(uuid) != null) {
		//		me.caio.HungerGames.Main.getInstance().getScoreboardManager().updatePlayerRank(Bukkit.getPlayer(uuid));
		//	}
		}
		return this.xp;
	}

}
