package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;
import me.caio.HungerGames.Utils.Cooldown;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Flash extends KitInterface {

	@SuppressWarnings("deprecation")
	public Flash(Main main) {
		super(main);
		Blocos.add((byte) 0);
		for (byte b = 8; b < 12; b++)
			Blocos.add(b);
		Blocos.add((byte) Material.SNOW.getId());
		Blocos.add((byte) Material.LONG_GRASS.getId());
		Blocos.add((byte) Material.RED_MUSHROOM.getId());
		Blocos.add((byte) Material.RED_ROSE.getId());
		Blocos.add((byte) Material.YELLOW_FLOWER.getId());
		Blocos.add((byte) Material.BROWN_MUSHROOM.getId());
		Blocos.add((byte) Material.SIGN_POST.getId());
		Blocos.add((byte) Material.WALL_SIGN.getId());
		Blocos.add((byte) Material.FIRE.getId());
		Blocos.add((byte) Material.TORCH.getId());
		Blocos.add((byte) Material.REDSTONE_WIRE.getId());
		Blocos.add((byte) Material.REDSTONE_TORCH_OFF.getId());
		Blocos.add((byte) Material.REDSTONE_TORCH_ON.getId());
		Blocos.add((byte) Material.VINE.getId());
		Blocos.add((byte) Material.WATER_LILY.getId());
	}

	public static HashSet<FallingBlock> blocos = new HashSet<FallingBlock>();
	ArrayList<String> chao = new ArrayList<String>();

	@SuppressWarnings("deprecation")
	public void Equalizador(final Location loc, final Player p) {
		int x = 0;
		int y = 0;
		int z = 0;
		if (loc.clone().add(x, y, z).getBlock().getType() == Material.AIR) {
			for (x = -4; x < 4; x++) {
				for (z = -4; z < 4; z++) {
					for (y = 0; y < 4; y++) {
						final Block b = loc.clone().add(x, 1.0D, z).getBlock();
						FallingBlock fb = b.getWorld().spawnFallingBlock(b.getLocation(), Material.REDSTONE_BLOCK, (byte) 0);
						fb.setDropItem(false);
						blocos.add(fb);
						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
							public void run() {
								if (chao.contains(p.getName()))
									chao.remove(p.getName());
							}
						}, 20 * 10);
					}
				}
			}
		}
	}

	@EventHandler
	public void bl(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Block b = e.getBlock();
		if (chao.contains(p.getName())) {
			if (b.getType() == Material.REDSTONE_BLOCK) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void Boost(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Location loc = new Location(p.getWorld(), e.getTo().getX(), e.getTo().getY() - 1.0D, e.getTo().getZ());

		if (chao.contains(p.getName())) {
			if (loc.getBlock().getType() == Material.REDSTONE_BLOCK) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 1));
				p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20, 0));
			}
		}
	}

	@EventHandler
	public void Trocar(EntityChangeBlockEvent e) {
		if (e.getEntity() instanceof FallingBlock) {
			final FallingBlock fb = (FallingBlock) e.getEntity();
			final Block cima = e.getBlock().getRelative(BlockFace.DOWN);

			if (blocos.contains(fb)) {
				e.setCancelled(true);
				fb.setDropItem(false);
				cima.setType(Material.REDSTONE_BLOCK);
				blocos.remove(fb);

				new BukkitRunnable() {
					int tempo = 10;

					public void run() {
						tempo--;
						if (tempo < 0) {
							cima.setType(Material.DIRT);
							cancel();
						}
					}
				}.runTaskTimer(Main.getInstance(), 0, 20);
			}
		}
	}

	@EventHandler
	public void Om(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if ((e.getAction() == Action.RIGHT_CLICK_AIR) || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (p.getItemInHand().getType() == Material.REDSTONE) {
				if (hasAbility(p)) {
					e.setCancelled(true);
					p.updateInventory();
					if (isInvencibility()) {
						p.sendMessage("§cVoce nao pode usar este Kit antes da invencibilidade!");
						return;
					}
					if (!Cooldown.isInCooldown(p.getUniqueId(), "equalizer")) {
						Equalizador(p.getLocation(), p);
						if (!chao.contains(p.getName()))
							chao.add(p.getName());
						Cooldown c = new Cooldown(p.getUniqueId(), "equalizer", 50);
						c.start();
					} else {
						int timeleft = Cooldown.getTimeLeft(p.getUniqueId(), "equalizer");
						p.sendMessage("§6Equalizer em cooldown de §f" + timeleft + "§6 segundos!");
					}
				}
			}
		}
	}

	private HashSet<Byte> Blocos = new HashSet<Byte>();

	@EventHandler
	public void Marcar(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		Block b = e.getBlock();
		if (b.getType() == Material.REDSTONE_TORCH_ON) {
			if (hasAbility(p)) {
				e.setCancelled(true);
				p.updateInventory();
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void dadsa(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (p.getItemInHand().getType() == Material.REDSTONE_TORCH_ON) {
			if (e.getAction() == Action.RIGHT_CLICK_AIR) {
				if (hasAbility(p)) {
					if (Cooldown.isInCooldown(p.getUniqueId(), "flash")) {
						e.setCancelled(true);
						int timeleft = Cooldown.getTimeLeft(p.getUniqueId(), "flash");
						p.sendMessage("§6Flash em cooldown de §f" + timeleft + "§6 segundos!");
					} else {
						e.setCancelled(true);
						List<Block> b = p.getLastTwoTargetBlocks(Blocos, 200);
						if (b.size() > 1 && b.get(1).getType() != Material.AIR) {
							double distancia = p.getLocation().distance(b.get(0).getLocation());
							p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
							p.getEyeLocation().getWorld().playEffect(p.getLocation(), Effect.ENDER_SIGNAL, 5);
							Location loc = b.get(0).getLocation().clone().add(0.5, 0.5, 0.5);
							loc.setPitch(p.getLocation().getPitch());
							loc.setYaw(p.getLocation().getYaw());
							p.teleport(loc);
							p.getWorld().strikeLightningEffect(loc);
							p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, (int) ((distancia / 2) * 20), 1), true);
							Cooldown c = new Cooldown(p.getUniqueId(), "flash", 50);
							c.start();
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		kititems.add(createItem(Material.getMaterial(76), "§cFlash"));
		return new Kit("flash", Arrays.asList(new String[] { "Teleporte-se instantâneamente para", "onde quer que você esteja olhando." }), kititems, new ItemStack(Material.getMaterial(76)));
	}
}
