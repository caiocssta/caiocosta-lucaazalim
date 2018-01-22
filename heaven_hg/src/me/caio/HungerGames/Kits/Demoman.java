package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class Demoman extends KitInterface {
	public Demoman(Main main) {
		super(main);
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Block b = e.getBlock();
		Player p = e.getPlayer();
		if (!hasAbility(p)) {
			return;
		}
		if (isInvencibility()) {
			p.sendMessage(ChatColor.RED + "Você não pode usar na invencibilidade.");
			return;
		}
		if (getMain().adm.isSpectating(p)) {
			return;
		}
		if (b.getType() == Material.GRAVEL) {
			if (b.hasMetadata("Demoman")) {
				b.removeMetadata("Demoman", getMain());
				b.setMetadata("Demoman", new FixedMetadataValue(getMain(), p.getName()));
			} else {
				b.setMetadata("Demoman", new FixedMetadataValue(getMain(), p.getName()));
			}
		}
		if (b.getType() == Material.STONE_PLATE) {
			if (b.hasMetadata("Demoman")) {
				b.removeMetadata("Demoman", getMain());
				b.setMetadata("Demoman", new FixedMetadataValue(getMain(), p.getName()));
			} else {
				b.setMetadata("Demoman", new FixedMetadataValue(getMain(), p.getName()));
			}
		}
	}

	@EventHandler
	public void interact(PlayerInteractEvent e) {
		Block b = e.getClickedBlock();
		if (b == null) {
			return;
		}
		if (!b.hasMetadata("Demoman")) {
			return;
		}
		if (b.getType() != Material.STONE_PLATE) {
			return;
		}
		if (b.getRelative(BlockFace.DOWN).getType() != Material.GRAVEL) {
			return;
		}
		if (e.getAction() == Action.PHYSICAL) {
			if (isInvencibility()) {
				return;
			}
			if (getMain().adm.isSpectating(e.getPlayer())) {
				return;
			}
			b.removeMetadata("Demoman", getMain());
			b.setType(Material.AIR);
			b.getWorld().createExplosion(b.getLocation().clone().add(0.5D, 0.5D, 0.5D), 4.0F);
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		kititems.add(createItem(Material.GRAVEL, 6, ""));
		kititems.add(createItem(Material.STONE_PLATE, 6, ""));
		return new Kit("demoman", Arrays.asList(new String[] { "Placa de pressão de pedra em", "cima de cascalho é igual a explosão" }), kititems, new ItemStack(Material.GRAVEL));
	}
}
