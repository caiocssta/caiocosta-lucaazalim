package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;
import me.caio.HungerGames.Utils.Enum.GameStage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class Digger extends KitInterface {
	public Digger(Main main) {
		super(main);
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		Player p = event.getPlayer();
		if (p.getItemInHand().getType() == Material.DRAGON_EGG && hasAbility(p)) {
			if (isOnWarning(p)) {
				p.sendMessage(ChatColor.RED + "Você não pode usar esse kit perto do forcefield.");
				event.setCancelled(true);
				return;
			}
			if (getMain().stage != GameStage.GAMETIME) {
				event.setCancelled(true);
				return;

			}
			final Block b = event.getBlock();
			b.setType(Material.AIR);
			p.sendMessage(ChatColor.RED + "Ovo colocado, Corra!");
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
				public void run() {
					int dist = (int) Math.ceil((double) (5 - 1) / 2);
					for (int y = -1; y >= -5; y--) {
						for (int x = -dist; x <= dist; x++) {
							for (int z = -dist; z <= dist; z++) {
								if (b.getY() + y <= 0)
									continue;
								Block block = b.getWorld().getBlockAt(b.getX() + x, b.getY() + y, b.getZ() + z);
								if (block.getType() != Material.BEDROCK && block.getType() != Material.GLASS) {
									block.setType(Material.AIR);
								}
							}
						}
					}
				}
			}, 30);
		}
	}

	private boolean isNotInBoard(Player p) {
		return (p.getLocation().getBlockX() > 500) || (p.getLocation().getBlockX() < -500) || (p.getLocation().getBlockZ() > 500) || (p.getLocation().getBlockZ() < -500);
	}

	private boolean isOnWarning(Player p) {
		return (!isNotInBoard(p)) && ((p.getLocation().getBlockX() > 480) || (p.getLocation().getBlockX() < -480) || (p.getLocation().getBlockZ() > 480) || (p.getLocation().getBlockZ() < -480));
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		kititems.add(createItem(Material.DRAGON_EGG, 5, ""));

		return new Kit("Digger", Arrays.asList(new String[] { "Use seu poder de cavar para", "fazer um buraco gigante!" }), kititems, new ItemStack(Material.DRAGON_EGG));
	}
}
