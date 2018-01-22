
package me.skater.Commands;

public class Unban {/* implements CommandExecutor {

	private Main m;

	public Unban(Main m) {
		this.m = m;
	}

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("unban")) {
			if (m.getPermissions().isAdmin(sender)) {
				if (args.length == 1) {
					new BukkitRunnable() {
						@Override
						public void run() {
							try {
								PreparedStatement stmt = m.getSQL().prepareStatement("SELECT * FROM heaven_bans WHERE ban_name='" + args[0] + "'");
								ResultSet result = stmt.executeQuery();
								if (result.next()) {
									stmt.execute("DELETE FROM heaven_bans WHERE ban_name='" + args[0] + "'");
								} else {
									sender.sendMessage(ChatColor.RED + args[0] + " nao esta banido!");
								}
								result.close();
								stmt.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					}.runTaskAsynchronously(m);
				} else {
					sender.sendMessage(ChatColor.RED + "Use: /unban <nick>");
				}
			}
		}
		return false;
	} */
}
