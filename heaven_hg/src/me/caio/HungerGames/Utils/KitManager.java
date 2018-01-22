package me.caio.HungerGames.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Events.PlayerSelectKitEvent;
import me.caio.HungerGames.NMS.Title;
import me.caio.HungerGames.Utils.Enum.GameStage;

public class KitManager {
	public HashMap<UUID, List<String>> KITSS = new HashMap<UUID, List<String>>();
	public ArrayList<String> kits = new ArrayList<String>();
	public HashMap<String, Kit> items = new HashMap<String, Kit>();
	public HashMap<UUID, ArrayList<String>> playerKit = new HashMap<UUID, ArrayList<String>>();
	public HashMap<String, ArrayList<String>> freeKits = new HashMap<String, ArrayList<String>>();
	private ArrayList<String> kitsDisabled = new ArrayList<String>();
	private ArrayList<String> premiumKits = new ArrayList<String>();

	public boolean kitsHabilitados = true;
	public HashMap<String, Integer> kits_prices = new HashMap<String, Integer>();
	public Main m;
	public HashMap<UUID, String> kit1 = new HashMap<>();
	public HashMap<UUID, String> kit2 = new HashMap<>();

	public KitManager(Main m) {
		this.m = m;
		kitsDisabled.add("provider");
		kitsDisabled.add("feaster");
		this.premiumKits.add("luckywool");

	}

	public void loadFreeKits() {
		new BukkitRunnable() {

			@Override
			public void run() {
				freeKits.clear();
				ArrayList<String> ranks = new ArrayList<String>();
				ranks.add("normal");
				ranks.add("vip");
				ranks.add("mvp");
				try {
					PreparedStatement stmt = null;
					ResultSet result = null;
					for (String rank : ranks) {
						stmt = Main.con.prepareStatement("SELECT * FROM kitrotation WHERE Rank='" + rank + "';");
						result = stmt.executeQuery();
						ArrayList<String> kits = new ArrayList<String>();
						while (result.next()) {
							kits.add(result.getString("Kits").toLowerCase());
						}
						freeKits.put(rank, kits);
						m.getLogger().info("Kit rotation: " + rank + ", " + kits);
					}
					result.close();
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Erro ao carregar Kit Rotation.");
				}
			}
		}.runTaskAsynchronously(m);
	}

	public void addPlayerKit(UUID uuid, String kit) {
		if (playerKit.containsKey(uuid)) {
			ArrayList<String> kits = playerKit.get(uuid);
			if (!kits.contains(kit)) {
				kits.add(kit);
				playerKit.put(uuid, kits);
			}
		} else {
			ArrayList<String> kits = new ArrayList<String>();
			kits.add(kit);
			playerKit.put(uuid, kits);
		}
	}

	public void giveKit(UUID uuid, String kit) {
		m.connection.SQLQuery("INSERT INTO `kits`(`Player`, `Kits`) VALUES ('" + uuid.toString().replace("-", "") + "','" + kit + "');");
		addPlayerKit(uuid, kit);
	}

	public void loadKitPrices(final String kitname, final String classname) {
		new BukkitRunnable() {

			@Override
			public void run() {
				try {
					PreparedStatement stmt = null;
					ResultSet result = null;
					stmt = Main.con.prepareStatement("SELECT * FROM kitprices WHERE name = '" + kitname + "'");
					result = stmt.executeQuery();
					if (!result.isBeforeFirst()) {
						stmt.executeUpdate("INSERT INTO kitprices (name, price) VALUES ('" + kitname + "', '" + 0 + "')");
					} else {
						result.next();
						kits_prices.put(kitname, result.getInt("price"));
					}
					result.close();
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Erro ao carregar preco dos kits.");
				}
			}
		}.runTaskAsynchronously(m);
	}

	public void giveItem(Player p) {
		p.getInventory().clear();
		if (this.KITSS.get(p.getUniqueId()) == null) {
			p.getInventory().addItem(new ItemStack[] { new ItemStack(Material.COMPASS) });
			return;
		}
		if (hasAbility(p, "surprise")) {
			setSurprise(p);
			p.getInventory().addItem(new ItemStack[] { new ItemStack(Material.COMPASS) });
			return;
		}
		List<String> nameList = (List<String>) this.KITSS.get(p.getUniqueId());
		for (String nameNormal : nameList) {
			String name = nameNormal.toLowerCase();
			if (this.items.get(name) != null) {
				for (ItemStack i : ((Kit) this.items.get(name)).items) {
					p.getInventory().addItem(new ItemStack[] { i });
				}
			}
		}
		if (p.getInventory().contains(Material.COMPASS)) {
			return;
		}
		p.getInventory().addItem(new ItemStack[] { new ItemStack(Material.COMPASS) });
	}

	public void giveMini(Player p, String kit) {
		if (!this.KITSS.containsKey(p.getUniqueId())) {
			return;
		}
		String name = kit.toLowerCase();
		if (getFirstKit(p).equalsIgnoreCase(name)) {
			if (this.items.get(name) != null) {
				for (ItemStack i : ((Kit) this.items.get(name)).items) {
					p.getInventory().addItem(new ItemStack[] { i });
				}
			}
		}
		if (getSecondKit(p).equalsIgnoreCase(name)) {
			if (this.items.get(name) != null) {
				for (ItemStack i : ((Kit) this.items.get(name)).items) {
					p.getInventory().addItem(new ItemStack[] { i });
				}
			}
		}
	}

	public void disableKitAll() {
		for (String str : this.kits) {
			this.kitsDisabled.add(str);
		}
		this.KITSS.clear();
	}

	public void enableKitAll() {
		this.kitsDisabled.clear();
	}

	public void disableKit(String kitName) {
		this.kitsDisabled.add(kitName);
		List<UUID> playerToRemove = new ArrayList<UUID>();
		for (Entry<UUID, List<String>> kitEntry : this.KITSS.entrySet()) {
			if (((List<?>) kitEntry.getValue()).contains(kitName)) {
				playerToRemove.add((UUID) kitEntry.getKey());
			}
		}
		for (UUID uuid : playerToRemove) {
			this.KITSS.remove(uuid);
		}
	}

	public void enableKit(String kitName) {
		this.kitsDisabled.remove(kitName);
	}

	public HashMap<String, Kit> getKits() {
		HashMap<String, Kit> map = new HashMap<String, Kit>();
		for (Entry<String, Kit> kitEntry : this.items.entrySet()) {
			if (!this.kitsDisabled.contains(kitEntry.getKey())) {
				map.put((String) kitEntry.getKey(), (Kit) kitEntry.getValue());
			}
		}
		return map;
	}

	public void setForcedKit(Player p, String kit) {
		if (!isKit(kit)) {
			p.sendMessage(ChatColor.RED + getKitName(kit) + " nao e um kit!");
			return;
		}
		removePlayerKit(p);
		
		p.sendMessage(ChatColor.GREEN + "Kit " + getKitName(kit) + " aplicado com sucesso.");
		List<String> kitList = (List<String>) this.KITSS.get(p.getUniqueId());
		if (kitList == null) {
			kitList = new ArrayList<String>();
		}
		kitList.add(kit);

		this.KITSS.put(p.getUniqueId(), kitList);
		this.m.sendTabToPlayer(p);
		Bukkit.getPluginManager().callEvent(new PlayerSelectKitEvent(p, kit));
		if (this.m.stage != GameStage.PREGAME) {
			giveItem(p);
		}
	}

	public void removePlayerKit(Player p) {
		if (this.KITSS.get(p.getUniqueId()) != null) {
			List<String> kitList = (List<String>) this.KITSS.get(p.getUniqueId());
			for (String kitName : kitList) {
				kitName = kitName.toLowerCase();
				if (this.items.get(kitName) != null) {
					for (ItemStack item : p.getInventory().getContents()) {
						boolean isItemKit = false;
						for (ItemStack i : ((Kit) this.items.get(kitName)).items) {
							if (item != null) {
								if (item.getType() == i.getType()) {
									isItemKit = true;
									break;
								}
							}
						}
						if (isItemKit) {
							p.getInventory().remove(item);
						}
					}
				}
			}
		}
		this.KITSS.remove(p.getUniqueId());
		Main.getInstance().getScoreboardManager().updateKit(p);

	}

	public void removePlayerKitAll() {
		for (Player p : this.m.getServer().getOnlinePlayers()) {
			removePlayerKit(p);
		}
	}

	public void setForcedKitAll(String kit) {
		for (Player p : this.m.getServer().getOnlinePlayers()) {
			setForcedKit(p, kit);
		}
	}

	public boolean isBothKits(Player p) {
		if (kit1.containsKey(p.getUniqueId()) && kit2.containsKey(p.getUniqueId())) {
			return true;
		}
		return false;

	}

	public void setTeamKit(Player p, String kit, boolean second) {
		if (this.KITSS.containsKey(p.getUniqueId())) {
			if (this.KITSS.get(p.getUniqueId()).contains(kit) && !kit.equals("surprise")) {
				p.sendMessage(ChatColor.RED + "Você já escolheu o kit " + getKitName(kit) + ".");
				return;
			}
		}
		if (kit.equals("surprise")) {
			if (kit1.containsKey(p.getUniqueId()) && kit2.containsKey(p.getUniqueId())) {
				if (kit1.get(p.getUniqueId()).equals("surprise") && kit2.get(p.getUniqueId()).equals("surprise")) {
					p.sendMessage(ChatColor.RED + "Você já escolheu o kit " + getKitName(kit) + ".");
					return;
				}
			}
		}
		if (!isKit(kit)) {
			p.sendMessage(ChatColor.RED + getKitName(kit) + " nao e um kit!");
			return;
		}
		if (((this.m.stage != GameStage.PREGAME) && (!this.m.perm.isVip(p))) || (this.m.GameTimer > 300)) {
			p.sendMessage(ChatColor.RED + "Partida em andamento.");
			return;
		}
		if (this.kitsDisabled.contains(kit)) {
			p.sendMessage(ChatColor.RED + "O kit " + getKitName(kit) + " foi desativado.");
			return;
		}
		if (!hasKit(p, kit)) {
			if ((second) || (premiumKits.contains(kit.toLowerCase()))) {
				p.sendMessage(ChatColor.RED + "Voce nao possui o kit " + getKitName(kit));
				p.sendMessage(ChatColor.RED + "Acesse: heavenmc.com.br");
				return;
			}
		}
		if ((this.m.stage != GameStage.PREGAME) && (isBothKits(p))) {
			p.sendMessage(ChatColor.RED + "O torneio ja iniciou");
			return;
		}
		Title title = new Title(ChatColor.GRAY + "Kit " + ChatColor.GOLD + getKitName(kit));
		title.setSubtitle(ChatColor.GRAY + "escolhido com sucesso!");
		title.setFadeInTime(1);
		title.setFadeOutTime(1);
		title.setStayTime(2);
		title.send(p);
		p.sendMessage(ChatColor.GREEN + "Kit " + getKitName(kit) + " escolhido com sucesso.");
		if (second) {
			List<String> kitList = new ArrayList<String>();
			if (kit1.containsKey(p.getUniqueId())) {
				kitList.add(kit1.get(p.getUniqueId()));
			}
			kitList.add(kit);
			this.KITSS.put(p.getUniqueId(), kitList);
			this.kit2.put(p.getUniqueId(), kit);

		} else {
			List<String> kitList = new ArrayList<>();
			if (kit2.containsKey(p.getUniqueId())) {
				kitList.add(kit2.get(p.getUniqueId()));
			}
			kitList.add(kit);
			this.KITSS.put(p.getUniqueId(), kitList);
			this.kit1.put(p.getUniqueId(), kit);
		}
		this.m.sendTabToPlayer(p);
		Bukkit.getPluginManager().callEvent(new PlayerSelectKitEvent(p, kit));
		if (m.stage != GameStage.PREGAME) {
			giveMini(p, kit);
		}
	}

	public void setKit(Player p, String kit) {
		if (!isKit(kit)) {
			p.sendMessage(ChatColor.RED + getKitName(kit) + " nao e um kit!");
			return;
		}
		if (((this.m.stage != GameStage.PREGAME) && (!this.m.perm.isVip(p))) || (this.m.GameTimer > 300)) {
			p.sendMessage(ChatColor.RED + "Partida em andamento.");
			return;
		}
		if (this.kitsDisabled.contains(kit)) {
			p.sendMessage(ChatColor.RED + "O kit " + getKitName(kit) + " foi desativado.");
			return;
		}
		if (!hasKit(p, kit)) {
			p.sendMessage(ChatColor.RED + "Voce nao possui o kit " + getKitName(kit));
			p.sendMessage(ChatColor.RED + "Acesse: heavenmc.com.br");
			return;
		}
		if ((this.m.stage != GameStage.PREGAME) && (this.KITSS.containsKey(p.getUniqueId()))) {
			p.sendMessage(ChatColor.RED + "O torneio ja iniciou");
			return;
		}
		Title title = new Title(ChatColor.YELLOW + "Kit " + ChatColor.AQUA + getKitName(kit));
		title.setSubtitle(ChatColor.GREEN + "escolhido com sucesso!");
		title.setFadeInTime(1);
		title.setFadeOutTime(1);
		title.setStayTime(2);
		title.send(p);
		p.sendMessage(ChatColor.GREEN + "Kit " + getKitName(kit) + " escolhido com sucesso.");
		this.KITSS.remove(p.getUniqueId());
		this.KITSS.put(p.getUniqueId(), Arrays.asList(new String[] { kit }));
		this.kit1.put(p.getUniqueId(), kit);
		this.m.sendTabToPlayer(p);
		Bukkit.getPluginManager().callEvent(new PlayerSelectKitEvent(p, kit));
		if (m.stage != GameStage.PREGAME) {
			giveMini(p, kit);
		}
	}

	public boolean hasAbility(Player p, String kitName) {
		return (this.kitsHabilitados) && (this.KITSS.containsKey(p.getUniqueId())) && ((this.KITSS.get(p.getUniqueId())).contains(kitName.toLowerCase()));
	}

	public boolean hasKit(Player p, String kit) {
		if (this.m.perm.isPro(p)) {
			return true;
		}
		if (this.m.perm.isCopaPlayer(p)) {
			return true;
		}
		if ((this.freeKits.get("normal")).contains("*")) {
			return true;
		}
		if (this.playerKit.containsKey(p.getUniqueId())) {
			for (String i : this.playerKit.get(p.getUniqueId())) {
				if (i.toLowerCase().equals(kit.toLowerCase())) {
					return true;
				}
			}
		}
		if (this.premiumKits.contains(kit.toLowerCase())) {
			return false;
		}
		for (String i : this.freeKits.get("normal")) {
			if (i.toLowerCase().equals(kit.toLowerCase())) {
				return true;
			}
		}
		if (this.m.perm.isVip(p)) {
			for (String i : this.freeKits.get("vip")) {
				if (i.toLowerCase().equals(kit.toLowerCase())) {
					return true;
				}
			}
		}
		if (this.m.perm.isMvp(p)) {
			for (String i : this.freeKits.get("mvp")) {
				if (i.toLowerCase().equals(kit.toLowerCase())) {
					return true;
				}
			}
		}
		return false;
	}

	public void addKit(String kit, Kit k) {
		this.kits.add(kit.toLowerCase());
		this.items.put(kit.toLowerCase(), k);
	}

	public void sendKitMessage(Player p) {

	}

	public boolean isKit(String kit) {
		return this.kits.contains(kit.toLowerCase());
	}

	public String getKitName(String kit) {
		char[] stringArray = kit.toLowerCase().toCharArray();
		stringArray[0] = Character.toUpperCase(stringArray[0]);
		return new String(stringArray);
	}

	public void setSurprise(Player player) {
		addSurprise(player);
	}

	public void addSurprise(Player p) {
		List<String> kitList = new ArrayList<String>();
		if (kit1.containsKey(p.getUniqueId())) {
			if (getFirstKit(p).equals("surprise")) {
				String kit = getViableKit();
				kitList.add(kit);
				kit1.put(p.getUniqueId(), kit);
				giveMini(p, kit);
				p.sendMessage(ChatColor.GREEN + "O kit surprise escolheu o kit: " + ChatColor.YELLOW + getKitName(kit) + ".");
			} else {
				giveMini(p, getFirstKit(p));
				kitList.add(getFirstKit(p));

			}
		}
		if (kit2.containsKey(p.getUniqueId())) {
			if (getSecondKit(p).equals("surprise")) {
				String kit = getViableKit();
				kitList.add(kit);
				kit2.put(p.getUniqueId(), kit);
				giveMini(p, kit);
				p.sendMessage(ChatColor.GREEN + "O kit surprise escolheu o kit: " + ChatColor.YELLOW + getKitName(kit) + ".");
			} else {
				giveMini(p, getSecondKit(p));
				kitList.add(getSecondKit(p));
			}
		}
		this.KITSS.put(p.getUniqueId(), kitList);
	}

	public String getViableKit() {
		List<String> kits = new ArrayList<String>();
		for (String kit : this.kits) {
			if (!this.kitsDisabled.contains(kit)) {
				if (!kit.equals("surprise")) {
					kits.add(kit);
				}
			}
		}
		if (kits.size() > 0) {
			return (String) kits.get(new Random().nextInt(kits.size()));
		}
		return null;
	}

	public String getFirstKit(Player p) {
		if (kit1.containsKey(p.getUniqueId())) {
			return kit1.get(p.getUniqueId());
		}
		return "Nenhum";
	}

	public String getSecondKit(Player p) {
		if (kit2.containsKey(p.getUniqueId())) {
			return kit2.get(p.getUniqueId());
		}
		return "Nenhum";
	}

	public String getPlayerKit(Player p) {
		List<String> kitList = (List<String>) this.KITSS.get(p.getUniqueId());
		String kitName = "";
		if (kitList != null) {
			for (int i = 0; i < kitList.size(); i++) {
				String kit = (String) kitList.get(i);
				char[] stringArray = kit.toLowerCase().toCharArray();
				stringArray[0] = Character.toUpperCase(stringArray[0]);
				if (i + 1 >= kitList.size()) {
					kitName = kitName + new String(stringArray);
				} else {
					kitName = kitName + new String(stringArray) + " e ";
				}
			}
			return kitName;
		}
		return "Nenhum";
	}

	public String replaceCoins(Integer coins) {
		DecimalFormat df = new DecimalFormat("###,###.##");
		return df.format(coins);
	}

	public int getKitPrice(String kit) {
		return this.kits_prices.get(kit);
	}
}
