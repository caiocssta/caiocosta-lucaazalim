package me.caio.HungerGames.Team.Listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Team.Enums.TeamColors;
import me.caio.HungerGames.Utils.ItemBuilder;
import me.caio.HungerGames.Utils.Enum.GameStage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class TeamInventory implements Listener {

	public Inventory inv;
	public Main m;
	private BukkitTask task;

	public TeamInventory(Main m) {
		this.m = m;
	}

	public void setupInventory() {
		inv = Bukkit.createInventory(null, 54, "Escolha seu Time:");
		task = new BukkitRunnable() {

			@Override
			public void run() {
				int i = 10;
				int team = 1;
				for (TeamColors tc : TeamColors.values()) {
					if ((i == 17) || (i == 26) || (i == 35)) {
						i++;
					}
					if (i % 9 == 0) {
						i++;
					}
					List<String> description = new ArrayList<String>();
					description.add("");
					description.add("§7Jogadores nesse Time:     ");
					description.add("");

					for (UUID uuid : m.team.teamPlayers.get(tc)) {
						Player p = Bukkit.getPlayer(uuid);
						if (p != null) {
							description.add("§7»§b " + p.getName());
						}
					}

					int size = m.team.teamPlayers.get(tc).size();

					if (size == 0 || size == 1) {
						size = 1;
					}

					inv.setItem(i, ItemBuilder.removeAttributes(new ItemBuilder(Material.LEATHER_CHESTPLATE).quantidade(size).cor(tc.getColor()).nome("§aTime " + team).lore(description).construir()));
					team++;
					i++;
				}
			}
		}.runTaskTimerAsynchronously(m, 0, 20);

	}

	public BukkitTask getTask() {
		return this.task;
	}

	public void cancelTask() {
		if (this.task != null) {
			task.cancel();
		}
	}

	public void open(Player p) {
		if (this.inv != null) {
			p.openInventory(inv);
		}
	}

	@SuppressWarnings("static-access")
	@EventHandler
	public void interactInventory(InventoryClickEvent e) {
		if (e.getInventory().getTitle().equalsIgnoreCase(inv.getTitle())) {
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			if (m.stage != GameStage.PREGAME) {
				p.sendMessage(ChatColor.RED + "A partida ja iniciou, você não pode trocar de time!");
				return;
			}
			ItemStack item = e.getCurrentItem();
			if (item.getType() == Material.LEATHER_CHESTPLATE) {
				int teamName = Integer.valueOf(ChatColor.stripColor(item.getItemMeta().getDisplayName()).substring(5));
				for (TeamColors tc : TeamColors.values()) {
					if (tc.values()[teamName - 1] == tc) {
						if (m.team.teamPlayers.get(tc).size() >= 4) {
							p.sendMessage("§cEsse time esta cheio, tente outro!");
							p.closeInventory();
						} else {
							m.team.addPlayerToTeam(p.getUniqueId(), tc);
							p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);
							p.getInventory().setItem(4, new ItemBuilder(Material.LEATHER_CHESTPLATE).nome(ChatColor.YELLOW + "Escolha o seu Time§7 (Clique)").cor(tc.getColor()).construir());
							p.sendMessage("§aVocê entrou no Time " + teamName);
							p.sendMessage("§bDigite §e/time §bpara conversar com seu Time!");
							p.closeInventory();
							// ScoreBManager.updateTeam(p);
						}
						break;
					}
				}
			}
		}
	}

}
