package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;
import me.caio.HungerGames.Utils.Cooldown;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Jackhammer extends KitInterface {
	public Jackhammer(Main main) {
		super(main);
	}

	public static HashMap<String, Integer> Usou = new HashMap<String, Integer>();

	@EventHandler
	public void Jack(BlockBreakEvent e) {
		Player p = e.getPlayer();
		final Block bloco = e.getBlock();
		final Block cima = bloco.getRelative(BlockFace.UP);
		final Block baixo = bloco.getRelative(BlockFace.DOWN);
		if (p.getItemInHand().getType() == Material.STONE_AXE) {
			if (hasAbility(p)) {
				if (isInvencibility()) {
					p.sendMessage(ChatColor.RED + "Você não pode usar na invencibilidade.");
					return;
				}
				if (Cooldown.isInCooldown(p.getUniqueId(), "Jackhammer")) {
					int timeleft = Cooldown.getTimeLeft(p.getUniqueId(), "Jackhammer");
					p.sendMessage("§6Jackhammer em cooldown de §f" + timeleft + "§6 segundos!");
					return;
				} else {
					if (Usou.get(p.getName()) == null)
						Usou.put(p.getName(), Integer.valueOf(0));

					new BukkitRunnable() {
						Block b = cima;

						public void run() {
							if (Gladiator.gladiatorBlocks.contains(b)) {
								cancel();
								return;
							}
							if (b.getType() != Material.AIR) {
								b.setType(Material.AIR);
								b = b.getRelative(BlockFace.UP);
							} else
								cancel();
						}
					}.runTaskTimer(this.getMain(), 20, 1);

					new BukkitRunnable() {
						Block b = baixo;

						public void run() {
							if (Gladiator.gladiatorBlocks.contains(b)) {
								cancel();
								return;
							}
							if (b.getType() != Material.AIR && b.getType() != Material.BEDROCK) {
								b.setType(Material.AIR);
								b = b.getRelative(BlockFace.DOWN);
							} else
								cancel();
						}
					}.runTaskTimer(this.getMain(), 20, 1);

					Usou.put(p.getName(), Usou.get(p.getName()) + 1);
					if (Usou.get(p.getName()) >= 6) {
						Cooldown c = new Cooldown(p.getUniqueId(), "Jackhammer", 60);
						c.start();
						Usou.put(p.getName(), Integer.valueOf(0));
					}

				}
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		kititems.add(createItem(Material.STONE_AXE, ""));

		return new Kit("Jackhammer", Arrays.asList(new String[] { "Quebre um bloco e veja todos os", "blocos sobre esse se quebrando automaticamente." }), kititems, new ItemStack(Material.STONE_AXE));
	}
}
