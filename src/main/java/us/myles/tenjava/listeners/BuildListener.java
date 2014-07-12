package us.myles.tenjava.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import us.myles.tenjava.Plugin;
import us.myles.tenjava.tasks.RocketVelocity;

public class BuildListener implements Listener {
	private Plugin plugin;

	public BuildListener(Plugin plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getClickedBlock().getType() == Material.FURNACE) {
				Block me = e.getClickedBlock();
				if (me.getRelative(BlockFace.UP).getType() == Material.WOOL && me.getRelative(BlockFace.DOWN).getType() == Material.WOOL) {
					Entity previous = null;
					List<Entity> entities = new ArrayList<Entity>();
					for (int i = 0; i < 3; i++) {
						Entity entity;
						if (i != 2) {
							entity = me.getWorld().spawnFallingBlock(me.getLocation().add(0, i - 0.25, 0), Material.WOOL, me.getRelative(BlockFace.DOWN).getData());
							FallingBlock fallingBlock = (FallingBlock) entity;
							fallingBlock.setDropItem(false);
						} else {
							entity = e.getPlayer();
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
					e.setCancelled(true);
					me.setType(Material.AIR);
					me.getRelative(BlockFace.UP).setType(Material.AIR);
					me.getRelative(BlockFace.DOWN).setType(Material.AIR);
				}
			}
		}
	}

	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
		if (e.getItem().getItemStack().getType() == Material.ENDER_STONE) {
			if (e.getPlayer().getWorld().getName().equals("moon")) {
				ItemMeta im = e.getItem().getItemStack().getItemMeta();
				im.setDisplayName("Moonstone");
				e.getItem().getItemStack().setItemMeta(im);
			}
		}
	}
}
