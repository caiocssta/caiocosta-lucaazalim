package me.skater.clans;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.skater.Utils.StringUtils;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ClanCommand implements CommandExecutor {

	private ClanManager manager;

	public ClanCommand(ClanManager manager) {
		this.manager = manager;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		final Player p = (Player) sender;
		if (args.length == 0) {
			p.sendMessage(StringUtils.line);
			p.sendMessage("              §6HeavenMC§8: §eClans");
			p.sendMessage(StringUtils.line);
			p.sendMessage("§f/clan criar <nome> <tag>§8: §7Crie seu clan.");
			p.sendMessage("§f/clan entrar <nome> §8: §7Entre para um clan");
			p.sendMessage("§f/clan invite <jogador>§8: §7Convide alguem para seu clan.");
			p.sendMessage("§f/clan sair§8: §7Saia do seu clan.");
			p.sendMessage("§f/clan excluir§8: §7Excluia o seu clan.");
			p.sendMessage("§f/clan promover <nick>§8: §7Promova um membro.");
			p.sendMessage("§f/clan rebaixar <nick>§8: §7Rebaixe um membro.");
			p.sendMessage("§f/clan status§8: §7Estatisticas do seu clan.");
			p.sendMessage("§f/clan status <tag>§8: §7Estatisticas de outro clan.");
			p.sendMessage("§f/clan kick <nome>§8: §7Expulsar um membro");
			p.sendMessage("§f/clan chat§8: §7Chat do clan");
			p.sendMessage("§f/clan rank§8: §7Veja todos os ranks");
			p.sendMessage(StringUtils.line);
			return false;
		}

		if (args[0].equalsIgnoreCase("rank")) {
			if (args.length < 1 || args.length == 0) {
				p.sendMessage("§cUse: /clan rank");
				return false;
			}

			p.sendMessage("§f§m------------------");
			p.sendMessage("§6§lRanks disponiveis:");
			p.sendMessage("§7- §8§lBronze §7Elo (§a▲0§7)");
			p.sendMessage("§7- §7§lPrata §7Elo (§a▲500§7)");
			p.sendMessage("§7- §e§lOuro §7Elo (§a▲1000§7)");
			p.sendMessage("§7- §f§lPlatina §7Elo (§a▲3000§7)");
			p.sendMessage("§7- §b§lDiamante §7Elo (§a▲8000§7)");
			p.sendMessage("§7- §4§lHeaven §7Elo (§a▲17000§7)");
			p.sendMessage("§f§m------------------");
			return true;

		}

		if (args[0].equalsIgnoreCase("chat")) {
			if (args.length < 1 || args.length == 0) {
				p.sendMessage("§cUse: /clan chat");
				return false;
			}

			if (manager.isOnClanChat(p)) {
				manager.removeFromClanChat(p);
			} else {
				manager.setClanChat(p);
			}
		}

		if (args[0].equalsIgnoreCase("entrar")) {
			if (args.length < 2 || args.length == 1) {
				p.sendMessage("§cUse: /clan entrar <nome>");
				return false;
			}

			new BukkitRunnable() {
				@Override
				public void run() {
					manager.joinClan(p, args[1]);

				}
			}.runTaskAsynchronously(manager.getPlugin());
		}

		if (args[0].equalsIgnoreCase("status")) {
			if (args.length < 1 || args.length > 2) {
				p.sendMessage(ChatColor.RED + "Use /clan status ou /clan status <nome>");
				return false;
			}
			if (args.length == 1) {
				new BukkitRunnable() {
					@Override
					public void run() {
						manager.ClanStatus(p);
					}
				}.runTaskAsynchronously(manager.getPlugin());
				return false;
			}
			if (args.length == 2) {
				new BukkitRunnable() {
					@Override
					public void run() {
						manager.ClanStatus(p, args[1]);
					}
				}.runTaskAsynchronously(manager.getPlugin());
				return false;
			}

		}

		if (args[0].equalsIgnoreCase("sair")) {
			if (args.length < 1 || args.length == 0) {
				p.sendMessage("§cUse: /clan sair");
				return false;
			}

			new BukkitRunnable() {
				@Override
				public void run() {
					manager.leaveClan(p);
					;

				}
			}.runTaskAsynchronously(manager.getPlugin());

		}

		if (args[0].equalsIgnoreCase("excluir")) {
			if (args.length < 1 || args.length == 0) {
				p.sendMessage("§cUse: /clan excluir");
				return false;
			}

			new BukkitRunnable() {
				@Override
				public void run() {
					manager.disbandClan(p);
					;

				}
			}.runTaskAsynchronously(manager.getPlugin());

		}

		if (args[0].equalsIgnoreCase("promover")) {
			if (args.length < 2 || args.length == 1) {
				p.sendMessage("§cUse: /clan promover <player>");
				return false;
			}
			final Player target = Bukkit.getPlayer(args[1]);
			if (target == null) {
				p.sendMessage(ChatColor.RED + "O jogador tem que estar online.");
				return false;
			}
			new BukkitRunnable() {

				@Override
				public void run() {
					manager.promotePlayer(p, target);

				}
			}.runTaskAsynchronously(manager.getPlugin());
		}

		if (args[0].equalsIgnoreCase("rebaixar")) {
			if (args.length < 2 || args.length == 1) {
				p.sendMessage("§cUse: /clan rebaixar <player>");
				return false;
			}
			final Player target = Bukkit.getPlayer(args[1]);
			if (target == null) {
				p.sendMessage(ChatColor.RED + "O jogador tem que estar online.");
				return false;
			}
			new BukkitRunnable() {

				@Override
				public void run() {
					manager.demotePlayer(p, target);

				}
			}.runTaskAsynchronously(manager.getPlugin());
		}

		if (args[0].equalsIgnoreCase("kick")) {
			if (args.length < 2 || args.length == 1) {
				p.sendMessage("§cUse: /clan kick <player>");
				return false;
			}
			final Player target = Bukkit.getPlayer(args[1]);
			if (target == null) {
				p.sendMessage(ChatColor.RED + "O jogador tem que estar online.");
				return false;
			}
			new BukkitRunnable() {
				@Override
				public void run() {
					manager.kickPlayer(p, target);

				}
			}.runTaskAsynchronously(manager.getPlugin());
		}

		if (args[0].equalsIgnoreCase("invite")) {
			if (args.length < 2 || args.length == 1) {
				p.sendMessage("§cUse: /clan invite <player>");
				return false;
			}
			final Player target = Bukkit.getPlayer(args[1]);
			if (target == null) {
				p.sendMessage(ChatColor.RED + "O jogador tem que estar online.");
				return false;
			}
			new BukkitRunnable() {
				@Override
				public void run() {

					manager.sendInvite(p, target);
				}
			}.runTaskAsynchronously(manager.getPlugin());
		}
		if (args[0].equalsIgnoreCase("criar")) {
			if (args.length < 3 || args.length == 1) {
				p.sendMessage("§cUse: /clan criar <nome> <tag>");
				return false;
			}
			final String nome = args[1];
			final String tag = args[2];
			if (!validateName(nome)) {
				p.sendMessage(ChatColor.RED + "O nome não deve conter simbolos e no maximo 16 letras.");
				return false;
			}
			if (!validateTag(tag)) {
				p.sendMessage(ChatColor.RED + "A tag deve conter 4 digitos e nenhum simbolo.");
				return false;
			}
			new BukkitRunnable() {
				@Override
				public void run() {
					try {
						manager.createClan(p, nome, tag);
					} catch (SQLException e) {
						p.sendMessage(ChatColor.RED + "Falha ao criar seu clan");
					}
				}
			}.runTaskAsynchronously(manager.getPlugin());
		}
		return false;
	}

	public static boolean validateName(String name) {
		Pattern pattern = Pattern.compile("[a-zA-Z0-9_]{1,16}");
		Matcher matcher = pattern.matcher(name);
		return matcher.matches();
	}

	public static boolean validateTag(String tag) {
		Pattern pattern = Pattern.compile("[a-zA-Z0-9_]{4}");
		Matcher matcher = pattern.matcher(tag);
		return matcher.matches();
	}

}
