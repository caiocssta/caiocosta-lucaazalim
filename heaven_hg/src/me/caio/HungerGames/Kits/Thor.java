package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;
import me.caio.HungerGames.Utils.Cooldown;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Thor extends KitInterface {
	public Thor(Main main) {
		super(main);
	}

	@EventHandler
	public void onThorWood(PlayerInteractEvent event) {
		final Player p = event.getPlayer();
		ItemStack item = p.getItemInHand();
		if (!hasAbility(p)) {
			return;
		}
		if (item == null) {
			return;
		}
		if (item.getType() != Material.WOOD_AXE) {
			return;
		}
		if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
			return;
		}
		if (isInvencibility()) {
			p.sendMessage(ChatColor.RED + "Você não pode usar na invencibilidade.");
			return;
		}
		if (Cooldown.isInCooldown(p.getUniqueId(), "thor")) {
			int timeleft = Cooldown.getTimeLeft(p.getUniqueId(), "thor");
			p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Thor " + ChatColor.GRAY + "em cooldown de " + ChatColor.RESET + ChatColor.BOLD + timeleft + ChatColor.GRAY + " segundos!");
			return;
		}
		Block block = event.getClickedBlock();
		if (block == null) {
			return;
		}
		Location altitude = block.getWorld().getHighestBlockAt(block.getLocation()).getLocation().subtract(0.0D, 1.0D, 0.0D);
		if (altitude.getBlock().getType() == Material.NETHERRACK) {
			p.getWorld().createExplosion(altitude, 3.0F);
			LightningStrike strike = p.getWorld().strikeLightning(altitude);
			strike.setFireTicks(5 * 20);
			strike.setMetadata("DontHurt", new FixedMetadataValue(getMain(), p.getName()));
			if (!Cooldown.isInCooldown(p.getUniqueId(), "thor")) {
				Cooldown c = new Cooldown(p.getUniqueId(), "thor", 5);
				c.start();
			}
		} else {
			LightningStrike strike = p.getWorld().strikeLightning(altitude);
			strike.setMetadata("DontHurt", new FixedMetadataValue(getMain(), p.getName()));
			if (altitude.getBlock().getType() != Material.BEDROCK) {
				if (altitude.getY() >= 80) {
					altitude.getBlock().setType(Material.NETHERRACK);
				}
				altitude.getBlock().getRelative(BlockFace.UP).setType(Material.FIRE);
			}
			if (!Cooldown.isInCooldown(p.getUniqueId(), "thor")) {
				Cooldown c = new Cooldown(p.getUniqueId(), "thor", 5);
				c.start();
			}
			Bukkit.getScheduler().scheduleSyncDelayedTask(getMain(), new Runnable() {
				public void run() {
					p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
				}
			}, 100L);
		}
	}

	@EventHandler
	public void onThorStone(PlayerInteractEvent event) {
		final Player p = event.getPlayer();
		ItemStack item = p.getItemInHand();
		if (!hasAbility(p)) {
			return;
		}
		if (item == null) {
			return;
		}
		if (item.getType() != Material.STONE_AXE) {
			return;
		}
		if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
			return;
		}
		if (Cooldown.isInCooldown(p.getUniqueId(), "thor")) {
			int timeleft = Cooldown.getTimeLeft(p.getUniqueId(), "thor");
			p.sendMessage("§6Thor em cooldown de §f" + timeleft + "§6 segundos!");
			return;
		}
		Block block = event.getClickedBlock();
		if (block == null) {
			return;
		}
		Location altitude = block.getWorld().getHighestBlockAt(block.getLocation()).getLocation().subtract(0.0D, 1.0D, 0.0D);
		if (altitude.getBlock().getType() == Material.NETHERRACK) {
			p.getWorld().createExplosion(altitude, 3.0F);
			LightningStrike strike = p.getWorld().strikeLightning(altitude);
			strike.setFireTicks(5 * 20);
			strike.setMetadata("DontHurt", new FixedMetadataValue(getMain(), p.getName()));
			if (!Cooldown.isInCooldown(p.getUniqueId(), "thor")) {
				Cooldown c = new Cooldown(p.getUniqueId(), "thor", 5);
				c.start();
			}
		} else {
			LightningStrike strike = p.getWorld().strikeLightning(altitude);
			strike.setMetadata("DontHurt", new FixedMetadataValue(getMain(), p.getName()));
			if (altitude.getBlock().getType() != Material.BEDROCK) {
				if (altitude.getY() >= 80) {
					altitude.getBlock().setType(Material.NETHERRACK);
				}
				altitude.getBlock().getRelative(BlockFace.UP).setType(Material.FIRE);
			}
			if (!Cooldown.isInCooldown(p.getUniqueId(), "thor")) {
				Cooldown c = new Cooldown(p.getUniqueId(), "thor", 5);
				c.start();
			}
			Bukkit.getScheduler().scheduleSyncDelayedTask(getMain(), new Runnable() {
				public void run() {
					p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
				}
			}, 100L);
		}
	}

	@EventHandler
	public void onThorIron(PlayerInteractEvent event) {
		final Player p = event.getPlayer();
		ItemStack item = p.getItemInHand();
		if (!hasAbility(p)) {
			return;
		}
		if (item == null) {
			return;
		}
		if (item.getType() != Material.IRON_AXE) {
			return;
		}
		if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
			return;
		}
		if (Cooldown.isInCooldown(p.getUniqueId(), "thor")) {
			int timeleft = Cooldown.getTimeLeft(p.getUniqueId(), "thor");
			p.sendMessage("§6Thor em cooldown de §f" + timeleft + "§6 segundos!");
			return;
		}
		Block block = event.getClickedBlock();
		if (block == null) {
			return;
		}
		Location altitude = block.getWorld().getHighestBlockAt(block.getLocation()).getLocation().subtract(0.0D, 1.0D, 0.0D);
		if ((item.getType() == Material.IRON_AXE) && (altitude.getBlock().getType() == Material.NETHERRACK)) {
			p.getWorld().createExplosion(altitude, 3.0F);
			LightningStrike strike = p.getWorld().strikeLightning(altitude);
			strike.setMetadata("DontHurt", new FixedMetadataValue(getMain(), p.getName()));
			for (Entity entity : p.getNearbyEntities(3.0D, 3.0D, 3.0D)) {
				if ((entity instanceof Player)) {
					entity.setFireTicks(100);
				}
			}
			if (!Cooldown.isInCooldown(p.getUniqueId(), "thor")) {
				Cooldown c = new Cooldown(p.getUniqueId(), "thor", 5);
				c.start();
			}
		} else {
			LightningStrike strike = p.getWorld().strikeLightning(altitude);
			strike.setMetadata("DontHurt", new FixedMetadataValue(getMain(), p.getName()));
			if (altitude.getBlock().getType() != Material.BEDROCK) {
				if (altitude.getY() >= 80) {
					altitude.getBlock().setType(Material.NETHERRACK);
				}
				altitude.getBlock().getRelative(BlockFace.UP).setType(Material.FIRE);
			}
			for (Entity entity : p.getNearbyEntities(3.0D, 3.0D, 3.0D)) {
				if ((entity instanceof Player)) {
					entity.setFireTicks(100);
				}
			}
			if (!Cooldown.isInCooldown(p.getUniqueId(), "thor")) {
				Cooldown c = new Cooldown(p.getUniqueId(), "thor", 5);
				c.start();
			}
			Bukkit.getScheduler().scheduleSyncDelayedTask(getMain(), new Runnable() {
				public void run() {
					p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
				}
			}, 100L);
		}
	}

	@EventHandler
	public void onThorDiamond(PlayerInteractEvent event) {
		final Player p = event.getPlayer();
		ItemStack item = p.getItemInHand();
		if (!hasAbility(p)) {
			return;
		}
		if (item == null) {
			return;
		}
		if (item.getType() != Material.DIAMOND_AXE) {
			return;
		}
		if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
			return;
		}
		if (Cooldown.isInCooldown(p.getUniqueId(), "thor")) {
			int timeleft = Cooldown.getTimeLeft(p.getUniqueId(), "thor");
			p.sendMessage("§6Thor em cooldown de §f" + timeleft + "§6 segundos!");
			return;
		}
		Block block = event.getClickedBlock();
		if (block == null) {
			return;
		}
		Location altitude = block.getWorld().getHighestBlockAt(block.getLocation()).getLocation().subtract(0.0D, 1.0D, 0.0D);
		if ((item.getType() == Material.DIAMOND_AXE) && (altitude.getBlock().getType() == Material.NETHERRACK)) {
			p.getWorld().createExplosion(altitude, 1.0F);
			LightningStrike strike = p.getWorld().strikeLightning(altitude);
			strike.setMetadata("DontHurt", new FixedMetadataValue(getMain(), p.getName()));
			if (!Cooldown.isInCooldown(p.getUniqueId(), "thor")) {
				Cooldown c = new Cooldown(p.getUniqueId(), "thor", 5);
				c.start();
			}
		} else {
			p.getWorld().createExplosion(altitude, 1.0F);
			LightningStrike strike = p.getWorld().strikeLightning(altitude);
			strike.setMetadata("DontHurt", new FixedMetadataValue(getMain(), p.getName()));
			if (!Cooldown.isInCooldown(p.getUniqueId(), "thor")) {
				Cooldown c = new Cooldown(p.getUniqueId(), "thor", 5);
				c.start();
			}
			Bukkit.getScheduler().scheduleSyncDelayedTask(getMain(), new Runnable() {
				public void run() {
					p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
				}
			}, 100L);
		}
	}

	@EventHandler
	public void onThorGold(PlayerInteractEvent event) {
		final Player p = event.getPlayer();
		ItemStack item = p.getItemInHand();
		if (!hasAbility(p)) {
			return;
		}
		if (item == null) {
			return;
		}
		if (item.getType() != Material.GOLD_AXE) {
			return;
		}
		if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
			return;
		}
		if (Cooldown.isInCooldown(p.getUniqueId(), "thor")) {
			int timeleft = Cooldown.getTimeLeft(p.getUniqueId(), "thor");
			p.sendMessage("§6Thor em cooldown de §f" + timeleft + "§6 segundos!");
			return;
		}
		Block block = event.getClickedBlock();
		if (block == null) {
			return;
		}
		Location altitude = block.getWorld().getHighestBlockAt(block.getLocation()).getLocation().subtract(0.0D, 1.0D, 0.0D);
		if ((item.getType() == Material.GOLD_AXE) && (altitude.getBlock().getType() == Material.NETHERRACK)) {
			p.getWorld().createExplosion(altitude, 3.0F);
			LightningStrike strike = p.getWorld().strikeLightning(altitude);
			strike.setMetadata("DontHurt", new FixedMetadataValue(getMain(), p.getName()));
			for (Entity entity : p.getNearbyEntities(3.0D, 3.0D, 3.0D)) {
				if ((entity instanceof Player)) {
					((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
				}
			}
			if (!Cooldown.isInCooldown(p.getUniqueId(), "thor")) {
				Cooldown c = new Cooldown(p.getUniqueId(), "thor", 5);
				c.start();
			}
		} else {
			LightningStrike strike = p.getWorld().strikeLightning(altitude);
			strike.setMetadata("DontHurt", new FixedMetadataValue(getMain(), p.getName()));
			if (altitude.getBlock().getType() != Material.BEDROCK) {
				if (altitude.getY() >= 80) {
					altitude.getBlock().setType(Material.NETHERRACK);
				}
				altitude.getBlock().getRelative(BlockFace.UP).setType(Material.FIRE);
			}
			for (Entity entity : p.getNearbyEntities(3.0D, 3.0D, 3.0D)) {
				if ((entity instanceof Player)) {
					((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
				}
			}
			if (!Cooldown.isInCooldown(p.getUniqueId(), "thor")) {
				Cooldown c = new Cooldown(p.getUniqueId(), "thor", 5);
				c.start();
			}
			Bukkit.getScheduler().scheduleSyncDelayedTask(getMain(), new Runnable() {
				public void run() {
					p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
				}
			}, 100L);
		}
	}

	@EventHandler
	public void onBlockIgnite(BlockIgniteEvent event) {
		if (event.getCause() == BlockIgniteEvent.IgniteCause.LIGHTNING) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onHabilidadeDeHitReceberDano(EntityDamageByEntityEvent event) {
		Entity vitima = event.getEntity();
		Entity atacante = event.getDamager();
		if (vitima.isDead()) {
			return;
		}
		if (!(vitima instanceof Player)) {
			return;
		}
		Player p = (Player) vitima;
		if (((atacante instanceof LightningStrike)) && (atacante.hasMetadata("DontHurt")) && (p.getName().equals(((MetadataValue) event.getDamager().getMetadata("DontHurt").get(0)).value()))) {
			event.setCancelled(true);
			return;
		}
		if ((atacante instanceof LightningStrike)) {
			event.setDamage(4.0D);
		}
	}

	@EventHandler
	public void onChange(final PlayerDeathEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		if (!(e.getEntity().getKiller() instanceof Player)) {
			return;
		}
		Player p = e.getEntity().getKiller();
		if (p.getLastDamageCause() == null) {
			return;
		}
		EntityDamageEvent.DamageCause cause = p.getLastDamageCause().getCause();
		if (cause == null) {
			return;
		}
		if (cause == EntityDamageEvent.DamageCause.LIGHTNING) {
			e.getEntity().damage(1.0D, p);
			Bukkit.getScheduler().scheduleSyncDelayedTask(getMain(), new Runnable() {
				public void run() {
					Main.getInstance().dropItems(e.getEntity(), e.getEntity().getKiller().getLocation());
				}
			}, 20L);
		}
	}

	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<ItemStack>();
		kitItems.add(new ItemStack(Material.WOOD_AXE));
		return new Kit("thor", Arrays.asList(new String[] { "Liberte um raio com seu machado!" }), kitItems, new ItemStack(Material.WOOD_AXE));
	}
}
