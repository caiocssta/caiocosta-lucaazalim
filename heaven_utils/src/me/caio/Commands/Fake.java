package me.skater.Commands;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.skater.Main;
import me.skater.Utils.FakePlayerUtils;
import me.skater.permissions.enums.Group;
import me.skater.tags.Tag;

public class Fake implements CommandExecutor {

	private Main m;

	public Fake(Main m) {
		this.m = m;
	}

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("fake")) {
			if ((sender instanceof Player)) {
				final Player p = (Player) sender;
				if (!m.getPermissionManager().hasGroupPermission(p, Group.YOUTUBER) && !m.getPermissionManager().hasGroupPermission(p, Group.BETA)) {
					p.sendMessage(ChatColor.RED + "Você não possui permissão.");
					return true;
				}
				if (args.length == 1) {
					try {
						if (FakePlayerUtils.isBanned(p.getUniqueId())) {
							p.sendMessage(ChatColor.RED + "Você perdeu acesso permanentemente a esta função pelo indevido uso.");
							return false;
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (args.length == 0) {
					if (FakePlayerUtils.isFake(p)) {
						new BukkitRunnable() {
							@Override
							public void run() {
								m.getTagManager().removePlayerTag(p);
								FakePlayerUtils.removePlayerSkin(p, false);
								FakePlayerUtils.changePlayerName(p, FakePlayerUtils.getOriginalName(p), true);
								m.getTagManager().addPlayerTag(p, m.getTagManager().getDefaultTag(p));
								FakePlayerUtils.removePlayer(p);
								p.sendMessage(ChatColor.YELLOW + "Você voltou ao seu nick original.");
							}
						}.runTask(m);
					}
					return false;
				}
				if (args[0].equalsIgnoreCase("ban")) {
					new BukkitRunnable() {
						@Override
						public void run() {
							UUID uuid = null;
							if (Bukkit.getPlayer(args[1]) != null) {
								uuid = Bukkit.getPlayer(args[1]).getUniqueId();
							} else {
								try {
									uuid = m.name.makeUUID(m.name.getUUID(args[1]));
								} catch (Exception e) {
									p.sendMessage(ChatColor.RED + "O player " + args[1] + " não existe.");
									return;
								}
							}
							try {
								FakePlayerUtils.banPlayer(p, uuid);
							} catch (SQLException e) {
								e.printStackTrace();
							}

						}
					}.runTaskAsynchronously(m);
					return false;

				}
				if (args[0].equalsIgnoreCase("check")) {
					if (!m.getPermissionManager().hasGroupPermission(p, Group.TRIAL)) {
						p.sendMessage(ChatColor.RED + "Você não possui permissão para esse comando.");
						return true;
					}
					if (FakePlayerUtils.fakenames.isEmpty()) {
						p.sendMessage("§cNão há nenhum fake no momento!");
						return true;
					}
					p.sendMessage("§cLista de todos os fakes: ");
					for (UUID u : FakePlayerUtils.fakenames.keySet()) {
						String nome = FakePlayerUtils.originalnames.get(u);
						String fake = FakePlayerUtils.fakenames.get(u);
						p.sendMessage("§7Nick: §b" + nome + " §7Fake: §c" + fake);
					}
					return false;
				}

				if (args.length != 1) {
					p.sendMessage(ChatColor.RED + "Use /fake <nick> para trocar de nick");
					p.sendMessage(ChatColor.RED + "Use /fake para voltar ao seu nick original");
					return true;
				}

				final String playerName = args[0];
				if (!FakePlayerUtils.validateName(playerName)) {
					p.sendMessage(ChatColor.RED + "Nick invalido.");
					return true;
				}
				if (!FakePlayerUtils.isFake(playerName)) {
					p.sendMessage(ChatColor.RED + "Nick invalido.");
					return true;
				}
				new BukkitRunnable() {
					@Override
					public void run() {
						if (m.name.getUUID(playerName) != null) {
							if (!m.getPermissionManager().hasGroupPermission(p, Group.ADMIN)) {
								p.sendMessage(ChatColor.RED + "Você não pode usar o nick de alguem que ja existe.");
								return;
							}
						}
						m.getTagManager().removePlayerTag(p);
						FakePlayerUtils.removePlayerSkin(p, false);
						FakePlayerUtils.changePlayerName(p, playerName, true);
						m.getTagManager().addPlayerTag(p, Tag.NORMAL);
						p.sendMessage(ChatColor.YELLOW + "Você mudou seu nick para: " + ChatColor.AQUA + playerName);
					//	p.sendMessage(ChatColor.RED + "Use com modera��o, qualquer abuso resultara em perda permanente dessa fun��o!");
					}
				}.runTaskAsynchronously(m);

			}

		}
		return false;
	}

}
