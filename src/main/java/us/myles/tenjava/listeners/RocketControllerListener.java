package us.myles.tenjava.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import us.myles.tenjava.Plugin;
import us.myles.tenjava.tasks.RocketVelocity;

public class RocketControllerListener implements Listener {
	private HashMap<String, Location> lastRocket = new HashMap<String, Location>();
	private HashMap<String, Integer> fuel = new HashMap<String, Integer>();
	private Plugin plugin;

	public RocketControllerListener(Plugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getClickedBlock().getType() == Material.FURNACE) {
				Block me = e.getClickedBlock();
				if (me.getRelative(BlockFace.UP).getType() == Material.WOOL && me.getRelative(BlockFace.DOWN).getType() == Material.WOOL) {
					Inventory i = Bukkit.createInventory(e.getPlayer(), InventoryType.FURNACE, ChatColor.RED + "Rocket Control Panel");
					ItemStack launch = new ItemStack(Material.EMERALD);
					ItemMeta launchMeta = launch.getItemMeta();
					launchMeta.setDisplayName(ChatColor.GREEN + "Launch Rocket");
					launch.setItemMeta(launchMeta);
					i.setItem(0, launch);
					ItemStack destroy = new ItemStack(Material.REDSTONE);
					ItemMeta destroyMeta = launch.getItemMeta();
					destroyMeta.setDisplayName(ChatColor.DARK_RED + "Destroy Rocket");
					destroy.setItemMeta(destroyMeta);
					i.setItem(2, destroy);
					lastRocket.put(e.getPlayer().getName(), me.getLocation());
					InventoryView iView = e.getPlayer().openInventory(i);
					if (fuel.containsKey(e.getPlayer().getName())) {
						iView.setProperty(Property.BURN_TIME, 300);
						iView.setProperty(Property.TICKS_FOR_CURRENT_FUEL, 400);
					}
					e.setCancelled(true);
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClick(final InventoryClickEvent e) {
		if (e.getInventory().getName().equals(ChatColor.RED + "Rocket Control Panel")) {
			int clicked = e.getRawSlot();
			if (clicked == 0) {
				if (lastRocket.containsKey(e.getViewers().get(0).getName())) {
					closeInv(e.getViewers().get(0));
					launchRocket(lastRocket.get(e.getViewers().get(0).getName()).getBlock(), e.getViewers().get(0));
				}
				e.setCancelled(true);
			}
			if (clicked == 1) {
				if (e.getAction() == InventoryAction.PLACE_ALL || e.getAction() == InventoryAction.PLACE_ONE || e.getAction() == InventoryAction.PLACE_SOME) {
					if (e.getCursor() != null) {
						if (e.getCursor().getType() == Material.COAL) {
							if (e.getCursor().getAmount() != 1 || fuel.containsKey(e.getViewers().get(0).getName())) {
								e.setCancelled(true);
								if(e.getCursor().getAmount() != 1) {
									((Player) e.getViewers().get(0)).sendMessage(ChatColor.RED + "Please put 1 coal in at once.");
								}else {
									((Player) e.getViewers().get(0)).sendMessage(ChatColor.RED + "There is already fuel in this rocket.");
								}
								return;
							}
							e.setCursor(null);
							e.getInventory().setItem(1, null);
							((Player) e.getViewers().get(0)).updateInventory();
							e.getView().setProperty(Property.BURN_TIME, 300);
							e.getView().setProperty(Property.TICKS_FOR_CURRENT_FUEL, 400);
							fuel.put(e.getViewers().get(0).getName(), fuel.containsKey(e.getViewers().get(0).getName()) ? fuel.get(e.getViewers().get(0).getName()) + 1 : 1);
							((Player) e.getViewers().get(0)).sendMessage(ChatColor.AQUA + "Fuel loaded, ready to fly!");
						}
					}
				}
			}
			if (clicked == 2) {
				closeInv(e.getViewers().get(0));
				destroyRocket(lastRocket.get(e.getViewers().get(0).getName()).getBlock(), e.getViewers().get(0));
			}
		}
	}

	private void closeInv(final HumanEntity humanEntity) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				humanEntity.closeInventory();
			}
		});
	}

	public void destroyRocket(Block furnace, HumanEntity launcher) {
		if (furnace.getRelative(BlockFace.UP).getType() == Material.WOOL && furnace.getRelative(BlockFace.DOWN).getType() == Material.WOOL) {
			furnace.breakNaturally();
			furnace.getRelative(BlockFace.UP).breakNaturally();
			furnace.getRelative(BlockFace.DOWN).breakNaturally();
		}
	}

	@SuppressWarnings("deprecation")
	public void launchRocket(Block furnace, HumanEntity launcher) {
		if (!fuel.containsKey(launcher.getName())) {
			((Player) launcher).sendMessage(ChatColor.RED + "You need to load coal into the rocket first!");
			return;
		}
		fuel.remove(launcher.getName());
		if (furnace.getRelative(BlockFace.UP).getType() == Material.WOOL && furnace.getRelative(BlockFace.DOWN).getType() == Material.WOOL) {
			Entity previous = null;
			List<Entity> entities = new ArrayList<Entity>();
			for (int i = 0; i < 3; i++) {
				Entity entity;
				if (i != 2) {
					entity = furnace.getWorld().spawnFallingBlock(furnace.getLocation().add(0, i - 0.25, 0), Material.WOOL, furnace.getRelative(BlockFace.DOWN).getData());
					FallingBlock fallingBlock = (FallingBlock) entity;
					fallingBlock.setDropItem(false);
				} else {
					entity = launcher;
				}
				entity.setVelocity(new Vector(0, 2D, 0));
				if (previous != null) {
					previous.setPassenger(entity);
				}
				entities.add(entity);
				previous = entity;
			}
			RocketVelocity rocketVelo = new RocketVelocity(entities);
			int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, rocketVelo, 1L, 1L);
			rocketVelo.setTaskID(id);
			furnace.setType(Material.AIR);
			furnace.getRelative(BlockFace.UP).setType(Material.AIR);
			furnace.getRelative(BlockFace.DOWN).setType(Material.AIR);
		}
	}
}
