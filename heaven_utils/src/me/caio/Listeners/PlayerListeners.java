package me.skater.Listeners;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.scheduler.BukkitRunnable;

import me.skater.Main;
import me.skater.Commands.Staff;
import me.skater.Utils.Cooldown;
import me.skater.permissions.enums.Group;

@SuppressWarnings("deprecation")
public class PlayerListeners implements Listener {

	public Main m;

	public PlayerListeners(Main main) {
		this.m = main;
	}

	public String getTime() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(final PlayerLoginEvent event) {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					m.name.updateAPI(event.getPlayer());
				} catch (Exception e) {
					event.disallow(PlayerLoginEvent.Result.KICK_OTHER,
							ChatColor.RED + "Nao foi possivel atualizar seus dados.");
				}
			}
		}.runTaskAsynchronously(m);
	}

	@EventHandler
	public void vipExpireCheck(PlayerJoinEvent e) {
		final Player p = e.getPlayer();
		new BukkitRunnable() {

			@Override
			public void run() {
				PreparedStatement stmt;
				try {
					stmt = m.getSQL().prepareStatement(
							"SELECT * FROM ranks WHERE uuid='" + p.getUniqueId().toString().replace("-", "") + "';");
					ResultSet rs = stmt.executeQuery();

					if (rs.next()) {
						if (rs.getBoolean("temporary")) {
							if ((rs.getLong("rank_time") + 1000 * rs.getInt("rank_left")) > System.currentTimeMillis()) {

								System.out.println("CARREGADO O RANK DO " + p.getName() + " - TEMPO RESTANTE: " + m.getBanManager().getStatsTime(Math.round(-((System.currentTimeMillis()
										- (rs.getLong("rank_time") + 1000 * rs.getInt("rank_left"))) / 1000L))));
							} else {
								
								stmt.execute("DELETE FROM ranks WHERE uuid='" + p.getUniqueId() + "';");
								m.getPermissionManager().removePlayer(p.getUniqueId());
								p.sendMessage("    ");
								p.sendMessage("§cO seu rank expirou... :(");
								p.sendMessage("§aMas você pode renová-lo!");
								p.sendMessage("§aAcesse loja.heavenmc.com.br");
								p.sendMessage("    ");
								m.getPermissionManager().setPlayerGroup(p, Group.NORMAL);
								m.getTagManager().removePlayerTag(p);
								
							}
						} else {
							System.out.println("rank permanente " + p.getName());
						}
					}
					stmt.close();
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(m);
	}

	@EventHandler
	public void onPlayerLoginBanned(final PlayerLoginEvent event) {
		final Player p = event.getPlayer();
		new BukkitRunnable() {

			@Override
			public void run() {
				try {
					PreparedStatement stmt = m.getSQL().prepareStatement("SELECT * FROM heaven_bans WHERE ban_uuid='"
							+ p.getUniqueId().toString().replace("-", "") + "'");
					ResultSet rs = stmt.executeQuery();
					if (rs.next()) {
						if (rs.getBoolean("tempban")) {
							if ((rs.getLong("ban_time") + 1000 * rs.getInt("ban_left")) > System.currentTimeMillis()) {
								String time = m.getBanManager().getStatsTime(Math.round(-((System.currentTimeMillis()
										- (rs.getLong("ban_time") + 1000 * rs.getInt("ban_left"))) / 1000L)));
								event.disallow(Result.KICK_BANNED, m.getBanManager().getTempBanMessage(p.getUniqueId(),
										rs.getString("ban_reason"), time, rs.getString("banned_by")));
							} else {
								stmt.execute("DELETE FROM heaven_bans WHERE ban_uuid='"
										+ p.getUniqueId().toString().replace("-", "") + "'");
							}
						} else {
							event.disallow(Result.KICK_BANNED, m.getBanManager().getBanMessage(p.getUniqueId(),
									rs.getString("banned_by"), rs.getString("ban_reason")));
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(m);
	}

	@EventHandler
	public void playerChat(PlayerChatEvent event) {
		Player p = event.getPlayer();
		/*
		 * for(String string : s){
		 * if(event.getMessage().toLowerCase().contains(string)){
		 * event.setMessage(event.getMessage().toLowerCase().replace(string,
		 * "")); } }
		 */
		if (!m.perm.isTrial(p)) {
			String msg = event.getMessage();
			msg = msg.toLowerCase();
			event.setMessage(msg);
		}

		if (m.getClanManager().isOnClanChat(p)) {
			String rawmsg = event.getMessage();
			event.setCancelled(true);
			m.getClanManager().clanPlayerMessage(p, rawmsg);
		}

		if (Staff.chatStaff.contains(p.getName())) {
			String rawmsg = event.getMessage();
			event.setCancelled(true);
			String msg = ChatColor.translateAlternateColorCodes('&',
					"§7- §6[Staff] §4" + p.getName() + ": §f" + rawmsg + "");
			for (Player online : Bukkit.getOnlinePlayers())
				if (Staff.chatStaff.contains(online.getName())) {
					online.sendMessage(msg);
				} else {
					if (this.m.perm.isTrial(online)) {
						online.sendMessage(msg);
					}
				}
		}
	}

	@EventHandler
	public void onPreCommand(PlayerCommandPreprocessEvent e) {
		if (!this.m.perm.isTrial(e.getPlayer())) {

			if (Cooldown.isInCooldown(e.getPlayer().getUniqueId(), "comando")) {
				e.setCancelled(true);
				e.getPlayer().sendMessage("§cAguarde um pouco para executar outro comando!");
				Cooldown cd = new Cooldown(e.getPlayer().getUniqueId(), "comando", 3);
				cd.start();
				return;
			}

			Cooldown cd = new Cooldown(e.getPlayer().getUniqueId(), "comando", 3);
			cd.start();

		}
	}

	@EventHandler
	public void onPreProcessCommand(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().toLowerCase().startsWith("/me ")) {
			event.setCancelled(true);
		}
		if (event.getMessage().split(" ")[0].contains(":")) {
			event.setCancelled(true);
		}
	}

	private String[] blockedWords = { "mush.com.br", "battlebits.com.br", "mc-alpha.com.br", "battlebits.net", "mush",
			"wombo", "faz evento", "pl kibado", "wave", "wavemc.com.br", "like", "likekits", "empire",
			"empire-network.com.br" };

	@EventHandler
	public void onAsync(PlayerChatEvent e) {
		if (this.m.globalmute == true) {
			if (!this.m.perm.isTrial(e.getPlayer())) {
				e.setCancelled(true);
				return;
			}
		}
		for (String blockedWord : blockedWords) {
			if (e.getMessage().toLowerCase().contains(blockedWord) && !m.perm.isTrial(e.getPlayer())) {
				e.setCancelled(true);
				e.getPlayer().sendMessage("§cSua frase contém palavras feias.");
				return;
			}
		}
		e.setFormat(m.getTagManager().getPlayerActiveTag(e.getPlayer()).getPrefix() + e.getPlayer().getName()
				+ ChatColor.RESET + ChatColor.GRAY + " §7["
				+ m.getRankManager().getPlayerRank(e.getPlayer()).getRank().getSymbol() + "§r§7]" + " » "
				+ ChatColor.WHITE + "%2$s");
		if (!this.m.perm.isTrial(e.getPlayer())) {

			if (Cooldown.isInCooldown(e.getPlayer().getUniqueId(), "chat")) {
				e.setCancelled(true);
				e.getPlayer().sendMessage("§cDigite mais devagar...");
				Cooldown cd = new Cooldown(e.getPlayer().getUniqueId(), "chat", 2);
				cd.start();
				return;
			}

			Cooldown cd = new Cooldown(e.getPlayer().getUniqueId(), "chat", 2);
			cd.start();

		}

	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if (m.getInventoryManager().isOpen(p)) {
			m.getInventoryManager().handle(p);
		}
	}
}
