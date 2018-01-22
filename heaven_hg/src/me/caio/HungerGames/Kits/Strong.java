package me.caio.HungerGames.Kits;

public class Strong /* extends KitInterface */ {
/*	public Strong(Main main) {
		super(main);
	}

	public static HashMap<String, Integer> Usou = new HashMap<String, Integer>();
	ItemStack Item = createItem(Material.GHAST_TEAR, ChatColor.LIGHT_PURPLE + "Strong Kit");
	public static HashMap<String, ItemStack[]> armadura = new HashMap<String, ItemStack[]>();

	@EventHandler
	public void Morte(final PlayerDeathEvent e) {
		final Player p = e.getEntity();
		if (hasAbility(p)) {
			for (ListIterator<ItemStack> item = e.getDrops().listIterator(); item.hasNext();) {
				ItemStack i = item.next();
				if ((i.getType() == Material.IRON_HELMET) || (i.getType() == Material.IRON_CHESTPLATE) || (i.getType() == Material.IRON_LEGGINGS) || (i.getType() == Material.IRON_BOOTS)) {
					item.remove();
				}
				if ((i.getItemMeta().hasDisplayName()) && (i.getItemMeta().getDisplayName().equals(ChatColor.LIGHT_PURPLE + "Strong Kit"))) {
					item.remove();
				}
			}
		}
	}

	@EventHandler
	public void Item(PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		if ((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			if (p.getItemInHand().getType() == Material.GHAST_TEAR) {
				if (hasAbility(p)) {
					if (Cooldown.isInCooldown(p.getUniqueId(), "Strong")) {
						int timeleft = Cooldown.getTimeLeft(p.getUniqueId(), "Strong");
						p.sendMessage("§6Strong em cooldown de §f" + timeleft + "§6 segundos!");
						return;
					}
					if (Usou.get(p.getName()) == null)
						Usou.put(p.getName(), Integer.valueOf(0));
					if (!Cooldown.isInCooldown(p.getUniqueId(), "Strong")) {
						Cooldown c = new Cooldown(p.getUniqueId(), "Strong", 60);
						c.start();
					}
					armadura.put(p.getName(), p.getInventory().getArmorContents());
					p.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
					p.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
					p.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
					p.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
					Usou.put(p.getName(), Usou.get(p.getName()) + 1);
					p.updateInventory();
					p.playSound(p.getLocation(), Sound.ITEM_BREAK, 1, 1);
					p.sendMessage("§aVocê tem 15 segundos de armadura!");
					if (Usou.get(p.getName()) >= 3) {
						p.getItemInHand().setType(Material.AIR);
						p.getInventory().removeItem(Item);
						p.updateInventory();
						p.sendMessage("§cVocê nao pode usar mais!!");
					}
					new BukkitRunnable() {
						int segundos = 15;

						public void run() {
							segundos--;
							if (segundos <= 0) {
								if (p.isOnline()) {
									p.getInventory().setArmorContents((ItemStack[]) armadura.get(p.getName()));
									p.updateInventory();
									armadura.remove(p.getName());
									p.sendMessage("§cVocê perdeu sua armadura!");
									cancel();
								}
							}
						}
					}.runTaskTimer(Main.getInstance(), 0, 20);
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
			if (!hasAbility((Player) e.getWhoClicked())) {
				return;
			}
			if ((e.getCurrentItem().getType() == Material.IRON_HELMET) || (e.getCurrentItem().getType() == Material.IRON_CHESTPLATE) || (e.getCurrentItem().getType() == Material.IRON_LEGGINGS)
					|| (e.getCurrentItem().getType() == Material.IRON_BOOTS))
				e.setCancelled(true);
		}
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();

		kititems.add(createItem(Material.GHAST_TEAR, ChatColor.LIGHT_PURPLE + "Strong Kit"));

		return new Kit("Strong", Arrays.asList(new String[] { "Utilize durante 15 segundos e 3 vezes", "no total um set full iron e mate seus inimigos" }), kititems, new ItemStack(Material.WATCH));
	} */
}
