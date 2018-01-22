package me.caio.HungerGames.Kits;

public class Freeze /* extends KitInterface */ {
/*	public Freeze(Main main) {
		super(main);
	}

	ArrayList<String> apanhou = new ArrayList<String>();
	ArrayList<String> bateu = new ArrayList<String>();

	@EventHandler
	public void inv(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (apanhou.contains(p.getName())) {
			e.setCancelled(true);
			p.updateInventory();
			p.sendMessage("§cVocê não pode mexer no inventario por 5 segundos!");
		}
	}

	@EventHandler
	public void Dano(EntityDamageByEntityEvent e) {
		if (((e.getEntity() instanceof Player)) && ((e.getDamager() instanceof Player))) {
			final Player p = (Player) e.getEntity();
			final Player d = (Player) e.getDamager();
			if (hasAbility(d)) {
				if (isInvencibility()) {
					p.sendMessage(ChatColor.RED + "Você não pode usar na invencibilidade.");
					return;
				}
				Random rand = new Random();
				if (rand.nextInt(90) + 10 < 50) {
					if (!bateu.contains(d.getName())) {
						p.sendMessage("§cSeu inventario foi congelado por " + d.getName());
						d.sendMessage("§eVocê congelou o inventario de " + p.getName());
						d.playSound(d.getLocation(), Sound.ANVIL_BREAK, 1, 1);

						if (!apanhou.contains(p.getName()))
							apanhou.add(p.getName());

						bateu.add(d.getName());
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
							public void run() {
								if (bateu.contains(d.getName()))
									bateu.remove(d.getName());
							}
						}, 20 * 90);

						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
							public void run() {
								if (apanhou.contains(p.getName())) {
									apanhou.remove(p.getName());
									p.sendMessage("§adescongelado!");
								}
							}
						}, 20 * 7);
					}
				}
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		return new Kit("Freeze", Arrays.asList(new String[] { "Ao você bater em um jogador você tem 50%", "para congela o refil dele por 7 segundos com", "cooldown de 1 minuto e meio depois de congelar." }), kititems,
				new ItemStack(Material.ICE));
	} */
}
