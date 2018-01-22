package me.caio.bungeecord.commands;

import me.caio.bungeecord.Main;
import me.caio.bungeecord.utils.StringUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Aviso extends Command {
	public Main m;

	public Aviso(Main m) {
		super("aviso");
		this.m = m;
	}

	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			return;
		}
		ProxiedPlayer player = (ProxiedPlayer) sender;
		if (!m.getPermissions().isAdmin(player)) {
			player.sendMessage(TextComponent.fromLegacyText(ChatColor.RED + "Você não possui permissão."));
			return;
		}

		StringBuilder str = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			str.append(args[i]).append(" ");
		}
		for (ProxiedPlayer p : m.getProxy().getPlayers()) {
			p.sendMessage("");
			StringUtils.sendCenteredMessage(p, "§c§lAVISO");
			StringUtils.sendCenteredMessage(p, ChatColor.translateAlternateColorCodes('&', str.toString()));
			p.sendMessage("");

		}

	}
}
