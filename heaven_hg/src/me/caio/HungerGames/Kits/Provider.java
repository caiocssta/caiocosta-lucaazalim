package me.caio.HungerGames.Kits;

public class Provider /* extends KitInterface */ {

/*	public Provider(Main main) {
		super(main);
	}

	public static HashMap<UUID, Inventory> playerInventory = new HashMap<UUID, Inventory>();
	public static HashMap<UUID, Inventory> playerItemInventory = new HashMap<UUID, Inventory>();

	@EventHandler
	public void click(PlayerInteractEvent e) {
		if (!e.hasItem()) {
			return;
		}
		if (e.getItem().getType() != Material.LAPIS_BLOCK) {
			return;
		}
		if (!hasAbility(e.getPlayer())) {
			return;
		}
		if ((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			if (!playerInventory.containsKey(e.getPlayer().getUniqueId())) {
				playerInventory.put(e.getPlayer().getUniqueId(), Bukkit.createInventory(e.getPlayer(), 27, e.getPlayer().getName() + " Backpack"));
			}
			e.getPlayer().openInventory(playerInventory.get(e.getPlayer().getUniqueId()));
		} else if ((e.getAction() == Action.LEFT_CLICK_AIR) || (e.getAction() == Action.LEFT_CLICK_BLOCK)) {
			if (!playerItemInventory.containsKey(e.getPlayer().getUniqueId())) {
				playerItemInventory.put(e.getPlayer().getUniqueId(), Bukkit.createInventory(e.getPlayer(), 9, e.getPlayer().getName() + " Items"));
			}
			e.getPlayer().openInventory(playerItemInventory.get(e.getPlayer().getUniqueId()));
		}
		e.setCancelled(true);
	}

	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		kititems.add(createItem(Material.LAPIS_BLOCK, "§cProvider"));
		return new Kit("provider", Arrays.asList(new String[] { "Receba os melhores itens da partida e", "tenha uma backpack para guardar seus itens!" }), kititems, new ItemStack(Material.LAPIS_BLOCK));
	} */
}
