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

public class Ban extends Command {
	public Main m;

	public Ban(Main m) {
		super("ban");
		this.m = m;
	}

	public void execute(CommandSender sender, final String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			return;
		}
		final ProxiedPlayer player = (ProxiedPlayer) sender;
		if (!m.getPermissions().isTrial(player)) {
			player.sendMessage(TextComponent.fromLegacyText(ChatColor.RED + "Você não possui permissão."));
			return;
		}
		if (args.length >= 2) {
			final StringBuilder reason = new StringBuilder();
			for (int i = 1; i < args.length; i++) {
				reason.append(args[i]).append(" ");
			}

			final ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
			m.getProxy().getScheduler().runAsync(m, new Runnable() {
				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					UUID uuid = null;
					if (target != null) {
						uuid = target.getUniqueId();
					//	player.sendMessage("target nao foi null, uuid = " + uuid);
					} else {
						try {
							uuid = m.name.getUUID(args[0]);
					//		player.sendMessage("target foi null, uuid = " + uuid);
						} catch (Exception e) {
							player.sendMessage(ChatColor.RED + args[0] + " não existe.");
							return;
						}
					}
					try {
						PreparedStatement stmt = m.mainConnection.prepareStatement(
								"SELECT * FROM heaven_bans WHERE ban_uuid='" + uuid.toString().replace("-", "") + "'");
						ResultSet rs = stmt.executeQuery();
						if (!rs.next()) {
							stmt.execute("INSERT INTO heaven_bans VALUES(NULL, '" + uuid.toString().replace("-", "")
									+ "', '" + args[0] + "', '" + player.getName() + "', '" + reason.toString()
									+ "', 'false', '" + System.currentTimeMillis() + "', '0')");
							String kickmsg = ChatColor.RED + "Sua conta foi suspensa permanentemente por "
									+ player.getName() + "\n§aMotivo: " + reason.toString()
									+ "\n§6Compre seu unban em: §floja.heavenmc.com.br";
							if (target != null) {
								target.disconnect(kickmsg);
							}
							player.sendMessage(ChatColor.GREEN + args[0] + " foi banido com sucesso.");
							ProxyServer.getInstance().broadcast("§7" + args[0] + " foi banido permanentemente.");
							for (ProxiedPlayer staffers : ProxyServer.getInstance().getPlayers()) {
								if (m.getPermissions().isTrial(staffers)) {
									staffers.sendMessage("§eO player §b" + args[0] + "§e foi banido!");
									staffers.sendMessage("§eMotivo: §c" + reason.toString());
									staffers.sendMessage("§eStaff: §b" + player.getName());
									staffers.sendMessage("§eServidor: §b" + player.getServer().getInfo().getName()
											.replace(".hg.heavenmc.com.br", ""));
								}
							}
						} else {
							player.sendMessage(ChatColor.RED + args[0] + " ja está banido!");
						}
						rs.close();
						stmt.close();
					} catch (SQLException e1) {
						System.out.println("Falha ao banir " + target + e1);

					}
				}
			});
		}
	}
}
