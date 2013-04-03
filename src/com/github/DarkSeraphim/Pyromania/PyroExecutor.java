package com.github.DarkSeraphim.Pyromania;

import org.bukkit.Bukkit;
import static org.bukkit.ChatColor.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author DarkSeraphim
 */
public class PyroExecutor implements CommandExecutor
{
    private Pyromania plugin;
    
    protected PyroExecutor(Pyromania plugin)
    {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(label.equalsIgnoreCase("pyro"))
        {
            if(args.length > 0)
            {
                if(args[0].equalsIgnoreCase("toggle"))
                {
                    Player p = null;
                    if(args.length == 2)
                    {
                        if(sender instanceof Player == false)
                        {
                            if(!plugin.externalForcesAreActive())
                            {
                                sender.sendMessage(RED+"Sorry but external influence is disabled");
                                return true;
                            }
                        }
                        else
                        {
                            if(!sender.hasPermission("pyro.toggle.other"))
                            {
                                sender.sendMessage(RED+"Sorry you cannot toggle this for others");
                                return true;
                            }
                        }
                        p = Bukkit.getPlayer(args[1]);
                        if(p == null)
                        {
                            sender.sendMessage(String.format(RED+"Sorry but "+AQUA+"%1$s"+RED+" is not online", args[1]));
                            return true;
                        }
                    }
                    else
                    {
                        if(sender instanceof Player == false)
                        {
                            sender.sendMessage(RED+"Sorry, but the console cannot toggle pyromania");
                            return true;
                        }
                        if(!sender.hasPermission("pyro.toggle.self"))
                        {
                            sender.sendMessage(RED+"You cannot toggle pyromania");
                            return true;
                        }
                        p = (Player)sender;
                    }
                    
                    if(plugin.toggled.contains(p.getName()))
                    {
                        plugin.toggled.remove(p.getName());
                    }
                    else
                    {
                        plugin.toggled.add(p.getName());
                    }
                    if(args.length == 2)
                    {
                        sender.sendMessage(GREEN+"Flamethrower has been "+(plugin.toggled.contains(args[1]) ? "enabled": "disabled")+" for "+args[1]+"!");
                    }
                    p.sendMessage(GREEN+"Flamethrower has been "+(plugin.toggled.contains(p.getName()) ? "enabled": "disabled")+"!");
                    return true;
                }
                else if(args[0].equalsIgnoreCase("reload"))
                {
                    plugin.reloadConfig();
                    sender.sendMessage(GREEN+"The config has been reloaded");
                    return true;
                }
            }
            else
            {
                sender.sendMessage(RED+"Insufficient arguments");
                return true;
            }
        }
        return false;
    }
}
