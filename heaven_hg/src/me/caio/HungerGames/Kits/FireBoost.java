package me.caio.HungerGames.Kits;

public class FireBoost /* extends KitInterface implements Listener */ { /*
	public FireBoost(Main main) {
		super(main);
	}

	private HashMap<String, Integer> pi = new HashMap<String, Integer>();
	private HashMap<String, Integer> pcount = new HashMap<String, Integer>();

	@EventHandler
	public void onclick(final PlayerToggleSneakEvent e) {
		if (!hasAbility(e.getPlayer())) {
			return;
		}
		if (getMain().stage == GameStage.INVENCIBILITY) {
			return;
		}
		if (!e.getPlayer().isSneaking()) {
			if (!pi.containsKey(e.getPlayer().getName())) {
				if (Cooldown.isInCooldown(e.getPlayer().getUniqueId(), "Booster")) {
					int timeleft = Cooldown.getTimeLeft(e.getPlayer().getUniqueId(), "Booster");
					e.getPlayer().sendMessage("§6Booster em cooldown de §f" + timeleft + "§6 segundos!");
					int task = pi.get(e.getPlayer().getName());
					pi.remove(e.getPlayer().getName());
					pcount.remove(e.getPlayer().getName());
					Bukkit.getScheduler().cancelTask(task);
					return;
				}
				pi.put(e.getPlayer().getName(), new BukkitRunnable() {
					public void run() {
						if ((e.getPlayer().isBlocking()) && (e.getPlayer().isSneaking())) {
							if (Cooldown.isInCooldown(e.getPlayer().getUniqueId(), "Booster")) {
								int timeleft = Cooldown.getTimeLeft(e.getPlayer().getUniqueId(), "Booster");
								e.getPlayer().sendMessage("§6Booster em cooldown de §f" + timeleft + "§6 segundos!");
								int task = pi.get(e.getPlayer().getName());
								pi.remove(e.getPlayer().getName());
								pcount.remove(e.getPlayer().getName());
								Bukkit.getScheduler().cancelTask(task);
								return;
							}
							if (!pcount.containsKey(e.getPlayer().getName())) {
								pcount.put(e.getPlayer().getName(), 0);
							}
							if (pcount.get(e.getPlayer().getName()) != 5) {
								pcount.put(e.getPlayer().getName(), pcount.get(e.getPlayer().getName()) + 1);
								e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ORB_PICKUP, 1, 1);
								e.getPlayer().sendMessage("§aCarregando boost: §c" + (pcount.get(e.getPlayer().getName()) * 20) + "%");
							}
						}

					}
				}.runTaskTimer(this.getMain(), 20, 20).getTaskId());
			}
		} else {
			if (pi.containsKey(e.getPlayer().getName()) && pcount.containsKey(e.getPlayer().getName())) {
				int task = pi.get(e.getPlayer().getName());
				pi.remove(e.getPlayer().getName());
				Bukkit.getScheduler().cancelTask(task);
				Cooldown c = new Cooldown(e.getPlayer().getUniqueId(), "Booster", 30);
				c.start();
				e.getPlayer().setNoDamageTicks(2 * 20);
				e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ENDERDRAGON_WINGS, 10f, 10f);
				for (Entity nearby : e.getPlayer().getNearbyEntities(10.0D, 5.0D, 10.0D)) {
					if ((nearby instanceof Player)) {
						Player t = (Player) nearby;
						if (!Main.getInstance().adm.isSpectating(t)) {
							t.damage(pcount.get(e.getPlayer().getName()) * 2);
							if (((Player) nearby).isSneaking()) {
								nearby.setVelocity(nearby.getLocation().toVector().subtract(e.getPlayer().getLocation().toVector()).multiply(0.1).setY(0.2));
							} else {
								nearby.setVelocity(nearby.getLocation().toVector().subtract(e.getPlayer().getLocation().toVector()).multiply(0.4).setY(0.5));
							}
						}
					}
				}
				if (pcount.containsKey(e.getPlayer().getName())) {
					e.getPlayer().setVelocity(new Vector(0, Double.valueOf(pcount.get(e.getPlayer().getName())) / 0.9, 0));
					pcount.remove(e.getPlayer().getName());
				}
			}
		}
	}

	@EventHandler
	public void onStomper(EntityDamageEvent event) {
		if (getMain().stage != GameStage.GAMETIME) {
			return;
		}
		if (event.isCancelled()) {
			return;
		}
		Entity stomper = event.getEntity();
		if (!(stomper instanceof Player)) {
			return;
		}
		Player stomped = (Player) stomper;
		if (!hasAbility(stomped)) {
			return;
		}
		EntityDamageEvent.DamageCause cause = event.getCause();
		if (cause != EntityDamageEvent.DamageCause.FALL) {
			return;
		}
		double dmg = event.getDamage();
		Location location = stomped.getLocation();
		for (int i = 1; i <= 8; i++) {
			for (int j = 1; j <= 80; j++) {
				float ratio = (float) j / 80;
				double angle = 10 * ratio * 2 * Math.PI / 8 + (2 * Math.PI * i / 8) + 10;
				double x = Math.cos(angle) * ratio * 10;
				double z = Math.sin(angle) * ratio * 10;
				location.add(x, 0, z);
				// ParticleEffect.FLAME.display(location, 0, 0, 0, 0, 5);
				location.subtract(x, 0, z);
			}
		}
		if (dmg > 4.0D) {
			event.setCancelled(true);
			stomped.damage(4.0D);
		}
		if (!Endermage.invencible.contains(stomped.getName()) || (!hasAbility(stomped, "antitower"))) {

			for (Entity nearby : stomped.getNearbyEntities(10.0D, 5.0D, 10.0D)) {
				if ((nearby instanceof Player)) {
					Player t = (Player) nearby;
					if (!Main.getInstance().adm.isSpectating(t)) {
						if (((Player) nearby).isSneaking()) {
							nearby.setVelocity(nearby.getLocation().toVector().subtract(stomped.getLocation().toVector()).multiply(0.1).setY(0.2));
						} else {
							nearby.setVelocity(nearby.getLocation().toVector().subtract(stomped.getPlayer().getLocation().toVector()).multiply(0.4).setY(0.5));
						}
						((Player) nearby).setNoDamageTicks(50);
						nearby.setFireTicks(10 * 20);
					}
				}
			}
		}

	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		return new Kit("FireBoost", Arrays.asList(new String[] { "Segure shift e defenda para", "carregar seu boost e causar um", "estrago em todos a sua volta!" }), kititems, new ItemStack(Material.MAGMA_CREAM));
	} */
}
