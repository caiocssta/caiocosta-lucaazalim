package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;
import me.caio.HungerGames.Utils.Enum.GameStage;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Urgal extends KitInterface {
	private HashMap<UUID, Long> potionDelay;

	public Urgal(Main main) {
		super(main);
		this.potionDelay = new HashMap<UUID, Long>();
	}

	@EventHandler
	public void onDropUrgal(PlayerDropItemEvent event) {
		if ((event.getItemDrop().getItemStack().getType() == Material.POTION) && (event.getItemDrop().getItemStack().getDurability() == 8201)) {
			event.setCancelled(true);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onChomp(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if ((e.getAction().name().contains("RIGHT")) && (hasAbility(p))) {
			if (Main.getInstance().stage != GameStage.GAMETIME) {
				p.sendMessage(ChatColor.RED + "Voce nao pode usar na invencibilidade.");
				return;
			}
			if (((!this.potionDelay.containsKey(p.getUniqueId())) || (((Long) this.potionDelay.get(p.getUniqueId())).longValue() < System.currentTimeMillis())) && (e.getItem() != null)
					&& (e.getItem().getType() == Material.POTION) && (e.getItem().getDurability() == 8201)) {
				e.setCancelled(true);

				p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1800, 0), true);

				e.getItem().setAmount(e.getItem().getAmount() - 1);
				if (e.getItem().getAmount() == 0) {
					p.setItemInHand(new ItemStack(0));
				}
				this.potionDelay.put(p.getUniqueId(), Long.valueOf(System.currentTimeMillis() + 1000L));
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		items.add(createItem(Material.POTION, 3, (short) 8201, null, new String[] { "Força" }));
		return new Kit("urgal", Arrays.asList(new String[] { "Fique mais forte bebendo pocao de forca" }), items, new ItemStack(Material.POTION, 1, (short) 8201));
	}
}
