package me.caio.HungerGames.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Type;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Utils.Enum.KitCategory;

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

public class InventorySelector {
	private Player player;
	private KitManager manager;
	private Inventory inventory;
	private KitCategory category;
	private Listener listener;
	private int pagina;
	private int paginaNumbers;
	private Map<KitCategory, List<Kit>> kits;
	private HashMap<Player, ItemStack> confirmando;
	public InventorySelector(Player player, KitManager manager) {
		this.player = player;
		this.manager = manager;
		this.category = KitCategory.TEAM_ALL;
		if (this.manager.m.type == Type.ULTRA) {
			this.category = KitCategory.TEAM_OWNED;
		}
		this.inventory = createInventory();
		this.pagina = 1;
		loadCategories();
	}

	public void open() {
		this.player.openInventory(this.inventory);
		createListener();
		updatePage();
		player.updateInventory();
	}

	@SuppressWarnings("rawtypes")
	private void updatePage() {

		for (int i = 18; i < 54; i++) {
			this.inventory.setItem(i, null);
		}
		if (this.category != KitCategory.CONFIRM) {
			this.paginaNumbers = (((List) this.kits.get(this.category)).size() / 21 + 1);
		}
		setPages(this.inventory);
		setupCategories();
		int i = 19;
		int page = 1;
		int count = 19;
		if (this.category == KitCategory.CONFIRM) {
			inventory.setItem(36, getSim());
			inventory.setItem(37, getSim());
			inventory.setItem(38, getSim());
			inventory.setItem(18, getSim());
			inventory.setItem(19, getSim());
			inventory.setItem(20, getSim());
			inventory.setItem(27, getSim());
			inventory.setItem(28, getSim());
			inventory.setItem(29, getSim());

			inventory.setItem(31, confirmando.get(player));

			inventory.setItem(24, getNao());
			inventory.setItem(25, getNao());
			inventory.setItem(26, getNao());
			inventory.setItem(33, getNao());
			inventory.setItem(34, getNao());
			inventory.setItem(35, getNao());
			inventory.setItem(42, getNao());
			inventory.setItem(43, getNao());
			inventory.setItem(44, getNao());
			manager.m.listener.updateTitle(player, "Comprando: " + confirmando.get(player).getItemMeta().getDisplayName(), inventory.getSize());
			return;
		}

	/*	if (this.category == KitCategory.STORE) {
			manager.m.listener.updateTitle(player, "Loja de Kits" + " " + this.pagina + "/" + paginaNumbers, inventory.getSize());
			this.confirmando = new HashMap<>();
			for (Kit kit : this.kits.get(this.category)) {
				if (page < pagina) {

					if (count == 39) {
						i = 18;
						count = 18;
						page++;
					}
					i++;
					count++;
				} else {
					if (count > 39) {
						break;
					}
					if (kit.icon != null) {
						ItemStack item = new ItemStack(kit.icon);
						ItemMeta meta = item.getItemMeta();

						meta.setDisplayName(ChatColor.GREEN + manager.getKitName(kit.name));
						String description = ChatColor.DARK_GRAY + "Kits. ";
						String s = ChatColor.GREEN + "Preço: " + manager.replaceCoins(manager.getKitPrice(kit.name)) + ". ";
						meta.setLore(wrap(description + s + kit.getKitInfo()));
						item.setItemMeta(meta);
						this.inventory.setItem(i, item);
						i++;
						count++;
						if (i % 9 > 7) {
							i += 2;
						}
					}
				}
			}
			return;
		} */
		if (this.category == KitCategory.FREE) {
			manager.m.listener.updateTitle(player, "Kits da Semana", inventory.getSize());
			ItemStack free = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
			ItemMeta i2 = free.getItemMeta();
			i2.setDisplayName(ChatColor.GRAY + "Rank Normal");
			free.setItemMeta(i2);

			ItemStack vip = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
			ItemMeta i3 = vip.getItemMeta();
			i3.setDisplayName(ChatColor.GREEN + "Rank VIP");
			vip.setItemMeta(i3);

			ItemStack mvp = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
			ItemMeta i4 = mvp.getItemMeta();
			i4.setDisplayName(ChatColor.BLUE + "Rank MVP");
			mvp.setItemMeta(i4);

			this.inventory.setItem(19, free);
			this.inventory.setItem(25, free);
			this.inventory.setItem(28, vip);
			this.inventory.setItem(34, vip);
			this.inventory.setItem(37, mvp);
			this.inventory.setItem(43, mvp);
			int n = 20;
			int v = 29;
			int m = 38;
			for (String kitName : this.manager.freeKits.get("normal")) {
				if (this.manager.isKit(kitName.toLowerCase())) {
					Kit kit = (Kit) this.manager.items.get(kitName);
					if (kit.getIcon() != null) {
						ItemStack item = new ItemStack(kit.getIcon());
						ItemMeta meta = item.getItemMeta();
						meta.setDisplayName(ChatColor.GREEN + this.manager.getKitName(kit.getName()));
						String description = ChatColor.GRAY + "Kit Free. ";
						meta.setLore(wrap(description + kit.getKitInfo()));
						item.setItemMeta(meta);
						this.inventory.setItem(n, item);
						n++;
					}
				}
			}
			for (String kitName : this.manager.freeKits.get("vip")) {
				if (this.manager.isKit(kitName.toLowerCase())) {
					Kit kit = (Kit) this.manager.items.get(kitName);
					if (kit.getIcon() != null) {
						ItemStack item = new ItemStack(kit.getIcon());
						ItemMeta meta = item.getItemMeta();
						meta.setDisplayName(ChatColor.GREEN + this.manager.getKitName(kit.getName()));
						String description = ChatColor.GREEN + "Kit VIP. ";
						meta.setLore(wrap(description + kit.getKitInfo()));
						item.setItemMeta(meta);
						this.inventory.setItem(v, item);
						v++;
					}
				}
			}
			for (String kitName : this.manager.freeKits.get("mvp")) {
				if (this.manager.isKit(kitName.toLowerCase())) {
					Kit kit = (Kit) this.manager.items.get(kitName);
					if (kit.getIcon() != null) {
						ItemStack item = new ItemStack(kit.getIcon());
						ItemMeta meta = item.getItemMeta();
						meta.setDisplayName(ChatColor.GREEN + this.manager.getKitName(kit.getName()));
						String description = ChatColor.BLUE + "Kit MVP. ";
						meta.setLore(wrap(description + kit.getKitInfo()));
						item.setItemMeta(meta);
						this.inventory.setItem(m, item);
						m++;
					}
				}
			}
			return;
		}
		for (Kit kit : this.kits.get(this.category)) {
			manager.m.listener.updateTitle(player, this.category.getName() + " " + this.pagina + "/" + paginaNumbers, inventory.getSize());

			if (page < pagina) {

				if (count == 39) {
					i = 18;
					count = 18;
					page++;
				}
				i++;
				count++;
			} else {
				if (count > 39) {
					break;
				}
				if (kit.icon != null) {
					ItemStack item = new ItemStack(kit.icon);
					ItemMeta meta = item.getItemMeta();

					meta.setDisplayName(ChatColor.GREEN + manager.getKitName(kit.name));
					String description = ChatColor.DARK_GRAY + "Kits. ";
					meta.setLore(wrap(description + kit.getKitInfo()));
					item.setItemMeta(meta);
					this.inventory.setItem(i, item);
					i++;
					count++;
					if (i % 9 > 7) {
						i += 2;
					}

				}
			}
		}
	}

	public void setCategory(KitCategory category) {
		this.category = category;
		this.pagina = 1;
		updatePage();
	}

	public void nextPage() {
		if (this.paginaNumbers >= this.pagina + 1) {
			this.pagina += 1;
			updatePage();
		}
	}

	public void previusPage() {
		if (this.pagina - 1 > 0) {
			this.pagina -= 1;
			updatePage();
		}
	}

	private List<String> wrap(String string) {
		String[] split = string.split(" ");
		string = "";
		ChatColor color = ChatColor.GRAY;
		ArrayList<String> newString = new ArrayList<String>();
		for (int i = 0; i < split.length; i++) {
			if ((string.length() > 20) || (string.endsWith(".")) || (string.endsWith("!"))) {
				newString.add(color + string.replace("[", ""));
				if ((string.endsWith(".")) || (string.endsWith("!"))) {
					newString.add("");
				}
				string = "";
			}
			string = string + (string.length() == 0 ? "" : " ") + split[i];
		}
		newString.add(color + string.replace("]", "").replace("[", ""));
		return newString;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadCategories() {
		this.kits = new HashMap();

		List<Kit> list = (List<Kit>) this.kits.get(KitCategory.TEAM_OWNED);
		if (list == null) {
			list = new ArrayList<Kit>();
		}
		for (Kit kit : this.manager.getKits().values()) {
			if (this.manager.hasKit(this.player, kit.getName())) {
				list.add(kit);
			}
		}
		Collections.sort(list, new Comparator<Kit>() {
			@Override
			public int compare(Kit kit1, Kit kit2) {
				return kit1.name.compareTo(kit2.name);
			}

		});
		this.kits.put(KitCategory.TEAM_OWNED, list);
		this.kits.put(KitCategory.FREE, new ArrayList());

		list = (List<Kit>) this.kits.get(KitCategory.BETA);
		if (list == null) {
			list = new ArrayList<Kit>();
		}
		ArrayList<String> betakits = new ArrayList<>();
		betakits.add("illusion");
		betakits.add("frozen");
		betakits.add("antitower");

		for (String s : betakits) {
			if (this.manager.isKit(s)) {
				list.add(this.manager.items.get(s));
			}
		}
		Collections.sort(list, new Comparator<Kit>() {
			@Override
			public int compare(Kit kit1, Kit kit2) {
				return kit1.name.compareTo(kit2.name);
			}

		});

		this.kits.put(KitCategory.BETA, list);
		list = (List<Kit>) this.kits.get(KitCategory.TEAM_ALL);
		if (list == null) {
			list = new ArrayList<Kit>();
		}
		for (Kit kit : this.manager.getKits().values()) {
			list.add(kit);
		}
		Collections.sort(list, new Comparator<Kit>() {
			@Override
			public int compare(Kit kit1, Kit kit2) {
				return kit1.name.compareTo(kit2.name);
			}

		});
		this.kits.put(KitCategory.TEAM_ALL, list);

		list = (List<Kit>) this.kits.get(KitCategory.STORE);
		if (list == null) {
			list = new ArrayList<Kit>();
		}
		for (Kit kit : this.manager.getKits().values()) {
			list.add(kit);
		}
		Collections.sort(list, new Comparator<Kit>() {
			@Override
			public int compare(Kit kit1, Kit kit2) {
				return kit1.name.compareTo(kit2.name);
			}

		});
		this.kits.put(KitCategory.STORE, list);
	}

	private Inventory createInventory() {
		Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.BLACK + "Kits");
		setPages(inventory);
		return inventory;
	}

	public void setupCategories() {
		ItemStack item;
		ItemStack item2;
	//	ItemStack item3;
		ItemStack item4;
	//	ItemStack item5;

		if (this.category == KitCategory.TEAM_ALL) {
			item = new ItemStack(Material.INK_SACK, 1, (short) 10);
			ItemMeta itemmeta = item.getItemMeta();
			itemmeta.setDisplayName("§aPrimeiro Kit");
			item.setItemMeta(itemmeta);
		} else {
			item = new ItemStack(Material.INK_SACK, 1, (short) 8);
			ItemMeta itemmeta = item.getItemMeta();
			itemmeta.setDisplayName("§7Primeiro Kit");
			item.setItemMeta(itemmeta);
		}
		if (this.category == KitCategory.TEAM_OWNED) {
			item2 = new ItemStack(Material.INK_SACK, 1, (short) 10);
			ItemMeta itemmeta = item2.getItemMeta();
			itemmeta.setDisplayName("§aSegundo Kit");
			item2.setItemMeta(itemmeta);
		} else {
			item2 = new ItemStack(Material.INK_SACK, 1, (short) 8);
			ItemMeta itemmeta = item2.getItemMeta();
			itemmeta.setDisplayName("§7Segundo Kit");
			item2.setItemMeta(itemmeta);
		}
		/*if (this.category == KitCategory.STORE) {
			item3 = new ItemStack(Material.INK_SACK, 1, (short) 10);
			ItemMeta itemmeta = item3.getItemMeta();
			itemmeta.setDisplayName("§aLoja de Kits");
			item3.setItemMeta(itemmeta);
		} else {
			item3 = new ItemStack(Material.INK_SACK, 1, (short) 8);
			ItemMeta itemmeta = item3.getItemMeta();
			itemmeta.setDisplayName("§7Loja de Kits");
			item3.setItemMeta(itemmeta);
		} */
		if (this.category == KitCategory.FREE) {
			item4 = new ItemStack(Material.INK_SACK, 1, (short) 10);
			ItemMeta itemmeta = item4.getItemMeta();
			itemmeta.setDisplayName("§aKits da Semana");
			item4.setItemMeta(itemmeta);
		} else {
			item4 = new ItemStack(Material.INK_SACK, 1, (short) 8);
			ItemMeta itemmeta = item4.getItemMeta();
			itemmeta.setDisplayName("§7Kits da Semana");
			item4.setItemMeta(itemmeta);
		}
	/*	if (this.category == KitCategory.BETA) {
			item5 = new ItemStack(Material.INK_SACK, 1, (short) 10);
			ItemMeta itemmeta = item5.getItemMeta();
			itemmeta.setDisplayName("§aKits em Beta");
			item5.setItemMeta(itemmeta);
		} else {
			item5 = new ItemStack(Material.INK_SACK, 1, (short) 8);
			ItemMeta itemmeta = item5.getItemMeta();
			itemmeta.setDisplayName("§7Kits em Beta");
			item5.setItemMeta(itemmeta);
		} */

		if (this.manager.m.type == Type.ULTRA) {
			this.inventory.setItem(4, item);
	//		this.inventory.setItem(3, item3);
			this.inventory.setItem(5, item4);
	//		this.inventory.setItem(5, item5);
		} else {
			this.inventory.setItem(3, item);
			this.inventory.setItem(4, item2);
	//		this.inventory.setItem(4, item3);
			this.inventory.setItem(5, item4);
	//		this.inventory.setItem(6, item5);
		}

	}

	private void setPages(Inventory inv) {
		if (this.pagina <= 1) {
			inv.setItem(27, getGray("§7◀ Página anterior"));
		} else {
			inv.setItem(27, getGreen("§a◀ Página anterior"));
		}
		if (this.pagina == this.paginaNumbers) {
			inv.setItem(35, getGray("§7Próxima página ▶"));
		} else {
			inv.setItem(35, getGreen("§aPróxima página ▶"));
		}
	}

	private ItemStack getGreen(String name) {
		ItemStack item = new ItemStack(Material.INK_SACK, 1, (short) 10);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName(name);
		item.setItemMeta(itemmeta);
		return item;
	}

	private ItemStack getGray(String name) {
		ItemStack item = new ItemStack(Material.INK_SACK, 1, (short) 8);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName(name);
		item.setItemMeta(itemmeta);
		return item;
	}

	private void createListener() {
		this.listener = new Listener() {
			@EventHandler
			public void onClose(InventoryCloseEvent event) {
				if (event.getPlayer() != InventorySelector.this.player) {
					return;
				}
				if (confirmando != null) {
					if (confirmando.containsKey(player)) {
						confirmando.remove(player);
					}
				}
				InventorySelector.this.destroy();
			}

			@EventHandler
			public void onInteract(InventoryClickEvent event) {
				if (event.getWhoClicked() != InventorySelector.this.player) {
					return;
				}
				if (!event.getInventory().getName().equalsIgnoreCase(ChatColor.BLACK + "Kits")) {
					return;
				}
				event.setCancelled(true);
				if (Cooldown.isInCooldown(InventorySelector.this.player.getUniqueId(), "inventoryclick")) {
					return;
				}
				Cooldown cd = new Cooldown(InventorySelector.this.player.getUniqueId(), "inventoryclick", 1);
				cd.start();
				InventorySelector.this.player.updateInventory();
				InventorySelector.this.player.setItemOnCursor(null);
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
				player.playSound(player.getLocation(), Sound.CLICK, 10, 10);
				if (item.getItemMeta().getDisplayName().contains("Próxima")) {
					InventorySelector.this.nextPage();
					return;
				}
				if (item.getItemMeta().getDisplayName().contains("Página")) {
					InventorySelector.this.previusPage();
					return;
				}
				if (item.getItemMeta().getDisplayName().contains("Kits da Semana")) {
					InventorySelector.this.setCategory(KitCategory.FREE);
				}
				if (item.getItemMeta().getDisplayName().contains("Segundo Kit")) {
					InventorySelector.this.setCategory(KitCategory.TEAM_OWNED);
				}
				if (item.getItemMeta().getDisplayName().contains("Primeiro Kit")) {
					if (manager.m.type == Type.ULTRA) {
						InventorySelector.this.setCategory(KitCategory.TEAM_OWNED);
					} else {
						InventorySelector.this.setCategory(KitCategory.TEAM_ALL);
					}
				}
				if (item.getItemMeta().getDisplayName().contains("Kits em Beta")) {
					InventorySelector.this.setCategory(KitCategory.BETA);
				}
				if (item.getItemMeta().getDisplayName().contains("Loja de Kits")) {
					InventorySelector.this.setCategory(KitCategory.STORE);
				}

				if (category == KitCategory.CONFIRM) {
					if (item.equals(getNao())) {
						confirmando.remove(player);
						player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
						setCategory(KitCategory.STORE);
						return;
					}
					if (item.equals(getSim())) {
						ItemStack produto = confirmando.get(player);
						int price = manager.getKitPrice((ChatColor.stripColor(produto.getItemMeta().getDisplayName()).toLowerCase()));
						manager.giveKit(player.getUniqueId(), ChatColor.stripColor(produto.getItemMeta().getDisplayName()).toLowerCase());
						me.skater.Main.getInstance().getRankManager().getPlayerRank(player).removeCoins(price);
						player.playSound(player.getLocation(), Sound.LEVEL_UP, 10, 10);
						player.sendMessage(ChatColor.GREEN + "Compra realizada com sucesso!");
						confirmando.remove(player);
						player.closeInventory();
						return;
					}
				}
				if (category == KitCategory.STORE) {
					int price = 0;
					int money = 0;
					if (!manager.isKit(ChatColor.stripColor(item.getItemMeta().getDisplayName()).toLowerCase())) {
						return;
					}
					if (manager.hasKit(player, ChatColor.stripColor(item.getItemMeta().getDisplayName()).toLowerCase())) {
						player.sendMessage(ChatColor.RED + "Você já possui esse kit.");
						player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
						return;
					}
					price = manager.getKitPrice((ChatColor.stripColor(item.getItemMeta().getDisplayName()).toLowerCase()));
					money = me.skater.Main.getInstance().getRankManager().getPlayerRank(player).getCoins();
					if (!(money >= price)) {
						int precisa = price - money;
						player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 10, 10);
						ItemMeta i = event.getCurrentItem().getItemMeta();
						i.setDisplayName(event.getCurrentItem().getItemMeta().getDisplayName());
						i.setLore(Arrays.asList("§cVocê precisa de mais §c§l" + precisa + " §cⓌCoins para comprar!"));
						event.getCurrentItem().setItemMeta(i);
						return;
					} else {
						confirmando.put(player, event.getCurrentItem());
						setCategory(KitCategory.CONFIRM);
						updatePage();
						return;

					}
				}
				if (event.getRawSlot() >= 18) {
					if (!manager.isKit(ChatColor.stripColor(item.getItemMeta().getDisplayName()).toLowerCase())) {
						return;
					}
					if (category == KitCategory.TEAM_ALL) {
						InventorySelector.this.manager.setTeamKit(InventorySelector.this.player, ChatColor.stripColor(item.getItemMeta().getDisplayName()).toLowerCase(), false);
					}
					if (category == KitCategory.TEAM_OWNED) {
						InventorySelector.this.manager.setTeamKit(InventorySelector.this.player, ChatColor.stripColor(item.getItemMeta().getDisplayName()).toLowerCase(), true);
					}
					InventorySelector.this.player.closeInventory();
					InventorySelector.this.destroy();
				}
			}
		};
		Main.getInstance().getServer().getPluginManager().registerEvents(this.listener, Main.getInstance());
	}

	public ItemStack getSim() {
		ItemStack sim = new ItemStack(Material.WOOL);
		ItemMeta simm = sim.getItemMeta();
		simm.setDisplayName("§a§lConfirmar");
		sim.setItemMeta(simm);
		sim.setDurability((short) 5);
		return sim;
	}

	public ItemStack getNao() {
		ItemStack nao = new ItemStack(Material.WOOL);
		ItemMeta naom = nao.getItemMeta();
		naom.setDisplayName("§c§lCancelar");
		nao.setItemMeta(naom);
		nao.setDurability((short) 14);
		return nao;
	}

	private void destroy() {
		HandlerList.unregisterAll(this.listener);
	}

}
