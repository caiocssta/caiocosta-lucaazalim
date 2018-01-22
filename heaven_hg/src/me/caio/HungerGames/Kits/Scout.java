package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class Scout extends KitInterface {
	public Scout(Main main) {
		super(main);
	}

	@EventHandler
	public void Morte(final PlayerDeathEvent e) {
		final Player p = e.getEntity();
		if (hasAbility(p)) {
			for (ListIterator<ItemStack> item = e.getDrops().listIterator(); item.hasNext();) {
				ItemStack i = item.next();
				if ((i.getItemMeta().hasDisplayName()) && (i.getItemMeta().getDisplayName().equals(ChatColor.AQUA + "Velocidade"))) {
					item.remove();
				}
			}
		}
	}

	public void Itens(final Player p) {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
			public void run() {
				ItemStack Item = createItem(Material.POTION, 2, (byte) 16418, ChatColor.AQUA + "Velocidade", "");
				if (!p.getInventory().contains(Item))
					p.getInventory().addItem(Item);
				p.updateInventory();
			}
		}, 0, 20 * 600);
	}

	// @EventHandler
	// public void Iniciou(PlayerStartGameEvent e) {
	// if (hasAbility(e.getPlayer()))
	// Itens(e.getPlayer());
	// }

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		kititems.add(createItem(Material.POTION, 2, (byte) 16418, ChatColor.AQUA + "Velocidade", ""));

		return new Kit("Scout", Arrays.asList(new String[] { "A cada 10 minutos receba 2 porçoes de velocidade!" }), kititems, createItem(Material.POTION, 1, (byte) 16418, ChatColor.AQUA + "Velocidade", ""));
	}
}
