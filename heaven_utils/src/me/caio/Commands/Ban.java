package me.skater.Commands;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.skater.Main;
import me.skater.permissions.enums.Group;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Ban implements CommandExecutor {
	private Main m;

	public Ban(Main m) {
		this.m = m;
	}

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("bana")) {
			if ((sender instanceof Player)) {
				Player player = (Player) sender;
				if (!m.getPermissionManager().hasGroupPermission(player, Group.TRIAL)) {
					sender.sendMessage(ChatColor.RED + "Voce nao possui permissao.");
					return true;
				}
			}
			if (args.length >= 2) {
				final StringBuilder reason = new StringBuilder();
				for (int i = 1; i < args.length; i++) {
					reason.append(args[i]).append(" ");
				}

				new BukkitRunnable() {

					@Override
					public void run() {
						String id = null;
						try {
							id = m.name.getUUID(args[0]);
						} catch (Exception e) {
						}
						if (id == null) {
							sender.sendMessage(ChatColor.RED + "O jogador §c§l" + args[0] + "§c não existe!");
							return;
						}

						try {
							PreparedStatement stmt = m.getSQL().prepareStatement("SELECT * FROM heaven_bans WHERE ban_uuid='" + id + "'");
							ResultSet rs = stmt.executeQuery();
							if (!rs.next()) {
								stmt.execute(
										"INSERT INTO heaven_bans VALUES(NULL, '" + id + "', '" + args[0] + "', '" + sender.getName() + "', '" + reason.toString() + "', 'false', '" + System.currentTimeMillis() + "', '0')");
								String kickmsg = ChatColor.RED + "Sua conta foi suspensa permanentemente por " + sender.getName() + "\n§cMotivo: " + reason.toString() + "\n§aCompre seu unban em: §fHeavenMC.com.br";
								if (Bukkit.getPlayer(args[0]) != null) {
									kickPlayer(Bukkit.getPlayer(args[0]), kickmsg);
								}
								sender.sendMessage(ChatColor.GREEN + args[0] + "foi banido com sucesso.");
							} else {
								sender.sendMessage(ChatColor.RED + args[0] + " ja está banido!");
							}
							rs.close();
							stmt.close();
						} catch (SQLException e) {
							System.out.println("Erro ao banir " + id);
							e.printStackTrace();
						}

					}
				}.runTaskAsynchronously(m);

			}
		}
		return false;
	}

	public void kickPlayer(final Player player, final String message) {
		new BukkitRunnable() {
			@Override
			public void run() {
				player.kickPlayer(message);
			}
		}.runTask(m);
	}

}
