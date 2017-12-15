package fr.etrenak.chargedcreepers;

import java.io.File;
import java.nio.file.Files;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ChargedCreepers extends JavaPlugin implements CommandExecutor, Listener
{
	public static ChargedCreepers instance;
	
	@Override
	public void onEnable()
	{
		instance = this;
		getServer().getPluginManager().registerEvents(this,  this);
		File check = new File(getDataFolder(), "config.yml");

		if(!getDataFolder().exists())
			getDataFolder().mkdir();

		if(!check.exists())
			try
			{
				Files.copy(getClassLoader().getResourceAsStream("config.yml"), check.toPath());
			}catch(Exception e)
			{
				e.printStackTrace();
			}
	}
	
	public static ChargedCreepers getInstance()
	{
		return instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(command.getName().equalsIgnoreCase("ChargedCreepersReload"))
			try
			{
				getConfig().load(new File(getDataFolder(), "config.yml"));
				sender.sendMessage("§aConfig reloaded");
			}catch(Exception e)
		{
				e.printStackTrace();
			}
		return true;
	}
	
	@EventHandler
	public void creeperSpawn(CreatureSpawnEvent e)
	{
			if(e.getEntityType().equals(EntityType.CREEPER))
				if(new Random().nextInt(100) <= getConfig().getDouble("spawn_chance"))
					((Creeper) e.getEntity()).setPowered(true);
	}
	
	@EventHandler
	public void creeperDeath(EntityDeathEvent e)
	{
		if(e.getEntity() instanceof Creeper && ((Creeper)e.getEntity()).isPowered())
			if(new Random().nextInt(100) <= getConfig().getDouble("tnt_drop_chance"))
			e.getDrops().add(new ItemStack(Material.TNT, getConfig().getInt("tnt_drop_amount")));
	}
}
