package me.caio.HungerGames.Caixas;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import me.caio.HungerGames.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class CaixaManager { /*

	Main m;
	public List<String> caixadiaria;
	public HashMap<UUID, Integer> mytask;
	public ItemStack usado;
	public ItemStack disponivel;

	@SuppressWarnings("deprecation")
	public CaixaManager(Main m) {
		this.m = m;
		this.mytask = new HashMap<UUID, Integer>();
		this.usado = new ItemStack(328);
		ItemMeta im7 = this.usado.getItemMeta();
		im7.setDisplayName(ChatColor.YELLOW + "Caixa Diária§7 (Clique)");
		this.usado.setItemMeta(im7);
		this.disponivel = new ItemStack(407);
		ItemMeta im8 = this.disponivel.getItemMeta();
		im8.setDisplayName(ChatColor.YELLOW + "Caixa Diária§7 (Clique)");
		this.disponivel.setItemMeta(im8);
		loadCaixaDiaria();
	}

	public void refreshPlayer(Player p) throws SQLException {
		PreparedStatement stmt = Main.con.prepareStatement("SELECT * FROM `caixadiaria` WHERE Player='" + p.getUniqueId().toString().replace("-", "") + "';");
		ResultSet result = stmt.executeQuery();
		if (result.next()) {
			if (!caixadiaria.contains(result.getString("Player"))) {
				caixadiaria.add(result.getString("Player"));
			}
		}
		result.close();
		stmt.close();
	}

	public void giveCaixaItem(Player p, int slot) {
		if (canClaim(p)) {
			p.getInventory().setItem(slot, disponivel);
		} else {
			p.getInventory().setItem(slot, usado);
		}
	}

	public void loadCaixaDiaria() {
		this.caixadiaria = new ArrayList<String>();
		new BukkitRunnable() {

			@Override
			public void run() {
				try {
					PreparedStatement sql = Main.con.prepareStatement("SELECT * FROM `caixadiaria`;");
					ResultSet result = sql.executeQuery();
					while (result.next()) {
						caixadiaria.add(result.getString("Player"));
					}
					result.close();
					sql.close();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Erro ao carregar Caixas Diarias");
				}
			}
		}.runTaskAsynchronously(m);
	}

	public Boolean canClaim(Player p) {
		if (caixadiaria.contains(p.getUniqueId().toString().replace("-", ""))) {
			return false;
		}
		return true;
	}

	public void ClaimBox(Player p) {
		if (!canClaim(p)) {
			p.sendMessage(ChatColor.RED + "Parece que vocÃª jÃ¡ usou sua caixa diaria, tente em uma outra hora!");
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
			return;
		}
		ItemStack a1 = new ItemStack(Material.EXP_BOTTLE);
		ItemMeta m1 = a1.getItemMeta();
		m1.setDisplayName("§e500 XP");
		a1.setItemMeta(m1);

		ItemStack a2 = new ItemStack(Material.EXP_BOTTLE);
		ItemMeta m2 = a2.getItemMeta();
		m2.setDisplayName("§e600 XP");
		a2.setItemMeta(m2);

		ItemStack a3 = new ItemStack(Material.EXP_BOTTLE);
		ItemMeta m3 = a3.getItemMeta();
		m3.setDisplayName("§e700 XP");
		a3.setItemMeta(m3);

		ItemStack a4 = new ItemStack(Material.EXP_BOTTLE);
		ItemMeta m4 = a4.getItemMeta();
		m4.setDisplayName("§e800 XP");
		a4.setItemMeta(m4);

		ItemStack a5 = new ItemStack(Material.EXP_BOTTLE);
		ItemMeta m5 = a5.getItemMeta();
		m5.setDisplayName("§e900 XP");
		a5.setItemMeta(m5);

		ItemStack a6 = new ItemStack(Material.EXP_BOTTLE);
		ItemMeta m6 = a6.getItemMeta();
		m6.setDisplayName("§e§l1000 XP");
		a6.setItemMeta(m6);

		ItemStack b1 = new ItemStack(Material.GOLD_INGOT);
		ItemMeta mb1 = b1.getItemMeta();
		mb1.setDisplayName("§a500 Coins");
		b1.setItemMeta(mb1);

		ItemStack b2 = new ItemStack(Material.GOLD_INGOT);
		ItemMeta mb2 = b2.getItemMeta();
		mb2.setDisplayName("§a600 Coins");
		b2.setItemMeta(mb2);

		ItemStack b3 = new ItemStack(Material.GOLD_INGOT);
		ItemMeta mb3 = b3.getItemMeta();
		mb3.setDisplayName("§a700 Coins");
		b3.setItemMeta(mb3);

		ItemStack b4 = new ItemStack(Material.GOLD_INGOT);
		ItemMeta mb4 = b4.getItemMeta();
		mb4.setDisplayName("§a800 Coins");
		b4.setItemMeta(mb4);

		ItemStack b5 = new ItemStack(Material.GOLD_INGOT);
		ItemMeta mb5 = b5.getItemMeta();
		mb5.setDisplayName("§a900 Coins");
		b5.setItemMeta(mb5);

		ItemStack b6 = new ItemStack(Material.GOLD_INGOT);
		ItemMeta mb6 = b6.getItemMeta();
		mb6.setDisplayName("§a§l1000 Coins");
		b6.setItemMeta(mb6);
		b6 = addGlowNameLore(b6, true, b6.getItemMeta().getDisplayName());
		ArrayList<ItemStack> conteudo = new ArrayList<ItemStack>();
		conteudo.add(a1);
		conteudo.add(a2);
		conteudo.add(a3);
		conteudo.add(a4);
		conteudo.add(a5);
		conteudo.add(a6);
		conteudo.add(b1);
		conteudo.add(b2);
		conteudo.add(b3);
		conteudo.add(b4);
		conteudo.add(b5);
		conteudo.add(b6);
		Animation(p, conteudo, 3L);
		m.connection.SQLQuery("INSERT INTO `caixadiaria`(`Player`) VALUES ('" + p.getUniqueId().toString().replace("-", "") + "');");
		this.caixadiaria.add(p.getUniqueId().toString().replace("-", ""));
		giveCaixaItem(p, 8);
	}

	public int FormatReward(String s) {
		String s1 = s;
		if (isXP(s)) {
			s1 = ChatColor.stripColor(s).replace(" xp", "");
		} else {
			s1 = ChatColor.stripColor(s).replace(" coins", "");
		}
		return Integer.parseInt(s1);
	}

	public boolean isXP(String s) {
		if (s.contains("xp")) {
			return true;
		}
		return false;
	}

	public void Animation(final Player p, final List<ItemStack> items, final Long time) {
		final Inventory inv = Bukkit.createInventory(null, 45, "Boa sorte!!!");
		p.openInventory(inv);

		final ItemStack verde = new ItemStack(Material.STAINED_GLASS_PANE);
		verde.setDurability((short) 5);
		ItemMeta verdem = verde.getItemMeta();
		verdem.setDisplayName("§a...");
		verde.setItemMeta(verdem);

		ItemStack red = new ItemStack(Material.STAINED_GLASS_PANE);
		red.setDurability((short) 14);
		ItemMeta redm = red.getItemMeta();
		redm.setDisplayName("§c...");
		red.setItemMeta(redm);

		ItemStack seta = new ItemStack(Material.HOPPER);
		ItemMeta setam = seta.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("     §e⬇⬇⬇");
		setam.setDisplayName("§6» §fPremio §6«");
		setam.setLore(lore);
		seta.setItemMeta(setam);

		inv.setItem(19, items.get(getRandom(0, items.size())));
		inv.setItem(20, items.get(getRandom(1, items.size())));
		inv.setItem(21, items.get(getRandom(2, items.size())));
		inv.setItem(22, items.get(getRandom(3, items.size())));
		inv.setItem(23, items.get(getRandom(4, items.size())));
		inv.setItem(24, items.get(getRandom(5, items.size())));
		inv.setItem(25, items.get(getRandom(6, items.size())));
		inv.setItem(13, seta);

		inv.setItem(0, red);
		inv.setItem(1, red);
		inv.setItem(9, red);
		////
		inv.setItem(8, red);
		inv.setItem(7, red);
		inv.setItem(17, red);
		////
		inv.setItem(27, red);
		inv.setItem(36, red);
		inv.setItem(37, red);
		////
		inv.setItem(35, red);
		inv.setItem(43, red);
		inv.setItem(44, red);
		p.updateInventory();

		final int task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
			int i = 0;

			@Override
			public void run() {

				if (!p.getOpenInventory().getTitle().equalsIgnoreCase("Boa sorte!!!")) {
					p.openInventory(inv);
				}

				p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 3, 3);

				int randn = getRandom(0, items.size());

				inv.setItem(19, inv.getItem(20));
				inv.setItem(20, inv.getItem(21));
				inv.setItem(21, inv.getItem(22));
				inv.setItem(22, inv.getItem(23));
				inv.setItem(23, inv.getItem(24));
				inv.setItem(24, inv.getItem(25));//
				inv.setItem(25, items.get(randn));

				if (i == 20) {
					Bukkit.getScheduler().cancelTask(mytask.get(p.getUniqueId()));

					inv.setItem(0, verde);
					inv.setItem(1, verde);
					inv.setItem(9, verde);
					////
					inv.setItem(8, verde);
					inv.setItem(7, verde);
					inv.setItem(17, verde);
					////
					inv.setItem(27, verde);
					inv.setItem(36, verde);
					inv.setItem(37, verde);
					////
					inv.setItem(35, verde);
					inv.setItem(43, verde);
					inv.setItem(44, verde);

					String premio = ChatColor.stripColor(inv.getItem(22).getItemMeta().getDisplayName().toLowerCase());
					if (isXP(premio)) {
						me.skater.Main.getInstance().getRankManager().getPlayerRank(p).addXp(FormatReward(premio));
					} else {
						me.skater.Main.getInstance().getRankManager().getPlayerRank(p).addCoins(FormatReward(premio));
					}
					p.playSound(p.getLocation(), Sound.LEVEL_UP, 3, 3);

					return;
				}

				i++;

			}
		}, time, time);
		mytask.put(p.getUniqueId(), task);

	}

	public ItemStack addGlowNameLore(ItemStack item, boolean enchanted, String name) {
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

			set.invoke(tag, "display", display);

			setTag.invoke(nmsStack, tag);

			return (org.bukkit.inventory.ItemStack) asCraftMirror.invoke(null, nmsStack);
		} catch (Exception e) {
			e.printStackTrace();
			return item;
		}
	}

	private final Random rand = new Random();

	private int getRandom(int min, int max) {
		return rand.nextInt(max - min) + min;
	} */
}
