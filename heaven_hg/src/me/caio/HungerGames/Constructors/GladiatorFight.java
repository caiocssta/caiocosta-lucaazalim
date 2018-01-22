package me.caio.HungerGames.Constructors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Kits.Gladiator;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class GladiatorFight {
	private static Main main;
	private Player gladiator;
	private Player target;
	private Location tpLocGladiator;
	private Location tpLocTarget;
	private BukkitRunnable witherEffect;
	private BukkitRunnable teleportBack;
	private List<Block> blocksToRemove;
	private Listener listener;
	public ArrayList<BO3Blocks> glad1;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public GladiatorFight(final Player gladiator, final Player target, Main m) {
		main = m;
		this.gladiator = gladiator;
		this.target = target;
		this.blocksToRemove = new ArrayList();
		this.glad1 = Main.getInstance().glad1;
		send1v1();
		this.listener = new Listener() {
			@EventHandler
			public void onEntityDamage(EntityDamageByEntityEvent event) {
				if (((event.getEntity() instanceof Player)) && ((event.getDamager() instanceof Player))) {
					Player recebe = (Player) event.getEntity();
					Player faz = (Player) event.getDamager();
					if ((GladiatorFight.this.isIn1v1(faz)) && (GladiatorFight.this.isIn1v1(recebe))) {
						return;
					}
					if ((GladiatorFight.this.isIn1v1(faz)) && (!GladiatorFight.this.isIn1v1(recebe))) {
						event.setCancelled(true);
					} else if ((!GladiatorFight.this.isIn1v1(faz)) && (GladiatorFight.this.isIn1v1(recebe))) {
						event.setCancelled(true);
					}
				}
			}

			public HashMap<String, Long> PodeChecar = new HashMap();

			public void updateCheck(Player p) {
				PodeChecar.put(p.getName(), System.currentTimeMillis());
			}

			public boolean podeChecar(Player p) {
				return System.currentTimeMillis() - PodeChecar.get(p.getName()) >= 10000L;
			}

			@EventHandler(priority = EventPriority.LOWEST)
			public void onDeath(PlayerDeathEvent event) {
				Player p = event.getEntity();
				if (!GladiatorFight.this.isIn1v1(p)) {
					return;
				}
				if (p == gladiator) {
					target.sendMessage(ChatColor.GRAY + "Voce venceu o 1v1 contra " + ChatColor.RED + gladiator.getName() + ChatColor.GRAY + "!");
					target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
					gladiator.sendMessage(ChatColor.GRAY + "Voce perdeu o 1v1 contra " + ChatColor.RED + target.getName() + ChatColor.GRAY + "!");
					GladiatorFight.main.dropItems(p, event.getDrops(), GladiatorFight.this.tpLocGladiator);
					event.getDrops().clear();
					GladiatorFight.this.teleportBack(target, gladiator);
					return;
				}
				gladiator.sendMessage(ChatColor.GRAY + "Voce venceu o 1v1 contra " + ChatColor.RED + target.getName() + ChatColor.GRAY + "!");
				gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
				target.sendMessage(ChatColor.RED + "Voce perdeu o 1v1 contra " + ChatColor.RED + gladiator.getName() + ChatColor.GRAY + "!");
				GladiatorFight.main.dropItems(p, event.getDrops(), GladiatorFight.this.tpLocTarget);
				event.getDrops().clear();
				GladiatorFight.this.teleportBack(gladiator, target);
			}

			@EventHandler
			public void onQuit(PlayerQuitEvent event) {
				Player p = event.getPlayer();
				if (!GladiatorFight.this.isIn1v1(p)) {
					return;
				}
				if (event.getPlayer().isDead()) {
					return;
				}
				if (p == gladiator) {
					target.sendMessage(ChatColor.RED.toString() + gladiator.getName() + ChatColor.GRAY + " deslogou!" + ChatColor.GRAY + "Voce venceu!");
					target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
					GladiatorFight.main.dropItems(p, GladiatorFight.this.tpLocGladiator);
					GladiatorFight.this.teleportBack(target, gladiator);
					return;
				}
				gladiator.sendMessage(ChatColor.RED.toString() + target.getName() + ChatColor.GRAY + " deslogou!" + ChatColor.GRAY + "Voce venceu!");
				gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
				GladiatorFight.main.dropItems(p, GladiatorFight.this.tpLocTarget);
				GladiatorFight.this.teleportBack(gladiator, target);
			}

			@EventHandler
			public void onKick(PlayerKickEvent event) {
				Player p = event.getPlayer();
				if (!GladiatorFight.this.isIn1v1(p)) {
					return;
				}
				if (event.getPlayer().isDead()) {
					return;
				}
				if (p == gladiator) {
					target.sendMessage(ChatColor.RED.toString() + gladiator.getName() + ChatColor.GRAY + " deslogou!" + ChatColor.GRAY + "Voce venceu!");
					target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
					GladiatorFight.main.dropItems(p, GladiatorFight.this.tpLocGladiator);
					GladiatorFight.this.teleportBack(target, gladiator);
					return;
				}
				gladiator.sendMessage(ChatColor.RED.toString() + target.getName() + ChatColor.GRAY + " deslogou!" + ChatColor.GRAY + "Voce venceu!");
				gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
				GladiatorFight.main.dropItems(p, GladiatorFight.this.tpLocTarget);
				GladiatorFight.this.teleportBack(gladiator, target);
			}

			@EventHandler
			public void onQuitGladiator(final PlayerMoveEvent e) {
				if (!GladiatorFight.this.isIn1v1(e.getPlayer())) {
					return;
				}
				if (GladiatorFight.this.blocksToRemove.contains(e.getTo().getBlock())) {
					return;
				}

				if (!PodeChecar.containsKey(e.getPlayer().getName()))
					PodeChecar.put(e.getPlayer().getName(), 0L);
				if (e.getPlayer() == gladiator) {
					new BukkitRunnable() {
						@Override
						public void run() {
							if (!podeChecar(e.getPlayer())) {
								return;
							}
							e.getPlayer().setFallDistance(0);
							GladiatorFight.this.teleportBack(target, gladiator);
							e.getPlayer().setFallDistance(0);
							updateCheck(e.getPlayer());
							return;

						}
					}.runTaskLater(main, 30L);
				}
				new BukkitRunnable() {
					@Override
					public void run() {
						if (!podeChecar(e.getPlayer())) {
							return;
						}
						e.getPlayer().setFallDistance(0);
						GladiatorFight.this.teleportBack(gladiator, target);
						e.getPlayer().setFallDistance(0);
						updateCheck(e.getPlayer());
						return;

					}
				}.runTaskLater(main, 30L);

			}

			@EventHandler
			public void onTeleport(PlayerTeleportEvent event) {
				if (event.isCancelled()) {
					return;
				}
				Player p = event.getPlayer();
				if (!GladiatorFight.this.isIn1v1(p)) {
					return;
				}
				if (!Gladiator.inGladiator(p)) {
					return;
				}
				if (GladiatorFight.this.blocksToRemove.contains(event.getTo().getBlock())) {
					return;
				}
				if (p == gladiator) {
					target.sendMessage(ChatColor.RED.toString() + gladiator.getName() + ChatColor.GRAY + " foi teleportado para fora!");
					gladiator.sendMessage(ChatColor.GRAY + "Voce foi teleportado para fora do Gladiator!");
					GladiatorFight.this.teleportBack(event.getTo(), GladiatorFight.this.tpLocTarget);
					return;
				}
				if (p == target) {
					gladiator.sendMessage(ChatColor.RED.toString() + gladiator.getName() + ChatColor.GRAY + " foi teleportado para fora!");
					target.sendMessage(ChatColor.GRAY + "Voce foi teleportado para fora do Gladiator!");
					GladiatorFight.this.teleportBack(GladiatorFight.this.tpLocGladiator, event.getTo());
				}
			}
		};
		Main.getInstance().getServer().getPluginManager().registerEvents(this.listener, Main.getInstance());
	}

	public boolean isIn1v1(Player player) {
		return (player == this.gladiator) || (player == this.target);
	}

	public void destroy() {
		HandlerList.unregisterAll(this.listener);
	}

	public void send1v1() {
		Location loc = this.gladiator.getLocation();
		boolean hasGladi = true;
		while (hasGladi) {
			hasGladi = false;
			boolean stop = false;
			for (double x = -8.0D; x <= 8.0D; x += 1.0D) {
				for (double z = -8.0D; z <= 8.0D; z += 1.0D) {
					for (double y = 0.0D; y <= 21.0D; y += 1.0D) {
						Location l = new Location(loc.getWorld(), loc.getX() + x, 100.0D + y, loc.getZ() + z);
						if (l.getBlock().getType() != Material.AIR) {
							hasGladi = true;
							loc = new Location(loc.getWorld(), loc.getX() + 20.0D, loc.getY(), loc.getZ());
							stop = true;
						}
						if (stop) {
							break;
						}
					}
					if (stop) {
						break;
					}
				}
				if (stop) {
					break;
				}
			}
		}
		Block mainBlock = loc.getBlock();
		generateArena(mainBlock);
		this.tpLocGladiator = this.gladiator.getLocation().clone();
		this.tpLocTarget = this.target.getLocation().clone();
		String messagegla = ChatColor.GRAY + "Voce puxou " + ChatColor.RED.toString() + this.target.getName() + ChatColor.GRAY + " para 1v1." + ChatColor.GRAY + "\nVoce tem 5 segundos de invencibilidade.";
		String messagetar = ChatColor.RED.toString() + this.gladiator.getName() + ChatColor.GRAY + " Puxou voce para 1v1." + ChatColor.GRAY + "\nVoce tem 5 segundos de invencibilidade.";
		this.gladiator.sendMessage(messagegla);
		this.target.sendMessage(messagetar);
		Location l1 = new Location(mainBlock.getWorld(), mainBlock.getX() + 6.5D, 101.0D, mainBlock.getZ() + 6.5D);
		l1.setYaw(135.0F);
		Location l2 = new Location(mainBlock.getWorld(), mainBlock.getX() - 5.5D, 101.0D, mainBlock.getZ() - 5.5D);
		l2.setYaw(315.0F);
		this.target.teleport(l1);
		this.gladiator.teleport(l2);
		Gladiator.playersIn1v1.add(this.gladiator.getUniqueId());
		Gladiator.playersIn1v1.add(this.target.getUniqueId());
		this.gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
		this.target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
		this.witherEffect = new BukkitRunnable() {
			public void run() {
				GladiatorFight.this.gladiator.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 1200, 5));
				GladiatorFight.this.target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 1200, 5));
			}
		};
		this.witherEffect.runTaskLater(Main.getInstance(), 2400L);
		this.teleportBack = new BukkitRunnable() {
			public void run() {
				GladiatorFight.this.teleportBack(GladiatorFight.this.tpLocGladiator, GladiatorFight.this.tpLocTarget);
			}
		};
		this.teleportBack.runTaskLater(Main.getInstance(), 3600L);
	}

	public void teleportBack(Location loc, Location loc1) {
		Gladiator.playersIn1v1.remove(this.gladiator.getUniqueId());
		Gladiator.playersIn1v1.remove(this.target.getUniqueId());
		this.gladiator.teleport(loc);
		this.target.teleport(loc1);
		removeBlocks();
		this.gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
		this.target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
		this.gladiator.removePotionEffect(PotionEffectType.WITHER);
		this.target.removePotionEffect(PotionEffectType.WITHER);
		stop();
		destroy();
	}

	public void teleportBack(Player winner, Player loser) {
		Gladiator.playersIn1v1.remove(winner.getUniqueId());
		Gladiator.playersIn1v1.remove(loser.getUniqueId());
		winner.teleport(this.tpLocGladiator);
		loser.teleport(this.tpLocTarget);
		removeBlocks();
		winner.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
		winner.removePotionEffect(PotionEffectType.WITHER);
		stop();
		destroy();
	}

	public void generateBlocks(Block mainBlock) {
		for (double x = -8.0D; x <= 8.0D; x += 1.0D) {
			for (double z = -8.0D; z <= 8.0D; z += 1.0D) {
				for (double y = 0.0D; y <= 20.0D; y += 1.0D) {
					Location l = new Location(mainBlock.getWorld(), mainBlock.getX() + x, 100.0D + y, mainBlock.getZ() + z);
					l.getBlock().setType(Material.GLASS);
					Gladiator.gladiatorBlocks.add(l.getBlock());
					this.blocksToRemove.add(l.getBlock());
				}
			}
		}
		for (double x = -7.0D; x <= 7.0D; x += 1.0D) {
			for (double z = -7.0D; z <= 7.0D; z += 1.0D) {
				for (double y = 1.0D; y <= 19.0D; y += 1.0D) {
					Location l = new Location(mainBlock.getWorld(), mainBlock.getX() + x, 100.0D + y, mainBlock.getZ() + z);
					l.getBlock().setType(Material.AIR);
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void generateArena(Block mainBlock) {
		Location l = mainBlock.getLocation();
		l.setY(100.0D);
		for (BO3Blocks bo3 : this.glad1) {
			Block b = new Location(l.getWorld(), l.getX() + bo3.getX(), l.getY() + bo3.getY(), l.getZ() + bo3.getZ()).getBlock();
			b.setType(bo3.getType());
			b.setData(bo3.getData());
			Gladiator.gladiatorBlocks.add(b);
			this.blocksToRemove.add(b);
		}
		for (double x = -7.0D; x <= 7.0D; x += 1.0D) {
			for (double z = -7.0D; z <= 7.0D; z += 1.0D) {
				for (double y = 1.0D; y <= 20.0D; y += 1.0D) {
					Location loc = new Location(mainBlock.getWorld(), mainBlock.getX() + x, 100.0D + y, mainBlock.getZ() + z);
					this.blocksToRemove.add(loc.getBlock());
				}
			}
		}
	}

	public void removeBlocks() {
		for (Block b : this.blocksToRemove) {
			if ((b.getType() != null) && (b.getType() != Material.AIR)) {
				b.setType(Material.AIR);
			}
			Gladiator.gladiatorBlocks.remove(b);
		}
		this.blocksToRemove.clear();
	}

	public void stop() {
		this.witherEffect.cancel();
		this.teleportBack.cancel();
	}
}
