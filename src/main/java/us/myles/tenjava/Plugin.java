package us.myles.tenjava;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.World.Environment;
import org.bukkit.plugin.java.JavaPlugin;

import us.myles.tenjava.generators.MoonGenerator;
import us.myles.tenjava.listeners.BuildListener;

public class Plugin extends JavaPlugin {
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new BuildListener(this), this);
		WorldCreator.name("moon").type(WorldType.NORMAL).environment(Environment.NORMAL).generator(new MoonGenerator()).createWorld();
	}
}