package us.myles.tenjava;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import us.myles.tenjava.listeners.BuildListener;

public class Plugin extends JavaPlugin {
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new BuildListener(this), this);
	}
}

// How can energy be harnessed and used in the Minecraft world?
// Machines?
// Power being used


// |
// v


// What can increase Minecraft's replay value?
// * Collection System with trading which makes you play more
// * New dimension
// * Dynamic 
// * Quest?