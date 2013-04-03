package com.github.DarkSeraphim.Pyromania;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author DarkSeraphim
 */
public class PyroTask extends BukkitRunnable
{
    private Pyromania plugin;
    
    protected PyroTask(Pyromania plugin)
    {
        this.plugin = plugin;
    }
    
    @Override
    public void run()
    {
        for(World w0 : Bukkit.getWorlds())
        for(FallingBlock b : w0.getEntitiesByClass(FallingBlock.class))
        {
            if(b.getBlockData() == 1)
            {
                if(b.getTicksLived() < 3) 
                {
                    continue;
                }
                final Location l = b.getLocation();
                final World w = b.getWorld();
                if(plugin.particlesAreEnabled())
                {
                    w.playEffect(l, Effect.MOBSPAWNER_FLAMES, 0, 50);
                }
                setFire(b);
                checkAlive(b);
                if(plugin.particlesAreEnabled())
                {
                    new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            w.playEffect(l, Effect.SMOKE, BlockFace.SELF, 50);
                        }
                    }.runTaskLater(plugin, 1L);
                }
            }
        }
    }

    public void setFire(FallingBlock fb)
    {
        for(Entity e : fb.getNearbyEntities(0.0, 0.0, 0.0))
        {
            if(e instanceof LivingEntity)
            {
                e.setFireTicks(100);
            }
        }
    }

    public boolean checkAlive(FallingBlock fb)
    {
        Material next = fb.getLocation().add(fb.getVelocity()).getBlock().getType();
        if(fb.getTicksLived() > plugin.getAliveTicks() || next == Material.WATER || next == Material.STATIONARY_WATER)
        {
            fb.remove();
            return false;
        }
        return true;
    }
}
