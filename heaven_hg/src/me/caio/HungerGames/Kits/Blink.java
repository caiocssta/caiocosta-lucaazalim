package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;
import me.caio.HungerGames.NMS.barapi.BarAPI;
import me.caio.HungerGames.Utils.Cooldown;
import me.caio.HungerGames.Utils.Enum.GameStage;

public class Blink extends KitInterface {
	private HashMap<UUID, Integer> uses;
	private ArrayList<UUID> timebar;
	private HashSet<Byte> ignore;

	public Blink(Main main) {
		super(main);
		this.uses = new HashMap<UUID, Integer>();
		this.timebar = new ArrayList<UUID>();
		this.ignore = new HashSet<Byte>();
		this.ignore.add(Byte.valueOf((byte) 0));
		for (byte b = 8; b < 12; b = (byte) (b + 1)) {
			this.ignore.add(Byte.valueOf(b));
		}
	}

	HashMap<String, Float> pitch = new HashMap<String, Float>();
	HashMap<String, Float> yaw = new HashMap<String, Float>();

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlink(PlayerInteractEvent event) {
		final Player p = event.getPlayer();
		if (!hasAbility(p)) {
			return;
		}
		if ((event.getItem() == null) || (event.getItem().getType() == Material.AIR)) {
			return;
		}
		if (event.getItem().getType() != Material.NETHER_STAR) {
			return;
		}
		if (Main.getInstance().stage != GameStage.GAMETIME) {
			p.sendMessage(ChatColor.RED + "Voce nao pode usar na invencibilidade");
			return;
		}
		if (Cooldown.isInCooldown(p.getUniqueId(), "blink")) {
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 1.0F, 1.0F);
			int timeleft = Cooldown.getTimeLeft(p.getUniqueId(), "blink");
			p.sendMessage("§6Blink em cooldown de §f" + timeleft + "§6 segundos!");

			return;
		}
		if ((p.getItemInHand().getType() == Material.NETHER_STAR) && (event.getAction() == Action.RIGHT_CLICK_AIR)) {
			if (!this.uses.containsKey(p.getUniqueId())) {
				this.uses.put(p.getUniqueId(), Integer.valueOf(1));
			}
			if (((Integer) this.uses.get(p.getUniqueId())).intValue() <= 3) {
				this.uses.put(p.getUniqueId(), Integer.valueOf(((Integer) this.uses.get(p.getUniqueId())).intValue() + 1));
			}
			if (((Integer) this.uses.get(p.getUniqueId())).intValue() > 3) {
				if (!Cooldown.isInCooldown(p.getUniqueId(), "blink")) {
					Cooldown c = new Cooldown(p.getUniqueId(), "blink", 15);
					c.start();
				}
				this.uses.remove(p.getUniqueId());
				Bukkit.getScheduler().scheduleSyncDelayedTask(getMain(), new Runnable() {
					public void run() {
						Blink.this.timebar.remove(p.getUniqueId());
						BarAPI.removeBar(p);
						p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
					}
				}, 600L);
			}

			pitch.put(p.getName(), p.getLocation().getPitch());
			yaw.put(p.getName(), p.getLocation().getYaw());

			p.teleport(new Location(p.getWorld(), p.getTargetBlock(this.ignore, 5).getLocation().getX(), p.getTargetBlock(this.ignore, 5).getLocation().getY(), p.getTargetBlock(this.ignore, 5).getLocation().getZ(),
					yaw.get(p.getName()), pitch.get(p.getName())));

			if (p.getLocation().clone().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.AIR) {
				p.getLocation().clone().subtract(0.0D, 1.0D, 0.0D).getBlock().setType(Material.LEAVES);
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		kititems.add(createItem(Material.NETHER_STAR, "Blink"));
		return new Kit("blink", Arrays.asList(new String[] { "Teleporte para onde estiver olhando" }), kititems, new ItemStack(Material.NETHER_STAR));

	}
}
