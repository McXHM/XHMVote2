package cn.mrxhm.xHMVote2;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;

public class Freeze implements Listener, CommandExecutor {
    public static List<String> playerList = new ArrayList<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (playerList.contains(event.getPlayer().getName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (playerList.contains(event.getPlayer().getName())) {
            event.setCancelled(true);
        }
    }

    public static void freeze(String playerName) {
        playerList.add(playerName);
    }

    public static void freeze(Player player) {
        freeze(player.getName());
    }

    public static void unFreeze(String playerName) {
        playerList.remove(playerName);
    }

    public static void unFreeze(Player player) {
        unFreeze(player.getName());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender.hasPermission("xhmvote.freeze")) {
            if (args.length == 0) {
                if (sender instanceof Player p) {
                    f(p);
                }
            } else if (args.length == 1) {
                Player p = Bukkit.getPlayer(args[0]);
                if (p != null) {
                    f(p);
                }
            }
        }
        return false;
    }

    void f(Player p) {
        if (Freeze.playerList.contains(p.getName())) {
            p.sendMessage("§e你已被解冻！");
            Freeze.unFreeze(p);
        } else {
            p.sendMessage("§b你已被冻结！");
            Freeze.freeze(p);
        }
    }
}
