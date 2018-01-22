package me.caio.HungerGames.Kits;

public class unTrap /* extends KitInterface */ {
/*	public unTrap(Main main) {
		super(main);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void Kit1(PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		if ((e.getAction() == Action.RIGHT_CLICK_AIR) || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (p.getItemInHand().getType() == Material.TNT) {
				if (hasAbility(p)) {
					e.setCancelled(true);
					p.updateInventory();
					if (Cooldown.isInCooldown(p.getUniqueId(), "Untrap")) {
						int timeleft = Cooldown.getTimeLeft(p.getUniqueId(), "Untrap");
						p.sendMessage("§6Untrap em cooldown de §f" + timeleft + "§6 segundos!");
						return;
					}
					if (!Cooldown.isInCooldown(p.getUniqueId(), "Untrap")) {
						Cooldown c = new Cooldown(p.getUniqueId(), "Untrap", 60);
						c.start();
					}
					final Entity tnt = p.getWorld().spawn(p.getLocation(), TNTPrimed.class);
					new BukkitRunnable() {
						int tempo = 4;

						public void run() {
							tempo--;
							if (tnt.isValid()) {
								p.playEffect(tnt.getLocation(), Effect.MOBSPAWNER_FLAMES, 3);
								tnt.teleport(p.getLocation());
							}
							if (tempo == 1) {
								for (Entity noraio : p.getNearbyEntities(10, 10, 10)) {
									if (noraio instanceof Player) {
										Player t = (Player) noraio;
										if (!Aura.contains(t.getName()))
											Aura.add(t.getName());
									}
								}
								if (!Aura.contains(p.getName()))
									Aura.add(p.getName());
							}
							if (tempo == 0) {
								if (tnt.isValid())
									tnt.remove();
								p.getWorld().createExplosion(p.getLocation(), 4, false);
							}
						}
					}.runTaskTimer(Main.getInstance(), 20, 20);

				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void Por(BlockPlaceEvent e) {
		final Player p = e.getPlayer();
		Block b = e.getBlock();
		if (b.getType() == Material.TNT) {
			if (hasAbility(p)) {
				e.setCancelled(true);

				Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Main.getInstance(), new Runnable() {
					public void run() {
						p.updateInventory();
					}
				}, 20 * 1);
			}
		}
	}

	public static ArrayList<String> Aura = new ArrayList<String>();

	@EventHandler
	public void Dano(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;
		Player p = (Player) e.getEntity();
		if (e.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
			if (Aura.contains(p.getName())) {
				e.setCancelled(true);
				Aura.remove(p.getName());
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();

		kititems.add(createItem(Material.TNT, "§4Untrap"));

		return new Kit("Untrap", Arrays.asList(new String[] { "Crie uma explosao dos blocos e fuja das traps!" }), kititems, new ItemStack(Material.TNT));
	} */
}
