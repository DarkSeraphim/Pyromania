package com.github.DarkSeraphim.Pyromania;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import static org.bukkit.ChatColor.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author DarkSeraphim
 */
public class Pyromania extends JavaPlugin implements Listener
{
    
    public Logger log;
    
    protected final HashSet<String> toggled = new HashSet<String>();
    
    private boolean externalForceActive = false;
    
    private PyroTask task;
    
    private ItemStack tool;
    
    private ItemStack ammo;
    
    private double spread = 0.0;
    
    private long maxTicks = 1;
    
    private int amount = 0;
    
    private boolean particlesEnabled = false;
    
    private boolean createFire = false;

    @Override
    public void onEnable()
    {
        log = getLogger();
        
        /****************************\
        |*          CONFIG          *|
        \****************************/
        
        getConfig().options().copyDefaults(true);
        InputStream defStream = getResource("config.yml");
        if(defStream != null)
        {
            YamlConfiguration def = YamlConfiguration.loadConfiguration(defStream);
            getConfig().setDefaults(def);
        }
        saveConfig();
        loadValues();
        
        Bukkit.getPluginManager().registerEvents(new PyroListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
        getCommand("pyro").setExecutor(new PyroExecutor(this));
        task = new PyroTask(this);
        task.runTaskTimer(this, 40L, 1L);
        
        /****************************\
        |*          Metrics         *|
        \****************************/
        getLogger().info("Hooking into Metrics...");
        try 
        {
            Metrics metrics = new Metrics(this);
            metrics.start();
            getLogger().info("Hooking into Metrics was successful :D");
        }
        catch (IOException e) 
        {
            // Failed to submit the stats :-(
            getLogger().warning("Hooking into Metrics was unsuccessful D:");
        }
    }
    
    @Override
    public void onDisable()
    {
        Bukkit.getScheduler().cancelTasks(this);
    }
    
    private void loadValues()
    {
        this.tool = new ItemStack(getConfig().getInt("pyro.tool"));
        this.ammo = new ItemStack(getConfig().getInt("pyro.ammo.id"),getConfig().getInt("pyro.ammo.amount", 1));
        this.spread = getConfig().getDouble("pyro.spread");
        this.maxTicks = getConfig().getLong("pyro.max-lived-ticks");
        this.amount = getConfig().getInt("pyro.amount");
        this.externalForceActive = getConfig().getBoolean("pyro.external-toggle");
        this.particlesEnabled = getConfig().getBoolean("pyro.particles-enabled");
        this.createFire = getConfig().getBoolean("pyro.create-fire");
    }
    
    public boolean externalForcesAreActive()
    {
        return this.externalForceActive;
    }
    
    public boolean particlesAreEnabled()
    {
        return this.particlesEnabled;
    }
    
    public boolean doesCreateFire()
    {
        return this.createFire;
    }
    
    public int getAmount()
    {
        return this.amount;
    }
    
    public long getAliveTicks()
    {
        return this.maxTicks;
    }
    
    public double getSpread()
    {
        return this.spread;
    }
    
    public ItemStack getAmmo()
    {
        return this.ammo;
    }
    
    public ItemStack getTool()
    {
        return this.tool;
    }
    
    @Override
    public void reloadConfig()
    {
        super.reloadConfig();
        loadValues();
    }
}
