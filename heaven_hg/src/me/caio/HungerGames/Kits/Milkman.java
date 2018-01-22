package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Milkman extends KitInterface {
	public Milkman(Main main) {
		super(main);
	}

	private transient HashMap<ItemStack, Integer> cooldown = new HashMap<ItemStack, Integer>();
	public String[] porçoes = new String[] { "REGENERATION 900 0", "FIRE_RESISTANCE 900 0", "SPEED 900 0" };
	public String usedMilk = ChatColor.GREEN + "Voce pode usar mais %s vezes!";

	@SuppressWarnings("deprecation")
	private ItemStack clone(ItemStack item, Material newMaterial) {
		ItemStack newItem = new ItemStack(newMaterial.getId(), item.getAmount(), item.getDurability());
		newItem.setItemMeta(item.getItemMeta());
		return newItem;
	}

	@EventHandler
	public void onConsume(PlayerItemConsumeEvent event) {
		ItemStack item = event.getItem();
		Player p = event.getPlayer();
		if (hasAbility(p)) {
			for (String string : porçoes) {
				String[] effect = string.split(" ");
				PotionEffect potionEffect = new PotionEffect(PotionEffectType.getByName(effect[0].toUpperCase()), Integer.parseInt(effect[1]), Integer.parseInt(effect[2]));
				p.addPotionEffect(potionEffect, true);
			}
			if (!cooldown.containsKey(item))
				cooldown.put(item, 0);
			cooldown.put(item, cooldown.get(item) + 1);
			if (cooldown.get(item) == 5) {
				cooldown.remove(item);
				event.setCancelled(true);
				p.setItemInHand(new ItemStack(Material.BUCKET, item.getAmount(), item.getDurability()));
			} else {
				p.sendMessage(String.format(usedMilk, 5 - cooldown.get(item)));
				event.setCancelled(true);
				p.setItemInHand(clone(item, Material.BUCKET));
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteractEntity(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		if (p.getItemInHand().getType() == Material.BUCKET && hasAbility(p)) {
			if (e.getRightClicked() instanceof Cow) {
				e.setCancelled(true);
				ItemStack cloned = p.getItemInHand().clone();
				p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
				if (p.getItemInHand().getAmount() == 0)
					p.setItemInHand(new ItemStack(0));
				p.setItemInHand(clone(cloned, Material.MILK_BUCKET));
				p.updateInventory();
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		kititems.add(createItem(Material.MILK_BUCKET, "§fMilkman"));

		return new Kit("Milkman", Arrays.asList(new String[] { "Bebe leite e tenha varios efeitos!" }), kititems, new ItemStack(Material.MILK_BUCKET));
	}
}
