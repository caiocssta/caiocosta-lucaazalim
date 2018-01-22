package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;
import me.caio.HungerGames.Utils.Enum.GameStage;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Anchor extends KitInterface {
	public Anchor(Main main) {
		super(main);
	}

	@EventHandler
	public void damager(EntityDamageByEntityEvent e) {
		if (((e.getEntity() instanceof Player)) && ((e.getDamager() instanceof Player))) {
			final Player p = (Player) e.getEntity();
			Player d = (Player) e.getDamager();
			if (!hasAbility(d, "anchor")) {
				return;
			}
			if (Main.getInstance().stage != GameStage.GAMETIME) {
				return;
			}
			if (Main.getInstance().isNotPlaying(p)) {
				return;
			}
			if (Main.getInstance().isNotPlaying(d)) {
				return;
			}
			p.setVelocity(new Vector());
			d.playSound(d.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getMain(), new Runnable() {
				public void run() {
					p.setVelocity(new Vector());
				}
			});
		}
	}

	@EventHandler
	public void damaged(EntityDamageByEntityEvent e) {
		if (((e.getEntity() instanceof Player)) && ((e.getDamager() instanceof Player))) {
			final Player p = (Player) e.getEntity();
			Player d = (Player) e.getDamager();
			if (!hasAbility(p, "anchor")) {
				return;
			}
			if (Main.getInstance().isNotPlaying(p)) {
				return;
			}
			if (Main.getInstance().isNotPlaying(d)) {
				return;
			}
			p.setVelocity(new Vector());
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getMain(), new Runnable() {
				public void run() {
					p.setVelocity(new Vector());
				}
			});
		}
	}

	public Kit getKit() {
		return new Kit("anchor", Arrays.asList(new String[] { "Nao leve e nao de knockback." }), new ArrayList<ItemStack>(), new ItemStack(Material.ANVIL));
	}
}
