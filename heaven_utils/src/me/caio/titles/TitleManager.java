package me.skater.titles;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.skater.Main;
import me.skater.Management;
import me.skater.Utils.StringUtils;
import me.skater.clans.Clan;
import me.skater.ranking.PlayerRank;
import me.skater.tags.Tag;

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

public class TitleManager extends Management {
	public HashMap<UUID, PlayerTitle> title;
	public ItemStack gui;

	public TitleManager(Main main) {
		super(main);
	}

	@Override
	public void onEnable() {
		this.title = new HashMap<UUID, PlayerTitle>();
		this.gui = new ItemStack(Material.NAME_TAG);
		ItemMeta i = gui.getItemMeta();
		i.setDisplayName("§eHeavenMC Titulos");
		this.gui.setItemMeta(i);
		getPlugin().getServer().getPluginManager().registerEvents(new TitleListener(this), getPlugin());
		// getPlugin().getCommand("rank").setExecutor(new
		// RankCommand(getPlugin()));
		try {
			PreparedStatement stmt = null;
			ResultSet result = null;
			for (Player p : getServer().getOnlinePlayers()) {
				UUID uuid = p.getUniqueId();
				stmt = getPlugin().mainConnection.prepareStatement("SELECT * FROM `playertitles` WHERE `uuid`='" + uuid.toString().replace("-", "") + "';");
				result = stmt.executeQuery();
				ArrayList<Integer> titles = new ArrayList<Integer>();
				if (result.next()) {
					String title = result.getString("titles");
					if (title.contains(",")) {
						for (String str : title.split(",")) {
							titles.add(Integer.valueOf(str));
						}
					} else {
						titles.add(Integer.valueOf(title));
					}
					int activetitle = result.getInt("activetitle");
					PlayerTitle ptitle = new PlayerTitle(TitleManager.this, uuid, activetitle);
					for (Integer a : titles) {
						ptitle.addTitleNoSave(a);
					}
					TitleManager.this.title.put(uuid, ptitle);
				}
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

	public void loadPlayerTitles(UUID uuid) throws SQLException {
		PreparedStatement stmt = getPlugin().mainConnection.prepareStatement("SELECT * FROM `playertitles` WHERE `uuid`='" + uuid.toString().replace("-", "") + "';");
		ResultSet result = stmt.executeQuery();
		ArrayList<Integer> titles = new ArrayList<Integer>();
		if (result.next()) {
			String title = result.getString("titles");
			if (title.contains(",")) {
				for (String str : title.split(",")) {
					titles.add(Integer.valueOf(str));
				}
			} else {
				titles.add(Integer.valueOf(title));
			}
			int activetitle = result.getInt("activetitle");
			PlayerTitle ptitle = new PlayerTitle(TitleManager.this, uuid, activetitle);
			for (Integer i : titles) {
				ptitle.addTitleNoSave(i);
			}
			TitleManager.this.title.put(uuid, ptitle);
		}
		result.close();
		stmt.close();
	}

	public boolean hasTitle(Player p, int id) {
		PlayerTitle title = getPlayerTitle(p);
		if (id == 10) {
			if (getPlugin().getCopaManager().isParticipating(p.getUniqueId())) {
				title.addTitle(10);
				return true;
			}
		}
		if (id == 11) {
			if (getPlugin().getClanManager().getPlayerClan(p) != null) {
				title.addTitle(11);
				return true;
			}
		}
		if (getPlugin().getPermissions().isAdmin(p)) {
			return true;
		}
		if (id == 0 || id == 1 || id == 2) {
			return true;
		}
		if (title.getTitles().contains(id)) {
			return true;
		}
		return false;
	}

	public PlayerTitle getPlayerTitle(UUID uuid) {
		if (this.title.containsKey(uuid)) {
			return this.title.get(uuid);
		}
		PlayerTitle prank = new PlayerTitle(this, uuid);
		this.title.put(uuid, prank);
		return prank;
	}

	public void savePlayerTitle(PlayerTitle title) throws SQLException {
		String titles = "";
		for (Integer t : title.getTitles()) {
			if (titles.equals("")) {
				titles = "" + t;
			} else {
				titles = titles + "," + t;
			}
		}
		PreparedStatement stmt = getPlugin().mainConnection.prepareStatement("SELECT * FROM `playertitles` WHERE `uuid`='" + title.getUuid().toString().replace("-", "") + "';");
		ResultSet result = stmt.executeQuery();
		if (result.next()) {
			stmt.execute("UPDATE `playertitles` SET titles='" + titles + "',activetitle='" + title.getActiveTitle() + "' WHERE uuid='" + title.getUuid().toString().replace("-", "") + "';");
		} else {
			stmt.execute("INSERT INTO `playertitles`(`uuid`, `titles`, `activetitle`) VALUES ('" + title.getUuid().toString().replace("-", "") + "','" + titles + "'," + title.getActiveTitle() + ")");
		}
		result.close();
		stmt.close();
	}

	public PlayerTitle getPlayerTitle(Player player) {
		return getPlayerTitle(player.getUniqueId());
	}

	public void removePlayerRank(UUID uuid) {
		this.title.remove(uuid);
	}

	public String getPlayerTitlePrefix(Player p) {
		PlayerTitle title = getPlayerTitle(p);
		String prefix = "§7[&§7] ";
		if (title.getActiveTitle() == Title.RANK.getID()) {
			if (getPlugin().getTagManager().getPlayerActiveTag(p) == Tag.NORMAL) {
				prefix = "";
			} else {
				prefix = getPlugin().getTagManager().getPlayerActiveTag(p).getColor() + "[" + getPlugin().getTagManager().getPlayerActiveTag(p) + "] ";
			}
		}
		if (title.getActiveTitle() == Title.LEVEL.getID()) {
			PlayerRank rank = getPlugin().getRankManager().getPlayerRank(p);
			if (rank.getLevel() < 10) {
				prefix = prefix.replace("&", "§eLv0" + rank.getLevel());
			} else {
				prefix = prefix.replace("&", "§eLv" + rank.getLevel());
			}
		}
		if (title.getActiveTitle() == Title.NENHUM.getID()) {
			prefix = "";
		}
		if (title.getActiveTitle() != Title.LEVEL.getID() && title.getActiveTitle() != Title.NENHUM.getID() && title.getActiveTitle() != Title.RANK.getID() && title.getActiveTitle() != Title.CLAN.getID()) {
			prefix = prefix.replace("&", "§e" + ChatColor.stripColor(title.getTitle().getPrefix().toUpperCase()));
		}
		if (title.getActiveTitle() == Title.CLAN.getID()) {
			Clan clan = getPlugin().getClanManager().getPlayerClan(p);
			if (clan == null) {
				prefix = "";
			} else {
				prefix = prefix.replace("&", getPlugin().getClanManager().getClanColorByElo(clan.getStatus().getElo()) + ChatColor.stripColor(clan.getTag()));
			}
		}
		if (getPlugin().getTagManager().getPlayerActiveTag(p) == Tag.SPEC) {
			prefix = "";
		}
		return prefix;
	}

	public String getTitlePrefixByID(Player p, int id) {
		String prefix = "§7[&§7] ";
		if (id == Title.RANK.getID()) {
			if (getPlugin().getTagManager().getPlayerActiveTag(p) == Tag.NORMAL) {
				prefix = "";
			} else {
				prefix = getPlugin().getTagManager().getPlayerActiveTag(p).getColor() + "[" + getPlugin().getTagManager().getPlayerActiveTag(p) + "] ";
			}
		}
		if (id == Title.LEVEL.getID()) {
			PlayerRank rank = getPlugin().getRankManager().getPlayerRank(p);
			if (rank.getLevel() < 10) {
				prefix = prefix.replace("&", "§eLv0" + rank.getLevel());
			} else {
				prefix = prefix.replace("&", "§eLv" + rank.getLevel());
			}
		}
		if (id == Title.NENHUM.getID()) {
			prefix = "";
		}
		if (id != Title.LEVEL.getID() && id != Title.NENHUM.getID() && id != Title.RANK.getID() && id != Title.CLAN.getID()) {
			prefix = prefix.replace("&", "§e" + ChatColor.stripColor(Title.getTitleByID(id).getPrefix().toUpperCase()));
		}

		if (id == Title.CLAN.getID()) {
			Clan clan = getPlugin().getClanManager().getPlayerClan(p);
			if (clan == null) {
				prefix = "";
			} else {
				prefix = prefix.replace("&", getPlugin().getClanManager().getClanColorByElo(clan.getStatus().getElo()) + ChatColor.stripColor(clan.getTag()));
			}
		}
		if (getPlugin().getTagManager().getPlayerActiveTag(p) == Tag.SPEC) {
			prefix = "";
		}
		return prefix;
	}

	@Override
	public void onDisable() {
		this.title.clear();
		this.title = null;
	}

	public void openTitleSelector(Player p) {
		new TitleSelector(p, this).open();
	}

	public class TitleSelector {
		private Player player;
		private TitleManager manager;
		private Inventory inventory;
		private List<Title> titles;
		private Listener listener;

		public TitleSelector(Player player, TitleManager manager) {
			this.player = player;
			this.manager = manager;
			this.inventory = createInventory();
			loadTitles();
			updatePage();
		}

		public void open() {
			manager.getPlugin().getInventoryManager().openInventory(player, StringUtils.makeCenteredInventoryTitle("HeavenMC - Titulos"), 36);
			createListener();
		}

		private Inventory createInventory() {
			Inventory inventory = manager.getPlugin().getInventoryManager().getPlayerInventory(player);
			inventory.clear();
			return inventory;
		}

		public void loadTitles() {
			this.titles = new ArrayList<>();
			for (Title title : Title.values()) {
				if (manager.hasTitle(player, title.getID())) {
					titles.add(title);
				}
			}
			Collections.sort(titles, new Comparator<Title>() {
				@Override
				public int compare(Title t1, Title t2) {
					Integer a = t1.getID();
					Integer b = t2.getID();
					return a.compareTo(b);
				}

			});
		}

		public void updatePage() {
			int i = 10;
			this.inventory.setItem(0, manager.getPlugin().getRankManager().voltar);
			PlayerTitle title = manager.getPlayerTitle(player);
			for (Title t : titles) {
				ItemStack item = new ItemStack(Material.NAME_TAG);
				ItemMeta m = item.getItemMeta();
				m.setDisplayName(t.getPrefix().toUpperCase());
				m.setLore(Arrays.asList("§8Titulos", " ", "§e► Clique para selecionar!", "§7Preview:",
						manager.getTitlePrefixByID(player, t.getID()) + getPlugin().getTagManager().getPlayerActiveTag(player).getColor() + player.getName()));
				item.setItemMeta(m);
				if (t.getID() == title.getActiveTitle()) {
					item = addGlowNameLore(item, true, "§a§l" + ChatColor.stripColor(m.getDisplayName()), m.getLore());
				}
				inventory.setItem(i, item);
				i++;
				if (i % 9 > 7) {
					i += 2;
				}
			}

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

		private void createListener() {
			this.listener = new Listener() {
				@EventHandler
				public void onClose(InventoryCloseEvent event) {
					if (event.getPlayer() != TitleSelector.this.player) {
						return;
					}
					TitleSelector.this.destroy();
				}

				@EventHandler
				public void onInteract(InventoryClickEvent event) {
					if (event.getWhoClicked() != TitleSelector.this.player) {
						return;
					}
					if (!manager.getPlugin().getInventoryManager().getOpenInventoryTitle(player).contains("HeavenMC - Titulos")) {
						return;
					}
					event.setCancelled(true);
					TitleSelector.this.player.updateInventory();
					TitleSelector.this.player.setItemOnCursor(null);
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
					if (item.equals(manager.getPlugin().getRankManager().voltar)) {
						manager.getPlugin().getRankManager().openWomboMenu(player);
						destroy();
						return;
					}
					PlayerTitle p = manager.getPlayerTitle(player);
					Title title = Title.valueOf(ChatColor.stripColor(item.getItemMeta().getDisplayName()).toUpperCase().replace("-", ""));
					if (p.getActiveTitle() == title.getID()) {
						player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
						return;
					}
					p.changeTitle(title.getID());
					player.playSound(player.getLocation(), Sound.LEVEL_UP, 10, 10);
					updatePage();
				}
			};
			Main.getInstance().getServer().getPluginManager().registerEvents(this.listener, Main.getInstance());
		}

		private void destroy() {
			HandlerList.unregisterAll(this.listener);
		}

	}

}