package me.caio.bungeecord.commands;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import me.caio.bungeecord.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Unban extends Command {

	private Main m;

	public Unban(Main m) {
		super("unban");
		this.m = m;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(final CommandSender sender, final String[] args) {
		final ProxiedPlayer player = (ProxiedPlayer) sender;
		if (!m.getPermissions().isAdmin(player)) {
			player.sendMessage(TextComponent.fromLegacyText(ChatColor.RED + "Você não possui permissão."));
			return;
		}
		if (args.length >= 1) {

			final ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
			m.getProxy().getScheduler().runAsync(m, new Runnable() {
				@Override
				public void run() {
					UUID uuid = null;
					if (target != null) {
						uuid = target.getUniqueId();
					//	player.sendMessage("target nao foi null, uuid = " + uuid);
					} else {
						try {
							uuid = m.name.getUUID(args[0]);
						//	player.sendMessage("target foi null, uuid = " + uuid);
						} catch (Exception e) {
							player.sendMessage(ChatColor.RED + args[0] + " não existe.");
							return;
						}
					}
					try {
						PreparedStatement stmt = m.mainConnection.prepareStatement(
								"SELECT * FROM heaven_bans WHERE ban_uuid='" + uuid.toString().replace("-", "") + "'");
						ResultSet rs = stmt.executeQuery();
						if (rs.next()) {
							stmt.execute("DELETE FROM heaven_bans WHERE ban_uuid='" + uuid.toString().replace("-", "") + "'");
							
							sender.sendMessage(ChatColor.GREEN + args[0] + " foi desbanido com sucesso.");
							for (ProxiedPlayer staffers : ProxyServer.getInstance().getPlayers()) {
								if (m.getPermissions().isTrial(staffers)) {
									staffers.sendMessage("§7O player " + args[0] + "§7 foi desbanido.");
								}
							}
						} else {
							sender.sendMessage(ChatColor.RED + args[0] + " não está banido!");
						}
						rs.close();
						stmt.close();
					} catch (SQLException e1) {
						System.out.println("Falha ao banir " + target + e1);

					}
				}
			});
		} else {
			sender.sendMessage("§cUse /unban <player>");
		}
	}
}
