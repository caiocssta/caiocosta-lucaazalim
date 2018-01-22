package br.com.wombocraft.lobby.menu.servers;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import br.com.wombocraft.lobby.Lobby;
import br.com.wombocraft.lobby.gamer.Gamer;
import br.com.wombocraft.lobby.menu.handlers.InventoryCustom;
import br.com.wombocraft.lobby.menu.handlers.enums.MenuType;
import br.com.wombocraft.lobby.utils.ItemBuilder;
import net.minecraft.util.com.google.common.io.ByteArrayDataOutput;
import net.minecraft.util.com.google.common.io.ByteStreams;

public class ServersMenu extends InventoryCustom {

	public ServersMenu(Gamer g) {
		super(g, MenuType.SERVER, "Modos de Jogo", 27, Bukkit.createInventory(null, 27, ""));
	}

	@Override
	public void inventoryClickEvent(InventoryClickEvent event) {
		event.setCancelled(true);
		if (event.getCurrentItem().getType() == Material.MUSHROOM_SOUP) {
			sendBungeeMessage((Player) event.getWhoClicked(), "HardcoreGames");
		} else if (event.getCurrentItem().getType() == Material.DIAMOND_CHESTPLATE) {
			sendBungeeMessage((Player) event.getWhoClicked(), "TeamHardcoreGames");
		} else if (event.getCurrentItem().getType() == Material.BEACON) {
			sendBungeeMessage((Player) event.getWhoClicked(), "PvP");
		} else if (event.getCurrentItem().getType() == Material.ARROW) {
			sendBungeeMessage((Player) event.getWhoClicked(), "UltraHardcoreGames");
		}
	}

	@Override
	public void inventory() {
		getInventory().setItem(13, new ItemBuilder(Material.MUSHROOM_SOUP).nome("§aHardcoreGames")
				.lore(Arrays.asList("§7Jogadores Online: " + Lobby.getLobbyManager().getServerManager().getHGPlayer()))
				.construir());
	/*	getInventory().setItem(12,
				new ItemBuilder(Material.DIAMOND_CHESTPLATE).nome("§aTeam HardcoreGames")
						.lore(Arrays.asList(
								"§7Jogadores Online: " + Lobby.getLobbyManager().getServerManager().getTeamHGPlayer()))
						.construir());
		getInventory().setItem(14, new ItemBuilder(Material.BEACON).nome("§aPvP")
				.lore(Arrays.asList("§7Jogadores Online: " + Lobby.getLobbyManager().getServerManager().getPvPPlayers()))
				.construir());
		getInventory().setItem(16,
				new ItemBuilder(Material.ARROW).nome("§aUltra HardcoreGames")
						.lore(Arrays.asList(
								"§7Jogadores Online: " + Lobby.getLobbyManager().getServerManager().getUltraHGPlayer()))
						.construir()); */
	}

	public void sendBungeeMessage(Player p, String server) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(server);
		p.sendPluginMessage(Lobby.getLobby(), "BungeeCord", out.toByteArray());
		p.closeInventory();
	}

}
