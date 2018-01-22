package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;
import net.minecraft.server.v1_7_R4.EntityWolf;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftWolf;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Beastmaster extends KitInterface {
	public Beastmaster(Main main) {
		super(main);
	}

	@EventHandler
	public void Morte(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if (hasAbility(p)) {
			for (ListIterator<ItemStack> item = e.getDrops().listIterator(); item.hasNext();) {
				ItemStack i = (ItemStack) item.next();
				if ((i.getItemMeta().hasDisplayName()) && (i.getItemMeta().getDisplayName().equals("§eLobos"))) {
					item.remove();
				}
			}
		}
	}

	@EventHandler
	public void drop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		ItemStack i = e.getItemDrop().getItemStack();
		if ((hasAbility(p)) && (i.getItemMeta().hasDisplayName()) && (i.getItemMeta().getDisplayName().equals("§eLobos"))) {
			p.setItemInHand(i);
			e.getItemDrop().remove();
		}
	}

	@EventHandler
	public void DeathXP(PlayerDeathEvent e) {
		if (((e.getEntity() instanceof Player)) && ((e.getEntity().getKiller() instanceof Player))) {
			Player p = e.getEntity();
			Player k = p.getKiller();
			if (hasAbility(k)) {
				Wolf wolf = (Wolf) p.getWorld().spawnEntity(k.getLocation(), EntityType.WOLF);
				wolf.setOwner(p);
				wolf.setAdult();
				EntityWolf w2 = ((CraftWolf) wolf).getHandle();
				wolf.setHealth(w2.getMaxHealth());
				wolf.setCustomName("§bLobo de " + p.getName());
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		ItemStack item = p.getItemInHand();
		if ((event.getAction() == Action.RIGHT_CLICK_BLOCK) && (item != null) && (item.getType() == Material.MONSTER_EGG) && (hasAbility(p))) {
			if (isInvencibility()) {
				p.sendMessage(ChatColor.RED + "Você não pode usar na invencibilidade.");
				return;
			}
			event.setCancelled(true);
			Wolf wolf = (Wolf) p.getWorld().spawnEntity(event.getClickedBlock().getRelative(event.getBlockFace()).getLocation(), EntityType.WOLF);
			wolf.setOwner(p);
			EntityWolf w2 = ((CraftWolf) wolf).getHandle();
			wolf.setHealth(w2.getMaxHealth());
			wolf.setHealth(w2.getMaxHealth());
			wolf.setCustomName("§bLobo de " + p.getName());
			if (item.getAmount() > 1) {
				item.setAmount(item.getAmount() - 1);
			} else {
				item = null;
			}
			p.setItemInHand(item);
		}
	}

	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<ItemStack>();
		kitItems.add(new ItemStack(Material.MONSTER_EGG, 3));
		return new Kit("beastmaster", Arrays.asList(new String[] { "Spawne seus lobos com 3 ovos" }), kitItems, new ItemStack(Material.MONSTER_EGG, (short) 1));
	}
}
