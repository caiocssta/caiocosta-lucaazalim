package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.GladiatorFight;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;
import me.caio.HungerGames.Utils.Enum.GameStage;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Gladiator extends KitInterface {
	public static List<UUID> playersIn1v1;
	public static List<Block> gladiatorBlocks;

	public Gladiator(Main main) {
		super(main);
		playersIn1v1 = new ArrayList<UUID>();
		gladiatorBlocks = new ArrayList<Block>();
	}

	@EventHandler
	public void onEntityClick(PlayerInteractEntityEvent event) {
		Player p = event.getPlayer();
		Entity e = event.getRightClicked();
		ItemStack i = p.getItemInHand();
		if (!(e instanceof Player)) {
			return;
		}
		if (!hasAbility(p)) {
			return;
		}
		if (i.getType() == null) {
			return;
		}
		if (i.getType() != Material.IRON_FENCE) {
			return;
		}
		if (i.getItemMeta() == null) {
			return;
		}
		if (getMain().stage != GameStage.GAMETIME) {
			return;
		}
		if (isOnWarning(p)) {
			p.sendMessage(ChatColor.RED + "Voce não pode usar o Gladiator perto da Borda!");
			return;
		}
		if (((Player) e).isDead()) {
			return;
		}
		if (playersIn1v1.contains(p.getUniqueId())) {
			return;
		}
		if (playersIn1v1.contains(((Player) e).getUniqueId())) {
			return;
		}
		new GladiatorFight(p, (Player) e, getMain());
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		ItemStack i = p.getItemInHand();
		if ((event.getAction() != Action.PHYSICAL) && (hasAbility(p)) && (i.getType() != null) && (i.getType() == Material.IRON_FENCE)) {
			p.updateInventory();
			event.setCancelled(true);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlock(BlockDamageEvent event) {
		if (gladiatorBlocks.contains(event.getBlock())) {
			Block b = event.getBlock();
			Player p = event.getPlayer();
			p.sendBlockChange(b.getLocation(), Material.BEDROCK, (byte) 0);
		}
	}

	@EventHandler
	public void onExplode(EntityExplodeEvent event) {
		Iterator<Block> blockIt = event.blockList().iterator();
		while (blockIt.hasNext()) {
			Block b = (Block) blockIt.next();
			if (gladiatorBlocks.contains(b)) {
				blockIt.remove();
			}
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (gladiatorBlocks.contains(event.getBlock())) {
			event.setCancelled(true);
		}
	}

	public static boolean inGladiator(Player p) {
		return playersIn1v1.contains(p.getUniqueId());
	}

	private boolean isNotInBoard(Player p) {
		return (p.getLocation().getBlockX() > 500) || (p.getLocation().getBlockX() < -500) || (p.getLocation().getBlockZ() > 500) || (p.getLocation().getBlockZ() < -500);
	}

	private boolean isOnWarning(Player p) {
		return (!isNotInBoard(p)) && ((p.getLocation().getBlockX() > 480) || (p.getLocation().getBlockX() < -480) || (p.getLocation().getBlockZ() > 480) || (p.getLocation().getBlockZ() < -480));
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		kititems.add(createItem(Material.IRON_FENCE, ChatColor.RED + "Gladiator"));
		return new Kit("gladiator", Arrays.asList(new String[] { "Puxe um jogador clicando com o direito nele", " para uma luta nos ceus." }), kititems, new ItemStack(Material.IRON_FENCE));
	}
}
