package br.com.wombocraft.lobby.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.wombocraft.lobby.Lobby;
import br.com.wombocraft.lobby.LobbyManager;
import br.com.wombocraft.lobby.LobbyPlugin;
import br.com.wombocraft.lobby.admin.comando.AdminComando;
import br.com.wombocraft.lobby.admin.comando.StaffComando;
import br.com.wombocraft.lobby.gamer.Gamer;
import br.com.wombocraft.lobby.rank.enums.RankType;
import br.com.wombocraft.lobby.utils.ItemBuilder;

public class AdminManager extends LobbyPlugin {
	public ArrayList<Player> admin;
	public List<UUID> _playerschat;
	public ItemStack adminstack;
	private Vanish _vanish;

	public AdminManager(LobbyManager hcm) {
		super("Admin Manager", hcm);
		this.admin = new ArrayList<Player>();
		this.adminstack = new ItemStack(Material.MAGMA_CREAM);
		ItemMeta a = adminstack.getItemMeta();
		a.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "Você esta no modo Admin");
		this.adminstack.setItemMeta(a);
		this._vanish = new Vanish();
		this._playerschat = new ArrayList<>();
		AddCommand(new AdminComando(this));
		AddCommand(new StaffComando(this));
	}

	public Vanish getVanish() {
		return this._vanish;
	}

	public void setAdmin(final Player p) {
		if ((!admin.contains(p)) || (admin.isEmpty())) {
			admin.add(p);
		}
		p.setGameMode(GameMode.CREATIVE);
		this.getVanish().makeVanished(p);
		for (Gamer gamers : Lobby.getLobbyManager().getGamerManager().getGamers()) {
			if (!gamers.getRank().hasRank(RankType.MOD)) {
				gamers.getPlayer().hidePlayer(p);
			}
		}
		this.getVanish().updateVanished();
		p.sendMessage(ChatColor.LIGHT_PURPLE + "Você entrou no modo Admin");
		p.sendMessage(ChatColor.LIGHT_PURPLE + "Você está invisível");
		p.getInventory().clear();
		p.getInventory().setItem(4, adminstack);
		p.updateInventory();
	}

	public void setPlayer(Player p) {
		admin.remove(p);
		for (Gamer gamers : Lobby.getLobbyManager().getGamerManager().getGamers()) {
			if (gamers.getPlayer() != null) {
				if (!gamers.getRank().hasRank(RankType.MOD)) {
					gamers.getPlayer().showPlayer(p);
				}
			}
		}
		p.setGameMode(GameMode.SURVIVAL);
		this.getVanish().makeVisible(p);
		p.sendMessage(ChatColor.LIGHT_PURPLE + "Você saiu do modo Admin!");
		p.sendMessage(ChatColor.LIGHT_PURPLE + "Você está visível para todos!");
		this.getVanish().updateVanished();
		p.getInventory().clear();
		p.getInventory().setItem(0, new ItemBuilder(Material.COMPASS).nome("§aServers").construir());
	}

	public boolean isAdmin(Player p) {
		return admin.contains(p);
	}

	public class Vanish {
		public ArrayList<UUID> vanished;

		public Vanish() {
			this.vanished = new ArrayList<UUID>();
		}

		public void makeVanished(Player p) {
			for (Gamer gamers : Lobby.getLobbyManager().getGamerManager().getGamers()) {
				if (gamers.getPlayer() != null) {
					if (!gamers.getRank().hasRank(RankType.MOD)) {
						gamers.getPlayer().hidePlayer(p);
					}
				}
			}
			for (Gamer player : Lobby.getLobbyManager().getGamerManager().getGamers()) {
				if (player.getPlayer() != null) {
					if (!player.getName().equals(p.getName())) {
						if (!player.getRank().hasRank(RankType.MOD)) {
							if (player.getPlayer().canSee(p)) {
								player.getPlayer().hidePlayer(p);
							}
						}
					}
				}
			}
			if (!vanished.contains(p.getUniqueId())) {
				vanished.add(p.getUniqueId());
			}
		}

		public boolean isVanished(Player p) {
			return vanished.contains(p.getUniqueId());
		}

		@SuppressWarnings("deprecation")
		public void updateVanished() {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (isVanished(p)) {
					makeVanished(p);
				} else if (!isVanished(p)) {
					makeVisible(p);
				}
			}
		}

		public void updateVanished(Player player) {
			for (Gamer p : Lobby.getLobbyManager().getGamerManager().getGamers()) {
				player.showPlayer(p.getPlayer());
				if (!player.getName().equals(p.getName())) {
					if (isVanished(p.getPlayer())) {
						if (!p.getRank().hasRank(RankType.MOD)) {
							if (player.canSee(p.getPlayer())) {
								player.hidePlayer(p.getPlayer());
							}
						}
					}
				}
			}
		}

		@SuppressWarnings("deprecation")
		public void makeVisible(Player p) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (!player.canSee(p)) {
					player.showPlayer(p);
				}
			}
			vanished.remove(p.getUniqueId());
		}
	}
}
