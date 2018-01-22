package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;
import me.caio.HungerGames.Utils.Cooldown;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Monk extends KitInterface {
	public Monk(Main main) {
		super(main);
	}

	@EventHandler
	public void onMonk(PlayerInteractEntityEvent event) {
		final Player p = event.getPlayer();
		if (!hasAbility(p)) {
			return;
		}
		if (isInvencibility()) {
			p.sendMessage(ChatColor.RED + "Você não pode usar na invencibilidade.");
			return;
		}
		ItemStack item = p.getItemInHand();
		if (item == null) {
			return;
		}
		if (item.getType() != Material.BLAZE_ROD) {
			return;
		}
		if (!(event.getRightClicked() instanceof Player)) {
			return;
		}
		if (Cooldown.isInCooldown(p.getUniqueId(), "monk")) {
			int timeLeft = Cooldown.getTimeLeft(p.getUniqueId(), "monk");
			p.sendMessage("§6Monk em cooldown de §f" + timeLeft + "§6 segundos!");
			return;
		}
		Random r = new Random();
		Player clickado = (Player) event.getRightClicked();
		int i = r.nextInt(36);
		Inventory inv = clickado.getInventory();
		ItemStack randoitem = inv.getItem(i);
		ItemStack itemhand = clickado.getItemInHand();
		clickado.setItemInHand(randoitem);
		inv.setItem(i, itemhand);
		if (!Cooldown.isInCooldown(p.getUniqueId(), "monk")) {
			Cooldown c = new Cooldown(p.getUniqueId(), "monk", 15);
			c.start();
		}
		new BukkitRunnable() {
			public void run() {
				p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
			}
		}.runTaskLater(getMain(), 300L);
	}

	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<ItemStack>();
		kitItems.add(new ItemStack(Material.BLAZE_ROD));
		return new Kit("monk", Arrays.asList(new String[] { "Desarme seu inimigo" }), kitItems, new ItemStack(Material.BLAZE_ROD));
	}
}
