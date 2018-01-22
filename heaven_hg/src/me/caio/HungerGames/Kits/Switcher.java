package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Switcher extends KitInterface {
	public Switcher(Main main) {
		super(main);
	}

	@EventHandler
	public void snowball(EntityDamageByEntityEvent e) {
		if (((e.getDamager() instanceof Snowball)) && ((e.getEntity() instanceof LivingEntity))) {
			Snowball s = (Snowball) e.getDamager();
			Player shooter = (Player) s.getShooter();
			if (!hasAbility(shooter)) {
				return;
			}
			if (isInvencibility()) {
				shooter.sendMessage(ChatColor.RED + "Você não pode usar na invencibilidade.");
				return;
			}
			if (!(s.getShooter() instanceof Player)) {
				return;
			}
			if (Gladiator.inGladiator(shooter)) {
				shooter.sendMessage(ChatColor.RED + "Voce nao pode usar o switcher no gladiator!");
				e.setCancelled(true);
				shooter.updateInventory();
				return;
			}
			Location shooterLoc = shooter.getLocation();
			shooter.teleport(e.getEntity().getLocation());
			e.getEntity().teleport(shooterLoc);
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		kititems.add(createItem(Material.SNOW_BALL, 10, "", new String[0]));
		return new Kit("switcher", Arrays.asList(new String[] { "Troque de lugar com jogadores", "ou mobs jogando uma bola de neve neles!" }), kititems, new ItemStack(Material.SNOW_BALL));
	}
}
