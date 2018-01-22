package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Jump extends KitInterface {
	public Jump(Main main) {
		super(main);
	}

	@EventHandler
	public void Morte(final PlayerDeathEvent e) {
		final Player p = e.getEntity();
		if (hasAbility(p)) {
			for (ListIterator<ItemStack> item = e.getDrops().listIterator(); item.hasNext();) {
				ItemStack i = item.next();
				if (i.getType() == Material.FISHING_ROD) {
					item.remove();
				}
			}
		}
	}

	@EventHandler
	public void FisherHomi(PlayerFishEvent event) {
		Player p = event.getPlayer();
		if ((event.getCaught() instanceof Player)) {
			Player pescado = (Player) event.getCaught();
			p.getItemInHand().setDurability((short) 0);
			if (hasAbility(p) && (p.getItemInHand().getType() == Material.FISHING_ROD)) {
				if (isInvencibility()) {
					p.sendMessage(ChatColor.RED + "Você não pode usar na invencibilidade.");
					return;
				}
				pescado.setVelocity(new Vector(0, 1.3, 0));
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();

		kititems.add(createItem(Material.FISHING_ROD, ChatColor.GREEN + "Jump"));
		return new Kit("Jump", Arrays.asList(new String[] { "Lance seus inimigos com sua", "vara de pesca!" }), kititems, new ItemStack(Material.FISHING_ROD));
	}
}
