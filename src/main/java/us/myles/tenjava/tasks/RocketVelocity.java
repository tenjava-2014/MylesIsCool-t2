package us.myles.tenjava.tasks;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.util.Vector;

public class RocketVelocity implements Runnable {

	private List<Entity> block;
	private int id;

	public RocketVelocity(List<Entity> entities) {
		this.block = entities;
	}

	@Override
	public void run() {
		for (Entity e : block) {
			if (e instanceof Player) {
				if (e.getLocation().getY() > 300) {
					Bukkit.getScheduler().cancelTask(this.id);
					return;
				}
			}
		}
		if (block.get(0).isDead()) {
			Bukkit.getScheduler().cancelTask(this.id);
			if (block.get(0).getLocation().getY() > 250) {
				for (Entity e : block) {
					if (e instanceof Player) {
						Player player = (Player) e;
						player.eject();
						String r = player.getWorld().getName().equals("moon") ? "world" : "moon";
						player.sendMessage("You are entering the " + r + " realm.");
						player.teleport((player.getWorld().getName().equals("moon") ? Bukkit.getWorlds().get(0) : Bukkit.getWorld("moon")).getHighestBlockAt((int) player.getLocation().getX(),
								(int) player.getLocation().getZ()).getLocation(), TeleportCause.COMMAND);
					}
				}
			}
			return;
		}
		for (Entity e : block) {
			if (e.getVehicle() == null && e instanceof Player)
				continue;
			if (e.getLocation().getBlock().getType() != Material.AIR) {
				Bukkit.getScheduler().cancelTask(this.id);
				e.getWorld().createExplosion(e.getLocation(), 2.5F);
				return;
			}
			e.setVelocity(new Vector(0, 2L, 0));
		}
		block.get(0).getWorld().playEffect(block.get(0).getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
	}

	public void setTaskID(int id) {
		this.id = id;
	}

}
