package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;
import me.caio.HungerGames.Utils.Enum.GameStage;
import net.minecraft.server.v1_7_R4.EntityPlayer;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Worm extends KitInterface {
	public Worm(Main main) {
		super(main);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDamage(BlockDamageEvent e) {
		Player p = e.getPlayer();
		if (hasAbility(p)) {
			if (Main.getInstance().stage != GameStage.PREGAME) {
				if (e.getBlock().getType() == Material.DIRT) {
					boolean drop = true;
					EntityPlayer p2 = ((CraftPlayer) p).getHandle();
					if (p2.getHealth() < p2.getMaxHealth()) {
						double hp = p2.getHealth() + 1;
						if (hp > p2.getMaxHealth())
							hp = p2.getMaxHealth();
						p.setHealth((int) hp);
						drop = false;
					} else if (p.getFoodLevel() < 20) {
						p.setFoodLevel(p.getFoodLevel() + 1);
						drop = false;
					} else if (p2.getHealth() > 19) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 2, 1));
						drop = false;
					}
					e.getBlock().getWorld().playEffect(e.getBlock().getLocation(), Effect.STEP_SOUND, Material.DIRT.getId());
					e.getBlock().setType(Material.AIR);

					if (drop)
						e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation().add(0.5, 0, 0.5), new ItemStack(Material.DIRT));
				}
			}
		}
	}

	@EventHandler
	public void onWormDamage(EntityDamageEvent e) {
		if (e.isCancelled()) {
			return;
		}
		if ((e.getEntity() instanceof Player)) {
			Player p = (Player) e.getEntity();
			if ((hasAbility(p)) && (e.getCause() == EntityDamageEvent.DamageCause.FALL)) {
				Location loc = e.getEntity().getLocation();
				Location l = loc.subtract(0.0D, 1.0D, 0.0D);
				if (l.getBlock().getType() == Material.DIRT) {
					e.setCancelled(true);
					p.damage(1.0D);
				}
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		return new Kit("worm", Arrays.asList(new String[] { "Quebre terra instantâneamente", "cure sua vida com elas!" }), kititems, new ItemStack(Material.DIRT));
	}
}
