package us.myles.tenjava.tasks;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class RocketVelocity implements Runnable {

	private List<Entity> block;
	private int id;

	public RocketVelocity(List<Entity> entities) {
		this.block = entities;
	}

	@Override
	public void run() {
		if (block.get(0).isDead()) {
			Bukkit.getScheduler().cancelTask(this.id);
			if (block.get(0).getLocation().getY() > 250) {
				for (Entity e : block) {
					if (e instanceof Player) {
						Player player = (Player) e;
						player.sendMessage("You are entering the moon realm.");
						player.teleport(Bukkit.getWorld("moon").getSpawnLocation());
					}
				}
			}
			return;
		}
		for (Entity e : block) {
			// if (e instanceof Player)
			// continue;
			e.setVelocity(new Vector(0, 2L, 0));
		}

		block.get(0).getWorld().playEffect(block.get(0).getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
	}

	public void setTaskID(int id) {
		this.id = id;
	}

}
