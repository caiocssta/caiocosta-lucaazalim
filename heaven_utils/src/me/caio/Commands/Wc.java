package me.skater.Commands;

public class Wc /*implements CommandExecutor */ {
//	private Main main;
/*
	public Wc(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("wc")) {
			if (sender instanceof Player) {
				sender.sendMessage("§cComando em manutenção");
				return true;
				/*
				 * Player p = (Player)sender; if(args.length < 3 || args.length
				 * == 1) { p.sendMessage(ChatColor.RED
				 * +"Use /wc doar <nick> <quantia>"); return true; }
				 * if(args[0].equalsIgnoreCase("doar")) {
				 * 
				 * 
				 * int valor = 0; try { valor =
				 * Integer.parseInt(args[2].replace("-", "")); } catch
				 * (NumberFormatException e) { p.sendMessage(ChatColor.RED
				 * +"Use apenas numeros para transferir."); return true; }
				 * if(valor < 1000) { p.sendMessage(ChatColor.RED
				 * +"Você não pode doar menos que 1,000 WC."); return true; }
				 * PlayerRank prank = main.getRankManager().getPlayerRank(p);
				 * if(valor > prank.getCoins()) { p.sendMessage(ChatColor.RED
				 * +"Você não possui " + valor + " WC."); return true; } Player
				 * target = Bukkit.getPlayer(args[1]); if(target == null) {
				 * p.sendMessage(ChatColor.RED + args[1] + " esta offline.");
				 * return true; } if(target.getName().equals(p.getName())) {
				 * p.sendMessage(ChatColor.RED
				 * +"Você não pode doar para você mesmo."); return true; }
				 * PlayerRank trank =
				 * main.getRankManager().getPlayerRank(target);
				 * trank.addCoinsNoMultiplier(valor); prank.removeCoins(valor);
				 * p.sendMessage(ChatColor.YELLOW + "Você enviou " + valor +
				 * " WC para " + target.getName() + " com sucesso!");
				 * target.sendMessage(ChatColor.YELLOW + p.getName() +
				 * " enviou " + valor + " WC para você!");
				 * p.playSound(p.getLocation(), Sound.LEVEL_UP, 10, 10);
				 * target.playSound(target.getLocation(), Sound.LEVEL_UP, 10,
				 * 10);
				 
			}
		} else {
			if (args[0].equalsIgnoreCase("doar")) {
				main.connect.SQLQuerySync("UPDATE `playerranking` SET `coins`= `coins` + " + args[2] + " WHERE `uuid`='" + args[1].replace("-", "") + "';");
			}
		}
		return false;
	} */
}
