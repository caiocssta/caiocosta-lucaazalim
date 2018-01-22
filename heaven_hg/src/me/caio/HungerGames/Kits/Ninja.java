package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

public class Ninja extends KitInterface {
	private HashMap<String, NinjaHit> ninjados;

	public Ninja(Main main) {
		super(main);
		this.ninjados = new HashMap<String, NinjaHit>();
	}

	@EventHandler
	public void onNinjaHit(EntityDamageByEntityEvent event) {
		if (((event.getDamager() instanceof Player)) && ((event.getEntity() instanceof Player))) {
			Player damager = (Player) event.getDamager();
			Player damaged = (Player) event.getEntity();
			if (hasAbility(damager, "ninja")) {
				NinjaHit ninjaHit = (NinjaHit) this.ninjados.get(damager.getName());
				if (ninjaHit == null) {
					ninjaHit = new NinjaHit(damaged);
				} else {
					ninjaHit.setTarget(damaged);
				}
				this.ninjados.put(damager.getName(), ninjaHit);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onShift(PlayerToggleSneakEvent event) {
		Player p = event.getPlayer();
		if (!event.isSneaking()) {
			return;
		}
		if (!hasAbility(p)) {
			return;
		}
		if (!this.ninjados.containsKey(p.getName())) {
			return;
		}
		NinjaHit ninjaHit = (NinjaHit) this.ninjados.get(p.getName());
		Player target = ninjaHit.getTarget();
		if (target.isDead()) {
			return;
		}
		if (Main.getInstance().adm.isSpectating(target)) {
			return;
		}
		if (target.getInventory().contains(342)) {
			return;
		}
		if (p.getLocation().distance(target.getLocation()) > 50.0D) {
			return;
		}
		if (p.getLocation().getY() - target.getLocation().getY() > 20.0D) {
			return;
		}
		if (ninjaHit.getTargetExpires() < System.currentTimeMillis()) {
			return;
		}
		if (ninjaHit.getCooldown() > System.currentTimeMillis()) {
			p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Ninja " + ChatColor.GRAY + "em cooldown!");
			return;
		}
		p.teleport(target.getLocation());
		p.sendMessage(ChatColor.GREEN + "Teleportado");
		ninjaHit.teleport();
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player p = event.getEntity();
		if (!this.ninjados.containsKey(p.getName())) {
			return;
		}
		this.ninjados.remove(p.getName());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		if (!this.ninjados.containsKey(p.getName())) {
			return;
		}
		this.ninjados.remove(p.getName());
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		return new Kit("ninja", Arrays.asList(new String[] { "Aperte shift e teleporte para o utltimo jogador hitado." }), kititems, new ItemStack(Material.EMERALD));
	}

	private static class NinjaHit {
		private Player target;
		private long cooldown;
		private long targetExpires;

		public NinjaHit(Player target) {
			this.target = target;
			this.cooldown = 0L;
			this.targetExpires = (System.currentTimeMillis() + 15000L);
		}

		public Player getTarget() {
			return this.target;
		}

		public long getCooldown() {
			return this.cooldown;
		}

		public long getTargetExpires() {
			return this.targetExpires;
		}

		public void teleport() {
			this.cooldown = (System.currentTimeMillis() + 5000L);
		}

		public void setTarget(Player player) {
			this.target = player;
			this.targetExpires = (System.currentTimeMillis() + 20000L);
		}
	}
}
