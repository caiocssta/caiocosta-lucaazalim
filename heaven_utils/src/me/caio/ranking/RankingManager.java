package me.skater.ranking;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.skater.Main;
import me.skater.Management;
import me.skater.Enums.ServerType;
import me.skater.MySQL.Connect;
import me.skater.Utils.ItemBuilder;
import me.skater.Utils.StringUtils;
import me.skater.titles.PlayerTitle;
import me.skater.titles.ReflectionUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RankingManager extends Management {
	public HashMap<UUID, PlayerRank> prank;
	public ItemStack gui;
	public ItemStack clans;

	public List<String> firstjoin;
	public ItemStack voltar;

	public RankingManager(Main main) {
		super(main);
	}

	@Override
	public void onEnable() {
		this.prank = new HashMap<UUID, PlayerRank>();

		this.gui = new ItemBuilder().setType(Material.BREWING_STAND_ITEM).setName("§eHeavenMC Levels").getStack();
		this.clans = new ItemBuilder().setType(Material.IRON_FENCE).setName("§eHeavenMC Clans")
				.setDescription(ChatColor.WHITE + StringUtils.line, "              §6HeavenMC§8: §eClans", ChatColor.WHITE + StringUtils.line, "§f/clan criar <nome> <tag>§8: §7Crie seu clan. §c(25,000 WC)",
						"§f/clan entrar <nome> §8: §7Entre para um clan", "§f/clan invite <jogador>§8: §7Convide alguem para seu clan.", "§f/clan sair§8: §7Saia do seu clan.", "§f/clan excluir§8: §7Excluia o seu clan.",
						"§f/clan promover <nick>§8: §7Promova um membro.", "§f/clan rebaixar <nick>§8: §7Rebaixe um membro.", "§f/clan status§8: §7Estatisticas do seu clan.",
						"§f/clan status <tag>§8: §7Estatisticas de outro clan.", "§f/clan kick <nome>§8: §7Expulsar um membro", "§f/clan chat§8: §7Chat do clan", "§f/clan rank§8: §7Veja todos os ranks",
						ChatColor.WHITE + StringUtils.line)
				.getStack();
		this.voltar = new ItemBuilder().setType(Material.REDSTONE).setAmount(1).setName("§c§lVoltar").getStack();

		getPlugin().getServer().getPluginManager().registerEvents(new RankingListener(this), getPlugin());
		getPlugin().getCommand("rank").setExecutor(new RankCommand(getPlugin()));
		this.firstjoin = new ArrayList<String>();
		try {
			PreparedStatement stmt = null;
			ResultSet result = null;
			for (Player p : getServer().getOnlinePlayers()) {
				UUID uuid = p.getUniqueId();
				stmt = getPlugin().mainConnection.prepareStatement("SELECT * FROM `playerranking` WHERE `uuid`='" + uuid.toString().replace("-", "") + "';");
				result = stmt.executeQuery();
				ArrayList<Integer> rewards = new ArrayList<Integer>();
				if (result.next()) {
					String reward = result.getString("rewards");
					if (reward.contains(",")) {
						for (String str : reward.split(",")) {
							rewards.add(Integer.valueOf(str));
						}
					} else {
						try {
							rewards.add(Integer.valueOf(reward));
						} catch (NumberFormatException e) {
						}
					}
					int coins = result.getInt("coins");
					int xp = result.getInt("xp");
					int xptotal = result.getInt("xptotal");
					int level = result.getInt("level");
					PlayerRank rank = new PlayerRank(this, uuid, coins, xp, xptotal, level);
					for (Integer a : rewards) {
						rank.claimRewardNoSave(a);
					}
					this.prank.put(uuid, rank);
				}
			}
			if (result != null) {
				result.close();
			}
			if (stmt != null) {
				stmt.close();
			}

			stmt = getPlugin().mainConnection.prepareStatement("SELECT * FROM `rewards`;");
			result = stmt.executeQuery();
			if (result.next()) {
				firstjoin.add(result.getString("Uuid"));
			}
			if (result != null) {
				result.close();
			}
			if (stmt != null) {
				stmt.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadPlayerRank(UUID uuid) throws SQLException {
		PreparedStatement stmt = getPlugin().mainConnection.prepareStatement("SELECT * FROM `playerranking` WHERE `uuid`='" + uuid.toString().replace("-", "") + "';");
		ResultSet result = stmt.executeQuery();
		ArrayList<Integer> rewards = new ArrayList<Integer>();
		if (result.next()) {
			String reward = result.getString("rewards");
			if (reward.contains(",")) {
				for (String str : reward.split(",")) {
					rewards.add(Integer.valueOf(str));
				}
			} else {
				try {
					rewards.add(Integer.valueOf(reward));
				} catch (NumberFormatException e) {
				}
			}
			int coins = result.getInt("coins");
			int xp = result.getInt("xp");
			int xptotal = result.getInt("xptotal");
			int level = result.getInt("level");
			PlayerRank rank = new PlayerRank(this, uuid, coins, xp, xptotal, level);
			for (Integer a : rewards) {
				rank.claimRewardNoSave(a);
			}
			this.prank.put(uuid, rank);
		}
		result.close();
		stmt.close();
	}

	public boolean containsPlayerRank(UUID uuid) {
		return this.prank.containsKey(uuid);
	}

	public PlayerRank getPlayerRank(UUID uuid) {
		if (this.prank.containsKey(uuid)) {
			return this.prank.get(uuid);
		}
		PlayerRank prank = new PlayerRank(this, uuid);
		this.prank.put(uuid, prank);
		return prank;
	}

	public void refreshPlayer(Player p) throws SQLException {
		PreparedStatement stmt = getPlugin().mainConnection.prepareStatement("SELECT * FROM `rewards` WHERE Uuid='" + p.getUniqueId().toString().replace("-", "") + "';");
		ResultSet result = stmt.executeQuery();
		if (result.next()) {
			if (!firstjoin.contains(result.getString("Uuid"))) {
				firstjoin.add(result.getString("Uuid"));
			}
		}
		result.close();
		stmt.close();

	}

	public void savePlayerRank(PlayerRank rank) throws SQLException {
		String rewards = "";
		for (Integer t : rank.getRewards()) {
			if (rewards.equals("")) {
				rewards = "" + t;
			} else {
				rewards = rewards + "," + t;
			}
		}
		Connect.lock.lock();
		PreparedStatement stmt = getPlugin().mainConnection.prepareStatement("SELECT * FROM `playerranking` WHERE `uuid`='" + rank.getUuid().toString().replace("-", "") + "';");
		ResultSet result = stmt.executeQuery();
		if (!result.isBeforeFirst()) {
			stmt.execute("INSERT INTO `playerranking`(`uuid`, `coins`, `rewards`, `xp`, `xptotal`) VALUES ('" + rank.getUuid().toString().replace("-", "") + "', '" + rank.getCoins() + "', '" + rewards + "', '"
					+ rank.getXp() + "','" + rank.getTotalXP() + "')");
		} else {
			stmt.execute("UPDATE `playerranking` SET coins='" + rank.getCoins() + "',rewards='" + rewards + "',xp='" + rank.getXp() + "',xptotal='" + rank.getTotalXP() + "',level='" + rank.getLevel() + "' WHERE uuid='"
					+ rank.getUuid().toString().replace("-", "") + "';");
		}
		result.close();
		stmt.close();
		Connect.lock.unlock();
	}

	public PlayerRank getPlayerRank(Player player) {
		return getPlayerRank(player.getUniqueId());
	}

	public void reward(Player p, int id) {
		PlayerRank rank = getPlayerRank(p);
/*		PlayerTitle title = getPlugin().getTitleManager().getPlayerTitle(p);
		if (id == 0) {
			rank.addCoinsNoMultiplier(2000);
			rank.addXpNoMultiplier(50);
		}
		if (id == 1) {
			rank.addCoinsNoMultiplier(5000);
			rank.addXpNoMultiplier(100);
			title.addTitle(4);
		}
		if (id == 2) {
			rank.addCoinsNoMultiplier(10000);
			rank.addXpNoMultiplier(200);
			title.addTitle(5);
		}
		if (id == 3) {
			rank.addCoinsNoMultiplier(15000);
			rank.addXpNoMultiplier(300);
		}
		if (id == 4) {
			rank.addCoinsNoMultiplier(20000);
			rank.addXpNoMultiplier(400);
			title.addTitle(6);
		}
		if (id == 5) {
			rank.addCoinsNoMultiplier(25000);
			rank.addXpNoMultiplier(500);
		}
		if (id == 6) {
			rank.addCoinsNoMultiplier(30000);
			rank.addXpNoMultiplier(600);
		}
		if (id == 7) {
			rank.addCoinsNoMultiplier(35000);
			rank.addXpNoMultiplier(700);
		}
		if (id == 8) {
			rank.addCoinsNoMultiplier(40000);
			rank.addXpNoMultiplier(800);
			title.addTitle(8);
		}
		if (id == 9) {
			rank.addCoinsNoMultiplier(45000);
			rank.addXpNoMultiplier(900);
		}
		if (id == 10) {
			rank.addCoinsNoMultiplier(50000);
			rank.addXpNoMultiplier(1000);
			title.addTitle(9);
		}
*/		rank.claimReward(id);
		checkLevelUp(p);
	}

	public void killReward(Player p) {
		int earn = 100;
		if (getPlugin().type == ServerType.KITPVP) {
			earn = 50;
		}
		PlayerRank prank = getPlayerRank(p);
		prank.addXp(earn);
	//	prank.addCoins(earn * 2);

	}

	public void checkLevelUp(Player p) {
		PlayerRank prank = getPlayerRank(p);
		if (prank.getXp() >= prank.getRank().getMax()) {
			prank.levelUp();
		//	Bukkit.broadcastMessage(p.getDisplayName() + " §esubiu para o nivel §b" + prank.getLevel() + "§e!");
			StringUtils.sendCenteredMessage(p, "");
			StringUtils.sendCenteredMessage(p, "§6HeavenMC§8: §cRanking");
			StringUtils.sendCenteredMessage(p, "§eVocê subiu para o nivel §b" + prank.getLevelName(prank.getLevel()) + "§e!");
			StringUtils.sendCenteredMessage(p, "");
			p.playSound(p.getLocation(), Sound.LEVEL_UP, 10, 10);
		}

	}

	public int getLevelPorcentage(Player p) {
		PlayerRank rank = getPlayerRank(p);
		int i = rank.getXp();
		Level r = rank.getRank();
		int i2 = r.getMax();
		int result = i * 100 / i2;
		return result;
	}

	public String getLevelPorcentageDouble(Player p) {
		PlayerRank rank = getPlayerRank(p);
		double rate = rank.getXp();
		double valorfinal = rank.getRank().getMax();
		int porcentagem = 100;
		double resultado = (rate * porcentagem) / valorfinal;
		DecimalFormat df = new DecimalFormat("##.##");
		String finall = resultado == -1 ? "0" : df.format(resultado);
		return finall;
	}

	public String PlayerRankPorcentage(Player p) {
		PlayerRank rank = getPlayerRank(p);
		double rate = rank.getXp();
		double valorfinal = rank.getRank().getMax();
		int porcentagem = 100;
		double resultado = (rate * porcentagem) / valorfinal;
		DecimalFormat df = new DecimalFormat("##.##");
		String finall = resultado == -1 ? "0" : df.format(resultado);
		return finall;
	}

	public void removePlayerRank(UUID uuid) {
		this.prank.remove(uuid);
	}

	@Override
	public void onDisable() {
		this.prank.clear();
		this.prank = null;
	}

	public void openRankSelector(Player p) {
		new RankingSelector(p, this).open();
	}

	public void openWomboMenu(Player p) {
		new PlayerSelector(p, getPlugin()).open();
	}

	public Boolean canClaim(Player p) {
		if (firstjoin.contains(p.getUniqueId().toString().replace("-", ""))) {
			return false;
		}
		return true;
	}

	public class PlayerSelector {
		private Player player;
		private Main m;
		private Inventory inventory;
		private Listener listener;

		public PlayerSelector(Player player, Main m) {
			this.player = player;
			this.m = m;
			this.inventory = createInventory();
			this.loadCategories();
		}

		private Inventory createInventory() {
			Inventory inventory = m.getInventoryManager().getPlayerInventory(player);
			inventory.clear();
			return inventory;
		}

		public void loadCategories() {
			this.inventory.setItem(11, gui);
			this.inventory.setItem(13, m.getTitleManager().gui);
			this.inventory.setItem(15, clans);
			this.inventory.setItem(4, getPlugin().getChangelog().getChangelogStack());

		}

		public void open() {
			m.getInventoryManager().openInventory(player, StringUtils.makeCenteredInventoryTitle("HeavenMC - Perfil"), 27);
			createListener();
		}

		private void createListener() {
			this.listener = new Listener() {
				@EventHandler
				public void onClose(InventoryCloseEvent event) {
					if (event.getPlayer() != PlayerSelector.this.player) {
						return;
					}
					PlayerSelector.this.destroy();
				}

				@EventHandler
				public void onInteract(InventoryClickEvent event) {
					if (event.getWhoClicked() != PlayerSelector.this.player) {
						return;
					}
					if (!m.getInventoryManager().getOpenInventoryTitle(player).contains("HeavenMC - Perfil")) {
						return;
					}
					event.setCancelled(true);
					PlayerSelector.this.player.updateInventory();
					PlayerSelector.this.player.setItemOnCursor(null);
					ItemStack item = event.getCurrentItem();
					if (item == null) {
						return;
					}
					if (!item.hasItemMeta()) {
						return;
					}
					if (!item.getItemMeta().hasDisplayName()) {
						return;
					}
					if (item.equals(gui)) {
						openRankSelector(player);
					}
					if (item.equals(m.getTitleManager().gui)) {
						m.getTitleManager().openTitleSelector(player);
					}
				}
			};
			m.getServer().getPluginManager().registerEvents(this.listener, m);
		}

		private void destroy() {
			HandlerList.unregisterAll(this.listener);
		}

	}

	public class RankingSelector {
		private Player player;
		private RankingManager manager;
		private Inventory inventory;
		private List<Reward> rewards;
		private Listener listener;

		public RankingSelector(Player player, RankingManager manager) {
			this.player = player;
			this.manager = manager;
			this.inventory = createInventory();
			loadRewards();
			updatePage();
		}

		public void open() {
			manager.getPlugin().getInventoryManager().openInventory(player, StringUtils.makeCenteredInventoryTitle("HeavenMC - Ranking"), 54);
			createListener();
		}

		private Inventory createInventory() {
			Inventory inventory = manager.getPlugin().getInventoryManager().getPlayerInventory(player);
			return inventory;
		}

		public void loadRewards() {
			this.rewards = new ArrayList<>();
			for (Reward r : Reward.values()) {
				rewards.add(r);
			}
			Collections.sort(rewards, new Comparator<Reward>() {
				@Override
				public int compare(Reward t1, Reward t2) {
					Integer a = t1.getID();
					Integer b = t2.getID();
					return a.compareTo(b);
				}

			});
		}

		@SuppressWarnings("deprecation")
		public void updatePage() {
			for (int i = 0; i < 54; i++) {
				this.inventory.setItem(i, null);
			}
			int i = 19;
			PlayerRank rank = manager.getPlayerRank(player);
			StringBuilder sb = new StringBuilder();
			int max = 100;
			int min = getLevelPorcentage(player);
			int result = max - min;
			for (int a = 0; a < min; a++) {
				sb.append("§a:");
			}
			for (int a = 0; a < result; a++) {
				sb.append("§c:");
			}
			ItemStack gui = new ItemStack(Material.BREWING_STAND_ITEM);
			ItemMeta i2 = gui.getItemMeta();
			i2.setDisplayName("§eHeavenMC Ranks");
			i2.setLore(Arrays.asList("§8Levels", " ", "§6▪ §7Acompanhe seu progresso aqui!", "", "§6▪ §7Experiência atual§8:§6 " + rank.getXp(),
					"§6▪ §7Experiência total§8:§6 " + rank.getTotalXP(), "§6▪ §7Experiência p/ proximo nivel§8:§6 " + (rank.getRank().getMax() - rank.getXp()), " ",
					"§7§m------------§r §a§lRank: §e§l" + rank.getLevelName(rank.getLevel()) + "§r §7§m------------", "" + sb, "§7§m-------§r §e§lProgresso: §b§l" + getLevelPorcentageDouble(player) + "%§r §7§m-------"));
			gui.setItemMeta(i2);
			this.inventory.setItem(0, voltar);
			inventory.setItem(4, gui);
			for (Reward r : rewards) {
				ItemStack item = new ItemStack(342);
				ItemMeta m = item.getItemMeta();
				m.setDisplayName(r.getPrefix());
				ArrayList<String> lore = new ArrayList<>();
				lore.addAll(r.getLore());
				lore.add("§e▶ Clique para resgatar!");
				m.setLore(lore);
				item.setItemMeta(m);

				if (rank.getLevel() <= r.getID()) {
					lore = new ArrayList<>();
					lore.addAll(r.getLore());
					lore.add("§c▶ Desbloqueado no nivel " + r.getID() + "!");
					m.setLore(lore);
					item.setItemMeta(m);
				}

				if (rank.getLevel() >= r.getID()) {
					lore = new ArrayList<>();
					lore.addAll(r.getLore());
					lore.add("§e▶ Clique para resgatar!");
					m.setLore(lore);
					item.setItemMeta(m);
					item = addGlowNameLore(item, true, item.getItemMeta().getDisplayName(), item.getItemMeta().getLore());
				}

				if (rank.getRewards().contains(r.getID())) {
					item = new ItemStack(328);
					lore = new ArrayList<>();
					lore.addAll(r.getLore());
					lore.add("§c▶ Você já resgatou essa recompensa.");
					m.setLore(lore);
					item.setItemMeta(m);
				}
				inventory.setItem(i, item);
				i++;
				if (i % 9 > 7) {
					i += 2;
				}
			}

		}

		private void createListener() {
			this.listener = new Listener() {
				@EventHandler
				public void onClose(InventoryCloseEvent event) {
					if (event.getPlayer() != RankingSelector.this.player) {
						return;
					}
					RankingSelector.this.destroy();
				}

				@EventHandler
				public void onInteract(InventoryClickEvent event) {
					if (event.getWhoClicked() != RankingSelector.this.player) {
						return;
					}
					if (!manager.getPlugin().getInventoryManager().getOpenInventoryTitle(player).contains("HeavenMC - Ranking")) {
						return;
					}
					event.setCancelled(true);
					RankingSelector.this.player.updateInventory();
					RankingSelector.this.player.setItemOnCursor(null);
					ItemStack item = event.getCurrentItem();
					if (item == null) {
						return;
					}
					if (!item.hasItemMeta()) {
						return;
					}
					if (!item.getItemMeta().hasDisplayName()) {
						return;
					}
					if (item.equals(voltar)) {
						manager.openWomboMenu(player);
						destroy();
						return;
					}
					if (event.getRawSlot() >= 18) {
						PlayerRank p = manager.getPlayerRank(player);
						Reward reward = Reward.valueOf(ChatColor.stripColor(item.getItemMeta().getDisplayName()).replace("Recompensa para Nivel ", "level").toUpperCase());
						if (p.getRewards().contains(reward.getID())) {
							player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
							return;
						}
						if (p.getLevel() < reward.getID()) {
							player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
							return;
						}
						if (p.getLevel() > 10) {
							player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
							return;
						}
						reward(player, reward.getID());
						player.playSound(player.getLocation(), Sound.LEVEL_UP, 10, 10);
						updatePage();
					}
				}
			};
			Main.getInstance().getServer().getPluginManager().registerEvents(this.listener, Main.getInstance());
		}

		public ItemStack addGlowNameLore(ItemStack item, boolean enchanted, String name, List<String> lore) {
			try {
				Class<?> ItemStack = ReflectionUtils.getCraftClass("ItemStack");
				Class<?> NBTTagCompound = ReflectionUtils.getCraftClass("NBTTagCompound");
				Class<?> NBTTagList = ReflectionUtils.getCraftClass("NBTTagList");
				Class<?> CraftItemStack = ReflectionUtils.getBukkitClass("inventory.CraftItemStack");
				Class<?> NBTTagString = ReflectionUtils.getCraftClass("NBTTagString");
				Class<?> NBTBase = ReflectionUtils.getCraftClass("NBTBase");

				Method asNMSCopy = CraftItemStack.getDeclaredMethod("asNMSCopy", org.bukkit.inventory.ItemStack.class);
				Method asCraftMirror = CraftItemStack.getDeclaredMethod("asCraftMirror", ItemStack);
				Method hasTag = ItemStack.getDeclaredMethod("hasTag");
				Method setTag = ItemStack.getDeclaredMethod("setTag", NBTTagCompound);
				Method getTag = ItemStack.getDeclaredMethod("getTag");
				Method set = NBTTagCompound.getDeclaredMethod("set", String.class, NBTBase);
				Method add = NBTTagList.getDeclaredMethod("add", NBTBase);

				asNMSCopy.setAccessible(true);
				asCraftMirror.setAccessible(true);
				hasTag.setAccessible(true);
				setTag.setAccessible(true);
				getTag.setAccessible(true);
				set.setAccessible(true);

				Constructor<?> NBTTagCompoundConstructor = NBTTagCompound.getConstructor();
				Constructor<?> NBTTagListConstructor = NBTTagList.getConstructor();
				Constructor<?> NBTTagStringConstructor = NBTTagString.getConstructor(String.class);

				NBTTagCompoundConstructor.setAccessible(true);
				NBTTagListConstructor.setAccessible(true);

				Object nmsStack = asNMSCopy.invoke(null, item);
				Object tag = null;

				if ((Boolean) hasTag.invoke(nmsStack))
					tag = getTag.invoke(nmsStack);
				else
					tag = NBTTagCompoundConstructor.newInstance();

				if (enchanted) {
					Object ench = NBTTagListConstructor.newInstance();
					set.invoke(tag, "ench", ench);
				}

				Object display = NBTTagCompoundConstructor.newInstance();
				set.invoke(display, "Name", NBTTagStringConstructor.newInstance(name));

				Object loreObj = NBTTagListConstructor.newInstance();
				for (String str : lore) {
					add.invoke(loreObj, NBTTagStringConstructor.newInstance(str));
				}
				set.invoke(display, "Lore", loreObj);

				set.invoke(tag, "display", display);

				setTag.invoke(nmsStack, tag);

				return (org.bukkit.inventory.ItemStack) asCraftMirror.invoke(null, nmsStack);
			} catch (Exception e) {
				e.printStackTrace();
				return item;
			}
		}

		private void destroy() {
			HandlerList.unregisterAll(this.listener);
		}

	}

}