package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;
import net.minecraft.server.v1_7_R4.EntityPlayer;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class Vampire extends KitInterface {
	public Vampire(Main main) {
		super(main);
	}

	@EventHandler
	public void onDeath(EntityDeathEvent event) {
		if (event.getEntity().getKiller() != null && hasAbility(event.getEntity().getKiller())) {
			Player p = event.getEntity().getKiller();
			if (event.getEntity() instanceof Creature) {
				EntityPlayer p2 = ((CraftPlayer) p).getHandle();
				double hp = p2.getHealth();
				hp += (event.getEntity() instanceof Animals ? 3 : 5);
				if (hp > 20) {
					hp = 20;
				}
				if (hasAbility(p))
					p.setHealth(hp);
			}
		}
	}

	@EventHandler
	public void DeathXP(PlayerDeathEvent e) {
		if (((e.getEntity() instanceof Player)) && ((e.getEntity().getKiller() instanceof Player))) {
			Player p = e.getEntity();
			Player k = p.getKiller();
			if (hasAbility(k))
				k.getInventory().addItem(new ItemStack[] { new ItemStack(Material.POTION, 1, (short) 16421) });
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();

		return new Kit("Vampire", Arrays.asList(new String[] { "Regenere vida matando mobs e ao matar", "um jogador ele dropa uma poçao de vida!" }), kititems, new ItemStack(Material.BOW));
	}
}
