package de.mayhan.challenge.timer;

import com.google.common.collect.Maps;
import de.mayhan.challenge.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimerCommand implements TabExecutor {

    public static HashMap<Integer, Timer> timers = Maps.newHashMap();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "Du bist kein Spieler!");
            return false;
        }


        Player player = (Player) sender;
        if (args.length == 1 && args[0].equalsIgnoreCase("start")){

            Timer timer = new Timer();
            if (Timer.isRunning){
                player.sendMessage(ChatColor.RED + "Timer läuft bereits!");
                return true;
            }


            List<Player> players = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()){
                players.add(p);
                player.setHealth(20.0);
                player.setFoodLevel(20);
            }

            timer.start(players);
            Timer.isRunning = true;
            timers.put(1, timer);

            for (Player p : Bukkit.getOnlinePlayers()){
                Location l = p.getLocation();
                p.playSound(p, Sound.ENTITY_ENDER_DRAGON_GROWL, 5, 0);
            }

            World w = player.getWorld();
            w.setTime(0);

            Bukkit.broadcastMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "TIMER GESTARTED!");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("stop")){
            Timer timer = timers.get(1);
            timers.remove(1);
            timer.stop();
            Timer.isRunning = false;
            Bukkit.broadcastMessage(ChatColor.RED + ChatColor.BOLD.toString() + "TIMER GESTOPPT!");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("pause")){
            Timer timer = timers.get(1);
            timer.pause();
            Timer.isRunning = false;
            Bukkit.broadcastMessage(ChatColor.GOLD + "Timer pausiert!");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("resume")){
            Timer timer = timers.get(1);

            timer.resume();
            Timer.isRunning = true;
            Bukkit.broadcastMessage(ChatColor.GREEN + "Timer fortgesetzt!");
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> strings = new ArrayList<>();

        strings.add("start");
        strings.add("stop");
        strings.add("pause");
        strings.add("resume");

        return strings;
    }
}
