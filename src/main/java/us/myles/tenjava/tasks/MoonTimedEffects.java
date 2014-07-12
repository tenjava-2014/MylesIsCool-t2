package us.myles.tenjava.tasks;

import org.bukkit.Bukkit;

public class MoonTimedEffects implements Runnable {

	@Override
	public void run() {
		Bukkit.getWorld("moon").setTime(14400L);
	}

}
