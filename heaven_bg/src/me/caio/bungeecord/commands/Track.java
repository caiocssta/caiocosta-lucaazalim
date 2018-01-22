package me.caio.bungeecord.commands;

import me.caio.bungeecord.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Track extends Command {
	public Main m;

	public Track(Main m) {
		super("track");
		this.m = m;
	}

	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			return;
		}
		ProxiedPlayer player = (ProxiedPlayer) sender;
		if (!m.getPermissions().isTrial(player)) {
			player.sendMessage(TextComponent.fromLegacyText(ChatColor.RED + "Você não possui permissão."));
			return;
		}
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
		if (target == null) {
			player.sendMessage(TextComponent.fromLegacyText(ChatColor.RED + args[0] + " esta offline."));
			return;
		}
		if (target.getServer().getInfo() == null) {
			player.sendMessage(TextComponent
					.fromLegacyText(ChatColor.RED + args[0] + " esta em um servidor indisponível para conexão."));
			return;
		}
		player.sendMessage(TextComponent.fromLegacyText(ChatColor.YELLOW + "Conectando..."));
		player.connect(target.getServer().getInfo());
	}
}
