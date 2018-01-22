package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Feaster extends KitInterface {
	public Feaster(Main main) {
		super(main);
	}

	// public ArrayList<UUID> minifeast = new ArrayList<>();
	// public ArrayList<UUID> feast = new ArrayList<>();
	// public ArrayList<UUID> spawnmini = new ArrayList<>();
	//
	// @EventHandler
	// public void onClick(InventoryClickEvent e) {
	// if (!e.getInventory().getTitle().equalsIgnoreCase("Feaster Menu")) {
	// return;
	// }
	// if (!(e.getWhoClicked() instanceof Player)) {
	// return;
	// }
	// if (!hasAbility((Player) e.getWhoClicked())) {
	// return;
	// }
	// Player p = (Player) e.getWhoClicked();
	// e.setCancelled(true);
	// if (e.getCurrentItem().getItemMeta().getDisplayName()
	// .equalsIgnoreCase("§6§lMinifeast")) {
	// if (getMain().GameTimer >= 2999) {
	// p.sendMessage("§cVocê não pode usar na batalha final!");
	// p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 1.0F, 1.0F);
	// return;
	// }
	// if (!getMain().minispawned) {
	// p.sendMessage("§cNenhum minifeast foi spawnado ainda!");
	// p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 1.0F, 1.0F);
	// return;
	// }
	//
	// if (minifeast.contains(p.getUniqueId())) {
	// p.sendMessage("§cVocê so pode usar uma vez o teleporte.");
	// p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 1.0F, 1.0F);
	// p.closeInventory();
	// return;
	// }
	// minifeast.add(p.getUniqueId());
	// p.teleport(getMain().lastminifeast.add(0, 5, 0));
	// p.setFallDistance(0);
	// p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
	// p.closeInventory();
	//
	// }
	// if (e.getCurrentItem().getItemMeta().getDisplayName()
	// .equalsIgnoreCase("§e§lFeast")) {
	// if (getMain().GameTimer >= 2999) {
	// p.sendMessage("§cVocê não pode usar na batalha final!");
	// p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 1.0F, 1.0F);
	// return;
	// }
	// if (!getMain().feastspawned) {
	// p.sendMessage("§cO feast não spawnou ainda!");
	// p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 1.0F, 1.0F);
	// return;
	// }
	//
	// if (feast.contains(p.getUniqueId())) {
	// p.sendMessage("§cVocê so pode usar uma vez o teleporte.");
	// p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 1.0F, 1.0F);
	// p.closeInventory();
	// return;
	// }
	// feast.add(p.getUniqueId());
	// p.setFallDistance(0.0F);
	// p.teleport(getMain().feastloc.add(0, 5, 0));
	// p.setFallDistance(0.0F);
	// p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
	// p.closeInventory();
	// }
	// if (e.getCurrentItem().getItemMeta().getDisplayName()
	// .equalsIgnoreCase("§a§lSpawnar Minifeast")) {
	// if (!(getMain().GameTimer >= 900)) {
	// p.sendMessage("§cVocê so podera spawnar quando o feast for anunciado.");
	// p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 1.0F, 1.0F);
	// return;
	// }
	// if (spawnmini.contains(p.getUniqueId())) {
	// p.sendMessage("§cVocê so pode usar uma vez.");
	// p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 1.0F, 1.0F);
	// p.closeInventory();
	// return;
	// }
	// spawnmini.add(p.getUniqueId());
	// new PlayerMinifeast(getMain(), p);
	// p.teleport(p.getLocation().add(0,5,0));
	// p.setFallDistance(0);
	// }
	//
	// }
	//
	// public String MinifeastStatus(Player p) {
	// if (minifeast.contains(p.getUniqueId())) {
	// return "§cJá utilizado.";
	// }
	// if (getMain().minispawned) {
	// return "§aDisponivel!";
	// }
	// return "§cIndisponivel.";
	// }
	//
	// public String SpawnMiniStatus(Player p) {
	// if (spawnmini.contains(p.getUniqueId())) {
	// return "§cJá utilizado.";
	// }
	// return "§aDisponivel!";
	// }
	//
	// public String FeastStatus(Player p) {
	// if (feast.contains(p.getUniqueId())) {
	// return "§cJá utilizado.";
	// }
	// if (getMain().feastspawned) {
	// return "§aDisponivel!";
	// }
	// return "§cIndisponivel.";
	// }
	//
	// public void FeasterMenu(Player p) {
	// Inventory inv = Bukkit.createInventory(p, 9, "Feaster Menu");
	//
	// ItemStack chest1 = new ItemStack(Material.CHEST);
	// ItemMeta chest1m = chest1.getItemMeta();
	// chest1m.setDisplayName("§6§lMinifeast");
	// chest1m.setLore(Arrays.asList("§7Você podera teleportar para o",
	// "§7minifeast mais recente.", " ", "§7Status: "
	// + MinifeastStatus(p)));
	// chest1.setItemMeta(chest1m);
	//
	// ItemStack chest2 = new ItemStack(Material.CHEST);
	// ItemMeta chest2m = chest2.getItemMeta();
	// chest2m.setDisplayName("§e§lFeast");
	// chest2m.setLore(Arrays.asList("§7Você podera teleportar para o",
	// "§7feast assim que spawnar.", " ", "§7Status: "
	// + FeastStatus(p)));
	// chest2.setItemMeta(chest2m);
	//
	// ItemStack chest3 = new ItemStack(Material.CHEST);
	// ItemMeta chest3m = chest3.getItemMeta();
	// chest3m.setDisplayName("§a§lSpawnar Minifeast");
	// chest3m.setLore(Arrays.asList("§7Você podera spawnar um unico",
	// "§7minifeast em sua posição, apos", "o anuncio", " ", "§7Status: "
	// + SpawnMiniStatus(p)));
	// chest3.setItemMeta(chest3m);
	//
	// inv.setItem(0, chest1);
	// inv.setItem(4, chest2);
	// inv.setItem(8, chest3);
	//
	// p.openInventory(inv);
	// }
	//
	// @EventHandler
	// public void onPlayerInteract(PlayerInteractEvent event) {
	// Player p = event.getPlayer();
	// if (p.getItemInHand().getType() == Material.MAP && hasAbility(p)) {
	// if (event.getAction().equals(Action.RIGHT_CLICK_AIR)
	// || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
	// FeasterMenu(p);
	// }
	// }
	// }

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		kititems.add(createItem(Material.MAP, "Feaster"));
		return new Kit("feaster", Arrays.asList(new String[] { "Você podera teleportar para o feast,", "um unico minifeast e ate mesmo", "spawnar um unico minifeast." }), kititems, new ItemStack(Material.MAP));
	}
}
