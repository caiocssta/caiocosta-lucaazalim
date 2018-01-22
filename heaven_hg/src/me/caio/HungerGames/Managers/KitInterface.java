package me.caio.HungerGames.Managers;

import java.util.Arrays;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Constructors.Kit;
import me.caio.HungerGames.Utils.KitManager;
import me.caio.HungerGames.Utils.Enum.GameStage;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class KitInterface implements Listener {
	private String kitName;
	private KitManager kitManager;
	private Main m;
	private static KitInterface ki;

	public KitInterface(Main main) {
		this.m = main;
		this.kitManager = this.m.kit;
		Kit kit = getKit();
		this.kitName = kit.name;
		this.m.kit.addKit(this.kitName, kit);
	}

	public KitManager getKitManager() {
		return this.kitManager;
	}

	public String getKitName() {
		return this.kitName;
	}

	public Main getMain() {
		return this.m;
	}

	public static KitInterface getKi() {
		return ki;
	}

	public boolean isInvencibility() {
		if (m.stage == GameStage.INVENCIBILITY)
			return true;
		return false;
	}

	public abstract Kit getKit();

	public boolean hasAbility(Player player) {
		return this.kitManager.hasAbility(player, this.kitName);
	}

	public Server getServer() {
		return getMain().getServer();
	}

	public boolean hasAbility(Player player, String kitName) {
		return this.kitManager.hasAbility(player, kitName);
	}

	public static ItemStack createItem(Material mat, String name) {
		return createItem(mat, 1, (short) 0, name, new String[] { "" });
	}

	public static ItemStack createItem(Material mat, String name, String... description) {
		return createItem(mat, 1, (short) 0, name, description);
	}

	public static ItemStack createItem(Material mat, int quantidade, String name, String... description) {
		return createItem(mat, quantidade, (short) 0, name, description);
	}

	public static ItemStack createItem(Material mat, int quantidade, short data, String name, String... description) {
		ItemStack item = new ItemStack(mat, quantidade, data);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(name);
		if ((description.length > 0) && (!description[0].isEmpty())) {
			im.setLore(Arrays.asList(description));
		}
		item.setItemMeta(im);
		return item;
	}
}
