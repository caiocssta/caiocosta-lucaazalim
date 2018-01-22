package me.caio.HungerGames.Kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Events.TimeSecondEvent;
import me.caio.HungerGames.Managers.KitInterface;
import me.caio.HungerGames.NMS.barapi.BarAPI;
import me.caio.HungerGames.Utils.Enum.GameStage;

public class Madman extends KitInterface {

	public Madman(Main main) {
		super(main);
	}

	private HashMap<String, Double> efeito = new HashMap<String, Double>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void madmanEffect(TimeSecondEvent event) {
		if (this.getMain().stage != GameStage.GAMETIME) {
			return;
		}
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (hasAbility(p)) {
				ArrayList<Player> nearby = new ArrayList<Player>();
				for (Entity e : p.getNearbyEntities(20, 20, 20)) {
					if (e instanceof Player) {
						if (!this.getMain().adm.isSpectating((Player) e)) {
							nearby.add((Player) e);
						}
					}
				}
				if (nearby.size() > 0) {
					for (Player t : nearby) {
						if (!efeito.containsKey(t.getName()))
							efeito.put(t.getName(), 0.0);
						else
							efeito.put(t.getName(), efeito.get(t.getName()) + 0.6);

						if (efeito.get(t.getName()) <= 300.0) {
							if (efeito.get(t.getName()) % 10.0 == 0.0) {
								t.playSound(t.getLocation(), Sound.AMBIENCE_CAVE, 1, 1);
								t.sendMessage("§cMadman por perto");
							}

							BarAPI.setMessage(t, "Efeito do madman de " + efeito.get(t.getName()).intValue() * 2 + "%");
							if (p.isOnline())
								BarAPI.setMessage(p, "Efeito do madman de " + efeito.get(t.getName()).intValue() * 2 + "%");
						}
					}
				}
				for (Player players : Bukkit.getOnlinePlayers()) {
					if (players != p) {
						if (!nearby.contains(players)) {
							if (efeito.containsKey(players.getName())) {
								efeito.put(players.getName(), efeito.get(players.getName()) - 2);
								BarAPI.setMessage(players, "Efeito do madman de " + efeito.get(players.getName()).intValue() * 2 + "%");
								if (efeito.get(players.getName()) < -1) {
									efeito.remove(players.getName());
									if (players.isOnline()) {
										BarAPI.removeBar(players);
									}
									if (p.isOnline()) {
										BarAPI.removeBar(p);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void dano(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if ((e.getDamager() instanceof Player)) {
				if (efeito.containsKey(p.getName())) {
					e.setDamage(e.getDamage() + (efeito.get(p.getName()) / 10) - 1);
				}
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		return new Kit("madman", Arrays.asList(new String[] { "Deixe seus inimigos fracos ao", "chegar perto deles!" }), kititems, new ItemStack(Material.REDSTONE));
	}
}
