package me.caio.HungerGames.Listener;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Utils.Enum.GameStage;

public class Espectador implements Listener {
	private Main m;
	private int pagina = 1;
	private int page = 1;
	private int paginaNumbers;
	private Inventory spectate;

	public Espectador(Main m) {
		this.m = m;
	}

	public void nextPage(Player p) {
		if (this.paginaNumbers >= this.pagina + 1) {
			this.pagina += 1;
			openSpectateGUI(p, Bukkit.getOnlinePlayers(), 6, new ItemStack(Material.SKULL_ITEM, 1, (short) 3));
		}
	}

	public void previusPage(Player p) {
		if (this.pagina > 0) {
			openSpectateGUI(p, Bukkit.getOnlinePlayers(), 6, new ItemStack(Material.SKULL_ITEM, 1, (short) 3));
		}
	}

	private void setPages(Inventory inv) {
		if (this.pagina > 1) {
			inv.setItem(53, getGreen(ChatColor.GREEN + "Página Anterior"));
		}
		if (this.pagina != this.paginaNumbers) {
			inv.setItem(53, getGreen(ChatColor.GREEN + "Próxima Página"));
		}
	}

	private ItemStack getGreen(String name) {
		ItemStack item = new ItemStack(Material.INK_SACK, 1, (short) 10);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName(name);
		item.setItemMeta(itemmeta);
		return item;
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (this.m.adm.isYTPRO(e.getPlayer())) {
			e.setCancelled(true);
		}

	}

	public void openSpectateGUI(Player p, Player[] players, int rows, ItemStack item) {
		ItemStack is = item;
		SkullMeta im = (SkullMeta) is.getItemMeta();
		this.spectate = Bukkit.createInventory(p, rows * 9, "Jogadores vivos");
		int slot = 0;
		for (Player player : players) {
			this.paginaNumbers = (players.length / 54 + 1);
			setPages(this.spectate);
			if ((this.page < this.pagina) && (slot == 53)) {
				slot = 0;
				this.page += 1;
				for (int j = 0; j < 53; j++) {
					this.spectate.setItem(j, null);
				}
			}
			if (slot >= 53) {
				break;
			}
			if ((player != p) && (!this.m.isNotPlaying(player))) {
				im.setDisplayName(player.getDisplayName());
				ArrayList<String> lore = new ArrayList<String>();
				lore.add("§7Kit: §e" + this.m.kit.getKitName(this.m.kit.getPlayerKit(player)));
				lore.add("§7Kills: §e" + this.m.getKills(player));
				lore.add("§aClique para teleportar");
				im.setLore(lore);
				im.setOwner(player.getName());
				is.setItemMeta(im);
				this.spectate.setItem(slot, is);
				slot++;
				lore.clear();
			}
		}
		p.openInventory(this.spectate);
	}

	@EventHandler
	public void onClick(PlayerInteractEntityEvent event) {
		Player p = event.getPlayer();
		if (!(event.getRightClicked() instanceof Player)) {
			return;
		}
		if (!this.m.adm.isSpectating(p)) {
			return;
		}
		if (this.m.adm.pro.contains(p)) {
			return;
		}
		if (this.m.adm.admin.contains(event.getPlayer())) {
			p.openInventory(((Player) event.getRightClicked()).getInventory());
			m.listener.updateTitle(event.getPlayer(), "Inv de: " + ((Player) event.getRightClicked()).getName(), ((Player) event.getRightClicked()).getInventory().getSize());
		}
	}

	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if (this.m.adm.isYTPRO(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) {
			return;
		}
		if (this.m.adm.isYTPRO((Player) event.getWhoClicked())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDamager(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) event.getEntity();
		if (this.m.adm.isYTPRO(p)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDamager2(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player)) {
			return;
		}
		Player p = (Player) event.getDamager();
		if (this.m.adm.isYTPRO(p)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void noFollow(EntityTargetEvent event) {
		if (!(event.getTarget() instanceof Player)) {
			return;
		}
		if (!this.m.adm.isSpectating((Player) event.getTarget())) {
			return;
		}
		event.setCancelled(true);
	}

	@EventHandler
	protected void onBlockCanBuild(BlockCanBuildEvent e) {
		if (!e.isBuildable()) {
			Location blockL = e.getBlock().getLocation();
			boolean allowed = false;
			for (Player target : this.m.getServer().getOnlinePlayers()) {
				if (this.m.adm.isSpectating(target)) {
					if (target.getWorld().equals(e.getBlock().getWorld())) {
						Location playerL = target.getLocation();
						if ((playerL.getX() > blockL.getBlockX() - 1) && (playerL.getX() < blockL.getBlockX() + 1) && (playerL.getZ() > blockL.getBlockZ() - 1) && (playerL.getZ() < blockL.getBlockZ() + 1)
								&& (playerL.getY() > blockL.getBlockY() - 1) && (playerL.getY() < blockL.getBlockY() + 2)) {
							allowed = true;
							break;
						}
					}
				}
			}
			e.setBuildable(allowed);
		}
	}

	@EventHandler
	public void invencible(EntityDamageEvent e) {
		if ((e.getEntity() instanceof Player)) {
			Player p = (Player) e.getEntity();
			if (this.m.adm.isYTPRO(p)) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerPickup(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		if (this.m.adm.isYTPRO(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (this.m.adm.isYTPRO(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player p = event.getPlayer();
		if (!this.m.adm.pro.contains(p)) {
			return;
		}
		if (this.m.winner != null) {
			return;
		}
		Iterator<Player> players = event.getRecipients().iterator();
		while (players.hasNext()) {
			Player pl = (Player) players.next();
			if (!this.m.adm.isSpectating(pl)) {
				players.remove();
			}
		}
	}

	@EventHandler
	public void playerInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		ItemStack item = p.getItemInHand();
		if (item == null) {
			return;
		}
		if (item.getType() != Material.CHEST) {
			return;
		}
		if (this.m.stage == GameStage.PREGAME) {
			return;
		}
		if (!this.m.isNotPlaying(p)) {
			return;
		}
		event.setCancelled(true);
		openSpectateGUI(p, Bukkit.getOnlinePlayers(), 6, new ItemStack(Material.SKULL_ITEM, 1, (short) 3));
	}

	@EventHandler
	public void onInteract(InventoryClickEvent event) {
		Player p = (Player) event.getWhoClicked();
		if (event.getWhoClicked() != p) {
			return;
		}
		if (!event.getInventory().getName().equalsIgnoreCase("Jogadores vivos")) {
			return;
		}
		event.setCancelled(true);
		p.updateInventory();
		p.setItemOnCursor(null);
		ItemStack item = event.getCurrentItem();
		if (item == null) {
			return;
		}
		if (event.getClick().isRightClick()) {
			event.setCancelled(true);
			return;
		}
		if (!item.hasItemMeta()) {
			return;
		}
		if (!item.getItemMeta().hasDisplayName()) {
			return;
		}
		if (item.getItemMeta().getDisplayName().contains("Proxima")) {
			nextPage(p);
		}
		if (item.getItemMeta().getDisplayName().contains("Anterior")) {
			previusPage(p);
		}
		if ((event.getRawSlot() >= 0) && (event.getRawSlot() < 53)) {
			String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
			Player p2 = Bukkit.getPlayerExact(name);
			if (p2 == null) {
				p.sendMessage(ChatColor.RED + name + " saiu da partida.");
				return;
			}
			p.teleport(p2);
			p.sendMessage("§aVocê se teleportou para " + p2.getDisplayName());
			this.spectate.clear();
			this.page = 1;
			this.pagina = 1;
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if (!e.getInventory().getTitle().equalsIgnoreCase("Jogadores vivos")) {
			return;
		}
		if (this.pagina > 1) {
			this.pagina = 1;
			this.page = 1;
		}
	}

}
