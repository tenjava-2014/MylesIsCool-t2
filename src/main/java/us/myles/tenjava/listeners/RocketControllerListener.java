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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import us.myles.tenjava.Plugin;
import us.myles.tenjava.tasks.RocketVelocity;

public class RocketControllerListener implements Listener {
	private HashMap<String, Location> lastRocket = new HashMap<String, Location>();
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
					Inventory i = Bukkit.createInventory(e.getPlayer(), InventoryType.HOPPER, ChatColor.RED + "Rocket Control Panel");
					ItemStack launch = new ItemStack(Material.EMERALD);
					ItemMeta launchMeta = launch.getItemMeta();
					launchMeta.setDisplayName(ChatColor.GREEN + "Launch Rocket");
					launch.setItemMeta(launchMeta);
					i.addItem(launch);
					lastRocket.put(e.getPlayer().getName(), me.getLocation());
					e.getPlayer().openInventory(i);
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onClick(final InventoryClickEvent e) {
		if (e.getInventory().getName().equals(ChatColor.RED + "Rocket Control Panel")) {
			e.setCancelled(true);
			int clicked = e.getRawSlot();
			if (clicked == 0) {
				if (lastRocket.containsKey(e.getViewers().get(0).getName())) {
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							e.getViewers().get(0).closeInventory();
						}

					});
					launchRocket(lastRocket.get(e.getViewers().get(0).getName()).getBlock(), e.getViewers().get(0));
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void launchRocket(Block furnace, HumanEntity launcher) {
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
