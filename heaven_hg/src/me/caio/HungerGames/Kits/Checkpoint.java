package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Checkpoint extends KitInterface {
	public Checkpoint(Main main) {
		super(main);
	}

	private HashMap<Player, Location> Check = new HashMap<Player, Location>();

	public static ItemStack Item = createItem(Material.NETHER_FENCE, 1, ChatColor.GREEN + "Marcador");
	public static ItemStack Item2 = createItem(Material.FLOWER_POT_ITEM, 1, ChatColor.GOLD + "Teleporte");

	@EventHandler
	public void Marcar(BlockPlaceEvent e) {
		final Player p = e.getPlayer();
		Block b = e.getBlock();
		if (b.getType() == Material.NETHER_FENCE) {
			if (Check.containsKey(p)) {
				Check.get(p).getBlock().setType(Material.AIR);
				Check.remove(p);
			}
			Check.put(p, b.getLocation());
			p.sendMessage("§aLugar marcado, use seu pote para teleportar para este local!");
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
				public void run() {
					if (!p.getInventory().contains(Item)) {
						p.getInventory().addItem(Item);
						p.updateInventory();
					}
				}
			}, 1);
		}
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	@EventHandler
	public void onDamage(BlockDamageEvent e) {
		Block b = e.getBlock();
		if (b.getType() == Material.NETHER_FENCE) {
			for (Map.Entry ed : Check.entrySet()) {
				Player dono = (Player) ed.getKey();
				b.getWorld().playEffect(e.getBlock().getLocation(), Effect.STEP_SOUND, Material.NETHER_FENCE.getId());
				b.getWorld().playEffect(b.getLocation(), Effect.MOBSPAWNER_FLAMES, 2);
				b.setType(Material.AIR);
				if (Check.containsKey(dono)) {
					Check.remove(dono);
					dono.getInventory().addItem(Item);
					dono.sendMessage("§cSeu ponto de retorno foi quebrado!");
				}
			}
		}
	}

	@EventHandler
	public void death(PlayerDeathEvent e) {
		if (hasAbility(e.getEntity())) {
			if (Check.containsKey(e.getEntity())) {
				Check.remove(e.getEntity());
			}
		}
	}

	@EventHandler
	public void Lele(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if ((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			if (p.getItemInHand().getType() == Material.FLOWER_POT_ITEM) {
				if (hasAbility(p)) {
					if (getMain().GameTimer > 2700) {
						e.setCancelled(true);
						p.sendMessage("§cVocê não pode usar para ir para fora!");
					} else if (!Check.containsKey(p)) {
						p.sendMessage("§cVocê não marcou seu lugar ainda!");
						e.setCancelled(true);
					} else {
						e.setCancelled(true);
						p.setFallDistance(-20);
						p.teleport(Check.get(p));
						Check.get(p).getBlock().setType(Material.AIR);
						Check.remove(p);
					}
					p.updateInventory();

				}
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		kititems.add(Item);
		kititems.add(Item2);
		return new Kit("Checkpoint", Arrays.asList(new String[] { "Marque um local com o kit para que você teleporte ate la quando desejar" }), kititems, new ItemStack(Material.NETHER_FENCE));
	}
}
