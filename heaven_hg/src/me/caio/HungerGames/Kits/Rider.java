package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;

public class Rider extends KitInterface {
	public Rider(Main main) {
		super(main);
	}

	private HashMap<Player, Horse> Cavalo = new HashMap<Player, Horse>();

	@EventHandler
	public void Lele(PlayerInteractEvent e) throws Exception {
		Player p = e.getPlayer();
		if ((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			if (p.getItemInHand().getType() == Material.SADDLE) {
				if (hasAbility(p)) {
					if (Cavalo.containsKey(p)) {
						Horse cavalo = Cavalo.remove(p);
						if (!cavalo.isDead()) {
							cavalo.eject();
							cavalo.leaveVehicle();
							cavalo.remove();
							// Particulas.FIREWORKS_SPARK.part(p,
							// cavalo.getLocation(), 1.0F, 1.0F, 1.0F,
							// 1.0F, 40);
						}
					} else {
						Horse cavalo = (Horse) p.getWorld().spawnEntity(p.getLocation(), EntityType.HORSE);
						Cavalo.put(p, cavalo);
						cavalo.setCustomName(String.format("Rider", p.getName()));
						cavalo.setAdult();
						cavalo.setCustomNameVisible(true);
						cavalo.setBreed(false);
						cavalo.setTamed(true);
						cavalo.setDomestication(cavalo.getMaxDomestication());
						cavalo.getInventory().setSaddle(new ItemStack(Material.SADDLE));
						cavalo.setOwner(p);
						cavalo.setJumpStrength(1);
						cavalo.setMaxHealth(50);
						cavalo.setHealth(50);
						cavalo.setColor(Color.WHITE);
						// Particulas.FIREWORKS_SPARK.part(p,
						// cavalo.getLocation(), 1.0F, 1.0F, 1.0F, 1.0F,
						// 40);

					}
				}
			}
		}
	}

	@EventHandler
	public void onRightClick(PlayerInteractEntityEvent event) {
		if (Cavalo.containsValue(event.getRightClicked())) {
			for (Player p : Cavalo.keySet()) {
				if (Cavalo.get(p) == event.getRightClicked()) {
					if (event.getPlayer() != p) {
						event.setCancelled(true);
						break;
					}
				}
			}
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.SADDLE) {
			if (event.getWhoClicked().getVehicle() != null) {
				if (Cavalo.containsValue(event.getWhoClicked().getVehicle())) {
					event.setCancelled(true);
				}
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		kititems.add(createItem(Material.SADDLE, "§aCavalinho"));

		return new Kit("Rider", Arrays.asList(new String[] { "Começe com uma sela e se aventure no seu cavalo" }), kititems, new ItemStack(Material.SADDLE));
	}
}
