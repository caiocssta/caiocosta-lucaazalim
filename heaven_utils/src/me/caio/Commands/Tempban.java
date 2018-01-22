package me.skater.Commands;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.skater.Main;
import me.skater.permissions.enums.Group;

public class Tempban implements CommandExecutor {

	private Main m;

	public Tempban(Main m) {
		this.m = m;
	}

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("tempban")) {
			if ((sender instanceof Player)) {
				Player player = (Player) sender;
				if (!m.getPermissionManager().hasGroupPermission(player, Group.TRIAL)) {
					sender.sendMessage(ChatColor.RED + "Voce nao possui permissao.");
					return true;
				}
			}
			if (args.length >= 3) {
				final int time;
				try {
					time = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					sender.sendMessage("§cVocê só pode digitar números nesse campo!");
					return true;
				}

				final StringBuilder reason = new StringBuilder();
				for (int i = 2; i < args.length; i++) {
					reason.append(args[i]).append(" ");
				}
				new BukkitRunnable() {
					public void run() {
						String id = m.name.getUUID(args[0]);
						if (id == null) {
							sender.sendMessage(
									ChatColor.RED + "O jogador §c§l" + args[0] + " §c não existe no banco de dados!");
							return;
						}
						try {
							PreparedStatement stmt = m.getSQL()
									.prepareStatement("SELECT * FROM heaven_bans WHERE ban_uuid='" + id + "'");
							ResultSet rs = stmt.executeQuery();
							if (!rs.next()) {
								stmt.execute("INSERT INTO heaven_bans VALUES(NULL, '" + id + "', '" + args[0] + "', '"
										+ sender.getName() + "', '" + reason.toString() + "', 'true', '"
										+ System.currentTimeMillis() + "', '" + time + "')");
								String kickmsg = ChatColor.RED + "Sua conta foi suspensa temporariamente por "
										+ sender.getName() + "\n§eMotivo: " + reason.toString()
										+ "\n§aTempo restante:§e " + m.ban.getStatsTime(time)
										+ "\n§6Compre seu unban em:  §floja.heavenmc.com.br";
								if (Bukkit.getPlayer(args[0]) != null) {
									kickPlayer(Bukkit.getPlayer(args[0]), kickmsg);
								}
								sender.sendMessage("§a" + args[0] + " foi banido temporariamente por " + m.getBanManager().getStatsTime(time));
							} else {
								sender.sendMessage(ChatColor.GRAY + args[0] + " ja está banido!");
							}
							rs.close();
							stmt.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}.runTaskAsynchronously(Main.getInstance());
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
