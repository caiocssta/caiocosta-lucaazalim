package me.caio.HungerGames.Kits;

public class LuckyWool /* extends KitInterface */ {

/*	private Map<Type, List<ItemStack>> loot;
	private HashMap<UUID, Integer> usecount;

	public LuckyWool(Main main) {
		super(main);
		this.usecount = new HashMap<UUID, Integer>();
		setupLoot();
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if (hasAbility(p, "luckywool")) {
			if (e.getItemInHand().getType() == Material.WOOL) {
				e.setCancelled(true);

			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if ((hasAbility(p, "luckywool") && (e.getPlayer().getItemInHand() != null) && (e.getPlayer().getItemInHand().getType() == Material.WOOL) && (e.getPlayer().getItemInHand().hasItemMeta())
				&& (e.getAction() != Action.PHYSICAL))) {
			e.setCancelled(true);
			ItemStack item = e.getPlayer().getItemInHand();
			if ((e.getAction() == Action.LEFT_CLICK_AIR) || (e.getAction() == Action.LEFT_CLICK_BLOCK)) {
				p.setItemInHand(switchType(p, item.getDurability()));
			} else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				claimLoot(p, e.getClickedBlock().getLocation());
				p.setItemInHand(getItemStack(p, item.getDurability()));
			} else {
				p.sendMessage(ChatColor.YELLOW + "Para receber um item coloque a la no chão!");
				p.setItemInHand(getItemStack(p, item.getDurability()));
			}
			p.updateInventory();
		}
	}

	private int getCount(Player p) {
		if (usecount.containsKey(p.getUniqueId())) {
			if (usecount.get(p.getUniqueId()) < 1) {
				return 1;
			}
			return usecount.get(p.getUniqueId());
		}
		return 1;
	}

	private ItemStack getItemStack(Player p, short i) {
		if (i == 3) {
			return new ItemBuilder().setType(Material.WOOL).setAmount(getCount(p)).setDurability(3).setName("§9§lPoções")
					.setDescription("§7Clique com o §aesquerdo §7para trocar!", "§7Coloque no chão para receber itens!").getStack();
		}
		if (i == 1) {
			return new ItemBuilder().setType(Material.WOOL).setAmount(getCount(p)).setDurability(1).setName("§6§lUtensilhos")
					.setDescription("§7Clique com o §aesquerdo §7para trocar!", "§7Coloque no chão para receber itens!").getStack();

		}
		return new ItemBuilder().setType(Material.WOOL).setAmount(getCount(p)).setDurability(4).setName("§e§lCombate").setDescription("§7Clique com o §aesquerdo §7para trocar!", "§7Coloque no chão para receber itens!")
				.getStack();

	}

	private ItemStack switchType(Player p, short i) {
		if (i == 4) {
			return new ItemBuilder().setType(Material.WOOL).setAmount(getCount(p)).setDurability(3).setName("§9§lPoções")
					.setDescription("§7Clique com o §aesquerdo §7para trocar!", "§7Coloque no chão para receber itens!").getStack();
		}
		if (i == 3) {
			return new ItemBuilder().setType(Material.WOOL).setAmount(getCount(p)).setDurability(1).setName("§6§lUtensilhos")
					.setDescription("§7Clique com o §aesquerdo §7para trocar!", "§7Coloque no chão para receber itens!").getStack();
		}

		return new ItemBuilder().setType(Material.WOOL).setAmount(getCount(p)).setDurability(4).setName("§e§lCombate").setDescription("§7Clique com o §aesquerdo §7para trocar!", "§7Coloque no chão para receber itens!")
				.getStack();
	}

	private Type getType(short i) {

		if (i == 3) {
			return Type.POTIONS;
		}
		if (i == 1) {
			return Type.TOOLS;
		}
		return Type.COMBAT;

	}

	@EventHandler
	public void onGainKill(PlayerGainKillEvent e) {
		Player p = e.getPlayer();
		if (hasAbility(p, "luckywool")) {
			if (e.getKills() % 2 == 0) {
				if (!this.usecount.containsKey(p.getUniqueId())) {
					usecount.put(p.getUniqueId(), 0);
				}
				this.usecount.put(p.getUniqueId(), usecount.get(p.getUniqueId()) + 1);
				p.sendMessage(ChatColor.YELLOW + "Você recebeu uma §6§lLuckyWool§e!");
				p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
				for (ItemStack i : p.getInventory().getContents()) {
					if (i != null) {
						if (i.getType() == Material.WOOL) {
							i.setAmount(getCount(p));
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onTime(TimeSecondEvent event) {
		if (Main.getInstance().Invenci == 0) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (hasAbility(p, "luckywool")) {

					if (Cooldown.isInCooldown(p.getUniqueId(), "luckywool")) {
						return;
					}
					if (!this.usecount.containsKey(p.getUniqueId())) {
						usecount.put(p.getUniqueId(), 0);
					}
					this.usecount.put(p.getUniqueId(), usecount.get(p.getUniqueId()) + 1);
					p.sendMessage(ChatColor.YELLOW + "Você recebeu uma §6§lLuckyWool§e!");
					p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
					for (ItemStack i : p.getInventory().getContents()) {
						if (i != null) {
							if (i.getType() == Material.WOOL) {
								i.setAmount(getCount(p));
							}
						}
					}
					p.updateInventory();
					if (!Cooldown.isInCooldown(p.getUniqueId(), "luckywool")) {
						Cooldown c = new Cooldown(p.getUniqueId(), "luckywool", 120);
						c.start();
					}

				}
			}
		}
	}

	public void generateLoot(Player p, Location loc) {
		Type type = getType(p.getItemInHand().getDurability());
		ItemStack i = null;
		for (ItemStack item : getRandomLoot(type)) {
			i = item;
			if (type == Type.COMBAT) {
				i = getRandomBasicLoot();
				if (getMain().GameTimer >= 300) {
					i = getRandomAdvancedLoot();
				}
				if (getMain().GameTimer >= 1200) {
					i = getRandomExtremeLoot();
				}
			}
			loc.getWorld().dropItemNaturally(loc.add(0.0D, 0.5D, 0.0D), i);
			loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1);
			p.playSound(p.getLocation(), Sound.FIREWORK_BLAST, 10F, 10F);
		}
	}

	private void claimLoot(Player p, Location loc) {
		if (!usecount.containsKey(p.getUniqueId())) {
			usecount.put(p.getUniqueId(), 0);
		}
		if (usecount.get(p.getUniqueId()) < 1) {
			p.sendMessage(ChatColor.YELLOW + "Você não pode usar ainda, mate alguem ou espere um pouco!");
			return;
		}
		usecount.put(p.getUniqueId(), usecount.get(p.getUniqueId()) - 1);
		generateLoot(p, loc);
	}

	private List<ItemStack> getRandomLoot(Type type) {
		List<ItemStack> result = new ArrayList<ItemStack>();
		result.add(this.loot.get(type).get(new Random().nextInt(this.loot.get(type).size())));
		ItemStack doubles = result.get(0);
		if (doubles.getType() == Material.BOW) {
			result.add(new ItemStack(Material.ARROW, 16));
		}
		if (doubles.getType() == Material.ARROW) {
			result.add(new ItemStack(Material.BOW));
		}
		if (doubles.getType() == Material.INK_SACK) {
			result.add(new ItemStack(Material.BOWL, 32));
		}
		if (doubles.getType() == Material.BOWL) {
			result.add(new ItemStack(Material.INK_SACK, 32, (short) 3));
		}

		return result;

	}

	private ItemStack getRandomBasicLoot() {
		List<ItemStack> result = new ArrayList<ItemStack>();

		for (ItemStack i : this.loot.get(Type.COMBAT)) {
			if (!i.getType().toString().contains("DIAMOND")) {
				result.add(i);
			}
		}
		return result.get(new Random().nextInt(result.size()));

	}

	private ItemStack getRandomAdvancedLoot() {
		List<ItemStack> result = new ArrayList<ItemStack>();

		for (ItemStack i : this.loot.get(Type.COMBAT)) {
			if (i.getType().toString().contains("IRON")) {
				result.add(i);
			}
		}
		return result.get(new Random().nextInt(result.size()));

	}

	private ItemStack getRandomExtremeLoot() {
		List<ItemStack> result = new ArrayList<ItemStack>();

		for (ItemStack i : this.loot.get(Type.COMBAT)) {
			if (i.getType().toString().contains("DIAMOND")) {
				result.add(i);
			}
		}
		return result.get(new Random().nextInt(result.size()));

	}

	private void setupLoot() {
		this.loot = new HashMap<Type, List<ItemStack>>();
		List<ItemStack> items = new ArrayList<ItemStack>();
		items.add(new ItemStack(Material.DIAMOND_SWORD));
		items.add(new ItemStack(Material.DIAMOND_HELMET));
		items.add(new ItemStack(Material.DIAMOND_CHESTPLATE));
		items.add(new ItemStack(Material.DIAMOND_LEGGINGS));
		items.add(new ItemStack(Material.DIAMOND_BOOTS));
		items.add(new ItemStack(Material.LEATHER_HELMET));
		items.add(new ItemStack(Material.LEATHER_CHESTPLATE));
		items.add(new ItemStack(Material.LEATHER_LEGGINGS));
		items.add(new ItemStack(Material.LEATHER_BOOTS));
		items.add(new ItemStack(Material.IRON_SWORD));
		items.add(new ItemStack(Material.IRON_HELMET));
		items.add(new ItemStack(Material.IRON_CHESTPLATE));
		items.add(new ItemStack(Material.IRON_LEGGINGS));
		items.add(new ItemStack(Material.IRON_BOOTS));
		items.add(new ItemStack(Material.BOW));
		items.add(new ItemStack(Material.ARROW, 16));
		this.loot.put(Type.COMBAT, items);
		items = new ArrayList<ItemStack>();
		items.add(new ItemStack(Material.WATER_BUCKET));
		items.add(new ItemStack(Material.LAVA_BUCKET));
		items.add(new ItemStack(Material.MILK_BUCKET));
		items.add(new ItemStack(Material.ENDER_PEARL, 4));
		items.add(new ItemStack(Material.ENCHANTMENT_TABLE));
		items.add(new ItemStack(Material.FLINT_AND_STEEL));
		items.add(new ItemStack(Material.TNT, 4));
		items.add(new ItemStack(Material.INK_SACK, 32, (short) 3));
		items.add(new ItemStack(Material.BOWL, 32));
		this.loot.put(Type.TOOLS, items);
		items = new ArrayList<ItemStack>();
		items.add(new ItemBuilder().setType(Material.POTION).setDurability(8194).getStack());
		items.add(new ItemBuilder().setType(Material.POTION).setDurability(8201).getStack());
		items.add(new ItemBuilder().setType(Material.POTION).setDurability(8227).getStack());
		items.add(new ItemBuilder().setType(Material.POTION).setDurability(8261).getStack());
		items.add(new ItemBuilder().setType(Material.POTION).setDurability(8193).getStack());
		items.add(new ItemBuilder().setType(Material.POTION).setDurability(16460).getStack());
		items.add(new ItemBuilder().setType(Material.POTION).setDurability(16426).getStack());
		items.add(new ItemBuilder().setType(Material.POTION).setDurability(16424).getStack());
		items.add(new ItemBuilder().setType(Material.POTION).setDurability(16388).getStack());
		this.loot.put(Type.POTIONS, items);
		items = null;
	}

	public enum Type {
		COMBAT,
		TOOLS,
		POTIONS;

	}

	public Kit getKit() {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		items.add(new ItemBuilder().setType(Material.WOOL).setAmount(1).setDurability(4).setName("§e§lCombate").setDescription("§7Clique com o §aesquerdo §7para trocar!").getStack());
		return new Kit("luckywool", Arrays.asList(new String[] { "Utilize suas lãs e ganhe diversos itens especiais ao longo da partida!" }), items, new ItemBuilder().setType(Material.WOOL).setDurability(4).getStack());
	} */
}
