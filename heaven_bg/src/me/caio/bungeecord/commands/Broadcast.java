package me.caio.bungeecord.commands;

import me.caio.bungeecord.Main;
import me.caio.bungeecord.utils.StringUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Broadcast extends Command {
	public Main m;

	public Broadcast(Main m) {
		super("broadcast");
		this.m = m;
	}

	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			return;
		}
		ProxiedPlayer player = (ProxiedPlayer) sender;
		if (!m.getPermissions().isOwner(player) && !args[0].equals("preview")) {
			player.sendMessage(TextComponent.fromLegacyText(ChatColor.RED + "Você não possui permissão."));
			return;
		}
		StringBuilder str = new StringBuilder();
		for (int i = 1; i < args.length; i++) {
			str.append(args[i]).append(" ");
		}
		if (args[0].equals("add")) {

			m.broadcast.addMessage(ChatColor.translateAlternateColorCodes('&', str.toString()));
			m.broadcast.reloadBroadcast();
			player.sendMessage(ChatColor.GREEN + "Mensagem incluida com sucesso.");
		}

		if (args[0].equals("get")) {
			m.broadcast.getActiveMessages(player);
		}

		if (args[0].equals("force")) {
			m.broadcast.broadcast();
		}
		if (args[0].equals("preview")) {
			if (m.getPermissions().isTrial(player)) {
				player.sendMessage("");
				StringUtils.sendCenteredMessage(player, m.broadcast.tag);
				StringUtils.sendCenteredMessage(player, ChatColor.translateAlternateColorCodes('&', str.toString()));
				player.sendMessage("");
			}
		}

		if (args[0].equals("remove")) {
			m.broadcast.removeMessage(Integer.parseInt(args[1]));
			m.broadcast.reloadBroadcast();
			player.sendMessage(ChatColor.RED + "Mensagem removida com sucesso.");

		}

	}

	public static String message(String[] value, String join) {
		String message = "";
		String[] arrayOfString = value;
		int j = value.length;
		for (int i = 0; i < j; i++) {
			String s = arrayOfString[i];

			message = message + join + s;
		}
		return message.substring(join.length());
	}
}
