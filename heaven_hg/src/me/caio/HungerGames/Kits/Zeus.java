package me.caio.HungerGames.Kits;

public class Zeus /*extends KitInterface*/ {
	/*public Zeus(Main main) {
		super(main);
	}

	private static List<String> ligou = new ArrayList<String>();

	@EventHandler
	public void Morte(final PlayerDeathEvent e) {
		final Player p = e.getEntity();
		if (hasAbility(p)) {
			for (ListIterator<ItemStack> item = e.getDrops().listIterator(); item.hasNext();) {
				ItemStack i = item.next();
				if ((i.getItemMeta().hasDisplayName()) && (i.getItemMeta().getDisplayName().equals("§aZeus"))) {
					item.remove();
				}
			}
		}
	}

	@EventHandler
	public void drop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		ItemStack i = e.getItemDrop().getItemStack();
		if (hasAbility(p)) {
			if ((i.getItemMeta().hasDisplayName()) && (i.getItemMeta().getDisplayName().equals("§aZeus"))) {
				p.setItemInHand(i);
				e.getItemDrop().remove();
			}
		}
	}

	@EventHandler
	public void Kit3e(ProjectileLaunchEvent e) {
		if (e.getEntity() instanceof Arrow && e.getEntity().getShooter() instanceof Player) {
			final Player p = (Player) e.getEntity().getShooter();
			if (hasAbility(p)) {
				if (!ligou.contains(p.getName()))
					ligou.add(p.getName());
			}
		}
	}

	@EventHandler
	public void Kit3(ProjectileHitEvent e) {
		Projectile p = e.getEntity();
		if (e.getEntity() instanceof Arrow && e.getEntity().getShooter() instanceof Player) {
			Arrow a = (Arrow) p;
			Player pe = (Player) e.getEntity().getShooter();
			if (ligou.contains(pe.getName())) {
				ligou.remove(pe.getName());
				a.getWorld().strikeLightning(a.getLocation());
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();

		kititems.add(createItem(Material.BOW, "§aZeus"));
		kititems.add(new ItemStack(Material.ARROW, 10));

		return new Kit("zeus", Arrays.asList(new String[] { "Lance uma flecha e libere", "um raio furioso do céu" }), kititems, new ItemStack(Material.BOW));
	} */
}
