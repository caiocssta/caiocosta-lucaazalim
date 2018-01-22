package me.skater.tags;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagCommand implements CommandExecutor {
	private TagManager tagManager;
	private char linha2 = '\u25AA';
	private String linha;
	private String LinhasJoin;

	public TagCommand(TagManager tag) {
		this.tagManager = tag;
		this.linha2 = '\u25AA';
		this.linha = Character.toString(linha2);
		this.LinhasJoin = ("§8§l" + linha + linha + linha + linha + linha + linha + linha + linha + linha + linha + linha + linha + linha + linha + linha + linha + linha + linha + linha + linha + linha + linha + linha
				+ linha + linha + linha + linha + linha + linha + linha + linha + linha + linha + linha + linha + linha + linha + linha + linha + linha + linha + linha + linha + linha + linha + linha);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		Player p = (Player) sender;
		String message = LinhasJoin + "\n" + "    §aHeavenMC: §eTags" + "\n" + LinhasJoin + "\n" + "§6▪ §7Default" + "\n" + "§6▪ §7Normal";
		if (tagManager.getPlugin().getPermissions().isCopaPlayer(p)) {
			message = message + "\n§6▪ §eCopa";
		}
		if (tagManager.getPlugin().getPermissions().isVip(p)) {
			message = message + "\n§6▪ §aLight";
		}
		if (tagManager.getPlugin().getPermissions().isMvp(p)) {
			message = message + "\n§6▪ §9Mvp";
		}

		if (tagManager.getPlugin().getPermissions().isPro(p)) {
			message = message + "\n§6▪ §6Pro";
		}
		if (tagManager.getPlugin().getPermissions().isBeta(p)) {
			message = message + "\n§d▪ §dBeta";
		}
		if (tagManager.getPlugin().getPermissions().isYouTuber(p)) {
			message = message + "\n§6▪ §bYoutuber";
		}
		if (tagManager.getPlugin().getPermissions().isBuilder(p)) {
			message = message + "\n§6▪ §2Builder";
		}
		if (tagManager.getPlugin().getPermissions().isTrial(p)) {
			message = message + "\n§6▪ §5Trial";
		}
		if (tagManager.getPlugin().getPermissions().isMod(p)) {
			message = message + "\n§6▪ §5§oMod";
		}
		if (tagManager.getPlugin().getPermissions().isAdmin(p)) {
			message = message + "\n§6▪ §cAdmin";
		}
		if (tagManager.getPlugin().getPermissions().isOwner(p)) {
			message = message + "\n§6▪ §4Dono";
		}
		if (label.equalsIgnoreCase("tag")) {
			if (args.length == 1) {
				if (tagManager.getPlayerActiveTag(p) == Tag.SPEC) {
					p.sendMessage(ChatColor.RED + "Você não pode alterar sua tag no modo espectador.");
					return true;
				}
				if (args[0].equalsIgnoreCase("light")) {
					if (tagManager.getPlugin().getPermissions().isVip(p)) {
						this.tagManager.addPlayerTag(p, Tag.VIP);
						p.sendMessage(ChatColor.GRAY + "Tag §aLight§7 aplicada.");
						return true;
					}
				} else if (args[0].equalsIgnoreCase("copa")) {
					if (tagManager.getPlugin().getPermissions().isCopaPlayer(p)) {
						this.tagManager.addPlayerTag(p, Tag.COPA);
						p.sendMessage(ChatColor.GRAY + "Tag §eCopa§7 aplicada.");
						return true;
					}
				} else if (args[0].equalsIgnoreCase("mvp")) {
					if (tagManager.getPlugin().getPermissions().isMvp(p)) {
						this.tagManager.addPlayerTag(p, Tag.MVP);
						p.sendMessage(ChatColor.GRAY + "Tag §9Mvp§7 aplicada.");
						return true;
					}
				} else if (args[0].equalsIgnoreCase("pro")) {
					if (tagManager.getPlugin().getPermissions().isPro(p)) {
						this.tagManager.addPlayerTag(p, Tag.PRO);
						p.sendMessage(ChatColor.GRAY + "Tag §6Pro§7 aplicada.");
						return true;
					}
				} else if (args[0].equalsIgnoreCase("beta")) {
					if (tagManager.getPlugin().getPermissions().isBeta(p)) {
						this.tagManager.addPlayerTag(p, Tag.BETA);
						p.sendMessage(ChatColor.GRAY + "Tag §dBeta§7 aplicada.");
						return true;
					}
				} else if (args[0].equalsIgnoreCase("youtuber")) {
					if (tagManager.getPlugin().getPermissions().isYouTuber(p)) {
						this.tagManager.addPlayerTag(p, Tag.YOUTUBER);
						p.sendMessage(ChatColor.GRAY + "Tag §3Youtuber§7 aplicada.");
						return true;
					}
				} else if (args[0].equalsIgnoreCase("trial")) {
					if (tagManager.getPlugin().getPermissions().isTrial(p)) {
						this.tagManager.addPlayerTag(p, Tag.TRIAL);
						p.sendMessage(ChatColor.GRAY + "Tag §5Trial§7 aplicada.");
						return true;
					}
				} else if (args[0].equalsIgnoreCase("mod")) {
					if (tagManager.getPlugin().getPermissions().isMod(p)) {
						this.tagManager.addPlayerTag(p, Tag.MOD);
						p.sendMessage(ChatColor.GRAY + "Tag §5§oMod§7 aplicada.");
						return true;
					}
				} else if (args[0].equalsIgnoreCase("admin")) {
					if (tagManager.getPlugin().getPermissions().isAdmin(p)) {
						this.tagManager.addPlayerTag(p, Tag.ADMIN);
						p.sendMessage(ChatColor.GRAY + "Tag §cAdmin§7 aplicada.");
						return true;
					}
				} else if (args[0].equalsIgnoreCase("dono")) {
					if (tagManager.getPlugin().getPermissions().isOwner(p)) {
						this.tagManager.addPlayerTag(p, Tag.DONO);
						p.sendMessage(ChatColor.GRAY + "Tag §4Dono§7 aplicada.");
						return true;
					}
				} else {
					if (args[0].equalsIgnoreCase("normal")) {
						this.tagManager.addPlayerTag(p, Tag.NORMAL);
						p.sendMessage(ChatColor.GRAY + "Tag Normal aplicada.");
						return true;
					}
					if (args[0].equalsIgnoreCase("default")) {
						this.tagManager.addPlayerTag(p, tagManager.getDefaultTag(p));
						p.sendMessage(ChatColor.GRAY + "Tag Default aplicada.");
						return true;
					}
				}
			}
			p.sendMessage(message);
			p.sendMessage(LinhasJoin);

			return true;
		}
		return false;

	}

}
