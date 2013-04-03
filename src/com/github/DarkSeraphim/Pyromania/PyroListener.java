package com.github.DarkSeraphim.Pyromania;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 *
 * @author DarkSeraphim
 */
public class PyroListener implements Listener
{
    private Pyromania plugin;
    
    protected PyroListener(Pyromania plugin)
    {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onLit(EntityChangeBlockEvent e)
    {
        if(e.getEntity() instanceof FallingBlock)
        {
            FallingBlock fb = (FallingBlock) e.getEntity();
            if(fb.getBlockId() == Material.FIRE.getId())
            {
                if(e.getBlock().getType() != Material.AIR || !plugin.doesCreateFire())
                {
                    e.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack hand = event.getItem();        
        
        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
        {
            return;
        }
        
        if(hand != null && hand.getTypeId() == plugin.getTool().getTypeId())
        {
            if(!plugin.toggled.contains(event.getPlayer().getName()))
            {
                return;
            }
            Material inHead = player.getEyeLocation().getBlock().getType();
            if(inHead == Material.WATER || inHead == Material.STATIONARY_WATER || inHead.isSolid())
            {
                Location l = player.getEyeLocation().add(player.getLocation().getDirection().normalize());
                for(int i = 0; i < 3; i++)
                {
                    player.getWorld().playEffect(l, Effect.SMOKE, BlockFace.SELF);
                    player.getWorld().playSound(l, Sound.FIZZ, 1, 63);
                }
                return;
            }
            if((player.getGameMode() != GameMode.CREATIVE && player.getInventory().removeItem(plugin.getAmmo()).isEmpty()) || player.getGameMode() == GameMode.CREATIVE)
            {
                player.updateInventory();
                Vector speed = player.getLocation().getDirection().normalize();
                Location l = player.getEyeLocation().add(speed);
                FallingBlock fb;
                for(int i = 0; i < plugin.getAmount(); i++)
                {
                    fb = player.getWorld().spawnFallingBlock(l, Material.FIRE, (byte)1);

                    double x = (Math.random()*plugin.getSpread()) - plugin.getSpread()/2;
                    double y = (Math.random()*plugin.getSpread()) - plugin.getSpread()/2;
                    double z = (Math.random()*plugin.getSpread()) - plugin.getSpread()/2;
                    fb.setVelocity(speed.clone().add(new Vector(x,y,z)));
                    fb.setDropItem(false);
                    fb.setTicksLived(1);
                    player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0, 100);
                }
            }
            else
            {
                Location l = player.getEyeLocation().add(player.getLocation().getDirection().normalize());
                for(int i = 0; i < 3; i++)
                {
                    player.getWorld().playEffect(l, Effect.SMOKE, BlockFace.SELF);
                    player.getWorld().playSound(l, Sound.FIZZ, 1, 63);
                }
            }
        }
    }
}
