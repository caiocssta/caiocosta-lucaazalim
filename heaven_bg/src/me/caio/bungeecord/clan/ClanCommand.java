package me.caio.bungeecord.clan;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.caio.bungeecord.Main;
import me.caio.bungeecord.utils.StringUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ClanCommand extends Command {
	public Main m;

	public ClanCommand(Main m) {
		super("clan");
		this.m = m;
	}

	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			return;
		}
		final ProxiedPlayer p = (ProxiedPlayer) sender;
		if (args.length == 0) {
			StringUtils.sendCenteredMessage(p, StringUtils.line);
			StringUtils.sendCenteredMessage(p, "§6HeavenMC§8: §eClans");
			StringUtils.sendCenteredMessage(p, "§aComandos");
			StringUtils.sendCenteredMessage(p, StringUtils.line);
			StringUtils.sendCenteredMessage(p, "§f/clan criar <nome> <tag> <senha>§8: §7Crie seu clan.");
			StringUtils.sendCenteredMessage(p, "§f/clan entrar <nome> <senha>§8: §7Entre para um clan.");
			StringUtils.sendCenteredMessage(p, "§f/clan sair§8: §7Saia do seu clan.");
			StringUtils.sendCenteredMessage(p, "§f/clan promover <nick>§8: §7Promova um membro.");
			StringUtils.sendCenteredMessage(p, "§f/clan rebaixar <nick>§8: §7Rebaixe um membro.");
			StringUtils.sendCenteredMessage(p, "§f/clan senha <senha>§8: §7Troque a senha do seu clan.");
			StringUtils.sendCenteredMessage(p, "§f/clan status§8: §7Estatisticas do seu clan.");
			StringUtils.sendCenteredMessage(p, "§f/clan status <nome>§8: §7Estatisticas de outro clan.");
			StringUtils.sendCenteredMessage(p, "§f/clan kick <nome>§8: §7Expulsar um membro");
			StringUtils.sendCenteredMessage(p, "§f/clan chat§8: §7Chat do clan");
			StringUtils.sendCenteredMessage(p, StringUtils.line);
			return;
		}

		if (args[0].equalsIgnoreCase("criar")) {
			if (args.length < 3 || args.length == 1) {
				p.sendMessage("§cArgumento invalido!");
				p.sendMessage("§cUse: /clan criar <nome> <tag> <senha>");
				return;
			}
			final String nome = args[1];
			final String tag = args[2];
			if (!validateName(nome)) {
				p.sendMessage(ChatColor.RED + "Nome invalido.");
				return;
			}
			if (nome.length() > 16) {
				p.sendMessage(ChatColor.RED + "Nome muito longo, o máximo deve ser 16 carácteres.");
				return;
			}

			if (tag.length() > 4) {
				p.sendMessage(ChatColor.RED + "A tag deve conter 4 dígitos.");
				return;
			}
			m.getProxy().getScheduler().runAsync(m, new Runnable() {
				@Override
				public void run() {
					try {
						m.getClanManager().createClan(p, nome, tag);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			});
		}
		return;
	}

	public static boolean validateName(String username) {
		Pattern pattern = Pattern.compile("[a-zA-Z0-9_]{1,16}");
		Matcher matcher = pattern.matcher(username);
		return matcher.matches();
	}

}
