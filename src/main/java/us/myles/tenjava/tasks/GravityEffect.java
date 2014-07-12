package us.myles.tenjava.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import us.myles.tenjava.listeners.MoonEffects;

public class GravityEffect implements Runnable {

	private Player player;
	private int id;
	private int tick = 0;

	public GravityEffect(Player player) {
		this.player = player;
	}

	public void setTaskID(int id) {
		this.id = id;
	}

	@Override
	public void run() {
		if (tick < 40) {
			player.setVelocity(new Vector(0, 0.5, 0));
		}
		if (tick > 200) {
			Bukkit.getScheduler().cancelTask(id);
			MoonEffects.removePlayer(player);
		}
		tick++;
	}

}
