package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Managers.KitInterface;
import me.caio.HungerGames.Utils.Enum.GameStage;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Launcher extends KitInterface {
	public Launcher(Main main) {
		super(main);
	}

	@EventHandler
	public void Morte(final PlayerDeathEvent e) {
		final Player p = e.getEntity();
		if (hasAbility(p)) {
			for (ListIterator<ItemStack> item = e.getDrops().listIterator(); item.hasNext();) {
				ItemStack i = item.next();
				if ((i.getItemMeta().hasDisplayName()) && (i.getItemMeta().getDisplayName().equals("Esponja"))) {
					item.remove();
				}
			}
		}
	}

	@SuppressWarnings("static-access")
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if ((event.getEntity() instanceof Player)) {
			Player player = (Player) event.getEntity();
			if ((this.nofalldamage.contains(player.getName())) && (event.getCause().equals(EntityDamageEvent.DamageCause.FALL))) {
				event.setCancelled(true);
				if ((hasAbility(player, "Stomper"))) {
					return;
				}
				if ((hasAbility(player, "Tower"))) {
					return;
				}
				this.nofalldamage.remove(player.getName());

			}
		}
	}

	public static ArrayList<String> nofalldamage = new ArrayList<String>();
	ArrayList<String> nofalldamagewait = new ArrayList<String>();

	@SuppressWarnings("static-access")
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (!(Main.getInstance().stage == GameStage.WINNER)) {
			Location standBlock = player.getWorld().getBlockAt(player.getLocation().add(0.0D, -0.01D, 0.0D)).getLocation();
			if (standBlock.getBlock().getType() == Material.SPONGE) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3 * 20, 0));
				player.setVelocity(new Vector(0, 3, 0));

				if (!this.nofalldamage.contains(player.getName())) {
					this.nofalldamage.add(player.getName());
				}

				if (hasAbility(player, "Stomper")) {
					nofalldamage.remove(player.getName());
					return;
				}
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		kititems.add(createItem(Material.SPONGE, 20, "Esponja"));

		return new Kit("Launcher", Arrays.asList(new String[] { "Pule em esponjas para te lançar", "para cima ou para frente!" }), kititems, new ItemStack(Material.SPONGE));
	}
}
