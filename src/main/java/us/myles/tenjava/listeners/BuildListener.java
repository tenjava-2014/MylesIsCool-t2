package us.myles.tenjava.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;

import us.myles.tenjava.Plugin;
import us.myles.tenjava.tasks.RocketVelocity;

public class BuildListener implements Listener {
	private Plugin plugin;

	public BuildListener(Plugin plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if (e.getBlock().getType() == Material.WOOL) {
			// This is not complete a rocket would be built here.
			Entity previous = null;
			List<Entity> entities = new ArrayList<Entity>();
			for (int i = 0; i < 6; i++) {
				Entity entity;
				if (i != 2) {
					entity = e.getBlock().getWorld().spawnFallingBlock(e.getBlock().getLocation().add(0, i - 0.25, 0), Material.WOOL, (byte) 7);
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
		}
	}
}
