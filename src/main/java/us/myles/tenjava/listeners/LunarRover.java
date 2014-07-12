package us.myles.tenjava.listeners;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleUpdateEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import us.myles.tenjava.Plugin;

public class LunarRover implements Listener {
	private Plugin plugin;

	public LunarRover(Plugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (!e.getPlayer().getWorld().getName().equals("moon"))
			return;
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getItem() == null)
				return;
			if (e.getItem().getType() == Material.MINECART) {
				e.getPlayer().getInventory().remove(e.getItem());
				Block target = e.getClickedBlock();
				Minecart minecart = (Minecart) target.getLocation().getWorld().spawnEntity(target.getLocation().add(0, 2, 0), EntityType.MINECART_FURNACE);
				minecart.setMetadata("rover", new FixedMetadataValue(plugin, true));
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void roverUpdate(VehicleUpdateEvent e) {
		Vehicle vehicle = e.getVehicle();
		if (vehicle.getMetadata("rover").size() != 0) {
			if (vehicle.getPassenger() != null) {
				Player passenger = (Player) vehicle.getPassenger();
				// distance to ground
				Location ground = vehicle.getWorld().getHighestBlockAt(vehicle.getLocation()).getLocation();
				Vector v = passenger.getLocation().getDirection();
				if (ground.distance(vehicle.getLocation()) > 2 && v.getY() > 0)
					v.setY(0);
				vehicle.getWorld().playEffect(vehicle.getLocation(), Effect.SMOKE, 0);
				vehicle.setVelocity(v);

			}
		}
	}

}
