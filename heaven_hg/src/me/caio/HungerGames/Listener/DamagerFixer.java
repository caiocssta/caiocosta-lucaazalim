package me.caio.HungerGames.Listener;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DamagerFixer implements Listener {
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamageEvent(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player)) {
			return;
		}
		Player p = (Player) event.getDamager();
		ItemStack sword = p.getItemInHand();
		double damage = event.getDamage();
		double danoEspada = getDamage(sword.getType());
		boolean isMore = false;
		if (damage > 2.0D) {
			isMore = true;
		}
		if (p.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
			for (PotionEffect effect : p.getActivePotionEffects()) {
				if (effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
					double minus;
					if (isCrital(p)) {
						minus = (danoEspada + danoEspada / 2.0D) * 1.3D * (effect.getAmplifier() + 1);
					} else {
						minus = danoEspada * 1.3D * (effect.getAmplifier() + 1);
					}
					damage -= minus;
					damage += 2 * (effect.getAmplifier() + 1);
					break;
				}
			}
		}
		if (!sword.getEnchantments().isEmpty()) {
			if ((sword.containsEnchantment(Enchantment.DAMAGE_ARTHROPODS)) && (isArthropod(event.getEntityType()))) {
				damage -= 1.5D * sword.getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS);
				damage += 1 * sword.getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS);
			}
			if ((sword.containsEnchantment(Enchantment.DAMAGE_UNDEAD)) && (isUndead(event.getEntityType()))) {
				damage -= 1.5D * sword.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD);
				damage += 1 * sword.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD);
			}
			if (sword.containsEnchantment(Enchantment.DAMAGE_ALL)) {
				damage -= 1.25D * sword.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
				damage += 1 * sword.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
			}
		}
		if (isCrital(p)) {
			damage -= danoEspada / 2.0D;
			damage += 1.0D;
		}
		if (isMore) {
			damage -= 2.0D;
		}
		event.setDamage(damage);
	}

	@SuppressWarnings("deprecation")
	private boolean isCrital(Player p) {
		return (p.getFallDistance() > 0.0F) && (!p.isOnGround()) && (!p.hasPotionEffect(PotionEffectType.BLINDNESS));
	}

	@SuppressWarnings("incomplete-switch")
	private boolean isArthropod(EntityType type) {
		switch (type) {
		case MINECART_HOPPER:
			return true;
		case LEASH_HITCH:
			return true;
		case MINECART_MOB_SPAWNER:
			return true;
		}
		return false;
	}

	@SuppressWarnings("incomplete-switch")
	private boolean isUndead(EntityType type) {
		switch (type) {
		case ITEM_FRAME:
			return true;
		case MAGMA_CUBE:
			return true;
		case ENDER_CRYSTAL:
			return true;
		case MINECART_COMMAND:
			return true;
		}
		return false;
	}

	private double getDamage(Material type) {
		double damage = 1.0D;
		if (type.toString().contains("DIAMOND_")) {
			damage = 8.0D;
		} else if (type.toString().contains("IRON_")) {
			damage = 7.0D;
		} else if (type.toString().contains("STONE_")) {
			damage = 6.0D;
		} else if (type.toString().contains("WOOD_")) {
			damage = 5.0D;
		} else if (type.toString().contains("GOLD_")) {
			damage = 5.0D;
		}
		if (!type.toString().contains("_SWORD")) {
			damage -= 1.0D;
			if (!type.toString().contains("_AXE")) {
				damage -= 1.0D;
				if (!type.toString().contains("_PICKAXE")) {
					damage -= 1.0D;
					if (!type.toString().contains("_SPADE")) {
						damage = 1.0D;
					}
				}
			}
		}
		return damage;
	}
}
