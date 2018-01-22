package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Achilles extends KitInterface {
	public Achilles(Main main) {
		super(main);
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		if (!(event.getDamager() instanceof Player)) {
			return;
		}
		Player p = (Player) event.getEntity();
		Player d = (Player) event.getDamager();
		ItemStack inHand = d.getItemInHand();
		if (!hasAbility(p)) {
			return;
		}
		if (inHand.getType().name().contains("WOOD_")) {
			event.setDamage(event.getDamage() + 3.0D);
		} else if (inHand.getType().name().contains("DIAMOND_")) {
			event.setDamage(event.getDamage() - 3.0D);
			d.sendMessage("§cVocê esta batendo em um achilles! Achilies tem fraqueza a itens de madeira.");
		} else if (inHand.getType().name().contains("IRON_")) {
			event.setDamage(event.getDamage() - 2.0D);
			d.sendMessage("§cVocê esta batendo em um achilles! Achilies tem fraqueza a itens de madeira.");
		} else if (inHand.getType().name().contains("STONE_")) {
			event.setDamage(event.getDamage() - 2.0D);
			d.sendMessage("§cVocê esta batendo em um achilles! Achilies tem fraqueza a itens de madeira.");
		}
	}

	public Kit getKit() {
		return new Kit("achilles", Arrays.asList(new String[] { "Itens de madeira dão mais dano a você", "enquanto outros dão menos." }), new ArrayList<ItemStack>(), new ItemStack(Material.WOOD_SWORD));
	}
}
