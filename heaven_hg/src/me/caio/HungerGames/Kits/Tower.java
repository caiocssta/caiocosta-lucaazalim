package me.caio.HungerGames.Kits;

public class Tower {/* extends KitInterface {
	public Tower(Main main) {
		super(main);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDamage(BlockDamageEvent e) {
		Player p = e.getPlayer();
		if (hasAbility(p)) {
			if (this.getMain().stage == GameStage.PREGAME) {
				return;
			}
			if (e.getBlock().getType() == Material.DIRT) {
				boolean drop = true;
				EntityPlayer p2 = ((CraftPlayer) p).getHandle();
				if (p2.getHealth() < p2.getMaxHealth()) {
					double hp = p2.getHealth() + 1;
					if (hp > p2.getMaxHealth())
						hp = p2.getMaxHealth();
					p.setHealth((int) hp);
					drop = false;
				} else if (p.getFoodLevel() < 20) {
					p.setFoodLevel(p.getFoodLevel() + 1);
					drop = false;
				} else if (p2.getHealth() > 19) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 2, 1));
					drop = false;
				}
				drop = true;
				e.getBlock().getWorld().playEffect(e.getBlock().getLocation(), Effect.STEP_SOUND, Material.DIRT.getId());
				e.getBlock().setType(Material.AIR);
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5 * 20, 1));

				if (drop)
					e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation().add(0.5, 0, 0.5), new ItemStack(Material.DIRT));
			}

		}
	}

	@EventHandler
	public void Stompar(EntityDamageEvent e) {
		Entity ent = e.getEntity();
		if ((ent instanceof Player)) {
			final Player p = (Player) ent;
			if ((e.getCause() == EntityDamageEvent.DamageCause.FALL) && hasAbility(p)) {

				if (Launcher.nofalldamage.contains(p.getName())) {
					e.setCancelled(true);
					Launcher.nofalldamage.remove(p.getName());
					return;
				}
				if (Main.getInstance().stage != GameStage.GAMETIME) {
					return;
				}
				e.setCancelled(true);
				p.damage(4);

				if (Endermage.invencible.contains(p.getName())) {
					return;
				}
				for (Entity nearby : p.getNearbyEntities(2.0D, 5.0D, 2.0D)) {
					if ((nearby instanceof Player)) {
						Player t = (Player) nearby;
						if (!Main.getInstance().adm.isSpectating(t)) {
							if (hasAbility(t, "antitower")) {
								return;
							}
							if (t.isSneaking()) {
								if (e.getDamage() <= 10.0D) {
									t.damage(e.getDamage() / 2.0D, p);
								} else {
									t.damage(10.0D, p);
								}
							} else {
								t.damage(e.getDamage() + 3, p);

							}
						}
					}
				}
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();

		return new Kit("Tower", Arrays.asList(new String[] { "Inicie o jogo com dois kits: Stomper e Worm" }), kititems, new ItemStack(Material.DIAMOND_BOOTS));
	} */
}
