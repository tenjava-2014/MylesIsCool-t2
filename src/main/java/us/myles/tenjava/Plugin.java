package us.myles.tenjava;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.World.Environment;
import org.bukkit.plugin.java.JavaPlugin;

import us.myles.tenjava.generators.MoonGenerator;
import us.myles.tenjava.listeners.LunarRover;
import us.myles.tenjava.listeners.MoonEffects;
import us.myles.tenjava.listeners.RocketControllerListener;
import us.myles.tenjava.tasks.MoonTimedEffects;

public class Plugin extends JavaPlugin {
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new RocketControllerListener(this), this);
		Bukkit.getPluginManager().registerEvents(new MoonEffects(this), this);
		Bukkit.getPluginManager().registerEvents(new LunarRover(this), this);
		WorldCreator.name("moon").type(WorldType.NORMAL).environment(Environment.NORMAL).generator(new MoonGenerator()).createWorld();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new MoonTimedEffects(), 5L, 5L);
	}
}