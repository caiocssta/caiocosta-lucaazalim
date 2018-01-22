package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Forger extends KitInterface {
	public Forger(Main main) {
		super(main);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		ItemStack currentItem = event.getCurrentItem();
		Player p = (Player) event.getWhoClicked();
		if (!hasAbility(p)) {
			return;
		}
		if ((currentItem != null) && (currentItem.getType() == Material.COAL)) {
			int coalAmount = 0;
			Inventory inv = event.getView().getBottomInventory();
			for (ItemStack item : inv.getContents()) {
				if ((item != null) && (item.getType() == Material.COAL)) {
					coalAmount += item.getAmount();
				}
			}
			if (coalAmount == 0) {
				return;
			}
			int hadCoal = coalAmount;
			if (currentItem.getType() == Material.COAL) {
				for (int slot = 0; slot < inv.getSize(); slot++) {
					ItemStack item = inv.getItem(slot);
					if ((item != null) && (item.getType().name().contains("ORE"))) {
						while ((item.getAmount() > 0) && (coalAmount > 0) && (item.getType() == Material.IRON_ORE)) {
							item.setAmount(item.getAmount() - 1);
							coalAmount--;
							if (item.getType() == Material.IRON_ORE) {
								p.getInventory().addItem(new ItemStack[] { new ItemStack(Material.IRON_INGOT, 1) });
							} else if (currentItem.getType() == Material.GOLD_ORE) {
								p.getInventory().addItem(new ItemStack[] { new ItemStack(Material.GOLD_INGOT, 1) });
							}
						}
						if (item.getAmount() == 0) {
							inv.setItem(slot, new ItemStack(0));
							p.getInventory().addItem(new ItemStack[] { new ItemStack(Material.IRON_INGOT, 1) });
						}
					}
				}
			} else if (currentItem.getType().name().contains("ORE")) {
				while ((currentItem.getAmount() > 0) && (coalAmount > 0) && (currentItem.getType() == Material.IRON_ORE)) {
					currentItem.setAmount(currentItem.getAmount() - 1);
					coalAmount--;
					if (currentItem.getType() == Material.IRON_ORE) {
						p.getInventory().addItem(new ItemStack[] { new ItemStack(Material.IRON_INGOT, 1) });
					} else if (currentItem.getType() == Material.GOLD_ORE) {
						p.getInventory().addItem(new ItemStack[] { new ItemStack(Material.GOLD_INGOT, 1) });
					}
				}
				if (currentItem.getAmount() == 0) {
					event.setCurrentItem(new ItemStack(0));
					p.getInventory().addItem(new ItemStack[] { new ItemStack(Material.IRON_INGOT, 1) });
				}
			}
			if (coalAmount != hadCoal) {
				for (int slot = 0; slot < inv.getSize(); slot++) {
					ItemStack item = inv.getItem(slot);
					if ((item != null) && (item.getType() == Material.COAL)) {
						while ((coalAmount < hadCoal) && (item.getAmount() > 0)) {
							item.setAmount(item.getAmount() - 1);
							coalAmount++;
						}
						if (item.getAmount() == 0) {
							inv.setItem(slot, new ItemStack(0));
							p.getInventory().addItem(new ItemStack[] { new ItemStack(Material.IRON_INGOT, 1) });
						}
					}
				}
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		kititems.add(createItem(Material.COAL, 3, "", new String[0]));
		return new Kit("forger", Arrays.asList(new String[] { "Queime seus irons rapidamente clicando encima" }), kititems, new ItemStack(Material.COAL));
	}
}
