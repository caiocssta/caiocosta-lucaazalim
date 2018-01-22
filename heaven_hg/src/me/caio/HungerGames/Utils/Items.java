package me.caio.HungerGames.Utils;

import java.util.ArrayList;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Utils.Enum.GameStage;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class Items {
	public static KitManager kit;

	@SuppressWarnings("deprecation")
	public static void dropItens(Player p, Location l) {
		if (Main.getInstance().stage != GameStage.PREGAME) {
			World world = p.getWorld();
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			for (ItemStack item : p.getPlayer().getInventory().getContents()) {
				if ((item != null) && (item.getType() != Material.AIR)) {
					items.add(item.clone());
				}
			}
			for (ItemStack item : p.getPlayer().getInventory().getArmorContents()) {
				if ((item != null) && (item.getType() != Material.AIR)) {
					items.add(item.clone());
				}
			}
			if ((p.getPlayer().getItemOnCursor() != null) && (p.getPlayer().getItemOnCursor().getType() != Material.AIR)) {
				items.add(p.getPlayer().getItemOnCursor().clone());
			}
			for (ItemStack item : items) {
				if ((item != null) && (item.getType() != Material.AIR)) {
					if ((item.getType() != Material.POTION) || (item.getDurability() != 8201)) {
						boolean isItemKit = false;
						if (kit.KITSS.get(p.getUniqueId()) != null) {
							for (String kitName : kit.KITSS.get(p.getUniqueId())) {
								kitName = kitName.toLowerCase();
								if (kit.items.get(kitName) != null) {
									for (ItemStack i : ((Kit) kit.items.get(kitName)).items) {
										if (item.getType() == i.getType()) {
											isItemKit = true;
											break;
										}
									}
								}
							}
						}
						if (!isItemKit) {
							if (item.hasItemMeta()) {
								world.dropItemNaturally(l, item.clone()).getItemStack().setItemMeta(item.getItemMeta());
							} else {
								world.dropItemNaturally(l, item);
							}
						}
					}
				}
			}
			p.getPlayer().getInventory().setArmorContents(new ItemStack[4]);
			p.getPlayer().getInventory().clear();
			p.getPlayer().setItemOnCursor(new ItemStack(0));
			for (PotionEffect pot : p.getActivePotionEffects()) {
				p.removePotionEffect(pot.getType());
			}
		}
	}
}
