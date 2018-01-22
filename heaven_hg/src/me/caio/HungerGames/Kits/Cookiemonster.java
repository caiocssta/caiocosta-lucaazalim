package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;
import net.minecraft.server.v1_7_R4.EntityPlayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Cookiemonster extends KitInterface {
	public Cookiemonster(Main main) {
		super(main);
	}

	private HashMap<Player, Long> cookieExpires = new HashMap<Player, Long>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onChomp(PlayerInteractEvent event) {
		if (event.getAction().name().contains("RIGHT")) {
			Player p = event.getPlayer();
			if (!cookieExpires.containsKey(p) || cookieExpires.get(p) < System.currentTimeMillis()) {
				if (hasAbility(p) && event.getItem() != null && event.getItem().getType() == Material.COOKIE) {
					if (!(p.getLocation().getBlockX() > Bukkit.getWorld("world").getSpawnLocation().getBlockX() + 490)) {
						if (!(p.getLocation().getBlockX() < -(490 - Bukkit.getWorld("world").getSpawnLocation().getBlockX()))) {
							if (!(p.getLocation().getBlockZ() > Bukkit.getWorld("world").getSpawnLocation().getBlockZ() + 490)) {
								if (!(p.getLocation().getBlockZ() < -(490 - Bukkit.getWorld("world").getSpawnLocation().getBlockZ()))) {
									event.setCancelled(true);
									EntityPlayer p2 = ((CraftPlayer) p).getHandle();
									if (p2.getHealth() < 20) {
										double hp = p2.getHealth() + 1;
										if (hp > 20)
											hp = 20;
										p.setHealth(hp);
									} else if (p.getFoodLevel() < 20) {
										p.setFoodLevel(p.getFoodLevel() + 1);
									} else {
										p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1), true);
									}
									event.getItem().setAmount(event.getItem().getAmount() - 1);
									if (event.getItem().getAmount() == 0)
										p.setItemInHand(new ItemStack(0));
									cookieExpires.put(p, System.currentTimeMillis() + 500);
								}
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onDamage(BlockDamageEvent event) {
		Player p = event.getPlayer();
		if (hasAbility(p)) {
			if (event.getBlock().getType() == Material.LONG_GRASS && new Random().nextInt(4) == 0) {
				Location loc = event.getBlock().getLocation().clone();
				loc.getWorld().dropItemNaturally(loc.add(0.5, 0, 0.5), new ItemStack(Material.COOKIE));
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();

		return new Kit("Cookiemonster", Arrays.asList(new String[] { "Use seus biscoitos para se curar, se alimentar ou receber velocidade!" }), kititems, new ItemStack(Material.COOKIE));
	}
}
