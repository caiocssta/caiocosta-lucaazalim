package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class Archer extends KitInterface implements Listener {
	public Archer(Main main) {
		super(main);
	}

	@EventHandler
	public void flechas(EntityDamageByEntityEvent event) {
		if (((event.getDamager() instanceof Arrow)) && ((event.getEntity() instanceof Player))) {
			Arrow a = (Arrow) event.getDamager();
			if ((a.getShooter() instanceof Player)) {
				Player d = (Player) a.getShooter();
				Player p = (Player) event.getEntity();
				if (hasAbility(d)) {
					if (d.getName() != p.getName()) {
						d.getInventory().addItem(new ItemStack[] { new ItemStack(Material.ARROW) });
						d.updateInventory();
					}
				}
			}
		}
	}

	@EventHandler
	public void Drops(EntityDeathEvent event) {
		EntityType en = event.getEntityType();
		if (!(event.getEntity().getKiller() instanceof Player)) {
			return;
		}
		Player p = event.getEntity().getKiller();
		if ((en.equals(EntityType.SKELETON)) && (hasAbility(p))) {
			event.getDrops().add(new ItemStack(Material.ARROW, 2));
		}
		if ((en.equals(EntityType.CHICKEN)) && (hasAbility(p))) {
			event.getDrops().add(new ItemStack(Material.FEATHER, 2));
		}
	}

	@EventHandler
	public void quebrar(BlockBreakEvent event) {
		if ((hasAbility(event.getPlayer())) && (event.getBlock().getType() == Material.GRAVEL)) {
			event.getBlock().setType(Material.AIR);
			event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation().add(0.5D, 0.0D, 0.5D), new ItemStack(Material.FLINT));
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		ItemStack arco = createItem(Material.BOW, "Arco");
		arco.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
		arco.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		kititems.add(arco);
		kititems.add(new ItemStack(Material.ARROW, 10));
		return new Kit("archer", Arrays.asList(new String[] { "Inicie o jogo com um arco e 10 flechas, cascalhos, galinhas, esqueletos tem chance 100% de drops" }), kititems, new ItemStack(Material.BOW));
	}
}
