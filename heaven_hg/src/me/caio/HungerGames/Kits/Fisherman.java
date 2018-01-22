package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

public class Fisherman extends KitInterface {
	public Fisherman(Main main) {
		super(main);
	}

	@EventHandler
	public void onFisherman(PlayerFishEvent e) {
		Player p = e.getPlayer();
		if (!hasAbility(p)) {
			return;
		}
		e.getPlayer().getItemInHand().setDurability((short) 0);
		if (isInvencibility()) {
			p.sendMessage(ChatColor.RED + "Você não pode usar na invencibilidade.");
			return;
		}
		if (e.getState() == PlayerFishEvent.State.CAUGHT_ENTITY) {
			Entity c = e.getCaught();
			World w = p.getLocation().getWorld();
			double x = p.getLocation().getBlockX() + 0.5D;
			double y = p.getLocation().getBlockY();
			double z = p.getLocation().getBlockZ() + 0.5D;
			float yaw = c.getLocation().getYaw();
			float pitch = c.getLocation().getPitch();
			Location loc = new Location(w, x, y, z, yaw, pitch);
			c.teleport(loc);
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		kititems.add(createItem(Material.FISHING_ROD, "Vara de Pescar"));
		return new Kit("fisherman", Arrays.asList(new String[] { "Começe com uma vara que pode pescar jogadores!" }), kititems, new ItemStack(Material.FISHING_ROD));
	}
}
