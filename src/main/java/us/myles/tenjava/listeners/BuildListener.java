package us.myles.tenjava.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
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
			Entity previous = null;
			Entity first = null;
			for (int i = 0; i < 6; i++) {
				Entity fb;
				if (i != 2) {
					fb = e.getBlock().getWorld().spawnFallingBlock(e.getBlock().getLocation().add(0, i - 0.25, 0), Material.WOOL, (byte) 7);
				} else {
					fb = e.getPlayer();
				}
				if (first == null) {
					fb.setVelocity(new Vector(0, 1D, 0));
					first = fb;
				}
				if (previous != null) {
					previous.setPassenger(fb);
				}
				previous = fb;
			}
			RocketVelocity rocketVelo = new RocketVelocity(first);
			int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, rocketVelo, 5L, 5L);
			rocketVelo.setTaskID(id);
			e.setCancelled(true);
		}
	}
}
