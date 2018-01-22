package me.caio.HungerGames.Kits;

public class Wizard /* extends KitInterface */ {
/*	public Wizard(Main main) {
		super(main);
	}

	ArrayList<String> Ligado = new ArrayList<String>();

	@EventHandler
	public void Om(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if ((e.getAction() == Action.RIGHT_CLICK_AIR) || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (p.getItemInHand().getType() == Material.CAULDRON_ITEM) {
				if (hasAbility(p)) {
					e.setCancelled(true);
					p.updateInventory();
					if (Cooldown.isInCooldown(p.getUniqueId(), "Wizard")) {
						int timeleft = Cooldown.getTimeLeft(p.getUniqueId(), "Wizard");
						p.sendMessage("§6Wizard em cooldown de §f" + timeleft + "§6 segundos!");
						return;
					}
					if (!Cooldown.isInCooldown(p.getUniqueId(), "Wizard")) {
						Cooldown c = new Cooldown(p.getUniqueId(), "Wizard", 60);
						c.start();
					}
					if (!Ligado.contains(p.getName()))
						Ligado.add(p.getName());
					Bruxa(p);
					p.sendMessage("§aFeitiço realizado! porçoes negativas sem efeitos por 30 segundos..");
					p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
				}
			}
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getEntity();
		if (Ligado.contains(p.getName())) {
			if ((e.getCause() == EntityDamageEvent.DamageCause.WITHER) || (e.getCause() == EntityDamageEvent.DamageCause.MELTING) || (e.getCause() == EntityDamageEvent.DamageCause.MAGIC)
					|| (e.getCause() == EntityDamageEvent.DamageCause.POISON) || (e.getCause() == EntityDamageEvent.DamageCause.WITHER)) {
				e.setCancelled(true);
				return;
			}
		}
	}

	BukkitRunnable task;

	public void Bruxa(final Player p) {
		final Entity witch = p.getWorld().spawnEntity(p.getLocation(), EntityType.WITCH);
		((Damageable) witch).setMaxHealth(500);
		((Damageable) witch).setHealth(500);
		Virar(p, witch);
		task = new BukkitRunnable() {
			int tempo = 30;

			public void run() {
				tempo--;
				if (tempo > 0) {
					if (p.hasPotionEffect(PotionEffectType.BLINDNESS))
						p.removePotionEffect(PotionEffectType.BLINDNESS);
					if (p.hasPotionEffect(PotionEffectType.CONFUSION))
						p.removePotionEffect(PotionEffectType.CONFUSION);
					if (p.hasPotionEffect(PotionEffectType.HUNGER))
						p.removePotionEffect(PotionEffectType.HUNGER);
					if (p.hasPotionEffect(PotionEffectType.POISON))
						p.removePotionEffect(PotionEffectType.POISON);
					if (p.hasPotionEffect(PotionEffectType.SLOW))
						p.removePotionEffect(PotionEffectType.SLOW);
					if (p.hasPotionEffect(PotionEffectType.SLOW_DIGGING))
						p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
					if (p.hasPotionEffect(PotionEffectType.WEAKNESS))
						p.removePotionEffect(PotionEffectType.WEAKNESS);
					if (p.hasPotionEffect(PotionEffectType.WITHER))
						p.removePotionEffect(PotionEffectType.WITHER);
					if (Ligado.contains(p.getName())) {
						if (!p.isOnline()) {
							cancel();
							witch.remove();
							task.cancel();
						}
					}
				} else {
					if (Ligado.contains(p.getName())) {
						if (p.isOnline()) {
							Ligado.remove(p.getName());
							cancel();
							witch.remove();
							task.cancel();
						} else {
							cancel();
							Ligado.remove(p.getName());
							witch.remove();
							task.cancel();
						}
					}
				}
			}
		};
		task.runTaskTimer(Main.getInstance(), 0, 20);
	}

	public Kit getKit() {

		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();

		kititems.add(createItem(Material.CAULDRON_ITEM, ChatColor.DARK_PURPLE + "Magia", "Use para spawnar uma bruxa que vai te ajudar", "nas lutas,e voce ganhara 30 segundos", "sem efeitos negativos de porçoes!"));

		return new Kit("wizard", Arrays.asList(new String[] { "Use para spawnar uma bruxa que vai te ajudar", "nas lutas,e voce ganhara 30 segundos", "sem efeitos negativos de porçoes!" }), kititems,
				new ItemStack(Material.CAULDRON_ITEM));
	}

	private void Virar(final Player player, final Entity pet, final double speed) {
		new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			public void run() {
				if ((!pet.isValid() || (!player.isOnline()))) {
					this.cancel();
				}
				net.minecraft.server.v1_7_R4.Entity pett = ((CraftEntity) pet).getHandle();
				((EntityInsentient) pett).getNavigation().a(2);
				Object petf = ((CraftEntity) pet).getHandle();
				Location targetLocation = player.getLocation();
				PathEntity path;
				path = ((EntityInsentient) petf).getNavigation().a(targetLocation.getX() + 1, targetLocation.getY(), targetLocation.getZ() + 1);
				if (path != null) {
					((EntityInsentient) petf).getNavigation().a(path, 1.0D);
					((EntityInsentient) petf).getNavigation().a(2.0D);
				}
				int distance = (int) Bukkit.getPlayer(player.getName()).getLocation().distance(pet.getLocation());
				if (distance > 10 && !pet.isDead() && player.isOnGround()) {
					pet.teleport(player.getLocation());
				}
				AttributeInstance attributes = ((EntityInsentient) ((CraftEntity) pet).getHandle()).getAttributeInstance(GenericAttributes.d);
				attributes.setValue(speed);
			}
		}.runTaskTimer(Main.getInstance(), 0L, 20L);
	}

	public void Virar(Player p, Entity pet) {
		Virar(p, pet, 0.3);
	} */
}
