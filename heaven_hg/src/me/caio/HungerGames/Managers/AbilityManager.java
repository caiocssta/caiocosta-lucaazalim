package me.caio.HungerGames.Managers;

import java.util.HashMap;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Utils.ClassGetter;

import org.bukkit.event.Listener;

public class AbilityManager {
	private HashMap<String, KitInterface> abilities = new HashMap<String, KitInterface>();
	private Main m;

	public AbilityManager(Main m) {
		this.m = m;
		initializeAllAbilitiesInPackage("me.caio.HungerGames.Kits");
	}

	public void initializeAllAbilitiesInPackage(String packageName) {
		int i = 0;
		for (Class<?> abilityClass : ClassGetter.getClassesForPackage(this.m, packageName)) {
			if (KitInterface.class.isAssignableFrom(abilityClass)) {
				try {
					KitInterface abilityListener;
					try {
						abilityListener = (KitInterface) abilityClass.getConstructor(new Class[] { Main.class }).newInstance(new Object[] { this.m });
					} catch (Exception e) {
						abilityListener = (KitInterface) abilityClass.newInstance();
					}
					this.abilities.put(abilityClass.getSimpleName(), abilityListener);
					m.kit.loadKitPrices(abilityListener.getKitName(), abilityClass.getSimpleName());
				} catch (Exception e) {
					e.printStackTrace();
					System.out.print("Erro ao carregar a habilidade " + abilityClass.getSimpleName());
				}
				i++;
			}
		}
		this.m.getLogger().info(i + " habilidades carregadas!");
	}

	public void registerAbilityListeners() {
		for (Listener abilityListener : this.abilities.values()) {
			this.m.getServer().getPluginManager().registerEvents(abilityListener, this.m);
		}
	}
}
