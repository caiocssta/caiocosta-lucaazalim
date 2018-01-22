package me.caio.HungerGames.Kits;

public class Change { /* extends KitInterface {
	public Change(Main main) {
		super(main);
	}

	public static HashMap<String, ItemStack[]> armadura = new HashMap<String, ItemStack[]>();
	public ArrayList<String> Bolas = new ArrayList<String>();

	@EventHandler
	public void Bolas(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			final Player p = (Player) e.getEntity();
			Entity snowball = e.getDamager();
			if ((snowball instanceof Egg)) {
				final Player hit = (Player) ((Egg) snowball).getShooter();
				if (hasAbility(hit) && (Cooldown.isInCooldown(hit.getUniqueId(), "Change"))) {
					if (hasAbility(p, "strong")) {
						hit.sendMessage(ChatColor.RED + "O kit change não funciona contra o kit Strong!");
						return;
					}
					if (Bolas.contains(hit.getName())) {
						Bolas.remove(hit.getName());
						p.sendMessage("§c" + hit.getName() + " atirou em você com sua gun change e trocou sua armadura!");
						p.sendMessage("§c15 segundos para voltar ao normal...");
						armadura.put(p.getName(), p.getInventory().getArmorContents());
						final ItemStack[] itemp1 = p.getInventory().getArmorContents();
						final ItemStack[] itemp2 = hit.getInventory().getArmorContents();
						p.getInventory().setArmorContents(itemp2);
						hit.getInventory().setArmorContents(itemp1);
						new BukkitRunnable() {
							int segundos = 15;

							public void run() {
								segundos--;
								if (segundos <= 0) {
									if (p.isOnline()) {
										p.getInventory().setArmorContents((ItemStack[]) armadura.get(p.getName()));
										p.updateInventory();
										p.getInventory().setArmorContents(itemp1);
										hit.getInventory().setArmorContents(itemp2);
										armadura.remove(p.getName());
										cancel();
									}
								}
							}
						}.runTaskTimer(Main.getInstance(), 0, 20);
					}
				}
			}
		}
	}

	@EventHandler
	public void teste(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (armadura.containsKey(p.getName())) {
			p.getInventory().setArmorContents((ItemStack[]) armadura.get(p.getName()));
			p.updateInventory();
			armadura.remove(p.getName());
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (armadura.containsKey(e.getWhoClicked().getName())) {
			if (!(e.getWhoClicked() instanceof Player)) {
				return;
			}
			if (e.getCurrentItem() == null) {
				e.setCancelled(true);
				return;
			}
			if (e.getCurrentItem().getType().name().contains("CHAINMAIL_"))
				e.setCancelled(true);
		}
	}

	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		if ((event.getEntity() instanceof Egg)) {
			Egg snowball = (Egg) event.getEntity();
			Player p = (Player) snowball.getShooter();
			if ((p.getItemInHand().getType().equals(Material.EGG)) && (p.getItemInHand().hasItemMeta()) && (p.getItemInHand().getItemMeta().hasDisplayName())
					&& (p.getItemInHand().getItemMeta().getDisplayName().equals("§eChange"))) {
				if (isInvencibility()) {
					p.sendMessage(ChatColor.RED + "Você não pode usar na invencibilidade.");
					return;
				}
				if (!Bolas.contains(p.getName()))
					Bolas.add(p.getName());
				if (hasAbility(p)) {
					if (!Cooldown.isInCooldown(p.getUniqueId(), "Change")) {
						event.setCancelled(true);
						ItemStack Item = createItem(Material.EGG, 1, "§eChange");
						p.getInventory().addItem(Item);
						Cooldown c = new Cooldown(p.getUniqueId(), "Change", 40);
						c.start();
					} else {
						int timeleft = Cooldown.getTimeLeft(p.getUniqueId(), "Change");
						p.sendMessage("§6Change em cooldown de §f" + timeleft + "§6 segundos!");
						return;
					}
				}
			}
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		kititems.add(createItem(Material.EGG, 2, "§eChange"));

		return new Kit("Change", Arrays.asList(new String[] { "Acerte seu ovo em um inimigo", "para trocar sua armadura por 15 segundos" }), kititems, new ItemStack(Material.EGG));
	} */
}
