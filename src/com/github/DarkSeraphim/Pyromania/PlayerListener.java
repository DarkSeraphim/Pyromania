package com.github.DarkSeraphim.Pyromania;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author DarkSeraphim
 */
public class PlayerListener implements Listener
{
    private Pyromania plugin;
    
    public PlayerListener(Pyromania plugin)
    {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onquit(PlayerQuitEvent e)
    {
        String leaver = e.getPlayer().getName();
        plugin.toggled.remove(leaver);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        Player player = e.getPlayer();
        if(player.hasPermission("pyro.auto") && plugin.getConfig().getBoolean("pyro.auto-enable", false))
        {
            plugin.toggled.add(player.getName());
        }
    }   
}
